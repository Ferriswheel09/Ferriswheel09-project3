import java.net.URL;

public class Card {
	private int rank; // 1-13 (1=Ace, 11=Jack, 12=Queen, 13=King)
    private int suit; // 1-4 (1=Clubs, 2=Diamonds, 3=Hearts, 4=Spades)

    public Card(int rank, int suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public int getRank() {
        return rank;
    }

    public int getSuit() {
        return suit;
    }

    public String getImagePath() {
        return "cardPNGs/" + rank + "_" + suit + ".png";
    }
}
