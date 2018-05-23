package uet.k59t;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Long laptop on 5/23/2018.
 */
public class TestGson {
    public static void main(String[] args) throws IOException {
        String sURL = "http://192.168.1.101/school1/index.php?admin/listAllParent"; //just a string

        // Connect to the URL using java's native library
        URL url = new URL(sURL);
        URLConnection request = url.openConnection();
        request.connect();

        // Convert to a JSON object to print data
        JsonParser jp = new JsonParser(); //from gson
        JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent())); //Convert the input stream to a json element
        JsonArray rootobj = root.getAsJsonArray(); //May be an array, may be an object.
        JsonObject teacherGson = rootobj.get(0).getAsJsonObject(); //just grab the zipcode
        System.out.println(teacherGson.get("name"));
    }
}
