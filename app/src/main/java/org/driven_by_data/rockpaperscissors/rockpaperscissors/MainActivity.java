package org.driven_by_data.rockpaperscissors.rockpaperscissors;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import static org.driven_by_data.rockpaperscissors.rockpaperscissors.R.id.button_paper;
import static org.driven_by_data.rockpaperscissors.rockpaperscissors.R.id.button_rock;
import static org.driven_by_data.rockpaperscissors.rockpaperscissors.R.id.button_scissors;

public class MainActivity extends AppCompatActivity {
    Game GAME;
    boolean isGameActive = false;
    boolean isButtonStateInGame = false;
    boolean isEnding = false;
    int MAX_ROUNDS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button button_start = (Button) findViewById(R.id.button_start);
        button_start.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!isGameActive){
                            startGame();
                            button_start.setText("Abort game");
                        } else {
                            endGame();
                            button_start.setText("Start new game");
                        }
                    }
                });

        Button button_rock = (Button) findViewById(R.id.button_rock);
        button_rock.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        move("button_rock");
                    }
                });

        Button button_paper = (Button) findViewById(R.id.button_paper);
        button_paper.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        move("button_paper");
                    }
                });

        Button button_scissors = (Button) findViewById(R.id.button_scissors);
        button_scissors.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        move("button_scissors");
                    }
                });
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        GAME = savedInstanceState.getParcelable("game");
        MAX_ROUNDS = savedInstanceState.getInt("max_rounds");
        isGameActive = savedInstanceState.getBoolean("is_game_active");
        isEnding = savedInstanceState.getBoolean("is_ending");
        updateRoundsText();
        if(isGameActive){
            updateScoreText();
            updateTableEntries();
            activateGame();
        }else {
            deactivateGame();
        }
        if(isEnding){
            showEndGameDialog();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("game", GAME);
        outState.putInt("max_rounds", MAX_ROUNDS);
        outState.putBoolean("is_game_active", isGameActive);
        outState.putBoolean("is_ending", isEnding);
        super.onSaveInstanceState(outState);
    }


    public void decreaseRounds(View view){
        if (MAX_ROUNDS >= 3){
            MAX_ROUNDS -= 2;
            updateRoundsText();
        }
    }

    public void increaseRounds(View view){
        MAX_ROUNDS += 2;
        updateRoundsText();
    }

    private void updateRoundsText(){
        TextView roundNumber = (TextView) findViewById(R.id.round_number);
        roundNumber.setText(String.valueOf(MAX_ROUNDS));
    }

    private void activateGame(){
        if (!isButtonStateInGame){
            switchButtonState();
            isGameActive = true;
        }
    }

    private void deactivateGame(){
        if (isButtonStateInGame){
            switchButtonState();
            isGameActive = false;
        }
    }

    private void switchButtonState(){
        Button figureRock = (Button) findViewById(button_rock);
        figureRock.setEnabled(!figureRock.isEnabled());
        Button figurePaper = (Button) findViewById(button_paper);
        figurePaper.setEnabled(!figurePaper.isEnabled());
        Button figureScissors = (Button) findViewById(button_scissors);
        figureScissors.setEnabled(!figureScissors.isEnabled());

        Button decreaseRounds = (Button) findViewById(R.id.decrease_rounds);
        decreaseRounds.setEnabled(!decreaseRounds.isEnabled());
        Button increaseRounds = (Button) findViewById(R.id.increase_rounds);
        increaseRounds.setEnabled(!increaseRounds.isEnabled());

        isButtonStateInGame = !isButtonStateInGame;
    }

    private void startGame(){
        GAME = new Game(MAX_ROUNDS);
        activateGame();
    }

    private void endGame(){
        deactivateGame();
        resetScoreText();
        resetTableEntries();
        isEnding=false;
    }

    public void move(String button_id){
        Figure player_choice = null;
        switch(button_id) {
            case "button_rock":
                player_choice = Figure.ROCK;
                break;
            case "button_paper":
                player_choice = Figure.PAPER;
                break;
            case "button_scissors":
                player_choice = Figure.SCISSORS;
                break;
        }
        if (player_choice == null){
            throw new NullPointerException("No player choice was made.");
        } else {
            try{
                if (GAME.playRound(player_choice)){
                    showEndGameDialog();
                    updateScoreText();
                    updateTableEntries();
                } else {
                    updateScoreText();
                    updateTableEntries();
                }
            } catch(GameEndedException e){
                showEndGameDialog();
            }
        }
    }

    private void updateScoreText(){
        TextView score_player = (TextView) findViewById(R.id.score_player);
        TextView score_computer = (TextView) findViewById(R.id.score_computer);
        Score score = GAME.getScore();
        score_player.setText(String.valueOf(score.player));
        score_computer.setText(String.valueOf(score.computer));
    }

    private void resetScoreText(){
        TextView score_player = (TextView) findViewById(R.id.score_player);
        TextView score_computer = (TextView) findViewById(R.id.score_computer);
        score_player.setText("0");
        score_computer.setText("0");
    }

    private void updateTableEntries(){
        TableLayout table = (TableLayout) findViewById(R.id.stats);
        final TableRow tableRow = (TableRow) getLayoutInflater()
                .inflate(R.layout.stats_table_row, null);

        SparseArray<Round> history = GAME.getGameHistory();
        int last_round_key = history.keyAt(history.size()-1);
        Round last_round = history.valueAt(history.size()-1);

        String indicator;

        TextView tv = (TextView) tableRow.findViewById(R.id.stats_round);
        tv.setText(String.valueOf(last_round_key));

        TextView tv_player = (TextView) tableRow.findViewById(R.id.stats_choice_player);
        tv_player.setText(last_round.PLAYER_CHOICE.toString());

        TextView tv_computer = (TextView) tableRow.findViewById(R.id.stats_choice_computer);
        tv_computer.setText(last_round.COMPUTER_CHOICE.toString());

        if (last_round.WINNER == 1){
            indicator = "\u2190";
            tv_player.setTypeface(Typeface.DEFAULT_BOLD);
        } else if (last_round.WINNER == -1){
            indicator = "\u2192";
            tv_computer.setTypeface(Typeface.DEFAULT_BOLD);
        } else {
            indicator = "\u2194";
        }


        tv = (TextView) tableRow.findViewById(R.id.stats_indicator);
        tv.setText(indicator);

        table.addView(tableRow);
    }

    private void resetTableEntries(){
        TableLayout table = (TableLayout) findViewById(R.id.stats);
        int childCount = table.getChildCount();

        // Remove all rows except the first one
        if (childCount > 1) {
            table.removeViews(1, childCount - 1);
        }
    }

    private void showEndGameDialog() {
        isEnding = true;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.endgame_dialog, null);
        dialogBuilder.setView(dialogView);

        final TextView dialog_message = (TextView) dialogView.findViewById(R.id.message);
        final TextView score_player = (TextView) dialogView.findViewById(R.id.score_player);
        final TextView score_computer = (TextView) dialogView.findViewById(R.id.score_computer);

        Score score = GAME.getScore();
        String message;
        if (score.player > score.computer){
            message = "You won!";
        } else if (score.player < score.computer){
            message = "You lost!";
        } else {
            message = "A draw! Try again!";
        }
        dialog_message.setText(message);

        score_player.setText(String.valueOf(score.player));
        score_computer.setText(String.valueOf(score.computer));

        dialogBuilder.setView(dialogView);
        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                endGame();
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }


}


