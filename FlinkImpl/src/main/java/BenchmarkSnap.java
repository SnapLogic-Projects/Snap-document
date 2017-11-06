import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.google.common.base.Throwables;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.UncheckedExecutionException;
import com.snaplogic.Document;
import com.snaplogic.DocumentImpl;
import com.snaplogic.api.ExecutionException;
import com.snaplogic.common.expressions.ScopeStack;
import com.snaplogic.expression.ExpressionUtil;
import com.snaplogic.expression.GlobalScope;
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

public class BenchmarkSnap {

    private static final Logger logger = LoggerFactory.getLogger(BenchmarkSnap.class.getName());
    private static final LoadingCache<String, SnapLogicExpression> PARSE_TREE_CACHE =
            CacheBuilder.newBuilder()
                    .softValues()
                    .build(new CacheLoader<String, SnapLogicExpression>() {
                        @Override
                        public SnapLogicExpression load(final String key) throws Exception {
                            return ExpressionUtil.compile(key);
                        }
                    });
    private static final GlobalScope GLOBAL_SCOPE = new GlobalScope();
    private static final DefaultValueHandler DEFAULT_VALUE_HANDLER = new DefaultValueHandler();
    private static final String expression = "$ProviderState == 'AL'";

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        // get flink environment.
        final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        final ScopeStack scopeStack = ExpressionEnv.InitializeEnvData(new HashMap<String, Object>());

        // warn up
        for (int i = 0; i < 5; i++) {

            process(env, scopeStack);
            try {
                env.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        long startTime = System.nanoTime();
        for (int i = 0; i < 10; i++) {

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
        logger.info("It takes : " + duration / 1000000L/10 + " milliseconds to finish.");
    }

    static void process(ExecutionEnvironment env, final ScopeStack scopes) throws IOException {
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

        // Filter Snap
        final DataSet<Document> csvInput = env.fromCollection(list);

        DataSet<Document> filterOut = csvInput.filter(new FilterFunction<Document>() {
            @Override
            public boolean filter(Document document) throws Exception {
                SnapLogicExpression snapLogicExpression = PARSE_TREE_CACHE.get(expression);

                ScopeStack scopeStack;
                if (scopes != null && scopes.getClass() == ScopeStack.class) {
                    scopeStack = (ScopeStack) scopes;
                } else {
                    scopeStack = new ScopeStack();
                    if (scopes != null) {
                        scopeStack.pushAllScopes(scopes);
                    } else {
                        scopeStack.push(GLOBAL_SCOPE);
                    }
                }
                try {
                    return (Boolean) snapLogicExpression.evaluate(document.get(), scopeStack, DEFAULT_VALUE_HANDLER);
                } catch (SnapDataException|ExecutionException e) {
                    throw e;
                } catch (Throwable th) {
                    throw new SnapDataException(th, "Unexpected error occurred while " +
                            "evaluating expression: %s")
                            .formatWith(expression)
                            .withResolution("Please check your expression");
                }
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
