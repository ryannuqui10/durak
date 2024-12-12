import java.util.*;
import java.util.stream.Collectors;

public class Game {
    int totalPlayers;

    boolean isOver;

    Player[] players;
    Deck deck;

    int firstAttackerIndex;
    int defenderIndex;
    int currentPlayerIndex;

    Set<Integer> valuesPlayed;
    Set<Player> playersOut;
    List<Battle> battles;


    static final int START_HAND_SIZE = 6;

    public Game(int numPlayers) {
        totalPlayers  = numPlayers;
        isOver = false;

        players = new Player[numPlayers];
        for (int i = 0; i < numPlayers; i++) {
            players[i] = new Player();
        }

        deck = Deck.getRandomDeck();

        for (int player = 0; player < numPlayers; player++) {
            for (int cardsDealt = 0; cardsDealt < START_HAND_SIZE; cardsDealt++) {
                Card topCard = deck.removeTop();
                players[player].getCard(topCard);
            }
        }

        firstAttackerIndex = wrapIndex(0);
        currentPlayerIndex = wrapIndex(0);
        defenderIndex = wrapIndex(1);

        valuesPlayed = new HashSet<>();
        playersOut = new HashSet<>();
        battles = new ArrayList<>();
    }

    public void givePlayerInfo() {
        System.out.println("It is your turn, Player " + currentPlayerIndex + ".\n");
        if (currentPlayerIndex == defenderIndex) {
            System.out.println("You are the defender.");
        } else {
            System.out.println("You are the attacker.");
        }
        System.out.println("The trump suit is " + deck.trumpSuit + ".\n");
    }

    public boolean canDefend() {
        Player defender = getPlayer(defenderIndex);

        List<Set<Card>> possibleDefenders = new ArrayList<>();
        for (int i = 0; i < battles.size(); i++) {
            Battle battle = battles.get(i);
            Set<Card> defenders = new HashSet<>();

            // If battle requires defense
            if (battle.defense == null) {
                for (Card card : defender.hand) {
                    if (card.beats(battle.attack)) {
                        defenders.add(card);
                    }
                }
            }

            possibleDefenders.add(defenders);
        }

        return canDefendHelper(0, possibleDefenders, new HashSet<>());
    }

    private boolean canDefendHelper(int currentBattleIndex,
                                           List<Set<Card>> possibleDefenders, Set<Card> cardsUsed) {
        if (currentBattleIndex == battles.size()) {
            return true;
        } else {
            Battle currentBattle = battles.get(currentBattleIndex);
            if (currentBattle.defense == null) {
                return canDefendHelper(currentBattleIndex + 1, possibleDefenders, cardsUsed);
            } else {
                for (Card card : possibleDefenders.get(currentBattleIndex)) {
                    if (!cardsUsed.contains(card) && card.beats(currentBattle.attack)) {
                        // Use this card and recurse
                        currentBattleIndex++;
                        cardsUsed.add(card);

                        // If other battles can be defended, return true.
                        if (canDefendHelper(currentBattleIndex + 1, possibleDefenders, cardsUsed)) {
                            return true;
                        } else {
                            currentBattleIndex--;
                            cardsUsed.remove(card);
                        }
                    }
                }
                return false;
            }
        }
    }

    private boolean willPLay(boolean isAttacking) {
        Scanner reader = new Scanner(System.in);
        if (isAttacking) {
            System.out.println("Do you want to attack?");
        } else {
            System.out.println("Do you want to defend?");
        }
        try {
            String input = reader.nextLine();
            if (input.equals("Yes")) {
                return true;
            } else if (input.equals("No")) {
                return false;
            } else {
                throw new IllegalArgumentException("Respond with either \"Yes\" or \"No\".");
            }
        } catch(IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return willPLay(isAttacking);
        }
    }

    public void defenderTake(){
        Player defender = getPlayer(defenderIndex);
        for (Battle battle : battles) {
            defender.getCard(battle.attack);
            defender.getCard(battle.defense);
        }

        // Skip current defender
        currentPlayerIndex = wrapIndex(defenderIndex + 1);
        defenderIndex = wrapIndex(currentPlayerIndex + 1);
    }

    private List<Card> validAttacks(Player player) {
        List<Card> toReturn = new ArrayList<>();

        for (Card c : player.hand) {
            if (valuesPlayed.contains(c.value)) {
                toReturn.add(c);
            }
        }

        return toReturn;
    }

    private void attackWithCard(Player attacker, Card card) {
        valuesPlayed.add(card.value);
        battles.add(new Battle(card));

        System.out.println("You played this card.");
        System.out.println(card.cardInfo());
    }

