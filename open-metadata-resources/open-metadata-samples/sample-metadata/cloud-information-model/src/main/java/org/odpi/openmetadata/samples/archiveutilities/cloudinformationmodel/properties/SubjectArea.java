/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.archiveutilities.cloudinformationmodel.properties;

import java.util.HashMap;
import java.util.Map;

/**
 * SubjectArea captures one of the subject areas of the CIM.
 */
public class SubjectArea extends ModelElement
{
    private Map<String, ConceptGroup> conceptGroups = new HashMap<>();


    public Map<String, ConceptGroup> getConceptGroups()
    {
        return conceptGroups;
    }


    public void addConceptGroup(String       id,
                                ConceptGroup conceptGroup)
    {
        this.conceptGroups.put(id, conceptGroup);
    }


    public void setConceptGroups(Map<String, ConceptGroup> conceptGroups)
    {
        this.conceptGroups = conceptGroups;
    }
}
