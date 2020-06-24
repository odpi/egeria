/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React, { useState, useContext } from "react";
import { GlossaryAuthorContext } from "../../contexts/GlossaryAuthorContext";
import {
  Accordion,
  AccordionItem,
  DataTable,
  MultiSelect,
  TableContainer,
  Table,
  TableHead,
  TableRow,
  TableSelectAll,
  TableSelectRow,
  TableCell,
  TableHeader,
  TableBody,
} from "carbon-components-react";

const NodeSearch = (props) => {
  console.log("NodeSearch");
  const glossaryAuthorContext = useContext(GlossaryAuthorContext);

  const [results, setResults] = useState([]);
  const [errorMsg, setErrorMsg] = useState([]);
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
  const additionalProperties = calculateAdditionalProperties();
  let selectedAdditionalProperties = [];
  function calculateHeaderData() {

    let allProperties = mainProperties;
    if (selectedAdditionalProperties !== undefined && selectedAdditionalProperties && selectedAdditionalProperties.length >0) {
      console.log("selectedAdditionalProperties.selectedItems 1");
      console.log(selectedAdditionalProperties);
      allProperties = mainProperties.concat(selectedAdditionalProperties);

    }  
    console.log("allProperties 1");
    console.log(allProperties);
    setHeaderData(allProperties);
  }
  const onAdditionalAttributesChanged = (items) => {
    console.log("onAdditionalAttributesChanged");
    console.log(items.selectedItems);
    selectedAdditionalProperties = [];
    const selectedItems = items.selectedItems;
    for (let i=0;i < selectedItems.length; i++) {
      let item = {};
      item.key = selectedItems[i].id;
      item.text= selectedItems[i].text;
      selectedAdditionalProperties.push(item);
    }
    // render the table by recalculating the header state based on the new values
    calculateHeaderData(); 
  };
  function calculateAdditionalProperties() {
    let items = [];
    glossaryAuthorContext.currentNodeType.attributes.map(function (attribute) {
      if (
        attribute.key != "name" &&
        attribute.key != "qualifiedName" &&
        attribute.key != "description"
      ) {
        let item = {};
        item.id = attribute.key;
        item.text = attribute.label;
        items.push(item);
      }
    });
    return items;
  }

  const isSelectedNode = () => {
    let isSelected = false;
    if (glossaryAuthorContext.selectedNode) {
      isSelected = true;
    }
    return isSelected;
  };

  const handleOnChange = (e) => {
    e.preventDefault();
    if (e.target.value && e.target.value.length > 0) {
      const fetchUrl =
        glossaryAuthorContext.currentNodeType.url +
        "?searchCriteria=" +
        e.target.value;
      fetch(fetchUrl, {
        method: "get",
        headers: {
          Accept: "application/json",
          "Content-Type": "application/json",
        },
      })
        .then((res) => res.json())
        .then((res) => {
          const nodesArray = res[glossaryAuthorContext.currentNodeType.plural];
          // if there is a node response then we have successfully created a node
          if (nodesArray) {
            if (nodesArray.length > 0) {
              let nodeRows = nodesArray.map(function (node, index) {
                let row = {};
                row.id = index;
                // row.name = node.name;
                // console.log(" name is " + row.name);
                // row.description = node.description;
                // row.qualifiedName = node.qualifiedName;
                for (const property in node) {
                  console.log("result property is ", property);
                  if (property == "glossary") {
                    const glossary = node[property];
                    row.glossaryName = glossary.name;
                    row.glossaryGuid = glossary.guid;
                  } else if (property == "systemAttributes") {
                    row.guid = node[property].guid;
                  } else {
                    row[property] = node[property];
                  }
                }
                return row;
              });
              setResults(nodeRows);
            } else {
              // no results
              setResults([]);
            }
          } else {
            setErrorMsg("Create Failed with code " + res.errno);
          }
        })
        .catch((res) => {
          setErrorMsg("Create Failed");
        });
    }
  };
  return (
    <div>
      {glossaryAuthorContext.currentNodeType &&
        glossaryAuthorContext.currentNodeType.attributes.length > 3 && (
          <Accordion>
            <AccordionItem title="Show additional properties">
              <div class="bx--form-item">
                <div style={{ width: 150 }}>
                  <MultiSelect
                    onChange= {onAdditionalAttributesChanged} 
                    items={additionalProperties}
                    itemToString={(item) => (item ? item.text : "")}
                  />
                </div>
              </div>
            </AccordionItem>
          </Accordion>
        )}
      <div data-search role="search" class="bx--search bx--search--l">
        <label
          id="search-input-label-1"
          class="bx--label"
          for="search__input-1"
        >
          Search
        </label>
        <input
          class="bx--search-input"
          type="text"
          id="search__input-1"
          onChange={handleOnChange}
          placeholder="Search"
        />
        <svg
          focusable="false"
          preserveAspectRatio="xMidYMid meet"
          xmlns="http://www.w3.org/2000/svg"
          class="bx--search-magnifier"
          width="16"
          height="16"
          viewBox="0 0 16 16"
          aria-hidden="true"
        >
          <path d="M15,14.3L10.7,10c1.9-2.3,1.6-5.8-0.7-7.7S4.2,0.7,2.3,3S0.7,8.8,3,10.7c2,1.7,5,1.7,7,0l4.3,4.3L15,14.3z M2,6.5	C2,4,4,2,6.5,2S11,4,11,6.5S9,11,6.5,11S2,9,2,6.5z"></path>
        </svg>
        <button
          class="bx--search-close bx--search-close--hidden"
          title="Clear search
            input"
          aria-label="Clear search input"
        >
          <svg
            focusable="false"
            preserveAspectRatio="xMidYMid meet"
            xmlns="http://www.w3.org/2000/svg"
            class="bx--search-clear"
            width="20"
            height="20"
            viewBox="0 0 32 32"
            aria-hidden="true"
          >
            <path d="M24 9.4L22.6 8 16 14.6 9.4 8 8 9.4 14.6 16 8 22.6 9.4 24 16 17.4 22.6 24 24 22.6 17.4 16 24 9.4z"></path>
          </svg>
        </button>
      </div>
      <DataTable
        isSortable
        rows={results}
        headers={headerData}
        render={({
          rows,
          headers,
          getHeaderProps,
          getSelectionProps,
          getRowProps,
        }) => (
          <TableContainer
            title={glossaryAuthorContext.currentNodeType.typeName}
          >
            <Table>
              <TableHead>
                <TableRow>
                  <TableSelectAll {...getSelectionProps()} />
                  {headers.map((header) => (
                    <TableHeader {...getHeaderProps({ header })}>
                      {header.text}
                    </TableHeader>
                  ))}
                </TableRow>
              </TableHead>
              <TableBody>
                {rows.map((row) => (
                  <TableRow {...getRowProps({ row })}>
                    <TableSelectRow {...getSelectionProps({ row })} />
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
  );
};

export default NodeSearch;
