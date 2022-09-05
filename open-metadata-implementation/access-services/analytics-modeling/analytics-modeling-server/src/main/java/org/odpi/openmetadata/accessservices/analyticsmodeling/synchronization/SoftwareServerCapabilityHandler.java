/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.builders.SoftwareServerCapabilityBuilder;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIGenericConverter;
import org.odpi.openmetadata.commonservices.generichandlers.ReferenceableHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.SoftwareCapability;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;


/**
 *	 Handler for SoftwareCapability object.
 */
public class SoftwareServerCapabilityHandler extends ReferenceableHandler<SoftwareCapability> {
	/**
	 * Construct the collection handler.
	 *
	 * @param converter               specific for SoftwareCapability bean class
	 * @param serviceName             name of this service
	 * @param serverName              name of the local server
	 * @param invalidParameterHandler handler for managing parameter errors
	 * @param repositoryHandler       manages calls to the repository services
	 * @param repositoryHelper        provides utilities for manipulating the repository services objects
	 * @param localServerUserId       userId for this server
	 * @param securityVerifier        open metadata security services verifier
	 * @param supportedZones          list of zones that the access service is allowed to serve Asset instances from.
	 * @param defaultZones            list of zones that the access service should set in all new Asset instances.
	 * @param publishZones            list of zones that the access service sets up in published Asset instances.
	 * @param auditLog                destination for audit log events.
	 */
	public SoftwareServerCapabilityHandler(	OpenMetadataAPIGenericConverter<SoftwareCapability> converter,
								String serviceName,
								String serverName,
								InvalidParameterHandler invalidParameterHandler,
								RepositoryHandler repositoryHandler,
								OMRSRepositoryHelper repositoryHelper,
								String localServerUserId,
								OpenMetadataServerSecurityVerifier securityVerifier,
								List<String> supportedZones,
								List<String> defaultZones,
								List<String> publishZones,
								AuditLog auditLog) {
		super(converter, SoftwareCapability.class, serviceName, serverName, invalidParameterHandler, repositoryHandler,
              repositoryHelper, localServerUserId, securityVerifier, supportedZones, defaultZones, publishZones,
              auditLog);
	}

	/**
	 * Create a new metadata element to represent a SoftwareCapability.
	 *
	 * @param userId     calling user
	 * @param source 	 identifier of the server capability (url of the service)
	 * @param methodName calling method
	 *
	 * @return unique identifier of the created collection.
	 *
	 * @throws InvalidParameterException  one of the parameters is invalid
	 * @throws UserNotAuthorizedException the user is not authorized to issue this request
	 * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
	 */
	public String createSoftwareServerCapability(
			String userId,
			String source,
			String methodName) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
		invalidParameterHandler.validateUserId(userId, methodName);

		SoftwareServerCapabilityBuilder builder = new SoftwareServerCapabilityBuilder(source, null, null, repositoryHelper, serviceName, serverName);
		
		// create SoftwareCapability
		String guid = createBeanInRepository(userId, null, null, IdMap.CAPABILITY_TYPE_GUID,
				IdMap.CAPABILITY_TYPE_NAME, builder, new Date(), methodName);

		return guid;
	}

	/**
	 * Returns the software server capability object corresponding to the requested identifier.
	 *
	 * @param userId        String - userId of user making request
	 * @param identifier    the identifier for the requested object
	 * @param methodName    calling method
	 *
	 * @return software server capability
	 * @throws InvalidParameterException  one of the properties (probably the GUID) is invalid
	 * @throws PropertyServerException    the repository services hit an unexpected problem
	 * @throws UserNotAuthorizedException the user is not permitted to access this entity
	 */
	public SoftwareCapability findSoftwareServerCapability(
			String userId,
			String identifier,
			String methodName) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException
	{
		return this.getBeanByValue(userId, identifier,
                "identifier",
                IdMap.CAPABILITY_TYPE_GUID,
                IdMap.CAPABILITY_TYPE_NAME,
                Arrays.asList("source"),
                false,
                false,
                new Date(),
                methodName);
	}
	
	/**
	 * Returns the SoftwareCapability object corresponding to the supplied GUID.
	 *
	 * @param userId        String - userId of user making request
	 * @param guid          the unique id for the SoftwareCapability
	 * @param guidParameter name of parameter supplying the guid
	 * @param methodName    calling method
	 *
	 * @return SoftwareCapability retrieved from the repository
	 * @throws InvalidParameterException  one of the properties (probably the GUID) is invalid
	 * @throws PropertyServerException    the repository services hit an unexpected problem
	 * @throws UserNotAuthorizedException the user is not permitted to access this entity
	 */
	public SoftwareCapability getSoftwareServerCapability(
			String userId,
			String guid,
			String guidParameter,
			String methodName) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException
	{
		return this.getBeanFromRepository(userId, guid, guidParameter, IdMap.CAPABILITY_TYPE_NAME, false, false, new Date(),
				methodName);
	}

}
