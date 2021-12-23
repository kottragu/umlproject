package com.kottragu.umlproject.service;

import com.kottragu.umlproject.model.Card;
import org.springframework.stereotype.Service;
import java.util.Random;

@Service
public class PayService {

    public boolean pay(Card card, double totalCost) {
        Random random = new Random((long) (card.hashCode() + totalCost));
        return random.nextBoolean();
    }
}
