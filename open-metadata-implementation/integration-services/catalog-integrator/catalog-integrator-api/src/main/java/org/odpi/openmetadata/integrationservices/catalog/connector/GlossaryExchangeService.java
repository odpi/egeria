/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.integrationservices.catalog.connector;

import org.odpi.openmetadata.accessservices.assetmanager.client.GlossaryExchangeClient;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.ExternalGlossaryLinkElement;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.GlossaryCategoryElement;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.GlossaryElement;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.GlossaryTermElement;
import org.odpi.openmetadata.accessservices.assetmanager.properties.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.integrationservices.catalog.ffdc.CatalogIntegratorErrorCode;

import java.util.List;
import java.util.Map;

/**
 * GlossaryExchangeService is the client for managing resources from a glossary.  This includes
 * the glossary container, glossary categories and terms as well as relationships between them.
 */
public class GlossaryExchangeService
{
    private GlossaryExchangeClient   glossaryManagerClient;
    private String                   userId;
    private String                   assetManagerGUID;
    private String                   assetManagerName;
    private String                   connectorName;
    private SynchronizationDirection synchronizationDirection;
    private AuditLog                 auditLog;


    /**
     * Create a new client to exchange glossary content with open metadata.
     *
     * @param glossaryManagerClient client for exchange requests
     * @param synchronizationDirection direction(s) that metadata can flow
     * @param userId integration daemon's userId
     * @param assetManagerGUID unique identifier of the software server capability for the asset manager
     * @param assetManagerName unique name of the software server capability for the asset manager
     * @param connectorName name of the connector using this context
     * @param auditLog logging destination
     */
    GlossaryExchangeService(GlossaryExchangeClient   glossaryManagerClient,
                            SynchronizationDirection synchronizationDirection,
                            String                   userId,
                            String                   assetManagerGUID,
                            String                   assetManagerName,
                            String                   connectorName,
                            AuditLog                 auditLog)
    {
        this.glossaryManagerClient    = glossaryManagerClient;
        this.synchronizationDirection = synchronizationDirection;
        this.userId                   = userId;
        this.assetManagerGUID         = assetManagerGUID;
        this.assetManagerName         = assetManagerName;
        this.connectorName            = connectorName;
        this.auditLog                 = auditLog;
    }


    /* ========================================================
     * The Glossary entity is the top level element in a glossary.
     */


