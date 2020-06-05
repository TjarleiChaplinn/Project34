package Server.java.server;

import Server.java.gosbankClient.Client;
import Server.java.gosbankClient.Codes;
import Server.java.gosbankClient.Log;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.PemKeyCertOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import org.json.JSONObject;

public class userAuthentication extends AbstractVerticle {

    private SQLClient sqlClient;

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new userAuthentication());
    }

    public void start() {
        final Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        sqlClient = new SQLClient(vertx);
        DbToRes dbToRes = new DbToRes(sqlClient);
        Client.getInstance().connect();
        Client.setSqlclient(sqlClient);

        //Returns balance
        router.put("/user/balance").handler(dbToRes::getBalance);

        //verifies user
        router.put("/user/verify").handler(dbToRes::verifyUser);

        //withdraw money
        router.put("/user/withdraw").handler(dbToRes::withdrawAmount);


        //Gosbank
        router.put("/gosbank/balance").handler(routingContext -> {
            JsonObject userInfo = routingContext.getBodyAsJson();
//            System.out.println(userInfo.encodePrettily());
            String countryCode = userInfo.getString("account").substring(0,2);
            String bankCode = userInfo.getString("account").substring(3,7);
            String bankNumber = userInfo.getString("account").substring(8,16);
            String pin = userInfo.getString("pin");

            HttpServerResponse response = routingContext.response()
                    .putHeader("content-type", "application/json");

            if (Client.getInstance().isConnected()) {
                String account = String.format("%s-%s-%s", countryCode, bankCode, bankNumber);
                System.out.println(account);

                Client.getInstance().sendBalanceMessage(account, pin, (JSONObject data) -> {
                    JSONObject body = data.getJSONObject("body");
                    int code = body.getInt("code");
                    if (code == Codes.SUCCESS) {
                        Log.info("Balance " + account + ": " + body.getFloat("balance"));
                        response.setStatusCode(200);
                        response.end(new JsonObject().put("balance", body.getFloat("balance")).encode());
                    } else {
                        Log.warning("Balance error code: " + code);
                    }
                });
            }
        });

        router.put("/gosbank/payment").handler(routingContext -> {
            JsonObject userInfo = routingContext.getBodyAsJson();
            String countryCode = userInfo.getString("account").substring(0,2);
            String bankCode = userInfo.getString("account").substring(3,7);
            String bankNumber = userInfo.getString("account").substring(8,16);
            String pin = userInfo.getString("pin");
            float amount = userInfo.getFloat("amount");

            HttpServerResponse response = routingContext.response()
                    .putHeader("content-type", "application/json");

            if (Client.getInstance().isConnected()) {
                System.out.println("OK");
                String fromAccount = String.format("%s-%s-%s", countryCode, bankCode, bankNumber);
                System.out.println(fromAccount);
                String toAccount = "SO-BANK-00000001";
                Client.getInstance().sendPaymentMessage(fromAccount, toAccount, pin, amount, (JSONObject data) -> {
                    JSONObject body = data.getJSONObject("body");
                    int code = body.getInt("code");
                    if (code == Codes.SUCCESS) {
                        Log.info("Payment success");
                        response.setStatusCode(200);
                        response.end(new JsonObject().put("message", "Transactie gelukt").encode());
                    }
                    else {
                        Log.warning("Payment error code: " + code);
                        response.setStatusCode(402);
                        response.end(new JsonObject().put("message", "Niet genoeg geld op rekening").encode());
                    }
                });
            }
        });

        //not local
        HttpServerOptions secureOptions = new HttpServerOptions()
                .setSsl(true)
                .setPemKeyCertOptions(new PemKeyCertOptions()
                        .setCertPath("/home/ubuntu-0968241/ssl-cert/certificate.pem")
                        .setKeyPath("/home/ubuntu-0968241/ssl-cert/cert.key"));

//        //local
//        HttpServerOptions secureOptions = new HttpServerOptions()
//                .setSsl(false);

        vertx.createHttpServer(secureOptions)
                .requestHandler(router)
                .listen(8080, res -> {
                    if (res.succeeded()) {
                        System.out.println("Server is now listening!");
                    } else {
                        System.out.println("Failed to bind!");
                    }
                });
    }
}
