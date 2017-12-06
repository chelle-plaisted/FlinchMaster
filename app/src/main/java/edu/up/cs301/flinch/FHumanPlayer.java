package edu.up.cs301.flinch;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.HashMap;

import edu.up.cs301.animation.AnimationSurface;
import edu.up.cs301.animation.Animator;
import edu.up.cs301.card.Card;
import edu.up.cs301.cardpile.DiscardPile;
import edu.up.cs301.cardpile.FlinchPile;
import edu.up.cs301.cardpile.Hand;
import edu.up.cs301.flinch.FStateElements.FState;
import edu.up.cs301.game.GameHumanPlayer;
import edu.up.cs301.game.GameMainActivity;
import edu.up.cs301.game.R;
import edu.up.cs301.game.infoMsg.GameInfo;
import edu.up.cs301.game.infoMsg.IllegalMoveInfo;
import edu.up.cs301.game.infoMsg.NotYourTurnInfo;
import edu.up.cs301.game.util.MessageBox;

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
    private final static float VERTICAL_BORDER_PERCENT_2 = 10;
    private final static float VERTICAL_BORDER_PERCENT_TOPPLAYER = 70;
    private final static float VERTICAL_CENTERONE = VERTICAL_BORDER_PERCENT_BOTTOMPLAYER + 10 + (CARD_HEIGHT_PERCENT * 2);
    private final static float VERTICAL_CENTERTWO = 20;
    private final static float CENTER_HEIGHT = 15;
    private final static float CENTER_WIDTH  = 8 ;
    private final static float FLINCH_PILE_HEIGHT = 20;//Flinch pile height (slightly larger than regular card)
    private final static float FLINCH_PILE_WIDTH = 10;//Flinch pile width
    private final static float FLINCH_BUTTON_WIDTH =10;
    private final static float FLINCH_BUTTON_HEIGHT = 7;
    private final static float FLINCH_TOP_HEIGHT = 13;
    private final static float FLINCH_TOP_WIDTH = 8;
    private final static float VERTICAL_BORDER_PERCENT_LEFT = 25;
    private final static float RIGHT_BORDER_SIDE = 95;


    // TEMP
    RectF flinchRect;
    // non-card icons
    private Bitmap[] icons;

    // are we in play mode
    private boolean inPlayMode;

    // keeps track of which player in terms of display (bottom, left, top, right) whose turn it is
    HashMap<Integer, RectF> turnTracker;

    private RectF[] buttons;

    //array to hold all the rectFs for the placing of the cards
    private RectF[] cardPlace;
    //array to hold actual cards
    private int[] toDraw;
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

    // the index of the player's selected card within cardPlace or toDraw
    private int selected;

    // rectF to represent the currently selected card
    private RectF selectIndicator;

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
        selected = -1;
        inPlayMode = true; // assume we start in play mode
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
        if (info instanceof IllegalMoveInfo) {
            // if we had an out-of-turn or illegal move, flash the screen blue
            surface.flash(Color.rgb(102,178,255), 50);
        } else if(info instanceof NotYourTurnInfo) {
            surface.flash(Color.GREEN, 50);
        }
        else if (!(info instanceof FState)) {
            // otherwise, if it's not a game-state message, ignore
            return;
        }
        else {
            // it's a game-state object: update the state. Since we have an animation
            // going, there is no need to explicitly display anything. That will happen
            // at the next animation-tick, which should occur within 1/20 of a second
            this.state = (FState) info;

            Log.i("human player", "receiving");

            // do we have a flinch message?\
            int[] messages = state.getFlinchMessageTracker();
            if(messages[0] == 1) {
                 MessageBox.popUpMessage(this.allPlayerNames[messages[1]] + " got Flinched by " + this.allPlayerNames[messages[2]], myActivity);
                state.retireFlinchMessage();
            }


            //update cards for all the players
            if (toDraw == null) {
                return;
            }
            int counter = 0;
            int player = this.playerNum;
            // get Bottom Player cards
            counter = getBottomCards(counter, player);
            player++; // increment the player
            if (player >= state.getNumPlayers()) {
                player = 0;
            }
            if (state.getNumPlayers() == 2) {
                /*
                placing of cards including flinch pile, cards in hand, and discard pile for human player
                done so by calling method where RectF is actually drawn adding them to an array
                 */
                counter = getPlayerCards(counter, player);

            } else if (state.getNumPlayers() == 3) {

                //draw left player's cards (5 discard and one flinch pile)
                counter = getPlayerCards(counter, player);
                player++;
                if (player >= state.getNumPlayers()) {
                    player = 0;
                }

                //draw right player's cards (5 discard and one flinch pile)
                counter = getPlayerCards(counter, player);

            } else if (state.getNumPlayers() == 4) {
                //draw Right player's cards (5 discard and one flinch pile)
                counter = getPlayerCards(counter, player);
                player++;
                if (player >= state.getNumPlayers()) {
                    player = 0;
                }

                //draw Left player's cards (5 discard and one flinch pile)
                counter = getPlayerCards(counter, player);
                player++;
                if (player >= state.getNumPlayers()) {
                    player = 0;
                }

                //draw Left player's cards (5 discard and one flinch pile)
                counter = getPlayerCards(counter, player);
            }
            // get center cards
            counter = getCenterCards(counter);
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

        // read in the buttons
        icons = new Bitmap[3];
        icons[0] = BitmapFactory.decodeResource(
                activity.getResources(),
                R.drawable.flinch_button);
        icons[1] = BitmapFactory.decodeResource(
                activity.getResources(),
                R.drawable.playbutton);
        icons[2] = BitmapFactory.decodeResource(
                activity.getResources(),
                R.drawable.discardbutton);
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
            // turn tracking values
            RectF LEFT = new RectF(0, 0, (2 * width) / 100f, height);
            RectF RIGHT  = new RectF((98 * width) / 100f, 0, width, height);
            RectF TOP = new RectF(0, 0, width, (2 * height) / 100f);
            RectF BOTTOM  = new RectF(0, (98) * height / 100f, width, height);

            getNumPlayers = 10 + 6 * state.getNumPlayers() + 5;
            //filling array with the amount of numplayers for RectF
            cardPlace = new RectF[getNumPlayers];
            toDraw = new int[getNumPlayers];

            // for turn tracking
            turnTracker = new HashMap<Integer, RectF>();
            turnTracker.put(new Integer(this.playerNum), BOTTOM); // HUMAN PLAYER IS ALWAYS ON THE BOTTOM

            /*
            depending on how many players there are will depend on how many of the RectFs are drawn, this will
            be dependent on the if statements below
             */
            int counter1 = 0;
            int counter2 = 0;
            int player = this.playerNum;

            if (state.getNumPlayers() == 2) { // TWO PLAYERS
                /*
                placing of cards including flinch pile, cards in hand, and discard pile for human player
                done so by calling method where RectF is actually drawn adding them to an array
                 */

                // draw Bottom Player cards-- HUMAN PLAYER

                counter1 = getBottomCardLocs(counter1, player);
                counter2 = getBottomCards(counter2, player);

                //start of top players cards (5 discard and one flinch pile)
                player++;
                if (player >= state.getNumPlayers()) {
                    player = 0;
                }
                // get their position
                turnTracker.put(new Integer(player), TOP);

                counter1 = getTopCardLocs(counter1, player);
                counter2 = getPlayerCards(counter2, player);

            } else if (state.getNumPlayers() == 3) {
                // draw Bottom Player cards
                counter1 = getBottomCardLocs(counter1, player);
                counter2 = getBottomCards(counter2, player);
                player++;
                if (player >= state.getNumPlayers()) {
                    player = 0;
                }


                //draw Left player's cards (5 discard and one flinch pile)
                counter1 = getLeftCardLocs(counter1, player);
                counter2 = getPlayerCards(counter2, player);
                turnTracker.put(new Integer(player), LEFT);


                player++;
                if (player >= state.getNumPlayers()) {
                    player = 0;
                }

                //draw Right player's cards (5 discard and one flinch pile)
                counter1 = getRightLocs(counter1, player);
                counter2 = getPlayerCards(counter2, player);
                turnTracker.put(new Integer(player), RIGHT);

            } else if (state.getNumPlayers() == 4) {
                // draw Bottom Player cards
                counter1 = getBottomCardLocs(counter1, player);
                counter2 = getBottomCards(counter2, player);
                player++;
                if (player >= state.getNumPlayers()) {
                    player = 0;
                }

                //draw Left player's cards (5 discard and one flinch pile)
                counter1 = getLeftCardLocs(counter1, player);
                counter2 = getPlayerCards(counter2, player);
                turnTracker.put(new Integer(player), LEFT);
                player++;
                if (player >= state.getNumPlayers()) {
                    player = 0;
                }

                //draw Top player's cards (5 discard and one flinch pile)
                counter1 = getTopCardLocs(counter1, player);
                counter2 = getPlayerCards(counter2, player);
                turnTracker.put(new Integer(player), TOP);
                player++;
                if (player >= state.getNumPlayers()) {
                    player = 0;
                }

                //draw Right player's cards (5 discard and one flinch pile)
                counter1 = getRightLocs(counter1, player);
                counter2 = getPlayerCards(counter2, player);
                turnTracker.put(new Integer(player), RIGHT);


            }
            // add the center cards
            counter1 = getCenterCardsLocs(counter1);
            counter2 = getCenterCards(counter2);

            // add buttons
            buttons = new RectF[5];
                /* POSITIONS:
                        0: Flinch button
                        1: toggle play
                        2: toggle discard
                 */
            setUpButtons();

            // TODO: loop to add center cards, draw center cards (make sure it doesn't crash when the card is null), other players, link the rectF's to the actual cards in the state
        } // setup completed
        Paint paint = new Paint();
        paint.setColor(Color.rgb(181, 205, 255));

        // draw the cards

        for (int i = 0; i < getNumPlayers; i++) {
            if (toDraw[i] < 1) {
                // there is no card to draw here, just draw a rectangle
                if (cardPlace[i] != null) {
                    canvas.drawRect(cardPlace[i], paint);
                    continue;
                }
            }
            if (cardPlace[i] != null) {
                drawCard(canvas, cardPlace[i], new Card(toDraw[i]));
            }
        }

        // if it is the player's turn, draw what card they have selected
        /* EXTRA GUI ELEMENTS BELOW */
        // if it is the player's turn, indicate what card was selected and show the card indicator
        // otherwise, draw the flinch button;
        RectF turnIndicator;
        // if it is the player's turn, draw what card they have selected
        paint.setColor(Color.BLUE);

        if(this.playerNum == state.getWhoseTurn() ) {
            // draw a blue line to indicate what card was selected
            selectIndicator = getSelectRect();
            if (selectIndicator != null) {
                canvas.drawRect(selectIndicator, paint);
            }

            // TOGGLE BUTTON
                // which button is selected
            paint.setColor(Color.rgb(255, 153,153));
            if(inPlayMode) {
                // highlight the play Mode
                canvas.drawRect(buttons[3], paint);
            } else {
                //highlight the discard Mode
                canvas.drawRect(buttons[4], paint);
            }
            drawToggleButtons(canvas);

        } else  {
            // IT IS NOT MY TURN
            // FLINCH Button
            paint.setColor(Color.rgb(255, 153, 153));
            drawFlinchButton(canvas);

         }

         // DRAW WHOSE TURN IT IS
        turnIndicator = turnTracker.get(state.getWhoseTurn());

        paint.setColor(Color.BLUE);
        if(turnIndicator != null) canvas.drawRect(turnIndicator, paint);



    }

    /**
     *  Draws flinch button, discard mode button, play mode button
     */
    private void setUpButtons() {
        int width = surface.getWidth();
        int height = surface.getHeight();
        // flinch button
        buttons[0] =  new RectF ((LEFT_BORDER_PERCENT+FLINCH_PILE_WIDTH +(CARD_WIDTH_PERCENT*5)+(BUFFER_PERCENT2)*6 )*width/100f,
                (100-VERTICAL_BORDER_PERCENT_BOTTOMPLAYER-(FLINCH_BUTTON_HEIGHT*2) - BUFFER_PERCENT2)*height/100f,
                (LEFT_BORDER_PERCENT+FLINCH_PILE_WIDTH+(BUFFER_PERCENT2*6) +(CARD_WIDTH_PERCENT*5)+ (FLINCH_BUTTON_WIDTH))*width/100f,
                (100-(VERTICAL_BORDER_PERCENT_BOTTOMPLAYER+FLINCH_BUTTON_HEIGHT+BUFFER_PERCENT2)) * height/100f);
        // botton toggle: play
        buttons[1] = (new RectF ((LEFT_BORDER_PERCENT+FLINCH_PILE_WIDTH +(CARD_WIDTH_PERCENT*5)+(BUFFER_PERCENT2)*6 )*width/100f,
                (100-VERTICAL_BORDER_PERCENT_BOTTOMPLAYER-(FLINCH_BUTTON_HEIGHT*2) - BUFFER_PERCENT2)*height/100f,
                (LEFT_BORDER_PERCENT+FLINCH_PILE_WIDTH+(BUFFER_PERCENT2*6) +(CARD_WIDTH_PERCENT*5)+ (FLINCH_BUTTON_WIDTH))*width/100f,
                (100-(VERTICAL_BORDER_PERCENT_BOTTOMPLAYER+FLINCH_BUTTON_HEIGHT+BUFFER_PERCENT2)) * height/100f));
        // bottom toggle: discard
        buttons[2] = (new RectF ((LEFT_BORDER_PERCENT+FLINCH_PILE_WIDTH+(BUFFER_PERCENT2 * 6)+(CARD_WIDTH_PERCENT*5))*width/100f,
                (100-VERTICAL_BORDER_PERCENT_BOTTOMPLAYER-(FLINCH_BUTTON_HEIGHT))*height/100f,
                (LEFT_BORDER_PERCENT+FLINCH_PILE_WIDTH+(BUFFER_PERCENT2 * 6) + (CARD_WIDTH_PERCENT * 5)+ (FLINCH_BUTTON_WIDTH))*width/100f,
                (100-VERTICAL_BORDER_PERCENT_BOTTOMPLAYER) * height/100f));
        buttons[3] = (new RectF ((LEFT_BORDER_PERCENT+FLINCH_PILE_WIDTH +(CARD_WIDTH_PERCENT*5)+(BUFFER_PERCENT2)*6 )*width/100f - 5,
                (100-VERTICAL_BORDER_PERCENT_BOTTOMPLAYER-(FLINCH_BUTTON_HEIGHT*2) - BUFFER_PERCENT2)*height/100f - 5,
                (LEFT_BORDER_PERCENT+FLINCH_PILE_WIDTH+(BUFFER_PERCENT2*6) +(CARD_WIDTH_PERCENT*5)+ (FLINCH_BUTTON_WIDTH))*width/100f + 5,
                (100-(VERTICAL_BORDER_PERCENT_BOTTOMPLAYER+FLINCH_BUTTON_HEIGHT+BUFFER_PERCENT2)) * height/100f + 5));
        buttons[4] = (new RectF ((LEFT_BORDER_PERCENT+FLINCH_PILE_WIDTH+(BUFFER_PERCENT2 * 6)+(CARD_WIDTH_PERCENT*5))*width/100f - 5,
                (100-VERTICAL_BORDER_PERCENT_BOTTOMPLAYER-(FLINCH_BUTTON_HEIGHT))*height/100f - 5,
                (LEFT_BORDER_PERCENT+FLINCH_PILE_WIDTH+(BUFFER_PERCENT2 * 6) + (CARD_WIDTH_PERCENT * 5)+ (FLINCH_BUTTON_WIDTH))*width/100f + 5,
                (100-VERTICAL_BORDER_PERCENT_BOTTOMPLAYER) * height/100f + 5));

    }

    /**
     *
     * @param counter
     * @param player
     * @return
     */
    private int getBottomCardLocs(int counter, int player){
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
        return counter;
    }

    /**
     *
     * @param counter
     * @param player
     * @return
     */
    private int getTopCardLocs(int counter, int player){
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
        return counter;
    }

    private int getCenterCardsLocs (int counter){
        cardPlace[counter] = drawCenterOne();
        counter++;
        cardPlace[counter] = drawCenterTwo();
        counter++;
        cardPlace[counter] = drawCenterThree();
        counter++;
        cardPlace[counter] = drawCenterFour();
        counter++;
        cardPlace[counter] = drawCenterFive();
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

        return counter;
    }

    /**
     *
     * @param counter
     * @param player
     * @return
     */
    private int getRightLocs (int counter, int player) {
        cardPlace[counter] = drawRightDiscardOne();
        counter++;
        cardPlace[counter] = drawRightDiscardTwo();
        counter++;
        cardPlace[counter] = drawRightDiscardthree();
        counter++;
        cardPlace[counter] = drawRightDiscardfour();
        counter++;
        cardPlace[counter] = drawRightDiscardfive();
        counter++;
        cardPlace[counter] = drawRightFlinch();
        counter++;
        return counter;

    }

    /**
     *
     * @param counter
     * @param player
     * @return
     */

    /**
     *Note this should call get player cards to be more efficient
     * @param counter
     * @param player
     * @return
     */
    private int getBottomCards(int counter, int player) {
        toDraw[counter] = state.getPlayerState(player).getTopFlinchCard();
        counter++;
        for(int i = 0; i < 5; i++) {
            toDraw[counter] = state.getPlayerState(player).getHand().getCardAt(i);
            counter++;
        }
        for(int i = 0; i < 5; i++) {
            toDraw[counter] = state.getPlayerState(player).getTopDiscards()[i];
            counter++;
        }
        return counter;
    }

    /**
     *
     * @param counter
     * @param player
     * @return
     */
    private int getPlayerCards(int counter, int player) {
        for(int i = 0; i < 5; i++) {
            toDraw[counter] = state.getPlayerState(player).getTopDiscards()[i];
            counter++;
        }
        toDraw[counter] = state.getPlayerState(player).getTopFlinchCard();
        counter++;
        return counter;
    }

    private int getCenterCards(int counter) {
        for(int i = 0; i < 10; i++) {
            toDraw[counter] = state.getCenterPiles()[i];
            counter++;
        }
        return counter;
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
    //discard card #1
    private RectF drawBottomDiscardOne () {

        int width = surface.getWidth();
        int height = surface.getHeight();
        RectF DiscardOne = new RectF ((LEFT_BORDER_PERCENT+FLINCH_PILE_WIDTH+BUFFER_PERCENT2)*width/100f,
                (100-VERTICAL_BORDER_PERCENT_BOTTOMPLAYER-(CARD_HEIGHT_PERCENT*2) - BUFFER_PERCENT2)*height/100f,
                (LEFT_BORDER_PERCENT+CARD_WIDTH_PERCENT + FLINCH_PILE_WIDTH + BUFFER_PERCENT2)*width/100f,
                (100-(VERTICAL_BORDER_PERCENT_BOTTOMPLAYER+CARD_HEIGHT_PERCENT+BUFFER_PERCENT2)) * height/100f);
        return DiscardOne;

    }
    //discrd card #2
    private RectF drawBottomDiscardtwo() {

        int width = surface.getWidth();
        int height = surface.getHeight();
        RectF DiscardTwo = new RectF ((LEFT_BORDER_PERCENT+FLINCH_PILE_WIDTH+(BUFFER_PERCENT2)*2 + (CARD_WIDTH_PERCENT))*width/100f,
                (100-VERTICAL_BORDER_PERCENT_BOTTOMPLAYER-(CARD_HEIGHT_PERCENT*2) - BUFFER_PERCENT2)*height/100f,
                (LEFT_BORDER_PERCENT+FLINCH_PILE_WIDTH+(BUFFER_PERCENT2)*2 + (CARD_WIDTH_PERCENT * 2))*width/100f,
                (100-(VERTICAL_BORDER_PERCENT_BOTTOMPLAYER+CARD_HEIGHT_PERCENT+BUFFER_PERCENT2)) * height/100f);
        return DiscardTwo;

    }
    //discard card #3
    private RectF drawBottomDiscardthree() {

        int width = surface.getWidth();
        int height = surface.getHeight();
        RectF DiscardThree = new RectF ((LEFT_BORDER_PERCENT+FLINCH_PILE_WIDTH+(BUFFER_PERCENT2)*3 + (CARD_WIDTH_PERCENT * 2))*width/100f,
                (100-VERTICAL_BORDER_PERCENT_BOTTOMPLAYER-(CARD_HEIGHT_PERCENT*2) - BUFFER_PERCENT2)*height/100f,
                (LEFT_BORDER_PERCENT+FLINCH_PILE_WIDTH+(BUFFER_PERCENT2)*3 + (CARD_WIDTH_PERCENT * 3))*width/100f,
                (100-(VERTICAL_BORDER_PERCENT_BOTTOMPLAYER+CARD_HEIGHT_PERCENT+BUFFER_PERCENT2)) * height/100f);
        return DiscardThree;

    }
    //discard card #4
    private RectF drawBottomDiscardfour() {

        int width = surface.getWidth();
        int height = surface.getHeight();
        RectF DiscardFour = new RectF ((LEFT_BORDER_PERCENT+FLINCH_PILE_WIDTH+(BUFFER_PERCENT2)*4 + (CARD_WIDTH_PERCENT * 3))*width/100f,
                (100-VERTICAL_BORDER_PERCENT_BOTTOMPLAYER-(CARD_HEIGHT_PERCENT*2) - BUFFER_PERCENT2)*height/100f,
                (LEFT_BORDER_PERCENT+FLINCH_PILE_WIDTH+(BUFFER_PERCENT2)*4 + (CARD_WIDTH_PERCENT * 4))*width/100f,
                (100-(VERTICAL_BORDER_PERCENT_BOTTOMPLAYER+CARD_HEIGHT_PERCENT+BUFFER_PERCENT2)) * height/100f);
        return DiscardFour;

    }
    //discard card #5
    private RectF drawBottomDiscardfive() {

        int width = surface.getWidth();
        int height = surface.getHeight();
        RectF DiscardFive = new RectF ((LEFT_BORDER_PERCENT+FLINCH_PILE_WIDTH+(BUFFER_PERCENT2)*5 + (CARD_WIDTH_PERCENT * 4))*width/100f,
                (100-VERTICAL_BORDER_PERCENT_BOTTOMPLAYER-(CARD_HEIGHT_PERCENT*2) - BUFFER_PERCENT2)*height/100f,
                (LEFT_BORDER_PERCENT+FLINCH_PILE_WIDTH+(BUFFER_PERCENT2)*5 + (CARD_WIDTH_PERCENT * 5))*width/100f,
                (100-(VERTICAL_BORDER_PERCENT_BOTTOMPLAYER+CARD_HEIGHT_PERCENT+BUFFER_PERCENT2)) * height/100f);
        return DiscardFive;

    }
/*
    private RectF drawFlinchButton() {
        int width = surface.getWidth();
        int height = surface.getHeight();
        RectF FlinchButton = new RectF ((LEFT_BORDER_PERCENT+ FLINCH_PILE_WIDTH+(BUFFER_PERCENT2)*6 +( FLINCH_BUTTON_WIDTH *5)) *width/100f,
                (100-VERTICAL_BORDER_PERCENT_BOTTOMPLAYER-FLINCH_BUTTON_HEIGHT*2 - BUFFER_PERCENT2)*height/100f,
                (LEFT_BORDER_PERCENT+FLINCH_PILE_WIDTH+ (BUFFER_PERCENT2)*6) + (FLINCH_PILE_WIDTH* 6 )*width/100f,
                (100-(VERTICAL_BORDER_PERCENT_BOTTOMPLAYER+FLINCH_BUTTON_HEIGHT+BUFFER_PERCENT2)) * height /100f);
        return FlinchButton;
    }
    */
    // side player drawing
    private RectF drawLeftDiscardOne () {

    int width = surface.getWidth();
    int height = surface.getHeight();
    RectF DiscardLeftOne = new RectF ((LEFT_BORDER_SIDE)*width/100f,
            (100-VERTICAL_BORDER_PERCENT_LEFT-CARD_WIDTH_PERCENT)*height/100f,
            ((LEFT_BORDER_SIDE+CARD_HEIGHT_PERCENT)) * width/100f ,
            (100-VERTICAL_BORDER_PERCENT_LEFT)*height/100f);
    return DiscardLeftOne;

    }

    private RectF drawLeftDiscardTwo () {

        int width = surface.getWidth();
        int height = surface.getHeight();
        RectF DiscardLeftTwo = new RectF ((LEFT_BORDER_SIDE)*width/100f,
                (100-VERTICAL_BORDER_PERCENT_LEFT-(CARD_WIDTH_PERCENT*2) - (BUFFER_PERCENT2))*height/100f,
                (LEFT_BORDER_SIDE+(CARD_HEIGHT_PERCENT))*width/100f,
                (100-(VERTICAL_BORDER_PERCENT_LEFT+CARD_WIDTH_PERCENT + BUFFER_PERCENT2)) * height/100f);
        return DiscardLeftTwo;

    }

    private RectF drawLeftDiscardthree() {

        int width = surface.getWidth();
        int height = surface.getHeight();
        RectF DiscardLeftThree = new RectF ((LEFT_BORDER_SIDE)*width/100f,
                (100-VERTICAL_BORDER_PERCENT_LEFT-(CARD_WIDTH_PERCENT*3) - BUFFER_PERCENT2*2)*height/100f,
                (LEFT_BORDER_SIDE+(CARD_HEIGHT_PERCENT))*width/100f,
                (100-(VERTICAL_BORDER_PERCENT_LEFT+(CARD_WIDTH_PERCENT * 2)+(BUFFER_PERCENT2 * 2))) * height/100f);
        return DiscardLeftThree;

    }

    private RectF drawLeftDiscardfour() {

        int width = surface.getWidth();
        int height = surface.getHeight();
        RectF DiscardLeftFour = new RectF ((LEFT_BORDER_SIDE)*width/100f,
                (100-VERTICAL_BORDER_PERCENT_LEFT-(CARD_WIDTH_PERCENT*4) - BUFFER_PERCENT2*3)*height/100f,
                (LEFT_BORDER_SIDE+(CARD_HEIGHT_PERCENT))*width/100f,
                (100-(VERTICAL_BORDER_PERCENT_LEFT+(CARD_WIDTH_PERCENT* 3)+(BUFFER_PERCENT2 * 3))) * height/100f);
        return DiscardLeftFour;

    }

    private RectF drawLeftDiscardfive() {

        int width = surface.getWidth();
        int height = surface.getHeight();
        RectF DiscardLeftFive = new RectF ((LEFT_BORDER_SIDE)*width/100f,
                (100-VERTICAL_BORDER_PERCENT_LEFT-(CARD_WIDTH_PERCENT*5) - (BUFFER_PERCENT2*4))*height/100f,
                (LEFT_BORDER_SIDE+CARD_HEIGHT_PERCENT)*width/100f,
                (100-(VERTICAL_BORDER_PERCENT_LEFT+(CARD_WIDTH_PERCENT* 4)+(BUFFER_PERCENT2* 4))) * height/100f);
        return DiscardLeftFive;

    }

    private RectF drawLeftFlinch() {

        int width = surface.getWidth();
        int height = surface.getHeight();
        RectF DiscardLeftFlinch = new RectF ((LEFT_BORDER_SIDE)*width/100f,
                (100-VERTICAL_BORDER_PERCENT_LEFT-(CARD_WIDTH_PERCENT*5) - FLINCH_TOP_WIDTH - BUFFER_PERCENT2*5)*height/100f,
                (LEFT_BORDER_SIDE+ (FLINCH_TOP_HEIGHT))*width/100f,
                (100-(VERTICAL_BORDER_PERCENT_LEFT+(CARD_WIDTH_PERCENT * 5)+(BUFFER_PERCENT2 * 5))) * height/100f);
        return DiscardLeftFlinch;

    }
    //RIGHT PLAYER

    private RectF drawRightDiscardOne () {

        int width = surface.getWidth();
        int height = surface.getHeight();
        RectF DiscardRightOne = new RectF ((100 - (LEFT_BORDER_SIDE + CARD_HEIGHT_PERCENT) )*width/100f,
                (100-VERTICAL_BORDER_PERCENT_LEFT-CARD_WIDTH_PERCENT - FLINCH_TOP_WIDTH - BUFFER_PERCENT2)*height/100f,
                (100 - LEFT_BORDER_SIDE)*width/100f,
                (100-VERTICAL_BORDER_PERCENT_LEFT - FLINCH_TOP_WIDTH - BUFFER_PERCENT2)*height/100f);
        return DiscardRightOne;

    }

    private RectF drawRightDiscardTwo () {

        int width = surface.getWidth();
        int height = surface.getHeight();
        RectF DiscardRightTwo = new RectF ((100 - (LEFT_BORDER_SIDE + CARD_HEIGHT_PERCENT) )*width/100f,
                (100-VERTICAL_BORDER_PERCENT_LEFT-(CARD_WIDTH_PERCENT*2) - FLINCH_TOP_WIDTH- (BUFFER_PERCENT2 * 2))*height/100f,
                (100 - LEFT_BORDER_SIDE )*width/100f,
                (100-(VERTICAL_BORDER_PERCENT_LEFT+CARD_WIDTH_PERCENT + FLINCH_TOP_WIDTH + (BUFFER_PERCENT2* 2))) * height/100f);
        return DiscardRightTwo;

    }

    private RectF drawRightDiscardthree() {

        int width = surface.getWidth();
        int height = surface.getHeight();
        RectF DiscardRightThree = new RectF ((100 - (LEFT_BORDER_SIDE + CARD_HEIGHT_PERCENT) )*width/100f,
                (100-VERTICAL_BORDER_PERCENT_LEFT-(CARD_WIDTH_PERCENT*3) - FLINCH_TOP_WIDTH -BUFFER_PERCENT2*3)*height/100f,
                (100 - LEFT_BORDER_SIDE )*width/100f,
                (100-(VERTICAL_BORDER_PERCENT_LEFT+(CARD_WIDTH_PERCENT * 2)+ FLINCH_TOP_WIDTH +(BUFFER_PERCENT2 * 3))) * height/100f);
        return DiscardRightThree;

    }

    private RectF drawRightDiscardfour() {

        int width = surface.getWidth();
        int height = surface.getHeight();
        RectF DiscardRightFour = new RectF ((100 - (LEFT_BORDER_SIDE + CARD_HEIGHT_PERCENT) )*width/100f,
                (100-VERTICAL_BORDER_PERCENT_LEFT-(CARD_WIDTH_PERCENT*4) - FLINCH_TOP_WIDTH -BUFFER_PERCENT2*4)*height/100f,
                (100 - LEFT_BORDER_SIDE )*width/100f,
                (100-(VERTICAL_BORDER_PERCENT_LEFT+(CARD_WIDTH_PERCENT* 3)+ FLINCH_TOP_WIDTH +(BUFFER_PERCENT2 * 4))) * height/100f);
        return DiscardRightFour;

    }

    private RectF drawRightDiscardfive() {

        int width = surface.getWidth();
        int height = surface.getHeight();
        RectF DiscardRightFive = new RectF ((100 - (LEFT_BORDER_SIDE + CARD_HEIGHT_PERCENT) )*width/100f,
                (100-VERTICAL_BORDER_PERCENT_LEFT-(CARD_WIDTH_PERCENT*5) - FLINCH_TOP_WIDTH -(BUFFER_PERCENT2*5))*height/100f,
                (100 - LEFT_BORDER_SIDE)*width/100f,
                (100-(VERTICAL_BORDER_PERCENT_LEFT+(CARD_WIDTH_PERCENT* 4)+ FLINCH_TOP_WIDTH +(BUFFER_PERCENT2* 5))) * height/100f);
        return DiscardRightFive;

    }

    private RectF drawRightFlinch() {

        int width = surface.getWidth();
        int height = surface.getHeight();
        RectF DiscardRightFlinch = new RectF ((100 - LEFT_BORDER_SIDE - FLINCH_TOP_HEIGHT)*width/100f,
                (100-VERTICAL_BORDER_PERCENT_LEFT - FLINCH_TOP_WIDTH)*height/100f,
                (100 - LEFT_BORDER_SIDE)*width/100f,
                (100-VERTICAL_BORDER_PERCENT_LEFT) * height/100f);
        return DiscardRightFlinch;

    }

    /**
     *
     * @param counter
     * @param player
     * @return
     */
    private int getLeftCardLocs (int counter, int player) {
        cardPlace[counter] = drawLeftDiscardOne();
        counter++;
        cardPlace[counter] = drawLeftDiscardTwo();
        counter++;
        cardPlace[counter] = drawLeftDiscardthree();
        counter++;
        cardPlace[counter] = drawLeftDiscardfour();
        counter++;
        cardPlace[counter] = drawLeftDiscardfive();
        counter++;
        cardPlace[counter] = drawLeftFlinch();
        counter++;
        return counter;
    }


    //start of center pile drawing

    //bottom left center card

    private RectF drawCenterOne () {
        int width = surface.getWidth();
        int height = surface.getHeight();
        RectF CenterOne = new RectF ((LEFT_BORDER_PERCENT+BUFFER_PERCENT2)*width/100f,
                (100-VERTICAL_CENTERONE-(CENTER_HEIGHT*2) - (BUFFER_PERCENT2 * 2))*height/100f,
                (LEFT_BORDER_PERCENT+CENTER_WIDTH + BUFFER_PERCENT2)*width/100f,
                (100-(VERTICAL_CENTERONE+CENTER_HEIGHT+ (BUFFER_PERCENT2 * 2))) * height/100f);
        return CenterOne;

    }

    private RectF drawCenterTwo() {

        int width = surface.getWidth();
        int height = surface.getHeight();
        RectF CenterTwo = new RectF ((LEFT_BORDER_PERCENT+(BUFFER_PERCENT2)*2 + (CENTER_WIDTH))*width/100f,
                (100-VERTICAL_CENTERONE-(CENTER_HEIGHT*2) - (BUFFER_PERCENT2 * 2))*height/100f,
                (LEFT_BORDER_PERCENT+(BUFFER_PERCENT2)*2 + (CENTER_WIDTH * 2))*width/100f,
                (100-(VERTICAL_CENTERONE+CENTER_HEIGHT+ (BUFFER_PERCENT2 * 2))) * height/100f);
        return CenterTwo;

    }
    //third card bottom row
    private RectF drawCenterThree() {

        int width = surface.getWidth();
        int height = surface.getHeight();
        RectF CenterThree = new RectF ((LEFT_BORDER_PERCENT+(BUFFER_PERCENT2)*3 + (CENTER_WIDTH * 2))*width/100f,
                (100-VERTICAL_CENTERONE-(CENTER_HEIGHT*2) - (BUFFER_PERCENT2 * 2))*height/100f,
                (LEFT_BORDER_PERCENT+(BUFFER_PERCENT2)*3 + (CENTER_WIDTH * 3))*width/100f,
                (100-(VERTICAL_CENTERONE+CENTER_HEIGHT+ (BUFFER_PERCENT2 * 2))) * height/100f);
        return CenterThree;

    }
    //fourth card bottom row
    private RectF drawCenterFour() {

        int width = surface.getWidth();
        int height = surface.getHeight();
        RectF CenterFour = new RectF ((LEFT_BORDER_PERCENT+(BUFFER_PERCENT2)*4 + (CENTER_WIDTH * 3))*width/100f,
                (100-VERTICAL_CENTERONE-(CENTER_HEIGHT*2) - (BUFFER_PERCENT2 * 2))*height/100f,
                (LEFT_BORDER_PERCENT+(BUFFER_PERCENT2)*4 + (CENTER_WIDTH * 4))*width/100f,
                (100-(VERTICAL_CENTERONE+CENTER_HEIGHT+ (BUFFER_PERCENT2 * 2))) * height/100f);
        return CenterFour;

    }
    //fifth card bottom row
    private RectF drawCenterFive() {

        int width = surface.getWidth();
        int height = surface.getHeight();
        RectF CenterFive = new RectF ((LEFT_BORDER_PERCENT+(BUFFER_PERCENT2)*5 + (CENTER_WIDTH * 4))*width/100f,
                (100-VERTICAL_CENTERONE-(CENTER_HEIGHT*2) - (BUFFER_PERCENT2 * 2))*height/100f,
                (LEFT_BORDER_PERCENT+(BUFFER_PERCENT2)*5 + (CENTER_WIDTH * 5))*width/100f,
                (100-(VERTICAL_CENTERONE+CENTER_HEIGHT+ (BUFFER_PERCENT2 * 2))) * height/100f);
        return CenterFive;

    }
    //top left card
    private RectF drawCenterSix () {

        int width = surface.getWidth();
        int height = surface.getHeight();
        RectF CenterSix = new RectF ((LEFT_BORDER_PERCENT+BUFFER_PERCENT2)*width/100f,
                (100-VERTICAL_CENTERONE-(CENTER_HEIGHT) - BUFFER_PERCENT2)*height/100f,
                (LEFT_BORDER_PERCENT+CENTER_WIDTH + BUFFER_PERCENT2)*width/100f,
                (100-(VERTICAL_CENTERONE +BUFFER_PERCENT2)) * height/100f);
        return CenterSix;

    }
    //second card top row
    private RectF drawCenterSeven() {

        int width = surface.getWidth();
        int height = surface.getHeight();
        RectF CenterSeven = new RectF ((LEFT_BORDER_PERCENT+(BUFFER_PERCENT2)*2 + (CENTER_WIDTH))*width/100f,
                (100-VERTICAL_CENTERTWO-(CENTER_HEIGHT*2) - BUFFER_PERCENT2)*height/100f,
                (LEFT_BORDER_PERCENT+(BUFFER_PERCENT2)*2 + (CENTER_WIDTH * 2))*width/100f,
                (100-(VERTICAL_CENTERTWO+CENTER_HEIGHT+BUFFER_PERCENT2)) * height/100f);
        return CenterSeven;

    }
    //third card top row
    private RectF drawCenterEight() {

        int width = surface.getWidth();
        int height = surface.getHeight();
        RectF CenterEight = new RectF ((LEFT_BORDER_PERCENT+(BUFFER_PERCENT2)*3 + (CENTER_WIDTH * 2))*width/100f,
                (100-VERTICAL_CENTERTWO-(CENTER_HEIGHT*2) - BUFFER_PERCENT2)*height/100f,
                (LEFT_BORDER_PERCENT+(BUFFER_PERCENT2)*3 + (CENTER_WIDTH * 3))*width/100f,
                (100-(VERTICAL_CENTERTWO+CENTER_HEIGHT+BUFFER_PERCENT2)) * height/100f);
        return CenterEight;

    }
    //fourth card top row
    private RectF drawCenterNine() {

        int width = surface.getWidth();
        int height = surface.getHeight();
        RectF CenterNine = new RectF ((LEFT_BORDER_PERCENT+(BUFFER_PERCENT2)*4 + (CENTER_WIDTH * 3))*width/100f,
                (100-VERTICAL_CENTERTWO-(CENTER_HEIGHT*2) - BUFFER_PERCENT2)*height/100f,
                (LEFT_BORDER_PERCENT+(BUFFER_PERCENT2)*4 + (CENTER_WIDTH * 4))*width/100f,
                (100-(VERTICAL_CENTERTWO+CENTER_HEIGHT+BUFFER_PERCENT2)) * height/100f);
        return CenterNine;

    }
    //fifth card top row
    private RectF drawCenterTen() {

        int width = surface.getWidth();
        int height = surface.getHeight();
        RectF CenterTen = new RectF ((LEFT_BORDER_PERCENT+(BUFFER_PERCENT2)*5 + (CENTER_WIDTH * 4))*width/100f,
                (100-VERTICAL_CENTERTWO-(CENTER_HEIGHT*2) - BUFFER_PERCENT2)*height/100f,
                (LEFT_BORDER_PERCENT+(BUFFER_PERCENT2)*5 + (CENTER_WIDTH * 5))*width/100f,
                (100-(VERTICAL_CENTERTWO+CENTER_HEIGHT+BUFFER_PERCENT2)) * height/100f);
        return CenterTen;

    }

   /* private RectF drawAllButtons() {
        int width = surface.getWidth();
        int height = surface.getHeight();
        RectF FlinchButton = new RectF (BUFFER_PERCENT_FLINCH_BUTTON *width/100f,
                (100-VERTICAL_BORDER_PERCENT_BOTTOMPLAYER-CARD_HEIGHT_PERCENT-5)*height/100f,
                (BUFFER_PERCENT_FLINCH_BUTTON+FLINCH_BUTTON_WIDTH)*width/100f,
                (100-(VERTICAL_BORDER_PERCENT_BOTTOMPLAYER+CARD_HEIGHT_PERCENT+15)) * height /100f);
        return FlinchButton;
    }*/

   //drawing of top player cards ( 5 discard 1 flinch)

    //discard one (left most)
    private RectF drawTopDiscardOne () {

        int width = surface.getWidth();
        int height = surface.getHeight();
        RectF DiscardTopOne = new RectF ((LEFT_BORDER_PERCENT2+FLINCH_PILE_WIDTH+BUFFER_PERCENT2)*width/100f,
                (100-VERTICAL_BORDER_PERCENT_TOPPLAYER-(CARD_HEIGHT_PERCENT*2) - BUFFER_PERCENT2)*height/100f,
                (LEFT_BORDER_PERCENT2+CARD_WIDTH_PERCENT + FLINCH_PILE_WIDTH + BUFFER_PERCENT2)*width/100f,
                (100-(VERTICAL_BORDER_PERCENT_TOPPLAYER+CARD_HEIGHT_PERCENT+BUFFER_PERCENT2)) * height/100f);
        return DiscardTopOne;

    }
    //discard two
    private RectF drawTopDiscardtwo() {

        int width = surface.getWidth();
        int height = surface.getHeight();
        RectF DiscardTwoTop = new RectF ((LEFT_BORDER_PERCENT2+FLINCH_BUTTON_WIDTH+(BUFFER_PERCENT2)*2 + (CARD_WIDTH_PERCENT))*width/100f,
                (100-VERTICAL_BORDER_PERCENT_TOPPLAYER-(CARD_HEIGHT_PERCENT*2) - BUFFER_PERCENT2)*height/100f,
                (LEFT_BORDER_PERCENT2+FLINCH_PILE_WIDTH+(BUFFER_PERCENT2)*2 + (CARD_WIDTH_PERCENT * 2))*width/100f,
                (100-(VERTICAL_BORDER_PERCENT_TOPPLAYER+CARD_HEIGHT_PERCENT+BUFFER_PERCENT2)) * height/100f);
        return DiscardTwoTop;

    }
    //discard three
    private RectF drawTopDiscardthree() {

        int width = surface.getWidth();
        int height = surface.getHeight();
        RectF DiscardThreeTop = new RectF ((LEFT_BORDER_PERCENT2+FLINCH_PILE_WIDTH+(BUFFER_PERCENT2)*3 + (CARD_WIDTH_PERCENT * 2))*width/100f,
                (100-VERTICAL_BORDER_PERCENT_TOPPLAYER-(CARD_HEIGHT_PERCENT*2) - BUFFER_PERCENT2)*height/100f,
                (LEFT_BORDER_PERCENT2+FLINCH_PILE_WIDTH+(BUFFER_PERCENT2)*3 + (CARD_WIDTH_PERCENT * 3))*width/100f,
                (100-(VERTICAL_BORDER_PERCENT_TOPPLAYER+CARD_HEIGHT_PERCENT+BUFFER_PERCENT2)) * height/100f);
        return DiscardThreeTop;

    }

    //discard four
    private RectF drawTopDiscardfour() {

        int width = surface.getWidth();
        int height = surface.getHeight();
        RectF DiscardFourTop = new RectF ((LEFT_BORDER_PERCENT2+FLINCH_PILE_WIDTH+(BUFFER_PERCENT2)*4 + (CARD_WIDTH_PERCENT * 3))*width/100f,
                (100-VERTICAL_BORDER_PERCENT_TOPPLAYER-(CARD_HEIGHT_PERCENT*2) - BUFFER_PERCENT2)*height/100f,
                (LEFT_BORDER_PERCENT2+FLINCH_PILE_WIDTH+(BUFFER_PERCENT2)*4 + (CARD_WIDTH_PERCENT * 4))*width/100f,
                (100-(VERTICAL_BORDER_PERCENT_TOPPLAYER+CARD_HEIGHT_PERCENT+BUFFER_PERCENT2)) * height/100f);
        return DiscardFourTop;

    }
    //discard five
    private RectF drawTopDiscardfive() {

        int width = surface.getWidth();
        int height = surface.getHeight();
        RectF DiscardFiveTop = new RectF ((LEFT_BORDER_PERCENT2+FLINCH_PILE_WIDTH+(BUFFER_PERCENT2)*5 + (CARD_WIDTH_PERCENT * 4))*width/100f,
                (100-VERTICAL_BORDER_PERCENT_TOPPLAYER-(CARD_HEIGHT_PERCENT*2) - BUFFER_PERCENT2)*height/100f,
                (LEFT_BORDER_PERCENT2+FLINCH_PILE_WIDTH+(BUFFER_PERCENT2)*5 + (CARD_WIDTH_PERCENT * 5))*width/100f,
                (100-(VERTICAL_BORDER_PERCENT_TOPPLAYER+CARD_HEIGHT_PERCENT+BUFFER_PERCENT2)) * height/100f);
        return DiscardFiveTop;

    }
    //flinch pile
    private RectF drawTopFlinch() {

        int width = surface.getWidth();
        int height = surface.getHeight();
        RectF FlinchTop = new RectF ((LEFT_BORDER_PERCENT2+FLINCH_PILE_WIDTH+(BUFFER_PERCENT2*6) + (CARD_WIDTH_PERCENT * 5))*width/100f,
                (100-VERTICAL_BORDER_PERCENT_TOPPLAYER-(FLINCH_TOP_HEIGHT) - CARD_HEIGHT_PERCENT-BUFFER_PERCENT2)*height/100f,
                (LEFT_BORDER_PERCENT2+FLINCH_PILE_WIDTH+(BUFFER_PERCENT2*6) + (CARD_WIDTH_PERCENT * 5) + FLINCH_TOP_WIDTH)*width/100f,
                (100-(VERTICAL_BORDER_PERCENT_TOPPLAYER+CARD_HEIGHT_PERCENT +BUFFER_PERCENT2)) * height/100f);
        return FlinchTop;

    }

    //LEFT PLAYER
    //STILL NEEDS TO BE COMPLETED

    /**
     *
     * @return
     */
    private RectF getSelectRect() {
        // if there is no card selected
        if(selected < 0 || selected > cardPlace.length) {
            return null;
        }

        // what card is selected
        RectF chosen = cardPlace[selected];
        return new RectF(chosen.left, chosen.bottom, chosen.right, chosen.bottom - 5);
    }


    /*
        PROBLEM: HOW DO WE DIFFERENTIATE BETWEEN SELECTING A CARD FROM THE DISCARD PILE TO PLAY AND SELECTING A DISCARD PILE TO DISCARD TO
        INTERIM SOLUTION: YOU CANNOT PLAY FROM THE DISCARD PILE
     */

    public void onTouch(MotionEvent event) {

        if(state == null) {return;}
        // ignore everything except down-touch events
        if (event.getAction() != MotionEvent.ACTION_DOWN) return;

        // get the location of the touch on the surface
        int x = (int) event.getX();
        int y = (int) event.getY();

        // helper variable
        int playedTo;
        int discardedTo;

        // is it the player's turn
        if(state.getWhoseTurn() == this.playerNum) {
            // it is the player's turn--did they select a card?
            // flinch: cardPlace[0]
            if (cardPlace[0].contains(x, y)) {
                if(toDraw[0] > 0)
                selected = 0;
            }
            //card 1: carcPlace[1]
            else if (cardPlace[1].contains(x, y)) {
                if(toDraw[1] > 0)
                selected = 1;
            }
            // card 2: cardPlace[2]
            else if (cardPlace[2].contains(x, y)) {
                if(toDraw[2] > 0)
                selected = 2;
            }
            //card 3: cardPlace[3]
            else if (cardPlace[3].contains(x,y)) {
                if(toDraw[3] > 0)
                selected = 3;
            }
            //card 4: cardPlace[4]
            else if(cardPlace[4].contains(x,y)){
                if(toDraw[4] > 0)
                selected = 4;
            }
            //card 5: cardPlace[5]
            else if(cardPlace[5].contains(x,y)){
                if(toDraw[5] > 0)
                selected = 5;
            }
            else if((discardedTo = isDiscardPileTouched(x, y)) != -1){
                // select a card from the discard Pile
                if(inPlayMode) {
                    //TODO: SHOULD CHECK IF I am in play mode
                    if (toDraw[discardedTo] > 0)
                        selected = discardedTo;
                } else {
                    // discard a card
                    if (selected > 0 && selected < 6) {
                        // this card is from the hand--we can discard
                        game.sendAction(new FDiscardAction(this, selected - 1, discardedTo - 6));
                        selected = -1;
                    } else {
                        // hand discard
                        // TODO: THIS SHOULD BE HANDLED BY LOCAL GAME
                        surface.flash(Color.rgb(102, 178, 255), 50);
                    }
                }
            }
            // they are not selected a card
            // do they want to play a card? -- use a helper method
            else if ((playedTo = isCenterPileTouched(x, y)) != -1) {
                if(selected != -1) {
                    // did the player play from the hand?
                    if (selected > 0 && selected < 6) {
                        game.sendAction(new FPlayAction(this, selected - 1, playedTo - (cardPlace.length - 10), new Hand()));
                    }
                    // did the player play from the Flinch pile
                    else if (selected == 0) {
                        game.sendAction(new FPlayAction(this, 0, playedTo - (cardPlace.length - 10), new FlinchPile()));
                    }
                    // did the player play from the discard piles (INVALID FOR NOW) TODO: FIX
                    else if (selected > 5 && selected < 11) {
                        game.sendAction(new FPlayAction(this, selected - 6, playedTo - (cardPlace.length - 10), new DiscardPile()));
                    }
                    selected = -1;
                }
            //  did they touch the playMode button
            } else if(buttons[1].contains(x,y)) {
                inPlayMode = true;
            } else if(buttons[2].contains(x,y)) {
                inPlayMode = false;
            }
            else {
                // illegal touch-location: flash for 1/20 second
                surface.flash(Color.RED, 50);
            }

        } else {
            // it is not the player's turn --did they Flinch someone?
            if(buttons[0].contains(x,y)) {
                //synchronized (syncObj) {
                    game.sendAction(new FFlinchAction(this, state.getWhoseTurn()));
                //}
            } else {
                // illegal touch-location: flash for 1/20 second
                surface.flash(Color.RED, 50);
            }
        }
    }

    private int isDiscardPileTouched(int xTouch, int yTouch) {
        int index = 6;
        for(int i = 0; i < 5; i++){
            if(cardPlace[i + index].contains(xTouch, yTouch)){
                return i + index;
            }
        }
        return -1; 
    }
    //
    /**
     * Method to determine whether a center pile was selected
     * @param xTouch
     * @param yTouch
     * @return
     *  -1 if no center pile was selected, or else the index of the center pile chosen within the cardPlace array
     */
    private int isCenterPileTouched(int xTouch, int yTouch) {
        // Note: the center piles are the last 10 indices in the cardPlace array
        int index = cardPlace.length - 10;
        for (int i = index; i < cardPlace.length; i++ ) {
            if(cardPlace[i].contains(xTouch, yTouch)) {
                return i;
            }
        }
        // no card selected
        return -1;

    }
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

    /**
     * draws a card on the canvas; if the card is null, draw a card-back
     *  
     */

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
     * method to draw the toggle buttons to the screen
     * @param g
     */
    public void drawToggleButtons(Canvas g) {
        // ignore if the icons haven't been loaded yet
        if(icons == null) {
            return;
        }
        // create the paint object
        Paint p = new Paint();
        p.setColor(Color.BLACK);
        // create the source rectangle
        for(int i = 1; i < 3; i++) {
            // ignore if the flinch button icon hasn't been loaded yet
            if(icons[i] == null) {
                continue;
            }
            Rect r = new Rect(0, 0, icons[i].getWidth(), icons[i].getHeight());
            // draw the bitmap into the target rectangle
            g.drawBitmap(icons[i], r, buttons[i], p);
        }
    }

    /**
     * method to draw the flinch button to the screen
     * @param g
     */
    public void drawFlinchButton(Canvas g) {
        // ignore if the icons haven't been loaded yet
        if(icons == null || icons[0] == null) {
            return;
        }
        // create the paint object
        Paint p = new Paint();
        p.setColor(Color.BLACK);
        // create the source rectangle
        Rect r = new Rect(0, 0, icons[0].getWidth(), icons[0].getHeight());
            // draw the bitmap into the target rectangle
        g.drawBitmap(icons[0], r, buttons[0], p);
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


