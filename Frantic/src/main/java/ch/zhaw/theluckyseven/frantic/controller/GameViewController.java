package ch.zhaw.theluckyseven.frantic.controller;

import ch.zhaw.theluckyseven.frantic.model.gamelogic.Config;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card.Card;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card.EventCard;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card.HandCard;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.player.Player;
import ch.zhaw.theluckyseven.frantic.model.server_client_communication.DataPackage;
import ch.zhaw.theluckyseven.frantic.model.server_client_communication.NetworkConfig;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.awt.*;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.FutureTask;
import java.util.logging.Logger;

import static ch.zhaw.theluckyseven.frantic.controller.ClientPackageFactory.create;
import static ch.zhaw.theluckyseven.frantic.model.server_client_communication.DataPackage.PlayerAction.*;
import static ch.zhaw.theluckyseven.frantic.model.server_client_communication.NetworkConfig.State.DISCONNECTED;

/**
 * Controller Class for MainGameWindow.fxml
 * This Controller is responsible for the main game view.
 * Gets called from a {@link Client} object.
 * Communicates with logic via {@link DataPackage} object,
 */
public class GameViewController extends PlayController {
    private static final Logger logger = Logger.getLogger(GameViewController.class.getName());
    public static final Insets PADDING = new Insets(10, 5, 10, 5);
    public static final int CARD_WIDTH = 77;
    public static final int CARD_HEIGHT = 132;
    public static final int PLAYER_SPACING = 5;
    public static final int PLAYER_WIDTH = 80;
    public static final int PLAYER_HEIGHT = 113;
    private Button lastPressed;
    private DataPackage data = new DataPackage();

    @FXML
    private HBox playerBox;
    @FXML
    private HBox currentCardBox;
    @FXML
    private HBox eventCardBox;
    @FXML
    private HBox handCardBox;
    @FXML
    private Button drawCardButton;
    @FXML
    private Button finishTurnButton;
    @FXML
    private TextArea gameLog;

    /**
     * Creates object of GameViewController with empty fields
     */
    public GameViewController() {
        myName = "";
        data.setCurrentPlayer(myName);
        data.setPlayerCount(0);
        data.setAnswers(Collections.emptyList());
        data.setQuestions(Collections.emptyList());
        data.setPlayers(Collections.emptyList());
    }

