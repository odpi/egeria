/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.apacheatlas.resource.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AtlasEntityHeader provides the summary information about an entity instance.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AtlasEntityHeader extends AtlasStruct
{
    private String                          guid                = null;
    private AtlasInstanceStatus             status              = AtlasInstanceStatus.ACTIVE;
    private String                          displayText         = null;
    private List<String>                    classificationNames = null;
    private List<AtlasClassification>       classifications     = null;
    private List<String>                    meaningNames        = null;
    private List<AtlasTermAssignmentHeader> meanings            = null;
    private boolean                         isIncomplete        = false;
    private Set<String>                     labels              = null;


    public AtlasEntityHeader()
    {
    }


    public String getGuid()
    {
        return guid;
    }


    public void setGuid(String guid)
    {
        this.guid = guid;
    }


    public AtlasInstanceStatus getStatus()
    {
        return status;
    }


    public void setStatus(AtlasInstanceStatus status)
    {
        this.status = status;
    }


    public String getDisplayText()
    {
        return displayText;
    }


    public void setDisplayText(String displayText)
    {
        this.displayText = displayText;
    }


    public List<String> getClassificationNames()
    {
        return classificationNames;
    }


    public void setClassificationNames(List<String> classificationNames)
    {
        this.classificationNames = classificationNames;
    }


    public List<AtlasClassification> getClassifications()
    {
        return classifications;
    }


    public void setClassifications(List<AtlasClassification> classifications)
    {
        this.classifications = classifications;
    }


    public List<String> getMeaningNames()
    {
        return meaningNames;
    }


    public void setMeaningNames(List<String> meaningNames)
    {
        this.meaningNames = meaningNames;
    }


    public List<AtlasTermAssignmentHeader> getMeanings()
    {
        return meanings;
    }


    public void setMeanings(List<AtlasTermAssignmentHeader> meanings)
    {
        this.meanings = meanings;
    }


    public boolean getIncomplete()
    {
        return isIncomplete;
    }


    public void setIncomplete(boolean incomplete)
    {
        isIncomplete = incomplete;
    }


    public Set<String> getLabels()
    {
        return labels;
    }


    public void setLabels(Set<String> labels)
    {
        this.labels = labels;
    }


    @Override
    public String toString()
    {
        return "AtlasEntityHeader{" +
                       "guid='" + guid + '\'' +
                       ", status=" + status +
                       ", displayText='" + displayText + '\'' +
                       ", classificationNames=" + classificationNames +
                       ", classifications=" + classifications +
                       ", meaningNames=" + meaningNames +
                       ", meanings=" + meanings +
                       ", isIncomplete=" + isIncomplete +
                       ", labels=" + labels +
                       ", typeName='" + getTypeName() + '\'' +
                       ", attributes=" + getAttributes() +
                       '}';
    }
}
