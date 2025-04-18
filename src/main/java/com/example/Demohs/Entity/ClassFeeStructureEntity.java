package com.example.Demohs.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClassFeeStructureEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String className; // Example: "10-A", "9-B"

    @Column(nullable = false)
    private Integer term1Fee;

    @Column(nullable = false)
    private Integer term2Fee;

    @Column(nullable = false)
    private Integer term3Fee;

    // Total fee is optional as it can be derived
    @Transient
    public Integer getTotalFee() {
        return term1Fee + term2Fee + term3Fee;
    }
}
