/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.omf.client;

import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.openmetadata.builders.OpenMetadataRelationshipBuilder;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ActivityStatus;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DeleteMethod;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementHeader;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ElementStatus;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.mapper.PropertyFacetValidValues;
import org.odpi.openmetadata.frameworks.openmetadata.properties.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.contextevents.ContextEventImpactProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.contextevents.ContextEventProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.contextevents.DependentContextEventProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.contextevents.RelatedContextEventProperties;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.AssignmentType;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworkservices.omf.client.rest.OMFRESTClient;
import org.odpi.openmetadata.frameworkservices.omf.ffdc.OMFServicesErrorCode;
import org.odpi.openmetadata.frameworkservices.omf.rest.*;

import java.util.*;

/**
 * OpenMetadataClientBase provides an interface to the open metadata store.  This is part of the Open Survey Framework (OGF)
 * and provides a comprehensive interface for working with all types of metadata, subject to the user's (and this OMAS's) security permissions.
 * The interface supports search, maintenance of metadata elements, classifications and relationships plus the ability to raise incident reports
 * and todos along with the ability to work with metadata valid values and translations.
 */
public abstract class OpenMetadataClientBase extends OpenMetadataClient
{
    private   final OMFRESTClient restClient;               /* Initialized in constructor */

    protected final InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();

    protected final OpenMetadataRelationshipBuilder relationshipBuilder = new OpenMetadataRelationshipBuilder();

