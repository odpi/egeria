/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.converters;

import org.odpi.openmetadata.accessservices.dataengine.model.Process;
import org.odpi.openmetadata.accessservices.dataengine.server.mappers.ProcessPropertiesMapper;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.converters.AssetConverter;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

/**
 * ProcessConverter transfers the relevant properties from an Open Metadata Repository Services (OMRS)
 * EntityDetail object into a Process bean.
 */
public class ProcessConverter extends AssetConverter {
    public ProcessConverter(EntityDetail processEntity, Relationship connectionToAssetRelationship,
                            OMRSRepositoryHelper repositoryHelper, String methodName) {
        super(processEntity, connectionToAssetRelationship, repositoryHelper, methodName);
    }

    /**
     * Request the bean is extracted from the repository objects
     *
     * @return output bean
     */
    public Process getProcessBean() {
        Process bean = new Process();
        updateBean(bean);

        return bean;
    }

    private void updateBean(Process bean) {
        final String methodName = "updateBean";

        super.updateBean(bean);

        if (entity != null) {
            InstanceProperties instanceProperties = entity.getProperties();

            if (instanceProperties != null) {
                bean.setFormula(repositoryHelper.removeStringProperty(serviceName,
                        ProcessPropertiesMapper.FORMULA_PROPERTY_NAME, instanceProperties, methodName));
            }
        }

    }
}
