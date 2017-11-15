package edu.up.cs301.flinch;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import edu.up.cs301.animation.AnimationSurface;
import edu.up.cs301.animation.Animator;
import edu.up.cs301.card.Card;
import edu.up.cs301.flinch.FStateElements.FState;
import edu.up.cs301.game.GameHumanPlayer;
import edu.up.cs301.game.GameMainActivity;
import edu.up.cs301.game.R;
import edu.up.cs301.game.infoMsg.GameInfo;
import edu.up.cs301.game.infoMsg.IllegalMoveInfo;
import edu.up.cs301.game.infoMsg.NotYourTurnInfo;

/**
 * Created by alexaruiz on 11/6/17.
 */

public class FHumanPlayer extends GameHumanPlayer implements Animator {
    private final static float CARD_HEIGHT_PERCENT = 20; // height of a card
    private final static float CARD_WIDTH_PERCENT = (int) 8.5; // width of a card
    private final static float LEFT_BORDER_PERCENT = 4; // width of left border
    private final static float RIGHT_BORDER_PERCENT = 20; // width of right border
    private final static float VERTICAL_BORDER_PERCENT = 300; // width of top/bottom borders
    private final static float VERTICAL_BORDER_CENTERPILE = 200;

    // our game state
    protected FState state;

    // our activity
    private Activity myActivity;


    // the amination surface
    private AnimationSurface surface;

    // the background color
    private int backgroundColor;

    /**
     * constructor
     *
     * @param name
     * 		the player's name
     * @param bkColor
     * 		the background color
     */
    public FHumanPlayer(String name, int bkColor) {
        super(name);
        backgroundColor = bkColor;
    }

    /**
     * callback method: we have received a message from the game
     *
     * @param info
     * 		the message we have received from the game
     */
    @Override
    public void receiveInfo(GameInfo info) {
        Log.i("FComputerPlayer", "receiving updated state ("+info.getClass()+")");
        if (info instanceof IllegalMoveInfo || info instanceof NotYourTurnInfo) {
            // if we had an out-of-turn or illegal move, flash the screen
            surface.flash(Color.rgb(102,178,255), 50);
        }
        else if (!(info instanceof FState)) {
            // otherwise, if it's not a game-state message, ignore
            return;
        }
        else {
            // it's a game-state object: update the state. Since we have an animation
            // going, there is no need to explicitly display anything. That will happen
            // at the next animation-tick, which should occur within 1/20 of a second
            this.state = (FState)info;
            Log.i("human player", "receiving");
        }
    }

    /**
     * call-back method: called whenever the GUI has changed (e.g., at the beginning
     * of the game, or when the screen orientation changes).
     *
     * @param activity
     * 		the current activity
     */

    public void setAsGui(GameMainActivity activity) {
        // remember the activity
        myActivity = activity;

        // Load the layout resource for the new configuration
        activity.setContentView(R.layout.sj_human_player);

        // link the animator (this object) to the animation surface
        surface = (AnimationSurface) myActivity
                .findViewById(R.id.animation_surface);
        surface.setAnimator(this);

        // read in the card images
        //Card.initImages(activity);

        // if the state is not null, simulate having just received the state so that
        // any state-related processing is done
        if (state != null) {
            receiveInfo(state);
        }
    }


    public int interval() {
        // 1/20 of a second
        return 50;
    }

    public int backgroundColor() {
        return backgroundColor;
    }

    public boolean doPause() {
        return false;
    }

    public boolean doQuit() {
        return false;
    }

    public void tick(Canvas canvas) {
        // TODO: BELOW
            /*
            // Draw the cards of each player
                ///suggestion: use helper methods for each of the spaces
                    drawBottomPlayer()
                    drawLeftPlayer()
                    drawRightPlayer()
                    drawTopPlayer()
                // draw the player cards based on the number of players
                    2 players: top and bottom
                    3 players: left, right, bottom
                    4 players: all 4
             // draw the center piles
             // draw the flinch button

             */
        // ignore if we have not yet received the game state
        if (state == null) return;

        // get the height and width of the animation surface
        int height = surface.getHeight();
        int width = surface.getWidth();

        // draw the middle card-pile
        Card c = state.getDeck(2).peekAtTopCard(); // top card in pile
        //if (c != null) {
            // if middle card is not empty, draw a set of N card-backs
            // behind the middle card, so that the user can see the size of
            // the pile
            RectF centerPile1 = centerPile1();
            //drawCardBacks(canvas, midTopLocation,
                   // 0.0025f*width, -0.01f*height, state.getDeck(2).size());
            // draw the top card, face-up
            //drawCard(canvas, midTopLocation, c);
        //}

        // draw the opponent's cards, face down
        RectF oppTopLocation = opponentTopCardLocation(); // drawing size/location
        //drawCardBacks(canvas, oppTopLocation,
                //0.0025f*width, -0.01f*height, state.getDeck(1-this.playerNum).size());

        // draw my cards, face down
        RectF thisTopLocation = thisPlayerTopCardLocation(); // drawing size/location
        //drawCardBacks(canvas, thisTopLocation,
                //0.0025f*width, -0.01f*height, state.getDeck(this.playerNum).size());

        // draw a red bar to denote which player is to play (flip) a card
        RectF currentPlayerRect =
                state.toPlay() == this.playerNum ? thisTopLocation : oppTopLocation;
        RectF turnIndicator =
                new RectF(currentPlayerRect.left,
                        currentPlayerRect.bottom,
                        currentPlayerRect.right,
                        height);
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        canvas.drawRect(turnIndicator, paint);

    }