    /**
     * Create a new metadata element to represent the root of a glossary.  All categories and terms are linked
     * to a single glossary.  They are owned by this glossary and if the glossary is deleted, any linked terms and
     * categories are deleted as well.
     *
     * @param glossaryExternalIdentifier unique identifier of the glossary in the external asset manager
     * @param glossaryExternalIdentifierName name of property for the external identifier in the external asset manager
     * @param glossaryExternalIdentifierUsage usage for the open metadata element when mapping to external element in the external asset manager
     * @param glossaryExternalIdentifierKeyPattern pattern for the external identifier within the external asset manager (default is LOCAL_KEY)
     * @param mappingProperties additional properties to help with the mapping of the elements in the
     *                          external asset manager and open metadata
     * @param glossaryProperties properties to store
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createGlossary(String              glossaryExternalIdentifier,
                                 String              glossaryExternalIdentifierName,
                                 String              glossaryExternalIdentifierUsage,
                                 KeyPattern          glossaryExternalIdentifierKeyPattern,
                                 Map<String, String> mappingProperties,
                                 GlossaryProperties  glossaryProperties) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        final String methodName = "createGlossary";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            return glossaryManagerClient.createGlossary(userId,
                                                        assetManagerGUID,
                                                        assetManagerName,
                                                        glossaryExternalIdentifier,
                                                        glossaryExternalIdentifierName,
                                                        glossaryExternalIdentifierUsage,
                                                        connectorName,
                                                        glossaryExternalIdentifierKeyPattern,
                                                        mappingProperties,
                                                        glossaryProperties);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                                                                        synchronizationDirection.getName(),
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
     *
     * All categories and terms are linked to a single glossary.  They are owned by this glossary and if the
     * glossary is deleted, any linked terms and categories are deleted as well.
     *
     * @param glossaryExternalIdentifier unique identifier of the glossary in the external asset manager
     * @param glossaryExternalIdentifierName name of property for the external identifier in the external asset manager
     * @param glossaryExternalIdentifierUsage usage for the open metadata element when mapping to external element in the external asset manager
     * @param glossaryExternalIdentifierKeyPattern pattern for the external identifier within the external asset manager (default is LOCAL_KEY)
     * @param mappingProperties additional properties to help with the mapping of the elements in the
     *                          external asset manager and open metadata
     * @param templateGUID unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createGlossaryFromTemplate(String              templateGUID,
                                             String              glossaryExternalIdentifier,
                                             String              glossaryExternalIdentifierName,
                                             String              glossaryExternalIdentifierUsage,
                                             KeyPattern          glossaryExternalIdentifierKeyPattern,
                                             Map<String, String> mappingProperties,
                                             TemplateProperties  templateProperties) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        final String methodName = "createGlossaryFromTemplate";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            return glossaryManagerClient.createGlossaryFromTemplate(userId,
                                                                    assetManagerGUID,
                                                                    assetManagerName,
                                                                    templateGUID,
                                                                    glossaryExternalIdentifier,
                                                                    glossaryExternalIdentifierName,
                                                                    glossaryExternalIdentifierUsage,
                                                                    connectorName,
                                                                    glossaryExternalIdentifierKeyPattern,
                                                                    mappingProperties,
                                                                    templateProperties);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                                                                    synchronizationDirection.getName(),
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
     * @param glossaryProperties new properties for this element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateGlossary(String             glossaryGUID,
                               String             glossaryExternalIdentifier,
                               GlossaryProperties glossaryProperties) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        final String methodName = "updateGlossary";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            glossaryManagerClient.updateGlossary(userId, assetManagerGUID, assetManagerName, glossaryGUID, glossaryExternalIdentifier, glossaryProperties);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    synchronizationDirection.getName(),
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
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeGlossary(String glossaryGUID,
                               String glossaryExternalIdentifier) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        final String methodName = "removeGlossary";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            glossaryManagerClient.removeGlossary(userId, assetManagerGUID, assetManagerName, glossaryGUID, glossaryExternalIdentifier);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    synchronizationDirection.getName(),
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
     * are linked to the assets etc and as such they are logically categorized by the linked category.
     *
     * @param glossaryGUID unique identifier of the metadata element to remove
     * @param glossaryExternalIdentifier unique identifier of the glossary in the external asset manager
     * @param organizingPrinciple description of how the glossary is organized
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setGlossaryAsTaxonomy(String glossaryGUID,
                                      String glossaryExternalIdentifier,
                                      String organizingPrinciple) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        final String methodName = "setGlossaryAsTaxonomy";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            glossaryManagerClient.setGlossaryAsTaxonomy(userId, assetManagerGUID, assetManagerName, glossaryGUID, glossaryExternalIdentifier, organizingPrinciple);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    synchronizationDirection.getName(),
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
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearGlossaryAsTaxonomy(String glossaryGUID,
                                        String glossaryExternalIdentifier) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        final String methodName = "clearGlossaryAsTaxonomy";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            glossaryManagerClient.clearGlossaryAsTaxonomy(userId, assetManagerGUID, assetManagerName, glossaryGUID, glossaryExternalIdentifier);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    synchronizationDirection.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Classify a glossary to declare that it has no two GlossaryTerm definitions with
     * the same name.  This means there is only one definition for each term.  Typically the terms are also of a similar
     * level of granularity and are limited to a specific scope of use.
     *
     * Canonical vocabularies are used to semantically classify assets in an unambiguous way.
     *
     * @param glossaryGUID unique identifier of the metadata element to remove
     * @param glossaryExternalIdentifier unique identifier of the glossary in the external asset manager
     * @param scope description of the situations where this glossary is relevant.
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setGlossaryAsCanonical(String glossaryGUID,
                                       String glossaryExternalIdentifier,
                                       String scope) throws InvalidParameterException,
                                                            UserNotAuthorizedException,
                                                            PropertyServerException
    {
        final String methodName = "setGlossaryAsCanonical";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            glossaryManagerClient.setGlossaryAsCanonical(userId, assetManagerGUID, assetManagerName, glossaryGUID, glossaryExternalIdentifier, scope);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    synchronizationDirection.getName(),
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
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearGlossaryAsCanonical(String glossaryGUID,
                                         String glossaryExternalIdentifier) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        final String methodName = "clearGlossaryAsCanonical";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            glossaryManagerClient.clearGlossaryAsCanonical(userId, assetManagerGUID, assetManagerName, glossaryGUID, glossaryExternalIdentifier);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    synchronizationDirection.getName(),
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
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<GlossaryElement>   findGlossaries(String searchString,
                                                  int    startFrom,
                                                  int    pageSize) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        return glossaryManagerClient.findGlossaries(userId, assetManagerGUID, assetManagerName, searchString, startFrom, pageSize);
    }


    /**
     * Retrieve the list of glossary metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<GlossaryElement>   getGlossariesByName(String name,
                                                       int    startFrom,
                                                       int    pageSize) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        return glossaryManagerClient.getGlossariesByName(userId, assetManagerGUID, assetManagerName, name, startFrom, pageSize);
    }


    /**
     * Retrieve the list of glossaries created on behalf of this asset manager.
     *
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<GlossaryElement>   getGlossariesForAssetManager(int startFrom,
                                                                int pageSize) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        return glossaryManagerClient.getGlossariesForAssetManager(userId, assetManagerGUID, assetManagerName, startFrom, pageSize);
    }


    /**
     * Retrieve the glossary metadata element with the supplied unique identifier.
     *
     * @param openMetadataGUID unique identifier of the requested metadata element
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GlossaryElement getGlossaryByGUID(String openMetadataGUID) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        return glossaryManagerClient.getGlossaryByGUID(userId, assetManagerGUID, assetManagerName, openMetadataGUID);
    }


    /* =====================================================================================================================
     * A glossary may host one or more glossary categories depending on its capability
     */

