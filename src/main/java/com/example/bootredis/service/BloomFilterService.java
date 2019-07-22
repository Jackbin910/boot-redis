package com.example.bootredis.service;

import com.example.bootredis.domain.SysUser;
import com.example.bootredis.domain.SysUserExample;
import com.example.bootredis.mapper.SysUserMapper;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

@Service
public class BloomFilterService {

    @Resource
    private SysUserMapper sysUserMapper;

    private BloomFilter<Integer> bf;

    /**
     * 程序启动时加载此方法
     */
    @PostConstruct
    public void initBloomFilter() {
        SysUserExample sysUserExample = new SysUserExample();
        List<SysUser> sysUserList = sysUserMapper.selectByExample(sysUserExample);
        if (CollectionUtils.isEmpty(sysUserList)) {
            return;
        }
        bf = BloomFilter.create(Funnels.integerFunnel(), sysUserList.size());
        for (SysUser sysUser : sysUserList) {
            bf.put(sysUser.getId());
        }

    }

    /***
     * 判断id可能存在在布隆过滤器中
     * @param id
     * @return
     */
    public boolean userIdExists(int id) {
        return bf.mightContain(id);
    }
}
