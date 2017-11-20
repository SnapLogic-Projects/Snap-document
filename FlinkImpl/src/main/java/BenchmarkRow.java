import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.operators.Order;
import org.apache.flink.api.common.typeinfo.BasicTypeInfo;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.io.TextOutputFormat;
import org.apache.flink.table.sources.CsvTableSource;
import org.apache.flink.types.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.apache.flink.core.fs.FileSystem.WriteMode.OVERWRITE;

public class BenchmarkRow {

    private static final Logger logger = LoggerFactory.getLogger(BenchmarkRow.class.getName());

    public static void main(String[] args) {
        final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        //warm up
        process(env);

        //test
        long startTime = System.nanoTime();
        for (int i = 0; i < 5; i++) {
            process(env);
            try {
                env.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        long endTime = System.nanoTime();

        long duration = (endTime - startTime);  //divide by 1000000 to get milliseconds.
        logger.info("It takes : " + duration / 1000000L / 5L + " milliseconds to finish.");
    }

    public static void process(ExecutionEnvironment env) {

        String[] fieldNames = {"DRGDefinition", "ProviderId", "ProviderName", "ProviderStreetAddress", "ProviderCity",
                "ProviderState", "ProviderZipCode", "HospitalReferralRegionDescription", "TotalDischarges",
                "AverageCoveredCharges", "AverageTotalPayments", "AverageMedicarePayments"};

        TypeInformation<?>[] fieldTypes = {BasicTypeInfo.STRING_TYPE_INFO, BasicTypeInfo.INT_TYPE_INFO,
                BasicTypeInfo.STRING_TYPE_INFO, BasicTypeInfo.STRING_TYPE_INFO, BasicTypeInfo.STRING_TYPE_INFO,
                BasicTypeInfo.STRING_TYPE_INFO, BasicTypeInfo.STRING_TYPE_INFO, BasicTypeInfo.STRING_TYPE_INFO,
                BasicTypeInfo.INT_TYPE_INFO, BasicTypeInfo.STRING_TYPE_INFO, BasicTypeInfo.STRING_TYPE_INFO,
                BasicTypeInfo.STRING_TYPE_INFO};

        CsvTableSource csvTableSource = new CsvTableSource("FlinkImpl/src/main/resources/test.csv", fieldNames, fieldTypes,
                ",", "\n", '"', true, null, false);

        DataSet<Row> dataSet = csvTableSource.getDataSet(env);
        DataSet<Row> filtered = dataSet.filter(new FilterFunction<Row>() {
            @Override
            public boolean filter(Row value) throws Exception {
                return value.getField(5).equals("AL");
            }
        });

        DataSet<Row> sorted = filtered.sortPartition(new KeySelector<Row, String>() {
            @Override
            public String getKey(Row value) throws Exception {
                return (String)value.getField(0);
            }
        }, Order.DESCENDING).setParallelism(1);

        sorted.writeAsFormattedText("FlinkImpl/src/main/resources/BenchmarkRow.csv", OVERWRITE,
                new TextOutputFormat.TextFormatter<Row>() {
                    @Override
                    public String format(Row record) {
                        return record.getField(0) + "|"
                                + record.getField(1) + "|"
                                + record.getField(2) + "|"
                                + record.getField(3) + "|"
                                + record.getField(4) + "|"
                                + record.getField(5) + "|"
                                + record.getField(6) + "|"
                                + record.getField(7) + "|"
                                + record.getField(8) + "|"
                                + record.getField(9) + "|"
                                + record.getField(10) + "|"
                                + record.getField(11) + "|";
                    }
                }
        ).setParallelism(1);
    }

}
