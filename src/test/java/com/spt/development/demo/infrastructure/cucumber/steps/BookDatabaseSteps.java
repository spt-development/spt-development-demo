package com.spt.development.demo.infrastructure.cucumber.steps;

import com.spt.development.demo.core.model.Book;
import com.spt.development.demo.infrastructure.adapter.repository.BookRepository;
import com.spt.development.test.integration.HttpTestManager;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;

public class BookDatabaseSteps {
    private static final class TestData extends RestApiSteps.TestData {
    }

    @Autowired private HttpTestManager httpTestManager;
    @Autowired private BookRepository bookRepository;

    @Given("a book exists in the database")
    public void aBookExistsInTheDatabase() {
        bookRepository.create(
                Book.builder()
                        .title(RestApiSteps.TestData.ValidBook.TITLE)
                        .blurb(RestApiSteps.TestData.ValidBook.BLURB)
                        .author(RestApiSteps.TestData.ValidBook.AUTHOR)
                        .rrp(RestApiSteps.TestData.ValidBook.RRP)
                        .build()
        );
    }

    @Then("the new book will be added to the database")
    public void theNewBookWillBeAddedToTheDatabase() {
        final long bookId = RestApiSteps.getBookIdFromResponse(httpTestManager);

        final Book book = bookRepository.read(bookId).orElseThrow(NoSuchElementException::new);

        assertThat(book).isNotNull();
        assertThat(book.getId()).isEqualTo(bookId);
        assertThat(book.getTitle()).isEqualTo(RestApiSteps.TestData.ValidBook.TITLE);
        assertThat(book.getBlurb()).isEqualTo(RestApiSteps.TestData.ValidBook.BLURB);
        assertThat(book.getAuthor()).isEqualTo(RestApiSteps.TestData.ValidBook.AUTHOR);
        assertThat(book.getRrp()).isEqualTo(RestApiSteps.TestData.ValidBook.RRP);
    }

    @Then("the last created book will be updated in the database")
    public void theLastCreatedBookWillBeUpdatedInTheDatabase() {
        final long bookId = RestApiSteps.getBookIdFromResponse(httpTestManager);

        final Book book = bookRepository.read(bookId).orElseThrow(NoSuchElementException::new);

        assertThat(book).isNotNull();
        assertThat(book.getId()).isEqualTo(bookId);
        assertThat(book.getTitle()).isEqualTo(TestData.UpdatedJob.TITLE);
        assertThat(book.getBlurb()).isEqualTo(TestData.UpdatedJob.BLURB);
        assertThat(book.getAuthor()).isEqualTo(TestData.UpdatedJob.AUTHOR);
        assertThat(book.getRrp()).isEqualTo(TestData.UpdatedJob.RRP);
    }

    @Then("the last created book will be deleted from the database")
    public void theLastCreatedBookWillBeDeletedFromTheDatabase() {
        final List<Book> books = bookRepository.readAll();

        assertThat(books).isEmpty();
    }
}
