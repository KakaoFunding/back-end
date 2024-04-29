-- funding 기존 status 컬럼을 열거체화
ALTER TABLE funding MODIFY `status` enum('PROGRESS', 'COMPLETE', 'CANCEL') DEFAULT 'PROGRESS' NOT NULL;