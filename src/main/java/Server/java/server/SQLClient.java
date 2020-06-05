package Server.java.server;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.mysqlclient.MySQLConnectOptions;
import io.vertx.mysqlclient.MySQLPool;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.SqlConnection;

public class SQLClient {

    private static MySQLPool client;

    MySQLConnectOptions connectOptions = new MySQLConnectOptions()
            .setPort(3306)
            .setHost("localhost")
            .setDatabase("ATM")
            .setUser("xo17t23s5m") //xo17t23s5m
            .setPassword("fJvX8UmaPmUMip9t"); //fJvX8UmaPmUMip9t

    // Pool options
    PoolOptions poolOptions = new PoolOptions()
            .setMaxSize(5);

    // Create the pooled client

    public SQLClient(Vertx vertx) {
        client = MySQLPool.pool(vertx, connectOptions, poolOptions);
    }

    public void verifyAccountExists(String account, Handler<AsyncResult<Boolean>> handler) {
        client.getConnection(ar -> {
            if (ar.succeeded()) {

                SqlConnection conn = ar.result();
                System.out.println(account);
                conn
                        .query("Select Rekeningnummer FROM G_Gev WHERE Rekeningnummer=" + "'" + account + "'")
                        .execute(ar2 -> {
                            RowSet<Row> result = ar2.result();
                            if (result.size() == 1) {
                                handler.handle(Future.succeededFuture(Boolean.TRUE));
                            } else {
                                handler.handle(Future.failedFuture(ar.cause()));
                            }
                        });
                conn.close();
            }
        });
    }

    public void verifyPinAttempts(String account, Handler<AsyncResult<Boolean>> handler) {
        client.getConnection(ar -> {
            if (ar.succeeded()) {

                SqlConnection conn = ar.result();
                System.out.println(account);
                conn
                        .query("Select Pinpogingen FROM G_Gev WHERE Rekeningnummer=" + "'" + account + "'")
                        .execute(ar2 -> {
                            RowSet<Row> result = ar2.result();
                            Row row = result.iterator().next();
                            if (row.getInteger(0) < 3) {
                                handler.handle(Future.succeededFuture(Boolean.TRUE));
                            } else {
                                handler.handle(Future.failedFuture(ar.cause()));
                            }
                        });
                conn.close();
            }
        });
    }


    public void verifyUserInfo(String account, String pin, Handler<AsyncResult<String>> handler) {
        client.getConnection(ar -> {
            if (ar.succeeded()) {

                SqlConnection conn = ar.result();

                conn
                        .query("Select Saldo FROM G_Gev WHERE Rekeningnummer=" + "'" + account + "'" + " AND Pincode=" + pin)
                        .execute(ar2 -> {
                            if (ar2.succeeded()) {
                                if (ar2.result().size() == 1) {
                                    client
                                            .query("UPDATE G_Gev SET Pinpogingen=0 WHERE Rekeningnummer=" + "'" + account + "'")
                                            .execute(ar3 -> {
                                                if (ar3.succeeded()) {
                                                    System.out.println("pin attempts set to 0");
                                                }
                                            });

                                    System.out.println("Correcte combinatie account en pin");
                                    String balance = String.valueOf(ar2.result().iterator().next().getInteger(0));
                                    handler.handle(Future.succeededFuture(balance));
                                } else {
                                    handler.handle(Future.failedFuture("Incorrecte pin"));
                                }

                            } else {
                                handler.handle((Future.failedFuture(ar2.cause())));
                            }
                        });
                conn.close();
            }
        });
    }

    public void getPinAttempts(String account, Handler<AsyncResult<String>> handler) {
        client.getConnection(ar -> {
            if (ar.succeeded()) {

                SqlConnection conn = ar.result();

                conn
                        .query("UPDATE G_Gev SET Pinpogingen=Pinpogingen+1 WHERE Rekeningnummer=" + "'" + account + "'")
                        .execute(ar3 -> {
                            if (ar3.succeeded()) {
                                System.out.println("Pin pogingen omhoog");
                                client
                                        .query("SELECT pinpogingen FROM G_Gev WHERE Rekeningnummer=" + "'" + account + "'")
                                        .execute(ar4 -> {
                                            if (ar4.succeeded()) {
                                                String pinPogingen = String.valueOf(3 - ar4.result().iterator().next().getInteger(0));
                                                handler.handle(Future.succeededFuture(pinPogingen));

                                            }
                                        });
                            }
                        });
                conn.close();
            } else {
                System.out.println("Pas is geblokkeerd");
                handler.handle(Future.succeededFuture());
            }
        });
    }


    public void getBalanceInfo(String account, String pin, Handler<AsyncResult<String>> handler) {
        client.getConnection(ar -> {
            if (ar.succeeded()) {
                System.out.println("Connected");

                SqlConnection conn = ar.result();

                conn
                        .query("SELECT Saldo FROM G_Gev WHERE Rekeningnummer=" + "'" + account + "'" + " AND Pincode=" + pin)
                        .execute(ar1 -> {
                            if (ar1.succeeded()) {
                                System.out.println("TESTESTESTESTEST");
                                String balance = String.valueOf(ar1.result().iterator().next().getFloat(0));
//                                System.out.println("test: balance in sqlclient:"+balance);
                                handler.handle(Future.succeededFuture(balance));
                            } else {
                                handler.handle(Future.failedFuture(ar1.cause()));
                            }
                        });
                conn.close();
            }
        });
    }

    public void makeWithdraw(String account, String pin, String
            amount, Handler<AsyncResult<String>> handler) {
        client.getConnection(ar -> {
            if (ar.succeeded()) {
                System.out.println("Connected");

                SqlConnection conn = ar.result();

                conn
                        .query("UPDATE G_Gev SET Saldo=Saldo-" + amount + " WHERE Rekeningnummer=" + "'" + account + "'" + " AND pincode=" + pin)
                        .execute(ar1 -> {
                            if (ar1.succeeded()) {
                                handler.handle(Future.succeededFuture());
                            } else {
                                handler.handle(Future.failedFuture(ar1.cause()));
                            }
                        });
                conn.close();
            }
        });
    }
}
