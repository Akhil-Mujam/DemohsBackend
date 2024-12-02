package com.example.Demohs.Dto;

import com.example.Demohs.Entity.AllTeachers;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
@Builder
public class AllTeachersDto {

    private UUID teacherId;


    private String regNo;  // Teacher registration number

    private String firstName;

    private String lastName;

    @Size(min = 10, max = 10, message = "Phone number must be exactly 10 characters long")
    private String phno;

    private String address;

    private String password;

    private String role;

//    public AllTeachers toAllTeachers(AllTeachersDto allTeachersDto)
//    {
//        AllTeachers a = new AllTeachers();
//
//        a.setAddress(allTeachersDto.getAddress());
//        a.setTeacherId(allTeachersDto.getTeacherId());
//        a.setFirstName(allTeachersDto.getFirstName());
//        a.setPhno(allTeachersDto.getPhno());
//        a.setRegNo(allTeachersDto.getRegNo());
//        a.setLastName(allTeachersDto.getLastName());
//
//        return a;
//    }

}
