/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.client;

import org.odpi.openmetadata.accessservices.subjectarea.*;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.subjectarea.validators.InputValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * SubjectAreaImpl is the OMAS client library implementation of the SubjectAreaImpl OMAS.
 * This interface provides glossary authoring interfaces for subject area experts.
 */
public class SubjectAreaImpl implements SubjectArea
{
    private static final Logger log = LoggerFactory.getLogger(SubjectAreaImpl.class);

    private static final String className = SubjectAreaImpl.class.getName();

    private final SubjectAreaTermImpl termAPI;
    private final SubjectAreaCategoryImpl categoryAPI;
    private final SubjectAreaGlossaryImpl glossaryAPI;
    public final  SubjectAreaRelationshipImpl relationshipAPI;
    /*
     * The URL of the server where OMAS is active
     */
    private String omasServerURL = null;


    /**
     * Default Constructor used once a connector is created.
     *
     * @param omasServerURL - unique id for the connector instance
     * @throws InvalidParameterException            one of the parameters is null or invalid.
     */
    public SubjectAreaImpl(String   omasServerURL) throws InvalidParameterException {
       String methodName = "SubjectAreaImpl";
        InputValidator.validateOMASServerURLNotNull(className,methodName,omasServerURL);
        this.omasServerURL = omasServerURL;
        this.glossaryAPI = new SubjectAreaGlossaryImpl(omasServerURL);
        this.termAPI = new SubjectAreaTermImpl(omasServerURL);
        this.categoryAPI = new SubjectAreaCategoryImpl(omasServerURL);
        this.relationshipAPI = new SubjectAreaRelationshipImpl(omasServerURL);
    }

    /**
     * Get the Category API. Use this API to author Glossary Categories.
     * @return SubjectAreaCategoryImpl
     */
    @Override
    public SubjectAreaCategory getSubjectAreaCategory() {
        return categoryAPI;
    }
    /**
     * Get the Glossary API. Use this API to author Glossaries
     * @return SubjectAreaGlossaryImpl
     */
    @Override
    public SubjectAreaGlossary getSubjectAreaGlossary() {
        return glossaryAPI;
    }
    /**
     * Get the Term API. Use this API to author Glossary Terms.
     * @return SubjectAreaTermImpl
     */

    /**
     * Get the Relationship API. Use this API to author Glossary Terms.
     * @return SubjectAreaRelationshipImpl
     */
    @Override
    public SubjectAreaTerm getSubjectAreaTerm() {
        return this.termAPI;
    }
    /**
     * Get the subject area relationship API class - use this class to issue relationship calls.
     * @return subject area relationship API class
     */
    @Override
    public SubjectAreaRelationship getSubjectAreaRelationship() {
        return this.relationshipAPI;
    }

}