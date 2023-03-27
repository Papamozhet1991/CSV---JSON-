import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";
        List<Employee> list = parseCSV(columnMapping, fileName);
        listToJson("data.json", list);
    }

    public static void listToJson(String fileNameJSON, List<Employee> listEmployee) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Type listType = new TypeToken<List<Employee>>() {}.getType();
        System.out.println(gson.toJson(listEmployee, listType));
        try (FileWriter file = new FileWriter(fileNameJSON)) {
            file.write(gson.toJson(listEmployee, listType));
            file.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    public static List<Employee> parseCSV(String[] columnMapping, String fileName) {
        List<Employee> employee = null;
        try (CSVReader csvReader = new CSVReader(new FileReader(fileName))) {
            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMapping);
            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(csvReader)
                    .withMappingStrategy(strategy)
                    .build();
            employee = csv.parse();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return employee;
    }

}
