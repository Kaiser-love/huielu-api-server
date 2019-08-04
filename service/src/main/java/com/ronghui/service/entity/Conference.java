package com.ronghui.service.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.*;
import com.ronghui.service.common.util.DateUtil;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author wdy
 * @since 2019-05-18
 */
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
@TableName("conference")
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity(name = "conference")
public class Conference extends Model<Conference> implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @Column(name = "name")
    @TableField("name")
    private String name;
    @Column(name = "address")
    @TableField("address")
    private String address;
    @Column(name = "start_time")
    @TableField("start_time")
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
    @JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
    private Timestamp startTime;
    @Column(name = "end_time")
    @TableField("end_time")
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
    @JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
    private Timestamp endTime;
    @Column(name = "owner")
    @TableField("owner")
    private String owner;
    @Column(name = "open_url")
    @TableField("open_url")
    private String openUrl;
    @Column(name = "classification")
    @TableField("classification")
    private String classification;
    @Column(name = "number")
    @TableField("number")
    private String number;
    @Column(name = "logo")
    @TableField("logo")
    private String logo;
    @Column(name = "is_payment")
    @TableField("is_payment")
    private Byte isPayment;
    @Column(name = "clicks_number")
    @TableField("clicks_number")
    private Long clicksNumber;
    @Column(name = "join_number")
    @TableField("join_number")
    private Long joinNumber;
    @Column(name = "status")
    @TableField("status")
    private Integer status;
    @Column(name = "description")
    @TableField("description")
    private String description;
    @OneToMany(mappedBy = "conference")
    @TableField(exist = false)
    @JSONField(serialize = false)
    private List<Process> processes;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