    /**
     * Create a new metadata element to represent a glossary category.
     *
     * @param glossaryGUID unique identifier of the glossary where the category is located
     * @param glossaryCategoryExternalIdentifier unique identifier of the glossary category in the external asset manager
     * @param glossaryCategoryExternalIdentifierName name of property for the external identifier in the external asset manager
     * @param glossaryCategoryExternalIdentifierUsage usage for the open metadata element when mapping to external element in the external asset manager
     * @param glossaryCategoryExternalIdentifierKeyPattern pattern for the external identifier within the external asset manager (default is LOCAL_KEY)
     * @param mappingProperties additional properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param glossaryCategoryProperties properties about the glossary category to store
     *
     * @return unique identifier of the new glossary category
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createGlossaryCategory(String                     glossaryGUID,
                                         String                     glossaryCategoryExternalIdentifier,
                                         String                     glossaryCategoryExternalIdentifierName,
                                         String                     glossaryCategoryExternalIdentifierUsage,
                                         KeyPattern                 glossaryCategoryExternalIdentifierKeyPattern,
                                         Map<String, String>        mappingProperties,
                                         GlossaryCategoryProperties glossaryCategoryProperties) throws InvalidParameterException,
                                                                                                       UserNotAuthorizedException,
                                                                                                       PropertyServerException
    {
        final String methodName = "createGlossaryCategory";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            return glossaryManagerClient.createGlossaryCategory(userId,
                                                                assetManagerGUID,
                                                                assetManagerName,
                                                                glossaryGUID,
                                                                glossaryCategoryExternalIdentifier,
                                                                glossaryCategoryExternalIdentifierName,
                                                                glossaryCategoryExternalIdentifierUsage,
                                                                connectorName,
                                                                glossaryCategoryExternalIdentifierKeyPattern,
                                                                mappingProperties,
                                                                glossaryCategoryProperties);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    synchronizationDirection.getName(),
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
     * @param templateGUID unique identifier of the metadata element to copy
     * @param glossaryCategoryExternalIdentifier unique identifier of the glossary category in the external asset manager
     * @param glossaryCategoryExternalIdentifierName name of property for the external identifier in the external asset manager
     * @param glossaryCategoryExternalIdentifierUsage usage for the open metadata element when mapping to external element in the external asset manager
     * @param glossaryCategoryExternalIdentifierKeyPattern pattern for the external identifier within the external asset manager (default is LOCAL_KEY)
     * @param mappingProperties additional properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new glossary category
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createGlossaryCategoryFromTemplate(String              templateGUID,
                                                     String              glossaryCategoryExternalIdentifier,
                                                     String              glossaryCategoryExternalIdentifierName,
                                                     String              glossaryCategoryExternalIdentifierUsage,
                                                     KeyPattern          glossaryCategoryExternalIdentifierKeyPattern,
                                                     Map<String, String> mappingProperties,
                                                     TemplateProperties  templateProperties) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException
    {
        final String methodName = "createGlossaryCategoryFromTemplate";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            return glossaryManagerClient.createGlossaryCategoryFromTemplate(userId,
                                                                            assetManagerGUID,
                                                                            assetManagerName,
                                                                            templateGUID,
                                                                            glossaryCategoryExternalIdentifier,
                                                                            glossaryCategoryExternalIdentifierName,
                                                                            glossaryCategoryExternalIdentifierUsage,
                                                                            connectorName,
                                                                            glossaryCategoryExternalIdentifierKeyPattern,
                                                                            mappingProperties,
                                                                            templateProperties);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    synchronizationDirection.getName(),
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
     * @param glossaryCategoryProperties new properties for the metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateGlossaryCategory(String                     glossaryCategoryGUID,
                                       String                     glossaryCategoryExternalIdentifier,
                                       GlossaryCategoryProperties glossaryCategoryProperties) throws InvalidParameterException,
                                                                                                     UserNotAuthorizedException,
                                                                                                     PropertyServerException
    {
        final String methodName = "updateGlossaryCategory";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            glossaryManagerClient.updateGlossaryCategory(userId, assetManagerGUID, assetManagerName, glossaryCategoryGUID, glossaryCategoryExternalIdentifier, glossaryCategoryProperties);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    synchronizationDirection.getName(),
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
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupCategoryParent(String glossaryParentCategoryGUID,
                                    String glossaryChildCategoryGUID) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        final String methodName = "setupCategoryParent";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            glossaryManagerClient.setupCategoryParent(userId, assetManagerGUID, assetManagerName, glossaryParentCategoryGUID, glossaryChildCategoryGUID);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    synchronizationDirection.getName(),
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
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearCategoryParent(String glossaryParentCategoryGUID,
                                    String glossaryChildCategoryGUID) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        final String methodName = "clearCategoryParent";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            glossaryManagerClient.clearCategoryParent(userId, assetManagerGUID, assetManagerName, glossaryParentCategoryGUID, glossaryChildCategoryGUID);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    synchronizationDirection.getName(),
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
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeGlossaryCategory(String glossaryCategoryGUID,
                                       String glossaryCategoryExternalIdentifier) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        final String methodName = "removeGlossaryCategory";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            glossaryManagerClient.removeGlossaryCategory(userId, assetManagerGUID, assetManagerName, glossaryCategoryGUID, glossaryCategoryExternalIdentifier);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    synchronizationDirection.getName(),
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
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<GlossaryCategoryElement>   findGlossaryCategories(String searchString,
                                                                  int    startFrom,
                                                                  int    pageSize) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        return glossaryManagerClient.findGlossaryCategories(userId, assetManagerGUID, assetManagerName, searchString, startFrom, pageSize);
    }


    /**
     * Return the list of categories associated with a glossary.
     *
     * @param glossaryGUID unique identifier of the glossary to query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of metadata elements describing the categories associated with the requested glossary
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<GlossaryCategoryElement>   getCategoriesForGlossary(String glossaryGUID,
                                                                    int    startFrom,
                                                                    int    pageSize) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        return glossaryManagerClient.getCategoriesForGlossary(userId, assetManagerGUID, assetManagerName, glossaryGUID, startFrom, pageSize);
    }


    /**
     * Retrieve the list of glossary category metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<GlossaryCategoryElement>   getGlossaryCategoriesByName(String name,
                                                                       int    startFrom,
                                                                       int    pageSize) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException
    {
        return glossaryManagerClient.getGlossaryCategoriesByName(userId, assetManagerGUID, assetManagerName, name, startFrom, pageSize);
    }


    /**
     * Retrieve the glossary category metadata element with the supplied unique identifier.
     *
     * @param guid unique identifier of the requested metadata element
     *
     * @return requested metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GlossaryCategoryElement getGlossaryCategoryByGUID(String guid) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        return glossaryManagerClient.getGlossaryCategoryByGUID(userId, assetManagerGUID, assetManagerName, guid);
    }


    /**
     * Retrieve the glossary category metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param glossaryCategoryGUID unique identifier of the requested metadata element
     *
     * @return parent glossary category element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GlossaryCategoryElement getGlossaryCategoryParent(String userId,
                                                             String assetManagerGUID,
                                                             String assetManagerName,
                                                             String glossaryCategoryGUID) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException
    {
        return glossaryManagerClient.getGlossaryCategoryParent(userId, assetManagerGUID, assetManagerName, glossaryCategoryGUID);
    }


    /**
     * Retrieve the glossary category metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param glossaryCategoryGUID unique identifier of the requested metadata element
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of glossary category element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<GlossaryCategoryElement> getGlossarySubCategories(String userId,
                                                                  String assetManagerGUID,
                                                                  String assetManagerName,
                                                                  String glossaryCategoryGUID,
                                                                  int    startFrom,
                                                                  int    pageSize) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        return glossaryManagerClient.getGlossarySubCategories(userId, assetManagerGUID, assetManagerName, glossaryCategoryGUID, startFrom, pageSize);
    }


    /* ===============================================================================
     * A glossary typically contains many glossary terms, linked with relationships.
     */

