/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.discoveryserver.server;

import org.odpi.openmetadata.frameworks.discovery.ffdc.DiscoveryEngineException;
import org.odpi.openmetadata.frameworks.discovery.properties.Annotation;
import org.odpi.openmetadata.frameworks.discovery.properties.DiscoveryReport;
import org.odpi.openmetadata.frameworks.discovery.properties.DiscoveryRequestStatus;

import java.util.List;

/**
 * DiscoveryEngineServices provides the external service implementation for a discovery engine.
 * Each method contains the discovery server name and the discovery engine identifier (guid).
 * The DiscoveryEngineServices locates the correct discovery engine instance within the correct
 * discovery server instance and delegates the request.
 */
public class DiscoveryEngineServices
{
    /**
     * Request the execution of a discovery service to explore a specific asset.
     *
     * @param serverName name of the discovery server.
     * @param discoveryEngineGUID unique identifier of the discovery engine.
     * @param assetGUID identifier of the asset to analyze.
     * @param assetType identifier of the type of asset to analyze - this determines which discovery service to run.
     *
     * @return unique id for the discovery request.
     *
     * @throws DiscoveryEngineException there was a problem detected by the discovery engine.
     */
    public  String discoverAsset(String   serverName,
                                 String   discoveryEngineGUID,
                                 String   assetGUID,
                                 String   assetType) throws DiscoveryEngineException
    {
        return null;
    }


    /**
     * Request the status of an executing discovery request.
     *
     * @param serverName name of the discovery server.
     * @param discoveryEngineGUID unique identifier of the discovery engine.
     * @param discoveryRequestGUID identifier of the discovery request.
     *
     * @return status enum
     *
     * @throws DiscoveryEngineException there was a problem detected by the discovery engine.
     */
    public DiscoveryRequestStatus getDiscoveryStatus(String   serverName,
                                                     String   discoveryEngineGUID,
                                                     String   discoveryRequestGUID) throws DiscoveryEngineException
    {
        return null;
    }


    /**
     * Request the discovery report for a discovery request that has completed.
     *
     * @param serverName name of the discovery server.
     * @param discoveryEngineGUID unique identifier of the discovery engine.
     * @param discoveryRequestGUID identifier of the discovery request.
     *
     * @return discovery report
     *
     * @throws DiscoveryEngineException there was a problem detected by the discovery engine.
     */
    public DiscoveryReport getDiscoveryReport(String   serverName,
                                              String   discoveryEngineGUID,
                                              String   discoveryRequestGUID) throws DiscoveryEngineException
    {
        return null;
    }


    /**
     * Return the annotations linked direction to the report.
     *
     * @param serverName name of the discovery server.
     * @param discoveryEngineGUID unique identifier of the discovery engine.
     * @param discoveryRequestGUID identifier of the discovery request.
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     *
     * @return list of annotations
     *
     * @throws DiscoveryEngineException there was a problem detected by the discovery engine.
     */
    public List<Annotation> getDiscoveryReportAnnotations(String   serverName,
                                                          String   discoveryEngineGUID,
                                                          String   discoveryRequestGUID,
                                                          int      startingFrom,
                                                          int      maximumResults) throws DiscoveryEngineException
    {
        return null;
    }


    /**
     * Return any annotations attached to this annotation.
     *
     * @param serverName name of the discovery server.
     * @param discoveryEngineGUID unique identifier of the discovery engine.
     * @param annotationGUID anchor annotation
     * @param startingFrom starting position in the list
     * @param maximumResults maximum number of annotations that can be returned.
     *
     * @return list of Annotation objects
     *
     * @throws DiscoveryEngineException there was a problem detected by the discovery engine.
     */
    public  List<Annotation>  getExtendedAnnotations(String   serverName,
                                                     String   discoveryEngineGUID,
                                                     String   annotationGUID,
                                                     int      startingFrom,
                                                     int      maximumResults) throws DiscoveryEngineException
    {
        return null;
    }


    /**
     * Retrieve a single annotation by unique identifier.  This call is typically used to retrieve the latest values
     * for an annotation.
     *
     * @param serverName name of the discovery server.
     * @param discoveryEngineGUID unique identifier of the discovery engine.
     * @param annotationGUID unique identifier of the annotation
     *
     * @return Annotation object
     *
     * @throws DiscoveryEngineException there was a problem detected by the discovery engine.
     */
    public  Annotation        getAnnotation(String   serverName,
                                            String   discoveryEngineGUID,
                                            String   annotationGUID) throws DiscoveryEngineException
    {
        return null;
    }
}
