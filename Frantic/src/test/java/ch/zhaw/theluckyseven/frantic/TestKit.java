package ch.zhaw.theluckyseven.frantic;

import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card.HandCard;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.deck.Deck;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.deck.DeckFactory;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.player.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;
import java.util.List;

public class TestKit {
    private List<Player> players;
    private Deck<HandCard> deck;

    @BeforeEach
    protected void setUp() {

    }

    @AfterEach
    protected void tearDown() throws Exception {
        players = null;
        deck = null;
    }

    protected void generatePlayers(int playerCount) {
        players = new ArrayList<>();
        int playerId = 0;
        for (int i = 0; i < playerCount; i++) {
            players.add(new Player("P " + playerId, playerId));
            playerId++;
        }
    }

    protected List<Player> getPlayers() {
        return players;
    }

    protected void setPlayers(List<Player> players) {
        this.players = players;
    }

    protected List<String> getNamesOfPlayers() {
        List<String> names = new ArrayList<>();
        for (Player p : players) {
            names.add(p.getName());
        }
        return names;
    }

    protected Player getPlayer(int playerId) {
        return players.get(playerId);
    }

    protected List<HandCard> getCardsOfPlayer(int playerId) {
        return players.get(playerId).getCards();
    }

    protected void generateDeckOfHandCards() {
        deck = (Deck<HandCard>) DeckFactory.create(DeckFactory.DeckType.HAND_CARD_DECK);
    }

    protected Deck<HandCard> getDeckOfHandCards() {
        return deck;
    }

    protected Deck<HandCard> getDeck() {
        return deck;
    }

    protected void setDeck(Deck<HandCard> deck) {
        this.deck = deck;
    }

}
