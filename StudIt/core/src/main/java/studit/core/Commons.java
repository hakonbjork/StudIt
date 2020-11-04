package studit.core;

public class Commons {

  /**
   * Check if an array contains an element.
   * @param <T> Generic type of the array.
   * @param array The array to check.
   * @param v the element we are checking.
   * @return true if array contains element, false otherwise.
   */
  public static <T> boolean contains(final T[] array, final T v) {
    if (v == null) {
      for (final T e : array) {
        if (e == null) {
          return true;
        }
      }

    } else {
      for (final T e : array) {
        if (e == v || v.equals(e)) {
          return true;
        }

      }

    }

    return false;
  }

}
