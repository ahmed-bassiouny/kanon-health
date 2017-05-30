package com.germanitlab.kanonhealth.models;

/**
 * Created by Mo on 3/6/17.
 */

public class Inquiry {

    private String firstLevel;
    private String secondLevel;
    private String thirdLevel;

    public void setSecondLevel(String secondLevel) {
        this.secondLevel = secondLevel;
    }

    public void setThirdLevel(String thirdLevel) {
        this.thirdLevel = thirdLevel;
    }


    public void setFirstLevel(String firstLevel) {

        this.firstLevel = firstLevel;
    }

    public String getFirstLevel() {
        return firstLevel;
    }

    public String getSecondLevel() {
        return secondLevel;
    }

    public String getThirdLevel() {
        return thirdLevel;
    }
}
