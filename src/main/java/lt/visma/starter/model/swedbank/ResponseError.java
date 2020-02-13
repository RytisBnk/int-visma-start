package lt.visma.starter.model.swedbank;

import java.util.List;

public class ResponseError {
    private List<TppMessage> tppMessages;

    public List<TppMessage> getTppMessages() {
        return tppMessages;
    }

    public void setTppMessages(List<TppMessage> tppMessages) {
        this.tppMessages = tppMessages;
    }
}
