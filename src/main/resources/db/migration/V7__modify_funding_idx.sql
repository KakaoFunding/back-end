ALTER TABLE funding
DROP FOREIGN KEY `FK308j7d5ln7xaq590xo8bxwul0`;

ALTER TABLE funding
DROP FOREIGN KEY `FK5cxch4qfn9ynsvod79uyulcvj`;

ALTER TABLE funding
DROP INDEX `idx_funding_member_id`;

ALTER TABLE funding
DROP INDEX `idx_funding_product_id`;

ALTER TABLE funding
    ADD CONSTRAINT `FK308j7d5ln7xaq590xo8bxwul0`
        FOREIGN KEY (`member_id`) REFERENCES `member` (`member_id`);

ALTER TABLE funding
    ADD CONSTRAINT `FK5cxch4qfn9ynsvod79uyulcvj`
        FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`);

ALTER TABLE funding
    ADD INDEX `idx_funding_member_id` (member_id);

ALTER TABLE funding
    ADD INDEX `idx_funding_product_id` (product_id);