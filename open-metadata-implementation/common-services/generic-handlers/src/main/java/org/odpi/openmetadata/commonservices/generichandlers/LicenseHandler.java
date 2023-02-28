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
 * LicenseHandler manages License objects.  It runs server-side in
 * the OMAG Server Platform and retrieves License entities through the OMRSRepositoryConnector.
 */
public class LicenseHandler<B> extends GovernanceDefinitionHandler<B>
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
    public LicenseHandler(OpenMetadataAPIGenericConverter<B> converter,
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
     * Count the number of licenses attached to an entity.
     *
     * @param userId     calling user
     * @param elementGUID identifier for the entity that the object is attached to
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @return count of attached objects
     * @throws InvalidParameterException  the parameters are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public int countLicenses(String   userId,
                             String   elementGUID,
                             boolean  forLineage,
                             boolean  forDuplicateProcessing,
                             Date     effectiveTime,
                             String   methodName) throws InvalidParameterException,
                                                         PropertyServerException,
                                                         UserNotAuthorizedException
    {
        return super.countAttachments(userId,
                                      elementGUID,
                                      OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                      OpenMetadataAPIMapper.LICENSE_OF_REFERENCEABLE_TYPE_GUID,
                                      OpenMetadataAPIMapper.LICENSE_OF_REFERENCEABLE_TYPE_NAME,
                                      2,
                                      forLineage,
                                      forDuplicateProcessing,
                                      effectiveTime,
                                      methodName);
    }


    /**
     * Return the Licenses attached to an anchor entity.
     *
     * @param userId     calling user
     * @param elementGUID identifier for the entity that the license is attached to
     * @param elementGUIDParameterName name of parameter supplying the GUID
     * @param elementTypeName name of the type of object being attached to
     * @param startFrom where to start from in the list
     * @param pageSize maximum number of results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime what is the effective time for related queries needed to do the update
     * @param methodName calling method
     * @return list of licenses or null if none found
     * @throws InvalidParameterException  the input properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public List<B>  getLicenses(String       userId,
                                String       elementGUID,
                                String       elementGUIDParameterName,
                                String       elementTypeName,
                                int          startFrom,
                                int          pageSize,
                                boolean      forLineage,
                                boolean      forDuplicateProcessing,
                                Date         effectiveTime,
                                String       methodName) throws InvalidParameterException,
                                                                PropertyServerException,
                                                                UserNotAuthorizedException
    {
        return this.getLicenses(userId,
                                elementGUID,
                                elementGUIDParameterName, elementTypeName,
                                supportedZones,
                                startFrom,
                                pageSize,
                                forLineage,
                                forDuplicateProcessing,
                                effectiveTime,
                                methodName);
    }


    /**
     * Return the Licenses attached to an element.
     *
     * @param userId     calling user
     * @param elementGUID identifier for the entity that the license is attached to
     * @param elementGUIDParameterName name of parameter supplying the GUID
     * @param elementTypeName name of the type of object being attached to
     * @param serviceSupportedZones supported zones for calling service
     * @param startFrom where to start from in the list
     * @param pageSize maximum number of results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @return list of licenses or null if none found
     * @throws InvalidParameterException  the input properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public List<B>  getLicenses(String       userId,
                                String       elementGUID,
                                String       elementGUIDParameterName,
                                String       elementTypeName,
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
                                                                    elementGUID,
                                                                    elementGUIDParameterName,
                                                                    elementTypeName,
                                                                    OpenMetadataAPIMapper.LICENSE_OF_REFERENCEABLE_TYPE_GUID,
                                                                    OpenMetadataAPIMapper.LICENSE_OF_REFERENCEABLE_TYPE_NAME,
                                                                    null,
                                                                    OpenMetadataAPIMapper.LICENSE_TYPE_TYPE_NAME,
                                                                    0,
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
                                                                           OpenMetadataAPIMapper.LICENSE_TYPE_TYPE_NAME,
                                                                           null,
                                                                           null,
                                                                           forLineage,
                                                                           forDuplicateProcessing,
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
     * Create a link between a license type and an element that has achieved the license.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param elementGUID unique identifier of the element
     * @param elementGUIDParameterName parameter supplying the elementGUID
     * @param elementTypeName typename of super-definition
     * @param licenseTypeGUID unique identifier of the license type
     * @param licenseTypeGUIDParameterName parameter supplying licenseTypeGUID
     * @param licenseTypeGUIDTypeName type name of the licenseTypeGUID
     * @param licenseGUID  unique identifier of the license (maybe from an external system)
     * @param start when did the license start
     * @param end when will the license end
     * @param conditions any conditions added to the license
     * @param licensedBy unique name/identifier of the element for the person/organization licensing the element
     * @param licensedByTypeName type of the licensedBy element
     * @param licensedByPropertyName property name for the unique identifier from the licensedBy element
     * @param custodian unique name/identifier of the element for the person/organization responsible for maintaining the license status
     * @param custodianTypeName type of the custodian element
     * @param custodianPropertyName property name for the unique identifier from the custodian element
     * @param licensee unique name/identifier of the element for the person/organization receiving the license
     * @param licenseeTypeName type of the licensee element
     * @param licenseePropertyName property name for the unique identifier from the licensee element
     * @param notes additional information, endorsements etc
     * @param effectiveFrom starting time for this relationship (null for all time)
     * @param effectiveTo ending time for this relationship (null for all time)
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return guid of license relationship
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String licenseElement(String  userId,
                                 String  externalSourceGUID,
                                 String  externalSourceName,
                                 String  elementGUID,
                                 String  elementGUIDParameterName,
                                 String  elementTypeName,
                                 String  licenseTypeGUID,
                                 String  licenseTypeGUIDParameterName,
                                 String  licenseTypeGUIDTypeName,
                                 String  licenseGUID,
                                 Date    start,
                                 Date    end,
                                 String  conditions,
                                 String  licensedBy,
                                 String  licensedByTypeName,
                                 String  licensedByPropertyName,
                                 String  custodian,
                                 String  custodianTypeName,
                                 String  custodianPropertyName,
                                 String  licensee,
                                 String  licenseeTypeName,
                                 String  licenseePropertyName,
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
                                              licenseTypeGUID,
                                              licenseTypeGUIDParameterName,
                                              licenseTypeGUIDTypeName,
                                              forLineage,
                                              forDuplicateProcessing,
                                              supportedZones,
                                              OpenMetadataAPIMapper.LICENSE_OF_REFERENCEABLE_TYPE_GUID,
                                              OpenMetadataAPIMapper.LICENSE_OF_REFERENCEABLE_TYPE_NAME,
                                              getLicenseProperties(licenseGUID,
                                                                   start,
                                                                   end,
                                                                   conditions,
                                                                   licensedBy,
                                                                   licensedByTypeName,
                                                                   licensedByPropertyName,
                                                                   custodian,
                                                                   custodianTypeName,
                                                                   custodianPropertyName,
                                                                   licensee,
                                                                   licenseeTypeName,
                                                                   licenseePropertyName,
                                                                   notes,
                                                                   effectiveFrom,
                                                                   effectiveTo,
                                                                   methodName),
                                              effectiveTime,
                                              methodName);
    }


    /**
     * Update the license relationship.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param licenseGUID unique identifier for the relationship
     * @param licenseGUIDParameterName parameter
     * @param licenseId  unique identifier of the license (maybe from an external system)
     * @param start when did the license start
     * @param end when will the license end
     * @param conditions any conditions added to the license
     * @param licensedBy unique name/identifier of the element for the person/organization licensing the element
     * @param licensedByTypeName type of the licensedBy element
     * @param licensedByPropertyName property name for the unique identifier from the licensedBy element
     * @param custodian unique name/identifier of the element for the person/organization responsible for maintaining the license status
     * @param custodianTypeName type of the custodian element
     * @param custodianPropertyName property name for the unique identifier from the custodian element
     * @param licensee unique name/identifier of the element for the person/organization receiving the license
     * @param licenseeTypeName type of the licensee element
     * @param licenseePropertyName property name for the unique identifier from the licensee element
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
    public void updateLicense(String  userId,
                              String  externalSourceGUID,
                              String  externalSourceName,
                              String  licenseGUID,
                              String  licenseGUIDParameterName,
                              String  licenseId,
                              Date    start,
                              Date    end,
                              String  conditions,
                              String  licensedBy,
                              String  licensedByTypeName,
                              String  licensedByPropertyName,
                              String  custodian,
                              String  custodianTypeName,
                              String  custodianPropertyName,
                              String  licensee,
                              String  licenseeTypeName,
                              String  licenseePropertyName,
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
                                          licenseGUID,
                                          licenseGUIDParameterName,
                                          OpenMetadataAPIMapper.LICENSE_OF_REFERENCEABLE_TYPE_NAME,
                                          isMergeUpdate,
                                          getLicenseProperties(licenseId,
                                                               start,
                                                               end,
                                                               conditions,
                                                               licensedBy,
                                                               licensedByTypeName,
                                                               licensedByPropertyName,
                                                               custodian,
                                                               custodianTypeName,
                                                               custodianPropertyName,
                                                               licensee,
                                                               licenseeTypeName,
                                                               licenseePropertyName,
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
     * Set up the properties for a license.
     *
     * @param licenseId  unique identifier of the license (maybe from an external system)
     * @param start when did the license start
     * @param end when will the license end
     * @param conditions any conditions added to the license
     * @param licensedBy unique name/identifier of the element for the person/organization licensing the element
     * @param licensedByTypeName type of the licensedBy element
     * @param licensedByPropertyName property name for the unique identifier from the licensedBy element
     * @param custodian unique name/identifier of the element for the person/organization responsible for maintaining the license status
     * @param custodianTypeName type of the custodian element
     * @param custodianPropertyName property name for the unique identifier from the custodian element
     * @param licensee unique name/identifier of the element for the person/organization receiving the license
     * @param licenseeTypeName type of the licensee element
     * @param licenseePropertyName property name for the unique identifier from the licensee element
     * @param notes additional information, endorsements etc
     * @param effectiveFrom starting time for this relationship (null for all time)
     * @param effectiveTo ending time for this relationship (null for all time)
     * @param methodName calling method
     * @return instance properties
     */
    private InstanceProperties getLicenseProperties(String  licenseId,
                                                    Date    start,
                                                    Date    end,
                                                    String  conditions,
                                                    String  licensedBy,
                                                    String  licensedByTypeName,
                                                    String  licensedByPropertyName,
                                                    String  custodian,
                                                    String  custodianTypeName,
                                                    String  custodianPropertyName,
                                                    String  licensee,
                                                    String  licenseeTypeName,
                                                    String  licenseePropertyName,
                                                    String  notes,
                                                    Date    effectiveFrom,
                                                    Date    effectiveTo,
                                                    String  methodName)
    {
        InstanceProperties properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                     null,
                                                                                     OpenMetadataAPIMapper.LICENSE_GUID_PROPERTY_NAME,
                                                                                     licenseId,
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
                                                                  OpenMetadataAPIMapper.LICENSED_BY_PROPERTY_NAME,
                                                                  licensedBy,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataAPIMapper.LICENSED_BY_TYPE_NAME_PROPERTY_NAME,
                                                                  licensedByTypeName,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataAPIMapper.LICENSED_BY_PROPERTY_NAME_PROPERTY_NAME,
                                                                  licensedByPropertyName,
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
                                                                  OpenMetadataAPIMapper.LICENSEE_PROPERTY_NAME,
                                                                  licensee,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataAPIMapper.LICENSEE_TYPE_NAME_PROPERTY_NAME,
                                                                  licenseeTypeName,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataAPIMapper.LICENSEE_PROPERTY_NAME_PROPERTY_NAME,
                                                                  licenseePropertyName,
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
     * @param licenseGUID unique identifier of the license relationship
     * @param licenseGUIDParameterName parameter supplying licenseGUID
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void unlicenseElement(String  userId,
                                 String  externalSourceGUID,
                                 String  externalSourceName,
                                 String  licenseGUID,
                                 String  licenseGUIDParameterName,
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
                                licenseGUID,
                                licenseGUIDParameterName,
                                OpenMetadataAPIMapper.LICENSE_OF_REFERENCEABLE_TYPE_NAME,
                                forLineage,
                                forDuplicateProcessing,
                                effectiveTime,
                                methodName);
    }
}