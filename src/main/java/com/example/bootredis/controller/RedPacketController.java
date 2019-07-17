package com.example.bootredis.controller;

import com.example.bootredis.RedisService;
import com.example.bootredis.domain.RedPacketInfo;
import com.example.bootredis.domain.RedPacketRecord;
import com.example.bootredis.mapper.RedPacketInfoMapper;
import com.example.bootredis.mapper.RedPacketRecordMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Random;

@RestController
public class RedPacketController {

    @Autowired
    private RedisService redisService;

    @Autowired
    private RedPacketInfoMapper redPacketInfoMapper;

    @Autowired
    private RedPacketRecordMapper redPacketRecordMapper;

    private static final String TOTAL_NUM = "_totalNum";
    private static final String TOTAL_AMOUNT= "_totalAmount";


    @ResponseBody
    @RequestMapping("/addPacket")
    public String saveRedPacket(Integer uid,Integer totalNum, Integer totalAmount) {
        RedPacketInfo record = new RedPacketInfo();
        record.setUid(uid);
        record.setTotalAmount(totalAmount);
        record.setTotalPacket(totalNum);
        record.setCreateTime(new Date());
        record.setRemainingAmount(totalAmount);
        record.setRemainingPacket(totalNum);
        Random random = new Random();
        long redPacketId = System.currentTimeMillis(); //此时无法保证唯一，雪花算法生成分布式系统唯一ID
        record.setRedPacketId(redPacketId);
        redPacketInfoMapper.insert(record);
        redisService.set(redPacketId + "_totalNum", totalNum+"");
        redisService.set(redPacketId + "_totalAmount", totalAmount+"");
        return "success";
    }


    @ResponseBody
    @RequestMapping("/getPacket")
    public Integer getRedPacket(long redPacketId) {
        String redPacketName = redPacketId + TOTAL_NUM;
        String num = (String) redisService.get(redPacketName);
        if (StringUtils.isNotBlank(num)) {
            return Integer.parseInt(num);
        }
        return 0;
    }

    @ResponseBody
    @RequestMapping("/getRedPacketMoney")
    public String getRedPacketMoney(int uid ,long redPacketId) {
        Integer randomAmount = 0;
        String redPacketName = redPacketId + TOTAL_NUM;
        String totalAmountName = redPacketId + TOTAL_AMOUNT;
        String num = (String) redisService.get(redPacketName);
        if (StringUtils.isBlank(num) || Integer.parseInt(num) == 0) {
            return "红包已经被抢完";
        }
        String totalAmount = (String) redisService.get(totalAmountName);
        if (StringUtils.isNotBlank(totalAmount)) {
            Integer totalAmountInt = Integer.parseInt(totalAmount);
            Integer maxMoney = totalAmountInt / Integer.parseInt(num) * 2;
            Random random = new Random();
            randomAmount = random.nextInt(maxMoney);
        }
        redisService.decr(redPacketName, 1);
        redisService.decr(totalAmountName, randomAmount);
        updatePacketInDB(uid, redPacketId, randomAmount);
        return randomAmount + "";
    }

    public void updatePacketInDB(int uid , long redPacketId , int amount) {
        RedPacketRecord redPacketRecord = new RedPacketRecord();
        redPacketRecord.setUid(uid);
        redPacketRecord.setRedPacketId(redPacketId);
        redPacketRecord.setAmount(1111);
        redPacketRecord.setCreateTime(new Date());
        redPacketRecordMapper.insertSelective(redPacketRecord);
        redisService.decr(redPacketId + "_totalNum", 1);
    }

}
