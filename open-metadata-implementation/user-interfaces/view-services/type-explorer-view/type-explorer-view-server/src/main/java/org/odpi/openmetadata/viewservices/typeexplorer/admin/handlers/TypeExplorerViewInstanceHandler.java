/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.typeexplorer.admin.handlers;

import org.odpi.openmetadata.adapters.repositoryservices.ConnectorConfigurationFactory;
import org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.commonservices.ffdc.exceptions.PropertyServerException;
import org.odpi.openmetadata.commonservices.ffdc.exceptions.UserNotAuthorizedException;
import org.odpi.openmetadata.commonservices.multitenant.OMVSServiceInstanceHandler;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBroker;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectionCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.userinterface.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.userinterface.common.ffdc.UserInterfaceErrorCode;
import org.odpi.openmetadata.viewservices.tex.properties.ClassificationExplorer;
import org.odpi.openmetadata.viewservices.tex.properties.EntityExplorer;
import org.odpi.openmetadata.viewservices.tex.properties.RelationshipExplorer;
import org.odpi.openmetadata.viewservices.tex.properties.TypeExplorer;
import org.odpi.openmetadata.viewservices.typeexplorer.admin.registration.TypeExplorerViewRegistration;
import org.odpi.openmetadata.viewservices.typeexplorer.admin.serviceinstances.TypeExplorerViewServicesInstance;
import org.odpi.openmetadata.userinterface.common.ffdc.DependantServerNotAvailableException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * TypeExplorerViewInstanceHandler retrieves information from the instance map for the
 * access service instances.  The instance map is thread-safe.  Instances are added
 * and removed by the TypeExplorerViewAdmin class.
 */
public class TypeExplorerViewInstanceHandler extends OMVSServiceInstanceHandler
{
    private String                 metadataCollectionId;
    private OMRSMetadataCollection metadataCollection;
    /**
     * Default constructor registers the view service
     */
    public TypeExplorerViewInstanceHandler() {
        super(ViewServiceDescription.TYPE_EXPLORER.getViewServiceName());
        TypeExplorerViewRegistration.registerViewService();
    }

    /**
     * Get the Type explorer object
     * @param localServerName local UI server name
     * @param userId userid
     * @param requestedServerName requested metadata server name
     * @param enterpriseOption boolean flag to indicate whether enterprise scope should be used
     * @return TypeExplorer type explorer
     * @throws ConnectionCheckedException
     * @throws ConnectorCheckedException
     * @throws org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException
     * @throws RepositoryErrorException
     * @throws org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException
     */
    public TypeExplorer getTypeExplorer(String localServerName,
                                        String  userId,
                                         String  requestedServerName,
                                         boolean enterpriseOption)
            throws
            ConnectionCheckedException,
            ConnectorCheckedException,
            org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException,
            RepositoryErrorException,
            org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException
    {

        try {
            // check that the requested server name and URL are defined for this tenant.
            TypeExplorerViewServicesInstance instance = getTypeExplorerViewServicesInstance(localServerName, userId, "getTypeExplorer");
            String metadataServerNameForTenant = instance.getMetadataServerName();
            String metadataServerURLForTenant = instance.getMetadataServerURL();
            if (metadataServerNameForTenant.equals(requestedServerName)) {

                this.getMetadataCollection(userId, metadataServerNameForTenant, metadataServerURLForTenant, enterpriseOption);

                TypeExplorer tex = new TypeExplorer();

                TypeDefGallery typeDefGallery = metadataCollection.getAllTypes(userId);

                List<TypeDef> typeDefs = typeDefGallery.getTypeDefs();
                for (TypeDef typeDef : typeDefs) {
                    TypeDefCategory tdCat = typeDef.getCategory();
                    switch (tdCat) {
                        case ENTITY_DEF:
                            EntityExplorer eex = new EntityExplorer((EntityDef) typeDef);
                            tex.addEntityExplorer(typeDef.getName(), eex);
                            break;
                        case RELATIONSHIP_DEF:
                            RelationshipExplorer rex = new RelationshipExplorer((RelationshipDef) typeDef);
                            tex.addRelationshipExplorer(typeDef.getName(), rex);
                            break;
                        case CLASSIFICATION_DEF:
                            ClassificationExplorer cex = new ClassificationExplorer((ClassificationDef) typeDef);
                            tex.addClassificationExplorer(typeDef.getName(), cex);
                            break;
                        default:
                            // Ignore this typeDef and continue with next
                            break;
                    }
                }

                // Include EnumDefs in the TEX
                List<AttributeTypeDef> attributeTypeDefs = typeDefGallery.getAttributeTypeDefs();
                for (AttributeTypeDef attributeTypeDef : attributeTypeDefs) {
                    AttributeTypeDefCategory tdCat = attributeTypeDef.getCategory();
                    switch (tdCat) {
                        case ENUM_DEF:
                            tex.addEnumExplorer(attributeTypeDef.getName(), (EnumDef) attributeTypeDef);
                            break;
                        default:
                            // Ignore this AttributeTypeDef and continue with next
                            break;
                    }
                }

                // All typeDefs processed, resolve linkages and return the TEX object
                tex.resolve();

                return tex;
            } else {
                //error
                return null;
            }

            }
        catch(ConnectionCheckedException |
                    ConnectorCheckedException |
                    org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException |
                    RepositoryErrorException |
                    org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException e ){
                throw e;
            }

    }



