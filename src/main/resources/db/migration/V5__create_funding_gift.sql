CREATE TABLE funding_gift
(
    funding_gift_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at      datetime     NULL,
    updated_at      datetime     NULL,
    funding_id      BIGINT       NOT NULL,
    status          VARCHAR(255) NOT NULL,
    expired_at      datetime     NOT NULL
);

ALTER TABLE funding_gift
    ADD CONSTRAINT FK_FUNDING_GIFT_ON_FUNDING FOREIGN KEY (funding_id) REFERENCES funding (funding_id);