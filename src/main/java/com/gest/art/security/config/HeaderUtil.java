package com.gest.art.security.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class HeaderUtil {
    private static final String APPLICATION_NAME = "bureau-backend";
    private static final Logger log = LoggerFactory.getLogger(HeaderUtil.class);

    private HeaderUtil() {
    }

    /**
     * <p>createAlert.</p>
     *
     * @param applicationName a {@link String} object.
     * @param message         a {@link String} object.
     * @param param           a {@link String} object.
     * @return a {@link HttpHeaders} object.
     */
    public static HttpHeaders createAlert(String applicationName, String message, String param) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-" + applicationName + "-alert", message);
        try {
            headers.add("X-" + applicationName + "-params", URLEncoder.encode(param, StandardCharsets.UTF_8.toString()));
        } catch (UnsupportedEncodingException e) {
            // StandardCharsets are supported by every Java implementation so this exception will never happen
        }
        return headers;
    }

    /**
     * <p>createEntityCreationAlert.</p>
     *
     * @param applicationName   a {@link String} object.
     * @param enableTranslation a boolean.
     * @param entityName        a {@link String} object.
     * @param param             a {@link String} object.
     * @return a {@link HttpHeaders} object.
     */
    public static HttpHeaders createEntityCreationAlert(String applicationName, boolean enableTranslation, String entityName, String param) {
        String message = enableTranslation ? applicationName + "." + entityName + ".created"
                : "A new " + entityName + " is created with identifier " + param;
        return createAlert(applicationName, message, param);
    }

    /**
     * <p>createEntityUpdateAlert.</p>
     *
     * @param applicationName   a {@link String} object.
     * @param enableTranslation a boolean.
     * @param entityName        a {@link String} object.
     * @param param             a {@link String} object.
     * @return a {@link HttpHeaders} object.
     */
    public static HttpHeaders createEntityUpdateAlert(String applicationName, boolean enableTranslation, String entityName, String param) {
        String message = enableTranslation ? applicationName + "." + entityName + ".updated"
                : "A " + entityName + " is updated with identifier " + param;
        return createAlert(applicationName, message, param);
    }

    /**
     * <p>createEntityDeletionAlert.</p>
     *
     * @param applicationName   a {@link String} object.
     * @param enableTranslation a boolean.
     * @param entityName        a {@link String} object.
     * @param param             a {@link String} object.
     * @return a {@link HttpHeaders} object.
     */
    public static HttpHeaders createEntityDeletionAlert(String applicationName, boolean enableTranslation, String entityName, String param) {
        String message = enableTranslation ? applicationName + "." + entityName + ".deleted"
                : "A " + entityName + " is deleted with identifier " + param;
        return createAlert(applicationName, message, param);
    }

    /**
     * <p>createFailureAlert.</p>
     *
     * @param applicationName   a {@link String} object.
     * @param enableTranslation a boolean.
     * @param entityName        a {@link String} object.
     * @param errorKey          a {@link String} object.
     * @param defaultMessage    a {@link String} object.
     * @return a {@link HttpHeaders} object.
     */
    public static HttpHeaders createFailureAlert(String applicationName, boolean enableTranslation, String entityName, String errorKey, String defaultMessage) {
        log.error("Entity processing failed, {}", defaultMessage);

        String message = enableTranslation ? "error." + errorKey : defaultMessage;

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-" + applicationName + "-error", message);
        headers.add("X-" + applicationName + "-params", entityName);
        return headers;
    }

    /**
     * Create alert method.
     *
     * @param message
     * @param param
     * @return Http headers
     */
    public static HttpHeaders createAlert(final String message,
                                          final String param
    ) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-" + APPLICATION_NAME + "-alert", message);
        headers.add("X-" + APPLICATION_NAME + "-params", param);
        return headers;
    }

    /**
     * @param entityName
     * @param param
     * @return Http headers
     */
    public static HttpHeaders createEntityCreationAlert(
            final String entityName,
            final String param
    ) {
        return createAlert("A new "
                + entityName
                + " is created with identifier "
                + param, param);
    }

    /**
     * @param entityName
     * @param param
     * @return Http headers
     */
    public static HttpHeaders createEntityUpdateAlert(
            final String entityName,
            final String param
    ) {
        return createAlert("A "
                + entityName
                + " is updated with identifier "
                + param, param);
    }

    /**
     * @param entityName
     * @param param
     * @return Http headers
     */
    public static HttpHeaders createEntityDeletionAlert(
            final String entityName,
            final String param
    ) {
        return createAlert("A "
                + entityName
                + " is deleted with identifier "
                + param, param);
    }

    /**
     * @param entityName
     * @param errorKey
     * @param defaultMessage
     * @return Http headers
     */
    public static HttpHeaders createFailureAlert(
            final String entityName,
            final String errorKey,
            final String defaultMessage
    ) {
        log.error("Entity processing failed, {}", defaultMessage);
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-" + APPLICATION_NAME + "-error", defaultMessage);
        headers.add("X-" + APPLICATION_NAME + "-params", entityName);
        return headers;
    }

}
