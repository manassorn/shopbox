CREATE TABLE ReturnBill (
		Id integer primary key autoincrement,
		SellBillId integer,
		CreatedTime text,
		Total real default 0
);

UPDATE SQLITE_SEQUENCE SET seq = 10000000 WHERE name = 'Bill';
UPDATE SQLITE_SEQUENCE SET seq = 20000000 WHERE name = 'ReturnBill';