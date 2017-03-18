package org.driven_by_data.rockpaperscissors.rockpaperscissors;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseArray;

/**
 * Created by tdraebing on 3/16/2017.
 */

public class Game implements Parcelable{
    private int ROUNDS = 1;
    private int CURRENTROUND = 0;
    private SparseArray<Round> GAME_HISTORY = new SparseArray<>();
    private Score SCORE = new Score();
    private boolean isEnded = false;

    public Game(int num_rounds){
        ROUNDS = num_rounds;
    }

    public boolean playRound(Figure player_choice) throws GameEndedException{
        if (isEnded){
            throw new GameEndedException("Game already exceeded maximum rounds.");
        }
        CURRENTROUND += 1;
        Round current_round = new Round(player_choice, CURRENTROUND);
        GAME_HISTORY.append(CURRENTROUND, current_round);
        SCORE.updateScore(current_round);

        if (CURRENTROUND >= ROUNDS){
            isEnded = true;
        }

        return isEnded;
    }

    public boolean getIsEnded(){
        return isEnded;
    }

    public Score getScore(){
        return SCORE;
    }

    public SparseArray<Round> getGameHistory(){
        return GAME_HISTORY;
    }


    public Game(Parcel in){

        Round[] history;

        ROUNDS = in.readInt();
        CURRENTROUND = in.readInt();
        history = (Round[]) in.readParcelableArray(Round.class.getClassLoader());
        SCORE = in.readParcelable(Score.class.getClassLoader());
        isEnded = in.readByte() != 0;

        for (Round r: history){
            GAME_HISTORY.put(r.ROUND_ID, r);
        }
    }

    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        Parcelable[] history = new Parcelable[GAME_HISTORY.size()];
        for (int i = 0; i < GAME_HISTORY.size(); i++)
            history[i] = GAME_HISTORY.valueAt(i);

        dest.writeInt(ROUNDS);
        dest.writeInt(CURRENTROUND);
        dest.writeParcelableArray(history, flags);
        dest.writeParcelable(SCORE, flags);
        dest.writeByte((byte) (isEnded ? 1 : 0));
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Game createFromParcel(Parcel in) {
            return new Game(in);
        }

        public Game[] newArray(int size) {
            return new Game[size];
        }
    };

}
