/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.cache;

import clojure.lang.Keyword;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mapping.AbstractMapping;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefAttribute;

import java.util.Objects;

/**
 * Captures the key characteristics of a property mapping.
 */
public class PropertyKeywords {

    private final TypeDefAttribute attribute;
    private final String simpleName;
    private final String embeddedPath;
    private final String searchablePath;

    public PropertyKeywords(String namespace,
                            String typeName,
                            String propertyName,
                            TypeDefAttribute attribute) {
        this.attribute = attribute;
        this.simpleName = propertyName;
        embeddedPath = getSerializedPropertyKeyword(namespace, propertyName);
        searchablePath = getSearchableValueKeyword(namespace, typeName, propertyName);
    }

    /**
     * Retrieve the attribute definition for this property.
     * @return TypeDefAttribute
     */
    public TypeDefAttribute getAttribute() {
        return attribute;
    }

    /**
     * Retrieve the keyword to be used for storing embedded JSON-serialized values.
     * @return Keyword
     */
    public Keyword getEmbeddedKeyword() {
        return Keyword.intern(embeddedPath);
    }

    /**
     * Retrieve the string representation of the keyword to be used for storing embedded JSON-serialized values.
     * @return String
     */
    public String getEmbeddedPath() {
        return embeddedPath;
    }

    /**
     * Retrieve the keyword to be used for storing a searchable value.
     * @return Keyword
     */
    public Keyword getSearchableKeyword() {
        return Keyword.intern(searchablePath);
    }

    /**
     * Retrieve the string representation of the keyword to be used for storing a searchable value.
     * @return String
     */
    public String getSearchablePath() {
        return searchablePath;
    }

    /**
     * Retrieve the simple name of the property (unqualified), primarily for logging purposes.
     * @return String
     */
    public String getPropertyName() {
        return simpleName;
    }

    /**
     * Retrieve the keyword to use to store the serialized value of the property.
     * @param namespace by which to qualify the property
     * @param propertyName of the property
     * @return String giving the qualified keyword
     */
    public static String getSerializedPropertyKeyword(String namespace, String propertyName) {
        return AbstractMapping.getKeyword(namespace, propertyName + ".json");
    }

    /**
     * Retrieve the keyword to use to store a searchable value for the property.
     * @param namespace by which to qualify the property
     * @param typeName by which to qualify the property
     * @param propertyName of the property
     * @return Keyword giving the qualified keyword for a searchable value
     */
    public static String getSearchableValueKeyword(String namespace, String typeName, String propertyName) {
        return AbstractMapping.getKeyword(namespace, typeName + getEndsWithPropertyNameForMatching(propertyName));
    }

    /**
     * Retrieve a partially-qualified property name that can be used to compare a Lucene match using ends-with.
     * @param propertyName of the property to reference
     * @return String match-able ending to the property (without any type qualification)
     */
    public static String getEndsWithPropertyNameForMatching(String propertyName) {
        return "." + propertyName + ".value";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "PropertyKeywords{" +
                "attribute=" + attribute +
                ", simpleName='" + simpleName + '\'' +
                ", embeddedPath='" + embeddedPath + '\'' +
                ", searchablePath='" + searchablePath + '\'' +
                ", embeddedKeyword=" + getEmbeddedKeyword() +
                ", searchableKeyword=" + getSearchableKeyword() +
                ", propertyName='" + getPropertyName() + '\'' +
                '}';
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PropertyKeywords that = (PropertyKeywords) o;
        return Objects.equals(attribute, that.attribute) &&
                Objects.equals(simpleName, that.simpleName) &&
                Objects.equals(embeddedPath, that.embeddedPath) &&
                Objects.equals(searchablePath, that.searchablePath);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(attribute, simpleName, embeddedPath, searchablePath);
    }

}
