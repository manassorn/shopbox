create table Category (
		Id integer primary key autoincrement,
		ParentId integer not null,
		Name char(50) not null
);
CREATE TABLE Product (
		Id integer primary key autoincrement,
		Name char(100) not null,
		Amount integer default 0,
		Price real default 0,
		CategoryId integer default 0,
		Barcode char(20),
		foreign key(CategoryId) references CATEGORY_TABLE_NAME (Id)
);
CREATE TABLE Supplement (
		Id integer primary key autoincrement,
		Name char(100) not null,
		Type char(8) not null,
		Percent real default 0, 
		Constant real default 0,
		Priority integer default 10
);
CREATE TABLE Bill (
		Id integer primary key autoincrement,
		CreatedTime text,  
		ReceiveMoney real default 0
);
CREATE TABLE BillProductItem (
		BillId integer not null,
		Sequence integer,
		ProductId integer not null,
		ProductName char(100) not null,
		ProductPrice real default 0,
		Amount integer not null,
		Total double not null
);
CREATE TABLE BillSupplementItem (
		BillId integer not null,
		Sequence integer,
		SupplementId integer not null,
		SupplementName char(100) not null,
		SupplementType char(8) not null,
		SupplementPercent real default 0, 
		SupplementConstant real default 0,
		Total double not null
);
CREATE TABLE BillSubTotalItem (
		BillId integer not null,
		Sequence integer,
		SubTotal double not null
);

-- CATEGORIES
INSERT INTO Category (Id, ParentId, Name)
VALUES (0, -1, 'หมวดหมู่แรก');
INSERT INTO Category (Id, ParentId, Name)
VALUES (1, 0, 'หมวดหมู่ย่อย');

-- PRODUCTS
INSERT INTO Product (Name, Price, Barcode)
VALUES ('SALE ColorPack II Polaroid Camera - Display Only', 8, '123450');
INSERT INTO Product (Name, Price, Barcode)
VALUES ('Vintage Polaroid Land 10 Instant Camera', 27, '123451');
INSERT INTO Product (Name, Price, Barcode)
VALUES ('Polaroid SX-70 One Step White Rainbow Stripe Instant Land Camera Tested & Working with Impossible Project PX 70 Color Protection Film', 89, '123452');
INSERT INTO Product (Name, Price, Barcode, CategoryId)
VALUES ('Vintage Canon A1, Flash, Teleconversion Lens, Macrolite, vintage accessories', 45, '123453', 1);
INSERT INTO Product (Name, Price, Barcode, CategoryId)
VALUES ('Olympus Trip 35', 45, '123454', 1);

-- SUPPLEMENTS
INSERT INTO Supplement (Name, Type, Percent, Priority)
VALUES ('ภาษี', 'PERCENT', 7, 99);
INSERT INTO Supplement (Name, Type, Percent, Priority)
VALUES ('ส่วนลดสมาชิก', 'PERCENT', -10, 10);
INSERT INTO Supplement (Name, Type, Percent, Priority)
VALUES ('ส่วนลดสมาชิก VIP', 'PERCENT', -20, 10);
INSERT INTO Supplement (Name, Type, Percent, Priority)
VALUES ('ค่าบริการ', 'PERCENT', 10, 9);
INSERT INTO Supplement (Name, Type, Constant, Priority)
VALUES ('ส่วนลดคูปอง', 'CONSTANT', -10, 20);

