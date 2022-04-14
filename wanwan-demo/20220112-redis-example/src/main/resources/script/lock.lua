local key = KEYS[1]; -- 第1个参数,锁的key
local threadId = ARGV[1]; -- 第2个参数,线程唯一标识
local releaseTime = ARGV[2]; -- 第3个参数,锁的自动释放时间

if(redis.call('exists', key) == 0) then -- 判断锁是否已存在
    redis.call('hset', key, threadId, '1'); -- 不存在, 则获取锁
    redis.call('expire', key, releaseTime); -- 设置有效期
    return 1; -- 返回结果
end;

if(redis.call('hexists', key, threadId) == 1) then -- 锁已经存在，判断threadId是否是自己
    redis.call('hincrby', key, threadId, '1'); -- 如果是自己，则重入次数+1
    redis.call('expire', key, releaseTime); -- 设置有效期
    return 1; -- 返回结果
end;
return 0; -- 代码走到这里,说明获取锁的不是自己，获取锁失败
