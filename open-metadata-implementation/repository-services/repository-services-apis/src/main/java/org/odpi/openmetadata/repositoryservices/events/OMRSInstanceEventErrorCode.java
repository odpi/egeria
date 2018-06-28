/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.repositoryservices.events;

import java.io.Serializable;

/**
 * OMRSInstanceEventErrorCode defines the list of error codes that are used to record errors in the metadata
 * instance replication process that is used by the repository connectors within the open metadata repository cluster.
 * <ul>
 *     <li>
 *         NOT_IN_USE: There has been no error detected and so the error code is not in use.
 *     </li>
 * </ul>
 */
public enum OMRSInstanceEventErrorCode implements Serializable
{
    NOT_IN_USE                (0, "No Error",
                                  "There has been no error detected and so the error code is not in use.",
                               null),
    CONFLICTING_INSTANCES     (1, "Conflicting Instances",
                               "There are two metadata instances that have the same unique identifier (guid) but" +
                                           " have different types.",
                               OMRSEventErrorCode.CONFLICTING_INSTANCES),
    CONFLICTING_TYPE         (2, "Conflicting Type Version",
                               "An instance can not be processed because there is a mismatch in the type definition (TypeDef) version.",
                               OMRSEventErrorCode.CONFLICTING_TYPE),
    UNKNOWN_ERROR_CODE        (99, "Unknown Error Code",
                               "Unrecognized error code from incoming event.",
                               null);

    private static final long serialVersionUID = 1L;

    private int                errorCodeId;
    private String             errorCodeName;
    private String             errorCodeDescription;
    private OMRSEventErrorCode errorCodeEncoding;


    /**
     * Default constructor sets up the values for this enum instance.
     *
     * @param errorCodeId int identifier for the enum, used for indexing arrays etc with the enum.
     * @param errorCodeName String name for the enum, used for message content.
     * @param errorCodeDescription String default description for the enum, used when there is not natural
     *                             language resource bundle available.
     * @param errorCodeEncoding code value to use in OMRSEvents
     */
    OMRSInstanceEventErrorCode(int                errorCodeId,
                               String             errorCodeName,
                               String             errorCodeDescription,
                               OMRSEventErrorCode errorCodeEncoding)
    {
        this.errorCodeId = errorCodeId;
        this.errorCodeName = errorCodeName;
        this.errorCodeDescription = errorCodeDescription;
        this.errorCodeEncoding = errorCodeEncoding;
    }


    /**
     * Return the identifier for the enum, used for indexing arrays etc with the enum.
     *
     * @return int identifier
     */
    public int getErrorCodeId()
    {
        return errorCodeId;
    }


    /**
     * Return the name for the enum, used for message content.
     *
     * @return String name
     */
    public String getErrorCodeName()
    {
        return errorCodeName;
    }


    /**
     * Return the default description for the enum, used when there is not natural
     * language resource bundle available.
     *
     * @return String default description
     */
    public String getErrorCodeDescription()
    {
        return errorCodeDescription;
    }


    /**
     * Return the encoding to use in OMRSEvents.
     *
     * @return String OMRSEvent encoding for this errorCode
     */
    public OMRSEventErrorCode getErrorCodeEncoding()
    {
        return errorCodeEncoding;
    }
}