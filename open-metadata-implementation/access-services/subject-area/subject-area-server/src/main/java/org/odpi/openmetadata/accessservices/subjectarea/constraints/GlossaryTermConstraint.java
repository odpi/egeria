/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.constraints;


import org.odpi.openmetadata.accessservices.subjectarea.ffdc.SubjectAreaErrorCode;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.subjectarea.generated.entities.GlossaryTerm.GlossaryTerm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GlossaryTermConstraint implements SubjectAreaConstraint{

    private static final Logger log = LoggerFactory.getLogger(GlossaryTermConstraint.class);
    private static final String className = GlossaryTermConstraint.class.getName();


    public GlossaryTermConstraint() {}

    @Override
    public void preCreate(Object artifact) throws InvalidParameterException {
        final String methodName = "preCreate";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName );
        }

        GlossaryTerm glossaryTerm=(GlossaryTerm)artifact;

        String displayName =glossaryTerm.getDisplayName();

        if (displayName == null ) {
            // error need a display name
            SubjectAreaErrorCode errorCode    = SubjectAreaErrorCode.GLOSSARY_TERM_CREATE_WITHOUT_NAME;
            String errorMessage = errorCode.getErrorMessageId()
                    + errorCode.getFormattedErrorMessage(className,
                    methodName);
            log.error(errorMessage);
            throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                    className,
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }
        if (log.isDebugEnabled()) {
            log.debug("<== Method: " + methodName );
        }
    }

    @Override
    public void postCreate(Object artifact) throws InvalidParameterException {
        GlossaryTerm glossaryTerm=(GlossaryTerm)artifact;
        String guid =glossaryTerm.getSystemAttributes().getGUID();
        glossaryTerm.setQualifiedName("GlossaryTerm" + "." +guid );
        // TODO issue update without applying update constraint

    }


    @Override
    public void preUpdate(Object artifact,Object proposedArtifact)throws InvalidParameterException {

    }

    @Override
    public void postUpdate(Object artifact)throws InvalidParameterException {

    }

    @Override
    public void preDelete(Object artifact) throws InvalidParameterException{

    }

    @Override
    public void postDelete(Object artifact) throws InvalidParameterException{

    }

}
