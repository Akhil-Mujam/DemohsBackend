package com.example.Demohs.Service.Impl;

import com.example.Demohs.Dto.SubjectDto;
import com.example.Demohs.Entity.Subject;
import com.example.Demohs.Exception.ResourceNotFoundException;
import com.example.Demohs.Repository.SubjectRepository;
import com.example.Demohs.Service.SubjectService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SubjectServiceImpl implements SubjectService {
    @Autowired
    SubjectRepository subjectRepository;
    @Autowired
    ModelMapper modelMapper;
    @Override
    public Subject findBySubjectName(String subjectName) {
        subjectName=subjectName.toUpperCase();
        return subjectRepository.findBySubjectName(subjectName);
    }

    @Override
    public SubjectDto createSubject(String subjectName) {
        subjectName=subjectName.toUpperCase();
        Subject SubjectisAlreadyExistorNot =  subjectRepository.findBySubjectName(subjectName);
        if(SubjectisAlreadyExistorNot != null)
        {
            throw new IllegalArgumentException("subject already exists`: " + subjectName);
        }
        Subject subject=new Subject();
        subject.setSubjectId(UUID.randomUUID());
        subject.setSubjectName(subjectName);
        SubjectDto subjectDto = modelMapper.map(subjectRepository.save(subject),SubjectDto.class);
        return subjectDto;
    }

    @Override
    public List<SubjectDto> getAllSubjects() {
        // Fetch all subjects from the database
        List<Subject> subjects = subjectRepository.findAll();

        // Check if no subjects are found
        if (subjects.isEmpty()) {
            throw new ResourceNotFoundException("No subjects found.");
        }

        // Map the Subject entities to SubjectDto objects
        return subjects.stream()
                .map(subject -> modelMapper.map(subject, SubjectDto.class))
                .collect(Collectors.toList());
    }


    @Override
    public String DeleteSubject(UUID id) {
        // Check if the subject exists
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found with ID: " + id));

        // Delete the subject
        subjectRepository.delete(subject);

        // Return a confirmation message
        return "Subject with ID " + id + " has been deleted successfully.";
    }

}
