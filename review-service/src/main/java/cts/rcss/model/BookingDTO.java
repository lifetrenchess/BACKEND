package cts.rcss.model;

import java.time.LocalDate;

public class BookingDTO {
    private Long bookingId;
    private Long userId;
    private Long packageId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private Integer adults;
    private Integer children;
    private Integer infants;
    private String contactFullName;
    private String contactEmail;
    private String contactPhone;
    private String travelerNames;
    private Boolean hasInsurance;
    private String insurancePlan;

    // Default constructor
    public BookingDTO() {}

    // Getters and setters
    public Long getBookingId() { return bookingId; }
    public void setBookingId(Long bookingId) { this.bookingId = bookingId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getPackageId() { return packageId; }
    public void setPackageId(Long packageId) { this.packageId = packageId; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Integer getAdults() { return adults; }
    public void setAdults(Integer adults) { this.adults = adults; }

    public Integer getChildren() { return children; }
    public void setChildren(Integer children) { this.children = children; }

    public Integer getInfants() { return infants; }
    public void setInfants(Integer infants) { this.infants = infants; }

    public String getContactFullName() { return contactFullName; }
    public void setContactFullName(String contactFullName) { this.contactFullName = contactFullName; }

    public String getContactEmail() { return contactEmail; }
    public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }

    public String getContactPhone() { return contactPhone; }
    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }

    public String getTravelerNames() { return travelerNames; }
    public void setTravelerNames(String travelerNames) { this.travelerNames = travelerNames; }

    public Boolean getHasInsurance() { return hasInsurance; }
    public void setHasInsurance(Boolean hasInsurance) { this.hasInsurance = hasInsurance; }

    public String getInsurancePlan() { return insurancePlan; }
    public void setInsurancePlan(String insurancePlan) { this.insurancePlan = insurancePlan; }
} 