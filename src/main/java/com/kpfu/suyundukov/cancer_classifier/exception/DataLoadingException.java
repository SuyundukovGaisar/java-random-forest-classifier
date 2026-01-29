package com.kpfu.suyundukov.cancer_classifier.exception;

/**
 * Исключение, выбрасываемое при ошибках загрузки или парсинга данных.
 * Используется для обертывания низкоуровневых ошибок ввода-вывода (IOException).
 */
public class DataLoadingException extends RuntimeException {

    /**
     * Конструктор с сообщением и причиной ошибки.
     *
     * @param message Описание ошибки.
     * @param cause   Исходное исключение (например, IOException).
     */
    public DataLoadingException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Конструктор только с сообщением.
     *
     * @param message Описание ошибки.
     */
    public DataLoadingException(String message) {
        super(message);
    }
}