package com.backend.spring.dto;

import lombok.Data;

@Data
public class ExamDTO {
  private String examId;
  private String examName;
  private Integer examType;// Thời gian duy trì (số giờ hoặc số phút)
  private Integer examStatus = 1;
}

