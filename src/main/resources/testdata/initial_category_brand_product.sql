
INSERT INTO Category (name, parent_id) VALUES ('Root', NULL);

INSERT INTO Category (name, parent_id) VALUES
('Category 1', (SELECT category_id FROM Category WHERE name = 'Root')),
('Category 2', (SELECT category_id FROM Category WHERE name = 'Root')),
('Category 3', (SELECT category_id FROM Category WHERE name = 'Root')),
('Category 4', (SELECT category_id FROM Category WHERE name = 'Root')),
('Category 5', (SELECT category_id FROM Category WHERE name = 'Root'));

INSERT INTO Category (name, parent_id) VALUES
('Category 1 - SUBCATEGORY 1', (SELECT category_id FROM Category WHERE name = 'Category 1')),
('Category 1 - SUBCATEGORY 2', (SELECT category_id FROM Category WHERE name = 'Category 1')),
('Category 1 - SUBCATEGORY 3', (SELECT category_id FROM Category WHERE name = 'Category 1')),
('Category 1 - SUBCATEGORY 4', (SELECT category_id FROM Category WHERE name = 'Category 1')),
('Category 1 - SUBCATEGORY 5', (SELECT category_id FROM Category WHERE name = 'Category 1'));
INSERT INTO Category (name, parent_id) VALUES
('Category 2 - SUBCATEGORY 1', (SELECT category_id FROM Category WHERE name = 'Category 2')),
('Category 2 - SUBCATEGORY 2', (SELECT category_id FROM Category WHERE name = 'Category 2')),
('Category 2 - SUBCATEGORY 3', (SELECT category_id FROM Category WHERE name = 'Category 2')),
('Category 2 - SUBCATEGORY 4', (SELECT category_id FROM Category WHERE name = 'Category 2')),
('Category 2 - SUBCATEGORY 5', (SELECT category_id FROM Category WHERE name = 'Category 2'));
INSERT INTO Category (name, parent_id) VALUES
('Category 3 - SUBCATEGORY 1', (SELECT category_id FROM Category WHERE name = 'Category 3')),
('Category 3 - SUBCATEGORY 2', (SELECT category_id FROM Category WHERE name = 'Category 3')),
('Category 3 - SUBCATEGORY 3', (SELECT category_id FROM Category WHERE name = 'Category 3')),
('Category 3 - SUBCATEGORY 4', (SELECT category_id FROM Category WHERE name = 'Category 3')),
('Category 3 - SUBCATEGORY 5', (SELECT category_id FROM Category WHERE name = 'Category 3'));
INSERT INTO Category (name, parent_id) VALUES
('Category 4 - SUBCATEGORY 1', (SELECT category_id FROM Category WHERE name = 'Category 4')),
('Category 4 - SUBCATEGORY 2', (SELECT category_id FROM Category WHERE name = 'Category 4')),
('Category 4 - SUBCATEGORY 3', (SELECT category_id FROM Category WHERE name = 'Category 4')),
('Category 4 - SUBCATEGORY 4', (SELECT category_id FROM Category WHERE name = 'Category 4')),
('Category 4 - SUBCATEGORY 5', (SELECT category_id FROM Category WHERE name = 'Category 4'));
INSERT INTO Category (name, parent_id) VALUES
('Category 5 - SUBCATEGORY 1', (SELECT category_id FROM Category WHERE name = 'Category 5')),
('Category 5 - SUBCATEGORY 2', (SELECT category_id FROM Category WHERE name = 'Category 5')),
('Category 5 - SUBCATEGORY 3', (SELECT category_id FROM Category WHERE name = 'Category 5')),
('Category 5 - SUBCATEGORY 4', (SELECT category_id FROM Category WHERE name = 'Category 5')),
('Category 5 - SUBCATEGORY 5', (SELECT category_id FROM Category WHERE name = 'Category 5'));

INSERT INTO Brand (name, category_id) VALUES
('Brand 7-1', 7),
('Brand 7-2', 7),
('Brand 7-3', 7),
('Brand 7-4', 7),
('Brand 7-5', 7);

INSERT INTO Brand (name, category_id) VALUES
('Brand 8-1', 8),
('Brand 8-2', 8),
('Brand 8-3', 8),
('Brand 8-4', 8),
('Brand 8-5', 8);

INSERT INTO Brand (name, category_id) VALUES
('Brand 9-1', 9),
('Brand 9-2', 9),
('Brand 9-3', 9),
('Brand 9-4', 9),
('Brand 9-5', 9);

INSERT INTO Brand (name, category_id) VALUES
('Brand 10-1', 10),
('Brand 10-2', 10),
('Brand 10-3', 10),
('Brand 10-4', 10),
('Brand 10-5', 10);

INSERT INTO Brand (name, category_id) VALUES
('Brand 11-1', 11),
('Brand 11-2', 11),
('Brand 11-3', 11),
('Brand 11-4', 11),
('Brand 11-5', 11);

INSERT INTO Brand (name, category_id) VALUES
('Brand 12-1', 12),
('Brand 12-2', 12),
('Brand 12-3', 12),
('Brand 12-4', 12),
('Brand 12-5', 12);

INSERT INTO Brand (name, category_id) VALUES
('Brand 13-1', 13),
('Brand 13-2', 13),
('Brand 13-3', 13),
('Brand 13-4', 13),
('Brand 13-5', 13);

INSERT INTO Brand (name, category_id) VALUES
('Brand 14-1', 14),
('Brand 14-2', 14),
('Brand 14-3', 14),
('Brand 14-4', 14),
('Brand 14-5', 14);

INSERT INTO Brand (name, category_id) VALUES
('Brand 15-1', 15),
('Brand 15-2', 15),
('Brand 15-3', 15),
('Brand 15-4', 15),
('Brand 15-5', 15);

INSERT INTO Brand (name, category_id) VALUES
('Brand 16-1', 16),
('Brand 16-2', 16),
('Brand 16-3', 16),
('Brand 16-4', 16),
('Brand 16-5', 16);

INSERT INTO Brand (name, category_id) VALUES
('Brand 17-1', 17),
('Brand 17-2', 17),
('Brand 17-3', 17),
('Brand 17-4', 17),
('Brand 17-5', 17);

INSERT INTO Brand (name, category_id) VALUES
('Brand 18-1', 18),
('Brand 18-2', 18),
('Brand 18-3', 18),
('Brand 18-4', 18),
('Brand 18-5', 18);

INSERT INTO Brand (name, category_id) VALUES
('Brand 19-1', 19),
('Brand 19-2', 19),
('Brand 19-3', 19),
('Brand 19-4', 19),
('Brand 19-5', 19);

INSERT INTO Brand (name, category_id) VALUES
('Brand 20-1', 20),
('Brand 20-2', 20),
('Brand 20-3', 20),
('Brand 20-4', 20),
('Brand 20-5', 20);

INSERT INTO Brand (name, category_id) VALUES
('Brand 21-1', 21),
('Brand 21-2', 21),
('Brand 21-3', 21),
('Brand 21-4', 21),
('Brand 21-5', 21);

INSERT INTO Brand (name, category_id) VALUES
('Brand 22-1', 22),
('Brand 22-2', 22),
('Brand 22-3', 22),
('Brand 22-4', 22),
('Brand 22-5', 22);

INSERT INTO Brand (name, category_id) VALUES
('Brand 23-1', 23),
('Brand 23-2', 23),
('Brand 23-3', 23),
('Brand 23-4', 23),
('Brand 23-5', 23);

INSERT INTO Brand (name, category_id) VALUES
('Brand 24-1', 24),
('Brand 24-2', 24),
('Brand 24-3', 24),
('Brand 24-4', 24),
('Brand 24-5', 24);

INSERT INTO Brand (name, category_id) VALUES
('Brand 25-1', 25),
('Brand 25-2', 25),
('Brand 25-3', 25),
('Brand 25-4', 25),
('Brand 25-5', 25);

INSERT INTO Brand (name, category_id) VALUES
('Brand 26-1', 26),
('Brand 26-2', 26),
('Brand 26-3', 26),
('Brand 26-4', 26),
('Brand 26-5', 26);

INSERT INTO Brand (name, category_id) VALUES
('Brand 27-1', 27),
('Brand 27-2', 27),
('Brand 27-3', 27),
('Brand 27-4', 27),
('Brand 27-5', 27);

INSERT INTO Brand (name, category_id) VALUES
('Brand 28-1', 28),
('Brand 28-2', 28),
('Brand 28-3', 28),
('Brand 28-4', 28),
('Brand 28-5', 28);

INSERT INTO Brand (name, category_id) VALUES
('Brand 29-1', 29),
('Brand 29-2', 29),
('Brand 29-3', 29),
('Brand 29-4', 29),
('Brand 29-5', 29);

INSERT INTO Brand (name, category_id) VALUES
('Brand 30-1', 30),
('Brand 30-2', 30),
('Brand 30-3', 30),
('Brand 30-4', 30),
('Brand 30-5', 30);

INSERT INTO Brand (name, category_id) VALUES
('Brand 31-1', 31),
('Brand 31-2', 31),
('Brand 31-3', 31),
('Brand 31-4', 31),
('Brand 31-5', 31);


-- Inserting Products for Brands in lowest level Categories
-- Inserting Products for Brand ID 1
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 1-1', 1000, 'type', 1),
('Product 1-2', 1000, 'type', 1),
('Product 1-3', 1000, 'type', 1),
('Product 1-4', 1000, 'type', 1),
('Product 1-5', 1000, 'type', 1),
('Product 1-6', 1000, 'type', 1),
('Product 1-7', 1000, 'type', 1),
('Product 1-8', 1000, 'type', 1),
('Product 1-9', 1000, 'type', 1),
('Product 1-10', 1000, 'type', 1);

