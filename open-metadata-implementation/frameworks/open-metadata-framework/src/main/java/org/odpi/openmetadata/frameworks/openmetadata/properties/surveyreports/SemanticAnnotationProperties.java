/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.surveyreports;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * SemanticAnnotationProperties provides a recommendation as to the likely meaning of data.  This can be expressed informally with
 * explicit strings, or via lists of GUIDs that match elements in a formal glossary.  It can be attached to an asset
 * or a data field within an asset.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)

public class SemanticAnnotationProperties extends DataFieldAnnotationProperties
{
    private String       informalTerm                 = null;
    private String       subjectAreaName              = null;
    private List<String> candidateGlossaryTermGUIDs   = null;
    private List<String> candidateGlossaryFolderGUIDs = null;

    /**
     * Default constructor
     */
    public SemanticAnnotationProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.SEMANTIC_ANNOTATION.typeName);
    }


    /**
     * Copy clone constructor
     *
     * @param template object to copy
     */
    public SemanticAnnotationProperties(SemanticAnnotationProperties template)
    {
        super(template);

        if (template != null)
        {
            informalTerm               = template.getInformalTerm();
            subjectAreaName            = template.getSubjectAreaName();
            candidateGlossaryTermGUIDs   = template.getCandidateGlossaryTermGUIDs();
            candidateGlossaryFolderGUIDs = template.getCandidateGlossaryFolderGUIDs();
        }
    }


    /**
     * Return a string that describes the meaning of this data.
     *
     * @return string name
     */
    public String getInformalTerm()
    {
        return informalTerm;
    }


    /**
     * Set up a string that describes the meaning of this data.
     *
     * @param informalTerm string name
     */
    public void setInformalTerm(String informalTerm)
    {
        this.informalTerm = informalTerm;
    }


    /**
     * Return a string that describes the topic that this data is about.
     *
     * @return string name
     */
    public String getSubjectAreaName()
    {
        return subjectAreaName;
    }


    /**
     * Set up a string that describes the topic that this data is about.
     *
     * @param subjectAreaName string name
     */
    public void setSubjectAreaName(String subjectAreaName)
    {
        this.subjectAreaName = subjectAreaName;
    }


    /**
     * Return a list of unique identifiers of glossary terms that describe the meaning of the data.
     *
     * @return list of guids
     */
    public List<String> getCandidateGlossaryTermGUIDs()
    {
        return candidateGlossaryTermGUIDs;
    }


    /**
     * Set up  a list of unique identifiers of glossary terms that describe the meaning of the data.
     *
     * @param candidateGlossaryTermGUIDs list of guids
     */
    public void setCandidateGlossaryTermGUIDs(List<String> candidateGlossaryTermGUIDs)
    {
        this.candidateGlossaryTermGUIDs = candidateGlossaryTermGUIDs;
    }


    /**
     * Return a list of unique identifiers of glossary categories that describe the topic of the data.
     *
     * @return list of guids
     */
    public List<String> getCandidateGlossaryFolderGUIDs()
    {
        return candidateGlossaryFolderGUIDs;
    }


    /**
     * Set up a list of unique identifiers of glossary categories that describe the topic of the data.
     *
     * @param candidateGlossaryFolderGUIDs list of guids
     */
    public void setCandidateGlossaryFolderGUIDs(List<String> candidateGlossaryFolderGUIDs)
    {
        this.candidateGlossaryFolderGUIDs = candidateGlossaryFolderGUIDs;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "SemanticAnnotationProperties{" +
                "informalTerm='" + informalTerm + '\'' +
                ", subjectAreaName='" + subjectAreaName + '\'' +
                ", candidateGlossaryTermGUIDs=" + candidateGlossaryTermGUIDs +
                ", candidateGlossaryFolderGUIDs=" + candidateGlossaryFolderGUIDs +
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
        SemanticAnnotationProperties that = (SemanticAnnotationProperties) objectToCompare;
        return Objects.equals(informalTerm, that.informalTerm) &&
                Objects.equals(subjectAreaName, that.subjectAreaName) &&
                Objects.equals(candidateGlossaryTermGUIDs, that.candidateGlossaryTermGUIDs) &&
                Objects.equals(candidateGlossaryFolderGUIDs, that.candidateGlossaryFolderGUIDs);
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), informalTerm, subjectAreaName, candidateGlossaryTermGUIDs, candidateGlossaryFolderGUIDs);
    }
}
