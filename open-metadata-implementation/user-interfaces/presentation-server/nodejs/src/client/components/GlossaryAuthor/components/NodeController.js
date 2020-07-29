/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React, { useEffect, useState, useContext} from "react";
import NodeSearchView from "./views/NodeSearchView";
import NodeUpdateView from "./views/NodeUpdateView";
import NodeCreateView from "./views/NodeCreateView.js";
import Add16 from "../../../images/Egeria_add_16";
import Search16 from "../../../images/Egeria_search_16";
import Delete16 from "../../../images/Egeria_delete_16";
import { GlossaryAuthorContext } from "../contexts/GlossaryAuthorContext";
import useDebounce from "./useDebounce";
import {
  issueRestCreate,
  issueRestUpdate,
  issueRestGet,
  issueRestDelete,
} from "./RestCaller";

/**
 * This component is responsible for controlling when rest calls need to be issued and what needs to be displayed.
 * It delegates to the RestCaller to issue the rest calls, which callback here for success (onSuccessful...) and errors (onError...).
 * It delegates to view components to display new content, which call back here when there is user input.
 * It is primary driven by context state.
 * This component issues action calls to context, which then changes the context state.
 * This component has conditional logic based on the context state to decide what needs be rendered.
 * @param {*} props
 */
const NodeController = (props) => {
  const glossaryAuthorContext = useContext(GlossaryAuthorContext);
  // State and setter for search term
  const [searchCriteria, setSearchCriteria] = useState("");
  // State and setter for search results
  const [results, setResults] = useState([]);
  // const [paginationOptions, setPaginationOptions] = useState();
  const [createdNode, setCreatedNode] = useState();

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
  const [searchTableRows, setSearchTableRows] = useState([]);
  const [searchTableKey, setSearchTableKey] = useState(1);
  const [total, setTotal] = useState(0);
  const [pageNumber, setPageNumber] = useState(1);
  const [pageSize, setPageSize] = useState(10);
  const [errorMsg, setErrorMsg] = useState();

  console.log("NodeController", props.showCreate + ",propskey = " + props.key);

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
        issueSearch(debouncedSearchCriteria);
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

  const onClickAdd = () => {
    setErrorMsg("");
    glossaryAuthorContext.doCreatingAction();
  };
  const onClickSearch = () => {
    setErrorMsg("");
    glossaryAuthorContext.doSearchingAction();
  };
  const onExactMatch = (flag) => {
    setExactMatch(flag);
  };
  const onSearchCriteria = (criteria) => {
    console.log("onSearchCriteria " + criteria);
    setErrorMsg("");
    setSearchCriteria(criteria);
  };
  const updateSearchTableRows = (rows) => {
    setSearchTableRows(rows);
    setSearchTableKey(searchTableKey + 1);
  };
  // driven when pagination options have changed - page size or page number
  const onPagination = (options) => {
    console.log("onPaginationChange");
    console.log(options);
    // save the pagination options in state
    //setPaginationOptions(options);
    setPageSize(options.pageSize);
    setPageNumber(options.page);
    refreshSearchResults();
  };
  const onSuccessfulCreate = (json) => {
    console.log("onSuccessfulCreate");
    if (json.result.length == 1) {
      const node = json.result[0];
      setCreatedNode(node);
      if (glossaryAuthorContext.isEdittingMyGlossary()) {
        glossaryAuthorContext.doCreatedMyGlossary(node);
      } else if (glossaryAuthorContext.isEdittingMyProject()) {
        glossaryAuthorContext.doCreatedMyProject(node);
      } else {
        // future thought consider passing the created node to save in context.
        glossaryAuthorContext.doCreatedAction();
      }
    } else {
      onErrorGet("Error did not get a node from the server");
    }
  };
  const onErrorCreate = (msg) => {
    console.log("Error on Get " + msg);
    setErrorMsg(msg);
    setCreatedNode(undefined);
  };
  const onSuccessfulGet = (json) => {
    if (json.result.length == 1) {
      glossaryAuthorContext.doUpdateSelectedNode(json.result[0]);
    } else {
      onErrorGet("Error did not get a node from the server");
    }
  };
  const onErrorGet = (msg) => {
    console.log("Error on Get " + msg);
    setErrorMsg(msg);
    // TODO should we reset the selected node here instead?
    //glossaryAuthorContext.updateSelectedNode(undefined);
    glossaryAuthorContext.doRefreshSearchAction();
  };
  const onErrorDelete = (msg) => {
    console.log("Error on delete " + msg);
    setErrorMsg(msg);
    // glossaryAuthorContext.updateSelectedNode(undefined);
    glossaryAuthorContext.doRefreshSearchAction();
  };
  const onSuccessfulDelete = () => {
    //glossaryAuthorContext.updateSelectedNode(undefined);
    // refresh the search results and unset selected node if the current node is not there.
    glossaryAuthorContext.doRefreshSearchAction();
  };

  const onSuccessfulSearch = (json) => {
    const nodesArray = json.result;
    // Set back to false since request finished
    setIsSearching(false);
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
      updateSearchTableRows(nodeRows.slice(0, pageSize));
      setTotal(nodeRows.length);
      // Set back to false since request finished
      // Set results state
      setResults(json);
      // return nodeRows;
    } else {
      updateSearchTableRows([]);
      setTotal(0);
      // Set results state
      setResults([]);
    }
  };
  const onErrorSearch = (msg) => {
    console.log("Error " + msg);
    setErrorMsg(msg);
    setResults([]);
    updateSearchTableRows([]);
    setTotal(0);
    setIsSearching(false);
  };
  // refresh the displayed search results
  // this involves taking the results from state and calculating what we need to display pased on the pagination options
  // current page is the subset of results that are displayed.
  function refreshSearchResults() {
    let selectedInResults = false;
    if (results && results.length > 0) {
      // there seems to be an issue when paginationOptions in the pagination handler
      // then calling this function, the first time paginationOptions is undefined.
      // A circumvention is to pass the page and page size as parameters and use them if they are set.
      // let pageSize;
      // let page;
      // if (passedPage) {
      //   page = passedPage;
      // } else {
      //   page = paginationOptions.page;
      // }
      // if (passedPageSize) {
      //   pageSize = passedPageSize;
      // } else {
      //   pageSize = paginationOptions.pageSize;
      // }

      // if page = 1 and pageSize 10, searchTableRowsStart = 1
      // if page = 2 and pageSize 10, searchTableRowsStart = 11
      // if page = 2 and pageSize 10 and results.length = 15, searchTableRowsStart = 11 , searchTableRowsSize = 5
      const searchTableRowsStart = (pageNumber - 1) * pageSize;
      let searchTableRowsSize = pageSize;
      // if the last page is not complete ensure that we only specify up the end of the what is actually there in the results.
      if (searchTableRowsStart + searchTableRowsSize - 1 > results.length) {
        searchTableRowsSize = results.length - searchTableRowsStart;
      }
      const slicedResults = results.slice(
        searchTableRowsStart,
        searchTableRowsStart + searchTableRowsSize
      );
      let resultsToshow = slicedResults.map(function (row) {
        row.id = row.systemAttributes.guid;
        if (
          glossaryAuthorContext.selectedNode &&
          glossaryAuthorContext.selectedNode.systemAttributes.guid == row.id
        ) {
          row.isSelected = true;
          selectedInResults = true;
        }
      });
      console.log("resultsToshow");
      console.log(resultsToshow);
      updateSearchTableRows(resultsToshow);
    } else {
      updateSearchTableRows([]);
    }
    // we have selectedNode but it is not in the search results - we must have deleted it.
    if (!selectedInResults) {
      glossaryAuthorContext.doUpdateSelectedNode(undefined);
    }
  }
  // issue the get rest call for particular guid
  function issueGet(guid) {
    const url = glossaryAuthorContext.currentNodeType.url + "/" + guid;
    console.log("issueGet " + url);
    issueRestGet(url, onSuccessfulGet, onErrorGet);
  }
  // issue the create rest call for the supplied body
  function issueCreate(body) {
    const url = glossaryAuthorContext.currentNodeType.url;
    console.log("issueCreate " + url);
    issueRestCreate(url, body, onSuccessfulCreate, onErrorCreate);
  }
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
    const url = encodeURI(
      glossaryAuthorContext.currentNodeType.url +
        "?offset=0&pageSize=1000&searchCriteria=" +
        actualCriteria
    );
    issueRestGet(url, onSuccessfulSearch, onErrorSearch);
  }
  function issueDelete() {
    // setIssuedDelete(true);
    const guid = glossaryAuthorContext.selectedNode.systemAttributes.guid;
    const url = glossaryAuthorContext.currentNodeType.url + "/" + guid;
    issueRestDelete(url, onSuccessfulDelete, onErrorDelete);
  }

  // if (glossaryAuthorContext.isRefreshSearchOperation()) {
  //   console.log("Refreshing search");
  //   issueSearch(debouncedSearchCriteria);
  // }
  <div style={{ color: "red" }}>{errorMsg}</div>;

  if (glossaryAuthorContext.currentNodeType === undefined) {
    return null;
  } else {
    return (
      <div>
        {glossaryAuthorContext.isSetupComplete() && (
          <div className="bx--row">
            <div className="bx--col-lg-1 bx--col-md-1">
              <Add16 kind="primary" onClick={() => onClickAdd()} />
            </div>
            <div className="bx--col-lg-1 bx--col-md-1">
              <Search16 onClick={() => onClickSearch()} />
            </div>
            {glossaryAuthorContext.selectedNode && (
              <div className="bx--col-lg-1 bx--col-md-1">
                <Delete16 onClick={() => issueDelete()} />
              </div>
            )}
            <div style={{ color: "red" }}>{errorMsg}</div>
          </div>
        )}
        <div className="bx--row">
          {glossaryAuthorContext.isCreatingOperation() && (
            <NodeCreateView issueCreate={issueCreate} />
          )}
          {glossaryAuthorContext.isCreatedOperation() && (
            <NodeCreateView createdNode={createdNode} />
          )}
          {(glossaryAuthorContext.isSearchingOperation() ||
            glossaryAuthorContext.isSearchedOperation()) && (
            <div className="actions-container">
              <div className="actions-item">
                <NodeSearchView
                  tableKey={searchTableKey}
                  tableRows={searchTableRows}
                  pageSize={pageSize}
                  total={total}
                  pageNumber={pageNumber}
                  onPagination={onPagination}
                  onSelect={onSelectRow}
                  onExactMatch={onExactMatch}
                  onSearchCriteria={onSearchCriteria}
                />
              </div>
              {glossaryAuthorContext.selectedNode && (
                <div className="actions-item">
                  <NodeUpdateView />
                </div>
              )}
            </div>
          )}
          {(glossaryAuthorContext.isRefreshSearchOperation() ||
            glossaryAuthorContext.isDeletingOperation()) && (
            <div className="actions-container">
              <div className="actions-item">
                <NodeSearchView
                  tableKey={searchTableKey}
                  searchTableRows={searchTableRows}
                  pageSize={pageSize}
                  total={total}
                  pageNumber={pageNumber}
                  onPagination={onPagination}
                  onSelect={onSelectRow}
                  onExactMatch={onExactMatch}
                  onSearchCriteria={onSearchCriteria}
                />
              </div>
              {glossaryAuthorContext.selectedNode && (
                <div className="actions-item">
                  <NodeUpdateView
                    key={
                      glossaryAuthorContext.selectedNode.systemAttributes.guid
                    }
                  />
                </div>
              )}
            </div>
          )}
          {glossaryAuthorContext.isUndefinedOperation() && <NodeCreateView />}
        </div>
      </div>
    );
  }
};

export default NodeController;
