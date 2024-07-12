/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.integration.reports;

import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.governanceaction.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.governanceaction.properties.AttachedClassification;
import org.odpi.openmetadata.frameworks.governanceaction.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.governanceaction.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.integration.client.OpenIntegrationClient;
import org.odpi.openmetadata.frameworks.governanceaction.properties.IntegrationReportProperties;

import java.util.*;

/**
 * IntegrationReportWriter is responsible for managing the assembly and production of IntegrationReports.
 * It offers methods to turn reporting on and off, define the time span (start capturing data/publish report) of a report; to record information that
 * should be included in the report.
 */
public class IntegrationReportWriter
{
    private boolean activeReportPublishing = true;

    private final String userId;
    private final Map<String, String> anchorMap = new HashMap<>();       // map of element GUIDs to their anchors
    private final Map<String, String> anchorTypeMap = new HashMap<>();   // map of anchor GUIDs to their type

    private Set<String>         createdElements = new HashSet<>();       // Set of elementGUIDs that have been created
    private Set<String>         updatedElements = new HashSet<>();       // Set of elementGUIDs that have been updated
    private Set<String>         deletedElements = new HashSet<>();       // Set of elementGUIDs that have been deleted
    private Map<String, String> additionalProperties = new HashMap<>();

    private Date startDate = null;
    private Date completionDate = null;

    private final String                serverName;
    private final String                connectorId;
    private final String                connectorName;

    private final OpenIntegrationClient openIntegrationClient;
    private final OpenMetadataClient    openMetadataStore;

    private final PropertyHelper        propertyHelper = new PropertyHelper();
    private final String                sourceName = this.getClass().getName();

