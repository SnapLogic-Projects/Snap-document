import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.operators.Order;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.io.TextOutputFormat;

import static org.apache.flink.core.fs.FileSystem.WriteMode.OVERWRITE;

public class POJOImpl {

    /**
     * This is the POJO (Plain Old Java Object) that is being used for all the operations.
     * As long as all fields are public or have a getter/setter, the system can handle them.
     */
    public static class Record {
        //fields: Name,Location,Extension,Email,Title,Department,Dept ID
        private String name;
        private String location;
        private int extension;
        private String email;
        private String title;
        private String department;
        private int deptID;

        public Record() {

        }

        public Record(String name, String location, int extension, String email, String title,
                      String department, int deptID) {
            this.name = name;
            this.location = location;
            this.extension = extension;
            this.email = email;
            this.title = title;
            this.department = department;
            this.deptID = deptID;
        }

        public String getName() {
            return name;
        }

        public String getLocation() {
            return location;
        }

        public int getExtension() {
            return extension;
        }

        public String getEmail() {
            return email;
        }

        public String getTitle() {
            return title;
        }

        public String getDepartment() {
            return department;
        }

        public int getDeptID() {
            return deptID;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public void setExtension(int extension) {
            this.extension = extension;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setDepartment(String department) {
            this.department = department;
        }

        public void setDeptID(int deptID) {
            this.deptID = deptID;
        }
    }

    public static void main(String[] args) {
        final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();



        DataSet<Record> csvInput = env.readCsvFile("/Users/Cheryl/git/SL_Flink/directory.csv")
                .ignoreFirstLine()
                .parseQuotedStrings('"')
                .pojoType(Record.class,
                        "name", "location", "extension", "email", "title", "department", "deptID");



        DataSet<Record> output0 = csvInput.filter(new FilterFunction<Record>() {
            @Override
            public boolean filter(Record record) throws Exception {
                return record.department.equals("Sales") && record.location.equals("Field");
            }
        })
                .sortPartition("name", Order.ASCENDING)
                .partitionByRange("name");



        //Write elements line-wise as Strings.
        // The Strings are obtained by calling a user-defined format() method for each element.
        output0.writeAsFormattedText("/Users/Cheryl/git/SL_Flink/target/pojo0.csv", OVERWRITE,
                new TextOutputFormat.TextFormatter<Record>() {
                    @Override
                    public String format(Record record) {
                        return record.name + "|"
                                + record.location + "|"
                                + record.extension + "|"
                                + record.email + "|"
                                + record.title + "|"
                                + record.department + "|"
                                + record.deptID + "|";
                    }
                }).setParallelism(1);


        try {
            env.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}