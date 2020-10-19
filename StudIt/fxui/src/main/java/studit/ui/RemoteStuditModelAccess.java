package studit.ui;

import java.io.IOException;
import java.net.ConnectException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.databind.ObjectMapper;

import studit.json.StuditModule;

public class RemoteStuditModelAccess {

  private static final String ENDPOINT_PATH = "http://localhost:8080/studit";
  private ObjectMapper objectMapper;

  public static void main(String[] args) {
    RemoteStuditModelAccess r = new RemoteStuditModelAccess();
    r.newGetRequest(null, "courses");

  }

  public RemoteStuditModelAccess() {
    this.objectMapper = new ObjectMapper().registerModule(new StuditModule());
  }

  private HttpRequest newGetRequest(Map<String, String> params, String... paths) {
    URI uri = buildUri(params, paths);
    HttpRequest request = HttpRequest.newBuilder(uri).header("Accept", "application/json").GET().build();

    try {
      final HttpResponse<String> response = HttpClient.newBuilder().build().send(request,
          HttpResponse.BodyHandlers.ofString());
      final String responseString = response.body();
      System.out.println("Response: " + responseString);
    } catch (ConnectException e) {
      System.out.println("Could not establish connection to server");
      // TODO: update gui and tell that we've lost connection to the server.
    }
    catch (IOException | InterruptedException e1) {
      throw new RuntimeException(e1);
    }

    return null;
  }

  private URI buildUri(Map<String, String> params, String... paths) {
    StringBuilder uri = new StringBuilder(ENDPOINT_PATH);
    for (String path : paths) {
      uri.append("/");
      uri.append(path);
    }

    if (params != null) {
      uri.append("?");
      for (Entry<String, String> param : params.entrySet()) {
        uri.append(param.getKey());
        uri.append("=");
        uri.append(param.getValue());
        uri.append("&");
      }
      // Remove last and
      uri.deleteCharAt(uri.length() - 1);
    }

    try {
      URI finalURI = new URI(uri.toString());
      return finalURI;
    } catch (URISyntaxException e) {
      e.printStackTrace();
      return null;
    }
  }

}