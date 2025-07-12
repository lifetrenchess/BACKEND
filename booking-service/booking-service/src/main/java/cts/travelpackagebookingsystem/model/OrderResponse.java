package cts.travelpackagebookingsystem.model;

public class OrderResponse {
	private String orderId;
    private String currency;
    private double amount; // Amount in INR, not paisa here. Razorpay will use paisa.
    private String keyId; // Razorpay Key ID to be sent to frontend

    public OrderResponse(String orderId, String currency, double amount, String keyId) {
        this.orderId = orderId;
        this.currency = currency;
        this.amount = amount;
        this.keyId = keyId;
    }

    // Getters (no setters needed if constructed this way)
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public String getKeyId() { return keyId; } // <--- Make sure this getter exists
    public void setKeyId(String keyId) { this.keyId = keyId; } // <--- Make sure this setter exists

}
