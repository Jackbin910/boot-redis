local bloomName = KEYS[1]
local value = KEYS[2]

--bloomFilter
local result_1 = redis.call("BF.ADD", bloomName, value)
return result_1