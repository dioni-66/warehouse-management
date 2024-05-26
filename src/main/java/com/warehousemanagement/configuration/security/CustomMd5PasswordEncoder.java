package com.warehousemanagement.configuration.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Class that implements the default Spring Security PasswordEncoder
 * by using the MD5 Password Encoder as in the PHP application
 * username is used as Salt whenever calling encode() or matches()
 */
@Configuration
public class CustomMd5PasswordEncoder implements PasswordEncoder {

    @Autowired
    private Md5Encoder md5Encoder;

    /**
     * {@inheritDoc}
     */
    @Override
    public String encode(final CharSequence charSequence) {
        return md5Encoder.getMd5(charSequence.toString());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean matches(final CharSequence charSequence, final String s) {
        return md5Encoder.getMd5(charSequence.toString()).equals(s);
    }

}
