/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.connectorcontext;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.ConnectorActivityReportClient;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DeleteMethod;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ElementStatus;
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
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * This is the base class for a connector context.  The connector context provides access to open metadata for
 * connectors.  Other frameworks extend this class to provide additional services.
 */
public class ConnectorContextBase
{
    protected final PropertyHelper          propertyHelper = new PropertyHelper();

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
    protected final boolean                 generateIntegrationReport;
    protected final ConnectorActivityReportWriter connectorActivityReportWriter;

    protected final FileClassifier       fileClassifier;
    private   final FilesListenerManager listenerManager;


    protected final OpenMetadataStore           openMetadataStore;
    private final   ActorProfileClient          actorProfileClient;
    private final   ActorRoleClient             actorRoleClient;
    private final   AnnotationClient            annotationClient;
    private final   AssetClient                 assetClient;
    private final   ClassificationManagerClient classificationManagerClient;
    private final   CollectionClient            collectionClient;
    private final   CommentClient               commentClient;
    private final   CommunityClient             communityClient;
    private final   ConnectionClient            connectionClient;
    private final   ConnectorTypeClient         connectorTypeClient;
    private final   DataClassClient             dataClassClient;
    private final   DataFieldClient             dataFieldClient;
    private final   DataStructureClient         dataStructureClient;
    private final   EndpointClient              endpointClient;
    private final   ExternalIdClient            externalIdClient;
    private final   ExternalReferenceClient     externalReferenceClient;
    private final   GlossaryTermClient          glossaryTermClient;
    protected final GovernanceDefinitionClient  governanceDefinitionClient;
    private final   InformalTagClient           informalTagClient;
    private final   LikeClient                  likeClient;
    private final   LineageClient               lineageClient;
    private final   LocationClient              locationClient;
    private final   MultiLanguageClient         multiLanguageClient;
    private final   NoteLogClient               noteLogClient;
    private final   ProjectClient               projectClient;
    private final   RatingClient                ratingClient;
    private final   SchemaAttributeClient       schemaAttributeClient;
    private final   SchemaTypeClient            schemaTypeClient;
    private final   SearchKeywordClient         searchKeywordClient;
    private final   SoftwareCapabilityClient    softwareCapabilityClient;
    private final   SolutionBlueprintClient     solutionBlueprintClient;
    private final   SolutionComponentClient     solutionComponentClient;
    private final   SpecificationPropertyClient specificationPropertyClient;
    private final   UserIdentityClient          userIdentityClient;
    private final   ValidMetadataValuesClient   validMetadataValuesClient;
    private final   ValidValueDefinitionClient  validValueDefinitionClient;


    private boolean isActive = true;



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

