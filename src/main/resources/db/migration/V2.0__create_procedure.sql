DELIMITER $$

CREATE PROCEDURE IF NOT EXISTS `CreateIndexIfNotExists`(
    IN `in_table_name` VARCHAR(64),
    IN `in_column_name` VARCHAR(64),
    IN `in_index_name` VARCHAR(64)
)
BEGIN
    DECLARE column_index_count INT;

    SELECT COUNT(DISTINCT INDEX_NAME) INTO column_index_count
    FROM INFORMATION_SCHEMA.STATISTICS
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = in_table_name
      AND COLUMN_NAME = in_column_name
      AND SEQ_IN_INDEX = 1
      AND (SELECT COUNT(*) FROM INFORMATION_SCHEMA.STATISTICS AS inner_stats
           WHERE inner_stats.TABLE_SCHEMA = INFORMATION_SCHEMA.STATISTICS.TABLE_SCHEMA
             AND inner_stats.TABLE_NAME = INFORMATION_SCHEMA.STATISTICS.TABLE_NAME
             AND inner_stats.INDEX_NAME = INFORMATION_SCHEMA.STATISTICS.INDEX_NAME) = 1;

    IF column_index_count = 0 THEN
        SET @sql = CONCAT('CREATE INDEX ', in_index_name, ' ON ', in_table_name, '(', in_column_name, ')');
        PREPARE stmt FROM @sql;
        EXECUTE stmt;
        DEALLOCATE PREPARE stmt;
        SELECT CONCAT('Unique index ', in_index_name, ' on column ', in_column_name, ' created.') AS result;
    ELSE
        SELECT CONCAT('A unique index on column ', in_column_name, ' already exists in table ', in_table_name, '. No new index created.') AS result;
    END IF;
END$$

DELIMITER ;

DELIMITER $$

CREATE PROCEDURE IF NOT EXISTS `CreateUniqueIndexIfNotExists`(
    IN `in_table_name` VARCHAR(64),
    IN `in_column_name` VARCHAR(64),
    IN `in_index_name` VARCHAR(64)
)
BEGIN
    DECLARE column_index_count INT;

    SELECT COUNT(DISTINCT INDEX_NAME) INTO column_index_count
    FROM INFORMATION_SCHEMA.STATISTICS
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = in_table_name
      AND COLUMN_NAME = in_column_name
      AND SEQ_IN_INDEX = 1
      AND NON_UNIQUE = 0
      AND (SELECT COUNT(*) FROM INFORMATION_SCHEMA.STATISTICS AS inner_stats
           WHERE inner_stats.TABLE_SCHEMA = INFORMATION_SCHEMA.STATISTICS.TABLE_SCHEMA
             AND inner_stats.TABLE_NAME = INFORMATION_SCHEMA.STATISTICS.TABLE_NAME
             AND inner_stats.INDEX_NAME = INFORMATION_SCHEMA.STATISTICS.INDEX_NAME) = 1;

    IF column_index_count = 0 THEN
        SET @sql = CONCAT('CREATE UNIQUE INDEX ', in_index_name,
                          ' ON ', in_table_name, '(', in_column_name, ')');
        PREPARE stmt FROM @sql;
        EXECUTE stmt;
        DEALLOCATE PREPARE stmt;
        SELECT CONCAT('Unique index ', in_index_name, ' on column ', in_column_name, ' created.') AS result;
    ELSE
        SELECT CONCAT('A unique index on column ', in_column_name,
                      ' already exists in table ', in_table_name,
                      '. No new unique index created.') AS result;
    END IF;
END$$

DELIMITER ;