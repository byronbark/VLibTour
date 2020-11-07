package vlibtour.vlibtour_bikestation.client.generated_from_json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "lat",
    "lng"
})
public class Position {

    @JsonProperty("lat")
    public double lat;
    @JsonProperty("lng")
    public double lng;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Position() {
    }

    /**
     * 
     * @param lng
     * @param lat
     */
    public Position(double lat, double lng) {
        super();
        this.lat = lat;
        this.lng = lng;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("lat", lat).append("lng", lng).toString();
    }

}