    /*
        If the current player has no more turns, skip this attack.

        If the player is the first attacker or choose to, player selects a valid card to attack with.
        This card is added to battles. The value is added to
        Else, skip this attack.
     */
    public boolean attack(boolean isFirst) {
        givePlayerInfo();
        Player attacker = getPlayer(currentPlayerIndex);

        if (attacker.hand.isEmpty()) {
            return false;
        }

        System.out.println("You have these cards.");
        printChoices(attacker.hand);

        List<Card> validAttacks;

        if (isFirst) {
            validAttacks = attacker.hand;
        } else {
            validAttacks = validAttacks(attacker);
        }

        if (!validAttacks.isEmpty() && (isFirst || willPLay(true))) {
            System.out.println("Select a card to attack with.");
            //int cardIndex = getChoice(validAttacks);
            Card card = attacker.lowestCard(); //validAttacks.get(cardIndex);

            attackWithCard(attacker, card);

            currentPlayerIndex = wrapIndex(currentPlayerIndex + 1);
            return true;
        } else {
            return false;
        }

    }



    public boolean defend() {
        givePlayerInfo();
        Player defender = getPlayer(currentPlayerIndex);

        System.out.println("You have these cards.");
        printChoices(defender.hand);

        System.out.println("These are the cards you are being attacked with.");
        List<Card> attacks = battles.stream().map(b -> b.attack).collect(Collectors.toList());
        printChoices(attacks);

        if (canDefend() && willPLay(false)) {
            for (Battle b : battles) {
                Card defendingFrom = b.attack;
                System.out.println("Which card will you play to defend this attack?");
                System.out.println(defendingFrom.cardInfo());
                boolean validDefense = false;

                while (!validDefense) {
                    printChoices(defender.hand);
                    int cardIndex = getChoice(defender.hand);
                    Card defendingWith = defender.playCard(cardIndex);

                    if (defendingWith.beats(defendingFrom)) {
                        b.defense = defendingWith;
                            System.out.println("You played this card.");
                            System.out.println(defendingFrom.cardInfo());
                            System.out.println();
                            validDefense = true;
                        } else {
                            System.out.println("Invalid. You will be unable to defend all battles. Try another card.");
                            defender.getCard(defendingWith);
                            b.defense = null;
                        }
                    } else {
                        System.out.println("Invalid. This card can not defend this attack. Try another card.");
                    }
                }

            } else {
            defenderTake();
            return false;
        }
    }

    public void printChoices(List<Card> options) {
        for (int i = 0; i < options.size(); i++) {
            Card card = options.get(i);

            System.out.println("[" + i + "]");

            System.out.println(card.cardInfo());
            System.out.println();
        }
    }

    public int getChoice(List<Card> options) {
        Scanner reader = new Scanner(System.in);

        try {
            int input = reader.nextInt();
            if (input < 0 || input >= options.size()) {
                throw new IllegalArgumentException("Select a valid option.");
            } else {
                return input;
            }
        } catch(IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return getChoice(options);
        }
    }


    public int cardIndexChosen(Player currentPlayer) {
        Scanner reader = new Scanner(System.in);
        System.out.println("Select a card.");
        try {
            int input = reader.nextInt();
            if (input < 0 || input >= currentPlayer.hand.size()) {
                throw new IllegalArgumentException("Select a valid card from your hand.");
            } else {
                return input;
            }
        } catch(IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return cardIndexChosen(currentPlayer);
        }
    }

    public void checkGameOver() {
        isOver = playersOut.size() == totalPlayers - 1;
    }

    public int wrapIndex(int index) {
        return Math.floorMod(index, players.length);
    }

    public Player getPlayer(int index) {
        return players[wrapIndex(index)];
    }



    public void checkPlayerOut() {
        Player currentPlayer = getPlayer(currentPlayerIndex);
        if (deck.isEmpty() && currentPlayer.hand.isEmpty()) {
            playersOut.add(currentPlayer);
        }
    }

    public void playGame() {
        while(!isOver) {
            attack(true);

            boolean finishedAttack = true;

            // Chose to defend
            if (defend()) {
                // Next player
                currentPlayerIndex = wrapIndex(currentPlayerIndex + 1);

                // Ask each other player
                while (currentPlayerIndex != defenderIndex) {
                    // Check if game has already ended
                    if (isOver) {
                        return;
                    }

                    Player currentPlayer = getPlayer(currentPlayerIndex);
                    // Chose to attack
                    if (attack(false)) {
                        finishedAttack = false;
                    }

                    currentPlayerIndex = wrapIndex(currentPlayerIndex + 1);
                }

                if (finishedAttack) {
                    battles = new ArrayList<>();
                }
            }
        }

    }

    public static void main(String[] args) {
        Game game = new Game(2);
        game.playGame();

    }




}
