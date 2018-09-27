package org.odpi.openmetadata.accessservices.subjectarea.properties.objects.project;

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

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.SystemAttributes;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * Project entity in the Subject Area OMAS.
 An organized activity, typically to achieve a well defined goal.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class  Project implements Serializable {
    private static final Logger log = LoggerFactory.getLogger(Project.class);
    private static final String className = Project.class.getName();
    private SystemAttributes systemAttributes = null;
    List<Classification> classifications = null;

    private Map<String, Object> extraAttributes =null;
    private Map<String, org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification> extraClassifications =null;


    /**
     * Get the system attributes
     * @return system attributes
     */
    public SystemAttributes getSystemAttributes() {
        return systemAttributes;
    }

    public void setSystemAttributes(SystemAttributes systemAttributes) {
        this.systemAttributes = systemAttributes;
    }

    // attributes
    public static final String[] PROPERTY_NAMES_SET_VALUES = new String[] {
            "name",
            "description",
            "startDate",
            "plannedEndDate",
            "status",
            "qualifiedName",
            "additionalProperties",

            // Terminate the list
            null
    };
    public static final String[] ATTRIBUTE_NAMES_SET_VALUES = new String[] {
            "name",
            "description",
            "startDate",
            "plannedEndDate",
            "status",
            "qualifiedName",

            // Terminate the list
            null
    };
    public static final String[] ENUM_NAMES_SET_VALUES = new String[] {

            // Terminate the list
            null
    };
    public static final String[] MAP_NAMES_SET_VALUES = new String[] {
            "additionalProperties",

            // Terminate the list
            null
    };
    public static final java.util.Set<String> PROPERTY_NAMES_SET = new HashSet(new HashSet<>(Arrays.asList(PROPERTY_NAMES_SET_VALUES)));
    public static final java.util.Set<String> ATTRIBUTE_NAMES_SET = new HashSet(new HashSet<>(Arrays.asList(ATTRIBUTE_NAMES_SET_VALUES)));
    public static final java.util.Set<String> ENUM_NAMES_SET = new HashSet(new HashSet<>(Arrays.asList(ENUM_NAMES_SET_VALUES)));
    public static final java.util.Set<String> MAP_NAMES_SET = new HashSet(new HashSet<>(Arrays.asList(MAP_NAMES_SET_VALUES)));


    InstanceProperties obtainInstanceProperties() {
        final String methodName = "obtainInstanceProperties";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName);
        }
        InstanceProperties instanceProperties = new InstanceProperties();
        EnumPropertyValue enumPropertyValue=null;
        MapPropertyValue mapPropertyValue=null;
        // Additional properties for the element.
        mapPropertyValue = new MapPropertyValue();
        PrimitivePropertyValue primitivePropertyValue=null;
        primitivePropertyValue = new PrimitivePropertyValue();
        primitivePropertyValue.setPrimitiveValue(name);
        instanceProperties.setProperty("name",primitivePropertyValue);
        primitivePropertyValue = new PrimitivePropertyValue();
        primitivePropertyValue.setPrimitiveValue(description);
        instanceProperties.setProperty("description",primitivePropertyValue);
        primitivePropertyValue = new PrimitivePropertyValue();
        primitivePropertyValue.setPrimitiveValue(startDate);
        instanceProperties.setProperty("startDate",primitivePropertyValue);
        primitivePropertyValue = new PrimitivePropertyValue();
        primitivePropertyValue.setPrimitiveValue(plannedEndDate);
        instanceProperties.setProperty("plannedEndDate",primitivePropertyValue);
        primitivePropertyValue = new PrimitivePropertyValue();
        primitivePropertyValue.setPrimitiveValue(status);
        instanceProperties.setProperty("status",primitivePropertyValue);
        primitivePropertyValue = new PrimitivePropertyValue();
        primitivePropertyValue.setPrimitiveValue(qualifiedName);
        instanceProperties.setProperty("qualifiedName",primitivePropertyValue);
        if (log.isDebugEnabled()) {
            log.debug("<== Method: " + methodName);
        }
        return instanceProperties;
    }

    private String name;
    /**
     * Name of the project.
     * @return String
     */
    public String getName() {
        return this.name;
    }
    public void setName(String name)  {
        this.name = name;
    }
    private String description;
    /**
     * Description of the project.
     * @return String
     */
    public String getDescription() {
        return this.description;
    }
    public void setDescription(String description)  {
        this.description = description;
    }
    private Date startDate;
    /**
     * Start date of the project.
     * @return Date
     */
    public Date getStartDate() {
        return this.startDate;
    }
    public void setStartDate(Date startDate)  {
        this.startDate = startDate;
    }
    private Date plannedEndDate;
    /**
     * Planned completion data for the project.
     * @return Date
     */
    public Date getPlannedEndDate() {
        return this.plannedEndDate;
    }
    public void setPlannedEndDate(Date plannedEndDate)  {
        this.plannedEndDate = plannedEndDate;
    }
    private String status;
    /**
     * Short description on current status of the project.
     * @return String
     */
    public String getStatus() {
        return this.status;
    }
    public void setStatus(String status)  {
        this.status = status;
    }
    private String qualifiedName;
    /**
     * Unique identifier for the entity.
     * @return String
     */
    public String getQualifiedName() {
        return this.qualifiedName;
    }
    public void setQualifiedName(String qualifiedName)  {
        this.qualifiedName = qualifiedName;
    }
    private Map<String,String> additionalProperties;
    /**
     * Additional properties for the element.
     * @return {@code Map<String,String> }
     */
    public Map<String,String> getAdditionalProperties() {
        return this.additionalProperties;
    }
    public void setAdditionalProperties(Map<String,String> additionalProperties)  {
        this.additionalProperties = additionalProperties;
    }

    public void setExtraAttributes(Map<String, Object> extraAttributes) {
        this.extraAttributes = extraAttributes;
    }

    public void setClassifications(List<Classification> classifications) {
        this.classifications = classifications;
    }

    /**
     * Get the extra attributes - ones that are in addition to the standard types.
     * @return extra attributes
     */
    public Map<String, Object> getExtraAttributes() {
        return extraAttributes;
    }

    /**
     * Classifications
     * @return classifications
     */
    public List<Classification> getClassifications() {
        return classifications;
    }
    /**
     * Extra classifications are classifications that are not in the open metadata model - we include the OMRS Classifications.
     * @return {@code Map<String, org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification> }
     */
    public Map<String, org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification> getExtraClassifications() {
        return extraClassifications;
    }

    public void setExtraClassifications(Map<String, org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification> extraClassifications) {
        this.extraClassifications = extraClassifications;
    }

    public StringBuilder toString(StringBuilder sb) {
        if (sb == null) {
            sb = new StringBuilder();
        }

        sb.append("Project{");
        if (systemAttributes !=null) {
            sb.append("systemAttributes='").append(systemAttributes.toString()).append('\'');
        }
        sb.append("Project Attributes{");
        sb.append("Name=" +this.name);
        sb.append("Description=" +this.description);
        sb.append("StartDate=" +this.startDate);
        sb.append("PlannedEndDate=" +this.plannedEndDate);
        sb.append("Status=" +this.status);
        sb.append("QualifiedName=" +this.qualifiedName);
        sb.append("AdditionalProperties=" +this.additionalProperties);

        sb.append('}');
        if (classifications != null) {
            sb.append(", classifications=[");
            for (Classification classification:classifications) {
                sb.append(classification.toString()).append(", ");
            }
            sb.append(" ],");
        }
        sb.append(", extraAttributes=[");
        if (extraAttributes !=null) {
            for (String attrname: extraAttributes.keySet()) {
                sb.append(attrname).append(":");
                sb.append(extraAttributes.get(attrname)).append(", ");
            }
        }
        sb.append(" ]");

        sb.append('}');

        return sb;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        if (!super.equals(o)) { return false; }

        Project that = (Project) o;
        if (this.name != null && !Objects.equals(this.name,that.getName())) {
            return false;
        }
        if (this.description != null && !Objects.equals(this.description,that.getDescription())) {
            return false;
        }
        if (this.startDate != null && !Objects.equals(this.startDate,that.getStartDate())) {
            return false;
        }
        if (this.plannedEndDate != null && !Objects.equals(this.plannedEndDate,that.getPlannedEndDate())) {
            return false;
        }
        if (this.status != null && !Objects.equals(this.status,that.getStatus())) {
            return false;
        }
        if (this.qualifiedName != null && !Objects.equals(this.qualifiedName,that.getQualifiedName())) {
            return false;
        }
        if (this.additionalProperties != null && !Objects.equals(this.additionalProperties,that.getAdditionalProperties())) {
            return false;
        }

        // We view projects as logically equal by checking the properties that the OMAS knows about - i.e. without accounting for extra attributes and references from the org.odpi.openmetadata.accessservices.subjectarea.server.
        return Objects.equals(systemAttributes, that.systemAttributes) &&
                Objects.equals(classifications, that.classifications) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(),
                systemAttributes.hashCode(),
                classifications.hashCode()
                , this.name
                , this.description
                , this.startDate
                , this.plannedEndDate
                , this.status
                , this.qualifiedName
                , this.additionalProperties
        );
    }

    @Override
    public String toString() {
        return toString(new StringBuilder()).toString();
    }
}

