/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.subjectarea.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * SubjectAreaImpl is the OMAS client library implementation of the SubjectAreaImpl OMAS.
 * This interface provides glossary term authoring interface for subject area experts.
 */
public class SubjectAreaTermImpl implements org.odpi.openmetadata.accessservices.subjectarea.SubjectAreaTerm
{
    private static final Logger log = LoggerFactory.getLogger(SubjectAreaTermImpl.class);

    private static final String className = SubjectAreaTermImpl.class.getName();

    /*
     * The URL of the server where OMAS is active
     */
    private String                    omasServerURL = null;


    /**
     * Default Constructor used once a connector is created.
     *
     * @param omasServerURL - unique id for the connector instance
     */
    public SubjectAreaTermImpl(String   omasServerURL)
    {
        /*
         * Save OMAS Server URL
         */
        this.omasServerURL = omasServerURL;
    }



}
