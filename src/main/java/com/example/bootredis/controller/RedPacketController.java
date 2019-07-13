package com.example.bootredis.controller;

import com.example.bootredis.RedisService;
import com.example.bootredis.domain.RedPacketInfo;
import com.example.bootredis.domain.RedPacketRecord;
import com.example.bootredis.mapper.RedPacketInfoMapper;
import com.example.bootredis.mapper.RedPacketRecordMapper;
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
    public String getRedPacket(int uid,long redPacketId) {
        RedPacketRecord redPacketRecord = new RedPacketRecord();
        redPacketRecord.setUid(uid);
        redPacketRecord.setRedPacketId(redPacketId);
        redPacketRecord.setAmount(1111);
        redPacketRecord.setCreateTime(new Date());
        redPacketRecordMapper.insertSelective(redPacketRecord);
        redisService.decr(redPacketId + "_totalNum", 1);
        return "success";
    }

}
