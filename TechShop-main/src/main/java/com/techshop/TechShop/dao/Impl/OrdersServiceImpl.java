package com.techshop.TechShop.dao.Impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.techshop.TechShop.dao.InventoryService;
import com.techshop.TechShop.dao.OrderDetailsService;
import com.techshop.TechShop.dao.OrdersService;
import com.techshop.TechShop.dao.ProductService;
import com.techshop.TechShop.entity.OrderDetails;
import com.techshop.TechShop.entity.Orders;
import com.techshop.TechShop.exception.DatabaseException;
import com.techshop.TechShop.exception.IncompleteOrderException;
import com.techshop.TechShop.exception.PaymentFailedException;
import com.techshop.TechShop.util.DBConnUtil;
import com.techshop.TechShop.util.DBPropertyUtil;

public class OrdersServiceImpl implements OrdersService{
	
	static InventoryService inventoryService = new InventoryServiceImpl();
	static ProductService productService = new ProductServiceImpl();
	static OrderDetailsService  orderDetailsService = new OrderDetailsServiceImpl();
	
private Connection connection;
	
	public OrdersServiceImpl() {
        this.connection = DBConnUtil.getConnection(DBPropertyUtil.getConnectionString());	
    }
	
	
	@Override
	public double calculateTotalAmount(int orderId) {
        double totalAmount = 0.0;
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT TotalAmount FROM orders WHERE OrderID = ?")) {
            preparedStatement.setInt(1, orderId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    totalAmount = resultSet.getDouble("TotalAmount");
                } else {
                    System.out.println("Order not found with ID: " + orderId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalAmount;
    }
	
	
	@Override
	public void getOrderDetails(int orderId) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT * FROM orders WHERE OrderID = ?")) {
            preparedStatement.setInt(1, orderId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
            	if (resultSet.next()) {
                    System.out.println("Order Details for Order ID: " + orderId);
                    System.out.println("Customer ID: " + resultSet.getInt("CustomerID"));
                    System.out.println("Order Date: " + resultSet.getDate("OrderDate"));
                    System.out.println("Total Amount: $" + resultSet.getDouble("TotalAmount"));
                    System.out.println("Order Status: " + resultSet.getString("Status"));
                } else {
                    System.out.println("Order not found with ID: " + orderId);
                }
            }  
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
	
	
	@Override
	public void updateOrderStatus(int orderId, String newStatus) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "UPDATE orders SET Status = ? WHERE OrderID = ?")) {
            preparedStatement.setString(1, newStatus);
            preparedStatement.setInt(2, orderId);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Order status updated successfully.");
            } else {
                System.out.println("Failed to update order status. Order not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
	
	 @Override
	    public void cancelOrder(int orderId) {
	        try (PreparedStatement preparedStatement = connection.prepareStatement(
	                "DELETE FROM orders WHERE OrderID = ?")) {
	            preparedStatement.setInt(1, orderId);

	            int rowsAffected = preparedStatement.executeUpdate();
	            if (rowsAffected > 0) {
	                System.out.println("Order canceled successfully.");
	            } else {
	                System.out.println("Failed to cancel order. Order not found.");
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	 
	 public void totalamount(int productid, int quantity) {
		 int price = productService.getprice(productid)*quantity;
		 System.out.println("Your order amount is "+price);
		 System.out.println("If You Want to place the order go to payment Option and Pay the Total amount" );
		 
	 }
	 
	 public void payment(int orderid, int customerid, int productid, int quantity , int amount) {
		 
		 int price = productService.getprice(productid)*quantity;
		 
		 if(price == amount) {
			 placeOrder(orderid, customerid, productid, quantity);
			 OrderDetails orderDetails = new OrderDetails();
			 orderDetails.setOrderDetailID(orderid);
			 orderDetails.setOrderID(orderid);
			 orderDetails.setProductID(productid);
			 orderDetails.setQuantity(quantity);
			 orderDetailsService.insertOrderDetails(orderDetails);
			 
		 }else {
			 throw new PaymentFailedException(String.format("Enter Valid details amount for Payment %d", amount ));
		 } 
		 
	 }
	 public void placeOrder(int orderid, int customerid, int productid, int quantity) {
		  if (productid != 0 && quantity !=0) {
	            throw new IncompleteOrderException("Plese Provide Valid Oeder Details");
	        }
		  int price = productService.getprice(productid)*quantity;
	        try (PreparedStatement preparedStatement = connection.prepareStatement(
	                "INSERT INTO orders (OrderID ,CustomerID, OrderDate, TotalAmount, ) VALUES (?, ?, ?, ?)")) {
	        	     preparedStatement.setInt(1,orderid);
	        	     preparedStatement.setInt(2,customerid);
	        	     preparedStatement.setDate(3, new  Date(System.currentTimeMillis()));
	        	     preparedStatement.setDouble(4, price);
	            int rowsAffected = preparedStatement.executeUpdate();
	            if (rowsAffected > 0) {
	            	inventoryService.processOrder(productid, quantity);
	                System.out.println("Order placed successfully.");
	            } else {
	                System.out.println("Failed to place order.");
	            }
	        } catch (SQLException e) {
	        	throw new DatabaseException("Error placing order.");
	        }
	    }
	 
	 // this methods calls internal methods to update the inventory and place the order 



	 

}
