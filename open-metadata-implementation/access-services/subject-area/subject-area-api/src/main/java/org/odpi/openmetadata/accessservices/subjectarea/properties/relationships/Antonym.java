/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
// This is a generated file - do not edit - changes should be made to the templates amd/or generator to generate this file with changes.

package org.odpi.openmetadata.accessservices.subjectarea.properties.relationships;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;
import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.*;

//omrs
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
//omrs beans
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Line;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.LineType;

/**
 * Link between glossary terms that have the opposite meaning.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class Antonym extends Line {
    private static final Logger log = LoggerFactory.getLogger(Antonym.class);
    private static final String className = Antonym.class.getName();

    private static final String[] PROPERTY_NAMES_SET_VALUES = new String[] {
            "description",
            "expression",
            "status",
            "steward",
            "source",

            // Terminate the list
            null
    };
    private static final String[] ATTRIBUTE_NAMES_SET_VALUES = new String[] {
            "description",
            "expression",
            "steward",
            "source",

            // Terminate the list
            null
    };
    private static final String[] ENUM_NAMES_SET_VALUES = new String[] {
            "status",

            // Terminate the list
            null
    };
    private static final String[] MAP_NAMES_SET_VALUES = new String[] {

            // Terminate the list
            null
    };
    private static final java.util.Set<String> PROPERTY_NAMES_SET = new HashSet<>(Arrays.asList(PROPERTY_NAMES_SET_VALUES));
    private static final java.util.Set<String> ATTRIBUTE_NAMES_SET = new HashSet<>(Arrays.asList(ATTRIBUTE_NAMES_SET_VALUES));
    private static final java.util.Set<String> ENUM_NAMES_SET = new HashSet<>(Arrays.asList(ENUM_NAMES_SET_VALUES));
    private static final java.util.Set<String> MAP_NAMES_SET = new HashSet<>(Arrays.asList(MAP_NAMES_SET_VALUES));
    private String antonym1Guid = null;
    private String antonym2Guid = null;

    public Antonym() {
        initialise();
    }

    private void initialise()
    {
        name = "Antonym";
        // set the LineType if this is a LineType enum value.
        try {
            lineType = LineType.valueOf(name);
        }
        catch (IllegalArgumentException e) {
            lineType = LineType.Unknown;
        }
        // save the below information to help map to OMRS.
         entity1Name = "antonyms";
         entity1Type = "GlossaryTerm";
         entity2Name = "antonyms";
         entity2Type = "GlossaryTerm";
         typeDefGuid = "ea5e126a-a8fa-4a43-bcfa-309a98aa0185";
    }

    public Antonym(Line template) {
        super(template);
        initialise();
    }

    public Antonym(Relationship omrsRelationship) {
        super(omrsRelationship);
        name = "Antonym";
        // set the LineType if this is a LineType enum value.
        try {
            lineType = LineType.valueOf(name);
        }
        catch (IllegalArgumentException e) {
            lineType = LineType.Unknown;
        }
        this.antonym1Guid = omrsRelationship.getEntityOneProxy().getGUID();
        this.antonym2Guid = omrsRelationship.getEntityTwoProxy().getGUID();
    }
    /**
     * {@literal Get the guid of Antonym at end 2 of the relationship. }
     * @return {@code String }
     */
    public String getAntonym1Guid()
    {
        return antonym1Guid;
    }

    public void setAntonym1Guid(String antonym1Guid)
    {
        this.antonym1Guid = antonym1Guid;
    }
    /**
     * {@literal Get the guid of Antonym at end 2 of the relationship. }
     * @return {@code String }
     */

    public String getAntonym2Guid()
    {
        return antonym2Guid;
    }

    public void setAntonym2Guid(String antonym2Guid)
    {
        this.antonym2Guid = antonym2Guid;
    }


    private String description;
    /**
     * {@literal Description of the relationship. }
     * @return {@code String }
     */
    public String getDescription() {
        return this.description;
    }
    public void setDescription(String description)  {
        this.description = description;
    }
    private String expression;
    /**
     * {@literal An expression that explains the relationship. }
     * @return {@code String }
     */
    public String getExpression() {
        return this.expression;
    }
    public void setExpression(String expression)  {
        this.expression = expression;
    }
    private TermRelationshipStatus status;
    /**
     * {@literal The status of or confidence in the relationship. }
     * @return {@code TermRelationshipStatus }
     */
    public TermRelationshipStatus getStatus() {
        return this.status;
    }
    public void setStatus(TermRelationshipStatus status)  {
        this.status = status;
    }
    private String steward;
    /**
     * {@literal Person responsible for the relationship. }
     * @return {@code String }
     */
    public String getSteward() {
        return this.steward;
    }
    public void setSteward(String steward)  {
        this.steward = steward;
    }
    private String source;
    /**
     * {@literal Person, organization or automated process that created the relationship. }
     * @return {@code String }
     */
    public String getSource() {
        return this.source;
    }
    public void setSource(String source)  {
        this.source = source;
    }

    @Override
    public StringBuilder toString(StringBuilder sb)
    {
        if (sb == null)
        {
            sb = new StringBuilder();
        }
        sb.append(" Antonym=");
        sb.append(super.toString(sb));
        sb.append(" Antonym Attributes{");
        sb.append("description=").append(this.description).append(",");
        sb.append("expression=").append(this.expression).append(",");
        sb.append("steward=").append(this.steward).append(",");
        sb.append("source=").append(this.source).append(",");
        if ( status!=null) {
            sb.append("status=").append(status.name());
        }
        sb.append("}");
        return sb;
    }
    @Override
    public String toString() {
        return toString(new StringBuilder()).toString();
    }
}