    private RectF opponentTopCardLocation() {
        // near the upper-left of the drawing surface, based on the height
        // and width, and the percentages defined above
        int width = surface.getWidth();
        int height = surface.getHeight();
        return new RectF(LEFT_BORDER_PERCENT*width/100f,
                (100-VERTICAL_BORDER_PERCENT-CARD_HEIGHT_PERCENT)*height/100f,
                (LEFT_BORDER_PERCENT+CARD_WIDTH_PERCENT)*width/100f,
                (100-VERTICAL_BORDER_PERCENT)*height/100f);
    }

    /**
     * @return
     * 		the rectangle that represents the location on the drawing
     * 		surface where the top card in the current player's deck is to
     * 		be drawn
     */
    private RectF thisPlayerTopCardLocation() {
        // near the right-bottom of the drawing surface, based on the height
        // and width, and the percentages defined above
        int width = surface.getWidth();
        int height = surface.getHeight();
        return new RectF((100-RIGHT_BORDER_PERCENT-CARD_WIDTH_PERCENT)*width/100f,
                (100-VERTICAL_BORDER_PERCENT-CARD_HEIGHT_PERCENT)*height/100f,
                (100-RIGHT_BORDER_PERCENT)*width/100f,
                (100-VERTICAL_BORDER_PERCENT)*height/100f);
    }

////
    /**
     * @return
     * 		the rectangle that represents the location on the drawing
     * 		surface where the top card in the middle pile is to
     * 		be drawn
     * 	can be used as our center piles
     */
    private RectF centerPile1 () {
        // near the middle-bottom (second row of cards) of the drawing surface, based on the height
        // and width, and the percentages defined above
        int height = surface.getHeight();
        int width = surface.getWidth();
        float rectLeft = (100-CARD_WIDTH_PERCENT+LEFT_BORDER_PERCENT-RIGHT_BORDER_PERCENT)*width/200;
        float rectRight = rectLeft + width*CARD_WIDTH_PERCENT/100;
        float rectTop = (100-VERTICAL_BORDER_CENTERPILE-CARD_HEIGHT_PERCENT)*height/100f;
        float rectBottom = (100-VERTICAL_BORDER_CENTERPILE)*height/100f;
        return new RectF(rectLeft, rectTop, rectRight, rectBottom);
    }
    //ALL CENTERPILES STIL NEED TO BE SCALED TO THE CORRECT SIZE AND PLACE ON SCREEN
    private RectF centerPile2() {

        // near the middle-bottom (first row of cards) of the drawing surface, based on the height
        // and width, and the percentages defined above
        int height = surface.getHeight();
        int width = surface.getWidth();
        float rectLeft = (CARD_WIDTH_PERCENT+LEFT_BORDER_PERCENT-RIGHT_BORDER_PERCENT)*width/200;
        float rectRight = rectLeft + width*CARD_WIDTH_PERCENT/100;
        float rectTop = (100-VERTICAL_BORDER_CENTERPILE-CARD_HEIGHT_PERCENT)*height/100f;
        float rectBottom = (100-VERTICAL_BORDER_CENTERPILE)*height/100f;
        return new RectF(rectLeft, rectTop, rectRight, rectBottom);
    }
    private RectF centerPile3() {

        // near the middle-bottom (first row of cards) of the drawing surface, based on the height
        // and width, and the percentages defined above
        int height = surface.getHeight();
        int width = surface.getWidth();
        float rectLeft = (CARD_WIDTH_PERCENT+LEFT_BORDER_PERCENT-RIGHT_BORDER_PERCENT)*width/200;
        float rectRight = rectLeft + width*CARD_WIDTH_PERCENT/100;
        float rectTop = (100-VERTICAL_BORDER_CENTERPILE-CARD_HEIGHT_PERCENT)*height/100f;
        float rectBottom = (100-VERTICAL_BORDER_CENTERPILE)*height/100f;
        return new RectF(rectLeft, rectTop, rectRight, rectBottom);
    }
    private RectF centerPile4() {

        // near the middle-bottom (first row of cards) of the drawing surface, based on the height
        // and width, and the percentages defined above
        int height = surface.getHeight();
        int width = surface.getWidth();
        float rectLeft = (CARD_WIDTH_PERCENT+LEFT_BORDER_PERCENT-RIGHT_BORDER_PERCENT)*width/200;
        float rectRight = rectLeft + width*CARD_WIDTH_PERCENT/100;
        float rectTop = (100-VERTICAL_BORDER_CENTERPILE-CARD_HEIGHT_PERCENT)*height/100f;
        float rectBottom = (100-VERTICAL_BORDER_CENTERPILE)*height/100f;
        return new RectF(rectLeft, rectTop, rectRight, rectBottom);
    }
    private RectF centerPile5() {

        // near the middle-bottom (first row of cards) of the drawing surface, based on the height
        // and width, and the percentages defined above
        int height = surface.getHeight();
        int width = surface.getWidth();
        float rectLeft = (CARD_WIDTH_PERCENT+LEFT_BORDER_PERCENT-RIGHT_BORDER_PERCENT)*width/200;
        float rectRight = rectLeft + width*CARD_WIDTH_PERCENT/100;
        float rectTop = (100-VERTICAL_BORDER_CENTERPILE-CARD_HEIGHT_PERCENT)*height/100f;
        float rectBottom = (100-VERTICAL_BORDER_CENTERPILE)*height/100f;
        return new RectF(rectLeft, rectTop, rectRight, rectBottom);
    }
    private RectF centerPile6() {

        // near the middle-bottom (first row of cards) of the drawing surface, based on the height
        // and width, and the percentages defined above
        int height = surface.getHeight();
        int width = surface.getWidth();
        float rectLeft = (CARD_WIDTH_PERCENT+LEFT_BORDER_PERCENT-RIGHT_BORDER_PERCENT)*width/200;
        float rectRight = rectLeft + width*CARD_WIDTH_PERCENT/100;
        float rectTop = (100-VERTICAL_BORDER_CENTERPILE-CARD_HEIGHT_PERCENT)*height/100f;
        float rectBottom = (100-VERTICAL_BORDER_CENTERPILE)*height/100f;
        return new RectF(rectLeft, rectTop, rectRight, rectBottom);
    }
    private RectF centerPile7() {

        // near the middle-bottom (first row of cards) of the drawing surface, based on the height
        // and width, and the percentages defined above
        int height = surface.getHeight();
        int width = surface.getWidth();
        float rectLeft = (CARD_WIDTH_PERCENT+LEFT_BORDER_PERCENT-RIGHT_BORDER_PERCENT)*width/200;
        float rectRight = rectLeft + width*CARD_WIDTH_PERCENT/100;
        float rectTop = (100-VERTICAL_BORDER_CENTERPILE-CARD_HEIGHT_PERCENT)*height/100f;
        float rectBottom = (100-VERTICAL_BORDER_CENTERPILE)*height/100f;
        return new RectF(rectLeft, rectTop, rectRight, rectBottom);
    }
    private RectF centerPile8() {

        // near the middle-bottom (first row of cards) of the drawing surface, based on the height
        // and width, and the percentages defined above
        int height = surface.getHeight();
        int width = surface.getWidth();
        float rectLeft = (CARD_WIDTH_PERCENT+LEFT_BORDER_PERCENT-RIGHT_BORDER_PERCENT)*width/200;
        float rectRight = rectLeft + width*CARD_WIDTH_PERCENT/100;
        float rectTop = (100-VERTICAL_BORDER_CENTERPILE-CARD_HEIGHT_PERCENT)*height/100f;
        float rectBottom = (100-VERTICAL_BORDER_CENTERPILE)*height/100f;
        return new RectF(rectLeft, rectTop, rectRight, rectBottom);
    }
    private RectF centerPile9() {

        // near the middle-bottom (first row of cards) of the drawing surface, based on the height
        // and width, and the percentages defined above
        int height = surface.getHeight();
        int width = surface.getWidth();
        float rectLeft = (CARD_WIDTH_PERCENT+LEFT_BORDER_PERCENT-RIGHT_BORDER_PERCENT)*width/200;
        float rectRight = rectLeft + width*CARD_WIDTH_PERCENT/100;
        float rectTop = (100-VERTICAL_BORDER_CENTERPILE-CARD_HEIGHT_PERCENT)*height/100f;
        float rectBottom = (100-VERTICAL_BORDER_CENTERPILE)*height/100f;
        return new RectF(rectLeft, rectTop, rectRight, rectBottom);
    }
    private RectF centerPile10() {

        // near the middle-bottom (first row of cards) of the drawing surface, based on the height
        // and width, and the percentages defined above
        int height = surface.getHeight();
        int width = surface.getWidth();
        float rectLeft = (CARD_WIDTH_PERCENT+LEFT_BORDER_PERCENT-RIGHT_BORDER_PERCENT)*width/200;
        float rectRight = rectLeft + width*CARD_WIDTH_PERCENT/100;
        float rectTop = (100-VERTICAL_BORDER_CENTERPILE-CARD_HEIGHT_PERCENT)*height/100f;
        float rectBottom = (100-VERTICAL_BORDER_CENTERPILE)*height/100f;
        return new RectF(rectLeft, rectTop, rectRight, rectBottom);
    }

