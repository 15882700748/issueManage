package com.zst.springbootstudy.demo01.service.impl;

import com.zst.springbootstudy.demo01.entity.Album;
import com.zst.springbootstudy.demo01.mapper.AlbumMapper;
import com.zst.springbootstudy.demo01.service.AlbumService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zst
 * @since 2020-03-13
 */
@Service
public class AlbumServiceImpl extends ServiceImpl<AlbumMapper, Album> implements AlbumService {

}
