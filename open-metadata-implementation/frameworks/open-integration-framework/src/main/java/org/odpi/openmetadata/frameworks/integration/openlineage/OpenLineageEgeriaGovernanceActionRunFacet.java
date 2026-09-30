/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.openlineage;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.net.URI;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * This class represents Egeria's custom egeria_governanceAction run facet.  It describes the governance action
 * that a run represents, including the information supply chain that the governance action is part of.
 * It follows the facet schema at {@value #SCHEMA_URL}.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenLineageEgeriaGovernanceActionRunFacet extends OpenLineageRunFacet
{
    /**
     * Key of the facet in the run's facets.
     */
    public static final String FACET_NAME = "egeria_governanceAction";

    /**
     * Location of the facet's schema.
     */
    public static final String SCHEMA_URL = "https://egeria-project.org/openlineage/facets/1-0-0/EgeriaGovernanceActionRunFacet.json#/$defs/EgeriaGovernanceActionRunFacet";

    private String iscQualifiedName         = null;
    private String engineActionGUID         = null;
    private String governanceEngineName     = null;
    private String requestType              = null;
    private String governanceActionTypeName = null;
    private String processName              = null;
    private String processStepName          = null;


    /**
     * Default constructor
     */
    public OpenLineageEgeriaGovernanceActionRunFacet()
    {
        super(URI.create(SCHEMA_URL));
    }


    /**
     * Return the qualified name of the information supply chain that the governance action is part of.
     *
     * @return string
     */
    public String getIscQualifiedName()
    {
        return iscQualifiedName;
    }


    /**
     * Set up the qualified name of the information supply chain that the governance action is part of.
     *
     * @param iscQualifiedName string
     */
    public void setIscQualifiedName(String iscQualifiedName)
    {
        this.iscQualifiedName = iscQualifiedName;
    }


    /**
     * Return the unique identifier of the engine action that describes this run.
     *
     * @return string
     */
    public String getEngineActionGUID()
    {
        return engineActionGUID;
    }


    /**
     * Set up the unique identifier of the engine action that describes this run.
     *
     * @param engineActionGUID string
     */
    public void setEngineActionGUID(String engineActionGUID)
    {
        this.engineActionGUID = engineActionGUID;
    }


    /**
     * Return the name of the governance engine that ran the governance action.
     *
     * @return string
     */
    public String getGovernanceEngineName()
    {
        return governanceEngineName;
    }


    /**
     * Set up the name of the governance engine that ran the governance action.
     *
     * @param governanceEngineName string
     */
    public void setGovernanceEngineName(String governanceEngineName)
    {
        this.governanceEngineName = governanceEngineName;
    }


    /**
     * Return the request type that selected the governance service.
     *
     * @return string
     */
    public String getRequestType()
    {
        return requestType;
    }


    /**
     * Set up the request type that selected the governance service.
     *
     * @param requestType string
     */
    public void setRequestType(String requestType)
    {
        this.requestType = requestType;
    }


    /**
     * Return the name of the governance action type that the engine action was created from.
     *
     * @return string
     */
    public String getGovernanceActionTypeName()
    {
        return governanceActionTypeName;
    }


    /**
     * Set up the name of the governance action type that the engine action was created from.
     *
     * @param governanceActionTypeName string
     */
    public void setGovernanceActionTypeName(String governanceActionTypeName)
    {
        this.governanceActionTypeName = governanceActionTypeName;
    }


    /**
     * Return the name of the governance action process that the engine action is part of.
     *
     * @return string
     */
    public String getProcessName()
    {
        return processName;
    }


    /**
     * Set up the name of the governance action process that the engine action is part of.
     *
     * @param processName string
     */
    public void setProcessName(String processName)
    {
        this.processName = processName;
    }


    /**
     * Return the name of the governance action process step that the engine action implements.
     *
     * @return string
     */
    public String getProcessStepName()
    {
        return processStepName;
    }


    /**
     * Set up the name of the governance action process step that the engine action implements.
     *
     * @param processStepName string
     */
    public void setProcessStepName(String processStepName)
    {
        this.processStepName = processStepName;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "OpenLineageEgeriaGovernanceActionRunFacet{" +
                       "iscQualifiedName='" + iscQualifiedName + '\'' +
                       ", engineActionGUID='" + engineActionGUID + '\'' +
                       ", governanceEngineName='" + governanceEngineName + '\'' +
                       ", requestType='" + requestType + '\'' +
                       ", governanceActionTypeName='" + governanceActionTypeName + '\'' +
                       ", processName='" + processName + '\'' +
                       ", processStepName='" + processStepName + '\'' +
                       ", _producer=" + get_producer() +
                       ", _schemaURL=" + get_schemaURL() +
                       ", additionalProperties=" + getAdditionalProperties() +
                       '}';
    }


    /**
     * Compare the values of the supplied object with those stored in the current object.
     *
     * @param objectToCompare supplied object
     * @return boolean result of comparison
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
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        OpenLineageEgeriaGovernanceActionRunFacet that = (OpenLineageEgeriaGovernanceActionRunFacet) objectToCompare;
        return Objects.equals(iscQualifiedName, that.iscQualifiedName) &&
                       Objects.equals(engineActionGUID, that.engineActionGUID) &&
                       Objects.equals(governanceEngineName, that.governanceEngineName) &&
                       Objects.equals(requestType, that.requestType) &&
                       Objects.equals(governanceActionTypeName, that.governanceActionTypeName) &&
                       Objects.equals(processName, that.processName) &&
                       Objects.equals(processStepName, that.processStepName);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), iscQualifiedName, engineActionGUID, governanceEngineName, requestType, governanceActionTypeName, processName, processStepName);
    }
}
