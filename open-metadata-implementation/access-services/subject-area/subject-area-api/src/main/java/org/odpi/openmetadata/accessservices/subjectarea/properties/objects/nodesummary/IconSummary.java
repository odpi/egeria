/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.odpi.openmetadata.accessservices.subjectarea.properties.objects.nodesummary;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.Classification;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.node.NodeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * A SummaryIcon is a summary of an icon. It is used to identify a related icon.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class IconSummary  {
    private static final Logger log = LoggerFactory.getLogger(IconSummary.class);
    private static final String className = IconSummary.class.getName();
    private String url =null;
    private String qualifiedName =null;
    private String guid = null;
    private String relationshipguid = null;

    /**
     * The url of the icon
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
     * @return guid
     */
    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    /**
     * The unique identifier of the associated Line (relationship)
     * @return relationship guid
     */
    public String getRelationshipguid() {
        return relationshipguid;
    }

    public void setRelationshipguid(String relationshipguid) {
        this.relationshipguid = relationshipguid;
    }

    public StringBuilder toString(StringBuilder sb) {
        if (sb == null) {
            sb = new StringBuilder();
        }

        sb.append("IconSummary{");
        if (url !=null) {
            sb.append("name='").append(url).append('\'');
        }
        if (qualifiedName!=null) {
            sb.append(", qualifiedName='").append(qualifiedName).append('\'');
        }
        if (guid != null) {
            sb.append(", guid='").append(guid).append('\'');
        }

        sb.append('}');

        return sb;
    }

    @Override
    public String toString() {
        return toString(new StringBuilder()).toString();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IconSummary node = (IconSummary) o;

        if (url != null ? !url.equals(node.url) : node.url != null) return false;
        if (qualifiedName != null ? !qualifiedName.equals(node.qualifiedName) : node.qualifiedName != null)
            return false;
       return true;
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
