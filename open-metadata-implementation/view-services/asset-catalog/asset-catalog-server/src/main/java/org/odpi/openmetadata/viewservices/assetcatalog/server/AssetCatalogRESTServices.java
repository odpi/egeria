/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project */
/* Copyright Contributors to the ODPi Egeria category. */
package org.odpi.openmetadata.viewservices.assetcatalog.server;

import org.odpi.openmetadata.accessservices.assetconsumer.client.OpenMetadataStoreClient;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementType;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataType;
import org.odpi.openmetadata.frameworks.governanceaction.properties.AttachedClassification;
import org.odpi.openmetadata.frameworks.governanceaction.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.RelatedMetadataElements;
import org.odpi.openmetadata.frameworks.governanceaction.search.ElementProperties;
import org.odpi.openmetadata.frameworks.governanceaction.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.governanceaction.search.SearchProperties;
import org.odpi.openmetadata.frameworks.governanceaction.search.SequencingOrder;
import org.odpi.openmetadata.tokencontroller.TokenController;
import org.odpi.openmetadata.viewservices.assetcatalog.beans.AssetCatalogBean;
import org.odpi.openmetadata.viewservices.assetcatalog.beans.Classification;
import org.odpi.openmetadata.viewservices.assetcatalog.beans.Element;
import org.odpi.openmetadata.viewservices.assetcatalog.beans.ElementOrigin;
import org.odpi.openmetadata.viewservices.assetcatalog.beans.Elements;
import org.odpi.openmetadata.viewservices.assetcatalog.beans.Relationship;
import org.odpi.openmetadata.viewservices.assetcatalog.beans.Type;
import org.odpi.openmetadata.viewservices.assetcatalog.rest.AssetCatalogResponse;
import org.odpi.openmetadata.viewservices.assetcatalog.rest.AssetCatalogSupportedTypes;
import org.odpi.openmetadata.viewservices.assetcatalog.rest.AssetListResponse;
import org.odpi.openmetadata.viewservices.assetcatalog.rest.AssetResponse;
import org.odpi.openmetadata.viewservices.assetcatalog.rest.ElementHierarchyRequest;
import org.odpi.openmetadata.viewservices.assetcatalog.rest.LineageNodeNamesResponse;
import org.odpi.openmetadata.viewservices.assetcatalog.rest.LineageResponse;
import org.odpi.openmetadata.viewservices.assetcatalog.rest.LineageSearchRequest;
import org.odpi.openmetadata.viewservices.assetcatalog.rest.LineageSearchResponse;
import org.odpi.openmetadata.viewservices.assetcatalog.rest.LineageTypesResponse;
import org.odpi.openmetadata.viewservices.assetcatalog.rest.LineageVertexResponse;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


/**
 * The AssetCatalogRESTServices provides the implementation of the Asset Catalog Open Metadata View Service (OMVS).
 * This interface provides view interfaces for glossary UIs.
 */

public class AssetCatalogRESTServices extends TokenController
{
    private static final AssetCatalogInstanceHandler instanceHandler = new AssetCatalogInstanceHandler();

