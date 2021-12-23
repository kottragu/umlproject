package com.kottragu.umlproject.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "purchase")
@NoArgsConstructor
public class Purchase {
    @Id
    @GeneratedValue
    private Long id;
    private Long ownerId;
    private Calendar date;
    private double totalCost;
    @OneToMany
    private List<Ticket> tickets;

    public String getDateFrontend() {
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        return format.format(date.getTime());
    }
    public Integer getTicketsCount() {
        return tickets.size();
    }
}
