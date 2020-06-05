package Server.java.server;

import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public class DbToRes {
    private SQLClient sqlClient;

    /**
     * Constructor.
     *
     * @param sqlClient SQLClient object.
     */
    DbToRes(SQLClient sqlClient) {
        this.sqlClient = sqlClient;
    }

    void getBalance(RoutingContext routingContext) {
        HttpServerResponse response = routingContext.response()
                .putHeader("content-type", "application/json");

        JsonObject userInfo = routingContext.getBodyAsJson();
        System.out.println(userInfo.encodePrettily());
        String account = userInfo.getString("account");
        String pin = userInfo.getString("pin");

        sqlClient.getBalanceInfo(account, pin, res -> {
            if (res.succeeded()) {
                response.setStatusCode(200);
                response.end(new JsonObject().put("balance", res.result()).encode());
            }
        });
    }

    void verifyUser(RoutingContext routingContext) {
        System.out.println("VERIFYUSER");
        HttpServerResponse response = routingContext.response()
                .putHeader("content-type", "application/json");

        JsonObject userInfo = routingContext.getBodyAsJson();
        System.out.println(userInfo.encodePrettily());
        String account = userInfo.getString("account");
        String pin = userInfo.getString("pin");

        //Checks if pas exists and if pas is blocked
        sqlClient.verifyAccountExists(account, res0 -> {
            if (res0.succeeded()) {
                sqlClient.verifyPinAttempts(account, res -> {
                    if (res.succeeded()) {
                        System.out.println("DbToRes: verifypinattempts succeeded");
                        //Verifies the user
                        sqlClient.verifyUserInfo(account, pin, res1 -> {
                            if (res1.succeeded()) {
                                System.out.println("DbToRes: verifyuserinfo succeeded");
                                response.setStatusCode(200);
                                System.out.println("Test: DbToRes" + res1.result());
                                response.end(new JsonObject().put("message", res1.result()).encode());
                            } else {
                                response.setStatusCode(401);
                                System.out.println("Test: res1 failed");
                                sqlClient.getPinAttempts(account, res2 -> {
                                    response.end(new JsonObject().put("message", "Aantal pogingen: " + res2.result()).encode());
                                });
                            }
                        });
                    } else {
                        response.setStatusCode(403);
                        response.end(new JsonObject().put("message", "Pas is geblokkeerd").encode());
                    }
                });

            } else {
                response.setStatusCode(404);
                response.end(new JsonObject().put("message", "Rekeningnummer bestaat niet").encode());
            }
        });
    }

    void withdrawAmount(RoutingContext routingContext) {
        HttpServerResponse response = routingContext.response()
                .putHeader("content-type", "application/json");

        JsonObject userInfo = routingContext.getBodyAsJson();
        System.out.println(userInfo.encodePrettily());
        String account = userInfo.getString("account");
        String pin = userInfo.getString("pin");
        Float amount = userInfo.getFloat("amount");

        sqlClient.getBalanceInfo(account, pin, res -> {
            if (res.succeeded()) {
                float balanceAmount = Float.parseFloat(res.result());
                if (balanceAmount >= amount) {
                    sqlClient.makeWithdraw(account, pin, String.valueOf(amount), res1 -> {
                        if (res1.succeeded()) {
                            response.setStatusCode(200);
                            System.out.println("Transactie gelukt");
                            response.end(new JsonObject().put("message", "Transactie gelukt").encode());
                        }
                    });
                } else {
                    response.setStatusCode(402);
                    response.end(new JsonObject().put("message", "Niet genoeg geld op rekening").encode());
                }
            }
        });

    }
}
