CREATE TABLE cart (
                      cart_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      item_count INT NOT NULL,
                      member_id BIGINT NOT NULL,
                      product_id BIGINT NOT NULL,
                      option_id BIGINT,
                      option_detail_id BIGINT,
                      created_at TIMESTAMP,
                      updated_at TIMESTAMP
);

ALTER TABLE cart
    ADD CONSTRAINT FK_MEMBER_ON_CART FOREIGN KEY (member_id) REFERENCES member(member_id),
    ADD CONSTRAINT FK_PRODUCT_ON_CART FOREIGN KEY (product_id) REFERENCES product(product_id),
    ADD CONSTRAINT FK_OPTION_ON_CART FOREIGN KEY (option_id) REFERENCES options(options_id),
    ADD CONSTRAINT FK_OPTION_DETAIL_ON_CART FOREIGN KEY (option_detail_id) REFERENCES option_detail(option_detail_id);

CREATE INDEX idx_cart_member_id ON cart(member_id);
CREATE INDEX idx_cart_product_id ON cart(product_id);
