package com.spt.development.demo.infrastructure.adapter.db;

import com.spt.development.audit.spring.Audited;
import com.spt.development.demo.core.model.Book;
import com.spt.development.demo.core.port.persistence.BookPersistenceGatewayOutputPort;
import com.spt.development.demo.infrastructure.adapter.db.repository.BookRepository;
import com.spt.development.demo.infrastructure.adapter.util.Constants;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import javax.annotation.concurrent.Immutable;
import java.util.List;
import java.util.Optional;

@Service
@Immutable
@AllArgsConstructor
public class BookPersistenceGateway implements BookPersistenceGatewayOutputPort {
    // NOTE. Having a gateway that delegates to a repository for this simple model is clearly overkill, but just serves to prove logging
    // for classes annotated with @Service annd a convenient place to include the auditing. Auditing could instead have been added to the
    // controllers
    private final BookRepository bookRepository;

    @Audited(type = Constants.Auditing.Type.BOOK, subType = Constants.Auditing.SubType.CREATED)
    public @Audited.Id("id") Book create(@NonNull @Audited.Detail Book book) {
        return bookRepository.create(book);
    }

    public Optional<Book> read(long id) {
        return bookRepository.read(id);
    }

    public List<Book> readAll() {
        return bookRepository.readAll();
    }

    @Audited(type = Constants.Auditing.Type.BOOK, subType = Constants.Auditing.SubType.UPDATED)
    public Optional<Book> update(@NonNull @Audited.Id("id") @Audited.Detail Book book) {
        return bookRepository.update(book);
    }

    @Audited(type = Constants.Auditing.Type.BOOK, subType = Constants.Auditing.SubType.DELETED)
    public void delete(@Audited.Id long id) {
        bookRepository.delete(id);
    }
}
