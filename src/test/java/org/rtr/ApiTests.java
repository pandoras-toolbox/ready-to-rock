package org.rtr;

import io.qameta.allure.*;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.FileNotFoundException;
import java.util.Map;

import static io.restassured.RestAssured.when;
import static org.apache.http.HttpStatus.SC_OK;
import static org.assertj.core.api.Assertions.assertThat;

@Epic("API Test Examples")
@Link(name = "Rates API", url = "https://ratesapi.io/documentation/")
@Tag(TestGroup.API_TESTS)
@ExtendWith(JUnitCallback.class)
final class ApiTests {

    @BeforeAll
    public static void beforeAll() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter(), new AllureRestAssured());
    }

    @Feature("Exchange Rates")
    @Story("Latest")
    @Description("Test if we get exchange rates by using the simplest request.")
    @Test
    void requestLatestExchangeRatesWorks() {
        // FIXME: Make the data-driven testing stronger by using a JUnit test factory for example.
        RatesTestData data = new RatesTestData().setDate("latest");
        Response response = requestExchangeRates(data);
        checkResponse(response, data);
    }

    @Feature("Exchange Rates")
    @Story("Past")
    @Description("Test if past exchange rates exist.")
    @Test
    void requestPastExchangeRatesWorks() {
        RatesTestData data = new RatesTestData().setDate("2010-01-12");
        Response response = requestExchangeRates(data);
        checkResponse(response, data);
    }

    @Feature("Exchange Rates")
    @Story("Base")
    @Description("Test if the exchange rates can be based on a certain currency.")
    @Test
    void requestExchangeRatesWithBaseWorks() {
        RatesTestData data = new RatesTestData().setBase(Currency.getRandomCurrency());
        Response response = requestExchangeRates(data);
        checkResponse(response, data);
    }

    @Feature("Exchange Rates")
    @Story("Symbols")
    @Description("Test if the exchange rates can be limited to a certain currency.")
    @Test
    void requestExchangeRatesWithSymbolsWorks() {
        RatesTestData data = new RatesTestData().setSymbols(Currency.getRandomCurrency());
        Response response = requestExchangeRates(data);
        checkResponse(response, data);
    }


    @Disabled("Disabled just to trigger the 'Ignored Tests' category")
    @Story("Other")
    @Test
    void triggerIgnoredTestsCategory() {
    }

    @Feature("Exchange Rates")
    @Story("Other")
    @Test
    void triggerInfrastructureProblemsCategory() {
        throw new RuntimeException("Infrastructure crashed, bye-bye");
    }

    @Feature("Exchange Rates")
    @Story("Other")
    @Test
    void triggerOutdatedTestsCategory() {
        throw new RuntimeException(new FileNotFoundException());
    }

    @Feature("Exchange Rates")
    @Story("Other")
    @Test
    void triggerProductDefectsCategory() {
        throw new AssertionError();
    }

    @Feature("Exchange Rates")
    @Story("Other")
    @Test
    void triggerTestDefectsCategory() {
        throw new RuntimeException();
    }

    @Step
    private Response requestExchangeRates(RatesTestData data) {
        return when()
            .get(data.createPath())
            .then()
            .extract()
            .response();
    }

    @Step
    private void checkResponse(Response response, RatesTestData data) {
        assertThat(response.getStatusCode())
            .as("status code")
            .isEqualTo(SC_OK);
        JsonPath jsonPath = response.getBody().jsonPath();
        String date = data.getDate();
        if (!date.equals(RatesTestData.DEFAULT_DATE)) {
            // When it is "latest" then the date in the response might be a few days in the past.
            assertThat(jsonPath.getString("date"))
                .as("date")
                .isEqualTo(date);
        }
        data.getBase().ifPresent(base -> assertThat(jsonPath.getString("base"))
            .as("base")
            .isEqualTo(base.name()));
        Map<String, String> rates = jsonPath.getMap("rates", String.class, String.class);
        assertThat(rates.keySet())
            .as("rates")
            .isNotEmpty();
        rates.keySet().forEach(key -> assertThat(rates.get(key))
            .as(String.format("rate %s", key))
            .isNotEmpty());
        data.getSymbols().ifPresent(symbols -> assertThat(rates.keySet())
            .as("symbols")
            .containsOnly(symbols.name()));
    }

}