-- Inserting Products for Brand ID 2
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 2-1', 1000, 'type', 2),
('Product 2-2', 1000, 'type', 2),
('Product 2-3', 1000, 'type', 2),
('Product 2-4', 1000, 'type', 2),
('Product 2-5', 1000, 'type', 2),
('Product 2-6', 1000, 'type', 2),
('Product 2-7', 1000, 'type', 2),
('Product 2-8', 1000, 'type', 2),
('Product 2-9', 1000, 'type', 2),
('Product 2-10', 1000, 'type', 2);

-- Inserting Products for Brand ID 3
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 3-1', 1000, 'type', 3),
('Product 3-2', 1000, 'type', 3),
('Product 3-3', 1000, 'type', 3),
('Product 3-4', 1000, 'type', 3),
('Product 3-5', 1000, 'type', 3),
('Product 3-6', 1000, 'type', 3),
('Product 3-7', 1000, 'type', 3),
('Product 3-8', 1000, 'type', 3),
('Product 3-9', 1000, 'type', 3),
('Product 3-10', 1000, 'type', 3);

-- Inserting Products for Brand ID 4
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 4-1', 1000, 'type', 4),
('Product 4-2', 1000, 'type', 4),
('Product 4-3', 1000, 'type', 4),
('Product 4-4', 1000, 'type', 4),
('Product 4-5', 1000, 'type', 4),
('Product 4-6', 1000, 'type', 4),
('Product 4-7', 1000, 'type', 4),
('Product 4-8', 1000, 'type', 4),
('Product 4-9', 1000, 'type', 4),
('Product 4-10', 1000, 'type', 4);

-- Inserting Products for Brand ID 5
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 5-1', 1000, 'type', 5),
('Product 5-2', 1000, 'type', 5),
('Product 5-3', 1000, 'type', 5),
('Product 5-4', 1000, 'type', 5),
('Product 5-5', 1000, 'type', 5),
('Product 5-6', 1000, 'type', 5),
('Product 5-7', 1000, 'type', 5),
('Product 5-8', 1000, 'type', 5),
('Product 5-9', 1000, 'type', 5),
('Product 5-10', 1000, 'type', 5);

-- Inserting Products for Brand ID 6
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 6-1', 1000, 'type', 6),
('Product 6-2', 1000, 'type', 6),
('Product 6-3', 1000, 'type', 6),
('Product 6-4', 1000, 'type', 6),
('Product 6-5', 1000, 'type', 6),
('Product 6-6', 1000, 'type', 6),
('Product 6-7', 1000, 'type', 6),
('Product 6-8', 1000, 'type', 6),
('Product 6-9', 1000, 'type', 6),
('Product 6-10', 1000, 'type', 6);

-- Inserting Products for Brand ID 7
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 7-1', 1000, 'type', 7),
('Product 7-2', 1000, 'type', 7),
('Product 7-3', 1000, 'type', 7),
('Product 7-4', 1000, 'type', 7),
('Product 7-5', 1000, 'type', 7),
('Product 7-6', 1000, 'type', 7),
('Product 7-7', 1000, 'type', 7),
('Product 7-8', 1000, 'type', 7),
('Product 7-9', 1000, 'type', 7),
('Product 7-10', 1000, 'type', 7);

-- Inserting Products for Brand ID 8
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 8-1', 1000, 'type', 8),
('Product 8-2', 1000, 'type', 8),
('Product 8-3', 1000, 'type', 8),
('Product 8-4', 1000, 'type', 8),
('Product 8-5', 1000, 'type', 8),
('Product 8-6', 1000, 'type', 8),
('Product 8-7', 1000, 'type', 8),
('Product 8-8', 1000, 'type', 8),
('Product 8-9', 1000, 'type', 8),
('Product 8-10', 1000, 'type', 8);

-- Inserting Products for Brand ID 9
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 9-1', 1000, 'type', 9),
('Product 9-2', 1000, 'type', 9),
('Product 9-3', 1000, 'type', 9),
('Product 9-4', 1000, 'type', 9),
('Product 9-5', 1000, 'type', 9),
('Product 9-6', 1000, 'type', 9),
('Product 9-7', 1000, 'type', 9),
('Product 9-8', 1000, 'type', 9),
('Product 9-9', 1000, 'type', 9),
('Product 9-10', 1000, 'type', 9);

-- Inserting Products for Brand ID 10
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 10-1', 1000, 'type', 10),
('Product 10-2', 1000, 'type', 10),
('Product 10-3', 1000, 'type', 10),
('Product 10-4', 1000, 'type', 10),
('Product 10-5', 1000, 'type', 10),
('Product 10-6', 1000, 'type', 10),
('Product 10-7', 1000, 'type', 10),
('Product 10-8', 1000, 'type', 10),
('Product 10-9', 1000, 'type', 10),
('Product 10-10', 1000, 'type', 10);

-- Inserting Products for Brand ID 11
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 11-1', 1000, 'type', 11),
('Product 11-2', 1000, 'type', 11),
('Product 11-3', 1000, 'type', 11),
('Product 11-4', 1000, 'type', 11),
('Product 11-5', 1000, 'type', 11),
('Product 11-6', 1000, 'type', 11),
('Product 11-7', 1000, 'type', 11),
('Product 11-8', 1000, 'type', 11),
('Product 11-9', 1000, 'type', 11),
('Product 11-10', 1000, 'type', 11);

-- Inserting Products for Brand ID 12
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 12-1', 1000, 'type', 12),
('Product 12-2', 1000, 'type', 12),
('Product 12-3', 1000, 'type', 12),
('Product 12-4', 1000, 'type', 12),
('Product 12-5', 1000, 'type', 12),
('Product 12-6', 1000, 'type', 12),
('Product 12-7', 1000, 'type', 12),
('Product 12-8', 1000, 'type', 12),
('Product 12-9', 1000, 'type', 12),
('Product 12-10', 1000, 'type', 12);

-- Inserting Products for Brand ID 13
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 13-1', 1000, 'type', 13),
('Product 13-2', 1000, 'type', 13),
('Product 13-3', 1000, 'type', 13),
('Product 13-4', 1000, 'type', 13),
('Product 13-5', 1000, 'type', 13),
('Product 13-6', 1000, 'type', 13),
('Product 13-7', 1000, 'type', 13),
('Product 13-8', 1000, 'type', 13),
('Product 13-9', 1000, 'type', 13),
('Product 13-10', 1000, 'type', 13);

-- Inserting Products for Brand ID 14
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 14-1', 1000, 'type', 14),
('Product 14-2', 1000, 'type', 14),
('Product 14-3', 1000, 'type', 14),
('Product 14-4', 1000, 'type', 14),
('Product 14-5', 1000, 'type', 14),
('Product 14-6', 1000, 'type', 14),
('Product 14-7', 1000, 'type', 14),
('Product 14-8', 1000, 'type', 14),
('Product 14-9', 1000, 'type', 14),
('Product 14-10', 1000, 'type', 14);

-- Inserting Products for Brand ID 15
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 15-1', 1000, 'type', 15),
('Product 15-2', 1000, 'type', 15),
('Product 15-3', 1000, 'type', 15),
('Product 15-4', 1000, 'type', 15),
('Product 15-5', 1000, 'type', 15),
('Product 15-6', 1000, 'type', 15),
('Product 15-7', 1000, 'type', 15),
('Product 15-8', 1000, 'type', 15),
('Product 15-9', 1000, 'type', 15),
('Product 15-10', 1000, 'type', 15);

-- Inserting Products for Brand ID 16
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 16-1', 1000, 'type', 16),
('Product 16-2', 1000, 'type', 16),
('Product 16-3', 1000, 'type', 16),
('Product 16-4', 1000, 'type', 16),
('Product 16-5', 1000, 'type', 16),
('Product 16-6', 1000, 'type', 16),
('Product 16-7', 1000, 'type', 16),
('Product 16-8', 1000, 'type', 16),
('Product 16-9', 1000, 'type', 16),
('Product 16-10', 1000, 'type', 16);

-- Inserting Products for Brand ID 17
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 17-1', 1000, 'type', 17),
('Product 17-2', 1000, 'type', 17),
('Product 17-3', 1000, 'type', 17),
('Product 17-4', 1000, 'type', 17),
('Product 17-5', 1000, 'type', 17),
('Product 17-6', 1000, 'type', 17),
('Product 17-7', 1000, 'type', 17),
('Product 17-8', 1000, 'type', 17),
('Product 17-9', 1000, 'type', 17),
('Product 17-10', 1000, 'type', 17);

-- Inserting Products for Brand ID 18
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 18-1', 1000, 'type', 18),
('Product 18-2', 1000, 'type', 18),
('Product 18-3', 1000, 'type', 18),
('Product 18-4', 1000, 'type', 18),
('Product 18-5', 1000, 'type', 18),
('Product 18-6', 1000, 'type', 18),
('Product 18-7', 1000, 'type', 18),
('Product 18-8', 1000, 'type', 18),
('Product 18-9', 1000, 'type', 18),
('Product 18-10', 1000, 'type', 18);

