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
public class Colum extends Model<Colum> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "columId", type = IdType.AUTO)
    private Integer columId;

    private String name;

    private String content;

    @TableField("orgId")
    private Integer orgId;

    @TableField("creatTime")
    private LocalDateTime creatTime;

    @TableField("orderNum")
    private Integer orderNum;

    @TableField("styleId")
    private Integer styleId;

    @TableField("issueId")
    private Integer issueId;


    @Override
    protected Serializable pkVal() {
        return this.columId;
    }

}