    /**
     * Set up the integration report writer.
     *
     * @param serverName name of this integration daemon
     * @param connectorId identifier of this running integration connector instance
     * @param connectorName name of this integration connector
     * @param userId calling user
     * @param openIntegrationClient client used to publish reports.
     * @param openMetadataStore client used to find out about metadata elements.
     */
    public IntegrationReportWriter(String                serverName,
                                   String                connectorId,
                                   String                connectorName,
                                   String                userId,
                                   OpenIntegrationClient openIntegrationClient,
                                   OpenMetadataClient    openMetadataStore)
    {
        this.serverName            = serverName;
        this.connectorId           = connectorId;
        this.connectorName         = connectorName;
        this.userId                = userId;
        this.openIntegrationClient = openIntegrationClient;
        this.openMetadataStore     = openMetadataStore;
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
                /*
                 * A report needs to be created for each Anchor.  First go through the elements
                 * that have been recorded and ensure we have the full list of anchors.
                 */
                getAllAnchors(createdElements);
                getAllAnchors(updatedElements);
                getAllAnchors(deletedElements);

                /*
                 * Now build and publish a report for each anchor.
                 */
                for (String anchorGUID : anchorMap.values())
                {
                    IntegrationReportProperties report = new IntegrationReportProperties();

                    report.setServerName(serverName);
                    report.setConnectorId(connectorId);
                    report.setConnectorName(connectorName);
                    report.setRefreshStartDate(startDate);
                    report.setRefreshCompletionDate(completionDate);
                    report.setAdditionalProperties(additionalProperties);

                    List<String> elementList = getAllAnchoredElements(anchorGUID, createdElements);
                    report.setCreatedElements(elementList);

                    elementList = getAllAnchoredElements(anchorGUID, updatedElements);
                    report.setUpdatedElements(elementList);

                    elementList = getAllAnchoredElements(anchorGUID, deletedElements);
                    report.setDeletedElements(elementList);

                    openIntegrationClient.publishIntegrationReport(userId, anchorGUID, report);
                }
            }
        }

        startRecording();
    }


    /**
     * Retrieve the anchors for all the reported elements.
     *
     * @param elementSet set of elementGUIDs
     *
     * @throws UserNotAuthorizedException the user is not authorized
     * @throws PropertyServerException there is a problem communicating with the metadata server (or it has a logic error).
     */
    private void getAllAnchors(Set<String> elementSet) throws UserNotAuthorizedException,
                                                              PropertyServerException
    {
        for (String elementGUID : elementSet)
        {
            getAnchor(elementGUID);
        }
    }


    /**
     * Return the list of elements that are associated with the supplied anchorGUID.
     *
     * @param anchorGUID anchor to test against
     * @param elementSet set of elementGUIDs
     * @return list of element GUIDs or null
     */
    private List<String> getAllAnchoredElements(String      anchorGUID,
                                                Set<String> elementSet)
    {
        List<String> anchoredElements = new ArrayList<>();
        for (String elementGUID : elementSet)
        {
            String elementAnchorGUID = anchorMap.get(elementGUID);

            if (anchorGUID.equals(elementAnchorGUID))
            {
                anchoredElements.add(elementGUID);
            }
        }

        if (!anchoredElements.isEmpty())
        {
            return anchoredElements;
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
     * Save the relationship between an element and its anchor.  This is used to identify
     * which report that the element should be reported under.
     *
     * @param elementGUID unique identifier of the element
     * @param anchorGUID unique identifier of the associated anchor
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setAnchor(String elementGUID,
                          String anchorGUID) throws InvalidParameterException,
                                                    PropertyServerException,
                                                    UserNotAuthorizedException
    {
        anchorMap.put(elementGUID, anchorGUID);

        if (anchorTypeMap.get(anchorGUID) != null)
        {
            OpenMetadataElement anchorElement = openMetadataStore.getMetadataElementByGUID(userId,
                                                                                           anchorGUID,
                                                                                           false,
                                                                                           false,
                                                                                           null);
            if (anchorElement != null)
            {
                anchorTypeMap.put(anchorGUID, anchorElement.getType().getTypeName());
            }
        }
    }


    /**
     * Save the relationship between an element and its anchor.  This is used to identify
     * which report that the element should be reported under.
     *
     * @param elementGUID unique identifier of the element
     * @param anchorGUID unique identifier of the associated anchor
     * @param anchorTypeName type of the associated anchor
     */
    public void setAnchor(String elementGUID,
                          String anchorGUID,
                          String anchorTypeName)
    {
        anchorMap.put(elementGUID, anchorGUID);
        anchorTypeMap.put(anchorGUID, anchorTypeName);
    }


    /**
     * Attempt to use the parent's GUID to discover the relationship between an element and its anchor.  This is used to identify
     * which report that the element should be reported under.
     *
     * @param elementGUID unique identifier of the element
     * @param parentGUID unique identifier of the associated parent
     */
    public void setParent(String elementGUID, String parentGUID)
    {
        String parentAnchorGUID = anchorMap.get(parentGUID);

        if (parentAnchorGUID != null)
        {
            anchorMap.put(elementGUID, parentAnchorGUID);
        }
    }


    /**
     * Retrieve the anchor GUID for a particular element.  This should have been determined already using setAnchor()
     * but if not known then the element is retrieved to find out its anchor.
     *
     * @param elementGUID element to investigate.
     *
     * @throws UserNotAuthorizedException the user is not authorized
     * @throws PropertyServerException there is a problem communicating with the metadata server (or it has a logic error).
     */
    private void getAnchor(String elementGUID) throws UserNotAuthorizedException,
                                                      PropertyServerException
    {
        final String methodName = "getAnchor";

        if (! anchorMap.containsKey(elementGUID))
        {
            /*
             * Retrieve the metadata element with the widest possible options.
             */
            OpenMetadataElement metadataElement;
            try
            {
                metadataElement = openMetadataStore.getMetadataElementByGUID(userId, elementGUID, true, true, null);
            }
            catch (InvalidParameterException notKnown)
            {
                metadataElement = null;
            }

            if (metadataElement != null)
            {
                List<AttachedClassification> classifications = metadataElement.getClassifications();

                if (classifications != null)
                {
                    for (AttachedClassification classification : classifications)
                    {
                        if (classification != null)
                        {
                            if ("Anchors".equals(classification.getClassificationName()))
                            {
                                String anchorGUIDPropertyName = "anchorGUID";
                                String anchorTypeNamePropertyName = "anchorTypeName";
                                String anchorGUID = propertyHelper.getStringProperty(sourceName,
                                                                                     anchorGUIDPropertyName,
                                                                                     classification.getClassificationProperties(),
                                                                                     methodName);
                                String anchorTypeName = propertyHelper.getStringProperty(sourceName,
                                                                                         anchorTypeNamePropertyName,
                                                                                         classification.getClassificationProperties(),
                                                                                         methodName);

                                if (anchorGUID == null)
                                {
                                    anchorGUID = elementGUID;
                                    anchorTypeName = metadataElement.getType().getTypeName();
                                }

                                setAnchor(elementGUID, anchorGUID, anchorTypeName);
                            }
                        }
                    }
                }
            }
        }
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
