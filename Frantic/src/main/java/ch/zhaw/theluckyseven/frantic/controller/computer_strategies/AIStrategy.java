package ch.zhaw.theluckyseven.frantic.controller.computer_strategies;

import ch.zhaw.theluckyseven.frantic.model.gamelogic.Config;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card.HandCard;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.card.SpecialCard;
import ch.zhaw.theluckyseven.frantic.model.gamelogic.component.player.Player;
import ch.zhaw.theluckyseven.frantic.model.server_client_communication.DataPackage;

import java.util.List;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * Strategy class for the AI player. A game tree is created. The tree is searched with a Monte Carlo method.
 * Source: Artificial Intelligence: A Modern Approach. Russell, Norvig 2020.
 * <p>
 * will be used in a future update
 */
public class AIStrategy implements ComputerStrategy {
    private List<Node> tree;
    private int iterations;
    private final TreeMap<Double, List<Node>> uct;

    /**
     * Creates an Instance of AIStrategy
     *
     * @param iterations number of iterations
     */
    public AIStrategy(int iterations) {
        this.iterations = iterations;
        uct = new TreeMap<>();
    }

    /**
     * Method to choose a card to play
     *
     * @param state       the data to base your decision on
     * @param playOptions list of all the cards you are able to play
     * @param me          player representing yourself
     * @return the chosen card
     */
    @Override
    public HandCard chooseCard(DataPackage state, List<HandCard> playOptions, Player me) {
        //create tree from state;
        tree = calculateOptions(state, null);

        while (iterations > 0) {
            Node leaf = select(tree);
            List<Node> child = expand(leaf);
            boolean result = simulate(child);
            backpropagate(result, child);
            iterations--;
        }
        //return best node from tree;
        //TODO
        return null;
    }

    private List<Node> calculateOptions(DataPackage state, Node parentNode) {
        //get Player from state
        Player me = state.getPlayers().get(0);
        for (Player player : state.getPlayers()) {
            if (player.getName().equals(state.getCurrentPlayer())) me = player;
        }
        //get playOptions from state
        List<HandCard> playOptions = me.getCards().stream().filter(HandCard::isPlayable).collect(Collectors.toList());

        //add draw option
        List<Node> nodes = new java.util.ArrayList<>(List.of(new Node(null, null, state, parentNode)));
        //add place option for numeral cards and special cards respectively
        Player finalMe = me;
        playOptions
                .forEach(handCard -> {
                    if (!(handCard instanceof SpecialCard)) {
                        nodes.add(new Node(handCard, null, state, parentNode));
                    } else {
                        List<Player> otherPlayers = state.getPlayers().stream().filter(player -> !(player.equals(finalMe))).collect(Collectors.toList());
                        for (Player target : otherPlayers) {
                            nodes.add(new Node(handCard, target, state, parentNode));
                        }
                    }
                });
        return nodes;
    }

    private void backpropagate(boolean result, List<Node> child) {
    }

    private boolean simulate(List<Node> child) {
        //TODO
        return false;
    }

    //Create the new options.
    private List<Node> expand(Node leaf) {
        return calculateOptions(leaf.state, leaf);
    }

    //Get the best node according to upper confidence bounds policy.
    private Node select(List<Node> tree) {
        List<Node> possibleNodes = uct.lastEntry().getValue();
        if (possibleNodes.size() > 1) {
            return possibleNodes.get(Config.random.nextInt() * possibleNodes.size());
        } else {
            return possibleNodes.get(0);
        }
    }

    private class Node {
        HandCard card; // null if the node represents drawing
        Player target; // null if the node represents a numeral card
        DataPackage state;
        List<Node> childNodes;
        Node parentNode;
        double wins;
        double playouts;

        Node(HandCard card, Player target, DataPackage state, Node parentNode) {
            this.card = card;
            this.target = target;
            this.state = state;
            this.parentNode = parentNode;
            childNodes = null;
            wins = 0.0;
            playouts = 0.0;
        }

        void addChildNode(Node node) {
            if (childNodes != null) {
                childNodes.add(node);
            } else {
                childNodes = List.of(node);
            }
        }

        double UCB1() {
            double c = 1.4142135623730951;
            double parentNodePlayouts = Objects.requireNonNullElse(parentNode.playouts, 0.0);
            return (wins / playouts) + c * Math.sqrt(Math.log(parentNodePlayouts) / playouts);
        }
    }
}
