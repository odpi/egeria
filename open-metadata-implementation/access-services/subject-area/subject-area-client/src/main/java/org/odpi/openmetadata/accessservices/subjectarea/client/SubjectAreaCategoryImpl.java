/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.subjectarea.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * SubjectAreaImpl is the OMAS client library implementation of the SubjectAreaImpl OMAS.
 * This interface provides glossary category  authoring interfaces for subject area experts.
 */
public class SubjectAreaCategoryImpl implements org.odpi.openmetadata.accessservices.subjectarea.SubjectAreaCategory
{
    private static final Logger log = LoggerFactory.getLogger(SubjectAreaCategoryImpl.class);

    private static final String className = SubjectAreaCategoryImpl.class.getName();

    /*
     * The URL of the server where OMAS is active
     */
    private String                    omasServerURL = null;


    /**
     * Default Constructor used once a connector is created.
     *
     * @param omasServerURL - unique id for the connector instance
     */
    public SubjectAreaCategoryImpl(String   omasServerURL)
    {
        /*
         * Save OMAS Server URL
         */
        this.omasServerURL = omasServerURL;
    }
}
