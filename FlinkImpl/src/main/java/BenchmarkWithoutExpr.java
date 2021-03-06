import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.snaplogic.Document;
import com.snaplogic.DocumentImpl;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.operators.Order;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.io.TextOutputFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.print.Doc;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import static org.apache.flink.core.fs.FileSystem.WriteMode.OVERWRITE;

public class BenchmarkWithoutExpr {

    private static final Logger logger = LoggerFactory.getLogger(BenchmarkWithoutExpr.class.getName());

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        // get flink environment.
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
        long startTime = System.nanoTime();
        // csv Reader Snap
        CsvMapper mapper = new CsvMapper();
        CsvSchema schema = CsvSchema.emptySchema().withHeader();
        File csvFile = new File("test.csv");

        MappingIterator<Map<String, Object>> iterator = mapper.reader(Map.class)
                .with(schema)
                .readValues(csvFile);

        ArrayList<Document> list = new ArrayList<Document>();

        while (iterator.hasNext()) {
            Map<String, Object> map = iterator.next();
            Document cur = new DocumentImpl(map);
            list.add(cur);
        }
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);  //divide by 1000000 to get milliseconds.
        logger.info("data transfer takes : " + duration / 1000000L + " milliseconds to finish.");

        startTime = System.nanoTime();
        // Filter Snap
        final DataSet<Document> csvInput = env.fromCollection(list);

        DataSet<Document> filterOut = csvInput.filter(new FilterFunction<Document>() {
            @Override
            public boolean filter(Document document) throws Exception {
                return ((Map<String, Object>) document.get()).get("ProviderState").equals("AL");
            }
        });
        DataSet<Document> filterOut1 = csvInput.filter(new FilterFunction<Document>() {
            @Override
            public boolean filter(Document document) throws Exception {
                return ((Map<String, Object>) document.get()).get("ProviderState").equals("AL");
            }
        });

        // Sort Snap
        filterOut.sortPartition(new KeySelector<Document, String>() {
            @Override
            public String getKey(Document document) throws Exception {
                return (String) ((Map<String, Object>) document.get()).get("ProviderCity");
            }
        }, Order.DESCENDING);
        endTime = System.nanoTime();
        duration = (endTime - startTime);  //divide by 1000000 to get milliseconds.
        logger.info("execute takes : " + duration / 1000000L + " milliseconds to finish.");

        startTime = System.nanoTime();
        // Writer Snap

        filterOut.writeAsFormattedText("BenchmarkWithoutExpr.csv", OVERWRITE,
                new TextOutputFormat.TextFormatter<Document>() {
                    @Override
                    public String format(Document document) {
                        Map<String, Object> record = (Map<String, Object>)document.get();
                        return record.get("ProviderId") + "|"
                                + record.get("ProviderName") + "|"
                                + record.get("ProviderStreetAddress") + "|"
                                + record.get("ProviderCity") + "|"
                                + record.get("ProviderState") + "|"
                                + record.get("ProviderZipCode") + "|"
                                + record.get("HospitalReferralRegionDescription") + "|";
                    }
                }
        ).setParallelism(1);
        endTime = System.nanoTime();
        duration = (endTime - startTime);  //divide by 1000000 to get milliseconds.
        logger.info("write takes : " + duration / 1000000L + " milliseconds to finish.");
    }
}
