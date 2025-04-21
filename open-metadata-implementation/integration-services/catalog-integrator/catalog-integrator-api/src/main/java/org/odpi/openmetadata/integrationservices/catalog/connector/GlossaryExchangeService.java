/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.integrationservices.catalog.connector;

import org.odpi.openmetadata.accessservices.assetmanager.client.exchange.GlossaryExchangeClient;
import org.odpi.openmetadata.frameworks.openmetadata.enums.GlossaryTermStatus;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.GlossaryCategoryElement;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.GlossaryElement;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.GlossaryTermElement;
import org.odpi.openmetadata.accessservices.assetmanager.properties.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.glossaries.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.governanceaction.properties.ExternalIdentifierProperties;
import org.odpi.openmetadata.frameworks.openmetadata.enums.GlossaryTermRelationshipStatus;
import org.odpi.openmetadata.frameworks.openmetadata.enums.PermittedSynchronization;
import org.odpi.openmetadata.integrationservices.catalog.ffdc.CatalogIntegratorErrorCode;

import java.util.Date;
import java.util.List;

/**
 * GlossaryExchangeService is the client for managing resources from a glossary.  This includes
 * the glossary container, glossary categories and terms as well as relationships between them.
 */
public class GlossaryExchangeService
{
    private final GlossaryExchangeClient   glossaryManagerClient;
    private final String                   userId;
    private final String                   assetManagerGUID;
    private final String                   assetManagerName;
    private final String                   connectorName;
    private final PermittedSynchronization permittedSynchronization;
    private final AuditLog                 auditLog;

    private boolean forLineage             = false;
    private boolean forDuplicateProcessing = false;

    /**
     * Create a new client to exchange glossary content with open metadata.
     *
     * @param glossaryManagerClient client for exchange requests
     * @param permittedSynchronization direction(s) that metadata can flow
     * @param userId integration daemon's userId
     * @param assetManagerGUID unique identifier of the software server capability for the asset manager
     * @param assetManagerName unique name of the software server capability for the asset manager
     * @param connectorName name of the connector using this context
     * @param auditLog logging destination
     */
    GlossaryExchangeService(GlossaryExchangeClient   glossaryManagerClient,
                            PermittedSynchronization permittedSynchronization,
                            String                   userId,
                            String                   assetManagerGUID,
                            String                   assetManagerName,
                            String                   connectorName,
                            AuditLog                 auditLog)
    {
        this.glossaryManagerClient    = glossaryManagerClient;
        this.permittedSynchronization = permittedSynchronization;
        this.userId                   = userId;
        this.assetManagerGUID         = assetManagerGUID;
        this.assetManagerName         = assetManagerName;
        this.connectorName            = connectorName;
        this.auditLog                 = auditLog;
    }


    /* ========================================================
     * Set up the forLineage flag
     */

    /**
     * Return whether retrieval requests from this service are to include elements with the Memento classification attached or not.
     *
     * @return boolean flag
     */
    public boolean isForLineage()
    {
        return forLineage;
    }


    /**
     * Set up whether retrieval requests from this service are to include elements with the Memento classification attached or not.
     *
     * @param forLineage boolean flag
     */
    public void setForLineage(boolean forLineage)
    {
        this.forLineage = forLineage;
    }
    

    /* ========================================================
     * Set up the forDuplicateProcessing flag
     */

    /**
     * Return whether retrieval requests from this service are to avoid merging duplicates or not.
     *
     * @return boolean flag
     */
    public boolean isForDuplicateProcessing()
    {
        return forDuplicateProcessing;
    }


    /**
     * Set up whether retrieval requests from this service are to avoid merging duplicates or not.
     *
     * @param forDuplicateProcessing boolean flag
     */
    public void setForDuplicateProcessing(boolean forDuplicateProcessing)
    {
        this.forDuplicateProcessing = forDuplicateProcessing;
    }



    /* ========================================================
     * The Glossary entity is the top level element in a glossary.
     */


