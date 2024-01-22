
############TASK 1. Database Design###########
use techshop;
insert into customers (CustomerID, FirstName, LastName, Email, Phone, Address)
values (1,'Meghana','kurmapu','meghana@gmail.com','1234589689','123 main st'),
(2,'vinay','kumar','vinay@gmail.com','6396389564','56 main st'),
(3,'Ghana','Megha','ghana@gmail.com','6396389564','763 main st'),
(4,'Alice','Jhon','alice@gmail.com','8956389563','583 main st'),
(5,'Ram','Kumar','ram@gmail.com','7894561237','478 main st'),
(6,'Sita','Williams','sita@gmail.com','7485974859','695 main st'),
(7,'Eva','Jones','eva@gmail.com','9658796587','12 main st'),
(8,'Grace','Davis','grace@gmail.com','6895665478','7 main st'),
(9,'Henry','Taylor','henry@gmail.com','8989898989','6 main st'),
(10,'Ivy','Moore','ivy@gmail.com','7878787878','3 main st');
select * from customers;
INSERT INTO Products (ProductID, ProductName, Description, Price) 
VALUES 
  (1, 'Laptop', 'High-performance laptop with advanced features', 1200.00),
  (2, 'Smartphone', 'Latest smartphone with cutting-edge technology', 800.00),
  (3, 'Tablet', 'Portable tablet for on-the-go use', 500.00),
  (4, 'Smartwatch', 'Feature-rich smartwatch with health tracking', 150.00),
  (5, 'Camera', 'Professional-grade camera for photography enthusiasts', 1000.00),
  (6, 'Headphones', 'Noise-canceling headphones for immersive audio', 150.00),
  (7, 'Bluetooth Speaker', 'Portable speaker with Bluetooth connectivity', 80.00),
  (8, 'GAming Console', 'High-performance gaming console for gamers', 400.00),
  (9, 'Fitness Tracker', 'Fitness tracker to monitor physical activities', 70.00),
  (10, 'E-reader', 'Electronic reader for book lovers', 120.00);
  select * from products;
  INSERT INTO Orders (OrderID, CustomerID, OrderDate, TotalAmount) 
VALUES 
  (1, 1, '2024-01-08', 2000.00),
  (2, 2, '2024-01-09', 1000.00),
  (3, 3, '2024-01-10', 1500.00),
  (4, 4, '2024-01-11', 800.00),
  (5, 5, '2024-01-12', 2500.00),
  (6, 6, '2024-01-13', 300.00),
  (7, 7, '2024-01-14', 160.00),
  (8, 8, '2024-01-15', 1200.00),
  (9, 9, '2024-01-16', 350.00),
  (10, 10, '2024-01-17', 600.00);
select * from orders;
INSERT INTO OrderDetails (OrderDetailID, OrderID, ProductID, Quantity) 
VALUES 
  (1, 1, 1, 2),
  (2, 1, 3, 1),
  (3, 2, 2, 1),
  (4, 3, 4, 3),
  (5, 4, 5, 1),
  (6, 5, 6, 2),
  (7, 6, 7, 1),
  (8, 7, 8, 1),
  (9, 8, 9, 4),
  (10, 9, 10, 2);
  select * from orderdetails;
INSERT INTO Inventory (InventoryID, ProductID, QuantityInStock, LastStockUpdate) 
VALUES 
  (1, 1, 20, '2024-01-08'),
  (2, 2, 15, '2024-01-09'),
  (3, 3, 30, '2024-01-10'),
  (4, 4, 10, '2024-01-11'),
  (5, 5, 25, '2024-01-12'),
  (6, 6, 18, '2024-01-13'),
  (7, 7, 40, '2024-01-14'),
  (8, 8, 12, '2024-01-15'),
  (9, 9, 50, '2024-01-16'),
  (10, 10, 22, '2024-01-17');
  select * from inventory;
  
  
  
  ################TASK 2: Select, Where, Between, AND, LIKE:#######################
  
  #1.Retrieve the names and emails of all customers:
SELECT FirstName, LastName, Email FROM Customers;