    /**
     * Create a new metadata element to represent a glossary term.
     *
     * @param glossaryGUID unique identifier of the glossary where the term is located
     * @param glossaryTermExternalIdentifier unique identifier of the glossary term in the external asset manager
     * @param glossaryTermExternalIdentifierName name of property for the external identifier in the external asset manager
     * @param glossaryTermExternalIdentifierUsage usage for the open metadata element when mapping to external element in the external asset manager
     * @param glossaryTermExternalIdentifierKeyPattern pattern for the external identifier within the external asset manager (default is LOCAL_KEY)
     * @param mappingProperties additional properties to help with the mapping of the elements in the
     *                          external asset manager and open metadata
     * @param glossaryTermProperties properties for the glossary term
     *
     * @return unique identifier of the new metadata element for the glossary term
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createGlossaryTerm(String                 glossaryGUID,
                                     String                 glossaryTermExternalIdentifier,
                                     String                 glossaryTermExternalIdentifierName,
                                     String                 glossaryTermExternalIdentifierUsage,
                                     KeyPattern             glossaryTermExternalIdentifierKeyPattern,
                                     Map<String, String>    mappingProperties,
                                     GlossaryTermProperties glossaryTermProperties) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        final String methodName = "createGlossaryTerm";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            return glossaryManagerClient.createGlossaryTerm(userId,
                                                            assetManagerGUID,
                                                            assetManagerName,
                                                            glossaryGUID,
                                                            glossaryTermExternalIdentifier,
                                                            glossaryTermExternalIdentifierName,
                                                            glossaryTermExternalIdentifierUsage,
                                                            connectorName,
                                                            glossaryTermExternalIdentifierKeyPattern,
                                                            mappingProperties,
                                                            glossaryTermProperties);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    synchronizationDirection.getName(),
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
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param glossaryGUID unique identifier of the glossary where the term is located
     * @param glossaryTermExternalIdentifier unique identifier of the glossary term in the external asset manager
     * @param glossaryTermExternalIdentifierName name of property for the external identifier in the external asset manager
     * @param glossaryTermExternalIdentifierUsage usage for the open metadata element when mapping to external element in the external asset manager
     * @param glossaryTermExternalIdentifierKeyPattern pattern for the external identifier within the external asset manager (default is LOCAL_KEY)
     * @param mappingProperties additional properties to help with the mapping of the elements in the
     *                          external asset manager and open metadata
     * @param glossaryTermProperties properties for the glossary term
     * @param initialStatus glossary term status to use when the object is created
     *
     * @return unique identifier of the new metadata element for the glossary term
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createControlledGlossaryTerm(String                 userId,
                                               String                 assetManagerGUID,
                                               String                 assetManagerName,
                                               String                 glossaryGUID,
                                               String                 glossaryTermExternalIdentifier,
                                               String                 glossaryTermExternalIdentifierName,
                                               String                 glossaryTermExternalIdentifierUsage,
                                               KeyPattern             glossaryTermExternalIdentifierKeyPattern,
                                               Map<String, String>    mappingProperties,
                                               GlossaryTermProperties glossaryTermProperties,
                                               GlossaryTermStatus     initialStatus) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        final String methodName = "createControlledGlossaryTerm";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            return glossaryManagerClient.createControlledGlossaryTerm(userId,
                                                                      assetManagerGUID,
                                                                      assetManagerName,
                                                                      glossaryGUID,
                                                                      glossaryTermExternalIdentifier,
                                                                      glossaryTermExternalIdentifierName,
                                                                      glossaryTermExternalIdentifierUsage,
                                                                      connectorName,
                                                                      glossaryTermExternalIdentifierKeyPattern,
                                                                      mappingProperties,
                                                                      glossaryTermProperties,
                                                                      initialStatus);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    synchronizationDirection.getName(),
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
     * @param templateGUID unique identifier of the metadata element to copy
     * @param glossaryGUID unique identifier of the glossary where the glossary term is located.
     * @param glossaryTermExternalIdentifier unique identifier of the glossary term in the external asset manager
     * @param glossaryTermExternalIdentifierName name of property for the external identifier in the external asset manager
     * @param glossaryTermExternalIdentifierUsage usage for the open metadata element when mapping to external element in the external asset manager
     * @param glossaryTermExternalIdentifierKeyPattern pattern for the external identifier within the external asset manager (default is LOCAL_KEY)
     * @param mappingProperties additional properties to help with the mapping of the elements in the
     *                          external asset manager and open metadata
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new metadata element for the glossary term
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createGlossaryTermFromTemplate(String              templateGUID,
                                                 String              glossaryGUID,
                                                 String              glossaryTermExternalIdentifier,
                                                 String              glossaryTermExternalIdentifierName,
                                                 String              glossaryTermExternalIdentifierUsage,
                                                 KeyPattern          glossaryTermExternalIdentifierKeyPattern,
                                                 Map<String, String> mappingProperties,
                                                 TemplateProperties  templateProperties) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException
    {
        final String methodName = "createGlossaryTermFromTemplate";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            return glossaryManagerClient.createGlossaryTermFromTemplate(userId,
                                                                        assetManagerGUID,
                                                                        assetManagerName,
                                                                        templateGUID,
                                                                        glossaryTermExternalIdentifier,
                                                                        glossaryTermExternalIdentifierName,
                                                                        glossaryTermExternalIdentifierUsage,
                                                                        connectorName,
                                                                        glossaryTermExternalIdentifierKeyPattern,
                                                                        mappingProperties,
                                                                        templateProperties);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    synchronizationDirection.getName(),
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
     * @param glossaryTermProperties new properties for the glossary term
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateGlossaryTerm(String                 glossaryTermGUID,
                                   String                 glossaryTermExternalIdentifier,
                                   GlossaryTermProperties glossaryTermProperties) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        final String methodName = "updateGlossaryTerm";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            glossaryManagerClient.updateGlossaryTerm(userId, assetManagerGUID, assetManagerName, glossaryTermGUID, glossaryTermExternalIdentifier, glossaryTermProperties);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    synchronizationDirection.getName(),
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
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param glossaryTermGUID unique identifier of the glossary term to update
     * @param glossaryTermExternalIdentifier unique identifier of the glossary term in the external asset manager
     * @param glossaryTermStatus new properties for the glossary term
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateGlossaryTermStatus(String             userId,
                                         String             assetManagerGUID,
                                         String             assetManagerName,
                                         String             glossaryTermGUID,
                                         String             glossaryTermExternalIdentifier,
                                         GlossaryTermStatus glossaryTermStatus) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        final String methodName = "updateGlossaryTermStatus";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            glossaryManagerClient.updateGlossaryTermStatus(userId, assetManagerGUID, assetManagerName, glossaryTermGUID, glossaryTermExternalIdentifier, glossaryTermStatus);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    synchronizationDirection.getName(),
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
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupTermCategory(String                     glossaryCategoryGUID,
                                  String                     glossaryTermGUID,
                                  GlossaryTermCategorization categorizationProperties) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException
    {
        final String methodName = "setupTermCategory";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            glossaryManagerClient.setupTermCategory(userId, assetManagerGUID, assetManagerName, glossaryCategoryGUID, glossaryTermGUID, categorizationProperties);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    synchronizationDirection.getName(),
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

     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearTermCategory(String glossaryCategoryGUID,
                                  String glossaryTermGUID) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        final String methodName = "clearTermCategory";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            glossaryManagerClient.clearTermCategory(userId, assetManagerGUID, assetManagerName, glossaryCategoryGUID, glossaryTermGUID);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    synchronizationDirection.getName(),
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
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupTermRelationship(String                   relationshipTypeName,
                                      String                   glossaryTermOneGUID,
                                      String                   glossaryTermTwoGUID,
                                      GlossaryTermRelationship relationshipsProperties) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException
    {
        final String methodName = "setupTermRelationship";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            glossaryManagerClient.setupTermRelationship(userId, assetManagerGUID, assetManagerName, relationshipTypeName, glossaryTermOneGUID, glossaryTermTwoGUID, relationshipsProperties);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    synchronizationDirection.getName(),
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
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateTermRelationship(String                   relationshipTypeName,
                                       String                   glossaryTermOneGUID,
                                       String                   glossaryTermTwoGUID,
                                       GlossaryTermRelationship relationshipsProperties) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException
    {
        final String methodName = "updateTermRelationship";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            glossaryManagerClient.updateTermRelationship(userId, assetManagerGUID, assetManagerName, relationshipTypeName, glossaryTermOneGUID, glossaryTermTwoGUID, relationshipsProperties);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    synchronizationDirection.getName(),
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

     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearTermRelationship(String relationshipTypeName,
                                      String glossaryTermOneGUID,
                                      String glossaryTermTwoGUID) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        final String methodName = "clearTermRelationship";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            glossaryManagerClient.clearTermRelationship(userId, assetManagerGUID, assetManagerName, relationshipTypeName, glossaryTermOneGUID, glossaryTermTwoGUID);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    synchronizationDirection.getName(),
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
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setTermAsAbstractConcept(String glossaryTermGUID,
                                         String glossaryTermExternalIdentifier) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        final String methodName = "setTermAsAbstractConcept";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            glossaryManagerClient.setTermAsAbstractConcept(userId, assetManagerGUID, assetManagerName, glossaryTermGUID, glossaryTermExternalIdentifier);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    synchronizationDirection.getName(),
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
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearTermAsAbstractConcept(String glossaryTermGUID,
                                           String glossaryTermExternalIdentifier) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        final String methodName = "clearTermAsAbstractConcept";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            glossaryManagerClient.clearTermAsAbstractConcept(userId, assetManagerGUID, assetManagerName, glossaryTermGUID, glossaryTermExternalIdentifier);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    synchronizationDirection.getName(),
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
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setTermAsDataValue(String glossaryTermGUID,
                                   String glossaryTermExternalIdentifier) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        final String methodName = "setTermAsDataValue";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            glossaryManagerClient.setTermAsDataValue(userId, assetManagerGUID, assetManagerName, glossaryTermGUID, glossaryTermExternalIdentifier);
        }
        else
        {
            throw new UserNotAuthorizedException(
                    CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(synchronizationDirection.getName(),
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
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearTermAsDataValue(String glossaryTermGUID,
                                     String glossaryTermExternalIdentifier) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        final String methodName = "clearTermAsDataValue";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            glossaryManagerClient.clearTermAsDataValue(userId, assetManagerGUID, assetManagerName, glossaryTermGUID, glossaryTermExternalIdentifier);
        }
        else
        {
            throw new UserNotAuthorizedException(
                    CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(synchronizationDirection.getName(),
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
     * @param activityType type of activity
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setTermAsActivity(String                   glossaryTermGUID,
                                  String                   glossaryTermExternalIdentifier,
                                  GlossaryTermActivityType activityType) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        final String methodName = "setTermAsActivity";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            glossaryManagerClient.setTermAsActivity(userId, assetManagerGUID, assetManagerName, glossaryTermGUID, glossaryTermExternalIdentifier, activityType);
        }
        else
        {
            throw new UserNotAuthorizedException(
                    CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(synchronizationDirection.getName(),
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
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearTermAsActivity(String glossaryTermGUID,
                                    String glossaryTermExternalIdentifier) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        final String methodName = "clearTermAsActivity";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            glossaryManagerClient.clearTermAsActivity(userId, assetManagerGUID, assetManagerName, glossaryTermGUID, glossaryTermExternalIdentifier);
        }
        else
        {
            throw new UserNotAuthorizedException(
                    CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(synchronizationDirection.getName(),
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
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setTermAsContext(String                        glossaryTermGUID,
                                 String                        glossaryTermExternalIdentifier,
                                 GlossaryTermContextDefinition contextDefinition) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        final String methodName = "setTermAsContext";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            glossaryManagerClient.setTermAsContext(userId, assetManagerGUID, assetManagerName, glossaryTermGUID, glossaryTermExternalIdentifier, contextDefinition);
        }
        else
        {
            throw new UserNotAuthorizedException(
                    CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(synchronizationDirection.getName(),
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
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearTermAsContext(String glossaryTermGUID,
                                   String glossaryTermExternalIdentifier) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        final String methodName = "clearTermAsContext";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            glossaryManagerClient.clearTermAsContext(userId, assetManagerGUID, assetManagerName, glossaryTermGUID, glossaryTermExternalIdentifier);
        }
        else
        {
            throw new UserNotAuthorizedException(
                    CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(synchronizationDirection.getName(),
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
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setTermAsSpineObject(String glossaryTermGUID,
                                     String glossaryTermExternalIdentifier) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        final String methodName = "setTermAsSpineObject";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            glossaryManagerClient.setTermAsSpineObject(userId, assetManagerGUID, assetManagerName, glossaryTermGUID, glossaryTermExternalIdentifier);
        }
        else
        {
            throw new UserNotAuthorizedException(
                    CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(synchronizationDirection.getName(),
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
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearTermAsSpineObject(String glossaryTermGUID,
                                       String glossaryTermExternalIdentifier) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        final String methodName = "clearTermAsSpineObject";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            glossaryManagerClient.clearTermAsSpineObject(userId, assetManagerGUID, assetManagerName, glossaryTermGUID, glossaryTermExternalIdentifier);
        }
        else
        {
            throw new UserNotAuthorizedException(
                    CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(synchronizationDirection.getName(),
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
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setTermAsSpineAttribute(String glossaryTermGUID,
                                        String glossaryTermExternalIdentifier) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        final String methodName = "setTermAsSpineAttribute";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            glossaryManagerClient.setTermAsSpineAttribute(userId, assetManagerGUID, assetManagerName, glossaryTermGUID, glossaryTermExternalIdentifier);
        }
        else
        {
            throw new UserNotAuthorizedException(
                    CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(synchronizationDirection.getName(),
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
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearTermAsSpineAttribute(String glossaryTermGUID,
                                          String glossaryTermExternalIdentifier) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        final String methodName = "clearTermAsSpineAttribute";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            glossaryManagerClient.clearTermAsSpineAttribute(userId, assetManagerGUID, assetManagerName, glossaryTermGUID, glossaryTermExternalIdentifier);
        }
        else
        {
            throw new UserNotAuthorizedException(
                    CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(synchronizationDirection.getName(),
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
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setTermAsObjectIdentifier(String glossaryTermGUID,
                                          String glossaryTermExternalIdentifier) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        final String methodName = "setTermAsObjectIdentifier";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            glossaryManagerClient.setTermAsObjectIdentifier(userId, assetManagerGUID, assetManagerName, glossaryTermGUID, glossaryTermExternalIdentifier);
        }
        else
        {
            throw new UserNotAuthorizedException(
                    CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(synchronizationDirection.getName(),
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
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearTermAsObjectIdentifier(String glossaryTermGUID,
                                            String glossaryTermExternalIdentifier) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        final String methodName = "clearTermAsObjectIdentifier";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            glossaryManagerClient.clearTermAsObjectIdentifier(userId, assetManagerGUID, assetManagerName, glossaryTermGUID, glossaryTermExternalIdentifier);
        }
        else
        {
            throw new UserNotAuthorizedException(
                    CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(synchronizationDirection.getName(),
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
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeGlossaryTerm(String glossaryTermGUID,
                                   String glossaryTermExternalIdentifier) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        final String methodName = "removeGlossaryTerm";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            glossaryManagerClient.removeGlossaryTerm(userId, assetManagerGUID, assetManagerName, glossaryTermGUID, glossaryTermExternalIdentifier);
        }
        else
        {
            throw new UserNotAuthorizedException(
                    CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(synchronizationDirection.getName(),
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
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<GlossaryTermElement>   findGlossaryTerms(String searchString,
                                                         int    startFrom,
                                                         int    pageSize) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        return glossaryManagerClient.findGlossaryTerms(userId, assetManagerGUID, assetManagerName, searchString, startFrom, pageSize);
    }


    /**
     * Retrieve the list of glossary terms associated with a glossary.
     *
     * @param glossaryGUID unique identifier of the glossary of interest
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of associated metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<GlossaryTermElement>    getTermsForGlossary(String glossaryGUID,
                                                            int    startFrom,
                                                            int    pageSize) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        return glossaryManagerClient.getTermsForGlossary(userId, assetManagerGUID, assetManagerName, glossaryGUID, startFrom, pageSize);
    }


    /**
     * Retrieve the list of glossary terms associated with a glossary category.
     *
     * @param glossaryCategoryGUID unique identifier of the glossary category of interest
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of associated metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<GlossaryTermElement>    getTermsForGlossaryCategory(String glossaryCategoryGUID,
                                                                    int    startFrom,
                                                                    int    pageSize) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        return glossaryManagerClient.getTermsForGlossaryCategory(userId, assetManagerGUID, assetManagerName, glossaryCategoryGUID, startFrom, pageSize);
    }


    /**
     * Retrieve the list of glossary term metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<GlossaryTermElement>   getGlossaryTermsByName(String name,
                                                              int    startFrom,
                                                              int    pageSize) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        return glossaryManagerClient.getGlossaryTermsByName(userId, assetManagerGUID, assetManagerName, name, startFrom, pageSize);
    }


    /**
     * Retrieve the glossary term metadata element with the supplied unique identifier.
     *
     * @param guid unique identifier of the requested metadata element
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GlossaryTermElement getGlossaryTermByGUID(String guid) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        return glossaryManagerClient.getGlossaryTermByGUID(userId, assetManagerGUID, assetManagerName, guid);
    }


    /* =========================================================================================
     * Support for linkage to external glossary resources.  These glossary resources are not
     * stored as metadata - they could be web pages, ontologies or some other format.
     * It is possible that the external glossary resource may have been generated by the metadata
     * representation or vice versa.
     */


