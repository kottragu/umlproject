package com.kottragu.umlproject.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Entity
@Table(name = "tickets")
public class Ticket {
    @Id
    @GeneratedValue
    private Long id;
    private Date date;
    private double price;
    private boolean isAvailable;
    private String directionFrom;
    private String directionTo;
}
