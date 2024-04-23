/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers.relationships;

import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.ProjectScope;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.SubjectAreaMapper;
import org.odpi.openmetadata.commonservices.generichandlers.*;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.SubjectAreaUtils;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;


/**
 * Mapping methods to map between the ProjectScope and the equivalent omrs Relationship.
 */
@SubjectAreaMapper
public class ProjectScopeMapper extends RelationshipMapper<ProjectScope> {
    public static final String PROJECT_SCOPE = "ProjectScope";

    public ProjectScopeMapper(OpenMetadataAPIGenericHandler genericHandler){
        super(genericHandler);
    }

    @Override
    public String getTypeName() {
        return PROJECT_SCOPE;
    }

    @Override
    public ProjectScope getRelationshipInstance() {
        return new ProjectScope();
    }

    /**
     * Map the supplied relationship to omrs InstanceProperties.
     *
     * @param projectScope               supplied relationship
     * @param instanceProperties equivalent instance properties to the relationship
     */
    @Override
    public void mapRelationshipToInstanceProperties(ProjectScope projectScope, InstanceProperties instanceProperties) {
        if (projectScope.getDescription() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, projectScope.getDescription(), "description");
        }
    }

    /**
     * Map a primitive omrs property to the antonym object.
     *
     * @param projectScope         the ProjectScope to be updated
     * @param propertyName the omrs property name
     * @param value        the omrs primitive property value
     * @return true if the propertyName was recognised and mapped to the relationship, otherwise false
     */
    @Override
    protected boolean mapPrimitiveToRelationship(ProjectScope projectScope, String propertyName, Object value) {
        String stringValue = (String) value;
        boolean foundProperty = false;
        if (propertyName.equals(OpenMetadataProperty.DESCRIPTION.name)) {
            projectScope.setDescription(stringValue);
            foundProperty = true;
        }

        return foundProperty;
    }
}
