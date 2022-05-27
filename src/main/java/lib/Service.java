package lib;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public record Service(
        int tspId,
        int serviceId,
        String countryCode,
        String serviceName,
        String type,
        String currentStatus,
        String tob,
        String[] qServiceTypes
) {

    public String toString() {
        return new String(
                this.tspId +
                        "\n" +
                        this.serviceId +
                        "\n" +
                        this.countryCode +
                        "\n" +
                        this.serviceName +
                        "\n" +
                        this.type +
                        "\n" +
                        this.currentStatus +
                        "\n" +
                        this.tob +
                        "\n" +
                        Arrays.toString(this.qServiceTypes)
        );
    }
}
