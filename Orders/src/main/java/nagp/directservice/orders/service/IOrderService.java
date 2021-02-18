package nagp.directservice.orders.service;

import java.util.List;
import java.util.Optional;

import nagp.directservice.orders.exceptions.OrderNotFoundException;
import nagp.directservice.orders.models.Order;

public interface IOrderService {

	Optional<Order> getOrder(String requestId);

	List<Order> getAllOrders();

	List<Order> getAllConsumerOrders(String consumerId);

	String getAllConsumerOrdersCount(String consumerId);

	List<Order> getAllSellerOrders(String sellerId);

	String getAllSellerOrdersCount(String sellerId);

	Optional<Order> completeOrder(String requestId);

	Order addOrder(String requestId, String sellerId, String consumerId, String address, double amount,
			String description, String service);

	String cancelOrder(String requestId) throws OrderNotFoundException;

}
