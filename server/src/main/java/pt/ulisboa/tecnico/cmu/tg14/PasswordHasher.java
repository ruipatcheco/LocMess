package pt.ulisboa.tecnico.cmu.tg14;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Random;

/**
 * A utility class to hash passwords and check passwords vs hashed values. It uses a combination of hashing and unique
 * salt. The algorithm used is PBKDF2WithHmacSHA1 which, although not the best for hashing password (vs. bcrypt) is
 * still considered robust and <a href="https://security.stackexchange.com/a/6415/12614"> recommended by NIST </a>.
 * The hashed value has 256 bits.
 */
public class PasswordHasher {

    private static final Random RANDOM = new SecureRandom();
    private static final int ITERATIONS = 10000;
    private static final int KEY_LENGTH = 256;
    private static final int SALTBYTES = 16;
    /**
     * static utility class
     */
    private PasswordHasher() { }

    /**
     * Returns a random salt to be used to hash a password.
     *
     * @return a 16 bytes random salt
     */
    public static byte[] getNextSalt() {
        byte[] salt = new byte[SALTBYTES];
        RANDOM.nextBytes(salt);
        return salt;
    }

    /**
     * Returns a salted and hashed password using the provided hash.<br>
     * Note - side effect: the password is destroyed (the char[] is filled with zeros)
     *
     * @param password the password to be hashed
     * @param salt     a 16 bytes salt, ideally obtained with the getNextSalt method
     *
     * @return the hashed password with a pinch of salt
     */
    private static byte[] hash(char[] password, byte[] salt) {
        PBEKeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, KEY_LENGTH);
        Arrays.fill(password, Character.MIN_VALUE);
        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            return skf.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new AssertionError("Error while hashing a password: " + e.getMessage(), e);
        } finally {
            spec.clearPassword();
        }
    }

    public static String hashToString(String password){
        try {
            byte[] salt = getNextSalt();
            byte[] hashed = hash(password.toCharArray(),salt);
            ByteArrayOutputStream out =  new ByteArrayOutputStream();
            out.write(salt);
            out.write(hashed);
            System.out.print("hhhhh:  "+new String(Base64.getEncoder().encode(out.toByteArray())).length());
            return new String(Base64.getEncoder().encode(out.toByteArray()));

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
     * Returns true if the given password and salt match the hashed value, false otherwise.<br>
     * Note - side effect: the password is destroyed (the char[] is filled with zeros)
     *
     * @param password     the password to check
     * @param salt         the salt used to hash the password
     * @param expectedHash the expected hashed value of the password
     *
     * @return true if the given password and salt match the hashed value, false otherwise
     */
    public static boolean isExpectedPassword(char[] password, String storedHash) {
        byte[] stored = Base64.getDecoder().decode(storedHash);
        byte[] salt = new byte[SALTBYTES];
        ByteBuffer bb = ByteBuffer.wrap(stored);
        bb.get(salt,0,SALTBYTES);
        byte[] hashedPass = new byte[stored.length-SALTBYTES];
        bb.get(hashedPass,SALTBYTES,stored.length);
        byte[] pwdHash = hash(password, salt);
        Arrays.fill(password, Character.MIN_VALUE);
        if (pwdHash.length != hashedPass.length) return false;
        for (int i = 0; i < pwdHash.length; i++) {
            if (pwdHash[i] != hashedPass[i]) return false;
        }
        return true;
    }

    /**
     * Generates a random password of a given length, using letters and digits.
     *
     * @param length the length of the password
     *
     * @return a random password
     */
    public static String generateRandomPassword(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int c = RANDOM.nextInt(62);
            if (c <= 9) {
                sb.append(String.valueOf(c));
            } else if (c < 36) {
                sb.append((char) ('a' + c - 10));
            } else {
                sb.append((char) ('A' + c - 36));
            }
        }
        return sb.toString();
    }
}