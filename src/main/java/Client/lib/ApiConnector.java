package Client.lib;
import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.format.DateTimeParseException;


public class ApiConnector {

    private static final String apiKey = "6v1x5ujo8a4xgarh6t379h3yszgsi55o";
    private OkHttpClient httpClient;
    private String apiUrl;

    private String account;
    private String pin;

    private String originCountry;
    private String originBank;
    private String receiveCountry;
    private String receiveBank;


    //ApiConnector
    //moet aangepast worden want moet ook kunnen verbinden zonder juiste gegevens
    public ApiConnector(String account, String pin, boolean localhost) {
        this.account = account;
        this.pin = pin;
        if (localhost) {
            this.httpClient = new OkHttpClient();
            this.apiUrl = "http://localhost:8080";
        } else {
            this.httpClient = secureHttpClient.build();
            this.apiUrl = "https://145.24.222.131:8080";
        }
    }

    public String verifyPin(String account, String pin) throws IOException {
        JSONObject json = new JSONObject();
        json.put("pin", pin);
        json.put("account", account);
        JSONObject json1 = basicRequest("PUT", "/user", json.toString());
        try {
            String balance = json1.getString("balance");
            return balance;
        } catch (DateTimeParseException | JSONException e) {
            throw new IOException();
        }

    }

    public String getBalance() throws IOException {
        JSONObject json = basicRequest("GET", "/user/balance", "");
        try {
            String balance = json.getString("balance");
            return balance;
        } catch (DateTimeParseException | JSONException e) {
            throw new IOException();
        }
    }

    public void makeWithdraw(double amount) throws IOException {
        JSONObject json = new JSONObject();
        json.put("balance", amount);
        basicRequest("PUT", "/user", json.toString());
    }

    protected JSONObject basicRequest(String method, String endpoint, String content) throws IOException {
        Request.Builder requestBuilder = new Request.Builder()
//                .header("originCountry", originCountry)
//                .header("originBank", originBank)
//                .header("receiveCountry", receiveCountry)
//                .header("receiveBank", receiveBank)
                .header("X-ApiKey", apiKey)
                .url(apiUrl + endpoint);

        RequestBody body = RequestBody.create(
                content, MediaType.get("application/json; charset=utf-8"));

        switch (method) {
            case "GET":
                requestBuilder.put(body);
                break;
            case "DELETE":
                requestBuilder.delete();
                break;
            case "POST":
                requestBuilder.post(body);
                break;
            case "PUT":
                requestBuilder.put(body);
                break;
            default:
                throw new IllegalArgumentException();
        }

        Response response = httpClient.newCall(requestBuilder.build()).execute();
        String responseString = response.body().string();
        response.body().close();

        try {
            switch (response.code()) {
                case 200:
                    //Got a good response, now return appropriately.
                    switch (method) {
                        case "DELETE":
                        case "POST":
//                        case "PUT":
//                            return null;
                        default:
                            return new JSONObject(responseString);
                    }
                case 401:
                    JSONObject json = new JSONObject(responseString);
                    String error = json.getString("message");
                    if (error.equals("Pas is geblokkeerd")) {
                        System.out.println(error);
                    } else {
                        System.out.println(error);
                    }
                default:
                    throw new IOException();
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            throw new IOException("Error code: " + response.code()
                    + ", response: " + response.body().string());
        }
    }
}
