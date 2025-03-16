package com.shivaji.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shivaji.entity.Products;

public interface ProductRepository extends JpaRepository<Products, Long> {

}
