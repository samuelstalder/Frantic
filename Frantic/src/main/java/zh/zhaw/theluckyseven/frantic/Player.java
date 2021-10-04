package zh.zhaw.theluckyseven.frantic;

import java.util.ArrayList;

public class Player {
    String name;
    ArrayList<Card> cards;
    int points;
    boolean canCounter = false;

    public Player(String name){
        this.name = name;
        points = 0;
        cards = new ArrayList<Card>();
    }

    public String getName() {
        return name;
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public int getPoints() {
        return points;
    }

    public boolean isCanCounter() {
        return canCounter;
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public void drawCard(){

    }
    public void removeCard(Card card){
        cards.remove(card);
    }
    public void setPoints(int points) {
        this.points = points;
    }

    public void setCanCounter(boolean canCounter) {
        this.canCounter = canCounter;
    }

    public void nextMove(){

    }
}