    /**
     * Method to open a webpage
     *
     * @param urlString url to open
     */
    public static void openWebpage(String urlString) {
        try {
            Desktop.getDesktop().browse(new URL(urlString).toURI());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the first stage when creating UI with all its functionalities.
     *
     * @param client       the client instance to communicate with the server
     * @param primaryStage the stage to show the GUI on
     */
    public void initGameViewController(Stage primaryStage, Client client) {
        initController(client);
        primaryStage.setOnCloseRequest(event -> client.applicationClose());
    }

    private void disableButtons() {
        setTurnFinishable(false);
        setCanDrawCards(false);
    }

    @Override
    protected void refreshData(DataPackage data) {
        Platform.runLater(() -> {
            updateMyPlayer(data);
            setPlayers(data);
            setHandCards(me.getCards());
            setGameLog(data.getMessage());
            setCurrentCard(data.getCurrentCard());
            setEventCard(data.getEventCard());
            if (isMyTurn) {
                logger.fine("Doing my turn...");
                boolean turnFinishable = me.canFinishTurn();
                boolean canDrawCards = me.canDrawCard();
                setTurnFinishable(turnFinishable);
                setCanDrawCards(canDrawCards);
            } else {
                disableButtons();
            }
            this.data = data;
            logger.info("finished interpreting dataPackage");
        });
    }

    private void terminateConnectionHandler() {
        client.terminateConnectionHandler();
    }

    /**
     * Method to change state of the client.
     * Updates the client with the new state
     *
     * @param newState new state for the client
     */
    @Override
    public void stateChanged(NetworkConfig.State newState) {
        if (newState == DISCONNECTED) {
            terminateConnectionHandler();
        }
    }

    /**
     * method to write an error to the client chat window
     *
     * @param message error to display
     */
    @Override
    public void writeError(String message) {
        setGameLog(String.format("[ERROR] %s%n", message));
    }

    /**
     * method to write an info to the clients chat window
     *
     * @param message info to display
     */
    @Override
    public void writeInfo(String message) {
        setGameLog(message);
    }

    private void updateMyPlayer(DataPackage data) {
        if (data.getPlayers() == null) return;
        for (int i = 0; i < data.getPlayers().size(); ++i) {
            if (myName.equals(data.getPlayers().get(i).getName())) {
                logger.fine("Found own player with id: " + i);
                isMyTurn = data.getCurrentPlayer().equals(myName);
                me = data.getPlayers().get(i);
                return;
            }
        }
        logger.severe("Couldn't find own player with name " + myName + " in DataPackage");
        throw new IllegalArgumentException("couldn't find player in DataPackage");
    }

    @Override
    Config.CardColor colorChoice(List<Object> offer) {
        return Config.CardColor.valueOf(choiceOfOffer("Choose a color", offer));
    }

    @Override
    int numberChoice(List<Object> offer) {
        return Integer.parseInt(choiceOfOffer("Choose a number", offer));
    }

    @Override
    Object numberOrColorChoice(List<Object> offer) {
        return choiceOfOffer("Choose a color or a number", offer);
    }

    @Override
    Player playerChoice(List<Object> offer) {
        FutureTask<Player> task = new FutureTask<>(() -> choiceOfPlayer(offer));
        Platform.runLater(task);
        try {
            return task.get();
        } catch (Exception e) {
            logger.severe("Couldn't choose a Player");
        }
        throw new InternalError("Couldn't choose a Player");
    }

    private Player choiceOfPlayer(List<Object> offer) {
        logger.finest("Opening Player choice popup");
        String player = displayPopup("Choose a player", "Choose a player", offer);
        logger.finer(myName + " chooses " + player);

        for (int i = 0; i < data.getPlayers().size(); ++i) {
            if (player.equals(data.getPlayers().get(i).getName())) {
                return data.getPlayers().get(i);
            }
        }

        logger.severe("Couldn't find player after choice in DataPackage");
        throw new InternalError("Couldn't choose a player");
    }

    @Override
    HandCard cardChoice(Player target) {
        FutureTask<HandCard> task = new FutureTask<>(() -> choiceOfCard(target));
        Platform.runLater(task);
        try {
            return task.get();
        } catch (Exception e) {
            logger.severe("Couldn't chose a card");
        }
        throw new InternalError("Couldn't choose a card");
    }

    private HandCard choiceOfCard(Player target) {
        if (target.getCards().isEmpty()) {
            return null;
        }
        String card = displayPopup("Choose a card", "choose a card", target.getCards());
        logger.finer(myName + " chooses " + card + " from " + target);
        for (int i = 0; i < target.getCards().size(); ++i) {
            if (card.equals(target.getCard(i).getCardName())) {
                return target.removeCard(target.getCard(i));
            }
        }

        logger.severe("couldn't find Card for the chosen card " + card + " of player " + target.getName());
        throw new InternalError("Couldn't choose a card");
    }

    @Override
    Player actorChoice() {
        return me;
    }

    @Override
    void handleRoundEnd(DataPackage dataPackage) {
        Platform.runLater(() ->
                displayPopup("Round finished",
                        "The round is over, The winner is " + dataPackage.getCurrentPlayer())
        );
    }

    @Override
    void handleGameEnd(DataPackage dataPackage) {
        Platform.runLater(() -> {
            displayPopup("Game finished", "The winner of  the game is " + dataPackage.getCurrentPlayer());
            disableButtons();
        });
    }

    private String choiceOfOffer(String text, List<Object> offer) {
        FutureTask<String> task = new FutureTask<>(() -> displayPopup(text, text, offer));
        Platform.runLater(task);
        try {
            return task.get();
        } catch (Exception e) {
            logger.severe("Couldn't choose from offer");
        }
        throw new InternalError("Couldn't choose from offer");
    }

    private void displayPopup(String title, String text) {
        logger.fine("Opening generic popup");
        Platform.runLater(() -> {
            Alert popup = new Alert(Alert.AlertType.INFORMATION);
            popup.setTitle(title);
            popup.setHeaderText(text);
            popup.showAndWait();
        });
    }

    private String displayPopup(String title, String text, List<?> list) {
        if (list.isEmpty()) throw new IllegalArgumentException("can't create popup with 0 buttons");
        logger.fine("Opening generic popup with buttons: " + list);
        Alert popup = new Alert(Alert.AlertType.INFORMATION);
        popup.setTitle(title);
        popup.setHeaderText(text);
        ObservableList<ButtonType> pButtons = popup.getButtonTypes();
        pButtons.clear();

        for (Object item : list) {
            pButtons.add(new ButtonType(String.valueOf(item)));
        }
        Optional<ButtonType> result = popup.showAndWait();
        return result.isPresent() ? result.get().getText() : displayPopup(title, text, list);
    }

    private void setTurnFinishable(boolean canBeFinished) {
        logger.finest("toggle finish button");
        Platform.runLater(() -> finishTurnButton.setDisable(!canBeFinished));
    }

    private void setCanDrawCards(boolean canDrawCards) {
        logger.finest("toggle draw button");
        Platform.runLater(() -> drawCardButton.setDisable(!canDrawCards));
    }

    private void setGameLog(String message) {
        if (message == null || message.equals(data.getMessage())) return;
        logger.fine("Setting message log" + message);
        Platform.runLater(() -> gameLog.setText(gameLog.getText().concat("\n").concat(message)));
    }

    /**
     * Method to handle press on help button
     */
    public void onHelpPressed() {
        openWebpage(Config.FRANTIC_URL);
    }

    private void setHandCards(List<HandCard> handCards) {
        if (handCards == null) return;

        logger.fine("Setting handCards" + handCards);
        handCardBox.getChildren().clear();
        handCards.forEach(card -> {
            handCardBox.getChildren().add(createCard(card, isMyTurn && card.isPlayable(), this::onCardPress));
            logger.finest("Added card " + card.toString());
        });
    }

    /**
     * Method to handle press on "Draw Card" button
     */
    public void onDrawCard() {
        send(create(DRAW));
        setCanDrawCards(false);
    }

    private Button createButton(String text, int width, int height, boolean enabled, EventHandler<ActionEvent> onAction) {
        Button button = new Button();
        button.setText(text);
        button.setMinWidth(width);
        button.setMaxWidth(width);
        button.setMinHeight(height);
        button.setMaxHeight(height);
        button.setDisable(!enabled);
        button.setOnAction(onAction);
        return button;
    }

    private Button createCard(Card card, int width, int height, boolean enabled, EventHandler<ActionEvent> onAction) {
        Button button = createCard(card.getCardImage(), width, height, enabled, onAction);
        if (card instanceof HandCard) {
            button.setId("" + ((HandCard) card).getId());
        }
        return button;
    }

    private Button createCard(Card card, boolean enabled, EventHandler<ActionEvent> onAction) {
        return createCard(card, CARD_WIDTH, CARD_HEIGHT, enabled, onAction);
    }

    private Button createCard(String url, boolean enabled, EventHandler<ActionEvent> onAction) {
        return createCard(url, CARD_WIDTH, CARD_HEIGHT, enabled, onAction);
    }

    private Button createCard(String url, int width, int height, boolean enabled, EventHandler<ActionEvent> onAction) {
        ImageView view = createImage(url, height, true);
        Button button = createButton("", width, height, enabled, onAction);
        button.setGraphic(view);
        return button;
    }

    private ImageView createImage(String url, int height, boolean ratio) {
        Image img = new Image(url);
        ImageView view = new ImageView(img);
        view.setSmooth(true);
        view.setFitHeight(height);
        view.setPreserveRatio(ratio);
        return view;
    }

    private void onCardPress(ActionEvent actionEvent) {
        Button pressed = (Button) actionEvent.getSource();
        if (pressed.equals(lastPressed)) {
            HandCard card = findCard(Integer.parseInt(pressed.getId()));
            send(create(PLACE, card));
        }
        lastPressed = pressed;
    }

    private HandCard findCard(int id) {
        for (HandCard card : me.getCards()) {
            if (card.getId() == id) {
                return card;
            }
        }
        throw new InternalError("Couldn't find card with id: " + id);
    }

    /**
     * Method to handle press on Finish Turn button
     */
    public void onFinishTurn() {
        send(create(END_TURN));
    }

    private void setCurrentCard(HandCard card) {
        logger.fine("Setting current card " + card);
        currentCardBox.getChildren().clear();
        currentCardBox.getChildren().add(createCard(card, true, dummy -> {
        }));
    }

    private void setEventCard(EventCard card) {
        eventCardBox.getChildren().clear();
        if (card != null) {
            eventCardBox.getChildren().add(createCard(card, true, dummy -> {
            }));
        } else {
            String eventBackUrl = "/ch.zhaw.theluckyseven.frantic.view/card/back/event_card_back.png";
            eventCardBox.getChildren().add(createCard(eventBackUrl, true, dummy -> {
            }));
        }
    }

    private void setPlayers(DataPackage data) {
        if (data.getPlayers() == null) return;
        logger.fine("Setting players" + data.getPlayers());
        playerBox.getChildren().clear();

        for (int i = 0; i < data.getPlayers().size(); ++i) {
            boolean highlighted = data.getCurrentPlayer().equals(data.getPlayers().get(i).getName());
            playerBox.getChildren().add(createPlayer(data.getPlayers().get(i), highlighted));
        }
    }

    private TitledPane createPlayer(Player player, boolean highlighted) {
        TitledPane playerPane = new TitledPane();
        playerPane.setText(player.getName());
        if (highlighted) playerPane.setTextFill(Paint.valueOf("RED"));
        playerPane.setCollapsible(false);
        VBox box = createVBox(PLAYER_SPACING, PLAYER_WIDTH, PLAYER_HEIGHT, Pos.TOP_CENTER);
        box.getChildren().addAll(
                new Label("Karten:"),
                new Text(String.valueOf(player.getCardCount())),
                new Separator(),
                new Label("Punkte:"),
                new Text(String.valueOf(player.getScore())));
        playerPane.setContent(box);
        return playerPane;
    }

    private VBox createVBox(int spacing, int width, int height, Pos alignment) {
        VBox box = new VBox();
        box.setAlignment(alignment);
        box.setSpacing(spacing);
        box.setPadding(PADDING);
        box.setMinWidth(width);
        box.setMaxWidth(width);
        box.setMinHeight(height);
        box.setMaxHeight(height);
        return box;
    }
}
