package com.spt.development.demo.infrastructure.adapter.repository;

import com.spt.development.demo.infrastructure.adapter.dao.BookDao;
import com.spt.development.demo.core.model.Book;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Repository;

import javax.annotation.concurrent.Immutable;
import java.util.List;
import java.util.Optional;

@Immutable
@Repository
@AllArgsConstructor
public class BookRepository {
    // NOTE. Having a repository that delegates to a DAO for this simple model is clearly overkill, but just serves to prove logging
    // for repositories and DAOs.
    private final BookDao bookDao;

    public Book create(@NonNull Book book) {
        return bookDao.create(book);
    }

    public Optional<Book> read(long id) {
        return bookDao.read(id);
    }

    public List<Book> readAll() {
        return bookDao.readAll();
    }

    public Optional<Book> update(@NonNull Book book) {
        return bookDao.update(book);
    }

    public void delete(long id) {
        bookDao.delete(id);
    }
}