    /**
     * MAY OR MAY NOT WANT THIS METHOD
     * draws a sequence of card-backs, each offset a bit from the previous one, so that all can be
     * seen to some extent
     *
     * @param g
     * 		the canvas to draw on
     * @param topRect
     * 		the rectangle that defines the location of the top card (and the size of all
     * 		the cards
     * @param deltaX
     * 		the horizontal change between the drawing position of two consecutive cards
     * @param deltaY
     * 		the vertical change between the drawing position of two consecutive cards
     * @param numCards
     * 		the number of card-backs to draw
     */
    private void drawCardBacks(Canvas g, RectF topRect, float deltaX, float deltaY,
                               int numCards) {
        // loop through from back to front, drawing a card-back in each location
        for (int i = numCards-1; i >= 0; i--) {
            // determine theh position of this card's top/left corner
            float left = topRect.left + i*deltaX;
            float top = topRect.top + i*deltaY;
            // draw a card-back (hence null) into the appropriate rectangle
            drawCard(g, new RectF(left, top, left + topRect.width(), top + topRect.height()),
                    null);
        }
    }

    public void onTouch(MotionEvent event) {
        // ignore everything except down-touch events
        if (event.getAction() != MotionEvent.ACTION_DOWN) return;

        // get the location of the touch on the surface
        int x = (int) event.getX();
        int y = (int) event.getY();

        // determine whether the touch occurred on the top-card of either
        // the player's pile or the middle pile
        RectF myTopCardLoc = thisPlayerTopCardLocation();
        RectF middleTopCardLoc = centerPile1();
        if (myTopCardLoc.contains(x, y)) {
            // it's on my pile: we're playing a card: send action to
            // the game
            //classes need to be implemented
            game.sendAction(new FPlayAction(this));
        }
        else if (middleTopCardLoc.contains(x, y)) {
            // it's on the middlel pile: we're slapping a card: send
            // action to the game
            //classes need to be implemented
           game.sendAction(new FFlinchAction(this));
        }
        else {
            // illegal touch-location: flash for 1/20 second
            surface.flash(Color.RED, 50);
        }
    }

