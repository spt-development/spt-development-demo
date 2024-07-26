package com.spt.development.demo.infrastructure.config;

import com.spt.development.demo.core.application.usecase.book.ManageBooksInputPort;
import com.spt.development.demo.core.application.usecase.book.ManageBooksUseCase;
import com.spt.development.demo.infrastructure.adapter.db.BookPersistenceGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public ManageBooksInputPort manageBooksUseCase(BookPersistenceGateway bookPersistenceGateway) {
        return new ManageBooksUseCase(bookPersistenceGateway);
    }
}
