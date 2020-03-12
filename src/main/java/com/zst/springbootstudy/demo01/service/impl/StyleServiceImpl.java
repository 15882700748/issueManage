package com.zst.springbootstudy.demo01.service.impl;

import com.zst.springbootstudy.demo01.entity.Style;
import com.zst.springbootstudy.demo01.mapper.StyleMapper;
import com.zst.springbootstudy.demo01.service.StyleService;
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
public class StyleServiceImpl extends ServiceImpl<StyleMapper, Style> implements StyleService {

}
