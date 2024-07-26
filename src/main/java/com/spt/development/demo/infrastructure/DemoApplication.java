package com.spt.development.demo.infrastructure;

import com.spt.development.demo.core.application.usecase.book.ManageBooksUseCase;
import com.spt.development.logging.spring.annotation.EnableBeanLogging;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableBeanLogging(
    includeBasePackageClasses = { ManageBooksUseCase.class }
)
@SpringBootApplication
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
