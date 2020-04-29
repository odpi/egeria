/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
// This is a generated file - do not edit - changes should be made to the templates amd/or generator to generate this file with changes.

package org.odpi.openmetadata.accessservices.subjectarea.properties.relationships;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Line;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.LineType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The Project Scope relationship links documentation, assets and definitions to the project.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ProjectScope extends Line {
    private static final Logger log = LoggerFactory.getLogger(ProjectScope.class);
    private static final String className = ProjectScope.class.getName();

      private static final String[] PROPERTY_NAMES_SET_VALUES = new String[] {
          "scopeDescription",
      // Terminate the list
          null
      };
      private static final String[] ATTRIBUTE_NAMES_SET_VALUES = new String[] {
          "scopeDescription",
       // Terminate the list
          null
      };
      private static final String[] ENUM_NAMES_SET_VALUES = new String[] {

           // Terminate the list
            null
      };
      private static final String[] MAP_NAMES_SET_VALUES = new String[] {

           // Terminate the list
           null
      };
      private static final Set<String> PROPERTY_NAMES_SET = new HashSet(new HashSet<>(Arrays.asList(PROPERTY_NAMES_SET_VALUES)));
      private static final Set<String> ATTRIBUTE_NAMES_SET = new HashSet(new HashSet<>(Arrays.asList(ATTRIBUTE_NAMES_SET_VALUES)));
      private static final Set<String> ENUM_NAMES_SET = new HashSet(new HashSet<>(Arrays.asList(ENUM_NAMES_SET_VALUES)));
      private static final Set<String> MAP_NAMES_SET = new HashSet(new HashSet<>(Arrays.asList(MAP_NAMES_SET_VALUES)));
      private String projectGuid;
      private String nodeGuid;


    public ProjectScope() {
        initialise();
    }

    private void initialise()
    {
       name = "ProjectScope";
       // set the LineType if this is a LineType enum value.
       try {
           lineType = LineType.valueOf(name);
        }
        catch (IllegalArgumentException e) {
           lineType = LineType.Unknown;
        }

        entity1Name = "impactingProjects";
        entity1Type = "Project";
        entity2Name = "projectScope";
        entity2Type = "Referencable";
        typeDefGuid = "bc63ac45-b4d0-4fba-b583-92859de77dd8";
    }

    public ProjectScope(Line template) {
        super(template);
        initialise();
    }

    public ProjectScope(Relationship omrsRelationship) {
        super(omrsRelationship);
        name = "ProjectScope";
       // set the LineType if this is a LineType enum value.
       try {
           lineType = LineType.valueOf(name);
        }
        catch (IllegalArgumentException e) {
           lineType = LineType.Unknown;
        }
    }

    public String getProjectGuid() {
        return projectGuid;
    }

    public void setProjectGuid(String projectGuid) {
        this.projectGuid = projectGuid;
    }

    public String getNodeGuid() {
        return nodeGuid;
    }

    public void setNodeGuid(String nodeGuid) {
        this.nodeGuid = nodeGuid;
    }

      @Override
         public StringBuilder toString(StringBuilder sb)
         {
             if (sb == null)
             {
                 sb = new StringBuilder();
             }
             sb.append(" ProjectScopeRelationship=");
             sb.append(super.toString(sb));
             sb.append(" ProjectScopeRelationship Attributes{");
             sb.append("}");
             return sb;
         }
         @Override
         public String toString() {
             return toString(new StringBuilder()).toString();
         }
    private String scopeDescription;
    /**
     * {@literal Description of the scope of the project. }
     * @return {@code String }
     */
    public String getScopeDescription() {
        return this.scopeDescription;
    }
    public void  setScopeDescription(String description)  {
        this.scopeDescription = description;
    }

}
