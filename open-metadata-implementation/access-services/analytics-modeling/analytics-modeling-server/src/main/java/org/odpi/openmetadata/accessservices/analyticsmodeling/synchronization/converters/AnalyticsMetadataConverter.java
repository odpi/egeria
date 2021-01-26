/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.converters;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.beans.SchemaAttribute;
import org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.model.AnalyticsMetadata;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIGenericConverter;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

public class AnalyticsMetadataConverter extends OpenMetadataAPIGenericConverter<SchemaAttribute> {

	public AnalyticsMetadataConverter(OMRSRepositoryHelper repositoryHelper, String serviceName, String serverName) {
		super(repositoryHelper, serviceName, serverName);
	}
	
    /**
     * Using the supplied entity, return a new instance of the bean. This is used for beans that have
     * contain a combination of the properties from an entity.
     *
     * @param beanClass name of the class to create
     * @param entity entity containing the properties
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    @Override
    public AnalyticsMetadata getNewBean(Class<SchemaAttribute>     beanClass,
                        EntityDetail entity,
                        String       methodName) throws PropertyServerException
    {
        if (entity == null) {
            handleMissingMetadataInstance(beanClass.getName(), TypeDefCategory.ENTITY_DEF, methodName);
        }
        
		InstanceProperties instanceProperties = entity.getProperties();
		if (instanceProperties != null) {
			String nativeClass = this.removeNativeClass(instanceProperties);
			
			if (nativeClass == null) {
				// error
			}
			
			Object object = null;
			try {
			
				Class<?> c = Class.forName(nativeClass);
				Constructor<?> cons = c.getConstructor();
				object = cons.newInstance();

			} catch (ClassNotFoundException // forName
					| NoSuchMethodException | SecurityException // getConstructor
					| InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				// error
			}
			
			if (!(object instanceof AnalyticsMetadata)) {
				// error
			}
			
			AnalyticsMetadata bean = (AnalyticsMetadata) object;
			
			bean.convertProperties();
			
			bean.setDisplayName(this.removeDisplayName(instanceProperties));
			bean.setDescription(this.removeDescription(instanceProperties));
			bean.setQualifiedName(this.removeQualifiedName(instanceProperties));
			bean.setAdditionalProperties(this.removeAdditionalProperties(instanceProperties));
			bean.setElementPosition(this.getPosition(instanceProperties));
			
			return bean;
		}

		// error or empty SchemaAttribute ?
        return null;
    }
}
