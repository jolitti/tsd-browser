package lib;

import java.util.Arrays;

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
