package com.example.tornet.reposotory;

import com.example.tornet.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findById(Long id);

    List<Category> findAll();

    void deleteById(Long id);

    boolean existsById(Long id);
    void save(String category);
}
