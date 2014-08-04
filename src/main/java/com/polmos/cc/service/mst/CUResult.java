package com.polmos.cc.service.mst;

/**
 *
 * @author RobicToNieMaKomu
 */
public class CUResult {

    private final int i;
    private final int j;
    private final float correlation;

    CUResult(int i, int j, float corr) {
        this.i = i;
        this.j = j;
        this.correlation = corr;
    }

    public float getCorrelation() {
        return correlation;
    }

    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }
}