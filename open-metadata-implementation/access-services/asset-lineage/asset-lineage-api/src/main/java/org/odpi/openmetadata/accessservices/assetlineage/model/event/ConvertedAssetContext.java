/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.model.event;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.assetlineage.model.assetContext.AssetLineageEvent;
import org.odpi.openmetadata.accessservices.assetlineage.model.assetContext.Element;
import org.odpi.openmetadata.accessservices.assetlineage.model.assetContext.Term;
import org.odpi.openmetadata.accessservices.assetlineage.model.assetContext.Connection;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConvertedAssetContext extends AssetLineageEvent {

    private Element baseAsset;
    private Map<String,Element> context;
    private Connection connection;

    public Element getBaseAsset() {
        return baseAsset;
    }

    public void setBaseAsset(Element superAsset) {
        this.baseAsset = superAsset;
    }

    public Map<String, Element> getContext() {
        return context;
    }

    public void setContext(Map<String, Element> context) {
        this.context = context;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void updateContext(String key,Element value){
        this.context.put(key,value);
    }
}
