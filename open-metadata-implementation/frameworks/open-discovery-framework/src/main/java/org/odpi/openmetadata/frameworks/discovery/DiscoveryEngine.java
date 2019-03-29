/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.discovery;

import org.odpi.openmetadata.frameworks.discovery.ffdc.DiscoveryEngineException;
import org.odpi.openmetadata.frameworks.discovery.properties.Annotation;
import org.odpi.openmetadata.frameworks.discovery.properties.DiscoveryReport;
import org.odpi.openmetadata.frameworks.discovery.properties.DiscoveryRequestStatus;

import java.util.List;

/**
 * DiscoveryEngine provides the interface for a discovery engine.  The discovery engine runs inside a discovery server.
 * The discovery server provides the server runtime environment for the discovery engine along with the interfaces for
 * configuring the discovery engine.
 */
public abstract class DiscoveryEngine
{
    /**
     * Request the execution of a discovery service to explore a specific asset.
     *
     * @param assetGUID identifier of the asset to analyze.
     * @param assetType identifier of the type of asset to analyze - this determines which discovery service to run.
     *
     * @return unique id for the discovery request.
     *
     * @throws DiscoveryEngineException there was a problem detected by the discovery engine.
     */
    public abstract String discoverAsset(String   assetGUID,
                                         String   assetType) throws DiscoveryEngineException;


    /**
     * Request the status of an executing discovery request.
     *
     * @param discoveryRequestGUID identifier of the discovery request.
     *
     * @return status enum
     *
     * @throws DiscoveryEngineException there was a problem detected by the discovery engine.
     */
    public abstract DiscoveryRequestStatus getDiscoveryStatus(String     discoveryRequestGUID) throws DiscoveryEngineException;


    /**
     * Request the discovery report for a discovery request that has completed.
     *
     * @param discoveryRequestGUID identifier of the discovery request.
     *
     * @return discovery report
     *
     * @throws DiscoveryEngineException there was a problem detected by the discovery engine.
     */
    public abstract DiscoveryReport   getDiscoveryReport(String     discoveryRequestGUID) throws DiscoveryEngineException;


    /**
     * Return the annotations linked direction to the report.
     *
     * @param discoveryRequestGUID identifier of the discovery request.
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     *
     * @return list of annotations
     *
     * @throws DiscoveryEngineException there was a problem detected by the discovery engine.
     */
    public abstract List<Annotation>  getDiscoveryReportAnnotations(String     discoveryRequestGUID,
                                                                    int        startingFrom,
                                                                    int        maximumResults) throws DiscoveryEngineException;


    /**
     * Return any annotations attached to this annotation.
     *
     * @param annotationGUID anchor annotation
     * @param startingFrom starting position in the list
     * @param maximumResults maximum number of annotations that can be returned.
     *
     * @return list of Annotation objects
     *
     * @throws DiscoveryEngineException there was a problem detected by the discovery engine.
     */
    public abstract List<Annotation>  getExtendedAnnotations(String     annotationGUID,
                                                             int        startingFrom,
                                                             int        maximumResults) throws DiscoveryEngineException;


    /**
     * Retrieve a single annotation by unique identifier.  This call is typically used to retrieve the latest values
     * for an annotation.
     *
     * @param annotationGUID unique identifier of the annotation
     *
     * @return Annotation object
     *
     * @throws DiscoveryEngineException there was a problem detected by the discovery engine.
     */
    public abstract Annotation        getAnnotation(String   annotationGUID) throws DiscoveryEngineException;
}
