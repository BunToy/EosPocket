package app.eospocket.android.eos.model.action;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Data {

    public String payer;

    public String quant;

    public String receiver;

    public String from;

    public String to;

    public String memo;

    public String quantity;
}
