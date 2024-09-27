/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * GovernanceDomainHandler provides the exchange of metadata about governance domains between the repository and the OMAS.
 * Note governanceDomains are governance metadata and are always defined with LOCAL-COHORT provenance.
 * There is no support for effectivity dating.
 *
 * @param <B> class that represents the governance domain
 */
public class GovernanceDomainHandler<B> extends ReferenceableHandler<B>
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
    public GovernanceDomainHandler(OpenMetadataAPIGenericConverter<B> converter,
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
     * Create the anchor object that all elements in a governance domain (terms and categories) are linked to.
     *
     * @param userId calling user
     * @param qualifiedName unique name for the governance domain - used in other configuration
     * @param displayName short display name for the governance domain
     * @param description description of the  governance domain
     * @param domainIdentifier the domain identifier used in the governance domain definitions
     * @param additionalProperties additional properties for a governance domain
     * @param suppliedTypeName type name from the caller (enables creation of subtypes)
     * @param extendedProperties  properties for a  governance domain subtype
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return unique identifier of the new governance domain object
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public String createGovernanceDomain(String              userId,
                                         String              qualifiedName,
                                         String              displayName,
                                         String              description,
                                         int                 domainIdentifier,
                                         Map<String, String> additionalProperties,
                                         String              suppliedTypeName,
                                         Map<String, Object> extendedProperties,
                                         Date                effectiveTime,
                                         String              methodName) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        final String qualifiedNameParameterName = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        String typeName = OpenMetadataType.GOVERNANCE_DOMAIN_DESCRIPTION_TYPE_NAME;

        if (suppliedTypeName != null)
        {
            typeName = suppliedTypeName;
        }

        String typeGUID = invalidParameterHandler.validateTypeName(typeName,
                                                                   OpenMetadataType.GOVERNANCE_DOMAIN_DESCRIPTION_TYPE_NAME,
                                                                   serviceName,
                                                                   methodName,
                                                                   repositoryHelper);

        GovernanceDomainBuilder builder = new GovernanceDomainBuilder(qualifiedName,
                                                                      displayName,
                                                                      description,
                                                                      domainIdentifier,
                                                                      additionalProperties,
                                                                      typeGUID,
                                                                      typeName,
                                                                      extendedProperties,
                                                                      repositoryHelper,
                                                                      serviceName,
                                                                      serverName);

        return this.createBeanInRepository(userId,
                                           null,
                                           null,
                                           typeGUID,
                                           typeName,
                                           builder,
                                           effectiveTime,
                                           methodName);
    }


    /**
     * Update the anchor object that all elements in a governance domain (terms and categories) are linked to.
     *
     * @param userId calling user
     * @param governanceDomainGUID unique identifier of the governance domain to update
     * @param governanceDomainGUIDParameterName parameter passing the governance domainGUID
     * @param qualifiedName unique name for the governance domain - used in other configuration
     * @param displayName short display name for the governance domain
     * @param description description of the  governance domain
     * @param domainIdentifier the domain identifier used in the governance domain definitions
     * @param additionalProperties additional properties for a  governance domain
     * @param suppliedTypeName type of governance domain
     * @param extendedProperties  properties for a  governance domain subtype
     * @param methodName calling method
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void   updateGovernanceDomain(String              userId,
                                         String              governanceDomainGUID,
                                         String              governanceDomainGUIDParameterName,
                                         String              qualifiedName,
                                         String              displayName,
                                         String              description,
                                         int                 domainIdentifier,
                                         Map<String, String> additionalProperties,
                                         String              suppliedTypeName,
                                         Map<String, Object> extendedProperties,
                                         String              methodName) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        final String qualifiedNameParameterName = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(governanceDomainGUID, governanceDomainGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        String typeName = OpenMetadataType.GOVERNANCE_DOMAIN_DESCRIPTION_TYPE_NAME;

        if (suppliedTypeName != null)
        {
            typeName = suppliedTypeName;
        }

        String typeGUID = invalidParameterHandler.validateTypeName(typeName,
                                                                   OpenMetadataType.GOVERNANCE_DOMAIN_DESCRIPTION_TYPE_NAME,
                                                                   serviceName,
                                                                   methodName,
                                                                   repositoryHelper);

        GovernanceDomainBuilder builder = new GovernanceDomainBuilder(qualifiedName,
                                                                      displayName,
                                                                      description,
                                                                      domainIdentifier,
                                                                      additionalProperties,
                                                                      typeGUID,
                                                                      typeName,
                                                                      extendedProperties,
                                                                      repositoryHelper,
                                                                      serviceName,
                                                                      serverName);

        this.updateBeanInRepository(userId,
                                    null,
                                    null,
                                    governanceDomainGUID,
                                    governanceDomainGUIDParameterName,
                                    typeGUID,
                                    typeName,
                                    false,
                                    false,
                                    supportedZones,
                                    builder.getInstanceProperties(methodName),
                                    false,
                                    new Date(),
                                    methodName);
    }


    /**
     * Remove the metadata element representing a governance domain.  This will delete the governance domain and all categories and terms because
     * the Anchors classifications are set up in these elements.
     *
     * @param userId calling user
     * @param governanceDomainGUID unique identifier of the metadata element to remove
     * @param governanceDomainGUIDParameterName parameter supplying the governance domainGUID
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeGovernanceDomain(String userId,
                                       String governanceDomainGUID,
                                       String governanceDomainGUIDParameterName,
                                       String methodName) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        this.deleteBeanInRepository(userId,
                                    null,
                                    null,
                                    governanceDomainGUID,
                                    governanceDomainGUIDParameterName,
                                    OpenMetadataType.GOVERNANCE_DOMAIN_DESCRIPTION_TYPE_GUID,
                                    OpenMetadataType.GOVERNANCE_DOMAIN_DESCRIPTION_TYPE_NAME,
                                    null,
                                    null,
                                    false,
                                    false,
                                    new Date(),
                                    methodName);
    }


    /**
     * Retrieve the list of governance domain metadata elements that contain the search string.
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
    public List<B> findGovernanceDomains(String userId,
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
                              OpenMetadataType.GOVERNANCE_DOMAIN_DESCRIPTION_TYPE_GUID,
                              OpenMetadataType.GOVERNANCE_DOMAIN_DESCRIPTION_TYPE_NAME,
                              null,
                              startFrom,
                              pageSize,
                              false,
                              false,
                              new Date(),
                              methodName);
    }


    /**
     * Retrieve the list of governance domain metadata elements with a matching qualified or display name.
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
    public List<B>   getGovernanceDomainsByName(String userId,
                                                String name,
                                                String nameParameterName,
                                                int    startFrom,
                                                int    pageSize,
                                                String methodName) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        List<String> specificMatchPropertyNames = new ArrayList<>();
        specificMatchPropertyNames.add(OpenMetadataProperty.QUALIFIED_NAME.name);
        specificMatchPropertyNames.add(OpenMetadataProperty.DISPLAY_NAME.name);

        return this.getBeansByValue(userId,
                                    name,
                                    nameParameterName,
                                    OpenMetadataType.GOVERNANCE_DOMAIN_DESCRIPTION_TYPE_GUID,
                                    OpenMetadataType.GOVERNANCE_DOMAIN_DESCRIPTION_TYPE_NAME,
                                    specificMatchPropertyNames,
                                    true,
                                    null,
                                    null,
                                    false,
                                    false,
                                    supportedZones,
                                    null,
                                    startFrom,
                                    pageSize,
                                    null,
                                    methodName);
    }



    /**
     * Return the keywords attached to a supplied entity.
     *
     * @param userId     calling user
     * @param startingGUID identifier for the entity that the keyword is attached to
     * @param startingGUIDParameterName name of parameter supplying the GUID
     * @param startingFrom start position for results
     * @param pageSize     maximum number of results
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of retrieved objects or null if none found
     *
     * @throws InvalidParameterException  the input properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public List<B>  getDomainsInSet(String       userId,
                                    String       startingGUID,
                                    String       startingGUIDParameterName,
                                    int          startingFrom,
                                    int          pageSize,
                                    boolean      forLineage,
                                    boolean      forDuplicateProcessing,
                                    Date         effectiveTime,
                                    String       methodName) throws InvalidParameterException,
                                                                    PropertyServerException,
                                                                    UserNotAuthorizedException
    {
        return this.getAttachedElements(userId,
                                        null,
                                        null,
                                        startingGUID,
                                        startingGUIDParameterName,
                                        OpenMetadataType.COLLECTION.typeName,
                                        OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeGUID,
                                        OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName,
                                        OpenMetadataType.GOVERNANCE_DOMAIN_DESCRIPTION_TYPE_NAME,
                                        null,
                                        null,
                                        2,
                                        forLineage,
                                        forDuplicateProcessing,
                                        supportedZones,
                                        startingFrom,
                                        pageSize,
                                        effectiveTime,
                                        methodName);
    }


    /**
     * Retrieve the governance domain metadata element with the supplied unique identifier.
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
    public B getGovernanceDomainByGUID(String userId,
                                       String guid,
                                       String guidParameterName,
                                       String methodName) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        return this.getBeanFromRepository(userId,
                                          guid,
                                          guidParameterName,
                                          OpenMetadataType.GOVERNANCE_DOMAIN_DESCRIPTION_TYPE_NAME,
                                          false,
                                          false,
                                          new Date(),
                                          methodName);

    }



    /**
     * Retrieve the governance domain metadata element with the supplied unique domainIdentifier.
     *
     * @param userId calling user
     * @param domainIdentifier identifier used to identify the domain
     * @param methodName calling method
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public B getGovernanceDomainByDomainIdentifier(String userId,
                                                   int    domainIdentifier,
                                                   String methodName) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        return this.getBeanByUniqueName(userId,
                                        "GovernanceDomain:" + domainIdentifier,
                                        OpenMetadataType.DOMAIN_IDENTIFIER_PROPERTY_NAME,
                                        OpenMetadataType.DOMAIN_IDENTIFIER_PROPERTY_NAME,
                                        OpenMetadataType.GOVERNANCE_DOMAIN_DESCRIPTION_TYPE_GUID,
                                        OpenMetadataType.GOVERNANCE_DOMAIN_DESCRIPTION_TYPE_NAME,
                                        false,
                                        false,
                                        supportedZones,
                                        new Date(),
                                        methodName);
    }
}
