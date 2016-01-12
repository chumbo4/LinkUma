package com.chumboapp.linkuma.models;

/**
 * Created by MANOLO on 09/01/2016.
 */
public class Event {

    private String mName = null;

    private String mDateStart = null; // FORMAT YYYY/MM/DD
    private String mDateEnd = null;
    private String mHourStart = null; // FORMAT HH:mm
    private String mHourEnd = null;
    private String mDescription = null;
    private String mDirection = null;
    private String mOrganizerName = null;
    private String mOrganizerWeb = null;
    private String mEventWeb = null;   // Web event
    private String mAuthor = null;

    public Event(String mAuthor, String mEventWeb, String mOrganizerWeb, String mOrganizerName,
                 String mDirection, String mDescription, String mHourEnd, String mHourStart,
                 String mDateEnd, String mDateStart, String mName) {
        this.mAuthor = mAuthor;
        this.mEventWeb = mEventWeb;
        this.mOrganizerWeb = mOrganizerWeb;
        this.mOrganizerName = mOrganizerName;
        this.mDirection = mDirection;
        this.mDescription = mDescription;
        this.mHourEnd = mHourEnd;
        this.mHourStart = mHourStart;
        this.mDateEnd = mDateEnd;
        this.mDateStart = mDateStart;
        this.mName = mName;
    }

    public String getmEventWeb() {
        return mEventWeb;
    }

    public void setmEventWeb(String mEventWeb) {
        this.mEventWeb = mEventWeb;
    }

    public String getmOrganizerWeb() {
        return mOrganizerWeb;
    }

    public void setmOrganizerWeb(String mOrganizerWeb) {
        this.mOrganizerWeb = mOrganizerWeb;
    }

    public String getmOrganizerName() {
        return mOrganizerName;
    }

    public void setmOrganizerName(String mOrganizerName) {
        this.mOrganizerName = mOrganizerName;
    }

    public String getmDirection() {
        return mDirection;
    }

    public void setmDirection(String mDirection) {
        this.mDirection = mDirection;
    }

    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getmHourEnd() {
        return mHourEnd;
    }

    public void setmHourEnd(String mHourEnd) {
        this.mHourEnd = mHourEnd;
    }

    public String getmHourStart() {
        return mHourStart;
    }

    public void setmHourStart(String mHourStart) {
        this.mHourStart = mHourStart;
    }

    public String getmDateEnd() {
        return mDateEnd;
    }

    public void setmDateEnd(String mDateEnd) {
        this.mDateEnd = mDateEnd;
    }

    public String getmDateStart() {
        return mDateStart;
    }

    public void setmDateStart(String mDateStart) {
        this.mDateStart = mDateStart;
    }

    public String getmAuthor() {
        return mAuthor;
    }

    public void setmAuthor(String mAuthor) {
        this.mAuthor = mAuthor;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }
}
