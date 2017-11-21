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

//

public class FHumanPlayer extends GameHumanPlayer implements Animator {
    private final static float CARD_HEIGHT_PERCENT = 10; // height of a card
    private final static float CARD_WIDTH_PERCENT = 5; // width of a card
    private final static float LEFT_BORDER_PERCENT = 18; // width of left border
    private final static float LEFT_BORDER_PERCENT2 = 10;
    private final static float LEFT_BORDER_SIDE = 5;
    private final static float BUFFER_PERCENT2 = 5;
    private final static float VERTICAL_BORDER_PERCENT_BOTTOMPLAYER = 5; // width of top/bottom borders
    private final static float VERTICAL_BORDER_PERCENT_TOPPLAYER = 70;
    private final static float VERTICAL_BORDER_CENTER_ONE= 20;
    private final static float VERTICAL_BORDER_CENTER_TWO= 40;
    private final static float FLINCH_PILE_HEIGHT = 20;//Flinch pile height (slightly larger than regular card)
    private final static float FLINCH_PILE_WIDTH = 10;//Flinch pile width
    private final static float CENTER_PILE_HEIGHT = 15;
    private final static float CENTER_PILE_WIDTH = 8;
    private final static float FLINCH_BUTTON_WIDTH =10;

    private final static float FLINCH_BUTTON_HEIGHT = 5;

    //array to hold all the rectFs for the placing of the cards
    private RectF[] cardPlace;
    //number of players (decided in configuration screen
    private int getNumPlayers;

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
        activity.setContentView(R.layout.f_human_player);

        // link the animator (this object) to the animation surface
        surface = (AnimationSurface) myActivity
                .findViewById(R.id.animation_surface);
        surface.setAnimator(this);

        // read in the card images
        Card.initImages(activity);

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
        // get the height and width of the animation surface
        // TODO: BELOW
        if(state == null) {
            // don't do things yet
            return;
        }
        int height = surface.getHeight();
        int width = surface.getWidth();
        //if the array is empty will add according to statements below
        if (cardPlace == null ) {
            getNumPlayers = 10 + 6 * state.getNumPlayers() + 5;
            //filling array with the amount of numplayers for RectF
            cardPlace = new RectF[getNumPlayers];
            /*
            depending on how many players there are will depend on how many of the RectFs are drawn, this will
            be dependent on the if statemtns below
             */

            if (state.getNumPlayers() == 2) {
                /*
                placing of cards including flinch pile, cards in hand, and discard pile for human player
                done so by calling method where RectF is actually drawn adding them to an array
                 */
                int counter = 0;
                // draw Bottom Player cards
                cardPlace[counter] = drawBottomPlayerFLINCH();
                counter++;
                cardPlace[counter] = drawBottomCardONE();
                counter++;
                cardPlace[counter] = drawBottomCardTWO();
                counter++;
                cardPlace[counter] = drawBottomCardTHREE();
                counter++;
                cardPlace[counter] = drawBottomCardFOUR();
                counter++;
                cardPlace[counter] = drawBottomCardFIVE();
                counter++;
                cardPlace[counter] = drawBottomDiscardOne();
                counter++;
                cardPlace[counter] = drawBottomDiscardtwo();
                counter++;
                cardPlace[counter] = drawBottomDiscardthree();
                counter++;
                cardPlace[counter] = drawBottomDiscardfour();
                counter++;
                cardPlace[counter] = drawBottomDiscardfive();
                counter++;
                //start of top players cards (5 discard and one flinch pile)
                cardPlace[counter] = drawTopDiscardOne();
                counter++;
                cardPlace[counter] = drawTopDiscardtwo();
                counter++;
                cardPlace[counter] = drawTopDiscardthree();
                counter++;
                cardPlace[counter] = drawTopDiscardfour();
                counter++;
                cardPlace[counter] = drawTopDiscardfive();
                counter++;
                cardPlace[counter] = drawTopFlinch();
                //drawing of center piles
                counter++;
                cardPlace[counter] = drawCenterOne();
                counter++;
                cardPlace[counter] = drawCenterTwo();
                counter++;
                cardPlace[counter] = drawCenterThree();
                counter++;
                cardPlace[counter] = drawCenterFour();
                counter++;
                cardPlace[counter] = drawCenterfive();
                counter++;
                cardPlace[counter] = drawCenterSix();
                counter++;
                cardPlace[counter] = drawCenterSeven();
                counter++;
                cardPlace[counter] = drawCenterEight();
                counter++;
                cardPlace[counter] = drawCenterNine();
                counter++;
                cardPlace[counter] = drawCenterTen();
                counter++;

            } else if (state.getNumPlayers() == 3) {
                int counter = 0;
                // draw Bottom Player cards
                cardPlace[counter] = drawBottomPlayerFLINCH();
                counter++;
                cardPlace[counter] = drawBottomCardONE();
                counter++;
                cardPlace[counter] = drawBottomCardTWO();
                counter++;
                cardPlace[counter] = drawBottomCardTHREE();
                counter++;
                cardPlace[counter] = drawBottomCardFOUR();
                counter++;
                cardPlace[counter] = drawBottomCardFIVE();
                counter++;
                cardPlace[counter] = drawBottomDiscardOne();
                counter++;
                cardPlace[counter] = drawBottomDiscardtwo();
                counter++;
                cardPlace[counter] = drawBottomDiscardthree();
                counter++;
                cardPlace[counter] = drawBottomDiscardfour();
                counter++;
                cardPlace[counter] = drawBottomDiscardfive();

                //draw Top players cards (5 discard and one flinch pile)
                cardPlace[counter] = drawTopDiscardOne();
                counter++;
                cardPlace[counter] = drawTopDiscardtwo();
                counter++;
                cardPlace[counter] = drawTopDiscardthree();
                counter++;
                cardPlace[counter] = drawTopDiscardfour();
                counter++;
                cardPlace[counter] = drawTopDiscardfive();
                counter++;
                cardPlace[counter] = drawTopFlinch();
                counter++;
                //drawing of center Piles
                cardPlace[counter] = drawCenterOne();
                counter++;
                cardPlace[counter] = drawCenterTwo();
                counter++;
                cardPlace[counter] = drawCenterThree();
                counter++;
                cardPlace[counter] = drawCenterFour();
                counter++;
                cardPlace[counter] = drawCenterfive();
                counter++;
                cardPlace[counter] = drawCenterSix();
                counter++;
                cardPlace[counter] = drawCenterSeven();
                counter++;
                cardPlace[counter] = drawCenterEight();
                counter++;
                cardPlace[counter] = drawCenterNine();
                counter++;
                cardPlace[counter] = drawCenterTen();
                counter++;


            } else if (state.getNumPlayers() == 4) {


            }



            // TODO: loop to add center cards, draw center cards (make sure it doesn't crash when the card is null), other players, link the rectF's to the actual cards in the state
        }
        Paint paint = new Paint();
        paint.setColor(Color.rgb(0,102,0));

