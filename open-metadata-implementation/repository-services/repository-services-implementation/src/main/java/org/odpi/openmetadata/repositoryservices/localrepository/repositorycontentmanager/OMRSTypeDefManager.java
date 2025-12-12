/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.localrepository.repositorycontentmanager;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.AttributeTypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;


/**
 * OMRSTypeDefManager provides maintenance methods for managing the TypeDefs in the local cache.
 */
public interface OMRSTypeDefManager
{
    /**
     * Cache a definition of a new TypeDef.
     *
     * @param sourceName source of the request (used for logging)
     * @param newTypeDef TypeDef structure describing the new TypeDef.
     */
    void addTypeDef(String  sourceName, TypeDef newTypeDef);


    /**
     * Cache a definition of a new AttributeTypeDef.
     *
     * @param sourceName source of the request (used for logging)
     * @param newAttributeTypeDef AttributeTypeDef structure describing the new TypeDef.
     */
    void addAttributeTypeDef(String  sourceName, AttributeTypeDef newAttributeTypeDef);


    /**
     * Update one or more properties of a cached TypeDef.
     *
     * @param sourceName source of the request (used for logging)
     * @param typeDef TypeDef structure.
     */
    void updateTypeDef(String  sourceName, TypeDef   typeDef);


    /**
     * Delete a cached TypeDef.
     *
     * @param sourceName source of the request (used for logging)
     * @param obsoleteTypeDefGUID String unique identifier for the TypeDef.
     * @param obsoleteTypeDefName String unique name for the TypeDef.
     */
    void deleteTypeDef(String    sourceName,
                       String    obsoleteTypeDefGUID,
                       String    obsoleteTypeDefName);


    /**
     * Delete a cached AttributeTypeDef.
     *
     * @param sourceName source of the request (used for logging)
     * @param obsoleteAttributeTypeDefGUID String unique identifier for the AttributeTypeDef.
     * @param obsoleteAttributeTypeDefName String unique name for the AttributeTypeDef.
     */
    void deleteAttributeTypeDef(String    sourceName,
                                String    obsoleteAttributeTypeDefGUID,
                                String    obsoleteAttributeTypeDefName);


    /**
     * Change the identifiers for a TypeDef.
     *
     * @param sourceName source of the request (used for logging).
     * @param originalTypeDefGUID TypeDef's original unique identifier.
     * @param originalTypeDefName TypeDef's original unique name.
     * @param newTypeDef updated TypeDef with new identifiers.
     */
    void reIdentifyTypeDef(String   sourceName,
                           String   originalTypeDefGUID,
                           String   originalTypeDefName,
                           TypeDef  newTypeDef);


    /**
     * Change the identifiers for an AttributeTypeDef.
     *
     * @param sourceName source of the request (used for logging).
     * @param originalAttributeTypeDefGUID AttributeTypeDef's original unique identifier.
     * @param originalAttributeTypeDefName AttributeTypeDef's original unique name.
     * @param newAttributeTypeDef updated AttributeTypeDef with new identifiers
     */
    void reIdentifyAttributeTypeDef(String            sourceName,
                                    String            originalAttributeTypeDefGUID,
                                    String            originalAttributeTypeDefName,
                                    AttributeTypeDef  newAttributeTypeDef);


    /**
     * Return a boolean indicating that the type name matches the category.
     *
     * @param sourceName source of the request (used for logging)
     * @param category TypeDefCategory enum value to test
     * @param typeName type name to test
     * @param methodName name of calling method.
     * @return boolean flag indicating that the type name is of the specified category
     * @throws TypeErrorException the type name is not a recognized type or the category is incorrect or there
     *                              is an error in the type definition (TypeDef) cached.
     */
    boolean    isValidTypeCategory(String            sourceName,
                                   TypeDefCategory   category,
                                   String            typeName,
                                   String            methodName) throws TypeErrorException;


    /**
     * Return boolean indicating if a classification type can be applied to a specified entity.  This
     * uses the list of valid entity types located in the ClassificationDef.
     *
     * @param sourceName source of the request (used for logging)
     * @param classificationTypeName name of the classification's type (ClassificationDef)
     * @param entityTypeName name of the entity's type (EntityDef)
     * @param methodName name of calling method.
     * @return boolean indicating if the classification is valid for the entity.
     * @throws TypeErrorException the type name is not a recognized type or the category is incorrect or there
     *                              is an error in the type definition (TypeDef) cached.
     */
    boolean    isValidClassificationForEntity(String  sourceName,
                                              String  classificationTypeName,
                                              String  entityTypeName,
                                              String  methodName) throws TypeErrorException;


    /**
     * Return identifiers for the TypeDef that matches the supplied type name.  If the type name is not recognized,
     * null is returned.
     *
     * @param sourceName source of the request (used for logging)
     * @param category category of type
     * @param typeName String type name the type name is not recognized or of the wrong category.
     * @param methodName name of calling method.
     * @return InstanceType object containing TypeDef unique identifier (guid), typeDef name and version
     * @throws TypeErrorException the type name is not a recognized type or the category is incorrect or there
     *                              is an error in the type definition (TypeDef) cached.
     */
    InstanceType getInstanceType(String            sourceName,
                                 TypeDefCategory   category,
                                 String            typeName,
                                 String            methodName) throws TypeErrorException;


    /**
     * Return the initial status set up for the instance.
     *
     * @param sourceName source of the request (used for logging)
     * @param typeName name of the type to extract the initial status from.
     * @param methodName calling method
     * @return InstanceStatus enum
     * @throws TypeErrorException the type name is not recognized.
     */
    InstanceStatus getInitialStatus(String sourceName,
                                    String typeName,
                                    String methodName) throws TypeErrorException;
}