    private void getMetadataCollection(String  userId,
                                       String  serverName,
                                       String  serverURLRoot,
                                       boolean enterpriseOption)
            throws
            ConnectionCheckedException,
            ConnectorCheckedException,
            RepositoryErrorException

    {

        // TODO - act on the Enterprise Option once it is supported.

        OMRSRepositoryConnector repositoryConnector;
        try {

            repositoryConnector = this.getRepositoryConnector(serverName, serverURLRoot);

        }
        catch (ConnectionCheckedException | ConnectorCheckedException e) {
            throw e;
        }

        if (repositoryConnector != null)  {

            try {

                metadataCollection = repositoryConnector.getMetadataCollection();
                metadataCollectionId = metadataCollection.getMetadataCollectionId(userId);

            }
            catch (RepositoryErrorException exception) {

                String tokens[] = exception.getErrorMessage().split(" on its REST API after it registered with the cohort");

                if (tokens.length > 0) {
                    String frontOfMessageTokens[] = tokens[0].split("returned a metadata collection identifier of ");

                    if (frontOfMessageTokens.length > 1) {
                        metadataCollectionId = frontOfMessageTokens[1];
                        repositoryConnector.setMetadataCollectionId(metadataCollectionId);
                        try {
                            metadataCollection = repositoryConnector.getMetadataCollection();

                        }
                        catch (RepositoryErrorException e) {
                            throw e;
                        }
                    }
                }
            }
        }

        /*
         * Perform integrity checks on metadataCollection
         */
        try {

            boolean error = false;
            if (metadataCollectionId == null) {
                error = true;
            }
            else if (!(metadataCollectionId.equals(metadataCollection.getMetadataCollectionId(userId)))) {
                error = true;
            }

            if (!error) {
                // Successfully located metadataCollection and id matches
                return;
            }
            else {
                final String methodName = "getMetadataCollection";

                OMRSErrorCode errorCode = OMRSErrorCode.NULL_METADATA_COLLECTION;
                String        errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(serverName);

                throw new RepositoryErrorException(errorCode.getHTTPErrorCode(),
                        this.getClass().getName(),
                        methodName,
                        errorMessage,
                        errorCode.getSystemAction(),
                        errorCode.getUserAction());
            }

        }
        catch (RepositoryErrorException e) {
            throw e;
        }

    }


    private OMRSRepositoryConnector getRepositoryConnector(String serverName, String serverURLRoot)

