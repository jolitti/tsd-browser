package lib;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.util.Arrays;

import java.util.Arrays;

public record Service(
        String tspId,
        int serviceId,
        String countryCode,
        String serviceName,
        String type,
        String currentStatus,
        String tob,
        String[] qServiceTypes
) {

    public Service {
        // Validation of the basic service parameters (for added robustness)
        if (tspId == null) throw new IllegalArgumentException("ProviderID (tspID) is null!");
        if (tspId.equals("")) throw new IllegalArgumentException("TspID is empty string!");
        if (countryCode == null) throw new IllegalArgumentException("CountryCode is null!");
        if (countryCode.equals("")) throw new IllegalArgumentException("CountryCode is empty string!");
        if (serviceName == null) throw new IllegalArgumentException("ServiceName is null!");
        if (type == null) throw new IllegalArgumentException("Type is null!");
        if (currentStatus == null) throw new IllegalArgumentException("CurrentStatus is null!");
        if (tob == null) throw new IllegalArgumentException("Tob is null!");
        if (qServiceTypes == null) throw new IllegalArgumentException("QServiceTypes is null!");
        //if (qServiceTypes.length <= 0) throw new IllegalArgumentException("QServiceTypes is empty!");

        // does the service provider id begin with the country code?
        //if (!tspId.startsWith(countryCode))
        //    throw new IllegalArgumentException("TspID doesn't start with country code "+countryCode);
    }

    public String toString() {
        return (
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
