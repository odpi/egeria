/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.dataplatform.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Deprecated
public class BusinessTerm {

    private String guid;
    private String name;
    private String query;
    private String description;
    private String abbreviation;
    private String usage;
    private String summary;
    private String examples;
    private String displayName;
    private String qualifiedName;
    private GlossaryCategory glossaryCategory;


    /**
     * Return the guid of the business term
     *
     * @return guid of the business term
     */
    public String getGuid() {
        return guid;
    }

    /**
     * set up the  guid of the business term
     *
     * @param guid - guid of the business term
     */
    public void setGuid(String guid) {
        this.guid = guid;
    }

    /**
     * Return the foreignKeyName of the business term
     *
     * @return foreignKeyName of the business term
     */
    public String getName() {
        return name;
    }

    /**
     * set up the foreignKeyName of the business term
     *
     * @param name - foreignKeyName of the business term
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Return the query of the business term
     *
     * @return query of the business term
     */
    public String getQuery() {
        return query;
    }

    /**
     * set up the query of the business term
     *
     * @param query - query for the business term
     */
    public void setQuery(String query) {
        this.query = query;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getExamples() {
        return examples;
    }

    public void setExamples(String examples) {
        this.examples = examples;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public GlossaryCategory getGlossaryCategory() {
        return glossaryCategory;
    }

    public void setGlossaryCategory(GlossaryCategory glossaryCategory) {
        this.glossaryCategory = glossaryCategory;
    }

    public String getQualifiedName() {
        return qualifiedName;
    }

    public void setQualifiedName(String qualifiedName) {
        this.qualifiedName = qualifiedName;
    }

    public String buildQualifiedName() {
        StringBuilder builder = new StringBuilder(this.getName());
        GlossaryCategory parentCategory = this.getGlossaryCategory();
        while (parentCategory != null) {
            builder.insert(0, parentCategory.getName() + ".");
            parentCategory = parentCategory.getParentCategory();
        }

        return builder.toString();
    }


    @Override
    public String toString() {
        return "BusinessTerm{" +
                "guid='" + guid + '\'' +
                ", name='" + name + '\'' +
                ", query='" + query + '\'' +
                ", description='" + description + '\'' +
                ", abbreviation='" + abbreviation + '\'' +
                ", usage='" + usage + '\'' +
                ", summary='" + summary + '\'' +
                ", examples='" + examples + '\'' +
                ", displayName='" + displayName + '\'' +
                ", glossaryCategory=" + glossaryCategory +
                '}';
    }
}
