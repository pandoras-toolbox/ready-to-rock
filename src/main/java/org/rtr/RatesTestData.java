package org.rtr;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Optional;

import static org.apache.commons.lang3.builder.ToStringStyle.JSON_STYLE;
import static org.assertj.core.api.Assertions.assertThat;

// Input data type for date-driven testing.
public final class RatesTestData {

    public static final String DEFAULT_DATE = "latest";

    private String date = DEFAULT_DATE;
    private Currency symbols;
    private Currency base;

    public String getDate() {
        return date;
    }

    public RatesTestData setDate(String date) {
        this.date = date;
        return this;
    }

    public Optional<Currency> getSymbols() {
        return Optional.ofNullable(symbols);
    }

    public RatesTestData setSymbols(Currency symbols) {
        this.symbols = symbols;
        return this;
    }

    public Optional<Currency> getBase() {
        return Optional.ofNullable(base);
    }

    public RatesTestData setBase(Currency base) {
        this.base = base;
        return this;
    }

    public String createPath() {
        StringBuffer sb = new StringBuffer("https://api.ratesapi.io/api/");
        assertThat(date).as("date").isNotEmpty();
        sb.append(date);
        sb.append("/");
        Optional<Currency> baseOpt = getBase();
        baseOpt.ifPresent(base -> {
            sb.append("?base=");
            sb.append(base.name());
        });
        getSymbols().ifPresent(symbols -> {
            if (baseOpt.isPresent()) {
                sb.append("&");
            } else {
                sb.append("?");
            }
            sb.append("symbols=");
            sb.append(symbols.name());
        });
        return sb.toString();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, JSON_STYLE);
    }

}
