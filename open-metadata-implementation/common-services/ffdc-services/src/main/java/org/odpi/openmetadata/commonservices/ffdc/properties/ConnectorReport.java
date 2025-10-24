/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.ffdc.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDescription;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.ConfigurationPropertyType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.ActionTargetType;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.GuardType;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.RequestParameterType;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.RequestTypeType;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.AnalysisStepType;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.AnnotationTypeType;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * ConnectorReport is a collection of information provided by a connector provider that describes the operation of
 * a connector.  It is designed to aid an administrator setting up the configuration for a connector.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ConnectorReport
{
    private ComponentDescription            componentDescription             = null;
    private ConnectorType                   connectorType                    = null;
    private long                            refreshTimeInterval              = 0L;
    private boolean                         usesBlockingCalls                = false;
    private List<ConfigurationPropertyType> supportedConfigurationProperties = null;
    private List<RequestTypeType>           supportedRequestTypes            = null;
    private List<RequestParameterType>      supportedRequestParameters       = null;
    private List<ActionTargetType>          supportedActionTargetTypes       = null;
    private List<AnalysisStepType>          supportedAnalysisSteps           = null;
    private List<AnnotationTypeType>        supportedAnnotationTypes         = null;
    private List<RequestParameterType>      producedRequestParameters        = null;
    private List<ActionTargetType>          producedActionTargetTypes        = null;
    private List<GuardType>                 producedGuards                   = null;


    /**
     * Default constructor
     */
    public ConnectorReport()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ConnectorReport(ConnectorReport template)
    {
        if (template != null)
        {
            this.componentDescription             = template.getComponentDescription();
            this.connectorType                    = template.getConnectorType();
            this.refreshTimeInterval              = template.getRefreshTimeInterval();
            this.usesBlockingCalls                = template.getUsesBlockingCalls();
            this.supportedConfigurationProperties = template.getSupportedConfigurationProperties();
            this.supportedRequestTypes            = template.getSupportedRequestTypes();
            this.supportedRequestParameters       = template.getSupportedRequestParameters();
            this.supportedActionTargetTypes       = template.getSupportedActionTargetTypes();
            this.supportedAnalysisSteps           = template.getSupportedAnalysisSteps();
            this.supportedAnnotationTypes         = template.getSupportedAnnotationTypes();
            this.producedRequestParameters        = template.getProducedRequestParameters();
            this.producedActionTargetTypes        = template.getProducedActionTargetTypes();
            this.producedGuards                   = template.getProducedGuards();
        }
    }


    /**
     * Return the component description information that the connector uses to register with the audit log.
     *
     * @return component description structure
     */
    public ComponentDescription getComponentDescription()
    {
        return componentDescription;
    }


    /**
     * Set up the component description information that the connector uses to register with the audit log.
     *
     * @param componentDescription component description structure
     */
    public void setComponentDescription(ComponentDescription componentDescription)
    {
        this.componentDescription = componentDescription;
    }


    /**
     * Return the ConnectorType object.
     *
     * @return connectorType
     */
    public ConnectorType getConnectorType()
    {
        return connectorType;
    }


    /**
     * Set up the ConnectorType object.
     *
     * @param connectorType - connectorType object
     */
    public void setConnectorType(ConnectorType connectorType)
    {
        this.connectorType = connectorType;
    }


    /**
     * Return the recommended number of minutes between each call to the connector to refresh the metadata.  Zero means that refresh
     * is only called at server start up and whenever the refresh REST API request is made to the integration daemon.
     * If the refresh time interval is greater than 0 then additional calls to refresh are added spaced out by the refresh time interval.
     *
     * @return minute count
     */
    public long getRefreshTimeInterval()
    {
        return refreshTimeInterval;
    }


    /**
     * Set up the recommended number of minutes between each call to the connector to refresh the metadata.  Zero means that refresh
     * is only called at server start up and whenever the refresh REST API request is made to the integration daemon.
     * If the refresh time interval is greater than 0 then additional calls to refresh are added spaced out by the refresh time interval.
     *
     * @param refreshTimeInterval minute count
     */
    public void setRefreshTimeInterval(long refreshTimeInterval)
    {
        this.refreshTimeInterval = refreshTimeInterval;
    }


    /**
     * Return if the connector should be started in its own thread to allow it to block on a listening call.
     *
     * @return boolean flag
     */
    public boolean getUsesBlockingCalls()
    {
        return usesBlockingCalls;
    }


    /**
     * Set up if the connector should be started in its own thread to allow it to block on a listening call.
     *
     * @param usesBlockingCalls boolean flag
     */
    public void setUsesBlockingCalls(boolean usesBlockingCalls)
    {
        this.usesBlockingCalls = usesBlockingCalls;
    }


    /**
     * Return the list of supported configuration property types that describe how the connector's
     * behaviour can be modified.
     *
     * @return list of configuration property types
     */
    public List<ConfigurationPropertyType> getSupportedConfigurationProperties()
    {
        return supportedConfigurationProperties;
    }


    /**
     * Set up the list of supported configuration property types that describe how the connector's
     * behaviour can be modified.
     *
     * @param supportedConfigurationProperties list of configuration property types
     */
    public void setSupportedConfigurationProperties(List<ConfigurationPropertyType> supportedConfigurationProperties)
    {
        this.supportedConfigurationProperties = supportedConfigurationProperties;
    }


    /**
     * The request types returned are those that affect the governance service's behaviour.  Other request types may be used
     * to call the governance service, but they result in default behaviour.
     *
     * @return list of request types with special meaning
     */
    public List<RequestTypeType> getSupportedRequestTypes()
    {
        return supportedRequestTypes;
    }


    /**
     * Set up the supported request types.
     *
     * @param supportedRequestTypes list of request types with special meaning
     */
    public void setSupportedRequestTypes(List<RequestTypeType> supportedRequestTypes)
    {
        this.supportedRequestTypes = supportedRequestTypes;
    }


    /**
     * The request parameters returned are used by the governance service to control its behaviour.
     *
     * @return list of parameter names with special meaning
     */
    public List<RequestParameterType> getSupportedRequestParameters()
    {
        return supportedRequestParameters;
    }


    /**
     * Set up request parameters returned are used by the governance service to control its behaviour.
     *
     * @param supportedRequestParameters list of parameter names with special meaning
     */
    public void setSupportedRequestParameters(List<RequestParameterType> supportedRequestParameters)
    {
        this.supportedRequestParameters = supportedRequestParameters;
    }


    /**
     * The action target names returned are those that affect the governance service's behaviour.  Other action target names may be used
     * in a call the governance service, but they result in default behaviour.
     *
     * @return list of action target names with special meaning
     */
    public List<ActionTargetType> getSupportedActionTargetTypes()
    {
        return supportedActionTargetTypes;
    }


    /**
     * Set up the supported action target names
     *
     * @param supportedActionTargetTypes list of action target names with special meaning
     */
    public void setSupportedActionTargetTypes(List<ActionTargetType> supportedActionTargetTypes)
    {
        this.supportedActionTargetTypes = supportedActionTargetTypes;
    }

    /**
     * Return the list of analysis steps (survey action service only).
     *
     * @return list
     */
    public List<AnalysisStepType> getSupportedAnalysisSteps()
    {
        return supportedAnalysisSteps;
    }


    /**
     * Set up the list of analysis steps (survey action service only).
     *
     * @param supportedAnalysisSteps list
     */
    public void setSupportedAnalysisSteps(List<AnalysisStepType> supportedAnalysisSteps)
    {
        this.supportedAnalysisSteps = supportedAnalysisSteps;
    }


    /**
     * Return the list of annotation types (survey action service only).
     *
     * @return list
     */
    public List<AnnotationTypeType> getSupportedAnnotationTypes()
    {
        return supportedAnnotationTypes;
    }


    /**
     * Set up the list of annotation types (survey action service only).
     *
     * @param supportedAnnotationTypes list
     */
    public void setSupportedAnnotationTypes(List<AnnotationTypeType> supportedAnnotationTypes)
    {
        this.supportedAnnotationTypes = supportedAnnotationTypes;
    }


    /**
     * The request parameters returned are produced by the governance service.
     *
     * @return list of parameter names with special meaning
     */
    public List<RequestParameterType> getProducedRequestParameters()
    {
        return producedRequestParameters;
    }


    /**
     * Set up request parameters returned are returned by the governance service.
     *
     * @param producedRequestParameters list of parameter names with special meaning
     */
    public void setProducedRequestParameters(List<RequestParameterType> producedRequestParameters)
    {
        this.producedRequestParameters = producedRequestParameters;
    }


    /**
     * The action target names returned are those produced by the governance service.
     *
     * @return list of action target names with special meaning
     */
    public List<ActionTargetType> getProducedActionTargetTypes()
    {
        return producedActionTargetTypes;
    }


    /**
     * Set up the produced action target names
     *
     * @param producedActionTargetTypes list of action target names with special meaning
     */
    public void setProducedActionTargetTypes(List<ActionTargetType> producedActionTargetTypes)
    {
        this.producedActionTargetTypes = producedActionTargetTypes;
    }



    /**
     * The guards describe the output assessment from the governance action service.  The list returned is the complete list of
     * guards to expect from the governance action service.  They are used when defining governance action processes that choreograph
     * the execution of governance action services using the guards to determine the path in the process to take.
     *
     * @return list of guards produced by this service with descriptions
     */
    public List<GuardType> getProducedGuards()
    {
        return producedGuards;
    }


    /**
     * Set up the supported guards
     *
     * @param producedGuards list of guards produced by this service with descriptions
     */
    public void setProducedGuards(List<GuardType> producedGuards)
    {
        this.producedGuards = producedGuards;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "ConnectorReport{" +
                "componentDescription=" + componentDescription +
                ", connectorType=" + connectorType +
                ", refreshTimeInterval=" + refreshTimeInterval +
                ", usesBlockingCalls=" + usesBlockingCalls +
                ", supportedConfigurationProperties=" + supportedConfigurationProperties +
                ", supportedRequestTypes=" + supportedRequestTypes +
                ", supportedRequestParameters=" + supportedRequestParameters +
                ", supportedActionTargetTypes=" + supportedActionTargetTypes +
                ", supportedAnalysisSteps=" + supportedAnalysisSteps +
                ", supportedAnnotationTypes=" + supportedAnnotationTypes +
                ", producedRequestParameters=" + producedRequestParameters +
                ", producedActionTargetTypes=" + producedActionTargetTypes +
                ", producedGuards=" + producedGuards +
                '}';
    }

    /**
     * Return comparison result based on the content of the properties.
     *
     * @param objectToCompare test object
     * @return result of comparison
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
        ConnectorReport that = (ConnectorReport) objectToCompare;
        return refreshTimeInterval == that.refreshTimeInterval &&
                       usesBlockingCalls == that.usesBlockingCalls &&
                       Objects.equals(componentDescription, that.componentDescription) &&
                       Objects.equals(connectorType, that.connectorType) &&
                       Objects.equals(supportedConfigurationProperties, that.supportedConfigurationProperties) &&
                       Objects.equals(supportedRequestTypes, that.supportedRequestTypes) &&
                       Objects.equals(supportedRequestParameters, that.supportedRequestParameters) &&
                       Objects.equals(supportedActionTargetTypes, that.supportedActionTargetTypes) &&
                       Objects.equals(supportedAnalysisSteps, that.supportedAnalysisSteps) &&
                       Objects.equals(supportedAnnotationTypes, that.supportedAnnotationTypes) &&
                       Objects.equals(producedRequestParameters, that.producedRequestParameters) &&
                       Objects.equals(producedActionTargetTypes, that.producedActionTargetTypes) &&
                       Objects.equals(producedGuards, that.producedGuards);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(componentDescription, connectorType, refreshTimeInterval, usesBlockingCalls,
                            supportedConfigurationProperties, supportedRequestTypes,
                            supportedRequestParameters, supportedActionTargetTypes, supportedAnalysisSteps,
                            supportedAnnotationTypes, producedRequestParameters, producedActionTargetTypes,
                            producedGuards);
    }
}
