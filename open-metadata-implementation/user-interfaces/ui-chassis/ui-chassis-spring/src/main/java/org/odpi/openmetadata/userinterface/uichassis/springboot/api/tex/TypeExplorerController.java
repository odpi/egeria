/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.api.tex;


import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectionCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;
import org.odpi.openmetadata.userinterface.uichassis.springboot.api.SecureController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

import org.odpi.openmetadata.adapters.repositoryservices.ConnectorConfigurationFactory;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBroker;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.AttributeTypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.AttributeTypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.RelationshipDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.ClassificationDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.EntityDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.EnumDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefGallery;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;


/**
 * The TypeExplorerRESTServicesInstance provides the server-side implementation
 * of the TypeExplorer UI-component
 */
@RestController
public class TypeExplorerController extends SecureController
{

    private static String className = TypeExplorerController.class.getName();
    private static final Logger LOG = LoggerFactory.getLogger(className);


    String                 metadataCollectionId;
    OMRSMetadataCollection metadataCollection;

    /**
     * Default constructor
     *
     */
    public TypeExplorerController() {
        metadataCollectionId = null;
        metadataCollection = null;
    }


    /*
     * This method retrieves all the types from the server in a TypeExplorer object.
     * In the RequestBody:
     *   serverName is the name of the repository server to be interrogated.
     *   serverURLRoot is the root of the URL to use to connect to the server.
     *   enterpriseOption is a string "true" or "false" indicating whether to include results from the cohorts to which the server belongs
     */

    @PostMapping( path = "/api/types/typeExplorer")
    public TypeExplorerResponse typeExplorer(@RequestBody Map<String,String> body, HttpServletRequest request)
    {

        String serverName        = body.get("serverName");
        String serverURLRoot     = body.get("serverURLRoot");
        boolean enterpriseOption = body.get("enterpriseOption").equals("true");

        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        // Look up types in server and construct TEX
        TypeExplorerResponse texResp;
        String exceptionMessage;

        try {

            TypeExplorer tex = this.getTypeExplorer(userId, serverName, serverURLRoot, enterpriseOption);

            if (tex != null) {
                texResp = new TypeExplorerResponse(200, "", tex);
            } else {
                texResp = new TypeExplorerResponse(400, "Could not retrieve type information", null);
            }
            return texResp;
        }
        catch (ConnectionCheckedException | ConnectorCheckedException e) {

            exceptionMessage = "Connector error occurred, please check the server name and URL root parameters";
        }
        catch (UserNotAuthorizedException e) {

            exceptionMessage = "Sorry - this username was not authorized to perform the request";
        }
        catch (RepositoryErrorException e) {

            exceptionMessage = "The repository could not be reached, please check the server name and URL root and verify that the server is running ";
        }
        catch (InvalidParameterException e) {

            exceptionMessage = "The request to load type information reported an invalid parameter, please check the server name and URL root parameters";
        }
        // For any of the above exceptions, incorporate the exception message into a response object
        texResp = new TypeExplorerResponse(400, exceptionMessage, null);

        return texResp;

    }



    private TypeExplorer getTypeExplorer(String  userId,
                                         String  serverName,
                                         String  serverURLRoot,
                                         boolean enterpriseOption)
        throws
            ConnectionCheckedException,
            ConnectorCheckedException,
            UserNotAuthorizedException,
            RepositoryErrorException,
            InvalidParameterException
    {

        try {

            this.getMetadataCollection(userId, serverName, serverURLRoot, enterpriseOption);

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
                        tex.addEnumExplorer(attributeTypeDef.getName(), (EnumDef)attributeTypeDef);
                        break;
                    default:
                        // Ignore this AttributeTypeDef and continue with next
                        break;
                }
            }

            // All typeDefs processed, resolve linkages and return the TEX object
            tex.resolve();
            return tex;

        }
        catch (ConnectionCheckedException |
               ConnectorCheckedException  |
               UserNotAuthorizedException |
               RepositoryErrorException   |
               InvalidParameterException e ) {
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

                // todo Should not use OMRSErrorCode - create an error code file for TEX
                throw new RepositoryErrorException(OMRSErrorCode.NULL_METADATA_COLLECTION.getMessageDefinition(serverName),
                        this.getClass().getName(),
                        methodName);
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
            Connection connection = factory.getDefaultLocalRepositoryRemoteConnection(serverName, serverURLRoot);

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


}
