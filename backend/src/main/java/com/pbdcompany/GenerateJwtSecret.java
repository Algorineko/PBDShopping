package com.pbdcompany;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Base64;

public class GenerateJwtSecret {
    public static void main(String[] args) {
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        System.out.println("Generated Secret Key: " + Base64.getEncoder().encodeToString(key.getEncoded()));
    }
}