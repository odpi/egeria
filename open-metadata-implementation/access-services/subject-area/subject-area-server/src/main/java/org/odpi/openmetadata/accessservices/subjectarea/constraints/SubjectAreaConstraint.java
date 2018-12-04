/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.constraints;


import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;

public interface SubjectAreaConstraint {
    public void preCreate(Object artifact) throws InvalidParameterException, InvalidParameterException;
    public void postCreate(Object artifact) throws InvalidParameterException;
    public void preUpdate(Object artifact, Object proposedArtifact) throws InvalidParameterException;
    public void postUpdate(Object artifact) throws InvalidParameterException;
    public void preDelete(Object artifact) throws InvalidParameterException;
    public void postDelete(Object artifact) throws InvalidParameterException;
}
