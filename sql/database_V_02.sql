CREATE TABLE Entertainment(
	eid INTEGER PRIMARY KEY,
	title VARCHAR(64) NOT NULL,
	release_date TIMESTAMP,
	genre VARCHAR(64) NOT NULL,
	num_in_stock INTEGER NOT NULL
);

CREATE TABLE Address(
	aid INTEGER PRIMARY KEY,
	state VARCHAR(2) NOT NULL,
	city VARCHAR(32) NOT NULL,
	street VARCHAR(64) NOT NULL,
	zip INTEGER NOT NULL
);

CREATE TABLE Sub_Plan(
	level_id INTEGER PRIMARY KEY,
	total_quota INTEGER NOT NULL
);

CREATE TABLE Movies(
	eid INTEGER PRIMARY KEY,
	awards_won INTEGER,
	sequal_id INTEGER,
    FOREIGN KEY (eid) REFERENCES Entertainment(eid) ON DELETE CASCADE
);

CREATE TABLE Games(
	eid INTEGER PRIMARY KEY,
	platform VARCHAR(64) NOT NULL,
	version FLOAT,
    FOREIGN KEY (eid) REFERENCES Entertainment(eid) ON DELETE CASCADE
);

CREATE TABLE Users(
	user_email VARCHAR(128) PRIMARY KEY,
	name VARCHAR(32) NOT NULL,
	pass VARCHAR(32) NOT NULL,
	phone INTEGER,
	aid INTEGER	NOT NULL,
	is_admin BOOLEAN NOT NULL,
	level_id INTEGER NOT NULL,
    FOREIGN KEY (aid) REFERENCES Address(aid),
    FOREIGN KEY (level_id) REFERENCES Sub_Plan(level_id)
);

CREATE TABLE Rent_History(
	rid INTEGER PRIMARY KEY,
	eid INTEGER NOT NULL,
	user_email VARCHAR(128) NOT NULL,
	time_rented TIMESTAMP NOT NULL,
	time_returned TIMESTAMP,
    FOREIGN KEY (eid) REFERENCES Entertainment(eid) ON DELETE NO ACTION,
    FOREIGN KEY (user_email) REFERENCES Users(user_email) ON DELETE NO ACTION
);

CREATE TABLE Cast_Member(
	cid INTEGER PRIMARY KEY,
    name VARCHAR(32) NOT NULL,
    aid INTEGER NOT NULL,
    is_director BOOLEAN NOT NULL,
    FOREIGN KEY (aid) REFERENCES Address(aid)
);

CREATE TABLE Worked_In(
	cid INTEGER,
	eid INTEGER,
	PRIMARY KEY(cid,eid),
    FOREIGN KEY (cid) REFERENCES Cast_Member(cid) ON DELETE CASCADE,
    FOREIGN KEY (eid) REFERENCES Movies(eid) ON DELETE CASCADE
);