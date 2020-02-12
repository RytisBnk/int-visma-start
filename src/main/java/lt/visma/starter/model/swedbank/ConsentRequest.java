package lt.visma.starter.model.swedbank;

import java.util.List;

public class ConsentRequest {
    private Access access;
    private boolean combinedServiceIndicator;
    private int frequencyPerDay;
    private boolean recurringIndicator;
    private String validUntil;

    public ConsentRequest(Access access, boolean combinedServiceIndicator, int frequencyPerDay, boolean recurringIndicator, String validUntil) {
        this.access = access;
        this.combinedServiceIndicator = combinedServiceIndicator;
        this.frequencyPerDay = frequencyPerDay;
        this.recurringIndicator = recurringIndicator;
        this.validUntil = validUntil;
    }

    public ConsentRequest() {
    }

    public Access getAccess() {
        return access;
    }

    public void setAccess(Access access) {
        this.access = access;
    }

    public boolean isCombinedServiceIndicator() {
        return combinedServiceIndicator;
    }

    public void setCombinedServiceIndicator(boolean combinedServiceIndicator) {
        this.combinedServiceIndicator = combinedServiceIndicator;
    }

    public int getFrequencyPerDay() {
        return frequencyPerDay;
    }

    public void setFrequencyPerDay(int frequencyPerDay) {
        this.frequencyPerDay = frequencyPerDay;
    }

    public boolean isRecurringIndicator() {
        return recurringIndicator;
    }

    public void setRecurringIndicator(boolean recurringIndicator) {
        this.recurringIndicator = recurringIndicator;
    }

    public String getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(String validUntil) {
        this.validUntil = validUntil;
    }
}
