/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.connectorcontext;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;

import org.odpi.openmetadata.frameworks.openmetadata.client.ConnectorActivityReportClient;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DeleteMethod;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.*;
import org.odpi.openmetadata.frameworks.openmetadata.fileclassifier.FileClassifier;
import org.odpi.openmetadata.frameworks.openmetadata.filelistener.FileDirectoryListenerInterface;
import org.odpi.openmetadata.frameworks.openmetadata.filelistener.FileListenerInterface;
import org.odpi.openmetadata.frameworks.openmetadata.filelistener.FilesListenerManager;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementClassification;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementHeader;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementType;
import org.odpi.openmetadata.frameworks.openmetadata.properties.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.contextevents.ContextEventImpactProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.contextevents.ContextEventProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.contextevents.DependentContextEventProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.contextevents.RelatedContextEventProperties;
import org.odpi.openmetadata.frameworks.openmetadata.reports.ConnectorActivityReportWriter;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;


import java.io.File;
import java.io.FileFilter;
import java.util.*;

public abstract class ConnectorContextBase
{
    protected final PropertyHelper propertyHelper = new PropertyHelper();

    protected final OpenMetadataClient      openMetadataClient;

    protected final String                  localServerName;
    protected final String                  localServiceName;
    protected final String                  externalSourceGUID;
    protected final String                  externalSourceName;
    protected final String                  connectorId;
    protected final String                  connectorName;
    protected final String                  connectorGUID;
    protected final String                  connectorUserId;
    protected final AuditLog                auditLog;
    protected final int                     maxPageSize;
    protected final DeleteMethod            defaultDeleteMethod;
    protected final boolean                       generateIntegrationReport;
    protected final ConnectorActivityReportWriter connectorActivityReportWriter;

    protected final FileClassifier       fileClassifier;
    private   final FilesListenerManager listenerManager;


    protected final OpenMetadataStore          openMetadataStore;
    private final   ActorProfileClient         actorProfileClient;
    private final   ActorRoleClient            actorRoleClient;
    private final   AssetClient                assetClient;
    private final   ConnectionClient           connectionClient;
    private final   ConnectorTypeClient        connectorTypeClient;
    private final   EndpointClient             endpointClient;
    private final   SoftwareCapabilityClient   softwareCapabilityClient;
    private final   ValidValueDefinitionClient validValueDefinitionClient;
    private final   GlossaryClient             glossaryClient;
    private final   GlossaryTermClient         glossaryTermClient;
    private final   MultiLanguageClient        multiLanguageClient;
    private final   SchemaTypeClient           schemaTypeClient;
    private final   SchemaAttributeClient      schemaAttributeClient;
    private final   ValidMetadataValuesClient  validMetadataValuesClient;


    private       boolean                     isActive = true;



