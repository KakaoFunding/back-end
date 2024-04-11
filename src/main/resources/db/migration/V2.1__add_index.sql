CALL CreateIndexIfNotExists('brand', 'name', 'idx_brand_name');
CALL CreateIndexIfNotExists('product', 'price', 'idx_product_price');
CALL CreateIndexIfNotExists('category', 'parent_id', 'idx_category_parent_id');
CALL CreateUniqueIndexIfNotExists('funding_detail', 'funding_id', 'idx_funding_detail_funding_id');
CALL CreateUniqueIndexIfNotExists('funding', 'member_id', 'idx_funding_member_id');
CALL CreateUniqueIndexIfNotExists('funding', 'product_id', 'idx_funding_product_id');
CALL CreateUniqueIndexIfNotExists('gift', 'receipt_receipt_id', 'idx_gift_receipt_receipt_id');
CALL CreateIndexIfNotExists('options', 'product_id', 'idx_options_product_id');
CALL CreateIndexIfNotExists('orders', 'receipt_id', 'idx_orders_receipt_id');
CALL CreateIndexIfNotExists('product', 'brand_id', 'idx_product_brand_id');
CALL CreateIndexIfNotExists('product', 'category_id', 'idx_product_category_id');
CALL CreateUniqueIndexIfNotExists('product_description_photo', 'product_id', 'idx_product_description_photo_product_id');
CALL CreateUniqueIndexIfNotExists('product', 'product_detail_id', 'idx_product_product_detail_id');
CALL CreateUniqueIndexIfNotExists('product_thumbnail', 'product_id', 'idx_product_thumbnail_product_id');
CALL CreateUniqueIndexIfNotExists('receipt_option', 'receipt_id', 'idx_receipt_option_receipt_id');
CALL CreateUniqueIndexIfNotExists('receipt', 'product_id', 'idx_receipt_product_id');
CALL CreateIndexIfNotExists('receipt', 'order_number', 'idx_receipt_order_number');
CALL CreateUniqueIndexIfNotExists('receipt', 'receiver_id', 'idx_receipt_receiver_id');
CALL CreateUniqueIndexIfNotExists('receipt', 'recipient_id', 'idx_receipt_recipient_id');
CALL CreateIndexIfNotExists('wish', 'product_id', 'idx_wish_product_id');


