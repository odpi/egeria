/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.surveyreports;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * RelationshipAdviceAnnotationProperties is used to record a recommendation that a new relationship should be made from this data field to another
 * object in the open metadata types.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class RelationshipAdviceAnnotationProperties extends DataFieldAnnotationProperties
{
    private String              relatedEntityGUID = null;
    private String              relationshipTypeName = null;
    private Map<String, String> relationshipProperties = null;


    /**
     * Default constructor
     */
    public RelationshipAdviceAnnotationProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.RELATIONSHIP_ADVICE_ANNOTATION.typeName);
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public RelationshipAdviceAnnotationProperties(RelationshipAdviceAnnotationProperties template)
    {
        super(template);

        if (template != null)
        {
            relatedEntityGUID = template.getRelatedEntityGUID();
            relationshipTypeName = template.getRelationshipTypeName();
            relationshipProperties = template.getRelationshipProperties();
        }
    }


    /**
     * Return the unique identifier of the object to connect to.
     *
     * @return string guid
     */
    public String getRelatedEntityGUID()
    {
        return relatedEntityGUID;
    }


    /**
     * Set up the unique identifier of the object to connect to.
     *
     * @param relatedEntityGUID string guid
     */
    public void setRelatedEntityGUID(String relatedEntityGUID)
    {
        this.relatedEntityGUID = relatedEntityGUID;
    }


    /**
     * Return the type of relationship to create.
     *
     * @return type name
     */
    public String getRelationshipTypeName()
    {
        return relationshipTypeName;
    }


    /**
     * Set up the type of relationship to create.
     *
     * @param relationshipTypeName type name
     */
    public void setRelationshipTypeName(String relationshipTypeName)
    {
        this.relationshipTypeName = relationshipTypeName;
    }


    /**
     * Return the properties that should be stored in the relationship.
     *
     * @return map of property names to property values
     */
    public Map<String, String> getRelationshipProperties()
    {
        return relationshipProperties;
    }


    /**
     * Set up the properties that should be stored in the relationship.
     *
     * @param relationshipProperties map of property names to property values
     */
    public void setRelationshipProperties(Map<String, String> relationshipProperties)
    {
        this.relationshipProperties = relationshipProperties;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "RelationshipAdviceAnnotationProperties{" +
                "relatedEntityGUID='" + relatedEntityGUID + '\'' +
                ", relationshipTypeName='" + relationshipTypeName + '\'' +
                ", relationshipProperties=" + relationshipProperties +
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
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        RelationshipAdviceAnnotationProperties that = (RelationshipAdviceAnnotationProperties) objectToCompare;
        return Objects.equals(relatedEntityGUID, that.relatedEntityGUID) &&
                Objects.equals(relationshipTypeName, that.relationshipTypeName) &&
                Objects.equals(relationshipProperties, that.relationshipProperties);
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), relatedEntityGUID, relationshipTypeName, relationshipProperties);
    }
}
