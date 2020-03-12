package com.zst.springbootstudy.demo01.service.impl;

import com.zst.springbootstudy.demo01.entity.Organization;
import com.zst.springbootstudy.demo01.mapper.OrganizationMapper;
import com.zst.springbootstudy.demo01.service.OrganizationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zst
 * @since 2020-02-16
 */
@Service
public class OrganizationServiceImpl extends ServiceImpl<OrganizationMapper, Organization> implements OrganizationService {

}
