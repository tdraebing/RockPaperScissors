package org.driven_by_data.rockpaperscissors.rockpaperscissors;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by tdraebing on 3/16/2017.
 */

public class Round implements Parcelable{
    Figure PLAYER_CHOICE;
    Figure COMPUTER_CHOICE;
    int WINNER;
    int ROUND_ID;

    public Round(Figure player_choice, int roundID){
        PLAYER_CHOICE = player_choice;
        COMPUTER_CHOICE= Figure.randomFigure();
        ROUND_ID = roundID;
        execute_move();
    }

    private void execute_move(){
        if (COMPUTER_CHOICE == PLAYER_CHOICE){
            WINNER = 0;
        } else if (COMPUTER_CHOICE == Figure.SCISSORS && PLAYER_CHOICE == Figure.ROCK){
            WINNER = 1;
        } else if (PLAYER_CHOICE == Figure.SCISSORS && COMPUTER_CHOICE == Figure.ROCK){
            WINNER = -1;
        } else if (PLAYER_CHOICE.ordinal() > COMPUTER_CHOICE.ordinal()){
            WINNER = 1;
        } else {
            WINNER = -1;
        }
    }

    public Round(Parcel in){
        PLAYER_CHOICE = Figure.valueOf(in.readString());
        COMPUTER_CHOICE = Figure.valueOf(in.readString());
        WINNER = in.readInt();
    }

    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(PLAYER_CHOICE.toString());
        dest.writeString(COMPUTER_CHOICE.toString());
        dest.writeInt(WINNER);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Round createFromParcel(Parcel in) {
            return new Round(in);
        }

        public Round[] newArray(int size) {
            return new Round[size];
        }
    };
}
