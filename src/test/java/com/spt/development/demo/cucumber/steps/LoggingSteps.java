package com.spt.development.demo.cucumber.steps;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import com.spt.development.test.integration.HttpTestManager;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Then;
import org.apache.commons.lang3.StringUtils;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

import static com.spt.development.cid.web.filter.CorrelationIdFilter.CID_HEADER;
import static com.spt.development.cid.web.filter.MdcCorrelationIdFilter.MDC_CID_KEY;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

public class LoggingSteps {
    @Value("${spt.cid.mdc.disabled:false}")
    private boolean mdcDisabled;

    @Autowired private HttpTestManager httpTestManager;

    private Appender<ILoggingEvent> appender;

    @Before
    public void setUp() {
        appender = createMockAppender();
        getSptDemoLogger().addAppender(appender);
    }

    @SuppressWarnings("unchecked")
    private static Appender<ILoggingEvent> createMockAppender() {
        return Mockito.mock(Appender.class);
    }

    @After
    public void tearDown() {
        getSptDemoLogger().detachAppender(appender);
    }

    private Logger getSptDemoLogger() {
        final LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        return loggerContext.getLogger("com.spt.development.demo");
    }

    @Then("the book creation is logged at all tiers")
    public void theBookCreationIsLoggedAtAllTiers() {
        final String correlationId = httpTestManager.getResponseHeaderValue(CID_HEADER).get();

        assertThatMessageIsLogged(Level.INFO, correlationId, "BookController.create(");
        assertThatMessageIsLogged(Level.DEBUG, correlationId, "BookService.create(");
        assertThatMessageIsLogged(Level.DEBUG, correlationId, "BookRepository.create(");
        assertThatMessageIsLogged(Level.DEBUG, correlationId, "BookDao.create(");
        assertThatMessageIsLogged(Level.TRACE, correlationId, "BookDao.create Returned: Book");
        assertThatMessageIsLogged(Level.TRACE, correlationId, "BookRepository.create Returned: Book");
        assertThatMessageIsLogged(Level.TRACE, correlationId, "BookService.create Returned: Book");
        assertThatMessageIsLogged(Level.TRACE, correlationId, "BookController.create Returned: <201");
    }

    @Then("the book read is logged at all tiers")
    public void theBookReadIsLoggedAtAllTiers() {
        final String correlationId = httpTestManager.getResponseHeaderValue(CID_HEADER).get();

        assertThatMessageIsLogged(Level.INFO, correlationId, "BookController.read(");
        assertThatMessageIsLogged(Level.DEBUG, correlationId, "BookService.read(");
        assertThatMessageIsLogged(Level.DEBUG, correlationId, "BookRepository.read(");
        assertThatMessageIsLogged(Level.DEBUG, correlationId, "BookDao.read(");
        assertThatMessageIsLogged(Level.TRACE, correlationId, "BookDao.read Returned: Optional[Book");
        assertThatMessageIsLogged(Level.TRACE, correlationId, "BookRepository.read Returned: Optional[Book");
        assertThatMessageIsLogged(Level.TRACE, correlationId, "BookService.read Returned: Optional[Book");
        assertThatMessageIsLogged(Level.TRACE, correlationId, "BookController.read Returned: <200");
    }

    @Then("the unsuccessful book read is logged at all tiers")
    public void theUnsuccessfulBookReadIsLoggedAtAllTiers() {
        final String correlationId = httpTestManager.getResponseHeaderValue(CID_HEADER).get();

        assertThatMessageIsLogged(Level.INFO, correlationId, "BookController.read(");
        assertThatMessageIsLogged(Level.DEBUG, correlationId, "BookService.read(");
        assertThatMessageIsLogged(Level.DEBUG, correlationId, "BookRepository.read(");
        assertThatMessageIsLogged(Level.DEBUG, correlationId, "BookDao.read(");
        assertThatMessageIsLogged(Level.TRACE, correlationId, "BookDao.read Returned: Optional.empty");
        assertThatMessageIsLogged(Level.TRACE, correlationId, "BookRepository.read Returned: Optional.empty");
        assertThatMessageIsLogged(Level.TRACE, correlationId, "BookService.read Returned: Optional.empty");
        assertThatMessageIsLogged(Level.TRACE, correlationId, "BookController.read Returned: <404");
    }