        this.annotationClient = new AnnotationClient(this,
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

        this.classificationManagerClient = new ClassificationManagerClient(this,
                                                                           localServerName,
                                                                           localServiceName,
                                                                           connectorUserId,
                                                                           connectorGUID,
                                                                           externalSourceGUID,
                                                                           externalSourceName,
                                                                           openMetadataClient,
                                                                           auditLog,
                                                                           maxPageSize);

        this.collectionClient = new CollectionClient(this,
                                                     localServerName,
                                                     localServiceName,
                                                     connectorUserId,
                                                     connectorGUID,
                                                     externalSourceGUID,
                                                     externalSourceName,
                                                     openMetadataClient,
                                                     auditLog,
                                                     maxPageSize);

        this.commentClient = new CommentClient(this,
                                               localServerName,
                                               localServiceName,
                                               connectorUserId,
                                               connectorGUID,
                                               externalSourceGUID,
                                               externalSourceName,
                                               openMetadataClient,
                                               auditLog,
                                               maxPageSize);

        this.communityClient = new CommunityClient(this,
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

        this.dataClassClient = new DataClassClient(this,
                                                   localServerName,
                                                   localServiceName,
                                                   connectorUserId,
                                                   connectorGUID,
                                                   externalSourceGUID,
                                                   externalSourceName,
                                                   openMetadataClient,
                                                   auditLog,
                                                   maxPageSize);

        this.dataFieldClient = new DataFieldClient(this,
                                                   localServerName,
                                                   localServiceName,
                                                   connectorUserId,
                                                   connectorGUID,
                                                   externalSourceGUID,
                                                   externalSourceName,
                                                   openMetadataClient,
                                                   auditLog,
                                                   maxPageSize);

        this.dataStructureClient = new DataStructureClient(this,
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

        this.externalIdClient = new ExternalIdClient(this,
                                                     localServerName,
                                                     localServiceName,
                                                     connectorUserId,
                                                     connectorGUID,
                                                     externalSourceGUID,
                                                     externalSourceName,
                                                     openMetadataClient,
                                                     auditLog,
                                                     maxPageSize);

        this.externalReferenceClient = new ExternalReferenceClient(this,
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

        this.governanceDefinitionClient = new GovernanceDefinitionClient(this,
                                                                         localServerName,
                                                                         localServiceName,
                                                                         connectorUserId,
                                                                         connectorGUID,
                                                                         externalSourceGUID,
                                                                         externalSourceName,
                                                                         openMetadataClient,
                                                                         auditLog,
                                                                         maxPageSize);

        this.informalTagClient = new InformalTagClient(this,
                                                       localServerName,
                                                       localServiceName,
                                                       connectorUserId,
                                                       connectorGUID,
                                                       externalSourceGUID,
                                                       externalSourceName,
                                                       openMetadataClient,
                                                       auditLog,
                                                       maxPageSize);

        this.likeClient = new LikeClient(this,
                                         localServerName,
                                         localServiceName,
                                         connectorUserId,
                                         connectorGUID,
                                         externalSourceGUID,
                                         externalSourceName,
                                         openMetadataClient,
                                         auditLog,
                                         maxPageSize);

        this.lineageClient = new LineageClient(this,
                                               localServerName,
                                               localServiceName,
                                               connectorUserId,
                                               connectorGUID,
                                               externalSourceGUID,
                                               externalSourceName,
                                               openMetadataClient,
                                               auditLog,
                                               maxPageSize);

        this.locationClient = new LocationClient(this,
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

        this.noteLogClient = new NoteLogClient(this,
                                               localServerName,
                                               localServiceName,
                                               connectorUserId,
                                               connectorGUID,
                                               externalSourceGUID,
                                               externalSourceName,
                                               openMetadataClient,
                                               auditLog,
                                               maxPageSize);

        this.projectClient = new ProjectClient(this,
                                               localServerName,
                                               localServiceName,
                                               connectorUserId,
                                               connectorGUID,
                                               externalSourceGUID,
                                               externalSourceName,
                                               openMetadataClient,
                                               auditLog,
                                               maxPageSize);

        this.ratingClient = new RatingClient(this,
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

        this.searchKeywordClient = new SearchKeywordClient(this,
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

        this.solutionBlueprintClient = new SolutionBlueprintClient(this,
                                                                   localServerName,
                                                                   localServiceName,
                                                                   connectorUserId,
                                                                   connectorGUID,
                                                                   externalSourceGUID,
                                                                   externalSourceName,
                                                                   openMetadataClient,
                                                                   auditLog,
                                                                   maxPageSize);

        this.solutionComponentClient = new SolutionComponentClient(this,
                                                                   localServerName,
                                                                   localServiceName,
                                                                   connectorUserId,
                                                                   connectorGUID,
                                                                   externalSourceGUID,
                                                                   externalSourceName,
                                                                   openMetadataClient,
                                                                   auditLog,
                                                                   maxPageSize);

        this.specificationPropertyClient = new SpecificationPropertyClient(this,
                                                                           localServerName,
                                                                           localServiceName,
                                                                           connectorUserId,
                                                                           connectorGUID,
                                                                           externalSourceGUID,
                                                                           externalSourceName,
                                                                           openMetadataClient,
                                                                           auditLog,
                                                                           maxPageSize);

        this.userIdentityClient = new UserIdentityClient(this,
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

    /*=========================
     * Return details of the metadata store that this connector is talking to
     */


    /**
     * Return the name of the server that this client is connected to.
     *
     * @return string name
     */
    public String getMetadataAccessServer()
    {
        return openMetadataClient.getServerName();
    }


    /**
     * Return the url root for the metadata access server's platform.
     *
     * @return string url root
     */
    public String getMetadataAccessServerPlatformURLRoot() { return openMetadataClient.getServerPlatformURLRoot(); }



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
     * Return the client for managing profiles of a specific subtype.
     *
     * @param specificTypeName override type name
     * @return connector context client
     */
    public ActorProfileClient getActorProfileClient(String specificTypeName)
    {
        return new ActorProfileClient(actorProfileClient, specificTypeName);
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
     * Return the client for managing roles of a specific subtype.
     *
     * @param specificTypeName override type name
     * @return connector context client
     */
    public ActorRoleClient getActorRoleClient(String specificTypeName)
    {
        return new ActorRoleClient(actorRoleClient, specificTypeName);
    }


    /**
     * Return the client for managing annotations for survey reports.
     *
     * @return connector context client
     */
    public AnnotationClient getAnnotationClient()
    {
        return annotationClient;
    }


    /**
     * Return the client for managing annotations of a specific subtype.
     *
     * @param specificTypeName override type name
     * @return connector context client
     */
    public AnnotationClient getAnnotationClient(String specificTypeName)
    {
        return new AnnotationClient(annotationClient, specificTypeName);
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
     * Return the client for managing classifications of a specific subtype.
     *
     * @param specificTypeName override type name
     * @return connector context client
     */
    public ClassificationManagerClient getClassificationManagerClient(String specificTypeName)
    {
        return new ClassificationManagerClient(classificationManagerClient, specificTypeName);
    }


    /**
     * Return the client for managing classifications.
     *
     * @return connector context client
     */
    public ClassificationManagerClient getClassificationManagerClient()
    {
        return classificationManagerClient;
    }


    /**
     * Return the client for managing collection of a specific subtype.
     *
     * @param specificTypeName override type name
     * @return connector context client
     */
    public CollectionClient getCollectionClient(String specificTypeName)
    {
        return new CollectionClient(collectionClient, specificTypeName);
    }


    /**
     * Return the client for managing collection.
     *
     * @return connector context client
     */
    public CollectionClient getCollectionClient()
    {
        return collectionClient;
    }


    /**
     * Return the client for managing comments.
     *
     * @return connector context client
     */
    public CommentClient getCommentClient()
    {
        return commentClient;
    }


    /**
     * Return the client for managing communities.
     *
     * @return connector context client
     */
    public CommunityClient getCommunityClient() { return communityClient; }


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
     * Return the client for managing connector types.
     *
     * @return connector context client
     */
    public ConnectorTypeClient getConnectorTypeClient()
    {
        return connectorTypeClient;
    }


    /**
     * Return the client for managing data classes.
     *
     * @return connector context client
     */
    public DataClassClient getDataClassClient()
    {
        return dataClassClient;
    }


    /**
     * Return the client for managing data fields.
     *
     * @return connector context client
     */
    public DataFieldClient getDataFieldClient()
    {
        return dataFieldClient;
    }


    /**
     * Return the client for managing data structures.
     *
     * @return connector context client
     */
    public DataStructureClient getDataStructureClient()
    {
        return dataStructureClient;
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
     * Return the client for managing external ids.
     *
     * @return connector context client
     */
    public ExternalIdClient getExternalIdClient()
    {
        return externalIdClient;
    }


    /**
     * Return the client for managing external references.
     *
     * @return connector context client
     */
    public ExternalReferenceClient getExternalReferenceClient()
    {
        return externalReferenceClient;
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
     * Return the client for managing governance definitions of a specific subtype.
     *
     * @param specificTypeName override type name
     * @return connector context client
     */
    public GovernanceDefinitionClient getGovernanceDefinitionClient(String specificTypeName)
    {
        return new GovernanceDefinitionClient(governanceDefinitionClient, specificTypeName);
    }


    /**
     * Return the client for managing governance definitions.
     *
     * @return connector context client
     */
    public GovernanceDefinitionClient getGovernanceDefinitionClient()
    {
        return governanceDefinitionClient;
    }


    /**
     * Return the client for managing informal tags.
     *
     * @return connector context client
     */
    public InformalTagClient getInformalTagClient()
    {
        return informalTagClient;
    }



    /**
     * Return the client for managing likes.
     *
     * @return connector context client
     */
    public LikeClient getLikeClient()
    {
        return likeClient;
    }



    /**
     * Return the client for managing lineage relationships.
     *
     * @return connector context client
     */
    public LineageClient getLineageClient()
    {
        return lineageClient;
    }


    /**
     * Return the client for managing locations.
     *
     * @return connector context client
     */
    public LocationClient getLocationClient()
    {
        return locationClient;
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
     * Return the client for managing note logs.
     *
     * @return connector context client
     */
    public NoteLogClient getNoteLogClient()
    {
        return noteLogClient;
    }



    /**
     * Return the client for managing projects.
     *
     * @return connector context client
     */
    public ProjectClient getProjectClient()
    {
        return projectClient;
    }


    /**
     * Return the client for managing ratings.
     *
     * @return connector context client
     */
    public RatingClient getRatingClient()
    {
        return ratingClient;
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
     * Return the client for managing search keywords.
     *
     * @return connector context client
     */
    public SearchKeywordClient getSearchKeywordClient()
    {
        return searchKeywordClient;
    }


    /**
     * Return the client for managing software capabilities of a specific subtype.
     *
     * @param specificTypeName override type name
     * @return connector context client
     */
    public SoftwareCapabilityClient getSoftwareCapabilityClient(String specificTypeName)
    {
        return new SoftwareCapabilityClient(softwareCapabilityClient, specificTypeName);
    }


    /**
     * Return the client for managing software capabilities.
     *
     * @return connector context client
     */
    public SoftwareCapabilityClient getSoftwareCapabilityClient()
    {
        return softwareCapabilityClient;
    }


    /**
     * Return the client for managing solution blueprints.
     *
     * @return connector context client
     */
    public SolutionBlueprintClient getSolutionBlueprintClient()
    {
        return solutionBlueprintClient;
    }


    /**
     * Return the client for managing solution components.
     *
     * @return connector context client
     */
    public SolutionComponentClient getSolutionComponentClient()
    {
        return solutionComponentClient;
    }


    /**
     * Return the client for managing specification properties.
     *
     * @return connector context client
     */
    public SpecificationPropertyClient getSpecificationPropertyClient()
    {
        return specificationPropertyClient;
    }


    /**
     * Return the client for managing user identities.
     *
     * @return connector context client
     */
    public UserIdentityClient getUserIdentityClient()
    {
        return userIdentityClient;
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
     * Return the client for managing valid value definitions.
     *
     * @param specialistTypeName override type name
     * @return connector context client
     */
    public ValidValueDefinitionClient getValidValueDefinitionClient(String specialistTypeName)
    {
        return new ValidValueDefinitionClient(validValueDefinitionClient, specialistTypeName);
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
     * @throws UserNotAuthorizedException the user is not authorized or the connector is not active
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
                if (anchorClassification.getClassificationProperties() instanceof AnchorsProperties anchorsProperties)
                {
                    return anchorsProperties.getAnchorGUID();
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
     * Create a specific governance action process from a generic governance action type.
     *
     * @param processQualifiedName new qualified name for the process
     * @param processName new name for the process
     * @param processDescription new description for the process
     * @param governanceActionTypeGUID the unique identifier of the governance action type
     * @param additionalRequestParameters the additional, predefined request parameters to add to the
     *                                   GovernanceActionProcessFlow relationship
     * @param anchorScopeGUID unique identifier for the top level project - used as a search scope
     * @return unique identifier of new governance action process
     * @throws InvalidParameterException parameter error
     * @throws PropertyServerException repository error
     * @throws UserNotAuthorizedException authorization error
     */
    public String createProcessFromGovernanceActionType(String processQualifiedName,
                                                        String processName,
                                                        String processDescription,
                                                        String governanceActionTypeGUID,
                                                        Map<String, String> additionalRequestParameters,
                                                        String anchorGUID,
                                                        String anchorScopeGUID) throws InvalidParameterException,
                                                                                                     PropertyServerException,
                                                                                                     UserNotAuthorizedException
    {
        String processGUID = this.createGovernanceActionProcess(processQualifiedName, processName, processDescription,anchorGUID, anchorScopeGUID);

        OpenMetadataElement governanceActionType = openMetadataStore.getMetadataElementByGUID(governanceActionTypeGUID);

        if (governanceActionType != null)
        {
            RelatedMetadataElement governanceActionExecutorRelationship = openMetadataStore.getRelatedMetadataElement(governanceActionTypeGUID,
                                                                                                                      1,
                                                                                                                      OpenMetadataType.GOVERNANCE_ACTION_EXECUTOR_RELATIONSHIP.typeName,
                                                                                                                      null);

            if (governanceActionExecutorRelationship != null)
            {
                String governanceEngineGUID = governanceActionExecutorRelationship.getElement().getElementGUID();

                ElementProperties processStepProperties = propertyHelper.addStringProperty(governanceActionType.getElementProperties(),
                                                                                           OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                           processQualifiedName + ":processStep1");

                ElementProperties processFlowProperties = propertyHelper.addStringMapProperty(null,
                                                                                              OpenMetadataProperty.REQUEST_PARAMETERS.name,
                                                                                              additionalRequestParameters);

                String processStep1GUID = openMetadataStore.createMetadataElementInStore(OpenMetadataType.GOVERNANCE_ACTION_PROCESS_STEP.typeName,
                                                                                         ElementStatus.ACTIVE,
                                                                                         null,
                                                                                         processGUID,
                                                                                         false,
                                                                                         anchorScopeGUID,
                                                                                         new NewElementProperties(processStepProperties),
                                                                                         processGUID,
                                                                                         OpenMetadataType.GOVERNANCE_ACTION_PROCESS_FLOW_RELATIONSHIP.typeName,
                                                                                         new NewElementProperties(processFlowProperties),
                                                                                         true);

                openMetadataStore.createRelatedElementsInStore(OpenMetadataType.GOVERNANCE_ACTION_EXECUTOR_RELATIONSHIP.typeName,
                                                               processStep1GUID,
                                                               governanceEngineGUID,
                                                               null,
                                                               null,
                                                               governanceActionExecutorRelationship.getRelationshipProperties());


                /*
                 * Copy the pre-populated governance action targets to the new process.
                 */
                RelatedMetadataElementList actionTargets = openMetadataStore.getRelatedMetadataElements(governanceActionTypeGUID,
                                                                                                        1,
                                                                                                        OpenMetadataType.TARGET_FOR_GOVERNANCE_ACTION_RELATIONSHIP.typeName,
                                                                                                        0,
                                                                                                        0);

                if ((actionTargets != null) && (actionTargets.getElementList() != null))
                {
                    for (RelatedMetadataElement actionTarget : actionTargets.getElementList())
                    {
                        if (actionTarget != null)
                        {
                            openMetadataStore.createRelatedElementsInStore(OpenMetadataType.TARGET_FOR_GOVERNANCE_ACTION_RELATIONSHIP.typeName,
                                                                           processGUID,
                                                                           actionTarget.getElement().getElementGUID(),
                                                                           null,
                                                                           null,
                                                                           actionTarget.getRelationshipProperties());
                        }
                    }
                }


                RelatedMetadataElementList specifications = openMetadataStore.getRelatedMetadataElements(governanceActionTypeGUID,
                                                                                                         1,
                                                                                                         OpenMetadataType.SPECIFICATION_PROPERTY_ASSIGNMENT_RELATIONSHIP.typeName,
                                                                                                         0,
                                                                                                         0);

                if ((specifications != null) && (specifications.getElementList() != null))
                {
                    for (RelatedMetadataElement specification : specifications.getElementList())
                    {
                        if (specification != null)
                        {
                            openMetadataStore.createRelatedElementsInStore(OpenMetadataType.SPECIFICATION_PROPERTY_ASSIGNMENT_RELATIONSHIP.typeName,
                                                                           processGUID,
                                                                           specification.getElement().getElementGUID(),
                                                                           null,
                                                                           null,
                                                                           specification.getRelationshipProperties());
                        }
                    }
                }
            }
        }

        return processGUID;
    }


    /**
     * Create the governance action process asset.
     *
     * @param processQualifiedName new qualified name for the process
     * @param processName new name for the process
     * @param processDescription new description for the process
     * @param anchorGUID unique identifier for the anchor = may be null
     * @param anchorScopeGUID unique identifier for the top level folder - used as a search scope
     * @return unique identifier of new governance action process
     * @throws InvalidParameterException parameter error
     * @throws PropertyServerException repository error
     * @throws UserNotAuthorizedException authorization error
     */
    public String createGovernanceActionProcess(String processQualifiedName,
                                                String processName,
                                                String processDescription,
                                                String anchorGUID,
                                                String anchorScopeGUID) throws InvalidParameterException,
                                                                               PropertyServerException,
                                                                               UserNotAuthorizedException
    {

        ElementProperties processProperties = propertyHelper.addStringProperty(null,
                                                                               OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                               processQualifiedName);

        processProperties = propertyHelper.addStringProperty(processProperties,
                                                             OpenMetadataProperty.DISPLAY_NAME.name,
                                                             processName);

        processProperties = propertyHelper.addStringProperty(processProperties,
                                                             OpenMetadataProperty.DESCRIPTION.name,
                                                             processDescription);

        return openMetadataStore.createMetadataElementInStore(OpenMetadataType.GOVERNANCE_ACTION_PROCESS.typeName,
                                                              ElementStatus.ACTIVE,
                                                              null,
                                                              anchorGUID,
                                                              (anchorGUID == null),
                                                              anchorScopeGUID,
                                                              new NewElementProperties(processProperties),
                                                              null,
                                                              null,
                                                              null,
                                                              false);
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
