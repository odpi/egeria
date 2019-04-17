/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.eventbus.topic.kafka;

import java.util.Properties;

import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;

/**
 * Configuration for the {@link KafkaOpenMetadataEventConsumerConfiguration}
 * 
 *
 */
public class KafkaOpenMetadataEventConsumerConfiguration
{
	private final Properties properties;
	private OMRSAuditLog     auditLog;

	public KafkaOpenMetadataEventConsumerConfiguration(Properties    properties,
													   OMRSAuditLog  auditLog)
	{
		this.properties = properties;
		this.auditLog = auditLog;
	}


	/**
	 * Gets the value of property whose value is an integer
	 * 
	 * @param property property object
	 * @return property value
	 */
	public int getIntProperty(KafkaOpenMetadataEventConsumerProperty property)
	{
		return Integer.parseInt(getProperty(property));		
	}
	
	
	/**
	 * Gets the value of a property whose value is a long integer
	 * 
	 * @param property property object
	 * @return property value
	 */
	public long getLongProperty(KafkaOpenMetadataEventConsumerProperty property)
	{
		return Long.parseLong(getProperty(property));
		
	}
	
	/**
	 * Gets the value of a property whose value is a String.
	 * 
	 * @param property property object
	 * @return property value
	 */
	public String getProperty(KafkaOpenMetadataEventConsumerProperty property)
	{
		String value = properties.getProperty(property.getPropertyName(), property.getDefaultValue());

		if (value == null || value.trim().length() == 0)
		{
			final String actionDescription = "getProperty";

			KafkaOpenMetadataTopicConnectorAuditCode auditCode = KafkaOpenMetadataTopicConnectorAuditCode.MISSING_PROPERTY;

			auditLog.logRecord(actionDescription,
							   auditCode.getLogMessageId(),
							   auditCode.getSeverity(),
							   auditCode.getFormattedLogMessage(property.getPropertyName()),
							   null,
							   auditCode.getSystemAction(),
							   auditCode.getUserAction());

			return "0";
		}

		return value;
	}
	
}
