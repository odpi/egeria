/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.archiveutilities.cloudinformationmodel.properties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ConceptGroup lists the entities defined in a subject area.
 */
public class ConceptGroup extends ModelElement
{
    private Map<String, Concept> concepts = new HashMap<>();
    private String               subjectAreaGUID = null;


    public List<Concept> getConcepts()
    {
        if (concepts.isEmpty())
        {
            return null;
        }
        else
        {
            return new ArrayList<>(concepts.values());
        }
    }

    public void addConcept(String  id,
                           Concept concept)
    {
        concepts.put(id, concept);
    }

    public void setConcepts(Map<String, Concept> concepts)
    {
        this.concepts = concepts;
    }


    public String getSubjectAreaGUID()
    {
        return subjectAreaGUID;
    }


    public void setSubjectAreaGUID(String subjectAreaGUID)
    {
        this.subjectAreaGUID = subjectAreaGUID;
    }


    @Override
    public String toString()
    {
        return "ConceptGroup{" +
                       "concepts=" + concepts +
                       ", subjectAreaGUID='" + subjectAreaGUID + '\'' +
                       ", GUID='" + getGUID() + '\'' +
                       ", technicalName='" + getTechnicalName() + '\'' +
                       ", displayName='" + getDisplayName() + '\'' +
                       ", description='" + getDescription() + '\'' +
                       ", version='" + getVersion() + '\'' +
                       '}';
    }
}
