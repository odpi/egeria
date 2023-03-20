/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.context;


import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.governanceaction.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.integration.client.OpenIntegrationClient;
import org.odpi.openmetadata.frameworks.integration.contextmanager.PermittedSynchronization;
import org.odpi.openmetadata.frameworks.integration.properties.CatalogTarget;
import org.odpi.openmetadata.frameworks.integration.reports.IntegrationReportWriter;

import java.util.List;

/**
 * IntegrationContext is the base class for the integration context provided to the integration connector to provide access to open metadata
 * services.  Each integration service specializes this class to provide the method appropriate for the particular type of technology it
 * is supporting.
 *
 * This base class supports the common methods available to all types of integration connectors.
 */
public class IntegrationContext
{
    protected final OpenIntegrationClient    openIntegrationClient;
    protected final OpenMetadataClient       openMetadataStoreClient;
    protected final String                   userId;
    protected final String                   externalSourceGUID;
    protected final String                   externalSourceName;
    protected       boolean                  externalSourceIsHome    = true;
    protected final String                   integrationConnectorGUID;
    protected final PermittedSynchronization permittedSynchronization;

    private   final IntegrationGovernanceContext integrationGovernanceContext;
    protected final IntegrationReportWriter      integrationReportWriter;


    /**
     * Constructor handles standard values for all integration contexts.
     *
     * @param connectorId unique identifier of the connector (used to configure the event listener)
     * @param connectorName name of connector from config
     * @param connectorUserId userId for the connector
     * @param serverName name of the integration daemon
     * @param openIntegrationClient client for calling the metadata server
     * @param openMetadataStoreClient client for calling the metadata server
     * @param generateIntegrationReport should the connector generate an integration reports?
     * @param permittedSynchronization enum
     * @param externalSourceGUID unique identifier of the software server capability for the source of metadata
     * @param externalSourceName unique name of the software server capability for the source of metadata
     * @param integrationConnectorGUID unique identifier of the integration connector entity (maybe null)
     */
    public IntegrationContext(String                       connectorId,
                              String                       connectorName,
                              String                       connectorUserId,
                              String                       serverName,
                              OpenIntegrationClient        openIntegrationClient,
                              OpenMetadataClient           openMetadataStoreClient,
                              boolean                      generateIntegrationReport,
                              PermittedSynchronization     permittedSynchronization,
                              String                       externalSourceGUID,
                              String                       externalSourceName,
                              String                       integrationConnectorGUID)
    {
        this.openIntegrationClient        = openIntegrationClient;
        this.openMetadataStoreClient      = openMetadataStoreClient;
        this.permittedSynchronization     = permittedSynchronization;
        this.userId                       = connectorUserId;
        this.externalSourceGUID           = externalSourceGUID;
        this.externalSourceName           = externalSourceName;
        this.integrationConnectorGUID     = integrationConnectorGUID;

        if (generateIntegrationReport)
        {
            this.integrationReportWriter = new IntegrationReportWriter(serverName,
                                                                       connectorId,
                                                                       connectorName,
                                                                       userId,
                                                                       openIntegrationClient,
                                                                       openMetadataStoreClient);
        }
        else
        {
            this.integrationReportWriter = null;
        }

        this.integrationGovernanceContext = constructIntegrationGovernanceContext(openMetadataStoreClient,
                                                                                  connectorUserId,
                                                                                  externalSourceGUID,
                                                                                  externalSourceName,
                                                                                  integrationReportWriter);

    }



    /**
     * Return a new  integrationGovernanceContext for a specific connector.
     *
     * @param openMetadataStore client implementation
     * @param userId calling user
     * @param externalSourceGUID unique identifier for external source (or null)
     * @param externalSourceName unique name for external source (or null)
     * @param integrationReportWriter report writer (maybe null)
     * @return new context
     */
    private IntegrationGovernanceContext constructIntegrationGovernanceContext(OpenMetadataClient      openMetadataStore,
                                                                               String                  userId,
                                                                               String                  externalSourceGUID,
                                                                               String                  externalSourceName,
                                                                               IntegrationReportWriter integrationReportWriter)
    {
        if (openMetadataStoreClient != null)
        {
            OpenMetadataAccess      openMetadataAccess      = new OpenMetadataAccess(openMetadataStore,
                                                                                     userId,
                                                                                     externalSourceGUID,
                                                                                     externalSourceName,
                                                                                     integrationReportWriter);
            MultiLanguageManagement multiLanguageManagement = new MultiLanguageManagement(openMetadataStore, userId);
            StewardshipAction       stewardshipAction       = new StewardshipAction(openMetadataStore, userId);
            ValidMetadataValues     validMetadataValues     = new ValidMetadataValues(openMetadataStore, userId);

            return new IntegrationGovernanceContext(openMetadataAccess, multiLanguageManagement, stewardshipAction, validMetadataValues);
        }

        return null;
    }



