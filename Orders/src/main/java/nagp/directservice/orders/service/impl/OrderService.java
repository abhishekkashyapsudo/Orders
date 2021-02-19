package nagp.directservice.orders.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import nagp.directservice.orders.dao.IOrderDao;
import nagp.directservice.orders.exceptions.OrderNotFoundException;
import nagp.directservice.orders.models.Order;
import nagp.directservice.orders.service.IOrderService;


@Service
public class OrderService implements IOrderService {

	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(OrderService.class);

	@Resource(name = "restTemplate")
	private RestTemplate restTemplate;
	
	@Resource
	IOrderDao orderDao;

	@Autowired
	LoadBalancerClient loadBalancerClient;

	@Override
	public Optional<Order> getOrder(String orderId) {
		return orderDao.getOrder(orderId);
	}

	@Override
	public List<Order> getAllOrders() {
		return orderDao.getAllOrders();
	}

	@Override
	public List<Order> getAllConsumerOrders(String consumerId) {
		return orderDao.getAllConsumerOrders(consumerId);
	}

	@Override
	public String getAllConsumerOrdersCount(String consumerId) {
		List<Order> orders = orderDao.getAllConsumerOrders(consumerId);;
		double sum = orders.stream().mapToDouble(Order::getAmount).sum();
		return "Consumer has "+orders.size() +" orders of amount " +sum+".";
	}

	@Override
	public List<Order> getAllSellerOrders(String sellerId) {
		return orderDao.getAllSellerOrders(sellerId);

	}

	@Override
	public String getAllSellerOrdersCount(String sellerId) {
		List<Order> orders = orderDao.getAllSellerOrders(sellerId);;
		double sum = orders.stream().mapToDouble(Order::getAmount).sum();
		return "Seller has "+orders.size() +" orders of amount " +sum+".";
	}

	@Override
	public Optional<Order> completeOrder(String requestId) {
		Optional<Order> order = getOrder(requestId);
		if(order.isPresent()) {
			order.get().setCompletionDate(new Date());
		}
		return order;
	}
	
	@Override
	public Order addOrder(String requestId, String sellerId, String consumerId, String address, double amount,
			String description, String service) {
		Order order = new Order(sellerId, address, amount, description, service, consumerId, requestId);
		orderDao.addOrder(order);
		return order;
	}

	@Override
	public String cancelOrder(String requestId) throws OrderNotFoundException {
		Optional<Order> order = getOrder(requestId);
		if(order.isPresent()) {
			return cancelOrder(order.get());
		}
		else {
			throw new OrderNotFoundException(requestId);
		}
	}
	
	@HystrixCommand(fallbackMethod = "cancelOrderFallback")
	private String cancelOrder(Order order) {
		String baseUrl = loadBalancerClient.choose("requests").getUri().toString() + "/requests/cancelOrder";
		try {
			UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl)
					.queryParam("sellerId", order.getSellerId())
					.queryParam("address", order.getAddress())
					.queryParam("amount", order.getAmount())
					.queryParam("description", order.getDescription())
					.queryParam("service", order.getService())
					.queryParam("consumerId", order.getConsumerId())
					.queryParam("requestId", order.getOrderId());

			restTemplate.exchange(builder.buildAndExpand().toUri(), HttpMethod.POST, null,
					String.class);
		} catch (Exception ex) {
			logger.warn(ex.getMessage(), ex);
		}
		orderDao.deleteOrder(order.getOrderId());
		return "Order successfully deleted and added to the requests database.";
	}
	
	public String cancelOrderFallback( Order order) {  
		logger.warn("Requests Service is down!!! fallback route enabled...");
			
		return  "CIRCUIT BREAKER ENABLED!!! No Response From Requests Service at this moment. " +
		" Service will be back shortly - ";

	}



}
