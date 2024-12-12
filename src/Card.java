import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Card implements Comparable<Card>{

    static Suit[] suits = {Suit.HEARTS, Suit.CLUBS, Suit.DIAMONDS, Suit.SPADES};
    static int[] values = {0, 1, 2, 3, 4, 5, 6, 7, 8};
    static String[] valuesStrings = {"6", "7", "8", "9", "10", "J", "Q", "K", "A"};
    Suit suit;
    int value;
    boolean isTrump;

    public Card(Suit suit, int value) {
        this.suit = suit;
        this.value = value;
        this.isTrump = false;
    }

    public Card(Suit suit, int value, boolean isTrump) {
        this.suit = suit;
        this.value = value;
        this.isTrump = isTrump;
    }

    public Card(Card card) {
        this.suit = card.suit;
        this.value = card.value;
        this.isTrump = card.isTrump;
    }

    public String cardInfo() {
        String toReturn =  "Suit: " + suit;
        if (isTrump) {
            toReturn += " (TRUMP)";
        }
        toReturn += "\nValue: " + valuesStrings[value];
        return toReturn;
    }

    @Override
    public String toString() {
        return "Suit: " + suit + "\nValue: " + valuesStrings[value] + "\nisTrump: " + isTrump ;
    }

    @Override
    public int compareTo(Card other) {
        if (this.beats(other)) {
            return 1;
        } else if (other.beats(this)) {
            return -1;
        } else {
            return this.suit.compareTo(other.suit);
        }
    }

    public boolean beats(Card other) {
        if (suit == other.suit) {
            return value > other.value;
        } else {
            if (isTrump) {
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public boolean equals(Object other) {
        // If the object is compared with itself then return true
        if (this == other) {
            return true;
        }

        /* Check if o is an instance of Card or not
          "null instanceof [type]" also returns false */
        if (!(other instanceof Card)) {
            return false;
        }

        Card otherCard = (Card) other;

        return suit.equals(otherCard.suit) && value == otherCard.value && isTrump == otherCard.isTrump;
    }
}
