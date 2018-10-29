/*
	Still need to finish creating all the foreign key constraints.
	And we might want to add NOT NULL to a few more of the attributes.
*/

CREATE TABLE Entertainment(
	eid NUMBER PRIMARY KEY,
	title VARCHAR2(64),
	release_date TIMESTAMP,
	genre VARCHAR2(64),
	num_in_stock NUMBER NOT NULL,
)

CREATE TABLE Movies(
	eid NUMBER PRIMARY KEY,
	awards_won NUMBER,
	sequal_id NUMBER,
)

CREATE TABLE Games(
	eid NUMBER PRIMARY KEY,
	platform VARCHAR2(64),
	version NUMBER,
)

CREATE TABLE Rent_History(
	rid NUMBER PRIMARY KEY,
	eid NUMBER,
	uid NUMBER,
	time_rented TIMESTAMP,
	time_returned TIMESTAMP,
)

CREATE TABLE Users(
	email VARCHAR2(128),
	name VARCHAR2(32),
	pass VARCHAR2(32),
	phone NUMBER,
	aid NUMBER,
	is_admin NUMBER,
	level_id NUMBER,
)

CREATE TABLE Sub_Plan(
	level_id NUMBER PRIMARY KEY,
	total_quota NUMBER NOT NULL,
)

CREATE TABLE Worked_In(
	cid NUMBER,
	eid NUMBER,
	CONSTRAINT PRIMARY KEY(cid,eid),
)

CREATE TABLE Address(
	aid NUMBER PRIMARY KEY,
	state VARCHAR2(2) NOT NULL,
	city VARCHAR2(32) NOT NULL,
	street VARCHAR2(64) NOT NULL,
	zip NUMBER NOT NULL,
)