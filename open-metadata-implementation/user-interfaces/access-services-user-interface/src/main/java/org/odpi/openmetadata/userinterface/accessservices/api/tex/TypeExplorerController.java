/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.accessservices.api.tex;


import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;
import org.odpi.openmetadata.userinterface.accessservices.api.SecureController;
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

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
        System.out.println("TypeExplorerController default ctor called");
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

    @RequestMapping(method = RequestMethod.POST, path = "/api/types/typeExplorer")
    public TypeExplorer typeExplorer(@RequestBody Map<String,String> body, HttpServletRequest request)
    {

        System.out.println("Server-side typeExplorer API invoked");

        String serverName        = body.get("serverName");
        String serverURLRoot     = body.get("serverURLRoot");
        boolean enterpriseOption = body.get("enterpriseOption").equals("true") ? true : false;

        String userId = getUser(request);

        // Look up types in server and construct TEX
        TypeExplorer tex = this.getTypeExplorer(userId, serverName, serverURLRoot, enterpriseOption);

        return tex;
    }



    private TypeExplorer getTypeExplorer(String  userId,
                                         String  serverName,
                                         String  serverURLRoot,
                                         boolean enterpriseOption)
    {

        this.getMetadataCollection(userId, serverName, serverURLRoot, enterpriseOption);

        System.out.println("getTypeExplorer with userId "+userId+" serverName "+serverName+" serverURLRoot "+serverURLRoot+" entOpt "+enterpriseOption);

        TypeExplorer tex = new TypeExplorer();
        try {

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
                        //System.out.println("Unrecognised type category ignored " + tdCat);
                        break;
                }
            }

            // Include EnumDefs in the TEX
            List<AttributeTypeDef> attributeTypeDefs = typeDefGallery.getAttributeTypeDefs();
            for (AttributeTypeDef attributeTypeDef : attributeTypeDefs) {
                AttributeTypeDefCategory tdCat = attributeTypeDef.getCategory();
                switch (tdCat) {
                    case ENUM_DEF:
                        //System.out.println("Add enum "+attributeTypeDef.getName()+" to TEX");
                        tex.addEnumExplorer(attributeTypeDef.getName(), (EnumDef)attributeTypeDef);
                        break;
                    default:
                        // Ignore this AttributeTypeDef and continue with next
                        //System.out.println("Ignored attribute type category " + tdCat);
                        break;
                }
            }

            // All typeDefs processed, resolve linkages and return the TEX object
            tex.resolve();
            return tex;

        } catch (UserNotAuthorizedException | RepositoryErrorException | InvalidParameterException e) {

            // TODO - when merged, integrate into Egeria error handling
            System.out.println("Failed to get TDG!! exception " + e.getMessage());

            return null;
        }

    }



    private void getMetadataCollection(String  userId,
                                       String  serverName,
                                       String  serverURLRoot,
                                       boolean enterpriseOption)
    {

        // TODO - take heed of Enterprise Option once it is supported.
        System.out.println("WARNING!!  Enterprise Option is ignored for now!! ");


        OMRSRepositoryConnector repositoryConnector = this.getRepositoryConnector(serverName, serverURLRoot);

        if (repositoryConnector != null)  {

            try {

                metadataCollection = repositoryConnector.getMetadataCollection();
                metadataCollectionId = metadataCollection.getMetadataCollectionId();

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

                        } catch (RepositoryErrorException e) {

                            // TODO - when merged, integrate into Egeria error handling
                            System.out.println("Error! could not get MDC from connector!!");
                            return;
                        }
                    }
                }
            }
        }

        /*
         * Perform integrity checks on metadataCollection
         */
        try {

            if (metadataCollectionId == null) {
                // TODO - when merged, integrate into Egeria error handling
                System.out.println("Error! - metadataCollectionId is null");
                return;
            }
            if (!(metadataCollectionId.equals(metadataCollection.getMetadataCollectionId()))) {
                // TODO - when merged, integrate into Egeria error handling
                System.out.println("Error! - local metadataCollectionId is "+metadataCollectionId
                        +" BUT actual is "+metadataCollection.getMetadataCollectionId());
                return;
            }
            System.out.println("Successfully located metadataCollection - id is "+metadataCollectionId);
            return;

        }
        catch (RepositoryErrorException e) {
            // TODO - when merged, integrate into Egeria error handling
            System.out.println("Give it up!");
            return;
        }

    }


    private OMRSRepositoryConnector getRepositoryConnector(String serverName, String serverURLRoot)
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

            /*
             * The metadataCollectionId parameter is not used by the REST connector - but it needs to be non-null and
             * preferably informative so it is meaningful in any error messages and audit log entries.
             */

            repositoryConnector.setMetadataCollectionId("Metadata Collection for repository "+serverName);

            repositoryConnector.start();

            return repositoryConnector;

        }
        catch (Throwable  exc)
        {
            // TODO - when merged, integrate into Egeria error handling
            System.out.println("Unable to create connector " + exc.getMessage());
            return null;
        }
    }


}
