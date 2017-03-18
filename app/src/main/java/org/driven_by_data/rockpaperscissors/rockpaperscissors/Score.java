package org.driven_by_data.rockpaperscissors.rockpaperscissors;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by tdraebing on 3/16/2017.
 */

public class Score implements Parcelable{
    int player = 0;
    int computer = 0;

    public Score(){}

    public void updateScore(Round r){
        if (r.WINNER == -1){
            computer += 1;
        } else if (r.WINNER == 1){
            player += 1;
        }
    }

    public Score(Parcel in){
        player = in.readInt();
        computer = in.readInt();
    }

    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(player);
        dest.writeInt(computer);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Score createFromParcel(Parcel in) {
            return new Score(in);
        }

        public Score[] newArray(int size) {
            return new Score[size];
        }
    };
}
