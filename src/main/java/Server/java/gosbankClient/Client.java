package Server.java.gosbankClient;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;
import Server.java.server.SQLClient;

import java.net.URI;
import java.util.ArrayList;

// Websocket client class
public class Client extends WebSocketClient {
    public static interface OnResponseListener {
        public void onResponse(JSONObject data);
    }

    public static class CallbackItem {
        public long id;
        public String type;
        public OnResponseListener onResponseListener;
    }

    private boolean connected;
    private ArrayList<CallbackItem> pendingCallbacks;

    public boolean isConnected() {
        return connected;
    }

    private void sendMessage(String type, JSONObject data, OnResponseListener onResponseListener) {
        long id = System.currentTimeMillis();

        if (onResponseListener != null) {
            CallbackItem callbackItem = new CallbackItem();
            callbackItem.id = id;
            callbackItem.type = type + "_response";
            callbackItem.onResponseListener = onResponseListener;
            pendingCallbacks.add(callbackItem);
        }

        JSONObject message = new JSONObject();
        message.put("id", id);
        message.put("type", type);
        message.put("data", data);
        String jsonMessage = message.toString();
        Log.debug("Sent: " + jsonMessage);
        send(jsonMessage);
    }

    public void sendRegisterMessage(OnResponseListener onResponseListener) {
        JSONObject registerMessage = new JSONObject();

        JSONObject registerMessageHeader = new JSONObject();
        registerMessageHeader.put("originCountry", Config.COUNTRY_CODE);
        registerMessageHeader.put("originBank", Config.BANK_CODE);
        registerMessageHeader.put("receiveCountry", "SO");
        registerMessageHeader.put("receiveBank", "GOSB");
        registerMessage.put("header", registerMessageHeader);

        registerMessage.put("body", new JSONObject());

        sendMessage("register", registerMessage, onResponseListener);
    }

    public void sendBalanceMessage(String account, String pin, OnResponseListener onResponseListener) {
        Utils.AccountParts accountParts = Utils.parseAccountParts(account);

        JSONObject balanceMessage = new JSONObject();

        JSONObject balanceMessageHeader = new JSONObject();
        balanceMessageHeader.put("originCountry", Config.COUNTRY_CODE);
        balanceMessageHeader.put("originBank", Config.BANK_CODE);
        balanceMessageHeader.put("receiveCountry", accountParts.country);
        balanceMessageHeader.put("receiveBank", accountParts.bank);
        balanceMessage.put("header", balanceMessageHeader);

        JSONObject balanceMessageBody = new JSONObject();
        balanceMessageBody.put("account", account);
        balanceMessageBody.put("pin", pin);
        balanceMessage.put("body", balanceMessageBody);

        sendMessage("balance", balanceMessage, onResponseListener);
    }

    public void sendPaymentMessage(String fromAccount, String toAccount, String pin, float amount, OnResponseListener onResponseListener) {
        Utils.AccountParts formAccountParts = Utils.parseAccountParts(fromAccount);
        Utils.AccountParts toAccountParts = Utils.parseAccountParts(toAccount);

        JSONObject paymentMessage = new JSONObject();

        JSONObject paymentMessageHeader = new JSONObject();
        paymentMessageHeader.put("originCountry", Config.COUNTRY_CODE);
        paymentMessageHeader.put("originBank", Config.BANK_CODE);
        if (!formAccountParts.bank.equals(Config.BANK_CODE)) {
            paymentMessageHeader.put("receiveCountry", formAccountParts.country);
            paymentMessageHeader.put("receiveBank", formAccountParts.bank);
        }
        if (!toAccountParts.bank.equals(Config.BANK_CODE)) {
            paymentMessageHeader.put("receiveCountry", toAccountParts.country);
            paymentMessageHeader.put("receiveBank", toAccountParts.bank);
        }
        paymentMessage.put("header", paymentMessageHeader);

        JSONObject paymentMessageBody = new JSONObject();
        paymentMessageBody.put("fromAccount", fromAccount);
        paymentMessageBody.put("toAccount", toAccount);
        paymentMessageBody.put("pin", pin);
        paymentMessageBody.put("amount", amount);
        paymentMessage.put("body", paymentMessageBody);

        sendMessage("payment", paymentMessage, onResponseListener);
    }

