ALTER TABLE receipt_option
    DROP CONSTRAINT `FK_RECEIPT_OPTION_ON_RECEIPT`;
ALTER TABLE receipt_option
    DROP INDEX `idx_receipt_option_receipt_id`;
ALTER TABLE receipt_option
    ADD CONSTRAINT `FK_RECEIPT_OPTION_ON_RECEIPT` FOREIGN KEY (`receipt_id`) REFERENCES `receipt` (`receipt_id`);
ALTER TABLE receipt_option
    ADD INDEX `idx_receipt_option_receipt_id` (`receipt_id`);