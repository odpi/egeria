/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.eventbus.topic.kafka;

import java.util.Properties;

import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.OMRSConnectorErrorException;

/**
 * Configuration for the {@link KafkaOpenMetadataEventConsumerConfiguration}
 * 
 *
 */
public class KafkaOpenMetadataEventConsumerConfiguration {
	
	private final Properties properties;
	
	
	public KafkaOpenMetadataEventConsumerConfiguration(Properties properties) {
		this.properties = properties;
	}


	/**
	 * Gets the value of property whose value is an integer
	 * 
	 * @param property
	 * @return
	 */
	public int getIntProperty(KafkaOpenMetadataEventConsumerProperty property) {
		return Integer.parseInt(getProperty(property));		
	}
	
	
	/**
	 * Gets the value of a property whose value is a long integer
	 * 
	 * @param property
	 * @return
	 */
	public long getLongProperty(KafkaOpenMetadataEventConsumerProperty property) {
		return Long.parseLong(getProperty(property));
		
	}
	
	/**
	 * Gets the value of a property whose value is a String.
	 * 
	 * @param property
	 * @return
	 */
	public String getProperty(KafkaOpenMetadataEventConsumerProperty property) {
		String value = properties.getProperty(property.getPropertyName(), property.getDefaultValue());
		if (value == null || value.trim().length() == 0) {
				

			 OMRSErrorCode errorCode = OMRSErrorCode.LOCAL_REPOSITORY_CONFIGURATION_ERROR;
			 String errorMessage = "The required connector property " + property.getPropertyName() + " is not set";
			 throw new OMRSConnectorErrorException(errorCode.getHTTPErrorCode(),
	                                                  this.getClass().getName(),
	                                                  "Please add the property " + property.getPropertyName() + " the connector configuration",
	                                                  errorMessage,
	                                                  errorCode.getSystemAction(),
	                                                  errorCode.getUserAction());

		}
		return value;
		
	}
	
}
