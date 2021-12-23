package com.kottragu.umlproject.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

@Data
@Entity
@Table(name = "tickets")
@NoArgsConstructor
public class Ticket {
    @Id
    @GeneratedValue
    private Long id;
    private Calendar date;
    private double price;
    private String directionFrom;
    private String directionTo;
    private Status status;
    private Long ownerId;

    public String getDateFrontend() {
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        return format.format(date.getTime());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        return id.equals(ticket.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
