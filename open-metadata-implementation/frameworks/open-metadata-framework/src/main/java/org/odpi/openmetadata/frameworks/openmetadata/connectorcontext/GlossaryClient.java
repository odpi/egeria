/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.connectorcontext;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.CollectionHandler;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.EditingCollectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.StagingCollectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.glossaries.*;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.List;
import java.util.Map;

/**
 * Provides services for connectors to work with Schema Type elements.
 */
public class GlossaryClient extends ConnectorContextClientBase
{
    private final CollectionHandler collectionHandler;


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
    public GlossaryClient(ConnectorContextBase     parentContext,
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

        this.collectionHandler = new CollectionHandler(localServerName, auditLog, localServiceName, openMetadataClient, OpenMetadataType.GLOSSARY.typeName);
    }



    /**
     * Create a new glossary.
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
    public String createGlossary(NewElementOptions                     newElementOptions,
                                 Map<String, ClassificationProperties> initialClassifications,
                                 GlossaryProperties                    properties,
                                 RelationshipProperties                parentRelationshipProperties) throws InvalidParameterException,
                                                                                                            PropertyServerException,
                                                                                                            UserNotAuthorizedException
    {
        String elementGUID = collectionHandler.createCollection(connectorUserId, newElementOptions, initialClassifications, properties, parentRelationshipProperties);

        if (parentContext.getIntegrationReportWriter() != null)
        {
            parentContext.getIntegrationReportWriter().reportElementCreation(elementGUID);
        }

        return elementGUID;
    }


    /**
     * Create a new metadata element to represent a glossary using an existing element as a template.
     * The template defines additional classifications and relationships that should be added to the new glossary.
     *
     * @param templateOptions details of the element to create
     * @param templateGUID the unique identifier of the existing glossary to copy (this will copy all the attachments such as nested content, schema
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
    public String createGlossaryFromTemplate(TemplateOptions        templateOptions,
                                             String                 templateGUID,
                                             ElementProperties      replacementProperties,
                                             Map<String, String>    placeholderProperties,
                                             RelationshipProperties parentRelationshipProperties) throws InvalidParameterException,
                                                                                                         UserNotAuthorizedException,
                                                                                                         PropertyServerException
    {
        String elementGUID = collectionHandler.createCollectionFromTemplate(connectorUserId, templateOptions, templateGUID, replacementProperties, placeholderProperties, parentRelationshipProperties);

        if (parentContext.getIntegrationReportWriter() != null)
        {
            parentContext.getIntegrationReportWriter().reportElementCreation(elementGUID);
        }

        return elementGUID;
    }


    /**
     * Update the properties of a glossary.
     *
     * @param glossaryGUID       unique identifier of the glossary (returned from create)
     * @param updateOptions provides a structure for the additional options when updating an element.
     * @param properties             properties for the element.
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void updateGlossary(String             glossaryGUID,
                               UpdateOptions      updateOptions,
                               GlossaryProperties properties) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException
    {
        collectionHandler.updateCollection(connectorUserId, glossaryGUID, updateOptions, properties);

        if (parentContext.getIntegrationReportWriter() != null)
        {
            parentContext.getIntegrationReportWriter().reportElementUpdate(glossaryGUID);
        }
    }


    /**
     * Classify the glossary to indicate that it is an editing glossary - this means it is
     * a collection of glossary updates that will be merged into its source glossary.
     *
     * @param glossaryGUID    unique identifier of the glossary.
     * @param properties            properties for the classification
     * @param metadataSourceOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void setGlossaryAsEditingGlossary(String                    glossaryGUID,
                                             EditingCollectionProperties properties,
                                             MetadataSourceOptions     metadataSourceOptions) throws InvalidParameterException,
                                                                                                     PropertyServerException,
                                                                                                     UserNotAuthorizedException
    {
        collectionHandler.setEditingCollection(connectorUserId, glossaryGUID, properties, metadataSourceOptions);
    }


    /**
     * Remove the editing glossary classification from the glossary.
     *
     * @param glossaryGUID    unique identifier of the glossary.
     * @param metadataSourceOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void clearGlossaryAsEditingGlossary(String                glossaryGUID,
                                               MetadataSourceOptions metadataSourceOptions) throws InvalidParameterException,
                                                                                                   PropertyServerException,
                                                                                                   UserNotAuthorizedException
    {
        collectionHandler.clearEditingCollection(connectorUserId, glossaryGUID, metadataSourceOptions);
    }



    /**
     * Classify the glossary to indicate that it is a staging glossary.
     *
     * @param glossaryGUID    unique identifier of the glossary.
     * @param properties            properties for the classification
     * @param metadataSourceOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void setGlossaryAsStagingGlossary(String                    glossaryGUID,
                                             StagingCollectionProperties properties,
                                             MetadataSourceOptions     metadataSourceOptions) throws InvalidParameterException,
                                                                                                     PropertyServerException,
                                                                                                     UserNotAuthorizedException
    {
        collectionHandler.setStagingCollection(connectorUserId, glossaryGUID, properties, metadataSourceOptions);
    }


    /**
     * Remove the staging glossary classification from the glossary.
     *
     * @param glossaryGUID    unique identifier of the glossary.
     * @param metadataSourceOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void clearGlossaryAsStagingGlossary(String                glossaryGUID,
                                               MetadataSourceOptions metadataSourceOptions) throws InvalidParameterException,
                                                                                                   PropertyServerException,
                                                                                                   UserNotAuthorizedException
    {
        collectionHandler.clearStagingCollection(connectorUserId, glossaryGUID, metadataSourceOptions);
    }


    /**
     * Classify the glossary to indicate that it can be used as a taxonomy.
     * This means each term is attached to one, and only one category and the categories are organized as a hierarchy
     * with a single root category.
     * Taxonomies are used as a way of organizing assets and other related metadata.  The terms in the taxonomy
     * are linked to the assets etc. and as such they are logically categorized by the linked category.
     *
     * @param glossaryGUID    unique identifier of the glossary.
     * @param properties            properties for the classification
     * @param metadataSourceOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void setGlossaryAsTaxonomy(String                glossaryGUID,
                                      TaxonomyProperties    properties,
                                      MetadataSourceOptions metadataSourceOptions) throws InvalidParameterException,
                                                                                          PropertyServerException,
                                                                                          UserNotAuthorizedException
    {
        collectionHandler.setGlossaryAsTaxonomy(connectorUserId, glossaryGUID, properties, metadataSourceOptions);
    }


    /**
     * Remove the taxonomy glossary classification from the glossary.
     *
     * @param glossaryGUID    unique identifier of the glossary.
     * @param metadataSourceOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void clearGlossaryAsTaxonomy(String                glossaryGUID,
                                        MetadataSourceOptions metadataSourceOptions) throws InvalidParameterException,
                                                                                            PropertyServerException,
                                                                                            UserNotAuthorizedException
    {
        collectionHandler.clearGlossaryAsTaxonomy(connectorUserId, glossaryGUID, metadataSourceOptions);
    }



    /**
     * Classify a glossary to declare that it has no two GlossaryTerm definitions with
     * the same name.  This means there is only one definition for each term.  Typically, the terms are also of a similar
     * level of granularity and are limited to a specific scope of use.
     * Canonical vocabularies are used to semantically classify assets in an unambiguous way.
     *
     * @param glossaryGUID    unique identifier of the glossary.
     * @param properties            properties for the classification
     * @param metadataSourceOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void setGlossaryAsCanonical(String                        glossaryGUID,
                                       CanonicalVocabularyProperties properties,
                                       MetadataSourceOptions         metadataSourceOptions) throws InvalidParameterException,
                                                                                                   PropertyServerException,
                                                                                                   UserNotAuthorizedException
    {
        collectionHandler.setGlossaryAsCanonical(connectorUserId, glossaryGUID, properties, metadataSourceOptions);
    }


    /**
     * Remove the canonical designation from the glossary.
     *
     * @param glossaryGUID    unique identifier of the glossary.
     * @param metadataSourceOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void clearGlossaryAsCanonical(String                glossaryGUID,
                                         MetadataSourceOptions metadataSourceOptions) throws InvalidParameterException,
                                                                                             PropertyServerException,
                                                                                             UserNotAuthorizedException
    {
        collectionHandler.clearGlossaryAsCanonical(connectorUserId, glossaryGUID, metadataSourceOptions);
    }


    /**
     * Delete a glossary.
     *
     * @param glossaryGUID       unique identifier of the element
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void deleteGlossary(String        glossaryGUID,
                               DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                   PropertyServerException,
                                                                   UserNotAuthorizedException
    {
        collectionHandler.deleteCollection(connectorUserId, glossaryGUID, deleteOptions);

        if (parentContext.getIntegrationReportWriter() != null)
        {
            parentContext.getIntegrationReportWriter().reportElementDelete(glossaryGUID);
        }
    }


    /**
     * Returns the list of glossaries with a particular name.
     *
     * @param name                   name of the element to return - match is full text match in qualifiedName, resourceName or displayName
     * @param queryOptions           multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getGlossariesByName(String       name,
                                                             QueryOptions queryOptions) throws InvalidParameterException,
                                                                                               PropertyServerException,
                                                                                               UserNotAuthorizedException
    {
        return collectionHandler.getCollectionsByName(connectorUserId, name, queryOptions);
    }


    /**
     * Return the properties of a specific glossary.
     *
     * @param glossaryGUID       unique identifier of the required element
     * @param getOptions multiple options to control the query
     * @return retrieved properties
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElement getGlossaryByGUID(String     glossaryGUID,
                                                     GetOptions getOptions) throws InvalidParameterException,
                                                                                   PropertyServerException,
                                                                                   UserNotAuthorizedException
    {
        return collectionHandler.getCollectionByGUID(connectorUserId, glossaryGUID, getOptions);
    }


    /**
     * Retrieve the list of glossaries metadata elements that contain the search string and show which solution components (if any) are attached to it.
     * The returned glossaries include a list of the components that are associated with it.
     * The search string is treated as a regular expression.
     *
     * @param searchString           string to find in the properties
     * @param searchOptions multiple options to control the query
     * @return list of matching metadata elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<OpenMetadataRootElement> findGlossaries(String        searchString,
                                                        SearchOptions searchOptions) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        return collectionHandler.findCollections(connectorUserId, searchString, searchOptions);
    }
}