    /**
     * Create a new metadata element to represent the root of a glossary.  All categories and terms are linked
     * to a single glossary.  They are owned by this glossary and if the glossary is deleted, any linked terms and
     * categories are deleted as well.
     *
     * @param assetManagerIsHome ensure that only the asset manager can update this asset
     * @param externalIdentifierProperties optional properties used to define an external identifier
     * @param glossaryProperties properties to store
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createGlossary(boolean                      assetManagerIsHome,
                                 ExternalIdentifierProperties externalIdentifierProperties,
                                 GlossaryProperties           glossaryProperties) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        final String methodName = "createGlossary";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            return glossaryManagerClient.createGlossary(userId,
                                                        assetManagerGUID,
                                                        assetManagerName,
                                                        assetManagerIsHome,
                                                        externalIdentifierProperties,
                                                        glossaryProperties);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Create a new metadata element to represent a glossary using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new glossary.
     * All categories and terms are linked to a single glossary.  They are owned by this glossary and if the
     * glossary is deleted, any linked terms and categories are deleted as well.
     *
     * @param assetManagerIsHome      ensure that only the asset manager can update this element
     * @param templateGUID unique identifier of the metadata element to copy
     * @param externalIdentifierProperties optional properties used to define an external identifier
     * @param templateProperties properties that override the template
     * @param deepCopy should the template creation extend to the anchored elements or just the direct entity?
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createGlossaryFromTemplate(boolean                      assetManagerIsHome,
                                             String                       templateGUID,
                                             ExternalIdentifierProperties externalIdentifierProperties,
                                             TemplateProperties           templateProperties,
                                             boolean                      deepCopy) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        final String methodName = "createGlossaryFromTemplate";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            return glossaryManagerClient.createGlossaryFromTemplate(userId,
                                                                    assetManagerGUID,
                                                                    assetManagerName,
                                                                    assetManagerIsHome,
                                                                    templateGUID,
                                                                    externalIdentifierProperties,
                                                                    deepCopy,
                                                                    templateProperties);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Update the metadata element representing a glossary.
     *
     * @param glossaryGUID unique identifier of the metadata element to update
     * @param glossaryExternalIdentifier unique identifier of the glossary in the external asset manager
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param glossaryProperties new properties for this element
     * @param effectiveTime the time that the retrieved elements must be effective for
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateGlossary(String             glossaryGUID,
                               String             glossaryExternalIdentifier,
                               boolean            isMergeUpdate,
                               GlossaryProperties glossaryProperties,
                               Date               effectiveTime) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        final String methodName = "updateGlossary";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            glossaryManagerClient.updateGlossary(userId,
                                                 assetManagerGUID,
                                                 assetManagerName,
                                                 glossaryGUID,
                                                 glossaryExternalIdentifier,
                                                 isMergeUpdate, glossaryProperties,
                                                 effectiveTime,
                                                 forLineage,
                                                 forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Remove the metadata element representing a glossary.  This will delete the glossary and all categories
     * and terms - use with care :)
     *
     * @param glossaryGUID unique identifier of the metadata element to remove
     * @param glossaryExternalIdentifier unique identifier of the glossary in the external asset manager
     * @param cascadedDelete     boolean indicating whether the delete request can cascade to dependent elements
     * @param effectiveTime the time that the retrieved elements must be effective for
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeGlossary(String  glossaryGUID,
                               String  glossaryExternalIdentifier,
                               boolean cascadedDelete,
                               Date    effectiveTime) throws InvalidParameterException,
                                                             UserNotAuthorizedException,
                                                             PropertyServerException
    {
        final String methodName = "removeGlossary";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            glossaryManagerClient.removeGlossary(userId, assetManagerGUID, assetManagerName, glossaryGUID, glossaryExternalIdentifier, cascadedDelete, effectiveTime, forLineage, forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Classify the glossary to indicate that it can be used as a taxonomy.
     * This means each term is attached to one, and only one category and the categories are organized as a hierarchy
     * with a single root category.
     *
     * Taxonomies are used as a way of organizing assets and other related metadata.  The terms in the taxonomy
     * are linked to the assets etc. and as such they are logically categorized by the linked category.
     *
     * @param glossaryGUID unique identifier of the metadata element to remove
     * @param glossaryExternalIdentifier unique identifier of the glossary in the external asset manager
     * @param properties description of how the glossary is organized
     * @param effectiveTime the time that the retrieved elements must be effective for
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setGlossaryAsTaxonomy(String             glossaryGUID,
                                      String             glossaryExternalIdentifier,
                                      TaxonomyProperties properties,
                                      Date               effectiveTime) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        final String methodName = "setGlossaryAsTaxonomy";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            glossaryManagerClient.setGlossaryAsTaxonomy(userId, assetManagerGUID, assetManagerName, glossaryGUID, glossaryExternalIdentifier, properties, effectiveTime, forLineage, forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Remove the taxonomy designation from the glossary.
     *
     * @param glossaryGUID unique identifier of the metadata element to remove
     * @param glossaryExternalIdentifier unique identifier of the glossary in the external asset manager
     * @param effectiveTime the time that the retrieved elements must be effective for
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearGlossaryAsTaxonomy(String glossaryGUID,
                                        String glossaryExternalIdentifier,
                                        Date   effectiveTime) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {
        final String methodName = "clearGlossaryAsTaxonomy";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            glossaryManagerClient.clearGlossaryAsTaxonomy(userId, assetManagerGUID, assetManagerName, glossaryGUID, glossaryExternalIdentifier, effectiveTime, forLineage, forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Classify a glossary to declare that it has no two GlossaryTerm definitions with
     * the same name.  This means there is only one definition for each term.  Typically, the terms are also of a similar
     * level of granularity and are limited to a specific scope of use.
     * Canonical vocabularies are used to semantically classify assets in an unambiguous way.
     *
     * @param glossaryGUID unique identifier of the metadata element to remove
     * @param glossaryExternalIdentifier unique identifier of the glossary in the external asset manager
     * @param properties description of the situations where this glossary is relevant.
     * @param effectiveTime the time that the retrieved elements must be effective for
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setGlossaryAsCanonical(String                        glossaryGUID,
                                       String                        glossaryExternalIdentifier,
                                       CanonicalVocabularyProperties properties,
                                       Date                          effectiveTime) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        final String methodName = "setGlossaryAsCanonical";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            glossaryManagerClient.setGlossaryAsCanonical(userId, assetManagerGUID, assetManagerName, glossaryGUID, glossaryExternalIdentifier, properties, effectiveTime, forLineage, forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Remove the canonical designation from the glossary.
     *
     * @param glossaryGUID unique identifier of the metadata element to remove
     * @param glossaryExternalIdentifier unique identifier of the glossary in the external asset manager
     * @param effectiveTime the time that the retrieved elements must be effective for
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearGlossaryAsCanonical(String glossaryGUID,
                                         String glossaryExternalIdentifier,
                                         Date   effectiveTime) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        final String methodName = "clearGlossaryAsCanonical";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            glossaryManagerClient.clearGlossaryAsCanonical(userId, assetManagerGUID, assetManagerName, glossaryGUID, glossaryExternalIdentifier, effectiveTime, forLineage, forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Retrieve the list of glossary metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime the time that the retrieved elements must be effective for
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<GlossaryElement>   findGlossaries(String searchString,
                                                  int    startFrom,
                                                  int    pageSize,
                                                  Date   effectiveTime) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        return glossaryManagerClient.findGlossaries(userId, assetManagerGUID, assetManagerName, searchString, startFrom, pageSize, effectiveTime, forLineage, forDuplicateProcessing);
    }


    /**
     * Retrieve the list of glossary metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime the time that the retrieved elements must be effective for
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<GlossaryElement>   getGlossariesByName(String name,
                                                       int    startFrom,
                                                       int    pageSize,
                                                       Date   effectiveTime) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        return glossaryManagerClient.getGlossariesByName(userId, assetManagerGUID, assetManagerName, name, startFrom, pageSize, effectiveTime, forLineage, forDuplicateProcessing);
    }


    /**
     * Retrieve the list of glossaries created on behalf of this asset manager.
     *
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime the time that the retrieved elements must be effective for
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<GlossaryElement>   getGlossariesForAssetManager(int  startFrom,
                                                                int  pageSize,
                                                                Date effectiveTime) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        return glossaryManagerClient.getGlossariesForAssetManager(userId, assetManagerGUID, assetManagerName, startFrom, pageSize, effectiveTime, forLineage, forDuplicateProcessing);
    }


    /**
     * Retrieve the glossary metadata element with the supplied unique identifier.
     *
     * @param glossaryGUID unique identifier of the requested metadata element
     * @param effectiveTime the time that the retrieved elements must be effective for
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GlossaryElement getGlossaryByGUID(String glossaryGUID,
                                             Date   effectiveTime) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        return glossaryManagerClient.getGlossaryByGUID(userId, assetManagerGUID, assetManagerName, glossaryGUID, effectiveTime, forLineage, forDuplicateProcessing);
    }


    /**
     * Retrieve the glossary metadata element for the requested category.
     *
     * @param glossaryCategoryGUID unique identifier of the requested metadata element
     * @param effectiveTime the time that the retrieved elements must be effective for
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GlossaryElement getGlossaryForCategory(String glossaryCategoryGUID,
                                                  Date   effectiveTime) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        return glossaryManagerClient.getGlossaryForCategory(userId, assetManagerGUID, assetManagerName, glossaryCategoryGUID, effectiveTime, forLineage, forDuplicateProcessing);
    }


    /**
     * Retrieve the glossary metadata element for the requested term.
     *
     * @param glossaryTermGUID unique identifier of the requested metadata element
     * @param effectiveTime the time that the retrieved elements must be effective for
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GlossaryElement getGlossaryForTerm(String glossaryTermGUID,
                                              Date   effectiveTime) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        return glossaryManagerClient.getGlossaryForTerm(userId, assetManagerGUID, assetManagerName, glossaryTermGUID, effectiveTime, forLineage, forDuplicateProcessing);
    }


    /* =====================================================================================================================
     * A glossary may host one or more glossary categories depending on its capability
     */

