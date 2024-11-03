package com.example.Demohs.util;

import com.example.Demohs.Dto.ExamResultDto;
import com.example.Demohs.Entity.*;
import com.example.Demohs.Exception.ResourceNotFoundException;
import com.example.Demohs.Service.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class SheetToExamResultConvertor {

    @Autowired
    private StudentMasterService studentMasterService;

    @Autowired
    private ExamTypeService examTypeService;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private ExamResultService examResultService;

    @Autowired
    AllTeachersService allTeachersService;

    @Autowired
    ClassTeacherService classTeacherService;

    public List<ExamResult> saveExamResultsFromExcel(MultipartFile file, String examType,String teacherRegNo) {


        ExamType examTypeEntity = fetchExamType(examType);
        List<Subject> subjects = fetchSubjectsFromHeader(file);
        TeacherClass allTeachers=classTeacherService.getClassTeacher(teacherRegNo);
        String className=allTeachers.getClassEntity();
        String classSection=allTeachers.getClassSection();

        List<StudentMaster> studentMasters=studentMasterService.findByClassesEntityAndClassSection(className,classSection);
        int size=studentMasters.size();
        List<ExamResult> examResultList = new ArrayList<>();
        List<String> studentsRegNoList=new ArrayList<>();
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            if(size!=sheet.getLastRowNum())
                throw new ResourceNotFoundException("ExcelSheet does not match the no of class students");
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                ExamResult examResult = processExamResultRow(row, subjects, examTypeEntity,studentMasters,studentsRegNoList);
                examResultList.add(examResult);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return examResultList;
    }

    public void updateExamResultsFromExcel(MultipartFile file, String examType,String teacherRegNo) {
        ExamType examTypeEntity = fetchExamType(examType);
        List<Subject> subjects = fetchSubjectsFromHeader(file);
        TeacherClass allTeachers=classTeacherService.getClassTeacher(teacherRegNo);
        String className=allTeachers.getClassEntity();
        String classSection=allTeachers.getClassSection();
        List<StudentMaster> studentMasters=studentMasterService.findByClassesEntityAndClassSection(className,classSection);
        List<String> studentsRegNoList=new ArrayList<>();
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                updateExamResultRow(row, subjects, examTypeEntity,studentMasters,studentsRegNoList);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ExamType fetchExamType(String examType) {
        ExamType examTypeEntity = examTypeService.findByExamName(examType);
        if (examTypeEntity == null) {
            throw new IllegalArgumentException("Invalid exam type: " + examType);
        }
        return examTypeEntity;
    }

    private List<Subject> fetchSubjectsFromHeader(MultipartFile file) {
        List<Subject> subjects = new ArrayList<>();
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Row headerRow = workbook.getSheetAt(0).getRow(0);
            for (int i = 1; i < headerRow.getLastCellNum(); i++) {
                String subjectName = headerRow.getCell(i).getStringCellValue();
                Subject subject = subjectService.findBySubjectName(subjectName);
                if (subject != null) {
                    subjects.add(subject);
                } else {
                    throw new IllegalArgumentException("Subject not found: " + subjectName);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return subjects;
    }

    private ExamResult processExamResultRow(Row row, List<Subject> subjects, ExamType examTypeEntity,List<StudentMaster> studentMasterList,List<String> studentRegNoList) {
        String studentRegNo = row.getCell(0).getStringCellValue();
        StudentMaster student = studentMasterService.findByRegNo(studentRegNo);
        if(studentRegNoList.contains(studentRegNo))
        {
            throw new IllegalArgumentException("Excel contains Student Redundant data of "+studentRegNo);
        }
         AtomicBoolean sameSecAndClass= new AtomicBoolean(false);
        studentMasterList.forEach(studentMaster -> {
            if(studentMaster.getRegNo().equalsIgnoreCase(studentRegNo)) {
                sameSecAndClass.set(true);
            }
        });
        if(!sameSecAndClass.get())
        {
           throw  new IllegalArgumentException("Excel contains the data of the Other Section student "+studentRegNo);
        }
        studentRegNoList.add(studentRegNo);
        ExamResult examResultPresent=examResultService.findByStudentMasterAndExamType(student,examTypeEntity);
        if(examResultPresent!=null)
        {
            throw  new IllegalArgumentException("Exam Result Data Already Exist for these Students");
        }
        ExamResult examResult = new ExamResult();
        examResult.setStudentMaster(student);
        examResult.setExamType(examTypeEntity);

        boolean isPass = true;
        double total = 0;

        List<SubjectMarks> subjectMarksList = generateSubjectMarksList(row, subjects, examResult);
        for (SubjectMarks subjectMarks : subjectMarksList) {
            if (subjectMarks.getGrade().equals("F")) {
                isPass = false;
                break;  // Exit the loop if a failing grade is found
            }
            if (!subjectMarks.getMarks().equals("A") && !subjectMarks.getMarks().equals("AB")) {
                total += Double.parseDouble(subjectMarks.getMarks());
            }
        }
        examResult.setTotal(isPass ? total : Integer.MIN_VALUE);
        examResult.setSubjectMarks(subjectMarksList);

        return examResult;
    }

    private void updateExamResultRow(Row row, List<Subject> subjects, ExamType examTypeEntity,List<StudentMaster> studentMasterList,List<String> studentRegNoList) {
        String studentRegNo = row.getCell(0).getStringCellValue();
        StudentMaster student = studentMasterService.findByRegNo(studentRegNo);
        if(studentRegNoList.contains(studentRegNo))
        {
            throw new IllegalArgumentException("Excel contains Student Redundant data of "+studentRegNo);
        }
        AtomicBoolean sameSecAndClass= new AtomicBoolean(false);
        studentMasterList.forEach(studentMaster -> {
            if(studentMaster.getRegNo().equalsIgnoreCase(studentRegNo)) {
                sameSecAndClass.set(true);
            }
        });
        if(!sameSecAndClass.get())
        {
            throw  new IllegalArgumentException("Excel contains the data of the Other Section student "+studentRegNo);
        }
        studentRegNoList.add(studentRegNo);
        ExamResult existingExamResult = examResultService.findByStudentMasterAndExamType(student, examTypeEntity);
        if (existingExamResult == null) {
            throw new IllegalArgumentException("Exam result not found for student: " + studentRegNo + " and exam type: " + examTypeEntity);
        }

        boolean isPass = true;
        double total = 0;

        List<SubjectMarks> updatedSubjectMarksList = generateSubjectMarksList(row, subjects, existingExamResult);
        for (SubjectMarks subjectMarks : updatedSubjectMarksList) {
            if (subjectMarks.getGrade().equals("F")) {
                isPass = false;
                break;  // Exit the loop if a failing grade is found
            }
            if (!subjectMarks.getMarks().equals("A") && !subjectMarks.getMarks().equals("AB")) {
                total += Double.parseDouble(subjectMarks.getMarks());
            }
        }

        // Set total to Integer.MIN_VALUE if any subject has a failing grade
        existingExamResult.setTotal(isPass ? total : Integer.MIN_VALUE);
        existingExamResult.setSubjectMarks(updatedSubjectMarksList);

        examResultService.save(Collections.singletonList(existingExamResult));
    }

    private List<SubjectMarks> generateSubjectMarksList(Row row, List<Subject> subjects, ExamResult examResult) {
        List<SubjectMarks> subjectMarksList = new ArrayList<>();
        for (int j = 1; j < row.getLastCellNum(); j++) {
            Cell cell = row.getCell(j);
            String marks;
            String grade;
            boolean isAbsent = cell.getCellType() == CellType.STRING &&
                    (cell.getStringCellValue().equalsIgnoreCase("A") ||
                            cell.getStringCellValue().equalsIgnoreCase("AB"));

            if (isAbsent) {
                marks = cell.getStringCellValue();
                grade = "F";
            } else {
                double numericMarks = cell.getNumericCellValue();
                marks = String.valueOf(numericMarks);
                grade = calculateGrade(numericMarks);
            }

            SubjectMarks subjectMarks = new SubjectMarks();
            subjectMarks.setExamResult(examResult);
            subjectMarks.setSubject(subjects.get(j - 1));
            subjectMarks.setMarks(marks);
            subjectMarks.setGrade(grade);
            subjectMarksList.add(subjectMarks);
        }
        return subjectMarksList;
    }

    public String calculateGrade(double marks) {
        if(marks>100) throw new IllegalArgumentException("Marks Should not be Greater than 100");
        if (marks >= 90) return "A+";
        if (marks >= 80) return "A";
        if (marks >= 70) return "B+";
        if (marks >= 60) return "B";
        if (marks >= 50) return "C+";
        if (marks >= 35) return "C";
        if(marks<0) throw new IllegalArgumentException("Marks should not be Negative");
        return "F";
    }
}
