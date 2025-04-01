/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.gaf.client;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.enums.SequencingOrder;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementHeader;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ElementStatus;
import org.odpi.openmetadata.frameworks.governanceaction.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ToDoStatus;
import org.odpi.openmetadata.frameworks.openmetadata.mapper.PropertyFacetValidValues;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ArchiveProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.contextevents.ContextEventImpactProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.contextevents.ContextEventProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.contextevents.DependentContextEventProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.contextevents.RelatedContextEventProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.governanceaction.properties.*;
import org.odpi.openmetadata.frameworks.governanceaction.search.*;
import org.odpi.openmetadata.frameworkservices.gaf.client.rest.GAFRESTClient;
import org.odpi.openmetadata.frameworkservices.gaf.ffdc.OpenMetadataStoreErrorCode;
import org.odpi.openmetadata.frameworkservices.gaf.rest.*;
import org.odpi.openmetadata.frameworkservices.gaf.rest.ArchiveRequestBody;
import org.odpi.openmetadata.frameworkservices.gaf.rest.EffectiveTimeQueryRequestBody;
import org.odpi.openmetadata.frameworkservices.gaf.rest.MetadataSourceRequestBody;
import org.odpi.openmetadata.frameworkservices.gaf.rest.NewOpenMetadataElementRequestBody;
import org.odpi.openmetadata.frameworkservices.gaf.rest.TemplateRequestBody;

import java.util.*;
import java.util.regex.Pattern;

/**
 * OpenMetadataClientBase provides an interface to the open metadata store.  This is part of the Governance Action Framework (GAF)
 * and provides a comprehensive interface for working with all types of metadata, subject to the user's (and this OMAS's) security permissions.
 * The interface supports search, maintenance of metadata elements, classifications and relationships plus the ability to raise incident reports
 * and todos along with the ability to work with metadata valid values and translations.
 */
public abstract class OpenMetadataClientBase extends OpenMetadataClient
{
    private   final GAFRESTClient restClient;               /* Initialized in constructor */

    protected final InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();

    private final PropertyHelper  propertyHelper  = new PropertyHelper();


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param maxPageSize maximum value allowed for page size
     *
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     *                                   REST API calls.
     */
    public OpenMetadataClientBase(String serviceURLMarker,
                                  String serverName,
                                  String serverPlatformURLRoot,
                                  int    maxPageSize) throws InvalidParameterException
    {
        super(serviceURLMarker, serverName, serverPlatformURLRoot);

        final String methodName = "Constructor (no security)";

        this.invalidParameterHandler.setMaxPagingSize(maxPageSize);
        this.invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);
        this.restClient = new GAFRESTClient(serverName, serverPlatformURLRoot);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param serverUserId          caller's userId embedded in all HTTP requests
     * @param serverPassword        caller's password embedded in all HTTP requests
     * @param maxPageSize maximum value allowed for page size
     *
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     *                                   REST API calls.
     */
    public OpenMetadataClientBase(String serviceURLMarker,
                                  String serverName,
                                  String serverPlatformURLRoot,
                                  String serverUserId,
                                  String serverPassword,
                                  int    maxPageSize) throws InvalidParameterException
    {
        super(serviceURLMarker, serverName, serverPlatformURLRoot);

        final String methodName = "Constructor (with security)";

        this.invalidParameterHandler.setMaxPagingSize(maxPageSize);
        this.invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.restClient = new GAFRESTClient(serverName, serverPlatformURLRoot, serverUserId, serverPassword);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param restClient            pre-initialized REST client
     * @param maxPageSize           pre-initialized parameter limit
     *
     * @throws InvalidParameterException there is a problem with the information about the remote OMAS
     */
    public OpenMetadataClientBase(String                      serviceURLMarker,
                                  String                      serverName,
                                  String                      serverPlatformURLRoot,
                                  GAFRESTClient               restClient,
                                  int                         maxPageSize) throws InvalidParameterException
    {
        super(serviceURLMarker, serverName, serverPlatformURLRoot);

        final String methodName = "Client Constructor (with REST client)";

        this.invalidParameterHandler.setMaxPagingSize(maxPageSize);
        this.invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.restClient = restClient;
    }


    /**
     * Set the max page size.
     *
     * @param maxPageSize pre-initialized parameter limit
     */
    public void setMaxPageSize(int maxPageSize)
    {
        invalidParameterHandler.setMaxPagingSize(maxPageSize);
    }


    /**
     * Get the maximum paging size.
     *
     * @return maxPagingSize new value
     */
    @Override
    public int getMaxPagingSize() { return  invalidParameterHandler.getMaxPagingSize(); }


    /**
     * Returns the list of different types of metadata organized into two groups.  The first are the
     * attribute type definitions (AttributeTypeDefs).  These provide types for properties in full
     * type definitions.  Full type definitions (TypeDefs) describe types for entities, relationships
     * and classifications.
     *
     * @param userId unique identifier for requesting user.
     *
     * @return TypeDefGallery  List of different categories of type definitions.
     *
     * @throws InvalidParameterException  the userId is null
     * @throws PropertyServerException    there is a problem communicating with the metadata repository.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public OpenMetadataTypeDefGallery getAllTypes(String userId) throws InvalidParameterException,
                                                                        PropertyServerException,
                                                                        UserNotAuthorizedException
    {
        final String methodName  = "getAllTypes";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-metadata-store/users/{2}/open-metadata-types";

        invalidParameterHandler.validateUserId(userId, methodName);

        TypeDefGalleryResponse restResult = restClient.callTypeDefGalleryGetRESTCall(methodName,
                                                                                     urlTemplate,
                                                                                     serverName,
                                                                                     serviceURLMarker,
                                                                                     userId);

        if (restResult != null)
        {
            OpenMetadataTypeDefGallery gallery = new OpenMetadataTypeDefGallery();

            gallery.setTypeDefs(restResult.getTypeDefs());
            gallery.setAttributeTypeDefs(restResult.getAttributeTypeDefs());

            return gallery;
        }

        return null;
    }


    /**
     * Returns all the TypeDefs for a specific category.
     *
     * @param userId   unique identifier for requesting user.
     * @param category enum value for the category of TypeDef to return.
     *
     * @return TypeDefs list.
     *
     * @throws InvalidParameterException  the TypeDefCategory is null.
     * @throws PropertyServerException    there is a problem communicating with the metadata repository.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public List<OpenMetadataTypeDef> findTypeDefsByCategory(String                      userId,
                                                            OpenMetadataTypeDefCategory category) throws InvalidParameterException,
                                                                                                         PropertyServerException,
                                                                                                         UserNotAuthorizedException
    {
        final String methodName  = "findTypeDefsByCategory";
        final String categoryParameterName  = "category";
        final String entityURLTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-metadata-store/users/{2}/open-metadata-types/entity-defs";
        final String relationshipURLTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-metadata-store/users/{2}/open-metadata-types/relationship-defs";
        final String classificationURLTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-metadata-store/users/{2}/open-metadata-types/classification-defs";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateEnum(category, categoryParameterName, methodName);

        String urlTemplate = entityURLTemplate;

        if (category == OpenMetadataTypeDefCategory.CLASSIFICATION_DEF)
        {
            urlTemplate = classificationURLTemplate;
        }
        else if (category == OpenMetadataTypeDefCategory.RELATIONSHIP_DEF)
        {
            urlTemplate = relationshipURLTemplate;
        }

        TypeDefListResponse restResult = restClient.callTypeDefListGetRESTCall(methodName,
                                                                               urlTemplate,
                                                                               serverName,
                                                                               serviceURLMarker,
                                                                               userId);

        return restResult.getTypeDefs();
    }


    /**
     * Returns all the AttributeTypeDefs for a specific category.
     *
     * @param userId   unique identifier for requesting user.
     * @param category enum value for the category of an AttributeTypeDef to return.
     *
     * @return AttributeTypeDefs list.
     *
     * @throws InvalidParameterException  the TypeDefCategory is null.
     * @throws PropertyServerException    there is a problem communicating with the metadata repository.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public List<OpenMetadataAttributeTypeDef> findAttributeTypeDefsByCategory(String                               userId,
                                                                              OpenMetadataAttributeTypeDefCategory category) throws InvalidParameterException,
                                                                                                                                    PropertyServerException,
                                                                                                                                    UserNotAuthorizedException
    {
        final String methodName  = "findAttributeTypeDefsByCategory";
        final String categoryParameterName  = "category";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-metadata-store/users/{2}/open-metadata-attribute-types/category/{3}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateEnum(category, categoryParameterName, methodName);

        AttributeTypeDefListResponse restResult = restClient.callAttributeTypeDefListGetRESTCall(methodName,
                                                                                                 urlTemplate,
                                                                                                 serverName,
                                                                                                 serviceURLMarker,
                                                                                                 userId,
                                                                                                 category);

        return restResult.getAttributeTypeDefs();
    }


    /**
     * Return the types that are linked to the elements from the specified standard.
     *
     * @param userId       unique identifier for requesting user.
     * @param standard     name of the standard null means any.
     * @param organization name of the organization null means any.
     * @param identifier   identifier of the element in the standard null means any.
     *
     * @return TypeDefs list  each entry in the list contains a TypeDef.  This is a structure
     * describing the TypeDef's category and properties.
     *
     * @throws InvalidParameterException  all attributes of the external id are null.
     * @throws PropertyServerException    there is a problem communicating with the metadata repository.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public List<OpenMetadataTypeDef> findTypesByExternalId(String userId,
                                                           String standard,
                                                           String organization,
                                                           String identifier) throws InvalidParameterException,
                                                                                     PropertyServerException,
                                                                                     UserNotAuthorizedException
    {
        final String methodName  = "findTypesByExternalId";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-metadata-store/users/{2}/open-metadata-types/external-id?standard={3}&organization={4}&identifier={5}";

        invalidParameterHandler.validateUserId(userId, methodName);

        TypeDefListResponse restResult = restClient.callTypeDefListGetRESTCall(methodName,
                                                                               urlTemplate,
                                                                               serverName,
                                                                               serviceURLMarker,
                                                                               userId,
                                                                               standard,
                                                                               organization,
                                                                               identifier);


        return restResult.getTypeDefs();
    }


    /**
     * Returns all the TypeDefs for a specific subtype.  If a null result is returned it means the
     * type has no subtypes.     *
     * @param userId       unique identifier for requesting user.
     * @param typeName     name of the standard null means any.
     *
     * @return TypeDefs list  each entry in the list contains a TypeDef.  This is a structure
     * describing the TypeDef's category and properties.  If null is returned as the TypeDef list it means the type
     * has no known subtypes
     *
     * @throws InvalidParameterException  all attributes of the external id are null.
     * @throws PropertyServerException    there is a problem communicating with the metadata repository.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public List<OpenMetadataTypeDef> getSubTypes(String userId,
                                                 String typeName) throws InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException
    {
        final String methodName  = "getSubTypes";
        final String parameterName = "typeName";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-metadata-store/users/{2}/open-metadata-types/sub-types?typeName={3}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(typeName, parameterName, methodName);

        TypeDefListResponse restResult = restClient.callTypeDefListGetRESTCall(methodName,
                                                                               urlTemplate,
                                                                               serverName,
                                                                               serviceURLMarker,
                                                                               userId,
                                                                               typeName);


        return restResult.getTypeDefs();
    }


    /**
     * Return the TypeDef identified by the GUID.
     *
     * @param userId unique identifier for requesting user.
     * @param guid   String unique id of the TypeDef
     *
     * @return TypeDef structure describing its category and properties.
     *
     * @throws InvalidParameterException  the guid is null.
     * @throws PropertyServerException    there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public OpenMetadataTypeDef getTypeDefByGUID(String userId,
                                                String guid) throws InvalidParameterException,
                                                                    PropertyServerException,
                                                                    UserNotAuthorizedException
    {
        final String methodName  = "getTypeDefByGUID";
        final String guidParameterName  = "guid";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-metadata-store/users/{2}/open-metadata-types/guid/{3}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(guid, guidParameterName, methodName);

        TypeDefResponse restResult = restClient.callTypeDefGetRESTCall(methodName,
                                                                       urlTemplate,
                                                                       serverName,
                                                                       serviceURLMarker,
                                                                       userId,
                                                                       guid);


        return restResult.getTypeDef();
    }


    /**
     * Return the AttributeTypeDef identified by the GUID.
     *
     * @param userId unique identifier for requesting user.
     * @param guid   String unique id of the TypeDef
     *
     * @return TypeDef structure describing its category and properties.
     *
     * @throws InvalidParameterException  the guid is null.
     * @throws PropertyServerException    there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public OpenMetadataAttributeTypeDef getAttributeTypeDefByGUID(String userId,
                                                                  String guid) throws InvalidParameterException,
                                                                                      PropertyServerException,
                                                                                      UserNotAuthorizedException
    {
        final String methodName  = "getAttributeTypeDefByGUID";
        final String guidParameterName  = "guid";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-metadata-store/users/{2}/open-metadata-attribute-types/guid/{3}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(guid, guidParameterName, methodName);

        AttributeTypeDefResponse restResult = restClient.callAttributeTypeDefGetRESTCall(methodName,
                                                                                         urlTemplate,
                                                                                         serverName,
                                                                                         serviceURLMarker,
                                                                                         userId,
                                                                                         guid);


        return restResult.getAttributeTypeDef();
    }


    /**
     * Return the TypeDef identified by the unique name.
     *
     * @param userId unique identifier for requesting user.
     * @param name   String name of the TypeDef.
     *
     * @return TypeDef structure describing its category and properties.
     *
     * @throws InvalidParameterException  the name is null.
     * @throws PropertyServerException    there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public OpenMetadataTypeDef getTypeDefByName(String userId,
                                                String name) throws InvalidParameterException,
                                                                    PropertyServerException,
                                                                    UserNotAuthorizedException
    {
        final String methodName  = "getTypeDefByName";
        final String nameParameterName  = "name";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-metadata-store/users/{2}/open-metadata-types/name/{3}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameterName, methodName);

        TypeDefResponse restResult = restClient.callTypeDefGetRESTCall(methodName,
                                                                       urlTemplate,
                                                                       serverName,
                                                                       serviceURLMarker,
                                                                       userId,
                                                                       name);


        return restResult.getTypeDef();
    }


    /**
     * Return the AttributeTypeDef identified by the unique name.
     *
     * @param userId unique identifier for requesting user.
     * @param name   String name of the TypeDef.
     *
     * @return TypeDef structure describing its category and properties.
     *
     * @throws InvalidParameterException  the name is null.
     * @throws PropertyServerException    there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public OpenMetadataAttributeTypeDef getAttributeTypeDefByName(String userId,
                                                                  String name) throws InvalidParameterException,
                                                                                      PropertyServerException,
                                                                                      UserNotAuthorizedException
    {
        final String methodName  = "getAttributeTypeDefByName";
        final String nameParameterName  = "name";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-metadata-store/users/{2}/open-metadata-attribute-types/name/{3}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameterName, methodName);

        AttributeTypeDefResponse restResult = restClient.callAttributeTypeDefGetRESTCall(methodName,
                                                                                         urlTemplate,
                                                                                         serverName,
                                                                                         serviceURLMarker,
                                                                                         userId,
                                                                                         name);

        return restResult.getAttributeTypeDef();
    }


    /**
     * Retrieve the metadata element using its unique identifier.
     *
     * @param userId      caller's userId
     * @param elementGUID unique identifier for the metadata element
     * @param forLineage             the retrieved element is for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param effectiveTime          only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     *
     * @return metadata element properties
     *
     * @throws InvalidParameterException  the unique identifier is null or not known.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    @Override
    public OpenMetadataElement getMetadataElementByGUID(String  userId,
                                                        String  elementGUID,
                                                        boolean forLineage,
                                                        boolean forDuplicateProcessing,
                                                        Date    asOfTime,
                                                        Date    effectiveTime) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        final String methodName        = "getMetadataElementByGUID";
        final String guidParameterName = "elementGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-metadata-store/users/{2}/metadata-elements/{3}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(elementGUID, guidParameterName, methodName);

        AnyTimeRequestBody requestBody = new AnyTimeRequestBody();

        requestBody.setAsOfTime(asOfTime);
        requestBody.setForLineage(forLineage);
        requestBody.setForDuplicateProcessing(forDuplicateProcessing);
        requestBody.setEffectiveTime(effectiveTime);

        OpenMetadataElementResponse restResult = restClient.callOpenMetadataElementPostRESTCall(methodName,
                                                                                               urlTemplate,
                                                                                               requestBody,
                                                                                               serverName,
                                                                                               serviceURLMarker,
                                                                                               userId,
                                                                                               elementGUID);

        return restResult.getElement();
    }


    /**
     * Retrieve the metadata element using its unique name (typically the qualified name).
     *
     * @param userId                 caller's userId
     * @param uniqueName             unique name for the metadata element
     * @param uniquePropertyName     name of property name to test in the open metadata element - if null "qualifiedName" is used
     * @param forLineage             the retrieved element is for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param effectiveTime          only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     *
     * @return metadata element properties or null if not found
     *
     * @throws InvalidParameterException the unique identifier is null.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    @Override
    public OpenMetadataElement getMetadataElementByUniqueName(String  userId,
                                                              String  uniqueName,
                                                              String  uniquePropertyName,
                                                              boolean forLineage,
                                                              boolean forDuplicateProcessing,
                                                              Date    asOfTime,
                                                              Date    effectiveTime) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        final String methodName          = "getMetadataElementByUniqueName";
        final String defaultPropertyName = OpenMetadataProperty.QUALIFIED_NAME.name;
        final String nameParameterName   = "uniqueName";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-metadata-store/users/{2}/metadata-elements/by-unique-name?forLineage={3}&forDuplicateProcessing={4}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(uniqueName, nameParameterName, methodName);

        NameRequestBody requestBody = new NameRequestBody();
        requestBody.setName(uniqueName);
        requestBody.setAsOfTime(asOfTime);
        requestBody.setEffectiveTime(effectiveTime);
        requestBody.setNameParameterName(nameParameterName);

        if (uniquePropertyName != null)
        {
            requestBody.setNamePropertyName(uniquePropertyName);
        }
        else
        {
            requestBody.setNamePropertyName(defaultPropertyName);
        }

        OpenMetadataElementResponse restResult = restClient.callOpenMetadataElementPostRESTCall(methodName,
                                                                                                urlTemplate,
                                                                                                requestBody,
                                                                                                serverName,
                                                                                                serviceURLMarker,
                                                                                                userId,
                                                                                                forLineage,
                                                                                                forDuplicateProcessing);

        return restResult.getElement();
    }


    /**
     * Retrieve the metadata element using its unique name (typically the qualified name) and the DELETED status.
     * This method assumes all effective dates, and forLineage and forDuplicateProcessing are false,
     * to cast the widest net.
     *
     * @param userId                 caller's userId
     * @param uniqueName unique name for the metadata element
     * @param uniquePropertyName name of property name to test in the open metadata element - if null "qualifiedName" is used
     *
     * @return metadata element properties or null if not found
     * @throws InvalidParameterException the unique identifier is null.
     * @throws UserNotAuthorizedException the governance action service is not able to access the element
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    public OpenMetadataElement getDeletedElementByUniqueName(String  userId,
                                                             String  uniqueName,
                                                             String  uniquePropertyName) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException
    {
        final String methodName          = "getDeletedElementByUniqueName";
        final String defaultPropertyName = OpenMetadataProperty.QUALIFIED_NAME.name;
        final String nameParameterName   = "uniqueName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(uniqueName, nameParameterName, methodName);

        List<String> propertyNames = new ArrayList<>();

        if (uniquePropertyName != null)
        {
            propertyNames.add(uniquePropertyName);
        }
        else
        {
            propertyNames.add(defaultPropertyName);
        }

        List<ElementStatus> requiredStatuses = new ArrayList<>();

        requiredStatuses.add(ElementStatus.DELETED);

        List<OpenMetadataElement> matchingElements = this.findMetadataElements(userId,
                                                                               OpenMetadataType.OPEN_METADATA_ROOT.typeName,
                                                                               null,
                                                                               propertyHelper.getSearchPropertiesByName(propertyNames, uniqueName, PropertyComparisonOperator.EQ),
                                                                               requiredStatuses,
                                                                               null,
                                                                               null,
                                                                               null,
                                                                               SequencingOrder.LAST_UPDATE_RECENT,
                                                                               true,
                                                                               true,
                                                                               null,
                                                                               0,
                                                                               10);

        if (matchingElements != null)
        {
            for (OpenMetadataElement matchingElement : matchingElements)
            {
                if (matchingElement != null)
                {
                    return matchingElement;
                }
            }
        }

        return null;
    }


    /**
     * Retrieve the unique identifier of a metadata element using its unique name (typically the qualified name).
     *
     * @param userId                 caller's userId
     * @param uniqueName             unique name for the metadata element
     * @param uniquePropertyName     name of property name to test in the open metadata element - if null "qualifiedName" is used
     * @param forLineage             the retrieved element is for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param effectiveTime          only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     *
     * @return metadata element unique identifier (guid)
     *
     * @throws InvalidParameterException  the unique name is null or not known.
     * @throws UserNotAuthorizedException the caller's userId is not able to access the element
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    @Override
    public String getMetadataElementGUIDByUniqueName(String  userId,
                                                     String  uniqueName,
                                                     String  uniquePropertyName,
                                                     boolean forLineage,
                                                     boolean forDuplicateProcessing,
                                                     Date    asOfTime,
                                                     Date    effectiveTime) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        final String methodName          = "getMetadataElementGUIDByUniqueName";
        final String defaultPropertyName = "qualifiedName";
        final String nameParameterName   = "uniqueName";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-metadata-store/users/{2}/metadata-elements/guid-by-unique-name?forLineage={3}&forDuplicateProcessing={4}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(uniqueName, nameParameterName, methodName);

        NameRequestBody requestBody = new NameRequestBody();
        requestBody.setName(uniqueName);
        requestBody.setNameParameterName(nameParameterName);
        requestBody.setAsOfTime(asOfTime);
        requestBody.setEffectiveTime(effectiveTime);

        if (uniquePropertyName != null)
        {
            requestBody.setNamePropertyName(uniquePropertyName);
        }
        else
        {
            requestBody.setNamePropertyName(defaultPropertyName);
        }

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  serviceURLMarker,
                                                                  userId,
                                                                  forLineage,
                                                                  forDuplicateProcessing);

        return restResult.getGUID();
    }


    /**
     * Retrieve the metadata elements that contain the requested string.
     *
     * @param userId                 caller's userId
     * @param searchString           name to retrieve
     * @param limitResultsByStatus By default, relationships in all statuses (other than DELETE) are returned.  However, it is possible
     *                             to specify a list of statuses (for example ACTIVE) to restrict the results to.  Null means all status values.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingProperty String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param forLineage the retrieved element is for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     * @param startFrom              paging start point
     * @param pageSize               maximum results that can be returned
     *
     * @return list of matching metadata elements (or null if no elements match the name)
     *
     * @throws InvalidParameterException  the qualified name is null
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    @Override
    public List<OpenMetadataElement> findMetadataElementsWithString(String              userId,
                                                                    String              searchString,
                                                                    List<ElementStatus> limitResultsByStatus,
                                                                    Date                asOfTime,
                                                                    String              sequencingProperty,
                                                                    SequencingOrder     sequencingOrder,
                                                                    boolean             forLineage,
                                                                    boolean             forDuplicateProcessing,
                                                                    Date                effectiveTime,
                                                                    int                 startFrom,
                                                                    int                 pageSize) throws InvalidParameterException,
                                                                                                         UserNotAuthorizedException,
                                                                                                         PropertyServerException
    {
        return this.findMetadataElementsWithString(userId, searchString, null, limitResultsByStatus, asOfTime, sequencingProperty, sequencingOrder, forLineage, forDuplicateProcessing, effectiveTime, startFrom, pageSize);
    }


    /**
     * Retrieve the metadata elements of the requested type that contain the requested string.
     *
     * @param userId                 caller's userId
     * @param searchString           name to retrieve
     * @param typeName               name of the type to limit the results to (maybe null to mean all types)
     * @param limitResultsByStatus By default, relationships in all statuses (other than DELETE) are returned.  However, it is possible
     *                             to specify a list of statuses (for example ACTIVE) to restrict the results to.  Null means all status values.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingProperty String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param forLineage the retrieved element is for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     * @param startFrom              paging start point
     * @param pageSize               maximum results that can be returned
     *
     * @return list of matching metadata elements (or null if no elements match the name)
     *
     * @throws InvalidParameterException  the qualified name is null
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    @Override
    public List<OpenMetadataElement> findMetadataElementsWithString(String              userId,
                                                                    String              searchString,
                                                                    String              typeName,
                                                                    List<ElementStatus> limitResultsByStatus,
                                                                    Date                asOfTime,
                                                                    String              sequencingProperty,
                                                                    SequencingOrder     sequencingOrder,
                                                                    boolean             forLineage,
                                                                    boolean             forDuplicateProcessing,
                                                                    Date                effectiveTime,
                                                                    int                 startFrom,
                                                                    int                 pageSize) throws InvalidParameterException,
                                                                                                         UserNotAuthorizedException,
                                                                                                         PropertyServerException
    {
        final String methodName                = "findMetadataElementsWithString";
        final String searchStringParameterName = "searchString";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-metadata-store/users/{2}/metadata-elements/by-search-string?forLineage={3}&forDuplicateProcessing={4}&startFrom={5}&pageSize={6}";

        invalidParameterHandler.validateUserId(userId, methodName);
        if (typeName == null)
        {
            invalidParameterHandler.validateSearchString(searchString, searchStringParameterName, methodName);
        }

        SearchStringRequestBody requestBody = new SearchStringRequestBody();

        requestBody.setSearchString(searchString);
        requestBody.setSearchStringParameterName(searchStringParameterName);
        requestBody.setEffectiveTime(effectiveTime);
        requestBody.setLimitResultsByStatus(limitResultsByStatus);
        requestBody.setAsOfTime(asOfTime);
        requestBody.setSequencingOrder(sequencingOrder);
        requestBody.setSequencingProperty(sequencingProperty);
        requestBody.setTypeName(typeName);

        OpenMetadataElementsResponse restResult = restClient.callOpenMetadataElementsPostRESTCall(methodName,
                                                                                                  urlTemplate,
                                                                                                  requestBody,
                                                                                                  serverName,
                                                                                                  serviceURLMarker,
                                                                                                  userId,
                                                                                                  forLineage,
                                                                                                  forDuplicateProcessing,
                                                                                                  startFrom,
                                                                                                  pageSize);

        return restResult.getElementList();
    }


    /**
     * Retrieve the metadata elements connected to the supplied element.
     *
     * @param userId                 caller's userId
     * @param elementGUID            unique identifier for the starting metadata element
     * @param startingAtEnd          indicates which end to retrieve from (0 is "either end"; 1 is end1; 2 is end 2)
     * @param relationshipTypeName   type name of relationships to follow (or null for all)
     * @param limitResultsByStatus By default, relationships in all statuses (other than DELETE) are returned.  However, it is possible
     *                             to specify a list of statuses (for example ACTIVE) to restrict the results to.  Null means all status values.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingProperty String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param forLineage the retrieved element is for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     * @param startFrom              paging start point
     * @param pageSize               maximum results that can be returned
     *
     * @return list of related elements
     *
     * @throws InvalidParameterException  the unique identifier is null or not known; the relationship type is invalid
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    @Override
    public   RelatedMetadataElementList getRelatedMetadataElements(String              userId,
                                                                   String              elementGUID,
                                                                   int                 startingAtEnd,
                                                                   String              relationshipTypeName,
                                                                   List<ElementStatus> limitResultsByStatus,
                                                                   Date                asOfTime,
                                                                   String              sequencingProperty,
                                                                   SequencingOrder     sequencingOrder,
                                                                   boolean             forLineage,
                                                                   boolean             forDuplicateProcessing,
                                                                   Date                effectiveTime,
                                                                   int                 startFrom,
                                                                   int                 pageSize) throws InvalidParameterException,
                                                                                                        UserNotAuthorizedException,
                                                                                                        PropertyServerException
    {
        final String methodName            = "getRelatedMetadataElements";
        final String guidParameterName     = "elementGUID";

        final String allURLTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-metadata-store/users/{2}/related-elements/{3}/any-type?startingAtEnd={4}&forLineage={5}&forDuplicateProcessing={6}&startFrom={7}&pageSize={8}";
        final String specificURLTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-metadata-store/users/{2}/related-elements/{3}/type/{4}?startingAtEnd={5}&forLineage={6}&forDuplicateProcessing={7}&startFrom={8}&pageSize={9}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(elementGUID, guidParameterName, methodName);

        RelatedMetadataElementListResponse restResult;

        ResultsRequestBody requestBody = new ResultsRequestBody();
        requestBody.setEffectiveTime(effectiveTime);
        requestBody.setLimitResultsByStatus(limitResultsByStatus);
        requestBody.setAsOfTime(asOfTime);
        requestBody.setSequencingOrder(sequencingOrder);
        requestBody.setSequencingProperty(sequencingProperty);

        if (relationshipTypeName == null)
        {
            restResult = restClient.callRelatedMetadataElementListPostRESTCall(methodName,
                                                                               allURLTemplate,
                                                                               requestBody,
                                                                               serverName,
                                                                               serviceURLMarker,
                                                                               userId,
                                                                               elementGUID,
                                                                               startingAtEnd,
                                                                               forLineage,
                                                                               forDuplicateProcessing,
                                                                               startFrom,
                                                                               pageSize);
        }
        else
        {
            restResult = restClient.callRelatedMetadataElementListPostRESTCall(methodName,
                                                                               specificURLTemplate,
                                                                               requestBody,
                                                                               serverName,
                                                                               serviceURLMarker,
                                                                               userId,
                                                                               elementGUID,
                                                                               relationshipTypeName,
                                                                               startingAtEnd,
                                                                               forLineage,
                                                                               forDuplicateProcessing,
                                                                               startFrom,
                                                                               pageSize);
        }

        return restResult.getRelatedElementList();
    }


    /**
     * Return all the elements that are anchored to an asset plus relationships between these elements and to other elements.
     *
     * @param userId name of the server instances for this request
     * @param elementGUID  unique identifier for the element
     * @param forLineage the retrieved element is for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved elements are for duplicate processing so do not combine results from known duplicates.
     * @param startFrom starting element (used in paging through large result sets)
     * @param pageSize maximum number of results to return
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param effectiveTime only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     *
     * @return graph of elements
     *
     * @throws InvalidParameterException  the unique identifier is null or not known; the relationship type is invalid
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    @Override
    public OpenMetadataElementGraph getAnchoredElementsGraph(String             userId,
                                                             String             elementGUID,
                                                             boolean            forLineage,
                                                             boolean            forDuplicateProcessing,
                                                             int                startFrom,
                                                             int                pageSize,
                                                             Date               asOfTime,
                                                             Date               effectiveTime) throws InvalidParameterException,
                                                                                                       UserNotAuthorizedException,
                                                                                                       PropertyServerException
    {
        final String methodName        = "getAnchoredElementsGraph";
        final String guidParameterName = "elementGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-metadata-store/users/{2}/metadata-elements/{3}/with-anchored-elements?startFrom={4}&pageSize={5}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(elementGUID, guidParameterName, methodName);

        AnyTimeRequestBody requestBody = new AnyTimeRequestBody();

        requestBody.setAsOfTime(asOfTime);
        requestBody.setForLineage(forLineage);
        requestBody.setForDuplicateProcessing(forDuplicateProcessing);
        requestBody.setEffectiveTime(effectiveTime);

        OpenMetadataGraphResponse restResult = restClient.callOpenMetadataGraphPostRESTCall(methodName,
                                                                                            urlTemplate,
                                                                                            requestBody,
                                                                                            serverName,
                                                                                            serviceURLMarker,
                                                                                            userId,
                                                                                            elementGUID,
                                                                                            startFrom,
                                                                                            pageSize);

        return restResult.getElementGraph();
    }


    /**
     * Retrieve the metadata element connected to the supplied element for a relationship type that only allows one
     * relationship to be attached.
     *
     * @param userId                 caller's userId
     * @param elementGUID            unique identifier for the starting metadata element
     * @param startingAtEnd          indicates which end to retrieve from (0 is "either end"; 1 is end1; 2 is end 2)
     * @param relationshipTypeName   type name of relationships to follow (or null for all)
     * @param forLineage             the retrieved element is for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved elements are for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime          only return an element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     *
     * @return list of related elements
     *
     * @throws InvalidParameterException  the unique identifier is null or not known; the relationship type is invalid
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException    there is a problem accessing the metadata store or multiple relationships have been returned
     */
    @Override
    public RelatedMetadataElement getRelatedMetadataElement(String  userId,
                                                            String  elementGUID,
                                                            int     startingAtEnd,
                                                            String  relationshipTypeName,
                                                            boolean forLineage,
                                                            boolean forDuplicateProcessing,
                                                            Date    effectiveTime) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        final String methodName = "getRelatedMetadataElement";

        RelatedMetadataElementList  relationships = this.getRelatedMetadataElements(userId,
                                                                                     elementGUID,
                                                                                     startingAtEnd,
                                                                                     relationshipTypeName,
                                                                                     null,
                                                                                     null,
                                                                                     null,
                                                                                     SequencingOrder.CREATION_DATE_RECENT,
                                                                                     forLineage,
                                                                                     forDuplicateProcessing,
                                                                                     effectiveTime,
                                                                                     0,
                                                                                     0);

        if ((relationships != null) && (relationships.getElementList() != null) && (!relationships.getElementList().isEmpty()))
        {
            if (relationships.getElementList().size() == 1)
            {
                return relationships.getElementList().get(0);
            }
            else
            {
                RelatedMetadataElement result = null;

                for (RelatedMetadataElement relatedMetadataElement : relationships.getElementList())
                {
                    if (relatedMetadataElement != null)
                    {
                        if (result == null)
                        {
                            result = relatedMetadataElement;
                        }
                        else
                        {
                            /*
                             * Multiple relationships have been returned
                             */
                            throw new PropertyServerException(OpenMetadataStoreErrorCode.MULTIPLE_RELATIONSHIPS_FOUND.getMessageDefinition(relationshipTypeName, elementGUID),
                                                              this.getClass().getName(),
                                                              methodName);
                        }
                    }
                }

                return result;
            }
        }

        return null;
    }