    private final PropertyHelper propertyHelper = new PropertyHelper();


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param maxPageSize maximum value allowed for page size
     *
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     *                                   REST API calls.
     */
    public OpenMetadataClientBase(String serverName,
                                  String serverPlatformURLRoot,
                                  int    maxPageSize) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot);

        final String methodName = "Constructor (no security)";

        this.invalidParameterHandler.setMaxPagingSize(maxPageSize);
        this.invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);
        this.restClient = new OMFRESTClient(serverName, serverPlatformURLRoot);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param serverUserId          caller's userId embedded in all HTTP requests
     * @param serverPassword        caller's password embedded in all HTTP requests
     * @param maxPageSize maximum value allowed for page size
     *
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     *                                   REST API calls.
     */
    public OpenMetadataClientBase(String serverName,
                                  String serverPlatformURLRoot,
                                  String serverUserId,
                                  String serverPassword,
                                  int    maxPageSize) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot);

        final String methodName = "Constructor (with security)";

        this.invalidParameterHandler.setMaxPagingSize(maxPageSize);
        this.invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.restClient = new OMFRESTClient(serverName, serverPlatformURLRoot, serverUserId, serverPassword);
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
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/open-metadata-store/users/{1}/open-metadata-types";

        invalidParameterHandler.validateUserId(userId, methodName);

        TypeDefGalleryResponse restResult = restClient.callTypeDefGalleryGetRESTCall(methodName,
                                                                                     urlTemplate,
                                                                                     serverName,
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
        final String entityURLTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/open-metadata-store/users/{1}/open-metadata-types/entity-defs";
        final String relationshipURLTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/open-metadata-store/users/{1}/open-metadata-types/relationship-defs";
        final String classificationURLTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/open-metadata-store/users/{1}/open-metadata-types/classification-defs";

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
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/open-metadata-store/users/{1}/open-metadata-attribute-types/category/{2}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateEnum(category, categoryParameterName, methodName);

        AttributeTypeDefListResponse restResult = restClient.callAttributeTypeDefListGetRESTCall(methodName,
                                                                                                 urlTemplate,
                                                                                                 serverName,
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
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/open-metadata-store/users/{1}/open-metadata-types/external-id?standard={2}&organization={3}&identifier={4}";

        invalidParameterHandler.validateUserId(userId, methodName);

        TypeDefListResponse restResult = restClient.callTypeDefListGetRESTCall(methodName,
                                                                               urlTemplate,
                                                                               serverName,
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
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/open-metadata-store/users/{1}/open-metadata-types/sub-types?typeName={2}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(typeName, parameterName, methodName);

        TypeDefListResponse restResult = restClient.callTypeDefListGetRESTCall(methodName,
                                                                               urlTemplate,
                                                                               serverName,
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
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/open-metadata-store/users/{1}/open-metadata-types/guid/{2}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(guid, guidParameterName, methodName);

        TypeDefResponse restResult = restClient.callTypeDefGetRESTCall(methodName,
                                                                       urlTemplate,
                                                                       serverName,
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
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/open-metadata-store/users/{1}/open-metadata-attribute-types/guid/{2}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(guid, guidParameterName, methodName);

        AttributeTypeDefResponse restResult = restClient.callAttributeTypeDefGetRESTCall(methodName,
                                                                                         urlTemplate,
                                                                                         serverName,
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
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/open-metadata-store/users/{1}/open-metadata-types/name/{2}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameterName, methodName);

        TypeDefResponse restResult = restClient.callTypeDefGetRESTCall(methodName,
                                                                       urlTemplate,
                                                                       serverName,
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
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/open-metadata-store/users/{1}/open-metadata-attribute-types/name/{2}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameterName, methodName);

        AttributeTypeDefResponse restResult = restClient.callAttributeTypeDefGetRESTCall(methodName,
                                                                                         urlTemplate,
                                                                                         serverName,
                                                                                         userId,
                                                                                         name);

        return restResult.getAttributeTypeDef();
    }


    /**
     * Retrieve the metadata element using its unique identifier.
     *
     * @param userId caller's userId
     * @param elementGUID unique identifier for the metadata element
     * @param getOptions multiple options to control the query
     *
     * @return metadata element properties
     *
     * @throws InvalidParameterException  the unique identifier is null or not known.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    @Override
    public OpenMetadataElement getMetadataElementByGUID(String     userId,
                                                        String     elementGUID,
                                                        GetOptions getOptions) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        final String methodName        = "getMetadataElementByGUID";
        final String guidParameterName = "elementGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/open-metadata-store/users/{1}/metadata-elements/{2}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(elementGUID, guidParameterName, methodName);

        GetRequestBody requestBody = new GetRequestBody(getOptions);

        OpenMetadataElementResponse restResult = restClient.callOpenMetadataElementPostRESTCall(methodName,
                                                                                               urlTemplate,
                                                                                               requestBody,
                                                                                               serverName,
                                                                                               userId,
                                                                                               elementGUID);

        return restResult.getElement();
    }


    /**
     * Retrieve the metadata element using its unique name (typically the qualified name).
     *
     * @param userId caller's userId
     * @param uniqueName unique name for the metadata element
     * @param uniquePropertyName name of property name to test in the open metadata element - if null "qualifiedName" is used
     * @param getOptions multiple options to control the query
     *
     * @return metadata element properties or null if not found
     *
     * @throws InvalidParameterException the unique identifier is null.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    @Override
    public OpenMetadataElement getMetadataElementByUniqueName(String     userId,
                                                              String     uniqueName,
                                                              String     uniquePropertyName,
                                                              GetOptions getOptions) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        final String methodName          = "getMetadataElementByUniqueName";
        final String defaultPropertyName = OpenMetadataProperty.QUALIFIED_NAME.name;
        final String nameParameterName   = "uniqueName";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/open-metadata-store/users/{1}/metadata-elements/by-unique-name";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(uniqueName, nameParameterName, methodName);

        UniqueNameRequestBody requestBody = new UniqueNameRequestBody(getOptions);
        requestBody.setName(uniqueName);
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
                                                                                                userId);

        return restResult.getElement();
    }


    /**
     * Retrieve the unique identifier of a metadata element using its unique name (typically the qualified name).
     *
     * @param userId                 caller's userId
     * @param uniqueName             unique name for the metadata element
     * @param uniquePropertyName     name of property name to test in the open metadata element - if null "qualifiedName" is used
     * @param getOptions multiple options to control the query
     *
     * @return metadata element unique identifier (guid)
     *
     * @throws InvalidParameterException  the unique name is null or not known.
     * @throws UserNotAuthorizedException the caller's userId is not able to access the element
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    @Override
    public String getMetadataElementGUIDByUniqueName(String     userId,
                                                     String     uniqueName,
                                                     String     uniquePropertyName,
                                                     GetOptions getOptions) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        final String methodName          = "getMetadataElementGUIDByUniqueName";
        final String defaultPropertyName = "qualifiedName";
        final String nameParameterName   = "uniqueName";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/open-metadata-store/users/{1}/metadata-elements/guid-by-unique-name";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(uniqueName, nameParameterName, methodName);

        UniqueNameRequestBody requestBody = new UniqueNameRequestBody(getOptions);
        requestBody.setName(uniqueName);
        requestBody.setNameParameterName(nameParameterName);

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
                                                                  userId);

        return restResult.getGUID();
    }


    /**
     * Extract the type name from the options
     *
     * @param getOptions value from the external caller
     * @return type name to pass to the query
     */
    private String getQueryTypeName(GetOptions   getOptions)
    {
        String queryTypeName = null;

        if ((getOptions != null) && (getOptions.getMetadataElementTypeName() != null))
        {
            queryTypeName = getOptions.getMetadataElementTypeName();
        }

        return queryTypeName;
    }


    /**
     * Retrieve the metadata elements of the requested type that contain the requested string.
     *
     * @param userId caller's userId
     * @param searchString name to retrieve
     * @param searchOptions multiple options to control the query
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
                                                                    SearchOptions       searchOptions) throws InvalidParameterException,
                                                                                                                      UserNotAuthorizedException,
                                                                                                                      PropertyServerException
    {
        final String methodName                = "findMetadataElementsWithString";
        final String searchStringParameterName = "searchString";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/open-metadata-store/users/{1}/metadata-elements/by-search-string";

        invalidParameterHandler.validateUserId(userId, methodName);

        /*
         * Need either a type name or a query string to have a valid query
         */
        String queryTypeName = this.getQueryTypeName(searchOptions);

        if (queryTypeName == null)
        {
            invalidParameterHandler.validateSearchString(searchString, searchStringParameterName, methodName);
        }

        SearchStringRequestBody requestBody = new SearchStringRequestBody(searchOptions);

        requestBody.setSearchString(searchString);
        requestBody.setSearchStringParameterName(searchStringParameterName);

        OpenMetadataElementsResponse restResult = restClient.callOpenMetadataElementsPostRESTCall(methodName,
                                                                                                  urlTemplate,
                                                                                                  requestBody,
                                                                                                  serverName,
                                                                                                  userId);

        return restResult.getElementList();
    }


    /**
     * Return a list of elements with the requested search string in their (display, resource)name, qualified name,
     * title, text, summary, identifier or description.  The search string is interpreted as a regular expression (RegEx).
     * The breadth of the search is determined by the supplied anchorGUID.
     *
     * @param userId caller's userId
     * @param searchString name to retrieve
     * @param anchorGUID unique identifier of anchor
     * @param queryOptions multiple options to control the query
     * @return list of matching metadata elements (or null if no elements match the name)
     * @throws InvalidParameterException  the qualified name is null
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    @Override
    public AnchorSearchMatches findElementsForAnchor(String       userId,
                                                     String       searchString,
                                                     String       anchorGUID,
                                                     QueryOptions queryOptions) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        final String methodName                = "findElementsForAnchor";
        final String contextParameterName      = "anchorGUID";
        final String searchStringParameterName = "searchString";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/open-metadata-store/users/{1}/metadata-elements/by-search-string/for-anchor/{2}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(searchString, searchStringParameterName, methodName);
        invalidParameterHandler.validateGUID(anchorGUID, contextParameterName, methodName);

        SearchStringRequestBody requestBody = new SearchStringRequestBody(queryOptions);

        requestBody.setSearchString(searchString);
        requestBody.setSearchStringParameterName(searchStringParameterName);

        AnchorSearchMatchesResponse restResult = restClient.callAnchorSearchMatchesPostRESTCall(methodName,
                                                                                                  urlTemplate,
                                                                                                  requestBody,
                                                                                                  serverName,
                                                                                                  userId,
                                                                                                  anchorGUID);

        return restResult.getElement();
    }


    /**
     * Return a list of elements with the requested search string in their (display, resource)name, qualified name,
     * title, text, summary, identifier or description.  The search string is interpreted as a regular expression (RegEx).
     * The breadth of the search is determined by the supplied domain name. The results are organized by anchor element.
     *
     * @param userId caller's userId
     * @param searchString name to retrieve
     * @param anchorDomainName name of open metadata type for the domain
     * @param searchOptions multiple options to control the query

     *
     * @return list of matching metadata elements (or null if no elements match the name)
     *
     * @throws InvalidParameterException  the qualified name is null
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    @Override
    public List<AnchorSearchMatches> findElementsInAnchorDomain(String        userId,
                                                                String        searchString,
                                                                String        anchorDomainName,
                                                                SearchOptions searchOptions) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException
    {
        final String methodName                = "findElementsInAnchorDomain";
        final String contextParameterName      = "anchorDomainName";
        final String searchStringParameterName = "searchString";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/open-metadata-store/users/{1}/metadata-elements/by-search-string/in-anchor-domain/{2}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(searchString, searchStringParameterName, methodName);
        invalidParameterHandler.validateName(anchorDomainName, contextParameterName, methodName);

        SearchStringRequestBody requestBody = new SearchStringRequestBody(searchOptions);

        requestBody.setSearchString(searchString);
        requestBody.setSearchStringParameterName(searchStringParameterName);

        AnchorSearchMatchesListResponse restResult = restClient.callAnchorSearchMatchesListPostRESTCall(methodName,
                                                                                                        urlTemplate,
                                                                                                        requestBody,
                                                                                                        serverName,
                                                                                                        userId,
                                                                                                        anchorDomainName);

        return restResult.getElements();
    }


    /**
     * Return a list of elements with the requested search string in their (display, resource)name, qualified name,
     * title, text, summary, identifier or description.  The search string is interpreted as a regular expression (RegEx).
     * The breadth of the search is determined by the supplied scope guid. The results are organized by anchor element.
     *
     * @param userId caller's userId
     * @param searchString name to retrieve
     * @param anchorScopeGUID unique identifier of the scope to use
     * @param searchOptions multiple options to control the query
     *
     * @return list of matching metadata elements (or null if no elements match the name)
     *
     * @throws InvalidParameterException  the qualified name is null
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    @Override
    public List<AnchorSearchMatches> findElementsInAnchorScope(String        userId,
                                                               String        searchString,
                                                               String        anchorScopeGUID,
                                                               SearchOptions searchOptions) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   PropertyServerException
    {
        final String methodName                = "findElementsInAnchorScope";
        final String contextParameterName      = "anchorScopeGUID";
        final String searchStringParameterName = "searchString";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/open-metadata-store/users/{1}/metadata-elements/by-search-string/in-anchor-scope/{2}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(searchString, searchStringParameterName, methodName);
        invalidParameterHandler.validateGUID(anchorScopeGUID, contextParameterName, methodName);

        SearchStringRequestBody requestBody = new SearchStringRequestBody(searchOptions);

        requestBody.setSearchString(searchString);
        requestBody.setSearchStringParameterName(searchStringParameterName);

        AnchorSearchMatchesListResponse restResult = restClient.callAnchorSearchMatchesListPostRESTCall(methodName,
                                                                                                        urlTemplate,
                                                                                                        requestBody,
                                                                                                        serverName,
                                                                                                        userId,
                                                                                                        anchorScopeGUID);

        return restResult.getElements();
    }


    /**
     * Retrieve the metadata elements connected to the supplied element.
     *
     * @param userId caller's userId
     * @param elementGUID unique identifier for the starting metadata element
     * @param startingAtEnd indicates which end to retrieve from (0 is "either end"; 1 is end1; 2 is end 2)
     * @param relationshipTypeName type name of relationships to follow (or null for all)
     * @param queryOptions multiple options to control the query
     *
     * @return list of related elements
     *
     * @throws InvalidParameterException  the unique identifier is null or not known; the relationship type is invalid
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    @Override
    public   RelatedMetadataElementList getRelatedMetadataElements(String       userId,
                                                                   String       elementGUID,
                                                                   int          startingAtEnd,
                                                                   String       relationshipTypeName,
                                                                   QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                     UserNotAuthorizedException,
                                                                                                     PropertyServerException
    {
        final String methodName            = "getRelatedMetadataElements";
        final String guidParameterName     = "elementGUID";

        final String allURLTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/open-metadata-store/users/{1}/related-elements/{2}/any-type?startingAtEnd={3}";
        final String specificURLTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/open-metadata-store/users/{1}/related-elements/{2}/type/{3}?startingAtEnd={4}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(elementGUID, guidParameterName, methodName);

        RelatedMetadataElementListResponse restResult;

        ResultsRequestBody requestBody = new ResultsRequestBody(queryOptions);

        if (relationshipTypeName == null)
        {
            restResult = restClient.callRelatedMetadataElementListPostRESTCall(methodName,
                                                                               allURLTemplate,
                                                                               requestBody,
                                                                               serverName,
                                                                               userId,
                                                                               elementGUID,
                                                                               startingAtEnd);
        }
        else
        {
            restResult = restClient.callRelatedMetadataElementListPostRESTCall(methodName,
                                                                               specificURLTemplate,
                                                                               requestBody,
                                                                               serverName,
                                                                               userId,
                                                                               elementGUID,
                                                                               relationshipTypeName,
                                                                               startingAtEnd);
        }

        return restResult.getRelatedElementList();
    }


    /**
     * Return all the elements that are anchored to an asset plus relationships between these elements and to other elements.
     *
     * @param userId name of the server instances for this request
     * @param elementGUID  unique identifier for the element
     * @param queryOptions multiple options to control the query
     *
     * @return graph of elements
     *
     * @throws InvalidParameterException  the unique identifier is null or not known; the relationship type is invalid
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    @Override
    public OpenMetadataElementGraph getAnchoredElementsGraph(String       userId,
                                                             String       elementGUID,
                                                             QueryOptions queryOptions) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException
    {
        final String methodName        = "getAnchoredElementsGraph";
        final String guidParameterName = "elementGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/open-metadata-store/users/{1}/metadata-elements/{2}/with-anchored-elements";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(elementGUID, guidParameterName, methodName);

        ResultsRequestBody requestBody = new ResultsRequestBody(queryOptions);

        OpenMetadataGraphResponse restResult = restClient.callOpenMetadataGraphPostRESTCall(methodName,
                                                                                            urlTemplate,
                                                                                            requestBody,
                                                                                            serverName,
                                                                                            userId,
                                                                                            elementGUID);

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
     * @param getOptions multiple options to control the query
     *
     * @return related element
     *
     * @throws InvalidParameterException  the unique identifier is null or not known; the relationship type is invalid
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException    there is a problem accessing the metadata store or multiple relationships have been returned
     */
    @Override
    public RelatedMetadataElement getRelatedMetadataElement(String     userId,
                                                            String     elementGUID,
                                                            int        startingAtEnd,
                                                            String     relationshipTypeName,
                                                            GetOptions getOptions) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        final String methodName = "getRelatedMetadataElement";

        QueryOptions queryOptions = new QueryOptions(getOptions);
        RelatedMetadataElementList  relationships = this.getRelatedMetadataElements(userId,
                                                                                     elementGUID,
                                                                                     startingAtEnd,
                                                                                     relationshipTypeName,
                                                                                     queryOptions);

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
                            throw new PropertyServerException(OMFServicesErrorCode.MULTIPLE_RELATIONSHIPS_FOUND.getMessageDefinition(relationshipTypeName, elementGUID),
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
     * @param queryOptions multiple options to control the query
     *
     * @return a list of elements matching the supplied criteria; null means no matching elements in the metadata store.
     * @throws InvalidParameterException one of the search parameters are is invalid
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    @Override
    public  List<OpenMetadataElement> getMetadataElementHistory(String                 userId,
                                                                String                 elementGUID,
                                                                HistoricalQueryOptions queryOptions) throws InvalidParameterException,
                                                                                                            UserNotAuthorizedException,
                                                                                                            PropertyServerException
    {
        final String methodName = "getMetadataElementHistory";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/open-metadata-store/users/{1}/metadata-elements/{2}/history";

        invalidParameterHandler.validateUserId(userId, methodName);

        HistoryRequestBody requestBody = new HistoryRequestBody(queryOptions);

        OpenMetadataElementsResponse restResult = restClient.callOpenMetadataElementsPostRESTCall(methodName,
                                                                                                  urlTemplate,
                                                                                                  requestBody,
                                                                                                  serverName,
                                                                                                  userId,
                                                                                                  elementGUID);

        return restResult.getElementList();
    }


    /**
     * Retrieve the relationships linking to the supplied elements.
     *
     * @param userId caller's userId
     * @param metadataElementAtEnd1GUID unique identifier of the metadata element at end 1 of the relationship
     * @param metadataElementAtEnd2GUID unique identifier of the metadata element at end 2 of the relationship
     * @param relationshipTypeName type name of relationships to follow (or null for all)
     * @param queryOptions multiple options to control the query
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
                                                                          QueryOptions        queryOptions) throws InvalidParameterException,
                                                                                                                   UserNotAuthorizedException,
                                                                                                                   PropertyServerException
    {
        final String methodName            = "getRelatedMetadataElements";
        final String guid1ParameterName    = "metadataElementAtEnd1GUID";

        final String allURLTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/open-metadata-store/users/{1}/metadata-elements/{2}/linked-by-any-type/to-elements/{3}";
        final String specificURLTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/open-metadata-store/users/{1}/metadata-elements/{2}/linked-by-type/{3}/to-elements/{4}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(metadataElementAtEnd1GUID, guid1ParameterName, methodName);

        ResultsRequestBody requestBody = new ResultsRequestBody(queryOptions);

        OpenMetadataRelationshipListResponse restResult;

        if (relationshipTypeName == null)
        {
            restResult = restClient.callOpenMetadataRelationshipListPostRESTCall(methodName,
                                                                                 allURLTemplate,
                                                                                 requestBody,
                                                                                 serverName,
                                                                                 userId,
                                                                                 metadataElementAtEnd1GUID,
                                                                                 metadataElementAtEnd2GUID);
        }
        else
        {
            restResult = restClient.callOpenMetadataRelationshipListPostRESTCall(methodName,
                                                                                 specificURLTemplate,
                                                                                 requestBody,
                                                                                 serverName,
                                                                                 userId,
                                                                                 metadataElementAtEnd1GUID,
                                                                                 relationshipTypeName,
                                                                                 metadataElementAtEnd2GUID);
        }

        return restResult.getRelationshipList();
    }


    /**
     * Return a list of metadata elements that match the supplied criteria.  The results can be returned over many pages.
     *
     * @param userId caller's userId
     * @param searchProperties Optional list of entity property conditions to match.
     * @param matchClassifications Optional list of classifications to match.
     * @param queryOptions multiple options to control the query
     *
     * @return a list of elements matching the supplied criteria; null means no matching elements in the metadata store.
     *
     * @throws InvalidParameterException  one of the search parameters are is invalid
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    @Override
    public List<OpenMetadataElement> findMetadataElements(String                userId,
                                                          SearchProperties      searchProperties,
                                                          SearchClassifications matchClassifications,
                                                          QueryOptions          queryOptions) throws InvalidParameterException,
                                                                                                     UserNotAuthorizedException,
                                                                                                     PropertyServerException
    {
        final String methodName = "findMetadataElements";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/open-metadata-store/users/{1}/metadata-elements/by-search-conditions";

        invalidParameterHandler.validateUserId(userId, methodName);

        FindRequestBody requestBody = new FindRequestBody(queryOptions);

        requestBody.setSearchProperties(searchProperties);
        requestBody.setMatchClassifications(matchClassifications);

        OpenMetadataElementsResponse restResult = restClient.callOpenMetadataElementsPostRESTCall(methodName,
                                                                                                  urlTemplate,
                                                                                                  requestBody,
                                                                                                  serverName,
                                                                                                  userId);

        return restResult.getElementList();
    }


    /**
     * Return a list of relationships that match the requested conditions.  The results can be received as a series of pages.
     *
     * @param userId caller's userId
     * @param relationshipTypeName relationship's type.  Null means all types
     *                             (but may be slow so not recommended).
     * @param searchProperties Optional list of relationship property conditions to match.
     * @param queryOptions multiple options to control the query
     *
     * @return a list of relationships.  Null means no matching relationships.
     *
     * @throws InvalidParameterException  one of the search parameters are is invalid
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    @Override
    public OpenMetadataRelationshipList findRelationshipsBetweenMetadataElements(String           userId,
                                                                                 String           relationshipTypeName,
                                                                                 SearchProperties searchProperties,
                                                                                 QueryOptions     queryOptions) throws InvalidParameterException,
                                                                                                                   UserNotAuthorizedException,
                                                                                                                   PropertyServerException
    {
        final String methodName = "findRelationshipsBetweenMetadataElements";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/open-metadata-store/users/{1}/relationships/by-search-conditions";

        invalidParameterHandler.validateUserId(userId, methodName);

        FindRelationshipRequestBody requestBody = new FindRelationshipRequestBody(queryOptions);

        requestBody.setRelationshipTypeName(relationshipTypeName);
        requestBody.setSearchProperties(searchProperties);

        OpenMetadataRelationshipListResponse restResult = restClient.callOpenMetadataRelationshipListPostRESTCall(methodName,
                                                                                                                  urlTemplate,
                                                                                                                  requestBody,
                                                                                                                  serverName,
                                                                                                                  userId);

        return restResult.getRelationshipList();
    }


    /**
     * Retrieve the relationship using its unique identifier.
     *
     * @param userId caller's userId
     * @param relationshipGUID unique identifier for the relationship
     * @param getOptions multiple options to control the query
     *
     * @return relationship properties
     * @throws InvalidParameterException the unique identifier is null or not known.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    @Override
    public OpenMetadataRelationship getRelationshipByGUID(String     userId,
                                                          String     relationshipGUID,
                                                          GetOptions getOptions) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        final String methodName = "getRelationshipByGUID";
        final String guidParameterName = "relationshipGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/open-metadata-store/users/{1}/relationships/by-guid/{2}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(relationshipGUID, guidParameterName, methodName);

        GetRequestBody requestBody = new GetRequestBody(getOptions);

        OpenMetadataRelationshipResponse restResult = restClient.callOpenMetadataRelationshipPostRESTCall(methodName,
                                                                                                          urlTemplate,
                                                                                                          requestBody,
                                                                                                          serverName,
                                                                                                          userId,
                                                                                                          relationshipGUID);

        return restResult.getElement();
    }


    /**
     * Return each of the versions of a relationship.
     *
     * @param userId caller's userId
     * @param relationshipGUID            unique identifier for the relationship
     * @param queryOptions multiple options to control the query
     *
     * @return a list of elements matching the supplied criteria; null means no matching elements in the metadata store.
     * @throws InvalidParameterException one of the search parameters are is invalid
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    @Override
    public  OpenMetadataRelationshipList getRelationshipHistory(String                 userId,
                                                                String                 relationshipGUID,
                                                                HistoricalQueryOptions queryOptions) throws InvalidParameterException,
                                                                                                        UserNotAuthorizedException,
                                                                                                        PropertyServerException
    {
        final String methodName = "getRelationshipHistory";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/open-metadata-store/users/{1}/relationships/{2}/history";

        invalidParameterHandler.validateUserId(userId, methodName);

        HistoryRequestBody requestBody = new HistoryRequestBody(queryOptions);

        OpenMetadataRelationshipListResponse restResult = restClient.callOpenMetadataRelationshipListPostRESTCall(methodName,
                                                                                                                  urlTemplate,
                                                                                                                  requestBody,
                                                                                                                  serverName,
                                                                                                                  userId,
                                                                                                                  relationshipGUID);

        return restResult.getRelationshipList();
    }



    /**
     * Create a new metadata element in the metadata store.  The type name comes from the open metadata types.
     * The selected type also controls the names and types of the properties that are allowed.
     * This version of the method allows access to advanced features such as multiple states and
     * effectivity dates.
     *
     * @param userId caller's userId
     * @param metadataElementTypeName typeName for the new element
     * @param newElementOptions details of the element to create
     * @param initialClassifications map of classification names to classification properties to include in the entity creation request
     * @param properties properties of the new metadata element
     * @param parentRelationshipProperties properties to include in parent relationship
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException the type name, status or one of the properties is invalid
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public String createMetadataElementInStore(String                            userId,
                                               String metadataElementTypeName,
                                               NewElementOptions                 newElementOptions,
                                               Map<String, NewElementProperties> initialClassifications,
                                               NewElementProperties              properties,
                                               NewElementProperties              parentRelationshipProperties) throws InvalidParameterException,
                                                                                                                      UserNotAuthorizedException,
                                                                                                                      PropertyServerException
    {
        final String methodName               = "createMetadataElementInStore";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/open-metadata-store/users/{1}/metadata-elements";

        invalidParameterHandler.validateUserId(userId, methodName);

        if ((newElementOptions != null) && (newElementOptions.getParentGUID() != null))
        {
            final String parentRelationshipTypeNameParameterName = "parentRelationshipTypeName";

            invalidParameterHandler.validateName(newElementOptions.getParentRelationshipTypeName(), parentRelationshipTypeNameParameterName, methodName);
        }

        NewOpenMetadataElementRequestBody requestBody = new NewOpenMetadataElementRequestBody(newElementOptions);

        requestBody.setTypeName(metadataElementTypeName);
        requestBody.setInitialClassifications(initialClassifications);
        requestBody.setProperties(properties);
        requestBody.setParentRelationshipProperties(parentRelationshipProperties);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
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
     * @param metadataElementTypeName expected type name for the new element
     * @param templateOptions details of the element to create
     * @param templateGUID the unique identifier of the existing asset to copy (this will copy all the attachments such as nested content, schema
     *                     connection etc)
     * @param replacementProperties properties of the new metadata element.  These override the template values
     * @param placeholderProperties property name-to-property value map to replace any placeholder values in the
     *                              template element - and their anchored elements, which are also copied as part of this operation.
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException the type name, status or one of the properties is invalid
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public String createMetadataElementFromTemplate(String               userId,
                                                    String metadataElementTypeName,
                                                    TemplateOptions      templateOptions,
                                                    String               templateGUID,
                                                    ElementProperties    replacementProperties,
                                                    Map<String, String>  placeholderProperties,
                                                    NewElementProperties parentRelationshipProperties) throws InvalidParameterException,
                                                                                                              UserNotAuthorizedException,
                                                                                                              PropertyServerException
    {
        final String methodName                = "createMetadataElementFromTemplate";
        final String templateGUIDParameterName = "templateGUID";

        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/open-metadata-store/users/{1}/metadata-elements/from-template";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(templateGUID, templateGUIDParameterName, methodName);

        if ((templateOptions != null) && (templateOptions.getParentGUID() != null))
        {
            final String parentRelationshipTypeNameParameterName = "parentRelationshipTypeName";

            invalidParameterHandler.validateName(templateOptions.getParentRelationshipTypeName(), parentRelationshipTypeNameParameterName, methodName);
        }

        OpenMetadataTemplateRequestBody requestBody = new OpenMetadataTemplateRequestBody(templateOptions);

        requestBody.setTypeName(metadataElementTypeName);
        requestBody.setTemplateGUID(templateGUID);
        requestBody.setReplacementProperties(replacementProperties);
        requestBody.setPlaceholderPropertyValues(placeholderProperties);
        requestBody.setParentRelationshipProperties(parentRelationshipProperties);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId);

        return restResult.getGUID();
    }


    /**
     * Update the properties of a specific metadata element.  The properties must match the type definition associated with the
     * metadata element when it was created.  However, it is possible to update a few properties, or replace all them by
     * the value used in the replaceProperties flag.
     *
     * @param userId                 caller's userId
     * @param metadataElementGUID    unique identifier of the metadata element to update
     * @param updateOptions provides a structure for the additional options when updating an element.
     * @param properties             new properties for the metadata element
     *
     * @throws InvalidParameterException  either the unique identifier or the properties are invalid in some way
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException    there is a problem with the metadata store
     */
    @Override
    public void updateMetadataElementInStore(String            userId,
                                             String            metadataElementGUID,
                                             UpdateOptions     updateOptions,
                                             ElementProperties properties) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        final String methodName        = "updateMetadataElementInStore";
        final String guidParameterName = "metadataElementGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/open-metadata-store/users/{1}/metadata-elements/{2}/update-properties";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(metadataElementGUID, guidParameterName, methodName);

        UpdatePropertiesRequestBody requestBody = new UpdatePropertiesRequestBody(updateOptions);

        requestBody.setProperties(properties);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
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
     * @param metadataSourceOptions  options to control access to open metadata
     * @param newElementStatus       new status value - or null to leave as is
     *
     * @throws InvalidParameterException  either the unique identifier or the status are invalid in some way
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException    there is a problem with the metadata store
     */
    @Override
    public void updateMetadataElementStatusInStore(String                userId,
                                                   String                metadataElementGUID,
                                                   MetadataSourceOptions metadataSourceOptions,
                                                   ElementStatus         newElementStatus) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        final String methodName        = "updateMetadataElementStatusInStore";
        final String guidParameterName = "metadataElementGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/open-metadata-store/users/{1}/metadata-elements/{2}/update-status";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(metadataElementGUID, guidParameterName, methodName);

        UpdateStatusRequestBody requestBody = new UpdateStatusRequestBody(metadataSourceOptions);

        requestBody.setNewStatus(newElementStatus);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
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
     * @param metadataSourceOptions  options to control access to open metadata
     * @param effectiveFrom          the date when this element is active - null for active now
     * @param effectiveTo            the date when this element becomes inactive - null for active until deleted
     *
     * @throws InvalidParameterException  either the unique identifier or the status are invalid in some way
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException    there is a problem with the metadata store
     */
    @Override
    public void updateMetadataElementEffectivityInStore(String                userId,
                                                        String                metadataElementGUID,
                                                        MetadataSourceOptions metadataSourceOptions,
                                                        Date                  effectiveFrom,
                                                        Date                  effectiveTo) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        final String methodName        = "updateMetadataElementEffectivityInStore";
        final String guidParameterName = "metadataElementGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/open-metadata-store/users/{1}/metadata-elements/{2}/update-effectivity";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(metadataElementGUID, guidParameterName, methodName);

        UpdateEffectivityDatesRequestBody requestBody = new UpdateEffectivityDatesRequestBody(metadataSourceOptions);

        requestBody.setEffectiveFrom(effectiveFrom);
        requestBody.setEffectiveTo(effectiveTo);

        restClient.callGUIDPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        metadataElementGUID);
    }


    /**
     * Delete a specific metadata element.
     *
     * @param userId                 caller's userId
     * @param metadataElementGUID    unique identifier of the metadata element to update
     * @param deleteOptions options for a delete request
     *
     * @throws InvalidParameterException  the unique identifier is null or invalid in some way
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException    there is a problem with the metadata store
     */
    @Override
    public void deleteMetadataElementInStore(String        userId,
                                             String        metadataElementGUID,
                                             DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        final String methodName        = "deleteMetadataElementInStore";
        final String guidParameterName = "metadataElementGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/open-metadata-store/users/{1}/metadata-elements/{2}/delete";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(metadataElementGUID, guidParameterName, methodName);

        OpenMetadataDeleteRequestBody requestBody = new OpenMetadataDeleteRequestBody(deleteOptions);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        metadataElementGUID);
    }


    /**
     * Archive a specific metadata element.
     *
     * @param userId caller's userId
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param deleteOptions description of the archiving process
     *
     * @throws InvalidParameterException the unique identifier is null or invalid in some way
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public void archiveMetadataElementInStore(String                userId,
                                              String                metadataElementGUID,
                                              DeleteOptions         deleteOptions) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        final String methodName        = "archiveMetadataElementInStore";
        final String guidParameterName = "metadataElementGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/open-metadata-store/users/{1}/metadata-elements/{2}/archive";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(metadataElementGUID, guidParameterName, methodName);

        DeleteRequestBody requestBody = new DeleteRequestBody(deleteOptions);

        requestBody.setDeleteMethod(DeleteMethod.ARCHIVE); // Just in case the caller is being funny

        if (requestBody.getArchiveProcess() == null)
        {
            requestBody.setArchiveProcess(serverName);
        }

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
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
     * @param metadataSourceOptions  options to control access to open metadata
     * @param properties             properties to store in the new classification.  These must conform to the valid properties associated with the
     *                               classification name
     *
     * @throws InvalidParameterException  the unique identifier or classification name is null or invalid in some way; properties do not match the
     *                                    valid properties associated with the classification's type definition
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException    there is a problem with the metadata store
     */
    @Override
    public void classifyMetadataElementInStore(String                userId,
                                               String                metadataElementGUID,
                                               String                classificationName,
                                               MetadataSourceOptions metadataSourceOptions,
                                               NewElementProperties  properties) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        final String methodName                  = "classifyMetadataElementInStore";
        final String guidParameterName           = "metadataElementGUID";
        final String classificationParameterName = "classificationName";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/open-metadata-store/users/{1}/metadata-elements/{2}/classifications/{3}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(metadataElementGUID, guidParameterName, methodName);
        invalidParameterHandler.validateName(classificationName, classificationParameterName, methodName);

        NewOpenMetadataClassificationRequestBody requestBody = new NewOpenMetadataClassificationRequestBody(metadataSourceOptions);

        requestBody.setProperties(properties);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
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
     * @param updateOptions provides a structure for the additional options when updating an element.
     * @param properties             new properties for the classification
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
                                                 UpdateOptions     updateOptions,
                                                 ElementProperties properties) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        final String methodName                  = "reclassifyMetadataElementInStore";
        final String guidParameterName           = "metadataElementGUID";
        final String classificationParameterName = "classificationName";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/open-metadata-store/users/{1}/metadata-elements/{2}/classifications/{3}/update-properties";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(metadataElementGUID, guidParameterName, methodName);
        invalidParameterHandler.validateName(classificationName, classificationParameterName, methodName);

        UpdatePropertiesRequestBody requestBody = new UpdatePropertiesRequestBody(updateOptions);

        requestBody.setProperties(properties);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
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
     * @param metadataSourceOptions  options to control access to open metadata
     * @param effectiveFrom          the date when this element is active - null for active now
     * @param effectiveTo            the date when this element becomes inactive - null for active until deleted
     *
     * @throws InvalidParameterException  either the unique identifier or the status are invalid in some way
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException    there is a problem with the metadata store
     */
    @Override
    public void updateClassificationEffectivityInStore(String                userId,
                                                       String                metadataElementGUID,
                                                       String                classificationName,
                                                       MetadataSourceOptions metadataSourceOptions,
                                                       Date                  effectiveFrom,
                                                       Date                  effectiveTo) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException
    {
        final String methodName                  = "updateClassificationEffectivityInStore";
        final String guidParameterName           = "metadataElementGUID";
        final String classificationParameterName = "classificationName";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/open-metadata-store/users/{1}/metadata-elements/{2}/classifications/{3}/update-effectivity";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(metadataElementGUID, guidParameterName, methodName);
        invalidParameterHandler.validateName(classificationName, classificationParameterName, methodName);

        UpdateEffectivityDatesRequestBody requestBody = new UpdateEffectivityDatesRequestBody(metadataSourceOptions);

        requestBody.setEffectiveFrom(effectiveFrom);
        requestBody.setEffectiveTo(effectiveTo);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
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
     * @param metadataSourceOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException  the unique identifier or classification name is null or invalid in some way
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException    there is a problem with the metadata store
     */
    @Override
    public void declassifyMetadataElementInStore(String                userId,
                                                 String                metadataElementGUID,
                                                 String                classificationName,
                                                 MetadataSourceOptions metadataSourceOptions) throws InvalidParameterException,
                                                                                                     UserNotAuthorizedException,
                                                                                                     PropertyServerException
    {
        final String methodName                  = "declassifyMetadataElementInStore";
        final String guidParameterName           = "metadataElementGUID";
        final String classificationParameterName = "classificationName";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/open-metadata-store/users/{1}/metadata-elements/{2}/classifications/{3}/delete";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(metadataElementGUID, guidParameterName, methodName);
        invalidParameterHandler.validateName(classificationName, classificationParameterName, methodName);

        MetadataSourceRequestBody requestBody = new MetadataSourceRequestBody(metadataSourceOptions);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        metadataElementGUID,
                                        classificationName);
    }

    /**
     * Create a relationship between two metadata elements.  It is important to put the right element at each end of the relationship
     * according to the type definition since this will affect how the relationship is interpreted.
     *
     * @param userId caller's userId
     * @param relationshipTypeName name of the type of relationship to create.  This will determine the types of metadata elements that can be
     *                             related and the properties that can be associated with this relationship.
     * @param metadataElement1GUID unique identifier of the metadata element at end 1 of the relationship
     * @param metadataElement2GUID unique identifier of the metadata element at end 2 of the relationship
     * @param metadataSourceOptions  options to control access to open metadata
     * @param properties the properties of the relationship
     *
     * @return unique identifier of the new relationship
     *
     * @throws InvalidParameterException  the unique identifier's of the metadata elements are null or invalid in some way; the properties are
     *                                    not valid for this type of relationship
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException    there is a problem with the metadata store
     */
    @Override
    public String createRelatedElementsInStore(String                userId,
                                               String                relationshipTypeName,
                                               String                metadataElement1GUID,
                                               String                metadataElement2GUID,
                                               MetadataSourceOptions metadataSourceOptions,
                                               NewElementProperties  properties) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        return this.createRelatedElementsInStore(userId,
                                                 relationshipTypeName,
                                                 metadataElement1GUID,
                                                 metadataElement2GUID,
                                                 new MakeAnchorOptions(metadataSourceOptions),
                                                 properties);
    }


    /**
     * Create a relationship between two metadata elements.  It is important to put the right element at each end of the relationship
     * according to the type definition since this will affect how the relationship is interpreted.
     *
     * @param userId caller's userId
     * @param relationshipTypeName name of the type of relationship to create.  This will determine the types of metadata elements that can be
     *                             related and the properties that can be associated with this relationship.
     * @param metadataElement1GUID unique identifier of the metadata element at end 1 of the relationship
     * @param metadataElement2GUID unique identifier of the metadata element at end 2 of the relationship
     * @param makeAnchorOptions  options to control access to open metadata
     * @param properties the properties of the relationship
     *
     * @return unique identifier of the new relationship
     *
     * @throws InvalidParameterException  the unique identifier's of the metadata elements are null or invalid in some way; the properties are
     *                                    not valid for this type of relationship
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException    there is a problem with the metadata store
     */
    @Override
    public String createRelatedElementsInStore(String                userId,
                                               String                relationshipTypeName,
                                               String                metadataElement1GUID,
                                               String                metadataElement2GUID,
                                               MakeAnchorOptions     makeAnchorOptions,
                                               NewElementProperties  properties) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        final String methodName               = "createRelatedElementsInStore";
        final String elementTypeParameterName = "relationshipTypeName";
        final String end1ParameterName        = "metadataElement1GUID";
        final String end2ParameterName        = "metadataElement2GUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/open-metadata-store/users/{1}/related-elements";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(relationshipTypeName, elementTypeParameterName, methodName);
        invalidParameterHandler.validateGUID(metadataElement1GUID, end1ParameterName, methodName);
        invalidParameterHandler.validateGUID(metadataElement2GUID, end2ParameterName, methodName);

        NewRelatedElementsRequestBody requestBody = new NewRelatedElementsRequestBody(makeAnchorOptions);

        requestBody.setTypeName(relationshipTypeName);
        requestBody.setMetadataElement1GUID(metadataElement1GUID);
        requestBody.setMetadataElement2GUID(metadataElement2GUID);
        requestBody.setProperties(properties);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId);

        return restResult.getGUID();
    }


    /**
     * Update the properties associated with a relationship.
     *
     * @param userId caller's userId
     * @param relationshipGUID unique identifier of the relationship to update
     * @param updateOptions provides a structure for the additional options when updating a relationship.
     * @param properties new properties for the relationship
     *
     * @throws InvalidParameterException the unique identifier of the relationship is null or invalid in some way; the properties are
     *                                    not valid for this type of relationship
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public void updateRelationshipInStore(String            userId,
                                          String            relationshipGUID,
                                          UpdateOptions     updateOptions,
                                          ElementProperties properties) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        final String methodName = "updateRelationshipInStore";
        final String guidParameterName = "relationshipGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/open-metadata-store/users/{1}/related-elements/{2}/update-properties";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(relationshipGUID, guidParameterName, methodName);

        UpdatePropertiesRequestBody requestBody = new UpdatePropertiesRequestBody(updateOptions);

        requestBody.setProperties(properties);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        relationshipGUID);
    }


    /**
     * Update all relationships of a particular type between two metadata elements.
     *
     * @param userId caller's userId
     * @param relationshipTypeName name of the type of relationship to create.  This will determine the types of metadata elements that can be
     *                             related and the properties that can be associated with this relationship.
     * @param metadataElement1GUID unique identifier of the metadata element at end 1 of the relationship
     * @param metadataElement2GUID unique identifier of the metadata element at end 2 of the relationship
     * @param updateOptions provides a structure for the additional options when updating a relationship.
     * @param properties new properties for the relationship
     *
     * @throws InvalidParameterException the unique identifier of the relationship is null or invalid in some way
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public void updateRelatedElementsInStore(String            userId,
                                             String            relationshipTypeName,
                                             String            metadataElement1GUID,
                                             String            metadataElement2GUID,
                                             UpdateOptions     updateOptions,
                                             ElementProperties properties) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        final String methodName = "updateRelatedElementsInStore";
        final String end1GUIDParameterName = "metadataElement1GUID";
        final String end2GUIDParameterName = "metadataElement2GUID";
        final String relationshipTypeNameParameterName = "relationshipTypeName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(relationshipTypeName, relationshipTypeNameParameterName, methodName);
        invalidParameterHandler.validateGUID(metadataElement1GUID, end1GUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(metadataElement2GUID, end2GUIDParameterName, methodName);

        QueryOptions queryOptions = new QueryOptions(updateOptions);

        OpenMetadataRelationshipList relationshipList = this.getMetadataElementRelationships(userId,
                                                                                             metadataElement1GUID,
                                                                                             metadataElement2GUID,
                                                                                             relationshipTypeName,
                                                                                             queryOptions);

        if ((relationshipList != null) && (relationshipList.getElementList() != null))
        {
            for (OpenMetadataRelationship relationship : relationshipList.getElementList())
            {
                if (relationship != null)
                {
                    if ((metadataElement1GUID.equals(relationship.getElementGUIDAtEnd1())) && (metadataElement2GUID.equals(relationship.getElementGUIDAtEnd2())))
                    {
                        this.updateRelationshipInStore(userId,
                                                       relationship.getRelationshipGUID(),
                                                       updateOptions,
                                                       properties);
                    }
                }
            }
        }
    }


    /**
     * Update the effectivity dates of a specific relationship between metadata elements.
     * The effectivity dates control the visibility of the classification through specific APIs.
     *
     * @param userId caller's userId
     * @param relationshipGUID unique identifier of the relationship to update
     * @param metadataSourceOptions  options to control access to open metadata
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     *
     * @throws InvalidParameterException either the unique identifier or the status are invalid in some way
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public  void updateRelationshipEffectivityInStore(String                userId,
                                                      String                relationshipGUID,
                                                      MetadataSourceOptions metadataSourceOptions,
                                                      Date                  effectiveFrom,
                                                      Date                  effectiveTo) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException
    {
        final String methodName = "updateRelatedElementsEffectivityInStore";
        final String guidParameterName = "relationshipGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/open-metadata-store/users/{1}/related-elements/{2}/update-effectivity";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(relationshipGUID, guidParameterName, methodName);

        UpdateEffectivityDatesRequestBody requestBody = new UpdateEffectivityDatesRequestBody(metadataSourceOptions);

        requestBody.setEffectiveFrom(effectiveFrom);
        requestBody.setEffectiveTo(effectiveTo);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        relationshipGUID);
    }


    /**
     * Delete a relationship between two metadata elements.
     *
     * @param userId caller's userId
     * @param relationshipGUID unique identifier of the relationship to delete
     * @param deleteOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException the unique identifier of the relationship is null or invalid in some way
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public void deleteRelationshipInStore(String        userId,
                                          String        relationshipGUID,
                                          DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        final String methodName = "deleteRelationshipInStore";
        final String guidParameterName = "relationshipGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/open-metadata-store/users/{1}/related-elements/{2}/delete";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(relationshipGUID, guidParameterName, methodName);

        OpenMetadataDeleteRequestBody requestBody = new OpenMetadataDeleteRequestBody(deleteOptions);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        relationshipGUID);
    }


    /**
     * Delete all relationships of a particular type between two metadata elements.
     *
     * @param userId caller's userId
     * @param relationshipTypeName name of the type of relationship to create.  This will determine the types of metadata elements that can be
     *                             related and the properties that can be associated with this relationship.
     * @param metadataElement1GUID unique identifier of the metadata element at end 1 of the relationship
     * @param metadataElement2GUID unique identifier of the metadata element at end 2 of the relationship
     * @param deleteOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException the unique identifier of the relationship is null or invalid in some way
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public void detachRelatedElementsInStore(String        userId,
                                             String        relationshipTypeName,
                                             String        metadataElement1GUID,
                                             String        metadataElement2GUID,
                                             DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        final String methodName = "detachRelatedElementsInStore";
        final String end1GUIDParameterName = "metadataElement1GUID";
        final String end2GUIDParameterName = "metadataElement2GUID";
        final String relationshipTypeNameParameterName = "relationshipTypeName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(relationshipTypeName, relationshipTypeNameParameterName, methodName);
        invalidParameterHandler.validateGUID(metadataElement1GUID, end1GUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(metadataElement2GUID, end2GUIDParameterName, methodName);

        QueryOptions queryOptions = new QueryOptions(deleteOptions);

        OpenMetadataRelationshipList relationshipList = this.getMetadataElementRelationships(userId,
                                                                                             metadataElement1GUID,
                                                                                             metadataElement2GUID,
                                                                                             relationshipTypeName,
                                                                                             queryOptions);

        if ((relationshipList != null) && (relationshipList.getElementList() != null))
        {
            for (OpenMetadataRelationship relationship : relationshipList.getElementList())
            {
                if (relationship != null)
                {
                    if ((metadataElement1GUID.equals(relationship.getElementGUIDAtEnd1())) && (metadataElement2GUID.equals(relationship.getElementGUIDAtEnd2())))
                    {
                        this.deleteRelationshipInStore(userId,
                                                       relationship.getRelationshipGUID(),
                                                       deleteOptions);
                    }
                }
            }
        }
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
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/open-metadata-store/users/{1}/incident-reports";

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
                                                                  userId);

        return restResult.getGUID();
    }


    /**
     * Create an action request for someone to work on.
     *
     * @param userId caller's userId
     * @param openMetadataTypeName type of action to create
     * @param properties properties of the action
     * @param actionSourceGUID unique identifier of the source of the action
     * @param actionCauseGUIDs unique identifiers of the cause for the action to be raised
     * @param assignToGUID unique identifier of the Actor element for the recipient
     * @param actionTargets the list of elements that should be acted upon
     *
     * @return unique identifier of new action element
     *
     * @throws InvalidParameterException either todoQualifiedName or assignedTo are null or not recognized
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException there is a problem connecting to (or inside) the metadata store
     */
    @Override
    public String createActorAction(String                userId,
                                    String                openMetadataTypeName,
                                    NewElementProperties  properties,
                                    String                actionSourceGUID,
                                    List<String>          actionCauseGUIDs,
                                    String                assignToGUID,
                                    List<NewActionTarget> actionTargets) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        return this.createAction(userId,
                                 openMetadataTypeName,
                                 properties,
                                 actionSourceGUID,
                                 actionCauseGUIDs,
                                 assignToGUID,
                                 OpenMetadataType.ASSIGNMENT_SCOPE_RELATIONSHIP.typeName,
                                 actionTargets);
    }


    /**
     * Create an entry in a note log.
     *
     * @param userId caller's userId
     * @param openMetadataTypeName type of action to create
     * @param properties properties of the action
     * @param actionSourceGUID unique identifier of the source of the action
     * @param actionCauseGUIDs unique identifiers of the cause for the action to be raised
     * @param noteLogGUID unique identifier of the note log
     * @param actionTargets the list of elements that should be acted upon
     *
     * @return unique identifier of new action element
     *
     * @throws InvalidParameterException either todoQualifiedName or assignedTo are null or not recognized
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException there is a problem connecting to (or inside) the metadata store
     */
    @Override
    public String createNoteLogEntry(String                userId,
                                     String                openMetadataTypeName,
                                     NewElementProperties  properties,
                                     String                actionSourceGUID,
                                     List<String>          actionCauseGUIDs,
                                     String                noteLogGUID,
                                     List<NewActionTarget> actionTargets) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        return this.createAction(userId,
                                 openMetadataTypeName,
                                 properties,
                                 actionSourceGUID,
                                 actionCauseGUIDs,
                                 noteLogGUID,
                                 OpenMetadataType.ATTACHED_NOTE_LOG_ENTRY_RELATIONSHIP.typeName,
                                 actionTargets);
    }



    /**
     * Create an action request for someone to work on.
     *
     * @param userId caller's userId
     * @param openMetadataTypeName type of action to create
     * @param properties properties of the action
     * @param actionSourceGUID unique identifier of the source of the action
     * @param actionCauseGUIDs unique identifiers of the cause for the action to be raised
     * @param assignToGUID unique identifier of the Actor element for the recipient
     * @param assignToRelationshipTypeName typeName of the relationship
     * @param actionTargets the list of elements that should be acted upon
     *
     * @return unique identifier of new action element
     *
     * @throws InvalidParameterException either todoQualifiedName or assignedTo are null or not recognized
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException there is a problem connecting to (or inside) the metadata store
     */
    private String createAction(String                userId,
                                String                openMetadataTypeName,
                                NewElementProperties  properties,
                                String                actionSourceGUID,
                                List<String>          actionCauseGUIDs,
                                String                assignToGUID,
                                String                assignToRelationshipTypeName,
                                List<NewActionTarget> actionTargets) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        final String methodName = "createAction";
        final String propertiesParameterName = "properties";

        propertyHelper.validateObject(properties, propertiesParameterName, methodName);

        /*
         * Set up the API options
         */
        MetadataSourceOptions metadataSourceOptions = new MetadataSourceOptions();
        metadataSourceOptions.setEffectiveTime(new Date());
        metadataSourceOptions.setForLineage(true);

        /*
         * Create the action entity
         */

        NewElementOptions newElementOptions = new NewElementOptions(metadataSourceOptions);

        newElementOptions.setInitialStatus(ElementStatus.ACTIVE);

        if (actionSourceGUID != null)
        {
            newElementOptions.setIsOwnAnchor(false);
            newElementOptions.setAnchorGUID(actionSourceGUID);
            newElementOptions.setParentAtEnd1(true);
            newElementOptions.setParentGUID(actionSourceGUID);
            newElementOptions.setParentRelationshipTypeName(OpenMetadataType.ACTION_REQUESTER_RELATIONSHIP.typeName);
        }
        else
        {
            newElementOptions.setIsOwnAnchor(true);
        }

        String actionGUID = this.createMetadataElementInStore(userId,
                                                              openMetadataTypeName,
                                                              newElementOptions,
                                                              null,
                                                              properties,
                                                              null);

        if (actionGUID != null)
        {
            if (actionTargets != null)
            {
                /*
                 * Link the action to the items to work on
                 */
                for (NewActionTarget actionTarget : actionTargets)
                {
                    if ((actionTarget != null) && (actionTarget.getActionTargetGUID() != null))
                    {
                        ElementProperties relationshipProperties = propertyHelper.addStringProperty(null,
                                                                                                    OpenMetadataProperty.ACTION_TARGET_NAME.name,
                                                                                                    actionTarget.getActionTargetName());

                        this.createRelatedElementsInStore(userId,
                                                          OpenMetadataType.ACTION_TARGET_RELATIONSHIP.typeName,
                                                          actionGUID,
                                                          actionTarget.getActionTargetGUID(),
                                                          metadataSourceOptions,
                                                          new NewElementProperties(relationshipProperties));
                    }
                }
            }

            if (assignToGUID != null)
            {
                /*
                 * Link the action and the person assigned to complete the work
                 */
                this.createRelatedElementsInStore(userId,
                                                  assignToRelationshipTypeName,
                                                  assignToGUID,
                                                  actionGUID,
                                                  metadataSourceOptions,
                                                  new NewElementProperties(propertyHelper.addStringProperty(null,
                                                                                                            OpenMetadataProperty.ASSIGNMENT_TYPE.name,
                                                                                                            AssignmentType.CONTRIBUTOR.getName())));
            }

            if (actionCauseGUIDs != null)
            {
                for (String actionCauseGUID : actionCauseGUIDs)
                {
                    if (actionCauseGUID != null)
                    {
                        /*
                         * Link the action and its cause.
                         */
                        this.createRelatedElementsInStore(userId,
                                                          OpenMetadataType.ACTIONS_RELATIONSHIP.typeName,
                                                          actionCauseGUID,
                                                          actionGUID,
                                                          metadataSourceOptions,
                                                          null);
                    }
                }
            }
        }

        return actionGUID;
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
         * Set up the API options
         */
        MetadataSourceOptions metadataSourceOptions = new MetadataSourceOptions();
        metadataSourceOptions.setEffectiveTime(new Date());
        metadataSourceOptions.setForLineage(true);


        /*
         * Create the to do entity
         */
        ElementProperties properties = propertyHelper.addStringProperty(null,
                                                                        OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                        qualifiedName);

        properties = propertyHelper.addStringProperty(properties, OpenMetadataProperty.DISPLAY_NAME.name, title);
        properties = propertyHelper.addStringProperty(properties, OpenMetadataProperty.DESCRIPTION.name, instructions);
        properties = propertyHelper.addStringProperty(properties, OpenMetadataProperty.CATEGORY.name, category);
        properties = propertyHelper.addDateProperty(properties, OpenMetadataProperty.DUE_TIME.name, dueDate);
        properties = propertyHelper.addIntProperty(properties, OpenMetadataProperty.PRIORITY.name, priority);
        properties = propertyHelper.addEnumProperty(properties,
                                                    OpenMetadataProperty.ACTIVITY_STATUS.name,
                                                    ActivityStatus.getOpenTypeName(),
                                                    ActivityStatus.REQUESTED.getName());

        NewElementOptions newElementOptions = new NewElementOptions(metadataSourceOptions);

        newElementOptions.setInitialStatus(ElementStatus.ACTIVE);
        newElementOptions.setIsOwnAnchor(true);
        newElementOptions.setParentAtEnd1(true);
        newElementOptions.setParentGUID(assignToGUID);
        newElementOptions.setParentRelationshipTypeName(OpenMetadataType.ASSIGNMENT_SCOPE_RELATIONSHIP.typeName);

        String toDoGUID = this.createMetadataElementInStore(userId,
                                                            OpenMetadataType.TO_DO.typeName,
                                                            newElementOptions,
                                                            null,
                                                            new NewElementProperties(properties),
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
                                                          metadataSourceOptions,
                                                          new NewElementProperties(relationshipProperties));
                    }
                }
            }

            if (sponsorGUID != null)
            {
                /*
                 * Link the "to do" and the sponsor
                 */
                this.createRelatedElementsInStore(userId,
                                                  OpenMetadataType.ASSIGNMENT_SCOPE_RELATIONSHIP.typeName,
                                                  sponsorGUID,
                                                  toDoGUID,
                                                  metadataSourceOptions,
                                                  new NewElementProperties(propertyHelper.addStringProperty(null,
                                                                                                            OpenMetadataProperty.ASSIGNMENT_TYPE.name,
                                                                                                            AssignmentType.SPONSOR.getName())));
            }

            if (originatorGUID != null)
            {
                /*
                 * Link the "to do" and the originator
                 */
                this.createRelatedElementsInStore(userId,
                                                  OpenMetadataType.ACTION_REQUESTER_RELATIONSHIP.typeName,
                                                  originatorGUID,
                                                  toDoGUID,
                                                  metadataSourceOptions,
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
             * Set up the API options
             */
            MetadataSourceOptions metadataSourceOptions = new MetadataSourceOptions();
            metadataSourceOptions.setEffectiveTime(new Date());
            metadataSourceOptions.setForLineage(true);

            /*
             * Create the to do entity
             */
            ElementProperties properties = propertyHelper.addStringProperty(null,
                                                                            OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                            contextEventProperties.getQualifiedName());

            properties = propertyHelper.addStringProperty(properties, OpenMetadataProperty.DISPLAY_NAME.name, contextEventProperties.getDisplayName());
            properties = propertyHelper.addStringProperty(properties, OpenMetadataProperty.DESCRIPTION.name, contextEventProperties.getDescription());
            properties = propertyHelper.addStringProperty(properties, OpenMetadataProperty.CATEGORY.name, contextEventProperties.getCategory());
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

            NewElementOptions newElementOptions = new NewElementOptions(metadataSourceOptions);
            newElementOptions.setInitialStatus(ElementStatus.ACTIVE);
            newElementOptions.setAnchorGUID(anchorGUID);
            newElementOptions.setIsOwnAnchor((anchorGUID == null));

            NewElementProperties newElementProperties = new NewElementProperties(properties);
            newElementProperties.setEffectiveFrom(contextEventProperties.getEffectiveFrom());
            newElementProperties.setEffectiveTo(contextEventProperties.getEffectiveTo());

            String contextEventGUID = this.createMetadataElementInStore(userId,
                                                                        OpenMetadataType.CONTEXT_EVENT.typeName,
                                                                        newElementOptions,
                                                                        null,
                                                                        newElementProperties,
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
                                ElementProperties elementProperties = relationshipBuilder.getElementProperties(suppliedRelationshipProperties);

                                NewElementProperties relationshipProperties = new NewElementProperties(elementProperties);
                                relationshipProperties.setEffectiveFrom(suppliedRelationshipProperties.getEffectiveFrom());
                                relationshipProperties.setEffectiveTo(suppliedRelationshipProperties.getEffectiveTo());

                                this.createRelatedElementsInStore(userId,
                                                                  OpenMetadataType.DEPENDENT_CONTEXT_EVENT_RELATIONSHIP.typeName,
                                                                  guid,
                                                                  contextEventGUID,
                                                                  metadataSourceOptions,
                                                                  relationshipProperties);
                            }
                            else
                            {
                                this.createRelatedElementsInStore(userId,
                                                                  OpenMetadataType.DEPENDENT_CONTEXT_EVENT_RELATIONSHIP.typeName,
                                                                  guid,
                                                                  contextEventGUID,
                                                                  metadataSourceOptions,
                                                                  null);
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
                                ElementProperties elementProperties = relationshipBuilder.getElementProperties(suppliedRelationshipProperties);

                                NewElementProperties relationshipProperties = new NewElementProperties(elementProperties);
                                relationshipProperties.setEffectiveFrom(suppliedRelationshipProperties.getEffectiveFrom());
                                relationshipProperties.setEffectiveTo(suppliedRelationshipProperties.getEffectiveTo());

                                this.createRelatedElementsInStore(userId,
                                                                  OpenMetadataType.DEPENDENT_CONTEXT_EVENT_RELATIONSHIP.typeName,
                                                                  contextEventGUID,
                                                                  guid,
                                                                  metadataSourceOptions,
                                                                  relationshipProperties);
                            }
                            else
                            {
                                this.createRelatedElementsInStore(userId,
                                                                  OpenMetadataType.DEPENDENT_CONTEXT_EVENT_RELATIONSHIP.typeName,
                                                                  contextEventGUID,
                                                                  guid,
                                                                  metadataSourceOptions,
                                                                  null);
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
                                ElementProperties elementProperties = relationshipBuilder.getElementProperties(suppliedRelationshipProperties);

                                NewElementProperties relationshipProperties = new NewElementProperties(elementProperties);
                                relationshipProperties.setEffectiveFrom(suppliedRelationshipProperties.getEffectiveFrom());
                                relationshipProperties.setEffectiveTo(suppliedRelationshipProperties.getEffectiveTo());

                                this.createRelatedElementsInStore(userId,
                                                                  OpenMetadataType.RELATED_CONTEXT_EVENT_RELATIONSHIP.typeName,
                                                                  guid,
                                                                  contextEventGUID,
                                                                  metadataSourceOptions,
                                                                  relationshipProperties);
                            }
                            else
                            {
                                this.createRelatedElementsInStore(userId,
                                                                  OpenMetadataType.RELATED_CONTEXT_EVENT_RELATIONSHIP.typeName,
                                                                  guid,
                                                                  contextEventGUID,
                                                                  metadataSourceOptions,
                                                                  null);
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
                                ElementProperties elementProperties = relationshipBuilder.getElementProperties(suppliedRelationshipProperties);

                                NewElementProperties relationshipProperties = new NewElementProperties(elementProperties);
                                relationshipProperties.setEffectiveFrom(suppliedRelationshipProperties.getEffectiveFrom());
                                relationshipProperties.setEffectiveTo(suppliedRelationshipProperties.getEffectiveTo());

                                this.createRelatedElementsInStore(userId,
                                                                  OpenMetadataType.CONTEXT_EVENT_IMPACT_RELATIONSHIP.typeName,
                                                                  guid,
                                                                  contextEventGUID,
                                                                  metadataSourceOptions,
                                                                  relationshipProperties);
                            }
                            else
                            {
                                this.createRelatedElementsInStore(userId,
                                                                  OpenMetadataType.CONTEXT_EVENT_IMPACT_RELATIONSHIP.typeName,
                                                                  guid,
                                                                  contextEventGUID,
                                                                  metadataSourceOptions,
                                                                  null);
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

                            NewElementProperties relationshipProperties = relationshipBuilder.getNewElementProperties(suppliedRelationshipProperties);
                            relationshipProperties.setEffectiveFrom(suppliedRelationshipProperties.getEffectiveFrom());
                            relationshipProperties.setEffectiveTo(suppliedRelationshipProperties.getEffectiveTo());

                            this.createRelatedElementsInStore(userId,
                                                              OpenMetadataType.CONTEXT_EVENT_FOR_TIMELINE_EFFECTS_RELATIONSHIP.typeName,
                                                              guid,
                                                              contextEventGUID,
                                                              metadataSourceOptions,
                                                              relationshipProperties);
                        }
                    }
                }

                if (contextEventEvidenceGUIDs != null)
                {
                    for (String guid : contextEventEvidenceGUIDs.keySet())
                    {
                        if (guid != null)
                        {
                            RelationshipProperties suppliedRelationshipProperties = contextEventEvidenceGUIDs.get(guid);

                            NewElementProperties relationshipProperties = relationshipBuilder.getNewElementProperties(suppliedRelationshipProperties);
                            relationshipProperties.setEffectiveFrom(suppliedRelationshipProperties.getEffectiveFrom());
                            relationshipProperties.setEffectiveTo(suppliedRelationshipProperties.getEffectiveTo());

                            this.createRelatedElementsInStore(userId,
                                                              OpenMetadataType.CONTEXT_EVENT_EVIDENCE_RELATIONSHIP.typeName,
                                                              contextEventGUID,
                                                              guid,
                                                              metadataSourceOptions,
                                                              relationshipProperties);
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
                               TranslationDetailProperties translationDetail) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        final String methodName = "setTranslation";
        final String elementGUIDParameterName = "elementGUID";
        final String translationDetailParameterName = "translationDetail";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/open-metadata-store/users/{1}/multi-language/set-translation/{2}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(elementGUID, elementGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(translationDetail, translationDetailParameterName, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        translationDetail,
                                        serverName,
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
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/open-metadata-store/users/{1}/multi-language/clear-translation/{2}?language={3}&locale={4}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(elementGUID, elementGUIDParameterName, methodName);
        invalidParameterHandler.validateName(language, languageParameterName, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        new NullRequestBody(),
                                        serverName,
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
    public TranslationDetailProperties getTranslation(String userId,
                                                      String elementGUID,
                                                      String language,
                                                      String locale) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        final String methodName = "getTranslation";
        final String elementGUIDParameterName = "elementGUID";
        final String languageParameterName = "language";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/open-metadata-store/users/{1}/multi-language/get-translation/{2}?language={3}&locale={4}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(elementGUID, elementGUIDParameterName, methodName);
        invalidParameterHandler.validateName(language, languageParameterName, methodName);

        TranslationDetailResponse response = restClient.callTranslationDetailGetRESTCall(methodName,
                                                                                         urlTemplate,
                                                                                         serverName,
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
    public List<TranslationDetailProperties> getTranslations(String userId,
                                                             String elementGUID,
                                                             int    startFrom,
                                                             int    pageSize) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        final String methodName = "getTranslations";
        final String elementGUIDParameterName = "elementGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/open-metadata-store/users/{1}/multi-language/get-translations/{2}?startFrom={3}&pageSize={4}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(elementGUID, elementGUIDParameterName, methodName);

        TranslationListResponse response = restClient.callTranslationListGetRESTCall(methodName,
                                                                                     urlTemplate,
                                                                                     new NullRequestBody(),
                                                                                     serverName,
                                                                                     userId,
                                                                                     elementGUID,
                                                                                     startFrom,
                                                                                     pageSize);

        return response.getElements();
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
                                        ValidMetadataValueProperties validMetadataValue) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException
    {
        final String methodName = "setUpValidMetadataValue";
        final String propertyNameParameterName = "propertyName";
        final String propertiesParameterName = "validMetadataValue";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/open-metadata-store/users/{1}/valid-metadata-values/setup-value/{2}?typeName={3}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(propertyName, propertyNameParameterName, methodName);
        invalidParameterHandler.validateObject(validMetadataValue, propertiesParameterName, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        validMetadataValue,
                                        serverName,
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
                                          ValidMetadataValueProperties validMetadataValue) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        final String methodName = "setUpValidMetadataMapName";
        final String propertyNameParameterName = "propertyName";
        final String propertiesParameterName = "validMetadataValue";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/open-metadata-store/users/{1}/valid-metadata-values/setup-map-name/{2}?typeName={3}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(propertyName, propertyNameParameterName, methodName);
        invalidParameterHandler.validateObject(validMetadataValue, propertiesParameterName, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        validMetadataValue,
                                        serverName,
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
                                           ValidMetadataValueProperties validMetadataValue) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   PropertyServerException
    {
        final String methodName = "setUpValidMetadataMapValue";
        final String propertyNameParameterName = "propertyName";
        final String mapNameParameterName = "mapName";
        final String propertiesParameterName = "validMetadataValue";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/open-metadata-store/users/{1}/valid-metadata-values/setup-map-value/{2}/{3}?typeName={4}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(propertyName, propertyNameParameterName, methodName);
        invalidParameterHandler.validateName(mapName, mapNameParameterName, methodName);
        invalidParameterHandler.validateObject(validMetadataValue, propertiesParameterName, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        validMetadataValue,
                                        serverName,
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
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/open-metadata-store/users/{1}/valid-metadata-values/clear-value/{2}?preferredValue={3}&typeName={4}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(propertyName, propertyNameParameterName, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        new NullRequestBody(),
                                        serverName,
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
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/open-metadata-store/users/{1}/valid-metadata-values/clear-map-name/{2}?preferredValue={3}&typeName={4}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(propertyName, propertyNameParameterName, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        new NullRequestBody(),
                                        serverName,
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
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/open-metadata-store/users/{1}/valid-metadata-values/clear-map-value/{2}/{3}?preferredValue={4}&typeName={5}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(propertyName, propertyNameParameterName, methodName);
        invalidParameterHandler.validateName(mapName, mapNameParameterName, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        new NullRequestBody(),
                                        serverName,
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
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/open-metadata-store/users/{1}/valid-metadata-values/validate-value/{2}?actualValue={3}&typeName={4}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(propertyName, propertyNameParameterName, methodName);

        BooleanResponse response = restClient.callBooleanGetRESTCall(methodName,
                                                                     urlTemplate,
                                                                     serverName,
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
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/open-metadata-store/users/{1}/valid-metadata-values/validate-map-name/{2}?actualValue={3}&typeName={4}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(propertyName, propertyNameParameterName, methodName);

        BooleanResponse response = restClient.callBooleanGetRESTCall(methodName,
                                                                     urlTemplate,
                                                                     serverName,
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
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/open-metadata-store/users/{1}/valid-metadata-values/validate-map-value/{2}/{3}?actualValue={4}&typeName={5}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(propertyName, propertyNameParameterName, methodName);
        invalidParameterHandler.validateName(mapName, mapNameParameterName, methodName);

        BooleanResponse response = restClient.callBooleanGetRESTCall(methodName,
                                                                     urlTemplate,
                                                                     serverName,
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
    public ValidMetadataValueProperties getValidMetadataValue(String userId,
                                                              String typeName,
                                                              String propertyName,
                                                              String preferredValue) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        final String methodName = "getValidMetadataValue";
        final String propertyNameParameterName = "propertyName";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/open-metadata-store/users/{1}/valid-metadata-values/get-value/{2}?preferredValue={3}&typeName={4}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(propertyName, propertyNameParameterName, methodName);

        ValidMetadataValueResponse response = restClient.callValidMetadataValueGetRESTCall(methodName,
                                                                                           urlTemplate,
                                                                                           serverName,
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
    public ValidMetadataValueProperties getValidMetadataMapName(String userId,
                                                                String typeName,
                                                                String propertyName,
                                                                String preferredValue) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        final String methodName = "getValidMetadataMapName";
        final String propertyNameParameterName = "propertyName";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/open-metadata-store/users/{1}/valid-metadata-values/get-map-name/{2}?preferredValue={3}&typeName={4}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(propertyName, propertyNameParameterName, methodName);

        ValidMetadataValueResponse response = restClient.callValidMetadataValueGetRESTCall(methodName,
                                                                                           urlTemplate,
                                                                                           serverName,
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
    public ValidMetadataValueProperties getValidMetadataMapValue(String userId,
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
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/open-metadata-store/users/{1}/valid-metadata-values/get-map-value/{2}/{3}?preferredValue={4}&typeName={5}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(propertyName, propertyNameParameterName, methodName);
        invalidParameterHandler.validateName(mapName, mapNameParameterName, methodName);

        ValidMetadataValueResponse response = restClient.callValidMetadataValueGetRESTCall(methodName,
                                                                                           urlTemplate,
                                                                                           serverName,
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
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/open-metadata-store/users/{1}/valid-metadata-values/get-valid-metadata-values/{2}?typeName={3}&startFrom={4}&pageSize={5}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(propertyName, propertyNameParameterName, methodName);

        ValidMetadataValueDetailListResponse response = restClient.callValidMetadataValueDetailListGetRESTCall(methodName,
                                                                                                               urlTemplate,
                                                                                                               serverName,
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
    public List<ValidMetadataValueProperties> getConsistentMetadataValues(String userId,
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
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/open-metadata-store/users/{1}/valid-metadata-values/{2}/consistent-metadata-values?typeName={3}&mapName={4}&preferredValue={5}&startFrom={6}&pageSize={7}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(propertyName, propertyNameParameterName, methodName);

        ValidMetadataValueListResponse response = restClient.callValidMetadataValueListGetRESTCall(methodName,
                                                                                                   urlTemplate,
                                                                                                   serverName,
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
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/open-metadata-store/users/{1}/valid-metadata-values/{2}/consistent-metadata-values/{3}?typeName1={4}&typeName2={5}&preferredValue1={6}&preferredValue2={7}&mapName1={8}&mapName2={9}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(propertyName1, propertyName1ParameterName, methodName);
        invalidParameterHandler.validateObject(preferredValue1, preferredValue1ParameterName, methodName);
        invalidParameterHandler.validateName(propertyName2, propertyName2ParameterName, methodName);
        invalidParameterHandler.validateObject(preferredValue2, preferredValue2ParameterName, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        new NullRequestBody(),
                                        serverName,
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
    @Override
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
                                                                                       new QueryOptions());

        if ((refDataElements != null) && (refDataElements.getElementList() != null))
        {
            for (RelatedMetadataElement refDataElement : refDataElements.getElementList())
            {
                if (refDataElement != null)
                {
                    String propertyType = propertyHelper.getStringProperty(AccessServiceDescription.OMF_METADATA_MANAGEMENT.getServiceName(), 
                                                                           OpenMetadataProperty.PROPERTY_NAME.name,
                                                                           refDataElement.getRelationshipProperties(),
                                                                           methodName);
                    if (propertyType != null)
                    {
                        Map<String, String> additionalProperties = propertyHelper.getStringMapFromProperty(AccessServiceDescription.OMF_METADATA_MANAGEMENT.getServiceName(),
                                                                                                           OpenMetadataProperty.ADDITIONAL_PROPERTIES.name,
                                                                                                           refDataElement.getElement().getElementProperties(),
                                                                                                           methodName);

                        if (additionalProperties == null)
                        {
                            additionalProperties = new HashMap<>();
                        }

                        additionalProperties.put(propertyType + "Name",
                                                 propertyHelper.getStringProperty(AccessServiceDescription.OMF_METADATA_MANAGEMENT.getServiceName(),
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
        invalidParameterHandler.validateGUID(externalIdentifierProperties.getIdentifier(), externalIdentifierParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/open-metadata-store/users/{1}/metadata-elements/{2}/{3}/external-identifiers/add?forLineage={4}&forDuplicateProcessing={5}";

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
        invalidParameterHandler.validateGUID(externalIdentifierProperties.getIdentifier(), externalIdentifierParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/open-metadata-store/users/{1}/metadata-elements/{2}/{3}/external-identifiers/update?forLineage={4}&forDuplicateProcessing={5}";

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

        final String urlTemplate =serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/open-metadata-store/users/{1}/metadata-elements/{2}/{3}/external-identifiers/remove?forLineage={4}&forDuplicateProcessing={5}";

        UpdateMetadataCorrelatorsRequestBody requestBody                    = new UpdateMetadataCorrelatorsRequestBody();
        MetadataCorrelationProperties        metadataCorrelationProperties  = new MetadataCorrelationProperties();

        metadataCorrelationProperties.setExternalScopeGUID(externalScopeGUID);
        metadataCorrelationProperties.setExternalScopeName(externalScopeName);
        metadataCorrelationProperties.setIdentifier(externalIdentifier);

        requestBody.setMetadataCorrelationProperties(metadataCorrelationProperties);
        requestBody.setEffectiveTime(effectiveTime);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
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

        final String urlTemplate =serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/open-metadata-store/users/{1}/external-scope/{2}/remove?forLineage={3}&forDuplicateProcessing={4}";

        EffectiveTimeRequestBody requestBody  = new EffectiveTimeRequestBody();

        requestBody.setEffectiveTime(effectiveTime);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
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

        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/open-metadata-store/users/{1}/metadata-elements/{2}/{3}/synchronized";

        MetadataCorrelationProperties requestBody = new MetadataCorrelationProperties();

        requestBody.setExternalScopeGUID(externalScopeGUID);
        requestBody.setExternalScopeName(externalScopeName);
        requestBody.setIdentifier(externalIdentifier);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
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

        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/open-metadata-store/users/{1}/external-identifiers/open-metadata-elements?startFrom={2}&pageSize={3}";

        UpdateMetadataCorrelatorsRequestBody requestBody                    = new UpdateMetadataCorrelatorsRequestBody();
        MetadataCorrelationProperties        metadataCorrelationProperties  = new MetadataCorrelationProperties();

        metadataCorrelationProperties.setExternalScopeGUID(externalScopeGUID);
        metadataCorrelationProperties.setExternalScopeName(externalScopeName);
        metadataCorrelationProperties.setIdentifier(externalIdentifier);

        requestBody.setMetadataCorrelationProperties(metadataCorrelationProperties);
        requestBody.setEffectiveTime(effectiveTime);

        ElementHeadersResponse restResult = restClient.callElementHeadersPostRESTCall(methodName,
                                                                                      urlTemplate,
                                                                                      requestBody,
                                                                                      serverName,
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

            final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/open-metadata-store/users/{1}/metadata-elements/{2}/{3}/external-identifiers/validate";

            BooleanResponse restResult = restClient.callBooleanPostRESTCall(methodName,
                                                                            urlTemplate,
                                                                            getExternalIdEffectiveTimeQueryRequestBody(externalScopeGUID, externalScopeName, null),
                                                                            serverName,
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

        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/open-metadata-store/users/{1}/metadata-elements/{2}/{3}/external-identifiers";

        MetadataCorrelationHeadersResponse restResult = restClient.callCorrelationHeadersPostRESTCall(methodName,
                                                                                                      urlTemplate,
                                                                                                      getExternalIdEffectiveTimeQueryRequestBody(externalScopeGUID, externalScopeName, null),
                                                                                                      serverName,
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
                                                                                      OpenMetadataType.REFERENCEABLE_FACET_RELATIONSHIP.typeName,
                                                                                      new QueryOptions());
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
    protected ExternalIdEffectiveTimeQueryRequestBody getExternalIdEffectiveTimeQueryRequestBody(String externalScopeGUID,
                                                                                                 String externalScopeName,
                                                                                                 Date   effectiveTime)
    {
        ExternalIdEffectiveTimeQueryRequestBody requestBody = new ExternalIdEffectiveTimeQueryRequestBody();

        if (externalScopeGUID != null)
        {
            requestBody.setExternalScopeGUID(externalScopeGUID);
            requestBody.setExternalScopeName(externalScopeName);
        }

        requestBody.setEffectiveTime(effectiveTime);

        return requestBody;
    }


}
