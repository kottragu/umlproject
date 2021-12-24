package com.kottragu.umlproject.service;

import com.kottragu.umlproject.model.Card;
import org.springframework.stereotype.Service;
import java.util.Random;

@Service
public class PayService {
    private Card card;

    public boolean pay(Card card, double totalCost) {
        this.card = card;
        Random random = new Random((long) (this.card.hashCode() + totalCost));
        return random.nextBoolean();
    }
}
