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
import java.util.List;

/**
 * CertificationHandler manages Certification objects.  It runs server-side in
 * the OMAG Server Platform and retrieves Certification entities through the OMRSRepositoryConnector.
 */
public class CertificationHandler<B> extends ReferenceableHandler<B>
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
     * Count the number of Certifications attached to an anchor entity.
     *
     * @param userId     calling user
     * @param anchorGUID identifier for the entity that the object is attached to
     * @param methodName calling method
     * @return count of attached objects
     * @throws InvalidParameterException  the parameters are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public int countCertifications(String   userId,
                                   String   anchorGUID,
                                   String   methodName) throws InvalidParameterException,
                                                               PropertyServerException,
                                                               UserNotAuthorizedException
    {
        return super.countAttachments(userId,
                                      anchorGUID,
                                      OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                      OpenMetadataAPIMapper.CERTIFICATION_OF_REFERENCEABLE_TYPE_GUID,
                                      OpenMetadataAPIMapper.CERTIFICATION_OF_REFERENCEABLE_TYPE_NAME,
                                      methodName);
    }


    /**
     * Return the Certifications attached to an anchor entity.
     *
     * @param userId     calling user
     * @param parentGUID identifier for the entity that the feedback is attached to
     * @param parentGUIDParameterName parameter name for the parentGUID
     * @param startFrom where to start from in the list
     * @param pageSize maximum number of results that can be returned
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
                                      String       methodName) throws InvalidParameterException,
                                                                      PropertyServerException,
                                                                      UserNotAuthorizedException
    {
        return this.getCertifications(userId, parentGUID, parentGUIDParameterName, supportedZones, startFrom, pageSize, methodName);
    }



    /**
     * Return the Certifications attached to an anchor entity.
     *
     * @param userId     calling user
     * @param parentGUID identifier for the entity that the feedback is attached to
     * @param parentGUIDParameterName parameter name for the parentGUID
     * @param serviceSupportedZones supported zones for calling service
     * @param startFrom where to start from in the list
     * @param pageSize maximum number of results that can be returned
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
                                                                    OpenMetadataAPIMapper.CERTIFICATION_TYPE_TYPE_NAME,
                                                                    startFrom,
                                                                    pageSize,
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
                                                                           serviceSupportedZones,
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
