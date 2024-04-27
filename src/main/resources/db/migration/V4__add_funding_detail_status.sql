-- funding_detail 에 status 컬럼 추가
ALTER TABLE funding_detail ADD COLUMN `status` enum ('PROGRESS', 'COMPLETE', 'CANCEL_REFUND') DEFAULT 'PROGRESS' NOT NULL;