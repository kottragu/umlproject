package com.kottragu.umlproject.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "bought_ticket_info")
public class BoughtTicketInfo {
    @Id
    @GeneratedValue
    private Long id;
}
