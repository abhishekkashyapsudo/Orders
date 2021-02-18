package nagp.directservice.orders.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import nagp.directservice.orders.dao.IOrderDao;
import nagp.directservice.orders.models.Order;
import nagp.directservice.orders.service.IOrderService;

@Service
public class OrderService implements IOrderService {

	@Resource
	IOrderDao orderDao;


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



}
