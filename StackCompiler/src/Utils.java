import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Utils {
    public static String getSourceFromFile(String filePath) {
        if (filePath == null || filePath.isEmpty())
            throw new IllegalArgumentException("fileName must not be null or empty");
        File file = new File(filePath);
        if (!file.exists())
            throw new IllegalArgumentException("The source file does not exist, filePath = " + file.getAbsolutePath());
        BufferedReader reader = null;
        StringBuilder builder = new StringBuilder();
        int ch;
        try {
            reader = new BufferedReader(new FileReader(file));
            while ((ch = reader.read()) != -1) {
                builder.append((char) ch);
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null)
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        return builder.toString();
    }

    public static ArrayList<String> splitString(String origin) {
        if (origin == null || origin.isEmpty())
            throw new IllegalArgumentException("The origin mast not be null or empty");
        ArrayList<String> result = new ArrayList<>();
        String[] arr = origin.split("\\+");
        for (int i = 0; i < arr.length; i++) {
            result.add(arr[i].trim());
            if (i != arr.length - 1)
                result.add("+");
        }
        return result;
    }
}
