package com.backend.spring.service;

import com.backend.spring.data.ResponseData;
import com.backend.spring.dto.ExamDTO;
import com.backend.spring.entity.Exam;
import java.util.List;

public interface IExamService {
  ResponseData<?> createExam(ExamDTO examDTO);
  ResponseData<?> deleteExam(String examID);
  ResponseData<?> updateExam(String examID,ExamDTO examDTO);
  ResponseData<?> updateExamStatus(String examID,Integer newStatus);
  ResponseData<List<Exam>> getListExams();
  ResponseData<Exam> getDetailExam(String id);
  ResponseData<List<Exam>> getFullTest();
  ResponseData<List<Exam>> getMiniTest();
  ResponseData<Long> countTotalExams();
  ResponseData<List<Exam>> getEnableFullTest();

}
