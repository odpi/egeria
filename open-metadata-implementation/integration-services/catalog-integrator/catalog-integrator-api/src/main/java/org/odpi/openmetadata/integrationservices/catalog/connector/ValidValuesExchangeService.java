/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.integrationservices.catalog.connector;

import org.odpi.openmetadata.accessservices.assetmanager.client.exchange.ValidValuesExchangeClient;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.ReferenceValueAssignmentDefinitionElement;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.ReferenceValueAssignmentItemElement;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.ValidValueElement;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.ValidValueMember;
import org.odpi.openmetadata.accessservices.assetmanager.properties.ExternalIdentifierProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.ReferenceValueAssignmentProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.SynchronizationDirection;
import org.odpi.openmetadata.accessservices.assetmanager.properties.ValidValueMembershipProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.ValidValueProperties;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.integrationservices.catalog.ffdc.CatalogIntegratorErrorCode;

import java.util.Date;
import java.util.List;


/**
 * ValidValuesExchangeService is the context for managing valid values and reference data.
 */
public class ValidValuesExchangeService
{
    private final ValidValuesExchangeClient validValuesExchangeClient;
    private final String                    userId;
    private final String                    assetManagerGUID;
    private final String                    assetManagerName;
    private final String                    connectorName;
    private final SynchronizationDirection  synchronizationDirection;
    private final AuditLog                  auditLog;

    private boolean forLineage             = false;
    private boolean forDuplicateProcessing = false;

