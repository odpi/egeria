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
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.OperatingPlatformManifestProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.OperatingPlatformProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.OperatingPlatformUseProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.SoftwarePackageDependencyProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.SoftwarePackageManifestProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * OperatingPlatformHandler provides methods to define operating platforms, the IT infrastructure they are
 * installed on, and the collections of software packages that they and other assets depend on.
 */
public class OperatingPlatformHandler extends OpenMetadataHandlerBase
{
    /**
     * Create a new handler.
     *
     * @param localServerName        name of this server (view server)
     * @param auditLog               logging destination
     * @param localServiceName       local service name
     * @param openMetadataClient     access to open metadata
     */
    public OperatingPlatformHandler(String             localServerName,
                                    AuditLog           auditLog,
                                    String             localServiceName,
                                    OpenMetadataClient openMetadataClient)
    {
        super(localServerName,
              auditLog,
              localServiceName,
              openMetadataClient,
              OpenMetadataType.OPERATING_PLATFORM.typeName);
    }


    /**
     * Create a new operating platform.
     *
     * @param userId                       userId of the user making the request.
     * @param newElementOptions details of the element to create
     * @param initialClassifications map of classification names to classification properties to include in the entity creation request
     * @param properties                   properties for the new element.
     * @param parentRelationshipProperties properties to include in parent relationship
     * @return unique identifier of the newly created element
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String createOperatingPlatform(String                                userId,
                                          NewElementOptions                     newElementOptions,
                                          Map<String, ClassificationProperties> initialClassifications,
                                          OperatingPlatformProperties           properties,
                                          RelationshipProperties                parentRelationshipProperties) throws InvalidParameterException,
                                                                                                                     PropertyServerException,
                                                                                                                     UserNotAuthorizedException
    {
        final String methodName = "createOperatingPlatform";

        return super.createNewElement(userId,
                                      newElementOptions,
                                      initialClassifications,
                                      properties,
                                      parentRelationshipProperties,
                                      methodName);
    }


    /**
     * Create a new metadata element to represent an operating platform using an existing element as a template.
     * The template defines additional classifications and relationships that should be added to the new operating platform.
     *
     * @param userId                       calling user
     * @param templateOptions details of the element to create
     * @param templateGUID the unique identifier of the existing element to copy
     * @param replacementProperties properties of the new metadata element.  These override the template values
     * @param replacementClassifications map of classification names to classification properties to include in the entity creation request. These override the template values.
     * @param placeholderProperties property name-to-property value map to replace any placeholder values in the
     *                              template element - and their anchored elements, which are also copied as part of this operation.
     * @param parentRelationshipProperties properties to include in parent relationship
     *
     * @return unique identifier of the new metadata element
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     */
    public String createOperatingPlatformFromTemplate(String                                userId,
                                                      TemplateOptions                       templateOptions,
                                                      String                                templateGUID,
                                                      EntityProperties                      replacementProperties,
                                                      Map<String, ClassificationProperties> replacementClassifications,
                                                      Map<String, String>                   placeholderProperties,
                                                      RelationshipProperties                parentRelationshipProperties) throws InvalidParameterException,
                                                                                                                                 UserNotAuthorizedException,
                                                                                                                                 PropertyServerException
    {
        return super.createElementFromTemplate(userId,
                                               templateOptions,
                                               templateGUID,
                                               replacementProperties,
                                               replacementClassifications,
                                               placeholderProperties,
                                               parentRelationshipProperties);
    }


