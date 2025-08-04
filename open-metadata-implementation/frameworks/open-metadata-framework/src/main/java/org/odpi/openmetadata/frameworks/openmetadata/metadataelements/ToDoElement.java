/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actions.ToDoProperties;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ToDoElement contains the properties and header for a "to do" (informal task) retrieved from the metadata repository.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ToDoElement extends OpenMetadataRootElement
{
    private RelatedMetadataElementSummary       toDoSource     = null;
    private List<RelatedMetadataElementSummary> sponsors       = null;
    private List<ToDoActionTargetElement>       actionTargets  = null;


    /**
     * Default constructor
     */
    public ToDoElement()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ToDoElement(ToDoElement template)
    {
        super(template);

        if (template != null)
        {
            toDoSource     = template.getToDoSource();
            sponsors       = template.getSponsors();
            actionTargets  = template.getActionTargets();
        }
    }


    /**
     * Return the source of the To Do
     *
     * @return  element stub
     */
    public RelatedMetadataElementSummary getToDoSource()
    {
        return toDoSource;
    }


    /**
     * Set up details of the To Do
     *
     * @param toDoSource element stub
     */
    public void setToDoSource(RelatedMetadataElementSummary toDoSource)
    {
        this.toDoSource = toDoSource;
    }


    /**
     * Return the list of sponsors for this action.
     *
     * @return list of sponsors
     */
    public List<RelatedMetadataElementSummary> getSponsors()
    {
        return sponsors;
    }


    /**
     * Set up the list of sponsors for this action.
     *
     * @param sponsors list of sponsors
     */
    public void setSponsors(List<RelatedMetadataElementSummary> sponsors)
    {
        this.sponsors = sponsors;
    }


    /**
     * Return the list of action targets to work on.
     *
     * @return list
     */
    public List<ToDoActionTargetElement> getActionTargets()
    {
        return actionTargets;
    }


    /**
     * Set up the list of action targets to work on.
     *
     * @param actionTargets list
     */
    public void setActionTargets(List<ToDoActionTargetElement> actionTargets)
    {
        this.actionTargets = actionTargets;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "ToDoElement{" +
                "toDoSource=" + toDoSource +
                ", sponsors=" + sponsors +
                ", actionTargets=" + actionTargets +
                "} " + super.toString();
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
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        ToDoElement that = (ToDoElement) objectToCompare;
        return Objects.equals(toDoSource, that.toDoSource) &&
                Objects.equals(sponsors, that.sponsors) &&
                Objects.equals(actionTargets, that.actionTargets);
    }

    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), toDoSource, sponsors, actionTargets);
    }
}
