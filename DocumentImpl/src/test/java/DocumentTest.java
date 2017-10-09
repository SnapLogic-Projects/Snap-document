import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * DocumentTest
 *
 * @author mklumpp
 */
public class DocumentTest {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final String SAME_HASHCODE_DIFFERENT_MD5 =
            "[{\"Checksum\":\"CONWAY-CWY01-01-031\"," +
                    "\"Floor_ID\":\"01\",\"Building_ID\":\"CWY01\",\"Business_Site_ID\":\"CONWAY\"," +
                    "\"Office_ID\":\"031\"},\n" +
                    "{\"Checksum\":\"CONWAY-CWY01-01-032\",\"Floor_ID\":\"01\",\"Building_ID\":\"CWY01\"," +
                    "\"Business_Site_ID\":\"CONWAY\",\"Office_ID\":\"032\"},\n" +
                    "{\"Checksum\":\"CONWAY-CWY01-01-035\",\"Floor_ID\":\"01\",\"Building_ID\":\"CWY01\"," +
                    "\"Business_Site_ID\":\"CONWAY\",\"Office_ID\":\"035\"},\n" +
                    "{\"Checksum\":\"CONWAY-CWY01-01-036\",\"Floor_ID\":\"01\",\"Building_ID\":\"CWY01\"," +
                    "\"Business_Site_ID\":\"CONWAY\",\"Office_ID\":\"036\"},\n" +
                    "{\"Checksum\":\"CONWAY-CWY01-01-039\",\"Floor_ID\":\"01\",\"Building_ID\":\"CWY01\"," +
                    "\"Business_Site_ID\":\"CONWAY\",\"Office_ID\":\"039\"}]";

    private static final String SAME__MD5 =
            "[{\"Checksum\":\"CONWAY-CWY01-01-031\"," +
                    "\"Floor_ID\":\"01\",\"Building_ID\":\"CWY01\"," +
                    "\"Business_Site_ID\":\"CONWAY\"," +
                    "\"Office_ID\":\"031\", \"child\": {\"c1\":1, \"c2\":2}},\n" +
                    "{\"Checksum\":\"CONWAY-CWY01-01-031\",\"Floor_ID\":\"01\"," +
                    "\"Business_Site_ID\":\"CONWAY\",\"Office_ID\":\"031\", " +
                    "\"Building_ID\":\"CWY01\", \"child\": {\"c2\":2, \"c1\":1}}]";
    @Test
    public void testMd5() throws Exception {
        List<Map<String, Object>> entries = OBJECT_MAPPER.readValue(SAME_HASHCODE_DIFFERENT_MD5,
                List.class);
        Iterator<Map<String, Object>> it = entries.iterator();
        Map<String, Object> prev = it.next();
        while (it.hasNext()) {
            Map<String, Object> cur = it.next();
            Document curDoc = new DocumentImpl(cur);
            Document prevDoc = new DocumentImpl(prev);
            BigInteger curMd5 = new BigInteger(curDoc.getMd5());
            BigInteger prevMd5 = new BigInteger(prevDoc.getMd5());
            Assert.assertNotEquals(curMd5, prevMd5);
            prev = cur;
        }
    }


    @Test
    public void testMd5Same() throws Exception {
        List<Map<String, Object>> entries = OBJECT_MAPPER.readValue(SAME__MD5, List.class);
        Iterator<Map<String, Object>> it = entries.iterator();
        Map<String, Object> prev = it.next();
        while (it.hasNext()) {
            Map<String, Object> cur = it.next();
            Document curDoc = new DocumentImpl(cur);
            Document prevDoc = new DocumentImpl(prev);
            BigInteger curMd5 = new BigInteger(curDoc.getMd5());
            BigInteger prevMd5 = new BigInteger(prevDoc.getMd5());
            Assert.assertEquals(curMd5, prevMd5);
            prev = cur;
        }
    }
}