    /**
     * Update the properties of an operating platform.
     *
     * @param userId                 userId of the user making the request.
     * @param operatingPlatformGUID unique identifier of the operating platform (returned from create)
     * @param updateOptions provides a structure for the additional options when updating an element.
     * @param properties             properties for the element.
     * @return boolean - true if an update occurred
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public boolean updateOperatingPlatform(String                      userId,
                                           String                      operatingPlatformGUID,
                                           UpdateOptions               updateOptions,
                                           OperatingPlatformProperties properties) throws InvalidParameterException,
                                                                                          PropertyServerException,
                                                                                          UserNotAuthorizedException
    {
        final String methodName        = "updateOperatingPlatform";
        final String guidParameterName = "operatingPlatformGUID";

        return super.updateElement(userId,
                                   operatingPlatformGUID,
                                   guidParameterName,
                                   updateOptions,
                                   properties,
                                   methodName);
    }


    /**
     * Attach an operating platform to the IT infrastructure that it is installed on.
     *
     * @param userId                 userId of the user making the request
     * @param operatingPlatformGUID  unique identifier of the operating platform
     * @param itInfrastructureGUID   unique identifier of the IT infrastructure that the operating platform is installed on
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkOperatingPlatformUse(String                        userId,
                                         String                        operatingPlatformGUID,
                                         String                        itInfrastructureGUID,
                                         MakeAnchorOptions             makeAnchorOptions,
                                         OperatingPlatformUseProperties relationshipProperties) throws InvalidParameterException,
                                                                                                       PropertyServerException,
                                                                                                       UserNotAuthorizedException
    {
        final String methodName            = "linkOperatingPlatformUse";
        final String end1GUIDParameterName = "operatingPlatformGUID";
        final String end2GUIDParameterName = "itInfrastructureGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(operatingPlatformGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(itInfrastructureGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.OPERATING_PLATFORM_USE_RELATIONSHIP.typeName,
                                                        operatingPlatformGUID,
                                                        itInfrastructureGUID,
                                                        makeAnchorOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach an operating platform from the IT infrastructure that it was installed on.
     *
     * @param userId                 userId of the user making the request.
     * @param operatingPlatformGUID  unique identifier of the operating platform
     * @param itInfrastructureGUID   unique identifier of the IT infrastructure that the operating platform was installed on
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachOperatingPlatformUse(String        userId,
                                           String        operatingPlatformGUID,
                                           String        itInfrastructureGUID,
                                           DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                               PropertyServerException,
                                                                               UserNotAuthorizedException
    {
        final String methodName            = "detachOperatingPlatformUse";
        final String end1GUIDParameterName = "operatingPlatformGUID";
        final String end2GUIDParameterName = "itInfrastructureGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(operatingPlatformGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(itInfrastructureGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.OPERATING_PLATFORM_USE_RELATIONSHIP.typeName,
                                                        operatingPlatformGUID,
                                                        itInfrastructureGUID,
                                                        deleteOptions);
    }


    /**
     * Attach an operating platform to the collection of software packages that it is packaged with.
     *
     * @param userId                 userId of the user making the request
     * @param operatingPlatformGUID  unique identifier of the operating platform
     * @param collectionGUID         unique identifier of the collection of software packages
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkOperatingPlatformManifest(String                              userId,
                                              String                              operatingPlatformGUID,
                                              String                              collectionGUID,
                                              MakeAnchorOptions                   makeAnchorOptions,
                                              OperatingPlatformManifestProperties relationshipProperties) throws InvalidParameterException,
                                                                                                                 PropertyServerException,
                                                                                                                 UserNotAuthorizedException
    {
        final String methodName            = "linkOperatingPlatformManifest";
        final String end1GUIDParameterName = "operatingPlatformGUID";
        final String end2GUIDParameterName = "collectionGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(operatingPlatformGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(collectionGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.OPERATING_PLATFORM_MANIFEST_RELATIONSHIP.typeName,
                                                        operatingPlatformGUID,
                                                        collectionGUID,
                                                        makeAnchorOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach an operating platform from a collection of software packages that it was packaged with.
     *
     * @param userId                 userId of the user making the request.
     * @param operatingPlatformGUID  unique identifier of the operating platform
     * @param collectionGUID         unique identifier of the collection of software packages
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachOperatingPlatformManifest(String        userId,
                                                String        operatingPlatformGUID,
                                                String        collectionGUID,
                                                DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                    PropertyServerException,
                                                                                    UserNotAuthorizedException
    {
        final String methodName            = "detachOperatingPlatformManifest";
        final String end1GUIDParameterName = "operatingPlatformGUID";
        final String end2GUIDParameterName = "collectionGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(operatingPlatformGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(collectionGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.OPERATING_PLATFORM_MANIFEST_RELATIONSHIP.typeName,
                                                        operatingPlatformGUID,
                                                        collectionGUID,
                                                        deleteOptions);
    }


    /**
     * Attach an asset to the collection of software packages that it depends on when it is running.
     *
     * @param userId                 userId of the user making the request
     * @param assetGUID              unique identifier of the asset
     * @param collectionGUID         unique identifier of the collection of software packages
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkSoftwarePackageDependency(String                              userId,
                                              String                              assetGUID,
                                              String                              collectionGUID,
                                              MakeAnchorOptions                   makeAnchorOptions,
                                              SoftwarePackageDependencyProperties relationshipProperties) throws InvalidParameterException,
                                                                                                                 PropertyServerException,
                                                                                                                 UserNotAuthorizedException
    {
        final String methodName            = "linkSoftwarePackageDependency";
        final String end1GUIDParameterName = "assetGUID";
        final String end2GUIDParameterName = "collectionGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(assetGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(collectionGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.SOFTWARE_PACKAGE_DEPENDENCY_RELATIONSHIP.typeName,
                                                        assetGUID,
                                                        collectionGUID,
                                                        makeAnchorOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach an asset from a collection of software packages that it no longer depends on.
     *
     * @param userId                 userId of the user making the request.
     * @param assetGUID              unique identifier of the asset
     * @param collectionGUID         unique identifier of the collection of software packages
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachSoftwarePackageDependency(String        userId,
                                                String        assetGUID,
                                                String        collectionGUID,
                                                DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                    PropertyServerException,
                                                                                    UserNotAuthorizedException
    {
        final String methodName            = "detachSoftwarePackageDependency";
        final String end1GUIDParameterName = "assetGUID";
        final String end2GUIDParameterName = "collectionGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(assetGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(collectionGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.SOFTWARE_PACKAGE_DEPENDENCY_RELATIONSHIP.typeName,
                                                        assetGUID,
                                                        collectionGUID,
                                                        deleteOptions);
    }


    /*
     * SoftwarePackageManifest is a classification rather than a relationship - it marks a collection as
     * describing a list of software packages.
     */

