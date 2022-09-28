package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 *
 * </p>
 *
 * @author brk
 * @since 2022-07-16
 */
@Getter
@Setter
@TableName("sys_course_02")
@ApiModel(value = "Course02对象", description = "")
public class Course02 implements Serializable {

    private static final long serialVersionUID = 1L;

    // 若此处没写Api, 则无法与数据库链接
    @ApiModelProperty("课程性质")
    private String property;

    @ApiModelProperty("学期")
    private Integer term;

    @ApiModelProperty("课程名称")
    private String name;

    @ApiModelProperty("学分")
    private Double score;

    @ApiModelProperty("课程状态")
    private String state;

    @ApiModelProperty("课程ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("授课老师ID")
    private Integer teacherId;


}