    /**
     * draws a card on the canvas; if the card is null, draw a card-back
     *
     * @param g
     * 		the canvas object
     * @param rect
     * 		a rectangle defining the location to draw the card
     * @param c
     * 		the card to draw; if null, a card-back is drawn
     */
    private static void drawCard(Canvas g, RectF rect, Card c) {
        if (c == null) {
            // null: draw a card-back, consisting of a blue card
            // with a white line near the border. We implement this
            // by drawing 3 concentric rectangles:
            // - blue, full-size
            // - white, slightly smaller
            // - blue, even slightly smaller
            Paint white = new Paint();
            white.setColor(Color.WHITE);
            Paint blue = new Paint();
            blue.setColor(Color.BLUE);
            RectF inner1 = scaledBy(rect, 0.96f); // scaled by 96%
            RectF inner2 = scaledBy(rect, 0.98f); // scaled by 98%
            g.drawRect(rect, blue); // outer rectangle: blue
            g.drawRect(inner2, white); // middle rectangle: white
            g.drawRect(inner1, blue); // inner rectangle: blue
        }
        else {
            // just draw the card
            //drawOn method?
            c.drawOn(g, rect);
        }
    }

    /**
     * scales a rectangle, moving all edges with respect to its center
     *
     * @param rect
     * 		the original rectangle
     * @param factor
     * 		the scaling factor
     * @return
     * 		the scaled rectangle
     */
    private static RectF scaledBy(RectF rect, float factor) {
        // compute the edge locations of the original rectangle, but with
        // the middle of the rectangle moved to the origin
        float midX = (rect.left+rect.right)/2;
        float midY = (rect.top+rect.bottom)/2;
        float left = rect.left-midX;
        float right = rect.right-midX;
        float top = rect.top-midY;
        float bottom = rect.bottom-midY;

        // scale each side; move back so that center is in original location
        left = left*factor + midX;
        right = right*factor + midX;
        top = top*factor + midY;
        bottom = bottom*factor + midY;

        // create/return the new rectangle
        return new RectF(left, top, right, bottom);
    }


    public View getTopView() {
        return myActivity.findViewById(R.id.top_gui_layout);
    }


}
