import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.snaplogic.Document;
import com.snaplogic.DocumentImpl;
import com.snaplogic.common.expressions.ScopeStack;
import com.snaplogic.expression.JaninoStringGeneratorVisitor;
import com.snaplogic.expression.SnapLogicExpression;
import com.snaplogic.snap.api.SnapDataException;
import com.snaplogic.util.DefaultValueHandler;
import org.antlr.v4.runtime.tree.ParseTree;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.operators.Order;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.io.TextOutputFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sl.EvaluatorUtils;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.fail;
import static org.apache.flink.core.fs.FileSystem.WriteMode.OVERWRITE;

public class BenchmarkSnapOld {

    private static final Logger logger = LoggerFactory.getLogger(BenchmarkSnap.class.getName());

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        // get flink environment.
        final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        final ScopeStack scopeStack = ExpressionEnv.InitializeEnvData(new HashMap<String, Object>());

        // warn up
        for (int i = 0; i < 1; i++) {

            process(env, scopeStack);
            try {
                env.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        long startTime = System.nanoTime();
        for (int i = 0; i < 1; i++) {

            process(env, scopeStack);
            try {
                env.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        long endTime = System.nanoTime();

        long duration = (endTime - startTime);  //divide by 1000000 to get milliseconds.
//        System.out.println("It takes : " + duration1 / 1000000l + " milliseconds to finish.");
        logger.info("It takes : " + duration / 1000000L + " milliseconds to finish.(Old)");
    }

    static void process(ExecutionEnvironment env, final ScopeStack scopes) throws IOException {
        // csv Reader Snap
        CsvMapper mapper = new CsvMapper();
        CsvSchema schema = CsvSchema.emptySchema().withHeader();
        File csvFile = new File("test80000.csv");

        MappingIterator<Map<String, Object>> iterator = mapper.reader(Map.class)
                .with(schema)
                .readValues(csvFile);

        ArrayList<Document> list = new ArrayList<Document>();

        while (iterator.hasNext()) {
            Map<String, Object> map = iterator.next();
            Document cur = new DocumentImpl(map);
            list.add(cur);
        }

        // Filter Snap
        final DataSet<Document> csvInput = env.fromCollection(list);

        DataSet<Document> filterOut = csvInput.filter(new FilterFunction<Document>() {
            @Override
            public boolean filter(Document document) throws Exception {
                ExpressionEnv env = new ExpressionEnv((Map<String, Object>) document.get());
                return env.eval("$ProviderState == 'AL'", document.get());
            }
        });

        // Sort Snap
        filterOut.sortPartition(new KeySelector<Document, String>() {
            @Override
            public String getKey(Document document) throws Exception {
                return (String) ((Map<String, Object>) document.get()).get("ProviderCity");
            }
        }, Order.DESCENDING);

        // Writer Snap

        filterOut.writeAsFormattedText("BenchmarkSnap.csv", OVERWRITE,
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
    }
}
