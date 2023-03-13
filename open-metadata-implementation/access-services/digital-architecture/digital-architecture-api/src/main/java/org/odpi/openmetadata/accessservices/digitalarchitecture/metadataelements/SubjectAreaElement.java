/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.digitalarchitecture.metadataelements;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.digitalarchitecture.properties.SubjectAreaProperties;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementHeader;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementStub;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * SubjectAreaElement is the bean used to return a subject area definition stored in the open metadata repositories.
 * It includes links to related subject areas and the governance definitions associated with the subject areas.
 * More information about the governance definitions can be retrieved through the Governance Program OMAS.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SubjectAreaElement implements Serializable, MetadataElement
{
    private static final long serialVersionUID = 1L;

    private ElementHeader         elementHeader                   = null;
    private SubjectAreaProperties properties                      = null;
    private String                parentSubjectAreaGUID           = null;
    private List<String>          nestedSubjectAreaGUIDs          = null;
    private List<ElementStub>     associatedGovernanceDefinitions = null;


    /**
     * Default constructor
     */
    public SubjectAreaElement()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public SubjectAreaElement(SubjectAreaElement template)
    {
        if (template != null)
        {
            elementHeader = template.getElementHeader();
            properties = template.getProperties();
            parentSubjectAreaGUID = template.getParentSubjectAreaGUID();
            nestedSubjectAreaGUIDs = template.getNestedSubjectAreaGUIDs();
            associatedGovernanceDefinitions = template.getAssociatedGovernanceDefinitions();   }
    }


    /**
     * Return the element header associated with the properties.
     *
     * @return element header object
     */
    public ElementHeader getElementHeader()
    {
        return elementHeader;
    }


    /**
     * Set up the element header associated with the properties.
     *
     * @param elementHeader element header object
     */
    public void setElementHeader(ElementHeader elementHeader)
    {
        this.elementHeader = elementHeader;
    }


    /**
     * Return the properties of the subject area.
     *
     * @return properties bean
     */
    public SubjectAreaProperties getProperties()
    {
        return properties;
    }


    /**
     * Set up the properties of the subject area.
     *
     * @param properties properties bean
     */
    public void setProperties(SubjectAreaProperties properties)
    {
        this.properties = properties;
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
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "SubjectAreaElement{" +
                       "elementHeader=" + elementHeader +
                       ", properties=" + properties +
                       ", parentSubjectAreaGUID='" + parentSubjectAreaGUID + '\'' +
                       ", nestedSubjectAreaGUIDs=" + nestedSubjectAreaGUIDs +
                       ", associatedGovernanceDefinitions=" + associatedGovernanceDefinitions +
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
        if (! (objectToCompare instanceof SubjectAreaElement))
        {
            return false;
        }
        SubjectAreaElement that = (SubjectAreaElement) objectToCompare;
        return Objects.equals(elementHeader, that.elementHeader) &&
                       Objects.equals(properties, that.properties) &&
                       Objects.equals(parentSubjectAreaGUID, that.parentSubjectAreaGUID) &&
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
        return Objects.hash(super.hashCode(), elementHeader, properties);
    }
}
