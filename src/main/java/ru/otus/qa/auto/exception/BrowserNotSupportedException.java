package ru.otus.qa.auto.exception;

public class BrowserNotSupportedException extends RuntimeException {

    public BrowserNotSupportedException(String incorrectBrowserName) {
        super(String.format("Неподдерживаемый браузер %s", incorrectBrowserName));
    }
}
