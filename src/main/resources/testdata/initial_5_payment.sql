INSERT INTO payment (created_at, delivery_price, payment_id, purchase_price, total_price, payment_number, method)
VALUES (NOW(), 0, 1, 3000, 3000, '1', 'KAKAO_PAY'),
       (NOW(), 0, 2, 2000, 2000, '2', 'KAKAO_PAY'),
       (NOW(), 0, 3, 1000, 1000, '3', 'KAKAO_PAY');