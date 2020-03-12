package com.zst.springbootstudy.demo01;

import com.zst.springbootstudy.demo01.service.OrganizationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class Demo01ApplicationTests {

    @Autowired
    OrganizationService organizationService;
    @Test
    void contextLoads() {
        System.out.println(organizationService.list());
    }

}
