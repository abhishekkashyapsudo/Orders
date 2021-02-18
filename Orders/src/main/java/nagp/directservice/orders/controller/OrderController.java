package nagp.directservice.orders.controller;

import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import nagp.directservice.orders.models.Order;
import nagp.directservice.orders.service.IOrderService;
import nagp.directservice.orders.exceptions.OrderNotFoundException;


@RestController
@EnableCircuitBreaker
@RequestMapping("orders")
public class OrderController {

	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(OrderController.class);

	@Resource(name = "restTemplate")
	private RestTemplate restTemplate;

	@Value("${server.port}")
	private int port;

	@Resource
	private IOrderService orderService;


	@GetMapping(value = "/{orderId}")
	Order getOrder(@PathVariable(name = "orderId") String requestId) throws OrderNotFoundException {
		logger.info("Working from port " + port + " of Requests service");
		Optional<Order>  order= orderService.getOrder(requestId);
		if(order.isPresent()) {
			return order.get();
		}
		throw new OrderNotFoundException(requestId);
	}

	@GetMapping()
	List<Order> getAllOrders() {
		logger.info("Working from port " + port + " of Requests service");
		return orderService.getAllOrders();
	}

	@GetMapping(value = "allConsumers")
	List<Order> getConsumerOrders(@RequestParam String  consumerId) {
		logger.info("Working from port " + port + " of Requests service");
		return orderService.getAllConsumerOrders(consumerId);
	}

	@GetMapping(value = "consumer")
	String getConsumerOrderCount(@RequestParam String  consumerId) {
		logger.info("Working from port " + port + " of Requests service");
		return orderService.getAllConsumerOrdersCount(consumerId);
	}

	@GetMapping(value = "allSellers")
	List<Order> getSellerOrders(@RequestParam String sellerId) {
		logger.info("Working from port " + port + " of Requests service");
		return orderService.getAllSellerOrders(sellerId);
	}

	@GetMapping(value = "seller")
	String getSellerOrderCount(@RequestParam String sellerId) {
		logger.info("Working from port " + port + " of Requests service");
		return orderService.getAllSellerOrdersCount(sellerId);
	}


	@GetMapping(value = "/complete/{orderId}")
	Order completeOrder(@PathVariable(name = "orderId") String requestId) throws OrderNotFoundException {
		logger.info("Working from port " + port + " of Requests service");
		Optional<Order>  order= orderService.completeOrder(requestId);
		if(order.isPresent()) {
			return order.get();
		}
		throw new OrderNotFoundException(requestId);
	}
	
	@GetMapping(value = "/cancelOrder/{orderId}")
	String cancelOrder(@PathVariable(name = "orderId") String requestId) throws OrderNotFoundException {
		logger.info("Working from port " + port + " of Requests service");
		return orderService.cancelOrder(requestId);
		
	}

	@PostMapping
	Order addRequest(@RequestParam String sellerId, @RequestParam String address, 
			@RequestParam double amount,@RequestParam String description, @RequestParam String service,
			@RequestParam String consumerId, @RequestParam String requestId) {
		logger.info("Working from port " + port + " of Consumer service");

		return orderService.addOrder(requestId, sellerId, consumerId, address, amount, description, service);
	}
}