    /**
     * Create a new metadata element to represent a glossary category.
     *
     * @param glossaryGUID unique identifier of anchor glossary
     * @param assetManagerIsHome      ensure that only the asset manager can update this element
     * @param externalIdentifierProperties optional properties used to define an external identifier
     * @param glossaryCategoryProperties properties about the glossary category to store
     * @param isRootCategory is this category a root category?
     * @param effectiveTime           the time that the retrieved elements must be effective for
     *
     * @return unique identifier of the new glossary category
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createGlossaryCategory(String                       glossaryGUID,
                                         boolean                      assetManagerIsHome,
                                         ExternalIdentifierProperties externalIdentifierProperties,
                                         GlossaryCategoryProperties   glossaryCategoryProperties,
                                         boolean                      isRootCategory,
                                         Date                         effectiveTime) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        final String methodName = "createGlossaryCategory";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            return glossaryManagerClient.createGlossaryCategory(userId,
                                                                assetManagerGUID,
                                                                assetManagerName,
                                                                assetManagerIsHome,
                                                                glossaryGUID,
                                                                externalIdentifierProperties,
                                                                glossaryCategoryProperties,
                                                                isRootCategory,
                                                                effectiveTime,
                                                                forLineage,
                                                                forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Create a new metadata element to represent a glossary category using an existing metadata element as a template.
     *
     * @param glossaryGUID unique identifier of the glossary where the category is located
     * @param assetManagerIsHome      ensure that only the asset manager can update this element
     * @param templateGUID unique identifier of the metadata element to copy
     * @param externalIdentifierProperties optional properties used to define an external identifier
     * @param templateProperties properties that override the template
     * @param deepCopy should the template creation extend to the anchored elements or just the direct entity?
     *
     * @return unique identifier of the new glossary category
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createGlossaryCategoryFromTemplate(String                       glossaryGUID,
                                                     boolean                      assetManagerIsHome,
                                                     String                       templateGUID,
                                                     ExternalIdentifierProperties externalIdentifierProperties,
                                                     TemplateProperties           templateProperties,
                                                     boolean                      deepCopy) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   PropertyServerException
    {
        final String methodName = "createGlossaryCategoryFromTemplate";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            return glossaryManagerClient.createGlossaryCategoryFromTemplate(userId,
                                                                            assetManagerGUID,
                                                                            assetManagerName,
                                                                            assetManagerIsHome,
                                                                            glossaryGUID,
                                                                            templateGUID,
                                                                            externalIdentifierProperties,
                                                                            deepCopy,
                                                                            templateProperties);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Update the metadata element representing a glossary category.
     *
     * @param glossaryCategoryGUID unique identifier of the metadata element to update
     * @param glossaryCategoryExternalIdentifier unique identifier of the glossary category in the external asset manager
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param glossaryCategoryProperties new properties for the metadata element
     * @param effectiveTime the time that the retrieved elements must be effective for
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateGlossaryCategory(String                     glossaryCategoryGUID,
                                       String                     glossaryCategoryExternalIdentifier,
                                       boolean                    isMergeUpdate,
                                       GlossaryCategoryProperties glossaryCategoryProperties,
                                       Date                       effectiveTime) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        final String methodName = "updateGlossaryCategory";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            glossaryManagerClient.updateGlossaryCategory(userId,
                                                         assetManagerGUID,
                                                         assetManagerName,
                                                         glossaryCategoryGUID,
                                                         glossaryCategoryExternalIdentifier,
                                                         isMergeUpdate,
                                                         glossaryCategoryProperties,
                                                         effectiveTime,
                                                         forLineage,
                                                         forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Create a parent-child relationship between two categories.
     *
     * @param glossaryParentCategoryGUID unique identifier of the glossary category in the external asset manager that is to be the super-category
     * @param glossaryChildCategoryGUID unique identifier of the glossary category in the external asset manager that is to be the subcategory
     * @param effectiveTime the time that the retrieved elements must be effective for
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupCategoryParent(String glossaryParentCategoryGUID,
                                    String glossaryChildCategoryGUID,
                                    Date   effectiveTime) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        final String methodName = "setupCategoryParent";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            glossaryManagerClient.setupCategoryParent(userId,
                                                      assetManagerGUID,
                                                      assetManagerName,
                                                      glossaryParentCategoryGUID,
                                                      glossaryChildCategoryGUID,
                                                      effectiveTime,
                                                      forLineage,
                                                      forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Remove a parent-child relationship between two categories.
     *
     * @param glossaryParentCategoryGUID unique identifier of the glossary category in the external asset manager that is to be the super-category
     * @param glossaryChildCategoryGUID unique identifier of the glossary category in the external asset manager that is to be the subcategory
     * @param effectiveTime the time that the retrieved elements must be effective for
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearCategoryParent(String glossaryParentCategoryGUID,
                                    String glossaryChildCategoryGUID,
                                    Date   effectiveTime) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        final String methodName = "clearCategoryParent";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            glossaryManagerClient.clearCategoryParent(userId,
                                                      assetManagerGUID,
                                                      assetManagerName,
                                                      glossaryParentCategoryGUID,
                                                      glossaryChildCategoryGUID,
                                                      effectiveTime,
                                                      forLineage,
                                                      forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Remove the metadata element representing a glossary category.
     *
     * @param glossaryCategoryGUID unique identifier of the metadata element to remove
     * @param glossaryCategoryExternalIdentifier unique identifier of the glossary category in the external asset manager
     * @param effectiveTime the time that the retrieved elements must be effective for
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeGlossaryCategory(String glossaryCategoryGUID,
                                       String glossaryCategoryExternalIdentifier,
                                       Date   effectiveTime) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        final String methodName = "removeGlossaryCategory";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            glossaryManagerClient.removeGlossaryCategory(userId, assetManagerGUID, assetManagerName, glossaryCategoryGUID, glossaryCategoryExternalIdentifier, effectiveTime, forLineage,
                                                         forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Retrieve the list of glossary category metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime the time that the retrieved elements must be effective for
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<GlossaryCategoryElement>   findGlossaryCategories(String searchString,
                                                                  int    startFrom,
                                                                  int    pageSize,
                                                                  Date   effectiveTime) throws InvalidParameterException, 
                                                                                               UserNotAuthorizedException, 
                                                                                               PropertyServerException
    {
        return glossaryManagerClient.findGlossaryCategories(userId,
                                                            assetManagerGUID,
                                                            assetManagerName,
                                                            null,
                                                            searchString,
                                                            startFrom,
                                                            pageSize,
                                                            effectiveTime,
                                                            forLineage,
                                                            forDuplicateProcessing);
    }


    /**
     * Retrieve the list of glossary category metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param glossaryGUID optional glossary unique identifier to scope the search to a glossary.
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime the time that the retrieved elements must be effective for
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<GlossaryCategoryElement>   findGlossaryCategories(String glossaryGUID,
                                                                  String searchString,
                                                                  int    startFrom,
                                                                  int    pageSize,
                                                                  Date   effectiveTime) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException
    {
        return glossaryManagerClient.findGlossaryCategories(userId,
                                                            assetManagerGUID,
                                                            assetManagerName,
                                                            glossaryGUID,
                                                            searchString,
                                                            startFrom,
                                                            pageSize,
                                                            effectiveTime,
                                                            forLineage,
                                                            forDuplicateProcessing);
    }


    /**
     * Return the list of categories associated with a glossary.
     *
     * @param glossaryGUID unique identifier of the glossary to query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime the time that the retrieved elements must be effective for
     *
     * @return list of metadata elements describing the categories associated with the requested glossary
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<GlossaryCategoryElement>   getCategoriesForGlossary(String glossaryGUID,
                                                                    int    startFrom,
                                                                    int    pageSize,
                                                                    Date   effectiveTime) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException
    {
        return glossaryManagerClient.getCategoriesForGlossary(userId,
                                                              assetManagerGUID,
                                                              assetManagerName,
                                                              glossaryGUID,
                                                              startFrom,
                                                              pageSize,
                                                              effectiveTime,
                                                              forLineage,
                                                              forDuplicateProcessing);
    }

    /**
     * Return the list of categories associated with a glossary term.
     *
     * @param glossaryTermGUID unique identifier of the glossary term to query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime the time that the retrieved elements must be effective for
     *
     * @return list of metadata elements describing the categories associated with the requested term
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<GlossaryCategoryElement>   getCategoriesForTerm(String glossaryTermGUID,
                                                                int    startFrom,
                                                                int    pageSize,
                                                                Date   effectiveTime) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        return glossaryManagerClient.getCategoriesForTerm(userId,
                                                          assetManagerGUID,
                                                          assetManagerName,
                                                          glossaryTermGUID,
                                                          startFrom,
                                                          pageSize,
                                                          effectiveTime,
                                                          forLineage,
                                                          forDuplicateProcessing);
    }


    /**
     * Retrieve the list of glossary category metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime the time that the retrieved elements must be effective for
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<GlossaryCategoryElement>   getGlossaryCategoriesByName(String name,
                                                                       int    startFrom,
                                                                       int    pageSize,
                                                                       Date   effectiveTime) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException
    {
        return glossaryManagerClient.getGlossaryCategoriesByName(userId,
                                                                 assetManagerGUID,
                                                                 assetManagerName,
                                                                 null,
                                                                 name,
                                                                 startFrom,
                                                                 pageSize,
                                                                 effectiveTime,
                                                                 forLineage,
                                                                 forDuplicateProcessing);
    }


    /**
     * Retrieve the list of glossary category metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param glossaryGUID optional glossary unique identifier to scope the search to a glossary.
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime the time that the retrieved elements must be effective for
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<GlossaryCategoryElement>   getGlossaryCategoriesByName(String glossaryGUID,
                                                                       String name,
                                                                       int    startFrom,
                                                                       int    pageSize,
                                                                       Date   effectiveTime) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException
    {
        return glossaryManagerClient.getGlossaryCategoriesByName(userId,
                                                                 assetManagerGUID,
                                                                 assetManagerName,
                                                                 glossaryGUID,
                                                                 name,
                                                                 startFrom,
                                                                 pageSize,
                                                                 effectiveTime,
                                                                 forLineage,
                                                                 forDuplicateProcessing);
    }


    /**
     * Retrieve the glossary category metadata element with the supplied unique identifier.
     *
     * @param guid unique identifier of the requested metadata element
     * @param effectiveTime the time that the retrieved elements must be effective for
     *
     * @return requested metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GlossaryCategoryElement getGlossaryCategoryByGUID(String guid,
                                                             Date   effectiveTime) throws InvalidParameterException, 
                                                                                          UserNotAuthorizedException, 
                                                                                          PropertyServerException
    {
        return glossaryManagerClient.getGlossaryCategoryByGUID(userId,
                                                               assetManagerGUID,
                                                               assetManagerName,
                                                               guid,
                                                               effectiveTime,
                                                               forLineage,
                                                               forDuplicateProcessing);
    }


    /**
     * Retrieve the glossary category metadata element with the supplied unique identifier.
     *
     * @param glossaryCategoryGUID unique identifier of the requested metadata element
     * @param effectiveTime the time that the retrieved elements must be effective for
     *
     * @return parent glossary category element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GlossaryCategoryElement getGlossaryCategoryParent(String glossaryCategoryGUID,
                                                             Date   effectiveTime) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        return glossaryManagerClient.getGlossaryCategoryParent(userId,
                                                               assetManagerGUID,
                                                               assetManagerName,
                                                               glossaryCategoryGUID,
                                                               effectiveTime,
                                                               forLineage,
                                                               forDuplicateProcessing);
    }


    /**
     * Retrieve the glossary category metadata element with the supplied unique identifier.
     *
     * @param glossaryCategoryGUID unique identifier of the requested metadata element
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime the time that the retrieved elements must be effective for
     *
     * @return list of glossary category element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<GlossaryCategoryElement> getGlossarySubCategories(String glossaryCategoryGUID,
                                                                  int    startFrom,
                                                                  int    pageSize,
                                                                  Date   effectiveTime) throws InvalidParameterException, 
                                                                                               UserNotAuthorizedException, 
                                                                                               PropertyServerException
    {
        return glossaryManagerClient.getGlossarySubCategories(userId,
                                                              assetManagerGUID,
                                                              assetManagerName,
                                                              glossaryCategoryGUID,
                                                              startFrom,
                                                              pageSize,
                                                              effectiveTime,
                                                              forLineage,
                                                              forDuplicateProcessing);
    }


    /* ===============================================================================
     * A glossary typically contains many glossary terms, linked with relationships.
     */

