/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers.relationships;

import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.ProjectScope;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.SubjectAreaMapper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.OMRSAPIHelper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.SubjectAreaUtils;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;


/**
 * Mapping methods to map between the ProjectScope and the equivalent omrs Relationship.
 */
@SubjectAreaMapper
public class ProjectScopeMapper extends LineMapper<ProjectScope> {
    public static final String PROJECT_SCOPE = "ProjectScope";

    public ProjectScopeMapper(OMRSAPIHelper omrsapiHelper) {
        super(omrsapiHelper);
    }

    /**
     * Get proxy1 guid.
     * The proxy has omrs type Project
     *
     * @param projectScope line
     * @return guid for entity proxy 1
     */
    @Override
    public String getProxy1Guid(ProjectScope projectScope) {
        return projectScope.getProjectGuid();
    }

    /**
     * Get proxy2 guid
     * The proxy has an omrs type that is a subtype of Referenceable
     *
     * @param projectScope for this Line
     * @return guid for entity proxy 2
     */
    @Override
    public String getProxy2Guid(ProjectScope projectScope) {
        return projectScope.getNodeGuid();
    }

    @Override
    public String getTypeName() {
        return PROJECT_SCOPE;
    }

    @Override
    public ProjectScope getLineInstance() {
        return new ProjectScope();
    }

    @Override
    public void setEnd1GuidInLine(ProjectScope line, String guid) {
        line.setProjectGuid(guid);
    }

    @Override
    public void setEnd2GuidInLine(ProjectScope line, String guid) {
        line.setNodeGuid(guid);
    }

    /**
     * Map the supplied Line to omrs InstanceProperties.
     *
     * @param projectScope               supplied line
     * @param instanceProperties equivalent instance properties to the Line
     */
    @Override
    public void mapLineToInstanceProperties(ProjectScope projectScope, InstanceProperties instanceProperties) {
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
     * @return true if the propertyName was recognised and mapped to the Line, otherwise false
     */
    @Override
    protected boolean mapPrimitiveToLine(ProjectScope projectScope, String propertyName, Object value) {
        String stringValue = (String) value;
        boolean foundProperty = false;
        if (propertyName.equals("description")) {
            projectScope.setDescription(stringValue);
            foundProperty = true;
        }

        return foundProperty;
    }
}
