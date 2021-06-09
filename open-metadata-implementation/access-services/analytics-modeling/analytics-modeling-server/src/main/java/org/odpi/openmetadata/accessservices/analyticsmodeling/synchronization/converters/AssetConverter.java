/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.converters;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.model.AnalyticsAsset;
import org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.model.AssetReference;
import org.odpi.openmetadata.accessservices.analyticsmodeling.utils.Constants;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIGenericConverter;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AssetConverter extends OpenMetadataAPIGenericConverter<AnalyticsAsset> {

	public AssetConverter(OMRSRepositoryHelper repositoryHelper, String serviceName, String serverName) {
		super(repositoryHelper, serviceName, serverName);
	}
	
    /**
     * Using the supplied instances, return a new instance of the bean. This is used for beans that have
     * contain a combination of the properties from an entity.
     *
     * @param beanClass name of the class to create
     * @param entity entity containing the properties
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    @Override
    public AnalyticsAsset getNewBean(Class<AnalyticsAsset>     beanClass,
                        EntityDetail entity,
                        String       methodName) throws PropertyServerException
    {
        if (entity == null) {
            handleMissingMetadataInstance(beanClass.getName(), TypeDefCategory.ENTITY_DEF, methodName);
        }
        
        AnalyticsAsset bean = new AnalyticsAsset();
		InstanceProperties instanceProperties = entity.getProperties();
		bean.setGuid(entity.getGUID());
		if (instanceProperties != null) {
			bean.setDisplayName(this.removeName(instanceProperties));
			bean.setDescription(this.removeDescription(instanceProperties));
			bean.setQualifiedName(this.removeQualifiedName(instanceProperties));
			Map<String, String> props = this.removeAdditionalProperties(instanceProperties);
			bean.setAdditionalProperties(props);
			
			if (props != null) {
				bean.setUid(props.get(Constants.UID));
				bean.setType(props.get(Constants.TYPE));
				bean.setLocation(props.get(Constants.LOCATION));
				bean.setLastModified(props.get(Constants.LASTMODIFIED));
				String jsonReference = props.get(Constants.REFERENCE);
				if (jsonReference != null && !jsonReference.isEmpty()) {
					try {
						bean.setReference(new ObjectMapper()
								.readValue(jsonReference.getBytes(), new TypeReference<List<AssetReference>>(){}));
					} catch (IOException e) {
						// log warning into execution context
					}
				} else {
					bean.setReference(null);
				}
			}
		}

        return bean;
    }
}
