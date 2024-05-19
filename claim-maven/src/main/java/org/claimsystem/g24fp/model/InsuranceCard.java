package org.claimsystem.g24fp.model;




public class InsuranceCard {
    private String cardID;
    private String card_holder;
    private String expiry_date;

    public InsuranceCard() {
        this.cardID = "empty";
        this.card_holder = "empty";
        this.expiry_date = "empty";
    }

    public InsuranceCard(String cardID, String card_holder, String expiry_date) {
        this.cardID = cardID;
        this.card_holder = card_holder;
        this.expiry_date = expiry_date;
    }

    public String getCardID() {
        return cardID;
    }

    public void setCardID(String cardID) {
        this.cardID = cardID;
    }

    public String getCardHolder() {
        return card_holder;
    }

    public void setCardHolder(String card_holder) {
        this.card_holder = card_holder;
    }

    public void setExpireDate(String expiry_date) {
        this.expiry_date = expiry_date;
    }

    public String getExpireDate() {
        return expiry_date;
    }
}
