/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.converters;

import org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.beans.SchemaType;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIGenericConverter;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

public class SchemaTypeConverter extends OpenMetadataAPIGenericConverter<SchemaType> {

	public SchemaTypeConverter(OMRSRepositoryHelper repositoryHelper, String serviceName, String serverName) {
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
    public SchemaType getNewBean(Class<SchemaType>     beanClass,
                        EntityDetail entity,
                        String       methodName) throws PropertyServerException
    {
        if (entity == null) {
            handleMissingMetadataInstance(beanClass.getName(), TypeDefCategory.ENTITY_DEF, methodName);
        }
        
        SchemaType bean = new SchemaType();
		InstanceProperties instanceProperties = entity.getProperties();
		if (instanceProperties != null) {
			bean.setDisplayName(this.removeDisplayName(instanceProperties));
			bean.setDescription(this.removeDescription(instanceProperties));
			bean.setQualifiedName(this.removeQualifiedName(instanceProperties));
			bean.setAdditionalProperties(this.removeAdditionalProperties(instanceProperties));
		}

        return bean;
    }
}
