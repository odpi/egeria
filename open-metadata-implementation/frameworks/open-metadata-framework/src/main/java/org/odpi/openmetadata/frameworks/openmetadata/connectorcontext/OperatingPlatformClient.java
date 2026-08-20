/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.connectorcontext;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.OperatingPlatformHandler;
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

import java.util.List;
import java.util.Map;

/**
 * Provides services for connectors to work with operating platforms and the software packages they depend on.
 */
public class OperatingPlatformClient extends ConnectorContextClientBase
{
    private final OperatingPlatformHandler operatingPlatformHandler;


    /**
     * Constructor for connector context client.
     *
     * @param parentContext connector's context
     * @param localServerName local server where this client is running - used for error handling
     * @param localServiceName local service that his connector is hosted by - used for error handling
     * @param connectorUserId the userId to use with all requests for open metadata
     * @param connectorGUID the unique identifier that represents this connector in open metadata
     * @param externalSourceGUID unique identifier of the software server capability for the source of metadata
     * @param externalSourceName unique name of the software server capability for the source of metadata
     * @param openMetadataClient client to access the open metadata store
     * @param auditLog logging destination
     * @param maxPageSize max number of elements that can be returned on a query
     */
    public OperatingPlatformClient(ConnectorContextBase     parentContext,
                                   String                   localServerName,
                                   String                   localServiceName,
                                   String                   connectorUserId,
                                   String                   connectorGUID,
                                   String                   externalSourceGUID,
                                   String                   externalSourceName,
                                   OpenMetadataClient       openMetadataClient,
                                   AuditLog                 auditLog,
                                   int                      maxPageSize)
    {
        super(parentContext, localServerName, localServiceName, connectorUserId, connectorGUID, externalSourceGUID, externalSourceName, auditLog, maxPageSize);

        this.operatingPlatformHandler = new OperatingPlatformHandler(localServerName, auditLog, localServiceName, openMetadataClient);
    }


    /**
     * Create a new operating platform.
     *
     * @param newElementOptions details of the element to create
     * @param initialClassifications map of classification names to classification properties to include in the entity creation request
     * @param properties                   properties for the new element.
     * @param parentRelationshipProperties properties to include in parent relationship
     * @return unique identifier of the newly created element
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String createOperatingPlatform(NewElementOptions                     newElementOptions,
                                          Map<String, ClassificationProperties> initialClassifications,
                                          OperatingPlatformProperties           properties,
                                          RelationshipProperties                parentRelationshipProperties) throws InvalidParameterException,
                                                                                                                     PropertyServerException,
                                                                                                                     UserNotAuthorizedException
    {
        String elementGUID = operatingPlatformHandler.createOperatingPlatform(connectorUserId, newElementOptions, initialClassifications, properties, parentRelationshipProperties);

        if (parentContext.getActivityReportWriter() != null)
        {
            parentContext.getActivityReportWriter().reportElementCreation(elementGUID);
        }

        return elementGUID;
    }


    /**
     * Create a new metadata element to represent an operating platform using an existing element as a template.
     *
     * @param templateOptions              details of the element to create
     * @param templateGUID                 the unique identifier of the existing element to copy
     * @param replacementProperties        properties of the new metadata element.  These override the template values
     * @param replacementClassifications map of classification names to classification properties to include in the entity creation request. These override the template values.
     * @param placeholderProperties        property name-to-property value map to replace any placeholder values in the
     *                                     template element - and their anchored elements, which are also copied as part of this operation.
     * @param parentRelationshipProperties properties to include in parent relationship
     * @return unique identifier of the new metadata element
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     */
    public String createOperatingPlatformFromTemplate(TemplateOptions                       templateOptions,
                                                      String                                templateGUID,
                                                      EntityProperties                      replacementProperties,
                                                      Map<String, ClassificationProperties> replacementClassifications,
                                                      Map<String, String>                   placeholderProperties,
                                                      RelationshipProperties                parentRelationshipProperties) throws InvalidParameterException,
                                                                                                                                 UserNotAuthorizedException,
                                                                                                                                 PropertyServerException
    {
        String elementGUID = operatingPlatformHandler.createOperatingPlatformFromTemplate(connectorUserId, templateOptions, templateGUID, replacementProperties, replacementClassifications, placeholderProperties, parentRelationshipProperties);

        if (parentContext.getActivityReportWriter() != null)
        {
            parentContext.getActivityReportWriter().reportElementCreation(elementGUID);
        }

        return elementGUID;
    }


