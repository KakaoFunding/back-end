CREATE TABLE `product`
(
    `product_id`        BIGINT         NOT NULL AUTO_INCREMENT,
    `name`              VARCHAR(255)   NOT NULL,
    `price`             DECIMAL(10, 2) NOT NULL,
    `photo`             VARCHAR(255),
    `type`              VARCHAR(50)    NOT NULL,
    `brand_id`          BIGINT         NOT NULL,
    `product_detail_id` BIGINT         NOT NULL,
    `created_at`        DATETIME DEFAULT NOW(),
    `updated_at`        DATETIME,
    PRIMARY KEY (`product_id`)
);

CREATE TABLE `brand`
(
    `brand_id`    BIGINT       NOT NULL AUTO_INCREMENT,
    `name`        VARCHAR(255) NOT NULL,
    `icon_photo`  VARCHAR(255),
    `category_id` BIGINT       NOT NULL,
    `created_at`  DATETIME DEFAULT NOW(),
    `updated_at`  DATETIME,
    PRIMARY KEY (`brand_id`)
);

CREATE TABLE `category`
(
    `category_id` BIGINT       NOT NULL AUTO_INCREMENT,
    `parent_id`   BIGINT,
    `name`        VARCHAR(255) NOT NULL,
    `created_at`  DATETIME DEFAULT NOW(),
    `updated_at`  DATETIME,
    PRIMARY KEY (`category_id`)
);

CREATE TABLE `hashtag`
(
    `hashtag_id` BIGINT       NOT NULL AUTO_INCREMENT,
    `name`       VARCHAR(255) NOT NULL,
    `created_at` DATETIME DEFAULT NOW(),
    `updated_at` DATETIME,
    PRIMARY KEY (`hashtag_id`)
);

CREATE TABLE `options`
(
    `options_id` BIGINT       NOT NULL AUTO_INCREMENT,
    `name`       VARCHAR(255) NOT NULL,
    `product_id` BIGINT       NOT NULL,
    `created_at` DATETIME DEFAULT NOW(),
    `updated_at` DATETIME,
    PRIMARY KEY (`options_id`)
);

CREATE TABLE `option_detail`
(
    `option_detail_id` BIGINT         NOT NULL AUTO_INCREMENT,
    `name`             VARCHAR(255)   NOT NULL,
    `stock_quantity`   BIGINT         NOT NULL,
    `additional_price` DECIMAL(10, 2) NOT NULL,
    `photo`            VARCHAR(255)   NOT NULL,
    `options_id`       BIGINT         NOT NULL,
    `created_at`       DATETIME DEFAULT NOW(),
    `updated_at`       DATETIME,
    PRIMARY KEY (`option_detail_id`)
);

CREATE TABLE `product_hashtag`
(
    `product_hashtag_id` BIGINT NOT NULL AUTO_INCREMENT,
    `hashtag_id`         BIGINT NOT NULL,
    `product_id`         BIGINT NOT NULL,
    `created_at`         DATETIME DEFAULT NOW(),
    `updated_at`         DATETIME,
    PRIMARY KEY (`product_hashtag_id`)
);

CREATE TABLE `wish`
(
    `wish_id`           BIGINT  NOT NULL AUTO_INCREMENT,
    `is_public`         BOOLEAN NOT NULL,
    `product_id`        BIGINT  NOT NULL,
    `product_detail_id` BIGINT  NOT NULL,
    `created_at`        DATETIME DEFAULT NOW(),
    `updated_at`        DATETIME,
    PRIMARY KEY (`wish_id`)
);

CREATE TABLE `member`
(
    `member_id`    BIGINT       NOT NULL AUTO_INCREMENT,
    `gender`       VARCHAR(255) NOT NULL,
    `name`         VARCHAR(255) NOT NULL,
    `phone_number` VARCHAR(255) NOT NULL,
    `provider_id`  VARCHAR(255) NOT NULL,
    `role`         VARCHAR(255) NOT NULL,
    `created_at`   DATETIME DEFAULT NOW(),
    `updated_at`   DATETIME,
    PRIMARY KEY (`member_id`)
);

CREATE TABLE `theme`
(
    `theme_id`    BIGINT       NOT NULL AUTO_INCREMENT,
    `photo`       VARCHAR(255) NOT NULL,
    `description` TEXT         NOT NULL,
    `product_id`  BIGINT       NOT NULL,
    `created_at`  DATETIME DEFAULT NOW(),
    `updated_at`  DATETIME,
    PRIMARY KEY (`theme_id`)
);

