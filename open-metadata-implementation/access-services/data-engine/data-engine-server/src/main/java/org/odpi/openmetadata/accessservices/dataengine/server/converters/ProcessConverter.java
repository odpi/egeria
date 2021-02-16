/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.converters;

import org.odpi.openmetadata.accessservices.dataengine.model.Process;
import org.odpi.openmetadata.accessservices.dataengine.server.mappers.ProcessPropertiesMapper;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIGenericConverter;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EnumPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstancePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Map;


/**
 * ProcessConverter transfers the relevant properties from an Open Metadata Repository Services (OMRS)
 * EntityDetail object into a Process bean.
 */
public class ProcessConverter<B> extends OpenMetadataAPIGenericConverter<B> {

    private static final int OWNER_TYPE_OTHER = 99;

    public ProcessConverter(OMRSRepositoryHelper repositoryHelper, String serviceName, String serverName) {
        super(repositoryHelper, serviceName, serverName);
    }

    /**
     * Request the bean is extracted from the repository objects
     * @param processClass the process class
     * @param entity the EntityDetail from which the bean is extracted
     * @throws PropertyServerException problem accessing the property server
     *
     * @return output bean
     */
    public Process getProcessBean(Class<B> processClass, EntityDetail entity) throws PropertyServerException {
        final String methodName = "updateBean";

        Process process = null;
        try {
            process = (Process) processClass.newInstance();

        if (entity != null) {
            InstanceType type = entity.getType();

            process.setTypeGUID(type.getTypeDefGUID());
            process.setTypeName(type.getTypeDefName());
            process.setGUID(entity.getGUID());

            InstanceProperties entityProperties = entity.getProperties();
            if(entityProperties != null) {

                InstanceProperties instanceProperties = new InstanceProperties(entityProperties);

                process.setName(removeName(instanceProperties));
                process.setQualifiedName(removeQualifiedName(instanceProperties));
                process.setDisplayName(removeDisplayName(instanceProperties));
                process.setDescription(removeDescription(instanceProperties));

                process.setOwner(removeOwner(instanceProperties));
                process.setOwnerType(removeOwnerTypeFromProperties(instanceProperties));
                process.setZoneMembership(removeZoneMembership(instanceProperties));

                process.setAdditionalProperties(removeAdditionalProperties(instanceProperties));
                process.setExtendedProperties(getRemainingExtendedProperties(instanceProperties));

                process.setFormula(repositoryHelper.removeStringProperty(serviceName,
                        ProcessPropertiesMapper.FORMULA_PROPERTY_NAME, instanceProperties, methodName));
            }
        }

        } catch (IllegalAccessException | InstantiationException | ClassCastException error) {
            super.handleInvalidBeanClass(process.getName(), error, methodName);
        }

        return process;
    }

    int removeOwnerTypeFromProperties(InstanceProperties properties) {
        int ownerType = this.getOwnerTypeFromProperties(properties);

        if (properties != null) {
            Map<String, InstancePropertyValue> instancePropertiesMap = properties.getInstanceProperties();

            if (instancePropertiesMap != null) {
                instancePropertiesMap.remove(OpenMetadataAPIMapper.OWNER_TYPE_PROPERTY_NAME);
            }
            properties.setInstanceProperties(instancePropertiesMap);
        }
        return ownerType;
    }

    int getOwnerTypeFromProperties(InstanceProperties properties) {
        int ownerType = OWNER_TYPE_OTHER;

        if (properties != null) {
            Map<String, InstancePropertyValue> instancePropertiesMap = properties.getInstanceProperties();

            if (instancePropertiesMap != null) {
                InstancePropertyValue instancePropertyValue = instancePropertiesMap.get(OpenMetadataAPIMapper.OWNER_TYPE_PROPERTY_NAME);

                if (instancePropertyValue instanceof EnumPropertyValue) {
                    EnumPropertyValue enumPropertyValue = (EnumPropertyValue) instancePropertyValue;

                    ownerType = enumPropertyValue.getOrdinal();
                }
            }
        }
        return ownerType;
    }
}
