package edu.up.cs301.card;

import android.graphics.Canvas;
import android.graphics.RectF;

import java.io.Serializable;

/**
 * Created by Weslyn on 11/5/2017.
 */

public class Card implements Serializable {
    // to satisfy the Serializable interface
    private static final long serialVersionUID = 893542931190030342L;

    // instance variables
    private int cardNum;

    /**
     * Card()
     *
     * The Card constructor. Creates an object of Card with the given number.
     *
     * @param num
     */
    public Card(int num) {
        // set the Card object number to num
        cardNum = num;
    }

    /**
     * Card()
     *
     * The Card copy constructor. Create a copy of the given Card object.
     *
     * @param orig
     */
    public Card(Card orig) {
        // set the Card object number to the same as orig
        cardNum = orig.getNum();
    }

    /**
     * drawCard()
     *
     * Method to draw the Card object on the GUI
     *
     * @param g
     * @param where
     */
    public void drawCard(Canvas g, RectF where) {
        // TODO: IMPLEMENT
    }

    /**
     * getCardNum()
     *
     * Method to return the number of the Card object
     *
     * @return the value of the Card object
     */
    public int getNum() {
        return cardNum;
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
