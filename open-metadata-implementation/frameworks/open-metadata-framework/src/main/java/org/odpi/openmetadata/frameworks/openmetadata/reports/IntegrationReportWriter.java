/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.reports;

import org.odpi.openmetadata.frameworks.openmetadata.client.IntegrationReportClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;

import java.util.*;

/**
 * IntegrationReportWriter is responsible for managing the assembly and production of IntegrationReports.
 * It offers methods to turn reporting on and off, define the time span (start capturing data/publish report)
 * of a report; to record information that should be included in the report.
 */
public class IntegrationReportWriter
{
    private boolean activeReportPublishing = true;

    private final String userId;

    private Set<String>         createdElements = new HashSet<>();       // Set of elementGUIDs that have been created
    private Set<String>         updatedElements = new HashSet<>();       // Set of elementGUIDs that have been updated
    private Set<String>         deletedElements = new HashSet<>();       // Set of elementGUIDs that have been deleted
    private Map<String, String> additionalProperties = new HashMap<>();

    private Date startDate = null;
    private Date completionDate = null;

    private final String serverName;
    private final String connectorId;
    private final String integrationConnectorGUID;
    private final String connectorName;

    private final IntegrationReportClient integrationReportClient;


    /**
     * Set up the integration report writer.
     *
     * @param localServerName name of this integration daemon
     * @param connectorId identifier of this running connector instance
     * @param connectorGUID identifier of the  connector entity
     * @param connectorName name of this connector
     * @param userId calling user
     * @param integrationReportClient client used to publish reports.
     */
    public IntegrationReportWriter(String                  localServerName,
                                   String                  connectorId,
                                   String                  connectorGUID,
                                   String                  connectorName,
                                   String                  userId,
                                   IntegrationReportClient integrationReportClient)
    {
        this.serverName               = localServerName;
        this.integrationConnectorGUID = connectorGUID;
        this.connectorId              = connectorId;
        this.connectorName            = connectorName;
        this.userId                  = userId;
        this.integrationReportClient = integrationReportClient;
    }


    /**
     * Set up properties ready for a new report.
     */
    public void startRecording()
    {
        startDate = new Date();
        completionDate = null;

        createdElements = new HashSet<>();
        updatedElements = new HashSet<>();
        deletedElements = new HashSet<>();
        additionalProperties = new HashMap<>();
    }


    /**
     * Assemble the data collected and write out a report (if configured).
     *
     * @throws InvalidParameterException an invalid property has been passed
     * @throws UserNotAuthorizedException the user is not authorized
     * @throws PropertyServerException there is a problem communicating with the metadata server (or it has a logic error).
     */
    public void publishReport() throws InvalidParameterException,
                                       UserNotAuthorizedException,
                                       PropertyServerException
    {
        if (activeReportPublishing)
        {
            /*
             * A report is only created if elements have changed.
             */
            if ((!createdElements.isEmpty()) || (!updatedElements.isEmpty()) || (!deletedElements.isEmpty()))
            {
                IntegrationReportProperties report = new IntegrationReportProperties();

                report.setServerName(serverName);
                report.setConnectorId(connectorId);
                report.setConnectorName(connectorName);
                report.setRefreshStartDate(startDate);
                report.setRefreshCompletionDate(completionDate);
                report.setAdditionalProperties(additionalProperties);
                report.setCreatedElements(getReportElements(createdElements));
                report.setUpdatedElements(getReportElements(updatedElements));
                report.setDeletedElements(getReportElements(deletedElements));

                integrationReportClient.publishIntegrationReport(userId, integrationConnectorGUID, report);
            }
        }

        startRecording();
    }


    /**
     * Converts a set into a list.  This should not be required by using the ArrayList constructor returns an
     * java.lang.ArrayIndexOutOfBoundsException exception.
     *
     * @param elementGUIDs set of element GUIDs.
     * @return list of guids
     */
    private List<String> getReportElements(Set<String> elementGUIDs)
    {
        if (elementGUIDs != null && !elementGUIDs.isEmpty())
        {
            List<String> guids = new ArrayList<>();

            for (String guid : elementGUIDs)
            {
                if (guid != null)
                {
                    guids.add(guid);
                }
            }

            return guids;
        }

        return null;
    }

    /**
     * Set whether an integration report should be assembled and published.
     *
     * @param flag required behaviour
     */
    public void setActiveReportPublishing(boolean flag)
    {
        this.activeReportPublishing = flag;
    }


    /**
     * Save information about a newly created element.
     *
     * @param elementGUID unique identifier of the element
     */
    public void reportElementCreation(String elementGUID)
    {
        createdElements.add(elementGUID);
    }


    /**
     * Save information about a newly updated element.
     *
     * @param elementGUID unique identifier of the element
     */
    public void reportElementUpdate(String elementGUID)
    {
        updatedElements.add(elementGUID);
    }


    /**
     * Save information about a newly archived or deleted element.
     *
     * @param elementGUID unique identifier of the element
     */
    public void reportElementDelete(String elementGUID)
    {
        deletedElements.add(elementGUID);
    }
}
