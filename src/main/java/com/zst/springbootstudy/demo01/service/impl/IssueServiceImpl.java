package com.zst.springbootstudy.demo01.service.impl;

import com.zst.springbootstudy.demo01.entity.Issue;
import com.zst.springbootstudy.demo01.mapper.IssueMapper;
import com.zst.springbootstudy.demo01.service.IssueService;
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
public class IssueServiceImpl extends ServiceImpl<IssueMapper, Issue> implements IssueService {

}
