package http;

public class Application {
    public static void main(String[] args) {
        int port = Integer.parseInt((String)System.getProperties().getOrDefault("port", "8180"));
        new HttpServer(port).start();
    }
}
