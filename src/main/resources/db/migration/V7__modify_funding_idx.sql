ALTER TABLE funding
    DROP FOREIGN KEY `fk_funding_member`;

ALTER TABLE funding
    DROP FOREIGN KEY `fk_funding_product`;

ALTER TABLE funding
    DROP INDEX `idx_funding_member_id`;

ALTER TABLE funding
    DROP INDEX `idx_funding_product_id`;

ALTER TABLE funding
    ADD CONSTRAINT `fk_funding_member`
        FOREIGN KEY (`member_id`) REFERENCES `member` (`member_id`);

ALTER TABLE funding
    ADD CONSTRAINT `fk_funding_product`
        FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`);

ALTER TABLE funding
    ADD INDEX `idx_funding_member_id` (member_id);

ALTER TABLE funding
    ADD INDEX `idx_funding_product_id` (product_id);