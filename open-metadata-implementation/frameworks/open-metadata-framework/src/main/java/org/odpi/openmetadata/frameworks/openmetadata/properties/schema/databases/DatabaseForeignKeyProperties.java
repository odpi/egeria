/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.schema.databases;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DatabaseForeignKeyProperties describes a foreign key relationship between two columns in different
 * tables in a database.  The foreign key shows where the primary key of one table is used in another
 * table to show they are related.  Foreign key relationships are typically discovered from analysis
 * of the values stored in the rows, or asserted by the DBA/steward.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DatabaseForeignKeyProperties extends RelationshipProperties
{
    private String name        = null;
    private String description = null;
    private int    confidence  = 0;
    private String steward     = null;
    private String source      = null;


    /**
     * Default constructor
     */
    public DatabaseForeignKeyProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.FOREIGN_KEY_RELATIONSHIP.typeName);
    }


    /**
     * Copy/clone constructor for an foreign key.
     *
     * @param template template object to copy.
     */
    public DatabaseForeignKeyProperties(DatabaseForeignKeyProperties template)
    {
        super(template);

        if (template != null)
        {
            name        = template.getName();
            description = template.getDescription();
            confidence  = template.getConfidence();
            steward     = template.getSteward();
            source      = template.getSource();
        }
    }


    /**
     * Set up the display name for UIs and reports.
     *
     * @param name String name
     */
    public void setName(String name)
    {
        this.name = name;
    }


    /**
     * Returns the stored display name property for the foreign key.
     *
     * @return String name
     */
    public String getName()
    {
        return name;
    }


    /**
     * Set up description of the foreign key.
     *
     * @param description String
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Return the description for the foreign key.
     *
     * @return String description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the level of confidence that the foreign key is correct.  This is a value between 0 and 100.
     *
     * @param confidence int
     */
    public void setConfidence(int confidence)
    {
        this.confidence = confidence;
    }


    /**
     * Returns the level of confidence that the foreign key is correct.  This is a value between 0 and 100.
     *
     * @return int
     */
    public int getConfidence()
    {
        return confidence;
    }


    /**
     * Set up the id of the steward who assigned the foreign key (or approved the discovered value).
     *
     * @param steward user id or name of steward
     */
    public void setSteward(String steward)
    {
        this.steward = steward;
    }


    /**
     * Returns the name of the steward who assigned the foreign key (or approved the discovered value).
     *
     * @return user id or name of steward
     */
    public String getSteward()
    {
        return steward;
    }


    /**
     * Set up the id of the source of the knowledge of the foreign key.
     *
     * @param source String id
     */
    public void setSource(String source)
    {
        this.source = source;
    }


    /**
     * Returns the id of the source of the knowledge of the foreign key.
     *
     * @return String id
     */
    public String getSource()
    {
        return source;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "DatabaseForeignKeyProperties{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", confidence=" + confidence +
                ", steward='" + steward + '\'' +
                ", source='" + source + '\'' +
                "} " + super.toString();
    }


    /**
     * Compare the values of the supplied object with those stored in the current object.
     *
     * @param objectToCompare supplied object
     * @return boolean result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (! (objectToCompare instanceof DatabaseForeignKeyProperties))
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        DatabaseForeignKeyProperties that = (DatabaseForeignKeyProperties) objectToCompare;
        return confidence == that.confidence &&
                       Objects.equals(name, that.name) &&
                       Objects.equals(description, that.description) &&
                       Objects.equals(steward, that.steward) &&
                       Objects.equals(source, that.source);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), name, description, confidence, steward, source);
    }
}
