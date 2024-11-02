package com.example.Demohs.Service;

import com.example.Demohs.Dto.AllTeachersDto;
import com.example.Demohs.Entity.AllTeachers;

import java.util.List;

public interface AllTeachersService {

    public String addTeacher(AllTeachersDto allTeachersDto);

    public String deleteTeacher(String regNo);

    public String updateTeacher(AllTeachersDto allTeachersDto);

    public AllTeachers getTeacher(String regNo);

    public List<AllTeachersDto> getAllTeachers();

}
