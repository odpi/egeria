/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

// This is a generated file - do not edit - changes should be made to the templates amd/or generator to generate this file with changes.

package org.odpi.openmetadata.fvt.opentypes.entities.Regulation;

import java.io.Serializable;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.odpi.openmetadata.fvt.opentypes.common.SystemAttributes;
import org.odpi.openmetadata.fvt.opentypes.common.ClassificationBean;
import org.odpi.openmetadata.fvt.opentypes.enums.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

// omrs
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EnumPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.MapPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.PrimitivePropertyValue;

/**
 * Regulation entity.
   Identifies a regulation related to data that must be supported.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class  Regulation implements Serializable {
    private static final Logger log = LoggerFactory.getLogger(Regulation.class);
    private static final String className = Regulation.class.getName();
    private SystemAttributes systemAttributes = null;
    private Date effectiveFromTime = null;
    private Date effectiveToTime = null;
    List<ClassificationBean> classifications = null;

    private Map<String, Object> extraAttributes =null;
    private Map<String, org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification> extraClassificationBeans =null;


    /**
     * Get the system attributes
     * @return SystemAttributes if populated, null otherwise.
     */
    public SystemAttributes getSystemAttributes() {
        return systemAttributes;
    }

    public void setSystemAttributes(SystemAttributes systemAttributes) {
        this.systemAttributes = systemAttributes;
    }

    // attributes
    public static final String[] PROPERTY_NAMES_SET_VALUES = new String[] {
        "jurisdiction",
        "title",
        "summary",
        "description",
        "scope",
        "domain",
        "priority",
        "implications",
        "outcomes",
        "results",
        "qualifiedName",
        "additionalProperties",

    // Terminate the list
        null
    };
    public static final String[] ATTRIBUTE_NAMES_SET_VALUES = new String[] {
        "jurisdiction",
        "title",
        "summary",
        "description",
        "scope",
        "priority",
        "implications",
        "outcomes",
        "results",
        "qualifiedName",

     // Terminate the list
        null
    };
    public static final String[] ENUM_NAMES_SET_VALUES = new String[] {
         "domain",

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
        enumPropertyValue = new EnumPropertyValue();
        // governance domain for this governance definition.
        enumPropertyValue.setOrdinal(domain.ordinal());
        enumPropertyValue.setSymbolicName(domain.name());
        instanceProperties.setProperty("domain",enumPropertyValue);
        MapPropertyValue mapPropertyValue=null;
        // Additional properties for the element.
        mapPropertyValue = new MapPropertyValue();
        PrimitivePropertyValue primitivePropertyValue=null;
        primitivePropertyValue = new PrimitivePropertyValue();
        primitivePropertyValue.setPrimitiveValue(jurisdiction);
        instanceProperties.setProperty("jurisdiction",primitivePropertyValue);
        primitivePropertyValue = new PrimitivePropertyValue();
        primitivePropertyValue.setPrimitiveValue(title);
        instanceProperties.setProperty("title",primitivePropertyValue);
        primitivePropertyValue = new PrimitivePropertyValue();
        primitivePropertyValue.setPrimitiveValue(summary);
        instanceProperties.setProperty("summary",primitivePropertyValue);
        primitivePropertyValue = new PrimitivePropertyValue();
        primitivePropertyValue.setPrimitiveValue(description);
        instanceProperties.setProperty("description",primitivePropertyValue);
        primitivePropertyValue = new PrimitivePropertyValue();
        primitivePropertyValue.setPrimitiveValue(scope);
        instanceProperties.setProperty("scope",primitivePropertyValue);
        primitivePropertyValue = new PrimitivePropertyValue();
        primitivePropertyValue.setPrimitiveValue(priority);
        instanceProperties.setProperty("priority",primitivePropertyValue);
        primitivePropertyValue = new PrimitivePropertyValue();
        primitivePropertyValue.setPrimitiveValue(implications);
        instanceProperties.setProperty("implications",primitivePropertyValue);
        primitivePropertyValue = new PrimitivePropertyValue();
        primitivePropertyValue.setPrimitiveValue(outcomes);
        instanceProperties.setProperty("outcomes",primitivePropertyValue);
        primitivePropertyValue = new PrimitivePropertyValue();
        primitivePropertyValue.setPrimitiveValue(results);
        instanceProperties.setProperty("results",primitivePropertyValue);
        primitivePropertyValue = new PrimitivePropertyValue();
        primitivePropertyValue.setPrimitiveValue(qualifiedName);
        instanceProperties.setProperty("qualifiedName",primitivePropertyValue);
        if (log.isDebugEnabled()) {
               log.debug("<== Method: " + methodName);
        }
        return instanceProperties;
    }

       private String jurisdiction;
       /**
        * {@literal Issuing authority for the regulation. }
        * @return {@code String }
        */
       public String getJurisdiction() {
           return this.jurisdiction;
       }
       public void setJurisdiction(String jurisdiction)  {
           this.jurisdiction = jurisdiction;
       }
       private String title;
       /**
        * {@literal Title describing the governance definition. }
        * @return {@code String }
        */
       public String getTitle() {
           return this.title;
       }
       public void setTitle(String title)  {
           this.title = title;
       }
       private String summary;
       /**
        * {@literal Short summary of the governance definition. }
        * @return {@code String }
        */
       public String getSummary() {
           return this.summary;
       }
       public void setSummary(String summary)  {
           this.summary = summary;
       }
       private String description;
       /**
        * {@literal Detailed description of the governance definition. }
        * @return {@code String }
        */
       public String getDescription() {
           return this.description;
       }
       public void setDescription(String description)  {
           this.description = description;
       }
       private String scope;
       /**
        * {@literal Scope of impact for this governance definition. }
        * @return {@code String }
        */
       public String getScope() {
           return this.scope;
       }
       public void setScope(String scope)  {
           this.scope = scope;
       }
       private GovernanceDomain domain;
       /**
        * {@literal Governance domain for this governance definition. }
        * @return {@code GovernanceDomain }
        */
       public GovernanceDomain getDomain() {
           return this.domain;
       }
       public void setDomain(GovernanceDomain domain)  {
           this.domain = domain;
       }
       private String priority;
       /**
        * {@literal Relative importance of this governance definition compared to its peers. }
        * @return {@code String }
        */
       public String getPriority() {
           return this.priority;
       }
       public void setPriority(String priority)  {
           this.priority = priority;
       }
       private List<String> implications;
       /**
        * {@literal Impact on the organization, people and services when adopting the recommendation in this governance definition. }
        * @return {@code {@code List<String> } }
        */
       public List<String> getImplications() {
           return this.implications;
       }
       public void setImplications(List<String> implications)  {
           this.implications = implications;
       }
       private List<String> outcomes;
       /**
        * {@literal Expected outcomes. }
        * @return {@code {@code List<String> } }
        */
       public List<String> getOutcomes() {
           return this.outcomes;
       }
       public void setOutcomes(List<String> outcomes)  {
           this.outcomes = outcomes;
       }
       private List<String> results;
       /**
        * {@literal Actual results. }
        * @return {@code {@code List<String> } }
        */
       public List<String> getResults() {
           return this.results;
       }
       public void setResults(List<String> results)  {
           this.results = results;
       }
       private String qualifiedName;
       /**
        * {@literal Unique identifier for the entity. }
        * @return {@code String }
        */
       public String getQualifiedName() {
           return this.qualifiedName;
       }
       public void setQualifiedName(String qualifiedName)  {
           this.qualifiedName = qualifiedName;
       }
       private Map<String,String> additionalProperties;
       /**
        * {@literal Additional properties for the element. }
        * @return {@code {@code Map<String,String> } }
        */
       public Map<String,String> getAdditionalProperties() {
           return this.additionalProperties;
       }
       public void setAdditionalProperties(Map<String,String> additionalProperties)  {
           this.additionalProperties = additionalProperties;
       }
    /**
     * Return the date/time that this Regulation should start to be used (null means it can be used from creationTime).
     * @return Date the Regulation becomes effective.
     */
    public Date getEffectiveFromTime()
    {
        return effectiveFromTime;
    }

    public void setEffectiveFromTime(Date effectiveFromTime)
    {
        this.effectiveFromTime = effectiveFromTime;
    }
    /**
     * Return the date/time that this Regulation should no longer be used.
     *
     * @return Date the Regulation stops being effective.
     */
    public Date getEffectiveToTime()
    {
        return effectiveToTime;
    }
    public void setEffectiveToTime(Date effectiveToTime)
    {
        this.effectiveToTime = effectiveToTime;
    }

    public void setExtraAttributes(Map<String, Object> extraAttributes) {
        this.extraAttributes = extraAttributes;
    }

    public void setClassificationBeans(List<ClassificationBean> classifications) {
        this.classifications = classifications;
    }

    /**
     * Get the extra attributes - ones that are in addition to the standard types.
     * @return map of attributes, null if there are none
     */
    public Map<String, Object> getExtraAttributes() {
        return extraAttributes;
    }

     /**
     * ClassificationBeans
     * @return List of ClassificationBeans, null if there are none
     */
    public List<ClassificationBean> getClassificationBeans() {
        return classifications;
    }
    /**
      * Extra classifications are classifications that are not in the open metadata model - we include the OMRS ClassificationBeans.
      * @return Map of classifications with the classification Name as the map key
      */
    public Map<String, org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification> getExtraClassificationBeans() {
        return extraClassificationBeans;
    }

    public void setExtraClassificationBeans(Map<String, org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification> extraClassificationBeans) {
        this.extraClassificationBeans = extraClassificationBeans;
    }

    public StringBuilder toString(StringBuilder sb) {
        if (sb == null) {
            sb = new StringBuilder();
        }

        sb.append("Regulation{");
        if (systemAttributes !=null) {
            sb.append("systemAttributes='").append(systemAttributes.toString()).append('\'');
        }
        sb.append("Regulation Attributes{");
    	sb.append("Jurisdiction=" +this.jurisdiction);
    	sb.append("Title=" +this.title);
    	sb.append("Summary=" +this.summary);
    	sb.append("Description=" +this.description);
    	sb.append("Scope=" +this.scope);
    	sb.append("Domain=" +this.domain);
    	sb.append("Priority=" +this.priority);
    	sb.append("Implications=" +this.implications);
    	sb.append("Outcomes=" +this.outcomes);
    	sb.append("Results=" +this.results);
    	sb.append("QualifiedName=" +this.qualifiedName);
    	sb.append("AdditionalProperties=" +this.additionalProperties);

        sb.append('}');
        if (classifications != null) {
        sb.append(", classifications=[");
            for (ClassificationBean classification:classifications) {
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

        Regulation that = (Regulation) o;
        if (this.jurisdiction != null && !Objects.equals(this.jurisdiction,that.getJurisdiction())) {
             return false;
        }
        if (this.title != null && !Objects.equals(this.title,that.getTitle())) {
             return false;
        }
        if (this.summary != null && !Objects.equals(this.summary,that.getSummary())) {
             return false;
        }
        if (this.description != null && !Objects.equals(this.description,that.getDescription())) {
             return false;
        }
        if (this.scope != null && !Objects.equals(this.scope,that.getScope())) {
             return false;
        }
        if (this.domain != null && !Objects.equals(this.domain,that.getDomain())) {
             return false;
        }
        if (this.priority != null && !Objects.equals(this.priority,that.getPriority())) {
             return false;
        }
        if (this.implications != null && !Objects.equals(this.implications,that.getImplications())) {
             return false;
        }
        if (this.outcomes != null && !Objects.equals(this.outcomes,that.getOutcomes())) {
             return false;
        }
        if (this.results != null && !Objects.equals(this.results,that.getResults())) {
             return false;
        }
        if (this.qualifiedName != null && !Objects.equals(this.qualifiedName,that.getQualifiedName())) {
             return false;
        }
        if (this.additionalProperties != null && !Objects.equals(this.additionalProperties,that.getAdditionalProperties())) {
             return false;
        }

        // We view regulations as logically equal by checking the properties that the OMAS knows about - i.e. without accounting for extra attributes and references from the org.odpi.openmetadata.accessservices.subjectarea.server.
        return Objects.equals(systemAttributes, that.systemAttributes) &&
                Objects.equals(classifications, that.classifications) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(),
         systemAttributes.hashCode(),
         classifications.hashCode()
          , this.jurisdiction
          , this.title
          , this.summary
          , this.description
          , this.scope
          , this.domain
          , this.priority
          , this.implications
          , this.outcomes
          , this.results
          , this.qualifiedName
          , this.additionalProperties
        );
    }

    @Override
    public String toString() {
        return toString(new StringBuilder()).toString();
    }
}
