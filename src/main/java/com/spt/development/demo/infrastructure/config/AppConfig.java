package com.spt.development.demo.infrastructure.config;

import com.spt.development.demo.core.usecase.book.ManageBooksUseCase;
import com.spt.development.demo.infrastructure.adapter.db.BookPersistenceGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public ManageBooksUseCase manageBooksUseCase(BookPersistenceGateway bookPersistenceGateway) {
        return new ManageBooksUseCase(bookPersistenceGateway);
    }
}
