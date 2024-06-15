package com.spt.development.demo.core.port.persistence;

import com.spt.development.demo.core.model.Book;
import lombok.NonNull;

import java.util.List;
import java.util.Optional;

public interface BookPersistenceGatewayOutputPort {
    Book create(@NonNull Book book);

    Optional<Book> read(long id);

    List<Book> readAll();

    Optional<Book> update(@NonNull Book book);

    void delete(long id);
}
