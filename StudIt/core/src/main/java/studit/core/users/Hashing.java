package studit.core.users;

import java.security.MessageDigest;

public class Hashing {
  // This is to prevent attackers from using a hash lookup table to predict our
  // password.
  private static final String salt = "6%b2A/662&cX[";

  /**
   * Hashes a string using the SHA256 hashing algoritm to ensure passwords are
   * stored safely.
   * 
   * @param base string to hash
   * @return the hashed string
   */
  public static String sha256(String base) {
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      byte[] hash = digest.digest(base.getBytes("UTF-8"));
      StringBuffer hexString = new StringBuffer();

      for (int i = 0; i < hash.length; i++) {
        String hex = Integer.toHexString(0xff & hash[i]);
        if (hex.length() == 1) {
          hexString.append('0');
        }
        hexString.append(hex);
      }

      return hexString.toString();
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  /**
   * Check if given password and stored password hash match.
   * 
   * @param password password to check
   * @param hash     stored hash
   * @return true if match, else false
   */
  public static boolean unhashPassword(String hash, String password) {
    return sha256(password + salt).equals(hash) ? true : false;
  }

  /**
   * Hash a password to sha256 for added security.
   * 
   * @param password password to hash
   * @return String[0] contains our hash if password is valid, otherwise null.
   *         String[1] contains error message if password is invalid, otherwise
   *         null
   */
  public static String[] hashPassword(String password) {
    if (password.length() < 8 || password.length() > 32) {
      return new String[] { null, "Passordet må være mellom 8 og 32 tegn" };
    }
    if (password.contains(" ")) {
      return new String[] { null, "Passordet kan ikke inneholde mellomrom" };
    }
    String charCheck = password
        .replaceAll("[a-zA-ZÆØÅ0-9\\.\\!\\@\\£\\#\\$\\¤\\€\\%\\&\\/\\(\\)\\=\\?\\+\\[\\]\\{\\}\\_\\-\\.\\,]", "");
    if (!charCheck.equals("")) {
      return new String[] { null, "Passordet har de følgende ulovlige tegnene: '" + charCheck + "'" };
    }

    return new String[] { sha256(password + salt), null };
  }
}