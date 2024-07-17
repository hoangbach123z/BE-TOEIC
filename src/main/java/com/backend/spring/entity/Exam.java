package com.backend.spring.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "exam" ,uniqueConstraints = {
    @UniqueConstraint(columnNames = "exam_name"),
}
)
public class Exam implements Serializable {
  @Id
  @Size(max = 36)
  @GeneratedValue(generator = "uuid-hibernate-generator")
  @GenericGenerator(name = "uuid-hibernate-generator", strategy = "org.hibernate.id.UUIDGenerator")
  @Column(name = "exam_id")
  private String examId;

  @Column(name = "exam_name", nullable = false, length = 255)
  private String examName;

  @Column(name = "exam_type", nullable = false)
  private Integer examType; // 0 for mini, 1 for full

  @Column(name = "exam_duration", nullable = false)
  private Integer examDuration; // Thời gian duy trì (số giờ hoặc số phút)

  @Column(name = "exam_status", nullable = false)
  private Integer examStatus = 1;

  @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
  private LocalDateTime createdAt;

  @Column(name = "updated_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
  private LocalDateTime updatedAt;

  public Exam(String examName, Integer examType, Integer examStatus) {
    this.examName = examName;
    this.examType = examType;
    this.examStatus = examStatus;
    this.createdAt = LocalDateTime.now();
    this.updatedAt = LocalDateTime.now();
    if (examType == 0) { // mini exam
      this.examDuration = 3600; // 1 hour in seconds
    } else if (examType == 1) { // full exam
      this.examDuration = 7200; // 2 hour in seconds
    } else {
      throw new IllegalArgumentException("Invalid exam type.");
    }
  }
}


