package com.project.mega.triplus.util;

import lombok.Builder;
import lombok.Data;

@Data   // DTO (getter, setter 따로 안 만들어도 기능함)
@Builder
public class EmailMessage {
    private String to;      // 수신인
    private String subject; // 제목
    private String message; // 내용
}
