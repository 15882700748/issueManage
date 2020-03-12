package com.zst.springbootstudy.demo01.entity;

import java.math.BigDecimal;
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
public class Organization extends Model<Organization> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "orgId", type = IdType.AUTO)
    private Integer orgId;

    private String account;

    private String password;

    private String name;

    private BigDecimal tel;

    private String email;

    @TableField("logoUrl")
    private String logoUrl;

    @TableField("createTime")
    private LocalDateTime createTime;

    @TableField("orgDesc")
    private String orgDesc;

    @TableField("webSite")
    private String webSite;


    @Override
    protected Serializable pkVal() {
        return this.orgId;
    }

}
