package com.spt.development.demo.core.application.usecase.book;

import com.spt.development.demo.core.domain.Book;
import com.spt.development.demo.core.application.port.persistence.BookPersistenceGatewayOutputPort;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.concurrent.Immutable;
import java.util.List;
import java.util.Optional;

@Slf4j
@Immutable
@Transactional
@AllArgsConstructor
public class ManageBooksUseCase implements ManageBooksInputPort {
    private final BookPersistenceGatewayOutputPort bookPersistenceGatewayOutputPort;

    public Book create(@NonNull Book book) {
        return bookPersistenceGatewayOutputPort.create(book.toBuilder().id(null).build());
    }

    public Optional<Book> read(long id) {
        return bookPersistenceGatewayOutputPort.read(id);
    }

    public List<Book> readAll() {
        return bookPersistenceGatewayOutputPort.readAll();
    }

    public Optional<Book> update(long id, @NonNull Book book) {
        if (!Long.valueOf(id).equals(book.getId())) {
            LOG.warn("ID on book payload: {}, does not match ID in URL: {}. Using ID from URL", book.getId(), id);
        }
        return bookPersistenceGatewayOutputPort.update(book.toBuilder().id(id).build());
    }

    public void delete(long id) {
        bookPersistenceGatewayOutputPort.delete(id);
    }
}
