package com.example.apiconnector.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class VehicleDTO {

    @NotNull
    private Long id;

    @NotNull
    private String model;

    @Positive(message = "Price per day must be positive")
    private double pricePerDay;

    public VehicleDTO() {}

    public VehicleDTO(Long id, String model, double pricePerDay) {
        this.id = id;
        this.model = model;
        this.pricePerDay = pricePerDay;
    }


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public double getPricePerDay() { return pricePerDay; }
    public void setPricePerDay(double pricePerDay) { this.pricePerDay = pricePerDay; }
}