    public void responseMessage(long id, String type, JSONObject data) {
        JSONObject message = new JSONObject();
        message.put("id", id);
        message.put("type", type + "_response");
        message.put("data", data);
        String jsonMessage = message.toString();
        Log.debug("Responed: " + jsonMessage);
        send(jsonMessage);
    }

    private Client(URI uri) {
        super(uri);
        connected = false;
        pendingCallbacks = new ArrayList<CallbackItem>();
    }

    private static Client instance;
    private static SQLClient sqlClient;
//    private Vertx vertx = Vertx.vertx();
//    private SQLClient sqlClient = new SQLClient(vertx);

    public static void setSqlclient(SQLClient sqlclient2) {
        sqlClient = sqlclient2;
    }

    public static Client getInstance() {
        if (instance == null) {
            try {
                instance = new Client(new URI(Config.GOSBANK_URL));
            } catch (Exception exception) {
                Log.error(exception);
            }
        }
        return instance;
    }

    public void onOpen(ServerHandshake handshakedata) {
        Log.debug("Connected");

        sendRegisterMessage((JSONObject data) -> {
            JSONObject body = data.getJSONObject("body");
            int code = body.getInt("code");
            if (code == Codes.SUCCESS) {
                connected = true;

                Log.info("Registered with Gosbank with bank code: " + Config.BANK_CODE);

                // Do stuff
            } else {
                Log.warning("Error with registering to Gosbank, code: " + code);
            }
        });
    }

