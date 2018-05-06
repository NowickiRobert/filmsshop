package net.stawrul.services.exceptions;

/**
 * Wyjątek sygnalizujący niedostępność towaru.
 *
 * Wystąpienie wyjątku z hierarchii RuntimeException w warstwie biznesowej
 * powoduje wycofanie transakcji (rollback).
 */
public class OutOfStockException extends RuntimeException {
}
