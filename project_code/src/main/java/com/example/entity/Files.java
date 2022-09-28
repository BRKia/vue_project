package com.example.entity;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

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
 * @since 2022-08-06
 */
@Getter
@Setter
@TableName("sys_file")
@ApiModel(value = "File对象", description = "")
public class Files implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("文件id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("文件名称")
    private String name;

    @ApiModelProperty("文件类型")
    private String type;

    @ApiModelProperty("文件大小（KB）")
    private Long size;

    @ApiModelProperty("下载链接")
    private String url;

    @ApiModelProperty("是否删除")
    private Boolean isDelete;

    @ApiModelProperty("是否禁用")
    private Boolean enable;

    @ApiModelProperty("文件标识")
    private String md5;

    @ApiModelProperty("上传作者")
    private String uploadUser;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")//这里加上json格式注解指定样式，前端获取到时间就是规定的样式！
    @ApiModelProperty("上传时间")
    private LocalDateTime UploadTime;

}