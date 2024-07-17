package com.backend.spring.service;

import com.backend.spring.data.ResponseData;
import com.backend.spring.dto.UserExamDTO;
import com.backend.spring.entity.UserExam;
import java.util.List;

public interface UserExamService {
  ResponseData<?> createUserExam(UserExamDTO userExamDTO);
  ResponseData<List<UserExam>> getListUser();
  ResponseData<UserExam> getDetailExamUser(String userExamID);


}
