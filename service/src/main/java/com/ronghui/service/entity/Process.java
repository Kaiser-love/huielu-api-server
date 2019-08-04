package com.ronghui.service.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
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
@TableName("process")
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity(name = "process")
@ToString(exclude = {"conference"})
public class Process extends Model<Process> implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @Column(name = "description")
    @TableField("description")
    private String description;
    @Column(name = "video_url")
    @TableField("video_url")
    private String videoUrl;
    @Column(name = "time_axis")
    @TableField("time_axis")
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
    @JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
    private Timestamp timeAxis;
    @Column(name = "type")
    @TableField("type")
    private Integer type;
    @TableField("conference_id")
    @Transient
    private Long conferenceId;
    @Column(name = "file_id")
    @TableField("file_id")
    private String fileId;
    @ManyToOne
    @JoinColumn(name = "conference_id", referencedColumnName = "id")
    @TableField(exist = false)
    private Conference conference;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
