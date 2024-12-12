import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Player {
    public List<Card> hand;

    public Player() {
        hand = new ArrayList<>();
    }

    public void getCard(Card card) {
        if (card != null) {
            hand.add(card);
            Collections.sort(hand);
        }
    }

    public Card playCard(Card card) {
        hand.remove(card);
        return card;
    }

    public Card playCard(int cardIndex) {
        return hand.remove(cardIndex);
    }

    public Card lowestCard() {
        if (hand.isEmpty()) {
            return null;
        } else {
            Card lowest = hand.get(0);
            for (Card c : hand) {
                if (lowest.beats(c) || c.value < lowest.value) {
                    lowest = c;
                }
            }
            return lowest;
        }
    }

    public void printHand() {
        System.out.println("You have these cards.");

        for (int i = 0; i < hand.size(); i++) {
            Card card = hand.get(i);

            System.out.println("[" + i + "]");

            System.out.println(card.cardInfo());
            System.out.println();
        }
    }

    @Override
    public String toString() {
        String toReturn = "Number of Cards: " + hand.size() + "\n\n";
        for (Card card : hand) {
            toReturn += card + "\n\n";
        }
        return toReturn;
    }
}
