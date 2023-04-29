import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Hand implements Comparable<Hand> {
    private Set<Card> cards;
    private int handValue;

    public Hand(Set<Card> cards) {
        this.cards = cards;
        calculateHandValue();
    }

    public int calculateHandValue() {
        // The handValue will be represented as a 6-digit number: HRRRRR
    // H: Poker hand type (1-9, where 9 is the highest)
    // RRRRR: Rank representation for tie-breaking

    int[] ranks = new int[14]; // Ace can be represented by 1 or 14
    int[] suits = new int[4];

    for (Card card : cards) {
        ranks[card.getRank()]++;
        if (card.getRank() == 1) {
            ranks[13]++;
        }
        suits[card.getSuit() - 1]++;
    }

    boolean flush = false;
    int flushSuit = -1;
    for (int i = 0; i < 4; i++) {
        if (suits[i] >= 5) {
            flush = true;
            flushSuit = i + 1;
            break;
        }
    }

    int straightHighRank = getStraightHighRank(ranks);
    boolean straight = straightHighRank != -1;

    if (straight && flush) {
        List<Card> flushStraightCards = new ArrayList<>();
        for (Card card : cards) {
            if (card.getSuit() == flushSuit) {
                flushStraightCards.add(card);
            }
        }
        int flushStraightHighRank = getStraightHighRank(getRanksArray(flushStraightCards));
        if (flushStraightHighRank == 14) {
            handValue = 9140000; // Royal Flush
        } else if (flushStraightHighRank != -1) {
            handValue = 800000 + flushStraightHighRank * 10000; // Straight Flush
        }
    }

    if (handValue == 0 && (straight || flush)) {
        if (straight) {
            handValue = 400000 + straightHighRank * 10000; // Straight
        } else {
            handValue = 500000 + getHighCardsValue(ranks, 5) * 100; // Flush
        }
    }

    if (handValue == 0) {
        int[] rankCount = new int[5]; // Stores count of pairs, three-of-a-kind, and four-of-a-kind
        for (int i = 13; i >= 2; i--) {
            if (ranks[i] >= 2) {
                if (ranks[i] == 4) {
                    rankCount[4]++;
                } else if (ranks[i] == 3) {
                    rankCount[3]++;
                } else {
                    rankCount[2]++;
                }
            }
        }

        if (rankCount[4] == 1) {
            handValue = 700000 + getIndexOf(ranks, 4) * 10000; // Four of a Kind
        } else if (rankCount[3] >= 1 && rankCount[2] >= 1) {
            int threeOfAKindRank = getIndexOf(ranks, 3);
            int pairRank = getIndexOfExcept(ranks, 2, threeOfAKindRank);
            handValue = 600000 + threeOfAKindRank * 10000 + pairRank * 100; // Full House
        } else if (rankCount[3] >= 1) {
            handValue = 300000 + getIndexOf(ranks, 3) * 10000 + getHighCardsValue(ranks, 2) * 100; // Three of a Kind
        } else if (rankCount[2] >= 2) {
            int highPairRank = getIndexOf(ranks, 2);
            int lowPairRank = getIndexOfExcept(ranks, 2, highPairRank);
            handValue = 200000 + highPairRank * 10000 + lowPairRank * 100 + getHighCardsValueExcept(ranks, 1, highPairRank, lowPairRank); // Two Pair
        } else if (rankCount[2] == 1) {
            int pairRank = getIndexOf(ranks, 2);
            handValue = 100000 + pairRank * 10000 + getHighCardsValueExcept(ranks, 3, pairRank) * 10; // One Pair
        } else {
            handValue = getHighCardsValue(ranks, 5); // High Card
        }
    }
    return handValue;
}

private int[] getRanksArray(List<Card> cards) {
    int[] ranks = new int[15];
    for (Card card : cards) {
        ranks[card.getRank()]++;
        if (card.getRank() == 1) {
            ranks[14]++;
        }
    }
    return ranks;
}

private int getStraightHighRank(int[] ranks) {
    for (int i = 13; i >= 5; i--) {
        if (ranks[i] >= 1 && ranks[i - 1] >= 1 && ranks[i - 2] >= 1 && ranks[i - 3] >= 1 && ranks[i - 4] >= 1) {
            return i;
        }
    }
    return -1;
}

private int getHighCardsValue(int[] ranks, int count) {
    int value = 0;
    int currentCount = 0;
    for (int i = 13; i >= 2 && currentCount < count; i--) {
        if (ranks[i] >= 1) {
            value = value * 100 + i;
            currentCount++;
        }
    }
    return value;
}

private int getHighCardsValueExcept(int[] ranks, int count, int... except) {
    int value = 0;
    int currentCount = 0;
    for (int i = 13; i >= 2 && currentCount < count; i--) {
        if (ranks[i] >= 1 && !contains(except, i)) {
            value = value * 100 + i;
            currentCount++;
        }
    }
    return value;
}

private int getIndexOf(int[] ranks, int targetCount) {
    for (int i = 13; i >= 2; i--) {
        if (ranks[i] == targetCount) {
            return i;
        }
    }
    return -1;
}

private int getIndexOfExcept(int[] ranks, int targetCount, int except) {
    for (int i = 13; i >= 2; i--) {
        if (ranks[i] == targetCount && i != except) {
            return i;
        }
    }
    return -1;
}

private boolean contains(int[] array, int value) {
    for (int i : array) {
        if (i == value) {
            return true;
        }
    }
    return false;
    }

    public int getHandValue() {
        return handValue;
    }

    @Override
    public int compareTo(Hand other) {
        return Integer.compare(this.handValue, other.handValue);
    }
}