-- Inserting Products for Brand ID 19
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 19-1', 1000, 'type', 19),
('Product 19-2', 1000, 'type', 19),
('Product 19-3', 1000, 'type', 19),
('Product 19-4', 1000, 'type', 19),
('Product 19-5', 1000, 'type', 19),
('Product 19-6', 1000, 'type', 19),
('Product 19-7', 1000, 'type', 19),
('Product 19-8', 1000, 'type', 19),
('Product 19-9', 1000, 'type', 19),
('Product 19-10', 1000, 'type', 19);

-- Inserting Products for Brand ID 20
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 20-1', 1000, 'type', 20),
('Product 20-2', 1000, 'type', 20),
('Product 20-3', 1000, 'type', 20),
('Product 20-4', 1000, 'type', 20),
('Product 20-5', 1000, 'type', 20),
('Product 20-6', 1000, 'type', 20),
('Product 20-7', 1000, 'type', 20),
('Product 20-8', 1000, 'type', 20),
('Product 20-9', 1000, 'type', 20),
('Product 20-10', 1000, 'type', 20);

-- Inserting Products for Brand ID 21
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 21-1', 1000, 'type', 21),
('Product 21-2', 1000, 'type', 21),
('Product 21-3', 1000, 'type', 21),
('Product 21-4', 1000, 'type', 21),
('Product 21-5', 1000, 'type', 21),
('Product 21-6', 1000, 'type', 21),
('Product 21-7', 1000, 'type', 21),
('Product 21-8', 1000, 'type', 21),
('Product 21-9', 1000, 'type', 21),
('Product 21-10', 1000, 'type', 21);

-- Inserting Products for Brand ID 22
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 22-1', 1000, 'type', 22),
('Product 22-2', 1000, 'type', 22),
('Product 22-3', 1000, 'type', 22),
('Product 22-4', 1000, 'type', 22),
('Product 22-5', 1000, 'type', 22),
('Product 22-6', 1000, 'type', 22),
('Product 22-7', 1000, 'type', 22),
('Product 22-8', 1000, 'type', 22),
('Product 22-9', 1000, 'type', 22),
('Product 22-10', 1000, 'type', 22);

-- Inserting Products for Brand ID 23
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 23-1', 1000, 'type', 23),
('Product 23-2', 1000, 'type', 23),
('Product 23-3', 1000, 'type', 23),
('Product 23-4', 1000, 'type', 23),
('Product 23-5', 1000, 'type', 23),
('Product 23-6', 1000, 'type', 23),
('Product 23-7', 1000, 'type', 23),
('Product 23-8', 1000, 'type', 23),
('Product 23-9', 1000, 'type', 23),
('Product 23-10', 1000, 'type', 23);

-- Inserting Products for Brand ID 24
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 24-1', 1000, 'type', 24),
('Product 24-2', 1000, 'type', 24),
('Product 24-3', 1000, 'type', 24),
('Product 24-4', 1000, 'type', 24),
('Product 24-5', 1000, 'type', 24),
('Product 24-6', 1000, 'type', 24),
('Product 24-7', 1000, 'type', 24),
('Product 24-8', 1000, 'type', 24),
('Product 24-9', 1000, 'type', 24),
('Product 24-10', 1000, 'type', 24);

-- Inserting Products for Brand ID 25
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 25-1', 1000, 'type', 25),
('Product 25-2', 1000, 'type', 25),
('Product 25-3', 1000, 'type', 25),
('Product 25-4', 1000, 'type', 25),
('Product 25-5', 1000, 'type', 25),
('Product 25-6', 1000, 'type', 25),
('Product 25-7', 1000, 'type', 25),
('Product 25-8', 1000, 'type', 25),
('Product 25-9', 1000, 'type', 25),
('Product 25-10', 1000, 'type', 25);

-- Inserting Products for Brand ID 26
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 26-1', 1000, 'type', 26),
('Product 26-2', 1000, 'type', 26),
('Product 26-3', 1000, 'type', 26),
('Product 26-4', 1000, 'type', 26),
('Product 26-5', 1000, 'type', 26),
('Product 26-6', 1000, 'type', 26),
('Product 26-7', 1000, 'type', 26),
('Product 26-8', 1000, 'type', 26),
('Product 26-9', 1000, 'type', 26),
('Product 26-10', 1000, 'type', 26);

-- Inserting Products for Brand ID 27
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 27-1', 1000, 'type', 27),
('Product 27-2', 1000, 'type', 27),
('Product 27-3', 1000, 'type', 27),
('Product 27-4', 1000, 'type', 27),
('Product 27-5', 1000, 'type', 27),
('Product 27-6', 1000, 'type', 27),
('Product 27-7', 1000, 'type', 27),
('Product 27-8', 1000, 'type', 27),
('Product 27-9', 1000, 'type', 27),
('Product 27-10', 1000, 'type', 27);

-- Inserting Products for Brand ID 28
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 28-1', 1000, 'type', 28),
('Product 28-2', 1000, 'type', 28),
('Product 28-3', 1000, 'type', 28),
('Product 28-4', 1000, 'type', 28),
('Product 28-5', 1000, 'type', 28),
('Product 28-6', 1000, 'type', 28),
('Product 28-7', 1000, 'type', 28),
('Product 28-8', 1000, 'type', 28),
('Product 28-9', 1000, 'type', 28),
('Product 28-10', 1000, 'type', 28);

-- Inserting Products for Brand ID 29
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 29-1', 1000, 'type', 29),
('Product 29-2', 1000, 'type', 29),
('Product 29-3', 1000, 'type', 29),
('Product 29-4', 1000, 'type', 29),
('Product 29-5', 1000, 'type', 29),
('Product 29-6', 1000, 'type', 29),
('Product 29-7', 1000, 'type', 29),
('Product 29-8', 1000, 'type', 29),
('Product 29-9', 1000, 'type', 29),
('Product 29-10', 1000, 'type', 29);

-- Inserting Products for Brand ID 30
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 30-1', 1000, 'type', 30),
('Product 30-2', 1000, 'type', 30),
('Product 30-3', 1000, 'type', 30),
('Product 30-4', 1000, 'type', 30),
('Product 30-5', 1000, 'type', 30),
('Product 30-6', 1000, 'type', 30),
('Product 30-7', 1000, 'type', 30),
('Product 30-8', 1000, 'type', 30),
('Product 30-9', 1000, 'type', 30),
('Product 30-10', 1000, 'type', 30);

-- Inserting Products for Brand ID 31
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 31-1', 1000, 'type', 31),
('Product 31-2', 1000, 'type', 31),
('Product 31-3', 1000, 'type', 31),
('Product 31-4', 1000, 'type', 31),
('Product 31-5', 1000, 'type', 31),
('Product 31-6', 1000, 'type', 31),
('Product 31-7', 1000, 'type', 31),
('Product 31-8', 1000, 'type', 31),
('Product 31-9', 1000, 'type', 31),
('Product 31-10', 1000, 'type', 31);

-- Inserting Products for Brand ID 32
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 32-1', 1000, 'type', 32),
('Product 32-2', 1000, 'type', 32),
('Product 32-3', 1000, 'type', 32),
('Product 32-4', 1000, 'type', 32),
('Product 32-5', 1000, 'type', 32),
('Product 32-6', 1000, 'type', 32),
('Product 32-7', 1000, 'type', 32),
('Product 32-8', 1000, 'type', 32),
('Product 32-9', 1000, 'type', 32),
('Product 32-10', 1000, 'type', 32);

-- Inserting Products for Brand ID 33
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 33-1', 1000, 'type', 33),
('Product 33-2', 1000, 'type', 33),
('Product 33-3', 1000, 'type', 33),
('Product 33-4', 1000, 'type', 33),
('Product 33-5', 1000, 'type', 33),
('Product 33-6', 1000, 'type', 33),
('Product 33-7', 1000, 'type', 33),
('Product 33-8', 1000, 'type', 33),
('Product 33-9', 1000, 'type', 33),
('Product 33-10', 1000, 'type', 33);

-- Inserting Products for Brand ID 34
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 34-1', 1000, 'type', 34),
('Product 34-2', 1000, 'type', 34),
('Product 34-3', 1000, 'type', 34),
('Product 34-4', 1000, 'type', 34),
('Product 34-5', 1000, 'type', 34),
('Product 34-6', 1000, 'type', 34),
('Product 34-7', 1000, 'type', 34),
('Product 34-8', 1000, 'type', 34),
('Product 34-9', 1000, 'type', 34),
('Product 34-10', 1000, 'type', 34);

-- Inserting Products for Brand ID 35
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 35-1', 1000, 'type', 35),
('Product 35-2', 1000, 'type', 35),
('Product 35-3', 1000, 'type', 35),
('Product 35-4', 1000, 'type', 35),
('Product 35-5', 1000, 'type', 35),
('Product 35-6', 1000, 'type', 35),
('Product 35-7', 1000, 'type', 35),
('Product 35-8', 1000, 'type', 35),
('Product 35-9', 1000, 'type', 35),
('Product 35-10', 1000, 'type', 35);

-- Inserting Products for Brand ID 36
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 36-1', 1000, 'type', 36),
('Product 36-2', 1000, 'type', 36),
('Product 36-3', 1000, 'type', 36),
('Product 36-4', 1000, 'type', 36),
('Product 36-5', 1000, 'type', 36),
('Product 36-6', 1000, 'type', 36),
('Product 36-7', 1000, 'type', 36),
('Product 36-8', 1000, 'type', 36),
('Product 36-9', 1000, 'type', 36),
('Product 36-10', 1000, 'type', 36);

