/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.ffdc;

import org.odpi.openmetadata.accessservices.informationview.events.Source;
import org.odpi.openmetadata.accessservices.informationview.ffdc.exceptions.runtime.AddEntityException;
import org.odpi.openmetadata.accessservices.informationview.ffdc.exceptions.runtime.AddRelationshipException;
import org.odpi.openmetadata.accessservices.informationview.ffdc.exceptions.runtime.ContextLoadException;
import org.odpi.openmetadata.accessservices.informationview.ffdc.exceptions.runtime.DeleteRelationshipException;
import org.odpi.openmetadata.accessservices.informationview.ffdc.exceptions.runtime.EntityNotFoundException;
import org.odpi.openmetadata.accessservices.informationview.ffdc.exceptions.runtime.InformationViewExceptionBase;
import org.odpi.openmetadata.accessservices.informationview.ffdc.exceptions.runtime.MultipleEntitiesMatching;
import org.odpi.openmetadata.accessservices.informationview.ffdc.exceptions.runtime.NoMatchingEntityException;
import org.odpi.openmetadata.accessservices.informationview.ffdc.exceptions.runtime.NoRegistrationDetailsProvided;
import org.odpi.openmetadata.accessservices.informationview.ffdc.exceptions.runtime.RetrieveEntityException;
import org.odpi.openmetadata.accessservices.informationview.ffdc.exceptions.runtime.RetrieveRelationshipException;
import org.odpi.openmetadata.accessservices.informationview.ffdc.exceptions.runtime.SourceNotFoundException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.OMRSCheckedExceptionBase;

public class ExceptionHandler {

    public static InformationViewExceptionBase buildEntityNotFoundException(String searchCriteriaPropertyName,
                                                                            String searchCriteriaPropertyValue,
                                                                            String entityType,
                                                                            String reportingClassName) {
        InformationViewErrorCode errorCode = InformationViewErrorCode.ENTITY_NOT_FOUND_EXCEPTION;
        return new EntityNotFoundException(errorCode.getHttpErrorCode(), reportingClassName,
                errorCode.getFormattedErrorMessage(searchCriteriaPropertyName, searchCriteriaPropertyValue, entityType),
                errorCode.getSystemAction(),
                errorCode.getUserAction(),
                null);
    }


    public static InformationViewExceptionBase buildAddEntityRelationship(String typeName,
                                                                          OMRSCheckedExceptionBase e,
                                                                          String reportingClassName) {
        InformationViewErrorCode errorCode = InformationViewErrorCode.ADD_ENTITY_EXCEPTION;
        return new AddEntityException(errorCode.getHttpErrorCode(), reportingClassName,
                errorCode.getFormattedErrorMessage(typeName, e.getMessage()),
                errorCode.getSystemAction(),
                errorCode.getUserAction(),
                null);
    }

    public static InformationViewExceptionBase buildAddRelationshipException(String relationshipName,
                                                                             String message,
                                                                             String reportingClassName) {
        InformationViewErrorCode errorCode = InformationViewErrorCode.ADD_RELATIONSHIP_EXCEPTION;
        return new AddRelationshipException(errorCode.getHttpErrorCode(), reportingClassName,
                errorCode.getFormattedErrorMessage(relationshipName, message),
                errorCode.getSystemAction(),
                errorCode.getUserAction(),
                null);
    }

    public static InformationViewExceptionBase buildNoRegistrationDetailsProvided(Throwable exception,
                                                                                  String reportingClassName) {
        InformationViewErrorCode errorCode = InformationViewErrorCode.NO_REGISTRATION_DETAILS_PROVIDED;
        return new NoRegistrationDetailsProvided(errorCode.getHttpErrorCode(), reportingClassName,
                errorCode.getFormattedErrorMessage(),
                errorCode.getSystemAction(),
                errorCode.getUserAction(),
                exception);
    }

    public static InformationViewExceptionBase buildSourceNotFoundException(Source source,
                                                                            Throwable exception,
                                                                            String reportingClassName) {
        InformationViewErrorCode code = InformationViewErrorCode.SOURCE_NOT_FOUND_EXCEPTION;
        return new SourceNotFoundException(code.getHttpErrorCode(),
                reportingClassName,
                code.getFormattedErrorMessage(source.toString()),
                code.getSystemAction(),
                code.getUserAction(),
                exception);
    }

