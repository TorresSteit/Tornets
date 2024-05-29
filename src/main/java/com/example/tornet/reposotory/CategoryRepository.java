package com.example.tornet.reposotory;

import com.example.tornet.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    <S extends Category> S save(S entity);

    // Ищет сущность по заданному идентификатору.
    Optional<Category> findById(Long id);

    // Возвращает список всех сущностей данного типа.
    List<Category> findAll();

    // Удаляет сущность по заданному идентификатору.
    void deleteById(Long id);

    // Проверяет, существует ли сущность с заданным идентификатором.
    boolean existsById(Long id);

    void save(String category);
}
