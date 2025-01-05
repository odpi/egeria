/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.generichandlers.ffdc.GenericHandlersErrorCode;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * GlossaryCategoryHandler provides the exchange of metadata about glossary categories between the repository and the OMAS.
 * Note glossary categories are governance metadata and are always defined with LOCAL-COHORT provenance.
 *
 * @param <B> class that represents the glossary category
 */
public class GlossaryCategoryHandler<B> extends ReferenceableHandler<B>
{
    /**
     * Construct the handler with information needed to work with B objects.
     *
     * @param converter specific converter for this bean class
     * @param beanClass name of bean class that is represented by the generic class B
     * @param serviceName name of this service
     * @param serverName name of the local server
     * @param invalidParameterHandler handler for managing parameter errors
     * @param repositoryHandler manages calls to the repository services
     * @param repositoryHelper provides utilities for manipulating the repository services objects
     * @param localServerUserId userId for this server
     * @param securityVerifier open metadata security services verifier
     * @param supportedZones list of zones that the access service is allowed to serve B instances from
     * @param defaultZones list of zones that the access service should set in all new B instances
     * @param publishZones list of zones that the access service sets up in published B instances
     * @param auditLog destination for audit log events
     */
    public GlossaryCategoryHandler(OpenMetadataAPIGenericConverter<B> converter,
                                   Class<B>                           beanClass,
                                   String                             serviceName,
                                   String                             serverName,
                                   InvalidParameterHandler            invalidParameterHandler,
                                   RepositoryHandler                  repositoryHandler,
                                   OMRSRepositoryHelper               repositoryHelper,
                                   String                             localServerUserId,
                                   OpenMetadataServerSecurityVerifier securityVerifier,
                                   List<String>                       supportedZones,
                                   List<String>                       defaultZones,
                                   List<String>                       publishZones,
                                   AuditLog                           auditLog)
    {
        super(converter,
              beanClass,
              serviceName,
              serverName,
              invalidParameterHandler,
              repositoryHandler,
              repositoryHelper,
              localServerUserId,
              securityVerifier,
              supportedZones,
              defaultZones,
              publishZones,
              auditLog);
    }


    /**
     * Create the glossary category object.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param glossaryGUID unique identifier of the owning glossary
     * @param glossaryGUIDParameterName parameter supplying glossaryGUID
     * @param qualifiedName unique name for the category - used in other configuration
     * @param displayName short display name for the category
     * @param description description of the category
     * @param additionalProperties additional properties for a category
     * @param rootCategory is this category a top-level (root) category
     * @param suppliedTypeName type name from the caller (enables creation of subtypes)
     * @param extendedProperties  properties for a category subtype
     * @param effectiveFrom  the time that the element must be effective from (null for any time, new Date() for now)
     * @param effectiveTo  the time that the must be effective to (null for any time, new Date() for now)
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return unique identifier of the new glossary object
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public String createGlossaryCategory(String              userId,
                                         String              externalSourceGUID,
                                         String              externalSourceName,
                                         String              glossaryGUID,
                                         String              glossaryGUIDParameterName,
                                         String              qualifiedName,
                                         String              displayName,
                                         String              description,
                                         Map<String, String> additionalProperties,
                                         boolean             rootCategory,
                                         String              suppliedTypeName,
                                         Map<String, Object> extendedProperties,
                                         Date                effectiveFrom,
                                         Date                effectiveTo,
                                         Date                effectiveTime,
                                         String              methodName) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryGUID, glossaryGUIDParameterName, methodName);

        String typeName = OpenMetadataType.GLOSSARY_CATEGORY.typeName;

        if (suppliedTypeName != null)
        {
            typeName = suppliedTypeName;
        }

        String typeGUID = invalidParameterHandler.validateTypeName(typeName,
                                                                   OpenMetadataType.GLOSSARY_CATEGORY.typeName,
                                                                   serviceName,
                                                                   methodName,
                                                                   repositoryHelper);

        GlossaryCategoryBuilder builder = new GlossaryCategoryBuilder(qualifiedName,
                                                                      displayName,
                                                                      description,
                                                                      additionalProperties,
                                                                      extendedProperties,
                                                                      repositoryHelper,
                                                                      serviceName,
                                                                      serverName);

        this.addAnchorGUIDToBuilder(userId,
                                    glossaryGUID,
                                    glossaryGUIDParameterName,
                                    false,
                                    false,
                                    effectiveTime,
                                    supportedZones,
                                    builder,
                                    methodName);

        builder.setEffectivityDates(effectiveFrom, effectiveTo);

        if (rootCategory)
        {
            builder.setRootCategory(userId, methodName);
        }

        String glossaryCategoryGUID = this.createBeanInRepository(userId,
                                                                  externalSourceGUID,
                                                                  externalSourceName,
                                                                  typeGUID,
                                                                  typeName,
                                                                  builder,
                                                                  effectiveTime,
                                                                  methodName);
        
        if (glossaryCategoryGUID != null)
        {
            /*
             * Link the category to its glossary.  This relationship is always effective.
             */
            final String glossaryCategoryGUIDParameterName = "glossaryCategoryGUID";

