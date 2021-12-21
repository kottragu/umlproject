package com.kottragu.umlproject.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "booked_ticket_info")
public class BookedTicketInfo {
    @Id
    @GeneratedValue
    private Long id;
}
