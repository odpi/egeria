/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetmanager.server;

import org.odpi.openmetadata.accessservices.assetmanager.connectors.outtopic.AssetManagerOutTopicClientProvider;
import org.odpi.openmetadata.accessservices.assetmanager.converters.*;
import org.odpi.openmetadata.accessservices.assetmanager.ffdc.AssetManagerErrorCode;
import org.odpi.openmetadata.accessservices.assetmanager.handlers.CommentExchangeHandler;
import org.odpi.openmetadata.accessservices.assetmanager.handlers.ConnectionExchangeHandler;
import org.odpi.openmetadata.accessservices.assetmanager.handlers.DataAssetExchangeHandler;
import org.odpi.openmetadata.accessservices.assetmanager.handlers.ExternalReferenceExchangeHandler;
import org.odpi.openmetadata.accessservices.assetmanager.handlers.GlossaryExchangeHandler;
import org.odpi.openmetadata.accessservices.assetmanager.handlers.NoteLogExchangeHandler;
import org.odpi.openmetadata.accessservices.assetmanager.handlers.ProcessExchangeHandler;
import org.odpi.openmetadata.accessservices.assetmanager.handlers.SchemaExchangeHandler;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.*;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.generichandlers.*;
import org.odpi.openmetadata.commonservices.multitenant.OMASServiceInstance;
import org.odpi.openmetadata.commonservices.multitenant.ffdc.exceptions.NewInstanceException;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementHeader;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementStub;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

import java.util.List;

/**
 * AssetManagerServicesInstance caches references to OMRS objects for a specific server.
 * It is also responsible for registering itself in the instance map.
 */
public class AssetManagerServicesInstance extends OMASServiceInstance
{
    private static final AccessServiceDescription myDescription = AccessServiceDescription.ASSET_MANAGER_OMAS;

    private final ReferenceableHandler<ElementStub>                                   elementStubHandler;
    private final ReferenceableHandler<RelatedElement>                                relatedElementHandler;
    private final AssetHandler<AssetElement>                                          assetHandler;
    private final SoftwareCapabilityHandler<SoftwareCapabilityElement>                assetManagerHandler;
    private final ExternalIdentifierHandler<MetadataCorrelationHeader, ElementHeader> externalIdentifierHandler;
    private final ConnectionExchangeHandler                                           connectionExchangeHandler;
    private final DataAssetExchangeHandler                                            dataAssetExchangeHandler;
    private final ExternalReferenceExchangeHandler                                    externalReferenceHandler;
    private final GlossaryExchangeHandler                                             glossaryExchangeHandler;
    private final ProcessExchangeHandler                                              processExchangeHandler;
    private final SchemaExchangeHandler                                               schemaExchangeHandler;
    private final GovernanceActionHandler<GovernanceActionElement>                    governanceActionHandler;
    private final GovernanceDefinitionHandler<GovernanceDefinitionElement>            governanceDefinitionHandler;
    private final AssetHandler<GovernanceActionProcessElement>                        governanceActionProcessHandler;
    private final GovernanceActionTypeHandler<GovernanceActionTypeElement>            governanceActionTypeHandler;
    private final InformalTagHandler<InformalTagElement>                              informalTagHandler;
    private final LikeHandler<LikeElement>                                            likeHandler;
    private final RatingHandler<RatingElement>                                        ratingHandler;
    private final CommentExchangeHandler                                              commentHandler;
    private final NoteLogExchangeHandler                                              noteLogHandler;

