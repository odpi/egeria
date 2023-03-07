/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project */
package org.odpi.openmetadata.accessservices.subjectarea.properties.objects.nodesummary;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.Classification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * A SummaryIcon is a summary of an icon. It is used to identify a related icon.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class IconSummary {
    private static final Logger log = LoggerFactory.getLogger(IconSummary.class);
    private static final String className = IconSummary.class.getName();
    private String url = null;
    private String qualifiedName = null;
    private String guid = null;
    private String relationshipguid = null;
    private String label = null;

    /**
     * The url of the icon
     *
     * @return url
     */
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * The qualified name of the node.
     *
     * @return qualified name
     */
    public String getQualifiedName() {
        return qualifiedName;
    }

    public void setQualifiedName(String qualifiedName) {
        this.qualifiedName = qualifiedName;
    }

    /**
     * A unique identifier for a node
     *
     * @return guid
     */
    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    /**
     * The unique identifier of the associated relationship
     *
     * @return relationship guid
     */
    public String getRelationshipguid() {
        return relationshipguid;
    }

    public void setRelationshipguid(String relationshipguid) {
        this.relationshipguid = relationshipguid;
    }

    /**
     * Display name of the icon to be used as a label in user interfaces
     *
     * @return label
     */
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String toString(StringBuilder sb) {
        if (sb == null) {
            sb = new StringBuilder();
        }
        sb.append("IconSummary{");
        if (url != null) {
            sb.append("name='").append(url).append('\'');
        }
        if (qualifiedName != null) {
            sb.append(", qualifiedName='").append(qualifiedName).append('\'');
        }
        if (guid != null) {
            sb.append(", guid='").append(guid).append('\'');
        }
        if (label != null) {
            sb.append(", label='").append(label);
        }
        sb.append('}');
        return sb.toString();
    }

    @Override
    public String toString() {
        return toString(new StringBuilder());
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IconSummary node = (IconSummary) o;

        if (!Objects.equals(url, node.url)) {
            return false;
        }
        return Objects.equals(qualifiedName, node.qualifiedName);
    }

    @Override
    public int hashCode() {
        int result = url != null ? url.hashCode() : 0;
        result = 31 * result + (qualifiedName != null ? qualifiedName.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        return result;
    }

    // allow child classes to process classifications
    protected void processClassification(Classification classification) {
    }
}
