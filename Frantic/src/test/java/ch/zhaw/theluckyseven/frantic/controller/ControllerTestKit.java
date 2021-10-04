package ch.zhaw.theluckyseven.frantic.controller;

import ch.zhaw.theluckyseven.frantic.model.gamelogic.PackageFactory;
import ch.zhaw.theluckyseven.frantic.MockTestKit;
import ch.zhaw.theluckyseven.frantic.model.server_client_communication.DataPackage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public class ControllerTestKit extends MockTestKit {

    protected DataPackage dataPackage;

    @BeforeEach
    protected void setUp() {
        super.setUp();
        dataPackage = new DataPackage();
        PackageFactory.setGameStateController(gameStateController);
        PackageFactory.setGameState(gameState);
    }

    @AfterEach
    protected void tearDown() throws Exception {
        super.tearDown();
        dataPackage = null;
    }

    @Override
    protected void generatePlayers(int playerCount) {
        super.generatePlayers(playerCount);
        dataPackage.setPlayers(getPlayers());
    }

}
