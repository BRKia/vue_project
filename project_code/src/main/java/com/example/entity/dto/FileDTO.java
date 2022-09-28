package com.example.entity.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FileDTO {
    private String nickname;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime downloadTime; // 如果写成download_time，则前端查询不到
}
