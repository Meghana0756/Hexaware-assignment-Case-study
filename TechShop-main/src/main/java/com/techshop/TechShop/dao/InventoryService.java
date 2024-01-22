package com.techshop.TechShop.dao;

import java.util.List;

import com.techshop.TechShop.entity.Inventory;
import com.techshop.TechShop.entity.Products;
public interface InventoryService {

	void updateStockQuantity(int productId, int newQuantity);

	void removeFromInventory(int productId, int quantity);

	void addToInventory(int productId, int quantity);
	
	int getQuantityInStock(int productId);

	void getProduct(int productId);

	boolean isProductAvailable(int productId, int quantityToCheck);

	

	List<Products> listOutOfStockProducts();

	List<Products> listAllProducts();

	double getInventoryValue();

	List<Products> listLowStockProducts();
	
	public void processOrder(int productId, int orderQuantity);

}