    /**
     * Constructor.
     *
     * @param localServerName name of local server
     * @param localServiceName name of the service to call
     * @param externalSourceGUID metadata collection unique id
     * @param externalSourceName metadata collection unique name
     * @param connectorId id of this connector instance
     * @param connectorName name of this connector instance
     * @param connectorUserId userId to use when issuing open metadata requests
     * @param connectorGUID unique identifier of the connector element that describes this connector in the open metadata store(s)
     * @param generateIntegrationReport should the context generate an integration report?
     * @param openMetadataClient client to access open metadata store
     * @param auditLog logging destination
     * @param maxPageSize max number of results
     * @param deleteMethod default delete method
     */
    public ConnectorContextBase(String                   localServerName,
                                String                   localServiceName,
                                String                   externalSourceGUID,
                                String                   externalSourceName,
                                String                   connectorId,
                                String                   connectorName,
                                String                   connectorUserId,
                                String                   connectorGUID,
                                boolean                  generateIntegrationReport,
                                OpenMetadataClient       openMetadataClient,
                                AuditLog                 auditLog,
                                int                      maxPageSize,
                                DeleteMethod             deleteMethod)
    {
        this.localServerName           = localServerName;
        this.localServiceName          = localServiceName;
        this.externalSourceGUID        = externalSourceGUID;
        this.externalSourceName        = externalSourceName;
        this.connectorId               = connectorId;
        this.connectorName             = connectorName;
        this.connectorGUID             = connectorGUID;
        this.connectorUserId           = connectorUserId;
        this.auditLog                  = auditLog;
        this.maxPageSize               = maxPageSize;
        this.defaultDeleteMethod       = deleteMethod;

        this.generateIntegrationReport = generateIntegrationReport;

        this.openMetadataClient        = openMetadataClient;

        this.openMetadataStore = new OpenMetadataStore(this,
                                                       localServerName,
                                                       localServiceName,
                                                       connectorUserId,
                                                       connectorGUID,
                                                       externalSourceGUID,
                                                       externalSourceName,
                                                       openMetadataClient,
                                                       auditLog,
                                                       maxPageSize);

        this.actorProfileClient = new ActorProfileClient(this,
                                                         localServerName,
                                                         localServiceName,
                                                         connectorUserId,
                                                         connectorGUID,
                                                         externalSourceGUID,
                                                         externalSourceName,
                                                         openMetadataClient,
                                                         auditLog,
                                                         maxPageSize);

        this.actorRoleClient = new ActorRoleClient(this,
                                                   localServerName,
                                                   localServiceName,
                                                   connectorUserId,
                                                   connectorGUID,
                                                   externalSourceGUID,
                                                   externalSourceName,
                                                   openMetadataClient,
                                                   auditLog,
                                                   maxPageSize);

        this.assetClient = new AssetClient(this,
                                           localServerName,
                                           localServiceName,
                                           connectorUserId,
                                           connectorGUID,
                                           externalSourceGUID,
                                           externalSourceName,
                                           openMetadataClient,
                                           auditLog,
                                           maxPageSize);

        this.connectionClient = new ConnectionClient(this,
                                                     localServerName,
                                                     localServiceName,
                                                     connectorUserId,
                                                     connectorGUID,
                                                     externalSourceGUID,
                                                     externalSourceName,
                                                     openMetadataClient,
                                                     auditLog,
                                                     maxPageSize);

        this.connectorTypeClient = new ConnectorTypeClient(this,
                                                           localServerName,
                                                           localServiceName,
                                                           connectorUserId,
                                                           connectorGUID,
                                                           externalSourceGUID,
                                                           externalSourceName,
                                                           openMetadataClient,
                                                           auditLog,
                                                           maxPageSize);

        this.endpointClient = new EndpointClient(this,
                                                 localServerName,
                                                 localServiceName,
                                                 connectorUserId,
                                                 connectorGUID,
                                                 externalSourceGUID,
                                                 externalSourceName,
                                                 openMetadataClient,
                                                 auditLog,
                                                 maxPageSize);

        this.softwareCapabilityClient = new SoftwareCapabilityClient(this,
                                                                     localServerName,
                                                                     localServiceName,
                                                                     connectorUserId,
                                                                     connectorGUID,
                                                                     externalSourceGUID,
                                                                     externalSourceName,
                                                                     openMetadataClient,
                                                                     auditLog,
                                                                     maxPageSize);

        this.schemaTypeClient = new SchemaTypeClient(this,
                                                     localServerName,
                                                     localServiceName,
                                                     connectorUserId,
                                                     connectorGUID,
                                                     externalSourceGUID,
                                                     externalSourceName,
                                                     openMetadataClient,
                                                     auditLog,
                                                     maxPageSize);

        this.schemaAttributeClient = new SchemaAttributeClient(this,
                                                               localServerName,
                                                               localServiceName,
                                                               connectorUserId,
                                                               connectorGUID,
                                                               externalSourceGUID,
                                                               externalSourceName,
                                                               openMetadataClient,
                                                               auditLog,
                                                               maxPageSize);

        this.validValueDefinitionClient = new ValidValueDefinitionClient(this,
                                                                         localServerName,
                                                                         localServiceName,
                                                                         connectorUserId,
                                                                         connectorGUID,
                                                                         externalSourceGUID,
                                                                         externalSourceName,
                                                                         openMetadataClient,
                                                                         auditLog,
                                                                         maxPageSize);

        this.glossaryClient = new GlossaryClient(this,
                                                 localServerName,
                                                 localServiceName,
                                                 connectorUserId,
                                                 connectorGUID,
                                                 externalSourceGUID,
                                                 externalSourceName,
                                                 openMetadataClient,
                                                 auditLog,
                                                 maxPageSize);

        this.glossaryTermClient = new GlossaryTermClient(this,
                                                         localServerName,
                                                         localServiceName,
                                                         connectorUserId,
                                                         connectorGUID,
                                                         externalSourceGUID,
                                                         externalSourceName,
                                                         openMetadataClient,
                                                         auditLog,
                                                         maxPageSize);

        this.multiLanguageClient = new MultiLanguageClient(this,
                                                           localServerName,
                                                           localServiceName,
                                                           connectorUserId,
                                                           connectorGUID,
                                                           externalSourceGUID,
                                                           externalSourceName,
                                                           openMetadataClient,
                                                           auditLog,
                                                           maxPageSize);

        this.validMetadataValuesClient = new ValidMetadataValuesClient(this,
                                                                       localServerName,
                                                                       localServiceName,
                                                                       connectorUserId,
                                                                       connectorGUID,
                                                                       externalSourceGUID,
                                                                       externalSourceName,
                                                                       openMetadataClient,
                                                                       auditLog,
                                                                       maxPageSize);

        this.fileClassifier            = new FileClassifier(this);
        this.listenerManager           = new FilesListenerManager(auditLog, connectorName);

        if (generateIntegrationReport)
        {
            this.connectorActivityReportWriter = new ConnectorActivityReportWriter(localServerName,
                                                                                   connectorId,
                                                                                   connectorGUID,
                                                                                   connectorName,
                                                                                   connectorUserId,
                                                                                   new ConnectorActivityReportClient(localServerName,
                                                                                                                     auditLog,
                                                                                                                     localServiceName,
                                                                                                                     openMetadataClient));
        }
        else
        {
            this.connectorActivityReportWriter = null;
        }
    }


