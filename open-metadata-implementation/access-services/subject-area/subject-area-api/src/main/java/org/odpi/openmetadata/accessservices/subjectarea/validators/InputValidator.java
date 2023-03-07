/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project */
package org.odpi.openmetadata.accessservices.subjectarea.validators;

import org.odpi.openmetadata.accessservices.subjectarea.ffdc.SubjectAreaErrorCode;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.Status;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.NodeType;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;

/**
 * Methods used for rest API input validation
 */
public class InputValidator {
    /**
     * Throw an exception if the metadata server URL (which comes from the configuration has not been supplied on the constructor.
     *
     * @param className         - name of the class making the call.
     * @param actionDescription - action description
     * @param omasServerURL     - metadata server url.
     * @throws InvalidParameterException - the org.odpi.openmetadata.accessservices.subjectarea.server URL is not set
     */
    static public void validateRemoteServerURLNotNull(String className, String actionDescription, String omasServerURL) throws InvalidParameterException {
        if (omasServerURL == null) {
            /*
             * It is not possible to retrieve a connection without knowledge of where the OMAS Server is located.
             */
            ExceptionMessageDefinition messageDefinition = SubjectAreaErrorCode.SERVER_URL_NOT_SPECIFIED.getMessageDefinition();

            String invalidPropertyName = "omasServerURL";
            messageDefinition.setMessageParameters(invalidPropertyName, null);
            throw new InvalidParameterException(messageDefinition,
                                                className,
                                                actionDescription,
                                                invalidPropertyName,
                                                null);
        }
    }

    /**
     * Throw an exception if the metadata server Name (which comes from the configuration has not been supplied on the constructor.
     *
     * @param className         - name of the class making the call.
     * @param actionDescription - action description
     * @param omasServerName    - metadata server name
     * @throws InvalidParameterException - the org.odpi.openmetadata.accessservices.subjectarea.server URL is not set
     */
    static public void validateRemoteServerNameNotNull(String className, String actionDescription, String omasServerName) throws InvalidParameterException {
        if (omasServerName == null) {
            /*
             * It is not possible to retrieve a connection without knowledge of where the OMAS Server is located.
             */
            ExceptionMessageDefinition messageDefinition = SubjectAreaErrorCode.SERVER_NAME_NOT_SPECIFIED.getMessageDefinition();
            String invalidPropertyName = "omasServerName";
            messageDefinition.setMessageParameters(invalidPropertyName, null);
            throw new InvalidParameterException(messageDefinition,
                                                className,
                                                actionDescription,
                                                invalidPropertyName,
                                                null);
        }
    }


    /**
     * Throw an exception if the supplied userId is null
     *
     * @param className         name of the class
     * @param actionDescription - action description
     * @param userId            user name to validate
     * @throws InvalidParameterException the userId is null
     */
    static public void validateUserIdNotNull(
            String className,
            String actionDescription,
            String userId) throws InvalidParameterException {
        if (userId == null) {
            ExceptionMessageDefinition messageDefinition = SubjectAreaErrorCode.NULL_USER_ID.getMessageDefinition();
            String invalidPropertyName = "userId";
            messageDefinition.setMessageParameters(invalidPropertyName, null);
            throw new InvalidParameterException(messageDefinition,
                                                className,
                                                actionDescription,
                                                invalidPropertyName,
                                                null);
        }
    }

    /**
     * Validate the supplied string can be converted to a Status and return that status. If it cannot be converted then null is returned.
     *
     * @param className         - name of the class making the call.
     * @param actionDescription - name of the method making the call.
     * @param statusName        - the String name to convert to a Status
     * @return Status or null.
     * @throws InvalidParameterException invalid status
     */
    static public Status validateStatusAndCheckNotDeleted(
            String className,
            String actionDescription,
            String statusName) throws InvalidParameterException {
        Status newStatus = null;
        try {
            newStatus = Status.valueOf(statusName);
        } catch (Exception e) {
            ExceptionMessageDefinition messageDefinition = SubjectAreaErrorCode.INVALID_STATUS_VALUE_SUPPLIED.getMessageDefinition();
            String invalidPropertyName = "status";
            messageDefinition.setMessageParameters(invalidPropertyName, null);
            throw new InvalidParameterException(messageDefinition,
                                                className,
                                                actionDescription,
                                                invalidPropertyName,
                                                statusName);

        }
        if (Status.DELETED == newStatus) {

            ExceptionMessageDefinition messageDefinition = SubjectAreaErrorCode.STATUS_UPDATE_TO_DELETED_NOT_ALLOWED.getMessageDefinition();
            String invalidPropertyName = "status";
            messageDefinition.setMessageParameters(invalidPropertyName, null);
            throw new InvalidParameterException(messageDefinition,
                                                className,
                                                actionDescription,
                                                invalidPropertyName,
                                                statusName);

        }

        return newStatus;
    }


    /**
     * Throw an exception if the supplied userId is null
     *
     * @param className         - name of the class making the call.
     * @param actionDescription - name of the method making the call.
     * @param guid              - unique identifier to validate
     * @param guidParameter     - name of the parameter that passed the userId
     * @throws InvalidParameterException - the userId is null
     */
    static public void validateGUIDNotNull(
            String className,
            String actionDescription,
            String guid,
            String guidParameter) throws InvalidParameterException {
        if (guid == null) {
            ExceptionMessageDefinition messageDefinition = SubjectAreaErrorCode.NULL_GUID.getMessageDefinition();
            String invalidPropertyName = "guid";
            messageDefinition.setMessageParameters(invalidPropertyName, null);
            throw new InvalidParameterException(messageDefinition,
                                                className,
                                                actionDescription,
                                                invalidPropertyName,
                                                null);
        }
    }


    /**
     * Throw an exception if the supplied userId is null
     *
     * @param className         - name of the class making the call.
     * @param actionDescription - name of the method making the call.
     * @param name              - unique name to validate
     * @param nameParameter     - name of the parameter that passed the name.
     * @throws InvalidParameterException - the userId is null
     */
    public static void validateNameNotNull(String className,
                                           String actionDescription,
                                           String name,
                                           String nameParameter
                                          ) throws InvalidParameterException {
        if (name == null) {

            ExceptionMessageDefinition messageDefinition = SubjectAreaErrorCode.NULL_NAME.getMessageDefinition();

            messageDefinition.setMessageParameters(nameParameter, null);
            throw new InvalidParameterException(messageDefinition,
                                                className,
                                                actionDescription,
                                                nameParameter,
                                                null);
        }
    }

    public static void validateNodeType(Object... args) throws InvalidParameterException {
        if (args.length < 4) return;
        boolean isValid = false;
        String className = (String) args[0];
        String actionDescription = (String) args[1];
        NodeType nodeTypeToCheck = (NodeType) args[2];

        if (nodeTypeToCheck != null) {
            for (int i = 3; i < args.length; i++) {
                if (nodeTypeToCheck.equals(args[i])) {
                    isValid = true;
                }
            }
            if (!isValid) {
                ExceptionMessageDefinition messageDefinition = SubjectAreaErrorCode.INVALID_NODETYPE.getMessageDefinition();
                String invalidPropertyName = "nodeType";
                messageDefinition.setMessageParameters(invalidPropertyName, null);
                throw new InvalidParameterException(messageDefinition,
                                                    className,
                                                    actionDescription,
                                                    invalidPropertyName,
                                                    nodeTypeToCheck.name());
            }
        }
    }
}
