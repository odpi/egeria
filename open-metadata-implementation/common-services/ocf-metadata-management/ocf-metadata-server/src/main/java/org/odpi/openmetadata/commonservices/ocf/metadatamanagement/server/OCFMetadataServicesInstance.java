/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.server;

import org.odpi.openmetadata.adminservices.configuration.registration.CommonServicesDescription;
import org.odpi.openmetadata.commonservices.ffdc.exceptions.PropertyServerException;
import org.odpi.openmetadata.commonservices.generichandlers.*;
import org.odpi.openmetadata.commonservices.multitenant.OMASServiceInstance;
import org.odpi.openmetadata.commonservices.multitenant.ffdc.exceptions.NewInstanceException;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.server.converters.*;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.ffdc.OMAGOCFErrorCode;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;


/**
 * OCFMetadataServicesInstance caches references to OMRS objects for a specific server.
 * It is also responsible for registering itself in the instance map.
 */
public class OCFMetadataServicesInstance extends OMASServiceInstance
{
    private final AssetHandler<Asset>                                   assetHandler;
    private final CertificationHandler<Certification>                   certificationHandler;
    private final CommentHandler<Comment>                               commentHandler;
    private final ConnectionHandler<Connection>                         connectionHandler;
    private final ConnectorTypeHandler<ConnectorType>                   connectorTypeHandler;
    private final EndpointHandler<Endpoint>                             endpointHandler;
    private final ExternalIdentifierHandler<ExternalIdentifier, Object> externalIdentifierHandler;
    private final ExternalReferenceLinkHandler<ExternalReference>       externalReferenceHandler;
    private final GlossaryTermHandler<Meaning>                          glossaryTermHandler;
    private final InformalTagHandler<InformalTag>                       informalTagHandler;
    private final LicenseHandler<License>                               licenseHandler;
    private final LikeHandler<Like>                                     likeHandler;
    private final SearchKeywordHandler<SearchKeyword>                   keywordHandler;
    private final LocationHandler<Location>                             locationHandler;
    private final NoteLogHandler<NoteLogHeader>                         noteLogHandler;
    private final NoteHandler<Note>                                     noteHandler;
    private final RatingHandler<Rating>                                 ratingHandler;
    private final ReferenceableHandler<Referenceable>                   referenceableHandler;
    private final RelatedAssetHandler<RelatedAsset>                     relatedAssetHandler;
    private final RelatedMediaHandler<RelatedMediaReference>            relatedMediaHandler;
    private final SchemaAttributeHandler<SchemaAttribute, SchemaType>   schemaAttributeHandler;
    private final SchemaTypeHandler<SchemaType>                         schemaTypeHandler;


