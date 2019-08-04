package com.ronghui.service.common.base;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ronghui.service.common.util.DateUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * @program: BaseEntity
 * @description:
 * @author: dongyang_wu
 * @create: 2019-06-02 00:58
 */
@Data
public class BaseEntity<T extends Model> extends Model<T> implements Serializable {

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    @Column(name = "create_user")
    private Long createUser;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
    @JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
    @ApiModelProperty(value = "创建时间")
    @Column(name = "create_time")
    private Timestamp createTime;

    /**
     * 更新人
     */
    @ApiModelProperty(value = "更新人")
    @Column(name = "update_user")
    private Long updateUser;

    /**
     * 更新时间
     */
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
    @JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
    @ApiModelProperty(value = "更新时间")
    @Column(name = "update_time")
    private Timestamp updateTime;

    /**
     * 状态[1:正常]
     */
    @ApiModelProperty(value = "业务状态")
    @Column(name = "status")
    private Byte status;

    /**
     * 状态[0:未删除,1:删除]
     */
    @TableLogic
    @ApiModelProperty(value = "是否已删除")
    @Column(name = "is_deleted")
    private Byte isDeleted;
}