    /* ========================================================
     * Return the different types of context clients. Each serves a particular type of metadata.
     */

    /**
     * Return the client for managing all types of metadata.
     *
     * @return connector context client
     * @throws UserNotAuthorizedException connector is disconnected
     */
    public OpenMetadataStore getOpenMetadataStore() throws UserNotAuthorizedException
    {
        final String methodName = "getOpenMetadataStore";

        validateIsActive(methodName);

        return openMetadataStore;
    }


    /**
     * Return the client for managing actor profiles.
     *
     * @return connector context client
     */
    public ActorProfileClient getActorProfileClient()
    {
        return actorProfileClient;
    }


    /**
     * Return the client for managing actor roles.
     *
     * @return connector context client
     */
    public ActorRoleClient getActorRoleClient()
    {
        return actorRoleClient;
    }


    /**
     * Return the client for managing assets of a specific subtype.
     *
     * @param specificTypeName override type name
     * @return connector context client
     */
    public AssetClient getAssetClient(String specificTypeName)
    {
        return new AssetClient(assetClient, specificTypeName);
    }


    /**
     * Return the client for managing assets.
     *
     * @return connector context client
     */
    public AssetClient getAssetClient()
    {
        return assetClient;
    }


    /**
     * Return the client for managing connections.
     *
     * @return connector context client
     */
    public ConnectionClient getConnectionClient()
    {
        return connectionClient;
    }


    /**
     * Return the client for managing endpoints.
     *
     * @return connector context client
     */
    public ConnectorTypeClient getConnectorTypeClient()
    {
        return connectorTypeClient;
    }


    /**
     * Return the client for managing endpoints.
     *
     * @return connector context client
     */
    public EndpointClient getEndpointClient()
    {
        return endpointClient;
    }



    /**
     * Return the client for managing assets of a specific subtype.
     *
     * @param specificTypeName override type name
     * @return connector context client
     */
    public SoftwareCapabilityClient getSoftwareCapabilityClient(String specificTypeName)
    {
        return new SoftwareCapabilityClient(softwareCapabilityClient, specificTypeName);
    }


    /**
     * Return the client for managing assets.
     *
     * @return connector context client
     */
    public SoftwareCapabilityClient getSoftwareCapabilityClient()
    {
        return softwareCapabilityClient;
    }


    /**
     * Return the client for managing schema types of a specific subtype.
     *
     * @param specificTypeName override type name
     * @return connector context client
     */
    public SchemaTypeClient getSchemaTypeClient(String specificTypeName)
    {
        return new SchemaTypeClient(schemaTypeClient, specificTypeName);
    }


    /**
     * Return the client for managing schema types.
     *
     * @return connector context client
     */
    public SchemaTypeClient getSchemaTypeClient()
    {
        return schemaTypeClient;
    }


