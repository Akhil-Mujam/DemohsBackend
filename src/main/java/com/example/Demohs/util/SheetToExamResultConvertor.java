package com.example.Demohs.util;

import com.example.Demohs.Dto.ExamResultDto;
import com.example.Demohs.Entity.*;
import com.example.Demohs.Exception.ResourceNotFoundException;
import com.example.Demohs.Repository.ExamResultRepository;
import com.example.Demohs.Repository.StudentMasterRepository;
import com.example.Demohs.Repository.SubjectMarkRepository;
import com.example.Demohs.Service.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
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
    ExamResultRepository examResultRepository;

    @Autowired
    ClassTeacherService classTeacherService;

    @Autowired
    StudentMasterRepository studentMasterRepository;

    @Autowired
    SubjectMarkRepository subjectMarkRepository;

    public void saveExamResultsFromExcel(MultipartFile file, String examType, String teacherRegNo) {

        ExamType examTypeEntity = fetchExamType(examType);
        if (examTypeEntity == null) throw new ResourceNotFoundException("Exam type not found");

        List<Subject> subjects = fetchSubjectsFromHeader(file);
        if (subjects.isEmpty()) throw new ResourceNotFoundException("No subjects found in Excel header");

        TeacherClass allTeachers = classTeacherService.getClassTeacher(teacherRegNo);
        if (allTeachers == null) throw new ResourceNotFoundException("Teacher not found");

        String className = allTeachers.getClassEntity();
        String classSection = allTeachers.getClassSection();
        List<StudentMaster> studentMasters = studentMasterRepository.findByClassesEntityAndClassSection(className, classSection);

        if (studentMasters.isEmpty()) throw new ResourceNotFoundException("No students found for the specified class and section");

        List<String> studentsRegNoList = new ArrayList<>();
        int studentCount = studentMasters.size();

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            int rowCount = sheet.getLastRowNum(); // Row count excluding the header
            if (studentCount != rowCount) {
                throw new ResourceNotFoundException("Excel sheet row count does not match the number of class students");
            }

            for (int i = 1; i <= rowCount; i++) { // Start from row 1 to skip the header
                Row row = sheet.getRow(i);
                if (row == null) {
                    System.out.println("Skipping empty row at index: " + i);
                    continue;
                }

                 processExamResultRow(row, subjects, examTypeEntity, studentMasters, studentsRegNoList);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error reading Excel file", e);
        }
    }


    public void updateExamResultsFromExcel(MultipartFile file, String examType,String teacherRegNo) {
        ExamType examTypeEntity = fetchExamType(examType);
        List<Subject> subjects = fetchSubjectsFromHeader(file);
        TeacherClass allTeachers=classTeacherService.getClassTeacher(teacherRegNo);
        String className=allTeachers.getClassEntity();
        String classSection=allTeachers.getClassSection();
        List<StudentMaster> studentMasters=studentMasterRepository.findByClassesEntityAndClassSection(className,classSection);
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

    private void processExamResultRow(Row row, List<Subject> subjects, ExamType examTypeEntity,
                                            List<StudentMaster> studentMasterList, List<String> studentRegNoList) {
        String studentRegNo = row.getCell(0).getStringCellValue();
        StudentMaster student = studentMasterService.findByRegNo(studentRegNo);

        if (studentRegNoList.contains(studentRegNo)) {
            throw new IllegalArgumentException("Excel contains redundant data for student: " + studentRegNo);
        }

        boolean isValidStudent = studentMasterList.stream()
                .anyMatch(s -> s.getRegNo().equalsIgnoreCase(studentRegNo));

        if (!isValidStudent) {
            throw new IllegalArgumentException("Excel contains data for another section's student: " + studentRegNo);
        }

        studentRegNoList.add(studentRegNo);

        ExamResult existingExamResult = examResultService.findByStudentMasterAndExamType(student, examTypeEntity);
        if (existingExamResult != null) {
            throw new IllegalArgumentException("Exam result data already exists for student: " + studentRegNo);
        }

        boolean isPass = true;
        double total = 0;

        // Step 1: Create and save ExamResult
        ExamResult examResult = new ExamResult();
        examResult.setExamResultId(UUID.randomUUID());
        examResult.setStudentMaster(student);
        examResult.setExamType(examTypeEntity);
        examResult.setTotal(0); // Temporary value for now
        examResult = examResultRepository.save(examResult); // Save the ExamResult first

        // Step 2: Generate and save SubjectMarks
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
                if(examTypeEntity.getExamName().contains("FA"))
                {
                    grade = calculateFAGrade(numericMarks);
                }
                else {
                    grade = calculateGrade(numericMarks);
                }

            }

            SubjectMarks subjectMarks = new SubjectMarks();
            subjectMarks.setExamResult(examResult); // Associate with saved ExamResult
            subjectMarks.setSubject(subjects.get(j - 1));
            subjectMarks.setMarks(marks);
            subjectMarks.setGrade(grade);
            subjectMarks.setId(UUID.randomUUID());

            subjectMarksList.add(subjectMarks);

            if (grade.equals("F")) {
                isPass = false;
            } else if (!marks.equals("A") && !marks.equals("AB")) {
                total += Double.parseDouble(marks);
            }
        }

        subjectMarkRepository.saveAll(subjectMarksList); // Save SubjectMarks

        // Step 3: Update ExamResult with calculated total
        examResult.setSubjectMarks(subjectMarksList);
       // examResult.setTotal(isPass ? total : Integer.MIN_VALUE);
        examResult.setTotal( total );
        examResultRepository.save(examResult); // Update ExamResult with subject marks
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

        List<SubjectMarks> updatedSubjectMarksList = generateSubjectMarksList(row, subjects, existingExamResult, examTypeEntity);
        for (SubjectMarks subjectMarks : updatedSubjectMarksList) {
//            if (subjectMarks.getGrade().equals("F")) {
//                isPass = false;
//                break;
//            }
            if (!subjectMarks.getMarks().equals("A") && !subjectMarks.getMarks().equals("AB")) {
                total += Double.parseDouble(subjectMarks.getMarks());
            }
        }

        // Set total to Integer.MIN_VALUE if any subject has a failing grade
        //existingExamResult.setTotal(isPass ? total : Integer.MIN_VALUE);
        existingExamResult.setTotal(total);
        existingExamResult.setSubjectMarks(updatedSubjectMarksList);
        existingExamResult.setExamResultId(existingExamResult.getExamResultId());

        examResultService.save(Collections.singletonList(existingExamResult));
    }

    private List<SubjectMarks> generateSubjectMarksList(Row row, List<Subject> subjects, ExamResult examResult, ExamType examTypeEntity) {


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
                if(examTypeEntity.getExamName().contains("FA"))
                {
                    grade = calculateFAGrade(numericMarks);
                }
                else {
                    grade = calculateGrade(numericMarks);
                }
            }
            Subject subject=subjects.get(j-1);
            Optional<SubjectMarks> subjectMarksOptional=subjectMarkRepository.findByExamResultAndSubject(examResult,subject);
            SubjectMarks subjectMarks = new SubjectMarks();
            if(subjectMarksOptional.isEmpty())
            {
                subjectMarks.setId(UUID.randomUUID());
                subjectMarks.setExamResult(null);
                subjectMarks.setSubject(subject);
            }
            else {
                subjectMarks=subjectMarksOptional.get();
            }
            subjectMarks.setMarks(marks);
            subjectMarks.setGrade(grade);
            subjectMarksList.add(subjectMarks);
        }

        return subjectMarkRepository.saveAll(subjectMarksList);
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

    public String calculateFAGrade(double marks) {
        if(marks>50) throw new IllegalArgumentException("Marks Should not be Greater than 100");
        if (marks >= 50) return "A+";
        if (marks >= 40) return "A";
        if (marks >= 30) return "B+";
        if (marks >= 20) return "B";
        if (marks >= 10) return "C+";
        if (marks >= 5) return "C";
        if(marks<0) throw new IllegalArgumentException("Marks should not be Negative");
        return "F";
    }
}
