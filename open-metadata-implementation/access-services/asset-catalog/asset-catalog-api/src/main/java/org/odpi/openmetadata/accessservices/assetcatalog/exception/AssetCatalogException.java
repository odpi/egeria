/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project */
package org.odpi.openmetadata.accessservices.assetcatalog.exception;

import org.odpi.openmetadata.commonservices.ffdc.exceptions.OMAGCheckedExceptionBase;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;

import java.util.Map;

/**
 * AssetCatalogException provides a checked exception for reporting errors found when using
 * the Asset Catalog OMAS services.
 * Typically these errors are either configuration or operational errors that can be fixed by an administrator
 * or power AssetConsumerInterface.  However, there may be the odd bug that surfaces here.
 * The AssetCatalogErrorCode can be used with this exception to populate it with standard messages.
 * The aim is to be able to uniquely identify the cause and remedy for the error.
 */
public class AssetCatalogException extends OMAGCheckedExceptionBase {
   
    private static final long    serialVersionUID = 1L;

    /**
     * This is the typical constructor used for creating an AssetCatalogException.
     *
     * @param messageDefinition  content of the message
     * @param className   name of class reporting error
     * @param actionDescription   description of function it was performing when error detected
     */
    public AssetCatalogException(ExceptionMessageDefinition messageDefinition,
                                String                     className,
                                String                     actionDescription)
    {
        super(messageDefinition, className, actionDescription);
    }


    /**
     * This is the typical constructor used for creating an AssetCatalogException.
     * The properties allow additional information to be associated with the exception.
     *
     * @param messageDefinition  content of the message
     * @param className   name of class reporting error
     * @param actionDescription   description of function it was performing when error detected
     * @param relatedProperties  arbitrary properties that may help with diagnosing the problem.
     */
    public AssetCatalogException(ExceptionMessageDefinition messageDefinition,
                                String                     className,
                                String                     actionDescription,
                                Map<String, Object> relatedProperties)
    {
        super(messageDefinition, className, actionDescription, relatedProperties);
    }


    /**
     * This is the constructor used for creating an AssetCatalogException when an unexpected error has been caught.
     * The properties allow additional information to be associated with the exception.
     *
     * @param messageDefinition  content of the message
     * @param className   name of class reporting error
     * @param actionDescription   description of function it was performing when error detected
     * @param caughtError   previous error causing this exception
     */
    public AssetCatalogException(ExceptionMessageDefinition messageDefinition,
                                String                     className,
                                String                     actionDescription,
                                Throwable                  caughtError)
    {
        super(messageDefinition, className, actionDescription, caughtError);
    }


    /**
     * This is the constructor used for creating an AssetCatalogException when an unexpected error has been caught.
     * The properties allow additional information to be associated with the exception.
     *
     * @param messageDefinition  content of the message
     * @param className   name of class reporting error
     * @param actionDescription   description of function it was performing when error detected
     * @param caughtError   previous error causing this exception
     * @param relatedProperties  arbitrary properties that may help with diagnosing the problem.
     */
    public AssetCatalogException(ExceptionMessageDefinition messageDefinition,
                                String                     className,
                                String                     actionDescription,
                                Throwable                  caughtError,
                                Map<String, Object>        relatedProperties)
    {
        super(messageDefinition, className, actionDescription, caughtError, relatedProperties);
    }

}
