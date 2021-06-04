/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.builders;

import java.util.Map;

import org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.ExecutionContext;
import org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.IdMap;
import org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.model.AnalyticsMetadata;
import org.odpi.openmetadata.commonservices.generichandlers.SchemaAttributeBuilder;

public class AnalyticsMetadataBuilder extends SchemaAttributeBuilder {

	/**
	 * Constructor initialized from source object and .
	 *
	 * @param src					source of the properties	
	 * @param extendedProperties	properties from the subtype.
	 * @param ctx					execution context to access repository
	 */
	public AnalyticsMetadataBuilder(
			AnalyticsMetadata src,
			Map<String, Object> extendedProperties,
			ExecutionContext ctx) 
	{
		super(src.getQualifiedName(), src.getDisplayName(), src.getDescription(), src.getElementPosition(),
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
                src.getAdditionalProperties(),
                IdMap.SCHEMA_ATTRIBUTE_TYPE_GUID,
                IdMap.SCHEMA_ATTRIBUTE_TYPE_NAME,
                extendedProperties,
                ctx.getRepositoryHelper(),
                ctx.getServiceName(),
                ctx.getServerName());
	}
}
