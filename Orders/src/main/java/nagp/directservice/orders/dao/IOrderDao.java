package nagp.directservice.orders.dao;

import java.util.List;
import java.util.Optional;

import nagp.directservice.orders.models.Order;

public interface IOrderDao {

	Optional<Order> getOrder(String orderId);

	List<Order> getAllOrders();

	List<Order> getAllConsumerOrders(String consumerId);
	List<Order> getAllSellerOrders(String sellerId);

	void addOrder(Order order);

}
