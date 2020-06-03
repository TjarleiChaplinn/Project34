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
    private String message;
    private boolean verification;

    private String originCountry = "SO";
    private String originBank = "BANK";
    private String receiveCountry;
    private String receiveBank;

    public String getMessage() {
        return message;
    }
    //ApiConnector
    //moet aangepast worden want moet ook kunnen verbinden zonder juiste gegevens
    public ApiConnector(String account, String pin, boolean localhost) {
        this.account = account;
        this.pin = pin;
        if (localhost) {
            this.httpClient = new OkHttpClient();
            this.apiUrl = "http://localhost:8082";
        } else {
            this.httpClient = secureHttpClient.build();
            this.apiUrl = "https://145.24.222.131:8080";
        }
    }

    public boolean verifyPin(String account, String pin) throws IOException {
        JSONObject json = new JSONObject();
        json.put("pin", pin);
        json.put("account", account);
        if (account.substring(3,7).equals("BANK")) {
            JSONObject json1 = basicRequest("PUT", "/user/verify", json.toString());
        } else {
//            json.put("country", account.substring(0,2));
//            json.put("bank", account.substring(3,7));
            JSONObject json1 = basicRequest("PUT", "/gosbank/balance", json.toString());
        }
        return verification;
    }

    public String getBalance(String account, String pin) throws IOException {
        JSONObject json1 = new JSONObject();
        json1.put("pin", pin);
        json1.put("account", account);
        JSONObject json = basicRequest("PUT", "/user/balance", json1.toString());
        try {
            String balance = json.getString("balance");
            return balance;
        } catch (DateTimeParseException | JSONException e) {
            throw new IOException();
        }
    }

    public void makeWithdraw(String account, String pin, Float amount) throws IOException {
        JSONObject json = new JSONObject();
        json.put("amount", amount);
        json.put("pin", pin);
        json.put("account", account);

        if (account.substring(3,7).equals("BANK")) {
            JSONObject json1 = basicRequest("PUT", "/user/withdraw", json.toString());
        } else {
//            json.put("country", account.substring(0,2));
//            json.put("bank", account.substring(3,7));
            JSONObject json1 = basicRequest("PUT", "/gosbank/payment", json.toString());
        }
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
                        case "PUT":
                            verification = true;
//                            return null;
                        default:
                            return new JSONObject(responseString);
                    }
                case 401:
                    JSONObject json = new JSONObject(responseString);
                    String error = json.getString("message");
                    message = error;
                    verification = false;
                    System.out.println(error);
                    return new JSONObject(responseString);
                case 402:
                    JSONObject json1 = new JSONObject(responseString);
                    String error1 = json1.getString("message");
                    message = error1;
                    System.out.println(error1);
                    return new JSONObject(responseString);
                case 403:
                    JSONObject json2 = new JSONObject(responseString);
                    String error2 = json2.getString("message");
                    message = error2;
                    System.out.println(error2);
                    return new JSONObject(responseString);
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
