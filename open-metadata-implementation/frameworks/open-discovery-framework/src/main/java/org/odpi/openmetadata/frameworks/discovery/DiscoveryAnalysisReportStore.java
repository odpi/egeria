/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.discovery;

import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.discovery.properties.DiscoveryRequestStatus;

import java.util.Date;

/**
 * DiscoveryAnalysisReportStore provides a client to the open metadata repository that manages the content
 * of the discovery analysis report.
 * It is part of the DiscoveryAnnotationStore.
 */
public abstract class DiscoveryAnalysisReportStore
{
    protected String                     userId                 = null;
    protected String                     discoveryReportGUID    = null;
    protected DiscoveryRequestStatus     discoveryRequestStatus = null;

    protected String                     reportQualifiedName = null;
    protected String                     reportDisplayName   = null;
    protected String                     reportDescription   = null;

    protected Date                       creationDate = new Date();

    protected String                     analysisStep = null;

    /**
     * Default constructor
     */
    protected DiscoveryAnalysisReportStore()
    {
    }


    /**
     * Return the report identifier for this discovery context.  Any new annotations added to tis discovery context
     * will be linked to this report.
     *
     * @return unique identifier (guid) of the new discovery report.
     */
    public String getDiscoveryReportGUID()
    {
        return discoveryReportGUID;
    }


    /**
     * Return the current status of the discovery request - this is stored in the report.
     *
     * @return current status of the discovery request/report
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem retrieving annotations from the annotation store.
     */
    public DiscoveryRequestStatus getDiscoveryRequestStatus() throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {
        return discoveryRequestStatus;
    }


    /**
     * Set up the current status of the discovery request - this is stored in the report so it can be monitored by the originator of the request.
     *
     * @param discoveryRequestStatus new status of the discovery request/report
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem retrieving annotations from the annotation store.
     * */
    public void setDiscoveryRequestStatus(DiscoveryRequestStatus discoveryRequestStatus) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException
    {
        this.discoveryRequestStatus = discoveryRequestStatus;
    }


    /**
     * Return the locally defined analysis step.  This value is used in annotations generated in this phase.
     *
     * @return name of analysis step
     */
    public String getAnalysisStep()
    {
        return analysisStep;
    }


    /**
     * Set up the name of the current analysis step.
     *
     * @param analysisStep name
     */
    public void setAnalysisStep(String analysisStep)
    {
        this.analysisStep = analysisStep;
    }


    /**
     * Return the unique name of the discovery analysis report that will result from this discovery request.
     *
     * @return String report name
     */
    public String getReportQualifiedName()
    {
        return reportQualifiedName;
    }


    /**
     * Set up the unique name of the discovery analysis report that will result from this discovery request.
     * The discovery engine will set up a default fully-qualified name.  This method enables it to be over-ridden.
     *
     * @param reportName  String report name
     */
    public void setReportQualifiedName(String reportName)
    {
        this.reportQualifiedName = reportName;
    }


    /**
     * Return the display name of the discovery analysis report that will result from this discovery request.
     *
     * @return String report name
     */
    public String getReportDisplayName()
    {
        return reportDisplayName;
    }


    /**
     * Set up the display name of the discovery analysis report that will result from this discovery request.
     * The default name is null.
     *
     * @param reportName  String report name
     */
    public void setReportDisplayName(String reportName)
    {
        this.reportDisplayName = reportName;
    }


    /**
     * Return the description for the discovery analysis report that will result from this discovery request.
     * The default value is null.
     *
     * @return String report description
     */
    public String getReportDescription()
    {
        return reportDescription;
    }


    /**
     * Set up the description for the discovery analysis report that will result from this discovery request.
     *
     * @param reportDescription String report description
     */
    public void setReportDescription(String reportDescription)
    {
        this.reportDescription = reportDescription;
    }


    /**
     * Return the creation date for the discovery analysis report that will result from this discovery request.
     *
     * @return Date that the report was created.
     */
    public Date getCreationDate() {
        return creationDate;
    }
}
