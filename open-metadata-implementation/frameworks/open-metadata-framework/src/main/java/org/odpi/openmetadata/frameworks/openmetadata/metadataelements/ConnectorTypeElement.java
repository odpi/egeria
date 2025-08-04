/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.connections.ConnectorTypeProperties;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ConnectorTypeElement contains the properties and header for a connector type retrieved from the metadata repository.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ConnectorTypeElement extends OpenMetadataRootElement
{
    private List<RelatedMetadataElementSummary> connections = null;


    /**
     * Default constructor
     */
    public ConnectorTypeElement()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ConnectorTypeElement(ConnectorTypeElement template)
    {
        super (template);

        if (template != null)
        {
            connections = template.getConnections();
        }
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ConnectorTypeElement(OpenMetadataRootElement template)
    {
        super (template);
    }



    /**
     * Return the connections using this connector type.
     *
     * @return list
     */
    public List<RelatedMetadataElementSummary> getConnections()
    {
        return connections;
    }



    public void setConnections(List<RelatedMetadataElementSummary> connections)
    {
        this.connections = connections;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "ConnectorTypeElement{" +
                "connections=" + connections +
                "} " + super.toString();
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
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        ConnectorTypeElement that = (ConnectorTypeElement) objectToCompare;
        return Objects.equals(connections, that.connections);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), connections);
    }
}
