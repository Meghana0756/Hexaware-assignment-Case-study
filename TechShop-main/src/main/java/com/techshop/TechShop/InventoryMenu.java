package com.techshop.TechShop;

import java.util.List;
import java.util.Scanner;

import com.techshop.TechShop.dao.InventoryService;
import com.techshop.TechShop.entity.Products;
import com.techshop.TechShop.dao.Impl.InventoryServiceImpl;
import com.techshop.TechShop.dao.Impl.ProductServiceImpl;


public class InventoryMenu {
	static InventoryService inventoryService = new InventoryServiceImpl();
	
	public boolean InventoryDetailsMenu() {
		Scanner scanner = new Scanner(System.in);
		Products product = null;
		while (true) {
			System.out.println("\n********** Inventory Menu **********");
			System.out.println("1. getProduct");
			System.out.println("2. getQuantityInStock");
			System.out.println("3. addToInventory");
			System.out.println("4. removeFromInventory");
			System.out.println("5. updateStockQuantity");
			System.out.println("6. isProductAvailable");
			System.out.println("7. getInventoryValue");
			System.out.println("8. listLowStockProducts");
			System.out.println("9. listOutOfStockProducts");
			System.out.println("10. listAllProducts");
			System.out.println("11. Exit");
			System.out.print("Enter your choice: ");
		int customerchoice = scanner.nextInt();
		switch(customerchoice) {
		case 1:
			System.out.println("\n*********** Inventory Menu ************");
			System.out.print("Enter the product Id: ");
			int productId = scanner.nextInt();
			inventoryService.getProduct(productId);
			System.out.println("Product found");
			
			break;
		case 2:
			System.out.println("\n*********** Inventory Menu ************");
			System.out.print("Enter the product Id: ");
			int productId1 = scanner.nextInt();
			int quantityInStock = inventoryService.getQuantityInStock(productId1);
			System.out.println("the quantity in stock: " + quantityInStock);
			break;
		case 3:
			System.out.println("\n*********** Inventory Menu ************");
			System.out.print("Enter the product Id: ");
			int productId2 = scanner.nextInt();
			scanner.nextLine();
			System.out.println("Enter the quantity to add: ");
			int quantity = scanner.nextInt();
			inventoryService.addToInventory(productId2, quantity);
			break;
		case 4:
			System.out.println("\n*********** Inventory Menu ************");
			System.out.print("Enter the product Id: ");
			int productId3 = scanner.nextInt();
			scanner.nextLine();
			System.out.println("Enter the quantity to remove: ");
			int quantity1 = scanner.nextInt();
			inventoryService.removeFromInventory(productId3, quantity1);
			
			break;
		case 5:
			System.out.println("\n*********** Inventory Menu ************");
			System.out.print("Enter the product Id: ");
			int productId4 = scanner.nextInt();
			scanner.nextLine();
			System.out.println("Enter new quantity stock: ");
			int quantity2 = scanner.nextInt();
			inventoryService.updateStockQuantity(productId4, quantity2);
			System.out.println("Stock of the quantity updated successfully");
			break;
		case 6:
			System.out.println("\n*********** Inventory Menu ************");
			System.out.print("Enter the product Id: ");
			int productId5 = scanner.nextInt();
			scanner.nextLine();
			System.out.println("Enter the quantity to check: ");
			int quantity3 = scanner.nextInt();
			boolean isAvailable = inventoryService.isProductAvailable(productId5, quantity3);
			if(isAvailable) {
				System.out.println("Product is available in sufficient quantity");
			} else {
				System.out.println("Product is not available in sufficient quantity");
			}
			break;
		case 7:
			System.out.println("\n*********** Inventory Menu ************");
			double productValue = inventoryService.getInventoryValue();
			System.out.println("Total Inventory Value: $" + productValue);
			break;
		case 8:
			System.out.println("\n*********** Inventory Menu ************");
			System.out.println("Enter low stock threshold: ");
			int threshold = scanner.nextInt();
			List<Products> lowStockProducts = inventoryService.listLowStockProducts();
			System.out.println("Low stock products: ");
			for(Products product2 : lowStockProducts) {
				System.out.println(product);
			}
			break;
		case 9:
			System.out.println("\n*********** Inventory Menu ************");
			List<Products> outOfStockProducts = inventoryService.listOutOfStockProducts();
			System.out.println("Out of stock products: ");
			for(Products product2 : outOfStockProducts) {
				System.out.println(product);
			}
			break;
		case 10:
			System.out.println("\n*********** Inventory Menu ************");
			List<Products> allProducts = inventoryService.listAllProducts();
			System.out.println("All products: ");
			for(Products product3 : allProducts) {
				System.out.println(product);
			}
			break;
		case 11:
			System.out.println("Returning to Main Menu.");
            return false;
        default:
            System.out.println("Invalid choice. Please enter a number between 1 and 5.");
            
            
		}
		}
	}
	}




		