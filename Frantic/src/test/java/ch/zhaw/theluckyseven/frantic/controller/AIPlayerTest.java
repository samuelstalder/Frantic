package ch.zhaw.theluckyseven.frantic.controller;

import ch.zhaw.theluckyseven.frantic.model.gamelogic.Config;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.PackageFactory;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card.HandCard;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.player.Player;
import ch.zhaw.theluckyseven.frantic.model.server_client_communication.DataPackage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AIPlayerTest extends ControllerTestKit {
    @Mock
    private Client client;
    private AIPlayer aiPlayer;
    private int idOfAIplayer;
    private String nameOfAIplayer;
    private int idOfHumanplayer;
    private String nameOfHumanplayer;

    @Override
    @BeforeEach
    protected void setUp() {
        super.setUp();
        generateDeckOfHandCards();
        final int playerCount = 4;
        //generate players
        generatePlayers(4);
        //set up human player
        idOfHumanplayer = 0;
        nameOfHumanplayer = getPlayer(idOfHumanplayer).getName();

        //set up ai player
        idOfAIplayer = 2;
        nameOfAIplayer = getPlayer(idOfAIplayer).getName();
        aiPlayer = new AIPlayer(getPlayer(idOfAIplayer).getName(), AIPlayer.Smartness.SIMPLE, client);
    }

    @Override
    @AfterEach
    protected void tearDown() throws Exception {
        super.tearDown();
        aiPlayer = null;
    }

    @Test
    void AITurn() {
        specifyCurrentPlayer(idOfAIplayer);
        dataPackage = PackageFactory.create(DataPackage.LogicAction.REFRESH);
        assertEquals(nameOfAIplayer, dataPackage.getCurrentPlayer());
    }

    @Test
    void notAITurn() {
        specifyCurrentPlayer(idOfHumanplayer);
        dataPackage = PackageFactory.create(DataPackage.LogicAction.REFRESH);
        assertNotEquals(nameOfAIplayer, dataPackage.getCurrentPlayer());
    }

    @Test
    void AIColorChoice(){
        List<Object> offer = List.of(Config.CardColor.BLUE, Config.CardColor.GREEN);
        Config.CardColor color = aiPlayer.colorChoice(offer);
        assertTrue(color.equals(Config.CardColor.BLUE) || color.equals(Config.CardColor.GREEN));
    }

    @Test
    void negativeAIColorChoice(){
        List<Object> offer = List.of(Config.CardColor.BLUE, Config.CardColor.GREEN);
        Config.CardColor color = aiPlayer.colorChoice(offer);
        assertFalse(color.equals(Config.CardColor.YELLOW) || color.equals(Config.CardColor.RED));
    }

    @Test
    void AINumberChoice(){
        List<Object> offer = List.of(1, 2, 3, 4);
        int number = aiPlayer.numberChoice(offer);
        assertTrue(number == 1 || number == 2 || number == 3 || number == 4);
    }

    @Test
    void negativeAINumberChoice(){
        List<Object> offer = List.of(1, 2, 3, 4);
        int number = aiPlayer.numberChoice(offer);
        assertFalse(number == 5 || number == 6);
    }

    @Test
    void AINumberAndColorChoice(){
        List<Object> offer = List.of(1, 2, Config.CardColor.BLUE, Config.CardColor.GREEN);
        Object item = aiPlayer.numberOrColorChoice(offer);
        assertTrue(item.equals(1) || item.equals(2) || item.equals(Config.CardColor.BLUE) || item.equals(Config.CardColor.GREEN));
    }

    @Test
    void negativeAINumberAndColorChoice(){
        List<Object> offer = List.of(1, 2, Config.CardColor.BLUE, Config.CardColor.GREEN);
        Object item = aiPlayer.numberOrColorChoice(offer);
        assertFalse(item.equals(3) || item.equals(4) || item.equals(Config.CardColor.YELLOW) || item.equals(Config.CardColor.RED));
    }

    @Test
    void AIPlayerChoice(){
        Player p1 = new Player("p1", 1);
        Player p2 = new Player("p2", 2);
        Player p3 = new Player("p3", 3);

        List<Object> offer = List.of(p1, p2);
        Player player = aiPlayer.playerChoice(offer);
        assertTrue(player.equals(p1) || player.equals(p2));
    }

    @Test
    void negativeAIPlayerChoice(){
        Player p1 = new Player("p1", 1);
        Player p2 = new Player("p2", 2);
        Player p3 = new Player("p3", 3);

        List<Object> offer = List.of(p1, p2);
        Player player = aiPlayer.playerChoice(offer);
        assertFalse(player.equals(p3));
    }

    @Test
    void AIcardChoice(){
        Player p1 = new Player("p1", 1);
        HandCard card = aiPlayer.cardChoice(p1);
        assertEquals(null, card);
    }
}
