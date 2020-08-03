/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.client;

import org.odpi.openmetadata.accessservices.subjectarea.SubjectArea;
import org.odpi.openmetadata.accessservices.subjectarea.client.nodes.SubjectAreaNode;
import org.odpi.openmetadata.accessservices.subjectarea.client.nodes.categories.SubjectAreaCategory;
import org.odpi.openmetadata.accessservices.subjectarea.client.nodes.glossaries.SubjectAreaGlossary;
import org.odpi.openmetadata.accessservices.subjectarea.client.nodes.projects.SubjectAreaProject;
import org.odpi.openmetadata.accessservices.subjectarea.client.nodes.terms.SubjectAreaTerm;
import org.odpi.openmetadata.accessservices.subjectarea.client.relationships.SubjectAreaGraph;
import org.odpi.openmetadata.accessservices.subjectarea.client.relationships.SubjectAreaGraphClient;
import org.odpi.openmetadata.accessservices.subjectarea.client.relationships.SubjectAreaLine;
import org.odpi.openmetadata.accessservices.subjectarea.client.relationships.SubjectAreaRelationship;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.SubjectAreaErrorCode;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;


/**
 * SubjectAreaImpl is the OMAS client library implementation of the SubjectAreaImpl OMAS.
 * This interface provides glossary authoring interfaces for subject area experts.
 */
public class SubjectAreaImpl implements SubjectArea {
    private static final String className = SubjectAreaImpl.class.getName();

    private final SubjectAreaTerm termAPI;
    private final SubjectAreaCategory categoryAPI;
    private final SubjectAreaGlossary glossaryAPI;
    private final SubjectAreaProject projectAPI;
    public final SubjectAreaRelationship relationshipAPI;
    public final SubjectAreaGraph graphAPI;
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
        this(initRestClient(serverName, omasServerURL, null, null));
    }

    /**
     * Constructor used once a connector is created.
     *
     * @param serverName    serverName under which this request is performed, this is used in multi tenanting to identify the tenant
     * @param omasServerURL - unique id for the connector instance
     * @param userId - unique identifier for user
     * @param password - password for user
     * @throws InvalidParameterException one of the parameters (serverName, omasServerURL) is null or invalid.
     */
    public SubjectAreaImpl(String serverName, String omasServerURL, String userId, String password) throws InvalidParameterException {
        this(initRestClient(serverName, omasServerURL, userId, password));
    }

    private SubjectAreaImpl(SubjectAreaRestClient client) {
        SubjectAreaNode subjectAreaNode = new SubjectAreaNode(client);
        SubjectAreaLine subjectAreaLine = new SubjectAreaLine(client);
        SubjectAreaGraph subjectAreaGraph = new SubjectAreaGraphClient(client);

        this.glossaryAPI = subjectAreaNode;
        this.termAPI =  subjectAreaNode;
        this.categoryAPI = subjectAreaNode;
        this.relationshipAPI = subjectAreaLine;
        this.graphAPI = subjectAreaGraph;
        this.projectAPI = subjectAreaNode;
        this.serverName = client.getServerName();
        this.omasServerUrl = client.getServerPlatformURLRoot();
    }

    private static SubjectAreaRestClient initRestClient(String serverName,
                                                        String omasServerURL,
                                                        String userId,
                                                        String password) throws InvalidParameterException {
        if (serverName == null || omasServerURL == null) {
            ExceptionMessageDefinition messageDefinition = SubjectAreaErrorCode.SUBJECT_AREA_FAILED_TO_INITIALISE.getMessageDefinition();
            throw new InvalidParameterException(messageDefinition, className, "initRestClient",
                    "One of the parameters (serverName, omasServerURL) is not correct.");
        }
        if (userId == null) {
            return new SubjectAreaRestClient(serverName, omasServerURL);
        } else {
            return new SubjectAreaRestClient(serverName, omasServerURL, userId, password);
        }
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