package logic;

import com.google.gson.Gson;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ShipReader {
    public static Ship ReadShipFromFile(String filename) throws IOException {
        byte[] fileContents = Files.readAllBytes(Paths.get(filename));
        Gson gson = new Gson();
        return gson.fromJson(new String(fileContents), Ship.class);
    }
}
