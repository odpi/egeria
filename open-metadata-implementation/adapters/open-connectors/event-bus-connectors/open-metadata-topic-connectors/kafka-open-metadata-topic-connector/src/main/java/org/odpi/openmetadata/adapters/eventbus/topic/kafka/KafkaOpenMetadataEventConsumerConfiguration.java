/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.eventbus.topic.kafka;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;

import java.util.Properties;


/**
 * Configuration for the {@link KafkaOpenMetadataEventConsumerConfiguration}
 * 
 *
 */
public class KafkaOpenMetadataEventConsumerConfiguration
{
	private final Properties properties;
	private final AuditLog   auditLog;

	KafkaOpenMetadataEventConsumerConfiguration(Properties properties,
												AuditLog   auditLog)
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
	int getIntProperty(KafkaOpenMetadataEventConsumerProperty property)
	{
		return Integer.parseInt(getProperty(property));		
	}
	
	
	/**
	 * Gets the value of a property whose value is a long integer
	 * 
	 * @param property property object
	 * @return property value
	 */
	long getLongProperty(KafkaOpenMetadataEventConsumerProperty property)
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

			auditLog.logMessage(actionDescription,
								KafkaOpenMetadataTopicConnectorAuditCode.MISSING_PROPERTY.getMessageDefinition(property.getPropertyName()));

			return "0";
		}

		return value;
	}
}
