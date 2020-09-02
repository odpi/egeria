/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.OmasObject;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.SystemAttributes;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.*;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * A relationship between 2 subject area OMAS Nodes. It is contains named attributes and has 2 Line ends.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "class",
        defaultImpl = Line.class,
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = HasA.class),
        @JsonSubTypes.Type(value = RelatedTerm.class),
        @JsonSubTypes.Type(value = Synonym.class),
        @JsonSubTypes.Type(value = Antonym.class),
        @JsonSubTypes.Type(value = PreferredTerm.class),
        @JsonSubTypes.Type(value = ReplacementTerm.class),
        @JsonSubTypes.Type(value = Translation.class),
        @JsonSubTypes.Type(value = IsA.class),
        @JsonSubTypes.Type(value = ValidValue.class),
        @JsonSubTypes.Type(value = UsedInContext.class),
        @JsonSubTypes.Type(value = IsATypeOf.class),
        @JsonSubTypes.Type(value = TypedBy.class),
        @JsonSubTypes.Type(value = TermAnchor.class),
        @JsonSubTypes.Type(value = CategoryAnchor.class),
        @JsonSubTypes.Type(value = Categorization.class),
})
abstract public class Line implements Serializable, OmasObject {
    protected static final long serialVersionUID = 1L;
    private SystemAttributes systemAttributes = null;
    private Date effectiveFromTime = null;
    private Date effectiveToTime = null;
    private Map<String, String> additionalProperties;
    protected String typeDefGuid;
    protected LineType lineType;
    // this is the line name
    protected String name;
    protected LineEnd end1;
    protected LineEnd end2;

//    /**
//     * Default constructor
//     */
//    public Line() { }

    protected Line(String name, String typeDefGuid, LineEnd end1, LineEnd end2) {
        this.end1 = end1;
        this.end2 = end2;
        this.name = name;
        this.typeDefGuid = typeDefGuid;
        initialise();
    }

    protected void initialise() {
        // set the LineType if this is a LineType enum value.
        try {
            lineType = LineType.valueOf(name);
        } catch (IllegalArgumentException e) {
            lineType = LineType.Unknown;
        }
    }

    public LineType getLineType() {
        return lineType;
    }

    public void setLineType(LineType lineType) {
        this.lineType = lineType;
    }

    public SystemAttributes getSystemAttributes() {
        return systemAttributes;
    }

    public void setSystemAttributes(SystemAttributes systemAttributes) {
        this.systemAttributes = systemAttributes;
    }

    /**
     * Return the date/time that this line should start to be used (null means it can be used from creationTime).
     *
     * @return Date the line becomes effective.
     */
    public Date getEffectiveFromTime() {
        return effectiveFromTime;
    }

    public void setEffectiveFromTime(Date effectiveFromTime) {
        this.effectiveFromTime = effectiveFromTime;
    }

    /**
     * Return the date/time that this line should no longer be used.
     *
     * @return Date the line stops being effective.
     */
    public Date getEffectiveToTime() {
        return effectiveToTime;
    }

    public void setEffectiveToTime(Date effectiveToTime) {
        this.effectiveToTime = effectiveToTime;
    }

    public String getGuid() {
        if (this.systemAttributes == null) {
            return null;
        } else {
            return systemAttributes.getGUID();
        }
    }

    public void setGuid(String guid) {
        if (this.systemAttributes == null) {
            this.systemAttributes = new SystemAttributes();
        }
        this.systemAttributes.setGUID(guid);
    }

    public String getName() {
        return name;
    }

    /**
     * Get the extra attributes - ones that are in addition to the standard types.
     *
     * @return extra attributes
     */
    public Map<String, String> getAdditionalProperties() {
        return additionalProperties;
    }

    public void setAdditionalProperties(Map<String, String> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }

    public LineEnd getEnd1() {
        return end1;
    }

    public void setEnd1(LineEnd end1) {
        this.end1 = end1;
    }

    public LineEnd getEnd2() {
        return end2;
    }

    public void setEnd2(LineEnd end2) {
        this.end2 = end2;
    }

    /**
     * Get line end 1. The child Line sets the appropriate values for its Line end 1
     *
     * @return LineEnd Line end 1
     */
    public LineEnd getEnd1() {
        return end1;
    }

    public void setEnd1(LineEnd end1) {
        this.end1 = end1;
    }

    /**
     * Get line end 1. The child Line sets the appropriate values for its Line end 1
     *
     * @return LineEnd Line end 1
     */
    public LineEnd getEnd2() {
        return end2;
    }

    public void setEnd2(LineEnd end2) {
        this.end2 = end2;
    }

    public StringBuilder toString(StringBuilder sb) {
        if (sb == null) {
            sb = new StringBuilder();
        }

        sb.append("Line{");
//        sb.append("typeDefGuid=").append(typeDefGuid).append(",");
        sb.append("lineType=").append(lineType.name()).append(",");
        sb.append("name=").append(name);
        if (this.systemAttributes != null) {
            sb.append("systemAttributes { ");
            sb = this.systemAttributes.toString(sb);
            sb.append("}");
        }
        sb.append('}');
        return sb;
    }

    @Override
    public String toString() {
        return this.toString(null).toString();
    }
}