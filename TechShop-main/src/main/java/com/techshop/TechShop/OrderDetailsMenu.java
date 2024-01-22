package com.techshop.TechShop;

import java.util.List;
import java.util.Scanner;

import com.techshop.TechShop.dao.OrderDetailsService;
import com.techshop.TechShop.entity.Products;
import com.techshop.TechShop.entity.Orders;
import com.techshop.TechShop.entity.OrderDetails;
import com.techshop.TechShop.dao.Impl.OrderDetailsServiceImpl;

public class OrderDetailsMenu {
static OrderDetailsService orderDetailsService = new OrderDetailsServiceImpl();
	
	public boolean OrderDetailsDetailsMenu() {
		Scanner scanner = new Scanner(System.in);
		while (true) {
			System.out.println("\n********** Order Details Menu **********");
            System.out.println("1. calculateSubTotal");
            System.out.println("2. getOrderDetailInfo");
            System.out.println("3. updateQuantity");
            System.out.println("4. addDiscount");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            
            int customerchoice = scanner.nextInt();
            switch(customerchoice) {
    		case 1:
    			System.out.println("\n********** Order Details Menu **********");
                System.out.print("Enter the Order Detail ID: ");
                int orderDetailId = scanner.nextInt();
                double subtotal = orderDetailsService.calculateSubtotal(orderDetailId);
                System.out.println("Subtotal for Order Detail ID: $" + subtotal);
                break;
            case 2:
                System.out.println("\n********** Order Details Menu **********");
                System.out.print("Enter the Order Detail ID: ");
                int orderDetailIdInfo = scanner.nextInt();
                orderDetailsService.getOrderDetailInfo(orderDetailIdInfo);
                break;
            case 3:
                System.out.println("\n********** Order Details Menu **********");
                System.out.print("Enter the Order Detail ID: ");
                int orderDetailIdUpdate = scanner.nextInt();
                System.out.print("Enter the new quantity: ");
                int newQuantity = scanner.nextInt();
                orderDetailsService.updateQuantity(orderDetailIdUpdate, newQuantity);
                System.out.println("Quantity updated successfully.");
                break;
            case 4:
                System.out.println("\n********** Order Details Menu **********");
                System.out.print("Enter the Order Detail ID: ");
                int orderDetailIdDiscount = scanner.nextInt();
                System.out.print("Enter the discount amount: ");
                double discountAmount = scanner.nextDouble();
                orderDetailsService.addDiscount(orderDetailIdDiscount, discountAmount);
                System.out.println("Discount added successfully.");
                break;
            case 5:
                System.out.println("Returning to Main Menu.");
                return false;
            default:
                System.out.println("Invalid choice. Please enter a number between 1 and 5.");
            }
		}
	}
}