    /**
     * Return the client for managing schema attributes of a specific subtype.
     *
     * @param specialistTypeName override type name
     * @return connector context client
     */
    public SchemaAttributeClient getSchemaAttributeClient(String specialistTypeName)
    {
        return new SchemaAttributeClient(schemaAttributeClient, specialistTypeName);
    }


    /**
     * Return the client for managing schema attributes.
     *
     * @return connector context client
     */
    public SchemaAttributeClient getSchemaAttributeClient()
    {
        return schemaAttributeClient;
    }


    /**
     * Return the client for managing valid value definitions.
     *
     * @return connector context client
     */
    public ValidValueDefinitionClient getValidValueDefinitionClient()
    {
        return validValueDefinitionClient;
    }

    /**
     * Return the client for managing glossaries.
     *
     * @return connector context client
     */
    public GlossaryClient getGlossaryClient()
    {
        return glossaryClient;
    }


    /**
     * Return the client for managing glossary terms.
     *
     * @return connector context client
     */
    public GlossaryTermClient getGlossaryTermClient()
    {
        return glossaryTermClient;
    }


    /**
     * Return the client for managing translations for properties of open metadata elements.
     *
     * @return connector context client
     */
    public MultiLanguageClient getMultiLanguageClient()
    {
        return multiLanguageClient;
    }



    /**
     * Return the client for managing valid values for open metadata properties.
     *
     * @return connector context client
     */
    public ValidMetadataValuesClient getValidMetadataValuesClient()
    {
        return validMetadataValuesClient;
    }


    /**
     * Return the name of the server where this connector is running
     *
     * @return string name
     */
    public String getLocalServerName()
    {
        return localServerName;
    }


    /**
     * Return the name of the service supporting this connector
     *
     * @return string name
     */
    public String getLocalServiceName()
    {
        return localServiceName;
    }


    /**
     * Return the id of this connector instance
     *
     * @return string name
     */
    public String getConnectorId()
    {
        return connectorId;
    }


    /**
     * Return the name of this connector
     *
     * @return string name
     */
    public String getConnectorName()
    {
        return connectorName;
    }


    /**
     * Return the userId for this connector.  It is used to determine if changes where made by this connector.
     * It should not be needed to issue calls to open metadata.
     *
     * @return string
     */
    public String getMyUserId()
    {
        return connectorUserId;
    }


    /**
     * Provide the report writer to the connector context clients.
     *
     * @return report writer or null
     */
    ConnectorActivityReportWriter getIntegrationReportWriter()
    {
        return connectorActivityReportWriter;
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
        if (connectorActivityReportWriter != null)
        {
            connectorActivityReportWriter.setActiveReportPublishing(flag);
        }
    }


    /**
     * Clear the report properties ready for a new report.  This is not
     * normally needed by the integration connector since it is called by the
     * connector handler just before refresh.  It is also called by publish report.
     */
    public void startRecording()
    {
        if (connectorActivityReportWriter != null)
        {
            connectorActivityReportWriter.startRecording();
        }
    }


    /**
     * Save information about a newly created element.
     *
     * @param elementGUID unique identifier of the element
     */
    protected void reportElementCreation(String elementGUID)
    {
        if (connectorActivityReportWriter != null)
        {
            connectorActivityReportWriter.reportElementCreation(elementGUID);
        }
    }


    /**
     * Save information about a newly updated element.
     *
     * @param elementGUID unique identifier of the element
     */
    protected void reportElementUpdate(String elementGUID)
    {
        if (connectorActivityReportWriter != null)
        {
            connectorActivityReportWriter.reportElementUpdate(elementGUID);
        }
    }


