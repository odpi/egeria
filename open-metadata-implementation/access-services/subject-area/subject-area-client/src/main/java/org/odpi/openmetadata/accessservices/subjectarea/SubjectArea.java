/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.subjectarea;

/**
 * The SubjectArea Open Metadata Access Service (OMAS).
 */
public interface SubjectArea
{
    /**
     * Get the subject area API class
     * @return subject area glossary API class
     */
    public SubjectAreaGlossary getSubjectAreaGlossary();
    /**
     * Get the subject area term API class - use this class to issue term calls.
     * @return subject area term API class
     */
    public SubjectAreaTerm getSubjectAreaTerm();
    /**
     * Get the subject area category API class - use this class to issue category calls.
     * @return subject area category API class
     */
    public SubjectAreaCategory getSubjectAreaCategory();
}