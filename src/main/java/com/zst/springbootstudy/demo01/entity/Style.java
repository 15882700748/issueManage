package com.zst.springbootstudy.demo01.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
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
public class Style extends Model<Style> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "styleId", type = IdType.AUTO)
    private Integer styleId;

    private String color;

    private String position;

    private String font;

    @TableField("orgId")
    private Integer orgId;

    private String layout;


    @Override
    protected Serializable pkVal() {
        return this.styleId;
    }

}