-- Inserting Products for Brand ID 37
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 37-1', 1000, 'type', 37),
('Product 37-2', 1000, 'type', 37),
('Product 37-3', 1000, 'type', 37),
('Product 37-4', 1000, 'type', 37),
('Product 37-5', 1000, 'type', 37),
('Product 37-6', 1000, 'type', 37),
('Product 37-7', 1000, 'type', 37),
('Product 37-8', 1000, 'type', 37),
('Product 37-9', 1000, 'type', 37),
('Product 37-10', 1000, 'type', 37);

-- Inserting Products for Brand ID 38
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 38-1', 1000, 'type', 38),
('Product 38-2', 1000, 'type', 38),
('Product 38-3', 1000, 'type', 38),
('Product 38-4', 1000, 'type', 38),
('Product 38-5', 1000, 'type', 38),
('Product 38-6', 1000, 'type', 38),
('Product 38-7', 1000, 'type', 38),
('Product 38-8', 1000, 'type', 38),
('Product 38-9', 1000, 'type', 38),
('Product 38-10', 1000, 'type', 38);

-- Inserting Products for Brand ID 39
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 39-1', 1000, 'type', 39),
('Product 39-2', 1000, 'type', 39),
('Product 39-3', 1000, 'type', 39),
('Product 39-4', 1000, 'type', 39),
('Product 39-5', 1000, 'type', 39),
('Product 39-6', 1000, 'type', 39),
('Product 39-7', 1000, 'type', 39),
('Product 39-8', 1000, 'type', 39),
('Product 39-9', 1000, 'type', 39),
('Product 39-10', 1000, 'type', 39);

-- Inserting Products for Brand ID 40
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 40-1', 1000, 'type', 40),
('Product 40-2', 1000, 'type', 40),
('Product 40-3', 1000, 'type', 40),
('Product 40-4', 1000, 'type', 40),
('Product 40-5', 1000, 'type', 40),
('Product 40-6', 1000, 'type', 40),
('Product 40-7', 1000, 'type', 40),
('Product 40-8', 1000, 'type', 40),
('Product 40-9', 1000, 'type', 40),
('Product 40-10', 1000, 'type', 40);

-- Inserting Products for Brand ID 41
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 41-1', 1000, 'type', 41),
('Product 41-2', 1000, 'type', 41),
('Product 41-3', 1000, 'type', 41),
('Product 41-4', 1000, 'type', 41),
('Product 41-5', 1000, 'type', 41),
('Product 41-6', 1000, 'type', 41),
('Product 41-7', 1000, 'type', 41),
('Product 41-8', 1000, 'type', 41),
('Product 41-9', 1000, 'type', 41),
('Product 41-10', 1000, 'type', 41);

-- Inserting Products for Brand ID 42
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 42-1', 1000, 'type', 42),
('Product 42-2', 1000, 'type', 42),
('Product 42-3', 1000, 'type', 42),
('Product 42-4', 1000, 'type', 42),
('Product 42-5', 1000, 'type', 42),
('Product 42-6', 1000, 'type', 42),
('Product 42-7', 1000, 'type', 42),
('Product 42-8', 1000, 'type', 42),
('Product 42-9', 1000, 'type', 42),
('Product 42-10', 1000, 'type', 42);

-- Inserting Products for Brand ID 43
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 43-1', 1000, 'type', 43),
('Product 43-2', 1000, 'type', 43),
('Product 43-3', 1000, 'type', 43),
('Product 43-4', 1000, 'type', 43),
('Product 43-5', 1000, 'type', 43),
('Product 43-6', 1000, 'type', 43),
('Product 43-7', 1000, 'type', 43),
('Product 43-8', 1000, 'type', 43),
('Product 43-9', 1000, 'type', 43),
('Product 43-10', 1000, 'type', 43);

-- Inserting Products for Brand ID 44
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 44-1', 1000, 'type', 44),
('Product 44-2', 1000, 'type', 44),
('Product 44-3', 1000, 'type', 44),
('Product 44-4', 1000, 'type', 44),
('Product 44-5', 1000, 'type', 44),
('Product 44-6', 1000, 'type', 44),
('Product 44-7', 1000, 'type', 44),
('Product 44-8', 1000, 'type', 44),
('Product 44-9', 1000, 'type', 44),
('Product 44-10', 1000, 'type', 44);

-- Inserting Products for Brand ID 45
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 45-1', 1000, 'type', 45),
('Product 45-2', 1000, 'type', 45),
('Product 45-3', 1000, 'type', 45),
('Product 45-4', 1000, 'type', 45),
('Product 45-5', 1000, 'type', 45),
('Product 45-6', 1000, 'type', 45),
('Product 45-7', 1000, 'type', 45),
('Product 45-8', 1000, 'type', 45),
('Product 45-9', 1000, 'type', 45),
('Product 45-10', 1000, 'type', 45);

-- Inserting Products for Brand ID 46
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 46-1', 1000, 'type', 46),
('Product 46-2', 1000, 'type', 46),
('Product 46-3', 1000, 'type', 46),
('Product 46-4', 1000, 'type', 46),
('Product 46-5', 1000, 'type', 46),
('Product 46-6', 1000, 'type', 46),
('Product 46-7', 1000, 'type', 46),
('Product 46-8', 1000, 'type', 46),
('Product 46-9', 1000, 'type', 46),
('Product 46-10', 1000, 'type', 46);

-- Inserting Products for Brand ID 47
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 47-1', 1000, 'type', 47),
('Product 47-2', 1000, 'type', 47),
('Product 47-3', 1000, 'type', 47),
('Product 47-4', 1000, 'type', 47),
('Product 47-5', 1000, 'type', 47),
('Product 47-6', 1000, 'type', 47),
('Product 47-7', 1000, 'type', 47),
('Product 47-8', 1000, 'type', 47),
('Product 47-9', 1000, 'type', 47),
('Product 47-10', 1000, 'type', 47);

-- Inserting Products for Brand ID 48
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 48-1', 1000, 'type', 48),
('Product 48-2', 1000, 'type', 48),
('Product 48-3', 1000, 'type', 48),
('Product 48-4', 1000, 'type', 48),
('Product 48-5', 1000, 'type', 48),
('Product 48-6', 1000, 'type', 48),
('Product 48-7', 1000, 'type', 48),
('Product 48-8', 1000, 'type', 48),
('Product 48-9', 1000, 'type', 48),
('Product 48-10', 1000, 'type', 48);

-- Inserting Products for Brand ID 49
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 49-1', 1000, 'type', 49),
('Product 49-2', 1000, 'type', 49),
('Product 49-3', 1000, 'type', 49),
('Product 49-4', 1000, 'type', 49),
('Product 49-5', 1000, 'type', 49),
('Product 49-6', 1000, 'type', 49),
('Product 49-7', 1000, 'type', 49),
('Product 49-8', 1000, 'type', 49),
('Product 49-9', 1000, 'type', 49),
('Product 49-10', 1000, 'type', 49);

-- Inserting Products for Brand ID 50
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 50-1', 1000, 'type', 50),
('Product 50-2', 1000, 'type', 50),
('Product 50-3', 1000, 'type', 50),
('Product 50-4', 1000, 'type', 50),
('Product 50-5', 1000, 'type', 50),
('Product 50-6', 1000, 'type', 50),
('Product 50-7', 1000, 'type', 50),
('Product 50-8', 1000, 'type', 50),
('Product 50-9', 1000, 'type', 50),
('Product 50-10', 1000, 'type', 50);

-- Inserting Products for Brand ID 51
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 51-1', 1000, 'type', 51),
('Product 51-2', 1000, 'type', 51),
('Product 51-3', 1000, 'type', 51),
('Product 51-4', 1000, 'type', 51),
('Product 51-5', 1000, 'type', 51),
('Product 51-6', 1000, 'type', 51),
('Product 51-7', 1000, 'type', 51),
('Product 51-8', 1000, 'type', 51),
('Product 51-9', 1000, 'type', 51),
('Product 51-10', 1000, 'type', 51);

-- Inserting Products for Brand ID 52
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 52-1', 1000, 'type', 52),
('Product 52-2', 1000, 'type', 52),
('Product 52-3', 1000, 'type', 52),
('Product 52-4', 1000, 'type', 52),
('Product 52-5', 1000, 'type', 52),
('Product 52-6', 1000, 'type', 52),
('Product 52-7', 1000, 'type', 52),
('Product 52-8', 1000, 'type', 52),
('Product 52-9', 1000, 'type', 52),
('Product 52-10', 1000, 'type', 52);

-- Inserting Products for Brand ID 53
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 53-1', 1000, 'type', 53),
('Product 53-2', 1000, 'type', 53),
('Product 53-3', 1000, 'type', 53),
('Product 53-4', 1000, 'type', 53),
('Product 53-5', 1000, 'type', 53),
('Product 53-6', 1000, 'type', 53),
('Product 53-7', 1000, 'type', 53),
('Product 53-8', 1000, 'type', 53),
('Product 53-9', 1000, 'type', 53),
('Product 53-10', 1000, 'type', 53);

-- Inserting Products for Brand ID 54
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 54-1', 1000, 'type', 54),
('Product 54-2', 1000, 'type', 54),
('Product 54-3', 1000, 'type', 54),
('Product 54-4', 1000, 'type', 54),
('Product 54-5', 1000, 'type', 54),
('Product 54-6', 1000, 'type', 54),
('Product 54-7', 1000, 'type', 54),
('Product 54-8', 1000, 'type', 54),
('Product 54-9', 1000, 'type', 54),
('Product 54-10', 1000, 'type', 54);

