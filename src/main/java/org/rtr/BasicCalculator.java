package org.rtr;

import io.qameta.allure.Step;

public class BasicCalculator {

    @Step
    public int add(int a, int b) {
        return a + b;
    }

    @Step
    public int multiply(int a, int b) {
        return a * b;
    }

    @Step
    public int divide(int a, int b) {
        return a / b;
    }

}
