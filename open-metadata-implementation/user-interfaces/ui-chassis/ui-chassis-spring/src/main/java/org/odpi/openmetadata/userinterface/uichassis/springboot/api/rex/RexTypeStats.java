/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.api.rex;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;


import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class RexTypeStats {

    private String  typeGUID;
    private Integer count;

    public RexTypeStats(String typeGUID, Integer count) {
       this.typeGUID = typeGUID;
       this.count = count;
    }

    /*
     * Getters for Jackson
     */

    public String getTypeGUID() { return typeGUID; }
    public Integer getCount() { return count; }



    public void setTypeGUID(String typeGUID) { this.typeGUID = typeGUID; }
    public void setCount(Integer count) { this.count = count; }



    @Override
    public String toString()
    {
        return "RexTypeStats{" +
                ", typeGUID=" + typeGUID +
                ", count=" + count +
                '}';
    }



}
