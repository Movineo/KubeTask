package com.movine.taskmanagementsystem.repository;

import com.movine.taskmanagementsystem.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}