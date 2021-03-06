package edu.up.cs301.card;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;

import java.io.Serializable;

import edu.up.cs301.game.R;

/**
 * Card
 *
 * A playing card to be used in the game of Flinch. Unlike a standard deck of 52 cards, Flinch
 * requires a deck of 150 cards numbered 1 through 15. All card images were made by the Flinch team
 * and have been placed in the res/drawable-hdpi folder in the project.
 *
 * NOTE: In order to display the card-images on the android you need to call the
 * Card.initImages(currentActivity) method during initialization;
 *
 * @author Weslyn Nishimura
 * @author Dr. Vegdahl (this card class is modeled after Dr. Vegdahl's)
 *
 * @version November 2018
 */
public class Card implements Serializable {

    // to satisfy the Serializable interface
    private static final long serialVersionUID = 893542931190030342L;

    // instance variables
    private int cardNum;

    /**
     * Constructor for class card
     *
     * @param num the card number
     */
    public Card(int num) {
        // if the number is valid
        if((num > 0) && (num <= 15)) {
            // set the Card object number to num
            cardNum = num;
        } else {
            // otherwise set to -1 to indicate an empty card
            cardNum = -1;
        }
    }

    /**
     * Constructor for class card (copy constructor)
     *
     * @param orig the card to copy
     */
    public Card(Card orig) {
        cardNum = orig.cardNum;
    }

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
     * Tells whether object are equal -- in other words that they are both Card
     * objects that represent the same card.
     *
     * @return
     *		true if the two card objects represent the same card, false
     *		otherwise.
     */
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Card)) return false;
        Card c = (Card)other;
        return this.cardNum == c.cardNum;
    }

    /**
     * Draws the card on a Graphics object.  The card is drawn as a
     * white card with a black border.  If the card's rank is numerih, the
     * appropriate number of spots is drawn.  Otherwise the appropriate
     * picture (e.g., of a queen) is included in the card's drawing.
     *
     * @param g  the graphics object on which to draw
     * @param where  a rectangle that tells where the card should be drawn
     */
    public void drawOn(Canvas g, RectF where, int needFlip) {
        // don't draw an invalid card
        if(cardNum < 1) {
            return;
        }
        // create the paint object
        Paint p = new Paint();
        p.setColor(Color.BLACK);

        // get the bitmap for the card
        Rect r;
        Bitmap bitmap;
        if(needFlip != 1) {
            bitmap = cardImages[0][this.getNum() - 1];

            // create the source rectangle
            r = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            g.drawBitmap(bitmap, r, where, p);
        } else {
            // draw the rotated bitmap
            bitmap = cardImages[0][this.getNum() - 1];
            bitmap = RotateBitmap(bitmap, 90);
            bitmap = bitmap.createScaledBitmap(bitmap, (int)(where.right - where.left), (int)(where.bottom - where.top), false);

            // create the source rectangle
            r = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            g.drawBitmap(bitmap, where.left, where.top, p);
        }

        // does the bitmap need flipped?
        /*if(needFlip == 1) {
            bitmap = flip(bitmap, Direction.VERTICAL);
        } */

        // draw the bitmap into the target rectangle
        //g.drawBitmap(bitmap, r, where, p);
    }

    public static Bitmap RotateBitmap(Bitmap source, float angle)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }
    public enum Direction {VERTICAL, HORIZONTAL}
    public static Bitmap flip(Bitmap src, Direction type) {
        Matrix matrix = new Matrix();

        if(type == Direction.VERTICAL) {
            matrix.preScale(1.0f, -1.0f);
        }
        else if(type == Direction.HORIZONTAL) {
            matrix.preScale(-1.0f, 1.0f);
        } else {
            return src;
        }

        return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
    }

    // array that contains the android resource indices for the 52 card
    // images
    private static int[][] resIdx = {
            {
                    R.drawable.card01_1x, R.drawable.card02_1x, R.drawable.card03_1x,
                    R.drawable.card04_1x, R.drawable.card05_1x, R.drawable.card06_1x,
                    R.drawable.card07_1x, R.drawable.card08_1x, R.drawable.card09_1x,
                    R.drawable.card10_1x, R.drawable.card11_1x, R.drawable.card12_1x,
                    R.drawable.card13_1x, R.drawable.card14_1x, R.drawable.card15_1x,
            },
            {
                    R.drawable.card01_2x, R.drawable.card02_2x, R.drawable.card03_2x,
                    R.drawable.card04_2x, R.drawable.card05_2x, R.drawable.card06_2x,
                    R.drawable.card07_2x, R.drawable.card08_2x, R.drawable.card09_2x,
                    R.drawable.card10_2x, R.drawable.card11_2x, R.drawable.card12_2x,
                    R.drawable.card13_2x, R.drawable.card14_2x, R.drawable.card15_2x,
            },
    };

    // the array of card images
    private static Bitmap[][] cardImages = null;

    /**
     * initializes the card images
     *
     * @param activity
     * 		the current activity
     */
    public static void initImages(Activity activity) {
        // if it's already initialized, then ignore
        if (cardImages != null) return;

        // create the outer array
        cardImages = new Bitmap[resIdx.length][resIdx.length];

        // loop through the resource-index array, creating a
        // "parallel" array with the images themselves
        for (int i = 0; i < resIdx.length; i++) {
            // create an inner array
            cardImages[i] = new Bitmap[resIdx[i].length];
            for (int j = 0; j < resIdx[i].length; j++) {
                // create the bitmap from the corresponding image
                // resource, and set the corresponding array element
                cardImages[i][j] =
                        BitmapFactory.decodeResource(
                                activity.getResources(),
                                resIdx[i][j]);
            }
        }
    }
}