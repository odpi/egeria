/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.configuration.properties;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.VirtualConnection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefSummary;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * CohortConfig provides the configuration properties used to connect to an open metadata repository cohort.
 * <ul>
 *     <li>
 *         cohortName is a descriptive name for the cohort that is used primarily for messages and diagnostics.
 *         It is also used to create a default name for the cohort's OMRS Topic and the cohortRegistry's store
 *         if these names are not explicitly defined.
 *     </li>
 *     <li>
 *         cohortRegistryConnection is the connection properties necessary to create the connector to the
 *         cohort registry store.  This is the store where the cohort registry keeps information about its
 *         local metadata collection Id and details of other repositories in the cohort.
 *
 *         The default value is to use a local file called "cohort.registry" that is stored in the server's
 *         home directory.
 *     </li>
 *     <li>
 *         cohortOMRSTopicConnection is the connection properties necessary to create the connector to the OMRS Topic.
 *         This is used to send/receive events between members of the open metadata repository cohort.
 *     </li>
 *     <li>
 *         cohortOMRSTopicProtocolVersion defines the version of the event payload to use when communicating with other
 *         members of the cohort through the OMRS Topic.
 *     </li>
 *     <li>
 *         eventsToProcessRule defines how incoming events on the OMRS Topic should be processed.
 *     </li>
 *     <li>
 *         selectedTypesToProcess - list of TypeDefs used if the eventsToProcess rule (above) says
 *         "SELECTED_TYPES" - otherwise it is set to null.
 *     </li>
 * </ul>
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class CohortConfig extends AdminServicesConfigHeader
{
    private static final long    serialVersionUID = 1L;

    private String                           cohortName                     = null;
    private Connection                       cohortRegistryConnection       = null;
    private Connection                       cohortOMRSTopicConnection      = null;
    private OpenMetadataEventProtocolVersion cohortOMRSTopicProtocolVersion = null;
    private OpenMetadataExchangeRule         eventsToProcessRule            = null;
    private List<TypeDefSummary>             selectedTypesToProcess         = null;



    /**
     * Default constructor does nothing.
     */
    public CohortConfig()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public CohortConfig(CohortConfig  template)
    {
        super(template);

        if (template != null)
        {
            cohortName = template.getCohortName();
            cohortRegistryConnection = template.getCohortRegistryConnection();
            cohortOMRSTopicConnection = template.getCohortOMRSTopicConnection();
            cohortOMRSTopicProtocolVersion = template.getCohortOMRSTopicProtocolVersion();
            eventsToProcessRule = template.getEventsToProcessRule();
            selectedTypesToProcess = template.getSelectedTypesToProcess();
        }
    }


    /**
     * Return the name of the cohort.
     *
     * @return String name
     */
    public String getCohortName()
    {
        return cohortName;
    }


    /**
     * Set up the name of the cohort.
     *
     * @param cohortName String
     */
    public void setCohortName(String cohortName)
    {
        this.cohortName = cohortName;
    }


    /**
     * Set up the connection to the cohort registry store.
     *
     * @return Connection object
     */
    public Connection getCohortRegistryConnection()
    {
        return cohortRegistryConnection;
    }


    /**
     * Set up the connection for the cohort registry store.
     *
     * @param cohortRegistryConnection Connection object
     */
    public void setCohortRegistryConnection(Connection cohortRegistryConnection)
    {
        this.cohortRegistryConnection = cohortRegistryConnection;
    }


    /**
     * Return the connection to the cohort's OMRS Topic.
     *
     * @return Connection object
     */
    public Connection getCohortOMRSTopicConnection()
    {
        return cohortOMRSTopicConnection;
    }


    /**
     * Set up the connection to the cohort's OMRS Topic.
     *
     * @param cohortOMRSTopicConnection Connection object
     */
    public void setCohortOMRSTopicConnection(Connection cohortOMRSTopicConnection)
    {
        this.cohortOMRSTopicConnection = cohortOMRSTopicConnection;
    }


    /**
     * Return the protocol version to use when exchanging events amongst the cohort members.
     *
     * @return protocol version enum
     */
    public OpenMetadataEventProtocolVersion getCohortOMRSTopicProtocolVersion()
    {
        return cohortOMRSTopicProtocolVersion;
    }


    /**
     * Set up the protocol version to use when exchanging events amongst the cohort members.
     *
     * @param cohortOMRSTopicProtocolVersion protocol version enum
     */
    public void setCohortOMRSTopicProtocolVersion(OpenMetadataEventProtocolVersion cohortOMRSTopicProtocolVersion)
    {
        this.cohortOMRSTopicProtocolVersion = cohortOMRSTopicProtocolVersion;
    }


    /**
     * Return the rule indicating whether incoming metadata events from a cohort should be processed.
     *
     * @return OpenMetadataExchangeRule - NONE, JUST_TYPEDEFS, SELECTED_TYPES and ALL.
     */
    public OpenMetadataExchangeRule getEventsToProcessRule()
    {
        return eventsToProcessRule;
    }


    /**
     * Set up the rule indicating whether incoming metadata events from a cohort should be processed.
     *
     * @param eventsToProcessRule OpenMetadataExchangeRule - NONE, JUST_TYPEDEFS, SELECTED_TYPES and ALL.
     */
    public void setEventsToProcessRule(OpenMetadataExchangeRule eventsToProcessRule)
    {
        this.eventsToProcessRule = eventsToProcessRule;
    }


    /**
     * Return the list of TypeDefs used if the eventsToProcess rule (above) says "SELECTED_TYPES" - otherwise
     * it is set to null.
     *
     * @return list of TypeDefs that determine which metadata instances to process
     */
    public List<TypeDefSummary> getSelectedTypesToProcess()
    {
        if (selectedTypesToProcess == null)
        {
            return null;
        }
        else if (selectedTypesToProcess.isEmpty())
        {
            return null;
        }
        else
        {
            List<TypeDefSummary>  resultList = new ArrayList<>();

            for (TypeDefSummary  typeDefSummary : selectedTypesToProcess)
            {
                if (typeDefSummary != null)
                {
                    resultList.add(new TypeDefSummary((typeDefSummary)));
                }
            }

            return resultList;
        }
    }


    /**
     * Set up the list of TypeDefs used if the EventsToProcess rule (above) says "SELECTED_TYPES" - otherwise
     * it is set to null.
     *
     * @param selectedTypesToProcess list of TypeDefs that determine which metadata instances to process
     */
    public void setSelectedTypesToProcess(List<TypeDefSummary> selectedTypesToProcess)
    {
        this.selectedTypesToProcess = selectedTypesToProcess;
    }


    /**
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString()
    {
        return "CohortConfig{" +
                "cohortName='" + cohortName + '\'' +
                ", cohortRegistryConnection=" + cohortRegistryConnection +
                ", cohortOMRSTopicConnection=" + cohortOMRSTopicConnection +
                ", cohortOMRSTopicProtocolVersion=" + cohortOMRSTopicProtocolVersion +
                ", eventsToProcessRule=" + eventsToProcessRule +
                ", selectedTypesToProcess=" + selectedTypesToProcess +
                '}';
    }


    /**
     * Validate that an object is equal depending on their stored values.
     *
     * @param objectToCompare object
     * @return boolean result
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        CohortConfig that = (CohortConfig) objectToCompare;
        return Objects.equals(getCohortName(), that.getCohortName()) &&
                Objects.equals(getCohortRegistryConnection(), that.getCohortRegistryConnection()) &&
                Objects.equals(getCohortOMRSTopicConnection(), that.getCohortOMRSTopicConnection()) &&
                getCohortOMRSTopicProtocolVersion() == that.getCohortOMRSTopicProtocolVersion() &&
                getEventsToProcessRule() == that.getEventsToProcessRule() &&
                Objects.equals(getSelectedTypesToProcess(), that.getSelectedTypesToProcess());
    }



    /**
     * Return a hash code based on the values of this object.
     *
     * @return in hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(getCohortName(), getCohortRegistryConnection(), getCohortOMRSTopicConnection(),
                            getCohortOMRSTopicProtocolVersion(), getEventsToProcessRule(), getSelectedTypesToProcess());
    }
}
