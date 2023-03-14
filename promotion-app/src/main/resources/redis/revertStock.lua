-- KEYS, [Promotion-id-stock]

-- 1. select available_stock
if redis.call('exists', KEYS[1]) == 1
then
    -- get redis.call('get', KEYS[1])
    local stock = tonumber(redis.call('get', KEYS[1]));

    -- validate available_stock >= 0
    if (stock >= 0)
    then
        -- 2. update available_stock
        redis.call('set', KEYS[1], stock + 1);
        return stock + 1;
    end
    -- return -1 if available_stock < 0
    return -1;
end

-- return -2 if promotion not exists
return -2;