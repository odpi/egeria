/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.util;

import com.sun.corba.se.spi.activation.RepositoryHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.odpi.openmetadata.accessservices.assetlineage.util.Constants.*;
import static org.odpi.openmetadata.accessservices.assetlineage.util.Constants.LINEAGE_MAPPING;

/**
 * The validator is used for validating whether the entity or relationship types are relevant for asset lineage context.
 */
public class Validator {

    private OMRSRepositoryHelper repositoryHelper;

    public Validator(OMRSRepositoryHelper repositoryHelper){
        this.repositoryHelper = repositoryHelper;
    }

    public Set<String> getSuperTypes(String typeDefName){
       return  collectSuperTypes(ASSET_LINEAGE_OMAS, typeDefName);
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
        TypeDef typeDefByName = repositoryHelper.getTypeDefByName(userId, type.getSuperType().getName());
        if (typeDefByName != null) {
            collectSuperTypes(userId, typeDefByName, superTypes);
        }
    }
    /**
     * Is valid lineage entity event boolean.
     *
     * @param typeDefName the type def name
     * @return the boolean
     */
    public boolean isValidLineageEntityEvent(String typeDefName) {
        final List<String> types = Arrays.asList(GLOSSARY_TERM, TABULAR_SCHEMA_TYPE, TABULAR_COLUMN, RELATIONAL_COLUMN, RELATIONAL_TABLE, DATA_FILE);
        return types.contains(typeDefName);
    }


    /**
     * Is valid lineage relationship event boolean.
     *
     * @param relationship the relationship
     * @return the boolean
     */
    public boolean isValidLineageRelationshipEvent(Relationship relationship) {
        String entityProxyOneType = relationship.getEntityOneProxy().getType().getTypeDefName();
        if (
                relationship.getType().getTypeDefName().equals(SEMANTIC_ASSIGNMENT)
                        &&
                        (
                                entityProxyOneType.equals(RELATIONAL_COLUMN) || entityProxyOneType.equals(RELATIONAL_TABLE)
                                        || entityProxyOneType.equals(TABULAR_COLUMN)
                        )
        )
            return true;

        final List<String> types = Arrays.asList(PROCESS_PORT, PORT_DELEGATION, PORT_SCHEMA, SCHEMA_TYPE, ATTRIBUTE_FOR_SCHEMA, LINEAGE_MAPPING);

        return types.contains(relationship.getType().getTypeDefName());
    }


}