    @Then("the book read all is logged at all tiers")
    public void theBookReadAllIsLoggedAtAllTiers() {
        final String correlationId = httpTestManager.getResponseHeaderValue(CID_HEADER).get();

        assertThatMessageIsLogged(Level.INFO, correlationId, "BookController.readAll(");
        assertThatMessageIsLogged(Level.DEBUG, correlationId, "BookService.readAll(");
        assertThatMessageIsLogged(Level.DEBUG, correlationId, "BookRepository.readAll(");
        assertThatMessageIsLogged(Level.DEBUG, correlationId, "BookDao.readAll(");
        assertThatMessageIsLogged(Level.TRACE, correlationId, "BookDao.readAll Returned: [Book");
        assertThatMessageIsLogged(Level.TRACE, correlationId, "BookRepository.readAll Returned: [Book");
        assertThatMessageIsLogged(Level.TRACE, correlationId, "BookService.readAll Returned: [Book");
        assertThatMessageIsLogged(Level.TRACE, correlationId, "BookController.readAll Returned: <200");
    }

    @Then("the book update is logged at all tiers")
    public void theBookUpdateIsLoggedAtAllTiers() {
        final String correlationId = httpTestManager.getResponseHeaderValue(CID_HEADER).get();

        assertThatMessageIsLogged(Level.INFO, correlationId, "BookController.update(");
        assertThatMessageIsLogged(Level.DEBUG, correlationId, "BookService.update(");
        assertThatMessageIsLogged(Level.DEBUG, correlationId, "BookRepository.update(");
        assertThatMessageIsLogged(Level.DEBUG, correlationId, "BookDao.update(");
        assertThatMessageIsLogged(Level.TRACE, correlationId, "BookDao.update Returned: Optional[Book");
        assertThatMessageIsLogged(Level.TRACE, correlationId, "BookRepository.update Returned: Optional[Book");
        assertThatMessageIsLogged(Level.TRACE, correlationId, "BookService.update Returned: Optional[Book");
        assertThatMessageIsLogged(Level.TRACE, correlationId, "BookController.update Returned: <200");
    }

    @Then("the unsuccessful book update is logged at all tiers")
    public void theUnsuccessfulBookUpdateIsLoggedAtAllTiers() {
        final String correlationId = httpTestManager.getResponseHeaderValue(CID_HEADER).get();

        assertThatMessageIsLogged(Level.INFO, correlationId, "BookController.update(");
        assertThatMessageIsLogged(Level.DEBUG, correlationId, "BookService.update(");
        assertThatMessageIsLogged(Level.DEBUG, correlationId, "BookRepository.update(");
        assertThatMessageIsLogged(Level.DEBUG, correlationId, "BookDao.update(");
        assertThatMessageIsLogged(Level.TRACE, correlationId, "BookDao.update Returned: Optional.empty");
        assertThatMessageIsLogged(Level.TRACE, correlationId, "BookRepository.update Returned: Optional.empty");
        assertThatMessageIsLogged(Level.TRACE, correlationId, "BookService.update Returned: Optional.empty");
        assertThatMessageIsLogged(Level.TRACE, correlationId, "BookController.update Returned: <404");
    }