    /**
     * Save information about a newly archived or deleted element.
     *
     * @param elementGUID unique identifier of the element
     */
    protected void reportElementDelete(String elementGUID)
    {
        if (connectorActivityReportWriter != null)
        {
            connectorActivityReportWriter.reportElementDelete(elementGUID);
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
        if (connectorActivityReportWriter != null)
        {
            connectorActivityReportWriter.publishReport();
        }
    }


    /**
     * Retrieve the anchorGUID from the Anchors classification.
     *
     * @param elementHeader element header where the classifications reside
     * @return anchorGUID or null
     */
    public String getAnchorGUID(ElementHeader elementHeader)
    {
        if (elementHeader != null)
        {
            ElementClassification anchorClassification = elementHeader.getAnchor();

            if (anchorClassification != null)
            {
                Map<String, Object> properties = anchorClassification.getClassificationProperties();

                if (properties != null)
                {
                    Object anchorGUID = properties.get(OpenMetadataProperty.ANCHOR_GUID.name);

                    if (anchorGUID != null)
                    {
                        return anchorGUID.toString();
                    }
                }
            }
        }

        return null;
    }


    /**
     * Retrieve the anchorGUID from the Anchors classification.
     *
     * @param openMetadataElement element header where the classifications reside
     * @return anchorGUID or null
     */
    public String getAnchorGUID(OpenMetadataElement openMetadataElement)
    {
        final String methodName = "getAnchorGUID";

        if (openMetadataElement.getClassifications() != null)
        {
            for (AttachedClassification classification : openMetadataElement.getClassifications())
            {
                if (classification.getClassificationName().equals(OpenMetadataType.ANCHORS_CLASSIFICATION.typeName))
                {
                    return propertyHelper.getStringProperty(connectorName,
                                                            OpenMetadataProperty.ANCHOR_GUID.name,
                                                            classification.getClassificationProperties(),
                                                            methodName);
                }
            }
        }

        return null;
    }


    /**
     * Return the file classifier that uses reference data to describe a file.
     *
     * @return file classifier utility
     */
    public FileClassifier getFileClassifier()
    {
        return fileClassifier;
    }


    /* ========================================================
     * Register/unregister for inbound events from the file system
     */


    /**
     * Register a listener object that will be called each time a specific file is created, changed or deleted.
     *
     * @param listener      listener object
     * @param fileToMonitor name of the file to monitor
     *
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     */
    public void registerFileListener(FileListenerInterface listener,
                                     File fileToMonitor) throws InvalidParameterException
    {
        listenerManager.registerFileListener(listener, fileToMonitor);
    }


    /**
     * Unregister a listener object that will be called each time a specific file is created, changed or deleted.
     *
     * @param listener      listener object
     * @param fileToMonitor name of the file to unregister
     *
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     */
    public void unregisterFileListener(FileListenerInterface listener,
                                       File                  fileToMonitor) throws InvalidParameterException
    {
        listenerManager.unregisterFileListener(listener, fileToMonitor);
    }


    /**
     * Register a listener object that will be called each time a file is created, changed or deleted in a specific root directory.
     * The file filter lets you request that only certain types of files are returned.
     *
     * @param listener           listener object
     * @param directoryToMonitor details of the file directory to monitor
     * @param fileFilter         a file filter implementation that restricts the files/directories that will be returned to the listener
     *
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     */
    public void registerDirectoryListener(FileDirectoryListenerInterface listener,
                                          File                           directoryToMonitor,
                                          FileFilter fileFilter) throws InvalidParameterException
    {
        listenerManager.registerDirectoryListener(listener, directoryToMonitor, fileFilter);
    }


    /**
     * Unregister a listener object for the directory.
     *
     * @param listener           listener object
     * @param directoryToMonitor details of the file directory unregister
     *
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     */
    public void unregisterDirectoryListener(FileDirectoryListenerInterface listener,
                                            File                           directoryToMonitor) throws InvalidParameterException
    {
        listenerManager.unregisterDirectoryListener(listener, directoryToMonitor);
    }


    /**
     * Register a listener object that will be called each time a file is created, changed or deleted in a specific root directory
     * and any of its subdirectories.  The file filter lets you request that only certain types of files and/or directories are returned.
     *
     * @param listener           listener object
     * @param directoryToMonitor details of the root file directory to monitor from
     * @param fileFilter         a file filter implementation that restricts the files/directories that will be returned to the listener
     *
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     */
    public void registerDirectoryTreeListener(FileDirectoryListenerInterface listener,
                                              File                           directoryToMonitor,
                                              FileFilter                     fileFilter) throws InvalidParameterException
    {
        listenerManager.registerDirectoryTreeListener(listener, directoryToMonitor, fileFilter);
    }


    /**
     * Unregister a listener object for the directory.
     *
     * @param listener           listener object
     * @param directoryToMonitor details of the root file directory to unregister
     *
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     */
    public void unregisterDirectoryTreeListener(FileDirectoryListenerInterface listener,
                                                File                           directoryToMonitor) throws InvalidParameterException
    {
        listenerManager.unregisterDirectoryTreeListener(listener, directoryToMonitor);
    }


    /* ==============================================================
     * Controlling paging
     */

    /**
     * Returns the server configuration for the maximum number of elements that can be returned on a request.  It is used to control
     * paging.
     *
     * @return integer
     */
    public int getMaxPageSize()
    {
        return maxPageSize;
    }


    /* =============================
     * Working with types
     */


    /**
     * Understand the type of element.  It checks the type and super types.
     *
     * @param elementHeader element to validate
     * @param typeName type to test
     * @return boolean flag
     */
    public boolean isTypeOf(ElementHeader  elementHeader,
                            String         typeName)
    {
        return isTypeOf(elementHeader.getType(), typeName);
    }


    /**
     * Understand the type of element.  It checks the type and super types.
     *
     * @param elementType element to validate
     * @param typeName type to test
     * @return boolean flag
     */
    public boolean isTypeOf(ElementType elementType,
                            String       typeName)
    {
        if (elementType != null)
        {
            List<String> elementTypeNames = new ArrayList<>();

            elementTypeNames.add(elementType.getTypeName());
            if (elementType.getSuperTypeNames() != null)
            {
                elementTypeNames.addAll(elementType.getSuperTypeNames());
            }

            if (elementTypeNames.contains(typeName))
            {
                return true;
            }
        }

        return false;
    }



    /**
     * Create an incident report to capture the situation detected by this governance action service.
     * This incident report will be processed by other governance activities.
     *
     * @param qualifiedName unique identifier to give this new incident report
     * @param domainIdentifier governance domain associated with this action (0=ALL)
     * @param background description of the situation
     * @param impactedResources details of the resources impacted by this situation
     * @param previousIncidents links to previous incident reports covering this situation
     * @param incidentClassifiers initial classifiers for the incident report
     * @param additionalProperties additional arbitrary properties for the incident reports
     *
     * @return unique identifier of the resulting incident report
     *
     * @throws InvalidParameterException null or non-unique qualified name for the incident report
     * @throws UserNotAuthorizedException this governance action service is not authorized to create an incident report
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public String createIncidentReport(String                        qualifiedName,
                                       int                           domainIdentifier,
                                       String                        background,
                                       List<IncidentImpactedElement> impactedResources,
                                       List<IncidentDependency>      previousIncidents,
                                       Map<String, Integer>          incidentClassifiers,
                                       Map<String, String>           additionalProperties) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        return openMetadataClient.createIncidentReport(connectorUserId,
                                                       qualifiedName,
                                                       domainIdentifier,
                                                       background,
                                                       impactedResources,
                                                       previousIncidents,
                                                       incidentClassifiers,
                                                       additionalProperties,
                                                       connectorGUID);
    }

    /**
     * Create a To-Do request for someone to work on.
     *
     * @param toDoQualifiedName unique name for the to do.  (Could be the engine name and a guid?)
     * @param title short meaningful phrase for the person receiving the request
     * @param instructions further details on what to do
     * @param priority priority value (based on organization's scale)
     * @param dueDate date/time this needs to be completed
     * @param assignToGUID unique identifier for the recipient actor
     * @param actionTargetGUID unique identifier of the element to work on.
     * @param actionTargetName name of the element to work on.
     * @return unique identifier of new to do element
     * @throws InvalidParameterException either todoQualifiedName or assignedTo are null or not recognized
     * @throws UserNotAuthorizedException the governance action service is not authorized to create a to-do
     * @throws PropertyServerException there is a problem connecting to (or inside) the metadata store
     */
    public String openToDo(String toDoQualifiedName,
                           String title,
                           String instructions,
                           String toDoType,
                           int    priority,
                           Date   dueDate,
                           String assignToGUID,
                           String actionTargetGUID,
                           String actionTargetName) throws InvalidParameterException,
                                                           UserNotAuthorizedException,
                                                           PropertyServerException
    {
        final String methodName = "openToDo";

        final String toDoQualifiedNameParameterName = "toDoQualifiedName";
        final String assignToParameterName          = "assignToGUID";

        propertyHelper.validateMandatoryName(toDoQualifiedName, toDoQualifiedNameParameterName, methodName);
        propertyHelper.validateMandatoryName(assignToGUID, assignToParameterName, methodName);

        /*
         * Create the to do entity
         */
        NewActionTarget actionTarget = new NewActionTarget();
        actionTarget.setActionTargetGUID(actionTargetGUID);
        actionTarget.setActionTargetName(actionTargetName);

        List<NewActionTarget> actionTargets = new ArrayList<>();
        actionTargets.add(actionTarget);

        return this.openToDo(toDoQualifiedName,
                             title,
                             instructions,
                             toDoType,
                             priority,
                             dueDate,
                             null,
                             assignToGUID,
                             null,
                             actionTargets);
    }


    /**
     * Create a "To Do" request for someone to work on.
     *
     * @param qualifiedName unique name for the to do.  (Could be the engine name and a guid?)
     * @param title short meaningful phrase for the person receiving the request
     * @param instructions further details on what to do
     * @param category a category of to dos (for example, "data error", "access request")
     * @param priority priority value (based on organization's scale)
     * @param dueDate date/time this needs to be completed
     * @param additionalProperties additional arbitrary properties for the incident reports
     * @param assignToGUID unique identifier of the Actor element for the recipient
     * @param sponsorGUID unique identifier of the element that describes the rule, project that this is on behalf of
     * @param actionTargets the list of elements that should be acted upon
     *
     * @return unique identifier of new to do element
     *
     * @throws InvalidParameterException either todoQualifiedName or assignedTo are null or not recognized
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException there is a problem connecting to (or inside) the metadata store
     */
    public String openToDo(String                qualifiedName,
                           String                title,
                           String                instructions,
                           String                category,
                           int                   priority,
                           Date                  dueDate,
                           Map<String, String>   additionalProperties,
                           String                assignToGUID,
                           String                sponsorGUID,
                           List<NewActionTarget> actionTargets) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        return openMetadataClient.openToDo(connectorUserId, qualifiedName, title, instructions, category, priority, dueDate, additionalProperties, assignToGUID, sponsorGUID, connectorGUID, actionTargets);
    }


