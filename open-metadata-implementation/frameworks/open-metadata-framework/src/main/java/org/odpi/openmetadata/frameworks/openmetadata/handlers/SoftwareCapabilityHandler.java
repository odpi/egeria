/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.handlers;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.EntityProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.CapabilityAssetUseProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.softwarecapabilities.SoftwareCapabilityProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * SoftwareCapabilityHandler provides methods to define all types of softwareCapabilities and their relationships
 */
public class SoftwareCapabilityHandler extends OpenMetadataHandlerBase
{
    /**
     * Create a new handler.
     *
     * @param localServerName        name of this server (view server)
     * @param auditLog               logging destination
     * @param localServiceName       local service name
     * @param openMetadataClient     access to open metadata
     */
    public SoftwareCapabilityHandler(String             localServerName,
                                     AuditLog           auditLog,
                                     String             localServiceName,
                                     OpenMetadataClient openMetadataClient)
    {
        super(localServerName,
              auditLog,
              localServiceName,
              openMetadataClient,
              OpenMetadataType.SOFTWARE_CAPABILITY.typeName);
    }


    /**
     * Create a new handler.
     *
     * @param localServerName        name of this server (view server)
     * @param auditLog               logging destination
     * @param localServiceName       local service name
     * @param openMetadataClient     access to open metadata
     * @param softwareCapabilityTypeName          subtype of softwareCapability to control handler
     */
    public SoftwareCapabilityHandler(String             localServerName,
                                     AuditLog           auditLog,
                                     String             localServiceName,
                                     OpenMetadataClient openMetadataClient,
                                     String             softwareCapabilityTypeName)
    {
        super(localServerName,
              auditLog,
              localServiceName,
              openMetadataClient,
              softwareCapabilityTypeName);
    }


    /**
     * Create a new handler.
     *
     * @param template        properties to copy
     * @param specificTypeName   subtype to control handler
     */
    public SoftwareCapabilityHandler(SoftwareCapabilityHandler template,
                                     String                    specificTypeName)
    {
        super(template, specificTypeName);
    }


