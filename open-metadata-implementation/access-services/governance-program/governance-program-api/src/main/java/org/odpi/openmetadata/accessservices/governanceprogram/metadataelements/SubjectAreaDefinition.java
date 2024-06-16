/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.metadataelements;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementStub;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * A subject area defines a group of definitions for governing assets related to a specific topic.  The subject area definition defines
 * how the assets related to the topic should be managed.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SubjectAreaDefinition extends SubjectAreaElement
{
    private String            parentSubjectAreaGUID           = null;
    private List<String>      nestedSubjectAreaGUIDs          = null;
    private List<ElementStub> associatedGovernanceDefinitions = null;


    /**
     * Default Constructor
     */
    public SubjectAreaDefinition()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public SubjectAreaDefinition(SubjectAreaDefinition template)
    {
        super(template);

         if (template != null)
         {
             this.parentSubjectAreaGUID = template.getParentSubjectAreaGUID();
             this.nestedSubjectAreaGUIDs = template.getNestedSubjectAreaGUIDs();
             this.associatedGovernanceDefinitions = template.getAssociatedGovernanceDefinitions();
         }
    }


    /**
     * Copy/clone constructor for element
     *
     * @param template object to copy
     */
    public SubjectAreaDefinition(SubjectAreaElement template)
    {
        super(template);
    }


    /**
     * Return the unique identifier of the subject area that this zone inherits governance definitions from.
     *
     * @return string guid
     */
    public String getParentSubjectAreaGUID()
    {
        return parentSubjectAreaGUID;
    }


    /**
     * Set up the unique identifier of the subject area that this zone inherits governance definitions from.
     *
     * @param parentSubjectAreaGUID string guid
     */
    public void setParentSubjectAreaGUID(String parentSubjectAreaGUID)
    {
        this.parentSubjectAreaGUID = parentSubjectAreaGUID;
    }


    /**
     * Return the list of unique identifiers of the subject areas that inherit governance definitions from this zone.
     *
     * @return list of string guids
     */
    public List<String> getNestedSubjectAreaGUIDs()
    {
        return nestedSubjectAreaGUIDs;
    }


    /**
     * Set up the list of unique identifiers of the subject areas that inherit governance definitions from this zone.
     *
     * @param nestedSubjectAreaGUIDs list of string guids
     */
    public void setNestedSubjectAreaGUIDs(List<String> nestedSubjectAreaGUIDs)
    {
        this.nestedSubjectAreaGUIDs = nestedSubjectAreaGUIDs;
    }


    /**
     * Return the list of the governance definitions that control assets in this zone.
     *
     * @return list of definitions
     */
    public List<ElementStub> getAssociatedGovernanceDefinitions()
    {
        return associatedGovernanceDefinitions;
    }


    /**
     * Set up  the list of the governance definitions that control assets in this zone.
     *
     * @param associatedGovernanceDefinitions list of definitions
     */
    public void setAssociatedGovernanceDefinitions(List<ElementStub> associatedGovernanceDefinitions)
    {
        this.associatedGovernanceDefinitions = associatedGovernanceDefinitions;
    }


    /**
     * JSON-style toString
     *
     * @return string containing the properties and their values
     */
    @Override
    public String toString()
    {
        return "SubjectAreaDefinition{" +
                       "parentSubjectAreaGUID='" + parentSubjectAreaGUID + '\'' +
                       ", nestedSubjectAreaGUIDs=" + nestedSubjectAreaGUIDs +
                       ", associatedGovernanceDefinitions=" + associatedGovernanceDefinitions +
                       ", elementHeader=" + getElementHeader() +
                       ", subjectAreaProperties=" + getProperties() +
                       '}';
    }


    /**
     * Return comparison result based on the content of the properties.
     *
     * @param objectToCompare test object
     * @return result of comparison
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
        SubjectAreaDefinition that = (SubjectAreaDefinition) objectToCompare;
        return Objects.equals(parentSubjectAreaGUID, that.parentSubjectAreaGUID) &&
                       Objects.equals(nestedSubjectAreaGUIDs, that.nestedSubjectAreaGUIDs) &&
                       Objects.equals(associatedGovernanceDefinitions, that.associatedGovernanceDefinitions);
    }



    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), parentSubjectAreaGUID, nestedSubjectAreaGUIDs, associatedGovernanceDefinitions);
    }
}
