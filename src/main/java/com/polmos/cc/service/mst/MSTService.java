package com.polmos.cc.service.mst;

import com.polmos.cc.constants.OperationType;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.json.JsonObject;

/**
 *
 * @author RobicToNieMaKomu
 */
public interface MSTService {
    Map<String, Set<String>> generateMST(List<JsonObject> timeSeries, OperationType type) throws IOException;
}