            throws
            ConnectionCheckedException,
            ConnectorCheckedException
    {
        try
        {
            ConnectorConfigurationFactory factory = new ConnectorConfigurationFactory();

            /*
             * We do not have an explicit repositoryName here so set repositoryName to serverName
             */
            Connection connection = factory.getDefaultLocalRepositoryRemoteConnection(serverName, serverName, serverURLRoot);

            ConnectorBroker connectorBroker = new ConnectorBroker();

            Connector connector = connectorBroker.getConnector(connection);

            OMRSRepositoryConnector  repositoryConnector = (OMRSRepositoryConnector)connector;

            repositoryConnector.setRepositoryName(serverName);

            /*
             * The metadataCollectionId parameter is not used by the REST connector - but it needs to be non-null and
             * preferably informative so it is meaningful in any error messages and audit log entries.
             */

            repositoryConnector.setMetadataCollectionId("Metadata Collection for repository "+serverName);

            repositoryConnector.start();

            return repositoryConnector;

        }
        catch (ConnectionCheckedException | ConnectorCheckedException e)
        {
            throw e;
        }
    }

    /**
     * Get the type explorer services instance. This is an instance associated with the UI servername (tenant).
     *
     * @param localServerName name of the server that the request is for
     * @param userId local server userid
     * @param serviceOperationName service operation - usually the top level rest call
     * @return SubjectAreaViewServicesInstance instance for this tenant to use.
     */
    private TypeExplorerViewServicesInstance getTypeExplorerViewServicesInstance( String localServerName, String userId, String serviceOperationName) {

        TypeExplorerViewServicesInstance instance = null;
        try {
            instance = (TypeExplorerViewServicesInstance)
                    super.getServerServiceInstance(userId,
                            localServerName,
                            serviceOperationName);
        } catch (InvalidParameterException e) {
            String badParmName = e.getParameterName();
            if ("serverName".equals(badParmName)) {
                UserInterfaceErrorCode errorCode = UserInterfaceErrorCode.INVALID_SERVERNAME;
                String errorMessage = errorCode.getErrorMessageId()
                        + errorCode.getFormattedErrorMessage(localServerName);
                //TODO
//                throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
//                        "SubjectAreaViewInstanceHandler",
//                        serviceOperationName,
//                        errorMessage,
//                        errorCode.getSystemAction(),
//                        errorCode.getUserAction());
            } else {
                UserInterfaceErrorCode errorCode = UserInterfaceErrorCode.INVALID_PARAMETER;
                String errorMessage = errorCode.getErrorMessageId()
                        + errorCode.getFormattedErrorMessage();
                //TODO
//                throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
//                        "SubjectAreaViewInstanceHandler",
//                        serviceOperationName,
//                        errorMessage,
//                        errorCode.getSystemAction(),
//                        errorCode.getUserAction());

            }

        } catch (UserNotAuthorizedException e) {
            UserInterfaceErrorCode errorCode = UserInterfaceErrorCode.USER_NOT_AUTHORIZED;
            String errorMessage = errorCode.getErrorMessageId()
                    + errorCode.getFormattedErrorMessage();
            //TODO
//            throw new UserNotAuthorizedException(errorCode.getHTTPErrorCode(),
//                    "SubjectAreaViewInstanceHandler",
//                    serviceOperationName,
//                    errorMessage,
//                    errorCode.getSystemAction(),
//                    errorCode.getUserAction(),
//                    userId);
        } catch (PropertyServerException e) {
            UserInterfaceErrorCode errorCode = UserInterfaceErrorCode.SERVICE_NOT_AVAILABLE;
            String errorMessage = errorCode.getErrorMessageId()
                    + errorCode.getFormattedErrorMessage(serviceName);
            //TODO
//            throw new PropertyServerException(errorCode.getHTTPErrorCode(),
//                    "SubjectAreaViewInstanceHandler",
//                    serviceOperationName,
//                    errorMessage,
//                    errorCode.getSystemAction(),
//                    errorCode.getUserAction());
        }
        return instance;
    }

    /**
     * get the list of type explorable server names for this tenant
     * @param userId user id
     * @param localServerName local UI server name
     * @return set of metadata servernames for this tenant
     */
    public Set<String> getMetadataServerNames(String localServerName, String userId ) {
        Set<String> serverNames =  new HashSet<>();

        TypeExplorerViewServicesInstance instance = getTypeExplorerViewServicesInstance(localServerName, userId, "getMetadataServerNames");
        if (instance !=null) {
            String metadataServerName = instance.getMetadataServerName();
            serverNames.add(metadataServerName);
        }
        // TODO add additional servers from the config
        return serverNames;
    }
}
