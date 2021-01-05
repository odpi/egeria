/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React, { useState } from "react";
import {
  DataTable,
  MultiSelect,
  TableContainer,
  Table,
  TableHead,
  TableRow,
  TableSelectRow,
  TableCell,
  TableHeader,
  TableBody,
} from "carbon-components-react";

/**
 * The view part of searching for nodes. 
 * <ul>
 * <li>It displays the search input box into which the user types the search criteria.</li>
 * <li>the search criteria is passed back in a callback to the caller, who debounces the search</li>
 * <li>the caller then communicates the search results using props.</li>
 * <li>Additional columns can be specified.</li>
 * <li>The search results can be selected.</li>
 * </ul>
 * @param nodeType type of node
 * @param nodes the rows of the table 
 * @param setSelected function to call when something is selected.
 */

export default function NodeTableView({nodeType, nodes, setSelected}) {
  console.log("NodeTableView ");

  // properties that will be displayed be default for a node
  const mainProperties = [
    {
      key: "name",
      text: "Name",
    },
    {
      key: "description",
      text: "Description",
    },
    {
      key: "qualifiedName",
      text: "Qualified Name",
    },
  ];

  const [headerData, setHeaderData] = useState(mainProperties);
  const [searchTableKey, setSearchTableKey] = useState(1);

  const additionalProperties = calculateAdditionalProperties();
  let selectedAdditionalProperties = [];

  // calculate the results table header - this will be the default columns plus any additional coliumns the user has specified
  function calculateHeaderData() {
    let allProperties = mainProperties;
    if (
      selectedAdditionalProperties !== undefined &&
      selectedAdditionalProperties &&
      selectedAdditionalProperties.length > 0
    ) {
      allProperties = mainProperties.concat(selectedAdditionalProperties);
    }
    console.log(allProperties);
    setHeaderData(allProperties);
  }
  // Additional attributes can be selected so more columns can be shown
  // the additional attriniutes are in selectedAdditionalProperties
  const onAdditionalAttributesChanged = (items) => {
    console.log("onAdditionalAttributesChanged");
    console.log(items.selectedItems);
    selectedAdditionalProperties = [];
    const selectedItems = items.selectedItems;
    for (let i = 0; i < selectedItems.length; i++) {
      let item = {};
      item.key = selectedItems[i].id;
      item.text = selectedItems[i].text;
      selectedAdditionalProperties.push(item);
    }
    // render the table by recalculating the header state based on the new values
    calculateHeaderData();
  };
  // calculate the columns from the main attributes and the additional attributes.
  function calculateAdditionalProperties() {
    let items = [];
    nodeType.attributes.map(function (attribute) {
      if (
        attribute.key !== "name" &&
        attribute.key !== "qualifiedName" &&
        attribute.key !== "description"
      ) {
        let item = {};
        item.id = attribute.key;
        item.text = attribute.label;
        items.push(item);
      }
    });
    return items;
  }

  const onSelectRow = (row) => {
    console.log("onSelectRow id="+ row.id);
    setSelected(row.id);
  };

  return (
    <div className="top-search-container">
      <div className="top-search-item">
        <div className="search-container">
          {nodeType && nodeType.attributes.length > 3 && (
            <div className="search-item">
              <div className="bx--form-item">
                <label htmlFor="multiselect">Show more properties </label>
                <div style={{ width: 150 }}>
                  <MultiSelect
                    id="multiselect"
                    onChange={onAdditionalAttributesChanged}
                    items={additionalProperties}
                    itemToString={(item) => (item ? item.text : "")}
                  />
                </div>
              </div>
            </div>
          )}
          <div className="search-item">
            <DataTable
              radio
              // key={searchTableKey}
              isSortable
              rows={nodes}
              headers={headerData}
              render={({
                rows,
                headers,
                getHeaderProps,
                getSelectionProps,
                getRowProps,
              }) => (
                <TableContainer>
                  <Table>
                    <TableHead>
                      <TableRow>
                        <TableHeader />
                        {headers.map((header) => (
                          <TableHeader
                            {...getHeaderProps({ header })}
                            key={header.key}
                          >
                            {header.text}
                          </TableHeader>
                        ))}
                      </TableRow>
                    </TableHead>
                    <TableBody>
                      {rows.map((row) => (
                        <TableRow {...getRowProps({ row })} key={row.id}>
                          <TableSelectRow
                            {...getSelectionProps({
                              row,
                              onClick: () => onSelectRow(row),
                            })}
                          />
                          {row.cells.map((cell) => (
                            <TableCell key={cell.id}>{cell.value}</TableCell>
                          ))}
                        </TableRow>
                      ))}
                    </TableBody>
                  </Table>
                </TableContainer>
              )}
            />
          </div>
        </div>
      </div>
    </div>
  );
}

