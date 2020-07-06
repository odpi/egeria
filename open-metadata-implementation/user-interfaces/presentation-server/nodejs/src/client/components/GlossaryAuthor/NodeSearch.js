/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React, { useState, useContext } from "react";
import { GlossaryAuthorContext } from "../../contexts/GlossaryAuthorContext";
import Delete16 from "../../images/Egeria_delete_16";
import Edit16 from "../../images/Egeria_edit_16";
import {
  Accordion,
  AccordionItem,
  Button,
  DataTable,
  MultiSelect,
  Pagination,
  TableContainer,
  Table,
  TableHead,
  TableRow,
  TableSelectAll,
  TableSelectRow,
  TableCell,
  TableHeader,
  TableBody,
  TableToolbar,
  TableToolbarContent,
  TableBatchActions,
  TableBatchAction,
  TableToolbarSearch,
} from "carbon-components-react";

const NodeSearch = (props) => {
  console.log("NodeSearch");
  const glossaryAuthorContext = useContext(GlossaryAuthorContext);

  const [results, setResults] = useState([]);
  const [total, setTotal] = useState(0);
  const [currentPage, setCurrentPage] = useState([]);
  const [pageNumber, setPageNumber] = useState(1);
  const [pageSize, setPageSize] = useState(10);
  const [errorMsg, setErrorMsg] = useState();
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

  const paginationProps = () => ({
    disabled: false,
    page: pageNumber,
    pagesUnknown: true,
    pageInputDisabled: false,
    backwardText: "Previous page",
    forwardText: "Next page",
    totalItems: total,
    pageSize: pageSize,
    pageSizes: [10, 50, 100],
    itemsPerPageText: "Items per page:",
    onChange: onPaginationChange,
  });
  const onPaginationChange = (paginationOptions) => {
    console.log("onPaginationChange");
    console.log(paginationOptions);
    if (results && results.length > 0) {
      const pageSize = paginationOptions.pageSize;
      // if page = 1 and pageSize 10, currentPageStart = 1
      // if page = 2 and pageSize 10, currentPageStart = 11
      // if page = 2 and pageSize 10 and results.length = 15, currentPageStart = 11 , currentPageSize = 5

      const currentPageStart =
        (paginationOptions.page - 1) * paginationOptions.pageSize;
      let currentPageSize = pageSize;
      // if the last page is not complete ensure that we only specify up the end of the what is actually there in the results.
      if (currentPageStart + currentPageSize - 1 > results.length) {
        currentPageSize = results.length - currentPageStart;
      }
      const resultsToshow = results.slice(
        currentPageStart,
        currentPageStart + currentPageSize
      );
      console.log("resultsToshow");
      console.log(resultsToshow);
      setCurrentPage(resultsToshow);
    } else {
      setCurrentPage([]);
    }
  };
  const [headerData, setHeaderData] = useState(mainProperties);
  const additionalProperties = calculateAdditionalProperties();
  let selectedAdditionalProperties = [];

  function calculateHeaderData() {
    let allProperties = mainProperties;
    if (
      selectedAdditionalProperties !== undefined &&
      selectedAdditionalProperties &&
      selectedAdditionalProperties.length > 0
    ) {
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
    for (let i = 0; i < selectedItems.length; i++) {
      let item = {};
      item.key = selectedItems[i].id;
      item.text = selectedItems[i].text;
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
  const batchActionClick = (selectedRows) => {
    console.log("batchActionClick" + selectedRows);
  };
  const handleAdd = (e) => {
    console.log("handleAdd" + e);
  };
  const handleDelete = (e) => {
    console.log("handleDelete" + e);
  };
  const handleEdit = (e) => {
    console.log("handleEdit" + e);
  };
  const handleOnChange = (e) => {
    e.preventDefault();
    if (e.target.value && e.target.value.length > 0) {
      setPageNumber(1);
      setTotal(0);
      const fetchUrl =
        glossaryAuthorContext.currentNodeType.url +
        "?offset=0&pageSize=1000&searchCriteria=" +
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
          if (res.relatedHTTPCode == 200 && res.result) {
            const nodesArray = res.result;
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
                setCurrentPage(nodeRows.slice(0, pageSize));
                setTotal(nodeRows.length);
              } else {
                // no results
                setResults([]);
              }
            } else if (res.relatedHTTPCode) {
              if (res.exceptionUserAction) {
                msg = "Search Failed: " + res.exceptionUserAction;
              } else {
                msg =
                  "search Failed unexpected Egeria response: " +
                  JSON.stringify(res);
              }
            } else if (res.errno) {
              if (res.errno == "ECONNREFUSED") {
                msg = "Connection refused to the view server.";
              } else {
                // TODO create nice messages for all the http codes we think are relevant
                msg = "Search Failed with http errno " + res.errno;
              }
            } else {
              msg = "Search Failed - unexpected response" + JSON.stringify(res);
            }
            setErrorMsg(msg);
          }
        })
        .catch((res) => {
          setErrorMsg("Search Failed" + JSON.stringify(res));
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
                    onChange={onAdditionalAttributesChanged}
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
        rows={currentPage}
        headers={headerData}
        render={({
          rows,
          headers,
          getHeaderProps,
          getSelectionProps,
          getRowProps,
          getBatchActionProps,
          onInputChange,
          selectedRows,
        }) => (
          <TableContainer
            title={glossaryAuthorContext.currentNodeType.typeName}
          >
            <TableToolbar>
              {/* make sure to apply getBatchActionProps so that the bar renders */}
              <TableBatchActions {...getBatchActionProps()}>
                {/* inside of you batch actions, you can include selectedRows */}
                <TableBatchAction
                  primaryFocus
                  onClick={handleDelete(selectedRows)}
                >
                  Delete
                </TableBatchAction>
                <TableBatchAction
                 onClick={handleEdit(selectedRows)}
                 >

                  Edit
                </TableBatchAction>
              </TableBatchActions>
              <TableToolbarSearch onChange={onInputChange} />
              <TableToolbarContent>
                <Button onClick={handleAdd} small kind="primary">
                  Add new
                </Button>
              </TableToolbarContent>
            </TableToolbar>
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
      <Pagination {...paginationProps()} />
    </div>
  );
};

export default NodeSearch;
