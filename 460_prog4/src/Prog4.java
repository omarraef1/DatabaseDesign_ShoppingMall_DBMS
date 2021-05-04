/**
 * 
 * Omar R.G. Mohamed, Paul Rich || CSC460 || Prog4 || Dr. Lester I. McCann
 * 
 * 
 * There is no need to call this program followed by a username and a password because
 * They are already hard coded
 * 
 * You do however need too call this ->
 * 
 * 					export CLASSPATH=/opt/oracle/product/10.2.0/client/jdbc/lib/ojdbc14.jar:${CLASSPATH}
 * 
 * Before you run the program.
 * 
 * 
 * This is a Database Management System for Tucson Mall,
 * The Database contains Members, Sales, Subsales, Products, Warehouse, Suppliers, and Employees.
 * The System operates a reward points system for members and offers discounts accordingly.
 * It allows users to add and update data about members, employees, products, suppliers, and sales.
 * 
 * This program is case sensitive so you must enter exactly the options shown on the screen.
 * 
 * On record insertion, the record ID is incremented using the table's length,
 * therefore no two ID's are the same.
 * 
 * 
 * The Program runs a Menu of 6 options:
 * a) Record Insertion
 * b) Record Deletion
 * c) Record Update
 * d) Queries
 * e) Show Menu
 * f) Quit Program
 * 
 * A connection is established through JDBC to my Oracle account and 
 * on each option:
 * Retrieves query results as requested,
 * and closes connection on program termination
 * 
 * Just make sure to call the program from command-line through lectura
 * e.g. java Prog4 
 * 
 */


import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;
import java.sql.*;

public class Prog4 {
	
