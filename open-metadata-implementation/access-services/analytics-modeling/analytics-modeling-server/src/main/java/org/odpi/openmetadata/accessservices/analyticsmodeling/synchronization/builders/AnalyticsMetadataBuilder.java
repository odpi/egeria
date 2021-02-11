/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.builders;

import java.util.Map;

import org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.IdMap;
import org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.model.AnalyticsMetadata;
import org.odpi.openmetadata.commonservices.generichandlers.SchemaAttributeBuilder;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

public class AnalyticsMetadataBuilder extends SchemaAttributeBuilder {

	/**
	 * Constructor supporting all properties.
	 *
	 * @param src					source of the properties	
	 * @param qualifiedName			of the attribute
	 * @param displayName			of the attribute
	 * @param description			of the attribute
	 * @param position				of the attribute in schema parent object
	 * @param additionalProperties	additional properties
	 * @param extendedProperties	properties from the subtype.
	 * @param repositoryHelper		helper methods
	 * @param serviceName			name of this OMAS
	 * @param serverName			name of local server
	 */
	public AnalyticsMetadataBuilder(
			AnalyticsMetadata src,
			String qualifiedName,
			String displayName,
			String description,
			int position,
			Map<String, String> additionalProperties,
			Map<String, Object> extendedProperties,
			OMRSRepositoryHelper repositoryHelper,
			String serviceName,
			String serverName) 
	{
		super(qualifiedName, displayName, description, position,
                0,	// minCardinality,
                0,	// maxCardinality,
                false, // isDeprecated,
                null, 	// defaultValueOverride,
                false, // allowsDuplicateValues,
                false, // orderedValues,
                0, // sortOrder,
                0, // minimumLength,
                0, // length,
                0, // significantDigits,
                false, // isNullable,
                src.getClass().getName(), // nativeJavaClass,
                null, // aliases,
                additionalProperties,
                IdMap.SCHEMA_ATTRIBUTE_TYPE_GUID,
                IdMap.SCHEMA_ATTRIBUTE_TYPE_NAME,
                extendedProperties,
                repositoryHelper,
                serviceName,
                serverName);
	}

	/**
	 * Return the supplied bean properties in an InstanceProperties object.
	 *
	 * @param methodName name of the calling method
	 * @return InstanceProperties object
	 * @throws InvalidParameterException there is a problem with the properties
	 */
	public InstanceProperties getInstanceProperties(String methodName) throws InvalidParameterException {
		
		InstanceProperties properties = super.getInstanceProperties(methodName);

		return properties;
	}

}
