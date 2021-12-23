package com.kottragu.umlproject.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Card {
    private String cardNumber;
    private String cardholderName;
    private int expirationMonth;
    private int expirationYear;
    private String csc;
}