    /**
     * Set up the local repository connector that will service the REST Calls.
     *
     * @param repositoryConnector link to the repository responsible for servicing the REST calls.
     * @param supportedZones list of zones that AssetManager is allowed to serve Assets from.
     * @param defaultZones list of zones that AssetManager sets up in new Asset instances.
     * @param publishZones list of zones that AssetManager sets up in published Asset instances.
     * @param auditLog logging destination
     * @param localServerUserId userId used for server initiated actions
     * @param maxPageSize max number of results to return on single request.
     * @param outTopicConnection topic of the client side listener
     * @throws NewInstanceException a problem occurred during initialization
     */
    public AssetManagerServicesInstance(OMRSRepositoryConnector repositoryConnector,
                                        List<String>            supportedZones,
                                        List<String>            defaultZones,
                                        List<String>            publishZones,
                                        AuditLog                auditLog,
                                        String                  localServerUserId,
                                        int                     maxPageSize,
                                        Connection              outTopicConnection) throws NewInstanceException
    {
        super(myDescription.getAccessServiceFullName(),
              repositoryConnector,
              supportedZones,
              defaultZones,
              publishZones,
              auditLog,
              localServerUserId,
              maxPageSize,
              null,
              null,
              AssetManagerOutTopicClientProvider.class.getName(),
              outTopicConnection);

        if (repositoryHandler == null)
        {
            final String methodName = "new ServiceInstance";

            throw new NewInstanceException(AssetManagerErrorCode.OMRS_NOT_INITIALIZED.getMessageDefinition(methodName),
                                           this.getClass().getName(),
                                           methodName);

        }

        this.elementStubHandler = new ReferenceableHandler<>(new ElementStubConverter<>(repositoryHelper, serviceName, serverName),
                                                             ElementStub.class,
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

        this.relatedElementHandler = new ReferenceableHandler<>(new RelatedElementConverter<>(repositoryHelper, serviceName, serverName),
                                                                RelatedElement.class,
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

        this.assetHandler = new AssetHandler<>(new AssetConverter<>(repositoryHelper, serviceName, serverName),
                                               AssetElement.class,
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

        this.assetManagerHandler = new SoftwareCapabilityHandler<>(new AssetManagerConverter<>(repositoryHelper, serviceName, serverName),
                                                                   SoftwareCapabilityElement.class,
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

        this.externalIdentifierHandler = new ExternalIdentifierHandler<>(new ExternalIdentifierConverter<>(repositoryHelper, serviceName, serverName),
                                                                         MetadataCorrelationHeader.class,
                                                                         new ElementHeaderConverter<>(repositoryHelper, serviceName, serverName),
                                                                         ElementHeader.class,
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

        this.connectionExchangeHandler = new ConnectionExchangeHandler(serviceName,
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

        this.dataAssetExchangeHandler = new DataAssetExchangeHandler(serviceName,
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

        this.externalReferenceHandler = new ExternalReferenceExchangeHandler(serviceName,
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

        this.glossaryExchangeHandler = new GlossaryExchangeHandler(serviceName,
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

        this.processExchangeHandler = new ProcessExchangeHandler(serviceName,
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

        this.schemaExchangeHandler = new SchemaExchangeHandler(serviceName,
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

        this.governanceActionHandler = new GovernanceActionHandler<>(new GovernanceActionConverter<>(repositoryHelper, serviceName, serverName),
                                                                     GovernanceActionElement.class,
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

        this.governanceActionProcessHandler = new AssetHandler<>(new GovernanceActionProcessConverter<>(repositoryHelper, serviceName, serverName),
                                                                 GovernanceActionProcessElement.class,
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

        this.governanceActionTypeHandler = new GovernanceActionTypeHandler<>(new GovernanceActionTypeConverter<>(repositoryHelper, serviceName, serverName),
                                                                             GovernanceActionTypeElement.class,
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

        this.governanceDefinitionHandler = new GovernanceDefinitionHandler<>(new GovernanceDefinitionConverter<>(repositoryHelper, serviceName, serverName),
                                                                             GovernanceDefinitionElement.class,
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
                                                           InformalTagElement.class,
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
                                             LikeElement.class,
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
                                                 RatingElement.class,
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

        this.commentHandler = new CommentExchangeHandler(serviceName,
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

        this.noteLogHandler = new NoteLogExchangeHandler(serviceName,
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


    /**
     * Return the handler for managing software server capability objects representing the integrator.
     *
     * @return  handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    SoftwareCapabilityHandler<SoftwareCapabilityElement> getAssetManagerIntegratorHandler() throws PropertyServerException
    {
        final String methodName = "getAssetManagerIntegratorHandler";

        validateActiveRepository(methodName);

        return assetManagerHandler;
    }


    /**
     * Return the handler for managing assets.
     *
     * @return  handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    AssetHandler<AssetElement> getAssetHandler() throws PropertyServerException
    {
        final String methodName = "getAssetHandler";

        validateActiveRepository(methodName);

        return assetHandler;
    }


    /**
     * Return the handler for managing arbitrary referenceable objects.
     *
     * @return  handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    ReferenceableHandler<ElementStub> getElementStubHandler() throws PropertyServerException
    {
        final String methodName = "getElementStubHandler";

        validateActiveRepository(methodName);

        return elementStubHandler;
    }


    /**
     * Return the handler for managing arbitrary referenceable objects.
     *
     * @return  handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    ReferenceableHandler<RelatedElement> getRelatedElementHandler() throws PropertyServerException
    {
        final String methodName = "getRelatedElementHandler";

        validateActiveRepository(methodName);

        return relatedElementHandler;
    }


    /**
     * Return the handler for managing external identifiers for third party metadata elements.
     *
     * @return  handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    ExternalIdentifierHandler<MetadataCorrelationHeader, ElementHeader> getExternalIdentifierHandler() throws PropertyServerException
    {
        final String methodName = "getExternalIdentifierHandler";

        validateActiveRepository(methodName);

        return externalIdentifierHandler;
    }


    /**
     * Return the handler for managing connection, connector type and endpoint objects.
     *
     * @return  handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    ConnectionExchangeHandler getConnectionExchangeHandler() throws PropertyServerException
    {
        final String methodName = "getConnectionExchangeHandler";

        validateActiveRepository(methodName);

        return connectionExchangeHandler;
    }


    /**
     * Return the handler for managing asset objects.
     *
     * @return  handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    DataAssetExchangeHandler getDataAssetExchangeHandler() throws PropertyServerException
    {
        final String methodName = "getDataAssetExchangeHandler";

        validateActiveRepository(methodName);

        return dataAssetExchangeHandler;
    }


    /**
     * Return the handler for managing external reference objects.
     *
     * @return  handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    ExternalReferenceExchangeHandler getExternalReferenceExchangeHandler() throws PropertyServerException
    {
        final String methodName = "getExternalReferenceExchangeHandler";

        validateActiveRepository(methodName);

        return externalReferenceHandler;
    }


    /**
     * Return the handler for managing glossary objects.
     *
     * @return  handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    GlossaryExchangeHandler getGlossaryExchangeHandler() throws PropertyServerException
    {
        final String methodName = "getGlossaryExchangeHandler";

        validateActiveRepository(methodName);

        return glossaryExchangeHandler;
    }


    /**
     * Return the handler for managing process objects.
     *
     * @return  handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    ProcessExchangeHandler getProcessExchangeHandler() throws PropertyServerException
    {
        final String methodName = "getProcessExchangeHandler";

        validateActiveRepository(methodName);

        return processExchangeHandler;
    }


    /**
     * Return the handler for managing schema objects.
     *
     * @return  handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    SchemaExchangeHandler getSchemaExchangeHandler() throws PropertyServerException
    {
        final String methodName = "getSchemaExchangeHandler";

        validateActiveRepository(methodName);

        return schemaExchangeHandler;
    }



    /**
     * Return the handler for governance action process requests.
     *
     * @return handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    public AssetHandler<GovernanceActionProcessElement> getGovernanceActionProcessHandler()  throws PropertyServerException
    {
        final String methodName = "getGovernanceActionProcessHandler";

        validateActiveRepository(methodName);

        return governanceActionProcessHandler;
    }


    /**
     * Return the handler for governance action type requests.
     *
     * @return handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    GovernanceActionTypeHandler<GovernanceActionTypeElement> getGovernanceActionTypeHandler() throws PropertyServerException
    {
        final String methodName = "getGovernanceActionTypeHandler";

        validateActiveRepository(methodName);

        return governanceActionTypeHandler;
    }


    /**
     * Return the handler for governance action requests.
     *
     * @return handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    GovernanceActionHandler<GovernanceActionElement> getGovernanceActionHandler() throws PropertyServerException
    {
        final String methodName = "getGovernanceActionHandler";

        validateActiveRepository(methodName);

        return governanceActionHandler;
    }


    /**
     * Return the handler for governance definition requests.
     *
     * @return handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    GovernanceDefinitionHandler<GovernanceDefinitionElement> getGovernanceDefinitionHandler() throws PropertyServerException
    {
        final String methodName = "getGovernanceDefinitionHandler";

        validateActiveRepository(methodName);

        return governanceDefinitionHandler;
    }



    /**
     * Return the handler for managing informal tag objects.
     *
     * @return  handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    InformalTagHandler<InformalTagElement> getInformalTagHandler() throws PropertyServerException
    {
        final String methodName = "getInformalTagHandler";

        validateActiveRepository(methodName);

        return informalTagHandler;
    }


    /**
     * Return the handler for managing like objects.
     *
     * @return  handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    LikeHandler<LikeElement> getLikeHandler() throws PropertyServerException
    {
        final String methodName = "getLikeHandler";

        validateActiveRepository(methodName);

        return likeHandler;
    }


    /**
     * Return the handler for managing rating objects.
     *
     * @return  handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    RatingHandler<RatingElement> getRatingHandler() throws PropertyServerException
    {
        final String methodName = "getRatingHandler";

        validateActiveRepository(methodName);

        return ratingHandler;
    }


    /**
     * Return the handler for managing comment objects.
     *
     * @return  handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    CommentExchangeHandler getCommentHandler() throws PropertyServerException
    {
        final String methodName = "getCommentHandler";

        validateActiveRepository(methodName);

        return commentHandler;
    }


    /**
     * Return the handler for managing notelogs and notes objects.
     *
     * @return  handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    NoteLogExchangeHandler getNoteLogHandler() throws PropertyServerException
    {
        final String methodName = "getNoteLogHandler";

        validateActiveRepository(methodName);

        return noteLogHandler;
    }
}
