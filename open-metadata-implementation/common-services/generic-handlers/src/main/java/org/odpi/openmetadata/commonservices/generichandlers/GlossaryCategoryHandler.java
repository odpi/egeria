/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;


import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.generichandlers.ffdc.GenericHandlersErrorCode;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
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
     * Construct the asset handler with information needed to work with B objects.
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
     * @param glossaryGUID unique identifier of the owning glossary
     * @param glossaryGUIDParameterName parameter supplying glossaryGUID
     * @param qualifiedName unique name for the category - used in other configuration
     * @param displayName short display name for the category
     * @param description description of the category
     * @param additionalProperties additional properties for a category
     * @param suppliedTypeName type name from the caller (enables creation of subtypes)
     * @param extendedProperties  properties for a category subtype
     * @param methodName calling method
     *
     * @return unique identifier of the new glossary object
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public String createGlossaryCategory(String              userId,
                                         String              glossaryGUID,
                                         String              glossaryGUIDParameterName,
                                         String              qualifiedName,
                                         String              displayName,
                                         String              description,
                                         Map<String, String> additionalProperties,
                                         String              suppliedTypeName,
                                         Map<String, Object> extendedProperties,
                                         String              methodName) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryGUID, glossaryGUIDParameterName, methodName);

        String typeName = OpenMetadataAPIMapper.GLOSSARY_CATEGORY_TYPE_NAME;

        if (suppliedTypeName != null)
        {
            typeName = suppliedTypeName;
        }

        String typeGUID = invalidParameterHandler.validateTypeName(typeName,
                                                                   OpenMetadataAPIMapper.GLOSSARY_CATEGORY_TYPE_NAME,
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
        
        builder.setAnchors(userId, glossaryGUID, methodName);

        String glossaryCategoryGUID = this.createBeanInRepository(userId,
                                                                  null,
                                                                  null,
                                                                  typeGUID,
                                                                  typeName,
                                                                  qualifiedName,
                                                                  OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                                                  builder,
                                                                  methodName);
        
        if (glossaryCategoryGUID != null)
        {
            /*
             * Link the category to its glossary.
             */
            final String glossaryCategoryGUIDParameterName = "glossaryCategoryGUID";

            this.linkElementToElement(userId,
                                      null,
                                      null,
                                      glossaryGUID,
                                      glossaryGUIDParameterName,
                                      OpenMetadataAPIMapper.GLOSSARY_TYPE_NAME,
                                      glossaryCategoryGUID,
                                      glossaryCategoryGUIDParameterName,
                                      OpenMetadataAPIMapper.GLOSSARY_CATEGORY_TYPE_NAME,
                                      OpenMetadataAPIMapper.CATEGORY_ANCHOR_TYPE_GUID,
                                      OpenMetadataAPIMapper.CATEGORY_ANCHOR_TYPE_NAME,
                                      null,
                                      methodName);
        }
        
        return glossaryCategoryGUID;
    }


    /**
     * Create a new metadata element to represent a glossary category using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new category.
     * This includes its link to the glossary.
     *
     * @param userId calling user
     * @param templateGUID unique identifier of the metadata element to copy
     * @param qualifiedName unique name for the category - used in other configuration
     * @param displayName short display name for the category
     * @param description description of the category
     * @param methodName calling method
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createGlossaryCategoryFromTemplate(String userId,
                                                     String templateGUID,
                                                     String qualifiedName,
                                                     String displayName,
                                                     String description,
                                                     String methodName) throws InvalidParameterException,
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

        return this.createBeanFromTemplate(userId,
                                           null,
                                           null,
                                           templateGUID,
                                           templateGUIDParameterName,
                                           OpenMetadataAPIMapper.GLOSSARY_CATEGORY_TYPE_GUID,
                                           OpenMetadataAPIMapper.GLOSSARY_CATEGORY_TYPE_NAME,
                                           qualifiedName,
                                           OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                           builder,
                                           methodName);
    }


    /**
     * Create the anchor object that all elements in a glossary (terms and categories) are linked to.
     *
     * @param userId calling user
     * @param glossaryCategoryGUID unique identifier for the category to update
     * @param glossaryCategoryGUIDParameterName parameter supplying the category
     * @param qualifiedName unique name for the category - used in other configuration
     * @param displayName short display name for the category
     * @param description description of the governance category
     * @param additionalProperties additional properties for a governance category
     * @param suppliedTypeName type of term
     * @param extendedProperties  properties for a governance category subtype
     * @param methodName calling method
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void   updateGlossaryCategory(String              userId,
                                         String              glossaryCategoryGUID,
                                         String              glossaryCategoryGUIDParameterName,
                                         String              qualifiedName,
                                         String              displayName,
                                         String              description,
                                         Map<String, String> additionalProperties,
                                         String              suppliedTypeName,
                                         Map<String, Object> extendedProperties,
                                         String              methodName) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        final String qualifiedNameParameterName = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(glossaryCategoryGUID, glossaryCategoryGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        String typeName = OpenMetadataAPIMapper.GLOSSARY_CATEGORY_TYPE_NAME;

        if (suppliedTypeName != null)
        {
            typeName = suppliedTypeName;
        }

        String typeGUID = invalidParameterHandler.validateTypeName(typeName,
                                                                   OpenMetadataAPIMapper.GLOSSARY_CATEGORY_TYPE_NAME,
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

        this.updateBeanInRepository(userId,
                                    null,
                                    null,
                                    glossaryCategoryGUID,
                                    glossaryCategoryGUIDParameterName,
                                    typeGUID,
                                    typeName,
                                    builder.getInstanceProperties(methodName),
                                    false,
                                    methodName);
    }


    /**
     * Create a parent-child relationship between two categories.
     *
     * @param userId calling user
     * @param glossaryParentCategoryGUID unique identifier of the glossary super-category
     * @param glossaryParentCategoryGUIDParameterName parameter supplying the super-category
     * @param glossaryChildCategoryGUID unique identifier of the glossary subcategory
     * @param glossaryChildCategoryGUIDParameterName parameter supplying the subcategory
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupCategoryParent(String userId,
                                    String glossaryParentCategoryGUID,
                                    String glossaryParentCategoryGUIDParameterName,
                                    String glossaryChildCategoryGUID,
                                    String glossaryChildCategoryGUIDParameterName,
                                    String methodName) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException
    {
        this.linkElementToElement(userId,
                                  null,
                                  null,
                                  glossaryParentCategoryGUID,
                                  glossaryParentCategoryGUIDParameterName,
                                  OpenMetadataAPIMapper.GLOSSARY_CATEGORY_TYPE_NAME,
                                  glossaryChildCategoryGUID,
                                  glossaryChildCategoryGUIDParameterName,
                                  OpenMetadataAPIMapper.GLOSSARY_CATEGORY_TYPE_NAME,
                                  OpenMetadataAPIMapper.CATEGORY_HIERARCHY_TYPE_GUID,
                                  OpenMetadataAPIMapper.CATEGORY_HIERARCHY_TYPE_NAME,
                                  null,
                                  methodName);
    }


    /**
     * Remove a parent-child relationship between two categories.
     *
     * @param userId calling user
     * @param glossaryParentCategoryGUID unique identifier of the glossary super-category
     * @param glossaryParentCategoryGUIDParameterName parameter supplying the super-category
     * @param glossaryChildCategoryGUID unique identifier of the glossary subcategory
     * @param glossaryChildCategoryGUIDParameterName parameter supplying the subcategory
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearCategoryParent(String userId,
                                    String glossaryParentCategoryGUID,
                                    String glossaryParentCategoryGUIDParameterName,
                                    String glossaryChildCategoryGUID,
                                    String glossaryChildCategoryGUIDParameterName,
                                    String methodName) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException
    {
        this.unlinkElementFromElement(userId,
                                      false,
                                      null,
                                      null,
                                      glossaryParentCategoryGUID,
                                      glossaryParentCategoryGUIDParameterName,
                                      OpenMetadataAPIMapper.GLOSSARY_CATEGORY_TYPE_NAME,
                                      glossaryChildCategoryGUID,
                                      glossaryChildCategoryGUIDParameterName,
                                      OpenMetadataAPIMapper.GLOSSARY_CATEGORY_TYPE_GUID,
                                      OpenMetadataAPIMapper.GLOSSARY_CATEGORY_TYPE_NAME,
                                      OpenMetadataAPIMapper.CATEGORY_HIERARCHY_TYPE_GUID,
                                      OpenMetadataAPIMapper.CATEGORY_HIERARCHY_TYPE_NAME,
                                      methodName);
    }


    /**
     * Remove the metadata element representing a glossary category.
     *
     * @param userId calling user
     * @param glossaryCategoryGUID unique identifier of the metadata element to remove
     * @param glossaryCategoryGUIDParameterName parameter for glossaryCategoryGUID
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeGlossaryCategory(String userId,
                                       String glossaryCategoryGUID,
                                       String glossaryCategoryGUIDParameterName,
                                       String methodName) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        this.deleteBeanInRepository(userId,
                                    null,
                                    null,
                                    glossaryCategoryGUID,
                                    glossaryCategoryGUIDParameterName,
                                    OpenMetadataAPIMapper.GLOSSARY_CATEGORY_TYPE_GUID,
                                    OpenMetadataAPIMapper.GLOSSARY_CATEGORY_TYPE_NAME,
                                    null,
                                    null,
                                    methodName);
    }


    /**
     * Retrieve the list of glossary category metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param searchString string to find in the properties
     * @param searchStringParameterName name of parameter supplying the search string
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param methodName calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<B> findGlossaryCategories(String userId,
                                          String searchString,
                                          String searchStringParameterName,
                                          int    startFrom,
                                          int    pageSize,
                                          String methodName) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        return this.findBeans(userId,
                              searchString,
                              searchStringParameterName,
                              OpenMetadataAPIMapper.GLOSSARY_CATEGORY_TYPE_GUID,
                              OpenMetadataAPIMapper.GLOSSARY_CATEGORY_TYPE_NAME,
                              startFrom,
                              pageSize,
                              methodName);
    }



    /**
     * Return the list of categories associated with a glossary.
     *
     * @param userId calling user
     * @param glossaryGUID unique identifier of the glossary to query
     * @param glossaryGUIDParameterName name of the parameter supplying glossaryGUID
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param methodName calling method
     *
     * @return list of metadata elements describing the categories associated with the requested glossary
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<B>   getCategoriesForGlossary(String userId,
                                              String glossaryGUID,
                                              String glossaryGUIDParameterName,
                                              int    startFrom,
                                              int    pageSize,
                                              String methodName) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        return this.getAttachedElements(userId,
                                        glossaryGUID,
                                        glossaryGUIDParameterName,
                                        OpenMetadataAPIMapper.GLOSSARY_TYPE_NAME,
                                        OpenMetadataAPIMapper.CATEGORY_ANCHOR_TYPE_GUID,
                                        OpenMetadataAPIMapper.CATEGORY_ANCHOR_TYPE_NAME,
                                        OpenMetadataAPIMapper.GLOSSARY_CATEGORY_TYPE_NAME,
                                        startFrom,
                                        pageSize,
                                        methodName);
    }


    /**
     * Retrieve the list of glossary metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param name name to search for
     * @param nameParameterName parameter supplying name
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param methodName calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<B>   getGlossaryCategoriesByName(String userId,
                                                 String name,
                                                 String nameParameterName,
                                                 int    startFrom,
                                                 int    pageSize,
                                                 String methodName) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameterName, methodName);

        List<String> specificMatchPropertyNames = new ArrayList<>();
        specificMatchPropertyNames.add(OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME);
        specificMatchPropertyNames.add(OpenMetadataAPIMapper.DISPLAY_NAME_PROPERTY_NAME);

        return this.getBeansByValue(userId,
                                    name,
                                    nameParameterName,
                                    OpenMetadataAPIMapper.GLOSSARY_CATEGORY_TYPE_GUID,
                                    OpenMetadataAPIMapper.GLOSSARY_CATEGORY_TYPE_NAME,
                                    specificMatchPropertyNames,
                                    true,
                                    null,
                                    null,
                                    startFrom,
                                    pageSize,
                                    methodName);
    }


    /**
     * Retrieve the glossary category metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param glossaryCategoryGUID unique identifier of the requested metadata element
     * @param glossaryCategoryGUIDParameterName parameter name of the glossaryCategoryGUID
     * @param methodName calling method
     *
     * @return parent glossary category element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public B getGlossaryCategoryParent(String userId,
                                       String glossaryCategoryGUID,
                                       String glossaryCategoryGUIDParameterName,
                                       String methodName) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        List<B> results = this.getAttachedElements(userId,
                                                   glossaryCategoryGUID,
                                                   glossaryCategoryGUIDParameterName,
                                                   OpenMetadataAPIMapper.GLOSSARY_CATEGORY_TYPE_NAME,
                                                   OpenMetadataAPIMapper.CATEGORY_HIERARCHY_TYPE_GUID,
                                                   OpenMetadataAPIMapper.CATEGORY_HIERARCHY_TYPE_NAME,
                                                   OpenMetadataAPIMapper.GLOSSARY_CATEGORY_TYPE_NAME,
                                                   null,
                                                   null,
                                                   1,
                                                   0,
                                                   invalidParameterHandler.getMaxPagingSize(),
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
            throw new PropertyServerException(GenericHandlersErrorCode.MULTIPLE_BEANS_FOUND.getMessageDefinition(OpenMetadataAPIMapper.GLOSSARY_CATEGORY_TYPE_NAME,
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
     * @param methodName calling method
     *
     * @return list of glossary category element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<B> getGlossarySubCategories(String userId,
                                            String glossaryCategoryGUID,
                                            String glossaryCategoryGUIDParameterName,
                                            int    startFrom,
                                            int    pageSize,
                                            String methodName) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        return this.getAttachedElements(userId,
                                        glossaryCategoryGUID,
                                        glossaryCategoryGUIDParameterName,
                                        OpenMetadataAPIMapper.GLOSSARY_CATEGORY_TYPE_NAME,
                                        OpenMetadataAPIMapper.CATEGORY_HIERARCHY_TYPE_GUID,
                                        OpenMetadataAPIMapper.CATEGORY_HIERARCHY_TYPE_NAME,
                                        OpenMetadataAPIMapper.GLOSSARY_CATEGORY_TYPE_NAME,
                                        null,
                                        null,
                                        2,
                                        startFrom,
                                        pageSize,
                                        methodName);
    }


    /**
     * Retrieve the glossary metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param guid unique identifier of the requested metadata element
     * @param guidParameterName parameter name of guid
     * @param methodName calling method
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public B getGlossaryCategoryByGUID(String userId,
                                       String guid,
                                       String guidParameterName,
                                       String methodName) throws InvalidParameterException,
                                                         UserNotAuthorizedException,
                                                         PropertyServerException
    {
        return this.getBeanFromRepository(userId,
                                          guid,
                                          guidParameterName,
                                          OpenMetadataAPIMapper.GLOSSARY_CATEGORY_TYPE_NAME,
                                          methodName);

    }
}