    private static final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(AssetCatalogRESTServices.class),
                                                                            instanceHandler.getServiceName());

    private final PropertyHelper propertyHelper = new PropertyHelper();

    private final String sourceName = ViewServiceDescription.ASSET_CATALOG.getViewServiceName();

    /**
     * Default constructor
     */
    public AssetCatalogRESTServices()
    {
    }


    /**
     * Return a list of assets matching the search criteria without the full context
     *
     * @param serverName name of the server to route the request to
     * @param searchCriteria the query parameter with the search phrase
     * @param types OM types list to search for
     * @param sequencingProperty name of the property based on which to sort the result
     * @param sequencingOrder PROPERTY_ASCENDING or PROPERTY_DESCENDING
     * @param caseSensitive set case sensitive flag
     * @param exactMatch set exact match flag
     * @param startFrom the offset for the results
     * @param pageSize the number of results per page
     * @return list of assets or
     * PropertyServerException if a configuration on the backend
     * UserNotAuthorizedException security access problem
     * InvalidParameterException if parameter validation fails
     */
    public AssetListResponse searchAssets(String          serverName,
                                          String          searchCriteria,
                                          List<String>    types,
                                          String          sequencingProperty,
                                          SequencingOrder sequencingOrder,
                                          boolean         caseSensitive,
                                          boolean         exactMatch,
                                          Integer         startFrom,
                                          Integer         pageSize)
    {
        final String methodName = "searchAssets";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        AssetListResponse response = new AssetListResponse();
        AuditLog          auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OpenMetadataStoreClient handler = instanceHandler.getOpenMetadataStoreClient(userId, serverName, methodName);

            SearchProperties searchProperties = new SearchProperties();


            instanceHandler.getSearchString(searchCriteria, false, false, !caseSensitive);

            List<OpenMetadataElement> results = handler.findMetadataElements(userId,
                                                                             null,
                                                                             types,
                                                                             null,
                                                                             null,
                                                                             null,
                                                                             sequencingProperty,
                                                                             sequencingOrder,
                                                                             false,
                                                                             false,
                                                                             null,
                                                                             startFrom,
                                                                             pageSize);

            response.setElementsList(this.getElementsList(results));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Return a list of assets matching the type name without the full context
     * The list includes also subtypes
     *
     * @param serverName name of the server to route the request to
     * @param typeName the assets type name to search for
     * @return list of assets by type name or
     * PropertyServerException if a configuration on the backend
     * UserNotAuthorizedException security access problem
     * InvalidParameterException if parameter validation fails
     */
    public AssetListResponse searchAssetsByTypeName(String serverName,
                                                    String typeName)
    {
        // todo
        return null;
    }


    /**
     * Return a list of assets matching the type GUID without the full context
     * The list includes also subtypes
     *
     * @param serverName name of the server to route the request to
     * @param typeGUID the assets type GUID to search for
     * @return list of assets by type GUID or
     * PropertyServerException if a configuration on the backend
     * UserNotAuthorizedException security access problem
     * InvalidParameterException if parameter validation fails
     */
    public AssetListResponse searchAssetsByTypeGUID(String serverName,
                                                    String typeGUID)
    {
        // todo
        return null;
    }


    /**
     * Returns the list with supported types for search, including the subtypes supported
     *
     * @param serverName name of the server to route the request to
     * @return the supported types from Asset Consumer OMAS or
     * PropertyServerException if a configuration on the backend
     * InvalidParameterException if parameter validation fails
     * UserNotAuthorizedException security access problem
     */
    public AssetCatalogSupportedTypes getSupportedTypes(String serverName)
    {
        // todo
        return null;
    }


    /**
     * Fetch asset's header, classification and properties.
     *
     * @param serverName name of the server to route the request to
     * @param guid of the Entity to be retrieved
     * @return the entity details or
     * PropertyServerException if a configuration on the backend
     * UserNotAuthorizedException security access problem
     * InvalidParameterException if parameter validation fails
     */
    public AssetCatalogResponse getAsset(String serverName,
                                         String guid)
    {
        // todo
        return null;
    }


    /**
     * Return the full context of an asset/glossary term based on its identifier.
     * The response contains the list of the connections assigned to the asset.
     *
     * @param serverName name of the server to route the request to
     * @param guid of the Entity to be retrieved
     * @return the entity context or
     *  PropertyServerException if a configuration on the backend
     *  UserNotAuthorizedException security access problem
     *  InvalidParameterException if parameter validation fails
     */
    public AssetResponse getAssetContext(String serverName,
                                         String guid)
    {
        // todo
        return null;
    }


    /**
     * Return the nodes and relationships that describe the know ultimate sources of the starting element.
     *
     * @param serverName name of the server to route the request to
     * @param guid unique identifier of the starting element
     * @param includeProcesses if true Process nodes will be included
     * @return graph of nodes and edges describing the ultimate sources of the asset or
     *  InvalidParameterException from the underlying service,
     *  PropertyServerException from the underlying service or
     *  UserNotAuthorizedException from the underlying service
     */
    public LineageResponse getUltimateSource(String  serverName,
                                             String  guid,
                                             boolean includeProcesses)
    {
        // todo
        return null;
    }


    /**
     * Return the graph of nodes that describe the end-to-end lineage for the starting element.
     *
     * @param serverName name of the server to route the request to
     * @param guid unique identifier of the starting element
     * @param includeProcesses if true Process nodes will be included
     * @return graph of nodes and edges describing the end to end flow or
     *  InvalidParameterException from the underlying service,
     *  PropertyServerException from the underlying service or
     *  UserNotAuthorizedException from the underlying service
     */
    public LineageResponse getEndToEndLineage(String serverName,
                                              String guid,
                                              boolean includeProcesses)
    {
        // todo
        return null;
    }


    /**
     * Return the nodes and relationships that describe the know ultimate destinations of the starting element.
     *
     * @param serverName name of the server to route the request to
     * @param guid unique identifier of the starting element
     * @param includeProcesses if true Process nodes will be included
     * @return graph of nodes and edges describing the ultimate destination of the asset or
     *  InvalidParameterException from the underlying service,
     *  PropertyServerException from the underlying service or
     *  UserNotAuthorizedException from the underlying service
     */
    public LineageResponse getUltimateDestination(String  serverName,
                                                  String  guid,
                                                  boolean includeProcesses)
    {
        // todo
        return null;
    }


    /**
     * Retrieve details of the asset related elements linked to a glossary term via the semantic assignment relationship.
     *
     * @param serverName name of the server to route the request to
     * @param guid unique identifier of the starting glossary term element
     * @param includeProcesses if true Process nodes will be included
     * @return graph of nodes and edges describing the assets linked to the glossary term or
     *  InvalidParameterException from the underlying service,
     *  PropertyServerException from the underlying service or
     *  UserNotAuthorizedException from the underlying service
     */
    public LineageResponse getSemanticLineage(String serverName,
                                              String guid,
                                              boolean includeProcesses)
    {
        // todo
        return null;
    }


    /**
     * Retrieves the details of a specific node in the lineage graph.
     *
     * @param serverName name of the server to route the request to
     * @param guid of the Entity to be retrieved
     * @return the entity details or
     *  InvalidParameterException from the underlying service,
     *  PropertyServerException from the underlying service or
     *  UserNotAuthorizedException from the underlying service
     */
    public LineageVertexResponse getLineageVertex(String serverName,
                                                  String guid)
    {
        // todo
        return null;
    }


    /**
     * Gets available entities types from lineage repository.
     *
     * @param serverName name of the server to route the request to
     * @return the available entities types or
     *  InvalidParameterException from the underlying service,
     *  PropertyServerException from the underlying service or
     *  UserNotAuthorizedException from the underlying service
     */
    public LineageTypesResponse getTypes(String serverName)
    {
        // todo
        return null;
    }


    /**
     * Gets nodes names of certain type with display name containing a certain value.
     *
     * @param serverName name of the server to route the request to
     * @param type        the type of the nodes name to search for
     * @param searchValue the string to be contained in the qualified name of the node - case-insensitive
     * @param limit       the maximum number of node names to retrieve
     *
     * @return the list of node names or
     *  InvalidParameterException from the underlying service,
     *  PropertyServerException from the underlying service or
     *  UserNotAuthorizedException from the underlying service
     */
    public LineageNodeNamesResponse getNodes(String serverName,
                                             String type,
                                             String searchValue,
                                             int    limit)
    {
        // todo
        return null;
    }


    /**
     * Returns the list of vertices that the user will initially see when querying lineage. In the future, this method will be
     * extended to condense large paths to prevent cluttering of the users screen. The user will be able to extend
     * the condensed path by querying a different method.
     *
     * @param serverName name of the server to route the request to
     * @param searchRequest filtering details for the search
     * @return list of graph nodes or
     *  InvalidParameterException from the underlying service,
     *  PropertyServerException from the underlying service or
     *  UserNotAuthorizedException from the underlying service
     */
    public LineageSearchResponse searchForVertices(String               serverName,
                                                   LineageSearchRequest searchRequest)
    {
        // todo
        return null;
    }


    /**
     * Returns a subgraph representing the hierarchy of a certain node, based on the request.
     *
     * @param serverName name of the server to route the request to
     * @param elementHierarchyRequest contains the guid of the queried node and the hierarchyType of the display name of the nodes
     *
     * @return a subgraph containing all relevant paths or
     *  InvalidParameterException from the underlying service,
     *  PropertyServerException from the underlying service or
     *  UserNotAuthorizedException from the underlying service
     */
    public LineageResponse getElementHierarchy(String                  serverName,
                                               ElementHierarchyRequest elementHierarchyRequest)
    {
        // todo
        return null;
    }


    private List<Elements> getElementsList(List<OpenMetadataElement> openMetadataElements)
    {
        // todo
        return null;
    }


    /**
     *
     * @param openMetadataElement
     * @return
     */
    private Element getElement(OpenMetadataElement openMetadataElement)
    {
        final String methodName = "getElement";

        if (openMetadataElement != null)
        {
            Elements result = new Elements();

            result.setGuid(openMetadataElement.getElementGUID());
            result.setType(this.getType(openMetadataElement.getType()));
            result.setName(propertyHelper.getStringProperty(instanceHandler.getServiceName(),
                                                            OpenMetadataProperty.NAME.name,
                                                            openMetadataElement.getElementProperties(),
                                                            methodName));


            return result;
        }

        return null;
    }


    private AssetCatalogBean getAssetCatalogBean(OpenMetadataElement           openMetadataElement,
                                                 List<RelatedMetadataElements> relatedMetadataElementsList,
                                                 Element                       anchorElement,
                                                 String                        methodName)
    {
        if (openMetadataElement != null)
        {
            AssetCatalogBean assetCatalogBean = new AssetCatalogBean();

            assetCatalogBean.setGuid(openMetadataElement.getElementGUID());
            assetCatalogBean.setType(this.getType(openMetadataElement.getType()));
            assetCatalogBean.setCreatedBy(openMetadataElement.getVersions().getCreatedBy());
            assetCatalogBean.setCreateTime(openMetadataElement.getVersions().getCreateTime());
            assetCatalogBean.setUpdatedBy(openMetadataElement.getVersions().getUpdatedBy());
            assetCatalogBean.setUpdateTime(openMetadataElement.getVersions().getUpdateTime());
            assetCatalogBean.setVersion(openMetadataElement.getVersions().getVersion());
            assetCatalogBean.setStatus(openMetadataElement.getStatus().getName());
            assetCatalogBean.setClassifications(this.getClassifications(openMetadataElement.getClassifications()));
            assetCatalogBean.setOrigin(this.getElementOrigin(openMetadataElement.getOrigin()));
            assetCatalogBean.setUrl("/api/open-metadata/asset-catalog/assets/" + openMetadataElement.getElementGUID());


            if (openMetadataElement.getElementProperties() != null)
            {
                /*
                 * Properties that are explicitly mapped are removed from the copy of the element properties.  The properties that are left are
                 * added to the assetCatalogBean's properties value.
                 */
                ElementProperties elementProperties = new ElementProperties((openMetadataElement.getElementProperties()));

                assetCatalogBean.setName(propertyHelper.removeStringProperty(sourceName,
                                                                             OpenMetadataProperty.NAME.name,
                                                                             elementProperties,
                                                                             methodName));

                if (assetCatalogBean.getName() == null)
                {
                    assetCatalogBean.setName(propertyHelper.removeStringProperty(sourceName,
                                                                                 OpenMetadataProperty.DISPLAY_NAME.name,
                                                                                 elementProperties,
                                                                                 methodName));
                }

                if (assetCatalogBean.getName() == null)
                {
                    assetCatalogBean.setName(propertyHelper.removeStringProperty(sourceName,
                                                                                 OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                 elementProperties,
                                                                                 methodName));
                }

                assetCatalogBean.setAdditionalProperties(propertyHelper.removeStringMapFromProperty(sourceName,
                                                                                                    OpenMetadataProperty.ADDITIONAL_PROPERTIES.name,
                                                                                                    elementProperties,
                                                                                                    methodName));

                assetCatalogBean.setProperties(elementProperties.getPropertiesAsStrings());
                assetCatalogBean.setAnchorElement(anchorElement);
            }

            assetCatalogBean.setRelationships(this.getRelationships(relatedMetadataElementsList));
        }

        return null;
    }


    /**
     * Convert a list classifications from open metadata to the asset catalog format.
     *
     * @param openMetadataClassifications classifications retrieved from the open metadata repositories
     * @return classification format used by asset catalog
     */
    private List<Classification> getClassifications(List<AttachedClassification> openMetadataClassifications)
    {
        if (openMetadataClassifications != null)
        {
            List<Classification> results = new ArrayList<>();

            for (AttachedClassification openMetadataClassification : openMetadataClassifications)
            {

            }

            return results;
        }

        return null;
    }


    /**
     * Return a list of relationships in the Asset Catalog OMVS format.
     *
     * @param relatedMetadataElementsList relationships retrieved from the open metadata store
     * @return reformatted relationships
     */
    private List<Relationship> getRelationships(List<RelatedMetadataElements> relatedMetadataElementsList)
    {
        if (relatedMetadataElementsList != null)
        {
            List<Relationship> results = new ArrayList<>();

            for (RelatedMetadataElements relatedMetadataElements : relatedMetadataElementsList)
            {

            }

            return results;
        }

        return null;
    }


    /**
     * Return the element origin in the Asset Catalog OMVS format.
     *
     * @param openMetadataElementOrigin retrieved origin
     * @return reformatted origin
     */
    private ElementOrigin getElementOrigin(org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementOrigin openMetadataElementOrigin)
    {
        if (openMetadataElementOrigin != null)
        {
            ElementOrigin result = new ElementOrigin();

            result.setInstanceLicense(openMetadataElementOrigin.getLicense());
            result.setOriginCategory(openMetadataElementOrigin.getOriginCategory());
            result.setMetadataCollectionId(openMetadataElementOrigin.getHomeMetadataCollectionId());
            result.setMetadataCollectionName(openMetadataElementOrigin.getHomeMetadataCollectionName());
            result.setSourceServer(openMetadataElementOrigin.getSourceServer());

            return result;
        }

        return null;
    }


    /**
     * Convert the open metadata type information into the required format.
     *
     * @param openMetadataType retrieved type information
     * @return type information in Asset Catalog format
     */
    private Type getType(ElementType openMetadataType)
    {
        if (openMetadataType != null)
        {
            Type type = new Type();

            type.setName(openMetadataType.getTypeName());
            type.setDescription(openMetadataType.getTypeDescription());
            type.setVersion(openMetadataType.getTypeVersion());

            if ((openMetadataType.getSuperTypeNames() != null) && (! openMetadataType.getSuperTypeNames().isEmpty()))
            {
                type.setSuperType(openMetadataType.getSuperTypeNames().get(0));
            }

            return type;
        }

        return null;
    }
}
