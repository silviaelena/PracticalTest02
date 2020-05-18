package ro.pub.cs.systems.eim.practicaltest02.model;

public class CurrencyInformation {
    private String rate;
    private String updated;

    public CurrencyInformation(String rate, String updated) {
        this.rate = rate;
        this.updated = updated;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }


    @Override
    public String toString() {
        return "Currency Information{" +
                "Rate='" + rate + '\'' +
                ", updated='" + updated + '\'' +
                '}';
    }
}
