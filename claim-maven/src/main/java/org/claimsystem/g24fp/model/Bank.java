package org.claimsystem.g24fp.model;

public class Bank {
    private String receiverName;
    private String bankAccountNumber;
    private String bankName;
    public Bank() {
        this.receiverName = "default";
        this.bankAccountNumber = "default";
        this.bankName = "default";
    }


    //Constructor
    public Bank(String bankAccountNumber, String receiverName, String bankName) {
        this.bankAccountNumber = bankAccountNumber;
        this.bankName = bankName;
        this.receiverName = receiverName;
    }
    //Getter
    public String getBankName() {
        return this.bankName;
    }

    public String getReceiverName() {
        return this.receiverName;
    }

    public String getBankAccountNumber() {
        return this.bankAccountNumber;
    }

}
