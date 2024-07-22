INSERT INTO funding_detail (created_at, amount, rate, funding_id, payment_id, member_id, status)
VALUES (NOW(), 3000, 30.0, 1, 1, 1, 'PROGRESS'),
       (NOW(), 2000, 20.0, 1, 1, 2, 'PROGRESS'),
       (NOW(), 1000, 10.0, 1, 1, 3, 'PROGRESS');