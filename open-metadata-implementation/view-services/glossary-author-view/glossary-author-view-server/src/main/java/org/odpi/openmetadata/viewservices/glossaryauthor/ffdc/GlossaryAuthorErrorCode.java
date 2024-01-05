/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.glossaryauthor.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageSet;


/**
 * The GlossaryAuthorErrorCode is used to define first failure data capture (FFDC) for errors that occur when working with
 * the Glossary Author OMVS Services.  It is used in conjunction with both Checked and Runtime (unchecked) exceptions.
 * <p>
 * The 5 fields in the enum are:
 * <ul>
 * <li>HTTP Error Code - for translating between REST and JAVA - Typically the numbers used are:</li>
 * <li><ul>
 * <li>500 - internal error</li>
 * <li>400 - invalid parameters</li>
 * <li>404 - not found</li>
 * <li>409 - data conflict errors - eg item already defined</li>
 * </ul></li>
 * <li>Error Message Id - to uniquely identify the message</li>
 * <li>Error Message Text - includes placeholder to allow additional values to be captured</li>
 * <li>SystemAction - describes the result of the error</li>
 * <li>UserAction - describes how a SubjectAreaInterface should correct the error</li>
 * </ul>
 */
public enum GlossaryAuthorErrorCode implements ExceptionMessageSet {
    GET_BREADCRUMBS_TERM_NOT_IN_GLOSSARY(400, "OMVS-GLOSSARY-AUTHOR-400-001",
                                         "Get BreadCrumbs call issued term {0} guid {1}, is not in the glossary {2} guid {3}  ",
                                         "The system is unable to return breadcrumbs if the Term is not owned by the Glossary.",
                                         "Supply valid Term and Glossary guids when requesting BreadCrumbs."),
    GET_BREADCRUMBS_TERM_NOT_IN_ROOT_CATEGORY(400, "OMVS-GLOSSARY-AUTHOR-400-002",
                                              "Get BreadCrumbs call issued term {0} guid {1}, is not in the root category {2} guid {3}.",
                                              "The system is unable to return breadcrumbs if the Term is not owned by the root Category.",
                                              "Supply valid Term and root category guids when requesting BreadCrumbs."),
    GET_BREADCRUMBS_TERM_NOT_IN_LEAF_CATEGORY(400, "OMVS-GLOSSARY-AUTHOR-400-003",
                                              "Get BreadCrumbs call issued term {0} guid {1}, is not in the leaf category {2} guid {3}.",
                                              "The system is unable to return breadcrumbs if the Term is not owned by the leaf Category.",
                                              "Supply valid Term and leaf category guids when requesting BreadCrumbs."),
    GET_BREADCRUMBS_ROOT_CATEGORY_NOT_ROOT(400, "OMVS-GLOSSARY-AUTHOR-400-003",
                                           "Get BreadCrumbs call the root category {0} guid {1}, is not actually the root as it has a parent category.",
                                           "The system is unable to return breadcrumbs if the root Category has a parent category.",
                                           "Supply a root category that does not have a parent category when requesting BreadCrumbs."),
    GET_BREADCRUMBS_ROOT_CATEGORY_NOT_GLOSSARY(400, "OMVS-GLOSSARY-AUTHOR-400-004",
                                               "Get BreadCrumbs call the root category {0} guid {1}, is not in the glossary {2} guid {3}",
                                               "The system is unable to return breadcrumbs if the root Category is not in the supplied glossary.",
                                               "Supply a root category that is in the requested glossary when requesting BreadCrumbs."),
    ;

    private final int    httpErrorCode;
    private final String errorMessageId;
    private final String errorMessage;
    private final String systemAction;
    private final String userAction;


    /**
     * The constructor expects to be passed one of the enumeration rows defined above.
     *
     * @param httpErrorCode   error code to use over REST calls
     * @param errorMessageId   unique id for the message
     * @param errorMessage   text for the message
     * @param systemAction   description of the action taken by the system when the error condition happened
     * @param userAction   instructions for resolving the error
     */
    GlossaryAuthorErrorCode(int httpErrorCode, String errorMessageId, String errorMessage, String systemAction, String userAction)
    {
        this.httpErrorCode = httpErrorCode;
        this.errorMessageId = errorMessageId;
        this.errorMessage = errorMessage;
        this.systemAction = systemAction;
        this.userAction = userAction;
    }


    /**
     * Retrieve a message definition object for an exception.  This method is used when there are no message inserts.
     *
     * @return message definition object.
     */
    @Override
    public ExceptionMessageDefinition getMessageDefinition()
    {
        return new ExceptionMessageDefinition(httpErrorCode,
                                              errorMessageId,
                                              errorMessage,
                                              systemAction,
                                              userAction);
    }


    /**
     * Retrieve a message definition object for an exception.  This method is used when there are values to be inserted into the message.
     *
     * @param params array of parameters (all strings).  They are inserted into the message according to the numbering in the message text.
     * @return message definition object.
     */
    @Override
    public ExceptionMessageDefinition getMessageDefinition(String... params)
    {
        ExceptionMessageDefinition messageDefinition = new ExceptionMessageDefinition(httpErrorCode,
                                                                                      errorMessageId,
                                                                                      errorMessage,
                                                                                      systemAction,
                                                                                      userAction);

        messageDefinition.setMessageParameters(params);

        return messageDefinition;
    }


    /**
     * JSON-style toString
     *
     * @return string of property names and values for this enum
     */
    @Override
    public String toString()
    {
        return "ErrorCode{" +
                       "httpErrorCode=" + httpErrorCode +
                       ", errorMessageId='" + errorMessageId + '\'' +
                       ", errorMessage='" + errorMessage + '\'' +
                       ", systemAction='" + systemAction + '\'' +
                       ", userAction='" + userAction + '\'' +
                       '}';
    }
}

