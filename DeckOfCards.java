import java.util.ArrayList;
import java.util.Collections;

public class DeckOfCards {
    private ArrayList<String> cards;

    private static final String[] RANKS = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13"};
    private static final String[] SUITS = {"1", "2", "3", "4"};

    // Constructor
    public DeckOfCards() {
        cards = new ArrayList<>();
        for (String suit : SUITS) {
            for (String rank : RANKS) {
                String card = rank + '_' + suit;
                cards.add(card);
            }
        }
    }

    // Shuffle the deck
    public void shuffle() {
        Collections.shuffle(cards);
    }

    // Draw a card from the deck
    public String drawCard() {
        if (cards.isEmpty()) {
            throw new IllegalStateException("The deck is empty.");
        }
        return cards.remove(0);
    }

    // Get the size of the deck
    public int size() {
        return cards.size();
    }

    
    // public static void main(String[] args) {
    //     DeckOfCards deck = new DeckOfCards();
    //     System.out.println("Initial deck size: " + deck.size());
    //     deck.shuffle();
    //     System.out.println("Shuffled deck size: " + deck.size());

    //     String drawnCard = deck.drawCard();
    //     System.out.println("Drawn card: " + drawnCard);
    //     System.out.println("Deck size after drawing a card: " + deck.size());
    // }
}





