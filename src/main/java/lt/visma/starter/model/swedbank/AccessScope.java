package lt.visma.starter.model.swedbank;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum AccessScope {
    PSD2,
    PSD2sandbox
}
