package jp.co.netprotections.pokerapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PokerResponse implements Parcelable {
    @SerializedName("result")
    private List<Result> results;
    @SerializedName("error")
    private List<Error> errors;

    public static class Result {
        @SerializedName("card")
        String card;
        @SerializedName("hand")
        String hand;
        @SerializedName("best")
        boolean best;

        public Result(String card, String hand, boolean best) {
            this.card = card;
            this.hand = hand;
            this.best = best;
        }

        public String getCard() {
            return card;
        }

        public String getHand() {
            return hand;
        }

        public boolean isBest() {
            return best;
        }
    }

    public static class Error {
        @SerializedName("card")
        String card;
        @SerializedName("msg")
        String msg;

        public Error(String card, String msg) {
            this.card = card;
            this.msg = msg;
        }

        public String getCard() {
            return card;
        }

        public String getMsg() {
            return msg;
        }
    }

    public List<Result> getResults() {
        return results;
    }

    public List<Error> getErrors() {
        return errors;
    }

    protected PokerResponse(Parcel in) {
    }

    public static final Creator<PokerResponse> CREATOR = new Creator<PokerResponse>() {
        @Override
        public PokerResponse createFromParcel(Parcel in) {
            return new PokerResponse(in);
        }

        @Override
        public PokerResponse[] newArray(int size) {
            return new PokerResponse[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

}
