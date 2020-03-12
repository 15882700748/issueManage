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
public class Issue extends Model<Issue> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "issueId", type = IdType.AUTO)
    private Integer issueId;

    private String logo;

    private String title;

    private String content;

    @TableField("holdupTime")
    private LocalDateTime holdupTime;

    private String location;

    @TableField("orgId")
    private Integer orgId;


    @Override
    protected Serializable pkVal() {
        return this.issueId;
    }

}
