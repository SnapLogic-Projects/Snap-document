import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.operators.Order;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.tuple.Tuple12;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static org.apache.flink.core.fs.FileSystem.WriteMode.OVERWRITE;

public class BenchmarkTuple {

    private static final Logger logger = LoggerFactory.getLogger(BenchmarkTuple.class.getName());

    public static void main(String[] args) throws IOException {
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

        long startTime1 = System.nanoTime();
        for (int i = 0; i < 50; i++) {

            process(env);
            try {
                env.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        long endTime1 = System.nanoTime();

        long duration1 = (endTime1 - startTime1);  //divide by 1000000 to get milliseconds.
//        System.out.println("It takes : " + duration1 / 1000000l + " milliseconds to finish.");
        logger.info("It takes : " + duration1 / 1000000L / 50L + " milliseconds to finish.");
    }

    static void process(ExecutionEnvironment env) throws IOException {
        DataSet<Tuple12<String, Integer, String, String, String, String, String, String, Integer, String, String, String>> csvInput
                = env.readCsvFile("test.csv")
                .ignoreFirstLine()
                .parseQuotedStrings('"')
                .types(String.class, Integer.class, String.class, String.class, String.class, String.class, String.class, String.class,
                        Integer.class, String.class, String.class, String.class);

        DataSet<Tuple12<String, Integer, String, String, String, String, String, String, Integer, String, String, String>> output0
                = csvInput.filter(new FilterFunction<Tuple12<String, Integer, String, String, String, String, String, String, Integer, String, String, String>>() {
            @Override
            public boolean filter(Tuple12<String, Integer, String, String, String, String, String, String, Integer, String, String, String> input) throws Exception {
                return input.f5.equals("AL");
            }
        }).sortPartition(4, Order.DESCENDING)
                .partitionByRange(0);

        output0.writeAsCsv("BenchmarkTuple.csv", "\n", "|", OVERWRITE).setParallelism(1);
    }

}
