package com.online.shopping.util;

import com.online.shopping.config.Generated;
import com.online.shopping.dto.DataResponse;
import com.online.shopping.dto.Header;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Random;

@Slf4j
@Generated
public class OnlineShoppingAppUtility {

    private OnlineShoppingAppUtility() {
    }

    private static final Random RANDOM = new SecureRandom();
    private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public static String generateUserId(int length) {
        return generateRandomString(length);
    }

    public String generateAddressId(int length) {
        return generateRandomString(length);
    }

    /**
     * Method to generate a random string based on the input length
     * @param length
     * @return
     */
    private static String generateRandomString(int length) {
        String upperChar = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String number = "0123456789";

        String randomString = upperChar + number;
        SecureRandom random = new SecureRandom();

        if (length < 1)
            throw new IllegalArgumentException();

        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int rndCharAt = random.nextInt(randomString.length());
            char rndChar = randomString.charAt(rndCharAt);
            sb.append(rndChar);
        }

        return sb.toString();
    }

    /**
     * Map the response to the DataResponse - used to send in both positive and negative scenarios.
     * @param responseString
     * @param httpStatus
     * @param traceId
     * @param payload
     * @return
     */
    public static DataResponse<String> setDataResponse(String responseString, HttpStatus httpStatus, String traceId, String payload) {
        log.info("Inside the setDataResponse() method");
        DataResponse<String> response = new DataResponse<>();
        response.setHeader(new Header(responseString, String.valueOf(httpStatus.value()), traceId));
        response.setPayload(payload);
        response.setErrors(new ArrayList<>());
        log.info("Exiting from the setDataResponse() method | Prepared the inputs for the DataResponse<T> object");
        return response;
    }
}
