/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.assetcatalog.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Date;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Asset implements Serializable {

    private static final long serialVersionUID = 1L;

    private String guid;
    private String metadataCollectionId;
    private String name;
    private String createdBy;
    private Date createTime;
    private String updatedBy;
    private Date updateTime;
    private Long version;
    private String status;
    private String typeDefName;
    private String typeDefDescription;
    private String url;


    /**
     * Returns the qualified name of the asset
     *
     * @return string - the name of the asset
     */
    public String getName() {
        return name;
    }

    /**
     * Set up the (qualified) name of the asset
     *
     * @param name - the name of the asset
     */
    public void setName(String name) {
        this.name = name;
    }




    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
