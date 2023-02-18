-- KEYS [PROMOTION_XYZ_STOCK] [ORDER_NUM] [OPERATION_NAME]

-- 1 select form available_stock = ?
if redis.call('exists', KEYS[1]) == 1
then
    -- 0 idempotence  if key value order_num: lock_stock exists return
    if redis.call('exists', KEYS[2]) == 1 and redis.call('get', KEYS[2]) == KEYS[3]
    then
        return redis.call('get', KEYS[1]);
    end
    -- get available_stock
    local stock = tonumber((redis.call('get', KEYS[1])));
    -- validate available_stock > 0
    if (stock > 0)
        then
        -- 2 update --available_stock
        redis.call('set', KEYS[1], stock - 1);
        redis.call('set', KEYS[2], KEYS[3]);
        return stock - 1;
    end
    -- -1 means available_stock <= 0
    return -1;
end
-- -2 promotion not exists
return -2;


