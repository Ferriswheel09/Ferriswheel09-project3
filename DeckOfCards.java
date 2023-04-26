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
                String card = rank + ':' + suit;
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





// import java.util.Collections;

// public class Shuffle {


//     public static String[][] shuffle() {
//         String[][] cards = new String[4][13];
        
//         String[] SUITS = {
//             "1", "2", "3", "4"
//         };

//         String[] RANKS = {
//             "1", "2", "3", "4", "5", "6", "7", "8", "9", 
//             "10", "11", "12", "13"
//         };

//         // initialize deck
//         int n = SUITS.length * RANKS.length;
//         for (int i = 0; i < SUITS.length; i++) {
//             for (int j = 0; j < RANKS.length; j++) {
//                 cards[i][j] = SUITS[i] + ':' + RANKS[j];
//                 System.out.println (cards[i][j]);   
//             }
//         }
//         Collenctions.shuffle(cards);
//         return cards;
        // for (int i = 0; i < RANKS.length; i++) {
        //     for (int j = 0; j < SUITS.length; j++) {
        //         System.out.println (cards[i][j]);
                
        //     }
        // }

        // shuffle
        // for (int i = 0; i < n; i++) {
        //     int r = i + (int) (Math.random() * (n-i));
        //     String temp = deck[r];
        //     deck[r] = deck[i];
        //     deck[i] = temp;
        // }

        // // print shuffled deck
        // for (int i = 0; i < n; i++) {
//         //     System.out.println(deck[i]);
//         // }
//     }
// }