#2.List all orders with their order dates and corresponding customer names:
SELECT Orders.OrderID, OrderDate, CONCAT(FirstName, ' ', LastName) AS CustomerName
FROM Orders
JOIN Customers ON Orders.CustomerID = Customers.CustomerID;



#3.Insert a new customer record into the "Customers" table:
INSERT INTO Customers (CustomerID, FirstName, LastName, Email, Phone, Address)
VALUES (11, 'Rani','lucky', 'rani@email.com', '7596589657', '156 New St');




#4.Update the prices of all electronic gadgets in the "Products" table by increasing them by 10%:
UPDATE Products SET Price = Price * 1.1 WHERE ProductID <=10;




#5.Delete a specific order and its associated order details from the "Orders" and "OrderDetails" tables:
-- Begin a transaction to ensure atomicity
START TRANSACTION;
-- Delete from OrderDetails first
DELETE FROM OrderDetails WHERE OrderID = 8;
-- Then delete from Orders
DELETE FROM Orders WHERE OrderID = 8;
-- Commit the transaction
COMMIT;
select * from orders;



#6.Insert a new order into the "Orders" table:
INSERT INTO Orders (CustomerID, OrderDate, TotalAmount)
VALUES (9, '24-01-23',70);



#7.Update the contact information of a specific customer in the "Customers" table:
UPDATE Customers SET Email = 'varma@gmail.com', Address = '658 main st'
WHERE CustomerID = 8;
select * from customers;



#8.Recalculate and update the total cost of each order in the "Orders" table:
UPDATE orders
SET TotalAmount = (SELECT SUM(products.Price * OrderDetails.Quantity)
                  FROM OrderDetails
                  JOIN products ON OrderDetails.ProductID = products.ProductID
                  WHERE OrderDetails.OrderID = orders.OrderID);
                  
-- Disable safe update mode for this session
SET SQL_SAFE_UPDATES = 0;
-- Update the total cost of each order in the "Orders" table
UPDATE Orders
JOIN (
   SELECT OrderID, SUM(Products.Price * OrderDetails.Quantity) AS TotalAmount
   FROM OrderDetails
   JOIN Products ON OrderDetails.ProductID = Products.ProductID
   GROUP BY OrderID
) AS CalculatedCost
ON Orders.OrderID = CalculatedCost.OrderID
SET Orders.TotalAmount = CalculatedCost.TotalAmount;
-- Enable safe update mode again
SET SQL_SAFE_UPDATES = 1;






#9.Delete all orders and their associated order details for a specific customer:
DELETE FROM Orders WHERE CustomerID = 8;


#10.Insert a new electronic gadget product into the "Products" table:
INSERT INTO Products (ProductID, ProductName, Description, Price)
VALUES (11, 'Washing Machine', 'Automatic washing machine', 800);



#11.Update the status of a specific order in the "Orders" table:
-- Add a "Status" column to the "Orders" table:
ALTER TABLE Orders ADD COLUMN Status VARCHAR(255) DEFAULT 'Pending';
-- Update the status of a specific order in the "Orders" table:
UPDATE Orders SET Status = 'Shipped' WHERE OrderID = 9;
select * from orders;




#12.Calculate and update the number of orders placed by each customer in the "Customers" table:
-- Add an "OrderCount" column to the "Customers" table:
ALTER TABLE Customers ADD COLUMN OrderCount INT DEFAULT 0;
-- Update the number of orders placed by each customer:
UPDATE Customers
SET OrderCount = (
    SELECT COUNT(*) 
    FROM Orders 
    WHERE Orders.CustomerID = Customers.CustomerID
);
-- Disable safe update mode for this session
SET SQL_SAFE_UPDATES = 0;
-- Update the number of orders placed by each customer
UPDATE Customers
SET OrderCount = (
    SELECT COUNT(*) 
    FROM Orders 
    WHERE Orders.CustomerID = Customers.CustomerID
);
-- Enable safe update mode again
SET SQL_SAFE_UPDATES = 1;
select * from customers;




###################TASK 3. Aggregate functions, Having, Order By, GroupBy and Joins:##################

#1.Retrieve a list of all orders along with customer information (e.g., customer name) for each order:
SELECT Orders.OrderID, OrderDate, CONCAT(FirstName, ' ', LastName) AS CustomerName
FROM Orders
JOIN Customers ON Orders.CustomerID = Customers.CustomerID;


