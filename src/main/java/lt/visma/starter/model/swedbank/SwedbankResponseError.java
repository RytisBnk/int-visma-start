package lt.visma.starter.model.swedbank;

import lt.visma.starter.model.ResponseError;

import java.util.List;

public class SwedbankResponseError implements ResponseError {
    private List<TppMessage> tppMessages;

    public List<TppMessage> getTppMessages() {
        return tppMessages;
    }

    public void setTppMessages(List<TppMessage> tppMessages) {
        this.tppMessages = tppMessages;
    }
}
