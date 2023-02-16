-- KEYS [PROMOTION_XYZ_STOCK]
-- 1 select form available_stock = ?
if redis.call('exists', KEYS[1]) == 1
then
    -- get available_stock
    local stock = tonumber((redis.call('get', KEYS[1])));
    -- validate available_stock >= 0
    if (stock >= 0)
    then
        -- 2 update --available_stock
        redis.call('set', KEYS[1], stock + 1);
        return stock + 1;
    end
    -- -1 means available_stock < 0
    return -1;
end
-- -2 promotion not exists
return -2;