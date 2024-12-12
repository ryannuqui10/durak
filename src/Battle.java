public class Battle {
    Card attack;
    Card defense;

    public Battle(Card attack) {
        this.attack = attack;
    }

    public boolean setDefense(Card defense) {
        if (defense.beats(attack)) {
            this.defense = defense;
            return true;
        }  else {
            return false;
        }
    }
}
