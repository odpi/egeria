/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.discoveryengine.client;

import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.discovery.DiscoveryAnalysisReportStore;
import org.odpi.openmetadata.frameworks.discovery.properties.DiscoveryRequestStatus;

import java.util.Map;

/**
 * DiscoveryReportClient is the client interface for managing the values in a Discovery Analysis Report using the Discovery Engine OMAS
 * services.
 */
public class DiscoveryAnalysisReportClient extends DiscoveryAnalysisReportStore
{
    private DiscoveryEngineClient discoveryEngineClient;    /* Initialized in constructor */


    /**
     * Constructor for this client
     *
     * @param userId calling user
     * @param discoveryRequestStatus current status
     * @param assetGUID linked asset
     * @param analysisParameters parameters that configured the analysis
     * @param firstAnalysisStepName name of the first analysis step performed by the discovery service
     * @param reportQualifiedName qualified name of report
     * @param reportDisplayName display name of report
     * @param reportDescription description of report
     * @param discoveryEngineGUID hosting discovery engine
     * @param discoveryServiceGUID executing discovery service
     * @param discoveryEngineClient calling engine client
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem retrieving annotations from the annotation store.
     */
    public DiscoveryAnalysisReportClient(String                 userId,
                                         DiscoveryRequestStatus discoveryRequestStatus,
                                         String                 assetGUID,
                                         Map<String, String>    analysisParameters,
                                         String                 firstAnalysisStepName,
                                         String                 reportQualifiedName,
                                         String                 reportDisplayName,
                                         String                 reportDescription,
                                         String                 discoveryEngineGUID,
                                         String                 discoveryServiceGUID,
                                         DiscoveryEngineClient  discoveryEngineClient) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException
    {
        super.userId                  = userId;
        super.discoveryRequestStatus  = discoveryRequestStatus;
        super.reportQualifiedName     = reportQualifiedName;
        super.reportDisplayName       = reportDisplayName;
        super.reportDescription       = reportDescription;

        this.discoveryEngineClient    = discoveryEngineClient;

        super.discoveryReportGUID = discoveryEngineClient.createDiscoveryAnalysisReport(userId,
                                                                                        reportQualifiedName,
                                                                                        reportDisplayName,
                                                                                        reportDescription,
                                                                                        super.creationDate,
                                                                                        analysisParameters,
                                                                                        firstAnalysisStepName,
                                                                                        discoveryRequestStatus,
                                                                                        assetGUID,
                                                                                        discoveryEngineGUID,
                                                                                        discoveryServiceGUID,
                                                                                        null);
    }


    /**
     * Return the current status of the discovery request - this is stored in the report.
     *
     * @return current status of the discovery request/report
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem retrieving annotations from the annotation store.
     */
    @Override
    public DiscoveryRequestStatus getDiscoveryRequestStatus() throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {
        super.discoveryRequestStatus = this.discoveryEngineClient.getDiscoveryStatus(userId, discoveryReportGUID);

        return super.discoveryRequestStatus;
    }


    /**
     * Set up the current status of the discovery request - this is stored in the report so it can be monitored by the originator of the request.
     *
     * @param discoveryRequestStatus new status of the discovery request/report
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem retrieving annotations from the annotation store.
     */
    @Override
    public void setDiscoveryRequestStatus(DiscoveryRequestStatus discoveryRequestStatus) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException
    {
        this.discoveryEngineClient.setDiscoveryStatus(userId, discoveryReportGUID, discoveryRequestStatus);
        super.discoveryRequestStatus = discoveryRequestStatus;
    }
}
