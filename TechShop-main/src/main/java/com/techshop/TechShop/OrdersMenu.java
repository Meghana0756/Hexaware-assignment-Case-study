package com.techshop.TechShop;

import java.util.Scanner;

import com.techshop.TechShop.dao.OrdersService;
import com.techshop.TechShop.entity.Customers;
import com.techshop.TechShop.dao.Impl.OrdersServiceImpl;

public class OrdersMenu {
	static OrdersService ordersService = new OrdersServiceImpl();
	public boolean OrdersDetailsMenu() {
		Scanner scanner = new Scanner(System.in);
		while(true) {
			System.out.println("\n********** Orders Menu **********");
            System.out.println("1. calculateTotalAmount");
            System.out.println("2. getOrderDetails");
            System.out.println("3. updateOrderStatus");
            System.out.println("4. cancelOrder");
            System.out.println("5. totalamount");
            System.out.println("6. payment");
            System.out.println("7. Exit");
            System.out.print("Enetr your choice: ");
            int customerchoice = scanner.nextInt();
            switch(customerchoice) {
            case 1:
            	System.out.println("\n********** Orders Menu **********");
            	System.out.print("Enter the Order Id: ");
            	int orderId = scanner.nextInt();
            	double totalAmount = ordersService.calculateTotalAmount(orderId);
            	System.out.println("Total Amount for Order ID: $" + totalAmount);
            	break;
            case 2:
            	System.out.println("\n********** Orders Menu **********");
            	System.out.print("Enter the Order Id: ");
            	int orderId1 = scanner.nextInt();
            	ordersService.getOrderDetails(orderId1);
            	break;
            case 3:
            	System.out.println("\n********** Orders Menu **********");
            	System.out.print("Enter the Order Id: ");
            	int orderId2 = scanner.nextInt();
            	scanner.nextLine();
            	System.out.print("Enter New Order status");
            	String newStatus = scanner.nextLine();
            	ordersService.updateOrderStatus(orderId2, newStatus);
            	break;
            case 4:
            	System.out.println("\n********** Orders Menu **********");
            	System.out.print("Enter the Order Id to cancel: ");
            	int orderId3 = scanner.nextInt();
            	ordersService.cancelOrder(orderId3);
            	break;
            	
            case 5:
            	System.out.println("\n********** Orders Menu **********");
            	System.out.print("Enter the Product id: ");
            	int productid = scanner.nextInt();
            	System.out.print("Enter the Quantity: ");
            	int quantity = scanner.nextInt();
            	ordersService.totalamount(productid, quantity);
            case 6:
            	System.out.println("\n********** Orders Menu **********");
            	System.out.print("Enter the Order id: ");
            	int payordid = scanner.nextInt();
            	System.out.print("Enter the Customer id: ");
            	int paycustid = scanner.nextInt();
            	System.out.print("Enter the Product id: ");
            	int payprodid = scanner.nextInt();
            	System.out.print("Enter the Quantity: ");
            	int payquanid = scanner.nextInt();
            	System.out.print("Enter the Toatal Amount You are paying for the products");
            	int totalamount = scanner.nextInt();
            	ordersService.payment(payordid,paycustid, payprodid, payquanid, totalamount);
            case 7:
            	System.out.println("Returning to Main Menu.");
                return false;
            default:
            	System.out.println("Invalid choice. Please enter a number between 1 and 5.");
            }
		}
	}
}
