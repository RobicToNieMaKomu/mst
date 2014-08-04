package com.polmos.cc.service.mst;

import com.polmos.cc.constants.OperationType;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

/**
 *
 * @author RobicToNieMaKomu
 */
public class CorrelationUnit implements Callable<CUResult> {

    private final List<TimeWindow> timeSeries;
    private final CUParameters params;
    private final OperationType type;
    private final int i;
    private final int j;

    public CorrelationUnit(List<TimeWindow> timeSeries, CUParameters params, OperationType type, int i, int j) {
        this.timeSeries = timeSeries;
        this.params = params;
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
            ExRate exRateA = timeWindow.forCurrency(params.getCurrA());
            ExRate exRateB = timeWindow.forCurrency(params.getCurrB());
            if (exRateA != null && exRateB != null) {
                float rA = exRateA.getValue(type);
                float rB = exRateB.getValue(type);
                numerator += (rA - params.getAvgA()) * (rB - params.getAvgB());
                sa += (rA - params.getAvgA()) * (rA - params.getAvgA());
                sb += (rB - params.getAvgB()) * (rB - params.getAvgB());
            }
        }
        float denominator = (sa != 0 && sb != 0) ? (float) (Math.sqrt(sa) * Math.sqrt(sb)) : 1;
        return new CUResult(i, j, numerator / denominator);
    }
}