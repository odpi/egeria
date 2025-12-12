/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.connectorcontext;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.SoftwareCapabilityHandler;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.CapabilityAssetUseProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.softwarecapabilities.SoftwareCapabilityProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;

import java.util.List;
import java.util.Map;

/**
 * Provides services for connectors to work with software capabilities.
 */
public class SoftwareCapabilityClient extends ConnectorContextClientBase
{
    private final SoftwareCapabilityHandler softwareCapabilityHandler;


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
    public SoftwareCapabilityClient(ConnectorContextBase     parentContext,
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

        this.softwareCapabilityHandler = new SoftwareCapabilityHandler(localServerName, auditLog, localServiceName, openMetadataClient);
    }


    /**
     * Copy/clone constructor.
     *
     * @param template client to copy
     * @param specificTypeName type name override
     */
    public SoftwareCapabilityClient(SoftwareCapabilityClient template,
                                    String                   specificTypeName)
    {
        super(template);

        this.softwareCapabilityHandler = new SoftwareCapabilityHandler(template.softwareCapabilityHandler, specificTypeName);
    }


    /**
     * Create a new softwareCapability.
     *
     * @param newElementOptions details of the element to create
     * @param initialClassifications map of classification names to classification properties to include in the entity creation request
     * @param properties                   properties for the new element.
     * @param parentRelationshipProperties properties to include in parent relationship
     * @return unique identifier of the newly created element
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String createSoftwareCapability(NewElementOptions                     newElementOptions,
                                           Map<String, ClassificationProperties> initialClassifications,
                                           SoftwareCapabilityProperties          properties,
                                           RelationshipProperties                parentRelationshipProperties) throws InvalidParameterException,
                                                                                                                      PropertyServerException,
                                                                                                                      UserNotAuthorizedException
    {
        String elementGUID = softwareCapabilityHandler.createSoftwareCapability(connectorUserId, newElementOptions, initialClassifications, properties, parentRelationshipProperties);

        if (parentContext.getIntegrationReportWriter() != null)
        {
            parentContext.getIntegrationReportWriter().reportElementCreation(elementGUID);
        }

        return elementGUID;
    }


    /**
     * Create a new metadata element to represent a softwareCapability using an existing element as a template.
     * The template defines additional classifications and relationships that should be added to the new softwareCapability.
     *
     * @param templateOptions details of the element to create
     * @param templateGUID the unique identifier of the existing softwareCapability to copy (this will copy all the attachments such as nested content, schema
     *                     connection etc)
     * @param replacementProperties properties of the new metadata element.  These override the template values
     * @param placeholderProperties property name-to-property value map to replace any placeholder values in the
     *                              template element - and their anchored elements, which are also copied as part of this operation.
     * @param parentRelationshipProperties properties to include in parent relationship
     * @return unique identifier of the new metadata element
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createSoftwareCapabilityFromTemplate(TemplateOptions        templateOptions,
                                                       String                 templateGUID,
                                                       ElementProperties      replacementProperties,
                                                       Map<String, String>    placeholderProperties,
                                                       RelationshipProperties parentRelationshipProperties) throws InvalidParameterException,
                                                                                                                   UserNotAuthorizedException,
                                                                                                                   PropertyServerException
    {
        String elementGUID = softwareCapabilityHandler.createSoftwareCapabilityFromTemplate(connectorUserId, templateOptions, templateGUID, replacementProperties, placeholderProperties, parentRelationshipProperties);

        if (parentContext.getIntegrationReportWriter() != null)
        {
            parentContext.getIntegrationReportWriter().reportElementCreation(elementGUID);
        }

        return elementGUID;
    }


    /**
     * Update the properties of a softwareCapability.
     *
     * @param softwareCapabilityGUID       unique identifier of the softwareCapability (returned from create)
     * @param updateOptions provides a structure for the additional options when updating an element.
     * @param properties             properties for the element.
     * @return boolean - true if an update occurred
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public boolean updateSoftwareCapability(String                       softwareCapabilityGUID,
                                            UpdateOptions                updateOptions,
                                            SoftwareCapabilityProperties properties) throws InvalidParameterException,
                                                                                            PropertyServerException,
                                                                                            UserNotAuthorizedException
    {
        boolean updateOccurred = softwareCapabilityHandler.updateSoftwareCapability(connectorUserId, softwareCapabilityGUID, updateOptions, properties);

        if ((updateOccurred) && (parentContext.getIntegrationReportWriter() != null))
        {
            parentContext.getIntegrationReportWriter().reportElementUpdate(softwareCapabilityGUID);
        }

        return updateOccurred;
    }


    /**
     * Create a relationship that represents the use of an asset (typically a data or process asset) by
     * a softwareCapability.
     *
     * @param softwareCapabilityGUID       unique identifier of the softwareCapability
     * @param assetGUID           unique identifier of the destination softwareCapability
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void addAssetUse(String                       softwareCapabilityGUID,
                            String                       assetGUID,
                            MakeAnchorOptions            makeAnchorOptions,
                            CapabilityAssetUseProperties relationshipProperties) throws InvalidParameterException,
                                                                                        PropertyServerException,
                                                                                        UserNotAuthorizedException
    {
        softwareCapabilityHandler.addAssetUse(connectorUserId, softwareCapabilityGUID, assetGUID, makeAnchorOptions, relationshipProperties);
    }


    /**
     * Remove a CapabilityAssetUse relationship.
     *
     * @param softwareCapabilityGUID       unique identifier of the softwareCapability
     * @param assetGUID           unique identifier of the destination softwareCapability
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachAssetUse(String        softwareCapabilityGUID,
                               String        assetGUID,
                               DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                   PropertyServerException,
                                                                   UserNotAuthorizedException
    {
        softwareCapabilityHandler.detachAssetUse(connectorUserId, softwareCapabilityGUID, assetGUID, deleteOptions);
    }


    /**
     * Delete a softwareCapability.
     *
     * @param softwareCapabilityGUID       unique identifier of the element
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void deleteSoftwareCapability(String        softwareCapabilityGUID,
                                         DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException
    {
        softwareCapabilityHandler.deleteSoftwareCapability(connectorUserId, softwareCapabilityGUID, deleteOptions);

        if (parentContext.getIntegrationReportWriter() != null)
        {
            parentContext.getIntegrationReportWriter().reportElementDelete(softwareCapabilityGUID);
        }
    }


    /**
     * Returns the list of software capabilities with a particular name.
     *
     * @param name                   name of the element to return - match is full text match in qualifiedName, resourceName or displayName
     * @param queryOptions           multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getSoftwareCapabilitiesByName(String       name,
                                                                       QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                         PropertyServerException,
                                                                                                         UserNotAuthorizedException
    {
        return softwareCapabilityHandler.getSoftwareCapabilitiesByName(connectorUserId, name, queryOptions);
    }


    /**
     * Return the properties of a specific softwareCapability.
     *
     * @param softwareCapabilityGUID       unique identifier of the required element
     * @param getOptions multiple options to control the query
     * @return retrieved properties
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElement getSoftwareCapabilityByGUID(String     softwareCapabilityGUID,
                                                               GetOptions getOptions) throws InvalidParameterException,
                                                                                             PropertyServerException,
                                                                                             UserNotAuthorizedException
    {
        return softwareCapabilityHandler.getSoftwareCapabilityByGUID(connectorUserId, softwareCapabilityGUID, getOptions);
    }


    /**
     * Retrieve the list of software capability metadata elements that are attached to a specific infrastructure element.
     *
     * @param infrastructureGUID element to search for
     * @param queryOptions multiple options to control the query
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<OpenMetadataRootElement> getSoftwareCapabilitiesForInfrastructure(String       infrastructureGUID,
                                                                                  QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                                      UserNotAuthorizedException,
                                                                                                                      PropertyServerException
    {
        return softwareCapabilityHandler.getSoftwareCapabilitiesForInfrastructure(connectorUserId, infrastructureGUID, queryOptions);
    }


    /**
     * Retrieve the list of software capabilities metadata elements that contain the search string and show which solution components (if any) are attached to it.
     * The returned software capabilities include a list of the components that are associated with it.
     * The search string is treated as a regular expression.
     *
     * @param searchString           string to find in the properties
     * @param searchOptions multiple options to control the query
     * @return list of matching metadata elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<OpenMetadataRootElement> findSoftwareCapabilities(String        searchString,
                                                                  SearchOptions searchOptions) throws InvalidParameterException,
                                                                                                      UserNotAuthorizedException,
                                                                                                      PropertyServerException
    {
        return softwareCapabilityHandler.findSoftwareCapabilities(connectorUserId, searchString, searchOptions);
    }
}