    /**
     * Create a link to an external glossary resource.  This is associated with a glossary to show that they have equivalent content.
     * It is possible that this resource was generated from the glossary content or was the source for it.
     *
     * @param linkProperties properties of the link
     *
     * @return unique identifier of the external reference
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createExternalGlossaryLink(ExternalGlossaryLinkProperties linkProperties) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   PropertyServerException
    {
        final String methodName = "createExternalGlossaryLink";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            return glossaryManagerClient.createExternalGlossaryLink(userId, assetManagerGUID, assetManagerName, linkProperties);
        }
        else
        {
            throw new UserNotAuthorizedException(
                    CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(synchronizationDirection.getName(),
                                                                                                  connectorName,
                                                                                                  methodName),
                    this.getClass().getName(),
                    methodName,
                    userId);
        }
    }


    /**
     * Update the properties of a reference to an external glossary resource.
     *
     * @param externalLinkGUID unique identifier of the external reference
     * @param linkProperties properties of the link
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateExternalGlossaryLink(String                         externalLinkGUID,
                                           ExternalGlossaryLinkProperties linkProperties) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException
    {
        final String methodName = "updateExternalGlossaryLink";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            glossaryManagerClient.updateExternalGlossaryLink(userId, assetManagerGUID, assetManagerName, externalLinkGUID, linkProperties);
        }
        else
        {
            throw new UserNotAuthorizedException(
                    CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(synchronizationDirection.getName(),
                                                                                                  connectorName,
                                                                                                  methodName),
                    this.getClass().getName(),
                    methodName,
                    userId);
        }
    }


    /**
     * Remove information about a link to an external glossary resource (and the relationships that attached it to the glossaries).
     *
     * @param externalLinkGUID unique identifier of the external reference
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeExternalGlossaryLink(String externalLinkGUID) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        final String methodName = "removeExternalGlossaryLink";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            glossaryManagerClient.removeExternalGlossaryLink(userId, assetManagerGUID, assetManagerName, externalLinkGUID);
        }
        else
        {
            throw new UserNotAuthorizedException(
                    CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(synchronizationDirection.getName(),
                                                                                                  connectorName,
                                                                                                  methodName),
                    this.getClass().getName(),
                    methodName,
                    userId);
        }
    }


    /**
     * Connect a glossary to a reference to an external glossary resource.
     *
     * @param externalLinkGUID unique identifier of the external reference
     * @param glossaryGUID unique identifier of the metadata element to attach
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void attachExternalLinkToGlossary(String externalLinkGUID,
                                             String glossaryGUID) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        final String methodName = "attachExternalLinkToGlossary";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            glossaryManagerClient.attachExternalLinkToGlossary(userId, assetManagerGUID, assetManagerName, externalLinkGUID, glossaryGUID);
        }
        else
        {
            throw new UserNotAuthorizedException(
                    CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(synchronizationDirection.getName(),
                                                                                                  connectorName,
                                                                                                  methodName),
                    this.getClass().getName(),
                    methodName,
                    userId);
        }
    }


    /**
     * Disconnect a glossary from a reference to an external glossary resource.
     *
     * @param externalLinkGUID unique identifier of the external reference
     * @param glossaryGUID unique identifier of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void detachExternalLinkFromGlossary(String externalLinkGUID,
                                               String glossaryGUID) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        final String methodName = "detachExternalLinkFromGlossary";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            glossaryManagerClient.detachExternalLinkFromGlossary(userId, assetManagerGUID, assetManagerName, externalLinkGUID, glossaryGUID);
        }
        else
        {
            throw new UserNotAuthorizedException(
                    CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(synchronizationDirection.getName(),
                                                                                                  connectorName,
                                                                                                  methodName),
                    this.getClass().getName(),
                    methodName,
                    userId);
        }
    }


    /**
     * Retrieve the list of links to external glossary resources attached to a glossary.
     *
     * @param glossaryGUID unique identifier of the metadata element for the glossary of interest
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of attached links to external glossary resources
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<ExternalGlossaryLinkElement> getExternalLinksForGlossary(String glossaryGUID,
                                                                         int    startFrom,
                                                                         int    pageSize) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException
    {
        return glossaryManagerClient.getExternalLinksForGlossary(userId, glossaryGUID, startFrom, pageSize);
    }


    /**
     * Return the glossaries connected to an external glossary source.
     *
     * @param externalLinkGUID unique identifier of the metadata element for the external glossary link of interest
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of glossaries
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<GlossaryElement> getGlossariesForExternalLink(String externalLinkGUID,
                                                              int    startFrom,
                                                              int    pageSize) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        return glossaryManagerClient.getGlossariesForExternalLink(userId, assetManagerGUID, assetManagerName, externalLinkGUID, startFrom, pageSize);
    }


    /**
     * Create a link to an external glossary category resource.  This is associated with a category to show that they have equivalent content.
     * It is possible that this resource was generated from the glossary content or was the source for it.
     *
     * @param externalLinkGUID unique identifier of the external reference
     * @param glossaryCategoryGUID unique identifier for the the glossary category
     * @param linkProperties properties of the link
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void attachExternalCategoryLink(String                                externalLinkGUID,
                                           String                                glossaryCategoryGUID,
                                           ExternalGlossaryElementLinkProperties linkProperties) throws InvalidParameterException,
                                                                                                        UserNotAuthorizedException,
                                                                                                        PropertyServerException
    {
        final String methodName = "attachExternalCategoryLink";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            glossaryManagerClient.attachExternalCategoryLink(userId, assetManagerGUID, assetManagerName, externalLinkGUID, glossaryCategoryGUID, linkProperties);
        }
        else
        {
            throw new UserNotAuthorizedException(
                    CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(synchronizationDirection.getName(),
                                                                                                  connectorName,
                                                                                                  methodName),
                    this.getClass().getName(),
                    methodName,
                    userId);
        }
    }


    /**
     * Remove the link to an external glossary category resource.
     *
     * @param externalLinkGUID unique identifier of the external reference
     * @param glossaryCategoryGUID unique identifier for the the glossary category
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void detachExternalCategoryLink(String externalLinkGUID,
                                           String glossaryCategoryGUID) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        final String methodName = "detachExternalCategoryLink";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            glossaryManagerClient.detachExternalCategoryLink(userId, assetManagerGUID, assetManagerName, externalLinkGUID, glossaryCategoryGUID);
        }
        else
        {
            throw new UserNotAuthorizedException(
                    CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(synchronizationDirection.getName(),
                                                                                                  connectorName,
                                                                                                  methodName),
                    this.getClass().getName(),
                    methodName,
                    userId);
        }
    }


    /**
     * Create a link to an external glossary term resource.  This is associated with a term to show that they have equivalent content.
     * It is possible that this resource was generated from the glossary content or was the source for it.
     *
     * @param externalLinkGUID unique identifier of the external reference
     * @param glossaryTermGUID unique identifier for the the glossary category
     * @param linkProperties properties of the link
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void attachExternalTermLink(String                                externalLinkGUID,
                                       String                                glossaryTermGUID,
                                       ExternalGlossaryElementLinkProperties linkProperties) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException
    {
        final String methodName = "attachExternalTermLink";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            glossaryManagerClient.attachExternalTermLink(userId, assetManagerGUID, assetManagerName, externalLinkGUID, glossaryTermGUID, linkProperties);
        }
        else
        {
            throw new UserNotAuthorizedException(
                    CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(synchronizationDirection.getName(),
                                                                                                  connectorName,
                                                                                                  methodName),
                    this.getClass().getName(),
                    methodName,
                    userId);
        }
    }


    /**
     * Remove the link to an external glossary term resource.
     *
     * @param externalLinkGUID unique identifier of the external reference
     * @param glossaryTermGUID unique identifier for the the glossary category
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void detachExternalTermLink(String externalLinkGUID,
                                       String glossaryTermGUID) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        final String methodName = "detachExternalTermLink";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            glossaryManagerClient.detachExternalTermLink(userId, assetManagerGUID, assetManagerName, externalLinkGUID, glossaryTermGUID);
        }
        else
        {
            throw new UserNotAuthorizedException(
                    CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(synchronizationDirection.getName(),
                                                                                                  connectorName,
                                                                                                  methodName),
                    this.getClass().getName(),
                    methodName,
                    userId);
        }
    }
}
