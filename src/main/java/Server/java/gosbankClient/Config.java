package Server.java.gosbankClient;

// Static config class
public class Config {
    private Config() {}

    public static final boolean DEBUG = true;

    public static final String GOSBANK_URL = "wss://ws.gosbank.ml/";
        //"ws://localhost:8080/";
    //"wss://ws.gosbank.ml/";

    public static final String COUNTRY_CODE = "SO";
    public static final String BANK_CODE = "BANK"; //"BANK"

    public static final int RECONNECT_TIMEOUT = 2 * 1000;
}