    /**
     * Create a new softwareCapability.
     *
     * @param userId                       userId of user making request.
     * @param newElementOptions details of the element to create
     * @param initialClassifications map of classification names to classification properties to include in the entity creation request
     * @param properties                   properties for the new element.
     * @param parentRelationshipProperties properties to include in parent relationship
     * @return unique identifier of the newly created element
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String createSoftwareCapability(String                                userId,
                                           NewElementOptions                     newElementOptions,
                                           Map<String, ClassificationProperties> initialClassifications,
                                           SoftwareCapabilityProperties          properties,
                                           RelationshipProperties                parentRelationshipProperties) throws InvalidParameterException,
                                                                                                                      PropertyServerException,
                                                                                                                      UserNotAuthorizedException
    {
        final String methodName = "createSoftwareCapability";

        return super.createNewElement(userId,
                                      newElementOptions,
                                      initialClassifications,
                                      properties,
                                      parentRelationshipProperties,
                                      methodName);
    }


    /**
     * Create a new metadata element to represent a software capability using an existing element as a template.
     * The template defines additional classifications and relationships that should be added to the new softwareCapability.
     *
     * @param userId                       calling user
     * @param templateOptions details of the element to create
     * @param templateGUID the unique identifier of the existing softwareCapability to copy (this will copy all the attachments such as nested content, schema
     *                     connection etc)
     * @param replacementProperties properties of the new metadata element.  These override the template values
     * @param placeholderProperties property name-to-property value map to replace any placeholder values in the
     *                              template element - and their anchored elements, which are also copied as part of this operation.
     * @param parentRelationshipProperties properties to include in parent relationship
     *
     * @return unique identifier of the new metadata element
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createSoftwareCapabilityFromTemplate(String                 userId,
                                                       TemplateOptions        templateOptions,
                                                       String                 templateGUID,
                                                       EntityProperties       replacementProperties,
                                                       Map<String, String>    placeholderProperties,
                                                       RelationshipProperties parentRelationshipProperties) throws InvalidParameterException,
                                                                                                                   UserNotAuthorizedException,
                                                                                                                   PropertyServerException
    {
        return super.createElementFromTemplate(userId,
                                               templateOptions,
                                               templateGUID,
                                               replacementProperties,
                                               placeholderProperties,
                                               parentRelationshipProperties);
    }


    /**
     * Update the properties of a software capability.
     *
     * @param userId                 userId of user making request.
     * @param softwareCapabilityGUID       unique identifier of the softwareCapability (returned from create)
     * @param updateOptions provides a structure for the additional options when updating an element.
     * @param properties             properties for the element.
     * @return boolean - true if an update occurred
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public boolean updateSoftwareCapability(String                       userId,
                                            String                       softwareCapabilityGUID,
                                            UpdateOptions                updateOptions,
                                            SoftwareCapabilityProperties properties) throws InvalidParameterException,
                                                                                            PropertyServerException,
                                                                                            UserNotAuthorizedException
    {
        final String methodName        = "updateSoftwareCapability";
        final String guidParameterName = "softwareCapabilityGUID";

        return super.updateElement(userId,
                                   softwareCapabilityGUID,
                                   guidParameterName,
                                   updateOptions,
                                   properties,
                                   methodName);
    }


    /**
     * Create a relationship that represents the use of an asset (typically a data or process asset) by
     * a softwareCapability.
     *
     * @param userId                 userId of user making request
     * @param softwareCapabilityGUID       unique identifier of the softwareCapability
     * @param assetGUID           unique identifier of the destination softwareCapability
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void addAssetUse(String                       userId,
                            String                       softwareCapabilityGUID,
                            String                       assetGUID,
                            MakeAnchorOptions            makeAnchorOptions,
                            CapabilityAssetUseProperties relationshipProperties) throws InvalidParameterException,
                                                                                        PropertyServerException,
                                                                                        UserNotAuthorizedException
    {
        final String methodName            = "addAssetUse";
        final String end1GUIDParameterName = "softwareCapabilityGUID";
        final String end2GUIDParameterName = "assetGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(softwareCapabilityGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(assetGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.CAPABILITY_ASSET_USE_RELATIONSHIP.typeName,
                                                        softwareCapabilityGUID,
                                                        assetGUID,
                                                        makeAnchorOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Remove a CapabilityAssetUse relationship.
     *
     * @param userId                 userId of user making request.
     * @param softwareCapabilityGUID       unique identifier of the softwareCapability
     * @param assetGUID           unique identifier of the destination softwareCapability
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachAssetUse(String        userId,
                               String        softwareCapabilityGUID,
                               String        assetGUID,
                               DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                   PropertyServerException,
                                                                   UserNotAuthorizedException
    {
        final String methodName = "detachAssetUse";

        final String end1GUIDParameterName = "softwareCapabilityGUID";
        final String end2GUIDParameterName = "assetGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(softwareCapabilityGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(assetGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.CAPABILITY_ASSET_USE_RELATIONSHIP.typeName,
                                                        assetGUID,
                                                        softwareCapabilityGUID,
                                                        deleteOptions);
    }


    /**
     * Delete a software capability.
     *
     * @param userId                 userId of user making request.
     * @param softwareCapabilityGUID       unique identifier of the element
     * @param deleteOptions options for a delete request
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void deleteSoftwareCapability(String        userId,
                                         String        softwareCapabilityGUID,
                                         DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException
    {
        final String methodName        = "deleteSoftwareCapability";
        final String guidParameterName = "softwareCapabilityGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(softwareCapabilityGUID, guidParameterName, methodName);

        openMetadataClient.deleteMetadataElementInStore(userId, softwareCapabilityGUID, deleteOptions);
    }


    /**
     * Returns the list of softwareCapabilities with a particular name.
     *
     * @param userId                 userId of user making request
     * @param name                   name of the element to return - match is full text match in qualifiedName, resourceName or displayName
     * @param queryOptions           multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getSoftwareCapabilitiesByName(String       userId,
                                                                       String       name,
                                                                       QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                         PropertyServerException,
                                                                                                         UserNotAuthorizedException
    {
        final String methodName = "getSoftwareCapabilitiesByName";

        List<String> propertyNames = Arrays.asList(OpenMetadataProperty.QUALIFIED_NAME.name,
                                                   OpenMetadataProperty.IDENTIFIER.name,
                                                   OpenMetadataProperty.DISPLAY_NAME.name,
                                                   OpenMetadataProperty.RESOURCE_NAME.name);

        return super.getRootElementsByName(userId,
                                           name,
                                           propertyNames,
                                           queryOptions,
                                           methodName);
    }


    /**
     * Returns the list of softwareCapabilities with a particular name.
     *
     * @param userId                 userId of user making request
     * @param name                   deployedImplementationType name of the element to return - match is full text match
     * @param queryOptions           multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getSoftwareCapabilitiesByDeployedImplementationType(String       userId,
                                                                                             String       name,
                                                                                             QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                                               PropertyServerException,
                                                                                                                               UserNotAuthorizedException
    {
        final String methodName = "getSoftwareCapabilitiesByDeployedImplementationType";

        List<String> propertyNames = Collections.singletonList(OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name);

        return super.getRootElementsByName(userId,
                                           name,
                                           propertyNames,
                                           queryOptions,
                                           methodName);
    }



    /**
     * Return a list of softwareCapabilities with the requested name.  The name must match exactly.
     *
     * @param userId calling user
     * @param metadataCollectionId unique identifier of the metadata collection to search for
     * @param queryOptions options to control the query
     *
     * @return list of unique identifiers of softwareCapabilities with matching name.
     *
     * @throws InvalidParameterException the name is invalid
     * @throws PropertyServerException there is a problem access in the property server
     * @throws UserNotAuthorizedException the user does not have access to the properties
     */
    public List<OpenMetadataRootElement> getSoftwareCapabilitiesByMetadataCollectionId(String       userId,
                                                                                       String       metadataCollectionId,
                                                                                       QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                                         PropertyServerException,
                                                                                                                         UserNotAuthorizedException
    {
        final String methodName = "getSoftwareCapabilitiesByMetadataCollectionId";

        List<String> propertyNames = Collections.singletonList(OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name);

        return super.getRootElementsByName(userId,
                                           metadataCollectionId,
                                           propertyNames,
                                           queryOptions,
                                           methodName);
    }


    /**
     * Returns the list of softwareCapabilities that are using a particular asset.
     *
     * @param userId                 userId of user making request
     * @param assetGUID              unique identifier of the starting element
     * @param queryOptions           multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getCapabilityUse(String       userId,
                                                          String       assetGUID,
                                                          QueryOptions queryOptions) throws InvalidParameterException,
                                                                                            PropertyServerException,
                                                                                            UserNotAuthorizedException
    {
        final String methodName = "getCapabilityUse";
        final String guidPropertyName = "assetGUID";

        return super.getRelatedRootElements(userId,
                                            assetGUID,
                                            guidPropertyName,
                                            2,
                                            OpenMetadataType.CAPABILITY_ASSET_USE_RELATIONSHIP.typeName,
                                            OpenMetadataType.SOFTWARE_CAPABILITY.typeName,
                                            queryOptions,
                                            methodName);
    }


    /**
     * Returns the list of governance engines connected to a particular governance service.
     *
     * @param userId                 userId of user making request
     * @param governanceServiceGUID              unique identifier of the starting element
     * @param queryOptions           multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getGovernanceEngines(String       userId,
                                                              String       governanceServiceGUID,
                                                              QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                PropertyServerException,
                                                                                                UserNotAuthorizedException
    {
        final String methodName = "getGovernanceEngines";
        final String guidPropertyName = "governanceServiceGUID";

        return super.getRelatedRootElements(userId,
                                            governanceServiceGUID,
                                            guidPropertyName,
                                            2,
                                            OpenMetadataType.SUPPORTED_GOVERNANCE_SERVICE_RELATIONSHIP.typeName,
                                            OpenMetadataType.GOVERNANCE_ENGINE.typeName,
                                            queryOptions,
                                            methodName);
    }


    /**
     * Returns the list of integration groups connected to a particular integration connector.
     *
     * @param userId                 userId of user making request
     * @param integrationConnectorGUID              unique identifier of the starting element
     * @param queryOptions           multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getIntegrationGroups(String       userId,
                                                              String       integrationConnectorGUID,
                                                              QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                PropertyServerException,
                                                                                                UserNotAuthorizedException
    {
        final String methodName = "getIntegrationGroups";
        final String guidPropertyName = "integrationConnectorGUID";

        return super.getRelatedRootElements(userId,
                                            integrationConnectorGUID,
                                            guidPropertyName,
                                            2,
                                            OpenMetadataType.REGISTERED_INTEGRATION_CONNECTOR_RELATIONSHIP.typeName,
                                            OpenMetadataType.INTEGRATION_GROUP.typeName,
                                            queryOptions,
                                            methodName);
    }


    /**
     * Return the properties of a specific softwareCapability.
     *
     * @param userId                 userId of user making request
     * @param softwareCapabilityGUID       unique identifier of the required element
     * @param getOptions multiple options to control the query
     * @return retrieved properties
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElement getSoftwareCapabilityByGUID(String     userId,
                                                               String     softwareCapabilityGUID,
                                                               GetOptions getOptions) throws InvalidParameterException,
                                                                                             PropertyServerException,
                                                                                             UserNotAuthorizedException
    {
        final String methodName = "getSoftwareCapabilityByGUID";

        return super.getRootElementByGUID(userId, softwareCapabilityGUID, getOptions, methodName);
    }


    /**
     * Retrieve the list of software capability metadata elements that are attached to a specific infrastructure element.
     *
     * @param userId         userId of user making request
     * @param infrastructureGUID element to search for
     * @param queryOptions multiple options to control the query
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<OpenMetadataRootElement> getSoftwareCapabilitiesForInfrastructure(String       userId,
                                                                                  String       infrastructureGUID,
                                                                                  QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                                    UserNotAuthorizedException,
                                                                                                                    PropertyServerException
    {
        final String methodName = "getSoftwareCapabilitiesForInfrastructure";
        final String parentGUIDParameterName = "infrastructureGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(infrastructureGUID, parentGUIDParameterName, methodName);

        return super.getRelatedRootElements(userId,
                                            infrastructureGUID,
                                            parentGUIDParameterName,
                                            1,
                                            OpenMetadataType.SUPPORTED_SOFTWARE_CAPABILITY_RELATIONSHIP.typeName,
                                            OpenMetadataType.SOFTWARE_CAPABILITY.typeName,
                                            queryOptions,
                                            methodName);
    }


    /**
     * Retrieve the list of software capabilities metadata elements that contain the search string.
     *
     * @param userId                 calling user
     * @param searchString           string to find in the properties
     * @param searchOptions multiple options to control the query
     * @return list of matching metadata elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<OpenMetadataRootElement> findSoftwareCapabilities(String        userId,
                                                                  String        searchString,
                                                                  SearchOptions searchOptions) throws InvalidParameterException,
                                                                                                      UserNotAuthorizedException,
                                                                                                      PropertyServerException
    {
        final String methodName = "findSoftwareCapabilities";

        return super.findRootElements(userId, searchString, searchOptions, methodName);
    }
}