    /**
     * Set up the handlers for this server.
     *
     * @param repositoryConnector link to the repository responsible for servicing the REST calls.
     * @param auditLog destination for audit log events.
     * @param localServerUserId userId for server initialed calls.
     * @param maxPageSize max number of results to return on single request.
     * @throws NewInstanceException a problem occurred during initialization
     */
    public OCFMetadataServicesInstance(OMRSRepositoryConnector repositoryConnector,
                                       AuditLog                auditLog,
                                       String                  localServerUserId,
                                       int                     maxPageSize) throws NewInstanceException
    {
        super(CommonServicesDescription.OCF_METADATA_MANAGEMENT.getServiceName(),
              repositoryConnector,
              auditLog,
              localServerUserId,
              maxPageSize);

        final String methodName = "new ServiceInstance";

        if (repositoryHandler != null)
        {

            this.assetHandler = new AssetHandler<>(new AssetConverter<>(repositoryHelper, serviceName, serverName),
                                                   Asset.class,
                                                   serviceName,
                                                   serverName,
                                                   invalidParameterHandler,
                                                   repositoryHandler,
                                                   repositoryHelper,
                                                   localServerUserId,
                                                   securityVerifier,
                                                   supportedZones,
                                                   defaultZones,
                                                   publishZones,
                                                   auditLog);

            this.commentHandler = new CommentHandler<>(new CommentConverter<>(repositoryHelper, serviceName, serverName),
                                                       Comment.class,
                                                       serviceName,
                                                       serverName,
                                                       invalidParameterHandler,
                                                       repositoryHandler,
                                                       repositoryHelper,
                                                       localServerUserId,
                                                       securityVerifier,
                                                       supportedZones,
                                                       defaultZones,
                                                       publishZones,
                                                       auditLog);

            this.certificationHandler = new CertificationHandler<>(new CertificationConverter<>(repositoryHelper, serviceName, serverName),
                                                                   Certification.class,
                                                                   serviceName,
                                                                   serverName,
                                                                   invalidParameterHandler,
                                                                   repositoryHandler,
                                                                   repositoryHelper,
                                                                   localServerUserId,
                                                                   securityVerifier,
                                                                   supportedZones,
                                                                   defaultZones,
                                                                   publishZones,
                                                                   auditLog);

            this.connectionHandler = new ConnectionHandler<>(new ConnectionConverter<>(repositoryHelper, serviceName, serverName),
                                                             Connection.class,
                                                             serviceName,
                                                             serverName,
                                                             invalidParameterHandler,
                                                             repositoryHandler,
                                                             repositoryHelper,
                                                             localServerUserId,
                                                             securityVerifier,
                                                             supportedZones,
                                                             defaultZones,
                                                             publishZones,
                                                             auditLog);

            this.connectorTypeHandler = new ConnectorTypeHandler<>(new ConnectorTypeConverter<>(repositoryHelper, serviceName, serverName),
                                                                   ConnectorType.class,
                                                                   serviceName,
                                                                   serverName,
                                                                   invalidParameterHandler,
                                                                   repositoryHandler,
                                                                   repositoryHelper,
                                                                   localServerUserId,
                                                                   securityVerifier,
                                                                   supportedZones,
                                                                   defaultZones,
                                                                   publishZones,
                                                                   auditLog);

            this.endpointHandler = new EndpointHandler<>(new EndpointConverter<>(repositoryHelper, serviceName, serverName),
                                                         Endpoint.class,
                                                         serviceName,
                                                         serverName,
                                                         invalidParameterHandler,
                                                         repositoryHandler,
                                                         repositoryHelper,
                                                         localServerUserId,
                                                         securityVerifier,
                                                         supportedZones,
                                                         defaultZones,
                                                         publishZones,
                                                         auditLog);

            this.externalIdentifierHandler = new ExternalIdentifierHandler<>(new ExternalIdentifierConverter<>(repositoryHelper,
                                                                                                               serviceName,
                                                                                                               serverName),
                                                                             ExternalIdentifier.class,
                                                                             null,
                                                                             Object.class,
                                                                             serviceName,
                                                                             serverName,
                                                                             invalidParameterHandler,
                                                                             repositoryHandler,
                                                                             repositoryHelper,
                                                                             localServerUserId,
                                                                             securityVerifier,
                                                                             supportedZones,
                                                                             defaultZones,
                                                                             publishZones,
                                                                             auditLog);

            this.externalReferenceHandler = new ExternalReferenceLinkHandler<>(new ExternalReferenceConverter<>(repositoryHelper, serviceName, serverName),
                                                                               ExternalReference.class,
                                                                               serviceName,
                                                                               serverName,
                                                                               invalidParameterHandler,
                                                                               repositoryHandler,
                                                                               repositoryHelper,
                                                                               localServerUserId,
                                                                               securityVerifier,
                                                                               supportedZones,
                                                                               defaultZones,
                                                                               publishZones,
                                                                               auditLog);

            this.glossaryTermHandler = new GlossaryTermHandler<>(new MeaningConverter<>(repositoryHelper, serviceName, serverName),
                                                                 Meaning.class,
                                                                 serviceName,
                                                                 serverName,
                                                                 invalidParameterHandler,
                                                                 repositoryHandler,
                                                                 repositoryHelper,
                                                                 localServerUserId,
                                                                 securityVerifier,
                                                                 supportedZones,
                                                                 defaultZones,
                                                                 publishZones,
                                                                 auditLog);

            this.informalTagHandler = new InformalTagHandler<>(new InformalTagConverter<>(repositoryHelper, serviceName, serverName),
                                                               InformalTag.class,
                                                               serviceName,
                                                               serverName,
                                                               invalidParameterHandler,
                                                               repositoryHandler,
                                                               repositoryHelper,
                                                               localServerUserId,
                                                               securityVerifier,
                                                               supportedZones,
                                                               defaultZones,
                                                               publishZones,
                                                               auditLog);

            this.keywordHandler = new SearchKeywordHandler<>(new SearchKeywordConverter<>(repositoryHelper, serviceName, serverName),
                                                             SearchKeyword.class,
                                                             serviceName,
                                                             serverName,
                                                             invalidParameterHandler,
                                                             repositoryHandler,
                                                             repositoryHelper,
                                                             localServerUserId,
                                                             securityVerifier,
                                                             supportedZones,
                                                             defaultZones,
                                                             publishZones,
                                                             auditLog);

            this.licenseHandler = new LicenseHandler<>(new LicenseConverter<>(repositoryHelper, serviceName, serverName),
                                                       License.class,
                                                       serviceName,
                                                       serverName,
                                                       invalidParameterHandler,
                                                       repositoryHandler,
                                                       repositoryHelper,
                                                       localServerUserId,
                                                       securityVerifier,
                                                       supportedZones,
                                                       defaultZones,
                                                       publishZones,
                                                       auditLog);

            this.likeHandler = new LikeHandler<>(new LikeConverter<>(repositoryHelper, serviceName, serverName),
                                                 Like.class,
                                                 serviceName,
                                                 serverName,
                                                 invalidParameterHandler,
                                                 repositoryHandler,
                                                 repositoryHelper,
                                                 localServerUserId,
                                                 securityVerifier,
                                                 supportedZones,
                                                 defaultZones,
                                                 publishZones,
                                                 auditLog);

            this.locationHandler = new LocationHandler<>(new LocationConverter<>(repositoryHelper, serviceName, serverName),
                                                         Location.class,
                                                         serviceName,
                                                         serverName,
                                                         invalidParameterHandler,
                                                         repositoryHandler,
                                                         repositoryHelper,
                                                         localServerUserId,
                                                         securityVerifier,
                                                         supportedZones,
                                                         defaultZones,
                                                         publishZones,
                                                         auditLog);

            this.noteLogHandler = new NoteLogHandler<>(new NoteLogConverter<>(repositoryHelper, serviceName, serverName),
                                                       NoteLogHeader.class,
                                                       serviceName,
                                                       serverName,
                                                       invalidParameterHandler,
                                                       repositoryHandler,
                                                       repositoryHelper,
                                                       localServerUserId,
                                                       securityVerifier,
                                                       supportedZones,
                                                       defaultZones,
                                                       publishZones,
                                                       auditLog);

            this.noteHandler = new NoteHandler<>(new NoteConverter<>(repositoryHelper, serviceName, serverName),
                                                 Note.class,
                                                 serviceName,
                                                 serverName,
                                                 invalidParameterHandler,
                                                 repositoryHandler,
                                                 repositoryHelper,
                                                 localServerUserId,
                                                 securityVerifier,
                                                 supportedZones,
                                                 defaultZones,
                                                 publishZones,
                                                 auditLog);

            this.ratingHandler = new RatingHandler<>(new RatingConverter<>(repositoryHelper, serviceName, serverName),
                                                     Rating.class,
                                                     serviceName,
                                                     serverName,
                                                     invalidParameterHandler,
                                                     repositoryHandler,
                                                     repositoryHelper,
                                                     localServerUserId,
                                                     securityVerifier,
                                                     supportedZones,
                                                     defaultZones,
                                                     publishZones,
                                                     auditLog);

            this.referenceableHandler = new ReferenceableHandler<>(new ReferenceableConverter<>(repositoryHelper, serviceName, serverName),
                                                                   Referenceable.class,
                                                                   serviceName,
                                                                   serverName,
                                                                   invalidParameterHandler,
                                                                   repositoryHandler,
                                                                   repositoryHelper,
                                                                   localServerUserId,
                                                                   securityVerifier,
                                                                   supportedZones,
                                                                   defaultZones,
                                                                   publishZones,
                                                                   auditLog);

            this.relatedAssetHandler = new RelatedAssetHandler<>(new RelatedAssetConverter<>(repositoryHelper, serviceName, serverName),
                                                                 RelatedAsset.class,
                                                                 serviceName,
                                                                 serverName,
                                                                 invalidParameterHandler,
                                                                 repositoryHandler,
                                                                 repositoryHelper,
                                                                 localServerUserId,
                                                                 securityVerifier,
                                                                 supportedZones,
                                                                 defaultZones,
                                                                 publishZones,
                                                                 auditLog);

            this.relatedMediaHandler = new RelatedMediaHandler<>(new RelatedMediaConverter<>(repositoryHelper, serviceName, serverName),
                                                                 RelatedMediaReference.class,
                                                                 serviceName,
                                                                 serverName,
                                                                 invalidParameterHandler,
                                                                 repositoryHandler,
                                                                 repositoryHelper,
                                                                 localServerUserId,
                                                                 securityVerifier,
                                                                 supportedZones,
                                                                 defaultZones,
                                                                 publishZones,
                                                                 auditLog);

            this.schemaAttributeHandler = new SchemaAttributeHandler<>(new SchemaAttributeConverter<>(repositoryHelper, serviceName, serverName),
                                                                       SchemaAttribute.class,
                                                                       new SchemaTypeConverter<>(repositoryHelper, serviceName, serverName),
                                                                       SchemaType.class,
                                                                       serviceName,
                                                                       serverName,
                                                                       invalidParameterHandler,
                                                                       repositoryHandler,
                                                                       repositoryHelper,
                                                                       localServerUserId,
                                                                       securityVerifier,
                                                                       supportedZones,
                                                                       defaultZones,
                                                                       publishZones,
                                                                       auditLog);

            this.schemaTypeHandler = new SchemaTypeHandler<>(new SchemaTypeConverter<>(repositoryHelper, serviceName, serverName),
                                                             SchemaType.class,
                                                             serviceName,
                                                             serverName,
                                                             invalidParameterHandler,
                                                             repositoryHandler,
                                                             repositoryHelper,
                                                             localServerUserId,
                                                             securityVerifier,
                                                             supportedZones,
                                                             defaultZones,
                                                             publishZones,
                                                             auditLog);
        }
        else
        {
            throw new NewInstanceException(OMAGOCFErrorCode.OMRS_NOT_INITIALIZED.getMessageDefinition(methodName),
                                           this.getClass().getName(),
                                           methodName);

        }
    }


