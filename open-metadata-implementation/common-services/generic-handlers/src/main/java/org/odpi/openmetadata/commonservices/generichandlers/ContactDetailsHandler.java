/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Date;
import java.util.List;


/**
 * ContactDetailHandler manages the ContactDetails entity.  The ContactDetails entity describes a contact method for an actor profile.
 */
public class ContactDetailsHandler<B> extends OpenMetadataAPIGenericHandler<B>
{
    /**
     * Construct the handler information needed to interact with the repository services
     *
     * @param converter specific converter for this bean class
     * @param beanClass name of bean class that is represented by the generic class B
     * @param serviceName      name of this service
     * @param serverName       name of the local server
     * @param invalidParameterHandler handler for managing parameter errors
     * @param repositoryHandler     manages calls to the repository services
     * @param repositoryHelper provides utilities for manipulating the repository services objects
     * @param localServerUserId userId for this server
     * @param securityVerifier open metadata security services verifier
     * @param supportedZones list of zones that the access service is allowed to serve Asset instances from.
     * @param defaultZones list of zones that the access service should set in all new Asset instances.
     * @param publishZones list of zones that the access service sets up in published Asset instances.
     * @param auditLog destination for audit log events.
     */
    public ContactDetailsHandler(OpenMetadataAPIGenericConverter<B> converter,
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
     * Return the ContactDetails attached to a supplied actor profile.
     *
     * @param userId     calling user
     * @param profileGUID identifier for the entity that the contact details are attached to
     * @param profileGUIDParameterName name of parameter supplying the GUID
     * @param startingFrom where to start from in the list
     * @param pageSize maximum number of results that can be returned
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @return list of objects or null if none found
     * @throws InvalidParameterException  the input properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public List<B>  getContactDetails(String              userId,
                                      String              profileGUID,
                                      String              profileGUIDParameterName,
                                      int                 startingFrom,
                                      int                 pageSize,
                                      boolean             forLineage,
                                      boolean             forDuplicateProcessing,
                                      Date                effectiveTime,
                                      String              methodName) throws InvalidParameterException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException
    {
        return this.getAttachedElements(userId,
                                        null,
                                        null,
                                        profileGUID,
                                        profileGUIDParameterName,
                                        OpenMetadataType.ACTOR_PROFILE_TYPE_NAME,
                                        OpenMetadataType.CONTACT_THROUGH_RELATIONSHIP.typeGUID,
                                        OpenMetadataType.CONTACT_THROUGH_RELATIONSHIP.typeName,
                                        OpenMetadataType.CONTACT_DETAILS.typeName,
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
     * Create a new contact method for a profile.
     *
     * @param userId      userId of user making request.
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param profileGUID   unique identifier for the connected entity (Referenceable).
     * @param profileGUIDParameterName parameter supplying the profileGUID
     * @param contactName name of this contact method - eg my office phone number
     * @param contactType type of contact eg "Office phone number" - typically controlled from a valid value set
     * @param contactMethodType  ordinal for contact type
     * @param contactMethodService   name of the service to call
     * @param contactMethodValue   identity value to use for this profile through this contact method
     * @param effectiveFrom starting time for this relationship (null for all time)
     * @param effectiveTo ending time for this relationship (null for all time)
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @return unique identifier of the contact method
     *
     * @throws InvalidParameterException  the endpoint bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public String createContactMethod(String              userId,
                                      String              externalSourceGUID,
                                      String              externalSourceName,
                                      String              profileGUID,
                                      String              profileGUIDParameterName,
                                      String              contactName,
                                      String              contactType,
                                      int                 contactMethodType,
                                      String              contactMethodService,
                                      String              contactMethodValue,
                                      Date                effectiveFrom,
                                      Date                effectiveTo,
                                      boolean             forLineage,
                                      boolean             forDuplicateProcessing,
                                      Date                effectiveTime,
                                      String              methodName) throws InvalidParameterException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException
    {
        ContactDetailsBuilder builder = new ContactDetailsBuilder(contactName,
                                                                  contactType,
                                                                  contactMethodType,
                                                                  contactMethodService,
                                                                  contactMethodValue,
                                                                  repositoryHelper,
                                                                  serviceName,
                                                                  serverName);

        this.addAnchorGUIDToBuilder(userId,
                                    profileGUID,
                                    profileGUIDParameterName,
                                    forLineage,
                                    forDuplicateProcessing,
                                    effectiveTime,
                                    supportedZones,
                                    builder,
                                    methodName);

        builder.setEffectivityDates(effectiveFrom, effectiveTo);

        String contactMethodGUID = this.createBeanInRepository(userId,
                                                               externalSourceGUID,
                                                               externalSourceName,
                                                               OpenMetadataType.CONTACT_DETAILS.typeGUID,
                                                               OpenMetadataType.CONTACT_DETAILS.typeName,
                                                               builder,
                                                               effectiveTime,
                                                               methodName);

        if ((contactMethodGUID != null) && (profileGUID != null))
        {
            final String contactMethodGUIDParameterName = "contactMethodGUID";

            this.uncheckedLinkElementToElement(userId,
                                               externalSourceGUID,
                                               externalSourceName,
                                               profileGUID,
                                               profileGUIDParameterName,
                                               OpenMetadataType.ACTOR_PROFILE_TYPE_NAME,
                                               contactMethodGUID,
                                               contactMethodGUIDParameterName,
                                               OpenMetadataType.CONTACT_DETAILS.typeName,
                                               forLineage,
                                               forDuplicateProcessing,
                                               supportedZones,
                                               OpenMetadataType.CONTACT_THROUGH_RELATIONSHIP.typeGUID,
                                               OpenMetadataType.CONTACT_THROUGH_RELATIONSHIP.typeName,
                                               null,
                                               effectiveTime,
                                               methodName);
        }

        return contactMethodGUID;
    }


    /**
     * Remove the requested contact method.
     *
     * @param userId       calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param contactMethodGUID   unique identifier for the connected entity (Referenceable).
     * @param contactMethodGUIDParameterName parameter supplying the contactMethodGUID
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName   calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public void removeContactDetail(String  userId,
                                    String  externalSourceGUID,
                                    String  externalSourceName,
                                    String  contactMethodGUID,
                                    String  contactMethodGUIDParameterName,
                                    boolean forLineage,
                                    boolean forDuplicateProcessing,
                                    Date    effectiveTime,
                                    String  methodName) throws InvalidParameterException,
                                                               PropertyServerException,
                                                               UserNotAuthorizedException
    {
        this.deleteBeanInRepository(userId,
                                    externalSourceGUID,
                                    externalSourceName,
                                    contactMethodGUID,
                                    contactMethodGUIDParameterName,
                                    OpenMetadataType.CONTACT_DETAILS.typeGUID,
                                    OpenMetadataType.CONTACT_DETAILS.typeName,
                                    null,
                                    null,
                                    forLineage,
                                    forDuplicateProcessing,
                                    effectiveTime,
                                    methodName);
    }
}
