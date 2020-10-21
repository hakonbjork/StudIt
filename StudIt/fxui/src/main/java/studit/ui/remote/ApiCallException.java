package studit.ui.remote;

public class ApiCallException extends Exception {
  private static final long serialVersionUID = -3705568636258341658L;

  public ApiCallException(String errorMessage) {
    super(errorMessage);
  }
}