    /**
     * Create a new client to exchange data asset content with open metadata.
     *
     * @param validValuesExchangeClient client for exchange requests
     * @param synchronizationDirection direction(s) that metadata can flow
     * @param userId integration daemon's userId
     * @param assetManagerGUID unique identifier of the software server capability for the asset manager
     * @param assetManagerName unique name of the software server capability for the asset manager
     * @param connectorName name of the connector using this context
     * @param auditLog logging destination
     */
    ValidValuesExchangeService(ValidValuesExchangeClient validValuesExchangeClient,
                               SynchronizationDirection  synchronizationDirection,
                               String                    userId,
                               String                    assetManagerGUID,
                               String                    assetManagerName,
                               String                    connectorName,
                               AuditLog                  auditLog)
    {
        this.validValuesExchangeClient = validValuesExchangeClient;
        this.synchronizationDirection  = synchronizationDirection;
        this.userId                    = userId;
        this.assetManagerGUID          = assetManagerGUID;
        this.assetManagerName          = assetManagerName;
        this.connectorName             = connectorName;
        this.auditLog                  = auditLog;
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



    /* =======================================================
     * Valid values methods
     */

    /**
     * Create a new valid value set.  This just creates the Set itself.  Members are added either as they are
     * created, or they can be attached to a set after they are created.
     *
     * @param externalIdentifierProperties optional properties used to define an external identifier
     * @param validValueProperties properties to store
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @return unique identifier for the new set
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public String  createValidValueSet(ExternalIdentifierProperties externalIdentifierProperties,
                                       ValidValueProperties         validValueProperties,
                                       Date                         effectiveTime) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        final String methodName = "createValidValueSet";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            return validValuesExchangeClient.createValidValueSet(userId,
                                                                 assetManagerGUID,
                                                                 assetManagerName,
                                                                 externalIdentifierProperties,
                                                                 validValueProperties,
                                                                 effectiveTime,
                                                                 forLineage,
                                                                 forDuplicateProcessing);
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
     * Create a new valid value definition.
     *
     * @param setGUID unique identifier of the set to attach this to.
     * @param externalIdentifierProperties optional properties used to define an external identifier
     * @param validValueProperties properties to store
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @return unique identifier for the new definition
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public String  createValidValueDefinition(String                       setGUID,
                                              ExternalIdentifierProperties externalIdentifierProperties,
                                              ValidValueProperties         validValueProperties,
                                              Date                         effectiveTime) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException
    {
        final String methodName = "createValidValueDefinition";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            return validValuesExchangeClient.createValidValueDefinition(userId,
                                                                        assetManagerGUID,
                                                                        assetManagerName,
                                                                        setGUID,
                                                                        externalIdentifierProperties,
                                                                        validValueProperties,
                                                                        effectiveTime,
                                                                        forLineage,
                                                                        forDuplicateProcessing);
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
     * Update the properties of the valid value.  All properties are updated.
     * If only changing some if the properties, retrieve the current values from the repository
     * and pass existing values back on this call if they are not to change.
     *
     * @param validValueGUID unique identifier of the valid value.
     * @param validValueExternalIdentifier unique identifier of the valid value in the external asset manager
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param validValueProperties properties to store
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public void    updateValidValue(String               validValueGUID,
                                    String               validValueExternalIdentifier,
                                    boolean              isMergeUpdate,
                                    ValidValueProperties validValueProperties,
                                    Date                 effectiveTime) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        final String methodName = "updateValidValue";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            validValuesExchangeClient.updateValidValue(userId,
                                                       assetManagerGUID,
                                                       assetManagerName,
                                                       validValueGUID,
                                                       validValueExternalIdentifier,
                                                       isMergeUpdate,
                                                       validValueProperties,
                                                       effectiveTime,
                                                       forLineage,
                                                       forDuplicateProcessing);
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
     * Remove the valid value form the repository.  All links to it are deleted too.
     *
     * @param validValueGUID unique identifier of the valid value.
     * @param validValueExternalIdentifier unique identifier of the valid value in the external asset manager
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public void    removeValidValue(String  validValueGUID,
                                    String  validValueExternalIdentifier,
                                    Date    effectiveTime) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        final String methodName = "removeValidValue";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            validValuesExchangeClient.removeValidValue(userId,
                                                       assetManagerGUID,
                                                       assetManagerName,
                                                       validValueGUID,
                                                       validValueExternalIdentifier,
                                                       effectiveTime,
                                                       forLineage,
                                                       forDuplicateProcessing);
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
     * Create a link between a valid value set or definition and a set.  This means the valid
     * value is a member of the set.
     *
     * @param setGUID unique identifier of the set.
     * @param validValueGUID unique identifier of the valid value to add to the set.
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public void    attachValidValueToSet(String  setGUID,
                                         String  validValueGUID,
                                         Date    effectiveTime) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        final String methodName = "attachValidValueToSet";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            validValuesExchangeClient.attachValidValueToSet(userId,
                                                            assetManagerGUID,
                                                            assetManagerName,
                                                            setGUID,
                                                            validValueGUID,
                                                            effectiveTime,
                                                            forLineage,
                                                            forDuplicateProcessing);
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
     * Create a link between a valid value set or definition and a set.  This means the valid
     * value is a member of the set.
     *
     * @param setGUID unique identifier of the set.
     * @param validValueGUID unique identifier of the valid value to add to the set.
     * @param properties is this the default value - used when creating a list of valid values
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public void    attachValidValueToSet(String                         setGUID,
                                         String                         validValueGUID,
                                         ValidValueMembershipProperties properties,
                                         Date                           effectiveTime) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException
    {
        final String methodName = "attachValidValueToSet";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            validValuesExchangeClient.attachValidValueToSet(userId,
                                                            assetManagerGUID,
                                                            assetManagerName,
                                                            setGUID,
                                                            validValueGUID,
                                                            properties,
                                                            effectiveTime,
                                                            forLineage,
                                                            forDuplicateProcessing);
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
     * Remove the link between a valid value and a set it is a member of.
     *
     * @param setGUID owning set
     * @param validValueGUID unique identifier of the member to be removed.
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public void    detachValidValueFromSet(String  setGUID,
                                           String  validValueGUID,
                                           Date    effectiveTime) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        final String methodName = "detachValidValueFromSet";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            validValuesExchangeClient.detachValidValueFromSet(userId,
                                                              assetManagerGUID,
                                                              assetManagerName,
                                                              setGUID,
                                                              validValueGUID,
                                                              effectiveTime,
                                                              forLineage,
                                                              forDuplicateProcessing);
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
     * Link a valid value as a reference value to a referencable to act as a tag/classification to help with locating and
     * grouping the referenceable.
     *
     * @param userId            calling user.
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param validValueGUID    unique identifier of the valid value.
     * @param referenceableGUID unique identifier of the element to link to.
     * @param properties        details of the relationship
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException    the repository is not available or not working properly.
     */
    public void assignReferenceValueToItem(String                             userId,
                                           String                             assetManagerGUID,
                                           String                             assetManagerName,
                                           String                             validValueGUID,
                                           String                             referenceableGUID,
                                           ReferenceValueAssignmentProperties properties,
                                           Date                               effectiveTime,
                                           boolean                            forLineage,
                                           boolean                            forDuplicateProcessing) throws InvalidParameterException,
                                                                                                             UserNotAuthorizedException,
                                                                                                             PropertyServerException
    {
        final String methodName = "assignReferenceValueToItem";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            validValuesExchangeClient.assignReferenceValueToItem(userId,
                                                                 assetManagerGUID,
                                                                 assetManagerName,
                                                                 validValueGUID,
                                                                 referenceableGUID,
                                                                 properties,
                                                                 effectiveTime,
                                                                 forLineage,
                                                                 forDuplicateProcessing);
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
     * Remove the reference value link between a valid value and a referenceable (item).
     *
     * @param userId            calling user.
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param validValueGUID    unique identifier of the valid value.
     * @param referenceableGUID unique identifier of the element to remove the link from.
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException    the repository is not available or not working properly.
     */
    public void unassignReferenceValueFromItem(String  userId,
                                               String  assetManagerGUID,
                                               String  assetManagerName,
                                               String  validValueGUID,
                                               String  referenceableGUID,
                                               Date    effectiveTime,
                                               boolean forLineage,
                                               boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        final String methodName = "unassignReferenceValueFromItem";

        if (synchronizationDirection != SynchronizationDirection.TO_THIRD_PARTY)
        {
            validValuesExchangeClient.unassignReferenceValueFromItem(userId,
                                                                     assetManagerGUID,
                                                                     assetManagerName,
                                                                     validValueGUID,
                                                                     referenceableGUID,
                                                                     effectiveTime,
                                                                     forLineage,
                                                                     forDuplicateProcessing);
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
     * Retrieve a specific valid value from the repository.
     *
     * @param validValueGUID unique identifier of the valid value.
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
    *
     * @return Valid value bean
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public ValidValueElement getValidValueByGUID(String  validValueGUID,
                                                 Date    effectiveTime) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        return validValuesExchangeClient.getValidValueByGUID(userId,
                                                             assetManagerGUID,
                                                             assetManagerName,
                                                             validValueGUID,
                                                             effectiveTime,
                                                             forLineage,
                                                             forDuplicateProcessing);
    }


    /**
     * Retrieve a specific valid value from the repository.  Duplicates may be returned if
     * multiple valid values have been assigned the same qualified name.
     *
     * @param validValueName qualified name of the valid value.
     * @param startFrom         starting element (used in paging through large result sets)
     * @param pageSize          maximum number of results to return
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @return Valid value beans
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public List<ValidValueElement> getValidValueByName(String  validValueName,
                                                       int     startFrom,
                                                       int     pageSize,
                                                       Date    effectiveTime) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        return validValuesExchangeClient.getValidValueByName(userId,
                                                             assetManagerGUID,
                                                             assetManagerName,
                                                             validValueName,
                                                             startFrom,
                                                             pageSize,
                                                             effectiveTime,
                                                             forLineage,
                                                             forDuplicateProcessing);
    }



    /**
     * Locate valid values that match the search string.  It considers the names, description, scope,
     * usage and preferred value.
     *
     * @param searchString string value to look for - may contain RegEx characters.
     * @param startFrom paging starting point
     * @param pageSize maximum number of return values.
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @return list of valid value beans
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public List<ValidValueElement> findValidValues(String  searchString,
                                                   int     startFrom,
                                                   int     pageSize,
                                                   Date    effectiveTime) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        return validValuesExchangeClient.findValidValues(userId,
                                                         assetManagerGUID,
                                                         assetManagerName,
                                                         searchString,
                                                         startFrom,
                                                         pageSize,
                                                         effectiveTime,
                                                         forLineage,
                                                         forDuplicateProcessing);
    }


    /**
     * Page through the members of a valid value set.
     *
     * @param validValueSetGUID unique identifier of the valid value set.
     * @param startFrom paging starting point
     * @param pageSize maximum number of return values.
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @return list of valid value beans
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public List<ValidValueMember> getValidValueSetMembers(String  validValueSetGUID,
                                                          int     startFrom,
                                                          int     pageSize,
                                                          Date    effectiveTime) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        return validValuesExchangeClient.getValidValueSetMembers(userId,
                                                                 assetManagerGUID,
                                                                 assetManagerName,
                                                                 validValueSetGUID,
                                                                 startFrom,
                                                                 pageSize,
                                                                 effectiveTime,
                                                                 forLineage,
                                                                 forDuplicateProcessing);
    }



    /**
     * Page through the list of valid value sets that a valid value definition/set belongs to.
     *
     * @param validValueGUID unique identifier of valid value to query
     * @param startFrom paging starting point
     * @param pageSize maximum number of return values.
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @return list of valid value beans
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public List<ValidValueElement> getSetsForValidValue(String  validValueGUID,
                                                        int     startFrom,
                                                        int     pageSize,
                                                        Date    effectiveTime) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        return validValuesExchangeClient.getSetsForValidValue(userId,
                                                              assetManagerGUID,
                                                              assetManagerName,
                                                              validValueGUID,
                                                              startFrom,
                                                              pageSize,
                                                              effectiveTime,
                                                              forLineage,
                                                              forDuplicateProcessing);
    }



    /**
     * Page through the list of referenceables that have this valid value as a reference value.
     *
     * @param validValueGUID unique identifier of valid value to query
     * @param startFrom      paging starting point
     * @param pageSize       maximum number of return values.
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @return list of referenceable beans
     *
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException    the repository is not available or not working properly.
     */
    public List<ReferenceValueAssignmentItemElement> getReferenceValueAssignedItems(String  validValueGUID,
                                                                                    int     startFrom,
                                                                                    int     pageSize,
                                                                                    Date    effectiveTime) throws InvalidParameterException,
                                                                                                                  UserNotAuthorizedException,
                                                                                                                  PropertyServerException
    {
        return validValuesExchangeClient.getReferenceValueAssignedItems(userId,
                                                                        assetManagerGUID,
                                                                        assetManagerName,
                                                                        validValueGUID,
                                                                        startFrom,
                                                                        pageSize,
                                                                        effectiveTime,
                                                                        forLineage,
                                                                        forDuplicateProcessing);
    }


    /**
     * Page through the list of assigned reference values for a referenceable.
     *
     * @param referenceableGUID unique identifier of assigned item
     * @param startFrom         paging starting point
     * @param pageSize          maximum number of return values.
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     *
     * @return list of valid value beans
     *
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException    the repository is not available or not working properly.
     */
    public List<ReferenceValueAssignmentDefinitionElement> getReferenceValueAssignments(String  referenceableGUID,
                                                                                        int     startFrom,
                                                                                        int     pageSize,
                                                                                        Date    effectiveTime) throws InvalidParameterException,
                                                                                                                      UserNotAuthorizedException,
                                                                                                                      PropertyServerException
    {
        return validValuesExchangeClient.getReferenceValueAssignments(userId,
                                                                      assetManagerGUID,
                                                                      assetManagerName,
                                                                      referenceableGUID,
                                                                      startFrom,
                                                                      pageSize,
                                                                      effectiveTime,
                                                                      forLineage,
                                                                      forDuplicateProcessing);
    }
}
