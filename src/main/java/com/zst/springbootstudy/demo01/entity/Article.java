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
 * @since 2020-02-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Article extends Model<Article> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "articleId", type = IdType.AUTO)
    private Integer articleId;

    private String title;

    private String content;

    @TableField("realseTime")
    private LocalDateTime realseTime;

    @TableField("holdupTime")
    private LocalDateTime holdupTime;

    @TableField("orgId")
    private Integer orgId;

    @TableField("issueId")
    private String issueId;


    @Override
    protected Serializable pkVal() {
        return this.articleId;
    }

}