        // draw the cards

        for(int i = 0; i < 27; i++) {
            //canvas.drawRect(cardPlace[i], paint);
            drawCard(canvas, cardPlace[i], new Card(8))
            ;
        }


        //Card c = state.getDeck(2).peekAtTopCard(); // top card in pile

        //drawCardBacks(canvas, oppTopLocation,
                //0.0025f*width, -0.01f*height, state.getDeck(1-this.playerNum).size());

        // draw my cards, face down
        //RectF thisTopLocation = thisPlayerTopCardLocation(); // drawing size/location
        //drawCardBacks(canvas, thisTopLocation,
                //0.0025f*width, -0.01f*height, state.getDeck(this.playerNum).size());

       /*
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        canvas.drawRect(turnIndicator, paint);*/

    }


    //this will represent the human player's cards
    private RectF drawBottomPlayerFLINCH() {
        int width = surface.getWidth();
        int height = surface.getHeight();
       RectF flinch = new RectF(LEFT_BORDER_PERCENT*width/100f,
                (100-VERTICAL_BORDER_PERCENT_BOTTOMPLAYER-FLINCH_PILE_HEIGHT)*height/100f,
                (LEFT_BORDER_PERCENT+FLINCH_PILE_WIDTH)*width/100f,
               (100-VERTICAL_BORDER_PERCENT_BOTTOMPLAYER) * height/100f);
        return flinch;
    }
    //draws the first card in the players hand
    private RectF drawBottomCardONE () {
        int width = surface.getWidth();
        int height = surface.getHeight();
        RectF one = new RectF ((LEFT_BORDER_PERCENT+FLINCH_PILE_WIDTH+BUFFER_PERCENT2)*width/100f,
                (100-VERTICAL_BORDER_PERCENT_BOTTOMPLAYER-CARD_HEIGHT_PERCENT)*height/100f,
                (LEFT_BORDER_PERCENT+CARD_WIDTH_PERCENT + FLINCH_PILE_WIDTH + BUFFER_PERCENT2)*width/100f,
                (100-VERTICAL_BORDER_PERCENT_BOTTOMPLAYER) * height/100f);
        return one;
    }
    //draws the second card in the players hand
    private RectF drawBottomCardTWO () {
        int width = surface.getWidth();
        int height = surface.getHeight();
        RectF two = new RectF ((LEFT_BORDER_PERCENT+FLINCH_PILE_WIDTH+(BUFFER_PERCENT2 * 2) + CARD_WIDTH_PERCENT)*width/100f,
                (100-VERTICAL_BORDER_PERCENT_BOTTOMPLAYER-CARD_HEIGHT_PERCENT)*height/100f,
                (LEFT_BORDER_PERCENT+FLINCH_PILE_WIDTH+(BUFFER_PERCENT2 * 2) + (CARD_WIDTH_PERCENT * 2))*width/100f,
                (100-VERTICAL_BORDER_PERCENT_BOTTOMPLAYER) * height/100f);
        return two;
    }
    //draws the third card in the players hand
    private RectF drawBottomCardTHREE () {
        int width = surface.getWidth();
        int height = surface.getHeight();
        RectF three = new RectF ((LEFT_BORDER_PERCENT+FLINCH_PILE_WIDTH+(BUFFER_PERCENT2 * 3) + (CARD_WIDTH_PERCENT * 2))*width/100f,
                (100-VERTICAL_BORDER_PERCENT_BOTTOMPLAYER-CARD_HEIGHT_PERCENT)*height/100f,
                (LEFT_BORDER_PERCENT+FLINCH_PILE_WIDTH+(BUFFER_PERCENT2 * 3) + (CARD_WIDTH_PERCENT * 3))*width/100f,
                (100-VERTICAL_BORDER_PERCENT_BOTTOMPLAYER) * height/100f);
        return three;
    }
    //draws the fourth card in the players hand
    private RectF drawBottomCardFOUR () {
        int width = surface.getWidth();
        int height = surface.getHeight();
        RectF four = new RectF ((LEFT_BORDER_PERCENT+FLINCH_PILE_WIDTH+(BUFFER_PERCENT2 * 4) + (CARD_WIDTH_PERCENT * 3))*width/100f,
                (100-VERTICAL_BORDER_PERCENT_BOTTOMPLAYER-CARD_HEIGHT_PERCENT)*height/100f,
                (LEFT_BORDER_PERCENT+FLINCH_PILE_WIDTH+(BUFFER_PERCENT2 * 4) + (CARD_WIDTH_PERCENT * 4))*width/100f,
                (100-VERTICAL_BORDER_PERCENT_BOTTOMPLAYER) * height/100f);
        return four;
    }
    //draws the fifth card in the players hand
    private RectF drawBottomCardFIVE () {
        int width = surface.getWidth();
        int height = surface.getHeight();
        RectF five = new RectF ((LEFT_BORDER_PERCENT+FLINCH_PILE_WIDTH+(BUFFER_PERCENT2 * 5) + (CARD_WIDTH_PERCENT * 4))*width/100f,
                (100-VERTICAL_BORDER_PERCENT_BOTTOMPLAYER-CARD_HEIGHT_PERCENT)*height/100f,
                (LEFT_BORDER_PERCENT+FLINCH_PILE_WIDTH+(BUFFER_PERCENT2 * 5) + (CARD_WIDTH_PERCENT * 5))*width/100f,
                (100-VERTICAL_BORDER_PERCENT_BOTTOMPLAYER) * height/100f);
        return five;

    }
    /*
    Start of drawing for discard piles for human player
     */

    private RectF drawBottomDiscardOne () {

        int width = surface.getWidth();
        int height = surface.getHeight();
        RectF DiscardOne = new RectF ((LEFT_BORDER_PERCENT+FLINCH_PILE_WIDTH+BUFFER_PERCENT2)*width/100f,
                (100-VERTICAL_BORDER_PERCENT_BOTTOMPLAYER-(CARD_HEIGHT_PERCENT*2) - BUFFER_PERCENT2)*height/100f,
                (LEFT_BORDER_PERCENT+CARD_WIDTH_PERCENT + FLINCH_PILE_WIDTH + BUFFER_PERCENT2)*width/100f,
                (100-(VERTICAL_BORDER_PERCENT_BOTTOMPLAYER+CARD_HEIGHT_PERCENT+BUFFER_PERCENT2)) * height/100f);
        return DiscardOne;

    }
    private RectF drawBottomDiscardtwo() {

        int width = surface.getWidth();
        int height = surface.getHeight();
        RectF DiscardTwo = new RectF ((LEFT_BORDER_PERCENT+FLINCH_PILE_WIDTH+(BUFFER_PERCENT2)*2 + (CARD_WIDTH_PERCENT))*width/100f,
                (100-VERTICAL_BORDER_PERCENT_BOTTOMPLAYER-(CARD_HEIGHT_PERCENT*2) - BUFFER_PERCENT2)*height/100f,
                (LEFT_BORDER_PERCENT+FLINCH_PILE_WIDTH+(BUFFER_PERCENT2)*2 + (CARD_WIDTH_PERCENT * 2))*width/100f,
                (100-(VERTICAL_BORDER_PERCENT_BOTTOMPLAYER+CARD_HEIGHT_PERCENT+BUFFER_PERCENT2)) * height/100f);
        return DiscardTwo;

    }

    private RectF drawBottomDiscardthree() {

        int width = surface.getWidth();
        int height = surface.getHeight();
        RectF DiscardThree = new RectF ((LEFT_BORDER_PERCENT+FLINCH_PILE_WIDTH+(BUFFER_PERCENT2)*3 + (CARD_WIDTH_PERCENT * 2))*width/100f,
                (100-VERTICAL_BORDER_PERCENT_BOTTOMPLAYER-(CARD_HEIGHT_PERCENT*2) - BUFFER_PERCENT2)*height/100f,
                (LEFT_BORDER_PERCENT+FLINCH_PILE_WIDTH+(BUFFER_PERCENT2)*3 + (CARD_WIDTH_PERCENT * 3))*width/100f,
                (100-(VERTICAL_BORDER_PERCENT_BOTTOMPLAYER+CARD_HEIGHT_PERCENT+BUFFER_PERCENT2)) * height/100f);
        return DiscardThree;

    }

    private RectF drawBottomDiscardfour() {

        int width = surface.getWidth();
        int height = surface.getHeight();
        RectF DiscardFour = new RectF ((LEFT_BORDER_PERCENT+FLINCH_PILE_WIDTH+(BUFFER_PERCENT2)*4 + (CARD_WIDTH_PERCENT * 3))*width/100f,
                (100-VERTICAL_BORDER_PERCENT_BOTTOMPLAYER-(CARD_HEIGHT_PERCENT*2) - BUFFER_PERCENT2)*height/100f,
                (LEFT_BORDER_PERCENT+FLINCH_PILE_WIDTH+(BUFFER_PERCENT2)*4 + (CARD_WIDTH_PERCENT * 4))*width/100f,
                (100-(VERTICAL_BORDER_PERCENT_BOTTOMPLAYER+CARD_HEIGHT_PERCENT+BUFFER_PERCENT2)) * height/100f);
        return DiscardFour;

    }

    private RectF drawBottomDiscardfive() {

        int width = surface.getWidth();
        int height = surface.getHeight();
        RectF DiscardFive = new RectF ((LEFT_BORDER_PERCENT+FLINCH_PILE_WIDTH+(BUFFER_PERCENT2)*5 + (CARD_WIDTH_PERCENT * 4))*width/100f,
                (100-VERTICAL_BORDER_PERCENT_BOTTOMPLAYER-(CARD_HEIGHT_PERCENT*2) - BUFFER_PERCENT2)*height/100f,
                (LEFT_BORDER_PERCENT+FLINCH_PILE_WIDTH+(BUFFER_PERCENT2)*5 + (CARD_WIDTH_PERCENT * 5))*width/100f,
                (100-(VERTICAL_BORDER_PERCENT_BOTTOMPLAYER+CARD_HEIGHT_PERCENT+BUFFER_PERCENT2)) * height/100f);
        return DiscardFive;

    }

   /* private RectF drawFlinchButton() {
        int width = surface.getWidth();
        int height = surface.getHeight();
        RectF FlinchButton = new RectF (BUFFER_PERCENT_FLINCH_BUTTON *width/100f,
                (100-VERTICAL_BORDER_PERCENT_BOTTOMPLAYER-CARD_HEIGHT_PERCENT-5)*height/100f,
                (BUFFER_PERCENT_FLINCH_BUTTON+FLINCH_BUTTON_WIDTH)*width/100f,
                (100-(VERTICAL_BORDER_PERCENT_BOTTOMPLAYER+CARD_HEIGHT_PERCENT+15)) * height /100f);
        return FlinchButton;
    }*/

    private RectF drawTopDiscardOne () {

        int width = surface.getWidth();
        int height = surface.getHeight();
        RectF DiscardTopOne = new RectF ((LEFT_BORDER_PERCENT2+FLINCH_PILE_WIDTH+BUFFER_PERCENT2)*width/100f,
                (100-VERTICAL_BORDER_PERCENT_TOPPLAYER-(CARD_HEIGHT_PERCENT*2) - BUFFER_PERCENT2)*height/100f,
                (LEFT_BORDER_PERCENT2+CARD_WIDTH_PERCENT + FLINCH_PILE_WIDTH + BUFFER_PERCENT2)*width/100f,
                (100-(VERTICAL_BORDER_PERCENT_TOPPLAYER+CARD_HEIGHT_PERCENT+BUFFER_PERCENT2)) * height/100f);
        return DiscardTopOne;

    }
    private RectF drawTopDiscardtwo() {

        int width = surface.getWidth();
        int height = surface.getHeight();
        RectF DiscardTwoTop = new RectF ((LEFT_BORDER_PERCENT2+FLINCH_BUTTON_WIDTH+(BUFFER_PERCENT2)*2 + (CARD_WIDTH_PERCENT))*width/100f,
                (100-VERTICAL_BORDER_PERCENT_TOPPLAYER-(CARD_HEIGHT_PERCENT*2) - BUFFER_PERCENT2)*height/100f,
                (LEFT_BORDER_PERCENT2+FLINCH_PILE_WIDTH+(BUFFER_PERCENT2)*2 + (CARD_WIDTH_PERCENT * 2))*width/100f,
                (100-(VERTICAL_BORDER_PERCENT_TOPPLAYER+CARD_HEIGHT_PERCENT+BUFFER_PERCENT2)) * height/100f);
        return DiscardTwoTop;

    }

    private RectF drawTopDiscardthree() {

        int width = surface.getWidth();
        int height = surface.getHeight();
        RectF DiscardThreeTop = new RectF ((LEFT_BORDER_PERCENT2+FLINCH_PILE_WIDTH+(BUFFER_PERCENT2)*3 + (CARD_WIDTH_PERCENT * 2))*width/100f,
                (100-VERTICAL_BORDER_PERCENT_TOPPLAYER-(CARD_HEIGHT_PERCENT*2) - BUFFER_PERCENT2)*height/100f,
                (LEFT_BORDER_PERCENT2+FLINCH_PILE_WIDTH+(BUFFER_PERCENT2)*3 + (CARD_WIDTH_PERCENT * 3))*width/100f,
                (100-(VERTICAL_BORDER_PERCENT_TOPPLAYER+CARD_HEIGHT_PERCENT+BUFFER_PERCENT2)) * height/100f);
        return DiscardThreeTop;

    }

    private RectF drawTopDiscardfour() {

        int width = surface.getWidth();
        int height = surface.getHeight();
        RectF DiscardFourTop = new RectF ((LEFT_BORDER_PERCENT2+FLINCH_PILE_WIDTH+(BUFFER_PERCENT2)*4 + (CARD_WIDTH_PERCENT * 3))*width/100f,
                (100-VERTICAL_BORDER_PERCENT_TOPPLAYER-(CARD_HEIGHT_PERCENT*2) - BUFFER_PERCENT2)*height/100f,
                (LEFT_BORDER_PERCENT2+FLINCH_PILE_WIDTH+(BUFFER_PERCENT2)*4 + (CARD_WIDTH_PERCENT * 4))*width/100f,
                (100-(VERTICAL_BORDER_PERCENT_TOPPLAYER+CARD_HEIGHT_PERCENT+BUFFER_PERCENT2)) * height/100f);
        return DiscardFourTop;

    }

    private RectF drawTopDiscardfive() {

        int width = surface.getWidth();
        int height = surface.getHeight();
        RectF DiscardFiveTop = new RectF ((LEFT_BORDER_PERCENT2+FLINCH_PILE_WIDTH+(BUFFER_PERCENT2)*5 + (CARD_WIDTH_PERCENT * 4))*width/100f,
                (100-VERTICAL_BORDER_PERCENT_TOPPLAYER-(CARD_HEIGHT_PERCENT*2) - BUFFER_PERCENT2)*height/100f,
                (LEFT_BORDER_PERCENT2+FLINCH_PILE_WIDTH+(BUFFER_PERCENT2)*5 + (CARD_WIDTH_PERCENT * 5))*width/100f,
                (100-(VERTICAL_BORDER_PERCENT_TOPPLAYER+CARD_HEIGHT_PERCENT+BUFFER_PERCENT2)) * height/100f);
        return DiscardFiveTop;

    }
    private RectF drawTopFlinch() {

        int width = surface.getWidth();
        int height = surface.getHeight();
        RectF FlinchTop = new RectF ((LEFT_BORDER_PERCENT2+FLINCH_PILE_WIDTH+(BUFFER_PERCENT2)*6 + (CARD_WIDTH_PERCENT * 5))*width/100f,
                (100-VERTICAL_BORDER_PERCENT_TOPPLAYER-(CARD_HEIGHT_PERCENT*2) - BUFFER_PERCENT2)*height/100f,
                (LEFT_BORDER_PERCENT2+FLINCH_PILE_WIDTH+(BUFFER_PERCENT2)*6 + (CARD_WIDTH_PERCENT * 6))*width/100f,
                (100-(VERTICAL_BORDER_PERCENT_TOPPLAYER+CARD_HEIGHT_PERCENT+BUFFER_PERCENT2)) * height/100f);
        return FlinchTop;

    }
    private RectF drawCenterOne () {

        int width = surface.getWidth();
        int height = surface.getHeight();
        RectF CenterOne = new RectF ((LEFT_BORDER_PERCENT+BUFFER_PERCENT2)*width/100f,
                (100-VERTICAL_BORDER_CENTER_ONE-(CENTER_PILE_HEIGHT*2) - BUFFER_PERCENT2)*height/100f,
                (LEFT_BORDER_PERCENT+CENTER_PILE_WIDTH + BUFFER_PERCENT2)*width/100f,
                (100-(VERTICAL_BORDER_CENTER_ONE+CENTER_PILE_HEIGHT+BUFFER_PERCENT2)) * height/100f);
        return CenterOne;

    }
    private RectF drawCenterTwo() {

        int width = surface.getWidth();
        int height = surface.getHeight();
        RectF CenterTwo = new RectF ((LEFT_BORDER_PERCENT+(BUFFER_PERCENT2)*2 + (CENTER_PILE_WIDTH))*width/100f,
                (100-VERTICAL_BORDER_CENTER_ONE-(CENTER_PILE_HEIGHT*2) - BUFFER_PERCENT2)*height/100f,
                (LEFT_BORDER_PERCENT+(BUFFER_PERCENT2)*2 + (CENTER_PILE_WIDTH * 2))*width/100f,
                (100-(VERTICAL_BORDER_CENTER_ONE+CENTER_PILE_HEIGHT+BUFFER_PERCENT2)) * height/100f);
        return CenterTwo;

    }

    private RectF drawCenterThree() {

        int width = surface.getWidth();
        int height = surface.getHeight();
        RectF CenterThree = new RectF ((LEFT_BORDER_PERCENT+(BUFFER_PERCENT2)*3 + (CENTER_PILE_WIDTH * 2))*width/100f,
                (100-VERTICAL_BORDER_CENTER_ONE-(CENTER_PILE_HEIGHT*2) - BUFFER_PERCENT2)*height/100f,
                (LEFT_BORDER_PERCENT+(BUFFER_PERCENT2)*3 + (CENTER_PILE_WIDTH * 3))*width/100f,
                (100-(VERTICAL_BORDER_CENTER_ONE+CENTER_PILE_HEIGHT+BUFFER_PERCENT2)) * height/100f);
        return CenterThree;

    }

    private RectF drawCenterFour() {

        int width = surface.getWidth();
        int height = surface.getHeight();
        RectF CenterFour = new RectF ((LEFT_BORDER_PERCENT+(BUFFER_PERCENT2)*4 + (CENTER_PILE_WIDTH * 3))*width/100f,
                (100-VERTICAL_BORDER_CENTER_ONE-(CENTER_PILE_HEIGHT*2) - BUFFER_PERCENT2)*height/100f,
                (LEFT_BORDER_PERCENT+(BUFFER_PERCENT2)*4 + (CENTER_PILE_WIDTH * 4))*width/100f,
                (100-(VERTICAL_BORDER_CENTER_ONE+CENTER_PILE_HEIGHT+BUFFER_PERCENT2)) * height/100f);
        return CenterFour;

    }

    private RectF drawCenterfive() {

        int width = surface.getWidth();
        int height = surface.getHeight();
        RectF CenterFive = new RectF ((LEFT_BORDER_PERCENT+(BUFFER_PERCENT2)*5 + (CENTER_PILE_WIDTH * 4))*width/100f,
                (100-VERTICAL_BORDER_CENTER_ONE-(CENTER_PILE_HEIGHT*2) - BUFFER_PERCENT2)*height/100f,
                (LEFT_BORDER_PERCENT+(BUFFER_PERCENT2)*5 + (CENTER_PILE_WIDTH * 5))*width/100f,
                (100-(VERTICAL_BORDER_CENTER_ONE+CENTER_PILE_HEIGHT+BUFFER_PERCENT2)) * height/100f);
        return CenterFive;

    }

    private RectF drawCenterSix () {

        int width = surface.getWidth();
        int height = surface.getHeight();
        RectF CenterSix = new RectF ((LEFT_BORDER_PERCENT+BUFFER_PERCENT2)*width/100f,
                (100-VERTICAL_BORDER_CENTER_TWO-(CENTER_PILE_HEIGHT*2) - BUFFER_PERCENT2)*height/100f,
                (LEFT_BORDER_PERCENT+CENTER_PILE_WIDTH + BUFFER_PERCENT2)*width/100f,
                (100-(VERTICAL_BORDER_CENTER_TWO+CENTER_PILE_HEIGHT+BUFFER_PERCENT2)) * height/100f);
        return CenterSix;

    }
    private RectF drawCenterSeven() {

        int width = surface.getWidth();
        int height = surface.getHeight();
        RectF CenterSeven = new RectF ((LEFT_BORDER_PERCENT+(BUFFER_PERCENT2)*2 + (CENTER_PILE_WIDTH))*width/100f,
                (100-VERTICAL_BORDER_CENTER_TWO-(CENTER_PILE_HEIGHT*2) - BUFFER_PERCENT2)*height/100f,
                (LEFT_BORDER_PERCENT+(BUFFER_PERCENT2)*2 + (CENTER_PILE_WIDTH * 2))*width/100f,
                (100-(VERTICAL_BORDER_CENTER_TWO+CENTER_PILE_HEIGHT+BUFFER_PERCENT2)) * height/100f);
        return CenterSeven;

    }

    private RectF drawCenterEight() {

        int width = surface.getWidth();
        int height = surface.getHeight();
        RectF CenterEight = new RectF ((LEFT_BORDER_PERCENT+(BUFFER_PERCENT2)*3 + (CENTER_PILE_WIDTH * 2))*width/100f,
                (100-VERTICAL_BORDER_CENTER_TWO-(CENTER_PILE_HEIGHT*2) - BUFFER_PERCENT2)*height/100f,
                (LEFT_BORDER_PERCENT+(BUFFER_PERCENT2)*3 + (CENTER_PILE_WIDTH * 3))*width/100f,
                (100-(VERTICAL_BORDER_CENTER_TWO+CENTER_PILE_HEIGHT+BUFFER_PERCENT2)) * height/100f);
        return CenterEight;

    }

    private RectF drawCenterNine() {

        int width = surface.getWidth();
        int height = surface.getHeight();
        RectF CenterNine = new RectF ((LEFT_BORDER_PERCENT+(BUFFER_PERCENT2)*4 + (CENTER_PILE_WIDTH * 3))*width/100f,
                (100-VERTICAL_BORDER_CENTER_TWO-(CENTER_PILE_HEIGHT*2) - BUFFER_PERCENT2)*height/100f,
                (LEFT_BORDER_PERCENT+(BUFFER_PERCENT2)*4 + (CENTER_PILE_WIDTH * 4))*width/100f,
                (100-(VERTICAL_BORDER_CENTER_TWO+CENTER_PILE_HEIGHT+BUFFER_PERCENT2)) * height/100f);
        return CenterNine;

    }

    private RectF drawCenterTen() {

        int width = surface.getWidth();
        int height = surface.getHeight();
        RectF CenterTen = new RectF ((LEFT_BORDER_PERCENT+(BUFFER_PERCENT2)*5 + (CENTER_PILE_WIDTH * 4))*width/100f,
                (100-VERTICAL_BORDER_CENTER_TWO-(CENTER_PILE_HEIGHT*2) - BUFFER_PERCENT2)*height/100f,
                (LEFT_BORDER_PERCENT+(BUFFER_PERCENT2)*5 + (CENTER_PILE_WIDTH * 5))*width/100f,
                (100-(VERTICAL_BORDER_CENTER_TWO+CENTER_PILE_HEIGHT+BUFFER_PERCENT2)) * height/100f);
        return CenterTen;

    }


    //LEFT PLAYER
    //STILL NEEDS TO BE COMPLETED


    private RectF drawLeftDiscardOne () {

        int width = surface.getWidth();
        int height = surface.getHeight();
        RectF DiscardLeftOne = new RectF ((LEFT_BORDER_SIDE)*height/100f,
                (100-VERTICAL_BORDER_PERCENT_BOTTOMPLAYER+FLINCH_PILE_WIDTH+BUFFER_PERCENT2)*width/100f,
                ((LEFT_BORDER_SIDE+CARD_HEIGHT_PERCENT+BUFFER_PERCENT2)) * height/100f ,
                (100-VERTICAL_BORDER_PERCENT_BOTTOMPLAYER)*width/100f);
        return DiscardLeftOne;

    }

    private RectF drawLeftDiscardTwo () {

        int width = surface.getWidth();
        int height = surface.getHeight();
        RectF DiscardLeftTwo = new RectF ((LEFT_BORDER_PERCENT2+FLINCH_PILE_WIDTH+BUFFER_PERCENT2)*width/100f,
                (100-VERTICAL_BORDER_PERCENT_TOPPLAYER-(CARD_HEIGHT_PERCENT*2) - BUFFER_PERCENT2)*height/100f,
                (LEFT_BORDER_PERCENT2+CARD_WIDTH_PERCENT + FLINCH_PILE_WIDTH + BUFFER_PERCENT2)*width/100f,
                (100-(VERTICAL_BORDER_PERCENT_TOPPLAYER+CARD_HEIGHT_PERCENT+BUFFER_PERCENT2)) * height/100f);
        return DiscardLeftTwo;

    }




    private RectF drawLeftDiscardthree() {

        int width = surface.getWidth();
        int height = surface.getHeight();
        RectF DiscardLeftThree = new RectF ((LEFT_BORDER_PERCENT+FLINCH_PILE_WIDTH+(BUFFER_PERCENT2)*3 + (CARD_WIDTH_PERCENT * 2))*width/100f,
                (100-VERTICAL_BORDER_PERCENT_BOTTOMPLAYER-(CARD_HEIGHT_PERCENT*2) - BUFFER_PERCENT2)*height/100f,
                (LEFT_BORDER_PERCENT+FLINCH_PILE_WIDTH+(BUFFER_PERCENT2)*3 + (CARD_WIDTH_PERCENT * 3))*width/100f,
                (100-(VERTICAL_BORDER_PERCENT_BOTTOMPLAYER+CARD_HEIGHT_PERCENT+BUFFER_PERCENT2)) * height/100f);
        return DiscardLeftThree;

    }

    private RectF drawLeftDiscardfour() {

        int width = surface.getWidth();
        int height = surface.getHeight();
        RectF DiscardLeftFour = new RectF ((LEFT_BORDER_PERCENT+FLINCH_PILE_WIDTH+(BUFFER_PERCENT2)*4 + (CARD_WIDTH_PERCENT * 3))*width/100f,
                (100-VERTICAL_BORDER_PERCENT_BOTTOMPLAYER-(CARD_HEIGHT_PERCENT*2) - BUFFER_PERCENT2)*height/100f,
                (LEFT_BORDER_PERCENT+FLINCH_PILE_WIDTH+(BUFFER_PERCENT2)*4 + (CARD_WIDTH_PERCENT * 4))*width/100f,
                (100-(VERTICAL_BORDER_PERCENT_BOTTOMPLAYER+CARD_HEIGHT_PERCENT+BUFFER_PERCENT2)) * height/100f);
        return DiscardLeftFour;

    }

    private RectF drawLeftDiscardfive() {

        int width = surface.getWidth();
        int height = surface.getHeight();
        RectF DiscardLeftFive = new RectF ((LEFT_BORDER_PERCENT+FLINCH_PILE_WIDTH+(BUFFER_PERCENT2)*5 + (CARD_WIDTH_PERCENT * 4))*width/100f,
                (100-VERTICAL_BORDER_PERCENT_BOTTOMPLAYER-(CARD_HEIGHT_PERCENT*2) - BUFFER_PERCENT2)*height/100f,
                (LEFT_BORDER_PERCENT+FLINCH_PILE_WIDTH+(BUFFER_PERCENT2)*5 + (CARD_WIDTH_PERCENT * 5))*width/100f,
                (100-(VERTICAL_BORDER_PERCENT_BOTTOMPLAYER+CARD_HEIGHT_PERCENT+BUFFER_PERCENT2)) * height/100f);
        return DiscardLeftFive;

    }

    private RectF drawLeftFlinch() {

        int width = surface.getWidth();
        int height = surface.getHeight();
        RectF DiscardLeftFlinch = new RectF ((LEFT_BORDER_PERCENT+FLINCH_PILE_WIDTH+(BUFFER_PERCENT2)*5 + (CARD_WIDTH_PERCENT * 4))*width/100f,
                (100-VERTICAL_BORDER_PERCENT_BOTTOMPLAYER-(CARD_HEIGHT_PERCENT*2) - BUFFER_PERCENT2)*height/100f,
                (LEFT_BORDER_PERCENT+FLINCH_PILE_WIDTH+(BUFFER_PERCENT2)*5 + (CARD_WIDTH_PERCENT * 5))*width/100f,
                (100-(VERTICAL_BORDER_PERCENT_BOTTOMPLAYER+CARD_HEIGHT_PERCENT+BUFFER_PERCENT2)) * height/100f);
        return DiscardLeftFlinch;

    }


    public void onTouch(MotionEvent event) {
        // ignore everything except down-touch events
        if (event.getAction() != MotionEvent.ACTION_DOWN) return;

        // get the location of the touch on the surface
        int x = (int) event.getX();
        int y = (int) event.getY();

        // determine whether the touch occurred on the top-card of either
        // the player's pile or the middle pile
        RectF FlinchPile =drawBottomPlayerFLINCH();
        RectF Card1 = drawBottomCardONE();
        RectF Card2 = drawBottomCardTWO();
        RectF Card3 = drawBottomCardTHREE();
        RectF Card4 = drawBottomCardFOUR();
        RectF Card5 = drawBottomCardFIVE();

        if (FlinchPile.contains(x, y)) {
            // it's on my pile: we're playing a card: send action to
            // the game
            //classes need to be implemented
            int x1 = (int) event.getX();
            int y1 = (int) event.getY();




            game.sendAction(new FFlinchAction(this));
        }
        else if (Card1.contains(x, y)) {
            // check if it is a card in your hand
            // action to the game
            //classes need to be implemented
            int x1 = (int) event.getX();
            int y1 = (int) event.getY();

           game.sendAction(new FFlinchAction(this));
        }
        else if(Card1.contains(x,y)) {
            // check if it is card in your hand
            // action to the game
            //classes need to be implemented
            game.sendAction(new FFlinchAction(this));

        }
        else if (Card2.contains(x,y)) {
            //check if it is a card in your hand

            game.sendAction(new FFlinchAction(this));
        }
        else if(Card3.contains(x,y)) {
            game.sendAction(new FFlinchAction(this));

        }
        else if(Card4.contains(x,y)){

            game.sendAction(new FFlinchAction(this));
        }
        else if(Card5.contains(x,y)) {
            game.sendAction(new FFlinchAction(this));
        }
        else{
            // illegal touch-location: flash for 1/20 second
            surface.flash(Color.RED, 50);
        }

    }
    /*private void drawCardBacks(Canvas g, RectF topRect, float deltaX, float deltaY,
                               int numCards) {
        // loop through from back to front, drawing a card-back in each location
        for (int i = numCards-1; i >= 0; i--) {
            // determine theh position of this card's top/left corner
            float left = topRect.left + i*deltaX;
            float top = topRect.top + i*deltaY;
            // draw a card-back (hence null) into the appropriate rectangle
            drawCard(g,
                    new RectF(left, top, left + topRect.width(), top + topRect.height()),
                    null);
        }
    }*/

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