    /**
     * Return the handler for managing asset objects.
     *
     * @return  handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    AssetHandler<Asset> getAssetHandler() throws PropertyServerException
    {
        final String methodName = "getAssetHandler";

        validateActiveRepository(methodName);

        return assetHandler;
    }


    /**
     * Return the handler for managing certification objects.
     *
     * @return  handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    CertificationHandler<Certification> getCertificationHandler() throws PropertyServerException
    {
        final String methodName = "getCertificationHandler";

        validateActiveRepository(methodName);

        return certificationHandler;
    }


    /**
     * Return the handler for managing comment objects.
     *
     * @return  handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    CommentHandler<Comment> getCommentHandler() throws PropertyServerException
    {
        final String methodName = "getCommentHandler";

        validateActiveRepository(methodName);

        return commentHandler;
    }


    /**
     * Return the handler for managing connection objects.
     *
     * @return  handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    ConnectionHandler<Connection> getConnectionHandler() throws PropertyServerException
    {
        final String methodName = "getConnectionHandler";

        validateActiveRepository(methodName);

        return connectionHandler;
    }


    /**
     * Return the handler for managing connector type objects.
     *
     * @return  handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    ConnectorTypeHandler<ConnectorType> getConnectorTypeHandler() throws PropertyServerException
    {
        final String methodName = "getConnectorTypeHandler";

        validateActiveRepository(methodName);

        return connectorTypeHandler;
    }


    /**
     * Return the handler for managing endpoint objects.
     *
     * @return  handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    EndpointHandler<Endpoint> getEndpointHandler() throws PropertyServerException
    {
        final String methodName = "getEndpointHandler";

        validateActiveRepository(methodName);

        return endpointHandler;
    }


    /**
     * Return the handler for managing external identifier objects.
     *
     * @return  handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    ExternalIdentifierHandler<ExternalIdentifier, Object> getExternalIdentifierHandler() throws PropertyServerException
    {
        final String methodName = "getExternalIdentifierHandler";

        validateActiveRepository(methodName);

        return externalIdentifierHandler;
    }


    /**
     * Return the handler for managing external reference objects.
     *
     * @return  handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    ExternalReferenceLinkHandler<ExternalReference> getExternalReferenceHandler() throws PropertyServerException
    {
        final String methodName = "getExternalReferenceHandler";

        validateActiveRepository(methodName);

        return externalReferenceHandler;
    }


    /**
     * Return the handler for managing glossary objects.
     *
     * @return glossary handler
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    GlossaryTermHandler<Meaning> getGlossaryTermHandler() throws PropertyServerException
    {
        final String methodName = "getGlossaryTermHandler";

        validateActiveRepository(methodName);

        return glossaryTermHandler;
    }


    /**
     * Return the handler for managing informal tag objects.
     *
     * @return  handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    InformalTagHandler<InformalTag> getInformalTagHandler() throws PropertyServerException
    {
        final String methodName = "getInformalTagHandler";

        validateActiveRepository(methodName);

        return informalTagHandler;
    }


    /**
     * Return the handler for managing license objects.
     *
     * @return  handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    LicenseHandler<License> getLicenseHandler() throws PropertyServerException
    {
        final String methodName = "getLicenseHandler";

        validateActiveRepository(methodName);

        return licenseHandler;
    }


    /**
     * Return the handler for managing like objects.
     *
     * @return  handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    LikeHandler<Like> getLikeHandler() throws PropertyServerException
    {
        final String methodName = "getLikeHandler";

        validateActiveRepository(methodName);

        return likeHandler;
    }


    /**
     * Return the handler for managing like objects.
     *
     * @return  handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    SearchKeywordHandler<SearchKeyword> getKeywordHandler() throws PropertyServerException
    {
        final String methodName = "getKeywordHandler";

        validateActiveRepository(methodName);

        return keywordHandler;
    }


    /**
     * Return the handler for managing location objects.
     *
     * @return  handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    LocationHandler<Location> getLocationHandler() throws PropertyServerException
    {
        final String methodName = "getLocationHandler";

        validateActiveRepository(methodName);

        return locationHandler;
    }


    /**
     * Return the handler for managing note log objects.
     *
     * @return  handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    NoteLogHandler<NoteLogHeader> getNoteLogHandler() throws PropertyServerException
    {
        final String methodName = "getNoteLogHandler";

        validateActiveRepository(methodName);

        return noteLogHandler;
    }


    /**
     * Return the handler for managing note objects (entries in a note log).
     *
     * @return  handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    NoteHandler<Note> getNoteHandler() throws PropertyServerException
    {
        final String methodName = "getNoteHandler";

        validateActiveRepository(methodName);

        return noteHandler;
    }


    /**
     * Return the handler for managing rating objects.
     *
     * @return  handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    RatingHandler<Rating> getRatingHandler() throws PropertyServerException
    {
        final String methodName = "getRatingHandler";

        validateActiveRepository(methodName);

        return ratingHandler;
    }


    /**
     * Return the handler for managing generic referenceable objects.
     *
     * @return  handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    ReferenceableHandler<Referenceable> getReferenceableHandler() throws PropertyServerException
    {
        final String methodName = "getReferenceableHandler";

        validateActiveRepository(methodName);

        return referenceableHandler;
    }


    /**
     * Return the handler for managing related asset objects.
     *
     * @return  handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    RelatedAssetHandler<RelatedAsset> getRelatedAssetHandler() throws PropertyServerException
    {
        final String methodName = "getRelatedMediaHandler";

        validateActiveRepository(methodName);

        return relatedAssetHandler;
    }


    /**
     * Return the handler for managing related media objects.
     *
     * @return  handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    RelatedMediaHandler<RelatedMediaReference> getRelatedMediaHandler() throws PropertyServerException
    {
        final String methodName = "getRelatedMediaHandler";

        validateActiveRepository(methodName);

        return relatedMediaHandler;
    }



    /**
     * Return the handler for managing schema objects.
     *
     * @return  handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    SchemaTypeHandler<SchemaType> getSchemaTypeHandler() throws PropertyServerException
    {
        final String methodName = "getSchemaTypeHandler";

        validateActiveRepository(methodName);

        return schemaTypeHandler;
    }


    /**
     * Return the handler for managing schema objects.
     *
     * @return  handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    SchemaAttributeHandler<SchemaAttribute, SchemaType> getSchemaAttributeHandler() throws PropertyServerException
    {
        final String methodName = "getSchemaAttributeHandler";

        validateActiveRepository(methodName);

        return schemaAttributeHandler;
    }
}