-- Inserting Products for Brand ID 55
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 55-1', 1000, 'type', 55),
('Product 55-2', 1000, 'type', 55),
('Product 55-3', 1000, 'type', 55),
('Product 55-4', 1000, 'type', 55),
('Product 55-5', 1000, 'type', 55),
('Product 55-6', 1000, 'type', 55),
('Product 55-7', 1000, 'type', 55),
('Product 55-8', 1000, 'type', 55),
('Product 55-9', 1000, 'type', 55),
('Product 55-10', 1000, 'type', 55);

-- Inserting Products for Brand ID 56
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 56-1', 1000, 'type', 56),
('Product 56-2', 1000, 'type', 56),
('Product 56-3', 1000, 'type', 56),
('Product 56-4', 1000, 'type', 56),
('Product 56-5', 1000, 'type', 56),
('Product 56-6', 1000, 'type', 56),
('Product 56-7', 1000, 'type', 56),
('Product 56-8', 1000, 'type', 56),
('Product 56-9', 1000, 'type', 56),
('Product 56-10', 1000, 'type', 56);

-- Inserting Products for Brand ID 57
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 57-1', 1000, 'type', 57),
('Product 57-2', 1000, 'type', 57),
('Product 57-3', 1000, 'type', 57),
('Product 57-4', 1000, 'type', 57),
('Product 57-5', 1000, 'type', 57),
('Product 57-6', 1000, 'type', 57),
('Product 57-7', 1000, 'type', 57),
('Product 57-8', 1000, 'type', 57),
('Product 57-9', 1000, 'type', 57),
('Product 57-10', 1000, 'type', 57);

-- Inserting Products for Brand ID 58
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 58-1', 1000, 'type', 58),
('Product 58-2', 1000, 'type', 58),
('Product 58-3', 1000, 'type', 58),
('Product 58-4', 1000, 'type', 58),
('Product 58-5', 1000, 'type', 58),
('Product 58-6', 1000, 'type', 58),
('Product 58-7', 1000, 'type', 58),
('Product 58-8', 1000, 'type', 58),
('Product 58-9', 1000, 'type', 58),
('Product 58-10', 1000, 'type', 58);

-- Inserting Products for Brand ID 59
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 59-1', 1000, 'type', 59),
('Product 59-2', 1000, 'type', 59),
('Product 59-3', 1000, 'type', 59),
('Product 59-4', 1000, 'type', 59),
('Product 59-5', 1000, 'type', 59),
('Product 59-6', 1000, 'type', 59),
('Product 59-7', 1000, 'type', 59),
('Product 59-8', 1000, 'type', 59),
('Product 59-9', 1000, 'type', 59),
('Product 59-10', 1000, 'type', 59);

-- Inserting Products for Brand ID 60
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 60-1', 1000, 'type', 60),
('Product 60-2', 1000, 'type', 60),
('Product 60-3', 1000, 'type', 60),
('Product 60-4', 1000, 'type', 60),
('Product 60-5', 1000, 'type', 60),
('Product 60-6', 1000, 'type', 60),
('Product 60-7', 1000, 'type', 60),
('Product 60-8', 1000, 'type', 60),
('Product 60-9', 1000, 'type', 60),
('Product 60-10', 1000, 'type', 60);

-- Inserting Products for Brand ID 61
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 61-1', 1000, 'type', 61),
('Product 61-2', 1000, 'type', 61),
('Product 61-3', 1000, 'type', 61),
('Product 61-4', 1000, 'type', 61),
('Product 61-5', 1000, 'type', 61),
('Product 61-6', 1000, 'type', 61),
('Product 61-7', 1000, 'type', 61),
('Product 61-8', 1000, 'type', 61),
('Product 61-9', 1000, 'type', 61),
('Product 61-10', 1000, 'type', 61);

-- Inserting Products for Brand ID 62
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 62-1', 1000, 'type', 62),
('Product 62-2', 1000, 'type', 62),
('Product 62-3', 1000, 'type', 62),
('Product 62-4', 1000, 'type', 62),
('Product 62-5', 1000, 'type', 62),
('Product 62-6', 1000, 'type', 62),
('Product 62-7', 1000, 'type', 62),
('Product 62-8', 1000, 'type', 62),
('Product 62-9', 1000, 'type', 62),
('Product 62-10', 1000, 'type', 62);

-- Inserting Products for Brand ID 63
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 63-1', 1000, 'type', 63),
('Product 63-2', 1000, 'type', 63),
('Product 63-3', 1000, 'type', 63),
('Product 63-4', 1000, 'type', 63),
('Product 63-5', 1000, 'type', 63),
('Product 63-6', 1000, 'type', 63),
('Product 63-7', 1000, 'type', 63),
('Product 63-8', 1000, 'type', 63),
('Product 63-9', 1000, 'type', 63),
('Product 63-10', 1000, 'type', 63);

-- Inserting Products for Brand ID 64
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 64-1', 1000, 'type', 64),
('Product 64-2', 1000, 'type', 64),
('Product 64-3', 1000, 'type', 64),
('Product 64-4', 1000, 'type', 64),
('Product 64-5', 1000, 'type', 64),
('Product 64-6', 1000, 'type', 64),
('Product 64-7', 1000, 'type', 64),
('Product 64-8', 1000, 'type', 64),
('Product 64-9', 1000, 'type', 64),
('Product 64-10', 1000, 'type', 64);

-- Inserting Products for Brand ID 65
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 65-1', 1000, 'type', 65),
('Product 65-2', 1000, 'type', 65),
('Product 65-3', 1000, 'type', 65),
('Product 65-4', 1000, 'type', 65),
('Product 65-5', 1000, 'type', 65),
('Product 65-6', 1000, 'type', 65),
('Product 65-7', 1000, 'type', 65),
('Product 65-8', 1000, 'type', 65),
('Product 65-9', 1000, 'type', 65),
('Product 65-10', 1000, 'type', 65);

-- Inserting Products for Brand ID 66
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 66-1', 1000, 'type', 66),
('Product 66-2', 1000, 'type', 66),
('Product 66-3', 1000, 'type', 66),
('Product 66-4', 1000, 'type', 66),
('Product 66-5', 1000, 'type', 66),
('Product 66-6', 1000, 'type', 66),
('Product 66-7', 1000, 'type', 66),
('Product 66-8', 1000, 'type', 66),
('Product 66-9', 1000, 'type', 66),
('Product 66-10', 1000, 'type', 66);

-- Inserting Products for Brand ID 67
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 67-1', 1000, 'type', 67),
('Product 67-2', 1000, 'type', 67),
('Product 67-3', 1000, 'type', 67),
('Product 67-4', 1000, 'type', 67),
('Product 67-5', 1000, 'type', 67),
('Product 67-6', 1000, 'type', 67),
('Product 67-7', 1000, 'type', 67),
('Product 67-8', 1000, 'type', 67),
('Product 67-9', 1000, 'type', 67),
('Product 67-10', 1000, 'type', 67);

-- Inserting Products for Brand ID 68
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 68-1', 1000, 'type', 68),
('Product 68-2', 1000, 'type', 68),
('Product 68-3', 1000, 'type', 68),
('Product 68-4', 1000, 'type', 68),
('Product 68-5', 1000, 'type', 68),
('Product 68-6', 1000, 'type', 68),
('Product 68-7', 1000, 'type', 68),
('Product 68-8', 1000, 'type', 68),
('Product 68-9', 1000, 'type', 68),
('Product 68-10', 1000, 'type', 68);

-- Inserting Products for Brand ID 69
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 69-1', 1000, 'type', 69),
('Product 69-2', 1000, 'type', 69),
('Product 69-3', 1000, 'type', 69),
('Product 69-4', 1000, 'type', 69),
('Product 69-5', 1000, 'type', 69),
('Product 69-6', 1000, 'type', 69),
('Product 69-7', 1000, 'type', 69),
('Product 69-8', 1000, 'type', 69),
('Product 69-9', 1000, 'type', 69),
('Product 69-10', 1000, 'type', 69);

-- Inserting Products for Brand ID 70
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 70-1', 1000, 'type', 70),
('Product 70-2', 1000, 'type', 70),
('Product 70-3', 1000, 'type', 70),
('Product 70-4', 1000, 'type', 70),
('Product 70-5', 1000, 'type', 70),
('Product 70-6', 1000, 'type', 70),
('Product 70-7', 1000, 'type', 70),
('Product 70-8', 1000, 'type', 70),
('Product 70-9', 1000, 'type', 70),
('Product 70-10', 1000, 'type', 70);

-- Inserting Products for Brand ID 71
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 71-1', 1000, 'type', 71),
('Product 71-2', 1000, 'type', 71),
('Product 71-3', 1000, 'type', 71),
('Product 71-4', 1000, 'type', 71),
('Product 71-5', 1000, 'type', 71),
('Product 71-6', 1000, 'type', 71),
('Product 71-7', 1000, 'type', 71),
('Product 71-8', 1000, 'type', 71),
('Product 71-9', 1000, 'type', 71),
('Product 71-10', 1000, 'type', 71);

-- Inserting Products for Brand ID 72
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 72-1', 1000, 'type', 72),
('Product 72-2', 1000, 'type', 72),
('Product 72-3', 1000, 'type', 72),
('Product 72-4', 1000, 'type', 72),
('Product 72-5', 1000, 'type', 72),
('Product 72-6', 1000, 'type', 72),
('Product 72-7', 1000, 'type', 72),
('Product 72-8', 1000, 'type', 72),
('Product 72-9', 1000, 'type', 72),
('Product 72-10', 1000, 'type', 72);