#2.Find the total revenue generated by each electronic gadget product. Include the product name and the total revenue:
SELECT Products.ProductID, ProductName, SUM(Products.Price * OrderDetails.Quantity) AS TotalRevenue
FROM OrderDetails
JOIN Products ON OrderDetails.ProductID = Products.ProductID
GROUP BY Products.ProductID, ProductName;


#3.List all customers who have made at least one purchase. Include their names and contact information:
SELECT DISTINCT Customers.CustomerID, FirstName, LastName, Email, Phone, Address
FROM Customers
JOIN Orders ON Customers.CustomerID = Orders.CustomerID;


#4.Find the most popular electronic gadget (highest total quantity ordered). Include the product name and the total quantity ordered:
SELECT Products.ProductID, ProductName, SUM(OrderDetails.Quantity) AS TotalQuantityOrdered
FROM OrderDetails
JOIN Products ON OrderDetails.ProductID = Products.ProductID
GROUP BY Products.ProductID, ProductName
ORDER BY TotalQuantityOrdered DESC
LIMIT 1;


#5.Retrieve a list of electronic gadgets along with their corresponding categories:
-- Retrieve a list of electronic gadgets along with their corresponding categories:
SELECT ProductID, ProductName
FROM Products;


#6.Calculate the average order value for each customer. Include the customer's name and their average order value:
SELECT Customers.CustomerID, FirstName, LastName, AVG(Orders.TotalAmount) AS AverageOrderValue
FROM Customers
JOIN Orders ON Customers.CustomerID = Orders.CustomerID
GROUP BY Customers.CustomerID, FirstName, LastName;


#7.Find the order with the highest total revenue. Include the order ID, customer information, and the total revenue:
SELECT Orders.OrderID, OrderDate, CONCAT(FirstName, ' ', LastName) AS CustomerName, SUM(Products.Price * OrderDetails.Quantity) AS TotalRevenue
FROM Orders
JOIN Customers ON Orders.CustomerID = Customers.CustomerID
JOIN OrderDetails ON Orders.OrderID = OrderDetails.OrderID
JOIN Products ON OrderDetails.ProductID = Products.ProductID
GROUP BY Orders.OrderID, OrderDate, CustomerName
ORDER BY TotalRevenue DESC
LIMIT 1;



#8.List electronic gadgets and the number of times each product has been ordered:
SELECT Products.ProductID, ProductName, COUNT(OrderDetails.OrderID) AS OrderCount
FROM Products
LEFT JOIN OrderDetails ON Products.ProductID = OrderDetails.ProductID
GROUP BY Products.ProductID, ProductName;



#9.Find customers who have purchased a specific electronic gadget product. Allow users to input the product name as a parameter:
SELECT DISTINCT Customers.CustomerID, FirstName, LastName, Email, Phone, Address
FROM Customers
JOIN Orders ON Customers.CustomerID = Orders.CustomerID
JOIN OrderDetails ON Orders.OrderID = OrderDetails.OrderID
JOIN Products ON OrderDetails.ProductID = Products.ProductID
WHERE Products.ProductName = 'Laptop';



#10.Calculate the total revenue generated by all orders placed within a specific time period. Allow users to input the start and end dates as parameters:
SELECT SUM(Orders.TotalAmount) AS TotalRevenue
FROM Orders
WHERE OrderDate BETWEEN '2024-01-09' AND '2024-01-11';





################TASK 4. Subquery and its type:#########################

#1.Find out which customers have not placed any orders:
SELECT CustomerID, FirstName, LastName
FROM Customers
WHERE CustomerID NOT IN (SELECT DISTINCT CustomerID FROM Orders);


#2.Find the total number of products available for sale:
SELECT COUNT(*) AS TotalProducts
FROM Products;


#3.Calculate the total revenue generated by TechShop:
SELECT SUM(TotalAmount) AS TotalRevenue
FROM Orders;


#4.Calculate the average quantity ordered for products in a specific category. Allow users to input the category name as a parameter:
SELECT AVG(Quantity) AS AvgQuantityOrdered
FROM OrderDetails
WHERE ProductID IN (SELECT ProductID FROM Products WHERE ProductName = 'Smartphone');


