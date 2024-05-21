-- orders 에 funding_detail_id (FK) 제거
ALTER TABLE orders
    DROP FOREIGN KEY `FKa02760ybny4lgcvn2wvhtcb6b`;

-- funding_detail 의 funding_id 인덱스의 UNIQUE 속성 제거1
ALTER TABLE funding_detail
    DROP INDEX idx_funding_detail_funding_id;
ALTER TABLE funding_detail
    ADD INDEX idx_funding_detail_funding_id (`funding_id`);

-- funding_detail 에 payment_id (FK) 추가
ALTER TABLE funding_detail
    ADD COLUMN `payment_id` BIGINT NOT NULL;
ALTER TABLE funding_detail
    ADD CONSTRAINT FK_FUNDINGDETAIL_ON_PAYMENT FOREIGN KEY (payment_id) REFERENCES payment (payment_id);


-- funding_detail 에 member_id (FK) 추가
ALTER TABLE funding_detail
    ADD COLUMN `member_id` bigint NOT NULL;
ALTER TABLE funding_detail
    ADD CONSTRAINT FK_FUNDINGDETAIL_ON_MEMBER FOREIGN KEY (member_id) REFERENCES member (member_id);

ALTER TABLE funding_detail
    MODIFY `rate` DOUBLE;

ALTER TABLE funding_detail
    ADD COLUMN `status` varchar(255) NOT NULL;
