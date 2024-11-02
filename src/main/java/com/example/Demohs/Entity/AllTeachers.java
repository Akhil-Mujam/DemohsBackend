package com.example.Demohs.Entity;

import com.example.Demohs.Dto.AllTeachersDto;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
public class AllTeachers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long teacherId;

    @Column(nullable = false, unique = true)
    private String regNo;  // Teacher registration number

    private String firstName;

    private String lastName;

    private String phno;

    private String address;

    public AllTeachersDto toAllTeachersDto(AllTeachers allTeachers)
    {
        AllTeachersDto a  = new AllTeachersDto();

        a.setAddress(allTeachers.getAddress());
        a.setTeacherId(allTeachers.getTeacherId());
        a.setFirstName(allTeachers.getFirstName());
        a.setPhno(allTeachers.getPhno());
        a.setRegNo(allTeachers.getRegNo());
        a.setLastName(allTeachers.getLastName());

        return a;
    }


}
