/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers.relationships;

import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Line;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.ProjectScope;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.OMRSAPIHelper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.SubjectAreaUtils;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Mapping methods to map between the ProjectScope and the equivalent omrs Relationship.
 */
public class ProjectScopeMapper extends LineMapper
{
    private static final Logger log = LoggerFactory.getLogger( ProjectScopeMapper.class);
    private static final String className = ProjectScopeMapper.class.getName();
    public static final String PROJECT_SCOPE = "ProjectScope";

    public ProjectScopeMapper(OMRSAPIHelper omrsapiHelper) {
        super(omrsapiHelper);
    }

    /**
     * Get proxy1 guid.
     * The proxy has omrs type Project
     * @param line line
     * @return guid for entity proxy 1
     */
    @Override
    protected String getProxy1Guid(Line line)
    {
        ProjectScope projectScope = (ProjectScope) line;
        return projectScope.getProjectGuid();
    }

    /**
     * Get proxy2 guid
     * The proxy has an omrs type that is a subtype of Referenceable  
     * @param line for this Line
     * @return guid for entity proxy 2
     */
    @Override
    protected String getProxy2Guid(Line line)
    {
        ProjectScope projectScope = (ProjectScope) line;
        return projectScope.getNodeGuid();
    }

    /**
     * Get the relationship type def guid.
     * @param relationship the relationship associated with the typedef whose guid is returned.
     * @return guid of the typedef
     */
    @Override
    protected String getRelationshipTypeDefGuid(Relationship relationship)
    {
        return repositoryHelper.getTypeDefByName(omrsapiHelper.getServiceName(), PROJECT_SCOPE).getGUID();
    }
    @Override
    public String getTypeName() {
        return  PROJECT_SCOPE;
    }
    @Override
    protected Line getLineInstance() {
        return new ProjectScope();
    }
    @Override
    protected void setEnd1GuidInLine(Line line, String guid){
        ProjectScope ProjectScopeRelationship = (ProjectScope)line;
        ProjectScopeRelationship.setProjectGuid(guid);
    }
    @Override
    protected void setEnd2GuidInLine(Line line, String guid) {
        ProjectScope ProjectScopeRelationship = (ProjectScope)line;
        ProjectScopeRelationship.setNodeGuid(guid);
    }
    /**
     * Map the supplied Line to omrs InstanceProperties.
     * @param line supplied line
     * @param instanceProperties equivalent instance properties to the Line
     */
    @Override
    protected void mapLineToInstanceProperties(Line line, InstanceProperties instanceProperties) {
        ProjectScope projectScope  = (ProjectScope)line;
        if (projectScope.getScopeDescription()!=null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, projectScope.getScopeDescription(), "scopeDescription");
        }
    }
    /**
     * Map a primitive omrs property to the antonym object.
     * @param line the ProjectScope to be updated
     * @param propertyName the omrs property name
     * @param value the omrs primitive property value
     * @return true if the propertyName was recognised and mapped to the Line, otherwise false
     */
    @Override
    protected boolean mapPrimitiveToLine(Line line, String propertyName, Object value) {
        String stringValue = (String) value;
        ProjectScope projectScope = (ProjectScope)line;
        boolean foundProperty = false;
        if (propertyName.equals("scopeDescription")) {
            projectScope.setScopeDescription(stringValue);
            foundProperty = true;
        }

        return foundProperty;
    }
}
