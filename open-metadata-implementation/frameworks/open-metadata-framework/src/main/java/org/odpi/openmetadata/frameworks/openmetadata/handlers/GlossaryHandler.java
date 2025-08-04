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
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.glossaries.*;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * GlossaryHandler describes how to maintain and query glossaries.
 */
public class GlossaryHandler extends OpenMetadataHandlerBase
{
    /**
     * Create a new handler.
     *
     * @param localServerName        name of this server (view server)
     * @param auditLog               logging destination
     * @param serviceName            local service name
     * @param openMetadataClient     access to open metadata
     */
    public GlossaryHandler(String             localServerName,
                           AuditLog           auditLog,
                           String             serviceName,
                           OpenMetadataClient openMetadataClient)
    {
        super(localServerName, auditLog, serviceName,openMetadataClient, OpenMetadataType.GLOSSARY.typeName);
    }


    /**
     * Create a new metadata element to represent a glossary.
     * Each term is both anchored and linked to a single glossary.  They are owned by this glossary and if the
     * glossary is deleted, any linked terms are deleted as well.
     *
     * @param userId                       userId of user making request.
     * @param newElementOptions details of the element to create
     * @param initialClassifications map of classification names to classification properties to include in the entity creation request
     * @param properties                   properties for the new element.
     * @param parentRelationshipProperties properties to include in parent relationship
     * @return unique identifier of the newly created element
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String createGlossary(String                                userId,
                                 NewElementOptions                     newElementOptions,
                                 Map<String, ClassificationProperties> initialClassifications,
                                 GlossaryProperties                    properties,
                                 RelationshipProperties                parentRelationshipProperties) throws InvalidParameterException,
                                                                                                            PropertyServerException,
                                                                                                            UserNotAuthorizedException
    {
        final String methodName = "createGlossary";

        return super.createNewElement(userId,
                                      newElementOptions,
                                      initialClassifications,
                                      properties,
                                      parentRelationshipProperties,
                                      methodName);
    }


    /**
     * Create a new metadata element to represent a glossary using an existing element as a template.
     * The template defines additional classifications and relationships that should be added to the new glossary.
     * Each term is both anchored and linked to a single glossary.  They are owned by this glossary and if the
     * glossary is deleted, any linked terms are deleted as well.
     *
     * @param userId                       calling user
     * @param templateOptions details of the element to create
     * @param templateGUID the unique identifier of the existing asset to copy (this will copy all the attachments such as nested content, schema
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
    public String createGlossaryFromTemplate(String                 userId,
                                             TemplateOptions        templateOptions,
                                             String                 templateGUID,
                                             ElementProperties      replacementProperties,
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
     * Update the properties of a glossary.
     *
     * @param userId                 userId of user making request.
     * @param glossaryGUID      unique identifier of the glossary (returned from create)
     * @param updateOptions provides a structure for the additional options when updating an element.
     * @param properties             properties for the element.
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void updateGlossary(String             userId,
                               String             glossaryGUID,
                               UpdateOptions      updateOptions,
                               GlossaryProperties properties) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException
    {
        final String methodName = "updateGlossary";
        final String guidParameterName = "glossaryGUID";

        super.updateElement(userId,
                            glossaryGUID,
                            guidParameterName,
                            updateOptions,
                            properties,
                            methodName);
    }


    /**
     * Classify the glossary to indicate that it is an editing glossary - this means it is
     * a collection of glossary updates that will be merged into its source glossary.
     *
     * @param userId                 userId of user making request.
     * @param glossaryGUID    unique identifier of the glossary.
     * @param properties            properties for the classification
     * @param metadataSourceOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void setGlossaryAsEditingGlossary(String                    userId,
                                             String                    glossaryGUID,
                                             EditingGlossaryProperties properties,
                                             MetadataSourceOptions     metadataSourceOptions) throws InvalidParameterException,
                                                                                                     PropertyServerException,
                                                                                                     UserNotAuthorizedException
    {
        final String methodName = "setGlossaryAsEditingGlossary";
        final String guidParameterName = "glossaryGUID";
        final String propertiesParameterName = "properties";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(glossaryGUID, guidParameterName, methodName);
        propertyHelper.validateObject(properties, propertiesParameterName, methodName);

        openMetadataClient.classifyMetadataElementInStore(userId,
                                                          glossaryGUID,
                                                          OpenMetadataType.EDITING_GLOSSARY_CLASSIFICATION.typeName,
                                                          metadataSourceOptions,
                                                          classificationBuilder.getNewElementProperties(properties));
    }


    /**
     * Remove the editing glossary classification from the glossary.
     *
     * @param userId                 userId of user making request.
     * @param glossaryGUID    unique identifier of the glossary.
     * @param metadataSourceOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void clearGlossaryAsEditingGlossary(String                userId,
                                               String                glossaryGUID,
                                               MetadataSourceOptions metadataSourceOptions) throws InvalidParameterException,
                                                                                                   PropertyServerException,
                                                                                                   UserNotAuthorizedException
    {
        final String methodName = "clearGlossaryAsEditingGlossary";
        final String guidParameterName = "glossaryGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(glossaryGUID, guidParameterName, methodName);

        openMetadataClient.declassifyMetadataElementInStore(userId,
                                                            glossaryGUID,
                                                            OpenMetadataType.EDITING_GLOSSARY_CLASSIFICATION.typeName,
                                                            metadataSourceOptions);
    }



    /**
     * Classify the glossary to indicate that it is a staging glossary.
     *
     * @param userId                 userId of user making request.
     * @param glossaryGUID    unique identifier of the glossary.
     * @param properties            properties for the classification
     * @param metadataSourceOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void setGlossaryAsStagingGlossary(String                    userId,
                                             String                    glossaryGUID,
                                             StagingGlossaryProperties properties,
                                             MetadataSourceOptions     metadataSourceOptions) throws InvalidParameterException,
                                                                                                     PropertyServerException,
                                                                                                     UserNotAuthorizedException
    {
        final String methodName = "setGlossaryAsStagingGlossary";
        final String guidParameterName = "glossaryGUID";
        final String propertiesParameterName = "properties";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(glossaryGUID, guidParameterName, methodName);
        propertyHelper.validateObject(properties, propertiesParameterName, methodName);

        openMetadataClient.classifyMetadataElementInStore(userId,
                                                          glossaryGUID,
                                                          OpenMetadataType.STAGING_GLOSSARY_CLASSIFICATION.typeName,
                                                          metadataSourceOptions,
                                                          classificationBuilder.getNewElementProperties(properties));
    }


    /**
     * Remove the staging glossary classification from the glossary.
     *
     * @param userId                 userId of user making request.
     * @param glossaryGUID    unique identifier of the glossary.
     * @param metadataSourceOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void clearGlossaryAsStagingGlossary(String                userId,
                                               String                glossaryGUID,
                                               MetadataSourceOptions metadataSourceOptions) throws InvalidParameterException,
                                                                                                   PropertyServerException,
                                                                                                   UserNotAuthorizedException
    {
        final String methodName = "clearGlossaryAsStagingGlossary";
        final String guidParameterName = "glossaryGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(glossaryGUID, guidParameterName, methodName);

        openMetadataClient.declassifyMetadataElementInStore(userId,
                                                            glossaryGUID,
                                                            OpenMetadataType.STAGING_GLOSSARY_CLASSIFICATION.typeName,
                                                            metadataSourceOptions);
    }


    /**
     * Classify the glossary to indicate that it can be used as a taxonomy.
     * This means each term is attached to one, and only one category and the categories are organized as a hierarchy
     * with a single root category.
     * Taxonomies are used as a way of organizing assets and other related metadata.  The terms in the taxonomy
     * are linked to the assets etc. and as such they are logically categorized by the linked category.
     *
     * @param userId                 userId of user making request.
     * @param glossaryGUID    unique identifier of the glossary.
     * @param properties            properties for the classification
     * @param metadataSourceOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void setGlossaryAsTaxonomy(String                userId,
                                      String                glossaryGUID,
                                      TaxonomyProperties    properties,
                                      MetadataSourceOptions metadataSourceOptions) throws InvalidParameterException,
                                                                                                     PropertyServerException,
                                                                                                     UserNotAuthorizedException
    {
        final String methodName = "setGlossaryAsTaxonomy";
        final String guidParameterName = "glossaryGUID";
        final String propertiesParameterName = "properties";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(glossaryGUID, guidParameterName, methodName);
        propertyHelper.validateObject(properties, propertiesParameterName, methodName);

        openMetadataClient.classifyMetadataElementInStore(userId,
                                                          glossaryGUID,
                                                          OpenMetadataType.TAXONOMY_CLASSIFICATION.typeName,
                                                          metadataSourceOptions,
                                                          classificationBuilder.getNewElementProperties(properties));
    }


    /**
     * Remove the taxonomy glossary classification from the glossary.
     *
     * @param userId                 userId of user making request.
     * @param glossaryGUID    unique identifier of the glossary.
     * @param metadataSourceOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void clearGlossaryAsTaxonomy(String                userId,
                                        String                glossaryGUID,
                                        MetadataSourceOptions metadataSourceOptions) throws InvalidParameterException,
                                                                                            PropertyServerException,
                                                                                            UserNotAuthorizedException
    {
        final String methodName = "clearGlossaryAsTaxonomy";
        final String guidParameterName = "glossaryGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(glossaryGUID, guidParameterName, methodName);

        openMetadataClient.declassifyMetadataElementInStore(userId,
                                                            glossaryGUID,
                                                            OpenMetadataType.TAXONOMY_CLASSIFICATION.typeName,
                                                            metadataSourceOptions);
    }



    /**
     * Classify a glossary to declare that it has no two GlossaryTerm definitions with
     * the same name.  This means there is only one definition for each term.  Typically, the terms are also of a similar
     * level of granularity and are limited to a specific scope of use.
     * Canonical vocabularies are used to semantically classify assets in an unambiguous way.
     *
     * @param userId                 userId of user making request.
     * @param glossaryGUID    unique identifier of the glossary.
     * @param properties            properties for the classification
     * @param metadataSourceOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void setGlossaryAsCanonical(String                       userId,
                                      String                        glossaryGUID,
                                      CanonicalVocabularyProperties properties,
                                      MetadataSourceOptions         metadataSourceOptions) throws InvalidParameterException,
                                                                                                  PropertyServerException,
                                                                                                  UserNotAuthorizedException
    {
        final String methodName = "setGlossaryAsCanonical";
        final String guidParameterName = "glossaryGUID";
        final String propertiesParameterName = "properties";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(glossaryGUID, guidParameterName, methodName);
        propertyHelper.validateObject(properties, propertiesParameterName, methodName);

        openMetadataClient.classifyMetadataElementInStore(userId,
                                                          glossaryGUID,
                                                          OpenMetadataType.CANONICAL_VOCABULARY_CLASSIFICATION.typeName,
                                                          metadataSourceOptions,
                                                          classificationBuilder.getNewElementProperties(properties));
    }


    /**
     * Remove the canonical designation from the glossary.
     *
     * @param userId                 userId of user making request.
     * @param glossaryGUID    unique identifier of the glossary.
     * @param metadataSourceOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void clearGlossaryAsCanonical(String                userId,
                                         String                glossaryGUID,
                                         MetadataSourceOptions metadataSourceOptions) throws InvalidParameterException,
                                                                                             PropertyServerException,
                                                                                             UserNotAuthorizedException
    {
        final String methodName = "clearGlossaryAsCanonical";
        final String guidParameterName = "glossaryGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(glossaryGUID, guidParameterName, methodName);

        openMetadataClient.declassifyMetadataElementInStore(userId,
                                                            glossaryGUID,
                                                            OpenMetadataType.CANONICAL_VOCABULARY_CLASSIFICATION.typeName,
                                                            metadataSourceOptions);
    }


    /**
     * Delete a glossary.
     *
     * @param userId                 userId of user making request.
     * @param glossaryGUID       unique identifier of the element
     * @param deleteOptions options for a delete request
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void deleteGlossary(String        userId,
                               String        glossaryGUID,
                               DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                   PropertyServerException,
                                                                   UserNotAuthorizedException
    {
        final String methodName = "deleteGlossary";
        final String guidParameterName = "glossaryGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(glossaryGUID, guidParameterName, methodName);

        openMetadataClient.deleteMetadataElementInStore(userId, glossaryGUID, deleteOptions);
    }



    /**
     * Returns the list of glossaries with a particular name.
     *
     * @param userId                 userId of user making request
     * @param name                   name of the element to return - match is full text match in qualifiedName or name
     * @param queryOptions           multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getGlossariesByName(String       userId,
                                                             String       name,
                                                             QueryOptions queryOptions) throws InvalidParameterException,
                                                                                               PropertyServerException,
                                                                                               UserNotAuthorizedException
    {
        final String methodName = "getGlossariesByName";

        List<String> propertyNames = Arrays.asList(OpenMetadataProperty.QUALIFIED_NAME.name,
                                                   OpenMetadataProperty.USER_ID.name,
                                                   OpenMetadataProperty.NETWORK_ADDRESS.name,
                                                   OpenMetadataProperty.DISTINGUISHED_NAME.name);

        return super.getRootElementsByName(userId, name, propertyNames, queryOptions, methodName);
    }


    /**
     * Return the properties of a specific glossary.
     *
     * @param userId                 userId of user making request
     * @param glossaryGUID      unique identifier of the required element
     * @param getOptions multiple options to control the query
     * @return retrieved properties
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElement getGlossaryByGUID(String     userId,
                                                     String     glossaryGUID,
                                                     GetOptions getOptions) throws InvalidParameterException,
                                                                                   PropertyServerException,
                                                                                   UserNotAuthorizedException
    {
        final String methodName = "getGlossaryByGUID";

        return super.getRootElementByGUID(userId, glossaryGUID, getOptions, methodName);
    }


    /**
     * Retrieve the list of glossaries metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId                 calling user
     * @param searchString           string to find in the properties
     * @param searchOptions multiple options to control the query
     * @return list of matching metadata elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<OpenMetadataRootElement> findGlossaries(String        userId,
                                                        String        searchString,
                                                        SearchOptions searchOptions) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        final String methodName = "findGlossaries";

        return super.findRootElements(userId, searchString, searchOptions, methodName);
    }


    /**
     * Retrieve the glossary metadata element for the requested glossary term.
     *
     * @param userId calling user
     * @param glossaryTermGUID unique identifier of the requested term
     * @param getOptions multiple options to control the query
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElement getGlossaryForTerm(String     userId,
                                                      String     glossaryTermGUID,
                                                      GetOptions getOptions) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        final String methodName        = "getGlossaryForTerm";
        final String guidParameterName = "glossaryTermGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(glossaryTermGUID, guidParameterName, methodName);

        List<OpenMetadataRootElement> attachedSchemas = super.getRelatedRootElements(userId,
                                                                                     glossaryTermGUID,
                                                                                     guidParameterName,
                                                                                     2,
                                                                                     OpenMetadataType.PARENT_GLOSSARY_RELATIONSHIP.typeName,
                                                                                     new QueryOptions(getOptions),
                                                                                     methodName);

        /*
         * There should be only one so we return the first non-null result.
         */
        if (attachedSchemas != null)
        {
            for (OpenMetadataRootElement returnedSchema : attachedSchemas)
            {
                if (returnedSchema != null)
                {
                    return returnedSchema;
                }
            }
        }

        return null;
    }
}
