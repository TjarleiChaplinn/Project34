package Client.lib;

//Main voor het testen van apiConnector methods
public class ClientLibMain {
    public static void main(String[] args) {
        try {

            //start of application
            ApiConnector apiconnector = new ApiConnector(
                    "SO-BANQ-00000005",
                    "1111",
                    true);
//
//            System.out.println(apiconnector.verifyPin("SO-BANQ-00000012", "1111"));
//            System.out.println(apiconnector.getBalance("SO-BANQ-00000005", "1234"));
            apiconnector.makeWithdraw("SO-BANQ-00000012", "1111", (float) 100);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}