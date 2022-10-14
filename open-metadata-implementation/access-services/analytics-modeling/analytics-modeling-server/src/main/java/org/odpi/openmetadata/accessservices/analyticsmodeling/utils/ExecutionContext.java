/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.analyticsmodeling.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.odpi.openmetadata.accessservices.analyticsmodeling.ffdc.AnalyticsModelingErrorCode;
import org.odpi.openmetadata.accessservices.analyticsmodeling.ffdc.exceptions.AnalyticsModelingCheckedException;
import org.odpi.openmetadata.accessservices.analyticsmodeling.model.response.Messages;
import org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.SoftwareServerCapabilityHandler;
import org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.converters.SoftwareServerCapabilityConverter;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.SoftwareCapability;
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
	private SoftwareCapability              softwareServerCapability;
	
	private Messages messages = new Messages();
	private List<String> tablesWithoutColumns = new ArrayList<>();
	private Map<String, List<String>> tableColumnsIgnored = new TreeMap<>();

	
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
	
	public void initialize(String userId)
	{
		messages = new Messages();
		this.userId = userId;
	}

	/**
	 * Get software server capability by GUID, name, or create.
	 * @param userId of the request.
	 * @param softwareServerCapabilityName to get.
	 * @param softwareServerCapabilityGUID to get.
	 * @throws AnalyticsModelingCheckedException in case of an errors.
	 */
	public void initializeSoftwareServerCapability(String userId, String softwareServerCapabilityName, String softwareServerCapabilityGUID)
			throws AnalyticsModelingCheckedException
	{
		
		String methodName = "InitializeExecutionContext";
		
		initialize(userId);

		softwareServerCapabilityHandler = new SoftwareServerCapabilityHandler(
				new SoftwareServerCapabilityConverter(repositoryHelper, serviceName, serverName),
				 serviceName, serverName, invalidParameterHandler, repositoryHandler, repositoryHelper,
				localServerUserId, securityVerifier, supportedZones, defaultZones, publishZones, auditLog);
		
		// first try to use GUID
		try {
			if (softwareServerCapabilityGUID != null) {
				softwareServerCapability = softwareServerCapabilityHandler
						.getSoftwareServerCapability(userId, softwareServerCapabilityGUID, "guid", methodName);
			}
		} catch (InvalidParameterException | PropertyServerException | UserNotAuthorizedException ex) {
			throw new AnalyticsModelingCheckedException(
					AnalyticsModelingErrorCode.FAILED_GET_SERVER_CAPABILITY_GUID.getMessageDefinition(userId, 
							softwareServerCapabilityName, ex.getLocalizedMessage()),
					this.getClass().getSimpleName(),
					methodName,
					ex);
		}
			
		try {
			if (softwareServerCapability == null) {
				softwareServerCapability = softwareServerCapabilityHandler
						.findSoftwareServerCapability(userId, softwareServerCapabilityName, methodName);
			}
		} catch (InvalidParameterException | PropertyServerException | UserNotAuthorizedException ex) {
			throw new AnalyticsModelingCheckedException(
					AnalyticsModelingErrorCode.FAILED_GET_SERVER_CAPABILITY_NAME.getMessageDefinition(userId, 
							softwareServerCapabilityName, ex.getLocalizedMessage()),
					this.getClass().getSimpleName(),
					methodName,
					ex);
		}

		try {
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

	
	public SoftwareCapability getServerSoftwareCapability() {
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

	public synchronized void addTableWithoutColumns(String name) {
		tablesWithoutColumns.add(name);
	}
	
	public synchronized void addIgnoredTableColumn(String nameTable, String nameColumn) {
		List<String> columns = tableColumnsIgnored.get(nameTable);
		if (columns == null) {
			columns = new ArrayList<>();
			tableColumnsIgnored.put(nameTable, columns);
		}
		columns.add(nameColumn);
	}
	
	public Messages getMessages() {
		
		if (!tablesWithoutColumns.isEmpty()) {
			ExceptionMessageDefinition def;
			if (tablesWithoutColumns.size() == 1) {
				def = AnalyticsModelingErrorCode.WARNING_TABLE_NO_COLUMNS.getMessageDefinition(tablesWithoutColumns.get(0));
			} else {
				tablesWithoutColumns.sort(null);	// sort by names helps to find table in question and preserves order. 
				def = AnalyticsModelingErrorCode.WARNING_TABLES_NO_COLUMNS.getMessageDefinition(String.join(", ", tablesWithoutColumns));
			}
			messages.addWarning(def);
			tablesWithoutColumns.clear();
		}
		
		return (messages.getMessages() == null || messages.getMessages().isEmpty()) ? null : messages;
	}

	public void addMessage(ExceptionMessageDefinition message, String detail) {
		messages.addWarning(message, detail);
	}
	
	public void addMessage(ExceptionMessageDefinition message) {
		messages.addWarning(message);
	}
	
}
