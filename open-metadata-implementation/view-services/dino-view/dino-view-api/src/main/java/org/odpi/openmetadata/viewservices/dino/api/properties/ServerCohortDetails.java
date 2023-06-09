/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.viewservices.dino.api.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.repositoryservices.connectors.stores.cohortregistrystore.properties.MemberRegistration;
import org.odpi.openmetadata.repositoryservices.properties.CohortDescription;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ServerCohortDetails {

    /*
     * A ServerCohortDetails object is constructed when a server overview is retrieved.
     * The server overview contains a map (by cohort name) with an entry for each of the cohorts the server is a membber of.
     * For each cohort, the map contains a ServerCohortDetails object which holds the:
     *   cohortDescription - the status and embedded connection details
     *   localRegistration - the registration of the local server in the cohort
     *   remoteRegistrations - the registrations of other servers in the cohort, as seen by the local server
     */


    private CohortDescription cohortDescription;

    private MemberRegistration localRegistration;

    private List<MemberRegistration> remoteRegistrations;



    /**
     * Default Constructor sets the properties to nulls
     */
    public ServerCohortDetails()
    {
        /*
         * Nothing to do.
         */
    }

    public ServerCohortDetails(CohortDescription cohortDescription,
                               MemberRegistration localRegistration,
                               List<MemberRegistration> remoteRegistrations) {


        this.cohortDescription   = cohortDescription;
        this.localRegistration   = localRegistration;
        this.remoteRegistrations = remoteRegistrations;

    }

    public CohortDescription getCohortDescription() {  return cohortDescription;  }

    public void setCohortDescription(CohortDescription cohortDescription) {
        this.cohortDescription = cohortDescription;
    }

    public MemberRegistration getLocalRegistration() {  return localRegistration;  }

    public void setLocalRegistration(MemberRegistration localRegistration) {
        this.localRegistration = localRegistration;
    }

    public List<MemberRegistration> getRemoteRegistrations() {  return remoteRegistrations;  }

    public void setRemoteRegistrations(List<MemberRegistration> remoteRegistrations) {
        this.remoteRegistrations = remoteRegistrations;
    }




}
