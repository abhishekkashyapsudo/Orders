package nagp.directservice.orders.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import nagp.directservice.orders.dao.IOrderDao;
import nagp.directservice.orders.models.Order;


@Repository
public class OrderDao implements IOrderDao{

	private static Random random;

	private static List<Order> orders = new ArrayList<>();

	@Override
	public Optional<Order> getOrder(String orderId) {
		return orders.stream()
				.filter(o -> orderId.trim().equalsIgnoreCase(o.getOrderId())).findFirst();
	}

	@Override
	public List<Order> getAllOrders() {
		return orders;
	}

	@Override
	public List<Order> getAllConsumerOrders(String consumerId) {
		return orders.stream().filter(o -> o.getConsumerId().equalsIgnoreCase(consumerId))
				.collect(Collectors.<Order>toList());
	}

	@Override
	public List<Order> getAllSellerOrders(String sellerId) {
		return orders.stream().filter(o -> o.getSellerId().equalsIgnoreCase(sellerId))
				.collect(Collectors.<Order>toList());	}

	@Override
	public void addOrder(Order order) {
		orders.add(order);
	}

	static {
		random = new Random();
		for (int i = 0; i<10; i++) {
			orders.add(randomOrder(i));
		}
	}

	private static Order randomOrder(int i) {
		return new Order("S1000000"+i,"New Address "+i , random.nextInt(10000)+5, "Random description "+i, "MAKE_UP", "C1000000"+i, "SR1000000"+i);
	}

	@Override
	public void deleteOrder(String orderId) {
		orders.removeIf(o -> orderId.trim().equalsIgnoreCase(o.getOrderId()));
		
	}


}