            this.uncheckedLinkElementToElement(userId,
                                               externalSourceGUID,
                                               externalSourceName,
                                               glossaryGUID,
                                               glossaryGUIDParameterName,
                                               glossaryCategoryGUID,
                                               glossaryCategoryGUIDParameterName,
                                               OpenMetadataType.CATEGORY_ANCHOR_RELATIONSHIP.typeGUID,
                                               null,
                                               methodName);
        }
        
        return glossaryCategoryGUID;
    }


    /**
     * Create a category from a template.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param glossaryGUID unique identifier of the glossary where the category is located
     * @param glossaryGUIDParameterName parameter supplying glossaryGUID
     * @param templateGUID unique identifier of the metadata element to copy
     * @param qualifiedName unique name for the category - used in other configuration
     * @param displayName short display name for the category
     * @param description description of the category
     * @param deepCopy should the template creation extend to the anchored elements or just the direct entity?
     * @param methodName calling method
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createGlossaryCategoryFromTemplate(String  userId,
                                                     String  externalSourceGUID,
                                                     String  externalSourceName,
                                                     String  glossaryGUID,
                                                     String  glossaryGUIDParameterName,
                                                     String  templateGUID,
                                                     String  qualifiedName,
                                                     String  displayName,
                                                     String  description,
                                                     boolean deepCopy,
                                                     String  methodName) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        final String templateGUIDParameterName   = "templateGUID";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(templateGUID, templateGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        GlossaryCategoryBuilder builder = new GlossaryCategoryBuilder(qualifiedName,
                                                                      displayName,
                                                                      description,
                                                                      repositoryHelper,
                                                                      serviceName,
                                                                      serverName);

        this.addAnchorGUIDToBuilder(userId,
                                    glossaryGUID,
                                    glossaryGUIDParameterName,
                                    false,
                                    false,
                                    null,
                                    supportedZones,
                                    builder,
                                    methodName);

        String glossaryCategoryGUID = this.createBeanFromTemplate(userId,
                                                                  externalSourceGUID,
                                                                  externalSourceName,
                                                                  templateGUID,
                                                                  templateGUIDParameterName,
                                                                  OpenMetadataType.GLOSSARY_CATEGORY.typeGUID,
                                                                  OpenMetadataType.GLOSSARY_CATEGORY.typeName,
                                                                  qualifiedName,
                                                                  OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                  builder,
                                                                  supportedZones,
                                                                  deepCopy,
                                                                  false,
                                                                  null,
                                                                  methodName);

        if (glossaryCategoryGUID != null)
        {
            /*
             * Link the category to its glossary.  This relationship is always effective.
             */
            final String glossaryCategoryGUIDParameterName = "glossaryCategoryGUID";

            this.uncheckedLinkElementToElement(userId,
                                               externalSourceGUID,
                                               externalSourceName,
                                               glossaryGUID,
                                               glossaryGUIDParameterName,
                                               glossaryCategoryGUID,
                                               glossaryCategoryGUIDParameterName,
                                               OpenMetadataType.CATEGORY_ANCHOR_RELATIONSHIP.typeGUID,
                                               null,
                                               methodName);
        }

        return glossaryCategoryGUID;
    }


    /**
     * Update the category.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param glossaryCategoryGUID unique identifier for the category to update
     * @param glossaryCategoryGUIDParameterName parameter supplying the category
     * @param qualifiedName unique name for the category - used in other configuration
     * @param displayName short display name for the category
     * @param description description of the governance category
     * @param additionalProperties additional properties for a governance category
     * @param suppliedTypeName type of term
     * @param extendedProperties  properties for a governance category subtype
     * @param effectiveFrom  the time that the element must be effective from (null for any time, new Date() for now)
     * @param effectiveTo  the time that the must be effective to (null for any time, new Date() for now)
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param methodName calling method
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void   updateGlossaryCategory(String              userId,
                                         String              externalSourceGUID,
                                         String              externalSourceName,
                                         String              glossaryCategoryGUID,
                                         String              glossaryCategoryGUIDParameterName,
                                         String              qualifiedName,
                                         String              displayName,
                                         String              description,
                                         Map<String, String> additionalProperties,
                                         String              suppliedTypeName,
                                         Map<String, Object> extendedProperties,
                                         Date                effectiveFrom,
                                         Date                effectiveTo,
                                         Date                effectiveTime,
                                         boolean             isMergeUpdate,
                                         boolean             forLineage,
                                         boolean             forDuplicateProcessing,
                                         String              methodName) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        final String qualifiedNameParameterName = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryCategoryGUID, glossaryCategoryGUIDParameterName, methodName);
        if (! isMergeUpdate)
        {
            invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);
        }

        String typeName = OpenMetadataType.GLOSSARY_CATEGORY.typeName;

        if (suppliedTypeName != null)
        {
            typeName = suppliedTypeName;
        }

        String typeGUID = invalidParameterHandler.validateTypeName(typeName,
                                                                   OpenMetadataType.GLOSSARY_CATEGORY.typeName,
                                                                   serviceName,
                                                                   methodName,
                                                                   repositoryHelper);

        GlossaryCategoryBuilder builder = new GlossaryCategoryBuilder(qualifiedName,
                                                                      displayName,
                                                                      description,
                                                                      additionalProperties,
                                                                      extendedProperties,
                                                                      repositoryHelper,
                                                                      serviceName,
                                                                      serverName);

        builder.setEffectivityDates(effectiveFrom, effectiveTo);

        this.updateBeanInRepository(userId,
                                    externalSourceGUID,
                                    externalSourceName,
                                    glossaryCategoryGUID,
                                    glossaryCategoryGUIDParameterName,
                                    typeGUID,
                                    typeName,
                                    forLineage,
                                    forDuplicateProcessing,
                                    supportedZones,
                                    builder.getInstanceProperties(methodName),
                                    isMergeUpdate,
                                    effectiveTime,
                                    methodName);
    }


    /**
     * Create a parent-child relationship between two categories.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param glossaryParentCategoryGUID unique identifier of the glossary super-category
     * @param glossaryParentCategoryGUIDParameterName parameter supplying the super-category
     * @param glossaryChildCategoryGUID unique identifier of the glossary subcategory
     * @param glossaryChildCategoryGUIDParameterName parameter supplying the subcategory
     * @param effectiveFrom  the time that the relationship element must be effective from (null for any time, new Date() for now)
     * @param effectiveTo  the time that the relationship must be effective to (null for any time, new Date() for now)
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupCategoryParent(String  userId,
                                    String  externalSourceGUID,
                                    String  externalSourceName,
                                    String  glossaryParentCategoryGUID,
                                    String  glossaryParentCategoryGUIDParameterName,
                                    String  glossaryChildCategoryGUID,
                                    String  glossaryChildCategoryGUIDParameterName,
                                    Date    effectiveFrom,
                                    Date    effectiveTo,
                                    Date    effectiveTime,
                                    boolean forLineage,
                                    boolean forDuplicateProcessing,
                                    String  methodName) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        this.linkElementToElement(userId,
                                  externalSourceGUID,
                                  externalSourceName,
                                  glossaryParentCategoryGUID,
                                  glossaryParentCategoryGUIDParameterName,
                                  OpenMetadataType.GLOSSARY_CATEGORY.typeName,
                                  glossaryChildCategoryGUID,
                                  glossaryChildCategoryGUIDParameterName,
                                  OpenMetadataType.GLOSSARY_CATEGORY.typeName,
                                  forLineage,
                                  forDuplicateProcessing,
                                  supportedZones,
                                  OpenMetadataType.CATEGORY_HIERARCHY_LINK_RELATIONSHIP.typeGUID,
                                  OpenMetadataType.CATEGORY_HIERARCHY_LINK_RELATIONSHIP.typeName,
                                  null,
                                  effectiveFrom,
                                  effectiveTo,
                                  effectiveTime,
                                  methodName);
    }


    /**
     * Remove a parent-child relationship between two categories.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param glossaryParentCategoryGUID unique identifier of the glossary super-category
     * @param glossaryParentCategoryGUIDParameterName parameter supplying the super-category
     * @param glossaryChildCategoryGUID unique identifier of the glossary subcategory
     * @param glossaryChildCategoryGUIDParameterName parameter supplying the subcategory
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearCategoryParent(String  userId,
                                    String  externalSourceGUID,
                                    String  externalSourceName,
                                    String  glossaryParentCategoryGUID,
                                    String  glossaryParentCategoryGUIDParameterName,
                                    String  glossaryChildCategoryGUID,
                                    String  glossaryChildCategoryGUIDParameterName,
                                    Date    effectiveTime,
                                    boolean forLineage,
                                    boolean forDuplicateProcessing,
                                    String  methodName) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        this.unlinkElementFromElement(userId,
                                      false,
                                      externalSourceGUID,
                                      externalSourceName,
                                      glossaryParentCategoryGUID,
                                      glossaryParentCategoryGUIDParameterName,
                                      OpenMetadataType.GLOSSARY_CATEGORY.typeName,
                                      glossaryChildCategoryGUID,
                                      glossaryChildCategoryGUIDParameterName,
                                      OpenMetadataType.GLOSSARY_CATEGORY.typeGUID,
                                      OpenMetadataType.GLOSSARY_CATEGORY.typeName,
                                      forLineage,
                                      forDuplicateProcessing,
                                      OpenMetadataType.CATEGORY_HIERARCHY_LINK_RELATIONSHIP.typeGUID,
                                      OpenMetadataType.CATEGORY_HIERARCHY_LINK_RELATIONSHIP.typeName,
                                      effectiveTime,
                                      methodName);
    }


    /**
     * Remove the metadata element representing a glossary category.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param glossaryCategoryGUID unique identifier of the metadata element to remove
     * @param glossaryCategoryGUIDParameterName parameter for glossaryCategoryGUID
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeGlossaryCategory(String  userId,
                                       String  externalSourceGUID,
                                       String  externalSourceName,
                                       String  glossaryCategoryGUID,
                                       String  glossaryCategoryGUIDParameterName,
                                       Date    effectiveTime,
                                       boolean forLineage,
                                       boolean forDuplicateProcessing,
                                       String  methodName) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        this.deleteBeanInRepository(userId,
                                    externalSourceGUID,
                                    externalSourceName,
                                    glossaryCategoryGUID,
                                    glossaryCategoryGUIDParameterName,
                                    OpenMetadataType.GLOSSARY_CATEGORY.typeGUID,
                                    OpenMetadataType.GLOSSARY_CATEGORY.typeName,
                                    null,
                                    null,
                                    forLineage,
                                    forDuplicateProcessing,
                                    effectiveTime,
                                    methodName);
    }


    /**
     * Retrieve the list of glossary category metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param glossaryGUID unique identifier of the glossary to query
     * @param searchString string to find in the properties
     * @param searchStringParameterName name of parameter supplying the search string
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param methodName calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<B> findGlossaryCategories(String  userId,
                                          String  glossaryGUID,
                                          String  searchString,
                                          String  searchStringParameterName,
                                          int     startFrom,
                                          int     pageSize,
                                          Date    effectiveTime,
                                          boolean forLineage,
                                          boolean forDuplicateProcessing,
                                          String  methodName) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {
        int queryPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        List<EntityDetail> categoryEntities = repositoryHandler.getEntitiesByValue(userId,
                                                                                   searchString,
                                                                                   OpenMetadataType.GLOSSARY_CATEGORY.typeGUID,
                                                                                   null,
                                                                                   null,
                                                                                   null,
                                                                                   SequencingOrder.CREATION_DATE_RECENT,
                                                                                   null,
                                                                                   forLineage,
                                                                                   forDuplicateProcessing,
                                                                                   startFrom,
                                                                                   queryPageSize,
                                                                                   effectiveTime,
                                                                                   methodName);

        return getValidCategories(userId,
                                  glossaryGUID,
                                  effectiveTime,
                                  forLineage,
                                  forDuplicateProcessing,
                                  methodName,
                                  categoryEntities);
    }


    /**
     * Return the valid category beans retrieved from the repository.
     *
     * @param userId calling user
     * @param glossaryGUID unique identifier of the glossary to query
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param methodName calling method
     * @param categoryEntities entities retrieved from the repository
     * @return list of beans
     */
    private List<B> getValidCategories(String             userId,
                                       String             glossaryGUID,
                                       Date               effectiveTime,
                                       boolean            forLineage,
                                       boolean            forDuplicateProcessing,
                                       String             methodName,
                                       List<EntityDetail> categoryEntities)
    {
        List<EntityDetail> validatedCategories = super.validateEntitiesAndAnchorsForRead(userId,
                                                                                         categoryEntities,
                                                                                         forLineage,
                                                                                         forDuplicateProcessing,
                                                                                         supportedZones,
                                                                                         effectiveTime,
                                                                                         methodName);
        if (validatedCategories != null)
        {
            List<B> results = new ArrayList<>();

            for (EntityDetail entity : validatedCategories)
            {
                if (entity != null)
                {
                    try
                    {
                        if (glossaryGUID == null)
                        {
                            results.add(converter.getNewBean(beanClass, entity, methodName));
                        }
                        else
                        {
                            AnchorIdentifiers anchorIdentifiers = this.getAnchorGUIDFromAnchorsClassification(entity, methodName);

                            if (glossaryGUID.equals(anchorIdentifiers.anchorGUID))
                            {
                                results.add(converter.getNewBean(beanClass, entity, methodName));
                            }
                        }
                    }
                    catch (Exception notVisible)
                    {
                        // ignore entity
                    }
                }
            }

            if (!results.isEmpty())
            {
                return results;
            }
        }

        return null;
    }


    /**
     * Return the list of categories associated with a glossary.
     *
     * @param userId calling user
     * @param glossaryGUID unique identifier of the glossary to query
     * @param glossaryGUIDParameterName name of the parameter supplying glossaryGUID
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param methodName calling method
     *
     * @return list of metadata elements describing the categories associated with the requested glossary
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<B>   getCategoriesForGlossary(String  userId,
                                              String  glossaryGUID,
                                              String  glossaryGUIDParameterName,
                                              int     startFrom,
                                              int     pageSize,
                                              Date    effectiveTime,
                                              boolean forLineage,
                                              boolean forDuplicateProcessing,
                                              String  methodName) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        invalidParameterHandler.validateGUID(glossaryGUID, glossaryGUIDParameterName, methodName);
        int queryPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        EntityDetail glossaryEntity = repositoryHandler.getEntityByGUID(userId,
                                                                        glossaryGUID,
                                                                        glossaryGUIDParameterName,
                                                                        OpenMetadataType.GLOSSARY.typeName,
                                                                        forLineage,
                                                                        forDuplicateProcessing,
                                                                        effectiveTime,
                                                                        methodName);

        securityVerifier.validateUserForElementRead(userId,
                                                    glossaryEntity,
                                                    repositoryHelper,
                                                    serviceName,
                                                    methodName);

        InstanceProperties matchProperties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                          null,
                                                                                          OpenMetadataProperty.ANCHOR_GUID.name,
                                                                                          glossaryGUID,
                                                                                          methodName);
        List<EntityDetail> retrievedEntities = repositoryHandler.getEntitiesForClassificationType(userId,
                                                                                                  OpenMetadataType.GLOSSARY_CATEGORY.typeGUID,
                                                                                                  OpenMetadataType.ANCHORS_CLASSIFICATION.typeName,
                                                                                                  matchProperties,
                                                                                                  MatchCriteria.ALL,
                                                                                                  null,
                                                                                                  null,
                                                                                                  SequencingOrder.CREATION_DATE_RECENT,
                                                                                                  null,
                                                                                                  forLineage,
                                                                                                  forDuplicateProcessing,
                                                                                                  startFrom,
                                                                                                  queryPageSize,
                                                                                                  effectiveTime,
                                                                                                  methodName);

        if (retrievedEntities != null)
        {
            List<B> results = new ArrayList<>();

            for (EntityDetail entityDetail : retrievedEntities)
            {
                if (entityDetail != null)
                {
                    results.add(converter.getNewBean(beanClass, entityDetail, methodName));
                }
            }

            return results;
        }

        return null;
    }


    /**
     * Return the list of categories associated with a glossary term.
     *
     * @param userId calling user
     * @param glossaryTermGUID unique identifier of the glossary term to query
     * @param glossaryTermGUIDParameterName name of the parameter supplying glossaryTermGUID
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param methodName calling method
     *
     * @return list of metadata elements describing the categories associated with the requested term
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<B>   getCategoriesForTerm(String  userId,
                                          String  glossaryTermGUID,
                                          String  glossaryTermGUIDParameterName,
                                          int     startFrom,
                                          int     pageSize,
                                          Date    effectiveTime,
                                          boolean forLineage,
                                          boolean forDuplicateProcessing,
                                          String  methodName) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {
        return this.getAttachedElements(userId,
                                        glossaryTermGUID,
                                        glossaryTermGUIDParameterName,
                                        OpenMetadataType.GLOSSARY_TERM.typeName,
                                        OpenMetadataType.TERM_CATEGORIZATION.typeGUID,
                                        OpenMetadataType.TERM_CATEGORIZATION.typeName,
                                        OpenMetadataType.GLOSSARY_CATEGORY.typeName,
                                        null,
                                        null,
                                        1,
                                        null,
                                        null,
                                        SequencingOrder.CREATION_DATE_RECENT,
                                        null,
                                        forLineage,
                                        forDuplicateProcessing,
                                        startFrom,
                                        pageSize,
                                        effectiveTime,
                                        methodName);
    }


    /**
     * Retrieve the list of glossary metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param glossaryGUID unique identifier of the glossary to query
     * @param name name to search for
     * @param nameParameterName parameter supplying name
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param methodName calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<B>   getGlossaryCategoriesByName(String  userId,
                                                 String  glossaryGUID,
                                                 String  name,
                                                 String  nameParameterName,
                                                 int     startFrom,
                                                 int     pageSize,
                                                 Date    effectiveTime,
                                                 boolean forLineage,
                                                 boolean forDuplicateProcessing,
                                                 String  methodName) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameterName, methodName);

        int queryPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        InstanceProperties matchProperties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                          null,
                                                                                          OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                          name,
                                                                                          methodName);
        matchProperties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                       matchProperties,
                                                                       OpenMetadataProperty.DISPLAY_NAME.name,
                                                                       name,
                                                                       methodName);

        List<EntityDetail> categoryEntities = repositoryHandler.getEntitiesByName(userId,
                                                                                  matchProperties,
                                                                                  OpenMetadataType.GLOSSARY_CATEGORY.typeGUID,
                                                                                  null,
                                                                                  null,
                                                                                  null,
                                                                                  SequencingOrder.CREATION_DATE_RECENT,
                                                                                  null,
                                                                                  forLineage,
                                                                                  forDuplicateProcessing,
                                                                                  startFrom,
                                                                                  queryPageSize,
                                                                                  effectiveTime,
                                                                                  methodName);

        return getValidCategories(userId,
                                  glossaryGUID,
                                  effectiveTime,
                                  forLineage,
                                  forDuplicateProcessing,
                                  methodName,
                                  categoryEntities);
    }


    /**
     * Retrieve the glossary category metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param glossaryCategoryGUID unique identifier of the requested metadata element
     * @param glossaryCategoryGUIDParameterName parameter name of the glossaryCategoryGUID
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param methodName calling method
     *
     * @return parent glossary category element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public B getGlossaryCategoryParent(String  userId,
                                       String  glossaryCategoryGUID,
                                       String  glossaryCategoryGUIDParameterName,
                                       Date    effectiveTime,
                                       boolean forLineage,
                                       boolean forDuplicateProcessing,
                                       String  methodName) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        List<B> results = this.getAttachedElements(userId,
                                                   glossaryCategoryGUID,
                                                   glossaryCategoryGUIDParameterName,
                                                   OpenMetadataType.GLOSSARY_CATEGORY.typeName,
                                                   OpenMetadataType.CATEGORY_HIERARCHY_LINK_RELATIONSHIP.typeGUID,
                                                   OpenMetadataType.CATEGORY_HIERARCHY_LINK_RELATIONSHIP.typeName,
                                                   OpenMetadataType.GLOSSARY_CATEGORY.typeName,
                                                   null,
                                                   null,
                                                   1,
                                                   null,
                                                   null,
                                                   SequencingOrder.CREATION_DATE_RECENT,
                                                   null,
                                                   forLineage,
                                                   forDuplicateProcessing,
                                                   0,
                                                   invalidParameterHandler.getMaxPagingSize(),
                                                   effectiveTime,
                                                   methodName);

        if ((results == null) || (results.isEmpty()))
        {
            return null;
        }
        else if (results.size() == 1)
        {
            return results.get(0);
        }
        else
        {
            throw new PropertyServerException(GenericHandlersErrorCode.MULTIPLE_BEANS_FOUND.getMessageDefinition(OpenMetadataType.GLOSSARY_CATEGORY.typeName,
                                                                                                                 methodName,
                                                                                                                 results.toString(),
                                                                                                                 glossaryCategoryGUID,
                                                                                                                 serviceName,
                                                                                                                 serverName),
                                              this.getClass().getName(),
                                              methodName);
        }
    }


    /**
     * Retrieve the glossary category metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param glossaryCategoryGUID unique identifier of the requested metadata element
     * @param glossaryCategoryGUIDParameterName parameter name of the glossaryCategoryGUID
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param methodName calling method
     *
     * @return list of glossary category element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<B> getGlossarySubCategories(String  userId,
                                            String  glossaryCategoryGUID,
                                            String  glossaryCategoryGUIDParameterName,
                                            int     startFrom,
                                            int     pageSize,
                                            Date    effectiveTime,
                                            boolean forLineage,
                                            boolean forDuplicateProcessing,
                                            String  methodName) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        return this.getAttachedElements(userId,
                                        glossaryCategoryGUID,
                                        glossaryCategoryGUIDParameterName,
                                        OpenMetadataType.GLOSSARY_CATEGORY.typeName,
                                        OpenMetadataType.CATEGORY_HIERARCHY_LINK_RELATIONSHIP.typeGUID,
                                        OpenMetadataType.CATEGORY_HIERARCHY_LINK_RELATIONSHIP.typeName,
                                        OpenMetadataType.GLOSSARY_CATEGORY.typeName,
                                        null,
                                        null,
                                        2,
                                        null,
                                        null,
                                        SequencingOrder.CREATION_DATE_RECENT,
                                        null,
                                        forLineage,
                                        forDuplicateProcessing,
                                        startFrom,
                                        pageSize,
                                        effectiveTime,
                                        methodName);
    }


    /**
     * Retrieve the glossary metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param guid unique identifier of the requested metadata element
     * @param guidParameterName parameter name of guid
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param methodName calling method
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public B getGlossaryCategoryByGUID(String  userId,
                                       String  guid,
                                       String  guidParameterName,
                                       Date    effectiveTime,
                                       boolean forLineage,
                                       boolean forDuplicateProcessing,
                                       String  methodName) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        return this.getBeanFromRepository(userId,
                                          guid,
                                          guidParameterName,
                                          OpenMetadataType.GLOSSARY_CATEGORY.typeName,
                                          forLineage,
                                          forDuplicateProcessing,
                                          supportedZones,
                                          effectiveTime,
                                          methodName);

    }
}