-- Inserting Products for Brand ID 73
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 73-1', 1000, 'type', 73),
('Product 73-2', 1000, 'type', 73),
('Product 73-3', 1000, 'type', 73),
('Product 73-4', 1000, 'type', 73),
('Product 73-5', 1000, 'type', 73),
('Product 73-6', 1000, 'type', 73),
('Product 73-7', 1000, 'type', 73),
('Product 73-8', 1000, 'type', 73),
('Product 73-9', 1000, 'type', 73),
('Product 73-10', 1000, 'type', 73);

-- Inserting Products for Brand ID 74
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 74-1', 1000, 'type', 74),
('Product 74-2', 1000, 'type', 74),
('Product 74-3', 1000, 'type', 74),
('Product 74-4', 1000, 'type', 74),
('Product 74-5', 1000, 'type', 74),
('Product 74-6', 1000, 'type', 74),
('Product 74-7', 1000, 'type', 74),
('Product 74-8', 1000, 'type', 74),
('Product 74-9', 1000, 'type', 74),
('Product 74-10', 1000, 'type', 74);

-- Inserting Products for Brand ID 75
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 75-1', 1000, 'type', 75),
('Product 75-2', 1000, 'type', 75),
('Product 75-3', 1000, 'type', 75),
('Product 75-4', 1000, 'type', 75),
('Product 75-5', 1000, 'type', 75),
('Product 75-6', 1000, 'type', 75),
('Product 75-7', 1000, 'type', 75),
('Product 75-8', 1000, 'type', 75),
('Product 75-9', 1000, 'type', 75),
('Product 75-10', 1000, 'type', 75);

-- Inserting Products for Brand ID 76
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 76-1', 1000, 'type', 76),
('Product 76-2', 1000, 'type', 76),
('Product 76-3', 1000, 'type', 76),
('Product 76-4', 1000, 'type', 76),
('Product 76-5', 1000, 'type', 76),
('Product 76-6', 1000, 'type', 76),
('Product 76-7', 1000, 'type', 76),
('Product 76-8', 1000, 'type', 76),
('Product 76-9', 1000, 'type', 76),
('Product 76-10', 1000, 'type', 76);

-- Inserting Products for Brand ID 77
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 77-1', 1000, 'type', 77),
('Product 77-2', 1000, 'type', 77),
('Product 77-3', 1000, 'type', 77),
('Product 77-4', 1000, 'type', 77),
('Product 77-5', 1000, 'type', 77),
('Product 77-6', 1000, 'type', 77),
('Product 77-7', 1000, 'type', 77),
('Product 77-8', 1000, 'type', 77),
('Product 77-9', 1000, 'type', 77),
('Product 77-10', 1000, 'type', 77);

-- Inserting Products for Brand ID 78
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 78-1', 1000, 'type', 78),
('Product 78-2', 1000, 'type', 78),
('Product 78-3', 1000, 'type', 78),
('Product 78-4', 1000, 'type', 78),
('Product 78-5', 1000, 'type', 78),
('Product 78-6', 1000, 'type', 78),
('Product 78-7', 1000, 'type', 78),
('Product 78-8', 1000, 'type', 78),
('Product 78-9', 1000, 'type', 78),
('Product 78-10', 1000, 'type', 78);

-- Inserting Products for Brand ID 79
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 79-1', 1000, 'type', 79),
('Product 79-2', 1000, 'type', 79),
('Product 79-3', 1000, 'type', 79),
('Product 79-4', 1000, 'type', 79),
('Product 79-5', 1000, 'type', 79),
('Product 79-6', 1000, 'type', 79),
('Product 79-7', 1000, 'type', 79),
('Product 79-8', 1000, 'type', 79),
('Product 79-9', 1000, 'type', 79),
('Product 79-10', 1000, 'type', 79);

-- Inserting Products for Brand ID 80
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 80-1', 1000, 'type', 80),
('Product 80-2', 1000, 'type', 80),
('Product 80-3', 1000, 'type', 80),
('Product 80-4', 1000, 'type', 80),
('Product 80-5', 1000, 'type', 80),
('Product 80-6', 1000, 'type', 80),
('Product 80-7', 1000, 'type', 80),
('Product 80-8', 1000, 'type', 80),
('Product 80-9', 1000, 'type', 80),
('Product 80-10', 1000, 'type', 80);

-- Inserting Products for Brand ID 81
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 81-1', 1000, 'type', 81),
('Product 81-2', 1000, 'type', 81),
('Product 81-3', 1000, 'type', 81),
('Product 81-4', 1000, 'type', 81),
('Product 81-5', 1000, 'type', 81),
('Product 81-6', 1000, 'type', 81),
('Product 81-7', 1000, 'type', 81),
('Product 81-8', 1000, 'type', 81),
('Product 81-9', 1000, 'type', 81),
('Product 81-10', 1000, 'type', 81);

-- Inserting Products for Brand ID 82
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 82-1', 1000, 'type', 82),
('Product 82-2', 1000, 'type', 82),
('Product 82-3', 1000, 'type', 82),
('Product 82-4', 1000, 'type', 82),
('Product 82-5', 1000, 'type', 82),
('Product 82-6', 1000, 'type', 82),
('Product 82-7', 1000, 'type', 82),
('Product 82-8', 1000, 'type', 82),
('Product 82-9', 1000, 'type', 82),
('Product 82-10', 1000, 'type', 82);

-- Inserting Products for Brand ID 83
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 83-1', 1000, 'type', 83),
('Product 83-2', 1000, 'type', 83),
('Product 83-3', 1000, 'type', 83),
('Product 83-4', 1000, 'type', 83),
('Product 83-5', 1000, 'type', 83),
('Product 83-6', 1000, 'type', 83),
('Product 83-7', 1000, 'type', 83),
('Product 83-8', 1000, 'type', 83),
('Product 83-9', 1000, 'type', 83),
('Product 83-10', 1000, 'type', 83);

-- Inserting Products for Brand ID 84
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 84-1', 1000, 'type', 84),
('Product 84-2', 1000, 'type', 84),
('Product 84-3', 1000, 'type', 84),
('Product 84-4', 1000, 'type', 84),
('Product 84-5', 1000, 'type', 84),
('Product 84-6', 1000, 'type', 84),
('Product 84-7', 1000, 'type', 84),
('Product 84-8', 1000, 'type', 84),
('Product 84-9', 1000, 'type', 84),
('Product 84-10', 1000, 'type', 84);

-- Inserting Products for Brand ID 85
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 85-1', 1000, 'type', 85),
('Product 85-2', 1000, 'type', 85),
('Product 85-3', 1000, 'type', 85),
('Product 85-4', 1000, 'type', 85),
('Product 85-5', 1000, 'type', 85),
('Product 85-6', 1000, 'type', 85),
('Product 85-7', 1000, 'type', 85),
('Product 85-8', 1000, 'type', 85),
('Product 85-9', 1000, 'type', 85),
('Product 85-10', 1000, 'type', 85);

-- Inserting Products for Brand ID 86
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 86-1', 1000, 'type', 86),
('Product 86-2', 1000, 'type', 86),
('Product 86-3', 1000, 'type', 86),
('Product 86-4', 1000, 'type', 86),
('Product 86-5', 1000, 'type', 86),
('Product 86-6', 1000, 'type', 86),
('Product 86-7', 1000, 'type', 86),
('Product 86-8', 1000, 'type', 86),
('Product 86-9', 1000, 'type', 86),
('Product 86-10', 1000, 'type', 86);

-- Inserting Products for Brand ID 87
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 87-1', 1000, 'type', 87),
('Product 87-2', 1000, 'type', 87),
('Product 87-3', 1000, 'type', 87),
('Product 87-4', 1000, 'type', 87),
('Product 87-5', 1000, 'type', 87),
('Product 87-6', 1000, 'type', 87),
('Product 87-7', 1000, 'type', 87),
('Product 87-8', 1000, 'type', 87),
('Product 87-9', 1000, 'type', 87),
('Product 87-10', 1000, 'type', 87);

-- Inserting Products for Brand ID 88
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 88-1', 1000, 'type', 88),
('Product 88-2', 1000, 'type', 88),
('Product 88-3', 1000, 'type', 88),
('Product 88-4', 1000, 'type', 88),
('Product 88-5', 1000, 'type', 88),
('Product 88-6', 1000, 'type', 88),
('Product 88-7', 1000, 'type', 88),
('Product 88-8', 1000, 'type', 88),
('Product 88-9', 1000, 'type', 88),
('Product 88-10', 1000, 'type', 88);

-- Inserting Products for Brand ID 89
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 89-1', 1000, 'type', 89),
('Product 89-2', 1000, 'type', 89),
('Product 89-3', 1000, 'type', 89),
('Product 89-4', 1000, 'type', 89),
('Product 89-5', 1000, 'type', 89),
('Product 89-6', 1000, 'type', 89),
('Product 89-7', 1000, 'type', 89),
('Product 89-8', 1000, 'type', 89),
('Product 89-9', 1000, 'type', 89),
('Product 89-10', 1000, 'type', 89);

-- Inserting Products for Brand ID 90
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 90-1', 1000, 'type', 90),
('Product 90-2', 1000, 'type', 90),
('Product 90-3', 1000, 'type', 90),
('Product 90-4', 1000, 'type', 90),
('Product 90-5', 1000, 'type', 90),
('Product 90-6', 1000, 'type', 90),
('Product 90-7', 1000, 'type', 90),
('Product 90-8', 1000, 'type', 90),
('Product 90-9', 1000, 'type', 90),
('Product 90-10', 1000, 'type', 90);

