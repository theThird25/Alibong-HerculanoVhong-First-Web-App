package edu.bsit.userhub.alibong.security;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

import org.springframework.stereotype.Component;

@Component
public class Sha256PasswordHasher implements PasswordHasher {

    @Override
    public String hash(String rawPassword) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256")
                    .digest(rawPassword.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean matches(String rawPassword, String hashedPassword) {
        // constant-time comparison prevents timing attacks
        return MessageDigest.isEqual(
                hash(rawPassword).getBytes(StandardCharsets.UTF_8),
                hashedPassword.getBytes(StandardCharsets.UTF_8));
    }
}
