import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.operators.Order;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.io.TextOutputFormat;

import java.io.*;
import java.util.*;

import com.snaplogic.Document;
import com.snaplogic.DocumentImpl;

import static org.apache.flink.core.fs.FileSystem.WriteMode.OVERWRITE;

public class POJOImpl {

    /**
     * This is the POJO (Plain Old Java Object) that is being used for all the operations.
     * As long as all fields are public or have a getter/setter, the system can handle them.
     */

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        // csv reader
        CsvMapper mapper = new CsvMapper();
        CsvSchema schema = CsvSchema.emptySchema().withHeader();
        File csvFile = new File("directory.csv");

        MappingIterator<Map<String, Object>> iterator = mapper.reader(Map.class)
                .with(schema)
                .readValues(csvFile);

        ArrayList<Document> list = new ArrayList<Document>();

        while (iterator.hasNext()) {
            Map<String, Object> map = iterator.next();
            System.out.println(map.get("deptID"));
            Document cur = new DocumentImpl(map);
            list.add(cur);
        }

        // flink do his job.
        DataSet<Document> csvInput = env.fromCollection(list);

        DataSet<Document> output0 = csvInput.filter(new FilterFunction<Document>() {
            @Override
            public boolean filter(Document document) throws Exception {
                return ((Map<String, Object>)document.get()).get("department").equals("Sales") && ((Map<String, Object>)document.get()).get("location").equals("Field");
            }
        });

        output0.sortPartition(new KeySelector<Document, String>() {
            @Override
            public String getKey(Document document) throws Exception {
                return (String) ((Map<String, Object>)document.get()).get("name");
            }
        }, Order.DESCENDING);


        output0.writeAsFormattedText("pojomap0.csv", OVERWRITE,
                new TextOutputFormat.TextFormatter<Document>() {
                    @Override
                    public String format(Document document) {
                        Map<String, Object> record = (Map<String, Object>)document.get();
                        return record.get("name") + "|"
                                + record.get("location") + "|"
                                + record.get("extension") + "|"
                                + record.get("email") + "|"
                                + record.get("title") + "|"
                                + record.get("department") + "|"
                                + record.get("deptID") + "|";
                    }
                }
        ).setParallelism(1);

        try {
            env.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}