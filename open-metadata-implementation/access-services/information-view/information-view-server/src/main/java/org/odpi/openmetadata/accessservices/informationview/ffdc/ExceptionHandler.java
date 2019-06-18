/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.ffdc;

import org.odpi.openmetadata.accessservices.informationview.events.ReportElement;
import org.odpi.openmetadata.accessservices.informationview.events.Source;
import org.odpi.openmetadata.accessservices.informationview.ffdc.exceptions.runtime.AddEntityException;
import org.odpi.openmetadata.accessservices.informationview.ffdc.exceptions.runtime.AddRelationshipException;
import org.odpi.openmetadata.accessservices.informationview.ffdc.exceptions.runtime.DeleteRelationshipException;
import org.odpi.openmetadata.accessservices.informationview.ffdc.exceptions.runtime.EntityNotFoundException;
import org.odpi.openmetadata.accessservices.informationview.ffdc.exceptions.runtime.NoRegistrationDetailsProvided;
import org.odpi.openmetadata.accessservices.informationview.ffdc.exceptions.runtime.RetrieveEntityException;
import org.odpi.openmetadata.accessservices.informationview.ffdc.exceptions.runtime.RetrieveRelationshipException;
import org.odpi.openmetadata.accessservices.informationview.ffdc.exceptions.runtime.SourceNotFoundException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.OMRSCheckedExceptionBase;

public class ExceptionHandler {

    public static EntityDetail throwEntityNotFoundException(String searchCriteriaPropertyName,
                                                               String searchCriteriaPropertyValue, String entityType,
                                                               String reportingClassName) {
        InformationViewErrorCode errorCode = InformationViewErrorCode.ENTITY_NOT_FOUND_EXCEPTION;
        throw new EntityNotFoundException(errorCode.getHttpErrorCode(), reportingClassName,
                errorCode.getFormattedErrorMessage(searchCriteriaPropertyName, searchCriteriaPropertyValue, entityType),
                errorCode.getSystemAction(),
                errorCode.getUserAction(),
                null);
    }


    public static void throwAddEntityRelationship(String typeName, OMRSCheckedExceptionBase e,
                                                     String reportingClassName) {
        InformationViewErrorCode errorCode = InformationViewErrorCode.ADD_ENTITY_EXCEPTION;
        throw new AddEntityException(errorCode.getHttpErrorCode(), reportingClassName,
                errorCode.getFormattedErrorMessage(typeName, e.getMessage()),
                errorCode.getSystemAction(),
                errorCode.getUserAction(),
                null);
    }

    public static void throwAddRelationshipException(String relationshipName, OMRSCheckedExceptionBase e,
                                                      String reportingClassName) {
        InformationViewErrorCode errorCode = InformationViewErrorCode.ADD_RELATIONSHIP_EXCEPTION;
        throw new AddRelationshipException(errorCode.getHttpErrorCode(), reportingClassName,
                errorCode.getFormattedErrorMessage(relationshipName, e.getMessage()),
                errorCode.getSystemAction(),
                errorCode.getUserAction(),
                null);
    }

    public static EntityDetail throwNoRegistrationDetailsProvided(Throwable exception, String reportingClassName) {
        InformationViewErrorCode errorCode = InformationViewErrorCode.NO_REGISTRATION_DETAILS_PROVIDED;
        throw new NoRegistrationDetailsProvided(errorCode.getHttpErrorCode(), reportingClassName,
                errorCode.getFormattedErrorMessage(),
                errorCode.getSystemAction(),
                errorCode.getUserAction(),
                exception);
    }

    public static void throwSourceNotFoundException(Source source, Throwable exception, String reportingClassName) {
        InformationViewErrorCode code = InformationViewErrorCode.SOURCE_NOT_FOUND_EXCEPTION;
        throw new SourceNotFoundException(code.getHttpErrorCode(),
                reportingClassName,
                code.getFormattedErrorMessage(source.toString()),
                code.getSystemAction(),
                code.getUserAction(),
                exception);
    }

    public static void throwDeleteRelationshipException(Relationship relationship, Throwable exception, String reportingClassName) {
        InformationViewErrorCode errorCode = InformationViewErrorCode.DELETE_RELATIONSHIP_EXCEPTION;
        throw new DeleteRelationshipException(errorCode.getHttpErrorCode(), reportingClassName,
                errorCode.getFormattedErrorMessage(relationship.getGUID(),
                        relationship.getType().getTypeDefName()),
                errorCode.getSystemAction(),
                errorCode.getUserAction(),
                exception);
    }

    public static void throwRetrieveEntityException(String searchCriteriaPropertyName,
                                                    String searchCriteriaPropertyValue,
                                                    Throwable exception,
                                                    String reportingClassName) {
        InformationViewErrorCode errorCode = InformationViewErrorCode.GET_ENTITY_EXCEPTION;
        throw new RetrieveEntityException(errorCode.getHttpErrorCode(), reportingClassName,
                                            errorCode.getFormattedErrorMessage(searchCriteriaPropertyName, searchCriteriaPropertyValue),
                                            errorCode.getSystemAction(),
                                            errorCode.getUserAction(),
                                            exception);
    }

    public static ReportElement throwRetrieveRelationshipException(String entityDetailGuid,
                                                   String schemaTypeName,
                                                   OMRSCheckedExceptionBase e,
                                                   String reportingClassName) {
        InformationViewErrorCode auditCode = InformationViewErrorCode.GET_RELATIONSHIP_EXCEPTION;
        throw new RetrieveRelationshipException(auditCode.getHttpErrorCode(),
                reportingClassName,
                auditCode.getFormattedErrorMessage(schemaTypeName, entityDetailGuid, e.getMessage()),
                auditCode.getSystemAction(),
                auditCode.getUserAction(),
                e);
    }
}
