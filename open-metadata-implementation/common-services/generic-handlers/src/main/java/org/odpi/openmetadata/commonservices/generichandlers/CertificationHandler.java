/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * CertificationHandler manages Certification objects.  It runs server-side in
 * the OMAG Server Platform and retrieves Certification entities through the OMRSRepositoryConnector.
 */
public class CertificationHandler<B> extends GovernanceDefinitionHandler<B>
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
    public CertificationHandler(OpenMetadataAPIGenericConverter<B> converter,
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
     * Count the number of Certifications attached to a referenceable entity.
     *
     * @param userId     calling user
     * @param connectedEntityGUID identifier for the entity that the object is attached to
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @return count of attached objects
     * @throws InvalidParameterException  the parameters are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public int countCertifications(String  userId,
                                   String  connectedEntityGUID,
                                   boolean forLineage,
                                   boolean forDuplicateProcessing,
                                   Date    effectiveTime,
                                   String  methodName) throws InvalidParameterException,
                                                               PropertyServerException,
                                                               UserNotAuthorizedException
    {
        return super.countAttachments(userId,
                                      connectedEntityGUID,
                                      OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                      OpenMetadataAPIMapper.CERTIFICATION_OF_REFERENCEABLE_TYPE_GUID,
                                      OpenMetadataAPIMapper.CERTIFICATION_OF_REFERENCEABLE_TYPE_NAME,
                                      2,
                                      forLineage,
                                      forDuplicateProcessing,
                                      effectiveTime,
                                      methodName);
    }


    /**
     * Return the Certifications attached to a referenceable entity.
     *
     * @param userId     calling user
     * @param parentGUID identifier for the entity that the feedback is attached to
     * @param parentGUIDParameterName parameter name for the parentGUID
     * @param startFrom where to start from in the list
     * @param pageSize maximum number of results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @return unique identifier of the object or null
     * @throws InvalidParameterException  the input properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public List<B>  getCertifications(String       userId,
                                      String       parentGUID,
                                      String       parentGUIDParameterName,
                                      int          startFrom,
                                      int          pageSize,
                                      boolean      forLineage,
                                      boolean      forDuplicateProcessing,
                                      Date         effectiveTime,
                                      String       methodName) throws InvalidParameterException,
                                                                      PropertyServerException,
                                                                      UserNotAuthorizedException
    {
        return this.getCertifications(userId,
                                      parentGUID,
                                      parentGUIDParameterName,
                                      supportedZones,
                                      startFrom,
                                      pageSize,
                                      forLineage,
                                      forDuplicateProcessing,
                                      effectiveTime,
                                      methodName);
    }


    /**
     * Return the Certifications attached to a referenceable entity.
     *
     * @param userId     calling user
     * @param parentGUID identifier for the entity that the feedback is attached to
     * @param parentGUIDParameterName parameter name for the parentGUID
     * @param serviceSupportedZones supported zones for calling service
     * @param startFrom where to start from in the list
     * @param pageSize maximum number of results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @return unique identifier of the object or null
     * @throws InvalidParameterException  the input properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public List<B>  getCertifications(String       userId,
                                      String       parentGUID,
                                      String       parentGUIDParameterName,
                                      List<String> serviceSupportedZones,
                                      int          startFrom,
                                      int          pageSize,
                                      boolean      forLineage,
                                      boolean      forDuplicateProcessing,
                                      Date         effectiveTime,
                                      String       methodName) throws InvalidParameterException,
                                                                      PropertyServerException,
                                                                      UserNotAuthorizedException
    {
        List<Relationship>  relationships = this.getAttachmentLinks(userId,
                                                                    parentGUID,
                                                                    parentGUIDParameterName,
                                                                    OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                                                    OpenMetadataAPIMapper.CERTIFICATION_OF_REFERENCEABLE_TYPE_GUID,
                                                                    OpenMetadataAPIMapper.CERTIFICATION_OF_REFERENCEABLE_TYPE_NAME,
                                                                    null,
                                                                    OpenMetadataAPIMapper.CERTIFICATION_TYPE_TYPE_NAME,
                                                                    2,
                                                                    forLineage,
                                                                    forDuplicateProcessing,
                                                                    serviceSupportedZones,
                                                                    startFrom,
                                                                    pageSize,
                                                                    effectiveTime,
                                                                    methodName);
        if (relationships != null)
        {
            List<B>  results = new ArrayList<>();

            for (Relationship relationship : relationships)
            {
                if (relationship != null)
                {
                    EntityProxy entityProxy = relationship.getEntityTwoProxy();

                    if (entityProxy != null)
                    {
                        final String  entityParameterName = "entityProxyTwo.getGUID";

                        EntityDetail entity = this.getEntityFromRepository(userId,
                                                                           entityProxy.getGUID(),
                                                                           entityParameterName,
                                                                           OpenMetadataAPIMapper.CERTIFICATION_TYPE_TYPE_NAME,
                                                                           null,
                                                                           null,
                                                                           false,
                                                                           false,
                                                                           serviceSupportedZones,
                                                                           effectiveTime,
                                                                           methodName);


                        results.add(converter.getNewBean(beanClass, entity, relationship, methodName));
                        // TODO external reference link see setLink();
                    }
                }
            }

            if (results.isEmpty())
            {
                return null;
            }
            else
            {
                return results;
            }
        }

        return null;
    }


    /**
     * Create a link between a certification type and an element that has achieved the certification.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param elementGUID unique identifier of the element
     * @param elementGUIDParameterName parameter supplying the elementGUID
     * @param elementTypeName typename of super-definition
     * @param certificationTypeGUID unique identifier of the certification type
     * @param certificationTypeGUIDParameterName parameter supplying certificationTypeGUID
     * @param certificationTypeGUIDTypeName type name of the certificationTypeGUID
     * @param certificateGUID  unique identifier of the certificate (maybe from an external system)
     * @param start when did the certification start
     * @param end when will the certification end
     * @param conditions any conditions added to the certification
     * @param certifiedBy unique name/identifier of the element for the person/organization certifying the element
     * @param certifiedByTypeName type of the certifiedBy element
     * @param certifiedByPropertyName property name for the unique identifier from the certifiedBy element
     * @param custodian unique name/identifier of the element for the person/organization responsible for maintaining the certification status
     * @param custodianTypeName type of the custodian element
     * @param custodianPropertyName property name for the unique identifier from the custodian element
     * @param recipient unique name/identifier of the element for the person/organization receiving the certification
     * @param recipientTypeName type of the recipient element
     * @param recipientPropertyName property name for the unique identifier from the recipient element
     * @param notes additional information, endorsements etc
     * @param effectiveFrom starting time for this relationship (null for all time)
     * @param effectiveTo ending time for this relationship (null for all time)
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return guid of certification relationship
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String certifyElement(String  userId,
                                 String  externalSourceGUID,
                                 String  externalSourceName,
                                 String  elementGUID,
                                 String  elementGUIDParameterName,
                                 String  elementTypeName,
                                 String  certificationTypeGUID,
                                 String  certificationTypeGUIDParameterName,
                                 String  certificationTypeGUIDTypeName,
                                 String  certificateGUID,
                                 Date    start,
                                 Date    end,
                                 String  conditions,
                                 String  certifiedBy,
                                 String  certifiedByTypeName,
                                 String  certifiedByPropertyName,
                                 String  custodian,
                                 String  custodianTypeName,
                                 String  custodianPropertyName,
                                 String  recipient,
                                 String  recipientTypeName,
                                 String  recipientPropertyName,
                                 String  notes,
                                 Date    effectiveFrom,
                                 Date    effectiveTo,
                                 boolean forLineage,
                                 boolean forDuplicateProcessing,
                                 Date    effectiveTime,
                                 String  methodName) throws InvalidParameterException,
                                                            UserNotAuthorizedException,
                                                            PropertyServerException
    {
        return this.multiLinkElementToElement(userId,
                                              externalSourceGUID,
                                              externalSourceName,
                                              elementGUID,
                                              elementGUIDParameterName,
                                              elementTypeName,
                                              certificationTypeGUID,
                                              certificationTypeGUIDParameterName,
                                              certificationTypeGUIDTypeName,
                                              forLineage,
                                              forDuplicateProcessing,
                                              supportedZones,
                                              OpenMetadataAPIMapper.CERTIFICATION_OF_REFERENCEABLE_TYPE_GUID,
                                              OpenMetadataAPIMapper.CERTIFICATION_OF_REFERENCEABLE_TYPE_NAME,
                                              getCertificationProperties(certificateGUID,
                                                                         start,
                                                                         end,
                                                                         conditions,
                                                                         certifiedBy,
                                                                         certifiedByTypeName,
                                                                         certifiedByPropertyName,
                                                                         custodian,
                                                                         custodianTypeName,
                                                                         custodianPropertyName,
                                                                         recipient,
                                                                         recipientTypeName,
                                                                         recipientPropertyName,
                                                                         notes,
                                                                         effectiveFrom,
                                                                         effectiveTo,
                                                                         methodName),
                                              effectiveTime,
                                              methodName);
    }


    /**
     * Update the certification relationship.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param certificationGUID unique identifier for the relationship
     * @param certificationGUIDParameterName parameter
     * @param certificateGUID  unique identifier of the certificate (maybe from an external system)
     * @param start when did the certification start
     * @param end when will the certification end
     * @param conditions any conditions added to the certification
     * @param certifiedBy unique name/identifier of the element for the person/organization certifying the element
     * @param certifiedByTypeName type of the certifiedBy element
     * @param certifiedByPropertyName property name for the unique identifier from the certifiedBy element
     * @param custodian unique name/identifier of the element for the person/organization responsible for maintaining the certification status
     * @param custodianTypeName type of the custodian element
     * @param custodianPropertyName property name for the unique identifier from the custodian element
     * @param recipient unique name/identifier of the element for the person/organization receiving the certification
     * @param recipientTypeName type of the recipient element
     * @param recipientPropertyName property name for the unique identifier from the recipient element
     * @param notes additional information, endorsements etc
     * @param isMergeUpdate are unspecified properties unchanged (true) or replaced with null?
     * @param effectiveFrom starting time for this relationship (null for all time)
     * @param effectiveTo ending time for this relationship (null for all time)
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateCertification(String  userId,
                                    String  externalSourceGUID,
                                    String  externalSourceName,
                                    String  certificationGUID,
                                    String  certificationGUIDParameterName,
                                    String  certificateGUID,
                                    Date    start,
                                    Date    end,
                                    String  conditions,
                                    String  certifiedBy,
                                    String  certifiedByTypeName,
                                    String  certifiedByPropertyName,
                                    String  custodian,
                                    String  custodianTypeName,
                                    String  custodianPropertyName,
                                    String  recipient,
                                    String  recipientTypeName,
                                    String  recipientPropertyName,
                                    String  notes,
                                    boolean isMergeUpdate,
                                    Date    effectiveFrom,
                                    Date    effectiveTo,
                                    boolean forLineage,
                                    boolean forDuplicateProcessing,
                                    Date    effectiveTime,
                                    String  methodName) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        this.updateRelationshipProperties(userId,
                                          externalSourceGUID,
                                          externalSourceName,
                                          certificationGUID,
                                          certificationGUIDParameterName,
                                          OpenMetadataAPIMapper.CERTIFICATION_OF_REFERENCEABLE_TYPE_NAME,
                                          isMergeUpdate,
                                          getCertificationProperties(certificateGUID,
                                                                     start,
                                                                     end,
                                                                     conditions,
                                                                     certifiedBy,
                                                                     certifiedByTypeName,
                                                                     certifiedByPropertyName,
                                                                     custodian,
                                                                     custodianTypeName,
                                                                     custodianPropertyName,
                                                                     recipient,
                                                                     recipientTypeName,
                                                                     recipientPropertyName,
                                                                     notes,
                                                                     effectiveFrom,
                                                                     effectiveTo,
                                                                     methodName),
                                          forLineage,
                                          forDuplicateProcessing,
                                          supportedZones,
                                          effectiveTime,
                                          methodName);
    }


    /**
     * Set up the properties for a certification.
     *
     * @param certificateGUID  unique identifier of the certificate (maybe from an external system)
     * @param start when did the certification start
     * @param end when will the certification end
     * @param conditions any conditions added to the certification
     * @param certifiedBy unique name/identifier of the element for the person/organization certifying the element
     * @param certifiedByTypeName type of the certifiedBy element
     * @param certifiedByPropertyName property name for the unique identifier from the certifiedBy element
     * @param custodian unique name/identifier of the element for the person/organization responsible for maintaining the certification status
     * @param custodianTypeName type of the custodian element
     * @param custodianPropertyName property name for the unique identifier from the custodian element
     * @param recipient unique name/identifier of the element for the person/organization receiving the certification
     * @param recipientTypeName type of the recipient element
     * @param recipientPropertyName property name for the unique identifier from the recipient element
     * @param notes additional information, endorsements etc
     * @param effectiveFrom starting time for this relationship (null for all time)
     * @param effectiveTo ending time for this relationship (null for all time)
     * @param methodName calling method
     * @return instance properties
     */
    private InstanceProperties getCertificationProperties(String  certificateGUID,
                                                          Date    start,
                                                          Date    end,
                                                          String  conditions,
                                                          String  certifiedBy,
                                                          String  certifiedByTypeName,
                                                          String  certifiedByPropertyName,
                                                          String  custodian,
                                                          String  custodianTypeName,
                                                          String  custodianPropertyName,
                                                          String  recipient,
                                                          String  recipientTypeName,
                                                          String  recipientPropertyName,
                                                          String  notes,
                                                          Date    effectiveFrom,
                                                          Date    effectiveTo,
                                                          String  methodName)
    {
        InstanceProperties properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                     null,
                                                                                     OpenMetadataAPIMapper.CERTIFICATE_GUID_PROPERTY_NAME,
                                                                                     certificateGUID,
                                                                                     methodName);

        properties = repositoryHelper.addDatePropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataAPIMapper.START_PROPERTY_NAME,
                                                                  start,
                                                                  methodName);

        properties = repositoryHelper.addDatePropertyToInstance(serviceName,
                                                                properties,
                                                                OpenMetadataAPIMapper.END_PROPERTY_NAME,
                                                                end,
                                                                methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataAPIMapper.CONDITIONS_PROPERTY_NAME,
                                                                  conditions,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataAPIMapper.CERTIFIED_BY_PROPERTY_NAME,
                                                                  certifiedBy,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataAPIMapper.CERTIFIED_BY_TYPE_NAME_PROPERTY_NAME,
                                                                  certifiedByTypeName,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataAPIMapper.CERTIFIED_BY_PROPERTY_NAME_PROPERTY_NAME,
                                                                  certifiedByPropertyName,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataAPIMapper.CUSTODIAN_PROPERTY_NAME,
                                                                  custodian,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataAPIMapper.CUSTODIAN_TYPE_NAME_PROPERTY_NAME,
                                                                  custodianTypeName,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataAPIMapper.CUSTODIAN_PROPERTY_NAME_PROPERTY_NAME,
                                                                  custodianPropertyName,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataAPIMapper.RECIPIENT_PROPERTY_NAME,
                                                                  recipient,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataAPIMapper.RECIPIENT_TYPE_NAME_PROPERTY_NAME,
                                                                  recipientTypeName,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataAPIMapper.RECIPIENT_PROPERTY_NAME_PROPERTY_NAME,
                                                                  recipientPropertyName,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataAPIMapper.NOTES_PROPERTY_NAME,
                                                                  notes,
                                                                  methodName);

        return this.setUpEffectiveDates(properties, effectiveFrom, effectiveTo);
    }


    /**
     * Remove a relationship between two definitions.
     *
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param userId calling user
     * @param certificationGUID unique identifier of the certification relationship
     * @param certificationGUIDParameterName parameter supplying certificationGUID
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void decertifyElement(String  userId,
                                 String  externalSourceGUID,
                                 String  externalSourceName,
                                 String  certificationGUID,
                                 String  certificationGUIDParameterName,
                                 boolean forLineage,
                                 boolean forDuplicateProcessing,
                                 Date    effectiveTime,
                                 String  methodName) throws InvalidParameterException,
                                                            UserNotAuthorizedException,
                                                            PropertyServerException
    {
        this.deleteRelationship(userId,
                                externalSourceGUID,
                                externalSourceName,
                                certificationGUID,
                                certificationGUIDParameterName,
                                OpenMetadataAPIMapper.CERTIFICATION_OF_REFERENCEABLE_TYPE_NAME,
                                forLineage,
                                forDuplicateProcessing,
                                effectiveTime,
                                methodName);
    }
}
