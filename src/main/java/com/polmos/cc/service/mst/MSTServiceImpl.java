package com.polmos.cc.service.mst;

import com.polmos.cc.constants.BundleName;
import com.polmos.cc.constants.OperationType;
import com.polmos.cc.service.ResourceManager;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.inject.Inject;
import javax.json.JsonObject;
import org.jboss.logging.Logger;

/**
 *
 * @author RobicToNieMaKomu
 */
public class MSTServiceImpl implements MSTService {

     private static Logger logger = Logger.getLogger(MSTServiceImpl.class);
    
    @Inject
    private MSTUtils mstUtils;

    @Inject
    private FxParser parser;

    @Override
    public Map<String, Set<String>> generateMST(List<String> currencies, List<JsonObject> timeSeries, OperationType type) throws IOException{
        logger.info("Generating MST. Size of time Series:" + ((timeSeries != null) ? timeSeries.size() : null));
        float[][] correlationMx = mstUtils.generateCorrelationMx(currencies, parser.toFxTimeSeries(timeSeries), type);
        float[][] distanceMx = mstUtils.convertCorrelationMxToDistanceMx(correlationMx);
        return mstUtils.constructMst(currencies, distanceMx);
    }
}
