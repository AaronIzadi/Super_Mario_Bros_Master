package SuperMario.repository;

import SuperMario.model.map.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static SuperMario.repository.SavePaths.*;

public class LoadConfigJsonFile {

    private JSONObject object;


    public LoadConfigJsonFile() throws IOException {
        String address = filePaths[3];

        Charset encoding = Charset.defaultCharset();
        List<String> lines = Files.readAllLines(Paths.get(address), encoding);

        JSONParser parser = new JSONParser();
        String s = String.join("\n", lines);

        try {
            object = (JSONObject) parser.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public Map createMap() {

        Map map = new Map();

        JSONArray array = (JSONArray) object.get("sections");


        System.out.println(array);


        return null;
    }
}
