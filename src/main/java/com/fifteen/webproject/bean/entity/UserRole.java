package com.fifteen.webproject.bean.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author by wzz
 * @implNote 2020/10/20 19:44
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "user_role")
public class UserRole implements Serializable {
    //  对应数据库的主键(uuid,自增id,雪花算法, redis,zookeeper)
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer roleId;
    private String roleName;

}