    public static InformationViewExceptionBase buildDeleteRelationshipException(Relationship relationship,
                                                                                Throwable exception,
                                                                                String reportingClassName) {
        InformationViewErrorCode errorCode = InformationViewErrorCode.DELETE_RELATIONSHIP_EXCEPTION;
        return new DeleteRelationshipException(errorCode.getHttpErrorCode(), reportingClassName,
                errorCode.getFormattedErrorMessage(relationship.getGUID(),
                        relationship.getType().getTypeDefName()),
                errorCode.getSystemAction(),
                errorCode.getUserAction(),
                exception);
    }

    public static InformationViewExceptionBase buildRetrieveEntityException(String searchCriteriaPropertyName,
                                                                            String searchCriteriaPropertyValue,
                                                                            Throwable exception,
                                                                            String reportingClassName) {
        InformationViewErrorCode errorCode = InformationViewErrorCode.GET_ENTITY_EXCEPTION;
        return new RetrieveEntityException(errorCode.getHttpErrorCode(), reportingClassName,
                                            errorCode.getFormattedErrorMessage(searchCriteriaPropertyName, searchCriteriaPropertyValue),
                                            errorCode.getSystemAction(),
                                            errorCode.getUserAction(),
                                            exception);
    }

    public static InformationViewExceptionBase buildRetrieveRelationshipException(String entityGuid,
                                                                                  String relationshipTypeName,
                                                                                  OMRSCheckedExceptionBase e,
                                                                                  String reportingClassName) {
        InformationViewErrorCode auditCode = InformationViewErrorCode.GET_RELATIONSHIP_EXCEPTION;
        return new RetrieveRelationshipException(auditCode.getHttpErrorCode(),
                reportingClassName,
                auditCode.getFormattedErrorMessage(relationshipTypeName, entityGuid, e.getMessage()),
                auditCode.getSystemAction(),
                auditCode.getUserAction(),
                e);
    }

    public static InformationViewExceptionBase buildMultipleEntitiesMatchingException(InstanceProperties matchingProperties,
                                                                                      Throwable reportedCaughtException,
                                                                                      String reportingClassName) {
        return new MultipleEntitiesMatching(InformationViewErrorCode.MULTIPLE_MATCHING_ENTITIES_EXCEPTION.getHttpErrorCode(),
                                            reportingClassName,
                                            InformationViewErrorCode.MULTIPLE_MATCHING_ENTITIES_EXCEPTION.getFormattedErrorMessage(matchingProperties.toString()),
                                            InformationViewErrorCode.MULTIPLE_MATCHING_ENTITIES_EXCEPTION.getSystemAction(),
                                            InformationViewErrorCode.MULTIPLE_MATCHING_ENTITIES_EXCEPTION.getUserAction(),
                                            reportedCaughtException);
    }

    public static InformationViewExceptionBase buildNoMatchingEntityException(InstanceProperties matchingProperties,
                                                                              Throwable reportedCaughtException,
                                                                              String reportingClassName) {
        return new NoMatchingEntityException(InformationViewErrorCode.NO_MATCHING_ENTITY_EXCEPTION.getHttpErrorCode(),
                                            reportingClassName,
                                            InformationViewErrorCode.NO_MATCHING_ENTITY_EXCEPTION.getFormattedErrorMessage(matchingProperties.toString()),
                                            InformationViewErrorCode.NO_MATCHING_ENTITY_EXCEPTION.getSystemAction(),
                                            InformationViewErrorCode.NO_MATCHING_ENTITY_EXCEPTION.getUserAction(),
                                            reportedCaughtException);
    }


    public static InformationViewExceptionBase buildUpdateEntityException(String entityGuid,
                                                                          Throwable reportedCaughtException,
                                                                          String reportingClassName) {
        return new NoMatchingEntityException(InformationViewErrorCode.UPDATE_ENTITY_EXCEPTION.getHttpErrorCode(),
                                            reportingClassName,
                                            InformationViewErrorCode.NO_MATCHING_ENTITY_EXCEPTION.getFormattedErrorMessage(entityGuid,reportedCaughtException.getMessage()),
                                            InformationViewErrorCode.NO_MATCHING_ENTITY_EXCEPTION.getSystemAction(),
                                            InformationViewErrorCode.NO_MATCHING_ENTITY_EXCEPTION.getUserAction(),
                                            reportedCaughtException);
    }


    public static InformationViewExceptionBase buildRetrieveContextException(String entityGuid, OMRSCheckedExceptionBase e, String reportingClassName) {
        InformationViewErrorCode code = InformationViewErrorCode.RETRIEVE_CONTEXT_EXCEPTION;
        return new ContextLoadException(code.getHttpErrorCode(), reportingClassName, code.getFormattedErrorMessage(entityGuid, e.getMessage()), code.getSystemAction(), code.getUserAction(), e);
    }
}
