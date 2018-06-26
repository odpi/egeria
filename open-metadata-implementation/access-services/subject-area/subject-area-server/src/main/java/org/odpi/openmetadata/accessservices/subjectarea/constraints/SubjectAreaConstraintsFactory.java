/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.subjectarea.constraints;

public class SubjectAreaConstraintsFactory {

    public static SubjectAreaConstraint getSubjectAreaConstraint(String name) {
        if ("GlossaryTerm".equals(name)) {
            return new GlossaryTermConstraint();
        }
        return null;
    }
}
