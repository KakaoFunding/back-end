ALTER TABLE gift
    DROP COLUMN `receipt_id`;

ALTER TABLE gift
    CHANGE receipt_receipt_id `receipt_id`  BIGINT NOT NULL;