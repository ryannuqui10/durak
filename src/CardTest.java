import static org.junit.Assert.*;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

public class CardTest {
    @Test
    public void testSameSuitCompare() {
        Card lowCard = new Card(Suit.HEARTS, 0);
        Card highCard = new Card(Suit.HEARTS, 8);

        // Checks for non trumps
        assertTrue(highCard.beats(lowCard));
        assertFalse(lowCard.beats(highCard));

        lowCard.isTrump = true;
        highCard.isTrump = true;

        // Checks for trumps
        assertTrue(highCard.beats(lowCard));
        assertFalse(lowCard.beats(highCard));
    }

    @Test
    public void testDifferentSuitCompare() {
        Card heartCard = new Card(Suit.HEARTS, 0);
        Card clubsCard = new Card(Suit.CLUBS, 5);

        assertFalse(heartCard.beats(clubsCard));
        assertFalse(clubsCard.beats(heartCard));

        heartCard.isTrump = true;
        assertTrue(heartCard.beats(clubsCard));
        assertFalse(clubsCard.beats(heartCard));
    }
}
