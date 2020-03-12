package com.zst.springbootstudy.demo01.service.impl;

import com.zst.springbootstudy.demo01.entity.Img;
import com.zst.springbootstudy.demo01.mapper.ImgMapper;
import com.zst.springbootstudy.demo01.service.ImgService;
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
public class ImgServiceImpl extends ServiceImpl<ImgMapper, Img> implements ImgService {

}
