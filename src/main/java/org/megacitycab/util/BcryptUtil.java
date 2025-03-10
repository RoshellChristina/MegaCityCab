package org.megacitycab.util;


import org.mindrot.jbcrypt.BCrypt;

public class BcryptUtil {
    // Adjust the workload as needed (default is 10)
    private static final int workload = 12;

    public static String hashPassword(String plainTextPassword) {
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt(workload));
    }

    // Verify hashed password
    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}
