import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Deck implements Iterable<Card>{
    List<Card> cards;
    Suit trumpSuit;

    public static List<Card> createNewCards() {
        List<Card> deck = new ArrayList<>();


        for (Suit suit : Card.suits) {
            for (int value : Card.values) {
                deck.add(new Card(suit, value));
            }
        }

        return deck;
    }

    public static Deck getRandomDeck() {
        Deck deck = new Deck();
        deck.shuffle();
        deck.fixTrumpSuit();
        return deck;
    }

    public Deck() {
        cards = Deck.createNewCards();
    }

    public Deck(Deck deck) {
        cards = new ArrayList<>();
        for (Card card : deck) {
            cards.add(new Card(card));
        }
    }

    @Override
    public boolean equals(Object other) {
        // If the object is compared with itself then return true
        if (this == other) {
            return true;
        }

        /* Check if o is an instance of Complex or not
          "null instanceof [type]" also returns false */
        if (!(other instanceof Deck)) {
            return false;
        }

        return cards.equals(((Deck) other).cards);
    }

    @Override
    public Iterator<Card> iterator() {
        return cards.iterator();
    }

    public void fixTrumpSuit() {
        trumpSuit = bottomCard().suit;
        for (Card card : cards) {
            if (card.suit == trumpSuit) {
                card.isTrump = true;
            } else {
                card.isTrump = false;
            }
        }
    }

    public void setTrumpSuit(Suit trumpSuit) {
        for (Card card : cards) {
            if (card.suit == trumpSuit) {
                card.isTrump = true;
            } else {
                card.isTrump = false;
            }
        }
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    public Card removeTop() {
        return cards.remove(0);
    }

    public Card bottomCard() {
        return cards.get(cards.size() - 1);
    }

    public int numCards() {
        return cards.size();
    }

    public void shuffle(){
        Collections.shuffle(cards);
    }

    public void sort() {
        Collections.sort(cards);
    }

    public void printDeck() {
        for (Card card : this) {
            System.out.println(card);
            System.out.println();
        }
    }

}
