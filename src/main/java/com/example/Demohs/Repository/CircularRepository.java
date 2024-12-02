package com.example.Demohs.Repository;

import com.example.Demohs.Entity.Circular;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CircularRepository extends JpaRepository<Circular, UUID> {}

