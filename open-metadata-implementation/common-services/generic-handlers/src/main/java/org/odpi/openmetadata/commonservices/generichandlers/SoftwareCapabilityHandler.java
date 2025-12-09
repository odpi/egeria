/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Date;
import java.util.Map;

/**
 * SoftwareCapabilityHandler provides the support for managing software capabilities.
 */
public class SoftwareCapabilityHandler<B> extends ReferenceableHandler<B>
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
     * @param auditLog destination for audit log events.
     */
    public SoftwareCapabilityHandler(OpenMetadataAPIGenericConverter<B> converter,
                                     Class<B>                           beanClass,
                                     String                             serviceName,
                                     String                             serverName,
                                     InvalidParameterHandler            invalidParameterHandler,
                                     RepositoryHandler                  repositoryHandler,
                                     OMRSRepositoryHelper               repositoryHelper,
                                     String                             localServerUserId,
                                     OpenMetadataServerSecurityVerifier securityVerifier,
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
              auditLog);
    }


    /**
     * Files live on a file system.  This method creates a top level anchor for a file system.
     * It has its own method because ot the extra properties in the FileSystem classification
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param uniqueName qualified name for the file system
     * @param displayName short display name
     * @param description description of the file system
     * @param type type of file system
     * @param version version of file system
     * @param patchLevel patchLevel of software supporting the file system
     * @param source supplier of the software for this file system
     * @param format format of files on this file system
     * @param encryption encryption type - null for unencrypted
     * @param additionalProperties additional properties
     * @param vendorProperties  properties about the vendor and/or their product
     * @param effectiveFrom starting time for this element (null for all time)
     * @param effectiveTo ending time for this element (null for all time)
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return unique identifier for the file system
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public String   createFileSystem(String               userId,
                                     String               externalSourceGUID,
                                     String               externalSourceName,
                                     String               uniqueName,
                                     String               displayName,
                                     String               description,
                                     String               type,
                                     String               version,
                                     String               patchLevel,
                                     String               source,
                                     String               format,
                                     String               encryption,
                                     Map<String, String>  additionalProperties,
                                     Map<String, String>  vendorProperties,
                                     Date                 effectiveFrom,
                                     Date                 effectiveTo,
                                     boolean              forLineage,
                                     boolean              forDuplicateProcessing,
                                     Date                 effectiveTime,
                                     String               methodName) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        SoftwareCapabilityBuilder builder = new SoftwareCapabilityBuilder(uniqueName,
                                                                          displayName,
                                                                          description,
                                                                          type,
                                                                          version,
                                                                          patchLevel,
                                                                          source,
                                                                          additionalProperties,
                                                                          OpenMetadataType.FILE_SYSTEM.typeGUID,
                                                                          OpenMetadataType.FILE_SYSTEM.typeName,
                                                                          null,
                                                                          repositoryHelper,
                                                                          serviceName,
                                                                          serverName);

        builder.setEffectivityDates(effectiveFrom, effectiveTo);

        builder.setFileSystemProperties(format, encryption);

        String fileSystemGUID = this.createBeanInRepository(userId,
                                                            externalSourceGUID,
                                                            externalSourceName,
                                                            OpenMetadataType.FILE_SYSTEM.typeGUID,
                                                            OpenMetadataType.FILE_SYSTEM.typeName,
                                                            OpenMetadataType.SOFTWARE_CAPABILITY.typeName,
                                                            null,
                                                            builder,
                                                            true,
                                                            effectiveTime,
                                                            methodName);

        if (fileSystemGUID != null)
        {
            this.setVendorProperties(userId, fileSystemGUID, vendorProperties, forLineage, forDuplicateProcessing, effectiveTime, methodName);
        }

        return fileSystemGUID;
    }
}
