/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization;

import java.util.List;

import org.odpi.openmetadata.accessservices.analyticsmodeling.ffdc.AnalyticsModelingErrorCode;
import org.odpi.openmetadata.accessservices.analyticsmodeling.ffdc.exceptions.AnalyticsModelingCheckedException;
import org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.converters.SoftwareServerCapabilityConverter;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.SoftwareServerCapability;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

/**
 * Context container class to hold all parts involved in execution.
 * It makes code cleaner to pass less parameters where they needed. 
 */
public class ExecutionContext {
	private String userId;
	private String serviceName;
	private String serverName;
	private InvalidParameterHandler invalidParameterHandler;
	private RepositoryHandler repositoryHandler;
	private OMRSRepositoryHelper repositoryHelper;
	private String localServerUserId;
	private OpenMetadataServerSecurityVerifier securityVerifier;
	private List<String> supportedZones;
	private List<String> defaultZones;
	private List<String> publishZones;
	private AuditLog auditLog;
	
	private SoftwareServerCapabilityHandler softwareServerCapabilityHandler;
	private SoftwareServerCapability softwareServerCapability;

	
	public ExecutionContext(
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
			AuditLog auditLog)
	{
		this.serviceName = serviceName;
		this.serverName = serverName;
		this.invalidParameterHandler = invalidParameterHandler;
		this.repositoryHandler = repositoryHandler;
		this.repositoryHelper = repositoryHelper;
		this.localServerUserId = localServerUserId;
		this.securityVerifier = securityVerifier;
		this.supportedZones = supportedZones;
		this.defaultZones = defaultZones;
		this.publishZones = publishZones;
		this.auditLog = auditLog;
	}
	
	public void initializeSoftwareServerCapability(String userId, String softwareServerCapabilityName)
			throws AnalyticsModelingCheckedException
	{
		
		String methodName = "InitializeExecutionContext";
		
		this.userId = userId;

		softwareServerCapabilityHandler = new SoftwareServerCapabilityHandler(
				new SoftwareServerCapabilityConverter(repositoryHelper, serviceName, serverName),
				 serviceName, serverName, invalidParameterHandler, repositoryHandler, repositoryHelper,
				localServerUserId, securityVerifier, supportedZones, defaultZones, publishZones, auditLog);
		
		try {
			softwareServerCapability = softwareServerCapabilityHandler
					.findSoftwareServerCapability(userId, softwareServerCapabilityName, methodName);
			
			if (softwareServerCapability == null) {
				String guid = softwareServerCapabilityHandler.createSoftwareServerCapability(userId, softwareServerCapabilityName, methodName);
				softwareServerCapability = softwareServerCapabilityHandler.getSoftwareServerCapability(userId, guid, "guid", methodName);
			}

		} catch (InvalidParameterException | PropertyServerException | UserNotAuthorizedException ex) {
			throw new AnalyticsModelingCheckedException(
					AnalyticsModelingErrorCode.FAILED_CREATE_SERVER_CAPABILITY.getMessageDefinition(userId, 
							softwareServerCapabilityName, ex.getLocalizedMessage()),
					this.getClass().getSimpleName(),
					methodName,
					ex);
			
		}
	}

	
	public SoftwareServerCapability getServerSoftwareCapability() {
		return softwareServerCapability;
	}
	
	public SoftwareServerCapabilityHandler getServerSoftwareCapabilityHandler() {
		return softwareServerCapabilityHandler;
	}
	
	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * Set the userId.
	 * @param value to set.
	 */
	public void setUserId(String value) {
		userId = value;
	}
	/**
	 * Get service name.
	 * @return the serviceName
	 */
	public String getServiceName() {
		return serviceName;
	}
	/**
	 * Get server name.
	 * @return the serverName
	 */
	public String getServerName() {
		return serverName;
	}
	/**
	 * Get invalid parameter handler.
	 * @return the invalidParameterHandler
	 */
	public InvalidParameterHandler getInvalidParameterHandler() {
		return invalidParameterHandler;
	}
	/**
	 * Get repository handler.
	 * @return the repositoryHandler
	 */
	public RepositoryHandler getRepositoryHandler() {
		return repositoryHandler;
	}
	/**
	 * Get repository helper.
	 * @return the repositoryHelper
	 */
	public OMRSRepositoryHelper getRepositoryHelper() {
		return repositoryHelper;
	}
	/**
	 * @return the localServerUserId
	 */
	public String getLocalServerUserId() {
		return localServerUserId;
	}
	/**
	 * @return the securityVerifier
	 */
	public OpenMetadataServerSecurityVerifier getSecurityVerifier() {
		return securityVerifier;
	}
	/**
	 * @return the supportedZones
	 */
	public List<String> getSupportedZones() {
		return supportedZones;
	}
	/**
	 * @return the defaultZones
	 */
	public List<String> getDefaultZones() {
		return defaultZones;
	}
	/**
	 * @return the publishZones
	 */
	public List<String> getPublishZones() {
		return publishZones;
	}
	/**
	 * @return the auditLog
	 */
	public AuditLog getAuditLog() {
		return auditLog;
	}
	
	/**
	 * Helper function to get string property from property collection of the entity.
	 * @param propName property name to retrieve.
	 * @param properties collection.
	 * @param methodName requested the operation.
	 * @return property value.
	 */
	public String getStringProperty(String propName, InstanceProperties properties, String methodName)
	{
		return repositoryHelper.getStringProperty(serviceName, propName, properties, methodName);
	}
}
