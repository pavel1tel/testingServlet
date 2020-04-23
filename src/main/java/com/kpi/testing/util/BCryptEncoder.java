package com.kpi.testing.util;

import org.mindrot.jbcrypt.BCrypt;
import java.security.SecureRandom;
import java.util.regex.Pattern;

public class BCryptEncoder {
    private final Pattern BCRYPT_PATTERN = Pattern.compile("\\A\\$2a?\\$(\\d\\d)\\$[./0-9A-Za-z]{53}");

    public static String encode(CharSequence rawPassword) {
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

    public static void main(String[] args) {
        //System.out.println(encode("grib1111"));
        System.out.println(new BCryptEncoder().matches("grib1111", "$2a$10$r.SHIFgRvMxf/NdWCsX89e2JLEHejb0r448vOLUkQDpYTs.VBfAl."));
    }
}
