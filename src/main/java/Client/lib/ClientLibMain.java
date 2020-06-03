package Client.lib;

public class ClientLibMain {
    /**
     * This class is only used for directly running methods in clientlib.
     * So the code here is not used in the rest of the project.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        try {
            //mock-up for demo 2 actions from GUI

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