/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.resource.apacheatlas.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AtlasAuditEventV2 describes the structure of an audit event
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AtlasAuditEventV2
{
    private String              userName    = null;
    private AtlasAuditOperation operation   = null;
    private String              params      = null;
    private Date                startTime   = null;
    private Date                endTime     = null;
    private String              clientId    = null;
    private String              result      = null;
    private long                resultCount = 0L;


    public AtlasAuditEventV2()
    {
    }


    public String getUserName()
    {
        return userName;
    }


    public void setUserName(String userName)
    {
        this.userName = userName;
    }


    public AtlasAuditOperation getOperation()
    {
        return operation;
    }


    public void setOperation(AtlasAuditOperation operation)
    {
        this.operation = operation;
    }


    public String getParams()
    {
        return params;
    }


    public void setParams(String params)
    {
        this.params = params;
    }


    public Date getStartTime()
    {
        return startTime;
    }


    public void setStartTime(Date startTime)
    {
        this.startTime = startTime;
    }


    public Date getEndTime()
    {
        return endTime;
    }


    public void setEndTime(Date endTime)
    {
        this.endTime = endTime;
    }


    public String getClientId()
    {
        return clientId;
    }


    public void setClientId(String clientId)
    {
        this.clientId = clientId;
    }


    public String getResult()
    {
        return result;
    }


    public void setResult(String result)
    {
        this.result = result;
    }


    public long getResultCount()
    {
        return resultCount;
    }


    public void setResultCount(long resultCount)
    {
        this.resultCount = resultCount;
    }


    @Override
    public String toString()
    {
        return "AtlasAuditEventV2{" +
                       "userName='" + userName + '\'' +
                       ", operation=" + operation +
                       ", params='" + params + '\'' +
                       ", startTime=" + startTime +
                       ", endTime=" + endTime +
                       ", clientId='" + clientId + '\'' +
                       ", result='" + result + '\'' +
                       ", resultCount=" + resultCount +
                       '}';
    }
}
