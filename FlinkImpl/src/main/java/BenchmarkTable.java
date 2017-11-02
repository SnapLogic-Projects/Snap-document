import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.core.fs.FileSystem;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.TableEnvironment;
import org.apache.flink.table.api.Types;
import org.apache.flink.table.api.java.BatchTableEnvironment;
import org.apache.flink.table.sinks.CsvTableSink;
import org.apache.flink.table.sources.CsvTableSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class BenchmarkTable {
    private static final Logger logger = LoggerFactory.getLogger(BenchmarkTable.class.getName());

    public static void main(String[] args) throws IOException, ClassNotFoundException {
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

    static void process(ExecutionEnvironment env) throws IOException {
        BatchTableEnvironment tableEnv = TableEnvironment.getTableEnvironment(env);

        CsvTableSource csvSource = CsvTableSource
                .builder()
                .path("test.csv")
                .field("DRGDefinition", Types.STRING())
                .field("ProviderId", Types.INT())
                .field("ProviderName", Types.STRING())
                .field("ProviderStreetAddress", Types.STRING())
                .field("ProviderCity", Types.STRING())
                .field("ProviderState", Types.STRING())
                .field("ProviderZipCode", Types.STRING())
                .field("HospitalReferralRegionDescription",Types.STRING())
                .field("TotalDischarges",Types.INT())
                .field("AverageCoveredCharges",Types.STRING())
                .field("AverageTotalPayments",Types.STRING())
                .field("AverageMedicarePayments",Types.STRING())
                .ignoreFirstLine()
                .quoteCharacter('"')    //string field
                .build();

        tableEnv.registerTableSource("csvTable", csvSource);
        Table result = tableEnv.scan("csvTable").filter("ProviderState === 'AL'").orderBy("ProviderCity");


        result.writeToSink(new CsvTableSink(
                "BenchmarkTable.csv",
                "|",
                1,
                FileSystem.WriteMode.OVERWRITE
        ));
    }
}
