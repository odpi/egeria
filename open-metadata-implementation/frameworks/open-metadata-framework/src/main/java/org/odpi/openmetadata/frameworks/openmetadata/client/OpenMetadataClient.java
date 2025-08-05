/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.client;

import org.odpi.openmetadata.frameworks.openmetadata.api.*;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementHeader;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ElementStatus;
import org.odpi.openmetadata.frameworks.openmetadata.properties.*;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;

import java.util.*;
import java.util.regex.Pattern;

/**
 * OpenMetadataClient provides access to metadata elements stored in the metadata repositories.  It is implemented by a
 * metadata repository provider. In Egeria, this class is implemented in the GAF Metadata Management running in the
 * Metadata Access Server OMAG Server.
 */
public abstract class OpenMetadataClient implements OpenMetadataTypesInterface,
                                                    MetadataElementInterface,
                                                    StewardshipRequestInterface,
                                                    MultiLanguageInterface,
                                                    ValidMetadataValuesInterface,
                                                    ExternalIdentifiersInterface
{
    protected final String serverName;               /* Initialized in constructor */
    protected final String serverPlatformURLRoot;    /* Initialized in constructor */

    protected PropertyHelper propertyHelper = new PropertyHelper();


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     */
    public OpenMetadataClient(String serverName,
                              String serverPlatformURLRoot)
    {
        this.serverName = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;
    }


    /**
     * Return the name of the server that this client is connected to.
     *
     * @return string name
     */
    public String getServerName()
    {
        return serverName;
    }


    /**
     * Get the maximum paging size.
     *
     * @return maxPagingSize new value
     */
    public abstract int getMaxPagingSize();


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
    public abstract OpenMetadataTypeDefGallery getAllTypes(String userId) throws InvalidParameterException,
                                                                                 PropertyServerException,
                                                                                 UserNotAuthorizedException;

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
    public abstract List<OpenMetadataTypeDef> findTypeDefsByCategory(String                      userId,
                                                                     OpenMetadataTypeDefCategory category) throws InvalidParameterException,
                                                                                                                  PropertyServerException,
                                                                                                                  UserNotAuthorizedException;


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
    public abstract List<OpenMetadataAttributeTypeDef> findAttributeTypeDefsByCategory(String                               userId,
                                                                                       OpenMetadataAttributeTypeDefCategory category) throws InvalidParameterException,
                                                                                                                                             PropertyServerException,
                                                                                                                                             UserNotAuthorizedException;


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
    public abstract List<OpenMetadataTypeDef> findTypesByExternalId(String userId,
                                                                    String standard,
                                                                    String organization,
                                                                    String identifier) throws InvalidParameterException,
                                                                                              PropertyServerException,
                                                                                              UserNotAuthorizedException;


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
    public abstract List<OpenMetadataTypeDef> getSubTypes(String userId,
                                                          String typeName) throws InvalidParameterException,
                                                                                  PropertyServerException,
                                                                                  UserNotAuthorizedException;


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
    public abstract OpenMetadataTypeDef getTypeDefByGUID(String userId,
                                                         String guid) throws InvalidParameterException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException;


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
    public abstract OpenMetadataAttributeTypeDef getAttributeTypeDefByGUID(String userId,
                                                                           String guid) throws InvalidParameterException,
                                                                                               PropertyServerException,
                                                                                               UserNotAuthorizedException;


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
    public abstract OpenMetadataTypeDef getTypeDefByName(String userId,
                                                         String name) throws InvalidParameterException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException;


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
    public abstract OpenMetadataAttributeTypeDef getAttributeTypeDefByName(String userId,
                                                                           String name) throws InvalidParameterException,
                                                                                               PropertyServerException,
                                                                                               UserNotAuthorizedException;


    /**
     * Retrieve the metadata element using its unique identifier.
     *
     * @param userId caller's userId
     * @param elementGUID unique identifier for the metadata element
     * @param getOptions multiple options to control the query
     *
     * @return metadata element properties
     * @throws InvalidParameterException the unique identifier is null or not known.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    @Override
    public abstract OpenMetadataElement getMetadataElementByGUID(String     userId,
                                                                 String     elementGUID,
                                                                 GetOptions getOptions) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException;


    /**
     * Retrieve the metadata element using its unique name (typically the qualified name).
     *
     * @param userId caller's userId
     * @param uniqueName unique name for the metadata element
     * @param uniquePropertyName name of property name to test in the open metadata element - if null "qualifiedName" is used
     * @param getOptions multiple options to control the query
     *
     * @return metadata element properties or null if not found
     * @throws InvalidParameterException the unique identifier is null.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    @Override
    public abstract OpenMetadataElement getMetadataElementByUniqueName(String     userId,
                                                                       String     uniqueName,
                                                                       String     uniquePropertyName,
                                                                       GetOptions getOptions) throws InvalidParameterException,
                                                                                                     UserNotAuthorizedException,
                                                                                                     PropertyServerException;


    /**
     * Retrieve the metadata element using its unique name (typically the qualified name) and the DELETED status.
     * This method assumes all effective dates, and forLineage and forDuplicateProcessing is set false,
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

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateMandatoryName(uniqueName, nameParameterName, methodName);

        List<String> propertyNames = new ArrayList<>();

        if (uniquePropertyName != null)
        {
            propertyNames.add(uniquePropertyName);
        }
        else
        {
            propertyNames.add(defaultPropertyName);
        }

        QueryOptions queryOptions = new QueryOptions();

        queryOptions.setLimitResultsByStatus(Collections.singletonList(ElementStatus.DELETED));

        List<OpenMetadataElement> matchingElements = this.findMetadataElements(userId,
                                                                               propertyHelper.getSearchPropertiesByName(propertyNames, uniqueName, PropertyComparisonOperator.EQ),
                                                                               null,
                                                                               queryOptions);

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
     * @param userId caller's userId
     * @param uniqueName unique name for the metadata element
     * @param uniquePropertyName name of property name to test in the open metadata element - if null "qualifiedName" is used
     * @param getOptions multiple options to control the query
     *
     * @return metadata element unique identifier (guid)
     * @throws InvalidParameterException the unique name is null or not known.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    @Override
    public abstract String getMetadataElementGUIDByUniqueName(String     userId,
                                                              String     uniqueName,
                                                              String     uniquePropertyName,
                                                              GetOptions getOptions) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException;


    /**
     * Retrieve metadata elements with any of the supplied list of property names similar to the supplied value.
     *
     * @param userId                     caller's userId
     * @param propertyName               property name to match against
     * @param propertyValue              value to match against
     * @param queryOptions multiple options to control the query
     *
     * @return a list of elements matching the supplied criteria; null means no matching elements in the metadata store.
     *
     * @throws InvalidParameterException  one of the search parameters are is invalid
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    public List<OpenMetadataElement> getMetadataElementsByPropertyValue(String                userId,
                                                                        String                propertyName,
                                                                        String                propertyValue,
                                                                        QueryOptions          queryOptions) throws InvalidParameterException,
                                                                                                                   UserNotAuthorizedException,
                                                                                                                   PropertyServerException
    {
        List<String> propertyNames = List.of(propertyName);

        return this.findMetadataElements(userId,
                                         propertyHelper.getSearchPropertiesByName(propertyNames, propertyValue, PropertyComparisonOperator.EQ),
                                         null,
                                         queryOptions);
    }


    /**
     * Retrieve metadata elements with any of the supplied list of property names similar to the supplied value.
     *
     * @param userId                     caller's userId
     * @param propertyName               property name to match against
     * @param propertyValue              value to match against
     * @param queryOptions               multiple options to control the query
     *
     * @return a list of elements matching the supplied criteria; null means no matching elements in the metadata store.
     *
     * @throws InvalidParameterException  one of the search parameters are is invalid
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    public List<OpenMetadataElement> findMetadataElementsByPropertyValue(String                userId,
                                                                         String                propertyName,
                                                                         String                propertyValue,
                                                                         QueryOptions          queryOptions) throws InvalidParameterException,
                                                                                                                    UserNotAuthorizedException,
                                                                                                                    PropertyServerException
    {
        List<String> propertyNames = List.of(propertyName);

        return this.findMetadataElements(userId,
                                         propertyHelper.getSearchPropertiesByName(propertyNames, propertyValue, PropertyComparisonOperator.LIKE),
                                         null,
                                         queryOptions);
    }



    /**
     * Retrieve metadata elements with any of the supplied list of property names similar to the supplied value.
     *
     * @param userId                     caller's userId
     * @param propertyNames              list of property names to match against
     * @param propertyValue              value to match against
     * @param queryOptions multiple options to control the query
     *
     * @return a list of elements matching the supplied criteria; null means no matching elements in the metadata store.
     *
     * @throws InvalidParameterException  one of the search parameters are is invalid
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    public List<OpenMetadataElement> getMetadataElementsByPropertyValue(String                userId,
                                                                        List<String>          propertyNames,
                                                                        String                propertyValue,
                                                                        QueryOptions          queryOptions) throws InvalidParameterException,
                                                                                                                   UserNotAuthorizedException,
                                                                                                                   PropertyServerException
    {
        return this.findMetadataElements(userId,
                                         propertyHelper.getSearchPropertiesByName(propertyNames, propertyValue, PropertyComparisonOperator.EQ),
                                         null,
                                         queryOptions);
    }


    /**
     * Retrieve metadata elements with any of the supplied list of property names set (exactly) to the supplied value.
     *
     * @param userId                     caller's userId
     * @param propertyNames              list of property names to match against
     * @param propertyValue              value to match against
     * @param queryOptions multiple options to control the query

     *
     * @return a list of elements matching the supplied criteria; null means no matching elements in the metadata store.
     *
     * @throws InvalidParameterException  one of the search parameters are is invalid
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    public List<OpenMetadataElement> findMetadataElementsByPropertyValue(String       userId,
                                                                         List<String> propertyNames,
                                                                         String       propertyValue,
                                                                         QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                           UserNotAuthorizedException,
                                                                                                           PropertyServerException
    {
        return this.findMetadataElements(userId,
                                         propertyHelper.getSearchPropertiesByName(propertyNames, propertyValue, PropertyComparisonOperator.LIKE),
                                         null,
                                         queryOptions);
    }


    /**
     * Retrieve metadata elements with any of the supplied list of property names similar to the supplied value.
     *
     * @param userId                     caller's userId
     * @param classificationName         name of the classification
     * @param classificationPropertyName  property name to match against
     * @param classificationPropertyValue value to match against
     * @param queryOptions                multiple options to control the query
     *
     * @return a list of elements matching the supplied criteria; null means no matching elements in the metadata store.
     *
     * @throws InvalidParameterException  one of the search parameters are is invalid
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    public List<OpenMetadataElement> getMetadataElementsByClassificationPropertyValue(String                userId,
                                                                                      String                classificationName,
                                                                                      String                classificationPropertyName,
                                                                                      String                classificationPropertyValue,
                                                                                      QueryOptions          queryOptions) throws InvalidParameterException,
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
                                         null,
                                         searchClassifications,
                                         queryOptions);
    }


    /**
     * Retrieve metadata elements with any of the supplied list of property names similar to the supplied value.
     *
     * @param userId                     caller's userId
     * @param classificationName         name of the classification
     * @param queryOptions multiple options to control the query
     *
     * @return a list of elements matching the supplied criteria; null means no matching elements in the metadata store.
     *
     * @throws InvalidParameterException  one of the search parameters are is invalid
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    public List<OpenMetadataElement> getMetadataElementsByClassification(String                userId,
                                                                         String                classificationName,
                                                                         QueryOptions          queryOptions) throws InvalidParameterException,
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
                                         null,
                                         searchClassifications,
                                         queryOptions);
    }


    /**
     * Retrieve metadata elements with any of the supplied list of property names similar to the supplied value.
     *
     * @param userId                     caller's userId
     * @param classificationName         name of the classification
     * @param classificationPropertyName  property name to match against
     * @param classificationPropertyValue value to match against
     * @param queryOptions multiple options to control the query
     *
     * @return a list of elements matching the supplied criteria; null means no matching elements in the metadata store.
     *
     * @throws InvalidParameterException  one of the search parameters are is invalid
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    public List<OpenMetadataElement> getMetadataElementsByClassificationPropertyValue(String                userId,
                                                                                      String                classificationName,
                                                                                      String                classificationPropertyName,
                                                                                      int                   classificationPropertyValue,
                                                                                      QueryOptions          queryOptions) throws InvalidParameterException,
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
                                         null,
                                         searchClassifications,
                                         queryOptions);
    }



    /**
     * Retrieve metadata elements with any of the supplied list of property names similar to the supplied value.
     *
     * @param userId                     caller's userId
     * @param classificationName         name of the classification
     * @param classificationPropertyName  property name to match against
     * @param classificationPropertyValue value to match against
     * @param queryOptions multiple options to control the query
     *
     * @return a list of elements matching the supplied criteria; null means no matching elements in the metadata store.
     *
     * @throws InvalidParameterException  one of the search parameters are is invalid
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    public List<OpenMetadataElement> findMetadataElementsByClassificationPropertyValue(String                userId,
                                                                                       String                classificationName,
                                                                                       String                classificationPropertyName,
                                                                                       String                classificationPropertyValue,
                                                                                       QueryOptions          queryOptions) throws InvalidParameterException,
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
                                         null,
                                         searchClassifications,
                                         queryOptions);
    }


    /**
     * Retrieve metadata elements with the requested classification containing any of the supplied list of property
     * names exactly matching the supplied value.
     *
     * @param userId                     caller's userId
     * @param classificationName         name of the classification
     * @param classificationPropertyNames property name to match against
     * @param classificationPropertyValue value to match against
     * @param queryOptions multiple options to control the query
     *
     * @return a list of elements matching the supplied criteria; null means no matching elements in the metadata store.
     *
     * @throws InvalidParameterException  one of the search parameters are is invalid
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    public List<OpenMetadataElement> getMetadataElementsByClassificationPropertyValue(String       userId,
                                                                                      String       classificationName,
                                                                                      List<String> classificationPropertyNames,
                                                                                      String       classificationPropertyValue,
                                                                                      QueryOptions queryOptions) throws InvalidParameterException,
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
                                         null,
                                         searchClassifications,
                                         queryOptions);
    }


    /**
     * Retrieve metadata elements with the requested classification containing any of the supplied list of property
     * names exactly matching the supplied value.
     *
     * @param userId                     caller's userId
     * @param classificationName         name of the classification
     * @param classificationPropertyNames property name to match against
     * @param classificationPropertyValue value to match against
     * @param queryOptions multiple options to control the query
     *
     * @return a list of elements matching the supplied criteria; null means no matching elements in the metadata store.
     *
     * @throws InvalidParameterException  one of the search parameters are is invalid
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    public List<OpenMetadataElement> findMetadataElementsByClassificationPropertyValue(String                userId,
                                                                                       String                classificationName,
                                                                                       List<String>          classificationPropertyNames,
                                                                                       String                classificationPropertyValue,
                                                                                       QueryOptions          queryOptions) throws InvalidParameterException,
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
                                         null,
                                         searchClassifications,
                                         queryOptions);
    }


    /**
     * Retrieve the metadata elements of the requested type that contain the requested string.
     *
     * @param userId caller's userId
     * @param searchString name to retrieve
     * @param searchOptions multiple options to control the query
     *
     * @return list of matching metadata elements (or null if no elements match the name)
     * @throws InvalidParameterException the qualified name is null
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    @Override
    public abstract List<OpenMetadataElement> findMetadataElementsWithString(String        userId,
                                                                             String        searchString,
                                                                             SearchOptions searchOptions) throws InvalidParameterException,
                                                                                                                 UserNotAuthorizedException,
                                                                                                                 PropertyServerException;


    /**
     * Return a list of elements with the requested search string in their (display, resource)name, qualified name,
     * title, text, summary, identifier or description.  The search string is interpreted as a regular expression (RegEx).
     * The breadth of the search is determined by the supplied anchorGUID.
     *
     * @param userId caller's userId
     * @param searchString name to retrieve
     * @param anchorGUID unique identifier of anchor
     * @param queryOptions multiple options to control the query
     *
     * @return list of matching metadata elements (or null if no elements match the name)
     * @throws InvalidParameterException the qualified name is null
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    @Override
    public abstract AnchorSearchMatches findElementsForAnchor(String       userId,
                                                              String       searchString,
                                                              String       anchorGUID,
                                                              QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException;


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
     * @throws InvalidParameterException the qualified name is null
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    @Override
    public abstract List<AnchorSearchMatches> findElementsInAnchorDomain(String        userId,
                                                                         String        searchString,
                                                                         String        anchorDomainName,
                                                                         SearchOptions searchOptions) throws InvalidParameterException,
                                                                                                             UserNotAuthorizedException,
                                                                                                             PropertyServerException;


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
     * @throws InvalidParameterException the qualified name is null
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    @Override
    public abstract List<AnchorSearchMatches> findElementsInAnchorScope(String              userId,
                                                                        String              searchString,
                                                                        String              anchorScopeGUID,
                                                                        SearchOptions        searchOptions) throws InvalidParameterException,
                                                                                                                 UserNotAuthorizedException,
                                                                                                                 PropertyServerException;


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
     * @throws InvalidParameterException the unique identifier is null or not known; the relationship type is invalid
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    @Override
    public abstract RelatedMetadataElementList getRelatedMetadataElements(String       userId,
                                                                          String       elementGUID,
                                                                          int          startingAtEnd,
                                                                          String       relationshipTypeName,
                                                                          QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                            UserNotAuthorizedException,
                                                                                                            PropertyServerException;


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
    public abstract OpenMetadataElementGraph getAnchoredElementsGraph(String       userId,
                                                                      String       elementGUID,
                                                                      QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                        UserNotAuthorizedException,
                                                                                                        PropertyServerException;


    /**
     * Retrieve the metadata element connected to the supplied element for a relationship type that only allows one
     * relationship to be attached.
     *
     * @param userId                 caller's userId
     * @param elementGUID            unique identifier for the starting metadata element
     * @param startingAtEnd          indicates which end to retrieve from (0 is "either end"; 1 is end1; 2 is end 2)
     * @param relationshipTypeName   type name of relationships to follow (or null for all)
     * @param getOptions multiple options to control the query
     * @return list of related elements
     *
     * @throws InvalidParameterException  the unique identifier is null or not known; the relationship type is invalid
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException    there is a problem accessing the metadata store or multiple relationships have been returned
     */
    @Override
    public abstract  RelatedMetadataElement getRelatedMetadataElement(String     userId,
                                                                      String     elementGUID,
                                                                      int        startingAtEnd,
                                                                      String     relationshipTypeName,
                                                                      GetOptions getOptions) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException;

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
    public  abstract List<OpenMetadataElement> getMetadataElementHistory(String                 userId,
                                                                         String                 elementGUID,
                                                                         HistoricalQueryOptions queryOptions) throws InvalidParameterException,
                                                                                                                     UserNotAuthorizedException,
                                                                                                                     PropertyServerException;


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
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    @Override
    public abstract OpenMetadataRelationshipList getMetadataElementRelationships(String              userId,
                                                                                 String              metadataElementAtEnd1GUID,
                                                                                 String              metadataElementAtEnd2GUID,
                                                                                 String              relationshipTypeName,
                                                                                 QueryOptions        queryOptions) throws InvalidParameterException,
                                                                                                                          UserNotAuthorizedException,
                                                                                                                          PropertyServerException;

    /**
     * Return a list of metadata elements that match the supplied criteria.  The results can be returned over many pages.
     *
     * @param userId caller's userId
     * @param searchProperties Optional list of entity property conditions to match.
     * @param matchClassifications Optional list of classifications to match.
     * @param queryOptions multiple options to control the query
     *
     * @return a list of elements matching the supplied criteria; null means no matching elements in the metadata store.
     * @throws InvalidParameterException one of the search parameters are is invalid
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    @Override
    public  abstract List<OpenMetadataElement> findMetadataElements(String                userId,
                                                                    SearchProperties      searchProperties,
                                                                    SearchClassifications matchClassifications,
                                                                    QueryOptions          queryOptions) throws InvalidParameterException,
                                                                                                               UserNotAuthorizedException,
                                                                                                               PropertyServerException;


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
     * @throws InvalidParameterException one of the search parameters are is invalid
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    @Override
    public  abstract OpenMetadataRelationshipList findRelationshipsBetweenMetadataElements(String           userId,
                                                                                           String           relationshipTypeName,
                                                                                           SearchProperties searchProperties,
                                                                                           QueryOptions     queryOptions) throws InvalidParameterException,
                                                                                                                                 UserNotAuthorizedException,
                                                                                                                                 PropertyServerException;


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
    public  abstract OpenMetadataRelationship getRelationshipByGUID(String     userId,
                                                                    String     relationshipGUID,
                                                                    GetOptions getOptions) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException;


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
    public  abstract OpenMetadataRelationshipList getRelationshipHistory(String                 userId,
                                                                         String                 relationshipGUID,
                                                                         HistoricalQueryOptions queryOptions) throws InvalidParameterException,
                                                                                                                 UserNotAuthorizedException,
                                                                                                                 PropertyServerException;


    /**
     * Create a new metadata element in the metadata store.  The type name comes from the open metadata types.
     * The selected type also controls the names and types of the properties that are allowed.
     * This version of the method allows access to advanced features such as multiple states and
     * effectivity dates.
     *
     * @param userId caller's userId
     * @param metadataElementTypeName type name of the new metadata element
     * @param initialStatus initial status of the metadata element
     * @param properties properties of the new metadata element
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException the type name, status or one of the properties is invalid
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public String createMetadataElementInStore(String               userId,
                                               String               metadataElementTypeName,
                                               ElementStatus        initialStatus,
                                               NewElementProperties properties) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        NewElementOptions newElementOptions = new NewElementOptions();

        newElementOptions.setInitialStatus(initialStatus);

        return this.createMetadataElementInStore(userId,
                                                 metadataElementTypeName,
                                                 newElementOptions,
                                                 null,
                                                 properties,
                                                 null);
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
     * @param properties properties of the new metadata element
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException the type name, status or one of the properties is invalid
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public String createMetadataElementInStore(String               userId,
                                               String               externalSourceGUID,
                                               String               externalSourceName,
                                               String               metadataElementTypeName,
                                               ElementStatus        initialStatus,
                                               NewElementProperties properties) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        NewElementOptions newElementOptions = new NewElementOptions();

        newElementOptions.setExternalSourceGUID(externalSourceGUID);
        newElementOptions.setExternalSourceName(externalSourceName);
        newElementOptions.setInitialStatus(initialStatus);

        return this.createMetadataElementInStore(userId,
                                                 metadataElementTypeName,
                                                 newElementOptions,
                                                 null,
                                                 properties,
                                                 null);
    }


    /**
     * Create a new metadata element in the metadata store.  The type name comes from the open metadata types.
     * The selected type also controls the names and types of the properties that are allowed.
     * This version of the method allows access to advanced features such as multiple states and
     * effectivity dates.
     *
     * @param userId caller's userId
     * @param metadataElementTypeName metadataElementTypeName
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
    public abstract String createMetadataElementInStore(String                            userId,
                                                        String                            metadataElementTypeName,
                                                        NewElementOptions                 newElementOptions,
                                                        Map<String, NewElementProperties> initialClassifications,
                                                        NewElementProperties              properties,
                                                        NewElementProperties              parentRelationshipProperties) throws InvalidParameterException,
                                                                                                                               UserNotAuthorizedException,
                                                                                                                               PropertyServerException;

    /**
     * Create a new metadata element in the metadata store using the template identified by the templateGUID.
     * The type name comes from the open metadata types.
     * The selected type also controls the names and types of the properties that are allowed.
     * The template and any similar anchored objects are
     * copied in this process.
     *
     * @param userId caller's userId
     * @param metadataElementTypeName expected type name for the new element
     * @param templateOptions details of the element to create
     * @param templateGUID the unique identifier of the existing asset to copy (this will copy all the attachments such as nested content, schema
     *                     connection etc)
     * @param replacementProperties properties of the new metadata element.  These override the template values
     * @param placeholderProperties property name-to-property value map to replace any placeholder values in the
     *                              template element - and their anchored elements, which are also copied as part of this operation.
     * @param parentRelationshipProperties properties to include in parent relationship
      *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException the type name, status or one of the properties is invalid
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public abstract String createMetadataElementFromTemplate(String               userId,
                                                             String               metadataElementTypeName,
                                                             TemplateOptions      templateOptions,
                                                             String               templateGUID,
                                                             ElementProperties    replacementProperties,
                                                             Map<String, String>  placeholderProperties,
                                                             NewElementProperties parentRelationshipProperties) throws InvalidParameterException,
                                                                                                                      UserNotAuthorizedException,
                                                                                                                      PropertyServerException;


    /**
     * Update the properties of a specific metadata element.  The properties must match the type definition associated with the
     * metadata element when it was created.  However, it is possible to update a few properties, or replace all them by
     * the value used in the replaceProperties flag.
     *
     * @param userId caller's userId
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param updateOptions provides a structure for the additional options when updating an element.
     * @param properties new properties for the metadata element
     *
     * @throws InvalidParameterException either the unique identifier or the properties are invalid in some way
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public abstract void updateMetadataElementInStore(String            userId,
                                                      String            metadataElementGUID,
                                                      UpdateOptions     updateOptions,
                                                      ElementProperties properties) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException;

    /**
     * Update the status of specific metadata element. The new status must match a status value that is defined for the element's type
     * assigned when it was created.
     *
     * @param userId caller's userId
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param metadataSourceOptions  options to control access to open metadata
     * @param newElementStatus new status value - or null to leave as is
     *
     * @throws InvalidParameterException either the unique identifier or the status are invalid in some way
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public abstract void updateMetadataElementStatusInStore(String                userId,
                                                            String                metadataElementGUID,
                                                            MetadataSourceOptions metadataSourceOptions,
                                                            ElementStatus         newElementStatus) throws InvalidParameterException,
                                                                                                           UserNotAuthorizedException,
                                                                                                           PropertyServerException;


    /**
     * Update the effectivity dates control the visibility of the element through specific APIs.
     *
     * @param userId caller's userId
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param metadataSourceOptions  options to control access to open metadata
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     *
     * @throws InvalidParameterException either the unique identifier or the status are invalid in some way
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public abstract void updateMetadataElementEffectivityInStore(String                userId,
                                                                 String                metadataElementGUID,
                                                                 MetadataSourceOptions metadataSourceOptions,
                                                                 Date                  effectiveFrom,
                                                                 Date                  effectiveTo) throws InvalidParameterException,
                                                                                                           UserNotAuthorizedException,
                                                                                                           PropertyServerException;


    /**
     * Delete a specific metadata element.
     *
     * @param userId caller's userId
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param deleteOptions options for a delete request
     *
     * @throws InvalidParameterException the unique identifier is null or invalid in some way
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public abstract void deleteMetadataElementInStore(String        userId,
                                                      String        metadataElementGUID,
                                                      DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException;


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
    public abstract void archiveMetadataElementInStore(String        userId,
                                                       String        metadataElementGUID,
                                                       DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException;


    /**
     * Add a new classification to the metadata element.  Note that only one classification with the same name can be attached to
     * a metadata element.
     *
     * @param userId caller's userId
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param classificationName name of the classification to add (if the classification is already present then use reclassify)
     * @param metadataSourceOptions  options to control access to open metadata
     * @param properties properties to store in the new classification.  These must conform to the valid properties associated with the
     *                   classification name
     *
     * @throws InvalidParameterException the unique identifier or classification name is null or invalid in some way; properties do not match the
     *                                   valid properties associated with the classification's type definition
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public abstract void classifyMetadataElementInStore(String                userId,
                                                        String                metadataElementGUID,
                                                        String                classificationName,
                                                        MetadataSourceOptions metadataSourceOptions,
                                                        NewElementProperties  properties) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException;


    /**
     * Update the properties of a classification that is currently attached to a specific metadata element.
     *
     * @param userId caller's userId
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param classificationName unique name of the classification to update
     * @param updateOptions provides a structure for the additional options when updating a classification.
     * @param properties new properties for the classification
     *
     * @throws InvalidParameterException the unique identifier or classification name is null or invalid in some way; properties do not match the
     *                                   valid properties associated with the classification's type definition
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public abstract void reclassifyMetadataElementInStore(String            userId,
                                                          String            metadataElementGUID,
                                                          String            classificationName,
                                                          UpdateOptions     updateOptions,
                                                          ElementProperties properties) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException;


    /**
     * Update the effectivity dates of a specific classification attached to a metadata element.
     * The effectivity dates control the visibility of the classification through specific APIs.
     *
     * @param userId caller's userId
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param classificationName unique name of the classification to update
     * @param metadataSourceOptions  options to control access to open metadata
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     *
     * @throws InvalidParameterException either the unique identifier or the status are invalid in some way
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public abstract void updateClassificationEffectivityInStore(String                userId,
                                                                String                metadataElementGUID,
                                                                String                classificationName,
                                                                MetadataSourceOptions metadataSourceOptions,
                                                                Date                  effectiveFrom,
                                                                Date                  effectiveTo) throws InvalidParameterException,
                                                                                                          UserNotAuthorizedException,
                                                                                                          PropertyServerException;


    /**
     * Remove the named classification from a specific metadata element.
     *
     * @param userId caller's userId
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param classificationName unique name of the classification to remove
     * @param metadataSourceOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException the unique identifier or classification name is null or invalid in some way
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public abstract void declassifyMetadataElementInStore(String                userId,
                                                          String                metadataElementGUID,
                                                          String                classificationName,
                                                          MetadataSourceOptions metadataSourceOptions) throws InvalidParameterException,
                                                                                                              UserNotAuthorizedException,
                                                                                                              PropertyServerException;


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
     * @throws InvalidParameterException the unique identifier's of the metadata elements are null or invalid in some way; the properties are
     *                                    not valid for this type of relationship
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public abstract String createRelatedElementsInStore(String                userId,
                                                        String                relationshipTypeName,
                                                        String                metadataElement1GUID,
                                                        String                metadataElement2GUID,
                                                        MakeAnchorOptions     makeAnchorOptions,
                                                        NewElementProperties  properties) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException;


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
     * @throws InvalidParameterException the unique identifier's of the metadata elements are null or invalid in some way; the properties are
     *                                    not valid for this type of relationship
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation
     * @throws PropertyServerException there is a problem with the metadata store
     */
    @Override
    public abstract String createRelatedElementsInStore(String                userId,
                                                        String                relationshipTypeName,
                                                        String                metadataElement1GUID,
                                                        String                metadataElement2GUID,
                                                        MetadataSourceOptions metadataSourceOptions,
                                                        NewElementProperties  properties) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException;

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
    public abstract void updateRelationshipInStore(String            userId,
                                                   String            relationshipGUID,
                                                   UpdateOptions     updateOptions,
                                                   ElementProperties properties) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException;



    /**
     * Delete all relationships of a particular type between two metadata elements.
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
    public abstract void updateRelatedElementsInStore(String            userId,
                                                      String            relationshipTypeName,
                                                      String            metadataElement1GUID,
                                                      String            metadataElement2GUID,
                                                      UpdateOptions     updateOptions,
                                                      ElementProperties properties) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException;


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
    public abstract void updateRelationshipEffectivityInStore(String                userId,
                                                              String                relationshipGUID,
                                                              MetadataSourceOptions metadataSourceOptions,
                                                              Date                  effectiveFrom,
                                                              Date                  effectiveTo) throws InvalidParameterException,
                                                                                                        UserNotAuthorizedException,
                                                                                                        PropertyServerException;


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
    public abstract void deleteRelationshipInStore(String        userId,
                                                   String        relationshipGUID,
                                                   DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException;


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
    public abstract void detachRelatedElementsInStore(String        userId,
                                                      String        relationshipTypeName,
                                                      String        metadataElement1GUID,
                                                      String        metadataElement2GUID,
                                                      DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException;

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
    public abstract void addExternalIdentifier(String                       userId,
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
                                                                                                  PropertyServerException;


    /**
     * Update the description of a specific external identifier.
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
    public abstract void updateExternalIdentifier(String                       userId,
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
                                                                                                     PropertyServerException;



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
    public abstract void removeExternalIdentifier(String  userId,
                                                  String  externalScopeGUID,
                                                  String  externalScopeName,
                                                  String  openMetadataElementGUID,
                                                  String  openMetadataElementTypeName,
                                                  String  externalIdentifier,
                                                  boolean forLineage,
                                                  boolean forDuplicateProcessing,
                                                  Date    effectiveTime) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException;



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
    public abstract void confirmSynchronization(String userId,
                                                String externalScopeGUID,
                                                String externalScopeName,
                                                String openMetadataElementGUID,
                                                String openMetadataElementTypeName,
                                                String externalIdentifier) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException;



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
    public abstract List<ElementHeader> getElementsForExternalIdentifier(String  userId,
                                                                         String  externalScopeGUID,
                                                                         String  externalScopeName,
                                                                         String  externalIdentifier,
                                                                         int     startFrom,
                                                                         int     pageSize,
                                                                         boolean forLineage,
                                                                         boolean forDuplicateProcessing,
                                                                         Date    effectiveTime) throws InvalidParameterException,
                                                                                                       UserNotAuthorizedException,
                                                                                                       PropertyServerException;


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
    public abstract boolean validateExternalIdentifier(String  userId,
                                                       String  externalScopeGUID,
                                                       String  externalScopeName,
                                                       String  openMetadataElementGUID,
                                                       String  openMetadataElementTypeName,
                                                       String  elementExternalIdentifier,
                                                       boolean forLineage,
                                                       boolean forDuplicateProcessing,
                                                       Date    effectiveTime) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException;



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
    public abstract List<MetadataCorrelationHeader> getExternalIdentifiers(String  userId,
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
                                                                                                         PropertyServerException;


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
    public abstract Map<String, Map<String, String>> getVendorProperties(String  userId,
                                                                         String  openMetadataElementGUID,
                                                                         String  openMetadataElementTypeName,
                                                                         boolean forLineage,
                                                                         boolean forDuplicateProcessing,
                                                                         Date    effectiveTime) throws InvalidParameterException,
                                                                                                       UserNotAuthorizedException,
                                                                                                       PropertyServerException;

    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "OpenMetadataClient{" +
                "serverName='" + serverName + '\'' +
                ", serverPlatformURLRoot='" + serverPlatformURLRoot + '\'' +
                '}';
    }
}
