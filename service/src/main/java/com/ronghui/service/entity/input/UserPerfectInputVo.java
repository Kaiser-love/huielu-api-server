package com.ronghui.service.entity.input;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ronghui.service.common.util.DateUtil;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * @program: monitor
 * @description:
 * @author: dongyang_wu
 * @create: 2019-07-12 11:37
 */
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class UserPerfectInputVo {
    private String account;
    private String password;
    private String qq;
    private String avatarUrl;
    private String nickName;
    private String country;
    private String province;
    private String city;
    private String realName;
    private String email;
    private String phone;
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
    @JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
    private Timestamp birthday;
    private Byte gender;
    private String job;
}