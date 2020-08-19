package rtr;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.apache.commons.lang3.NotImplementedException;
import org.assertj.core.api.ThrowableAssertAlternative;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static rtr.TestGroup.UNIT_TESTS;

@Epic("Unit Test Examples")
@Feature("Calculator")
@Tag(UNIT_TESTS)
@ExtendWith(JUnitCallback.class)
final class UnitTests {

    private static final BasicCalculator CALCULATOR = new BasicCalculator();

    @Story("Add")
    @Test
    @DisplayName("1 + 1 = 2")
    void addsTwoNumbers() {
        assertThat(CALCULATOR.add(1, 1)).as("1 + 1 should equal 2").isEqualTo(2);
    }

    @Story("Multiply")
    @Test
    @DisplayName("2 * 2 = 4")
    void multiplyTwoNumbers() {
        assertThat(CALCULATOR.multiply(2, 2)).as("2 * 2 should equal 4").isEqualTo(4);
    }

    @Story("Divide")
    @Test
    @DisplayName("1 / 0 = ERROR")
    void divideByZero() {
        ThrowableAssertAlternative<ArithmeticException> thrownBy = assertThatExceptionOfType(ArithmeticException.class)
            .isThrownBy(() -> CALCULATOR.divide(1, 0));
        thrownBy.withMessage("/ by zero");
    }

    @Disabled("Not implemented yet")
    @Story("Square")
    @Test
    void square() {
        // TODO Implement test for the square feature.
        throw new NotImplementedException();
    }

}
