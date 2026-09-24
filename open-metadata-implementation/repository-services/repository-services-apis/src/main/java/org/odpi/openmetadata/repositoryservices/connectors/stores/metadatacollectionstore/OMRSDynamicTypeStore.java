/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.AttributeTypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefGallery;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;

/**
 * OMRSDynamicTypeStore is the interface that a repository built on OMRSDynamicTypeMetadataCollectionBase implements
 * so that the type definitions it originates are kept with its instances, and so that it can say whether a type
 * has ever been used.
 * <br><br>
 * Only the types that are homed in the repository are stored - that is, the types whose origin is the repository's
 * metadata collection id.  These are the types that were defined through the API.  Types from open metadata archives
 * and from other members of the cohort are not stored because their originators supply them again each time the
 * server starts.
 * <br><br>
 * The metadata collection base class does the validation and the checks against the rest of the type system.  The
 * store only has to hold what it is given and report on its instances.
 */
public interface OMRSDynamicTypeStore
{
    /**
     * Save a type definition, replacing any previous version of it.  Saving a type definition that is already
     * stored at the same version must be harmless because this happens each time the repository starts: the stored
     * types are replayed through the same path as a new type.
     *
     * @param typeDef type definition to store
     * @throws RepositoryErrorException problem accessing the store
     */
    void saveTypeDef(TypeDef typeDef) throws RepositoryErrorException;


    /**
     * Save an attribute type definition, replacing any previous version of it.  As for saveTypeDef(), saving one
     * that is already stored must be harmless.
     *
     * @param attributeTypeDef attribute type definition to store
     * @throws RepositoryErrorException problem accessing the store
     */
    void saveAttributeTypeDef(AttributeTypeDef attributeTypeDef) throws RepositoryErrorException;


    /**
     * Remove a type definition.  Nothing happens if it is not stored.
     *
     * @param typeDefGUID unique identifier of the type definition
     * @param typeDefName unique name of the type definition
     * @throws RepositoryErrorException problem accessing the store
     */
    void removeTypeDef(String typeDefGUID,
                       String typeDefName) throws RepositoryErrorException;


    /**
     * Remove an attribute type definition.  Nothing happens if it is not stored.
     *
     * @param attributeTypeDefGUID unique identifier of the attribute type definition
     * @param attributeTypeDefName unique name of the attribute type definition
     * @throws RepositoryErrorException problem accessing the store
     */
    void removeAttributeTypeDef(String attributeTypeDefGUID,
                                String attributeTypeDefName) throws RepositoryErrorException;


    /**
     * Return all the stored type definitions.  They are returned in the order that they were first stored, which
     * is an order in which each type's dependencies on other stored types are satisfied - unless an update has
     * since changed a dependency - so the caller must still order them before replaying them.
     *
     * @return gallery of stored types, or null if there are none
     * @throws RepositoryErrorException problem accessing the store
     */
    TypeDefGallery getStoredTypes() throws RepositoryErrorException;


    /**
     * Return whether the repository holds any instance of the type - an entity, entity proxy, relationship or
     * classification - in any version and any status, including soft-deleted instances.  Only purged instances
     * are ignored.
     *
     * @param typeDefGUID unique identifier of the type definition
     * @param typeDefName unique name of the type definition
     * @return boolean
     * @throws RepositoryErrorException problem accessing the store
     */
    boolean isTypeDefInstantiated(String typeDefGUID,
                                  String typeDefName) throws RepositoryErrorException;
}
