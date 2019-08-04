package com.ronghui.service.context;


import com.ronghui.service.entity.User;
import com.ronghui.service.redis.RedisUtil;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 访问用户信息
 *
 * @author dongyang_wu
 * @date 2019/4/12 12:22
 */
public class ContextUtil {
    public static final String AUTH = "HUIELU-AUTH";
    public static final String AUTHORIZATION = "HUIELU-TOKEN";
    private static final String BASE_USERNAME = "未携带token用户";
    private static User user = new User(BASE_USERNAME);

    public static User getUser() {
        RedisUtil redisUtil = SpringContextBeanService.getBean("RedisUtil");
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            String token = request.getHeader(AUTHORIZATION);
            if (!StringUtils.isEmpty(token)) {
                user = (User) redisUtil.sentinelGet(token, User.class);
            }
        } catch (Exception e) {
        }
        return user;
    }


    public static User getUserByToken(HttpServletRequest request) {
        String id = request.getHeader(AUTHORIZATION);
        RedisUtil redisUtil = SpringContextBeanService.getBean("RedisUtil");
        return (User) redisUtil.get(id);
    }
}
