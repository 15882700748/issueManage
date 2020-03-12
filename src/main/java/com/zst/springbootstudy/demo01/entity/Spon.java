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
public class Spon extends Model<Spon> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "sponId", type = IdType.AUTO)
    private Integer sponId;

    @TableField("sponName")
    private String sponName;

    private String site;

    @TableField("logoUrl")
    private String logoUrl;

    @TableField("orgId")
    private Integer orgId;


    @Override
    protected Serializable pkVal() {
        return this.sponId;
    }

}
