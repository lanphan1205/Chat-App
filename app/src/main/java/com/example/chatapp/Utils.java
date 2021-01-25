package com.example.chatapp;
import java.util.Random;

public class Utils {
    public static String generateRandomString() {

        //...
        String str = "";
        Random r = new Random();

        String alphabet = "123xyz";
        for (int i = 0; i < 20; i++) {
            char c = alphabet.charAt(r.nextInt(alphabet.length()));
            str += String.valueOf(c);
        } // prints 50 random characters from alphabet
        return str;
    }
}
