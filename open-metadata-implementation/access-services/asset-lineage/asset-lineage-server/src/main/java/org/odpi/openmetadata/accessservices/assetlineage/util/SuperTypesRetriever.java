/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.util;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.HashSet;
import java.util.Set;

import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.REFERENCEABLE;

/**
 * Retrieves information about the super-types of a given type
 */
public class SuperTypesRetriever {

    private OMRSRepositoryHelper repositoryHelper;

    /**
     * SuperTypeRetriever class provides details about a Open Metadata Type
     *
     * @param repositoryHelper repository connector helper
     */
    public SuperTypesRetriever(OMRSRepositoryHelper repositoryHelper) {
        this.repositoryHelper = repositoryHelper;
    }

    /**
     * Returns a collection with super type's names for a type
     *
     * @param userId      String - userId of user making request.
     * @param typeDefName type name
     * @return a set with supertype names
     */
    public Set<String> getSuperTypes(String userId, String typeDefName) {
        return collectSuperTypes(userId, typeDefName);
    }

    private Set<String> collectSuperTypes(String userId, String typeDefName) {
        Set<String> superTypes = new HashSet<>();

        TypeDef typeDefByName = repositoryHelper.getTypeDefByName(userId, typeDefName);
        if (typeDefByName != null) {
            collectSuperTypes(userId, typeDefByName, superTypes);
        }

        return superTypes;
    }

    private void collectSuperTypes(String userId, TypeDef type, Set<String> superTypes) {
        if (type.getName().equals(REFERENCEABLE)) {
            return;
        }
        superTypes.add(type.getName());

        if (type.getSuperType() == null) {
            return;
        }

        TypeDef typeDefByName = repositoryHelper.getTypeDefByName(userId, type.getSuperType().getName());
        if (typeDefByName != null) {
            collectSuperTypes(userId, typeDefByName, superTypes);
        }
    }
}
