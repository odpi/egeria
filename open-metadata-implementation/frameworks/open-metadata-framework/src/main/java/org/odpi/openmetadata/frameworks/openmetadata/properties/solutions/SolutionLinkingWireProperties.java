/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.solutions;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.LabeledRelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * SolutionLinkingWireProperties identifies a relationship between solution components that is part of an information supply chain segment implementation.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SolutionLinkingWireProperties extends LabeledRelationshipProperties
{
    private List<String> iscQualifiedNames = null;
    private boolean      oneWay            = true;
    private String       integrationStyle  = null;
    private String       protocol          = null;
    private String       frequency         = null;
    private String       dataExchanged     = null;

    /*
    | `integrationStyle` | e.g.API call, event stream, file transfer, database query,LDAP, network tap |
    | `protocol` |e.g.REST/HTTPS,OPC-UA,MQTT,SFTP,LDAP/S,ODBC,Syslog |
    | `frequency` |e.g.real-time,hourly,daily, on batch completion |
    | `dataExchanged` | Full explanation of what flows and why |
    */


    /**
     * Default constructor
     */
    public SolutionLinkingWireProperties()
    {
        super();
        super.typeName = OpenMetadataType.SOLUTION_LINKING_WIRE_RELATIONSHIP.typeName;
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public SolutionLinkingWireProperties(SolutionLinkingWireProperties template)
    {
        super(template);

        if (template != null)
        {
            iscQualifiedNames = template.getISCQualifiedNames();
            oneWay = template.getOneWay();
            integrationStyle = template.getIntegrationStyle();
            protocol = template.getProtocol();
            frequency = template.getFrequency();
            dataExchanged = template.getDataExchanged();
        }
    }


    /**
     * Return the information supply chain segments that his wire implements.
     *
     * @return list
     */
    public List<String> getISCQualifiedNames()
    {
        return iscQualifiedNames;
    }


    /**
     * Set up the information supply chain segments that his wire implements.
     *
     * @param iscQualifiedNames list
     */
    public void setISCQualifiedNames(List<String> iscQualifiedNames)
    {
        this.iscQualifiedNames = iscQualifiedNames;
    }



    /**
     * Returns whether the data is flowing one-way or bidirectional.
     *
     * @return boolean flag
     */
    public boolean getOneWay()
    {
        return oneWay;
    }


    /**
     * Set up whether the data is flowing one-way or bidirectional.
     *
     * @param oneWay boolean flag
     */
    public void setOneWay(boolean oneWay)
    {
        this.oneWay = oneWay;
    }


    /**
     * Returns the integration style of the lineage relationship.
     *
     * @return string
     */
    public String getIntegrationStyle()
    {
        return integrationStyle;
    }


    /**
     * Set up the integration style of the lineage relationship.
     *
     * @param integrationStyle string
     */
    public void setIntegrationStyle(String integrationStyle)
    {
        this.integrationStyle = integrationStyle;
    }


    /**
     * Returns the protocol used to exchange data.
     *
     * @return string
     */
    public String getProtocol()
    {
        return protocol;
    }


    /**
     * Set up the protocol used to exchange data.
     *
     * @param protocol string
     */
    public void setProtocol(String protocol)
    {
        this.protocol = protocol;
    }


    /**
     * Returns the frequency of the data exchange.
     *
     * @return string
     */
    public String getFrequency()
    {
        return frequency;
    }

    /**
     * Set up the frequency of the data exchange.
     *
     * @param frequency string
     */
    public void setFrequency(String frequency)
    {
        this.frequency = frequency;
    }


    /**
     * Returns the description of the data exchanged.
     *
     * @return string
     */
    public String getDataExchanged()
    {
        return dataExchanged;
    }


    /**
     * Set up the description of the data exchanged.
     *
     * @param dataExchanged string
     */
    public void setDataExchanged(String dataExchanged)
    {
        this.dataExchanged = dataExchanged;
    }



    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "SolutionLinkingWireProperties{" +
                "iscQualifiedNames=" + iscQualifiedNames +
                ", oneWay=" + oneWay +
                ", integrationStyle='" + integrationStyle + '\'' +
                ", protocol='" + protocol + '\'' +
                ", frequency='" + frequency + '\'' +
                ", dataExchanged='" + dataExchanged + '\'' +
                "} " + super.toString();
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
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        SolutionLinkingWireProperties that = (SolutionLinkingWireProperties) objectToCompare;
        return oneWay == that.oneWay && Objects.equals(iscQualifiedNames, that.iscQualifiedNames) && Objects.equals(integrationStyle, that.integrationStyle) && Objects.equals(protocol, that.protocol) && Objects.equals(frequency, that.frequency) && Objects.equals(dataExchanged, that.dataExchanged);
    }

    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), iscQualifiedNames, oneWay, integrationStyle, protocol, frequency, dataExchanged);
    }
}