    /**
     * Update the properties of an operating platform.
     *
     * @param operatingPlatformGUID unique identifier of the operating platform (returned from create)
     * @param updateOptions provides a structure for the additional options when updating an element.
     * @param properties             properties for the element.
     * @return boolean - true if an update occurred
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public boolean updateOperatingPlatform(String                      operatingPlatformGUID,
                                           UpdateOptions               updateOptions,
                                           OperatingPlatformProperties properties) throws InvalidParameterException,
                                                                                          PropertyServerException,
                                                                                          UserNotAuthorizedException
    {
        boolean updateOccurred = operatingPlatformHandler.updateOperatingPlatform(connectorUserId, operatingPlatformGUID, updateOptions, properties);

        if ((updateOccurred) && (parentContext.getActivityReportWriter() != null))
        {
            parentContext.getActivityReportWriter().reportElementUpdate(operatingPlatformGUID);
        }

        return updateOccurred;
    }


    /**
     * Attach an operating platform to the IT infrastructure that it is installed on.
     *
     * @param operatingPlatformGUID  unique identifier of the operating platform
     * @param itInfrastructureGUID   unique identifier of the IT infrastructure that the operating platform is installed on
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkOperatingPlatformUse(String                        operatingPlatformGUID,
                                         String                        itInfrastructureGUID,
                                         MakeAnchorOptions             makeAnchorOptions,
                                         OperatingPlatformUseProperties relationshipProperties) throws InvalidParameterException,
                                                                                                       PropertyServerException,
                                                                                                       UserNotAuthorizedException
    {
        operatingPlatformHandler.linkOperatingPlatformUse(connectorUserId, operatingPlatformGUID, itInfrastructureGUID, makeAnchorOptions, relationshipProperties);
    }


    /**
     * Detach an operating platform from the IT infrastructure that it was installed on.
     *
     * @param operatingPlatformGUID  unique identifier of the operating platform
     * @param itInfrastructureGUID   unique identifier of the IT infrastructure that the operating platform was installed on
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachOperatingPlatformUse(String        operatingPlatformGUID,
                                           String        itInfrastructureGUID,
                                           DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                               PropertyServerException,
                                                                               UserNotAuthorizedException
    {
        operatingPlatformHandler.detachOperatingPlatformUse(connectorUserId, operatingPlatformGUID, itInfrastructureGUID, deleteOptions);
    }


    /**
     * Attach an operating platform to the collection of software packages that it is packaged with.
     *
     * @param operatingPlatformGUID  unique identifier of the operating platform
     * @param collectionGUID         unique identifier of the collection of software packages
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkOperatingPlatformManifest(String                              operatingPlatformGUID,
                                              String                              collectionGUID,
                                              MakeAnchorOptions                   makeAnchorOptions,
                                              OperatingPlatformManifestProperties relationshipProperties) throws InvalidParameterException,
                                                                                                                 PropertyServerException,
                                                                                                                 UserNotAuthorizedException
    {
        operatingPlatformHandler.linkOperatingPlatformManifest(connectorUserId, operatingPlatformGUID, collectionGUID, makeAnchorOptions, relationshipProperties);
    }


    /**
     * Detach an operating platform from a collection of software packages that it was packaged with.
     *
     * @param operatingPlatformGUID  unique identifier of the operating platform
     * @param collectionGUID         unique identifier of the collection of software packages
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachOperatingPlatformManifest(String        operatingPlatformGUID,
                                                String        collectionGUID,
                                                DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                    PropertyServerException,
                                                                                    UserNotAuthorizedException
    {
        operatingPlatformHandler.detachOperatingPlatformManifest(connectorUserId, operatingPlatformGUID, collectionGUID, deleteOptions);
    }


    /**
     * Attach an asset to the collection of software packages that it depends on when it is running.
     *
     * @param assetGUID              unique identifier of the asset
     * @param collectionGUID         unique identifier of the collection of software packages
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkSoftwarePackageDependency(String                              assetGUID,
                                              String                              collectionGUID,
                                              MakeAnchorOptions                   makeAnchorOptions,
                                              SoftwarePackageDependencyProperties relationshipProperties) throws InvalidParameterException,
                                                                                                                 PropertyServerException,
                                                                                                                 UserNotAuthorizedException
    {
        operatingPlatformHandler.linkSoftwarePackageDependency(connectorUserId, assetGUID, collectionGUID, makeAnchorOptions, relationshipProperties);
    }


    /**
     * Detach an asset from a collection of software packages that it no longer depends on.
     *
     * @param assetGUID              unique identifier of the asset
     * @param collectionGUID         unique identifier of the collection of software packages
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachSoftwarePackageDependency(String        assetGUID,
                                                String        collectionGUID,
                                                DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                    PropertyServerException,
                                                                                    UserNotAuthorizedException
    {
        operatingPlatformHandler.detachSoftwarePackageDependency(connectorUserId, assetGUID, collectionGUID, deleteOptions);
    }


    /**
     * Classify an element to indicate that it describes a list of software packages.
     *
     * @param elementGUID            unique identifier of the element.
     * @param properties             properties for the classification
     * @param metadataSourceOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void setSoftwarePackageManifest(String                            elementGUID,
                                           SoftwarePackageManifestProperties properties,
                                           MetadataSourceOptions             metadataSourceOptions) throws InvalidParameterException,
                                                                                                           PropertyServerException,
                                                                                                           UserNotAuthorizedException
    {
        operatingPlatformHandler.setSoftwarePackageManifest(connectorUserId, elementGUID, properties, metadataSourceOptions);

        if (parentContext.getActivityReportWriter() != null)
        {
            parentContext.getActivityReportWriter().reportElementUpdate(elementGUID);
        }
    }


    /**
     * Remove the software package manifest designation from the element.
     *
     * @param elementGUID            unique identifier of the element.
     * @param metadataSourceOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void clearSoftwarePackageManifest(String                elementGUID,
                                             MetadataSourceOptions metadataSourceOptions) throws InvalidParameterException,
                                                                                                 PropertyServerException,
                                                                                                 UserNotAuthorizedException
    {
        operatingPlatformHandler.clearSoftwarePackageManifest(connectorUserId, elementGUID, metadataSourceOptions);

        if (parentContext.getActivityReportWriter() != null)
        {
            parentContext.getActivityReportWriter().reportElementUpdate(elementGUID);
        }
    }


    /**
     * Delete an operating platform.
     *
     * @param operatingPlatformGUID unique identifier of the element
     * @param deleteOptions options for a delete request
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void deleteOperatingPlatform(String        operatingPlatformGUID,
                                        DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                            PropertyServerException,
                                                                            UserNotAuthorizedException
    {
        operatingPlatformHandler.deleteOperatingPlatform(connectorUserId, operatingPlatformGUID, deleteOptions);

        if (parentContext.getActivityReportWriter() != null)
        {
            parentContext.getActivityReportWriter().reportElementDelete(operatingPlatformGUID);
        }
    }


    /**
     * Returns the list of operating platforms with a particular name.
     *
     * @param name                   name of the element to return - match is full text match in qualifiedName, identifier or displayName
     * @param queryOptions           multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getOperatingPlatformsByName(String       name,
                                                                     QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                       PropertyServerException,
                                                                                                       UserNotAuthorizedException
    {
        return operatingPlatformHandler.getOperatingPlatformsByName(connectorUserId, name, queryOptions);
    }


    /**
     * Return the properties of a specific operating platform.
     *
     * @param operatingPlatformGUID unique identifier of the required element
     * @param getOptions multiple options to control the query
     * @return retrieved properties
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElement getOperatingPlatformByGUID(String     operatingPlatformGUID,
                                                              GetOptions getOptions) throws InvalidParameterException,
                                                                                            PropertyServerException,
                                                                                            UserNotAuthorizedException
    {
        return operatingPlatformHandler.getOperatingPlatformByGUID(connectorUserId, operatingPlatformGUID, getOptions);
    }


    /**
     * Retrieve the list of operating platform metadata elements that contain the search string.
     *
     * @param searchString           string to find in the properties
     * @param searchOptions multiple options to control the query
     * @return list of matching metadata elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     */
    public List<OpenMetadataRootElement> findOperatingPlatforms(String        searchString,
                                                                SearchOptions searchOptions) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException
    {
        return operatingPlatformHandler.findOperatingPlatforms(connectorUserId, searchString, searchOptions);
    }
}
