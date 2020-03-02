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
public class SubjectAreaImpl implements SubjectArea {
    private static final Logger log = LoggerFactory.getLogger(SubjectAreaImpl.class);

    private static final String className = SubjectAreaImpl.class.getName();
    static final String SUBJECT_AREA_BASE_URL = "/servers/%s/open-metadata/access-services/subject-area/users/%s/";

    private final SubjectAreaTermImpl termAPI;
    private final SubjectAreaCategoryImpl categoryAPI;
    private final SubjectAreaGlossaryImpl glossaryAPI;
    private final SubjectAreaProjectImpl projectAPI;
    public final SubjectAreaRelationshipImpl relationshipAPI;
    public final SubjectAreaGraphImpl graphAPI;
    public final String serverName;
    public final String omasServerUrl;

    /**
     * Default Constructor used once a connector is created.
     *
     * @param serverName    serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param omasServerURL - unique id for the connector instance
     * @throws InvalidParameterException one of the parameters is null or invalid.
     */
    public SubjectAreaImpl(String serverName, String omasServerURL) throws InvalidParameterException {
        String methodName = "SubjectAreaImpl";
        InputValidator.validateRemoteServerNameNotNull(className, methodName, serverName);
        InputValidator.validateRemoteServerURLNotNull(className, methodName, omasServerURL);
        this.glossaryAPI = new SubjectAreaGlossaryImpl(omasServerURL, serverName);
        this.termAPI = new SubjectAreaTermImpl(omasServerURL, serverName);
        this.categoryAPI = new SubjectAreaCategoryImpl(omasServerURL, serverName);
        this.relationshipAPI = new SubjectAreaRelationshipImpl(omasServerURL, serverName);
        this.graphAPI = new SubjectAreaGraphImpl(omasServerURL, serverName);
        this.projectAPI = new SubjectAreaProjectImpl(omasServerURL, serverName);
        this.serverName = serverName;
        this.omasServerUrl = omasServerURL;
    }

    /**
     * Get the Category API. Use this API to author Glossary Categories.
     *
     * @return SubjectAreaCategoryImpl
     */
    @Override
    public SubjectAreaCategory getSubjectAreaCategory() {
        return categoryAPI;
    }

    /**
     * Get the Glossary API. Use this API to author Glossaries
     *
     * @return SubjectAreaGlossaryImpl
     */
    @Override
    public SubjectAreaGlossary getSubjectAreaGlossary() {
        return glossaryAPI;
    }
    /**
     * Get the subject area project API class - use this class to issue project calls.
     * @return subject area project API class
     */
    @Override
    public SubjectAreaProject getSubjectAreaProject() {
        return  projectAPI;
    }

    /**
     * Get the Relationship API. Use this API to author Glossary Terms.
     *
     * @return SubjectAreaRelationshipImpl
     */
    @Override
    public SubjectAreaTerm getSubjectAreaTerm() {
        return this.termAPI;
    }

    /**
     * Get the subject area relationship API class - use this class to issue relationship calls.
     *
     * @return subject area relationship API class
     */
    @Override
    public SubjectAreaRelationship getSubjectAreaRelationship() {
        return this.relationshipAPI;
    }

    /**
     * Get the subject area graph API class - use this class to issue graph calls.
     *
     * @return subject area graph API class
     */
    @Override
    public SubjectAreaGraph getSubjectAreaGraph() {
        return this.graphAPI;
    }

    /**
     * Server Name under which this request is performed, this is used in multi tenanting to identify the tenant
     *
     * @return serverName name of the server
     */

    @Override
    public String getServerName() {
        return serverName;
    }

    /**
     * Base url used to issue OMAS Rest calls
     *
     * @return url of the server
     */
    @Override
    public String getOmasServerUrl() {
        return omasServerUrl;
    }
}