    /**
     * Create a new metadata element to represent a glossary term.
     *
     * @param assetManagerIsHome      ensure that only the asset manager can update this element
     * @param glossaryGUID unique identifier of the glossary where the term is located
     * @param externalIdentifierProperties optional properties used to define an external identifier
     * @param glossaryTermProperties properties for the glossary term
     * @param effectiveTime           the time that the retrieved elements must be effective for
     *
     * @return unique identifier of the new metadata element for the glossary term
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createGlossaryTerm(String                       glossaryGUID,
                                     boolean                      assetManagerIsHome,
                                     ExternalIdentifierProperties externalIdentifierProperties,
                                     GlossaryTermProperties       glossaryTermProperties,
                                     Date                         effectiveTime) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        final String methodName = "createGlossaryTerm";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            return glossaryManagerClient.createGlossaryTerm(userId,
                                                            assetManagerGUID,
                                                            assetManagerName,
                                                            assetManagerIsHome,
                                                            glossaryGUID,
                                                            externalIdentifierProperties,
                                                            glossaryTermProperties,
                                                            effectiveTime,
                                                            forLineage,
                                                            forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Create a new metadata element to represent a glossary term whose lifecycle is managed through a controlled workflow.
     *
     * @param assetManagerIsHome      ensure that only the asset manager can update this element
     * @param glossaryGUID unique identifier of the glossary where the term is located
     * @param externalIdentifierProperties optional properties used to define an external identifier
     * @param glossaryTermProperties properties for the glossary term
     * @param initialStatus glossary term status to use when the object is created
     * @param effectiveTime           the time that the retrieved elements must be effective for
     *
     * @return unique identifier of the new metadata element for the glossary term
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createControlledGlossaryTerm(boolean                      assetManagerIsHome,
                                               String                       glossaryGUID,
                                               ExternalIdentifierProperties externalIdentifierProperties,
                                               GlossaryTermProperties       glossaryTermProperties,
                                               GlossaryTermStatus initialStatus,
                                               Date                         effectiveTime) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        final String methodName = "createControlledGlossaryTerm";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            return glossaryManagerClient.createControlledGlossaryTerm(userId,
                                                                      assetManagerGUID,
                                                                      assetManagerName,
                                                                      assetManagerIsHome,
                                                                      glossaryGUID,
                                                                      externalIdentifierProperties,
                                                                      glossaryTermProperties,
                                                                      initialStatus,
                                                                      effectiveTime,
                                                                      forLineage,
                                                                      forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Create a new metadata element to represent a glossary term using an existing metadata element as a template.
     *
     * @param glossaryGUID unique identifier of the glossary where the category is located
     * @param assetManagerIsHome      ensure that only the asset manager can update this element
     * @param templateGUID unique identifier of the metadata element to copy
     * @param externalIdentifierProperties optional properties used to define an external identifier
     * @param templateProperties properties that override the template
     * @param deepCopy should the template creation extend to the anchored elements or just the direct entity?
     * @param templateSubstitute is this element a template substitute (used as the "other end" of a new/updated relationship)
     * @param initialStatus what status should the copy be set to
     *
     * @return unique identifier of the new metadata element for the glossary term
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createGlossaryTermFromTemplate(String                       glossaryGUID,
                                                 boolean                      assetManagerIsHome,
                                                 String                       templateGUID,
                                                 ExternalIdentifierProperties externalIdentifierProperties,
                                                 TemplateProperties           templateProperties,
                                                 boolean                      deepCopy,
                                                 boolean                      templateSubstitute,
                                                 GlossaryTermStatus           initialStatus) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException
    {
        final String methodName = "createGlossaryTermFromTemplate";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            return glossaryManagerClient.createGlossaryTermFromTemplate(userId,
                                                                        assetManagerGUID,
                                                                        assetManagerName,
                                                                        assetManagerIsHome,
                                                                        glossaryGUID,
                                                                        templateGUID,
                                                                        externalIdentifierProperties,
                                                                        deepCopy,
                                                                        templateSubstitute,
                                                                        initialStatus,
                                                                        templateProperties);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Update the properties of the metadata element representing a glossary term.
     *
     * @param glossaryTermGUID unique identifier of the glossary term to update
     * @param glossaryTermExternalIdentifier unique identifier of the glossary term in the external asset manager
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param glossaryTermProperties new properties for the glossary term
     * @param updateDescription description of the change to the term
     * @param effectiveTime the time that the retrieved elements must be effective for
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateGlossaryTerm(String                 glossaryTermGUID,
                                   String                 glossaryTermExternalIdentifier,
                                   boolean                isMergeUpdate,
                                   GlossaryTermProperties glossaryTermProperties,
                                   String                 updateDescription,
                                   Date                   effectiveTime) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        final String methodName = "updateGlossaryTerm";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            glossaryManagerClient.updateGlossaryTerm(userId,
                                                     assetManagerGUID,
                                                     assetManagerName,
                                                     glossaryTermGUID,
                                                     glossaryTermExternalIdentifier,
                                                     isMergeUpdate,
                                                     glossaryTermProperties,
                                                     updateDescription,
                                                     effectiveTime,
                                                     forLineage,
                                                     forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Update the status of the metadata element representing a glossary term.  This is only valid on
     * a controlled glossary term.
     *
     * @param glossaryTermGUID unique identifier of the glossary term to update
     * @param glossaryTermExternalIdentifier unique identifier of the glossary term in the external asset manager
     * @param glossaryTermStatus new properties for the glossary term
     * @param effectiveTime the time that the retrieved elements must be effective for
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateGlossaryTermStatus(String             glossaryTermGUID,
                                         String             glossaryTermExternalIdentifier,
                                         GlossaryTermStatus glossaryTermStatus,
                                         Date               effectiveTime) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        final String methodName = "updateGlossaryTermStatus";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            glossaryManagerClient.updateGlossaryTermStatus(userId,
                                                           assetManagerGUID,
                                                           assetManagerName,
                                                           glossaryTermGUID,
                                                           glossaryTermExternalIdentifier,
                                                           glossaryTermStatus,
                                                           effectiveTime,
                                                           forLineage,
                                                           forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Link a term to a category.
     *
     * @param glossaryCategoryGUID unique identifier of the glossary category
     * @param glossaryTermGUID unique identifier of the glossary term
     * @param categorizationProperties properties for the categorization relationship
     * @param effectiveTime the time that the retrieved elements must be effective for
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupTermCategory(String                     glossaryCategoryGUID,
                                  String                     glossaryTermGUID,
                                  GlossaryTermCategorization categorizationProperties,
                                  Date                       effectiveTime) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        final String methodName = "setupTermCategory";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            glossaryManagerClient.setupTermCategory(userId,
                                                    assetManagerGUID,
                                                    assetManagerName,
                                                    glossaryCategoryGUID,
                                                    glossaryTermGUID,
                                                    categorizationProperties,
                                                    effectiveTime,
                                                    forLineage,
                                                    forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Unlink a term from a category.
     *
     * @param glossaryCategoryGUID unique identifier of the glossary category
     * @param glossaryTermGUID unique identifier of the glossary term
     * @param effectiveTime the time that the retrieved elements must be effective for
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearTermCategory(String glossaryCategoryGUID,
                                  String glossaryTermGUID,
                                  Date   effectiveTime) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        final String methodName = "clearTermCategory";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            glossaryManagerClient.clearTermCategory(userId,
                                                    assetManagerGUID,
                                                    assetManagerName,
                                                    glossaryCategoryGUID,
                                                    glossaryTermGUID,
                                                    effectiveTime,
                                                    forLineage,
                                                    forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Link two terms together using a specialist relationship.
     *
     * @param relationshipTypeName name of the type of relationship to create
     * @param glossaryTermOneGUID unique identifier of the glossary term at end 1
     * @param glossaryTermTwoGUID unique identifier of the glossary term at end 2
     * @param relationshipsProperties properties for the categorization relationship
     * @param effectiveTime the time that the retrieved elements must be effective for
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupTermRelationship(String                   relationshipTypeName,
                                      String                   glossaryTermOneGUID,
                                      String                   glossaryTermTwoGUID,
                                      GlossaryTermRelationship relationshipsProperties,
                                      Date                     effectiveTime) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        final String methodName = "setupTermRelationship";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            glossaryManagerClient.setupTermRelationship(userId,
                                                        assetManagerGUID,
                                                        assetManagerName,
                                                        relationshipTypeName,
                                                        glossaryTermOneGUID,
                                                        glossaryTermTwoGUID,
                                                        relationshipsProperties,
                                                        effectiveTime,
                                                        forLineage,
                                                        forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Update the relationship properties for the two terms.
     *
     * @param relationshipTypeName name of the type of relationship to create
     * @param glossaryTermOneGUID unique identifier of the glossary term at end 1
     * @param glossaryTermTwoGUID unique identifier of the glossary term at end 2
     * @param relationshipsProperties properties for the categorization relationship
     * @param effectiveTime the time that the retrieved elements must be effective for
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateTermRelationship(String                   relationshipTypeName,
                                       String                   glossaryTermOneGUID,
                                       String                   glossaryTermTwoGUID,
                                       GlossaryTermRelationship relationshipsProperties,
                                       Date                     effectiveTime) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        final String methodName = "updateTermRelationship";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            glossaryManagerClient.updateTermRelationship(userId,
                                                         assetManagerGUID,
                                                         assetManagerName,
                                                         relationshipTypeName,
                                                         glossaryTermOneGUID,
                                                         glossaryTermTwoGUID,
                                                         relationshipsProperties,
                                                         effectiveTime,
                                                         forLineage,
                                                         forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Remove the relationship between two terms.
     *
     * @param relationshipTypeName name of the type of relationship to create
     * @param glossaryTermOneGUID unique identifier of the glossary term at end 1
     * @param glossaryTermTwoGUID unique identifier of the glossary term at end 2
     * @param effectiveTime the time that the retrieved elements must be effective for
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearTermRelationship(String relationshipTypeName,
                                      String glossaryTermOneGUID,
                                      String glossaryTermTwoGUID,
                                      Date   effectiveTime) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        final String methodName = "clearTermRelationship";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            glossaryManagerClient.clearTermRelationship(userId,
                                                        assetManagerGUID,
                                                        assetManagerName,
                                                        relationshipTypeName,
                                                        glossaryTermOneGUID,
                                                        glossaryTermTwoGUID,
                                                        effectiveTime,
                                                        forLineage,
                                                        forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Classify the glossary term to indicate that it describes an abstract concept.
     *
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param glossaryTermExternalIdentifier unique identifier of the glossary term in the external asset manager
     * @param effectiveTime the time that the retrieved elements must be effective for
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setTermAsAbstractConcept(String glossaryTermGUID,
                                         String glossaryTermExternalIdentifier,
                                         Date   effectiveTime) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        final String methodName = "setTermAsAbstractConcept";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            glossaryManagerClient.setTermAsAbstractConcept(userId,
                                                           assetManagerGUID,
                                                           assetManagerName,
                                                           glossaryTermGUID,
                                                           glossaryTermExternalIdentifier,
                                                           effectiveTime,
                                                           forLineage,
                                                           forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Remove the abstract concept designation from the glossary term.
     *
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param glossaryTermExternalIdentifier unique identifier of the glossary term in the external asset manager
     * @param effectiveTime the time that the retrieved elements must be effective for
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearTermAsAbstractConcept(String glossaryTermGUID,
                                           String glossaryTermExternalIdentifier,
                                           Date   effectiveTime) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        final String methodName = "clearTermAsAbstractConcept";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            glossaryManagerClient.clearTermAsAbstractConcept(userId,
                                                             assetManagerGUID,
                                                             assetManagerName,
                                                             glossaryTermGUID,
                                                             glossaryTermExternalIdentifier,
                                                             effectiveTime,
                                                             forLineage,
                                                             forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Classify the glossary term to indicate that it describes a data value.
     *
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param glossaryTermExternalIdentifier unique identifier of the glossary term in the external asset manager
     * @param effectiveTime the time that the retrieved elements must be effective for
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setTermAsDataValue(String glossaryTermGUID,
                                   String glossaryTermExternalIdentifier,
                                   Date   effectiveTime) throws InvalidParameterException,
                                                                UserNotAuthorizedException,
                                                                PropertyServerException
    {
        final String methodName = "setTermAsDataValue";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            glossaryManagerClient.setTermAsDataValue(userId,
                                                     assetManagerGUID,
                                                     assetManagerName,
                                                     glossaryTermGUID,
                                                     glossaryTermExternalIdentifier,
                                                     effectiveTime,
                                                     forLineage,
                                                     forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(
                    CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(permittedSynchronization.getName(),
                                                                                                  connectorName,
                                                                                                  methodName),
                    this.getClass().getName(),
                    methodName,
                    userId);
        }
    }


    /**
     * Remove the data value designation from the glossary term.
     *
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param glossaryTermExternalIdentifier unique identifier of the glossary term in the external asset manager
     * @param effectiveTime the time that the retrieved elements must be effective for
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearTermAsDataValue(String glossaryTermGUID,
                                     String glossaryTermExternalIdentifier,
                                     Date   effectiveTime) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        final String methodName = "clearTermAsDataValue";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            glossaryManagerClient.clearTermAsDataValue(userId,
                                                       assetManagerGUID,
                                                       assetManagerName,
                                                       glossaryTermGUID,
                                                       glossaryTermExternalIdentifier,
                                                       effectiveTime,
                                                       forLineage,
                                                       forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(
                    CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(permittedSynchronization.getName(),
                                                                                                  connectorName,
                                                                                                  methodName),
                    this.getClass().getName(),
                    methodName,
                    userId);
        }
    }


    /**
     * Classify the glossary term to indicate that it describes a data value.
     *
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param glossaryTermExternalIdentifier unique identifier of the glossary term in the external asset manager
     * @param properties type of activity
     * @param effectiveTime the time that the retrieved elements must be effective for
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setTermAsActivity(String                        glossaryTermGUID,
                                  String                        glossaryTermExternalIdentifier,
                                  ActivityDescriptionProperties properties,
                                  Date                          effectiveTime) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        final String methodName = "setTermAsActivity";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            glossaryManagerClient.setTermAsActivity(userId,
                                                    assetManagerGUID,
                                                    assetManagerName,
                                                    glossaryTermGUID,
                                                    glossaryTermExternalIdentifier,
                                                    properties,
                                                    effectiveTime,
                                                    forLineage,
                                                    forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(
                    CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(permittedSynchronization.getName(),
                                                                                                  connectorName,
                                                                                                  methodName),
                    this.getClass().getName(),
                    methodName,
                    userId);
        }
    }


    /**
     * Remove the activity designation from the glossary term.
     *
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param glossaryTermExternalIdentifier unique identifier of the glossary term in the external asset manager
     * @param effectiveTime the time that the retrieved elements must be effective for
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearTermAsActivity(String glossaryTermGUID,
                                    String glossaryTermExternalIdentifier,
                                    Date   effectiveTime) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        final String methodName = "clearTermAsActivity";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            glossaryManagerClient.clearTermAsActivity(userId,
                                                      assetManagerGUID,
                                                      assetManagerName,
                                                      glossaryTermGUID,
                                                      glossaryTermExternalIdentifier,
                                                      effectiveTime,
                                                      forLineage,
                                                      forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(
                    CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(permittedSynchronization.getName(),
                                                                                                  connectorName,
                                                                                                  methodName),
                    this.getClass().getName(),
                    methodName,
                    userId);
        }
    }


    /**
     * Classify the glossary term to indicate that it describes a context.
     *
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param glossaryTermExternalIdentifier unique identifier of the glossary term in the external asset manager
     * @param contextDefinition more details of the context
     * @param effectiveTime the time that the retrieved elements must be effective for
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setTermAsContext(String                        glossaryTermGUID,
                                 String                        glossaryTermExternalIdentifier,
                                 GlossaryTermContextDefinition contextDefinition,
                                 Date                          effectiveTime) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        final String methodName = "setTermAsContext";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            glossaryManagerClient.setTermAsContext(userId,
                                                   assetManagerGUID,
                                                   assetManagerName,
                                                   glossaryTermGUID,
                                                   glossaryTermExternalIdentifier,
                                                   contextDefinition,
                                                   effectiveTime,
                                                   forLineage,
                                                   forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(
                    CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(permittedSynchronization.getName(),
                                                                                                  connectorName,
                                                                                                  methodName),
                    this.getClass().getName(),
                    methodName,
                    userId);
        }
    }


    /**
     * Remove the context definition designation from the glossary term.
     *
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param glossaryTermExternalIdentifier unique identifier of the glossary term in the external asset manager
     * @param effectiveTime the time that the retrieved elements must be effective for
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearTermAsContext(String glossaryTermGUID,
                                   String glossaryTermExternalIdentifier,
                                   Date   effectiveTime) throws InvalidParameterException,
                                                                UserNotAuthorizedException,
                                                                PropertyServerException
    {
        final String methodName = "clearTermAsContext";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            glossaryManagerClient.clearTermAsContext(userId,
                                                     assetManagerGUID,
                                                     assetManagerName,
                                                     glossaryTermGUID,
                                                     glossaryTermExternalIdentifier,
                                                     effectiveTime,
                                                     forLineage,
                                                     forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(
                    CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(permittedSynchronization.getName(),
                                                                                                  connectorName,
                                                                                                  methodName),
                    this.getClass().getName(),
                    methodName,
                    userId);
        }
    }


    /**
     * Classify the glossary term to indicate that it describes a spine object.
     *
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param glossaryTermExternalIdentifier unique identifier of the glossary term in the external asset manager
     * @param effectiveTime the time that the retrieved elements must be effective for
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setTermAsSpineObject(String glossaryTermGUID,
                                     String glossaryTermExternalIdentifier,
                                     Date   effectiveTime) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        final String methodName = "setTermAsSpineObject";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            glossaryManagerClient.setTermAsSpineObject(userId,
                                                       assetManagerGUID,
                                                       assetManagerName,
                                                       glossaryTermGUID,
                                                       glossaryTermExternalIdentifier,
                                                       effectiveTime,
                                                       forLineage,
                                                       forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(
                    CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(permittedSynchronization.getName(),
                                                                                                  connectorName,
                                                                                                  methodName),
                    this.getClass().getName(),
                    methodName,
                    userId);
        }
    }


    /**
     * Remove the spine object designation from the glossary term.
     *
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param glossaryTermExternalIdentifier unique identifier of the glossary term in the external asset manager
     * @param effectiveTime the time that the retrieved elements must be effective for
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearTermAsSpineObject(String glossaryTermGUID,
                                       String glossaryTermExternalIdentifier,
                                       Date   effectiveTime) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        final String methodName = "clearTermAsSpineObject";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            glossaryManagerClient.clearTermAsSpineObject(userId,
                                                         assetManagerGUID,
                                                         assetManagerName,
                                                         glossaryTermGUID,
                                                         glossaryTermExternalIdentifier,
                                                         effectiveTime,
                                                         forLineage,
                                                         forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(
                    CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(permittedSynchronization.getName(),
                                                                                                  connectorName,
                                                                                                  methodName),
                    this.getClass().getName(),
                    methodName,
                    userId);
        }
    }



    /**
     * Classify the glossary term to indicate that it describes a spine attribute.
     *
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param glossaryTermExternalIdentifier unique identifier of the glossary term in the external asset manager
     * @param effectiveTime the time that the retrieved elements must be effective for
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setTermAsSpineAttribute(String glossaryTermGUID,
                                        String glossaryTermExternalIdentifier,
                                        Date   effectiveTime) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {
        final String methodName = "setTermAsSpineAttribute";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            glossaryManagerClient.setTermAsSpineAttribute(userId,
                                                          assetManagerGUID,
                                                          assetManagerName,
                                                          glossaryTermGUID,
                                                          glossaryTermExternalIdentifier,
                                                          effectiveTime,
                                                          forLineage,
                                                          forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(
                    CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(permittedSynchronization.getName(),
                                                                                                  connectorName,
                                                                                                  methodName),
                    this.getClass().getName(),
                    methodName,
                    userId);
        }
    }


    /**
     * Remove the spine attribute designation from the glossary term.
     *
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param glossaryTermExternalIdentifier unique identifier of the glossary term in the external asset manager
     * @param effectiveTime the time that the retrieved elements must be effective for
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearTermAsSpineAttribute(String glossaryTermGUID,
                                          String glossaryTermExternalIdentifier,
                                          Date   effectiveTime) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        final String methodName = "clearTermAsSpineAttribute";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            glossaryManagerClient.clearTermAsSpineAttribute(userId,
                                                            assetManagerGUID,
                                                            assetManagerName,
                                                            glossaryTermGUID,
                                                            glossaryTermExternalIdentifier,
                                                            effectiveTime,
                                                            forLineage,
                                                            forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(
                    CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(permittedSynchronization.getName(),
                                                                                                  connectorName,
                                                                                                  methodName),
                    this.getClass().getName(),
                    methodName,
                    userId);
        }
    }


    /**
     * Classify the glossary term to indicate that it describes an object identifier.
     *
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param glossaryTermExternalIdentifier unique identifier of the glossary term in the external asset manager
     * @param effectiveTime the time that the retrieved elements must be effective for
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setTermAsObjectIdentifier(String glossaryTermGUID,
                                          String glossaryTermExternalIdentifier,
                                          Date   effectiveTime) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        final String methodName = "setTermAsObjectIdentifier";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            glossaryManagerClient.setTermAsObjectIdentifier(userId,
                                                            assetManagerGUID,
                                                            assetManagerName,
                                                            glossaryTermGUID,
                                                            glossaryTermExternalIdentifier,
                                                            effectiveTime,
                                                            forLineage,
                                                            forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(
                    CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(permittedSynchronization.getName(),
                                                                                                  connectorName,
                                                                                                  methodName),
                    this.getClass().getName(),
                    methodName,
                    userId);
        }
    }


    /**
     * Remove the object identifier designation from the glossary term.
     *
     * @param glossaryTermGUID unique identifier of the metadata element to update
     * @param glossaryTermExternalIdentifier unique identifier of the glossary term in the external asset manager
     * @param effectiveTime the time that the retrieved elements must be effective for
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearTermAsObjectIdentifier(String glossaryTermGUID,
                                            String glossaryTermExternalIdentifier,
                                            Date   effectiveTime) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        final String methodName = "clearTermAsObjectIdentifier";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            glossaryManagerClient.clearTermAsObjectIdentifier(userId,
                                                              assetManagerGUID,
                                                              assetManagerName,
                                                              glossaryTermGUID,
                                                              glossaryTermExternalIdentifier,
                                                              effectiveTime,
                                                              forLineage,
                                                              forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(
                    CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(permittedSynchronization.getName(),
                                                                                                  connectorName,
                                                                                                  methodName),
                    this.getClass().getName(),
                    methodName,
                    userId);
        }
    }


    /**
     * Remove the metadata element representing a glossary term.
     *
     * @param glossaryTermGUID unique identifier of the metadata element to remove
     * @param glossaryTermExternalIdentifier unique identifier of the glossary term in the external asset manager
     * @param effectiveTime the time that the retrieved elements must be effective for
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeGlossaryTerm(String glossaryTermGUID,
                                   String glossaryTermExternalIdentifier,
                                   Date   effectiveTime) throws InvalidParameterException,
                                                                UserNotAuthorizedException,
                                                                PropertyServerException
    {
        final String methodName = "removeGlossaryTerm";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            glossaryManagerClient.removeGlossaryTerm(userId,
                                                     assetManagerGUID,
                                                     assetManagerName,
                                                     glossaryTermGUID,
                                                     glossaryTermExternalIdentifier,
                                                     effectiveTime,
                                                     forLineage,
                                                     forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(
                    CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(permittedSynchronization.getName(),
                                                                                                  connectorName,
                                                                                                  methodName),
                    this.getClass().getName(),
                    methodName,
                    userId);
        }
    }


    /**
     * Retrieve the list of glossary term metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime the time that the retrieved elements must be effective for
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<GlossaryTermElement>   findGlossaryTerms(String searchString,
                                                         int    startFrom,
                                                         int    pageSize,
                                                         Date   effectiveTime) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        return glossaryManagerClient.findGlossaryTerms(userId,
                                                       assetManagerGUID,
                                                       assetManagerName,
                                                       null,
                                                       searchString,
                                                       null,
                                                       startFrom,
                                                       pageSize,
                                                       effectiveTime,
                                                       forLineage,
                                                       forDuplicateProcessing);
    }


    /**
     * Retrieve the list of glossary term metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param glossaryGUID unique identifier of the glossary to query
     * @param searchString string to find in the properties
     * @param limitResultsByStatus By default, terms in all statuses are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all status values.
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime the time that the retrieved elements must be effective for
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<GlossaryTermElement>   findGlossaryTerms(String                   glossaryGUID,
                                                         String                   searchString,
                                                         List<GlossaryTermStatus> limitResultsByStatus,
                                                         int                      startFrom,
                                                         int                      pageSize,
                                                         Date                     effectiveTime) throws InvalidParameterException,
                                                                                                        UserNotAuthorizedException,
                                                                                                        PropertyServerException
    {
        return glossaryManagerClient.findGlossaryTerms(userId,
                                                       assetManagerGUID,
                                                       assetManagerName,
                                                       glossaryGUID,
                                                       searchString,
                                                       limitResultsByStatus,
                                                       startFrom,
                                                       pageSize,
                                                       effectiveTime,
                                                       forLineage,
                                                       forDuplicateProcessing);
    }


    /**
     * Retrieve the list of glossary terms associated with a glossary.
     *
     * @param glossaryGUID unique identifier of the glossary of interest
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime the time that the retrieved elements must be effective for
     *
     * @return list of associated metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<GlossaryTermElement>    getTermsForGlossary(String glossaryGUID,
                                                            int    startFrom,
                                                            int    pageSize,
                                                            Date   effectiveTime) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        return glossaryManagerClient.getTermsForGlossary(userId,
                                                         assetManagerGUID,
                                                         assetManagerName,
                                                         glossaryGUID,
                                                         startFrom,
                                                         pageSize,
                                                         effectiveTime,
                                                         forLineage,
                                                         forDuplicateProcessing);
    }


    /**
     * Retrieve the list of glossary terms associated with a glossary category.
     *
     * @param glossaryCategoryGUID unique identifier of the glossary category of interest
     * @param limitResultsByStatus By default, term relationships in all statuses are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all status values.
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime the time that the retrieved elements must be effective for
     *
     * @return list of associated metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<GlossaryTermElement>    getTermsForGlossaryCategory(String                               glossaryCategoryGUID,
                                                                    List<GlossaryTermRelationshipStatus> limitResultsByStatus,
                                                                    int                                  startFrom,
                                                                    int                                  pageSize,
                                                                    Date                                 effectiveTime) throws InvalidParameterException,
                                                                                                                               UserNotAuthorizedException,
                                                                                                                               PropertyServerException
    {
        return glossaryManagerClient.getTermsForGlossaryCategory(userId,
                                                                 assetManagerGUID,
                                                                 assetManagerName,
                                                                 glossaryCategoryGUID,
                                                                 limitResultsByStatus,
                                                                 startFrom,
                                                                 pageSize,
                                                                 effectiveTime,
                                                                 forLineage,
                                                                 forDuplicateProcessing);
    }


    /**
     * Retrieve the list of glossary terms associated with the requested glossary term.
     *
     * @param glossaryTermGUID unique identifier of the glossary of interest
     * @param relationshipTypeName optional name of relationship
     * @param limitResultsByStatus By default, term relationships in all statuses are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all status values.
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime the time that the retrieved elements must be effective for
     *
     * @return list of associated metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<GlossaryTermElement>    getRelatedTerms(String                               glossaryTermGUID,
                                                        String                               relationshipTypeName,
                                                        List<GlossaryTermRelationshipStatus> limitResultsByStatus,
                                                        int                                  startFrom,
                                                        int                                  pageSize,
                                                        Date                                 effectiveTime) throws InvalidParameterException,
                                                                                                                   UserNotAuthorizedException,
                                                                                                                   PropertyServerException
    {
        return glossaryManagerClient.getRelatedTerms(userId,
                                                     assetManagerGUID,
                                                     assetManagerName,
                                                     glossaryTermGUID,
                                                     relationshipTypeName,
                                                     limitResultsByStatus,
                                                     startFrom,
                                                     pageSize,
                                                     effectiveTime,
                                                     forLineage,
                                                     forDuplicateProcessing);
    }

    /**
     * Retrieve the list of glossary term metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime the time that the retrieved elements must be effective for
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<GlossaryTermElement>   getGlossaryTermsByName(String name,
                                                              int    startFrom,
                                                              int    pageSize,
                                                              Date   effectiveTime) throws InvalidParameterException, 
                                                                                           UserNotAuthorizedException, 
                                                                                           PropertyServerException
    {
        return glossaryManagerClient.getGlossaryTermsByName(userId,
                                                            assetManagerGUID,
                                                            assetManagerName,
                                                            null,
                                                            name,
                                                            null,
                                                            startFrom,
                                                            pageSize,
                                                            effectiveTime,
                                                            forLineage,
                                                            forDuplicateProcessing);
    }


    /**
     * Retrieve the list of glossary term metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param glossaryGUID unique identifier of the glossary to query
     * @param name name to search for
     * @param limitResultsByStatus By default, terms in all statuses are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all status values.
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime the time that the retrieved elements must be effective for
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<GlossaryTermElement>   getGlossaryTermsByName(String                   glossaryGUID,
                                                              String                   name,
                                                              List<GlossaryTermStatus> limitResultsByStatus,
                                                              int                      startFrom,
                                                              int                      pageSize,
                                                              Date                     effectiveTime) throws InvalidParameterException,
                                                                                                             UserNotAuthorizedException,
                                                                                                             PropertyServerException
    {
        return glossaryManagerClient.getGlossaryTermsByName(userId,
                                                            assetManagerGUID,
                                                            assetManagerName,
                                                            glossaryGUID,
                                                            name,
                                                            limitResultsByStatus,
                                                            startFrom,
                                                            pageSize,
                                                            effectiveTime,
                                                            forLineage,
                                                            forDuplicateProcessing);
    }


    /**
     * Retrieve the glossary term metadata element with the supplied unique identifier.
     *
     * @param guid unique identifier of the requested metadata element
     * @param effectiveTime the time that the retrieved elements must be effective for
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GlossaryTermElement getGlossaryTermByGUID(String guid,
                                                     Date   effectiveTime) throws InvalidParameterException, 
                                                                                  UserNotAuthorizedException, 
                                                                                  PropertyServerException
    {
        return glossaryManagerClient.getGlossaryTermByGUID(userId, assetManagerGUID, assetManagerName, guid, effectiveTime, forLineage,
                                                           forDuplicateProcessing);
    }
}