    /**
     * Retrieve the identifiers of the metadata elements identified as catalog targets with an integration connector.
     * Each catalog target may be configured with an optional symbolic name to guide the integration connector on how to use
     * the catalog targets.
     *
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     *
     * @return list of named element headers
     * @throws InvalidParameterException one of the parameters is null or invalid,
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the integration connector definition.
     */
    public List<CatalogTarget> getCatalogTargets(int     startingFrom,
                                                 int     maximumResults) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        if ((openIntegrationClient != null) && (integrationConnectorGUID != null))
        {
            return openIntegrationClient.getCatalogTargets(userId, integrationConnectorGUID, startingFrom, maximumResults);
        }

        return null;
    }



    /**
     * Return the qualified name of the software capability that represents an external source of metadata.  Used to control external provenance.
     * If null the provenance is LOCAL_COHORT.
     *
     * @return  string name
     */
    public String getMetadataSourceQualifiedName()
    {
        return externalSourceName;
    }


    /**
     * Return the permitted synchronization direction.  This setting may affect which method in the context are available to the integration
     * connector.
     *
     * @return permittedSynchronization enum
     */
    public PermittedSynchronization getPermittedSynchronization()
    {
        return permittedSynchronization;
    }


    /**
     * Return the integration governance context that provides access to various Governance Action Framework (GAF) function.
     *
     * @return IntegrationGovernanceContext context object
     */
    public IntegrationGovernanceContext getIntegrationGovernanceContext()
    {
        return  integrationGovernanceContext;
    }


    /**
     * Set whether an integration report should be assembled and published.
     * This allows the integration connector to turn off/on integration report writing.
     * It only has an effect if the connector is configured to allow report writing
     *
     * @param flag required behaviour
     */
    public void setActiveReportPublishing(boolean flag)
    {
        if (integrationReportWriter != null)
        {
            integrationReportWriter.setActiveReportPublishing(flag);
        }
    }


    /**
     * Clear the report properties ready for a new report.  This is not
     * normally needed by the integration connector since it is called by the
     * connector handler just before refresh.  It is also called by publish report.
     */
    public void startRecording()
    {
        if (integrationReportWriter != null)
        {
            integrationReportWriter.startRecording();
        }
    }


    /**
     * Save the relationship between an element and its anchor.  This is called by the methods of the context that
     * create, update, archive or delete elements.
     *
     * @param elementGUID unique identifier of the element
     * @param anchorGUID unique identifier of the associated anchor
     */
    protected void reportAnchorGUID(String elementGUID, String anchorGUID)
    {
        if (integrationReportWriter != null)
        {
            integrationReportWriter.setAnchor(elementGUID, anchorGUID);
        }
    }


    /**
     * Attempt to use the parent's GUID to discover the relationship between an element and its anchor.  This is used to identify
     * which report that the element should be reported under.
     *
     * @param elementGUID unique identifier of the element
     * @param parentGUID unique identifier of the associated parent
     */
    protected void reportParentGUID(String elementGUID, String parentGUID)
    {
        if (integrationReportWriter != null)
        {
            integrationReportWriter.setParent(elementGUID, parentGUID);
        }
    }


    /**
     * Save information about a newly created element.
     *
     * @param elementGUID unique identifier of the element
     */
    protected void reportElementCreation(String elementGUID)
    {
        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementCreation(elementGUID);
        }
    }


    /**
     * Save information about a newly updated element.
     *
     * @param elementGUID unique identifier of the element
     */
    protected void reportElementUpdate(String elementGUID)
    {
        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(elementGUID);
        }
    }


    /**
     * Save information about a newly archived or deleted element.
     *
     * @param elementGUID unique identifier of the element
     */
    protected void reportElementDelete(String elementGUID)
    {
        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementDelete(elementGUID);
        }
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
        if (integrationReportWriter != null)
        {
            integrationReportWriter.publishReport();
        }
    }
}
