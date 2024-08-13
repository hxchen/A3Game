package com.a3fun.pudding.tickers;

import com.a3fun.core.tick.AbstractTicker;
import com.a3fun.pudding.model.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
@Service
public class RoleTicker extends AbstractTicker<Role> {

    @Override
    protected void tick(Role role, long now) {
//        log.info("处理角色：" + role);
    }

    @Override
    protected Collection<Role> findAll() {
        List<Role> list = new ArrayList<>();
        list.add(new Role(100000001L, "ZhangSan", 1677600000000L, 1680278399000L));
        list.add(new Role(100000002L, "KiSi", 1677600000000L, 1680278399000L));
        list.add(new Role(100000003L, "WangWu", 1677600000000L, 1680278399000L));
        list.add(new Role(100000004L, "ZhaoLiu", 1677600000000L, 1680278399000L));
        return list;
    }


}
