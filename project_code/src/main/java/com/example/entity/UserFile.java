package com.example.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
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
 * @since 2022-08-11
 */
@Getter
@Setter
@TableName("sys_user_file")
@ApiModel(value = "UserFile对象", description = "")
public class UserFile implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("文件id")
    private Integer fileId;

    @ApiModelProperty("用户id")
    private Integer userId;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")//这里加上json格式注解指定样式，前端获取到时间就是规定的样式！
    @ApiModelProperty("下载时间")
    private LocalDateTime downloadTime;


}