package nagp.directservice.orders.models;

import java.util.Date;


public class Order {
	private String sellerId;
	private String address;
	private double amount;
	private String description;
	private String service;
	private String consumerId;
	private String orderId;
	private Date orderDate;
	private Date completionDate;
	public Order(String sellerId, String address, double amount, String description, String service,
			String consumerId, String requestId) {
		super();
		this.sellerId = sellerId;
		this.address = address;
		this.amount = amount;
		this.description = description;
		this.service = service;
		this.consumerId = consumerId;
		this.orderId = requestId;
		this.orderDate = new Date();
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public Date getCompletionDate() {
		return completionDate;
	}
	public void setCompletionDate(Date completionDate) {
		this.completionDate = completionDate;
	}
	public String getSellerId() {
		return sellerId;
	}
	public String getDescription() {
		return description;
	}
	public String getService() {
		return service;
	}
	public String getConsumerId() {
		return consumerId;
	}
	public String getOrderId() {
		return orderId;
	}
	public Date getOrderDate() {
		return orderDate;
	}
	
	
	
	
}
