package zh.zhaw.theluckyseven.frantic;

import java.util.ArrayList;
import java.util.Collections;

public class Game {
    Player currentPlayer;
    ArrayList<Player> players;
    int maxPoints;
    Card currentCard;

    public Game(){
        players = new ArrayList<>();
        maxPoints = 420;
    }

    public void startGame(){

    }

    public Player nextPlayer(){
        return players.get(0);
    }

    public void shuffleCards(){
        Collections.shuffle(players);
    }
}
