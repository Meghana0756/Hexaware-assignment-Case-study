package com.techshop.TechShop.dao.Impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.techshop.TechShop.dao.OrderDetailsService;
import com.techshop.TechShop.util.DBConnUtil;
import com.techshop.TechShop.util.DBPropertyUtil;
import com.techshop.TechShop.entity.OrderDetails;
import com.techshop.TechShop.entity.Orders;
import com.techshop.TechShop.entity.Products;
import com.techshop.TechShop.exception.DatabaseException;

public class OrderDetailsServiceImpl implements OrderDetailsService{
private Connection connection;
	
	public OrderDetailsServiceImpl() {
        this.connection = DBConnUtil.getConnection(DBPropertyUtil.getConnectionString());	
    }
	
	@Override
	 public double calculateSubtotal(int orderDetailId) {
	        double subtotal = 0.0;
	        try (PreparedStatement preparedStatement = connection.prepareStatement(
	        		"SELECT p.Price, od.Quantity, od.Discount " +
	        	            "FROM OrderDetails od " +
	        	            "JOIN Products p ON od.ProductID = p.ProductID " +
	        	            "WHERE od.OrderDetailID = ?"  )) {
	            preparedStatement.setInt(1, orderDetailId);

	            try (ResultSet resultSet = preparedStatement.executeQuery()) {
	                if (resultSet.next()) {
	                    double productPrice = resultSet.getDouble("Price");
	                    int quantity = resultSet.getInt("Quantity");
	                    double discount = resultSet.getDouble("Discount");

	                    subtotal = (productPrice * quantity) - discount;
	                }
	            }
	        } catch (SQLException e) {
	        	throw new DatabaseException("Error calculating subtotal.");
	        }
	        return subtotal;
	    }
	 
	    
	@Override
	 public void getOrderDetailInfo(int orderDetailId) {
	        try (PreparedStatement preparedStatement = connection.prepareStatement(
	                "SELECT * FROM OrderDetails WHERE OrderDetailID = ?")) {
	            preparedStatement.setInt(1, orderDetailId);

	            try (ResultSet resultSet = preparedStatement.executeQuery()) {
	                if (resultSet.next()) {
	                    int productId = resultSet.getInt("ProductID");
	                    int quantity = resultSet.getInt("Quantity");
	                    double discount = resultSet.getDouble("Discount");

	                    System.out.println("Order Detail Information for OrderDetailID: " + orderDetailId);
	                    System.out.println("Product ID: " + productId);
	                    System.out.println("Quantity: " + quantity);
	                    System.out.println("Discount: $" + discount);
	                    System.out.println("Subtotal: $" + calculateSubtotal(orderDetailId));
	                }
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	
	@Override
	 public void updateQuantity(int orderDetailId, int newQuantity) {
	        try (PreparedStatement preparedStatement = connection.prepareStatement(
	                "UPDATE OrderDetails SET Quantity = ? WHERE OrderDetailID = ?")) {
	            preparedStatement.setInt(1, newQuantity);
	            preparedStatement.setInt(2, orderDetailId);

	            int rowsAffected = preparedStatement.executeUpdate();
	            if (rowsAffected > 0) {
	                System.out.println("Quantity updated successfully for OrderDetailID: " + orderDetailId);
	            } else {
	                System.out.println("Failed to update quantity. OrderDetail not found.");
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	 
	@Override
	 public void addDiscount(int orderDetailId, double discountAmount) {
	        try (PreparedStatement preparedStatement = connection.prepareStatement(
	                "UPDATE OrderDetails SET Discount = ? WHERE OrderDetailID = ?")) {
	            preparedStatement.setDouble(1, discountAmount);
	            preparedStatement.setInt(2, orderDetailId);

	            int rowsAffected = preparedStatement.executeUpdate();
	            if (rowsAffected > 0) {
	                System.out.println("Discount added successfully for OrderDetailID: " + orderDetailId);
	            } else {
	                System.out.println("Failed to add discount. OrderDetail not found.");
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	
	 public void insertOrderDetails(OrderDetails orderDetails) {
	        try {
	            String sql = "INSERT INTO orderdetails (OrderDetailID,OrderID, ProductID, Quantity) VALUES (?,?, ?, ?)";
	            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
	            	preparedStatement.setInt(1, orderDetails.getOrderDetailID());
	                preparedStatement.setInt(2, orderDetails.getOrderID());
	                preparedStatement.setInt(3,orderDetails.getProductID() );
	                preparedStatement.setInt(4, orderDetails.getQuantity());
	                int rowsAffected = preparedStatement.executeUpdate();
	                if (rowsAffected > 0) {
	                    System.out.println("Order details inserted successfully.");
	                } else {
	                    System.out.println("Failed to insert order details.");
	                }
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
}
