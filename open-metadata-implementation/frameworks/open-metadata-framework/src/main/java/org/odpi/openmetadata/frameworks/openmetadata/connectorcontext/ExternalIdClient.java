/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.connectorcontext;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.*;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.ExternalIdHandler;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.metadatarepositories.MetadataCollectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.externalidentifiers.ExternalIdLinkProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.externalidentifiers.ExternalIdProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Provides services for connectors to work with external id elements.
 */
public class ExternalIdClient extends ConnectorContextClientBase
{
    private final ExternalIdHandler  externalIdHandler;
    private final OpenMetadataClient openMetadataClient;


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
    public ExternalIdClient(ConnectorContextBase     parentContext,
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

        this.openMetadataClient = openMetadataClient;
        this.externalIdHandler  = new ExternalIdHandler(localServerName, auditLog, localServiceName, openMetadataClient);
    }


    /**
     * Create a new externalId.  It is linked to its scope as long as externalSourceGUID is not null.
     *
     * @param elementGUID element that the external id is for
     * @param externalIdLinkProperties properties for the relationship
     * @param externalIdProperties properties for the external Id itself.
     *
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void createExternalId(String                   elementGUID,
                                 ExternalIdLinkProperties externalIdLinkProperties,
                                 ExternalIdProperties     externalIdProperties) throws InvalidParameterException,
                                                                                       PropertyServerException,
                                                                                       UserNotAuthorizedException
    {
        NewElementOptions newElementOptions = new NewElementOptions(this.getMetadataSourceOptions());

        newElementOptions.setIsOwnAnchor(false);
        newElementOptions.setAnchorGUID(elementGUID);
        newElementOptions.setParentGUID(elementGUID);
        newElementOptions.setParentAtEnd1(true);
        newElementOptions.setParentRelationshipTypeName(OpenMetadataType.EXTERNAL_ID_LINK_RELATIONSHIP.typeName);

        this.createExternalId(newElementOptions,
                              null,
                              externalIdProperties,
                              externalIdLinkProperties);
    }

    /**
     * Create a new externalId.  It is linked to its scope as long as externalSourceGUID is not null.
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
    public String createExternalId(NewElementOptions                     newElementOptions,
                                   Map<String, ClassificationProperties> initialClassifications,
                                   ExternalIdProperties                  properties,
                                   RelationshipProperties                parentRelationshipProperties) throws InvalidParameterException,
                                                                                                              PropertyServerException,
                                                                                                              UserNotAuthorizedException
    {
        String elementGUID = externalIdHandler.createExternalId(connectorUserId, newElementOptions, initialClassifications, properties, parentRelationshipProperties);

        /*
         * Link the new external Id to its scope.
         */
        if ((elementGUID != null) && (externalSourceGUID != null))
        {
            openMetadataClient.createRelatedElementsInStore(connectorUserId,
                                                            OpenMetadataType.SCOPED_BY_RELATIONSHIP.typeName,
                                                            elementGUID,
                                                            externalSourceGUID,
                                                            this.getMetadataSourceOptions(),
                                                            null);
        }

        if (parentContext.getIntegrationReportWriter() != null)
        {
            parentContext.getIntegrationReportWriter().reportElementCreation(elementGUID);
        }

