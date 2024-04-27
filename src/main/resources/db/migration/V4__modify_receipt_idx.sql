-- receipt 테이블 product_id, receiver_id, recipient_id 컬럼 별 인덱스 UNIQUE 속성 제거
ALTER TABLE receipt
    DROP CONSTRAINT FK8gm1uqkp10nbbqq06i5nql4k,
    DROP CONSTRAINT FKfitwgst1g85qcvskkg54u9xiy,
    DROP CONSTRAINT FKn0i2aaqgc4jp8syviodr7lcpb;

DROP INDEX `idx_receipt_product_id` ON receipt;
DROP INDEX `idx_receipt_receiver_id` ON receipt;
DROP INDEX `idx_receipt_recipient_id` ON receipt;

ALTER TABLE receipt
    ADD CONSTRAINT FK8gm1uqkp10nbbqq06i5nql4k FOREIGN KEY (receiver_id) REFERENCES member (member_id),
    ADD CONSTRAINT FKfitwgst1g85qcvskkg54u9xiy FOREIGN KEY (product_id) REFERENCES product (product_id),
    ADD CONSTRAINT FKn0i2aaqgc4jp8syviodr7lcpb FOREIGN KEY (recipient_id) REFERENCES member (member_id);

CREATE INDEX `idx_receipt_product_id` ON receipt (`product_id`);
CREATE INDEX `idx_receipt_receiver_id` ON receipt (`receiver_id`);
CREATE INDEX `idx_receipt_recipient_id` ON receipt (`recipient_id`);
