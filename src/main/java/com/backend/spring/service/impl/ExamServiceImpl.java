package com.backend.spring.service.impl;

import com.backend.spring.data.ResponseData;
import com.backend.spring.dto.ExamDTO;
import com.backend.spring.entity.Exam;
import com.backend.spring.exception.BaseException;
import com.backend.spring.exception.CommonErrorCode;
import com.backend.spring.repository.ExamRepository;
import com.backend.spring.service.IExamService;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExamServiceImpl implements IExamService {

  private final ExamRepository examRepository;
  @Override
  public ResponseData<Exam> createExam(ExamDTO examDTO) {
    if(examRepository.existsByExamName(examDTO.getExamName())){
      throw new BaseException(CommonErrorCode.EXISTED_EXAM_NAME);
    }
    ResponseData<Exam> data = new ResponseData<>();
    Exam exam = Exam.builder()
        .examId(examDTO.getExamId())
        .examName(examDTO.getExamName())
        .examType(examDTO.getExamType())
        .examStatus(examDTO.getExamStatus())
        .build();
    data.setData(exam);
    data.setCode(200);
    data.setMessage("Thêm bài thi thành công!!!");
    return data;
  }

  @Override
  public ResponseData<?> deleteExam(String examID) {
    ResponseData<?> data = new ResponseData<>();
    if(!examRepository.existsByExamId(examID)){
      throw new BaseException(CommonErrorCode.NOT_EXISTED_EXAM_ID);
    }
    examRepository.deleteById(examID);
    data.setMessage("Xóa bài thi thành công");
  }

  @Override
  public ResponseData<Objects> updateExam(String examID, ExamDTO examDTO) {
    ResponseData<Objects> data = new ResponseData<>();
    if(examRepository.existsByExamName(examDTO.getExamName())){
      throw new BaseException(CommonErrorCode.EXISTED_EXAM_NAME);
    }
    Exam exam = examRepository.findById(examID).get();
    if(Objects.isNull(exam)){
      throw new BaseException(CommonErrorCode.NOT_EXISTED_EXAM_ID);
    }
    exam.setExamName(examDTO.getExamName());
    exam.setExamType(examDTO.getExamType());
    exam.setExamStatus(examDTO.getExamStatus());
//    tính toán lại examDuration dựa vào loại kỳ thi mới
    if(examDTO.getExamType() == 0) { // mini exam
      exam.setExamDuration(3600);// 60 minutes in seconds
    }else if(examDTO.getExamType() == 1){ //full exam
      exam.setExamDuration(7200); // 1 hour in seconds
    }else {
      throw new BaseException(CommonErrorCode.INVALID_EXAM_TYPE);
    }
    data.setMessage("Cập nhật Exam thành công");
    return null;
  }

  @Override
  public ResponseData<?> updateExamStatus(String examID, Integer newStatus) {
    return null;
  }

  @Override
  public ResponseData<List<Exam>> getListExams() {
    ResponseData<List<Exam>> data = new ResponseData<>();
    List<Exam> listExams = examRepository.findAll();
    data.setData(listExams);
    return data;
  }

  @Override
  public ResponseData<Exam> getDetailExam(String examID) {
    ResponseData<Exam> data = new ResponseData<>();
    Exam exam = examRepository.findById(examID).orElseThrow(()-> new BaseException(CommonErrorCode.NOT_EXISTED_EXAM_ID));
    data.setData(exam);
    return data;
  }

  @Override
  public ResponseData<List<Exam>> getFullTest() {
    ResponseData<List<Exam>> data = new ResponseData<>();
    List<Exam> listFulltest = examRepository.findByExamType(1);
    data.setData(listFulltest);
    return data ;
  }

  @Override
  public ResponseData<List<Exam>> getMiniTest() {
    ResponseData<List<Exam>> data = new ResponseData<>();
    List<Exam> listMinitest = examRepository.findByExamType(0);
    data.setData(listMinitest);
    return data ;
  }

  @Override
  public ResponseData<Long> countTotalExams() {
    long count = examRepository.count();
    ResponseData<Long> data = new ResponseData<>();
    data.setData(count);
    return data;
  }

  @Override
  public ResponseData<List<Exam>> getEnableFullTest() {
//    ResponseData<List<Exam>> data = new ResponseData<>();
//    List<Exam> listFulltest = examRepository.findByExamType(1);
//    // giữ lại các Exam có examStatus là 1
//    List<Exam> filteredFulltests = listFulltest.stream()
//        .filter(exam -> exam.getExamStatus() == 1 )
//        .toList();
//    data.setData(filteredFulltests);
//    return filteredFulltests;
    return  null;
  }
}
