package com.example.bookshop.dao;

import com.example.bookshop.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

public interface BookDao extends JpaRepositoryImplementation<Book,Integer> {
}
