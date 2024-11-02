package com.example.Demohs.util;

import com.example.Demohs.Entity.*;
import com.example.Demohs.Service.ExamResultService;
import com.example.Demohs.Service.ExamTypeService;
import com.example.Demohs.Service.StudentMasterService;
import com.example.Demohs.Service.SubjectService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class SheetToExamResultConvertor {

    @Autowired
    private StudentMasterService studentMasterService;

    @Autowired
    private ExamTypeService examTypeService;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    ExamResultService examResultService;

    public List<ExamResult> saveExamResultsFromExcel(MultipartFile file, String examType) {
        List<ExamResult> examResultList = new ArrayList<>();

        // Fetch and map ExamType entity
        ExamType examTypeEntity = examTypeService.findByExamName(examType);
        if (examTypeEntity == null) {
            throw new IllegalArgumentException("Invalid exam type: " + examType);
        }

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            // Retrieve and map all Subject entities from the header row
            Row headerRow = sheet.getRow(0);
            List<Subject> subjects = new ArrayList<>();
            for (int i = 1; i < headerRow.getLastCellNum(); i++) {
                String subjectName = headerRow.getCell(i).getStringCellValue();
                Subject subject = subjectService.findBySubjectName(subjectName);
                if (subject != null) {
                    subjects.add(subject);
                } else {
                    throw new IllegalArgumentException("Subject not found: " + subjectName);
                }
            }
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                String studentRegNo = row.getCell(0).getStringCellValue();

                // Fetch and map StudentMaster entity
                StudentMaster student = studentMasterService.findByRegNo(studentRegNo);
                if (student == null) {
                    throw new IllegalArgumentException("Student not found: " + studentRegNo);
                }
                ExamResult examResult = new ExamResult();
                examResult.setStudentMaster(student);
                examResult.setExamType(examTypeEntity);

                // Create SubjectMarks entries for each subject
                List<SubjectMarks> subjectMarksList = new ArrayList<>();
                double total=0;
                boolean isPass=true;
                for (int j = 1; j < row.getLastCellNum(); j++) {
                    Cell cell= row.getCell(j);
                    String marks =  "";
                    String grade="";
                    if(cell.getCellType().equals(CellType.STRING)) {
                        if (cell.getStringCellValue().equalsIgnoreCase("A")||cell.getStringCellValue().equalsIgnoreCase("AB"))
                        {
                            isPass=false;
                            marks=cell.getStringCellValue();
                            grade="F";

                        }
                    }
                    else if (cell.getCellType().equals(CellType.NUMERIC)) {
                        if(cell.getNumericCellValue()<=35)
                        {
                            isPass=false;
                        }
                        total=total+cell.getNumericCellValue();
                        grade=calculateGrade(cell.getNumericCellValue());
                        marks=String.valueOf(cell.getNumericCellValue());

                    }
                    Subject subject = subjects.get(j - 1);
                    SubjectMarks subjectMarks = new SubjectMarks();
                    subjectMarks.setExamResult(examResult);  // Link to ExamResult
                    subjectMarks.setSubject(subject);
                    subjectMarks.setMarks(marks);
                    subjectMarks.setGrade(grade);
                    subjectMarksList.add(subjectMarks);
                }
                if(!isPass)
                    total=Integer.MIN_VALUE;
                examResult.setTotal(total);
                examResult.setSubjectMarks(subjectMarksList);
                examResultList.add(examResult);
            }

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        return examResultList;
    }
    public void updateExamResultsFromExcel(MultipartFile file, String examType) {
        ExamType examTypeEntity = examTypeService.findByExamName(examType);
        if (examTypeEntity == null) {
            throw new IllegalArgumentException("Invalid exam type: " + examType);
        }

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            Row headerRow = sheet.getRow(0);
            List<Subject> subjects = new ArrayList<>();
            for (int i = 1; i < headerRow.getLastCellNum(); i++) {
                String subjectName = headerRow.getCell(i).getStringCellValue();
                Subject subject = subjectService.findBySubjectName(subjectName);
                if (subject != null) {
                    subjects.add(subject);
                } else {
                    throw new IllegalArgumentException("Subject not found: " + subjectName);
                }
            }

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                String studentRegNo = row.getCell(0).getStringCellValue();
                StudentMaster student = studentMasterService.findByRegNo(studentRegNo);
                ExamResult existingExamResult =examResultService.findByStudentMasterAndExamType(student, examTypeEntity);
                if (existingExamResult == null) {
                    throw new IllegalArgumentException("Exam result not found for student: " + studentRegNo + " and exam type: " + examType);
                }
                boolean isPass=true;
                double total=0;
                for (int j = 1; j < row.getLastCellNum(); j++) {
                    Cell cell= row.getCell(j);
                    String marks =  "";
                    String grade="";
                    if(cell.getCellType().equals(CellType.STRING)) {
                        if (cell.getStringCellValue().equalsIgnoreCase("A")||cell.getStringCellValue().equalsIgnoreCase("AB"))
                        {
                            isPass=false;
                            marks=cell.getStringCellValue();
                            grade="F";

                        }
                    }
                    else if (cell.getCellType().equals(CellType.NUMERIC)) {
                        if(cell.getNumericCellValue()<=35)
                        {
                            isPass=false;
                        }
                        marks=String.valueOf(cell.getNumericCellValue());
                        grade=calculateGrade(cell.getNumericCellValue());

                    }
                    else {
                        total=total+cell.getNumericCellValue();
                    }
                    Subject subject = subjects.get(j - 1);
                    for (SubjectMarks subjectMarks : existingExamResult.getSubjectMarks()) {
                        if (subjectMarks.getSubject().getSubjectId().equals(subject.getSubjectId())) {
                            if (!subjectMarks.getMarks().equals(marks)) {
                                subjectMarks.setMarks(marks);
                                subjectMarks.setGrade(grade);
                            }
                        }
                    }
                }
                if(!isPass)
                    total=Integer.MIN_VALUE;
                existingExamResult.setTotal(total);
                List<ExamResult> examResultList=new ArrayList<>();
                examResultList.add(existingExamResult);
                examResultService.save(examResultList);//ave the updated exam result
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
    public  String calculateGrade(double total) {
        if (total >= 90) {
            return "A+";
        } else if (total >= 80) {
            return "A";
        } else if (total >= 70) {
            return "B+";
        } else if (total >= 60) {
            return "B";
        } else if (total >= 50) {
            return "C+";
        } else if (total >= 35) {
            return "C";
        } else {
            return "F";
        }
    }

}
