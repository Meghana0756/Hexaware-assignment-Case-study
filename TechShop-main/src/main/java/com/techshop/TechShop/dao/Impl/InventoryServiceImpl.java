package com.techshop.TechShop.dao.Impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.techshop.TechShop.dao.InventoryService;
import com.techshop.TechShop.util.DBConnUtil;
import com.techshop.TechShop.util.DBPropertyUtil;
import com.techshop.TechShop.entity.Inventory;
import com.techshop.TechShop.entity.Orders;
import com.techshop.TechShop.entity.Products;
import com.techshop.TechShop.exception.DatabaseException;
import com.techshop.TechShop.exception.InsufficientStockException;

public class InventoryServiceImpl implements InventoryService{
	
private Connection connection;
	
	public InventoryServiceImpl() {
        this.connection = DBConnUtil.getConnection(DBPropertyUtil.getConnectionString());
	}
	
	@Override
	public void getProduct(int productId) {
		try (PreparedStatement preparedStatement = connection.prepareStatement(
				"SELECT * FROM products WHERE ProductID = ?")) {
	        preparedStatement.setInt(1, productId);

	        try (ResultSet resultSet = preparedStatement.executeQuery()) {
	            if (resultSet.next()) {
	                System.out.println("Product ID: " + resultSet.getInt("ProductID"));
	                System.out.println("Product Name: " + resultSet.getString("ProductName"));
	                System.out.println("Description: " + resultSet.getString("Description"));
	                System.out.println("Price: " + resultSet.getDouble("Price"));
	               
	            } else {
	                System.out.println("Product not found with ID: " + productId);
	            }
	        } 
		}
		catch (SQLException e) {
	        e.printStackTrace();
	        }
	}
	
