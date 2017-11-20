package row;

import org.apache.flink.types.Row;

import java.util.HashMap;
import java.util.Map;

public class SnapRow extends Row{
    private Map<String,Integer> fieldName;

    public SnapRow(int arity, Map<String,Integer> fieldName){
        super(arity);
        this.fieldName = new HashMap<>(fieldName);
    }

    public SnapRow(int arity) {
        super(arity);
        this.fieldName = new HashMap<>();
    }

    public Object getFeild(String fieldName){
        return getField(this.fieldName.get(fieldName));
    }

    public void setField(String name,int pos,Object value){
        super.setField(pos,value);
        this.fieldName.put(name,pos);
    }
}
