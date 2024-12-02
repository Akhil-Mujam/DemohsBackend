package com.example.Demohs.Service;

import com.example.Demohs.Dto.AllTeachersDto;
import com.example.Demohs.Entity.AllTeachers;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AllTeachersService {

   String addTeacher(AllTeachersDto allTeachersDto);

    String deleteTeacher(String regNo);

    String updateTeacher(AllTeachersDto allTeachersDto);

  AllTeachers getTeacher(String regNo);


    Page<AllTeachersDto> getAllTeachers(int page, int size);


}
