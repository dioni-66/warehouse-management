package com.warehousemanagement.configuration.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Class to encode strings with MD5 algorithm
 */
@Transactional
@Configuration
public class Md5Encoder {

    private static final Logger LOGGER = LoggerFactory.getLogger(Md5Encoder.class);

    /**
     * Get Md 5 String from an input String
     *
     * @param input The input string
     * @return The encoded string
     */
    public String getMd5(final String input) {
        if (input == null) {
            return null;
        }

        try {
            // Static getInstance method is called with hashing SHA
            final MessageDigest md = MessageDigest.getInstance("MD5");

            // digest() method called
            // to calculate message digest of an input
            // and return array of byte
            final byte[] messageDigest = md.digest(input.getBytes());

            // Convert byte array into signum representation
            final BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            StringBuilder hashtext = new StringBuilder(no.toString(16));

            while (hashtext.length() < 32) {
                hashtext.insert(0, "0");
            }

            return hashtext.toString();
        }

        // For specifying wrong message digest algorithms
        catch (final NoSuchAlgorithmException e) {
            Md5Encoder.LOGGER.error("Exception thrown for incorrect algorithm: {}", e.getMessage(), e);
            return null;
        }
    }

}
