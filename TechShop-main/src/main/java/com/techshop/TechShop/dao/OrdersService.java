package com.techshop.TechShop.dao;



public interface OrdersService {

	void cancelOrder(int orderId);

	void updateOrderStatus(int orderId, String newStatus);

	void getOrderDetails(int orderId);

	double calculateTotalAmount(int orderId);
	
	public void totalamount(int productid, int quantity);
	
	public void payment(int orderid, int customerid, int productid, int quantity , int amount);
	


}