	@Override
	public int getQuantityInStock(int productId) {
		try (PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT QuantityInStock FROM inventory WHERE ProductID = ?")) {

            preparedStatement.setInt(1, productId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int stockQuantity = resultSet.getInt("QuantityInStock");
                    return stockQuantity;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
		return -1; // For example, -1 indicates an error or not found
	
	}
	
	@Override
	public void addToInventory(int productId, int quantity) {
		try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO inventory (productId, QuantityInStock) VALUES (?, ?) ON DUPLICATE KEY UPDATE QuantityInStock = QuantityInStock + ?")) {
            preparedStatement.setInt(1, productId);
            preparedStatement.setInt(2, quantity);
            preparedStatement.setInt(3, quantity);
            	int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Quantity added successfully.");
                } else {
                	throw new DatabaseException("Failed to add quantity to inventory.");
                }
        
		}
        catch (SQLException e) {
        	throw new DatabaseException("Error adding quantity to inventory.");
        }
	}
	@Override
	public void removeFromInventory(int productId, int quantity) {
		try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE inventory SET QuantityInStock = QuantityInStock - ? WHERE productId = ?")) {
			int currentStock = getQuantityInStock(productId);
			if(currentStock < quantity) {
				throw new InsufficientStockException("Insufficient stock for product ID: " +productId);
			}
			preparedStatement.setInt(1, quantity);
            preparedStatement.setInt(2, productId);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) {
            	System.out.println("Quantity is removed from inventory unsuccessfully");
            } else {
            	System.out.println("Quantity is removed from inventory successfully");
            }
		}
		catch (SQLException e) {
			throw new DatabaseException("Error removing quantity to inventory.");
		}
	}
	
	@Override
	public void updateStockQuantity(int productId, int newQuantity) {
		try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE inventory SET QuantityInStock = ? WHERE productId = ?")) {
			preparedStatement.setInt(1,  newQuantity);
			preparedStatement.setInt(2,  productId);
			int rowsAffected = preparedStatement.executeUpdate();
			if (rowsAffected == 0) {
				System.out.println("Product not found in inventory");
			} else {
				System.out.println("Product found in inventory");
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean isProductAvailable(int productId, int quantityToCheck) {
		try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT QuantityInStock FROM inventory WHERE productId = ?")) {
			preparedStatement.setInt(1, productId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int stockQuantity = resultSet.getInt("QuantityInStock");
                    boolean isAvailable = stockQuantity > 0;
                    System.out.println("Product availablility chack for " + productId + ": " + isAvailable);
                    return isAvailable;
                } else {
                	System.out.println("Product not found in inventory: " + productId);
                	
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
		return true;
	}
	
	@Override
	public List<Products> listLowStockProducts() {
		int threshold = 10;
		List<Products> lowStockProducts = new ArrayList<>();
		try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM products WHERE productId IN (SELECT productId FROM inventory WHERE QuantityInStock < ?)")) {
					preparedStatement.setInt(1, threshold);
					try (ResultSet resultSet = preparedStatement.executeQuery()) {
		                while (resultSet.next()) {
		                    Products product = extractProductFromResultSet(resultSet);
		                    lowStockProducts.add(product);
		                }
		            
					}
		System.out.println("Low Stock Products:");
		for (Products product : lowStockProducts) {
			System.out.println(product);
		}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return lowStockProducts;
	}
	
	@Override
	public List<Products> listOutOfStockProducts() {
	    List<Products> outOfStockProducts = new ArrayList<>();
	    try (PreparedStatement preparedStatement = connection.prepareStatement(
	    		"SELECT * FROM products LEFT JOIN inventory ON products.productId = inventory.productId WHERE inventory.productId IS NULL OR inventory.QuantityInStock = 0")) {
	    	try(ResultSet resultSet = preparedStatement.executeQuery()) {
	    		while (resultSet.next()) {
	    			 Products product = extractProductFromResultSet(resultSet);
	                 outOfStockProducts.add(product);
	                 System.out.println("Out of Stock Products: " + product);
	    		}
	        }
	    } catch (SQLException e) {
	    	e.printStackTrace();
	    }
		return outOfStockProducts;
	}
	
	@Override
	public List<Products> listAllProducts() {
	    List<Products> allProducts = new ArrayList<>();
	    try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM products")) {
	    	try(ResultSet resultSet = preparedStatement.executeQuery()) {
	    		while (resultSet.next()) {
	                Products product = extractProductFromResultSet(resultSet);
	                allProducts.add(product);
	                System.out.println("All Products: " + product);
	                }
	    		}
	    } catch (SQLException e) {
	    	e.printStackTrace();
	    }
		return allProducts;
	}
	    
	@Override 
	public double getInventoryValue() {
		double totalValue = 0.0;
		try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT p.ProductID, p.ProductName, p.Price, i.QuantityInStock FROM products p JOIN inventory i ON p.productId = i.productId")) {
			try(ResultSet resultSet = preparedStatement.executeQuery()) {
	            System.out.println("Product Inventory Details:");

	            while (resultSet.next()) {
	                int productId = resultSet.getInt("ProductID");
	                String productName = resultSet.getString("ProductName");
	                double productPrice = resultSet.getDouble("Price");
	                int quantityInStock = resultSet.getInt("QuantityInSTock");

	                double productValue = productPrice * quantityInStock;

	                System.out.println("Product ID: " + productId);
	                System.out.println("Product Name: " + productName);
	                System.out.println("Product Price: $" + productPrice);
	                System.out.println("Quantity in Stock: " + quantityInStock);
	                System.out.println("Product Value: $" + productValue);
	                System.out.println();

	                totalValue += productValue;
	            }
	        }
	    }
			
		catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("Total Inventory Value: $" + totalValue);
		return totalValue;
	}
	    
	private Products extractProductFromResultSet(ResultSet resultSet) throws SQLException {
		Products product = new Products();
	    product.setProductID(resultSet.getInt("ProductID"));
	    product.setProductName(resultSet.getString("ProductName"));
	    product.setDescription(resultSet.getString("Description"));
	    product.setPrice(resultSet.getDouble("Price"));
	    return product;
	}
	
	
	public void processOrder(int productId, int orderQuantity) {
        if (isProductAvailable(productId, orderQuantity)) {
            System.out.println("Inventory Updated Successfully..............");
            removeFromInventory(productId, orderQuantity);
        } else {
        	throw new InsufficientStockException("Insufficient stock for product ID: " +productId);
            
        }
    }


	
        
}