    public void onMessage(String jsonMessage) {
        try {
            Log.debug("Message: " + jsonMessage);

            // Parse message
            JSONObject message = new JSONObject(jsonMessage);
            long id = message.getLong("id");
            String type = message.getString("type");
            JSONObject data = message.getJSONObject("data");
            JSONObject header = data.getJSONObject("header");
            JSONObject body = data.getJSONObject("body");

            // Resolve pending callbacks
            for (int i = 0; i < pendingCallbacks.size(); i++) {
                CallbackItem callbackItem = pendingCallbacks.get(i);
                if (callbackItem.id == id && callbackItem.type.equals(type)) {
                    callbackItem.onResponseListener.onResponse(data);
                    pendingCallbacks.remove(i--);
                }
            }

            if (type.equals("balance")) {
                Log.info("Balance request from " + body.getString("account"));
                // Fetch balance
                String account = body.getString("account");
                String pin = body.getString("pin");

                // Send response message back;
                JSONObject balanceMessage = new JSONObject();

                JSONObject balanceMessageHeader = new JSONObject();
                balanceMessageHeader.put("originCountry", Config.COUNTRY_CODE);
                balanceMessageHeader.put("originBank", Config.BANK_CODE);
                balanceMessageHeader.put("receiveCountry", header.getString("originCountry"));
                balanceMessageHeader.put("receiveBank", header.getString("originBank"));
                balanceMessage.put("header", balanceMessageHeader);

                JSONObject balanceMessageBody = new JSONObject();

                sqlClient.verifyAccountExists(account, res0 -> {
                    if (res0.succeeded()) {
                        System.out.println("Verifyaccount succeeded");
                        sqlClient.verifyPinAttempts(account, res -> {
                            if (res.succeeded()) {
                                System.out.println("Verifypinattempts succeeded");
                                sqlClient.verifyUserInfo(account, pin, res1 -> {
                                    if (res1.succeeded()) {
                                        sqlClient.getBalanceInfo(account, pin, res2 -> {
                                            if (res2.succeeded()) {
                                                float balance = Float.parseFloat(res2.result());
                                                System.out.println("TESTTESTESTESTEST: " + balance);

                                                balanceMessageBody.put("code", Codes.SUCCESS);
                                                balanceMessageBody.put("balance", balance);
                                                balanceMessage.put("body", balanceMessageBody);

                                                responseMessage(id, "balance", balanceMessage);
                                            }
                                        });
                                    } else {
                                        balanceMessageBody.put("code", Codes.AUTH_FAILD);
                                        sqlClient.getPinAttempts(account, res3 -> {
                                            int attempts = Integer.valueOf(res3.result());
                                            balanceMessageBody.put("attempts", attempts);
                                            balanceMessage.put("body", balanceMessageBody);
                                            responseMessage(id, "balance", balanceMessage);
                                        });

                                    }
                                });
                            } else {
                                System.out.println("Verifypinattempts failed");
                                balanceMessageBody.put("code", Codes.BLOCKED);
                                balanceMessage.put("body", balanceMessageBody);
                                Log.debug("balancemessage: " + balanceMessage);
                                responseMessage(id, "balance", balanceMessage);

                            }
                        });

                    } else {
                        balanceMessageBody.put("code", Codes.NOT_FOUND);
                        balanceMessage.put("body", balanceMessageBody);

                        responseMessage(id, "balance", balanceMessage);
                    }
                });
            }

            if (type.equals("payment")) {
                Log.info("Payment request from " + body.getString("fromAccount") + " to " + body.getString("toAccount"));

                String account = body.getString("fromAccount");
                String pin = body.getString("pin");
                float amount = body.getFloat("amount");
                // Send response back
                JSONObject paymentMessage = new JSONObject();

                JSONObject paymentMessageHeader = new JSONObject();
                paymentMessageHeader.put("originCountry", Config.COUNTRY_CODE);
                paymentMessageHeader.put("originBank", Config.BANK_CODE);
                paymentMessageHeader.put("receiveCountry", header.getString("originCountry"));
                paymentMessageHeader.put("receiveBank", header.getString("originBank"));
                paymentMessage.put("header", paymentMessageHeader);

                JSONObject paymentMessageBody = new JSONObject();


                // Process payment in database
                sqlClient.verifyAccountExists(account, res0 -> {
                    if (res0.succeeded()) {
                        sqlClient.verifyPinAttempts(account, res -> {
                            if (res.succeeded()) {
                                sqlClient.verifyUserInfo(account, pin, res1 -> {
                                    if (res1.succeeded()) {
                                        sqlClient.getBalanceInfo(account, pin, res2 -> {
                                            if (res2.succeeded()) {
                                                float balanceAmount = Float.parseFloat(res2.result());
                                                if (amount > 0) {
                                                    if (balanceAmount >= amount) {
                                                        sqlClient.makeWithdraw(account, pin, String.valueOf(amount), res3 -> {
                                                            if (res3.succeeded()) {
                                                                paymentMessageBody.put("code", Codes.SUCCESS);
                                                                paymentMessage.put("body", paymentMessageBody);

                                                                responseMessage(id, "payment", paymentMessage);
                                                            }
                                                        });
                                                    } else {
                                                        paymentMessageBody.put("code", Codes.NOT_ENOUGHT_BALANCE);
                                                        paymentMessage.put("body", paymentMessageBody);

                                                        responseMessage(id, "payment", paymentMessage);
                                                    }
                                                } else {
                                                    paymentMessageBody.put("code", Codes.BROKEN);
                                                    paymentMessage.put("body", paymentMessageBody);

                                                    responseMessage(id, "payment", paymentMessage);
                                                }

                                            }
                                        });

                                    } else {
                                        paymentMessageBody.put("code", Codes.AUTH_FAILD);
                                        sqlClient.getPinAttempts(account, res3 -> {
                                            int attempts = Integer.valueOf(res3.result());
                                            paymentMessageBody.put("attempts", attempts);
                                            paymentMessage.put("body", paymentMessageBody);
                                            responseMessage(id, "payment", paymentMessage);
                                        });

                                    }
                                });
                            } else {
                                paymentMessageBody.put("code", Codes.BLOCKED);
                                paymentMessage.put("body", paymentMessageBody);

                                responseMessage(id, "payment", paymentMessage);

                            }
                        });
                    } else {
                        paymentMessageBody.put("code", Codes.NOT_FOUND);
                        paymentMessage.put("body", paymentMessageBody);

                        responseMessage(id, "payment", paymentMessage);
                    }
                });
            }
        } catch (Exception exception) {
            Log.warning(exception);
        }
    }

    public void onClose(int code, String reason, boolean remote) {
        connected = false;

        Log.warning("Disconnected, try to reconnect in " + (Config.RECONNECT_TIMEOUT / 1000) + " seconds!");

        new Thread(() -> {
            try {
                Thread.sleep(Config.RECONNECT_TIMEOUT);
            } catch (Exception exception) {
                Log.error(exception);
            }

            reconnect();
        }).start();
    }

    public void onError(Exception exception) {
        Log.warning(exception);
    }
}
