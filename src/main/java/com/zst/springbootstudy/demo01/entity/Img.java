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
 * @since 2020-03-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Img extends Model<Img> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "imgId", type = IdType.AUTO)
    private Integer imgId;

    @TableField("articleId")
    private Integer articleId;

    @TableField("imgUrl")
    private String imgUrl;

    @TableField("columId")
    private Integer columId;

    @TableField("orgId")
    private Integer orgId;

    @TableField("issueId")
    private Integer issueId;

    @TableField("albumId")
    private Integer albumId;

    @TableField("uploadTime")
    private LocalDateTime uploadTime;


    @Override
    protected Serializable pkVal() {
        return this.imgId;
    }

}
