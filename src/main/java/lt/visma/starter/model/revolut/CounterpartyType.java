package lt.visma.starter.model.revolut;

import com.fasterxml.jackson.annotation.JsonValue;

public enum CounterpartyType {
    SELF("self"),
    REVOLUT("revolut"),
    EXTERNAL("external");

    private String text;

    CounterpartyType(String text) {
        this.text = text;
    }

    @JsonValue
    public String getText() {
        return text;
    }
}
