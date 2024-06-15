package com.spt.development.demo.infrastructure.adapter.rest.controller;

import com.spt.development.demo.core.model.Book;
import com.spt.development.demo.core.usecase.book.ManageBooksInputPort;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1.0/books")
public class BookController {
    private final ManageBooksInputPort manageBooksUseCase;

    @PostMapping
    public ResponseEntity<Book> create(@RequestBody Book book) {
        return new ResponseEntity<>(manageBooksUseCase.create(book), HttpStatus.CREATED);
    }

    @GetMapping("/{id:\\d+}")
    public ResponseEntity<Book> read(@PathVariable long id) {
        return ResponseEntity.of(manageBooksUseCase.read(id));
    }

    @GetMapping
    public ResponseEntity<List<Book>> readAll() {
        return ResponseEntity.ok(manageBooksUseCase.readAll());
    }

    @PutMapping("/{id:\\d+}")
    public ResponseEntity<Book> update(@PathVariable long id, @RequestBody Book book) {
        return ResponseEntity.of(manageBooksUseCase.update(id, book));
    }

    @DeleteMapping("/{id:\\d+}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        manageBooksUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }
}
