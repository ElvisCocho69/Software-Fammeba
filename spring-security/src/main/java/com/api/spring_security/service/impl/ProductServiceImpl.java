package com.api.spring_security.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.api.spring_security.dto.SaveProduct;
import com.api.spring_security.exception.ObjectNotFoundExeption;
import com.api.spring_security.persistence.entity.Category;
import com.api.spring_security.persistence.entity.Product;
import com.api.spring_security.persistence.repository.ProductRepository;
import com.api.spring_security.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Page<Product> findAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    public Optional<Product> findOneById(Long productId) {
        return productRepository.findById(productId);
    }

    @Override
    public Product createOne(SaveProduct saveProduct) {
        
        Product product = new Product();
        product.setName(saveProduct.getName());
        product.setPrice(saveProduct.getPrice());
        product.setStatus(Product.ProductStatus.ENABLED);

        Category category = new Category();
        category.setId(saveProduct.getCategoryId());
        product.setCategory(category);

        return productRepository.save(product);

    }

    @Override
    public Product updateOneById(Long productId, SaveProduct saveProduct) {
        
        Product productFromBD = productRepository.findById(productId)
            .orElseThrow( () -> new ObjectNotFoundExeption("Product not found with id: " + productId) );

        productFromBD.setName(saveProduct.getName());
        productFromBD.setPrice(saveProduct.getPrice());

        Category category = new Category();
        category.setId(saveProduct.getCategoryId());
        productFromBD.setCategory(category);

        return productRepository.save(productFromBD);

    }

    @Override
    public Product disableOneById(Long productId) {
        Product productFromBD = productRepository.findById(productId)
            .orElseThrow( () -> new ObjectNotFoundExeption("Product not found with id: " + productId) );

        productFromBD.setStatus(Product.ProductStatus.DISABLED);

        return productRepository.save(productFromBD);
    }    

}
