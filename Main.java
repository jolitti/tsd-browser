import org.json.*; // Imported from Maven, downloaded into lib/ext/

public class Main {
    public static void main (String[] args) {
        System.out.println("Hello world!");

        JSONArray a = new JSONArray("[1,2]");
        System.out.println(a);
    }
}
