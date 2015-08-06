package com.bryancapps.blackjack;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class GameActivity extends Activity {
    final private int[] CARD_IDS = {R.drawable.card1, R.drawable.card2, R.drawable.card3, R.drawable.card4, R.drawable.card5, R.drawable.card6, R.drawable.card7, R.drawable.card8, R.drawable.card9, R.drawable.card10, R.drawable.card11, R.drawable.card12, R.drawable.card13, R.drawable.card14, R.drawable.card15, R.drawable.card16, R.drawable.card17, R.drawable.card18, R.drawable.card19, R.drawable.card20, R.drawable.card21, R.drawable.card22, R.drawable.card23, R.drawable.card24, R.drawable.card25, R.drawable.card26, R.drawable.card27, R.drawable.card28, R.drawable.card29, R.drawable.card30, R.drawable.card31, R.drawable.card32, R.drawable.card33, R.drawable.card34, R.drawable.card35, R.drawable.card36, R.drawable.card37, R.drawable.card38, R.drawable.card39, R.drawable.card40, R.drawable.card41, R.drawable.card42, R.drawable.card43, R.drawable.card44, R.drawable.card45, R.drawable.card46, R.drawable.card47, R.drawable.card48, R.drawable.card49, R.drawable.card50, R.drawable.card51, R.drawable.card52};
    TextView moneyTextView;
    TextView betTextView;
    TextView betTextView2;
    TextView playerScoreTextView;
    TextView dealerScoreTextView;
    TextView handOverTextView;
    int currentBet;
    int currentMoney;
    CardHand playerHand;
    CardHand dealerHand;
    CardDeck deck;
    View betDecisionView;
    View hitAndStayView;
    View playAgainView;
    Button doubleButton;
    Button splitButton;
    ImageView dealerFirstCardView;
    ImageView dealerSecondCardView;
    LinearLayout dealerHandView;
    LinearLayout playerHandView;
    ImageView playerFirstCardView;
    ImageView playerSecondCardView;
    String playerMoneyLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        moneyTextView = (TextView) findViewById(R.id.moneyTextView);
        betTextView = (TextView) findViewById(R.id.betTextView);
        betTextView2 = (TextView) findViewById(R.id.betTextView2);
        playerScoreTextView = (TextView) findViewById(R.id.playerScoreTextView);
        dealerScoreTextView = (TextView) findViewById(R.id.dealerScoreTextView);
        handOverTextView = (TextView) findViewById(R.id.handOverTextView);
        currentBet = 100;
        deck = new CardDeck();
        dealerHand = new CardHand(deck);
        playerHand = new CardHand(deck);
        betTextView.setText("$" + currentBet);
        betDecisionView = findViewById(R.id.betDecisionView);
        hitAndStayView = findViewById(R.id.hittingAndStayingView);
        playAgainView = findViewById(R.id.playAgainView);
        doubleButton = (Button) findViewById(R.id.doubleButton);
        splitButton = (Button) findViewById(R.id.splitButton);
        dealerFirstCardView = (ImageView) findViewById(R.id.dealerFirstCardView);
        dealerSecondCardView = (ImageView) findViewById(R.id.dealerSecondCardView);
        dealerHandView = (LinearLayout) findViewById(R.id.dealerHand);
        playerHandView = (LinearLayout) findViewById(R.id.playerHand);
        playerFirstCardView = (ImageView) findViewById(R.id.playerFirstCardView);
        playerSecondCardView = (ImageView) findViewById(R.id.playerSecondCardView);
        playerMoneyLabel = getResources().getString(R.string.player_money);
    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        int defaultValue = getResources().getInteger(R.integer.saved_money_default);
        currentMoney = sharedPref.getInt("money", defaultValue);
        moneyTextView.setText(playerMoneyLabel + (currentMoney + 100));
    }

    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("money", currentMoney);
        editor.apply();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void decreaseBet(View view) {
        if (currentBet >= 100) {
            currentBet -= 100;
            currentMoney += 100;
            betTextView.setText("$" + currentBet);
        }
    }

    public void increaseBet(View view) {
        if (currentMoney >= 100) {
            currentBet += 100;
            currentMoney -= 100;
            betTextView.setText("$" + currentBet);
        }
    }

    public void onBet(View view) {

        // switch to view for player hitting and staying
        betDecisionView.setVisibility(View.GONE);
        hitAndStayView.setVisibility(View.VISIBLE);
        TextView betView = (TextView) findViewById(R.id.betTextView2);
        betView.setText("$" + currentBet);
        moneyTextView.setText("");

        dealerHand.draw();
        playerHand.draw();
        playerHand.draw();
        dealerHand.draw();

        playerFirstCardView.setImageResource(CARD_IDS[playerHand.get(0)]);
        playerSecondCardView.setImageResource(CARD_IDS[playerHand.get(1)]);
        dealerSecondCardView.setImageResource(CARD_IDS[dealerHand.get(1)]);

        updateScoreViews(false);

        // enable split option if the cards are the same value
        if ((playerHand.get(0) / 4 == playerHand.get(1) / 4) || (playerHand.get(0) >= 4 && playerHand.get(0) < 20 && playerHand.get(1) >= 4 && playerHand.get(1) < 20)) {
            splitButton.setEnabled(true);
        }

        // check for blackjacks
        if (playerHand.getScore(true) == 21 || dealerHand.getScore(true) == 21) {
            endHand();
        }
    }

    private void updateScoreViews(boolean showDealerFirstCard) {
        playerScoreTextView.setText("" + playerHand.getScore(true));
        dealerScoreTextView.setText("" + dealerHand.getScore(showDealerFirstCard));
    }

    public void onStay(View view) {
        dealerFirstCardView.setImageResource(CARD_IDS[dealerHand.get(0)]);

        // hit dealer
        // dealer stays on all 17s
        while (dealerHand.getScore(true) < 17) {
            int hitCard = dealerHand.draw();
            addCardToView(dealerHandView, hitCard);
        }

        endHand();
    }

    public void onHit(View view) {
        int hitCard = playerHand.draw();
        addCardToView(playerHandView, hitCard);

        doubleButton.setEnabled(false);
        updateScoreViews(false);
        if (playerHand.getScore(true) > 21) {
            endHand();
        }
    }

    private void addCardToView(LinearLayout handView, int card) {
        ImageView cardView = new ImageView(this);
        cardView.setImageResource(CARD_IDS[card]);
        ViewGroup.LayoutParams originalParams = dealerFirstCardView.getLayoutParams();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(originalParams);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        float logicalDensity = metrics.density;
        int marginInPx = (int) Math.floor(-50 * logicalDensity);
        params.setMargins(marginInPx, 0, 0, 0);
        cardView.setLayoutParams(params);
        handView.addView(cardView);
    }

    public void onDouble(View view) {
        currentMoney -= currentBet;
        currentBet = currentBet * 2;
        int hitCard = playerHand.draw();
        addCardToView(playerHandView, hitCard);
        endHand();
    }

    public void onSplit(View view) {
    }

    public void playAgain(View view) {
        playAgainView.setVisibility(View.GONE);
        betDecisionView.setVisibility(View.VISIBLE);
        splitButton.setEnabled(false);
        doubleButton.setEnabled(true);
        deck.reset();
        currentBet = 100;
        currentMoney -= 100;
        betTextView.setText("$" + currentBet);
        dealerScoreTextView.setText("" + 0);
        playerScoreTextView.setText("" + 0);
        playerHand.clear();
        dealerHand.clear();
        int dealerCardCount = dealerHandView.getChildCount();
        for (int i = dealerCardCount - 1; i >= 2; i--) {
            dealerHandView.removeViewAt(i);
        }
        int playerCardCount = playerHandView.getChildCount();
        for (int i = playerCardCount - 1; i >= 2; i--) {
            playerHandView.removeViewAt(i);
        }
        dealerFirstCardView.setImageResource(R.drawable.b1fv);
        dealerSecondCardView.setImageResource(R.drawable.b1fv);
        playerFirstCardView.setImageResource(R.drawable.b2fv);
        playerSecondCardView.setImageResource(R.drawable.b2fv);
    }

    private void endHand() {

        int playerScore = playerHand.getScore(true);
        int dealerScore = dealerHand.getScore(true);
        updateScoreViews(true);
        dealerFirstCardView.setImageResource(CARD_IDS[dealerHand.get(0)]);
        Resources resources = getResources();

        if (playerScore > dealerScore && playerScore <= 21) {
            // player wins!
            String text = resources.getString(R.string.player_wins) + currentBet + "!";
            handOverTextView.setText(text);
            currentMoney += (currentBet * 2);
        } else if (playerScore > dealerScore && playerScore == 21 && playerHand.size() == 2) {
            // player has a blackjack!
            String text = resources.getString(R.string.player_blackjack) + (currentBet * 1.5) + "!";
            handOverTextView.setText(text);
            currentMoney += (currentBet * 2.5);
        } else if (playerScore <= 21 && dealerScore > 21) {
            // dealer busts!
            String text = resources.getString(R.string.dealer_busts) + currentBet + "!";
            handOverTextView.setText(text);
            currentMoney += (currentBet * 2);
        } else if (dealerScore > playerScore && dealerScore <= 21) {
            // dealer wins
            String text = resources.getString(R.string.dealer_wins) + currentBet + ".";
            handOverTextView.setText(text);
        } else if (dealerScore > playerScore && dealerScore == 21 && dealerHand.size() == 2) {
            // dealer has a blackjack
            String text = resources.getString(R.string.dealer_blackjack) + currentBet + ".";
            handOverTextView.setText(text);
        } else if (dealerScore == playerScore && dealerScore <= 21) {
            // push
            handOverTextView.setText(R.string.push);
            currentMoney += currentBet;
        } else if (playerScore > 21) {
            // player busts
            String text = resources.getString(R.string.player_busts) + currentBet + ".";
            handOverTextView.setText(text);
        }

        if (currentMoney <= 0) {
            currentMoney = 1000;
        }
        hitAndStayView.setVisibility(View.GONE);
        playAgainView.setVisibility(View.VISIBLE);
    }

}
