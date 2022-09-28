package com.example.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("sys_dict")
@Data
public class Dict {
    private String name;
    private String type;
    private String value;
}
