package jp.co.netprotections.pokerapp;

import android.os.Parcel;
import android.os.Parcelable;

public class Poker implements Parcelable {
    private String inputPoker;
    private String pokerPosition;
    private boolean strongPoker;
    private String checkTime;

    public Poker(String inputPoker, String pokerPosition, boolean strongPoker, String checkTime) {
        super();
        this.inputPoker = inputPoker;
        this.pokerPosition = pokerPosition;
        this.strongPoker = strongPoker;
        this.checkTime = checkTime;
    }

    protected Poker(Parcel in) {
        inputPoker = in.readString();
        pokerPosition = in.readString();
        strongPoker = in.readByte() != 0;
    }

    public String getInputPoker() {
        return inputPoker;
    }

    public void setInputPoker(String inputPoker) {
        this.inputPoker = inputPoker;
    }

    public String getPokerPosition() {
        return pokerPosition;
    }

    public void setPokerPosition(String pokerPosition) {
        this.pokerPosition = pokerPosition;
    }

    public boolean isStrongPoker() {
        return strongPoker;
    }

    public void setStrongPoker(boolean strongPoker) {
        this.strongPoker = strongPoker;
    }

    public String getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(String checkTime) {
        this.checkTime = checkTime;
    }

    public static final Creator<Poker> CREATOR = new Creator<Poker>() {
        @Override
        public Poker createFromParcel(Parcel in) {
            return new Poker(in);
        }

        @Override
        public Poker[] newArray(int size) {
            return new Poker[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(inputPoker);
        dest.writeString(pokerPosition);
        dest.writeByte((byte) (strongPoker ? 1 : 0));
    }
}
