local key = KEYS[1]; -- 第1个参数,锁的key
local threadId = ARGV[1]; -- 第2个参数,线程唯一标识

if (redis.call('HEXISTS', key, threadId) == 0) then -- 判断当前锁是否还是被自己持有
    return nil; -- 如果已经不是自己，则直接返回
end;
local count = redis.call('HINCRBY', key, threadId, -1); -- 是自己的锁，则重入次数-1

if (count == 0) then -- 判断是否重入次数是否已经为0
    redis.call('DEL', key); -- 等于0说明可以释放锁，直接删除
    return nil;
end;
