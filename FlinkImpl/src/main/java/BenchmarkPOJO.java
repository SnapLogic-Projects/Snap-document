import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.operators.Order;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.io.TextOutputFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.apache.flink.core.fs.FileSystem.WriteMode.OVERWRITE;

public class BenchmarkPOJO {

    private static final Logger logger = LoggerFactory.getLogger(BenchmarkPOJO.class.getName());

    public static class InputCSV {
        private String dRGDefinition;
        private int providerId;
        private String providerName;
        private String providerStreetAddress;
        private String providerCity;
        private String providerState;
        private String providerZipCode;
        private String hospitalReferralRegionDescription;
        private int totalDischarges;
        private String averageCoveredCharges;
        private String averageTotalPayments;
        private String averageMedicarePayments;

        public InputCSV() {

        }

        public InputCSV(String dRGDefinition, int providerId, String providerName, String providerStreetAddress,
                       String providerCity, String providerState, String providerZipCode, String hospitalReferralRegionDescription,
                       int totalDischarges, String averageCoveredCharges, String averageTotalPayments, String averageMedicarePayments) {
            this.dRGDefinition = dRGDefinition;
            this.providerId = providerId;
            this.providerName = providerName;
            this.providerStreetAddress = providerStreetAddress;
            this.providerCity = providerCity;
            this.providerState = providerState;
            this.providerZipCode = providerZipCode;
            this.hospitalReferralRegionDescription = hospitalReferralRegionDescription;
            this.totalDischarges = totalDischarges;
            this.averageCoveredCharges = averageCoveredCharges;
            this.averageTotalPayments = averageTotalPayments;
            this.averageMedicarePayments = averageMedicarePayments;
        }

        public int getProviderId() {
            return providerId;
        }

        public int getTotalDischarges() {
            return totalDischarges;
        }

        public String getAverageCoveredCharges() {
            return averageCoveredCharges;
        }

        public String getAverageMedicarePayments() {
            return averageMedicarePayments;
        }

        public String getAverageTotalPayments() {
            return averageTotalPayments;
        }

        public String getdRGDefinition() {
            return dRGDefinition;
        }

        public String getHospitalReferralRegionDescription() {
            return hospitalReferralRegionDescription;
        }

        public String getProviderCity() {
            return providerCity;
        }

        public String getProviderName() {
            return providerName;
        }

        public String getProviderState() {
            return providerState;
        }

        public String getProviderStreetAddress() {
            return providerStreetAddress;
        }

        public String getProviderZipCode() {
            return providerZipCode;
        }

        public void setAverageCoveredCharges(String averageCoveredCharges) {
            this.averageCoveredCharges = averageCoveredCharges;
        }

        public void setAverageMedicarePayments(String averageMedicarePayments) {
            this.averageMedicarePayments = averageMedicarePayments;
        }

        public void setAverageTotalPayments(String averageTotalPayments) {
            this.averageTotalPayments = averageTotalPayments;
        }

        public void setdRGDefinition(String dRGDefinition) {
            this.dRGDefinition = dRGDefinition;
        }

        public void setHospitalReferralRegionDescription(String hospitalReferralRegionDescription) {
            this.hospitalReferralRegionDescription = hospitalReferralRegionDescription;
        }

        public void setProviderCity(String providerCity) {
            this.providerCity = providerCity;
        }

        public void setProviderId(int providerId) {
            this.providerId = providerId;
        }

        public void setProviderName(String providerName) {
            this.providerName = providerName;
        }

        public void setProviderState(String providerState) {
            this.providerState = providerState;
        }

        public void setProviderStreetAddress(String providerStreetAddress) {
            this.providerStreetAddress = providerStreetAddress;
        }

        public void setProviderZipCode(String providerZipCode) {
            this.providerZipCode = providerZipCode;
        }

        public void setTotalDischarges(int totalDischarges) {
            this.totalDischarges = totalDischarges;
        }
    }

    public static void main(String[] args) {

        final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        // warn up
        for (int i = 0; i < 10; i++) {

            process(env);
            try {
                env.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        long startTime = System.nanoTime();
        for (int i = 0; i < 50; i++) {

            process(env);
            try {
                env.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        long endTime = System.nanoTime();

        long duration = (endTime - startTime);  //divide by 1000000 to get milliseconds.
//        System.out.println("It takes : " + duration1 / 1000000l + " milliseconds to finish.");
        logger.info("It takes : " + duration / 1000000L / 50L + " milliseconds to finish.");
    }

    static void process(ExecutionEnvironment env) {
        long startTime = System.nanoTime();

        DataSet<InputCSV> csvInput = env.readCsvFile("test.csv")
                .ignoreFirstLine()
                .parseQuotedStrings('"')
                .pojoType(InputCSV.class, "dRGDefinition", "providerId", "providerName", "providerStreetAddress",
                        "providerCity", "providerState", "providerZipCode", "hospitalReferralRegionDescription",
                        "totalDischarges", "averageCoveredCharges", "averageTotalPayments", "averageMedicarePayments");
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);  //divide by 1000000 to get milliseconds.
        logger.info("read takes : " + duration / 1000000L + " milliseconds to finish.");

        startTime = System.nanoTime();
        DataSet<InputCSV> output0 = csvInput.filter(new FilterFunction<InputCSV>() {
            @Override
            public boolean filter(InputCSV inputCSV) throws Exception {
                return inputCSV.getProviderState().equals("AL");
            }
        }).sortPartition("providerCity", Order.DESCENDING)
                .partitionByRange("providerCity");
        endTime = System.nanoTime();
        duration = (endTime - startTime);  //divide by 1000000 to get milliseconds.
        logger.info("execute takes : " + duration / 1000000L + " milliseconds to finish.");

        startTime = System.nanoTime();
        output0.writeAsFormattedText("BenchmarkPOJO.csv", OVERWRITE,
                new TextOutputFormat.TextFormatter<InputCSV>() {
                    @Override
                    public String format(InputCSV inputCSV) {
                        return inputCSV.getProviderState() + "|"
                                + inputCSV.getAverageCoveredCharges() + "|"
                                + inputCSV.getAverageMedicarePayments() + "|"
                                + inputCSV.getAverageTotalPayments() + "|"
                                + inputCSV.getdRGDefinition() + "|"
                                + inputCSV.getHospitalReferralRegionDescription() + "|"
                                + inputCSV.getProviderName() + "|"
                                + inputCSV.getProviderName() + "|";
                    }
                }).setParallelism(1);
        endTime = System.nanoTime();
        duration = (endTime - startTime);  //divide by 1000000 to get milliseconds.
        logger.info("write takes : " + duration / 1000000L + " milliseconds to finish.");
    }




}
