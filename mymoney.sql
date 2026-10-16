BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS "budgets" (
	"bid"	INTEGER,
	"catid"	INTEGER NOT NULL DEFAULT 0,
	"amount"	numeric NOT NULL DEFAULT 0,
	"period"	TEXT DEFAULT ' ',
	"start_date"	TEXT NOT NULL DEFAULT ' ',
	"end_date"	TEXT NOT NULL DEFAULT ' ',
	"createdat"	TEXT DEFAULT ' ',
	"spent"	NUMERIC DEFAULT 0,
	PRIMARY KEY("bid" AUTOINCREMENT)
);
CREATE TABLE IF NOT EXISTS "categories" (
	"cid"	INTEGER,
	"cname"	TEXT DEFAULT ' ',
	"ctype"	TEXT DEFAULT ' ',
	PRIMARY KEY("cid" AUTOINCREMENT)
);
CREATE TABLE IF NOT EXISTS "payments" (
	"pid"	INTEGER,
	"name"	TEXT DEFAULT ' ',
	PRIMARY KEY("pid" AUTOINCREMENT)
);
CREATE TABLE IF NOT EXISTS "subcategories" (
	"scid"	INTEGER,
	"sname"	TEXT DEFAULT ' ',
	"stype"	TEXT DEFAULT ' ',
	"cid"	INTEGER DEFAULT 0,
	PRIMARY KEY("scid" AUTOINCREMENT)
);
CREATE TABLE IF NOT EXISTS "transactions" (
	"tid"	INTEGER,
	"date"	TEXT DEFAULT ' ',
	"type"	TEXT DEFAULT ' ',
	"catid"	INTEGER DEFAULT 0,
	"description"	TEXT DEFAULT ' ',
	"pid"	INTEGER DEFAULT 0,
	"amount"	REAL DEFAULT 0,
	"note"	TEXT DEFAULT ' ',
	PRIMARY KEY("tid" AUTOINCREMENT)
);
INSERT INTO "budgets" ("bid","catid","amount","period","start_date","end_date","createdat","spent") VALUES (12,81,1000,'Monthly','2026-01-01','2026-01-31','2026-07-04',2000);
INSERT INTO "budgets" ("bid","catid","amount","period","start_date","end_date","createdat","spent") VALUES (13,97,1000,'Monthly','2026-01-01','2026-01-31','2026-07-04','');
INSERT INTO "budgets" ("bid","catid","amount","period","start_date","end_date","createdat","spent") VALUES (14,98,1000,'Monthly','2026-09-01','2026-09-30','2026-07-05',1000);
INSERT INTO "budgets" ("bid","catid","amount","period","start_date","end_date","createdat","spent") VALUES (15,97,1000,'Monthly','2026-10-01','2026-10-31','2026-07-05',500);
INSERT INTO "budgets" ("bid","catid","amount","period","start_date","end_date","createdat","spent") VALUES (16,107,2000,'Monthly','2026-09-01','2026-09-30','2026-07-05',1000);
INSERT INTO "categories" ("cid","cname","ctype") VALUES (1,'Education','E');
INSERT INTO "categories" ("cid","cname","ctype") VALUES (2,'Shopping','E');
INSERT INTO "categories" ("cid","cname","ctype") VALUES (3,'Food & Dining','E');
INSERT INTO "categories" ("cid","cname","ctype") VALUES (4,'Health & Fitness','E');
INSERT INTO "categories" ("cid","cname","ctype") VALUES (9,'Income categories','I');
INSERT INTO "categories" ("cid","cname","ctype") VALUES (10,'Entertaintment','E');
INSERT INTO "categories" ("cid","cname","ctype") VALUES (11,'Other expenses','E');
INSERT INTO "categories" ("cid","cname","ctype") VALUES (12,'Travel','E');
INSERT INTO "categories" ("cid","cname","ctype") VALUES (13,'Transport & auto','E');
INSERT INTO "categories" ("cid","cname","ctype") VALUES (14,'Bills & utilities','E');
INSERT INTO "categories" ("cid","cname","ctype") VALUES (15,'Gifts & donations','E');
INSERT INTO "categories" ("cid","cname","ctype") VALUES (16,'Home','E');
INSERT INTO "categories" ("cid","cname","ctype") VALUES (18,'Eating-out','E');
INSERT INTO "categories" ("cid","cname","ctype") VALUES (20,'Children','E');
INSERT INTO "categories" ("cid","cname","ctype") VALUES (21,'Bank','E');
INSERT INTO "categories" ("cid","cname","ctype") VALUES (22,'Household','E');
INSERT INTO "payments" ("pid","name") VALUES (1,'Payment Method');
INSERT INTO "payments" ("pid","name") VALUES (2,'Cash');
INSERT INTO "payments" ("pid","name") VALUES (3,'Credit Card');
INSERT INTO "payments" ("pid","name") VALUES (4,'Debit Card');
INSERT INTO "payments" ("pid","name") VALUES (5,'Bank Transfer');
INSERT INTO "payments" ("pid","name") VALUES (6,'Check');
INSERT INTO "payments" ("pid","name") VALUES (7,'Mobile Payment');
INSERT INTO "payments" ("pid","name") VALUES (8,'PayPal');
INSERT INTO "payments" ("pid","name") VALUES (9,'Venmo');
INSERT INTO "payments" ("pid","name") VALUES (10,'Bitcoin/Cryptocurrency');
INSERT INTO "payments" ("pid","name") VALUES (11,'Gift Card');
INSERT INTO "payments" ("pid","name") VALUES (12,'Store Credit');
INSERT INTO "payments" ("pid","name") VALUES (13,'Other');
INSERT INTO "subcategories" ("scid","sname","stype","cid") VALUES (28,'Basic Salary','I',9);
INSERT INTO "subcategories" ("scid","sname","stype","cid") VALUES (29,'Overtime','I',9);
INSERT INTO "subcategories" ("scid","sname","stype","cid") VALUES (30,'Bonus, Commission','I',9);
INSERT INTO "subcategories" ("scid","sname","stype","cid") VALUES (31,'Allowances','I',9);
INSERT INTO "subcategories" ("scid","sname","stype","cid") VALUES (32,'Rental Income','I',9);
INSERT INTO "subcategories" ("scid","sname","stype","cid") VALUES (33,'Investment Income','I',9);
INSERT INTO "subcategories" ("scid","sname","stype","cid") VALUES (34,'Freelance Work','I',9);
INSERT INTO "subcategories" ("scid","sname","stype","cid") VALUES (35,'Business Income','I',9);
INSERT INTO "subcategories" ("scid","sname","stype","cid") VALUES (36,'Retirement Benefits','I',9);
INSERT INTO "subcategories" ("scid","sname","stype","cid") VALUES (37,'Other income','I',9);
INSERT INTO "subcategories" ("scid","sname","stype","cid") VALUES (38,'Rent/Mortgage','E',16);
INSERT INTO "subcategories" ("scid","sname","stype","cid") VALUES (39,'Electricity','E',14);
INSERT INTO "subcategories" ("scid","sname","stype","cid") VALUES (40,'Water','E',16);
INSERT INTO "subcategories" ("scid","sname","stype","cid") VALUES (41,'Gas','E',16);
INSERT INTO "subcategories" ("scid","sname","stype","cid") VALUES (44,'Transportation','E',13);
INSERT INTO "subcategories" ("scid","sname","stype","cid") VALUES (45,'Fuel','E',13);
INSERT INTO "subcategories" ("scid","sname","stype","cid") VALUES (46,'Public Transport','E',13);
INSERT INTO "subcategories" ("scid","sname","stype","cid") VALUES (47,'Health Insurance','E',4);
INSERT INTO "subcategories" ("scid","sname","stype","cid") VALUES (48,'Life Insurance','E',4);
INSERT INTO "subcategories" ("scid","sname","stype","cid") VALUES (49,'Vehicle Insurance','E',13);
INSERT INTO "subcategories" ("scid","sname","stype","cid") VALUES (51,'Medications','E',4);
INSERT INTO "subcategories" ("scid","sname","stype","cid") VALUES (54,'Shopping Clothing','E',2);
INSERT INTO "subcategories" ("scid","sname","stype","cid") VALUES (55,'Shopping Accessories','E',2);
INSERT INTO "subcategories" ("scid","sname","stype","cid") VALUES (57,'Accommodation','E',12);
INSERT INTO "subcategories" ("scid","sname","stype","cid") VALUES (58,'Debt Repayment','E',21);
INSERT INTO "subcategories" ("scid","sname","stype","cid") VALUES (59,'Savings/Investments','E',21);
INSERT INTO "subcategories" ("scid","sname","stype","cid") VALUES (73,'Insurance','E',14);
INSERT INTO "subcategories" ("scid","sname","stype","cid") VALUES (74,'Internet','E',14);
INSERT INTO "subcategories" ("scid","sname","stype","cid") VALUES (75,'Phone','E',14);
INSERT INTO "subcategories" ("scid","sname","stype","cid") VALUES (76,'Water','E',14);
INSERT INTO "subcategories" ("scid","sname","stype","cid") VALUES (77,'Games','E',10);
INSERT INTO "subcategories" ("scid","sname","stype","cid") VALUES (78,'Movies','E',10);
INSERT INTO "subcategories" ("scid","sname","stype","cid") VALUES (79,'Music','E',10);
INSERT INTO "subcategories" ("scid","sname","stype","cid") VALUES (80,'Sports','E',10);
INSERT INTO "subcategories" ("scid","sname","stype","cid") VALUES (81,'Fast food','E',3);
INSERT INTO "subcategories" ("scid","sname","stype","cid") VALUES (82,'Restaurants','E',3);
INSERT INTO "subcategories" ("scid","sname","stype","cid") VALUES (83,'Groceries','E',3);
INSERT INTO "subcategories" ("scid","sname","stype","cid") VALUES (84,'Elecronics','E',2);
INSERT INTO "subcategories" ("scid","sname","stype","cid") VALUES (85,'General merchandise','E',2);
INSERT INTO "subcategories" ("scid","sname","stype","cid") VALUES (86,'Doctor','E',4);
INSERT INTO "subcategories" ("scid","sname","stype","cid") VALUES (87,'Gym','E',4);
INSERT INTO "subcategories" ("scid","sname","stype","cid") VALUES (88,'Pharmacy','E',4);
INSERT INTO "subcategories" ("scid","sname","stype","cid") VALUES (89,'Car maintenance','E',13);
INSERT INTO "subcategories" ("scid","sname","stype","cid") VALUES (90,'Gas & fuels','E',13);
INSERT INTO "subcategories" ("scid","sname","stype","cid") VALUES (91,'Parking','E',13);
INSERT INTO "subcategories" ("scid","sname","stype","cid") VALUES (92,'Public transportation','E',13);
INSERT INTO "subcategories" ("scid","sname","stype","cid") VALUES (93,'Flights','E',12);
INSERT INTO "subcategories" ("scid","sname","stype","cid") VALUES (94,'Hotels','E',12);
INSERT INTO "subcategories" ("scid","sname","stype","cid") VALUES (95,'Vacations','E',12);
INSERT INTO "subcategories" ("scid","sname","stype","cid") VALUES (97,'Car insurance','E',13);
INSERT INTO "subcategories" ("scid","sname","stype","cid") VALUES (98,'Daycare','E',20);
INSERT INTO "subcategories" ("scid","sname","stype","cid") VALUES (99,'Babysittering','E',20);
INSERT INTO "subcategories" ("scid","sname","stype","cid") VALUES (100,'Lessons','E',20);
INSERT INTO "subcategories" ("scid","sname","stype","cid") VALUES (101,'Sports','E',20);
INSERT INTO "subcategories" ("scid","sname","stype","cid") VALUES (102,'Activities','E',20);
INSERT INTO "subcategories" ("scid","sname","stype","cid") VALUES (103,'School fees','E',20);
INSERT INTO "subcategories" ("scid","sname","stype","cid") VALUES (104,'School supplies','E',20);
INSERT INTO "subcategories" ("scid","sname","stype","cid") VALUES (105,'Medical treatments','E',4);
INSERT INTO "subcategories" ("scid","sname","stype","cid") VALUES (106,'Dental','E',4);
INSERT INTO "subcategories" ("scid","sname","stype","cid") VALUES (107,'Optical','E',4);
INSERT INTO "subcategories" ("scid","sname","stype","cid") VALUES (108,'Prescription','E',4);
INSERT INTO "subcategories" ("scid","sname","stype","cid") VALUES (109,'Home repairs','E',22);
INSERT INTO "subcategories" ("scid","sname","stype","cid") VALUES (110,'Home improvements','E',22);
INSERT INTO "subcategories" ("scid","sname","stype","cid") VALUES (111,'Gardening supplies','E',22);
INSERT INTO "transactions" ("tid","date","type","catid","description","pid","amount","note") VALUES (10,'2026-01-31','Income',28,'Salary',5,20000.0,'Monthly salary');
INSERT INTO "transactions" ("tid","date","type","catid","description","pid","amount","note") VALUES (11,'2026-02-28','Income',28,'Salary',5,20000.0,'');
INSERT INTO "transactions" ("tid","date","type","catid","description","pid","amount","note") VALUES (12,'2026-03-31','Income',28,'Salary',5,20000.0,'');
INSERT INTO "transactions" ("tid","date","type","catid","description","pid","amount","note") VALUES (13,'2026-04-30','Income',28,'Salary',5,20000.0,'');
INSERT INTO "transactions" ("tid","date","type","catid","description","pid","amount","note") VALUES (14,'2026-05-31','Income',28,'Salary',5,20000.0,'');
INSERT INTO "transactions" ("tid","date","type","catid","description","pid","amount","note") VALUES (15,'2026-06-30','Income',28,'Salary',5,20000.0,'');
INSERT INTO "transactions" ("tid","date","type","catid","description","pid","amount","note") VALUES (16,'2026-07-31','Income',28,'Salary',5,20000.0,'');
INSERT INTO "transactions" ("tid","date","type","catid","description","pid","amount","note") VALUES (17,'2026-08-31','Income',28,'Salary',5,20000.0,'');
INSERT INTO "transactions" ("tid","date","type","catid","description","pid","amount","note") VALUES (18,'2026-09-30','Income',28,'Salary',5,20000.0,'');
INSERT INTO "transactions" ("tid","date","type","catid","description","pid","amount","note") VALUES (19,'2026-10-31','Income',28,'Salary',5,20000.0,'');
INSERT INTO "transactions" ("tid","date","type","catid","description","pid","amount","note") VALUES (20,'2026-11-30','Income',28,'Salary',5,20000.0,'');
INSERT INTO "transactions" ("tid","date","type","catid","description","pid","amount","note") VALUES (21,'2026-12-31','Income',28,'Salary',5,20000.0,'');
INSERT INTO "transactions" ("tid","date","type","catid","description","pid","amount","note") VALUES (22,'2026-01-05','Expense',81,'Fast food kecskemét',5,2000.0,'');
INSERT INTO "transactions" ("tid","date","type","catid","description","pid","amount","note") VALUES (23,'2026-01-15','Expense',39,'TV is out of order',5,5000.0,'');
INSERT INTO "transactions" ("tid","date","type","catid","description","pid","amount","note") VALUES (24,'2026-01-20','Expense',86,'Some health problems',5,6000.0,'');
INSERT INTO "transactions" ("tid","date","type","catid","description","pid","amount","note") VALUES (25,'2026-02-09','Expense',88,'Some new tablets',5,3000.0,'');
INSERT INTO "transactions" ("tid","date","type","catid","description","pid","amount","note") VALUES (26,'2026-02-20','Expense',89,'Car problems',5,2000.0,'');
INSERT INTO "transactions" ("tid","date","type","catid","description","pid","amount","note") VALUES (27,'2026-02-27','Expense',45,'Bensin',5,2000.0,'');
INSERT INTO "transactions" ("tid","date","type","catid","description","pid","amount","note") VALUES (28,'2026-03-01','Expense',83,'Fruits',5,1000.0,'');
INSERT INTO "transactions" ("tid","date","type","catid","description","pid","amount","note") VALUES (29,'2026-03-11','Expense',87,'Gym exercises',5,500.0,'');
INSERT INTO "transactions" ("tid","date","type","catid","description","pid","amount","note") VALUES (30,'2026-04-11','Expense',57,'Acc money',5,2000.0,'');
INSERT INTO "transactions" ("tid","date","type","catid","description","pid","amount","note") VALUES (31,'2026-04-15','Expense',73,'Car ',5,2000.0,'');
INSERT INTO "transactions" ("tid","date","type","catid","description","pid","amount","note") VALUES (32,'2026-01-10','Income',34,'Free work',5,5000.0,'');
INSERT INTO "transactions" ("tid","date","type","catid","description","pid","amount","note") VALUES (33,'2026-10-03','Expense',97,'Car insurance',5,500.0,'');
INSERT INTO "transactions" ("tid","date","type","catid","description","pid","amount","note") VALUES (34,'2026-05-05','Expense',90,'Gas',5,1000.0,'');
INSERT INTO "transactions" ("tid","date","type","catid","description","pid","amount","note") VALUES (35,'2026-06-05','Expense',86,'Examine',5,1000.0,'');
INSERT INTO "transactions" ("tid","date","type","catid","description","pid","amount","note") VALUES (36,'2026-07-05','Expense',103,'School',5,1000.0,'');
INSERT INTO "transactions" ("tid","date","type","catid","description","pid","amount","note") VALUES (37,'2026-08-05','Expense',89,'Car',5,1000.0,'');
INSERT INTO "transactions" ("tid","date","type","catid","description","pid","amount","note") VALUES (38,'2026-09-05','Expense',98,'My son',5,1000.0,'');
INSERT INTO "transactions" ("tid","date","type","catid","description","pid","amount","note") VALUES (39,'2026-09-10','Expense',107,'My boy',5,1000.0,'');
INSERT INTO "transactions" ("tid","date","type","catid","description","pid","amount","note") VALUES (40,'2026-10-10','Expense',108,'My wife',5,1000.0,'');
INSERT INTO "transactions" ("tid","date","type","catid","description","pid","amount","note") VALUES (41,'2026-11-10','Expense',86,'My problem',5,1000.0,'');
INSERT INTO "transactions" ("tid","date","type","catid","description","pid","amount","note") VALUES (42,'2026-12-10','Expense',104,'My daughter',5,1000.0,'');
COMMIT;
