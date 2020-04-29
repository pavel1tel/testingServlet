package com.kpi.testing.util;

import org.mindrot.jbcrypt.BCrypt;
import org.modelmapper.ModelMapper;

import java.security.SecureRandom;
import java.util.regex.Pattern;

public class BCryptEncoder {
    private final Pattern BCRYPT_PATTERN = Pattern.compile("\\A\\$2a?\\$(\\d\\d)\\$[./0-9A-Za-z]{53}");

    public  String encode(CharSequence rawPassword) {
        String salt;
        int strength = 10; //default in spring
        salt = BCrypt.gensalt(strength, new SecureRandom());
        return BCrypt.hashpw(rawPassword.toString(), salt);
    }

    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        if (encodedPassword != null && encodedPassword.length() != 0) {
            if (!this.BCRYPT_PATTERN.matcher(encodedPassword).matches()) {
                return false;
            } else {
                return BCrypt.checkpw(rawPassword.toString(), encodedPassword);
            }
        } else {
            return false;
        }
    }
}
