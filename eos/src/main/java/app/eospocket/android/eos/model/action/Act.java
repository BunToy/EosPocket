package app.eospocket.android.eos.model.action;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Act {

    public String account;

    public List<Authorization> authorization;

    public Data data;

    @JsonProperty("hex_data")
    @SerializedName("hex_data")
    public String hexData;

    public String name;
}