    /**
     * Return each of the versions of a metadata element.
     *
     * @param userId caller's userId
     * @param elementGUID            unique identifier for the metadata element
     * @param fromTime the earliest point in time from which to retrieve historical versions of the element (inclusive)
     * @param toTime the latest point in time from which to retrieve historical versions of the element (exclusive)
     * @param oldestFirst  defining how the results should be ordered.
     * @param forLineage the retrieved element is for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return a list of elements matching the supplied criteria; null means no matching elements in the metadata store.
     * @throws InvalidParameterException one of the search parameters are is invalid
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    @Override
    public  List<OpenMetadataElement> getMetadataElementHistory(String  userId,
                                                                String  elementGUID,
                                                                Date    fromTime,
                                                                Date    toTime,
                                                                boolean oldestFirst,
                                                                boolean forLineage,
                                                                boolean forDuplicateProcessing,
                                                                Date    effectiveTime,
                                                                int     startFrom,
                                                                int     pageSize) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        final String methodName = "getMetadataElementHistory";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-metadata-store/users/{2}/metadata-elements/{3}/history?forLineage={4}&forDuplicateProcessing={5}&oldestFirst={6}&startFrom={7}&pageSize={8}";

        invalidParameterHandler.validateUserId(userId, methodName);

        HistoryRequestBody requestBody = new HistoryRequestBody();

        requestBody.setFromTime(fromTime);
        requestBody.setToTime(toTime);
        requestBody.setEffectiveTime(effectiveTime);

        OpenMetadataElementsResponse restResult = restClient.callOpenMetadataElementsPostRESTCall(methodName,
                                                                                                  urlTemplate,
                                                                                                  requestBody,
                                                                                                  serverName,
                                                                                                  serviceURLMarker,
                                                                                                  userId,
                                                                                                  elementGUID,
                                                                                                  forLineage,
                                                                                                  forDuplicateProcessing,
                                                                                                  oldestFirst,
                                                                                                  startFrom,
                                                                                                  pageSize);

        return restResult.getElementList();
    }


    /**
     * Retrieve the relationships linking to the supplied elements.
     *
     * @param userId caller's userId
     * @param metadataElementAtEnd1GUID unique identifier of the metadata element at end 1 of the relationship
     * @param metadataElementAtEnd2GUID unique identifier of the metadata element at end 2 of the relationship
     * @param relationshipTypeName type name of relationships to follow (or null for all)
     * @param limitResultsByStatus By default, relationships in all statuses (other than DELETE) are returned.  However, it is possible
     *                             to specify a list of statuses (for example ACTIVE) to restrict the results to.  Null means all status values.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingProperty String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param forLineage the retrieved element is for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of related elements
     * @throws InvalidParameterException the unique identifier is null or not known; the relationship type is invalid
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    @Override
    public   OpenMetadataRelationshipList getMetadataElementRelationships(String              userId,
                                                                          String              metadataElementAtEnd1GUID,
                                                                          String              metadataElementAtEnd2GUID,
                                                                          String              relationshipTypeName,
                                                                          List<ElementStatus> limitResultsByStatus,
                                                                          Date                asOfTime,
                                                                          String              sequencingProperty,
                                                                          SequencingOrder     sequencingOrder,
                                                                          boolean             forLineage,
                                                                          boolean             forDuplicateProcessing,
                                                                          Date                effectiveTime,
                                                                          int                 startFrom,
                                                                          int                 pageSize) throws InvalidParameterException,
                                                                                                               UserNotAuthorizedException,
                                                                                                               PropertyServerException
    {
        final String methodName            = "getRelatedMetadataElements";
        final String guid1ParameterName    = "metadataElementAtEnd1GUID";

        final String allURLTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-metadata-store/users/{2}/metadata-elements/{3}/linked-by-any-type/to-elements/{4}?&forLineage={5}&forDuplicateProcessing={6}&startFrom={7}&pageSize={8}";
        final String specificURLTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-metadata-store/users/{2}/metadata-elements/{3}/linked-by-type/{4}/to-elements/{5}?forLineage={6}&forDuplicateProcessing={7}&startFrom={8}&pageSize={9}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(metadataElementAtEnd1GUID, guid1ParameterName, methodName);

        ResultsRequestBody requestBody = new ResultsRequestBody();
        requestBody.setEffectiveTime(effectiveTime);
        requestBody.setLimitResultsByStatus(limitResultsByStatus);
        requestBody.setAsOfTime(asOfTime);
        requestBody.setSequencingOrder(sequencingOrder);
        requestBody.setSequencingProperty(sequencingProperty);

        OpenMetadataRelationshipListResponse restResult;

        if (relationshipTypeName == null)
        {
            restResult = restClient.callOpenMetadataRelationshipListPostRESTCall(methodName,
                                                                                 allURLTemplate,
                                                                                 requestBody,
                                                                                 serverName,
                                                                                 serviceURLMarker,
                                                                                 userId,
                                                                                 metadataElementAtEnd1GUID,
                                                                                 metadataElementAtEnd2GUID,
                                                                                 forLineage,
                                                                                 forDuplicateProcessing,
                                                                                 startFrom,
                                                                                 pageSize);
        }
        else
        {
            restResult = restClient.callOpenMetadataRelationshipListPostRESTCall(methodName,
                                                                                 specificURLTemplate,
                                                                                 requestBody,
                                                                                 serverName,
                                                                                 serviceURLMarker,
                                                                                 userId,
                                                                                 metadataElementAtEnd1GUID,
                                                                                 relationshipTypeName,
                                                                                 metadataElementAtEnd2GUID,
                                                                                 forLineage,
                                                                                 forDuplicateProcessing,
                                                                                 startFrom,
                                                                                 pageSize);
        }

        return restResult.getRelationshipList();
    }


    /**
     * Retrieve metadata elements with any of the supplied list of property names similar to the supplied value.
     *
     * @param userId                     caller's userId
     * @param metadataElementTypeName    type of interest (null means any element type)
     * @param metadataElementSubtypeNames optional list of the subtypes of the metadataElementTypeName to
     *                                   include in the search results. Null means all subtypes.
     * @param classificationName         name of the classification
     * @param classificationPropertyName  property name to match against
     * @param classificationPropertyValue value to match against
     * @param limitResultsByStatus       By default, entities in all statuses (other than DELETE) are returned.  However, it is possible
     *                                   to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all status values.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingProperty         String name of the property that is to be used to sequence the results.
     *                                   Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder            Enum defining how the results should be ordered.
     * @param forLineage                 the retrieved element is for lineage processing so include archived elements
     * @param forDuplicateProcessing     the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime              only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     * @param startFrom                  paging start point
     * @param pageSize                   maximum results that can be returned
     *
     * @return a list of elements matching the supplied criteria; null means no matching elements in the metadata store.
     *
     * @throws InvalidParameterException  one of the search parameters are is invalid
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    public List<OpenMetadataElement> getMetadataElementsByClassificationPropertyValue(String                userId,
                                                                                      String                metadataElementTypeName,
                                                                                      List<String>          metadataElementSubtypeNames,
                                                                                      String                classificationName,
                                                                                      String                classificationPropertyName,
                                                                                      String                classificationPropertyValue,
                                                                                      List<ElementStatus>   limitResultsByStatus,
                                                                                      Date                  asOfTime,
                                                                                      String                sequencingProperty,
                                                                                      SequencingOrder       sequencingOrder,
                                                                                      boolean               forLineage,
                                                                                      boolean               forDuplicateProcessing,
                                                                                      Date                  effectiveTime,
                                                                                      int                   startFrom,
                                                                                      int                   pageSize) throws InvalidParameterException,
                                                                                                                             UserNotAuthorizedException,
                                                                                                                             PropertyServerException
    {
        PrimitiveTypePropertyValue requestedPropertyValue = new PrimitiveTypePropertyValue();

        requestedPropertyValue.setPrimitiveTypeCategory(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_STRING);
        requestedPropertyValue.setPrimitiveValue(classificationPropertyValue);
        requestedPropertyValue.setTypeName(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_STRING.getName());

        List<PropertyCondition> conditions = new ArrayList<>();

        PropertyCondition nameCondition = new PropertyCondition();

        nameCondition.setProperty(classificationPropertyName);
        nameCondition.setOperator(PropertyComparisonOperator.EQ);
        nameCondition.setValue(requestedPropertyValue);

        conditions.add(nameCondition);

        SearchProperties searchClassificationProperties = new SearchProperties();

        searchClassificationProperties.setConditions(conditions);
        searchClassificationProperties.setMatchCriteria(MatchCriteria.ANY);

        SearchClassifications searchClassifications = new SearchClassifications();

        List<ClassificationCondition> classificationConditions = new ArrayList<>();
        ClassificationCondition       classificationCondition  = new ClassificationCondition();

        classificationCondition.setName(classificationName);

        classificationCondition.setSearchProperties(searchClassificationProperties);
        classificationConditions.add(classificationCondition);

        searchClassifications.setConditions(classificationConditions);
        searchClassifications.setMatchCriteria(MatchCriteria.ALL);

        return this.findMetadataElements(userId,
                                         metadataElementTypeName,
                                         metadataElementSubtypeNames,
                                         null,
                                         limitResultsByStatus,
                                         asOfTime,
                                         searchClassifications,
                                         sequencingProperty,
                                         sequencingOrder,
                                         forLineage,
                                         forDuplicateProcessing,
                                         effectiveTime,
                                         startFrom,
                                         pageSize);
    }



    /**
     * Retrieve metadata elements with any of the supplied list of property names similar to the supplied value.
     *
     * @param userId                     caller's userId
     * @param metadataElementTypeName    type of interest (null means any element type)
     * @param metadataElementSubtypeNames optional list of the subtypes of the metadataElementTypeName to
     *                                   include in the search results. Null means all subtypes.
     * @param classificationName         name of the classification
     * @param classificationPropertyName  property name to match against
     * @param classificationPropertyValue value to match against
     * @param limitResultsByStatus       By default, entities in all statuses (other than DELETE) are returned.  However, it is possible
     *                                   to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all status values.
     * @param asOfTime                   Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingProperty         String name of the property that is to be used to sequence the results.
     *                                   Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder            Enum defining how the results should be ordered.
     * @param forLineage                 the retrieved element is for lineage processing so include archived elements
     * @param forDuplicateProcessing     the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime              only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     * @param startFrom                  paging start point
     * @param pageSize                   maximum results that can be returned
     *
     * @return a list of elements matching the supplied criteria; null means no matching elements in the metadata store.
     *
     * @throws InvalidParameterException  one of the search parameters are is invalid
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    public List<OpenMetadataElement> getMetadataElementsByClassificationPropertyValue(String                userId,
                                                                                      String                metadataElementTypeName,
                                                                                      List<String>          metadataElementSubtypeNames,
                                                                                      String                classificationName,
                                                                                      String                classificationPropertyName,
                                                                                      int                   classificationPropertyValue,
                                                                                      List<ElementStatus>   limitResultsByStatus,
                                                                                      Date                  asOfTime,
                                                                                      String                sequencingProperty,
                                                                                      SequencingOrder       sequencingOrder,
                                                                                      boolean               forLineage,
                                                                                      boolean               forDuplicateProcessing,
                                                                                      Date                  effectiveTime,
                                                                                      int                   startFrom,
                                                                                      int                   pageSize) throws InvalidParameterException,
                                                                                                                             UserNotAuthorizedException,
                                                                                                                             PropertyServerException
    {
        PrimitiveTypePropertyValue requestedPropertyValue = new PrimitiveTypePropertyValue();

        requestedPropertyValue.setPrimitiveTypeCategory(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_INT);
        requestedPropertyValue.setPrimitiveValue(classificationPropertyValue);
        requestedPropertyValue.setTypeName(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_INT.getName());

        List<PropertyCondition> conditions = new ArrayList<>();

        PropertyCondition nameCondition = new PropertyCondition();

        nameCondition.setProperty(classificationPropertyName);
        nameCondition.setOperator(PropertyComparisonOperator.EQ);
        nameCondition.setValue(requestedPropertyValue);

        conditions.add(nameCondition);

        SearchProperties searchClassificationProperties = new SearchProperties();

        searchClassificationProperties.setConditions(conditions);
        searchClassificationProperties.setMatchCriteria(MatchCriteria.ANY);

        SearchClassifications searchClassifications = new SearchClassifications();

        List<ClassificationCondition> classificationConditions = new ArrayList<>();
        ClassificationCondition       classificationCondition  = new ClassificationCondition();

        classificationCondition.setName(classificationName);

        classificationCondition.setSearchProperties(searchClassificationProperties);
        classificationConditions.add(classificationCondition);

        searchClassifications.setConditions(classificationConditions);
        searchClassifications.setMatchCriteria(MatchCriteria.ALL);

        return this.findMetadataElements(userId,
                                         metadataElementTypeName,
                                         metadataElementSubtypeNames,
                                         null,
                                         limitResultsByStatus,
                                         asOfTime,
                                         searchClassifications,
                                         sequencingProperty,
                                         sequencingOrder,
                                         forLineage,
                                         forDuplicateProcessing,
                                         effectiveTime,
                                         startFrom,
                                         pageSize);
    }


    /**
     * Retrieve metadata elements with any of the supplied list of property names similar to the supplied value.
     *
     * @param userId                     caller's userId
     * @param metadataElementTypeName    type of interest (null means any element type)
     * @param metadataElementSubtypeNames optional list of the subtypes of the metadataElementTypeName to
     *                                   include in the search results. Null means all subtypes.
     * @param classificationName         name of the classification
     * @param classificationPropertyName  property name to match against
     * @param classificationPropertyValue value to match against
     * @param limitResultsByStatus       By default, entities in all statuses (other than DELETE) are returned.  However, it is possible
     *                                   to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all status values.
     * @param asOfTime                   Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingProperty         String name of the property that is to be used to sequence the results.
     *                                   Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder            Enum defining how the results should be ordered.
     * @param forLineage                 the retrieved element is for lineage processing so include archived elements
     * @param forDuplicateProcessing     the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime              only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     * @param startFrom                  paging start point
     * @param pageSize                   maximum results that can be returned
     *
     * @return a list of elements matching the supplied criteria; null means no matching elements in the metadata store.
     *
     * @throws InvalidParameterException  one of the search parameters are is invalid
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    public List<OpenMetadataElement> findMetadataElementsByClassificationPropertyValue(String                userId,
                                                                                       String                metadataElementTypeName,
                                                                                       List<String>          metadataElementSubtypeNames,
                                                                                       String                classificationName,
                                                                                       String                classificationPropertyName,
                                                                                       String                classificationPropertyValue,
                                                                                       List<ElementStatus>   limitResultsByStatus,
                                                                                       Date                  asOfTime,
                                                                                       String                sequencingProperty,
                                                                                       SequencingOrder       sequencingOrder,
                                                                                       boolean               forLineage,
                                                                                       boolean               forDuplicateProcessing,
                                                                                       Date                  effectiveTime,
                                                                                       int                   startFrom,
                                                                                       int                   pageSize) throws InvalidParameterException,
                                                                                                                              UserNotAuthorizedException,
                                                                                                                              PropertyServerException
    {
        PrimitiveTypePropertyValue requestedPropertyValue = new PrimitiveTypePropertyValue();

        requestedPropertyValue.setPrimitiveTypeCategory(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_STRING);
        requestedPropertyValue.setPrimitiveValue(".*" + Pattern.quote(classificationPropertyValue) + ".*");
        requestedPropertyValue.setTypeName(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_STRING.getName());

        List<PropertyCondition> conditions = new ArrayList<>();

        PropertyCondition nameCondition = new PropertyCondition();

        nameCondition.setProperty(classificationPropertyName);
        nameCondition.setOperator(PropertyComparisonOperator.LIKE);
        nameCondition.setValue(requestedPropertyValue);

        conditions.add(nameCondition);

        SearchProperties searchClassificationProperties = new SearchProperties();

        searchClassificationProperties.setConditions(conditions);
        searchClassificationProperties.setMatchCriteria(MatchCriteria.ANY);

        SearchClassifications searchClassifications = new SearchClassifications();

        List<ClassificationCondition> classificationConditions = new ArrayList<>();
        ClassificationCondition       classificationCondition  = new ClassificationCondition();

        classificationCondition.setName(classificationName);

        classificationCondition.setSearchProperties(searchClassificationProperties);
        classificationConditions.add(classificationCondition);

        searchClassifications.setConditions(classificationConditions);
        searchClassifications.setMatchCriteria(MatchCriteria.ALL);

        return this.findMetadataElements(userId,
                                         metadataElementTypeName,
                                         metadataElementSubtypeNames,
                                         null,
                                         limitResultsByStatus,
                                         asOfTime,
                                         searchClassifications,
                                         sequencingProperty,
                                         sequencingOrder,
                                         forLineage,
                                         forDuplicateProcessing,
                                         effectiveTime,
                                         startFrom,
                                         pageSize);
    }


    /**
     * Retrieve metadata elements with the requested classification containing any of the supplied list of property
     * names exactly matching the supplied value.
     *
     * @param userId                     caller's userId
     * @param metadataElementTypeName    type of interest (null means any element type)
     * @param metadataElementSubtypeNames optional list of the subtypes of the metadataElementTypeName to
     *                                   include in the search results. Null means all subtypes.
     * @param classificationName         name of the classification
     * @param classificationPropertyNames property name to match against
     * @param classificationPropertyValue value to match against
     * @param limitResultsByStatus       By default, entities in all statuses (other than DELETE) are returned.  However, it is possible
     *                                   to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all status values.
     * @param asOfTime                   Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingProperty         String name of the property that is to be used to sequence the results.
     *                                   Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder            Enum defining how the results should be ordered.
     * @param forLineage                 the retrieved element is for lineage processing so include archived elements
     * @param forDuplicateProcessing     the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime              only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     * @param startFrom                  paging start point
     * @param pageSize                   maximum results that can be returned
     *
     * @return a list of elements matching the supplied criteria; null means no matching elements in the metadata store.
     *
     * @throws InvalidParameterException  one of the search parameters are is invalid
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    public List<OpenMetadataElement> getMetadataElementsByClassificationPropertyValue(String                userId,
                                                                                      String                metadataElementTypeName,
                                                                                      List<String>          metadataElementSubtypeNames,
                                                                                      String                classificationName,
                                                                                      List<String>          classificationPropertyNames,
                                                                                      String                classificationPropertyValue,
                                                                                      List<ElementStatus>   limitResultsByStatus,
                                                                                      Date                  asOfTime,
                                                                                      String                sequencingProperty,
                                                                                      SequencingOrder       sequencingOrder,
                                                                                      boolean               forLineage,
                                                                                      boolean               forDuplicateProcessing,
                                                                                      Date                  effectiveTime,
                                                                                      int                   startFrom,
                                                                                      int                   pageSize) throws InvalidParameterException,
                                                                                                                             UserNotAuthorizedException,
                                                                                                                             PropertyServerException
    {
        PrimitiveTypePropertyValue requestedPropertyValue = new PrimitiveTypePropertyValue();

        requestedPropertyValue.setPrimitiveTypeCategory(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_STRING);
        requestedPropertyValue.setPrimitiveValue(classificationPropertyValue);
        requestedPropertyValue.setTypeName(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_STRING.getName());

        List<PropertyCondition> conditions = new ArrayList<>();

        if (classificationPropertyNames != null)
        {
            for (String propertyName : classificationPropertyNames)
            {
                PropertyCondition nameCondition = new PropertyCondition();

                nameCondition.setProperty(propertyName);
                nameCondition.setOperator(PropertyComparisonOperator.EQ);
                nameCondition.setValue(requestedPropertyValue);

                conditions.add(nameCondition);
            }
        }

        SearchProperties searchClassificationProperties = new SearchProperties();

        searchClassificationProperties.setConditions(conditions);
        searchClassificationProperties.setMatchCriteria(MatchCriteria.ANY);

        SearchClassifications searchClassifications = new SearchClassifications();

        List<ClassificationCondition> classificationConditions = new ArrayList<>();
        ClassificationCondition       classificationCondition  = new ClassificationCondition();

        classificationCondition.setName(classificationName);

        classificationCondition.setSearchProperties(searchClassificationProperties);
        classificationConditions.add(classificationCondition);

        searchClassifications.setConditions(classificationConditions);
        searchClassifications.setMatchCriteria(MatchCriteria.ALL);

        return this.findMetadataElements(userId,
                                         metadataElementTypeName,
                                         metadataElementSubtypeNames,
                                         null,
                                         limitResultsByStatus,
                                         asOfTime,
                                         searchClassifications,
                                         sequencingProperty,
                                         sequencingOrder,
                                         forLineage,
                                         forDuplicateProcessing,
                                         effectiveTime,
                                         startFrom,
                                         pageSize);
    }


    /**
     * Retrieve metadata elements with the requested classification containing any of the supplied list of property
     * names exactly matching the supplied value.
     *
     * @param userId                     caller's userId
     * @param metadataElementTypeName    type of interest (null means any element type)
     * @param metadataElementSubtypeNames optional list of the subtypes of the metadataElementTypeName to
     *                                   include in the search results. Null means all subtypes.
     * @param classificationName         name of the classification
     * @param classificationPropertyNames property name to match against
     * @param classificationPropertyValue value to match against
     * @param limitResultsByStatus       By default, entities in all statuses (other than DELETE) are returned.  However, it is possible
     *                                   to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all status values.
     * @param asOfTime                   Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingProperty         String name of the property that is to be used to sequence the results.
     *                                   Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder            Enum defining how the results should be ordered.
     * @param forLineage                 the retrieved element is for lineage processing so include archived elements
     * @param forDuplicateProcessing     the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime              only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     * @param startFrom                  paging start point
     * @param pageSize                   maximum results that can be returned
     *
     * @return a list of elements matching the supplied criteria; null means no matching elements in the metadata store.
     *
     * @throws InvalidParameterException  one of the search parameters are is invalid
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    public List<OpenMetadataElement> findMetadataElementsByClassificationPropertyValue(String                userId,
                                                                                       String                metadataElementTypeName,
                                                                                       List<String>          metadataElementSubtypeNames,
                                                                                       String                classificationName,
                                                                                       List<String>          classificationPropertyNames,
                                                                                       String                classificationPropertyValue,
                                                                                       List<ElementStatus>   limitResultsByStatus,
                                                                                       Date                  asOfTime,
                                                                                       String                sequencingProperty,
                                                                                       SequencingOrder       sequencingOrder,
                                                                                       boolean               forLineage,
                                                                                       boolean               forDuplicateProcessing,
                                                                                       Date                  effectiveTime,
                                                                                       int                   startFrom,
                                                                                       int                   pageSize) throws InvalidParameterException,
                                                                                                                              UserNotAuthorizedException,
                                                                                                                              PropertyServerException
    {
        PrimitiveTypePropertyValue requestedPropertyValue = new PrimitiveTypePropertyValue();

        requestedPropertyValue.setPrimitiveTypeCategory(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_STRING);
        requestedPropertyValue.setPrimitiveValue(".*" + Pattern.quote(classificationPropertyValue) + ".*");
        requestedPropertyValue.setTypeName(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_STRING.getName());

        List<PropertyCondition> conditions = new ArrayList<>();

        if (classificationPropertyNames != null)
        {
            for (String propertyName : classificationPropertyNames)
            {
                PropertyCondition nameCondition = new PropertyCondition();

                nameCondition.setProperty(propertyName);
                nameCondition.setOperator(PropertyComparisonOperator.LIKE);
                nameCondition.setValue(requestedPropertyValue);

                conditions.add(nameCondition);
            }
        }

        SearchProperties searchClassificationProperties = new SearchProperties();

        searchClassificationProperties.setConditions(conditions);
        searchClassificationProperties.setMatchCriteria(MatchCriteria.ANY);

        SearchClassifications searchClassifications = new SearchClassifications();

        List<ClassificationCondition> classificationConditions = new ArrayList<>();
        ClassificationCondition       classificationCondition  = new ClassificationCondition();

        classificationCondition.setName(classificationName);

        classificationCondition.setSearchProperties(searchClassificationProperties);
        classificationConditions.add(classificationCondition);

        searchClassifications.setConditions(classificationConditions);
        searchClassifications.setMatchCriteria(MatchCriteria.ALL);

        return this.findMetadataElements(userId,
                                         metadataElementTypeName,
                                         metadataElementSubtypeNames,
                                         null,
                                         limitResultsByStatus,
                                         asOfTime,
                                         searchClassifications,
                                         sequencingProperty,
                                         sequencingOrder,
                                         forLineage,
                                         forDuplicateProcessing,
                                         effectiveTime,
                                         startFrom,
                                         pageSize);
    }


    /**
     * Retrieve metadata elements with any of the supplied list of property names similar to the supplied value.
     *
     * @param userId                     caller's userId
     * @param metadataElementTypeName    type of interest (null means any element type)
     * @param metadataElementSubtypeNames optional list of the subtypes of the metadataElementTypeName to
     *                                   include in the search results. Null means all subtypes.
     * @param classificationName         name of the classification
     * @param limitResultsByStatus       By default, entities in all statuses (other than DELETE) are returned.  However, it is possible
     *                                   to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all status values.
     * @param asOfTime                   Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingProperty         String name of the property that is to be used to sequence the results.
     *                                   Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder            Enum defining how the results should be ordered.
     * @param forLineage                 the retrieved element is for lineage processing so include archived elements
     * @param forDuplicateProcessing     the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime              only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     * @param startFrom                  paging start point
     * @param pageSize                   maximum results that can be returned
     *
     * @return a list of elements matching the supplied criteria; null means no matching elements in the metadata store.
     *
     * @throws InvalidParameterException  one of the search parameters are is invalid
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    public List<OpenMetadataElement> getMetadataElementsByClassification(String                userId,
                                                                         String                metadataElementTypeName,
                                                                         List<String>          metadataElementSubtypeNames,
                                                                         String                classificationName,
                                                                         List<ElementStatus>   limitResultsByStatus,
                                                                         Date                  asOfTime,
                                                                         String                sequencingProperty,
                                                                         SequencingOrder       sequencingOrder,
                                                                         boolean               forLineage,
                                                                         boolean               forDuplicateProcessing,
                                                                         Date                  effectiveTime,
                                                                         int                   startFrom,
                                                                         int                   pageSize) throws InvalidParameterException,
                                                                                                                UserNotAuthorizedException,
                                                                                                                PropertyServerException
    {
        SearchClassifications searchClassifications =  new SearchClassifications();

        List<ClassificationCondition> classificationConditions = new ArrayList<>();
        ClassificationCondition       classificationCondition  = new ClassificationCondition();

        classificationCondition.setName(classificationName);

        classificationConditions.add(classificationCondition);

        searchClassifications.setConditions(classificationConditions);
        searchClassifications.setMatchCriteria(MatchCriteria.ALL);

        return this.findMetadataElements(userId,
                                         metadataElementTypeName,
                                         metadataElementSubtypeNames,
                                         null,
                                         limitResultsByStatus,
                                         asOfTime,
                                         searchClassifications,
                                         sequencingProperty,
                                         sequencingOrder,
                                         forLineage,
                                         forDuplicateProcessing,
                                         effectiveTime,
                                         startFrom,
                                         pageSize);
    }


    /**
     * Retrieve metadata elements with any of the supplied list of property names similar to the supplied value.
     *
     * @param userId                     caller's userId
     * @param metadataElementTypeName    type of interest (null means any element type)
     * @param metadataElementSubtypeNames optional list of the subtypes of the metadataElementTypeName to
     *                                   include in the search results. Null means all subtypes.
     * @param propertyName               property name to match against
     * @param propertyValue              value to match against
     * @param limitResultsByStatus       By default, entities in all statuses (other than DELETE) are returned.  However, it is possible
     *                                   to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all status values.
     * @param asOfTime                   Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingProperty         String name of the property that is to be used to sequence the results.
     *                                   Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder            Enum defining how the results should be ordered.
     * @param forLineage                 the retrieved element is for lineage processing so include archived elements
     * @param forDuplicateProcessing     the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime              only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     * @param startFrom                  paging start point
     * @param pageSize                   maximum results that can be returned
     *
     * @return a list of elements matching the supplied criteria; null means no matching elements in the metadata store.
     *
     * @throws InvalidParameterException  one of the search parameters are is invalid
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    public List<OpenMetadataElement> getMetadataElementsByPropertyValue(String                userId,
                                                                        String                metadataElementTypeName,
                                                                        List<String>          metadataElementSubtypeNames,
                                                                        String                propertyName,
                                                                        String                propertyValue,
                                                                        List<ElementStatus>   limitResultsByStatus,
                                                                        Date                  asOfTime,
                                                                        String                sequencingProperty,
                                                                        SequencingOrder       sequencingOrder,
                                                                        boolean               forLineage,
                                                                        boolean               forDuplicateProcessing,
                                                                        Date                  effectiveTime,
                                                                        int                   startFrom,
                                                                        int                   pageSize) throws InvalidParameterException,
                                                                                                               UserNotAuthorizedException,
                                                                                                               PropertyServerException
    {
        List<String> propertyNames = List.of(propertyName);

        return this.findMetadataElements(userId,
                                         metadataElementTypeName,
                                         metadataElementSubtypeNames,
                                         propertyHelper.getSearchPropertiesByName(propertyNames, propertyValue, PropertyComparisonOperator.EQ),
                                         limitResultsByStatus,
                                         asOfTime,
                                         null,
                                         sequencingProperty,
                                         sequencingOrder,
                                         forLineage,
                                         forDuplicateProcessing,
                                         effectiveTime,
                                         startFrom,
                                         pageSize);
    }


    /**
     * Retrieve metadata elements with any of the supplied list of property names similar to the supplied value.
     *
     * @param userId                     caller's userId
     * @param metadataElementTypeName    type of interest (null means any element type)
     * @param metadataElementSubtypeNames optional list of the subtypes of the metadataElementTypeName to
     *                                   include in the search results. Null means all subtypes.
     * @param propertyName               property name to match against
     * @param propertyValue              value to match against
     * @param limitResultsByStatus       By default, entities in all statuses (other than DELETE) are returned.  However, it is possible
     *                                   to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all status values.
     * @param asOfTime                   Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingProperty         String name of the property that is to be used to sequence the results.
     *                                   Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder            Enum defining how the results should be ordered.
     * @param forLineage                 the retrieved element is for lineage processing so include archived elements
     * @param forDuplicateProcessing     the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime              only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     * @param startFrom                  paging start point
     * @param pageSize                   maximum results that can be returned
     *
     * @return a list of elements matching the supplied criteria; null means no matching elements in the metadata store.
     *
     * @throws InvalidParameterException  one of the search parameters are is invalid
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    public List<OpenMetadataElement> findMetadataElementsByPropertyValue(String                userId,
                                                                         String                metadataElementTypeName,
                                                                         List<String>          metadataElementSubtypeNames,
                                                                         String                propertyName,
                                                                         String                propertyValue,
                                                                         List<ElementStatus>   limitResultsByStatus,
                                                                         Date                  asOfTime,
                                                                         String                sequencingProperty,
                                                                         SequencingOrder       sequencingOrder,
                                                                         boolean               forLineage,
                                                                         boolean               forDuplicateProcessing,
                                                                         Date                  effectiveTime,
                                                                         int                   startFrom,
                                                                         int                   pageSize) throws InvalidParameterException,
                                                                                                                UserNotAuthorizedException,
                                                                                                                PropertyServerException
    {
        List<String> propertyNames = List.of(propertyName);

        return this.findMetadataElements(userId,
                                         metadataElementTypeName,
                                         metadataElementSubtypeNames,
                                         propertyHelper.getSearchPropertiesByName(propertyNames, propertyValue, PropertyComparisonOperator.LIKE),
                                         limitResultsByStatus,
                                         asOfTime,
                                         null,
                                         sequencingProperty,
                                         sequencingOrder,
                                         forLineage,
                                         forDuplicateProcessing,
                                         effectiveTime,
                                         startFrom,
                                         pageSize);
    }


    /**
     * Retrieve metadata elements with any of the supplied list of property names similar to the supplied value.
     *
     * @param userId                     caller's userId
     * @param metadataElementTypeName    type of interest (null means any element type)
     * @param metadataElementSubtypeNames optional list of the subtypes of the metadataElementTypeName to
     *                                   include in the search results. Null means all subtypes.
     * @param propertyNames              list of property names to match against
     * @param propertyValue              value to match against
     * @param limitResultsByStatus       By default, entities in all statuses (other than DELETE) are returned.  However, it is possible
     *                                   to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all status values.
     * @param asOfTime                   Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingProperty         String name of the property that is to be used to sequence the results.
     *                                   Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder            Enum defining how the results should be ordered.
     * @param forLineage                 the retrieved element is for lineage processing so include archived elements
     * @param forDuplicateProcessing     the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime              only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     * @param startFrom                  paging start point
     * @param pageSize                   maximum results that can be returned
     *
     * @return a list of elements matching the supplied criteria; null means no matching elements in the metadata store.
     *
     * @throws InvalidParameterException  one of the search parameters are is invalid
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    public List<OpenMetadataElement> getMetadataElementsByPropertyValue(String                userId,
                                                                        String                metadataElementTypeName,
                                                                        List<String>          metadataElementSubtypeNames,
                                                                        List<String>          propertyNames,
                                                                        String                propertyValue,
                                                                        List<ElementStatus>   limitResultsByStatus,
                                                                        Date                  asOfTime,
                                                                        String                sequencingProperty,
                                                                        SequencingOrder       sequencingOrder,
                                                                        boolean               forLineage,
                                                                        boolean               forDuplicateProcessing,
                                                                        Date                  effectiveTime,
                                                                        int                   startFrom,
                                                                        int                   pageSize) throws InvalidParameterException,
                                                                                                               UserNotAuthorizedException,
                                                                                                               PropertyServerException
    {
        return this.findMetadataElements(userId,
                                         metadataElementTypeName,
                                         metadataElementSubtypeNames,
                                         propertyHelper.getSearchPropertiesByName(propertyNames, propertyValue, PropertyComparisonOperator.EQ),
                                         limitResultsByStatus,
                                         asOfTime,
                                         null,
                                         sequencingProperty,
                                         sequencingOrder,
                                         forLineage,
                                         forDuplicateProcessing,
                                         effectiveTime,
                                         startFrom,
                                         pageSize);
    }


    /**
     * Retrieve metadata elements with any of the supplied list of property names set (exactly) to the supplied value.
     *
     * @param userId                     caller's userId
     * @param metadataElementTypeName    type of interest (null means any element type)
     * @param metadataElementSubtypeNames optional list of the subtypes of the metadataElementTypeName to
     *                                   include in the search results. Null means all subtypes.
     * @param propertyNames              list of property names to match against
     * @param propertyValue              value to match against
     * @param limitResultsByStatus       By default, entities in all statuses (other than DELETE) are returned.  However, it is possible
     *                                   to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all status values.
     * @param asOfTime                   Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingProperty         String name of the property that is to be used to sequence the results.
     *                                   Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder            Enum defining how the results should be ordered.
     * @param forLineage                 the retrieved element is for lineage processing so include archived elements
     * @param forDuplicateProcessing     the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime              only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     * @param startFrom                  paging start point
     * @param pageSize                   maximum results that can be returned
     *
     * @return a list of elements matching the supplied criteria; null means no matching elements in the metadata store.
     *
     * @throws InvalidParameterException  one of the search parameters are is invalid
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    public List<OpenMetadataElement> findMetadataElementsByPropertyValue(String                userId,
                                                                         String                metadataElementTypeName,
                                                                         List<String>          metadataElementSubtypeNames,
                                                                         List<String>          propertyNames,
                                                                         String                propertyValue,
                                                                         List<ElementStatus>   limitResultsByStatus,
                                                                         Date                  asOfTime,
                                                                         String                sequencingProperty,
                                                                         SequencingOrder       sequencingOrder,
                                                                         boolean               forLineage,
                                                                         boolean               forDuplicateProcessing,
                                                                         Date                  effectiveTime,
                                                                         int                   startFrom,
                                                                         int                   pageSize) throws InvalidParameterException,
                                                                                                                UserNotAuthorizedException,
                                                                                                                PropertyServerException
    {
        return this.findMetadataElements(userId,
                                         metadataElementTypeName,
                                         metadataElementSubtypeNames,
                                         propertyHelper.getSearchPropertiesByName(propertyNames, propertyValue, PropertyComparisonOperator.LIKE),
                                         limitResultsByStatus,
                                         asOfTime,
                                         null,
                                         sequencingProperty,
                                         sequencingOrder,
                                         forLineage,
                                         forDuplicateProcessing,
                                         effectiveTime,
                                         startFrom,
                                         pageSize);
    }


    /**
     * Return a list of metadata elements that match the supplied criteria.  The results can be returned over many pages.
     *
     * @param userId                     caller's userId
     * @param metadataElementTypeName    type of interest (null means any element type)
     * @param metadataElementSubtypeNames optional list of the subtypes of the metadataElementTypeName to
     *                                   include in the search results. Null means all subtypes.
     * @param searchProperties           Optional list of entity property conditions to match.
     * @param limitResultsByStatus       By default, entities in all statuses (other than DELETE) are returned.  However, it is possible
     *                                   to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all status values.
     * @param asOfTime                   Requests a historical query of the entity.  Null means return the present values.
     * @param matchClassifications       Optional list of classifications to match.
     * @param sequencingProperty         String name of the property that is to be used to sequence the results.
     *                                   Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder            Enum defining how the results should be ordered.
     * @param forLineage                 the retrieved element is for lineage processing so include archived elements
     * @param forDuplicateProcessing     the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime              only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     * @param startFrom                  paging start point
     * @param pageSize                   maximum results that can be returned
     *
     * @return a list of elements matching the supplied criteria; null means no matching elements in the metadata store.
     *
     * @throws InvalidParameterException  one of the search parameters are is invalid
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    @Override
    public List<OpenMetadataElement> findMetadataElements(String                userId,
                                                          String                metadataElementTypeName,
                                                          List<String>          metadataElementSubtypeNames,
                                                          SearchProperties      searchProperties,
                                                          List<ElementStatus>   limitResultsByStatus,
                                                          Date                  asOfTime,
                                                          SearchClassifications matchClassifications,
                                                          String                sequencingProperty,
                                                          SequencingOrder       sequencingOrder,
                                                          boolean               forLineage,
                                                          boolean               forDuplicateProcessing,
                                                          Date                  effectiveTime,
                                                          int                   startFrom,
                                                          int                   pageSize) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException
    {
        final String methodName = "findMetadataElements";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-metadata-store/users/{2}/metadata-elements/by-search-conditions?forLineage={3}&forDuplicateProcessing={4}&startFrom={5}&pageSize={6}";

        invalidParameterHandler.validateUserId(userId, methodName);

        FindRequestBody requestBody = new FindRequestBody();

        requestBody.setMetadataElementTypeName(metadataElementTypeName);
        requestBody.setMetadataElementSubtypeNames(metadataElementSubtypeNames);
        requestBody.setSearchProperties(searchProperties);
        requestBody.setLimitResultsByStatus(limitResultsByStatus);
        requestBody.setMatchClassifications(matchClassifications);
        requestBody.setSequencingProperty(sequencingProperty);
        requestBody.setEffectiveTime(effectiveTime);

        if (sequencingOrder == null)
        {
            requestBody.setSequencingOrder(SequencingOrder.LAST_UPDATE_RECENT);
        }
        else
        {
            requestBody.setSequencingOrder(sequencingOrder);
        }

        requestBody.setAsOfTime(asOfTime);

        OpenMetadataElementsResponse restResult = restClient.callOpenMetadataElementsPostRESTCall(methodName,
                                                                                                  urlTemplate,
                                                                                                  requestBody,
                                                                                                  serverName,
                                                                                                  serviceURLMarker,
                                                                                                  userId,
                                                                                                  forLineage,
                                                                                                  forDuplicateProcessing,
                                                                                                  startFrom,
                                                                                                  pageSize);

        return restResult.getElementList();
    }


    /**
     * Return a list of relationships that match the requested conditions.  The results can be received as a series of pages.
     *
     * @param userId                 caller's userId
     * @param relationshipTypeName   relationship's type.  Null means all types
     *                               (but may be slow so not recommended).
     * @param limitResultsByStatus By default, relationships in all statuses (other than DELETE) are returned.  However, it is possible
     *                             to specify a list of statuses (for example ACTIVE) to restrict the results to.  Null means all status values.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param searchProperties       Optional list of relationship property conditions to match.
     * @param sequencingProperty     String name of the property that is to be used to sequence the results.
     *                               Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder        Enum defining how the results should be ordered.
     * @param forLineage             the retrieved elements are for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime          only return an element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     * @param startFrom              paging start point
     * @param pageSize               maximum results that can be returned
     *
     * @return a list of relationships.  Null means no matching relationships.
     *
     * @throws InvalidParameterException  one of the search parameters are is invalid
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    @Override
    public OpenMetadataRelationshipList findRelationshipsBetweenMetadataElements(String              userId,
                                                                                 String              relationshipTypeName,
                                                                                 SearchProperties    searchProperties,
                                                                                 List<ElementStatus> limitResultsByStatus,
                                                                                 Date                asOfTime,
                                                                                 String              sequencingProperty,
                                                                                 SequencingOrder     sequencingOrder,
                                                                                 boolean             forLineage,
                                                                                 boolean             forDuplicateProcessing,
                                                                                 Date                effectiveTime,
                                                                                 int                 startFrom,
                                                                                 int                 pageSize) throws InvalidParameterException,
                                                                                                                      UserNotAuthorizedException,
                                                                                                                      PropertyServerException
    {
        final String methodName = "findRelationshipsBetweenMetadataElements";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-metadata-store/users/{2}/relationships/by-search-conditions?forLineage={3}&forDuplicateProcessing={4}&startFrom={5}&pageSize={6}";

        invalidParameterHandler.validateUserId(userId, methodName);

        FindRelationshipRequestBody requestBody = new FindRelationshipRequestBody();

        requestBody.setRelationshipTypeName(relationshipTypeName);
        requestBody.setSearchProperties(searchProperties);
        requestBody.setSequencingProperty(sequencingProperty);
        requestBody.setSequencingOrder(sequencingOrder);
        requestBody.setLimitResultsByStatus(limitResultsByStatus);
        requestBody.setAsOfTime(asOfTime);
        requestBody.setEffectiveTime(effectiveTime);

        OpenMetadataRelationshipListResponse restResult = restClient.callOpenMetadataRelationshipListPostRESTCall(methodName,
                                                                                                                  urlTemplate,
                                                                                                                  requestBody,
                                                                                                                  serverName,
                                                                                                                  serviceURLMarker,
                                                                                                                  userId,
                                                                                                                  forLineage,
                                                                                                                  forDuplicateProcessing,
                                                                                                                  startFrom,
                                                                                                                  pageSize);

        return restResult.getRelationshipList();
    }


    /**
     * Retrieve the relationship using its unique identifier.
     *
     * @param relationshipGUID unique identifier for the relationship
     * @param forLineage the retrieved element is for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param effectiveTime only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     *
     * @return relationship properties
     * @throws InvalidParameterException the unique identifier is null or not known.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    @Override
    public OpenMetadataRelationship getRelationshipByGUID(String  userId,
                                                          String  relationshipGUID,
                                                          boolean forLineage,
                                                          boolean forDuplicateProcessing,
                                                          Date    asOfTime,
                                                          Date    effectiveTime) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        final String methodName = "getRelationshipByGUID";
        final String guidParameterName = "relationshipGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-metadata-store/users/{2}/relationships/by-guid/{3}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(relationshipGUID, guidParameterName, methodName);

        AnyTimeRequestBody requestBody = new AnyTimeRequestBody();

        requestBody.setAsOfTime(asOfTime);
        requestBody.setForLineage(forLineage);
        requestBody.setForDuplicateProcessing(forDuplicateProcessing);
        requestBody.setEffectiveTime(effectiveTime);

        OpenMetadataRelationshipResponse restResult = restClient.callOpenMetadataRelationshipPostRESTCall(methodName,
                                                                                                          urlTemplate,
                                                                                                          requestBody,
                                                                                                          serverName,
                                                                                                          serviceURLMarker,
                                                                                                          userId,
                                                                                                          relationshipGUID);

        return restResult.getElement();
    }


    /**
     * Return each of the versions of a relationship.
     *
     * @param userId caller's userId
     * @param relationshipGUID            unique identifier for the relationship
     * @param fromTime the earliest point in time from which to retrieve historical versions of the relationship (inclusive)
     * @param toTime the latest point in time from which to retrieve historical versions of the relationship (exclusive)
     * @param oldestFirst  defining how the results should be ordered.
     * @param forLineage the retrieved element is for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return a list of elements matching the supplied criteria; null means no matching elements in the metadata store.
     * @throws InvalidParameterException one of the search parameters are is invalid
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    @Override
    public  OpenMetadataRelationshipList getRelationshipHistory(String  userId,
                                                                String  relationshipGUID,
                                                                Date    fromTime,
                                                                Date    toTime,
                                                                boolean oldestFirst,
                                                                boolean forLineage,
                                                                boolean forDuplicateProcessing,
                                                                Date    effectiveTime,
                                                                int     startFrom,
                                                                int     pageSize) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        final String methodName = "getRelationshipHistory";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-metadata-store/users/{2}/relationships/{3}/history?forLineage={4}&forDuplicateProcessing={5}&oldestFirst={6}&startFrom={7}&pageSize={8}";

        invalidParameterHandler.validateUserId(userId, methodName);

        HistoryRequestBody requestBody = new HistoryRequestBody();

        requestBody.setFromTime(fromTime);
        requestBody.setToTime(toTime);
        requestBody.setEffectiveTime(effectiveTime);

        OpenMetadataRelationshipListResponse restResult = restClient.callOpenMetadataRelationshipListPostRESTCall(methodName,
                                                                                                                  urlTemplate,
                                                                                                                  requestBody,
                                                                                                                  serverName,
                                                                                                                  serviceURLMarker,
                                                                                                                  userId,
                                                                                                                  relationshipGUID,
                                                                                                                  forLineage,
                                                                                                                  forDuplicateProcessing,
                                                                                                                  oldestFirst,
                                                                                                                  startFrom,
                                                                                                                  pageSize);

        return restResult.getRelationshipList();
    }


    /**
     * Create a new metadata element in the metadata store.  The type name comes from the open metadata types.
     * The selected type also controls the names and types of the properties that are allowed.
     * This version of the method allows access to advanced features such as multiple states and
     * effectivity dates.
     *
     * @param userId                  caller's userId
     * @param metadataElementTypeName type name of the new metadata element
     * @param initialStatus           initial status of the metadata element
     * @param effectiveFrom           the date when this element is active - null for active on creation
     * @param effectiveTo             the date when this element becomes inactive - null for active until deleted
     * @param properties              properties of the new metadata element
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  the type name, status or one of the properties is invalid
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException    there is a problem with the metadata store
     */
    @Override
    public String createMetadataElementInStore(String            userId,
                                               String            metadataElementTypeName,
                                               ElementStatus     initialStatus,
                                               Date              effectiveFrom,
                                               Date              effectiveTo,
                                               ElementProperties properties) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        return this.createMetadataElementInStore(userId,
                                                 null,
                                                 null,
                                                 metadataElementTypeName,
                                                 initialStatus,
                                                 null,
                                                 null,
                                                 false,
                                                 effectiveFrom,
                                                 effectiveTo,
                                                 properties,
                                                 null,
                                                 null,
                                                 null,
                                                 true,
                                                 false,
                                                 false,
                                                 null);
    }


    /**
     * Create a new metadata element in the metadata store.  The type name comes from the open metadata types.
     * The selected type also controls the names and types of the properties that are allowed.
     * This version of the method allows access to advanced features such as multiple states and
     * effectivity dates.
     *
     * @param userId                  caller's userId
     * @param externalSourceGUID      unique identifier of the software capability that owns this collection
     * @param externalSourceName      unique name of the software capability that owns this collection
     * @param metadataElementTypeName type name of the new metadata element
     * @param initialStatus           initial status of the metadata element
     * @param effectiveFrom           the date when this element is active - null for active on creation
     * @param effectiveTo             the date when this element becomes inactive - null for active until deleted
     * @param properties              properties of the new metadata element
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  the type name, status or one of the properties is invalid
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException    there is a problem with the metadata store
     */
    @Override
    public String createMetadataElementInStore(String            userId,
                                               String            externalSourceGUID,
                                               String            externalSourceName,
                                               String            metadataElementTypeName,
                                               ElementStatus     initialStatus,
                                               Date              effectiveFrom,
                                               Date              effectiveTo,
                                               ElementProperties properties) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        return this.createMetadataElementInStore(userId,
                                                 externalSourceGUID,
                                                 externalSourceName,
                                                 metadataElementTypeName,
                                                 initialStatus,
                                                 null,
                                                 null,
                                                 false,
                                                 effectiveFrom,
                                                 effectiveTo,
                                                 properties,
                                                 null,
                                                 null,
                                                 null,
                                                 true,
                                                 false,
                                                 false,
                                                 null);
    }


    /**
     * Create a new metadata element in the metadata store.  The type name comes from the open metadata types.
     * The selected type also controls the names and types of the properties that are allowed.
     * This version of the method allows access to advanced features such as multiple states and
     * effectivity dates.
     *
     * @param userId caller's userId
     * @param metadataElementTypeName type name of the new metadata element
     * @param initialStatus initial status of the metadata element
     * @param initialClassifications map of classification names to classification properties to include in the entity creation request
     * @param anchorGUID unique identifier of the element that should be the anchor for the new element. Set to null if no anchor,
     *                   or the Anchors classification is included in the initial classifications.
     * @param isOwnAnchor boolean flag to day that the element should be classified as its own anchor once its element
     *                    is created in the repository.
     * @param effectiveFrom the date when this element is active - null for active on creation
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param properties properties of the new metadata element
     * @param parentGUID unique identifier of optional parent entity
     * @param parentRelationshipTypeName type of relationship to connect the new element to the parent
     * @param parentRelationshipProperties properties to include in parent relationship
     * @param parentAtEnd1 which end should the parent GUID go in the relationship
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException the type name, status or one of the properties is invalid
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public String createMetadataElementInStore(String                         userId,
                                               String                         metadataElementTypeName,
                                               ElementStatus                  initialStatus,
                                               Map<String, ElementProperties> initialClassifications,
                                               String                         anchorGUID,
                                               boolean                        isOwnAnchor,
                                               Date                           effectiveFrom,
                                               Date                           effectiveTo,
                                               ElementProperties              properties,
                                               String                         parentGUID,
                                               String                         parentRelationshipTypeName,
                                               ElementProperties              parentRelationshipProperties,
                                               boolean                        parentAtEnd1,
                                               boolean                        forLineage,
                                               boolean                        forDuplicateProcessing,
                                               Date                           effectiveTime) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException
    {
        return this.createMetadataElementInStore(userId,
                                                 null,
                                                 null,
                                                 metadataElementTypeName,
                                                 initialStatus,
                                                 initialClassifications,
                                                 anchorGUID,
                                                 isOwnAnchor,
                                                 effectiveFrom,
                                                 effectiveTo,
                                                 properties,
                                                 parentGUID,
                                                 parentRelationshipTypeName,
                                                 parentRelationshipProperties,
                                                 parentAtEnd1,
                                                 forLineage,
                                                 forDuplicateProcessing,
                                                 effectiveTime);
    }


    /**
     * Create a new metadata element in the metadata store.  The type name comes from the open metadata types.
     * The selected type also controls the names and types of the properties that are allowed.
     * This version of the method allows access to advanced features such as multiple states and
     * effectivity dates.
     *
     * @param userId caller's userId
     * @param externalSourceGUID      unique identifier of the software capability that owns this collection
     * @param externalSourceName      unique name of the software capability that owns this collection
     * @param metadataElementTypeName type name of the new metadata element
     * @param initialStatus initial status of the metadata element
     * @param initialClassifications map of classification names to classification properties to include in the entity creation request
     * @param anchorGUID unique identifier of the element that should be the anchor for the new element. Set to null if no anchor,
     *                   or the Anchors classification is included in the initial classifications.
     * @param isOwnAnchor boolean flag to day that the element should be classified as its own anchor once its element
     *                    is created in the repository.
     * @param effectiveFrom the date when this element is active - null for active on creation
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param properties properties of the new metadata element
     * @param parentGUID unique identifier of optional parent entity
     * @param parentRelationshipTypeName type of relationship to connect the new element to the parent
     * @param parentRelationshipProperties properties to include in parent relationship
     * @param parentAtEnd1 which end should the parent GUID go in the relationship
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException the type name, status or one of the properties is invalid
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public String createMetadataElementInStore(String                         userId,
                                               String                         externalSourceGUID,
                                               String                         externalSourceName,
                                               String                         metadataElementTypeName,
                                               ElementStatus                  initialStatus,
                                               Map<String, ElementProperties> initialClassifications,
                                               String                         anchorGUID,
                                               boolean                        isOwnAnchor,
                                               Date                           effectiveFrom,
                                               Date                           effectiveTo,
                                               ElementProperties              properties,
                                               String                         parentGUID,
                                               String                         parentRelationshipTypeName,
                                               ElementProperties              parentRelationshipProperties,
                                               boolean                        parentAtEnd1,
                                               boolean                        forLineage,
                                               boolean                        forDuplicateProcessing,
                                               Date                           effectiveTime) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException
    {
        final String methodName               = "createMetadataElementInStore";
        final String elementTypeParameterName = "metadataElementTypeName";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-metadata-store/users/{2}/metadata-elements";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(metadataElementTypeName, elementTypeParameterName, methodName);

        if (parentGUID != null)
        {
            final String parentRelationshipTypeNameParameterName = "parentRelationshipTypeName";

            invalidParameterHandler.validateName(parentRelationshipTypeName, parentRelationshipTypeNameParameterName, methodName);
        }

        NewOpenMetadataElementRequestBody requestBody = new NewOpenMetadataElementRequestBody();

        requestBody.setExternalSourceGUID(externalSourceGUID);
        requestBody.setExternalSourceName(externalSourceName);
        requestBody.setTypeName(metadataElementTypeName);
        requestBody.setInitialStatus(initialStatus);
        requestBody.setInitialClassifications(initialClassifications);
        requestBody.setAnchorGUID(anchorGUID);
        requestBody.setIsOwnAnchor(isOwnAnchor);
        requestBody.setEffectiveFrom(effectiveFrom);
        requestBody.setEffectiveTo(effectiveTo);
        requestBody.setProperties(properties);
        requestBody.setParentGUID(parentGUID);
        requestBody.setParentRelationshipTypeName(parentRelationshipTypeName);
        requestBody.setParentRelationshipProperties(parentRelationshipProperties);
        requestBody.setParentAtEnd1(parentAtEnd1);
        requestBody.setForLineage(forLineage);
        requestBody.setForDuplicateProcessing(forDuplicateProcessing);
        requestBody.setEffectiveTime(effectiveTime);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  serviceURLMarker,
                                                                  userId);

        return restResult.getGUID();
    }


    /**
     * Create a new metadata element in the metadata store.  The type name comes from the open metadata types.
     * The selected type also controls the names and types of the properties that are allowed.
     * This version of the method allows access to advanced features such as multiple states and
     * effectivity dates.
     *
     * @param userId caller's userId
     * @param metadataElementTypeName type name of the new metadata element
     * @param anchorGUID unique identifier of the element that should be the anchor for the new element. Set to null if no anchor,
     *                   or the Anchors classification is included in the initial classifications.
     * @param isOwnAnchor boolean flag to day that the element should be classified as its own anchor once its element
     *                    is created in the repository.
     * @param effectiveFrom the date when this element is active - null for active on creation
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param templateGUID the unique identifier of the existing asset to copy (this will copy all the attachments such as nested content, schema
     *                     connection etc)
     * @param replacementProperties properties of the new metadata element.  These override the template values
     * @param placeholderProperties property name-to-property value map to replace any placeholder values in the
     *                              template element - and their anchored elements, which are also copied as part of this operation.
     * @param parentGUID unique identifier of optional parent entity
     * @param parentRelationshipTypeName type of relationship to connect the new element to the parent
     * @param parentRelationshipProperties properties to include in parent relationship
     * @param parentAtEnd1 which end should the parent GUID go in the relationship
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException the type name, status or one of the properties is invalid
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public String createMetadataElementFromTemplate(String              userId,
                                                    String              metadataElementTypeName,
                                                    String              anchorGUID,
                                                    boolean             isOwnAnchor,
                                                    Date                effectiveFrom,
                                                    Date                effectiveTo,
                                                    String              templateGUID,
                                                    ElementProperties   replacementProperties,
                                                    Map<String, String> placeholderProperties,
                                                    String              parentGUID,
                                                    String              parentRelationshipTypeName,
                                                    ElementProperties   parentRelationshipProperties,
                                                    boolean             parentAtEnd1,
                                                    boolean             forLineage,
                                                    boolean             forDuplicateProcessing,
                                                    Date                effectiveTime) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException
    {
        return this.createMetadataElementFromTemplate(userId,
                                                      null,
                                                      null,
                                                      metadataElementTypeName,
                                                      anchorGUID,
                                                      isOwnAnchor,
                                                      effectiveFrom,
                                                      effectiveTo,
                                                      templateGUID,
                                                      replacementProperties,
                                                      placeholderProperties,
                                                      parentGUID,
                                                      parentRelationshipTypeName,
                                                      parentRelationshipProperties,
                                                      parentAtEnd1,
                                                      forLineage,
                                                      forDuplicateProcessing,
                                                      effectiveTime);
    }


    /**
     * Create a new metadata element in the metadata store using the template identified by the templateGUID.
     * The type name comes from the open metadata types.
     * The selected type also controls the names and types of the properties that are allowed.
     * The template and any similar anchored objects are
     * copied in this process.
     *
     * @param userId caller's userId
     * @param externalSourceGUID      unique identifier of the software capability that owns this collection
     * @param externalSourceName      unique name of the software capability that owns this collection
     * @param metadataElementTypeName type name of the new metadata element
     * @param anchorGUID unique identifier of the element that should be the anchor for the new element. Set to null if no anchor,
     *                   or the Anchors classification is included in the initial classifications.
     * @param isOwnAnchor boolean flag to day that the element should be classified as its own anchor once its element
     *                    is created in the repository.
     * @param effectiveFrom the date when this element is active - null for active on creation
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param templateGUID the unique identifier of the existing asset to copy (this will copy all the attachments such as nested content, schema
     *                     connection etc)
     * @param replacementProperties properties of the new metadata element.  These override the template values
     * @param placeholderProperties property name-to-property value map to replace any placeholder values in the
     *                              template element - and their anchored elements, which are also copied as part of this operation.
     * @param parentGUID unique identifier of optional parent entity
     * @param parentRelationshipTypeName type of relationship to connect the new element to the parent
     * @param parentRelationshipProperties properties to include in parent relationship
     * @param parentAtEnd1 which end should the parent GUID go in the relationship
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException the type name, status or one of the properties is invalid
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public String createMetadataElementFromTemplate(String              userId,
                                                    String              externalSourceGUID,
                                                    String              externalSourceName,
                                                    String              metadataElementTypeName,
                                                    String              anchorGUID,
                                                    boolean             isOwnAnchor,
                                                    Date                effectiveFrom,
                                                    Date                effectiveTo,
                                                    String              templateGUID,
                                                    ElementProperties   replacementProperties,
                                                    Map<String, String> placeholderProperties,
                                                    String              parentGUID,
                                                    String              parentRelationshipTypeName,
                                                    ElementProperties   parentRelationshipProperties,
                                                    boolean             parentAtEnd1,
                                                    boolean             forLineage,
                                                    boolean             forDuplicateProcessing,
                                                    Date                effectiveTime) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException
    {
        final String methodName                = "createMetadataElementFromTemplate";
        final String templateGUIDParameterName = "templateGUID";

        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-metadata-store/users/{2}/metadata-elements/from-template";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(templateGUID, templateGUIDParameterName, methodName);

        if (parentGUID != null)
        {
            final String parentRelationshipTypeNameParameterName = "parentRelationshipTypeName";

            invalidParameterHandler.validateName(parentRelationshipTypeName, parentRelationshipTypeNameParameterName, methodName);
        }

        TemplateRequestBody requestBody = new TemplateRequestBody();

        requestBody.setExternalSourceGUID(externalSourceGUID);
        requestBody.setExternalSourceName(externalSourceName);
        requestBody.setTypeName(metadataElementTypeName);
        requestBody.setAnchorGUID(anchorGUID);
        requestBody.setIsOwnAnchor(isOwnAnchor);
        requestBody.setEffectiveFrom(effectiveFrom);
        requestBody.setEffectiveTo(effectiveTo);
        requestBody.setTemplateGUID(templateGUID);
        requestBody.setReplacementProperties(replacementProperties);
        requestBody.setPlaceholderPropertyValues(placeholderProperties);
        requestBody.setParentGUID(parentGUID);
        requestBody.setParentRelationshipTypeName(parentRelationshipTypeName);
        requestBody.setParentRelationshipProperties(parentRelationshipProperties);
        requestBody.setParentAtEnd1(parentAtEnd1);
        requestBody.setForLineage(forLineage);
        requestBody.setForDuplicateProcessing(forDuplicateProcessing);
        requestBody.setEffectiveTime(effectiveTime);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  serviceURLMarker,
                                                                  userId);

        return restResult.getGUID();
    }



    /**
     * Create a new metadata element in the metadata store using the template identified by the templateGUID.
     * The type name comes from the open metadata types.
     * The selected type also controls the names and types of the properties that are allowed.
     * The template and any similar anchored objects are
     * copied in this process.
     *
     * @param userId caller's userId
     * @param externalSourceGUID      unique identifier of the software capability that owns this collection
     * @param externalSourceName      unique name of the software capability that owns this collection
     * @param metadataElementTypeName type name of the new metadata element
     * @param anchorGUID unique identifier of the element that should be the anchor for the new element. Set to null if no anchor,
     *                   or the Anchors classification is included in the initial classifications.
     * @param isOwnAnchor boolean flag to day that the element should be classified as its own anchor once its element
     *                    is created in the repository.
     * @param effectiveFrom the date when this element is active - null for active on creation
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param templateGUID the unique identifier of the existing asset to copy (this will copy all the attachments such as nested content, schema
     *                     connection etc)
     * @param replacementProperties properties of the new metadata element.  These override the template values
     * @param placeholderProperties property name-to-property value map to replace any placeholder values in the
     *                              template element - and their anchored elements, which are also copied as part of this operation.
     * @param parentGUID unique identifier of optional parent entity
     * @param parentRelationshipTypeName type of relationship to connect the new element to the parent
     * @param parentRelationshipProperties properties to include in parent relationship
     * @param parentAtEnd1 which end should the parent GUID go in the relationship
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException the type name, status or one of the properties is invalid
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public String getMetadataElementFromTemplate(String              userId,
                                                 String              externalSourceGUID,
                                                 String              externalSourceName,
                                                 String              metadataElementTypeName,
                                                 String              anchorGUID,
                                                 boolean             isOwnAnchor,
                                                 Date                effectiveFrom,
                                                 Date                effectiveTo,
                                                 String              templateGUID,
                                                 ElementProperties   replacementProperties,
                                                 Map<String, String> placeholderProperties,
                                                 String              parentGUID,
                                                 String              parentRelationshipTypeName,
                                                 ElementProperties   parentRelationshipProperties,
                                                 boolean             parentAtEnd1,
                                                 boolean             forLineage,
                                                 boolean             forDuplicateProcessing,
                                                 Date                effectiveTime) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        final String methodName                = "getMetadataElementFromTemplate";
        final String templateGUIDParameterName = "templateGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-metadata-store/users/{2}/metadata-elements/from-template?allowRetrieve={3}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(templateGUID, templateGUIDParameterName, methodName);

        if (parentGUID != null)
        {
            final String parentRelationshipTypeNameParameterName = "parentRelationshipTypeName";

            invalidParameterHandler.validateName(parentRelationshipTypeName, parentRelationshipTypeNameParameterName, methodName);
        }

        TemplateRequestBody requestBody = new TemplateRequestBody();

        requestBody.setExternalSourceGUID(externalSourceGUID);
        requestBody.setExternalSourceName(externalSourceName);
        requestBody.setTypeName(metadataElementTypeName);
        requestBody.setAnchorGUID(anchorGUID);
        requestBody.setIsOwnAnchor(isOwnAnchor);
        requestBody.setEffectiveFrom(effectiveFrom);
        requestBody.setEffectiveTo(effectiveTo);
        requestBody.setTemplateGUID(templateGUID);
        requestBody.setReplacementProperties(replacementProperties);
        requestBody.setPlaceholderPropertyValues(placeholderProperties);
        requestBody.setParentGUID(parentGUID);
        requestBody.setParentRelationshipTypeName(parentRelationshipTypeName);
        requestBody.setParentRelationshipProperties(parentRelationshipProperties);
        requestBody.setParentAtEnd1(parentAtEnd1);
        requestBody.setForLineage(forLineage);
        requestBody.setForDuplicateProcessing(forDuplicateProcessing);
        requestBody.setEffectiveTime(effectiveTime);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  serviceURLMarker,
                                                                  userId,
                                                                  true);

        return restResult.getGUID();
    }


    /**
     * Update the properties of a specific metadata element.  The properties must match the type definition associated with the
     * metadata element when it was created.  However, it is possible to update a few properties, or replace all them by
     * the value used in the replaceProperties flag.
     *
     * @param userId                 caller's userId
     * @param metadataElementGUID    unique identifier of the metadata element to update
     * @param replaceAllProperties      flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                               the individual properties specified on the request.
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param properties             new properties for the metadata element
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException  either the unique identifier or the properties are invalid in some way
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException    there is a problem with the metadata store
     */
    @Override
    public void updateMetadataElementInStore(String            userId,
                                             String            metadataElementGUID,
                                             boolean           replaceAllProperties,
                                             boolean           forLineage,
                                             boolean           forDuplicateProcessing,
                                             ElementProperties properties,
                                             Date              effectiveTime) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        this.updateMetadataElementInStore(userId,
                                          null,
                                          null,
                                          metadataElementGUID,
                                          replaceAllProperties,
                                          forLineage,
                                          forDuplicateProcessing,
                                          properties,
                                          effectiveTime);
    }


    /**
     * Update the properties of a specific metadata element.  The properties must match the type definition associated with the
     * metadata element when it was created.  However, it is possible to update a few properties, or replace all them by
     * the value used in the replaceProperties flag.
     *
     * @param userId                 caller's userId
     * @param externalSourceGUID      unique identifier of the software capability that owns this collection
     * @param externalSourceName      unique name of the software capability that owns this collection
     * @param metadataElementGUID    unique identifier of the metadata element to update
     * @param replaceProperties      flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                               the individual properties specified on the request.
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param properties             new properties for the metadata element
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException  either the unique identifier or the properties are invalid in some way
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException    there is a problem with the metadata store
     */
    public void updateMetadataElementInStore(String            userId,
                                             String            externalSourceGUID,
                                             String            externalSourceName,
                                             String            metadataElementGUID,
                                             boolean           replaceProperties,
                                             boolean           forLineage,
                                             boolean           forDuplicateProcessing,
                                             ElementProperties properties,
                                             Date              effectiveTime) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        final String methodName        = "updateMetadataElementInStore";
        final String guidParameterName = "metadataElementGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-metadata-store/users/{2}/metadata-elements/{3}/update-properties";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(metadataElementGUID, guidParameterName, methodName);

        UpdatePropertiesRequestBody requestBody = new UpdatePropertiesRequestBody();

        requestBody.setExternalSourceGUID(externalSourceGUID);
        requestBody.setExternalSourceName(externalSourceName);
        requestBody.setReplaceProperties(replaceProperties);
        requestBody.setForLineage(forLineage);
        requestBody.setForDuplicateProcessing(forDuplicateProcessing);
        requestBody.setProperties(properties);
        requestBody.setEffectiveTime(effectiveTime);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        serviceURLMarker,
                                        userId,
                                        metadataElementGUID);
    }


    /**
     * Update the status of specific metadata element. The new status must match a status value that is defined for the element's type
     * assigned when it was created.  The effectivity dates control the visibility of the element
     * through specific APIs.
     *
     * @param userId                 caller's userId
     * @param metadataElementGUID    unique identifier of the metadata element to update
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param newElementStatus       new status value - or null to leave as is
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException  either the unique identifier or the status are invalid in some way
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException    there is a problem with the metadata store
     */
    @Override
    public void updateMetadataElementStatusInStore(String        userId,
                                                   String        metadataElementGUID,
                                                   boolean       forLineage,
                                                   boolean       forDuplicateProcessing,
                                                   ElementStatus newElementStatus,
                                                   Date          effectiveTime) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        this.updateMetadataElementStatusInStore(userId,
                                                null,
                                                null,
                                                metadataElementGUID,
                                                forLineage,
                                                forDuplicateProcessing,
                                                newElementStatus,
                                                effectiveTime);
    }


    /**
     * Update the status of specific metadata element. The new status must match a status value that is defined for the element's type
     * assigned when it was created.  The effectivity dates control the visibility of the element
     * through specific APIs.
     *
     * @param userId                 caller's userId
     * @param externalSourceGUID      unique identifier of the software capability that owns this collection
     * @param externalSourceName      unique name of the software capability that owns this collection
     * @param metadataElementGUID    unique identifier of the metadata element to update
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param newElementStatus       new status value - or null to leave as is
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException  either the unique identifier or the status are invalid in some way
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException    there is a problem with the metadata store
     */
    public void updateMetadataElementStatusInStore(String        userId,
                                                   String        externalSourceGUID,
                                                   String        externalSourceName,
                                                   String        metadataElementGUID,
                                                   boolean       forLineage,
                                                   boolean       forDuplicateProcessing,
                                                   ElementStatus newElementStatus,
                                                   Date          effectiveTime) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        final String methodName        = "updateMetadataElementStatusInStore";
        final String guidParameterName = "metadataElementGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-metadata-store/users/{2}/metadata-elements/{3}/update-status";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(metadataElementGUID, guidParameterName, methodName);

        UpdateStatusRequestBody requestBody = new UpdateStatusRequestBody();

        requestBody.setExternalSourceGUID(externalSourceGUID);
        requestBody.setExternalSourceName(externalSourceName);
        requestBody.setForLineage(forLineage);
        requestBody.setForDuplicateProcessing(forDuplicateProcessing);
        requestBody.setNewStatus(newElementStatus);
        requestBody.setEffectiveTime(effectiveTime);

        restClient.callGUIDPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        serviceURLMarker,
                                        userId,
                                        metadataElementGUID);
    }


    /**
     * Update the status of specific metadata element. The new status must match a status value that is defined for the element's type
     * assigned when it was created.  The effectivity dates control the visibility of the element
     * through specific APIs.
     *
     * @param userId                 caller's userId
     * @param metadataElementGUID    unique identifier of the metadata element to update
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveFrom          the date when this element is active - null for active now
     * @param effectiveTo            the date when this element becomes inactive - null for active until deleted
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException  either the unique identifier or the status are invalid in some way
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException    there is a problem with the metadata store
     */
    @Override
    public void updateMetadataElementEffectivityInStore(String        userId,
                                                        String        metadataElementGUID,
                                                        boolean       forLineage,
                                                        boolean       forDuplicateProcessing,
                                                        Date          effectiveFrom,
                                                        Date          effectiveTo,
                                                        Date          effectiveTime) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        this.updateMetadataElementEffectivityInStore(userId,
                                                     null,
                                                     null,
                                                     metadataElementGUID,
                                                     forLineage,
                                                     forDuplicateProcessing,
                                                     effectiveFrom,
                                                     effectiveTo,
                                                     effectiveTime);
    }


    /**
     * Update the status of specific metadata element. The new status must match a status value that is defined for the element's type
     * assigned when it was created.  The effectivity dates control the visibility of the element
     * through specific APIs.
     *
     * @param userId                 caller's userId
     * @param externalSourceGUID      unique identifier of the software capability that owns this collection
     * @param externalSourceName      unique name of the software capability that owns this collection
     * @param metadataElementGUID    unique identifier of the metadata element to update
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveFrom          the date when this element is active - null for active now
     * @param effectiveTo            the date when this element becomes inactive - null for active until deleted
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException  either the unique identifier or the status are invalid in some way
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException    there is a problem with the metadata store
     */
    public void updateMetadataElementEffectivityInStore(String        userId,
                                                        String        externalSourceGUID,
                                                        String        externalSourceName,
                                                        String        metadataElementGUID,
                                                        boolean       forLineage,
                                                        boolean       forDuplicateProcessing,
                                                        Date          effectiveFrom,
                                                        Date          effectiveTo,
                                                        Date          effectiveTime) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        final String methodName        = "updateMetadataElementEffectivityInStore";
        final String guidParameterName = "metadataElementGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-metadata-store/users/{2}/metadata-elements/{3}/update-effectivity";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(metadataElementGUID, guidParameterName, methodName);

        UpdateEffectivityDatesRequestBody requestBody = new UpdateEffectivityDatesRequestBody();

        requestBody.setExternalSourceGUID(externalSourceGUID);
        requestBody.setExternalSourceName(externalSourceName);
        requestBody.setForLineage(forLineage);
        requestBody.setForDuplicateProcessing(forDuplicateProcessing);
        requestBody.setEffectiveFrom(effectiveFrom);
        requestBody.setEffectiveTo(effectiveTo);
        requestBody.setEffectiveTime(effectiveTime);

        restClient.callGUIDPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        serviceURLMarker,
                                        userId,
                                        metadataElementGUID);
    }


    /**
     * Delete a specific metadata element.
     *
     * @param userId                 caller's userId
     * @param metadataElementGUID    unique identifier of the metadata element to update
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException  the unique identifier is null or invalid in some way
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException    there is a problem with the metadata store
     */
    @Override
    public void deleteMetadataElementInStore(String  userId,
                                             String  metadataElementGUID,
                                             boolean forLineage,
                                             boolean forDuplicateProcessing,
                                             Date    effectiveTime) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        this.deleteMetadataElementInStore(userId,
                                          null,
                                          null,
                                          metadataElementGUID,
                                          forLineage,
                                          forDuplicateProcessing,
                                          effectiveTime);
    }


    /**
     * Delete a specific metadata element.
     *
     * @param userId                 caller's userId
     * @param externalSourceGUID     unique identifier of the software capability that owns this collection
     * @param externalSourceName     unique name of the software capability that owns this collection
     * @param metadataElementGUID    unique identifier of the metadata element to update
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException  the unique identifier is null or invalid in some way
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException    there is a problem with the metadata store
     */
    public void deleteMetadataElementInStore(String  userId,
                                             String  externalSourceGUID,
                                             String  externalSourceName,
                                             String  metadataElementGUID,
                                             boolean forLineage,
                                             boolean forDuplicateProcessing,
                                             Date    effectiveTime) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        final String methodName        = "deleteMetadataElementInStore";
        final String guidParameterName = "metadataElementGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-metadata-store/users/{2}/metadata-elements/{3}/delete";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(metadataElementGUID, guidParameterName, methodName);

        MetadataSourceRequestBody requestBody = new MetadataSourceRequestBody();

        requestBody.setExternalSourceGUID(externalSourceGUID);
        requestBody.setExternalSourceName(externalSourceName);
        requestBody.setForLineage(forLineage);
        requestBody.setForDuplicateProcessing(forDuplicateProcessing);
        requestBody.setEffectiveTime(effectiveTime);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        serviceURLMarker,
                                        userId,
                                        metadataElementGUID);
    }


    /**
     * Archive a specific metadata element.
     *
     * @param userId caller's userId
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param archiveProperties description of the archiving process
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException the unique identifier is null or invalid in some way
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public void archiveMetadataElementInStore(String            userId,
                                              String            metadataElementGUID,
                                              ArchiveProperties archiveProperties,
                                              boolean           forLineage,
                                              boolean           forDuplicateProcessing,
                                              Date              effectiveTime) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        this.archiveMetadataElementInStore(userId,
                                           null,
                                           null,
                                           metadataElementGUID,
                                           archiveProperties,
                                           forLineage,
                                           forDuplicateProcessing,
                                           effectiveTime);
    }


    /**
     * Archive a specific metadata element.
     *
     * @param userId caller's userId
     * @param externalSourceGUID      unique identifier of the software capability that owns this collection
     * @param externalSourceName      unique name of the software capability that owns this collection
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param archiveProperties description of the archiving process
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException the unique identifier is null or invalid in some way
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public void archiveMetadataElementInStore(String            userId,
                                              String            externalSourceGUID,
                                              String            externalSourceName,
                                              String            metadataElementGUID,
                                              ArchiveProperties archiveProperties,
                                              boolean           forLineage,
                                              boolean           forDuplicateProcessing,
                                              Date              effectiveTime) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        final String methodName        = "archiveMetadataElementInStore";
        final String guidParameterName = "metadataElementGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-metadata-store/users/{2}/metadata-elements/{3}/archive";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(metadataElementGUID, guidParameterName, methodName);

        ArchiveRequestBody requestBody = new ArchiveRequestBody();

        requestBody.setExternalSourceGUID(externalSourceGUID);
        requestBody.setExternalSourceName(externalSourceName);
        requestBody.setForLineage(forLineage);
        requestBody.setForDuplicateProcessing(forDuplicateProcessing);
        requestBody.setEffectiveTime(effectiveTime);
        requestBody.setArchiveProperties(archiveProperties);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        serviceURLMarker,
                                        userId,
                                        metadataElementGUID);
    }


    /**
     * Add a new classification to the metadata element.  Note that only one classification with the same name can be attached to
     * a metadata element.
     *
     * @param userId                 caller's userId
     * @param metadataElementGUID    unique identifier of the metadata element to update
     * @param classificationName     name of the classification to add (if the classification is already present then use reclassify)
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveFrom          the date when this classification is active - null for active now
     * @param effectiveTo            the date when this classification becomes inactive - null for active until deleted
     * @param properties             properties to store in the new classification.  These must conform to the valid properties associated with the
     *                               classification name
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException  the unique identifier or classification name is null or invalid in some way; properties do not match the
     *                                    valid properties associated with the classification's type definition
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException    there is a problem with the metadata store
     */
    @Override
    public void classifyMetadataElementInStore(String            userId,
                                               String            metadataElementGUID,
                                               String            classificationName,
                                               boolean           forLineage,
                                               boolean           forDuplicateProcessing,
                                               Date              effectiveFrom,
                                               Date              effectiveTo,
                                               ElementProperties properties,
                                               Date              effectiveTime) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        this.classifyMetadataElementInStore(userId,
                                            null,
                                            null,
                                            metadataElementGUID,
                                            classificationName,
                                            forLineage,
                                            forDuplicateProcessing,
                                            effectiveFrom,
                                            effectiveTo,
                                            properties,
                                            effectiveTime);
    }


    /**
     * Add a new classification to the metadata element.  Note that only one classification with the same name can be attached to
     * a metadata element.
     *
     * @param userId                 caller's userId
     * @param externalSourceGUID     unique identifier of the software capability that owns this collection
     * @param externalSourceName     unique name of the software capability that owns this collection
     * @param metadataElementGUID    unique identifier of the metadata element to update
     * @param classificationName     name of the classification to add (if the classification is already present then use reclassify)
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveFrom          the date when this classification is active - null for active now
     * @param effectiveTo            the date when this classification becomes inactive - null for active until deleted
     * @param properties             properties to store in the new classification.  These must conform to the valid properties associated with the
     *                               classification name
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException  the unique identifier or classification name is null or invalid in some way; properties do not match the
     *                                    valid properties associated with the classification's type definition
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException    there is a problem with the metadata store
     */
    public void classifyMetadataElementInStore(String            userId,
                                               String            externalSourceGUID,
                                               String            externalSourceName,
                                               String            metadataElementGUID,
                                               String            classificationName,
                                               boolean           forLineage,
                                               boolean           forDuplicateProcessing,
                                               Date              effectiveFrom,
                                               Date              effectiveTo,
                                               ElementProperties properties,
                                               Date              effectiveTime) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        final String methodName                  = "classifyMetadataElementInStore";
        final String guidParameterName           = "metadataElementGUID";
        final String classificationParameterName = "classificationName";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-metadata-store/users/{2}/metadata-elements/{3}/classifications/{4}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(metadataElementGUID, guidParameterName, methodName);
        invalidParameterHandler.validateName(classificationName, classificationParameterName, methodName);

        NewClassificationRequestBody requestBody = new NewClassificationRequestBody();

        requestBody.setExternalSourceGUID(externalSourceGUID);
        requestBody.setExternalSourceName(externalSourceName);
        requestBody.setEffectiveFrom(effectiveFrom);
        requestBody.setEffectiveTo(effectiveTo);
        requestBody.setProperties(properties);
        requestBody.setForLineage(forLineage);
        requestBody.setForDuplicateProcessing(forDuplicateProcessing);
        requestBody.setEffectiveTime(effectiveTime);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        serviceURLMarker,
                                        userId,
                                        metadataElementGUID,
                                        classificationName);
    }


    /**
     * Update the properties of a classification that is currently attached to a specific metadata element.
     *
     * @param userId                 caller's userId
     * @param metadataElementGUID    unique identifier of the metadata element to update
     * @param classificationName     unique name of the classification to update
     * @param replaceProperties      flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                               the individual properties specified on the request.
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param properties             new properties for the classification
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException  the unique identifier or classification name is null or invalid in some way; properties do not match the
     *                                    valid properties associated with the classification's type definition
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException    there is a problem with the metadata store
     */
    @Override
    public void reclassifyMetadataElementInStore(String            userId,
                                                 String            metadataElementGUID,
                                                 String            classificationName,
                                                 boolean           replaceProperties,
                                                 boolean           forLineage,
                                                 boolean           forDuplicateProcessing,
                                                 ElementProperties properties,
                                                 Date              effectiveTime) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        this.reclassifyMetadataElementInStore(userId,
                                              null,
                                              null,
                                              metadataElementGUID,
                                              classificationName,
                                              replaceProperties,
                                              forLineage,
                                              forDuplicateProcessing,
                                              properties,
                                              effectiveTime);
    }


    /**
     * Update the properties of a classification that is currently attached to a specific metadata element.
     *
     * @param userId                 caller's userId
     * @param externalSourceGUID     unique identifier of the software capability that owns this collection
     * @param externalSourceName     unique name of the software capability that owns this collection
     * @param metadataElementGUID    unique identifier of the metadata element to update
     * @param classificationName     unique name of the classification to update
     * @param replaceProperties      flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                               the individual properties specified on the request.
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param properties             new properties for the classification
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException  the unique identifier or classification name is null or invalid in some way; properties do not match the
     *                                    valid properties associated with the classification's type definition
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException    there is a problem with the metadata store
     */
    public void reclassifyMetadataElementInStore(String            userId,
                                                 String            externalSourceGUID,
                                                 String            externalSourceName,
                                                 String            metadataElementGUID,
                                                 String            classificationName,
                                                 boolean           replaceProperties,
                                                 boolean           forLineage,
                                                 boolean           forDuplicateProcessing,
                                                 ElementProperties properties,
                                                 Date              effectiveTime) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        final String methodName                  = "reclassifyMetadataElementInStore";
        final String guidParameterName           = "metadataElementGUID";
        final String classificationParameterName = "classificationName";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-metadata-store/users/{2}/metadata-elements/{3}/classifications/{4}/update-properties";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(metadataElementGUID, guidParameterName, methodName);
        invalidParameterHandler.validateName(classificationName, classificationParameterName, methodName);

        UpdatePropertiesRequestBody requestBody = new UpdatePropertiesRequestBody();

        requestBody.setExternalSourceGUID(externalSourceGUID);
        requestBody.setExternalSourceName(externalSourceName);
        requestBody.setReplaceProperties(replaceProperties);
        requestBody.setForLineage(forLineage);
        requestBody.setForDuplicateProcessing(forDuplicateProcessing);
        requestBody.setProperties(properties);
        requestBody.setEffectiveTime(effectiveTime);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        serviceURLMarker,
                                        userId,
                                        metadataElementGUID,
                                        classificationName);
    }


    /**
     * Update the effectivity dates of a specific classification attached to a metadata element.
     * The effectivity dates control the visibility of the classification through specific APIs.
     *
     * @param userId                 caller's userId
     * @param metadataElementGUID    unique identifier of the metadata element to update
     * @param classificationName     unique name of the classification to update
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveFrom          the date when this element is active - null for active now
     * @param effectiveTo            the date when this element becomes inactive - null for active until deleted
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException  either the unique identifier or the status are invalid in some way
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException    there is a problem with the metadata store
     */
    @Override
    public void updateClassificationEffectivityInStore(String  userId,
                                                       String  metadataElementGUID,
                                                       String  classificationName,
                                                       boolean forLineage,
                                                       boolean forDuplicateProcessing,
                                                       Date    effectiveFrom,
                                                       Date    effectiveTo,
                                                       Date    effectiveTime) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        this.updateClassificationEffectivityInStore(userId,
                                                    null,
                                                    null,
                                                    metadataElementGUID,
                                                    classificationName,
                                                    forLineage,
                                                    forDuplicateProcessing,
                                                    effectiveFrom,
                                                    effectiveTo,
                                                    effectiveTime);
    }


    /**
     * Update the effectivity dates of a specific classification attached to a metadata element.
     * The effectivity dates control the visibility of the classification through specific APIs.
     *
     * @param userId                 caller's userId
     * @param externalSourceGUID     unique identifier of the software capability that owns this collection
     * @param externalSourceName     unique name of the software capability that owns this collection
     * @param metadataElementGUID    unique identifier of the metadata element to update
     * @param classificationName     unique name of the classification to update
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveFrom          the date when this element is active - null for active now
     * @param effectiveTo            the date when this element becomes inactive - null for active until deleted
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException  either the unique identifier or the status are invalid in some way
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException    there is a problem with the metadata store
     */
    public void updateClassificationEffectivityInStore(String  userId,
                                                       String  externalSourceGUID,
                                                       String  externalSourceName,
                                                       String  metadataElementGUID,
                                                       String  classificationName,
                                                       boolean forLineage,
                                                       boolean forDuplicateProcessing,
                                                       Date    effectiveFrom,
                                                       Date    effectiveTo,
                                                       Date    effectiveTime) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        final String methodName                  = "updateClassificationEffectivityInStore";
        final String guidParameterName           = "metadataElementGUID";
        final String classificationParameterName = "classificationName";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-metadata-store/users/{2}/metadata-elements/{3}/classifications/{4}/update-effectivity";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(metadataElementGUID, guidParameterName, methodName);
        invalidParameterHandler.validateName(classificationName, classificationParameterName, methodName);

        UpdateEffectivityDatesRequestBody requestBody = new UpdateEffectivityDatesRequestBody();

        requestBody.setExternalSourceGUID(externalSourceGUID);
        requestBody.setExternalSourceName(externalSourceName);
        requestBody.setForLineage(forLineage);
        requestBody.setForDuplicateProcessing(forDuplicateProcessing);
        requestBody.setEffectiveFrom(effectiveFrom);
        requestBody.setEffectiveTo(effectiveTo);
        requestBody.setEffectiveTime(effectiveTime);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        serviceURLMarker,
                                        userId,
                                        metadataElementGUID,
                                        classificationName);
    }


    /**
     * Remove the named classification from a specific metadata element.
     *
     * @param userId                 caller's userId
     * @param metadataElementGUID    unique identifier of the metadata element to update
     * @param classificationName     unique name of the classification to remove
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException  the unique identifier or classification name is null or invalid in some way
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException    there is a problem with the metadata store
     */
    @Override
    public void declassifyMetadataElementInStore(String  userId,
                                                 String  metadataElementGUID,
                                                 String  classificationName,
                                                 boolean forLineage,
                                                 boolean forDuplicateProcessing,
                                                 Date    effectiveTime) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        this.declassifyMetadataElementInStore(userId,
                                              null,
                                              null,
                                              metadataElementGUID,
                                              classificationName,
                                              forLineage,
                                              forDuplicateProcessing,
                                              effectiveTime);
    }


    /**
     * Remove the named classification from a specific metadata element.
     *
     * @param userId                 caller's userId
     * @param externalSourceGUID     unique identifier of the software capability that owns this collection
     * @param externalSourceName     unique name of the software capability that owns this collection
     * @param metadataElementGUID    unique identifier of the metadata element to update
     * @param classificationName     unique name of the classification to remove
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException  the unique identifier or classification name is null or invalid in some way
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException    there is a problem with the metadata store
     */
    public void declassifyMetadataElementInStore(String  userId,
                                                 String  externalSourceGUID,
                                                 String  externalSourceName,
                                                 String  metadataElementGUID,
                                                 String  classificationName,
                                                 boolean forLineage,
                                                 boolean forDuplicateProcessing,
                                                 Date    effectiveTime) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        final String methodName                  = "declassifyMetadataElementInStore";
        final String guidParameterName           = "metadataElementGUID";
        final String classificationParameterName = "classificationName";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-metadata-store/users/{2}/metadata-elements/{3}/classifications/{4}/delete";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(metadataElementGUID, guidParameterName, methodName);
        invalidParameterHandler.validateName(classificationName, classificationParameterName, methodName);

        MetadataSourceRequestBody requestBody = new MetadataSourceRequestBody();

        requestBody.setExternalSourceGUID(externalSourceGUID);
        requestBody.setExternalSourceName(externalSourceName);
        requestBody.setForLineage(forLineage);
        requestBody.setForDuplicateProcessing(forDuplicateProcessing);
        requestBody.setEffectiveTime(effectiveTime);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        serviceURLMarker,
                                        userId,
                                        metadataElementGUID,
                                        classificationName);
    }


    /**
     * Create a relationship between two metadata elements.  It is important to put the right element at each end of the relationship
     * according to the type definition since this will affect how the relationship is interpreted.
     *
     * @param userId                 caller's userId
     * @param relationshipTypeName   name of the type of relationship to create.  This will determine the types of metadata elements that can be
     *                               related and the properties that can be associated with this relationship.
     * @param metadataElement1GUID   unique identifier of the metadata element at end 1 of the relationship
     * @param metadataElement2GUID   unique identifier of the metadata element at end 2 of the relationship
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveFrom          the date when this element is active - null for active now
     * @param effectiveTo            the date when this element becomes inactive - null for active until deleted
     * @param properties             the properties of the relationship
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @return unique identifier of the new relationship
     *
     * @throws InvalidParameterException  the unique identifier's of the metadata elements are null or invalid in some way; the properties are
     *                                    not valid for this type of relationship
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException    there is a problem with the metadata store
     */
    @Override
    public String createRelatedElementsInStore(String            userId,
                                               String            relationshipTypeName,
                                               String            metadataElement1GUID,
                                               String            metadataElement2GUID,
                                               boolean           forLineage,
                                               boolean           forDuplicateProcessing,
                                               Date              effectiveFrom,
                                               Date              effectiveTo,
                                               ElementProperties properties,
                                               Date              effectiveTime) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        return this.createRelatedElementsInStore(userId,
                                                 null,
                                                 null,
                                                 relationshipTypeName,
                                                 metadataElement1GUID,
                                                 metadataElement2GUID,
                                                 forLineage,
                                                 forDuplicateProcessing,
                                                 effectiveFrom,
                                                 effectiveTo,
                                                 properties,
                                                 effectiveTime);
    }


    /**
     * Create a relationship between two metadata elements.  It is important to put the right element at each end of the relationship
     * according to the type definition since this will affect how the relationship is interpreted.
     *
     * @param userId                 caller's userId
     * @param externalSourceGUID     unique identifier of the software capability that owns this collection
     * @param externalSourceName     unique name of the software capability that owns this collection
     * @param relationshipTypeName   name of the type of relationship to create.  This will determine the types of metadata elements that can be
     *                               related and the properties that can be associated with this relationship.
     * @param metadataElement1GUID   unique identifier of the metadata element at end 1 of the relationship
     * @param metadataElement2GUID   unique identifier of the metadata element at end 2 of the relationship
     * @param forLineage             the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveFrom          the date when this element is active - null for active now
     * @param effectiveTo            the date when this element becomes inactive - null for active until deleted
     * @param properties             the properties of the relationship
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @return unique identifier of the new relationship
     *
     * @throws InvalidParameterException  the unique identifier's of the metadata elements are null or invalid in some way; the properties are
     *                                    not valid for this type of relationship
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException    there is a problem with the metadata store
     */
    public String createRelatedElementsInStore(String            userId,
                                               String            externalSourceGUID,
                                               String            externalSourceName,
                                               String            relationshipTypeName,
                                               String            metadataElement1GUID,
                                               String            metadataElement2GUID,
                                               boolean           forLineage,
                                               boolean           forDuplicateProcessing,
                                               Date              effectiveFrom,
                                               Date              effectiveTo,
                                               ElementProperties properties,
                                               Date              effectiveTime) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        final String methodName               = "createRelatedElementsInStore";
        final String elementTypeParameterName = "relationshipTypeName";
        final String end1ParameterName        = "metadataElement1GUID";
        final String end2ParameterName        = "metadataElement2GUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-metadata-store/users/{2}/related-elements";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(relationshipTypeName, elementTypeParameterName, methodName);
        invalidParameterHandler.validateGUID(metadataElement1GUID, end1ParameterName, methodName);
        invalidParameterHandler.validateGUID(metadataElement2GUID, end2ParameterName, methodName);

        NewRelatedElementsRequestBody requestBody = new NewRelatedElementsRequestBody();

        requestBody.setExternalSourceGUID(externalSourceGUID);
        requestBody.setExternalSourceName(externalSourceName);
        requestBody.setTypeName(relationshipTypeName);
        requestBody.setMetadataElement1GUID(metadataElement1GUID);
        requestBody.setMetadataElement2GUID(metadataElement2GUID);
        requestBody.setForLineage(forLineage);
        requestBody.setForDuplicateProcessing(forDuplicateProcessing);
        requestBody.setEffectiveFrom(effectiveFrom);
        requestBody.setEffectiveTo(effectiveTo);
        requestBody.setProperties(properties);
        requestBody.setEffectiveTime(effectiveTime);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  serviceURLMarker,
                                                                  userId);

        return restResult.getGUID();
    }


    /**
     * Update the properties associated with a relationship.
     *
     * @param userId caller's userId
     * @param relationshipGUID unique identifier of the relationship to update
     * @param replaceProperties flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                          the individual properties specified on the request.
     * @param forLineage the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param properties new properties for the relationship
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException the unique identifier of the relationship is null or invalid in some way; the properties are
     *                                    not valid for this type of relationship
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public void updateRelatedElementsInStore(String            userId,
                                             String            relationshipGUID,
                                             boolean           replaceProperties,
                                             boolean           forLineage,
                                             boolean           forDuplicateProcessing,
                                             ElementProperties properties,
                                             Date              effectiveTime) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        this.updateRelatedElementsInStore(userId,
                                          null,
                                          null,
                                          relationshipGUID,
                                          replaceProperties,
                                          forLineage,
                                          forDuplicateProcessing,
                                          properties,
                                          effectiveTime);
    }


    /**
     * Update the properties associated with a relationship.
     *
     * @param userId caller's userId
     * @param externalSourceGUID     unique identifier of the software capability that owns this collection
     * @param externalSourceName     unique name of the software capability that owns this collection
     * @param relationshipGUID unique identifier of the relationship to update
     * @param replaceProperties flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                          the individual properties specified on the request.
     * @param forLineage the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param properties new properties for the relationship
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException the unique identifier of the relationship is null or invalid in some way; the properties are
     *                                    not valid for this type of relationship
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public void updateRelatedElementsInStore(String            userId,
                                             String            externalSourceGUID,
                                             String            externalSourceName,
                                             String            relationshipGUID,
                                             boolean           replaceProperties,
                                             boolean           forLineage,
                                             boolean           forDuplicateProcessing,
                                             ElementProperties properties,
                                             Date              effectiveTime) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        final String methodName = "updateRelatedElementsInStore";
        final String guidParameterName = "relationshipGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-metadata-store/users/{2}/related-elements/{3}/update-properties";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(relationshipGUID, guidParameterName, methodName);

        UpdatePropertiesRequestBody requestBody = new UpdatePropertiesRequestBody();

        requestBody.setExternalSourceGUID(externalSourceGUID);
        requestBody.setExternalSourceName(externalSourceName);
        requestBody.setReplaceProperties(replaceProperties);
        requestBody.setProperties(properties);
        requestBody.setForLineage(forLineage);
        requestBody.setForDuplicateProcessing(forDuplicateProcessing);
        requestBody.setEffectiveTime(effectiveTime);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        serviceURLMarker,
                                        userId,
                                        relationshipGUID);
    }


    /**
     * Update the effectivity dates of a specific relationship between metadata elements.
     * The effectivity dates control the visibility of the classification through specific APIs.
     *
     * @param userId caller's userId
     * @param relationshipGUID unique identifier of the relationship to update
     * @param forLineage the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException either the unique identifier or the status are invalid in some way
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public  void updateRelatedElementsEffectivityInStore(String  userId,
                                                         String  relationshipGUID,
                                                         boolean forLineage,
                                                         boolean forDuplicateProcessing,
                                                         Date    effectiveFrom,
                                                         Date    effectiveTo,
                                                         Date    effectiveTime) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        this.updateRelatedElementsEffectivityInStore(userId,
                                                     null,
                                                     null,
                                                     relationshipGUID,
                                                     forLineage,
                                                     forDuplicateProcessing,
                                                     effectiveFrom,
                                                     effectiveTo,
                                                     effectiveTime);
    }


    /**
     * Update the effectivity dates of a specific relationship between metadata elements.
     * The effectivity dates control the visibility of the classification through specific APIs.
     *
     * @param userId caller's userId
     * @param externalSourceGUID     unique identifier of the software capability that owns this collection
     * @param externalSourceName     unique name of the software capability that owns this collection
     * @param relationshipGUID unique identifier of the relationship to update
     * @param forLineage the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException either the unique identifier or the status are invalid in some way
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public  void updateRelatedElementsEffectivityInStore(String  userId,
                                                         String  externalSourceGUID,
                                                         String  externalSourceName,
                                                         String  relationshipGUID,
                                                         boolean forLineage,
                                                         boolean forDuplicateProcessing,
                                                         Date    effectiveFrom,
                                                         Date    effectiveTo,
                                                         Date    effectiveTime) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        final String methodName = "updateRelatedElementsEffectivityInStore";
        final String guidParameterName = "relationshipGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-metadata-store/users/{2}/related-elements/{3}/update-effectivity";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(relationshipGUID, guidParameterName, methodName);

        UpdateEffectivityDatesRequestBody requestBody = new UpdateEffectivityDatesRequestBody();

        requestBody.setExternalSourceGUID(externalSourceGUID);
        requestBody.setExternalSourceName(externalSourceName);
        requestBody.setForLineage(forLineage);
        requestBody.setForDuplicateProcessing(forDuplicateProcessing);
        requestBody.setEffectiveFrom(effectiveFrom);
        requestBody.setEffectiveTo(effectiveTo);
        requestBody.setEffectiveTime(effectiveTime);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        serviceURLMarker,
                                        userId,
                                        relationshipGUID);
    }


    /**
     * Delete a relationship between two metadata elements.
     *
     * @param userId caller's userId
     * @param relationshipGUID unique identifier of the relationship to delete
     * @param forLineage the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException the unique identifier of the relationship is null or invalid in some way
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public void deleteRelatedElementsInStore(String  userId,
                                             String  relationshipGUID,
                                             boolean forLineage,
                                             boolean forDuplicateProcessing,
                                             Date    effectiveTime) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        this.deleteRelatedElementsInStore(userId,
                                          null,
                                          null,
                                          relationshipGUID,
                                          forLineage,
                                          forDuplicateProcessing,
                                          effectiveTime);
    }


    /**
     * Delete a relationship between two metadata elements.
     *
     * @param userId caller's userId
     * @param externalSourceGUID     unique identifier of the software capability that owns this collection
     * @param externalSourceName     unique name of the software capability that owns this collection
     * @param relationshipGUID unique identifier of the relationship to delete
     * @param forLineage the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException the unique identifier of the relationship is null or invalid in some way
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public void deleteRelatedElementsInStore(String  userId,
                                             String  externalSourceGUID,
                                             String  externalSourceName,
                                             String  relationshipGUID,
                                             boolean forLineage,
                                             boolean forDuplicateProcessing,
                                             Date    effectiveTime) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        final String methodName = "deleteRelatedElementsInStore";
        final String guidParameterName = "relationshipGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-metadata-store/users/{2}/related-elements/{3}/delete";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(relationshipGUID, guidParameterName, methodName);

        MetadataSourceRequestBody requestBody = new MetadataSourceRequestBody();

        requestBody.setExternalSourceGUID(externalSourceGUID);
        requestBody.setExternalSourceName(externalSourceName);
        requestBody.setForLineage(forLineage);
        requestBody.setForDuplicateProcessing(forDuplicateProcessing);
        requestBody.setEffectiveTime(effectiveTime);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        serviceURLMarker,
                                        userId,
                                        relationshipGUID);
    }


    /**
     * Using the named governance action process as a template, initiate a chain of engine actions.
     *
     * @param userId caller's userId
     * @param processQualifiedName unique name of the governance action process to use
     * @param requestSourceGUIDs  request source elements for the resulting governance service
     * @param actionTargets list of action target names to GUIDs for the resulting governance service
     * @param startTime future start time or null for "as soon as possible".
     * @param requestParameters request properties to be passed to the first governance service
     * @param originatorServiceName unique name of the requesting governance service (if initiated by a governance engine).
     * @param originatorEngineName optional unique name of the governance engine (if initiated by a governance engine).
     *
     * @return unique identifier of the governance action process instance
     * @throws InvalidParameterException null or unrecognized qualified name of the process
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public String initiateGovernanceActionProcess(String                userId,
                                                  String                processQualifiedName,
                                                  List<String>          requestSourceGUIDs,
                                                  List<NewActionTarget> actionTargets,
                                                  Date                  startTime,
                                                  Map<String, String>   requestParameters,
                                                  String                originatorServiceName,
                                                  String                originatorEngineName) throws InvalidParameterException,
                                                                                                     UserNotAuthorizedException,
                                                                                                     PropertyServerException
    {
        final String methodName = "initiateGovernanceActionProcess";
        final String qualifiedNameParameterName = "processQualifiedName";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-governance-service/users/{2}/governance-action-processes/initiate";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(processQualifiedName, qualifiedNameParameterName, methodName);

        InitiateGovernanceActionProcessRequestBody requestBody = new InitiateGovernanceActionProcessRequestBody();

        requestBody.setProcessQualifiedName(processQualifiedName);
        requestBody.setRequestSourceGUIDs(requestSourceGUIDs);
        requestBody.setActionTargets(actionTargets);
        requestBody.setStartDate(startTime);
        requestBody.setRequestParameters(requestParameters);
        requestBody.setOriginatorServiceName(originatorServiceName);
        requestBody.setOriginatorEngineName(originatorEngineName);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  serviceURLMarker,
                                                                  userId);

        return restResult.getGUID();
    }


    /**
     * Create an incident report to capture the situation detected by the caller.
     * This incident report will be processed by other governance activities.
     *
     * @param userId caller's userId
     * @param qualifiedName unique identifier to give this new incident report
     * @param domainIdentifier governance domain associated with this action (0=ALL)
     * @param background description of the situation
     * @param impactedResources details of the resources impacted by this situation
     * @param previousIncidents links to previous incident reports covering this situation
     * @param incidentClassifiers initial classifiers for the incident report
     * @param additionalProperties additional arbitrary properties for the incident reports
     * @param originatorGUID the unique identifier of the person or process that created the incident
     *
     * @return unique identifier of the resulting incident report
     * @throws InvalidParameterException null or non-unique qualified name for the incident report
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public  String createIncidentReport(String                        userId,
                                        String                        qualifiedName,
                                        int                           domainIdentifier,
                                        String                        background,
                                        List<IncidentImpactedElement> impactedResources,
                                        List<IncidentDependency>      previousIncidents,
                                        Map<String, Integer>          incidentClassifiers,
                                        Map<String, String>           additionalProperties,
                                        String                        originatorGUID) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        final String methodName = "createIncidentReport";
        final String qualifiedNameParameterName = "qualifiedName";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-metadata-store/users/{2}/incident-reports";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        IncidentReportRequestBody requestBody = new IncidentReportRequestBody();

        requestBody.setQualifiedName(qualifiedName);
        requestBody.setDomainIdentifier(domainIdentifier);
        requestBody.setBackground(background);
        requestBody.setImpactedResources(impactedResources);
        requestBody.setPreviousIncidents(previousIncidents);
        requestBody.setIncidentClassifiers(incidentClassifiers);
        requestBody.setAdditionalProperties(additionalProperties);
        requestBody.setOriginatorGUID(originatorGUID);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  serviceURLMarker,
                                                                  userId);

        return restResult.getGUID();
    }


    /**
     * Create a "To Do" request for someone to work on.
     *
     * @param userId caller's userId
     * @param qualifiedName unique name for the to do.  (Could be the engine name and a guid?)
     * @param title short meaningful phrase for the person receiving the request
     * @param instructions further details on what to do
     * @param category a category of to dos (for example, "data error", "access request")
     * @param priority priority value (based on organization's scale)
     * @param dueDate date/time this needs to be completed
     * @param additionalProperties additional arbitrary properties for the incident reports
     * @param assignToGUID unique identifier of the Actor element for the recipient
     * @param sponsorGUID unique identifier of the element that describes the rule, project that this is on behalf of
     * @param originatorGUID unique identifier of the source of the to do
     * @param actionTargets the list of elements that should be acted upon
     *
     * @return unique identifier of new to do element
     *
     * @throws InvalidParameterException either todoQualifiedName or assignedTo are null or not recognized
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException there is a problem connecting to (or inside) the metadata store
     */
    @Override
    public String openToDo(String                userId,
                           String                qualifiedName,
                           String                title,
                           String                instructions,
                           String                category,
                           int                   priority,
                           Date                  dueDate,
                           Map<String, String>   additionalProperties,
                           String                assignToGUID,
                           String                sponsorGUID,
                           String                originatorGUID,
                           List<NewActionTarget> actionTargets) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        final String methodName = "openToDo";
        final String toDoQualifiedNameParameterName = "qualifiedName";
        final String assignToParameterName          = "assignToGUID";

        propertyHelper.validateMandatoryName(qualifiedName, toDoQualifiedNameParameterName, methodName);
        propertyHelper.validateMandatoryName(assignToGUID, assignToParameterName, methodName);


        /*
         * Create the to do entity
         */
        ElementProperties properties = propertyHelper.addStringProperty(null,
                                                                        OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                        qualifiedName);

        properties = propertyHelper.addStringProperty(properties, OpenMetadataProperty.NAME.name, title);
        properties = propertyHelper.addStringProperty(properties, OpenMetadataProperty.DESCRIPTION.name, instructions);
        properties = propertyHelper.addStringProperty(properties, OpenMetadataProperty.TO_DO_TYPE.name, category);
        properties = propertyHelper.addDateProperty(properties, OpenMetadataProperty.DUE_TIME.name, dueDate);
        properties = propertyHelper.addIntProperty(properties, OpenMetadataProperty.PRIORITY.name, priority);
        properties = propertyHelper.addEnumProperty(properties,
                                                    OpenMetadataProperty.TO_DO_STATUS.name,
                                                    ToDoStatus.getOpenTypeName(),
                                                    ToDoStatus.OPEN.getName());

        String toDoGUID = this.createMetadataElementInStore(userId,
                                                            OpenMetadataType.TO_DO.typeName,
                                                            ElementStatus.ACTIVE,
                                                            null,
                                                            null,
                                                            false,
                                                            null,
                                                            null,
                                                            properties,
                                                            assignToGUID,
                                                            OpenMetadataType.ACTION_ASSIGNMENT_RELATIONSHIP.typeName,
                                                            null,
                                                            true,
                                                            true,
                                                            false,
                                                            null);

        if (toDoGUID != null)
        {
            if (actionTargets != null)
            {
                for (NewActionTarget actionTarget : actionTargets)
                {
                    if ((actionTarget != null) && (actionTarget.getActionTargetGUID() != null))
                    {
                        ElementProperties relationshipProperties = propertyHelper.addStringProperty(null,
                                                                                                    OpenMetadataProperty.ACTION_TARGET_NAME.name,
                                                                                                    actionTarget.getActionTargetName());
                        this.createRelatedElementsInStore(userId,
                                                          OpenMetadataType.ACTION_TARGET_RELATIONSHIP.typeName,
                                                          toDoGUID,
                                                          actionTarget.getActionTargetGUID(),
                                                          false,
                                                          false,
                                                          null,
                                                          null,
                                                          relationshipProperties,
                                                          null);
                    }
                }
            }

            if (sponsorGUID != null)
            {
                /*
                 * Link the "to do" and the sponsor
                 */
                this.createRelatedElementsInStore(userId,
                                                  OpenMetadataType.ACTION_SPONSOR_RELATIONSHIP.typeName,
                                                  sponsorGUID,
                                                  toDoGUID,
                                                  false,
                                                  false,
                                                  null,
                                                  null,
                                                  null,
                                                  null);
            }

            if (originatorGUID != null)
            {
                /*
                 * Link the "to do" and the originator
                 */
                this.createRelatedElementsInStore(userId,
                                                  OpenMetadataType.TO_DO_SOURCE_RELATIONSHIP.typeName,
                                                  originatorGUID,
                                                  toDoGUID,
                                                  false,
                                                  false,
                                                  null,
                                                  null,
                                                  null,
                                                  null);
            }
        }

        return toDoGUID;
    }


    /**
     * Create a new context event
     *
     * @param userId calling user
     * @param anchorGUID unique identifier for the context event's anchor element
     * @param parentContextEvents which context events should be linked as parents (guid->relationship properties)
     * @param childContextEvents which context events should be linked as children (guid->relationship properties)
     * @param relatedContextEvents which context events should be linked as related (guid->relationship properties)
     * @param impactedElements which elements are impacted by this context event (guid->relationship properties)
     * @param effectedDataResourceGUIDs which data resources are effected by this context event (asset guid->relationship properties)
     * @param contextEventEvidenceGUIDs which elements provide evidence that the context event is happening (element GUIDs)
     * @param contextEventProperties properties for the context event itself
     * @return guid of the new context event
     * @throws InvalidParameterException one of the properties are invalid
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException there is a problem connecting to (or inside) the metadata store
     */
    @Override
    public String registerContextEvent(String                                       userId,
                                       String                                       anchorGUID,
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
        final String methodName = "registerContextEvent";
        final String qualifiedNameParameterName = "qualifiedName";

        /*
         * The qualified name is needed.
         */
        if (contextEventProperties == null)
        {
            propertyHelper.validateMandatoryName(null, qualifiedNameParameterName, methodName);
            // not reached
            return null;
        }
        else
        {
            propertyHelper.validateMandatoryName(contextEventProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

            /*
             * Create the to do entity
             */
            ElementProperties properties = propertyHelper.addStringProperty(null,
                                                                            OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                            contextEventProperties.getQualifiedName());

            properties = propertyHelper.addStringProperty(properties, OpenMetadataProperty.NAME.name, contextEventProperties.getName());
            properties = propertyHelper.addStringProperty(properties, OpenMetadataProperty.DESCRIPTION.name, contextEventProperties.getEventDescription());
            properties = propertyHelper.addStringProperty(properties, OpenMetadataProperty.CONTEXT_EVENT_TYPE.name, contextEventProperties.getContextEventType());
            properties = propertyHelper.addStringProperty(properties, OpenMetadataProperty.EVENT_EFFECT.name, contextEventProperties.getEventEffect());
            properties = propertyHelper.addDateProperty(properties, OpenMetadataProperty.PLANNED_START_DATE.name, contextEventProperties.getPlannedStartDate());
            properties = propertyHelper.addDateProperty(properties, OpenMetadataProperty.ACTUAL_START_DATE.name, contextEventProperties.getActualStartDate());
            properties = propertyHelper.addDateProperty(properties, OpenMetadataProperty.PLANNED_COMPLETION_DATE.name, contextEventProperties.getPlannedCompletionDate());
            properties = propertyHelper.addDateProperty(properties, OpenMetadataProperty.ACTUAL_COMPLETION_DATE.name, contextEventProperties.getActualCompletionDate());
            properties = propertyHelper.addDateProperty(properties, OpenMetadataProperty.REFERENCE_EFFECTIVE_FROM.name, contextEventProperties.getReferenceEffectiveFrom());
            properties = propertyHelper.addDateProperty(properties, OpenMetadataProperty.REFERENCE_EFFECTIVE_TO.name, contextEventProperties.getReferenceEffectiveTo());
            properties = propertyHelper.addLongProperty(properties, OpenMetadataProperty.PLANNED_DURATION.name, contextEventProperties.getPlannedDuration());
            properties = propertyHelper.addLongProperty(properties, OpenMetadataProperty.ACTUAL_DURATION.name, contextEventProperties.getActualDuration());
            properties = propertyHelper.addLongProperty(properties, OpenMetadataProperty.REPEAT_INTERVAL.name, contextEventProperties.getRepeatInterval());
            properties = propertyHelper.addStringMapProperty(properties, OpenMetadataProperty.ADDITIONAL_PROPERTIES.name, contextEventProperties.getAdditionalProperties());

            boolean isOwnAnchor = (anchorGUID == null);

            String contextEventGUID = this.createMetadataElementInStore(userId,
                                                                        OpenMetadataType.CONTEXT_EVENT.typeName,
                                                                        ElementStatus.ACTIVE,
                                                                        null,
                                                                        anchorGUID,
                                                                        isOwnAnchor,
                                                                        contextEventProperties.getEffectiveFrom(),
                                                                        contextEventProperties.getEffectiveTo(),
                                                                        properties,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        true,
                                                                        true,
                                                                        false,
                                                                        null);

            if (contextEventGUID != null)
            {
                if (parentContextEvents != null)
                {
                    for (String guid : parentContextEvents.keySet())
                    {
                        if (guid != null)
                        {
                            DependentContextEventProperties suppliedRelationshipProperties = parentContextEvents.get(guid);
                            
                            if (suppliedRelationshipProperties != null)
                            {
                                ElementProperties relationshipProperties = propertyHelper.addStringProperty(null,
                                                                                          OpenMetadataProperty.DESCRIPTION.name,
                                                                                          suppliedRelationshipProperties.getDescription());

                                this.createRelatedElementsInStore(userId,
                                                                  OpenMetadataType.DEPENDENT_CONTEXT_EVENT_RELATIONSHIP.typeName,
                                                                  guid,
                                                                  contextEventGUID,
                                                                  false,
                                                                  false,
                                                                  suppliedRelationshipProperties.getEffectiveFrom(),
                                                                  suppliedRelationshipProperties.getEffectiveTo(),
                                                                  relationshipProperties,
                                                                  new Date());
                            }
                            else
                            {
                                this.createRelatedElementsInStore(userId,
                                                                  OpenMetadataType.DEPENDENT_CONTEXT_EVENT_RELATIONSHIP.typeName,
                                                                  guid,
                                                                  contextEventGUID,
                                                                  false,
                                                                  false,
                                                                  null,
                                                                  null,
                                                                  null,
                                                                  new Date());
                            }
                        }
                    }
                }

                if (childContextEvents != null)
                {
                    for (String guid : childContextEvents.keySet())
                    {
                        if (guid != null)
                        {
                            DependentContextEventProperties suppliedRelationshipProperties = childContextEvents.get(guid);

                            if (suppliedRelationshipProperties != null)
                            {
                                ElementProperties relationshipProperties = propertyHelper.addStringProperty(null,
                                                                                                            OpenMetadataProperty.DESCRIPTION.name,
                                                                                                            suppliedRelationshipProperties.getDescription());

                                this.createRelatedElementsInStore(userId,
                                                                  OpenMetadataType.DEPENDENT_CONTEXT_EVENT_RELATIONSHIP.typeName,
                                                                  contextEventGUID,
                                                                  guid,
                                                                  false,
                                                                  false,
                                                                  suppliedRelationshipProperties.getEffectiveFrom(),
                                                                  suppliedRelationshipProperties.getEffectiveTo(),
                                                                  relationshipProperties,
                                                                  new Date());
                            }
                            else
                            {
                                this.createRelatedElementsInStore(userId,
                                                                  OpenMetadataType.DEPENDENT_CONTEXT_EVENT_RELATIONSHIP.typeName,
                                                                  contextEventGUID,
                                                                  guid,
                                                                  false,
                                                                  false,
                                                                  null,
                                                                  null,
                                                                  null,
                                                                  new Date());
                            }
                        }
                    }
                }

                if (relatedContextEvents != null)
                {
                    for (String guid : relatedContextEvents.keySet())
                    {
                        if (guid != null)
                        {
                            RelatedContextEventProperties suppliedRelationshipProperties = relatedContextEvents.get(guid);

                            if (suppliedRelationshipProperties != null)
                            {
                                ElementProperties relationshipProperties = propertyHelper.addIntProperty(null,
                                                                                                         OpenMetadataProperty.STATUS_IDENTIFIER.name,
                                                                                                         suppliedRelationshipProperties.getStatusIdentifier());

                                relationshipProperties = propertyHelper.addIntProperty(relationshipProperties,
                                                                                       OpenMetadataProperty.CONFIDENCE.name,
                                                                                       suppliedRelationshipProperties.getConfidence());

                                relationshipProperties = propertyHelper.addStringProperty(relationshipProperties,
                                                                                          OpenMetadataProperty.STEWARD.name,
                                                                                          suppliedRelationshipProperties.getSteward());
                                relationshipProperties = propertyHelper.addStringProperty(relationshipProperties,
                                                                                          OpenMetadataProperty.STEWARD_TYPE_NAME.name,
                                                                                          suppliedRelationshipProperties.getStewardTypeName());
                                relationshipProperties = propertyHelper.addStringProperty(relationshipProperties,
                                                                                          OpenMetadataProperty.STEWARD_PROPERTY_NAME.name,
                                                                                          suppliedRelationshipProperties.getStewardPropertyName());
                                relationshipProperties = propertyHelper.addStringProperty(relationshipProperties,
                                                                                          OpenMetadataProperty.SOURCE.name,
                                                                                          suppliedRelationshipProperties.getSource());
                                relationshipProperties = propertyHelper.addStringProperty(relationshipProperties,
                                                                                          OpenMetadataProperty.NOTES.name,
                                                                                          suppliedRelationshipProperties.getNotes());

                                this.createRelatedElementsInStore(userId,
                                                                  OpenMetadataType.RELATED_CONTEXT_EVENT_RELATIONSHIP.typeName,
                                                                  guid,
                                                                  contextEventGUID,
                                                                  false,
                                                                  false,
                                                                  suppliedRelationshipProperties.getEffectiveFrom(),
                                                                  suppliedRelationshipProperties.getEffectiveTo(),
                                                                  relationshipProperties,
                                                                  new Date());
                            }
                            else
                            {
                                this.createRelatedElementsInStore(userId,
                                                                  OpenMetadataType.RELATED_CONTEXT_EVENT_RELATIONSHIP.typeName,
                                                                  guid,
                                                                  contextEventGUID,
                                                                  false,
                                                                  false,
                                                                  null,
                                                                  null,
                                                                  null,
                                                                  new Date());
                            }
                        }
                    }
                }

                if (impactedElements != null)
                {
                    for (String guid : impactedElements.keySet())
                    {
                        if (guid != null)
                        {
                            ContextEventImpactProperties suppliedRelationshipProperties = impactedElements.get(guid);

                            if (suppliedRelationshipProperties != null)
                            {
                                ElementProperties relationshipProperties = propertyHelper.addIntProperty(null,
                                                                                                         OpenMetadataProperty.SEVERITY_LEVEL_IDENTIFIER.name,
                                                                                                         suppliedRelationshipProperties.getSeverityLevelIdentifier());
                                relationshipProperties = propertyHelper.addStringProperty(relationshipProperties,
                                                                                          OpenMetadataProperty.DESCRIPTION.name,
                                                                                          suppliedRelationshipProperties.getDescription());

                                this.createRelatedElementsInStore(userId,
                                                                  OpenMetadataType.CONTEXT_EVENT_IMPACT_RELATIONSHIP.typeName,
                                                                  guid,
                                                                  contextEventGUID,
                                                                  false,
                                                                  false,
                                                                  suppliedRelationshipProperties.getEffectiveFrom(),
                                                                  suppliedRelationshipProperties.getEffectiveTo(),
                                                                  relationshipProperties,
                                                                  new Date());
                            }
                            else
                            {
                                this.createRelatedElementsInStore(userId,
                                                                  OpenMetadataType.CONTEXT_EVENT_IMPACT_RELATIONSHIP.typeName,
                                                                  guid,
                                                                  contextEventGUID,
                                                                  false,
                                                                  false,
                                                                  null,
                                                                  null,
                                                                  null,
                                                                  new Date());
                            }
                        }
                    }
                }

                if (effectedDataResourceGUIDs != null)
                {
                    for (String guid : effectedDataResourceGUIDs.keySet())
                    {
                        if (guid != null)
                        {
                            RelationshipProperties suppliedRelationshipProperties = effectedDataResourceGUIDs.get(guid);

                            if (suppliedRelationshipProperties != null)
                            {
                                this.createRelatedElementsInStore(userId,
                                                                  OpenMetadataType.CONTEXT_EVENT_FOR_TIMELINE_EFFECTS_RELATIONSHIP.typeName,
                                                                  guid,
                                                                  contextEventGUID,
                                                                  false,
                                                                  false,
                                                                  suppliedRelationshipProperties.getEffectiveFrom(),
                                                                  suppliedRelationshipProperties.getEffectiveTo(),
                                                                  null,
                                                                  new Date());
                            }
                            else
                            {
                                this.createRelatedElementsInStore(userId,
                                                                  OpenMetadataType.CONTEXT_EVENT_FOR_TIMELINE_EFFECTS_RELATIONSHIP.typeName,
                                                                  guid,
                                                                  contextEventGUID,
                                                                  false,
                                                                  false,
                                                                  null,
                                                                  null,
                                                                  null,
                                                                  new Date());
                            }
                        }
                    }
                }

                if (contextEventEvidenceGUIDs != null)
                {
                    for (String guid : contextEventEvidenceGUIDs.keySet())
                    {
                        if (guid != null)
                        {
                            RelationshipProperties suppliedProperties = contextEventEvidenceGUIDs.get(guid);

                            if (suppliedProperties != null)
                            {
                                this.createRelatedElementsInStore(userId,
                                                                  OpenMetadataType.CONTEXT_EVENT_EVIDENCE_RELATIONSHIP.typeName,
                                                                  contextEventGUID,
                                                                  guid,
                                                                  false,
                                                                  false,
                                                                  suppliedProperties.getEffectiveFrom(),
                                                                  suppliedProperties.getEffectiveTo(),
                                                                  null,
                                                                  new Date());
                            }
                            else
                            {
                                this.createRelatedElementsInStore(userId,
                                                                  OpenMetadataType.CONTEXT_EVENT_EVIDENCE_RELATIONSHIP.typeName,
                                                                  contextEventGUID,
                                                                  guid,
                                                                  false,
                                                                  false,
                                                                  null,
                                                                  null,
                                                                  null,
                                                                  new Date());
                            }
                        }
                    }
                }
            }

            return contextEventGUID;
        }
    }


    /**
     * Create or update the translation for a particular language/locale for a metadata element.
     *
     * @param userId caller's userId
     * @param elementGUID unique identifier of the element that this translation is related to
     * @param translationDetail properties of the translation
     *
     * @throws InvalidParameterException  the unique identifier is null or not known.
     * @throws UserNotAuthorizedException the service is not able to access the element
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    @Override
    public void setTranslation(String            userId,
                               String            elementGUID,
                               TranslationDetail translationDetail) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        final String methodName = "setTranslation";
        final String elementGUIDParameterName = "elementGUID";
        final String translationDetailParameterName = "translationDetail";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-metadata-store/users/{2}/multi-language/set-translation/{3}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(elementGUID, elementGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(translationDetail, translationDetailParameterName, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        translationDetail,
                                        serverName,
                                        serviceURLMarker,
                                        userId,
                                        elementGUID);
    }


    /**
     * Remove the translation for a particular language/locale for a metadata element.
     *
     * @param userId caller's userId
     * @param elementGUID unique identifier of the element that this translation is related to
     * @param language language requested
     * @param locale optional locale to qualify which translation if there are multiple translations for the language.
     *
     * @throws InvalidParameterException  the language is null or not known or not unique (add locale)
     * @throws UserNotAuthorizedException the service is not able to access the element
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    @Override
    public void clearTranslation(String userId,
                                 String elementGUID,
                                 String language,
                                 String locale) throws InvalidParameterException,
                                                       UserNotAuthorizedException,
                                                       PropertyServerException
    {
        final String methodName = "clearTranslation";
        final String elementGUIDParameterName = "elementGUID";
        final String languageParameterName = "language";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-metadata-store/users/{2}/multi-language/clear-translation/{3}?language={4}&locale={5}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(elementGUID, elementGUIDParameterName, methodName);
        invalidParameterHandler.validateName(language, languageParameterName, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        new NullRequestBody(),
                                        serverName,
                                        serviceURLMarker,
                                        userId,
                                        elementGUID,
                                        language,
                                        locale);
    }


    /**
     * Retrieve the translation for the matching language/locale.
     *
     * @param userId caller's userId
     * @param elementGUID unique identifier of the element that this translation is related to
     * @param language language requested
     * @param locale optional locale to qualify which translation if there are multiple translations for the language.
     *
     * @return the properties of the translation or null if there is none
     *
     * @throws InvalidParameterException  the unique identifier is null or not known.
     * @throws UserNotAuthorizedException the service is not able to access the element
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    @Override
    public TranslationDetail getTranslation(String userId,
                                            String elementGUID,
                                            String language,
                                            String locale) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        final String methodName = "getTranslation";
        final String elementGUIDParameterName = "elementGUID";
        final String languageParameterName = "language";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-metadata-store/users/{2}/multi-language/get-translation/{3}?language={4}&locale={5}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(elementGUID, elementGUIDParameterName, methodName);
        invalidParameterHandler.validateName(language, languageParameterName, methodName);

        TranslationDetailResponse response = restClient.callTranslationDetailGetRESTCall(methodName,
                                                                                         urlTemplate,
                                                                                         serverName,
                                                                                         serviceURLMarker,
                                                                                         userId,
                                                                                         elementGUID,
                                                                                         language,
                                                                                         locale);

        return response.getElement();
    }


    /**
     * Retrieve all translations associated with a metadata element.
     *
     * @param userId caller's userId
     * @param elementGUID unique identifier of the element that this translation is related to
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return
     *
     * @return list of translation properties or null if there are none
     *
     * @throws InvalidParameterException  the unique identifier is null or not known.
     * @throws UserNotAuthorizedException the service is not able to access the element
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    @Override
    public List<TranslationDetail> getTranslations(String userId,
                                                   String elementGUID,
                                                   int    startFrom,
                                                   int    pageSize) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        final String methodName = "getTranslations";
        final String elementGUIDParameterName = "elementGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-metadata-store/users/{2}/multi-language/get-translations/{3}?startFrom={4}&pageSize={5}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(elementGUID, elementGUIDParameterName, methodName);

        TranslationListResponse response = restClient.callTranslationListGetRESTCall(methodName,
                                                                                     urlTemplate,
                                                                                     new NullRequestBody(),
                                                                                     serverName,
                                                                                     serviceURLMarker,
                                                                                     userId,
                                                                                     elementGUID,
                                                                                     startFrom,
                                                                                     pageSize);

        return response.getElementList();
    }


    /**
     * Create or update the valid value for a particular open metadata property name.  If the typeName is null, this valid value
     * applies to properties of this name from all types.  The valid value is stored in the preferredValue property.  If a valid value is
     * already set up for this property (with overlapping effective dates) then the valid value is updated.
     *
     * @param userId caller's userId
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param validMetadataValue preferred value to use in the open metadata types plus additional descriptive values.
     *
     * @throws InvalidParameterException  the property name is null or not known.
     * @throws UserNotAuthorizedException the service is not able to create/access the element
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    @Override
    public void setUpValidMetadataValue(String             userId,
                                        String             typeName,
                                        String             propertyName,
                                        ValidMetadataValue validMetadataValue) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        final String methodName = "setUpValidMetadataValue";
        final String propertyNameParameterName = "propertyName";
        final String propertiesParameterName = "validMetadataValue";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-metadata-store/users/{2}/valid-metadata-values/setup-value/{3}?typeName={4}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(propertyName, propertyNameParameterName, methodName);
        invalidParameterHandler.validateObject(validMetadataValue, propertiesParameterName, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        validMetadataValue,
                                        serverName,
                                        serviceURLMarker,
                                        userId,
                                        propertyName,
                                        typeName);
    }


    /**
     * Create or update the valid value for a name that can be stored in a particular open metadata property name.
     * This property is of type map from name to string.
     * The valid value is stored in the preferredValue property of validMetadataValue.
     * If the typeName is null, this valid value applies to properties of this name from any open metadata type.
     * If a valid value is already set up for this property (with overlapping effective dates) then the valid value is updated.
     *
     * @param userId caller's userId
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param validMetadataValue preferred value to use in the open metadata types plus additional descriptive values.
     *
     * @throws InvalidParameterException  the property name is null or not known.
     * @throws UserNotAuthorizedException the service is not able to create/access the element
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    @Override
    public void setUpValidMetadataMapName(String             userId,
                                          String             typeName,
                                          String             propertyName,
                                          ValidMetadataValue validMetadataValue) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        final String methodName = "setUpValidMetadataMapName";
        final String propertyNameParameterName = "propertyName";
        final String propertiesParameterName = "validMetadataValue";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-metadata-store/users/{2}/valid-metadata-values/setup-map-name/{3}?typeName={4}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(propertyName, propertyNameParameterName, methodName);
        invalidParameterHandler.validateObject(validMetadataValue, propertiesParameterName, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        validMetadataValue,
                                        serverName,
                                        serviceURLMarker,
                                        userId,
                                        propertyName,
                                        typeName);
    }


    /**
     * Create or update the valid value for a name that can be stored in a particular open metadata property name.
     * This property is of type map from name to string.
     * The valid value is stored in the preferredValue property of validMetadataValue.
     * If the typeName is null, this valid value applies to properties of this name from any open metadata type.
     * If a valid value is already set up for this property (with overlapping effective dates) then the valid value is updated.
     *
     * @param userId caller's userId
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param mapName name in the map that this valid value applies.  If null then the value can be used for any name in the map.
     * @param validMetadataValue preferred value to use in the open metadata types plus additional descriptive values.
     *
     * @throws InvalidParameterException  the property name is null or not known.
     * @throws UserNotAuthorizedException the service is not able to create/access the element
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    @Override
    public void setUpValidMetadataMapValue(String             userId,
                                           String             typeName,
                                           String             propertyName,
                                           String             mapName,
                                           ValidMetadataValue validMetadataValue) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        final String methodName = "setUpValidMetadataMapValue";
        final String propertyNameParameterName = "propertyName";
        final String mapNameParameterName = "mapName";
        final String propertiesParameterName = "validMetadataValue";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-metadata-store/users/{2}/valid-metadata-values/setup-map-value/{3}/{4}?typeName={5}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(propertyName, propertyNameParameterName, methodName);
        invalidParameterHandler.validateName(mapName, mapNameParameterName, methodName);
        invalidParameterHandler.validateObject(validMetadataValue, propertiesParameterName, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        validMetadataValue,
                                        serverName,
                                        serviceURLMarker,
                                        userId,
                                        propertyName,
                                        mapName,
                                        typeName);
    }


    /**
     * Remove a valid value for a property.
     *
     * @param userId caller's userId
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param preferredValue specific valid value to remove
     *
     * @throws InvalidParameterException  the property name is null or not known.
     * @throws UserNotAuthorizedException the service is not able to create/access the element
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    @Override
    public void clearValidMetadataValue(String userId,
                                        String typeName,
                                        String propertyName,
                                        String preferredValue) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        final String methodName = "clearValidMetadataValue";
        final String propertyNameParameterName = "propertyName";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-metadata-store/users/{2}/valid-metadata-values/clear-value/{3}?preferredValue={4}&typeName={5}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(propertyName, propertyNameParameterName, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        new NullRequestBody(),
                                        serverName,
                                        serviceURLMarker,
                                        userId,
                                        propertyName,
                                        preferredValue,
                                        typeName);
    }


    /**
     * Remove a valid map name value for a property.  The match is done on preferred name.
     *
     * @param userId caller's userId
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param preferredValue specific valid value to remove
     *
     * @throws InvalidParameterException  the property name is null or not known.
     * @throws UserNotAuthorizedException the service is not able to create/access the element
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    @Override
    public void clearValidMetadataMapName(String userId,
                                          String typeName,
                                          String propertyName,
                                          String preferredValue) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        final String methodName = "clearValidMetadataMapName";
        final String propertyNameParameterName = "propertyName";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-metadata-store/users/{2}/valid-metadata-values/clear-map-name/{3}?preferredValue={4}&typeName={5}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(propertyName, propertyNameParameterName, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        new NullRequestBody(),
                                        serverName,
                                        serviceURLMarker,
                                        userId,
                                        propertyName,
                                        preferredValue,
                                        typeName);
    }


    /**
     * Remove a valid map name value for a property.  The match is done on preferred name.
     *
     * @param userId caller's userId
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param mapName name in the map that this valid value applies.  If null then the value can be used for any name in the map.
     * @param preferredValue specific valid value to remove
     *
     * @throws InvalidParameterException  the property name is null or not known.
     * @throws UserNotAuthorizedException the service is not able to create/access the element
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    @Override
    public void clearValidMetadataMapValue(String userId,
                                           String typeName,
                                           String propertyName,
                                           String mapName,
                                           String preferredValue) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        final String methodName = "clearValidMetadataMapValue";
        final String propertyNameParameterName = "propertyName";
        final String mapNameParameterName = "mapName";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-metadata-store/users/{2}/valid-metadata-values/clear-map-value/{3}/{4}?preferredValue={5}&typeName={6}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(propertyName, propertyNameParameterName, methodName);
        invalidParameterHandler.validateName(mapName, mapNameParameterName, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        new NullRequestBody(),
                                        serverName,
                                        serviceURLMarker,
                                        userId,
                                        propertyName,
                                        mapName,
                                        preferredValue,
                                        typeName);
    }


    /**
     * Validate whether the value found in an open metadata property is valid.
     *
     * @param userId caller's userId
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param actualValue value stored in the property - if this is null, true is only returned if null is set up as a valid value.
     *
     * @return boolean flag - true if the value is one of the defined valid values or there are no valid values set up for the property (and so any value is value).
     *
     * @throws InvalidParameterException  the property name is null or not known.
     * @throws UserNotAuthorizedException the service is not able to create/access the element
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    @Override
    public boolean validateMetadataValue(String userId,
                                         String typeName,
                                         String propertyName,
                                         String actualValue) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        final String methodName = "validateMetadataValue";
        final String propertyNameParameterName = "propertyName";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-metadata-store/users/{2}/valid-metadata-values/validate-value/{3}?actualValue={4}&typeName={5}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(propertyName, propertyNameParameterName, methodName);

        BooleanResponse response = restClient.callBooleanGetRESTCall(methodName,
                                                                     urlTemplate,
                                                                     serverName,
                                                                     serviceURLMarker,
                                                                     userId,
                                                                     propertyName,
                                                                     actualValue,
                                                                     typeName);

        return response.getFlag();
    }


    /**
     * Validate whether the name found in an open metadata map property is valid.
     *
     * @param userId caller's userId
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param actualValue value stored in the property - if this is null, true is only returned if null is set up as a valid value.
     *
     * @return boolean flag - true if the value is one of the defined valid values or there are no valid values set up for the property (and so any value is value).
     *
     * @throws InvalidParameterException  the property name is null or not known.
     * @throws UserNotAuthorizedException the service is not able to create/access the element
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    @Override
    public boolean validateMetadataMapName(String userId,
                                           String typeName,
                                           String propertyName,
                                           String actualValue) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        final String methodName = "validateMetadataMapName";
        final String propertyNameParameterName = "propertyName";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-metadata-store/users/{2}/valid-metadata-values/validate-map-name/{3}?actualValue={4}&typeName={5}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(propertyName, propertyNameParameterName, methodName);

        BooleanResponse response = restClient.callBooleanGetRESTCall(methodName,
                                                                     urlTemplate,
                                                                     serverName,
                                                                     serviceURLMarker,
                                                                     userId,
                                                                     propertyName,
                                                                     actualValue,
                                                                     typeName);

        return response.getFlag();
    }


    /**
     * Validate whether the name found in an open metadata map property is valid.
     *
     * @param userId caller's userId
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param mapName name in the map that this valid value applies.  If null then the value can be used for any name in the map.
     * @param actualValue value stored in the property - if this is null, true is only returned if null is set up as a valid value.
     *
     * @return boolean flag - true if the value is one of the defined valid values or there are no valid values set up for the property (and so any value is value).
     *
     * @throws InvalidParameterException  the property name is null or not known.
     * @throws UserNotAuthorizedException the service is not able to create/access the element
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    @Override
    public boolean validateMetadataMapValue(String userId,
                                            String typeName,
                                            String propertyName,
                                            String mapName,
                                            String actualValue) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        final String methodName = "validateMetadataMapValue";
        final String propertyNameParameterName = "propertyName";
        final String mapNameParameterName = "mapName";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-metadata-store/users/{2}/valid-metadata-values/validate-map-value/{3}/{4}?actualValue={5}&typeName={6}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(propertyName, propertyNameParameterName, methodName);
        invalidParameterHandler.validateName(mapName, mapNameParameterName, methodName);

        BooleanResponse response = restClient.callBooleanGetRESTCall(methodName,
                                                                     urlTemplate,
                                                                     serverName,
                                                                     serviceURLMarker,
                                                                     userId,
                                                                     propertyName,
                                                                     mapName,
                                                                     actualValue,
                                                                     typeName);

        return response.getFlag();
    }


    /**
     * Retrieve details of a specific valid value for a property.
     *
     * @param userId caller's userId
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param preferredValue valid value to match
     *
     * @return specific valid value definition or none if there is no definition stored
     *
     * @throws InvalidParameterException  the property name is null or not known.
     * @throws UserNotAuthorizedException the service is not able to create/access the element
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    @Override
    public ValidMetadataValue getValidMetadataValue(String userId,
                                                    String typeName,
                                                    String propertyName,
                                                    String preferredValue) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        final String methodName = "getValidMetadataValue";
        final String propertyNameParameterName = "propertyName";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-metadata-store/users/{2}/valid-metadata-values/get-value/{3}?preferredValue={4}&typeName={5}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(propertyName, propertyNameParameterName, methodName);

        ValidMetadataValueResponse response = restClient.callValidMetadataValueGetRESTCall(methodName,
                                                                                           urlTemplate,
                                                                                           serverName,
                                                                                           serviceURLMarker,
                                                                                           userId,
                                                                                           propertyName,
                                                                                           preferredValue,
                                                                                           typeName);

        return response.getElement();
    }


    /**
     * Retrieve details of a specific valid name for a map property.
     *
     * @param userId caller's userId
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param preferredValue valid value to match
     *
     * @return specific valid value definition or none if there is no definition stored
     *
     * @throws InvalidParameterException  the property name is null or not known.
     * @throws UserNotAuthorizedException the service is not able to create/access the element
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    @Override
    public ValidMetadataValue getValidMetadataMapName(String userId,
                                                      String typeName,
                                                      String propertyName,
                                                      String preferredValue) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        final String methodName = "getValidMetadataMapName";
        final String propertyNameParameterName = "propertyName";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-metadata-store/users/{2}/valid-metadata-values/get-map-name/{3}?preferredValue={4}&typeName={5}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(propertyName, propertyNameParameterName, methodName);

        ValidMetadataValueResponse response = restClient.callValidMetadataValueGetRESTCall(methodName,
                                                                                           urlTemplate,
                                                                                           serverName,
                                                                                           serviceURLMarker,
                                                                                           userId,
                                                                                           propertyName,
                                                                                           preferredValue,
                                                                                           typeName);

        return response.getElement();
    }


    /**
     * Retrieve details of a specific valid value for a map name.
     *
     * @param userId caller's userId
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param mapName name in the map that this valid value applies.  If null then the value can be used for any name in the map.
     * @param preferredValue valid value to match
     *
     * @return specific valid value definition or none if there is no definition stored
     *
     * @throws InvalidParameterException  the property name is null or not known.
     * @throws UserNotAuthorizedException the service is not able to create/access the element
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    @Override
    public ValidMetadataValue getValidMetadataMapValue(String userId,
                                                       String typeName,
                                                       String propertyName,
                                                       String mapName,
                                                       String preferredValue) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        final String methodName = "getValidMetadataMapValue";
        final String propertyNameParameterName = "propertyName";
        final String mapNameParameterName = "mapName";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-metadata-store/users/{2}/valid-metadata-values/get-map-value/{3}/{4}?preferredValue={5}&typeName={6}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(propertyName, propertyNameParameterName, methodName);
        invalidParameterHandler.validateName(mapName, mapNameParameterName, methodName);

        ValidMetadataValueResponse response = restClient.callValidMetadataValueGetRESTCall(methodName,
                                                                                           urlTemplate,
                                                                                           serverName,
                                                                                           serviceURLMarker,
                                                                                           userId,
                                                                                           propertyName,
                                                                                           mapName,
                                                                                           preferredValue,
                                                                                           typeName);

        return response.getElement();
    }


    /**
     * Retrieve all the valid values for the requested property.
     *
     * @param userId caller's userId
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of valid values defined for the property
     *
     * @throws InvalidParameterException  the property name is null or not known.
     * @throws UserNotAuthorizedException the service is not able to create/access the element
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    @Override
    public List<ValidMetadataValueDetail> getValidMetadataValues(String userId,
                                                                 String typeName,
                                                                 String propertyName,
                                                                 int    startFrom,
                                                                 int    pageSize) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        final String methodName = "getValidMetadataValues";
        final String propertyNameParameterName = "propertyName";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-metadata-store/users/{2}/valid-metadata-values/get-valid-metadata-values/{3}?typeName={4}&startFrom={5}&pageSize={6}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(propertyName, propertyNameParameterName, methodName);

        ValidMetadataValueDetailListResponse response = restClient.callValidMetadataValueDetailListGetRESTCall(methodName,
                                                                                                               urlTemplate,
                                                                                                               serverName,
                                                                                                               serviceURLMarker,
                                                                                                               userId,
                                                                                                               propertyName,
                                                                                                               typeName,
                                                                                                               startFrom,
                                                                                                               pageSize);

        return response.getElementList();
    }


    /**
     * Retrieve all the consistent valid values for the requested property.
     *
     * @param userId caller's userId
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param mapName optional name of map key that this valid value applies
     * @param preferredValue the value to match against
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of valid values defined for the property
     *
     * @throws InvalidParameterException  the property name is null or not known.
     * @throws UserNotAuthorizedException the service is not able to create/access the element
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    @Override
    public List<ValidMetadataValue> getConsistentMetadataValues(String userId,
                                                                String typeName,
                                                                String propertyName,
                                                                String mapName,
                                                                String preferredValue,
                                                                int    startFrom,
                                                                int    pageSize) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        final String methodName = "getConsistentMetadataValues";
        final String propertyNameParameterName = "propertyName";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-metadata-store/users/{2}/valid-metadata-values/{3}/consistent-metadata-values?typeName={4}&mapName={5}&preferredValue={6}&startFrom={7}&pageSize={8}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(propertyName, propertyNameParameterName, methodName);

        ValidMetadataValueListResponse response = restClient.callValidMetadataValueListGetRESTCall(methodName,
                                                                                                   urlTemplate,
                                                                                                   serverName,
                                                                                                   serviceURLMarker,
                                                                                                   userId,
                                                                                                   propertyName,
                                                                                                   typeName,
                                                                                                   mapName,
                                                                                                   preferredValue,
                                                                                                   startFrom,
                                                                                                   pageSize);

        return response.getElementList();
    }


    /**
     * Set up consistent metadata values relationship between the two property values.
     *
     * @param userId caller's userId
     * @param typeName1 type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName1 name of property that this valid value applies
     * @param mapName1 optional name of map key that this valid value applies
     * @param preferredValue1 the value to match against
     * @param typeName2 type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName2 name of property that this valid value applies
     * @param mapName2 optional name of map key that this valid value applies
     * @param preferredValue2 the value to match against
     *
     * @throws InvalidParameterException  the property name is null or not known.
     * @throws UserNotAuthorizedException the service is not able to create/access the element
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    @Override
    public void setConsistentMetadataValues(String userId,
                                            String typeName1,
                                            String propertyName1,
                                            String mapName1,
                                            String preferredValue1,
                                            String typeName2,
                                            String propertyName2,
                                            String mapName2,
                                            String preferredValue2) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        final String methodName = "setConsistentMetadataValues";
        final String propertyName1ParameterName = "propertyName1";
        final String preferredValue1ParameterName = "preferredValue1";
        final String propertyName2ParameterName = "propertyName2";
        final String preferredValue2ParameterName = "preferredValue2";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-metadata-store/users/{2}/valid-metadata-values/{3}/consistent-metadata-values/{4}?typeName1={5}&typeName2={6}&preferredValue1={7}&preferredValue2={8}&mapName1={9}&mapName2={10}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(propertyName1, propertyName1ParameterName, methodName);
        invalidParameterHandler.validateObject(preferredValue1, preferredValue1ParameterName, methodName);
        invalidParameterHandler.validateName(propertyName2, propertyName2ParameterName, methodName);
        invalidParameterHandler.validateObject(preferredValue2, preferredValue2ParameterName, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        new NullRequestBody(),
                                        serverName,
                                        serviceURLMarker,
                                        userId,
                                        propertyName1,
                                        propertyName2,
                                        typeName1,
                                        typeName2,
                                        preferredValue1,
                                        preferredValue2,
                                        mapName1,
                                        mapName2);
    }



    /**
     * Retrieve the reference data for this element.
     *
     * @param userId calling user
     * @param elementGUID element to query
     * @return map of reference data
     * @throws InvalidParameterException bad parameter
     * @throws PropertyServerException repository error
     * @throws UserNotAuthorizedException authorization issue
     */
    public Map<String, List<Map<String, String>>> getSpecification(String userId,
                                                                   String elementGUID) throws InvalidParameterException,
                                                                                              PropertyServerException,
                                                                                              UserNotAuthorizedException
    {
        final String methodName = "getSpecification";

        Map<String, List<Map<String, String>>> specification = new HashMap<>();

        RelatedMetadataElementList  refDataElements = this.getRelatedMetadataElements(userId,
                                                                                       elementGUID,
                                                                                       1,
                                                                                       OpenMetadataType.SPECIFICATION_PROPERTY_ASSIGNMENT_RELATIONSHIP.typeName,
                                                                                       null,
                                                                                       null,
                                                                                       null,
                                                                                       SequencingOrder.CREATION_DATE_RECENT,
                                                                                       false,
                                                                                       false,
                                                                                       new Date(),
                                                                                       0,
                                                                                       0);

        if ((refDataElements != null) && (refDataElements.getElementList() != null))
        {
            for (RelatedMetadataElement refDataElement : refDataElements.getElementList())
            {
                if (refDataElement != null)
                {
                    String propertyType = propertyHelper.getStringProperty(serviceURLMarker,
                                                                           OpenMetadataProperty.PROPERTY_TYPE.name,
                                                                           refDataElement.getRelationshipProperties(),
                                                                           methodName);
                    if (propertyType != null)
                    {
                        Map<String, String> additionalProperties = propertyHelper.getStringMapFromProperty(serviceURLMarker,
                                                                                                           OpenMetadataProperty.ADDITIONAL_PROPERTIES.name,
                                                                                                           refDataElement.getElement().getElementProperties(),
                                                                                                           methodName);

                        if (additionalProperties == null)
                        {
                            additionalProperties = new HashMap<>();
                        }

                        additionalProperties.put(propertyType + "Name",
                                                 propertyHelper.getStringProperty(serviceURLMarker,
                                                                                  OpenMetadataProperty.PREFERRED_VALUE.name,
                                                                                  refDataElement.getElement().getElementProperties(),
                                                                                  methodName));

                        List<Map<String, String>> properties = specification.get(propertyType);

                        if (properties == null)
                        {
                            properties = new ArrayList<>();
                        }

                        properties.add(additionalProperties);

                        specification.put(propertyType, properties);
                    }
                }
            }
        }

        if (! specification.isEmpty())
        {
            return specification;
        }

        return null;
    }


    /*
     * Work with external identifiers.
     */


    /**
     * Add a new external identifier to an existing open metadata element.
     *
     * @param userId calling user
     * @param externalScopeGUID unique identifier of software server capability representing the caller
     * @param externalScopeName unique name of software server capability representing the caller
     * @param externalScopeTypeName type name of the software capability describing the manager for the external identifier
     * @param openMetadataElementGUID unique identifier (GUID) of the element in the open metadata ecosystem
     * @param openMetadataElementTypeName type name for the open metadata element
     * @param externalIdentifierProperties optional properties used to define an external identifier
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime what is the effective time for related queries needed to do the update
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    @Override
    public void addExternalIdentifier(String                       userId,
                                      String                       externalScopeGUID,
                                      String                       externalScopeName,
                                      String                       externalScopeTypeName,
                                      String                       openMetadataElementGUID,
                                      String                       openMetadataElementTypeName,
                                      ExternalIdentifierProperties externalIdentifierProperties,
                                      Date                         effectiveFrom,
                                      Date                         effectiveTo,
                                      boolean                      forLineage,
                                      boolean                      forDuplicateProcessing,
                                      Date                         effectiveTime) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        final String methodName                      = "addExternalIdentifier";
        final String externalScopeGUIDParameterName  = "externalScopeGUID";
        final String externalScopeNameParameterName  = "externalScopeName";
        final String openMetadataGUIDParameterName   = "openMetadataElementGUID";
        final String openMetadataTypeParameterName   = "openMetadataElementTypeName";
        final String externalIdentifierPropertiesParameterName = "externalIdentifierProperties";
        final String externalIdentifierParameterName = "externalIdentifierProperties.externalIdentifier";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(externalScopeGUID, externalScopeGUIDParameterName, methodName);
        invalidParameterHandler.validateName(externalScopeName, externalScopeNameParameterName, methodName);
        invalidParameterHandler.validateGUID(openMetadataElementGUID, openMetadataGUIDParameterName, methodName);
        invalidParameterHandler.validateName(openMetadataElementTypeName, openMetadataTypeParameterName, methodName);
        invalidParameterHandler.validateObject(externalIdentifierProperties, externalIdentifierPropertiesParameterName, methodName);
        invalidParameterHandler.validateGUID(externalIdentifierProperties.getExternalIdentifier(), externalIdentifierParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-metadata-store/users/{2}/metadata-elements/{3}/{4}/external-identifiers/add?forLineage={5}&forDuplicateProcessing={6}";

        MetadataCorrelationProperties metadataCorrelationProperties = new MetadataCorrelationProperties(externalIdentifierProperties);

        metadataCorrelationProperties.setExternalScopeGUID(externalScopeGUID);
        metadataCorrelationProperties.setExternalScopeName(externalScopeName);
        metadataCorrelationProperties.setExternalScopeTypeName(externalScopeTypeName);

        UpdateMetadataCorrelatorsRequestBody requestBody = new UpdateMetadataCorrelatorsRequestBody();

        requestBody.setMetadataCorrelationProperties(metadataCorrelationProperties);
        requestBody.setEffectiveFrom(effectiveFrom);
        requestBody.setEffectiveTo(effectiveTo);
        requestBody.setEffectiveTime(effectiveTime);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        serviceURLMarker,
                                        userId,
                                        openMetadataElementTypeName,
                                        openMetadataElementGUID,
                                        forLineage,
                                        forDuplicateProcessing);
    }


    /**
     * Update the description of a specific external identifier.
     *
     * @param userId calling user
     * @param externalScopeGUID unique identifier of software server capability representing the caller
     * @param externalScopeName unique name of software server capability representing the caller
     * @param openMetadataElementGUID unique identifier (GUID) of the element in the open metadata ecosystem
     * @param openMetadataElementTypeName type name for the open metadata element
     * @param externalIdentifierProperties optional properties used to define an external identifier
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime what is the effective time for related queries needed to do the update
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    @Override
    public void updateExternalIdentifier(String                       userId,
                                         String                       externalScopeGUID,
                                         String                       externalScopeName,
                                         String                       externalScopeTypeName,
                                         String                       openMetadataElementGUID,
                                         String                       openMetadataElementTypeName,
                                         ExternalIdentifierProperties externalIdentifierProperties,
                                         Date                         effectiveFrom,
                                         Date                         effectiveTo,
                                         boolean                      forLineage,
                                         boolean                      forDuplicateProcessing,
                                         Date                         effectiveTime) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        final String methodName                      = "updateExternalIdentifier";
        final String externalScopeGUIDParameterName  = "externalScopeGUID";
        final String externalScopeNameParameterName  = "externalScopeName";
        final String openMetadataGUIDParameterName   = "openMetadataElementGUID";
        final String openMetadataTypeParameterName   = "openMetadataElementTypeName";
        final String externalIdentifierPropertiesParameterName = "externalIdentifierProperties";
        final String externalIdentifierParameterName = "externalIdentifierProperties.externalIdentifier";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(externalScopeGUID, externalScopeGUIDParameterName, methodName);
        invalidParameterHandler.validateName(externalScopeName, externalScopeNameParameterName, methodName);
        invalidParameterHandler.validateGUID(openMetadataElementGUID, openMetadataGUIDParameterName, methodName);
        invalidParameterHandler.validateName(openMetadataElementTypeName, openMetadataTypeParameterName, methodName);
        invalidParameterHandler.validateObject(externalIdentifierProperties, externalIdentifierPropertiesParameterName, methodName);
        invalidParameterHandler.validateGUID(externalIdentifierProperties.getExternalIdentifier(), externalIdentifierParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-metadata-store/users/{2}/metadata-elements/{3}/{4}/external-identifiers/update?forLineage={5}&forDuplicateProcessing={6}";

        MetadataCorrelationProperties metadataCorrelationProperties = new MetadataCorrelationProperties(externalIdentifierProperties);

        metadataCorrelationProperties.setExternalScopeGUID(externalScopeGUID);
        metadataCorrelationProperties.setExternalScopeName(externalScopeName);
        metadataCorrelationProperties.setExternalScopeTypeName(externalScopeTypeName);

        UpdateMetadataCorrelatorsRequestBody requestBody = new UpdateMetadataCorrelatorsRequestBody();

        requestBody.setMetadataCorrelationProperties(metadataCorrelationProperties);
        requestBody.setEffectiveFrom(effectiveFrom);
        requestBody.setEffectiveTo(effectiveTo);
        requestBody.setEffectiveTime(effectiveTime);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        serviceURLMarker,
                                        userId,
                                        openMetadataElementTypeName,
                                        openMetadataElementGUID,
                                        forLineage,
                                        forDuplicateProcessing);
    }


    /**
     * Remove an external identifier from an existing open metadata element.  The open metadata element is not
     * affected.
     *
     * @param userId calling user
     * @param externalScopeGUID unique identifier of software server capability representing the caller
     * @param externalScopeName unique name of software server capability representing the caller
     * @param openMetadataElementGUID unique identifier (GUID) of the element in the open metadata ecosystem
     * @param openMetadataElementTypeName type name for the open metadata element
     * @param externalIdentifier unique identifier of this element in the third party asset manager
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime what is the effective time for related queries needed to do the update
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    @Override
    public void removeExternalIdentifier(String  userId,
                                         String  externalScopeGUID,
                                         String  externalScopeName,
                                         String  openMetadataElementGUID,
                                         String  openMetadataElementTypeName,
                                         String  externalIdentifier,
                                         boolean forLineage,
                                         boolean forDuplicateProcessing,
                                         Date    effectiveTime) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        final String methodName                      = "removeExternalIdentifier";
        final String externalScopeGUIDParameterName  = "externalScopeGUID";
        final String externalScopeNameParameterName  = "externalScopeName";
        final String openMetadataGUIDParameterName   = "openMetadataElementGUID";
        final String openMetadataTypeParameterName   = "openMetadataElementTypeName";
        final String externalIdentifierParameterName = "externalIdentifier";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(externalScopeGUID, externalScopeGUIDParameterName, methodName);
        invalidParameterHandler.validateName(externalScopeName, externalScopeNameParameterName, methodName);
        invalidParameterHandler.validateGUID(openMetadataElementGUID, openMetadataGUIDParameterName, methodName);
        invalidParameterHandler.validateName(openMetadataElementTypeName, openMetadataTypeParameterName, methodName);
        invalidParameterHandler.validateGUID(externalIdentifier, externalIdentifierParameterName, methodName);

        final String urlTemplate =serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-metadata-store/users/{2}/metadata-elements/{3}/{4}/external-identifiers/remove?forLineage={5}&forDuplicateProcessing={6}";

        UpdateMetadataCorrelatorsRequestBody requestBody                    = new UpdateMetadataCorrelatorsRequestBody();
        MetadataCorrelationProperties        metadataCorrelationProperties  = new MetadataCorrelationProperties();

        metadataCorrelationProperties.setExternalScopeGUID(externalScopeGUID);
        metadataCorrelationProperties.setExternalScopeName(externalScopeName);
        metadataCorrelationProperties.setExternalIdentifier(externalIdentifier);

        requestBody.setMetadataCorrelationProperties(metadataCorrelationProperties);
        requestBody.setEffectiveTime(effectiveTime);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        serviceURLMarker,
                                        userId,
                                        openMetadataElementTypeName,
                                        openMetadataElementGUID,
                                        forLineage,
                                        forDuplicateProcessing);
    }


    /**
     * Remove the scope associated with a collection of external identifiers.  All associated external identifiers are removed too.
     * The linked open metadata elements are not affected.
     *
     * @param userId calling user
     * @param externalScopeGUID unique identifier of software capability representing the caller
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime what is the effective time for related queries needed to do the update
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    @Override
    public void removeExternalScope(String  userId,
                                    String  externalScopeGUID,
                                    boolean forLineage,
                                    boolean forDuplicateProcessing,
                                    Date    effectiveTime) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        final String methodName                      = "removeExternalScope";
        final String externalScopeGUIDParameterName  = "externalScopeGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(externalScopeGUID, externalScopeGUIDParameterName, methodName);

        final String urlTemplate =serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-metadata-store/users/{2}/external-scope/{3}/remove?forLineage={4}&forDuplicateProcessing={5}";

        EffectiveTimeRequestBody requestBody  = new EffectiveTimeRequestBody();

        requestBody.setEffectiveTime(effectiveTime);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        serviceURLMarker,
                                        userId,
                                        externalScopeGUID,
                                        forLineage,
                                        forDuplicateProcessing);
    }


    /**
     * Confirm that the values of a particular metadata element have been synchronized.  This is important
     * from an audit point of view, and to allow bidirectional updates of metadata using optimistic locking.
     *
     * @param userId calling user
     * @param externalScopeGUID unique identifier of software server capability representing the caller
     * @param externalScopeName unique name of software server capability representing the caller
     * @param openMetadataElementGUID unique identifier (GUID) of this element in open metadata
     * @param openMetadataElementTypeName type name for the open metadata element
     * @param externalIdentifier unique identifier of this element in the external asset manager
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    @Override
    public void confirmSynchronization(String userId,
                                       String externalScopeGUID,
                                       String externalScopeName,
                                       String openMetadataElementGUID,
                                       String openMetadataElementTypeName,
                                       String externalIdentifier) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        final String methodName                      = "confirmSynchronization";
        final String openMetadataGUIDParameterName   = "openMetadataElementGUID";
        final String externalScopeGUIDParameterName  = "externalScopeGUID";
        final String externalScopeNameParameterName  = "externalScopeName";
        final String externalIdentifierParameterName = "externalIdentifier";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(externalScopeGUID, externalScopeGUIDParameterName, methodName);
        invalidParameterHandler.validateName(externalScopeName, externalScopeNameParameterName, methodName);
        invalidParameterHandler.validateGUID(openMetadataElementGUID, openMetadataGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(externalIdentifier, externalIdentifierParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-metadata-store/users/{2}/metadata-elements/{3}/{4}/synchronized";

        MetadataCorrelationProperties requestBody = new MetadataCorrelationProperties();

        requestBody.setExternalScopeGUID(externalScopeGUID);
        requestBody.setExternalScopeName(externalScopeName);
        requestBody.setExternalIdentifier(externalIdentifier);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        serviceURLMarker,
                                        userId,
                                        openMetadataElementTypeName,
                                        openMetadataElementGUID);
    }


    /**
     * Return the list of headers for open metadata elements that are associated with a particular
     * external identifier.
     *
     * @param userId calling user
     * @param externalScopeGUID unique identifier of software server capability representing the caller
     * @param externalScopeName unique name of software server capability representing the caller
     * @param externalIdentifier unique identifier of this element in the external asset manager
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @return list of element headers
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    @Override
    public List<ElementHeader> getElementsForExternalIdentifier(String  userId,
                                                                String  externalScopeGUID,
                                                                String  externalScopeName,
                                                                String  externalIdentifier,
                                                                int     startFrom,
                                                                int     pageSize,
                                                                boolean forLineage,
                                                                boolean forDuplicateProcessing,
                                                                Date    effectiveTime) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException
    {
        final String methodName                      = "getElementsForExternalIdentifier";
        final String externalScopeGUIDParameterName  = "externalScopeGUID";
        final String externalScopeNameParameterName  = "externalScopeName";
        final String externalIdentifierParameterName = "externalIdentifier";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(externalScopeGUID, externalScopeGUIDParameterName, methodName);
        invalidParameterHandler.validateName(externalScopeName, externalScopeNameParameterName, methodName);
        invalidParameterHandler.validateGUID(externalIdentifier, externalIdentifierParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-metadata-store/users/{2}/external-identifiers/open-metadata-elements?startFrom={3}&pageSize={4}";

        UpdateMetadataCorrelatorsRequestBody requestBody                    = new UpdateMetadataCorrelatorsRequestBody();
        MetadataCorrelationProperties        metadataCorrelationProperties  = new MetadataCorrelationProperties();

        metadataCorrelationProperties.setExternalScopeGUID(externalScopeGUID);
        metadataCorrelationProperties.setExternalScopeName(externalScopeName);
        metadataCorrelationProperties.setExternalIdentifier(externalIdentifier);

        requestBody.setMetadataCorrelationProperties(metadataCorrelationProperties);
        requestBody.setEffectiveTime(effectiveTime);

        ElementHeadersResponse restResult = restClient.callElementHeadersPostRESTCall(methodName,
                                                                                      urlTemplate,
                                                                                      requestBody,
                                                                                      serverName,
                                                                                      serviceURLMarker,
                                                                                      userId,
                                                                                      startFrom,
                                                                                      validatedPageSize);

        return restResult.getElementHeaders();
    }


    /**
     * Check that the supplied external identifier matches the element GUID.
     *
     * @param userId calling user
     * @param externalScopeGUID unique identifier of software server capability representing the caller
     * @param externalScopeName unique name of software server capability representing the caller
     * @param openMetadataElementGUID element guid used for the lookup
     * @param openMetadataElementTypeName type name for the open metadata element
     * @param elementExternalIdentifier external identifier value
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @return boolean
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    @Override
    public boolean validateExternalIdentifier(String  userId,
                                              String  externalScopeGUID,
                                              String  externalScopeName,
                                              String  openMetadataElementGUID,
                                              String  openMetadataElementTypeName,
                                              String  elementExternalIdentifier,
                                              boolean forLineage,
                                              boolean forDuplicateProcessing,
                                              Date    effectiveTime) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        if ((elementExternalIdentifier != null) && (externalScopeGUID != null) && (externalScopeName != null))
        {
            final String methodName = "getExternalIdentifiers";
            final String guidParameterName = "openMetadataElementGUID";

            invalidParameterHandler.validateUserId(userId, methodName);
            invalidParameterHandler.validateGUID(openMetadataElementGUID, guidParameterName, methodName);

            final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-metadata-store/users/{2}/metadata-elements/{3}/{4}/external-identifiers/validate";

            BooleanResponse restResult = restClient.callBooleanPostRESTCall(methodName,
                                                                            urlTemplate,
                                                                            getEffectiveTimeQueryRequestBody(externalScopeGUID, externalScopeName, null),
                                                                            serverName,
                                                                            serviceURLMarker,
                                                                            userId,
                                                                            openMetadataElementTypeName,
                                                                            openMetadataElementGUID);

            return restResult.getFlag();
        }

        return true;
    }


    /**
     * Assemble the correlation headers attached to the supplied element guid.  This includes the external identifiers
     * plus information on the scope and usage.
     *
     * @param userId calling user
     * @param externalScopeGUID unique identifier of software server capability representing the caller
     * @param externalScopeName unique name of software server capability representing the caller
     * @param openMetadataElementGUID unique identifier (GUID) of this element in open metadata
     * @param openMetadataElementTypeName type name for the open metadata element
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @return list of correlation headers (note if asset manager identifiers are present, only the matching correlation header is returned)
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    @Override
    public List<MetadataCorrelationHeader> getExternalIdentifiers(String  userId,
                                                                  String  externalScopeGUID,
                                                                  String  externalScopeName,
                                                                  String  openMetadataElementGUID,
                                                                  String  openMetadataElementTypeName,
                                                                  int     startFrom,
                                                                  int     pageSize,
                                                                  boolean forLineage,
                                                                  boolean forDuplicateProcessing,
                                                                  Date    effectiveTime) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException
    {
        final String methodName = "getExternalIdentifiers";
        final String guidParameterName = "openMetadataElementGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(openMetadataElementGUID, guidParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/open-metadata-store/users/{2}/metadata-elements/{3}/{4}/external-identifiers";

        MetadataCorrelationHeadersResponse restResult = restClient.callCorrelationHeadersPostRESTCall(methodName,
                                                                                                      urlTemplate,
                                                                                                      getEffectiveTimeQueryRequestBody(externalScopeGUID, externalScopeName, null),
                                                                                                      serverName,
                                                                                                      serviceURLMarker,
                                                                                                      userId,
                                                                                                      openMetadataElementTypeName,
                                                                                                      openMetadataElementGUID);

        return restResult.getElementList();
    }


    /**
     * Return the vendor properties associated with an element.  The inner map holds the specific properties for each
     * vendor.  The outer maps the vendor identifier to the properties.
     *
     * @param userId calling user
     * @param openMetadataElementGUID unique identifier (GUID) of this element in open metadata
     * @param openMetadataElementTypeName type name for the open metadata element
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @return map of vendor properties
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    @Override
    public Map<String, Map<String, String>> getVendorProperties(String  userId,
                                                                String  openMetadataElementGUID,
                                                                String  openMetadataElementTypeName,
                                                                boolean forLineage,
                                                                boolean forDuplicateProcessing,
                                                                Date    effectiveTime) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException
    {
        final String methodName = "getVendorProperties";

        RelatedMetadataElementList  propertyFacets = this.getRelatedMetadataElements(userId,
                                                                                      openMetadataElementGUID,
                                                                                      1,
                                                                                      OpenMetadataType.REFERENCEABLE_FACET.typeName,
                                                                                      null,
                                                                                      null,
                                                                                      null,
                                                                                      SequencingOrder.CREATION_DATE_RECENT,
                                                                                      false,
                                                                                      false,
                                                                                      new Date(),
                                                                                      0,
                                                                                      0);
        if ((propertyFacets != null) && (propertyFacets.getElementList() != null))
        {
            Map<String, Map<String, String>> vendorProperties = new HashMap<>();

            for (RelatedMetadataElement relatedMetadataElement : propertyFacets.getElementList())
            {
                if (relatedMetadataElement != null)
                {
                    String vendorIdentifier = propertyHelper.getStringProperty(serverName,
                                                                               OpenMetadataProperty.SOURCE.name,
                                                                               relatedMetadataElement.getRelationshipProperties(),
                                                                               methodName);

                    if (vendorIdentifier != null)
                    {
                        String propertyFacetDescription = propertyHelper.getStringProperty(serverName,
                                                                                           OpenMetadataProperty.DESCRIPTION.name,
                                                                                           relatedMetadataElement.getElement().getElementProperties(),
                                                                                           methodName);
                        if (PropertyFacetValidValues.VENDOR_PROPERTIES_DESCRIPTION_VALUE.equals(propertyFacetDescription))
                        {
                            Map<String, String> facetProperties = propertyHelper.getStringMapFromProperty(serverName,
                                                                                                          OpenMetadataProperty.PROPERTIES.name,
                                                                                                          relatedMetadataElement.getElement().getElementProperties(),
                                                                                                          methodName);

                            vendorProperties.put(vendorIdentifier, facetProperties);
                        }
                    }
                }
            }

            return vendorProperties;
        }

        return null;
    }


    /**
     * Return the asset manager identifiers packaged in an appropriate request body.
     * @param externalScopeGUID unique identifier for the asset manager
     * @param externalScopeName unique name for the asset manager
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @return request body
     */
    protected EffectiveTimeQueryRequestBody getEffectiveTimeQueryRequestBody(String externalScopeGUID,
                                                                             String externalScopeName,
                                                                             Date   effectiveTime)
    {
        EffectiveTimeQueryRequestBody requestBody = new EffectiveTimeQueryRequestBody();

        if (externalScopeGUID != null)
        {
            requestBody.setExternalScopeGUID(externalScopeGUID);
            requestBody.setExternalScopeName(externalScopeName);
        }

        requestBody.setEffectiveTime(effectiveTime);

        return requestBody;
    }


}
