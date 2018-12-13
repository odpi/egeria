/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservice.assetcatalog.model.rest.body;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservice.assetcatalog.model.SequenceOrderType;
import org.odpi.openmetadata.accessservice.assetcatalog.model.Status;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * SearchParameters provides a structure to make the assets's search results more precise.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchParameters {

    private Integer limit = 0;
    private Integer offset = 0;
    private String orderProperty;
    private SequenceOrderType orderType;
    private Status status;
    private Boolean excludeDeleted = Boolean.TRUE;
    private Integer level = 0;
    private List<String> types;
    private String propertyName;
    private String propertyValue;

    /**
     * Return the maximum page size supported by this server.
     *
     * @return max number of elements that can be returned on a request.
     */
    public Integer getLimit() {
        return limit;
    }

    /**
     * Set up the limit the result set to only include the specified number of entries
     *
     * @param limit max number of elements that can be returned on a request.
     */
    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    /**
     * Return the  start offset of the result set
     *
     * @return the start offset of result
     */
    public Integer getOffset() {
        return offset;
    }

    /**
     * Set up the start offset of the result set for pagination
     *
     * @param offset start offset of the result set
     */
    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    /**
     * Return the name of the property that is to be used to sequence the results
     *
     * @return the name of the property that is to be used to sequence the results
     */
    public String getOrderProperty() {
        return orderProperty;
    }

    /**
     * Set up the name of the property that is to be used to sequence the results
     *
     * @param orderProperty the name of the property that is to be used to sequence the results
     */
    public void setOrderProperty(String orderProperty) {
        this.orderProperty = orderProperty;
    }

    /**
     * Return the enum defining how the results should be ordered
     *
     * @return the enum defining how the results should be ordered
     */
    public SequenceOrderType getOrderType() {
        return orderType;
    }

    /**
     * Set up the enum defining how the results should be ordered
     *
     * @param orderType the enum defining how the results should be ordered
     */
    public void setOrderType(SequenceOrderType orderType) {
        this.orderType = orderType;
    }

    /**
     * @return Status to restrict the result
     */
    public Status getStatus() {
        return status;
    }

    /**
     * By default, all the assets/relationships are returned.
     * However, it is possible to specify a single status (eg ACTIVE) to restrict the results to.
     *
     * @param status to restrict the result
     */
    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * Return the boolean that exclude deleted assets from result
     *
     * @return excludeDeleted to restrict the result
     */
    public Boolean getExcludeDeleted() {
        return excludeDeleted;
    }

    /**
     * Set up if deleted assets are excluded from the result.
     * By default, deleted assets are not included in the result.
     *
     * @param excludeDeleted deleted assets are excluded (or not) in the result.
     */
    public void setExcludeDeleted(Boolean excludeDeleted) {
        this.excludeDeleted = excludeDeleted;
    }

    /**
     * Return the number of the relationships out from the starting asset that the query will traverse to gather results.
     *
     * @return the number of the relationships that will be travers to get the result
     */
    public Integer getLevel() {
        return level;
    }

    /**
     * Set up the number of the relationships out from the starting asset that the query will traverse to gather results.
     *
     * @param level the number of the relationships that will be travers to get the result
     */
    public void setLevel(Integer level) {
        this.level = level;
    }

    /**
     * Return the list of types to search for. Null means any types.
     *
     * @return the list of types to search for
     */
    public List<String> getTypes() {
        return types;
    }

    /**
     * Set up the list of types to search for. Null means any types.
     *
     * @param types the list of types to search for
     */
    public void setTypes(List<String> types) {
        this.types = types;
    }

    /**
     * Return the property name searched
     *
     * @return the property name searched
     */
    public String getPropertyName() {
        return propertyName;
    }

    /**
     * Set up the property name searched.
     *
     * @param propertyName the property name searched
     */
    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    /**
     * Return the property value searched
     *
     * @return the property value searched
     */
    public String getPropertyValue() {
        return propertyValue;
    }

    /**
     * Set up the property value searched
     *
     * @param propertyValue the property value searched
     */
    public void setPropertyValue(String propertyValue) {
        this.propertyValue = propertyValue;
    }
}
