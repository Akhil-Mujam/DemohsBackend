package com.example.Demohs.Service;

import com.example.Demohs.Dto.AllTeachersDto;
import com.example.Demohs.Dto.TeacherClassDto;
import com.example.Demohs.Entity.TeacherClass;

public interface ClassTeacherService {

    public String makeClassTeacher(String regNo,String ClassName,String classSection);

    public AllTeachersDto getTeacherByClassName(String ClassName);

    public TeacherClass getClassTeacher(String regNo);

}
