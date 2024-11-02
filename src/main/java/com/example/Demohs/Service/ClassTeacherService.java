package com.example.Demohs.Service;

import com.example.Demohs.Dto.AllTeachersDto;
import com.example.Demohs.Dto.TeacherClassDto;

public interface ClassTeacherService {

    public String makeClassTeacher(String regNo,String ClassName,String classSection);

    public AllTeachersDto getTeacherByClassName(String ClassName);

}
