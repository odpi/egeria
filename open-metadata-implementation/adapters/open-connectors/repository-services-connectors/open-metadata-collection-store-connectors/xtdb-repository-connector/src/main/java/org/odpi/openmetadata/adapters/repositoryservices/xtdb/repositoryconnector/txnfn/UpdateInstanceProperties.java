/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.txnfn;

import clojure.lang.IPersistentMap;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.ffdc.XTDBErrorCode;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.cache.TypeDefCache;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mapping.ClassificationMapping;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mapping.InstancePropertiesMapping;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;

import java.io.IOException;
import java.util.Date;

/**
 * Base transaction function for updating an instance's properties.
 */
public abstract class UpdateInstanceProperties extends AbstractTransactionFunction {

    /**
     * Makes the necessary changes to update a metadata instance's properties.
     *
     * @param userId doing the update
     * @param existing metadata instance
     * @param properties full set of properties for the instance
     * @return IPersistentMap giving the updated instance representation
     * @throws InvalidParameterException if any of the properties cannot be persisted
     * @throws IOException on any error serializing the properties
     * @throws ClassificationErrorException if the requested classification cannot be found
     */
    protected static IPersistentMap updateInstanceProperties(String userId,
                                                             IPersistentMap existing,
                                                             InstanceProperties properties)
            throws InvalidParameterException, IOException, ClassificationErrorException {
        return updateInstanceProperties(userId, existing, properties, null);
    }

    /**
     * Makes the necessary changes to update a metadata instance's properties, or the classification's properties
     * if a classification name is provided.
     *
     * @param userId doing the update
     * @param existing metadata instance
     * @param properties full set of properties for the instance
     * @param classificationName of the classification whose properties should be updated (or null to update base instance's properties)
     * @return IPersistentMap giving the updated instance representation
     * @throws InvalidParameterException if any of the properties cannot be persisted
     * @throws IOException on any error serializing the properties
     * @throws ClassificationErrorException if the requested classification cannot be found
     */
    protected static IPersistentMap updateInstanceProperties(String userId,
                                                             IPersistentMap existing,
                                                             InstanceProperties properties,
                                                             String classificationName)
            throws InvalidParameterException, IOException, ClassificationErrorException {
        final String methodName = "updateInstanceProperties";
        IPersistentMap doc = incrementVersion(userId, existing, classificationName);
        String typeDefGUID;
        if (classificationName == null) {
            typeDefGUID = getTypeDefGUID(existing);
        } else {
            TypeDef classificationTypeDef = TypeDefCache.getTypeDefByName(classificationName);
            if (classificationTypeDef == null) {
                throw new ClassificationErrorException(XTDBErrorCode.INVALID_TYPEDEF.getMessageDefinition(
                        classificationName), TxnValidations.class.getName(), methodName);
            }
            typeDefGUID = classificationTypeDef.getGUID();
            ClassificationMapping.validateHasClassification(existing, classificationName, UpdateEntityClassification.CLASS_NAME, UpdateEntityClassification.METHOD_NAME);
            doc = doc.assoc(Keywords.LAST_CLASSIFICATION_CHANGE, new Date());
        }
        return InstancePropertiesMapping.addToMap(doc, typeDefGUID, properties);
    }

    /**
     * Validate the properties to be changed.
     *
     * @param existing metadata instance
     * @param instanceGUID unique identifier of the metadata instance
     * @param metadataCollectionId of the metadata instance
     * @param properties new properties for the metadata instance
     * @param className calling class
     * @param methodName calling method
     * @throws InvalidParameterException on any null or invalid parameters
     * @throws IOException on any error deserializing values
     * @throws PropertyErrorException one or more of the requested properties are not defined, or have different characteristics in the TypeDef for this instance's type
     * @throws RepositoryErrorException on any other error
     */
    protected static void validate(IPersistentMap existing,
                                   String instanceGUID,
                                   String metadataCollectionId,
                                   InstanceProperties properties,
                                   String className,
                                   String methodName)
            throws InvalidParameterException, RepositoryErrorException, IOException, PropertyErrorException {
        TxnValidations.instanceIsNotDeleted(existing, instanceGUID, className, methodName);
        TxnValidations.instanceCanBeUpdated(existing, instanceGUID, metadataCollectionId, className, methodName);
        TxnValidations.instanceType(existing, className, methodName);
        TypeDef typeDef = getTypeDefForInstance(existing);
        TxnValidations.propertiesForType(typeDef, properties, className, methodName);
    }

}
