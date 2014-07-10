package com.polmos.cc.service.mst;

import com.polmos.cc.constants.OperationType;
import java.util.List;
import java.util.concurrent.Callable;

/**
 *
 * @author RobicToNieMaKomu
 */
public class CorrelationUnit implements Callable<CUResult> {

    private final List<TimeWindow> timeSeries;
    private final String currA;
    private final String currB;
    private final float avgA;
    private final float avgB;
    private final OperationType type;
    private final int i;
    private final int j;

    public CorrelationUnit(List<TimeWindow> timeSeries, String currA, String currB, float avgA, float avgB, OperationType type, int i, int j) {
        this.timeSeries = timeSeries;
        this.currA = currA;
        this.currB = currB;
        this.avgA = avgA;
        this.avgB = avgB;
        this.type = type;
        this.i = i;
        this.j = j;
    }

    @Override
    public CUResult call() throws Exception {
        float numerator = 0;
        float sa = 0;
        float sb = 0;
        for (TimeWindow timeWindow : timeSeries) {
            ExRate exRateA = timeWindow.forCurrency(currA);
            ExRate exRateB = timeWindow.forCurrency(currB);
            if (exRateA != null && exRateB != null) {
                float rA = exRateA.getValue(type);
                float rB = exRateB.getValue(type);
                numerator += (rA - avgA) * (rB - avgB);
                sa += (rA - avgA) * (rA - avgA);
                sb += (rB - avgB) * (rB - avgB);
            }
        }
        float denominator = (sa != 0 && sb != 0) ? (float) (Math.sqrt(sa) * Math.sqrt(sb)) : 1;
        return new CUResult(i, j, numerator / denominator);
    }
}

class CUResult {

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
