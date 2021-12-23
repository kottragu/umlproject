package com.kottragu.umlproject.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Calendar;

@Data
@Entity
@Table(name = "timetable_ticket")
public class TimetableTicket {
    @Id
    @GeneratedValue
    private Long id;
    private Calendar startDate;
    private int frequency;
    private double price;
    private String directionFrom;
    private String directionTo;
}
