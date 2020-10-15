package studit.restserver;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import studit.restapi.StuditService;


public class StuditServer {
    private static URI BASE_URI = URI.create("http://localhost:8080/");

    public static void main(final String[] args, int awaitPeriodSeconds) {

    }

    public static HttpServer startServer(final String[] args, int waitSecondsForServer)
      throws IOException {
    URI baseUri = (args.length >= 1 ? URI.create(args[0]) : BASE_URI);
    ResourceConfig resourceConfig = new StuditConfig();
    HttpServer httpServer = GrizzlyHttpServerFactory.createHttpServer(baseUri, resourceConfig);
    if (waitSecondsForServer < 0) {
      return httpServer;
    }
    while (waitSecondsForServer > 0) {
      try {
        URL clientUrl = new URL(baseUri + StuditService.STUDIT_SERVICE_PATH);
        HttpURLConnection connection = (HttpURLConnection) clientUrl.openConnection();
        int responseCode = connection.getResponseCode();
        System.out.println("Trying " + clientUrl + ": " + responseCode);
        connection.disconnect();
        if (responseCode == 200) {
          return httpServer;
        }
      } catch (final RuntimeException e) {
        // ignore
      }
      try {
        Thread.sleep(1000);
        waitSecondsForServer -= 1;
      } catch (final InterruptedException e) {
        return null;
      }
    }
    return null;
  }


}