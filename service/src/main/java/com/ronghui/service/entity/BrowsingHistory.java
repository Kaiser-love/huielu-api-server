package com.ronghui.service.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.*;
import com.ronghui.service.common.util.DateUtil;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

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
@TableName("browsing_history")
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity(name = "browsing_history")
public class BrowsingHistory extends Model<BrowsingHistory> implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @Column(name = "conference_id")
    @TableField("conference_id")
    private Long conferenceId;
    @Column(name = "user_id")
    @TableField("user_id")
    private Long userId;
    @Column(name = "browsing_time")
    @TableField("browsing_time")
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
    @JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
    private Timestamp browsingTime;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
