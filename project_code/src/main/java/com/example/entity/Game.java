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
 * @since 2022-07-24
 */
@Getter
@Setter
    @TableName("sys_game")
@ApiModel(value = "Game对象", description = "")
public class Game implements Serializable {

private static final long serialVersionUID = 1L;

                            @ApiModelProperty("游戏id")
                                        @TableId(value = "id", type = IdType.AUTO)
                                    private Integer id;

                        @ApiModelProperty("游戏链接")
                                private String url;


        }