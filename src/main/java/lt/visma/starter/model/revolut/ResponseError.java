package lt.visma.starter.model.revolut;

import com.fasterxml.jackson.annotation.JsonAlias;

public class ResponseError {
    private String error;
    @JsonAlias({"error_description"})
    private String errorDescription;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }
}
