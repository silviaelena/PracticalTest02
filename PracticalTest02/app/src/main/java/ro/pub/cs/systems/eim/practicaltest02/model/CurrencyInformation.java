package ro.pub.cs.systems.eim.practicaltest02.model;

public class CurrencyInformation {
    private String usd;
    private String eur;
    private String rate;
    private String updated;

    public CurrencyInformation() {
        this.usd = null;
        this.rate = null;
        this.updated = null;
    }


    public CurrencyInformation(String rateUSD, String rateEUR, String updated) {
        this.usd = rateUSD;
        this.eur = rateEUR;
        this.updated = updated;
    }

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

    public String getRateUSD() {
        return usd;
    }

    public void setRateUSD(String usd) {
        this.usd = usd;
    }

    public String getRateEUR() {
        return eur;
    }

    public void setRateEUR(String eur) {
        this.eur = eur;
    }


    @Override
    public String toString() {
        return "Currency Information{" +
                "Rate='" + rate + '\'' +
                ", updated='" + updated + '\'' +
                '}';
    }
}
