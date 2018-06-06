/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.repositoryservices.events;


import java.io.Serializable;

/**
 * OMRSEventDirection defines the origin of an OMRSEvent.  It is used primarily for logging and debug.
 */
public enum OMRSEventDirection implements Serializable
{
    UNKNOWN  (0, "<Unknown>     ", "Uninitialized event direction"),
    INBOUND  (1, "Inbound Event ", "Event from a remote member of the open metadata repository cluster."),
    OUTBOUND (2, "Outbound Event", "Event from local server to other members of the open metadata repository cluster.");

    private static final long serialVersionUID = 1L;


    private  int    eventDirectionCode;
    private  String eventDirectionName;
    private  String eventDirectionDescription;


    /**
     * Default constructor - sets up the specific values for this enum instance.
     *
     * @param eventDirectionCode - int identifier for the enum, used for indexing arrays etc with the enum.
     * @param eventDirectionName - String name for the enum, used for message content.
     * @param eventDirectionDescription - String default description for the enum, used when there is not natural
     *                             language resource bundle available.
     */
    OMRSEventDirection(int eventDirectionCode, String eventDirectionName, String eventDirectionDescription)
    {
        this.eventDirectionCode = eventDirectionCode;
        this.eventDirectionName = eventDirectionName;
        this.eventDirectionDescription = eventDirectionDescription;
    }


    /**
     * Return the identifier for the enum, used for indexing arrays etc with the enum.
     *
     * @return int identifier
     */
    public int getEventDirectionCode()
    {
        return eventDirectionCode;
    }


    /**
     * Return the name for the enum, used for message content.
     *
     * @return String name
     */
    public String getEventDirectionName()
    {
        return eventDirectionName;
    }


    /**
     * Return the default description for the enum, used when there is not natural
     * language resource bundle available.
     *
     * @return String default description
     */
    public String getEventDirectionDescription()
    {
        return eventDirectionDescription;
    }
}
