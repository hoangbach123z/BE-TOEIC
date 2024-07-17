package com.backend.spring.repository;

import com.backend.spring.entity.Exam;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExamRepository extends JpaRepository<Exam,String> {
  Boolean existsByExamName(String examName);
  Boolean existsByExamId(String examID);
  Boolean existsByExamNameAndExamIdNot(String examName,String examID);
  List<Exam> findByExamType(int examType);

}
