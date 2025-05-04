package com.api.spring_security.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.api.spring_security.dto.SaveCategory;
import com.api.spring_security.persistence.entity.Category;

public interface CategoryService {

    Page<Category> findAll(Pageable pageable);

    Optional<Category> findOneById(Long categoryId);

    Category createOne(SaveCategory saveCategory);

    Category updateOneById(Long categoryId, SaveCategory saveCategory);

    Category disableOneById(Long categoryId);

}
