import com.snaplogic.DocumentImpl;
import org.apache.flink.api.common.functions.FoldFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.wikiedits.WikipediaEditEvent;
import org.apache.flink.streaming.connectors.wikiedits.WikipediaEditsSource;

import java.util.TreeMap;

public class WikipediaAnalysisDataSet {

    public static void main(String[] args) {

        StreamExecutionEnvironment see = StreamExecutionEnvironment.getExecutionEnvironment();
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        DataStream<WikipediaEditEvent> edits = see.addSource(new WikipediaEditsSource());

        KeyedStream<WikipediaEditEvent, String> keyedEdits = edits
                .keyBy(new KeySelector<WikipediaEditEvent, String>() {
                    @Override
                    public String getKey(WikipediaEditEvent event) {
                        return event.getUser();
                    }
                });
    }
}
