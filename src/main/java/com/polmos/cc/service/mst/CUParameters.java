package com.polmos.cc.service.mst;

/**
 *
 * @author RobicToNieMaKomu
 */
public class CUParameters {

    private final String currA;
    private final String currB;
    private final float avgA;
    private final float avgB;

    public CUParameters(String currA, String currB, float avgA, float avgB) {
        this.currA = currA;
        this.currB = currB;
        this.avgA = avgA;
        this.avgB = avgB;
    }

    public float getAvgA() {
        return avgA;
    }

    public float getAvgB() {
        return avgB;
    }

    public String getCurrA() {
        return currA;
    }

    public String getCurrB() {
        return currB;
    }
}