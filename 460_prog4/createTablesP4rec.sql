create table Members (
	memID integer,
	firstName varchar2(20),
	lastName varchar2(20),
	DOB date,
	address varchar2(100),
	phone varchar2(15),
	points integer,
	primary key (memID)
);

create table Sale (
	saleID integer,
	saleDate date,
	payMethod varchar2(4),
	totalPrice float,
	memID integer,
	primary key (saleID)
);

create table Subsale (
	subsaleID integer,
	prodID integer,
	saleID integer,
	price float,
	amount integer,
	primary key (subsaleID)
);

create table Products (
	prodID integer,
	name varchar2(20),
	retailPrice float,
	category varchar2(15),
	memDisc float,
	stock integer,
	primary key (prodID)
);

create table Warehouse (
	supID integer,
	prodID integer,
	incomDate date,
	purchasePrice float,
	amount integer
);

create table Suppliers (
	supID integer,
	name varchar2(20),
	address varchar2(100),
	contactPerson varchar2(20),
	primary key (supID)
);

create table Employees (
	empID integer,
	firstName varchar2(20),
	lastName varchar2(20),
	gender varchar2(20),
	address varchar2(100),
	phone varchar2(15),
	empGroup varchar2(7),
	salary float,
	primary key (empID)
);
