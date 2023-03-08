/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project */
package org.odpi.openmetadata.accessservices.subjectarea.events.content;

import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;

public class SubjectAreaTermEventContent extends SubjectAreaEventContent{

    Term term = null;
    public Term getTerm() {
        return term;
    }

    public void setTerm(Term glossaryTerm) {
        this.term = glossaryTerm;
    }

}
