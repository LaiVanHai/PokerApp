package jp.co.netprotections.pokerapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class PokerCheckHistory implements Parcelable {
    private String inputPoker;
    private String pokerPosition;
    private String checkedTime;

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

    public String getCheckedTime() {
        return checkedTime;
    }

    public void setCheckedTime(String checkedTime) {
        this.checkedTime = checkedTime;
    }

    public PokerCheckHistory(String inputPoker, String pokerPosition, String checkedTime) {
        this.inputPoker = inputPoker;
        this.pokerPosition = pokerPosition;
        this.checkedTime = checkedTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(inputPoker);
        dest.writeString(pokerPosition);
        dest.writeString(checkedTime);
    }

    protected PokerCheckHistory(Parcel in) {
        inputPoker = in.readString();
        pokerPosition = in.readString();
        checkedTime = in.readString();
    }

    public static final Creator<PokerCheckHistory> CREATOR = new Creator<PokerCheckHistory>() {
        @Override
        public PokerCheckHistory createFromParcel(Parcel in) {
            return new PokerCheckHistory(in);
        }

        @Override
        public PokerCheckHistory[] newArray(int size) {
            return new PokerCheckHistory[size];
        }
    };
}