        return elementGUID;
    }


    /**
     * Create a new metadata element to represent an external id using an existing element as a template.
     * The template defines additional classifications and relationships that should be added to the new external id.
     *
     * @param templateOptions details of the element to create
     * @param templateGUID the unique identifier of the existing external id to copy (this will copy all the attachments such as nested content, schema
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
    public String createExternalIdFromTemplate(TemplateOptions        templateOptions,
                                               String                 templateGUID,
                                               ElementProperties      replacementProperties,
                                               Map<String, String>    placeholderProperties,
                                               RelationshipProperties parentRelationshipProperties) throws InvalidParameterException,
                                                                                                           UserNotAuthorizedException,
                                                                                                           PropertyServerException
    {
        String elementGUID = externalIdHandler.createExternalIdFromTemplate(connectorUserId, templateOptions, templateGUID, replacementProperties, placeholderProperties, parentRelationshipProperties);

        if (parentContext.getIntegrationReportWriter() != null)
        {
            parentContext.getIntegrationReportWriter().reportElementCreation(elementGUID);
        }

        return elementGUID;
    }


    /**
     * Update the properties of an external id.
     *
     * @param externalIdGUID       unique identifier of the external id (returned from create)
     * @param updateOptions provides a structure for the additional options when updating an element.
     * @param properties             properties for the element.
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void updateExternalId(String               externalIdGUID,
                                 UpdateOptions        updateOptions,
                                 ExternalIdProperties properties) throws InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException
    {
        externalIdHandler.updateExternalId(connectorUserId, externalIdGUID, updateOptions, properties);

        if (parentContext.getIntegrationReportWriter() != null)
        {
            parentContext.getIntegrationReportWriter().reportElementUpdate(externalIdGUID);
        }
    }


    /**
     * Attach an external id to an open metadata element.
     *
     * @param externalIdGUID            unique identifier of the external id
     * @param itAssetGUID             unique identifier of the infrastructure asset
     * @param metadataSourceOptions  options to control access to open metadata
     * @param relationshipProperties  description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkExternalIdToElement(String                   itAssetGUID,
                                        String                   externalIdGUID,
                                        MetadataSourceOptions    metadataSourceOptions,
                                        ExternalIdLinkProperties relationshipProperties) throws InvalidParameterException,
                                                                                                  PropertyServerException,
                                                                                                  UserNotAuthorizedException
    {
        externalIdHandler.linkExternalIdToElement(connectorUserId, itAssetGUID, externalIdGUID, metadataSourceOptions, relationshipProperties);
    }


    /**
     * Detach an external id from an open metadata element.
     *
     * @param itAssetGUID            unique identifier of the infrastructure asset
     * @param externalIdGUID       unique identifier of the external id
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachExternalIdFromElement(String        itAssetGUID,
                                            String        externalIdGUID,
                                            DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        externalIdHandler.detachExternalIdFromElement(connectorUserId, itAssetGUID, externalIdGUID, deleteOptions);
    }


    /**
     * Sift through the related external identifiers looking for the one that matches the system being synchronized with.
     * Ideally the external identifiers are catalogued under the external source GUID for this system - in which case
     * it is a simple look-up.  Otherwise, we need to retrieve the external identifier and validate whether it is
     * connected to the desired metadata collection.
     *
     * @param metadataElement starting element
     * @param externalSourceGUID unique identifier of the metadata source to locate
     * @param externalKey unique identifier of the external element
     *
     * @return the matching related external identifier
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public RelatedMetadataElementSummary getRelatedExternalId(OpenMetadataRootElement metadataElement,
                                                              String                  externalSourceGUID,
                                                              String                  externalKey) throws InvalidParameterException,
                                                                                                                 PropertyServerException,
                                                                                                                 UserNotAuthorizedException
    {
        final String methodName = "getRelatedExternalId";
        final String guidParameterName = "externalSourceGUID";

        propertyHelper.validateGUID(externalSourceGUID, guidParameterName, methodName);

        List<RelatedMetadataElementSummary> relatedExternalIdentifiers = metadataElement.getAlsoKnownAs();

        if (relatedExternalIdentifiers != null)
        {
            for (RelatedMetadataElementSummary relatedExternalIdentifier : relatedExternalIdentifiers)
            {
                if (relatedExternalIdentifier != null)
                {
                    if (relatedExternalIdentifier.getRelatedElement().getElementHeader().getOrigin().getHomeMetadataCollectionId().equals(externalSourceGUID))
                    {
                        /*
                         * This element is from the right metadata collection - is it the right element since
                         * mappings can be many to many.
                         */
                        if (externalKey == null)
                        {
                            /*
                             * Mapping is one to one so do not need to check key
                             */
                            return relatedExternalIdentifier;
                        }
                        else if ((relatedExternalIdentifier.getRelatedElement().getProperties() instanceof ExternalIdProperties externalIdProperties) &&
                                (externalKey.equals(externalIdProperties.getKey())))
                        {
                            return relatedExternalIdentifier;
                        }
                    }
                    else if (relatedExternalIdentifier.getRelatedElement().getElementHeader().getOrigin().getHomeMetadataCollectionId() == null)
                    {
                        /*
                         * The external Id is not registered in the external metadata collection, but is it linked to the external metadata collection
                         * via the ScopedBy relationship?
                         */
                        OpenMetadataRootElement externalId = this.getExternalIdByGUID(relatedExternalIdentifier.getRelatedElement().getElementHeader().getGUID(), this.getGetOptions());

                        if ((externalId != null) &&
                                (externalId.getProperties() instanceof ExternalIdProperties externalIdProperties) &&
                                ((externalKey == null) || (externalKey.equals(externalIdProperties.getKey()))))
                        {
                            if (externalId.getRelevantToScopes() != null)
                            {
                                for (RelatedMetadataElementSummary metadataCollection : externalId.getRelevantToScopes())
                                {
                                    if ((metadataCollection != null) && (metadataCollection.getRelatedElement().getProperties() instanceof MetadataCollectionProperties metadataCollectionProperties))
                                    {
                                        if (externalSourceGUID.equals(metadataCollectionProperties.getManagedMetadataCollectionId()))
                                        {
                                            return relatedExternalIdentifier;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return null;
    }


    /**
     * Confirm that this element has been synchronized at this time.
     *
     * @param metadataElement starting element
     * @param externalSourceGUID unique identifier of the metadata source to locate
     * @param externalKey unique identifier of the external element
     *
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void confirmSynchronization(OpenMetadataRootElement metadataElement,
                                       String                  externalSourceGUID,
                                       String                  externalKey) throws InvalidParameterException,
                                                                                   PropertyServerException,
                                                                                   UserNotAuthorizedException
    {
        final String methodName = "confirmSynchronization";

        RelatedMetadataElementSummary relatedExternalId = this.getRelatedExternalId(metadataElement, externalSourceGUID, externalKey);

        if (relatedExternalId != null)
        {
            this.confirmSynchronization(relatedExternalId.getRelationshipHeader().getGUID());
        }
        else
        {
            auditLog.logMessage(methodName,
                                OMFAuditCode.MISSING_CORRELATION.getMessageDefinition(parentContext.connectorName,
                                                                                      metadataElement.getElementHeader().getType().getTypeName(),
                                                                                      metadataElement.getElementHeader().getGUID(),
                                                                                      externalSourceGUID,
                                                                                      externalKey));
            throw new PropertyServerException(OMFErrorCode.MISSING_CORRELATION.getMessageDefinition(parentContext.connectorName,
                                                                                                    metadataElement.getElementHeader().getType().getTypeName(),
                                                                                                    metadataElement.getElementHeader().getGUID(),
                                                                                                    externalSourceGUID,
                                                                                                    externalKey),
                                              this.getClass().getName(),
                                              methodName);
        }
    }


    /**
     * Confirm that the values of a particular metadata element have been synchronized.  This is important
     * from an audit point of view, and to allow bidirectional updates of metadata using optimistic locking.
     *
     * @param externalIdLinkGUID unique identifier of the ExternalIdLink relationship to update
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public void confirmSynchronization(String externalIdLinkGUID) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        ExternalIdLinkProperties relationshipProperties = new ExternalIdLinkProperties();
        relationshipProperties.setLastSynchronized(new Date());

        externalIdHandler.confirmSynchronization(connectorUserId,
                                                 externalIdLinkGUID,
                                                 this.getUpdateOptions(true),
                                                 relationshipProperties);
    }


    /**
     * Delete an external id.
     *
     * @param externalIdGUID       unique identifier of the element
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void deleteExternalId(String        externalIdGUID,
                                 DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException
    {
        externalIdHandler.deleteExternalId(connectorUserId, externalIdGUID, deleteOptions);

        if (parentContext.getIntegrationReportWriter() != null)
        {
            parentContext.getIntegrationReportWriter().reportElementDelete(externalIdGUID);
        }
    }


    /**
     * Returns the list of external ids with a particular name.
     *
     * @param name                   name of the element to return - match is full text match in qualifiedName, resourceName or displayName
     * @param queryOptions           multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getExternalIdsByName(String       name,
                                                              QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                PropertyServerException,
                                                                                                UserNotAuthorizedException
    {
        return externalIdHandler.getExternalIdsByName(connectorUserId, name, queryOptions);
    }


    /**
     * Return the properties of a specific external id.
     *
     * @param externalIdGUID       unique identifier of the required element
     * @param getOptions multiple options to control the query
     * @return retrieved properties
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElement getExternalIdByGUID(String     externalIdGUID,
                                                       GetOptions getOptions) throws InvalidParameterException,
                                                                                     PropertyServerException,
                                                                                     UserNotAuthorizedException
    {
        return externalIdHandler.getExternalIdByGUID(connectorUserId, externalIdGUID, getOptions);
    }


    /**
     * Retrieve the list of external id metadata elements with a matching identifier.
     * There are no wildcards supported on this request.
     *
     * @param identifier identifier to search for
     * @param queryOptions           multiple options to control the query
     *
     * @return a list of elements
     *
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getExternalIdsByIdentifier(String       identifier,
                                                                    QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                      UserNotAuthorizedException,
                                                                                                      PropertyServerException
    {
        return externalIdHandler.getExternalIdsByIdentifier(connectorUserId, identifier, queryOptions);
    }


    /**
     * Retrieve the list of external id metadata elements that are attached to a specific element.
     *
     * @param elementGUID element to search for
     * @param queryOptions multiple options to control the query
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<OpenMetadataRootElement> getExternalIdsForElement(String       elementGUID,
                                                                  QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException
    {
        return externalIdHandler.getExternalIdsForElement(connectorUserId, elementGUID, queryOptions);
    }


    /**
     * Retrieve the list of external ids metadata elements that contain the search string and show which solution components (if any) are attached to it.
     * The returned external ids include a list of the components that are associated with it.
     * The search string is treated as a regular expression.
     *
     * @param searchString           string to find in the properties
     * @param searchOptions multiple options to control the query
     * @return list of matching metadata elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<OpenMetadataRootElement> findExternalIds(String        searchString,
                                                         SearchOptions searchOptions) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        return externalIdHandler.findExternalIds(connectorUserId, searchString, searchOptions);
    }
}
