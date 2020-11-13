/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.rex.api.properties;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.RelationshipDef;


public class RelationshipExplorer {

    private RelationshipDef relationshipDef;


    public RelationshipExplorer(RelationshipDef def) {
        relationshipDef = def;
    }

    public RelationshipDef getRelationshipDef() { return relationshipDef; }
}

