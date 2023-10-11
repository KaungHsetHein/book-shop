package com.example.bookshop.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record Bookdto(int id, String title, double price, String author, String imageUrl, LocalDate publishedDate,
                      String category) {
}