-- Inserting Products for Brand ID 91
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 91-1', 1000, 'type', 91),
('Product 91-2', 1000, 'type', 91),
('Product 91-3', 1000, 'type', 91),
('Product 91-4', 1000, 'type', 91),
('Product 91-5', 1000, 'type', 91),
('Product 91-6', 1000, 'type', 91),
('Product 91-7', 1000, 'type', 91),
('Product 91-8', 1000, 'type', 91),
('Product 91-9', 1000, 'type', 91),
('Product 91-10', 1000, 'type', 91);

-- Inserting Products for Brand ID 92
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 92-1', 1000, 'type', 92),
('Product 92-2', 1000, 'type', 92),
('Product 92-3', 1000, 'type', 92),
('Product 92-4', 1000, 'type', 92),
('Product 92-5', 1000, 'type', 92),
('Product 92-6', 1000, 'type', 92),
('Product 92-7', 1000, 'type', 92),
('Product 92-8', 1000, 'type', 92),
('Product 92-9', 1000, 'type', 92),
('Product 92-10', 1000, 'type', 92);

-- Inserting Products for Brand ID 93
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 93-1', 1000, 'type', 93),
('Product 93-2', 1000, 'type', 93),
('Product 93-3', 1000, 'type', 93),
('Product 93-4', 1000, 'type', 93),
('Product 93-5', 1000, 'type', 93),
('Product 93-6', 1000, 'type', 93),
('Product 93-7', 1000, 'type', 93),
('Product 93-8', 1000, 'type', 93),
('Product 93-9', 1000, 'type', 93),
('Product 93-10', 1000, 'type', 93);

-- Inserting Products for Brand ID 94
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 94-1', 1000, 'type', 94),
('Product 94-2', 1000, 'type', 94),
('Product 94-3', 1000, 'type', 94),
('Product 94-4', 1000, 'type', 94),
('Product 94-5', 1000, 'type', 94),
('Product 94-6', 1000, 'type', 94),
('Product 94-7', 1000, 'type', 94),
('Product 94-8', 1000, 'type', 94),
('Product 94-9', 1000, 'type', 94),
('Product 94-10', 1000, 'type', 94);

-- Inserting Products for Brand ID 95
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 95-1', 1000, 'type', 95),
('Product 95-2', 1000, 'type', 95),
('Product 95-3', 1000, 'type', 95),
('Product 95-4', 1000, 'type', 95),
('Product 95-5', 1000, 'type', 95),
('Product 95-6', 1000, 'type', 95),
('Product 95-7', 1000, 'type', 95),
('Product 95-8', 1000, 'type', 95),
('Product 95-9', 1000, 'type', 95),
('Product 95-10', 1000, 'type', 95);

-- Inserting Products for Brand ID 96
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 96-1', 1000, 'type', 96),
('Product 96-2', 1000, 'type', 96),
('Product 96-3', 1000, 'type', 96),
('Product 96-4', 1000, 'type', 96),
('Product 96-5', 1000, 'type', 96),
('Product 96-6', 1000, 'type', 96),
('Product 96-7', 1000, 'type', 96),
('Product 96-8', 1000, 'type', 96),
('Product 96-9', 1000, 'type', 96),
('Product 96-10', 1000, 'type', 96);

-- Inserting Products for Brand ID 97
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 97-1', 1000, 'type', 97),
('Product 97-2', 1000, 'type', 97),
('Product 97-3', 1000, 'type', 97),
('Product 97-4', 1000, 'type', 97),
('Product 97-5', 1000, 'type', 97),
('Product 97-6', 1000, 'type', 97),
('Product 97-7', 1000, 'type', 97),
('Product 97-8', 1000, 'type', 97),
('Product 97-9', 1000, 'type', 97),
('Product 97-10', 1000, 'type', 97);

-- Inserting Products for Brand ID 98
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 98-1', 1000, 'type', 98),
('Product 98-2', 1000, 'type', 98),
('Product 98-3', 1000, 'type', 98),
('Product 98-4', 1000, 'type', 98),
('Product 98-5', 1000, 'type', 98),
('Product 98-6', 1000, 'type', 98),
('Product 98-7', 1000, 'type', 98),
('Product 98-8', 1000, 'type', 98),
('Product 98-9', 1000, 'type', 98),
('Product 98-10', 1000, 'type', 98);

-- Inserting Products for Brand ID 99
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 99-1', 1000, 'type', 99),
('Product 99-2', 1000, 'type', 99),
('Product 99-3', 1000, 'type', 99),
('Product 99-4', 1000, 'type', 99),
('Product 99-5', 1000, 'type', 99),
('Product 99-6', 1000, 'type', 99),
('Product 99-7', 1000, 'type', 99),
('Product 99-8', 1000, 'type', 99),
('Product 99-9', 1000, 'type', 99),
('Product 99-10', 1000, 'type', 99);

-- Inserting Products for Brand ID 100
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 100-1', 1000, 'type', 100),
('Product 100-2', 1000, 'type', 100),
('Product 100-3', 1000, 'type', 100),
('Product 100-4', 1000, 'type', 100),
('Product 100-5', 1000, 'type', 100),
('Product 100-6', 1000, 'type', 100),
('Product 100-7', 1000, 'type', 100),
('Product 100-8', 1000, 'type', 100),
('Product 100-9', 1000, 'type', 100),
('Product 100-10', 1000, 'type', 100);

-- Inserting Products for Brand ID 101
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 101-1', 1000, 'type', 101),
('Product 101-2', 1000, 'type', 101),
('Product 101-3', 1000, 'type', 101),
('Product 101-4', 1000, 'type', 101),
('Product 101-5', 1000, 'type', 101),
('Product 101-6', 1000, 'type', 101),
('Product 101-7', 1000, 'type', 101),
('Product 101-8', 1000, 'type', 101),
('Product 101-9', 1000, 'type', 101),
('Product 101-10', 1000, 'type', 101);

-- Inserting Products for Brand ID 102
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 102-1', 1000, 'type', 102),
('Product 102-2', 1000, 'type', 102),
('Product 102-3', 1000, 'type', 102),
('Product 102-4', 1000, 'type', 102),
('Product 102-5', 1000, 'type', 102),
('Product 102-6', 1000, 'type', 102),
('Product 102-7', 1000, 'type', 102),
('Product 102-8', 1000, 'type', 102),
('Product 102-9', 1000, 'type', 102),
('Product 102-10', 1000, 'type', 102);

-- Inserting Products for Brand ID 103
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 103-1', 1000, 'type', 103),
('Product 103-2', 1000, 'type', 103),
('Product 103-3', 1000, 'type', 103),
('Product 103-4', 1000, 'type', 103),
('Product 103-5', 1000, 'type', 103),
('Product 103-6', 1000, 'type', 103),
('Product 103-7', 1000, 'type', 103),
('Product 103-8', 1000, 'type', 103),
('Product 103-9', 1000, 'type', 103),
('Product 103-10', 1000, 'type', 103);

-- Inserting Products for Brand ID 104
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 104-1', 1000, 'type', 104),
('Product 104-2', 1000, 'type', 104),
('Product 104-3', 1000, 'type', 104),
('Product 104-4', 1000, 'type', 104),
('Product 104-5', 1000, 'type', 104),
('Product 104-6', 1000, 'type', 104),
('Product 104-7', 1000, 'type', 104),
('Product 104-8', 1000, 'type', 104),
('Product 104-9', 1000, 'type', 104),
('Product 104-10', 1000, 'type', 104);

-- Inserting Products for Brand ID 105
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 105-1', 1000, 'type', 105),
('Product 105-2', 1000, 'type', 105),
('Product 105-3', 1000, 'type', 105),
('Product 105-4', 1000, 'type', 105),
('Product 105-5', 1000, 'type', 105),
('Product 105-6', 1000, 'type', 105),
('Product 105-7', 1000, 'type', 105),
('Product 105-8', 1000, 'type', 105),
('Product 105-9', 1000, 'type', 105),
('Product 105-10', 1000, 'type', 105);

-- Inserting Products for Brand ID 106
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 106-1', 1000, 'type', 106),
('Product 106-2', 1000, 'type', 106),
('Product 106-3', 1000, 'type', 106),
('Product 106-4', 1000, 'type', 106),
('Product 106-5', 1000, 'type', 106),
('Product 106-6', 1000, 'type', 106),
('Product 106-7', 1000, 'type', 106),
('Product 106-8', 1000, 'type', 106),
('Product 106-9', 1000, 'type', 106),
('Product 106-10', 1000, 'type', 106);

-- Inserting Products for Brand ID 107
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 107-1', 1000, 'type', 107),
('Product 107-2', 1000, 'type', 107),
('Product 107-3', 1000, 'type', 107),
('Product 107-4', 1000, 'type', 107),
('Product 107-5', 1000, 'type', 107),
('Product 107-6', 1000, 'type', 107),
('Product 107-7', 1000, 'type', 107),
('Product 107-8', 1000, 'type', 107),
('Product 107-9', 1000, 'type', 107),
('Product 107-10', 1000, 'type', 107);

