package swen221.cardgame.cards.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Represents a trick being played. This includes the cards that have been
 * played so far, as well as what the suit of trumps is for this trick.
 * 
 * @author David J. Pearce
 * 
 */
public class Trick implements Serializable {
	private static final long serialVersionUID = 1L;
    private Card[] cards = new Card[4];
    private Player.Direction lead;
    private Card.Suit trumps;

    /**
     * Construct a new trick with a given lead player and suit of trumps.
     * 
     * @param lead
     *            --- lead player for this trick.
     * @param trumps
     *            --- maybe null if no trumps.
     */
    public Trick(Player.Direction lead, Card.Suit trumps) {
        this.lead = lead;
        this.trumps = trumps;
    }

    /**
     * Determine who the lead player for this trick is.
     * 
     * @return --- who the lead player for this trick is.
     */
    public Player.Direction getLeadPlayer() {
        return lead;
    }

    /**
     * Determine which suit are trumps for this trick, or null if there are no
     * trumps.
     * 
     * @return --- which suit are trumps for this trick, or null if there are no
     *         trumps.
     */
    public Card.Suit getTrumps() {
        return trumps;
    }

    /**
     * Get the list of cards played so far in the order they were played.
     * 
     * @return
     */
    public List<Card> getCardsPlayed() {
        ArrayList<Card> cs = new ArrayList<Card>();
        for (int i = 0; i != 4; ++i) {
            if (cards[i] != null) {
                cs.add(cards[i]);
            } else {
                break;
            }
        }
        return cs;
    }

    /**
     * Get the card played by a given player, or null if that player has yet to
     * play.
     * 
     * @param p --- player
     * @return 
     */
    public Card getCardPlayed(Player.Direction p) {
        Player.Direction player = lead;
        for (int i = 0; i != 4; ++i) {
            if (player.equals(p)) {
                return cards[i];
            }
            player = player.next();
        }
        // dead code
        return null;
    }

    /**
     * Determine the next player to play in this trick.
     * 
     * @return 
     */
    public Player.Direction getNextToPlay() {
        Player.Direction dir = lead;
        for (int i = 0; i != 4; ++i) {
            if (cards[i] == null) {
                return dir;
            }
            dir = dir.next();
        }
        return null;
    }

    /**
     * Determine the winning player for this trick. This requires looking to see
     * which player led the highest card that followed suit; or, was a trump.
     * 
     * @return --- the winning player for this trick
     */
    public Player.Direction getWinner() {
        Player.Direction player = lead;
        Player.Direction winningPlayer = null;
        Card winningCard = cards[0];
        for (int i = 0; i != 4; ++i) {
            if (cards[i].suit() == winningCard.suit() 
            		&& cards[i].compareTo(winningCard) >= 0) {
                winningPlayer = player;
                winningCard = cards[i];
            } else if (trumps != null && cards[i].suit() == trumps 
            		&& winningCard.suit() != trumps) {
                // in this case, the winning card is a trump
                winningPlayer = player;
                winningCard = cards[i];
            }
            player = player.next();
        }
        return winningPlayer;
    }

    /**
     * Player attempts to play a card. This method checks that the given player
     * is entitled to play, and that the played card follows suit. If either of
     * these are not true, it throws an IllegalMove exception.
     * 
     * @param player --- the player who will play the given card
     * @param card ------ the card will be played
     * @throws IllegalMove  -- if playing the card is illegal, an IllegalMove exception
     *             is thrown
     */
    public void play(Player player, Card card) throws IllegalMove {
    		//check if the card is existing on hand
        if (!player.getHand().contains(card)) {
            throw new IllegalMove(
                    "The player at " + player.getDirection().toString() + " doesn't have " + card.toString() + " to play.");
        }

        // the player can only play a card when it's his turn
        if (getNextToPlay() != player.getDirection()) {
            throw new IllegalMove("It's not the turn for the player at " + player.getDirection().toString() + "to play.");
        }

        /*
         * if the player is not the lead and 
         * he has at least a card which he can play to follow the lead.
         * Besides, he did not play that card and he violates the rule.
         */
        if (player.getDirection() != lead) {
            Card.Suit leadSuit = getCardPlayed(lead).suit();
            Set<Card> cardsMatchesLead = player.getHand().matches(leadSuit);
            if (!cardsMatchesLead.isEmpty() && !cardsMatchesLead.contains(card)) {
                throw new IllegalMove(
                        "The player at " + player.getDirection().toString() + " doesn't follow the lead suit.");
            }
        }

        // Finally, play the card.
        for (int i = 0; i != 4; ++i) {
            if (cards[i] == null) {
                cards[i] = card;
                player.getHand().remove(card);
                break;
            }
        }
    }
}
