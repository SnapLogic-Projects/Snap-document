//import com.fasterxml.jackson.databind.MappingIterator;
//import com.fasterxml.jackson.dataformat.csv.CsvMapper;
//import com.fasterxml.jackson.dataformat.csv.CsvSchema;
//import com.snaplogic.Document;
//import com.snaplogic.DocumentImpl;
//import org.apache.flink.api.common.functions.FilterFunction;
//import org.apache.flink.api.common.operators.Order;
//import org.apache.flink.api.java.DataSet;
//import org.apache.flink.api.java.ExecutionEnvironment;
//import org.apache.flink.api.java.functions.KeySelector;
//import org.apache.flink.api.java.io.TextOutputFormat;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Map;
//
//import static org.apache.flink.core.fs.FileSystem.WriteMode.OVERWRITE;
//
//public class PipelineDemo {
//
//    public static void main(String[] args) throws IOException, ClassNotFoundException {
//
//        // get flink environment.
//        final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
//
//        // csv Reader Snap
//        CsvMapper mapper = new CsvMapper();
//        CsvSchema schema = CsvSchema.emptySchema().withHeader();
//        File csvFile = new File("directory.csv");
//
//        MappingIterator<Map<String, Object>> iterator = mapper.reader(Map.class)
//                .with(schema)
//                .readValues(csvFile);
//
//        ArrayList<Document> list = new ArrayList<Document>();
//
//        while (iterator.hasNext()) {
//            Map<String, Object> map = iterator.next();
//            Document cur = new DocumentImpl(map);
//            list.add(cur);
//        }
//
//        // Filter Snap
//        final DataSet<Document> csvInput = env.fromCollection(list);
//
//        DataSet<Document> filterOut = csvInput.filter(new FilterFunction<Document>() {
//            @Override
//            public boolean filter(Document document) throws Exception {
//                ExpressionEnv filterEnv = new ExpressionEnv((Map<String, Object>)document.get());
//                return filterEnv.eval("$location == 'Field'", document.get());
//            }
//        });
//
//        // Sort Snap
//        filterOut.sortPartition(new KeySelector<Document, String>() {
//            @Override
//            public String getKey(Document document) throws Exception {
//                return (String) ((Map<String, Object>) document.get()).get("name");
//            }
//        }, Order.DESCENDING);
//
//
//        // Writer Snap
//
//        filterOut.writeAsFormattedText("pipeLineDemo.csv", OVERWRITE,
//                new TextOutputFormat.TextFormatter<Document>() {
//                    @Override
//                    public String format(Document document) {
//                        Map<String, Object> record = (Map<String, Object>)document.get();
//                        return record.get("name") + "|"
//                                + record.get("location") + "|"
//                                + record.get("extension") + "|"
//                                + record.get("email") + "|"
//                                + record.get("title") + "|"
//                                + record.get("department") + "|"
//                                + record.get("deptID") + "|";
//                    }
//                }
//        ).setParallelism(1);
//
//        try {
//            env.execute();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
//}