#5.Calculate the total revenue generated by a specific customer. Allow users to input the customer ID as a parameter:
SELECT SUM(TotalAmount) AS TotalRevenue
FROM Orders
WHERE CustomerID = 7;


#6.Find the customers who have placed the most orders. List their names and the number of orders they've placed:
SELECT Customers.CustomerID, FirstName, LastName, COUNT(*) AS OrderCount
FROM Customers
JOIN Orders ON Customers.CustomerID = Orders.CustomerID
GROUP BY Customers.CustomerID, FirstName, LastName
ORDER BY OrderCount DESC
LIMIT 1;



#7.Find the most popular product category (highest total quantity ordered across all orders):
-- Find the most popular product category with the highest total quantity ordered:
SELECT Products.Price, SUM(OrderDetails.Quantity) AS TotalQuantityOrdered
FROM OrderDetails
JOIN Products ON OrderDetails.ProductID = Products.ProductID
GROUP BY Products.Price
ORDER BY TotalQuantityOrdered DESC
LIMIT 1;


#8.Find the customer who has spent the most money (highest total revenue) on electronic gadgets. List their name and total spending:
SELECT Customers.CustomerID, FirstName, LastName, SUM(TotalAmount) AS TotalSpending
FROM Orders
JOIN Customers ON Orders.CustomerID = Customers.CustomerID
WHERE Orders.CustomerID = (SELECT CustomerID FROM Orders GROUP BY CustomerID ORDER BY SUM(TotalAmount) DESC LIMIT 1)
GROUP BY Customers.CustomerID, FirstName, LastName;


#9.Calculate the average order value (total revenue divided by the number of orders) for all customers:
SELECT AVG(TotalAmount) AS AvgOrderValue
FROM Orders;


#10.Find the total number of orders placed by each customer and list their names along with the order count:
SELECT Customers.CustomerID, FirstName, LastName, COUNT(Orders.OrderID) AS OrderCount
FROM Customers
LEFT JOIN Orders ON Customers.CustomerID = Orders.CustomerID
GROUP BY Customers.CustomerID, FirstName, LastName;
use tech_shop;
select * from Products;

use techshop;
select * from Orders;
DELETE FROM orders WHERE OrderID = 2;
ALTER TABLE orderdetails
DROP FOREIGN KEY OrderID;
ALTER TABLE orderdetails
ADD CONSTRAINT fk_orderdetails_order
FOREIGN KEY (OrderID)
REFERENCES orders(OrderID)
ON DELETE SET NULL;
ALTER TABLE orderdetails
MODIFY COLUMN OrderID INT NULL;
ALTER TABLE orderdetails
ADD CONSTRAINT fk_orderdetails_order
FOREIGN KEY (OrderID)
REFERENCES orders(OrderID)
ON DELETE SET NULL;
select * from Products;
ALTER TABLE OrderDetails
ADD COLUMN Discount DOUBLE DEFAULT 0.0;

-- Assuming OrderDetailID starts from 1 and goes up to 10

UPDATE OrderDetails SET Discount = 5.0 WHERE OrderDetailID = 1;
UPDATE OrderDetails SET Discount = 7.5 WHERE OrderDetailID = 2;
-- Assuming OrderDetailID starts from 3 and goes up to 10

UPDATE OrderDetails SET Discount = 10.0 WHERE OrderDetailID = 3;
UPDATE OrderDetails SET Discount = 3.5 WHERE OrderDetailID = 4;
UPDATE OrderDetails SET Discount = 8.0 WHERE OrderDetailID = 5;
UPDATE OrderDetails SET Discount = 2.0 WHERE OrderDetailID = 6;
UPDATE OrderDetails SET Discount = 6.5 WHERE OrderDetailID = 7;
UPDATE OrderDetails SET Discount = 4.0 WHERE OrderDetailID = 8;
UPDATE OrderDetails SET Discount = 9.5 WHERE OrderDetailID = 9;
UPDATE OrderDetails SET Discount = 1.0 WHERE OrderDetailID = 10;


