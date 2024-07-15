package com.spt.development.demo.infrastructure.cucumber.steps;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.spt.development.demo.infrastructure.cucumber.util.MapStringObjectTypeToken;
import com.spt.development.test.integration.HttpTestManager;
import io.cucumber.java.en.Then;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.Optional;

import static com.spt.development.cid.web.filter.CorrelationIdFilter.CID_HEADER;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class RestApiSteps {
    private static final Gson GSON = new GsonBuilder().create();

    static class TestData {
        static final String CORRELATION_ID = "709ac6e1-8ace-422d-9421-b8d93f0c6505";

        static class Resource {
            static final String ROOT = "/com/spt/development/demo/infrastructure/cucumber/requests/";
        }

        static class Api {
            static final String USERNAME = "bob";
            static final String PASSWORD = "password123!";
        }

        static class ValidBook {
            static final String TITLE = "The Hitchhikers Guide to the Galaxy";
            static final String BLURB = "The Hitchhikers Guide to the Galaxy', 'One Thursday lunchtime the Earth gets unexpectedly demolished to make way for a new hyperspace bypass.";
            static final String AUTHOR = "Douglas Adams";
            static final int RRP = 699;

            static final String RESOURCE = Resource.ROOT + "valid-book.json";
        }

        static class UpdatedJob {
            static final String TITLE = ValidBook.TITLE + " (updated)";
            static final String BLURB = ValidBook.BLURB + " (updated)";
            static final String AUTHOR = ValidBook.AUTHOR + " (updated)";
            static final int RRP = ValidBook.RRP + 100;

            static final String RESOURCE = Resource.ROOT + "updated-book.json";
        }
    }

    @Autowired private HttpTestManager httpTestManager;

    @Then("^the server will respond with a HTTP status of '(\\d+)'$")
    public void theServerWillRespondWithAHTTPStatusOf(int statusCode) {
        assertThat(httpTestManager.getStatusCode()).isEqualTo(statusCode);
    }

    @Then("^the response will have a correlationId header$")
    public void theResponseWillHaveACorrelationIdHeader() {
        final Optional<String> correlationId = httpTestManager.getResponseHeaderValue(CID_HEADER);

        assertThat(correlationId).isPresent();
    }

    @Then("the response will have the correlationID header sent in the request")
    public void theResponseWillHaveTheCorrelationIDHeaderSentInTheRequest() {
        final Optional<String> correlationId = httpTestManager.getResponseHeaderValue(CID_HEADER);

        assertThat(correlationId).contains(TestData.CORRELATION_ID);
    }

    static long getBookIdFromResponse(HttpTestManager httpTestManager) {
        final Map<String, Object> book = GSON.fromJson(
                httpTestManager.getResponseBody(), new MapStringObjectTypeToken().getType()
        );
        return Double.valueOf(book.get("id").toString()).longValue();
    }
}
