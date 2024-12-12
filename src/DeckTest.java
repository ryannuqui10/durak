import static org.junit.Assert.*;
import org.junit.Test;

public class DeckTest {

    @Test
    public void testSetTrump() {
        Deck deck = new Deck();
        deck.setTrumpSuit(Suit.CLUBS);

        for (Card card : deck) {
           if (card.suit == Suit.CLUBS) {
               assertTrue(card.isTrump);
           } else {
               assertFalse(card.isTrump);
           }
        }
    }

    @Test
    public void testEquals() {
        Deck deck1 = new Deck();
        Deck deck2 = new Deck();

        assertEquals(deck1, deck2);

        Deck deck3 = new Deck();
        deck3.shuffle();

        Deck deck4 = new Deck(deck3);

        assertEquals(deck3, deck4);
        assertNotEquals(deck1, deck3);

        deck3.setTrumpSuit(Suit.CLUBS);
        deck4.setTrumpSuit(Suit.CLUBS);

        assertEquals(deck3, deck4);
        assertNotEquals(deck1, deck3);
    }

    @Test
    public void shuffleThenSort() {
        Deck expected = new Deck();
        Deck actual = new Deck();

        assertEquals(expected, actual);

        actual.shuffle();
        actual.sort();

        assertEquals(expected, actual);
    }
}
