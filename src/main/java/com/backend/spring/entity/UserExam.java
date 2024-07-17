package com.backend.spring.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "user_exam")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserExam implements Serializable {
  @Id
  @Size(max = 36)
  @GeneratedValue(generator = "uuid-hibernate-generator")
  @GenericGenerator(name = "uuid-hibernate-generator", strategy = "org.hibernate.id.UUIDGenerator")
  @Column(name = "user_exam_id")
  private String userExamId;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ManyToOne
  @JoinColumn(name = "exam_id", nullable = false)
  private Exam exam;

  @Column(name = "completion_time")
  private Integer completionTime;

  @Column(name = "num_listening_correct_answers")
  private Integer numListeningCorrectAnswers;

  @Column(name = "listening_score")
  private Integer listeningScore;

  @Column(name = "num_reading_correct_answers")
  private Integer numReadingCorrectAnswers;

  @Column(name = "reading_score")
  private Integer readingScore;

  @Column(name = "total_score")
  private Integer totalScore;

  @Column(name = "num_correct_answers")
  private Integer numCorrectAnswers;

  @Column(name = "num_wrong_answers")
  private Integer numWrongAnswers;

  @Column(name = "num_skipped_questions")
  private Integer numSkippedQuestions;

  @Column(name = "goal_score")
  private Integer goalScore;

  @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
  private LocalDateTime createdAt;
}