-- Inserting Products for Brand ID 108
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 108-1', 1000, 'type', 108),
('Product 108-2', 1000, 'type', 108),
('Product 108-3', 1000, 'type', 108),
('Product 108-4', 1000, 'type', 108),
('Product 108-5', 1000, 'type', 108),
('Product 108-6', 1000, 'type', 108),
('Product 108-7', 1000, 'type', 108),
('Product 108-8', 1000, 'type', 108),
('Product 108-9', 1000, 'type', 108),
('Product 108-10', 1000, 'type', 108);

-- Inserting Products for Brand ID 109
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 109-1', 1000, 'type', 109),
('Product 109-2', 1000, 'type', 109),
('Product 109-3', 1000, 'type', 109),
('Product 109-4', 1000, 'type', 109),
('Product 109-5', 1000, 'type', 109),
('Product 109-6', 1000, 'type', 109),
('Product 109-7', 1000, 'type', 109),
('Product 109-8', 1000, 'type', 109),
('Product 109-9', 1000, 'type', 109),
('Product 109-10', 1000, 'type', 109);

-- Inserting Products for Brand ID 110
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 110-1', 1000, 'type', 110),
('Product 110-2', 1000, 'type', 110),
('Product 110-3', 1000, 'type', 110),
('Product 110-4', 1000, 'type', 110),
('Product 110-5', 1000, 'type', 110),
('Product 110-6', 1000, 'type', 110),
('Product 110-7', 1000, 'type', 110),
('Product 110-8', 1000, 'type', 110),
('Product 110-9', 1000, 'type', 110),
('Product 110-10', 1000, 'type', 110);

-- Inserting Products for Brand ID 111
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 111-1', 1000, 'type', 111),
('Product 111-2', 1000, 'type', 111),
('Product 111-3', 1000, 'type', 111),
('Product 111-4', 1000, 'type', 111),
('Product 111-5', 1000, 'type', 111),
('Product 111-6', 1000, 'type', 111),
('Product 111-7', 1000, 'type', 111),
('Product 111-8', 1000, 'type', 111),
('Product 111-9', 1000, 'type', 111),
('Product 111-10', 1000, 'type', 111);

-- Inserting Products for Brand ID 112
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 112-1', 1000, 'type', 112),
('Product 112-2', 1000, 'type', 112),
('Product 112-3', 1000, 'type', 112),
('Product 112-4', 1000, 'type', 112),
('Product 112-5', 1000, 'type', 112),
('Product 112-6', 1000, 'type', 112),
('Product 112-7', 1000, 'type', 112),
('Product 112-8', 1000, 'type', 112),
('Product 112-9', 1000, 'type', 112),
('Product 112-10', 1000, 'type', 112);

-- Inserting Products for Brand ID 113
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 113-1', 1000, 'type', 113),
('Product 113-2', 1000, 'type', 113),
('Product 113-3', 1000, 'type', 113),
('Product 113-4', 1000, 'type', 113),
('Product 113-5', 1000, 'type', 113),
('Product 113-6', 1000, 'type', 113),
('Product 113-7', 1000, 'type', 113),
('Product 113-8', 1000, 'type', 113),
('Product 113-9', 1000, 'type', 113),
('Product 113-10', 1000, 'type', 113);

-- Inserting Products for Brand ID 114
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 114-1', 1000, 'type', 114),
('Product 114-2', 1000, 'type', 114),
('Product 114-3', 1000, 'type', 114),
('Product 114-4', 1000, 'type', 114),
('Product 114-5', 1000, 'type', 114),
('Product 114-6', 1000, 'type', 114),
('Product 114-7', 1000, 'type', 114),
('Product 114-8', 1000, 'type', 114),
('Product 114-9', 1000, 'type', 114),
('Product 114-10', 1000, 'type', 114);

-- Inserting Products for Brand ID 115
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 115-1', 1000, 'type', 115),
('Product 115-2', 1000, 'type', 115),
('Product 115-3', 1000, 'type', 115),
('Product 115-4', 1000, 'type', 115),
('Product 115-5', 1000, 'type', 115),
('Product 115-6', 1000, 'type', 115),
('Product 115-7', 1000, 'type', 115),
('Product 115-8', 1000, 'type', 115),
('Product 115-9', 1000, 'type', 115),
('Product 115-10', 1000, 'type', 115);

-- Inserting Products for Brand ID 116
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 116-1', 1000, 'type', 116),
('Product 116-2', 1000, 'type', 116),
('Product 116-3', 1000, 'type', 116),
('Product 116-4', 1000, 'type', 116),
('Product 116-5', 1000, 'type', 116),
('Product 116-6', 1000, 'type', 116),
('Product 116-7', 1000, 'type', 116),
('Product 116-8', 1000, 'type', 116),
('Product 116-9', 1000, 'type', 116),
('Product 116-10', 1000, 'type', 116);

-- Inserting Products for Brand ID 117
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 117-1', 1000, 'type', 117),
('Product 117-2', 1000, 'type', 117),
('Product 117-3', 1000, 'type', 117),
('Product 117-4', 1000, 'type', 117),
('Product 117-5', 1000, 'type', 117),
('Product 117-6', 1000, 'type', 117),
('Product 117-7', 1000, 'type', 117),
('Product 117-8', 1000, 'type', 117),
('Product 117-9', 1000, 'type', 117),
('Product 117-10', 1000, 'type', 117);

-- Inserting Products for Brand ID 118
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 118-1', 1000, 'type', 118),
('Product 118-2', 1000, 'type', 118),
('Product 118-3', 1000, 'type', 118),
('Product 118-4', 1000, 'type', 118),
('Product 118-5', 1000, 'type', 118),
('Product 118-6', 1000, 'type', 118),
('Product 118-7', 1000, 'type', 118),
('Product 118-8', 1000, 'type', 118),
('Product 118-9', 1000, 'type', 118),
('Product 118-10', 1000, 'type', 118);

-- Inserting Products for Brand ID 119
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 119-1', 1000, 'type', 119),
('Product 119-2', 1000, 'type', 119),
('Product 119-3', 1000, 'type', 119),
('Product 119-4', 1000, 'type', 119),
('Product 119-5', 1000, 'type', 119),
('Product 119-6', 1000, 'type', 119),
('Product 119-7', 1000, 'type', 119),
('Product 119-8', 1000, 'type', 119),
('Product 119-9', 1000, 'type', 119),
('Product 119-10', 1000, 'type', 119);

-- Inserting Products for Brand ID 120
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 120-1', 1000, 'type', 120),
('Product 120-2', 1000, 'type', 120),
('Product 120-3', 1000, 'type', 120),
('Product 120-4', 1000, 'type', 120),
('Product 120-5', 1000, 'type', 120),
('Product 120-6', 1000, 'type', 120),
('Product 120-7', 1000, 'type', 120),
('Product 120-8', 1000, 'type', 120),
('Product 120-9', 1000, 'type', 120),
('Product 120-10', 1000, 'type', 120);

-- Inserting Products for Brand ID 121
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 121-1', 1000, 'type', 121),
('Product 121-2', 1000, 'type', 121),
('Product 121-3', 1000, 'type', 121),
('Product 121-4', 1000, 'type', 121),
('Product 121-5', 1000, 'type', 121),
('Product 121-6', 1000, 'type', 121),
('Product 121-7', 1000, 'type', 121),
('Product 121-8', 1000, 'type', 121),
('Product 121-9', 1000, 'type', 121),
('Product 121-10', 1000, 'type', 121);

-- Inserting Products for Brand ID 122
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 122-1', 1000, 'type', 122),
('Product 122-2', 1000, 'type', 122),
('Product 122-3', 1000, 'type', 122),
('Product 122-4', 1000, 'type', 122),
('Product 122-5', 1000, 'type', 122),
('Product 122-6', 1000, 'type', 122),
('Product 122-7', 1000, 'type', 122),
('Product 122-8', 1000, 'type', 122),
('Product 122-9', 1000, 'type', 122),
('Product 122-10', 1000, 'type', 122);

-- Inserting Products for Brand ID 123
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 123-1', 1000, 'type', 123),
('Product 123-2', 1000, 'type', 123),
('Product 123-3', 1000, 'type', 123),
('Product 123-4', 1000, 'type', 123),
('Product 123-5', 1000, 'type', 123),
('Product 123-6', 1000, 'type', 123),
('Product 123-7', 1000, 'type', 123),
('Product 123-8', 1000, 'type', 123),
('Product 123-9', 1000, 'type', 123),
('Product 123-10', 1000, 'type', 123);

-- Inserting Products for Brand ID 124
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 124-1', 1000, 'type', 124),
('Product 124-2', 1000, 'type', 124),
('Product 124-3', 1000, 'type', 124),
('Product 124-4', 1000, 'type', 124),
('Product 124-5', 1000, 'type', 124),
('Product 124-6', 1000, 'type', 124),
('Product 124-7', 1000, 'type', 124),
('Product 124-8', 1000, 'type', 124),
('Product 124-9', 1000, 'type', 124),
('Product 124-10', 1000, 'type', 124);

-- Inserting Products for Brand ID 125
INSERT INTO Product (name, price, type, brand_id) VALUES
('Product 125-1', 1000, 'type', 125),
('Product 125-2', 1000, 'type', 125),
('Product 125-3', 1000, 'type', 125),
('Product 125-4', 1000, 'type', 125),
('Product 125-5', 1000, 'type', 125),
('Product 125-6', 1000, 'type', 125),
('Product 125-7', 1000, 'type', 125),
('Product 125-8', 1000, 'type', 125),
('Product 125-9', 1000, 'type', 125),
('Product 125-10', 1000, 'type', 125);