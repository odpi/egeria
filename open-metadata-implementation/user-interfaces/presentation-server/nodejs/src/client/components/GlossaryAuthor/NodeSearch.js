/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React, { useState, useEffect, useContext } from "react";
import { GlossaryAuthorContext } from "../../contexts/GlossaryAuthorContext";
import useDebounce from "./useDebounce";
// import Delete16 from "../../images/Egeria_delete_16";
// import Edit16 from "../../images/Egeria_edit_16";
import {
  Accordion,
  AccordionItem,
  DataTable,
  MultiSelect,
  Pagination,
  TableContainer,
  Table,
  TableHead,
  TableRow,
  TableSelectRow,
  TableCell,
  TableHeader,
  TableBody,
} from "carbon-components-react";

// Responsible for issuing search requests on a node and displaying the results.
// - the search is issue with debounce
// - additional columns can be specified.
// - the search has pagination
// - the search results can be selected.
// @param {*} props
//
const NodeSearch = (props) => {
  console.log("NodeSearch " + props.refresh);
  const glossaryAuthorContext = useContext(GlossaryAuthorContext);

  const [total, setTotal] = useState(0);
  const [currentPage, setCurrentPage] = useState([]);
  const [pageNumber, setPageNumber] = useState(1);
  const [refreshed, setRefreshed] = useState(false);
  const [pageSize] = useState(10);
  const [errorMsg, setErrorMsg] = useState();
  const [tableKey, setTableKey] = useState(1);
  const [paginationOptions, setPaginationOptions] = useState();

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

  // State and setter for search term
  const [searchCriteria, setSearchCriteria] = useState("");
  // State and setter for search results
  const [results, setResults] = useState([]);

  // State for search status (whether there is a pending API request)
  const [isSearching, setIsSearching] = useState(false);
  // const [refresh, setRefresh] = useState(false);
  // Now we call our hook, passing in the current searchCriteria value.
  // The hook will only return the latest value (what we passed in) ...
  // ... if it's been more than 500ms since it was last called.
  // Otherwise, it will return the previous value of searchCriteria.
  // The goal is to only have the API call fire when user stops typing ...
  // ... so that we aren't hitting our API rapidly.
  const debouncedSearchCriteria = useDebounce(searchCriteria, 500);
  const [exactMatch, setExactMatch] = useState(false);

  // let refresh = false;
  // Here's where the API call happens
  // We use useEffect since this is an asynchronous action
  useEffect(
    () => {
      // Make sure we have a value (user has entered something in input)

      if (debouncedSearchCriteria) {
        // Set isSearching state
        setIsSearching(true);
        // Fire off our API call
        issueSearch(debouncedSearchCriteria).then((results) => {
          // Set back to false since request finished
          setIsSearching(false);
          // Set results state
          setResults(results);
        });
      } else {
        setResults([]);
      }
    },
    // This is the useEffect input array
    // Our useEffect function will only execute if this value changes ...
    // ... and thanks to our hook it will only change if the original ...
    // value (searchCriteria) hasn't changed for more than 500ms.
    [debouncedSearchCriteria]
  );

  const paginationProps = () => ({
    disabled: false,
    page: pageNumber,
    pagesUnknown: true,
    pageInputDisabled: false,
    backwardText: "Previous page",
    forwardText: "Next page",
    totalItems: total,
    pageSize: pageSize,
    pageSizes: [5, 10, 50, 100],
    itemsPerPageText: "Items per page:",
    onChange: onPaginationChange,
  });

  if (glossaryAuthorContext.authoringActionState == 5) {
    console.log("Refreshing search");
    issueSearch(debouncedSearchCriteria).then((results) => {
      // Set back to false since request finished
      setIsSearching(false);
      // Set results state
      setResults(results);
    });
  }

  // driven when pagination options have changed - page size or page number
  const onPaginationChange = (options) => {
    console.log("onPaginationChange");
    console.log(options);
    // save the pagination options in state
    setPaginationOptions(options);
    refreshSearchResults(options.page, options.pageSize);
  };
  const [headerData, setHeaderData] = useState(mainProperties);
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
      console.log("selectedAdditionalProperties.selectedItems 1");
      console.log(selectedAdditionalProperties);
      allProperties = mainProperties.concat(selectedAdditionalProperties);
    }
    console.log("allProperties 1");
    console.log(allProperties);
    setHeaderData(allProperties);
  }

  // refresh the displayed search results
  // this involves taking the results from state and calculating what we need to display pased on the pagination options
  // current page is the subset of results that are displayed.
  function refreshSearchResults(passedPage, passedPageSize) {
    let selectedInResults = false;
    if (results && results.length > 0) {
      // there seems to be an issue when paginationOptions in the pagination handler
      // then calling this function, the first time paginationOptions is undefined.
      // A circumvention is to pass the page and page size as parameters and use them if they are set.
      let pageSize;
      let page;
      if (passedPage) {
        page = passedPage;
      } else {
        page = paginationOptions.page;
      }
      if (passedPageSize) {
        pageSize = passedPageSize;
      } else {
        pageSize = paginationOptions.pageSize;
      }

      // if page = 1 and pageSize 10, currentPageStart = 1
      // if page = 2 and pageSize 10, currentPageStart = 11
      // if page = 2 and pageSize 10 and results.length = 15, currentPageStart = 11 , currentPageSize = 5
      const currentPageStart = (page - 1) * pageSize;
      let currentPageSize = pageSize;
      // if the last page is not complete ensure that we only specify up the end of the what is actually there in the results.
      if (currentPageStart + currentPageSize - 1 > results.length) {
        currentPageSize = results.length - currentPageStart;
      }
      const slicedResults = results.slice(
        currentPageStart,
        currentPageStart + currentPageSize
      );
      let resultsToshow = slicedResults.map(function (row) {
        row.id = row.systemAttributes.guid;
        if (
          glossaryAuthorContext.selectedNode &&
          glossaryAuthorContext.selectedNode.systemAttributes.guid == row.id
        ) {
          row.isSelected = true;
          selectedInResults =true;
        }
      });
      console.log("resultsToshow");
      console.log(resultsToshow);
      setCurrentPage(resultsToshow);
    } else {
      setCurrentPage([]);
    }
    // we have selectedNode but it is not in the search results - we must have deleted it. 
    if (!selectedInResults) {
      glossaryAuthorContext.updateSelectedNode(undefined);
    }

  }
  // Additonal attributes can be selected so more columns can be shown
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
  // issue the get rest call for particular guid
  function issueGet(guid) {
    const url = glossaryAuthorContext.currentNodeType.url + "/" + guid;
    console.log("issueGet " + url);
    let msg = "";
    return fetch(url, {
      method: "get",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
      },
    })
      .then((res) => res.json())
      .then((res) => {
        console.log("get completed " + JSON.stringify(res));
        if (res.relatedHTTPCode == 200 && res.result) {
          console.log("Get successful for guid " + guid);
          console.log("res.result) " + res.result);
          if (res.result.length == 1) {
            glossaryAuthorContext.updateSelectedNode(res.result[0]);
            console.log("glossaryAuthorContext.selectedNode");
            console.log(glossaryAuthorContext.selectedNode);
          }
        } else {
          // if this is a formatted Egeria response, we have a user action
          if (res.relatedHTTPCode) {
            if (res.exceptionUserAction) {
              msg = "Get Failed: " + res.exceptionUserAction;
            } else {
              msg =
                "Get Failed unexpected Egeria response: " + JSON.stringify(res);
            }
          } else if (res.errno) {
            if (res.errno == "ECONNREFUSED") {
              msg = "Connection refused to the view server.";
            } else {
              // TODO create nice messages for all the http codes we think are relevant
              msg = "Get Failed with http errno " + res.errno;
            }
          } else {
            msg = "Get Failed - unexpected response" + JSON.stringify(res);
          }
          setErrorMsg(errorMsg + ",\n" + msg);
          document.getElementById("nodeCreateButton").classList.add("shaker");
        }
      })
      .catch((res) => {
        const msg = "Get Failed - logic error " + JSON.stringify(res);
        setErrorMsg(errorMsg + ",\n" + msg);
      });
  }
  const onClickExactMatch = () => {
    console.log("onClickExactMatch");
    const checkBox = document.getElementById("exactMatch");
    setExactMatch(checkBox.checked);
  };
  const onSelectRow = (row) => {
    console.log("onSelectRow");
    issueGet(row.id);
  };
  // issue search using a criteria
  function issueSearch(criteria) {
    setPageNumber(1);
    setTotal(0);
    let actualCriteria = criteria;
    if (!exactMatch) {
      actualCriteria = criteria + ".*";
    }

    // encode the URI. Be aware the more recent RFC3986 for URLs makes use of square brackets which are reserved (for IPv6)
    const fetchUrl = encodeURI(
      glossaryAuthorContext.currentNodeType.url +
        "?offset=0&pageSize=1000&searchCriteria=" +
        actualCriteria
    );
    return fetch(fetchUrl, {
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
          let msg = "";
          // if there is a node response then we have successfully updated a node
          if (nodesArray) {
            if (nodesArray.length > 0) {
              let nodeRows = nodesArray.map(function (node) {
                let row = {};
                for (const property in node) {
                  console.log("result property is ", property);
                  if (property == "glossary") {
                    const glossary = node[property];
                    row.glossaryName = glossary.name;
                    row.glossaryGuid = glossary.guid;
                  } else if (property == "systemAttributes") {
                    row.guid = node[property].guid;
                    row.id = node[property].guid;
                  } else {
                    row[property] = node[property];
                  }
                }
                return row;
              });
              //setResults(nodeRows);
              setCurrentPage(nodeRows.slice(0, pageSize));
              setTotal(nodeRows.length);
              setTableKey(tableKey+1);
              return nodeRows;
            } else {
              // no results
              // setResults([]);
              setCurrentPage([]);
              setTotal(0);
              setTableKey(tableKey+1);
              return [];
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
          // no results
          setResults([]);
          setCurrentPage([]);
          setTotal(0);
          setTableKey(tableKey+1);
        }
      })
      .catch((res) => {
        setErrorMsg("Search Failed" + JSON.stringify(res));
        setTableKey(tableKey+1);
      });
  }
  return (
    <div className="top-search-container">
      <div className="top-search-item">
        <div className="search-container">
          <div data-search role="search" className="bx--search bx--search--l">
            <div className="search-item">
              <label
                id="search-input-label-1"
                className="bx--label"
                for="search__input-1"
              >
                Search
              </label>
              <input
                className="bx--search-input"
                type="text"
                id="search__input-1"
                onChange={(e) => setSearchCriteria(e.target.value)}
                placeholder="Search"
              />
              <svg
                focusable="false"
                preserveAspectRatio="xMidYMid meet"
                xmlns="http://www.w3.org/2000/svg"
                className="bx--search-magnifier"
                width="16"
                height="16"
                viewBox="0 0 16 16"
                aria-hidden="true"
              >
                <path d="M15,14.3L10.7,10c1.9-2.3,1.6-5.8-0.7-7.7S4.2,0.7,2.3,3S0.7,8.8,3,10.7c2,1.7,5,1.7,7,0l4.3,4.3L15,14.3z M2,6.5  C2,4,4,2,6.5,2S11,4,11,6.5S9,11,6.5,11S2,9,2,6.5z"></path>
              </svg>
              <button
                className="bx--search-close bx--search-close--hidden"
                title="Clear search
            input"
                aria-label="Clear search input"
              >
                <svg
                  focusable="false"
                  preserveAspectRatio="xMidYMid meet"
                  xmlns="http://www.w3.org/2000/svg"
                  className="bx--search-clear"
                  width="20"
                  height="20"
                  viewBox="0 0 32 32"
                  aria-hidden="true"
                >
                  <path d="M24 9.4L22.6 8 16 14.6 9.4 8 8 9.4 14.6 16 8 22.6 9.4 24 16 17.4 22.6 24 24 22.6 17.4 16 24 9.4z"></path>
                </svg>
              </button>
            </div>
          </div>
          {glossaryAuthorContext.currentNodeType &&
            glossaryAuthorContext.currentNodeType.attributes.length > 3 && (
              <div className="search-item">
                <Accordion>
                  <AccordionItem title="Search options">
                    <label forHtml="exactMatch">Exact Match </label>
                    <input type="checkbox" id="exactMatch" onClick={onClickExactMatch}/>
                    <div className="bx--form-item">
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
              </div>
            )}
          {isSearching && <div className="search-item">Searching ...</div>}
          <div className="search-item">
            <DataTable
              radio
              key={tableKey}
              isSortable
              rows={currentPage}
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
          <div className="search-item">
            <Pagination {...paginationProps()} />
          </div>
        </div>
      </div>
    </div>
  );
};

export default NodeSearch;
