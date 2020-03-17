package com.zst.springbootstudy.demo01.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author zst
 * @since 2020-03-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Album extends Model<Album> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "albumId", type = IdType.AUTO)
    private Integer albumId;

    @TableField("albumName")
    private String albumName;

    @TableField("albumDesc")
    private String albumDesc;

    @TableField("createTime")
    private LocalDateTime createTime;

    @TableField("orgId")
    private Integer orgId;


    @Override
    protected Serializable pkVal() {
        return this.albumId;
    }

}