CREATE TABLE `orders`
(
    `orders_id`         BIGINT       NOT NULL AUTO_INCREMENT,
    `stock_quantity`    BIGINT       NOT NULL,
    `order_number`      VARCHAR(10)  NOT NULL,
    `status`            VARCHAR(255) NOT NULL,
    `member_id`         BIGINT       NOT NULL,
    `product_id`        BIGINT       NOT NULL,
    `payment_id`        BIGINT       NOT NULL,
    `funding_detail_id` BIGINT       NOT NULL,
    `created_at`        DATETIME DEFAULT NOW(),
    `updated_at`        DATETIME,
    PRIMARY KEY (`orders_id`)
);

CREATE TABLE `payment`
(
    `payment_id`     BIGINT         NOT NULL AUTO_INCREMENT,
    `method`         VARCHAR(255)   NOT NULL,
    `total_price`    DECIMAL(10, 2) NOT NULL,
    `purchase_price` DECIMAL(10, 2) NOT NULL,
    `delivery_price` DECIMAL(10, 2) NOT NULL,
    `created_at`     DATETIME DEFAULT NOW(),
    `updated_at`     DATETIME,
    PRIMARY KEY (`payment_id`)
);

CREATE TABLE `gift`
(
    `gift_id`       BIGINT      NOT NULL AUTO_INCREMENT,
    `status`        VARCHAR(50) NOT NULL,
    `message`       VARCHAR(255),
    `message_photo` VARCHAR(255),
    `product_id`    BIGINT      NOT NULL,
    `orders_id`     BIGINT      NOT NULL,
    `created_at`    DATETIME DEFAULT NOW(),
    `updated_at`    DATETIME,
    PRIMARY KEY (`gift_id`)
);

CREATE TABLE `funding`
(
    `funding_id`        BIGINT         NOT NULL AUTO_INCREMENT,
    `status`            VARCHAR(255),
    `goal_amount`       DECIMAL(10, 2) NOT NULL,
    `accumulate_amount` DECIMAL(10, 2) NOT NULL,
    `member_id`         BIGINT         NOT NULL,
    `product_id`        BIGINT         NOT NULL,
    `created_at`        DATETIME DEFAULT NOW(),
    `expired_at`        DATETIME       NOT NULL,
    `updated_at`        DATETIME,
    PRIMARY KEY (`funding_id`)
);

CREATE TABLE `funding_detail`
(
    `funding_detail_id` BIGINT         NOT NULL AUTO_INCREMENT,
    `amount`            DECIMAL(10, 2) NOT NULL,
    `rate`              DECIMAL(5, 2)  NOT NULL,
    `funding_id`        BIGINT         NOT NULL,
    `created_at`        DATETIME DEFAULT NOW(),
    `updated_at`        DATETIME,
    PRIMARY KEY (`funding_detail_id`)
);

CREATE TABLE `product_detail`
(
    `product_detail_id`   BIGINT       NOT NULL AUTO_INCREMENT,
    `description`         TEXT         NOT NULL,
    `has_photo`           BOOLEAN      NOT NULL,
    `product_name`        VARCHAR(255) NOT NULL,
    `origin`              VARCHAR(255) NOT NULL,
    `manufacturer`        VARCHAR(255) NOT NULL,
    `tel`                 VARCHAR(20)  NOT NULL,
    `deliver_description` TEXT         NOT NULL,
    `billing_notice`      TEXT         NOT NULL,
    `caution`             TEXT         NOT NULL,
    `created_at`          DATETIME DEFAULT NOW(),
    `updated_at`          DATETIME,
    PRIMARY KEY (`product_detail_id`)
);

CREATE TABLE `product_thumbnail`
(
    `product_thumbnail_id` BIGINT NOT NULL AUTO_INCREMENT,
    `thumbnail_url`        VARCHAR(255),
    `product_id`           BIGINT NOT NULL,
    `created_at`           DATETIME DEFAULT NOW(),
    `updated_at`           DATETIME,
    PRIMARY KEY (`product_thumbnail_id`)
);

CREATE TABLE `product_description_photo`
(
    `photo_id`   BIGINT NOT NULL AUTO_INCREMENT,
    `photo_url`  VARCHAR(255),
    `product_id` BIGINT NOT NULL,
    `created_at` DATETIME DEFAULT NOW(),
    `updated_at` DATETIME,
    PRIMARY KEY (`photo_id`)
);