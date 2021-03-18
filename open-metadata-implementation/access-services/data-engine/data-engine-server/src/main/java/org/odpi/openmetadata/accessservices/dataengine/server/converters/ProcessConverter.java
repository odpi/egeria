/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.converters;

import org.odpi.openmetadata.accessservices.dataengine.model.OwnerType;
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
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.List;
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
     *
     * @param processClass the process class
     * @param entity       the EntityDetail from which the bean is extracted
     * @param methodName   the name of the caller method
     *
     * @return output bean
     *
     * @throws PropertyServerException problem accessing the property server
     */
    public B getNewBean(Class<B> processClass, EntityDetail entity, String methodName) throws PropertyServerException {
        B returnBean;
        try {
            returnBean = processClass.newInstance();

            if (returnBean instanceof Process) {
                Process process = (Process) returnBean;


                if (entity != null) {
                    InstanceType type = entity.getType();

                    process.setTypeGUID(type.getTypeDefGUID());
                    process.setTypeName(type.getTypeDefName());
                    process.setGUID(entity.getGUID());

                    InstanceProperties entityProperties = entity.getProperties();
                    if (entityProperties != null) {

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
            }
            return returnBean;
        } catch (IllegalAccessException | InstantiationException | ClassCastException error) {
            super.handleInvalidBeanClass(processClass.getName(), error, methodName);
        }

        return null;
    }

    /**
     * Using the supplied instances, return a new instance of the bean.  It is used for beans such as
     * a connection bean which made up of 3 entities (Connection, ConnectorType and Endpoint) plus the
     * relationships between them.  The relationships may be omitted if they do not have any properties.
     * <p>
     * TO BE IMPLEMENTED IN CASE OF NEED
     *
     * @param beanClass             name of the class to create
     * @param primaryEntity         entity that is the root of the cluster of entities that make up the content of the bean
     * @param supplementaryEntities entities connected to the primary entity by the relationships
     * @param relationships         relationships linking the entities
     * @param methodName            calling method
     *
     * @return bean populated with properties from the instances supplied
     *
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    @SuppressWarnings(value = "unused")
    public B getNewComplexBean(Class<B> beanClass,
                               EntityDetail primaryEntity,
                               List<EntityDetail> supplementaryEntities,
                               List<Relationship> relationships,
                               String methodName) throws PropertyServerException {
        return super.getNewComplexBean(beanClass, primaryEntity, supplementaryEntities, relationships, methodName);
    }

    /**
     * Retrieve and delete the OwnerType enum property from the instance properties of an entity
     *
     * @param properties entity properties
     *
     * @return OwnerType  enum value
     */
    OwnerType removeOwnerTypeFromProperties(InstanceProperties properties) {
        OwnerType ownerType = this.getOwnerTypeFromProperties(properties);

        if (properties != null) {
            Map<String, InstancePropertyValue> instancePropertiesMap = properties.getInstanceProperties();

            if (instancePropertiesMap != null) {
                instancePropertiesMap.remove(OpenMetadataAPIMapper.OWNER_TYPE_PROPERTY_NAME);
            }

            properties.setInstanceProperties(instancePropertiesMap);
        }

        return ownerType;
    }


    /**
     * Retrieve the OwnerType enum property from the instance properties of an entity
     *
     * @param properties entity properties
     *
     * @return OwnerType  enum value
     */
    private OwnerType getOwnerTypeFromProperties(InstanceProperties properties) {
        OwnerType ownerType = null;

        if (properties != null) {
            InstancePropertyValue instancePropertyValue = properties.getPropertyValue(OpenMetadataAPIMapper.OWNER_TYPE_PROPERTY_NAME);

            if (instancePropertyValue instanceof EnumPropertyValue) {
                EnumPropertyValue enumPropertyValue = (EnumPropertyValue) instancePropertyValue;

                switch (enumPropertyValue.getOrdinal()) {
                    case 0:
                        ownerType = OwnerType.USER_ID;
                        break;

                    case 1:
                        ownerType = OwnerType.PROFILE_ID;
                        break;

                    case 99:
                        ownerType = OwnerType.OTHER;
                        break;
                }
            }

            Map<String, InstancePropertyValue> instancePropertyValueMap = properties.getInstanceProperties();
            instancePropertyValueMap.remove(OpenMetadataAPIMapper.OWNER_TYPE_PROPERTY_NAME);
            properties.setInstanceProperties(instancePropertyValueMap);
        }

        return ownerType;
    }
}