	public static void main(String[] args) {
		
		/////////// logging onto oracle ///////////
		final String oracleURL =   // Magic lectura -> aloe access spell
              "jdbc:oracle:thin:@aloe.cs.arizona.edu:1521:oracle";
		String username = null,    // Oracle DBMS username
		       password = null;    // Oracle DBMS password
		username = "omarraef1";
		password = "a9572";
		try {Class.forName("oracle.jdbc.OracleDriver");
		} catch (ClassNotFoundException e) {
	        System.err.println("*** ClassNotFoundException:  "
	            + "Error loading Oracle JDBC driver.  \n"
	            + "\tPerhaps the driver is not on the Classpath?");
	        System.exit(-1);
		}
		Connection dbconn = null;
		try {dbconn = DriverManager.getConnection
		                       (oracleURL,username,password);
		} catch (SQLException e) {
		
		        System.err.println("*** SQLException:  "
		            + "Could not open JDBC connection.");
		        System.err.println("\tMessage:   " + e.getMessage());
		        System.err.println("\tSQLState:  " + e.getSQLState());
		        System.err.println("\tErrorCode: " + e.getErrorCode());
		        System.exit(-1);
		        
		}
		////////////////////////////////////////
		
		
		//First Menu Prompt
		
		System.out.println();
		System.out.println("Options: a) Record Insertion, b) Record Deletion, c) Record Update, d) Query, e) Show Menu, f) Quit");
		System.out.println("Enter a, b, c, d, e, or f");
		System.out.println();
		
		Scanner sc = new Scanner(System.in);
		String usrInp = "";
		
		// Program will keep running until user selects 'f'
		while(true) {
			usrInp = sc.nextLine();
			if(usrInp.equals("f")) { // quit program
				break;
			}
			else if(usrInp.equals("e")) { // show menu
				System.out.println();
				System.out.println("Options: a) Record Insertion, b) Record Deletion, c) Record Update, d) Query, e) Show Menu, f) Quit");
				System.out.println("Enter a, b, c, d, e, or f");
				System.out.println();
			}
			else if(usrInp.equals("d")) { // queries
				
				System.out.println();
				System.out.println("Available Queries:");
				System.out.println("a) Display member by phone number or ID");
				System.out.println("b) Display profit of the current month ");
				System.out.println("c) Display current most profitable product");
				System.out.println("d) Display the all-time highest spending members"); 
				System.out.println("Enter a, b, c, or d");
				String selectedQuery = sc.nextLine();
				
				// Perform Query 1
				if (selectedQuery.equals("a")) {
					System.out.println("Enter 'p' to search by phone number or 'i' to search by ID");
					String phoneNumOrID = sc.nextLine();
					
					// Search by phone number
					if (phoneNumOrID.equals("p")) {
						
						
						System.out.println("Enter phone number (in format 0123456789): ");
						String enteredPhoneNum = sc.nextLine();
						Statement stmt;
						try {
							stmt = dbconn.createStatement();
							String aQuery = "select firstname, lastname, DOB, points from members where phone = " + enteredPhoneNum;
							ResultSet answer = stmt.executeQuery(aQuery);
							
							if (answer != null) {
								answer.next();
								String firstName = answer.getString("FIRSTNAME");
								String lastName = answer.getString("LASTNAME");
								String DOB = answer.getString("DOB");
								String points = answer.getString("POINTS");
								System.out.println("First Name\tLastName\tDOB\tPoints");
								System.out.println(firstName + "\t" + lastName + "\t" + DOB + "\t" + points);
							}
							
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					// Search by ID
					} else if (phoneNumOrID.equals("i")) {
						
						
						System.out.println("Enter ID: ");
						String enteredID = String.valueOf(Integer.valueOf(sc.nextLine()));
						Statement stmt;
						try {
							stmt = dbconn.createStatement();
							String aQuery = "select firstname, lastname, DOB, points from members where memid = " + enteredID;
							ResultSet answer = stmt.executeQuery(aQuery);
							
							if (answer != null) {
								answer.next();
								String firstName = answer.getString("FIRSTNAME");
								String lastName = answer.getString("LASTNAME");
								String DOB = answer.getString("DOB");
								String points = answer.getString("POINTS");
								System.out.println("First Name\tLastName\tDOB\tPoints");
								System.out.println(firstName + "\t" + lastName + "\t" + DOB + "\t" + points);
							}
							
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} // --- END Query 1 ---
				
				// Perform (or not really) Query 2
				} else if (selectedQuery.equals("b")) {
					System.out.println("The second query was removed per the spec - sorry!");
					
				// Perform Query 3
				} else if (selectedQuery.equals("c")) {

					try {
						Statement stmt = dbconn.createStatement();
						String cQuery = "select * from ("
								+ "select products.prodid, products.name, sum(subsale.price * subsale.amount - warehouse.purchaseprice) "
								+ "as profit from products join subsale on products.prodid=subsale.prodid join warehouse on products.prodid=warehouse.prodid " 
								+ "group by products.prodid, products.name order by profit desc) where rownum = 1";
								
						ResultSet answer = stmt.executeQuery(cQuery);
						
					
						System.out.println("\n");
						
						if (answer != null) {
							answer.next();
							String prodID = answer.getString("PRODID");
							String name = answer.getString("NAME");
							String profit = answer.getString("PROFIT");
							System.out.println("Product ID\tName\tProfit");
							System.out.println(prodID + "\t\t" + name + "\t" + profit);
						}
						System.out.println("\n");
						stmt.close();
						
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				// Perform Query 4	
				} else if (selectedQuery.equals("d")) {
					
					try {
						Statement stmt = dbconn.createStatement();
						String dQuery = "select members.memid, members.firstName, members.lastName, sum(sale.totalPrice) as totalspent "
								+ "from members join sale on members.memid=sale.memid group by members.memid, members.firstName, members.lastName "
								+ "order by sum (sale.totalPrice) desc";
						ResultSet answer = stmt.executeQuery(dQuery);
						
						if (answer != null) {
							
							System.out.println("\n");
							System.out.println("Member ID\tName\t\t\tTotal Spent");
							
							while(answer.next()) {
								String memID = answer.getString("MEMID");
								String firstName = answer.getString("FIRSTNAME");
								String lastName = answer.getString("LASTNAME");
								String totalSpent = answer.getString("TOTALSPENT");
								
								System.out.println(memID + "\t\t" + firstName + " " + lastName + "\t\t" + totalSpent);
							}
							System.out.println("\n");
							stmt.close();
						}
						
						
						
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				
				
				
			}
			else if(usrInp.equals("c")) { // record update
				System.out.println("What would you like to update? (member, employee, product, supplier)");
				usrInp = sc.nextLine().toLowerCase();
				System.out.println();
				
				
				try {
					
					Statement stmt = dbconn.createStatement();
					
					if(usrInp.equals("member")) { // update members info accordingly
						System.out.println("Enter the ID of the Member to be updated: ");
						String memberToUpdate = sc.nextLine();
						String fieldToUpdate = "";
						System.out.println("Enter the name of the field (one at a time) you want to update, or 'done' to finish updating.");
						System.out.println("Fields: firstName, lastName, DOB, Address, phone, points");
						
						while (!fieldToUpdate.equals("done")) {
							String updateQuery = "";
							fieldToUpdate = sc.nextLine().toLowerCase();
							if (fieldToUpdate.equals("firstname")) {
								System.out.println("Enter the new first name: ");
								String newFirstName = sc.nextLine();
								updateQuery = "update omarraef1.members set firstName='" + newFirstName + "' where memid=" + memberToUpdate;
								stmt.executeQuery(updateQuery);
							} else if (fieldToUpdate.equals("lastname")) {
								System.out.println("Enter the new last name: ");
								String newLastName = sc.nextLine();
								updateQuery = "update omarraef1.members set lastName='" + newLastName + "' where memid=" + memberToUpdate;
								stmt.executeQuery(updateQuery);
							} else if (fieldToUpdate.equals("dob")) {
								System.out.println("Enter the new DOB: ");
								String newDOB = sc.nextLine();
								updateQuery = "update omarraef1.members set DOB='" + newDOB + "' where memid=" + memberToUpdate;
								stmt.executeQuery(updateQuery);
							} else if (fieldToUpdate.equals("address")) {
								System.out.println("Enter the new address");
								String newAddress = sc.nextLine();
								updateQuery = "update omarraef1.members set address='" + newAddress + "' where memid=" + memberToUpdate;
								stmt.executeQuery(updateQuery);
							} else if (fieldToUpdate.equals("phone")) {
								System.out.println("Enter the new phone: ");
								String newPhone = sc.nextLine();
								updateQuery = "update omarraef1.members set phone='" + newPhone + "' where memid=" + memberToUpdate;
								stmt.executeQuery(updateQuery);
							} else if (fieldToUpdate.equals("points")) {
								System.out.println("Enter the new points total: ");
								String newPhone = sc.nextLine();
								updateQuery = "update omarraef1.members set points='" + newPhone + "' where memid=" + memberToUpdate;
								stmt.executeQuery(updateQuery);
							} else {
								System.out.println("Invalid field name, please try again.");
							}
							
							
							System.out.println("Enter the name of the field (one at a time) you want to update, or 'done' to finish updating.");
							System.out.println("Fields: firstName, lastName, DOB, Address, phone, points");
							fieldToUpdate = sc.nextLine();
							
							
						}
						System.out.println("");
						
					} else if (usrInp.equals("employee")) { // update employee info
						System.out.println("Enter the ID of the employee to be updated: ");
						String employeeToUpdate = sc.nextLine();
						String fieldToUpdate = "";
						
						System.out.println("Enter the name of the field (one at a time) you want to update, or 'done' to finish updating.");
						System.out.println("Fields: firstName, lastName, gender, address, phone, group, salary");
						fieldToUpdate = sc.nextLine().toLowerCase();
						while (!fieldToUpdate.equals("done")) {
							String updateQuery = "";
							if (fieldToUpdate.equals("firstname")) {
								System.out.println("Enter the new first name: ");
								String newFirstName = sc.nextLine();
								updateQuery = "update omarraef1.employees set firstName='" + newFirstName + "' where empid=" + employeeToUpdate;
								stmt.executeQuery(updateQuery);
							} else if (fieldToUpdate.equals("lastname")) {
								System.out.println("Enter the new last name: ");
								String newLastName = sc.nextLine();
								updateQuery = "update omarraef1.employees set lastName='" + newLastName + "' where empid=" + employeeToUpdate;
								stmt.executeQuery(updateQuery);
							} else if (fieldToUpdate.equals("gender")) {
								System.out.println("Enter the new gender: ");
								String newGender = sc.nextLine();
								updateQuery = "update omarraef1.employees set gender='" + newGender + "' where empid=" + employeeToUpdate;
								stmt.executeQuery(updateQuery);
							} else if (fieldToUpdate.equals("address")) {
								System.out.println("Enter the new address: ");
								String newAddress = sc.nextLine();
								updateQuery = "update omarraef1.employees set address='" + newAddress + "' where empid=" + employeeToUpdate;
								stmt.executeQuery(updateQuery);
							} else if (fieldToUpdate.equals("phone")) {
								System.out.println("Enter the new phone: ");
								String newPhone = sc.nextLine();
								updateQuery = "update omarraef1.employees set phone='" + newPhone + "' where empid=" + employeeToUpdate;
								stmt.executeQuery(updateQuery);
							} else if (fieldToUpdate.equals("group")) {
								System.out.println("Enter the new group: ");
								String newGroup = sc.nextLine();
								updateQuery = "update omarraef1.employees set empgroup='" + newGroup + "' where empid=" + employeeToUpdate;
								stmt.executeQuery(updateQuery);
							} else if (fieldToUpdate.equals("salary")) {
								System.out.println("Enter the new salary: ");
								String newSalary = sc.nextLine();
								updateQuery = "update omarraef1.employees set salary='" + newSalary + "' where empid=" + employeeToUpdate;
								stmt.executeQuery(updateQuery);
							} else {
								System.out.println("Invalid field name, please try again.");
							}
							
							System.out.println("Enter the name of the field (one at a time) you want to update, or 'done' to finish updating.");
							System.out.println("Fields: firstName, lastName, gender, Address, phone, group, salary");
							fieldToUpdate = sc.nextLine();
							
						}
						
						
					} else if (usrInp.equals("product")) { // update product info
						
						System.out.println("Enter the ID of the product to be updated: ");
						String productToUpdate = sc.nextLine();
						String fieldToUpdate = "";
						
						System.out.println("Enter the name of the field (one at a time) you want to update, or 'done' to finish updating.");
						System.out.println("Fields: name, retailPrice, category, memberDiscount, stock");
						fieldToUpdate = sc.nextLine().toLowerCase();
						
						while (!fieldToUpdate.equals("done")) {
							String updateQuery = "";
							if (fieldToUpdate.equals("name")) {
								System.out.println("Enter the new name: ");
								String newName = sc.nextLine();
								updateQuery = "update omarraef1.products set name='" + newName + "' where prodid=" + productToUpdate;
								stmt.executeQuery(updateQuery);
							} else if (fieldToUpdate.equals("retailprice")) {
								System.out.println("Enter the new retail price: ");
								String newPrice = sc.nextLine();
								updateQuery = "update omarraef1.products set retailprice='" + newPrice + "' where prodid=" + productToUpdate;
								stmt.executeQuery(updateQuery);
							} else if (fieldToUpdate.equals("category")) {
								System.out.println("Enter the new category: ");
								String newCategory = sc.nextLine();
								updateQuery = "update omarraef1.products set category='" + newCategory + "' where prodid=" + productToUpdate;
								stmt.executeQuery(updateQuery);
							} else if (fieldToUpdate.equals("memberDiscount")) {
								System.out.println("Enter the new member discount: ");
								String newMemDisc = sc.nextLine();
								updateQuery = "update omarraef1.products set memdisc='" + newMemDisc + "' where prodid=" + productToUpdate;
								stmt.executeQuery(updateQuery);
							} else if (fieldToUpdate.equals("stock")) {
								System.out.println("Enter the new stock: ");
								String newStock = sc.nextLine();
								updateQuery = "update omarraef1.products set stock='" + newStock + "' where prodid=" + productToUpdate;
								stmt.executeQuery(updateQuery);
							} else {
								System.out.println("Invalid field name, please try again.");
							}
							System.out.println("Enter the name of the field (one at a time) you want to update, or 'done' to finish updating.");
							System.out.println("Fields: name, retailPrice, category, memberDiscount, stock");
							fieldToUpdate = sc.nextLine();
						}		
					} else if (usrInp.equals("supplier")) { // update supplier info
						System.out.println("Enter the ID of the supplier to be updated: ");
						String supplierToUpdate = sc.nextLine();
						String fieldToUpdate = "";
						
						System.out.println("Enter the name of the field (one at a time) you want to update, or 'done' to finish updating.");
						System.out.println("Fields: name, address, contactPerson");
						fieldToUpdate = sc.nextLine().toLowerCase();
						while (!fieldToUpdate.equals("done")) {
							String updateQuery = "";
							if (fieldToUpdate.equals("name")) {
								System.out.println("Enter the new name: ");
								String newName = sc.nextLine();
								updateQuery = "update omarraef1.suppliers set name='" + newName + "' where supid=" + supplierToUpdate;
								stmt.executeQuery(updateQuery);
							} else if (fieldToUpdate.equals("address")) {
								System.out.println("Enter the new address: ");
								String newAddress = sc.nextLine();
								updateQuery = "update omarraef1.suppliers set address='" + newAddress + "' where supid=" + supplierToUpdate;
								stmt.executeQuery(updateQuery);
							} else if (fieldToUpdate.equals("contactperson")) {
								System.out.println("Enter the new contact person: ");
								String newContact = sc.nextLine();
								updateQuery = "update omarraef1.suppliers set contactperson='" + newContact + "' where supid=" + supplierToUpdate;
								stmt.executeQuery(updateQuery);
							} else {
								System.out.println("Invalid field name, please try again.");
							}
							System.out.println("Enter the name of the field (one at a time) you want to update, or 'done' to finish updating.");
							System.out.println("Fields: name, address, contactPerson");
							fieldToUpdate = sc.nextLine();
						}
						
					}
					
					
				} catch (SQLException e) {
					
			        System.err.println("*** SQLException:  "
			            + "Could not fetch query results.");
			        System.err.println("\tMessage:   " + e.getMessage());
			        System.err.println("\tSQLState:  " + e.getSQLState());
			        System.err.println("\tErrorCode: " + e.getErrorCode());
			        System.exit(-1);
			
			}
				
				
				
				
			}
			else if(usrInp.equals("b")) { // record deletion
				System.out.println("What would you like to delete? (Member, Employee, Product, Supplier)");
				usrInp = sc.nextLine();
				System.out.println();
				
				try {
					
					if(usrInp.equals("Member")){ //delete member
						Statement stmt = null;
						ResultSet answer = null;
						String query = "";
						//enter query info
						System.out.println("Enter member ID(cannot be empty): ");
						String id = sc.nextLine();
						
						query = "delete from members where memID = "+Integer.valueOf(id);
						stmt = dbconn.createStatement();
						answer = stmt.executeQuery(query);
						System.out.println("Record deleted Successfully");
						stmt.close();
					}
					else if(usrInp.equals("Employee")) { // delete employee
						Statement stmt = null;
						ResultSet answer = null;
						String query = "";
						//enter query info
						System.out.println("Enter Employee ID(cannot be empty): ");
						String id = sc.nextLine();
						
						query = "delete from employees where empID = "+Integer.valueOf(id);
						stmt = dbconn.createStatement();
						answer = stmt.executeQuery(query);
						System.out.println("Record deleted Successfully");
						stmt.close();
					}
					else if(usrInp.equals("Product")) { // delete product
						Statement stmt = null;
						ResultSet answer = null;
						String query = "";
						//enter query info
						System.out.println("Enter Product ID(cannot be empty): ");
						String id = sc.nextLine();
						
						query = "delete from products where prodID = "+Integer.valueOf(id);
						stmt = dbconn.createStatement();
						answer = stmt.executeQuery(query);
						System.out.println("Record deleted Successfully");
						stmt.close();
					}
					else if(usrInp.equals("Supplier")) { // delete supplier
						Statement stmt = null;
						ResultSet answer = null;
						String query = "";
						//enter query info
						System.out.println("Enter Supplier ID(cannot be empty): ");
						String id = sc.nextLine();
						

						query = "delete from warehouse where supid=" + id;
						stmt = dbconn.createStatement();
						answer = stmt.executeQuery(query);
						stmt.close();
						
						query = "delete from suppliers where supID = "+Integer.valueOf(id);
						stmt = dbconn.createStatement();
						answer = stmt.executeQuery(query);
						System.out.println("Record deleted Successfully");
						stmt.close();
					}
					
				}
				catch (SQLException e) {
					
			        System.err.println("*** SQLException:  "
			            + "Could not fetch query results.");
			        System.err.println("\tMessage:   " + e.getMessage());
			        System.err.println("\tSQLState:  " + e.getSQLState());
			        System.err.println("\tErrorCode: " + e.getErrorCode());
			        System.exit(-1);
			
			}
				
				
				
			}
			else if(usrInp.equals("a")) { // record insertion
				System.out.println("What would you like to insert? (Member, Employee, Product, Supplier, Sale)"); // todo not accounting for sale yet
				usrInp = sc.nextLine();
				System.out.println();
				
				try {
					
					if(usrInp.equals("Member")){ // insert member
						Statement stmt = null;
						ResultSet answer = null;
						String query = "";
						//enter query info
						System.out.println("Enter member first name(cannot be empty): ");
						String fName = sc.nextLine();
						System.out.println("Enter member last name(cannot be empty): ");
						String lName = sc.nextLine();
						System.out.println("Enter member DOB: ");
						String DOB = sc.nextLine();
						System.out.println("Enter member address: ");
						String addy = sc.nextLine();
						System.out.println("Enter member phone(cannot be empty): ");
						String phone = sc.nextLine();
						
						// increment ID using a count query and initialize points with 0
						Statement idstmt = null;
						ResultSet idanswer = null;
						String idquery = "select count(*) from members";
						idstmt = dbconn.createStatement();
						idanswer = idstmt.executeQuery(idquery);
						if(idanswer != null) {
							ResultSetMetaData idanswermeta = idanswer.getMetaData();
							//System.out.println(idanswermeta.getColumnName(1));
						}
						
						idanswer.next();
						String memID = idanswer.getString("count(*)");
						//System.out.println(memID);
						idstmt.close();
						//query to enter member
						int nuM = Integer.valueOf(memID)*3;
						query = "insert into members values ("+nuM
						+", \'"+fName+"\', \'"+lName+"\', DATE \'"+DOB+"\', \'"+addy+"\', \'"+phone+"\', "+0+")";
						stmt = dbconn.createStatement();
						answer = stmt.executeQuery(query);
						System.out.println("Record Inserted Successfully");
						stmt.close();
					}
					else if(usrInp.equals("Employee")) { // insert employee
						Statement stmt = null;
						ResultSet answer = null;
						String query = "";
						//enter query info
						System.out.println("Enter employee first name(cannot be empty): ");
						String fName = sc.nextLine();
						System.out.println("Enter employee last name(cannot be empty): ");
						String lName = sc.nextLine();
						System.out.println("Enter employee gender: ");
						String gen = sc.nextLine();
						System.out.println("Enter employee address: ");
						String addy = sc.nextLine();
						System.out.println("Enter employee phone(cannot be empty): ");
						String phone = sc.nextLine();
						System.out.println("Enter employee group: ");
						String group = sc.nextLine();
						System.out.println("Enter employee salary: ");
						String salary = sc.nextLine();
						
						// increment ID using a count query
						Statement idstmt = null;
						ResultSet idanswer = null;
						String idquery = "select count(*) from employees";
						idstmt = dbconn.createStatement();
						idanswer = idstmt.executeQuery(idquery);
						if(idanswer != null) {
							ResultSetMetaData idanswermeta = idanswer.getMetaData();
							//System.out.println(idanswermeta.getColumnName(1));
						}
						
						idanswer.next();
						String empID = idanswer.getString("count(*)");
						//System.out.println(memID);
						idstmt.close();
						//query to enter member
						int nuE = Integer.valueOf(empID)*3;
						query = "insert into employees values ("+nuE
						+", \'"+fName+"\', \'"+lName+"\', \'"+gen+"\', \'"+addy+"\', \'"+phone+"\', \'"+group+"\', "+Double.valueOf(salary)+")";
						stmt = dbconn.createStatement();
						answer = stmt.executeQuery(query);
						System.out.println("Record Inserted Successfully");
						stmt.close();
					}
					else if(usrInp.equals("Product")) { // insert product
						Statement stmt = null;
						ResultSet answer = null;
						String query = "";
						//enter query info
						String prodID;
						System.out.println("Enter product name(cannot be empty): ");
						String name = sc.nextLine();
						System.out.println("Enter retail Price: ");
						String retP = sc.nextLine();
						System.out.println("Enter category: ");
						String categ = sc.nextLine();
						System.out.println("Enter member discount: ");
						String memDisc = sc.nextLine();
						System.out.println("Enter stock: ");
						String stock = sc.nextLine();
						
						// increment ID using a count query
						Statement idstmt = null;
						ResultSet idanswer = null;
						String idquery = "select count(*) from products";
						idstmt = dbconn.createStatement();
						idanswer = idstmt.executeQuery(idquery);
						if(idanswer != null) {
							ResultSetMetaData idanswermeta = idanswer.getMetaData();
							//System.out.println(idanswermeta.getColumnName(1));
						}
						
						idanswer.next();
						prodID = idanswer.getString("count(*)");
						//System.out.println(memID);
						idstmt.close();
						
						int nuP = Integer.valueOf(prodID)*3;
						
						//query to enter member
						query = "insert into products values ("+nuP
						+", \'"+name+"\', "+Double.valueOf(retP)+", \'"+categ+"\', "+Double.valueOf(memDisc)+", "+Integer.valueOf(stock)+")";
						stmt = dbconn.createStatement();
						answer = stmt.executeQuery(query);
						System.out.println("Record Inserted Successfully");
						stmt.close();
					}
					else if(usrInp.equals("Supplier")) { // insert supplier
						Statement stmt = null;
						ResultSet answer = null;
						String query = "";
						//enter query info
						String supID;
						System.out.println("Enter supplier name(cannot be empty): ");
						String name = sc.nextLine();
						System.out.println("Enter supplier address: ");
						String addy = sc.nextLine();
						System.out.println("Enter supplier contact person: ");
						String conP = sc.nextLine();
						
						// increment ID using a count query
						Statement idstmt = null;
						ResultSet idanswer = null;
						String idquery = "select count(*) from suppliers";
						idstmt = dbconn.createStatement();
						idanswer = idstmt.executeQuery(idquery);
						if(idanswer != null) {
							ResultSetMetaData idanswermeta = idanswer.getMetaData();
							//System.out.println(idanswermeta.getColumnName(1));
						}
						
						idanswer.next();
						supID = idanswer.getString("count(*)");
						//System.out.println(memID);
						idstmt.close();
						
						int nuSu = Integer.valueOf(supID)*3;
						
						//query to enter member
						query = "insert into suppliers values ("+nuSu
						+", \'"+name+"\', \'"+addy+"\', \'"+conP+"\')";
						stmt = dbconn.createStatement();
						answer = stmt.executeQuery(query);
						System.out.println("Record Inserted Successfully");
						stmt.close();
					}
					else if(usrInp.equals("Sale")) { // initiate sale
						Statement stmt = null;
						ResultSet answer = null;
						String query = "";
						double total = 0.0;
						double totalDisc = 0.0;
						int saleID;
						

						//fetch saleID 
						Statement saleIDstmt = null;
						ResultSet saleIDanswer = null;
						String saleIDQuery = "select count(*) from sale";
						saleIDstmt = dbconn.createStatement();
						saleIDanswer = saleIDstmt.executeQuery(saleIDQuery);
						saleIDanswer.next();
						String tmpsaleID = saleIDanswer.getString("count(*)");
						saleID = Integer.valueOf(tmpsaleID);
						saleIDstmt.close();
						
						//enter query info
						
						while(true) {
							System.out.println("Insert Subsale: ");
							System.out.println("Enter product ID: ");
							String prodID = sc.nextLine();
							String price = "";
							System.out.println("Enter amount: ");
							String amount = sc.nextLine();
							
							//fetch product price
							
							Statement prstmt = null;
							ResultSet prodanswer = null;
							String prodQuery = "select retailPrice from products where prodID = "+Integer.valueOf(prodID);
							prstmt = dbconn.createStatement();
							prodanswer = prstmt.executeQuery(prodQuery);
							
							prodanswer.next();
							price = prodanswer.getString("retailprice");
							prstmt.close();
							
							total += Double.valueOf(price);
							
							
							//fetch discount
							Statement disstmt = null;
							ResultSet disanswer = null;
							String disQuery = "select memDisc from products where prodID = "+Integer.valueOf(prodID);
							disstmt = dbconn.createStatement();
							disanswer = disstmt.executeQuery(disQuery);
							
							disanswer.next();
							String currDis = disanswer.getString("memdisc");
							disstmt.close();
							
							totalDisc += Double.valueOf(currDis);
							
							//fetch subsaleID
							Statement sidstmt = null;
							ResultSet sidanswer = null;
							String sidQuery = "select count(*) from subsale";
							sidstmt = dbconn.createStatement();
							sidanswer = sidstmt.executeQuery(sidQuery);
							sidanswer.next();
							String subsaleID = sidanswer.getString("count(*)");
							sidstmt.close();
							
							int ss = Integer.valueOf(subsaleID) * 3;
							
							//insert subsale record
							Statement substmt =null;
							ResultSet subanswer=null;
							String subQuery = "insert into subsale values ("+ss+", "+Integer.valueOf(prodID)+", "+saleID+
									", "+Double.valueOf(price)+", "+Integer.valueOf(amount)+")";
							substmt = dbconn.createStatement();
							subanswer = substmt.executeQuery(subQuery);
							
							System.out.println("Subsale inserted");
							substmt.close();
							
							
							Statement prodstmt = dbconn.createStatement();
							String oldQtyQuery = "select stock from products where prodid=" + prodID;
							ResultSet qtyAnswer = prodstmt.executeQuery(oldQtyQuery);
							qtyAnswer.next();
							int oldQty = Integer.parseInt(qtyAnswer.getString("stock"));
							int newQty = oldQty - Integer.parseInt(amount);
							String updateQtyQuery = "update products set stock=" + newQty + " where prodid=" + prodID;
							prodstmt.close();
							prodstmt = dbconn.createStatement();
							qtyAnswer = prodstmt.executeQuery(updateQtyQuery);
							//qtyAnswer.next();
							prodstmt.close();
							
							//
							System.out.println("Enter \'next\' for next subsale or \'done\' to end sale");
							usrInp = sc.nextLine();
							if(usrInp.equals("done")) {
								break;
							}
						}
						
						System.out.println("Enter member id: ");
						String memID = sc.nextLine();
						System.out.println("Payment method? (Cash or Card)");
						String pInfo = sc.nextLine();
						

						// check if client has enough points to redeem rewards
						
						String pointsQuery = "select points from members where memid=" + memID;
						stmt = dbconn.createStatement();
						answer = stmt.executeQuery(pointsQuery);
						int currPoints = 0;
						int rewardsToRedeem = 0;
						if (answer != null) {
							answer.next();
							currPoints = Integer.parseInt(answer.getString("POINTS"));
							System.out.println("CURRPOINTS: " + currPoints);
							if (currPoints >= 100) {
								int rewardDollars = currPoints / 100;
								System.out.println("Reward points available! Enter up to " + rewardDollars + " rewards");
								rewardsToRedeem = Integer.parseInt(sc.nextLine());
								if (rewardsToRedeem != 0 && rewardsToRedeem <=rewardDollars) {
									int pointsDeduct = rewardsToRedeem * 100;
									currPoints = currPoints - pointsDeduct;
									String deductQuery = "update members set points=" + currPoints + " where memid=" + memID;
									stmt.executeQuery(deductQuery);
									
									
								} 
							}
							
							
						}

						stmt.close();
						
						
						
						//calculate discount
						double newTotal = total - (total*totalDisc);
						if(newTotal < 0) {
							newTotal = 0;
						}
						
						
						//query to enter sale
						int nuS = saleID*3;
						query = "insert into sale values ("+nuS+", DATE \'"+java.time.LocalDate.now()+"\', \'"+pInfo+"\', "+newTotal+", "+Integer.valueOf(memID)+")";
						stmt = dbconn.createStatement();
						answer = stmt.executeQuery(query);
						System.out.println("Sale Inserted Successfully");
						stmt.close();
						
						// insert new rewards earned from sale
						
						stmt = dbconn.createStatement();
						double newPointTotal = Math.floor(newTotal) + currPoints;
						String newPointQuery = "update members set points=" + newPointTotal + " where memid=" + memID;
						System.out.println("Rewards Balance: " + newPointTotal);
						stmt.executeQuery(newPointQuery);
						stmt.close();
						
						
						
					}
				}
				catch (SQLException e) {
					
			        System.err.println("*** SQLException:  "
			            + "Could not fetch query results.");
			        System.err.println("\tMessage:   " + e.getMessage());
			        System.err.println("\tSQLState:  " + e.getSQLState());
			        System.err.println("\tErrorCode: " + e.getErrorCode());
			        System.exit(-1);
			
			}
				
				
				
			}
			else {
				System.out.println("Wrong Format Entered;");
			}
			System.out.println("Enter next query");
		}

		try {
			dbconn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println();
		System.out.println("System quit.");
		sc.close();
	}
}
