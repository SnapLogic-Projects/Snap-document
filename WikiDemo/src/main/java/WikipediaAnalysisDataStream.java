import com.snaplogic.DocumentImpl;
import org.apache.flink.api.common.functions.FoldFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.wikiedits.WikipediaEditEvent;
import org.apache.flink.streaming.connectors.wikiedits.WikipediaEditsSource;

import java.util.TreeMap;

public class WikipediaAnalysisDataStream {

    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment see = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStream<WikipediaEditEvent> edits = see.addSource(new WikipediaEditsSource());

        KeyedStream<WikipediaEditEvent, String> keyedEdits = edits
                .keyBy(new KeySelector<WikipediaEditEvent, String>() {
                    @Override
                    public String getKey(WikipediaEditEvent event) {
                        return event.getUser();
                    }
                });

        DataStream<DocumentImpl> result = keyedEdits
                .timeWindow(Time.seconds(5))
                .fold(new DocumentImpl(new TreeMap<String, Integer> ()), new FoldFunction<WikipediaEditEvent, DocumentImpl>() {
                    @Override
                    public DocumentImpl fold(DocumentImpl document, WikipediaEditEvent o) throws Exception {
                        TreeMap<String, Integer> tmp = new TreeMap<>();
                        tmp.put(o.getUser(), o.getByteDiff());
                        document.set(tmp);
                        return document;
                    }
                });

         result.print();

        see.execute();
    }
}
