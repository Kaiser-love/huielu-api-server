package com.ronghui.service.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@TableName("operation_log")
@Table(name = "operation_log", schema = "tags", catalog = "")
@Entity
public class OperationLog extends Model<OperationLog> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 日志描述
     */
    @TableField("log_description")
    @Column(name = "log_description")
    private String logDescription;
    /**
     * 方法参数
     */
    @TableField("action_args")
    @Column(name = "action_args")
    private String actionArgs;
    /**
     * 用户主键
     */
    @TableField("user_name")
    @Column(name = "user_name")
    private String userName;
    /**
     * 类名称
     */
    @TableField("class_name")
    @Column(name = "class_name")
    private String className;
    /**
     * 方法名称
     */
    @TableField("method_name")
    @Column(name = "method_name")
    private String methodName;
    @Column(name = "ip")
    private String ip;
    /**
     * 创建时间
     */
    @TableField("create_time")
    @Column(name = "create_time")
    private Long createTime;
    /**
     * 是否成功 1:成功 2异常
     */
    @Column(name = "succeed")
    private String succeed;
    /**
     * 异常堆栈信息
     */
    @Column(name = "message")
    private String message;

    /**
     * 模块名称
     */
    @TableField("model_name")
    @Column(name = "model_name")
    private String modelName;

    /**
     * 操作
     */
    @Column(name = "action")
    private String action;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
