package com.example.Demohs.Service;

import com.example.Demohs.Entity.ClassFeeStructureEntity;

import java.util.List;
import java.util.Optional;

public interface ClassFeeStructureService {

    public void updateClassFeeStructure(String className, int term1Fee, int term2Fee, int term3Fee);

    public Optional<ClassFeeStructureEntity> getFeeStructureByClassName(String className);

    public ClassFeeStructureEntity createOrUpdateFeeStructure(ClassFeeStructureEntity feeStructure);

    public List<ClassFeeStructureEntity> getAllClassesFeeStrutures();

    public ClassFeeStructureEntity updateTermFees(String className, Integer term1Fee, Integer term2Fee, Integer term3Fee);

}
