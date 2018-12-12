package org.odpi.openmetadata.accessservices.dataengine.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * GUIDResponse is the response structure used on the OMAS REST API calls that return a
 * unique identifier (guid) object as a response.
 */
@EqualsAndHashCode(callSuper = true)
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@Data
public class GUIDResponse extends DataEngineOMASAPIResponse
{
    private String   guid = null;


    /**
     * Default constructor
     */
    public GUIDResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public GUIDResponse(GUIDResponse  template)
    {
        super(template);

        if (template != null)
        {
            this.guid = template.getGuid();
        }
    }
}