    @Then("the book delete is logged at all tiers")
    public void theBookDeleteIsLoggedAtAllTiers() {
        final String correlationId = httpTestManager.getResponseHeaderValue(CID_HEADER).get();

        assertThatMessageIsLogged(Level.INFO, correlationId, "BookController.delete(");
        assertThatMessageIsLogged(Level.DEBUG, correlationId, "BookService.delete(");
        assertThatMessageIsLogged(Level.DEBUG, correlationId, "BookRepository.delete(");
        assertThatMessageIsLogged(Level.DEBUG, correlationId, "BookDao.delete(");
        assertThatMessageIsLogged(Level.DEBUG, correlationId, "BookDao.delete - complete");
        assertThatMessageIsLogged(Level.DEBUG, correlationId, "BookRepository.delete - complete");
        assertThatMessageIsLogged(Level.DEBUG, correlationId, "BookService.delete - complete");
        assertThatMessageIsLogged(Level.TRACE, correlationId, "BookController.delete Returned: <204");
    }

    @Then("the successful login audit event processing is logged at all tiers")
    public void theSuccessfulLoginAuditEventProcessingIsLoggedAtAllTiers() {
        assertThatAuditLogCreationIsLogged("Security", "AUTHENTICATION_SUCCESS");
    }

    @Then("the new book audit event processing is logged at all tiers")
    public void theNewBookAuditEventProcessingIsLoggedAtAllTiers() {
        assertThatAuditLogCreationIsLogged("Book", "CREATED");
    }

    @Then("the update book audit event processing is logged at all tiers")
    public void theUpdateBookAuditEventProcessingIsLoggedAtAllTiers() {
        assertThatAuditLogCreationIsLogged("Book", "UPDATED");
    }

    @Then("the delete book audit event processing is logged at all tiers")
    public void theDeleteBookAuditEventProcessingIsLoggedAtAllTiers() {
        assertThatAuditLogCreationIsLogged("Book", "DELETED");
    }

    public void assertThatAuditLogCreationIsLogged(String type, String subType) {
        final String correlationId = httpTestManager.getResponseHeaderValue(CID_HEADER).get();

        assertThatMessageIsLogged(Level.INFO, correlationId,
                String.format("AuditListener.onMessage('%s', '{\"type\":\"%s\",\"subType\":\"%s\"", correlationId, type, subType));

        assertThatMessageIsLogged(Level.DEBUG, correlationId,
                String.format("AuditRepository.create(AuditEvent(type=%s, subType=%s", type, subType));
        assertThatMessageIsLogged(Level.DEBUG, correlationId,
                String.format("AuditDao.create(AuditEvent(type=%s, subType=%s", type, subType));
        assertThatMessageIsLogged(Level.DEBUG, correlationId, "AuditDao.create - complete");
        assertThatMessageIsLogged(Level.DEBUG, correlationId, "AuditRepository.create - complete");
        assertThatMessageIsLogged(Level.INFO, correlationId, "AuditListener.onMessage - complete");
    }

    private void assertThatMessageIsLogged(Level logLevel, String correlationId, String message) {
        final List<ILoggingEvent> loggingEvents = getLoggingEvents();

        final String formattedCorrelationId = String.format("[%s]", correlationId);

        final Predicate<ILoggingEvent> correlationIdPredicate = mdcDisabled
                ? e -> !e.getMDCPropertyMap().containsKey(MDC_CID_KEY) &&
                        e.getFormattedMessage().startsWith(formattedCorrelationId)
                : e -> e.getMDCPropertyMap().getOrDefault(MDC_CID_KEY, StringUtils.EMPTY).equals(correlationId) &&
                       !e.getFormattedMessage().startsWith(formattedCorrelationId);

        loggingEvents.stream()
                .filter(e -> e.getLevel().equals(logLevel))
                .filter(correlationIdPredicate)
                .filter(e -> e.getFormattedMessage().contains(message))
                .findFirst()
                .orElseThrow(NoSuchElementException::new);
    }

    private List<ILoggingEvent> getLoggingEvents() {
        final ArgumentCaptor<ILoggingEvent> eventCaptor = ArgumentCaptor.forClass(ILoggingEvent.class);
        verify(appender, atLeastOnce()).doAppend(eventCaptor.capture());

        return eventCaptor.getAllValues();
    }
}