    /**
     * Create a new context event
     *
     * @param anchorGUID unique identifier for the context event's anchor element
     * @param parentContextEvents which context events should be linked as parents (guid->relationship properties)
     * @param childContextEvents which context events should be linked as children (guid->relationship properties)
     * @param relatedContextEvents which context events should be linked as related (guid->relationship properties)
     * @param impactedElements which elements are impacted by this context event (guid->relationship properties)
     * @param effectedDataResourceGUIDs which data resources are effected by this context event (asset guid->effectivity dates)
     * @param contextEventEvidenceGUIDs which elements provide evidence that the context event is happening (element GUIDs-> effectivity dates)
     * @param contextEventProperties properties for the context event itself
     * @return guid of the new context event
     * @throws InvalidParameterException one of the properties are invalid
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException there is a problem connecting to (or inside) the metadata store
     */
    public String registerContextEvent(String                                       anchorGUID,
                                       Map<String, DependentContextEventProperties> parentContextEvents,
                                       Map<String, DependentContextEventProperties> childContextEvents,
                                       Map<String, RelatedContextEventProperties>   relatedContextEvents,
                                       Map<String, ContextEventImpactProperties>    impactedElements,
                                       Map<String, RelationshipProperties>          effectedDataResourceGUIDs,
                                       Map<String, RelationshipProperties>          contextEventEvidenceGUIDs,
                                       ContextEventProperties                       contextEventProperties) throws InvalidParameterException,
                                                                                                                   UserNotAuthorizedException,
                                                                                                                   PropertyServerException
    {
        return openMetadataClient.registerContextEvent(connectorUserId,
                                                       anchorGUID,
                                                       parentContextEvents,
                                                       childContextEvents,
                                                       relatedContextEvents,
                                                       impactedElements,
                                                       effectedDataResourceGUIDs,
                                                       contextEventEvidenceGUIDs,
                                                       contextEventProperties);
    }


    /**
     * Disconnect the file listener.
     */
    public void disconnect()
    {
        listenerManager.disconnect();
        // todo - disconnect for event client

        isActive = false;
    }


    /**
     * Verify that the connector is still active.
     *
     * @param methodName calling method
     * @throws UserNotAuthorizedException exception thrown if no longer active
     */
    public void validateIsActive(String methodName) throws UserNotAuthorizedException
    {
        if (! isActive)
        {
            if (auditLog != null)
            {
                auditLog.logMessage(methodName,
                                    OMFAuditCode.DISCONNECT_DETECTED.getMessageDefinition(connectorName));
            }

            throw new UserNotAuthorizedException(OMFErrorCode.DISCONNECT_DETECTED.getMessageDefinition(connectorName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 connectorUserId);
        }
    }

}
