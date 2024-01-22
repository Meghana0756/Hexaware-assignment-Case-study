package com.techshop.TechShop.exception;

import java.sql.SQLException;

public class DatabaseException extends RuntimeException{
	public DatabaseException(String message) {
		super(message);
	}

	
}
