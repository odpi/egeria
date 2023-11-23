/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.txnfn;

import clojure.lang.IPersistentMap;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.cache.TypeDefCache;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mapping.InstanceAuditHeaderMapping;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mapping.InstancePropertiesMapping;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PropertyErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;

import java.io.IOException;

/**
 * Base transaction function for changing an instance's type.
 */
public abstract class ReTypeInstance extends AbstractTransactionFunction {

    /**
     * Makes the necessary changes to an instance's type.
     *
     * @param userId doing the update
     * @param existing metadata instance
     * @param newTypeDef new type for the instance
     * @return IPersistentMap giving the updated instance representation
     * @throws TypeErrorException the requested type is not known or supported by the repository
     * @throws IOException on any error serializing values
     */
    protected static IPersistentMap reTypeInstance(String userId,
                                                   IPersistentMap existing,
                                                   TypeDef newTypeDef)
            throws TypeErrorException, IOException {
        InstanceType newInstanceType = TypeDefCache.getInstanceType(newTypeDef.getCategory(), newTypeDef.getName());
        IPersistentMap doc = incrementVersion(userId, existing);
        return InstanceAuditHeaderMapping.addTypeDetailsToMap(doc, newInstanceType, null);
    }

    /**
     * Validate the type change.
     *
     * @param existing metadata instance
     * @param instanceGUID unique identifier of the metadata instance
     * @param newTypeDef new type for the instance
     * @param metadataCollectionId of the metadata instance
     * @param className calling class
     * @param methodName calling method
     * @throws InvalidParameterException on any null or invalid parameters
     * @throws IOException on any error deserializing values
     * @throws PropertyErrorException the properties in the instance are incompatible with the requested type
     * @throws RepositoryErrorException on any other error
     */
    protected static void validate(IPersistentMap existing,
                                   String instanceGUID,
                                   TypeDef newTypeDef,
                                   String metadataCollectionId,
                                   String className,
                                   String methodName)
            throws InvalidParameterException, RepositoryErrorException, IOException, PropertyErrorException {
        TxnValidations.instanceCanBeUpdated(existing, instanceGUID, metadataCollectionId, className, methodName);
        TxnValidations.instanceType(existing, className, methodName);
        InstanceType instanceType = InstanceAuditHeaderMapping.getTypeFromInstance(existing, null);
        InstanceProperties properties = InstancePropertiesMapping.getFromMap(instanceType, existing);
        TxnValidations.propertiesForType(newTypeDef, properties, className, methodName);
    }

}
