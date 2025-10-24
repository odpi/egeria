/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.connectorcontext;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.ExternalReferenceHandler;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.externalreferences.CitedDocumentLinkProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.externalreferences.ExternalReferenceLinkProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.externalreferences.ExternalReferenceProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.externalreferences.MediaReferenceProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;

import java.util.List;
import java.util.Map;

/**
 * Provides services for connectors to work with external reference elements.
 */
public class ExternalReferenceClient extends ConnectorContextClientBase
{
    private final ExternalReferenceHandler externalReferenceHandler;


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
    public ExternalReferenceClient(ConnectorContextBase     parentContext,
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

        this.externalReferenceHandler = new ExternalReferenceHandler(localServerName, auditLog, localServiceName, openMetadataClient);
    }


    /**
     * Create a new external reference.
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
    public String createExternalReference(NewElementOptions                     newElementOptions,
                                          Map<String, ClassificationProperties> initialClassifications,
                                          ExternalReferenceProperties           properties,
                                          RelationshipProperties                parentRelationshipProperties) throws InvalidParameterException,
                                                                                                                     PropertyServerException,
                                                                                                                     UserNotAuthorizedException
    {
        String elementGUID = externalReferenceHandler.createExternalReference(connectorUserId, newElementOptions, initialClassifications, properties, parentRelationshipProperties);

        if (parentContext.getIntegrationReportWriter() != null)
        {
            parentContext.getIntegrationReportWriter().reportElementCreation(elementGUID);
        }

        return elementGUID;
    }


    /**
     * Create a new metadata element to represent an external reference using an existing element as a template.
     * The template defines additional classifications and relationships that should be added to the new external reference.
     *
     * @param templateOptions details of the element to create
     * @param templateGUID the unique identifier of the existing external reference to copy (this will copy all the attachments such as nested content, schema
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
    public String createExternalReferenceFromTemplate(TemplateOptions        templateOptions,
                                                      String                 templateGUID,
                                                      ElementProperties      replacementProperties,
                                                      Map<String, String>    placeholderProperties,
                                                      RelationshipProperties parentRelationshipProperties) throws InvalidParameterException,
                                                                                                                  UserNotAuthorizedException,
                                                                                                                  PropertyServerException
    {
        String elementGUID = externalReferenceHandler.createExternalReferenceFromTemplate(connectorUserId, templateOptions, templateGUID, replacementProperties, placeholderProperties, parentRelationshipProperties);

        if (parentContext.getIntegrationReportWriter() != null)
        {
            parentContext.getIntegrationReportWriter().reportElementCreation(elementGUID);
        }

        return elementGUID;
    }


    /**
     * Update the properties of an external reference.
     *
     * @param externalReferenceGUID       unique identifier of the external reference (returned from create)
     * @param updateOptions provides a structure for the additional options when updating an element.
     * @param properties             properties for the element.
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void updateExternalReference(String                      externalReferenceGUID,
                                        UpdateOptions               updateOptions,
                                        ExternalReferenceProperties properties) throws InvalidParameterException,
                                                                                       PropertyServerException,
                                                                                       UserNotAuthorizedException
    {
        externalReferenceHandler.updateExternalReference(connectorUserId, externalReferenceGUID, updateOptions, properties);

        if (parentContext.getIntegrationReportWriter() != null)
        {
            parentContext.getIntegrationReportWriter().reportElementUpdate(externalReferenceGUID);
        }
    }




    /**
     * Attach an external reference to an element.
     *
     * @param elementGUID          unique identifier of the element
     * @param externalReferenceGUID          unique identifier of the external reference
     * @param metadataSourceOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkExternalReference(String                     elementGUID,
                                      String                     externalReferenceGUID,
                                      MetadataSourceOptions      metadataSourceOptions,
                                      ExternalReferenceLinkProperties relationshipProperties) throws InvalidParameterException,
                                                                                                     PropertyServerException,
                                                                                                     UserNotAuthorizedException
    {
        externalReferenceHandler.linkExternalReference(connectorUserId, elementGUID, externalReferenceGUID, metadataSourceOptions, relationshipProperties);
    }


    /**
     * Detach an external reference from an element.
     *
     * @param elementGUID          unique identifier of the first external reference
     * @param externalReferenceGUID          unique identifier of the second external reference
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachExternalReference(String        elementGUID,
                                        String        externalReferenceGUID,
                                        DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                            PropertyServerException,
                                                                            UserNotAuthorizedException
    {
        externalReferenceHandler.detachExternalReference(connectorUserId, elementGUID, externalReferenceGUID, deleteOptions);
    }


    /**
     * Attach an external media reference to an element.
     *
     * @param elementGUID          unique identifier of the first external reference
     * @param externalReferenceGUID          unique identifier of the second external reference
     * @param metadataSourceOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkMediaReference(String                   elementGUID,
                                   String                   externalReferenceGUID,
                                   MetadataSourceOptions    metadataSourceOptions,
                                   MediaReferenceProperties relationshipProperties) throws InvalidParameterException,
                                                                                           PropertyServerException,
                                                                                           UserNotAuthorizedException
    {
        externalReferenceHandler.linkMediaReference(connectorUserId, elementGUID, externalReferenceGUID, metadataSourceOptions, relationshipProperties);
    }


    /**
     * Detach an external media reference from an element.
     *
     * @param elementGUID          unique identifier of the first external reference
     * @param externalReferenceGUID          unique identifier of the second external reference
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachMediaReference(String        elementGUID,
                                     String        externalReferenceGUID,
                                     DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException
    {
        externalReferenceHandler.detachMediaReference(connectorUserId, elementGUID, externalReferenceGUID, deleteOptions);
    }


    /**
     * Attach an element to its external document reference.
     *
     * @param elementGUID       unique identifier of the element to connect
     * @param externalReferenceGUID            unique identifier of the external reference
     * @param metadataSourceOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkCitedDocumentReference(String                      elementGUID,
                                           String                      externalReferenceGUID,
                                           MetadataSourceOptions       metadataSourceOptions,
                                           CitedDocumentLinkProperties relationshipProperties) throws InvalidParameterException,
                                                                                                      PropertyServerException,
                                                                                                      UserNotAuthorizedException
    {
        externalReferenceHandler.linkCitedDocumentReference(connectorUserId, elementGUID, externalReferenceGUID, metadataSourceOptions, relationshipProperties);
    }


    /**
     * Detach an element from its external document reference.
     *
     * @param elementGUID              unique identifier of the element
     * @param externalReferenceGUID          unique identifier of the external reference
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachCitedDocumentReference(String        elementGUID,
                                             String        externalReferenceGUID,
                                             DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                 PropertyServerException,
                                                                                 UserNotAuthorizedException
    {
        externalReferenceHandler.detachCitedDocumentReference(connectorUserId, elementGUID, externalReferenceGUID, deleteOptions);
    }


    /**
     * Delete an external reference.
     *
     * @param externalReferenceGUID       unique identifier of the element
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void deleteExternalReference(String        externalReferenceGUID,
                                        DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                            PropertyServerException,
                                                                            UserNotAuthorizedException
    {
        externalReferenceHandler.deleteExternalReference(connectorUserId, externalReferenceGUID, deleteOptions);

        if (parentContext.getIntegrationReportWriter() != null)
        {
            parentContext.getIntegrationReportWriter().reportElementDelete(externalReferenceGUID);
        }
    }


    /**
     * Returns the list of external references with a particular name.
     *
     * @param name                   name of the element to return - match is full text match in qualifiedName, resourceName or displayName
     * @param queryOptions           multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getExternalReferencesByName(String       name,
                                                                     QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                       PropertyServerException,
                                                                                                       UserNotAuthorizedException
    {
        return externalReferenceHandler.getExternalReferencesByName(connectorUserId, name, queryOptions);
    }


    /**
     * Return the properties of a specific external reference.
     *
     * @param externalReferenceGUID       unique identifier of the required element
     * @param getOptions multiple options to control the query
     * @return retrieved properties
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElement getExternalReferenceByGUID(String     externalReferenceGUID,
                                                              GetOptions getOptions) throws InvalidParameterException,
                                                                                            PropertyServerException,
                                                                                            UserNotAuthorizedException
    {
        return externalReferenceHandler.getExternalReferenceByGUID(connectorUserId, externalReferenceGUID, getOptions);
    }


    /**
     * Retrieve the list of external references metadata elements that contain the search string and show which solution components (if any) are attached to it.
     * The returned external references include a list of the components that are associated with it.
     * The search string is treated as a regular expression.
     *
     * @param searchString           string to find in the properties
     * @param searchOptions multiple options to control the query
     * @return list of matching metadata elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<OpenMetadataRootElement> findExternalReferences(String        searchString,
                                                                SearchOptions searchOptions) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException
    {
        return externalReferenceHandler.findExternalReferences(connectorUserId, searchString, searchOptions);
    }
}
