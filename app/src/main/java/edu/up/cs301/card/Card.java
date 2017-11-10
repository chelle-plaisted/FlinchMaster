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
     * Card constructor
     *
     * @param num
     */
    public Card(int num) {
        // TODO: IMPLEMENT
        cardNum = 0;
    }

    /**
     * Card copy constructor
     *
     * @param orig
     */
    public Card(Card orig) {
        // TODO: IMPLEMENT
    }

    /**
     * drawCard()
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
     * @return
     */
    public int getNum() {
        // TODO: IMPLEMENT
        return 0;
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
