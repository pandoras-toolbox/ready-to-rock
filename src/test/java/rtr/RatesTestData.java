package rtr;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Optional;

import static org.apache.commons.lang3.builder.ToStringStyle.JSON_STYLE;
import static org.assertj.core.api.Assertions.assertThat;

// Input data type for date-driven testing.
public final class RatesTestData {

    public static String DEFAULT_DATE = "latest";

    private String date = DEFAULT_DATE;
    private String symbols;
    private String base;

    public String getDate() {
        return date;
    }

    public RatesTestData setDate(String date) {
        this.date = date;
        return this;
    }

    public Optional<String> getSymbols() {
        return Optional.ofNullable(symbols);
    }

    public RatesTestData setSymbols(String symbols) {
        this.symbols = symbols;
        return this;
    }

    public Optional<String> getBase() {
        return Optional.ofNullable(base);
    }

    public RatesTestData setBase(String base) {
        this.base = base;
        return this;
    }

    public String createPath() {
        StringBuffer sb = new StringBuffer("https://api.ratesapi.io/api/");
        assertThat(date).as("date").isNotEmpty();
        sb.append(date);
        sb.append("/");
        Optional<String> baseOpt = getBase();
        baseOpt.ifPresent(base -> {
            sb.append("?base=");
            sb.append(base);
        });
        getSymbols().ifPresent(symbols -> {
            if (baseOpt.isPresent()) {
                sb.append("&");
            } else {
                sb.append("?");
            }
            sb.append("symbols=");
            sb.append(symbols);
        });
        return sb.toString();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, JSON_STYLE);
    }

}
