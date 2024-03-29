package api;

import exceptions.ManagerSaveException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private final URI url;
    private final String API_TOKEN;
    protected HttpClient client;

    public KVTaskClient(URI url) throws IOException, InterruptedException {
        client = HttpClient.newHttpClient();
        this.url = url; // URL к серверу хранилища
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url + "/register"))
                .version(HttpClient.Version.HTTP_1_1)
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        API_TOKEN = response.body();
        client = HttpClient.newHttpClient();
    }

    public String getAPI_TOKEN() {
        return API_TOKEN;
    }

    public void put(String key, String json) throws IOException, InterruptedException {
        HttpRequest requestSave = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(URI.create(url + "/save/" + key + "?API_TOKEN=DEBUG"))
                .header("Content-Type", "application/json")
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpResponse<String> response = client.send(requestSave, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new ManagerSaveException("Данные не сохранены, код ошибки: " + response.statusCode());
        }
    }

    public String load(String key) throws IOException, InterruptedException {
        HttpRequest requestLoad = HttpRequest.newBuilder()
                .GET()
                .version(HttpClient.Version.HTTP_1_1)
                .header("Content-Type", "application/json")
                .uri(URI.create(url + "/load/" + key + "?API_TOKEN=DEBUG"))
                .build();
        HttpResponse<String> responseLoad = client.send(requestLoad, HttpResponse.BodyHandlers.ofString());
        if (responseLoad.statusCode() != 200) {
            throw new ManagerSaveException("Данные не загружены, код ошибки: " + responseLoad.statusCode());
        }
        return responseLoad.body();
    }
}
