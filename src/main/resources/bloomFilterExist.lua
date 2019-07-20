--
-- Created by IntelliJ IDEA.
-- User: yangbin1
-- Date: 2019/7/20
-- Time: 17:12
-- To change this template use File | Settings | File Templates.
--

local bloomName = KEYS[1]
local value = KEYS[2]

--bloomFilter
local result_1 = redis.call("BF.EXISTS",bloomName,value)
return result_1