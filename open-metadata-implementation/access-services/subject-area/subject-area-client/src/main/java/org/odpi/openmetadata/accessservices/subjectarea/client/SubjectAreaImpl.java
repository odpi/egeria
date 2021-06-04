/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.client;

import org.odpi.openmetadata.accessservices.subjectarea.SubjectArea;
import org.odpi.openmetadata.accessservices.subjectarea.client.configs.SubjectAreaConfigClient;
import org.odpi.openmetadata.accessservices.subjectarea.client.nodes.DefaultSubjectAreaNodeClients;
import org.odpi.openmetadata.accessservices.subjectarea.client.nodes.SubjectAreaNodeClients;
import org.odpi.openmetadata.accessservices.subjectarea.client.relationships.SubjectAreaGraphClient;
import org.odpi.openmetadata.accessservices.subjectarea.client.relationships.SubjectAreaRelationship;
import org.odpi.openmetadata.accessservices.subjectarea.client.relationships.SubjectAreaRelationshipClients;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.SubjectAreaErrorCode;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.subjectarea.validators.InputValidator;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;


/**
 * SubjectAreaImpl is the OMAS client library implementation of the SubjectAreaImpl OMAS.
 * This interface provides glossary authoring interfaces for subject area experts.
 */
public class SubjectAreaImpl implements SubjectArea {
    private static final String className = SubjectAreaImpl.class.getName();

    private final SubjectAreaNodeClients nodeClients;
    private final SubjectAreaRelationshipClients relationshipAPI;
    private final SubjectAreaGraphClient graphAPI;
    private final SubjectAreaConfigClient configAPI;
    private final String serverName;
    private final String omasServerUrl;

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
        try {
            SubjectAreaRestClient client = new SubjectAreaRestClient(serverName, omasServerURL);
            DefaultSubjectAreaNodeClients subjectAreaNode = new DefaultSubjectAreaNodeClients(client);
            SubjectAreaRelationship subjectAreaRelationship = new SubjectAreaRelationship(client);
            SubjectAreaGraphClient subjectAreaGraph = new SubjectAreaGraphClient(client);
            SubjectAreaConfigClient subjectAreaConfig = new SubjectAreaConfigClient(client);

            this.nodeClients = subjectAreaNode;
            this.relationshipAPI = subjectAreaRelationship;
            this.graphAPI = subjectAreaGraph;
            this.configAPI = subjectAreaConfig;

        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException e) {
            String parameterName = "serverName or omasServerURL";
            String parameterValue = "unknown";
            if (serverName == null ||  "".equals(serverName)) {
                parameterName = "serverName";
                parameterValue = serverName;
            }
            if (omasServerURL == null || "".equals(omasServerURL)) {
                parameterName = "omasServerURL";
                parameterValue = omasServerURL;
            }
            ExceptionMessageDefinition messageDefinition = SubjectAreaErrorCode.SUBJECT_AREA_FAILED_TO_INITIALISE.getMessageDefinition();
            messageDefinition.setMessageParameters(parameterName, parameterValue);
            throw new InvalidParameterException(messageDefinition, className, methodName, e, parameterName, parameterValue);
        }

        this.serverName = serverName;
        this.omasServerUrl = omasServerURL;
    }


    @Override
    public SubjectAreaNodeClients nodeClients() {
        return this.nodeClients;
    }

    /**
     * Get the subject area relationship API class - use this class to issue relationship calls.
     *
     * @return subject area relationship API class
     */
    @Override
    public SubjectAreaRelationshipClients relationshipClients() {
        return this.relationshipAPI;
    }

    /**
     * Get the subject area graph API class - use this class to issue config calls.
     *
     * @return subject area config API class
     */
    @Override
    public SubjectAreaConfigClient subjectAreaConfigClient() {
        return this.configAPI;
    }
    /**
     * Get the subject area graph API class - use this class to issue graph calls.
     *
     * @return subject area graph API class
     */
    @Override
    public SubjectAreaGraphClient subjectAreaGraphClient() { return this.graphAPI; }

    /**
     * Server Name under which this request is performed, this is used in multi tenanting to identify the tenant
     *
     * @return serverName name of the server
     */

    @Override
    public String serverName() {
        return serverName;
    }

    /**
     * Base url used to issue OMAS Rest calls
     *
     * @return url of the server
     */
    @Override
    public String omasServerUrl() {
        return omasServerUrl;
    }
}