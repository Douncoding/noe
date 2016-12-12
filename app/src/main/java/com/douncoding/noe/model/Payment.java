package com.douncoding.noe.model;

/**
 * Created by douncoding on 2016. 12. 12..
 */

public class Payment {
    private String password; // 결제 비밀번호
    private String payId; // 선택한 결제 수단

    private String product;
    private String price;
    private String status;
    private String consumerId;
    private String providerId;

    public Payment() {
    }

    public Payment(String password, String payId, String product, String price, String status, String consumerId, String providerId) {
        this.password = password;
        this.payId = payId;
        this.product = product;
        this.price = price;
        this.status = status;
        this.consumerId = consumerId;
        this.providerId = providerId;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(String consumerId) {
        this.consumerId = consumerId;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPayId() {
        return payId;
    }

    public void setPayId(String payId) {
        this.payId = payId;
    }

    public enum  Status {WAIT, ING, COMPLETE}
}
