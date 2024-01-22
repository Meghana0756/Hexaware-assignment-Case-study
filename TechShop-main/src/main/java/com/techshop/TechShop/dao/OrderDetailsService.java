package com.techshop.TechShop.dao;

import com.techshop.TechShop.entity.OrderDetails;

public interface OrderDetailsService {

	double calculateSubtotal(int orderDetailId);

	void getOrderDetailInfo(int orderDetailId);

	void updateQuantity(int orderDetailId, int newQuantity);

	void addDiscount(int orderDetailId, double discountAmount);
	
	public void insertOrderDetails(OrderDetails orderDetails);

}
