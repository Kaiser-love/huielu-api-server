package com.ronghui.service.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.*;
import com.ronghui.service.common.base.BaseEntity;
import com.ronghui.service.common.util.DateUtil;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
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
@TableName("user")
@Entity(name = "user")
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@ToString(exclude = {"ppts", "directories"})
@EqualsAndHashCode(callSuper = false, exclude = {"ppts", "directories"})
public class User extends BaseEntity<User> implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @Column(name = "account")
    private String account;
    @Column(name = "password")
    private String password;
    @Column(name = "qq")
    private String qq;
    @Column(name = "openid")
    private String openid;
    @Column(name = "appid")
    private String appid;
    @Column(name = "avatar_url")
    private String avatarUrl;
    @Column(name = "nick_name")
    private String nickName;
    @Column(name = "country")
    private String country;
    @Column(name = "province")
    private String province;
    @Column(name = "city")
    private String city;
    @Column(name = "real_name")
    private String realName;
    @Column(name = "email")
    private String email;
    @Column(name = "phone")
    private String phone;
    @Column(name = "birthday")
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
    @JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
    private Timestamp birthday;
    @Column(name = "gender")
    private Byte gender;
    @Column(name = "job")
    private String job;
    @Transient
    @TableField(exist = false)
    private Integer pptCount;
    @Transient
    @TableField(exist = false)
    private Integer pdfCount;
    @OneToMany(mappedBy = "uid", fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @TableField(exist = false)
    @JSONField(serialize = false)
    private List<PPT> ppts;
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @TableField(exist = false)
    @JSONField(serialize = false)
    private List<Directory> directories;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public User(String account) {
        this.account = account;
    }

}
