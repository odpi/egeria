/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category;

import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.node.NodeType;

/**
 * A type of Category called a Subject Area is one that describes a subject area or a domain.
 * For a category to me in a subject area - would be with in the children category hierarchies under a SubjectArea.
 */
public class SubjectArea extends Category{
    String subjectAreaName =null;

    public SubjectArea() {
        nodeType = NodeType.SubjectArea;
    }

    /**
     * The name of the subject area.
     * @return subject area name
     */
    public String getSubjectAreaName() {
        return subjectAreaName;
    }

    public void setSubjectAreaName(String subjectAreaName) {
        this.subjectAreaName = subjectAreaName;
    }
}
