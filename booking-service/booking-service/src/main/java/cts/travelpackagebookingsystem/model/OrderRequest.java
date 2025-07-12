package cts.travelpackagebookingsystem.model;

public class OrderRequest {
	private double amount; // Use double or BigDecimal
    private String currency; // e.g., "INR"
    private Long bookingId; // To link the payment to a specific booking
    private Long userId; // To link the payment to a specific user

    // Getters and Setters
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public Long getBookingId() { return bookingId; }
    public void setBookingId(Long bookingId) { this.bookingId = bookingId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

}
