package com.example.Demohs.Dto;

import com.example.Demohs.Entity.AllTeachers;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
@Builder
public class AllTeachersDto {

    private Long teacherId;


    private String regNo;  // Teacher registration number

    private String firstName;

    private String lastName;

    private String phno;

    private String address;

    public AllTeachers toAllTeachers(AllTeachersDto allTeachersDto)
    {
        AllTeachers a = new AllTeachers();

        a.setAddress(allTeachersDto.getAddress());
        a.setTeacherId(allTeachersDto.getTeacherId());
        a.setFirstName(allTeachersDto.getFirstName());
        a.setPhno(allTeachersDto.getPhno());
        a.setRegNo(allTeachersDto.getRegNo());
        a.setLastName(allTeachersDto.getLastName());

        return a;
    }

}
