package com.rmt.railway_management_system.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "seattype")
public class SeatType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "type_id", nullable = false)
    private Integer typeId;

    @Column(name = "type_name", length = 50, nullable = false, unique = true)
    private String typeName;

    @Column(name = "price", precision = 10, scale = 2, nullable = false)
    private BigDecimal price;

    // Constructors
    public SeatType() {
    }

    public SeatType(String typeName, BigDecimal price) {
        this.typeName = typeName;
        this.price = price;
    }

    // Getters and Setters
    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "SeatType{" +
                "typeId=" + typeId +
                ", typeName='" + typeName + '\'' +
                ", price=" + price +
                '}';
    }
}