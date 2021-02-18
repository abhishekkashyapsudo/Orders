package nagp.directservice.orders.exceptions;

public class OrderNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6170196487063827224L;
	private static final String ERROR_MESSAGE = "No Order exists with the passed order id";
	public OrderNotFoundException(String msg) {
		super(ERROR_MESSAGE +":" + msg);
	}
	
	public OrderNotFoundException() {
		super(ERROR_MESSAGE);
	}
}
