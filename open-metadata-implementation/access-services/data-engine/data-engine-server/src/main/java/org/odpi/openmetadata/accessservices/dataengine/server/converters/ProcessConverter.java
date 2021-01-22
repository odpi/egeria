/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.converters;

import org.odpi.openmetadata.accessservices.dataengine.model.Process;
import org.odpi.openmetadata.accessservices.dataengine.server.mappers.ProcessPropertiesMapper;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

/**
 * ProcessConverter transfers the relevant properties from an Open Metadata Repository Services (OMRS)
 * EntityDetail object into a Process bean.
 */
public class ProcessConverter extends AssetConverter {
    public ProcessConverter(OMRSRepositoryHelper repositoryHelper,
                            String serviceName,
                            String serverName) {
        super(repositoryHelper, serviceName, serverName);
    }

    /**
     * Request the bean is extracted from the repository objects
     *
     * @return output bean
     */
    public Process getProcessBean(EntityDetail entity) throws PropertyServerException {
        final String methodName = "updateBean";

        Process process = (Process) getNewBean(Process.class, entity, methodName);

        if (entity != null) {
            InstanceProperties instanceProperties = entity.getProperties();

            if (instanceProperties != null) {
                process.setFormula(repositoryHelper.removeStringProperty(serviceName,
                        ProcessPropertiesMapper.FORMULA_PROPERTY_NAME, instanceProperties, methodName));
            }
        }

        return process;
    }
}
