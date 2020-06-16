package jp.co.netprotections.pokerapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PokerRequest {
    @SerializedName("cards")
    private List<String> cards;

    public PokerRequest(List<String> cards) {
        this.cards = cards;
    }

    public void addCard(String card) {
        this.cards.add(card);
    }

    public void changeCard(int i, String card) {
        this.cards.set(i, card);
    }

    public List<String> getCards() {
        return cards;
    }
}