    /**
     * Classify an element to indicate that it describes a list of software packages.
     *
     * @param userId                 userId of the user making the request.
     * @param elementGUID            unique identifier of the element.
     * @param properties             properties for the classification
     * @param metadataSourceOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void setSoftwarePackageManifest(String                            userId,
                                           String                            elementGUID,
                                           SoftwarePackageManifestProperties properties,
                                           MetadataSourceOptions             metadataSourceOptions) throws InvalidParameterException,
                                                                                                           PropertyServerException,
                                                                                                           UserNotAuthorizedException
    {
        final String methodName        = "setSoftwarePackageManifest";
        final String guidParameterName = "elementGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(elementGUID, guidParameterName, methodName);

        openMetadataClient.classifyMetadataElementInStore(userId,
                                                          elementGUID,
                                                          OpenMetadataType.SOFTWARE_PACKAGE_MANIFEST_CLASSIFICATION.typeName,
                                                          metadataSourceOptions,
                                                          classificationBuilder.getNewElementProperties(properties));
    }


    /**
     * Remove the software package manifest designation from the element.
     *
     * @param userId                 userId of the user making the request.
     * @param elementGUID            unique identifier of the element.
     * @param metadataSourceOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void clearSoftwarePackageManifest(String                userId,
                                             String                elementGUID,
                                             MetadataSourceOptions metadataSourceOptions) throws InvalidParameterException,
                                                                                                 PropertyServerException,
                                                                                                 UserNotAuthorizedException
    {
        final String methodName        = "clearSoftwarePackageManifest";
        final String guidParameterName = "elementGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(elementGUID, guidParameterName, methodName);

        openMetadataClient.declassifyMetadataElementInStore(userId,
                                                            elementGUID,
                                                            OpenMetadataType.SOFTWARE_PACKAGE_MANIFEST_CLASSIFICATION.typeName,
                                                            metadataSourceOptions);
    }


    /**
     * Delete an operating platform.
     *
     * @param userId                 userId of the user making the request.
     * @param operatingPlatformGUID unique identifier of the element
     * @param deleteOptions options for a delete request
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void deleteOperatingPlatform(String        userId,
                                        String        operatingPlatformGUID,
                                        DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                            PropertyServerException,
                                                                            UserNotAuthorizedException
    {
        final String methodName        = "deleteOperatingPlatform";
        final String guidParameterName = "operatingPlatformGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(operatingPlatformGUID, guidParameterName, methodName);

        openMetadataClient.deleteMetadataElementInStore(userId, operatingPlatformGUID, deleteOptions);
    }


    /**
     * Returns the list of operating platforms with a particular name.
     *
     * @param userId                 userId of the user making the request
     * @param name                   name of the element to return - match is full text match in qualifiedName, identifier or displayName
     * @param queryOptions           multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getOperatingPlatformsByName(String       userId,
                                                                     String       name,
                                                                     QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                       PropertyServerException,
                                                                                                       UserNotAuthorizedException
    {
        final String methodName = "getOperatingPlatformsByName";

        List<String> propertyNames = Arrays.asList(OpenMetadataProperty.QUALIFIED_NAME.name,
                                                   OpenMetadataProperty.DISPLAY_NAME.name,
                                                   OpenMetadataProperty.IDENTIFIER.name);

        return super.getRootElementsByName(userId,
                                           name,
                                           propertyNames,
                                           queryOptions,
                                           methodName);
    }


    /**
     * Return the properties of a specific operating platform.
     *
     * @param userId                 userId of the user making the request
     * @param operatingPlatformGUID unique identifier of the required element
     * @param getOptions multiple options to control the query
     * @return retrieved properties
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElement getOperatingPlatformByGUID(String     userId,
                                                              String     operatingPlatformGUID,
                                                              GetOptions getOptions) throws InvalidParameterException,
                                                                                            PropertyServerException,
                                                                                            UserNotAuthorizedException
    {
        final String methodName = "getOperatingPlatformByGUID";

        return super.getRootElementByGUID(userId,
                                          operatingPlatformGUID,
                                          getOptions,
                                          methodName);
    }


    /**
     * Retrieve the list of operating platform metadata elements that contain the search string.
     *
     * @param userId                 calling user
     * @param searchString           string to find in the properties
     * @param searchOptions multiple options to control the query
     * @return list of matching metadata elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     */
    public List<OpenMetadataRootElement> findOperatingPlatforms(String        userId,
                                                                String        searchString,
                                                                SearchOptions searchOptions) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException
    {
        final String methodName  = "findOperatingPlatforms";

        return super.findRootElements(userId, searchString, searchOptions, methodName);
    }
}
