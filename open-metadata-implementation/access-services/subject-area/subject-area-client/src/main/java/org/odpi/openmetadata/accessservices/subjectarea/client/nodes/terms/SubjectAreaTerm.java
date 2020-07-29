/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.client.nodes.terms;

import org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaClient;
import org.odpi.openmetadata.accessservices.subjectarea.client.SubjectAreaNodeClient;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term.Term;

/**
 * The SubjectAreaDefinition Open Metadata Access Service (OMAS) API for terms.
 */
public interface SubjectAreaTerm {

    /**
     * @return {@link SubjectAreaNodeClient} for client calls(described in {@link SubjectAreaClient})
     * when working with Terms
     */
    SubjectAreaNodeClient<Term> term();
}
