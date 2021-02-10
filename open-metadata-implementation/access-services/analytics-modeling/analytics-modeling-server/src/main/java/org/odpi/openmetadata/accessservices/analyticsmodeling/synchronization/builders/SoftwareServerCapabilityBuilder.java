/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.builders;

import java.util.Map;

import org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.IdMap;
import org.odpi.openmetadata.accessservices.analyticsmodeling.utils.QualifiedNameUtils;
import org.odpi.openmetadata.commonservices.generichandlers.ReferenceableBuilder;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

public class SoftwareServerCapabilityBuilder extends ReferenceableBuilder {

	private String source;

	/**
	 * Constructor supporting all properties.
	 *
	 * @param source          	   new value for the name.
	 * @param additionalProperties additional properties
	 * @param extendedProperties   properties from the subtype.
	 * @param repositoryHelper     helper methods
	 * @param serviceName          name of this OMAS
	 * @param serverName           name of local server
	 */
	public SoftwareServerCapabilityBuilder(
						String source,
						Map<String, String> additionalProperties,
						Map<String, Object> extendedProperties,
						OMRSRepositoryHelper repositoryHelper,
						String serviceName,
						String serverName) {
		super( QualifiedNameUtils.buildQualifiedName(null, IdMap.CAPABILITY_TYPE_NAME, source),
				additionalProperties, IdMap.CAPABILITY_TYPE_GUID,
				IdMap.CAPABILITY_TYPE_NAME, extendedProperties, InstanceStatus.ACTIVE, repositoryHelper,
				serviceName, serverName);

		this.source = source;
	}

	/**
	 * Return the supplied bean properties in an InstanceProperties object.
	 *
	 * @param methodName name of the calling method
	 * @return InstanceProperties object
	 * @throws InvalidParameterException there is a problem with the properties
	 */
	public InstanceProperties getInstanceProperties(
			String methodName) throws InvalidParameterException {
		InstanceProperties properties = super.getInstanceProperties(methodName);

		if (source != null) {
			properties = repositoryHelper.addStringPropertyToInstance(serviceName, properties,
					"source", source, methodName);
		}
		
		return properties;
	}

}
