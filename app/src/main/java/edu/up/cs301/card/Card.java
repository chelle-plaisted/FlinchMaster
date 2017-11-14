package edu.up.cs301.card;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import java.io.Serializable;

/**
 * Card
 *
 * A playing card to be used in the game of Flinch. Unlike a standard deck of 52 cards, Flinch
 * requires a deck of 150 cards numbered 1 through 15.
 *
 * @author Alexa Ruiz
 * @author Chelle Plaisted
 * @author Rhianna Pinkerton
 * @author Weslyn Nishimura
 *
 * @version November 2018
 */
public class Card implements Serializable {
    // to satisfy the Serializable interface
    private static final long serialVersionUID = 893542931190030342L;

    // instance variables
    private int cardNum; // the card's value (between 1 and 15)

    /**
     * Card()
     *
     * The Card constructor. Creates an object of Card with the given number.
     *
     * @param num the value of the card
     */
    public Card(int num) {
        // if the number is valid
        if((num > 0) && (num <= 16)) {
            // set the Card object number to num
            cardNum = num;
        }

        // ********* TODO DETERMINE WHAT TO DO WHEN THE NUMBER IS NOT WITHIN RANGE
    }

    /**
     * Card()
     *
     * The Card copy constructor. Create a copy of the given Card object.
     *
     * @param orig the card to copy
     */
    public Card(Card orig) {
        // set the Card object number to the same as orig
        cardNum = orig.cardNum;
    }

    // Card Methods
    /**
     * getCardNum()
     *
     * Method to return the number of the Card object
     *
     * @return the value of the Card object
     */
    public int getNum() {
        // return the value of the card
        return cardNum;
    }

    /**
     * drawCard()
     *
     * Method to draw the Card object on the GUI
     *
     * @param g the graphics object on which to draw
     * @param where a rectangle that tells where the card should be drawn
     */
    public void drawCard(Canvas g, RectF where) {
        // TODO: IMPLEMENT
        /* DO NOT DELETE
        // create the paint object
        Paint p = new Paint();
        p.setColor(Color.BLACK);

        // get the bitmap for the card
        // Bitmap bitmap = cardImages[this.getNum().ordinal()];
        */
    }

    /* additional needed methods/variables: see the original card class for details
    -public static void initImiages (Activity activity)
    -private static Bitmap[][] cardImages
    -private static int[][] resIdx
    -public String shortName()
    -public int hasCode()

    ALS0 see res/drawable for card files
     */
}
