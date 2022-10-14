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
}
