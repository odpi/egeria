/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React, { useEffect, useState, useContext } from "react";
import NodeSearchView from "./views/NodeSearchView";
import NodeUpdateView from "./views/NodeUpdateView";
import NodeCreateView from "./views/NodeCreateView.js";
import LinesView from "./views/LinesView.js";
import Add16 from "../../../images/Egeria_add_16";
import Edit16 from "../../../images/Egeria_edit_16";
import Search16 from "../../../images/Egeria_search_16";
import Delete16 from "../../../images/Egeria_delete_16";
import Relationships16 from "../../../images/Egeria_relationships_16";
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

  const [currentNodeGuid, setCurrentNodeGuid] = useState();

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
    // If the exactMatch changes then we need to re-issue the search.
    [debouncedSearchCriteria, exactMatch]
  );

  const onClickAdd = () => {
    setErrorMsg("");
    glossaryAuthorContext.doCreatingAction();
  };
  const onClickRelationships = () => {
    setErrorMsg("");
    getCurrentNodeLines();
  };

  const onClickSearch = () => {
    setErrorMsg("");
    glossaryAuthorContext.doSearchingAction();
  };
  const onClickEdit = () => {
    setErrorMsg("");
    getCurrentNode();
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
      glossaryAuthorContext.doCreatedAction(node);
      setCurrentNodeGuid(undefined);
    } else {
      onErrorGetNode("Error did not get a node from the server");
    }
  };
  const onErrorCreate = (msg) => {
    console.log("Error on Get " + msg);
    setErrorMsg(msg);
    setCreatedNode(undefined);
    setCurrentNodeGuid(undefined);
  };
  const onSuccessfulGetNode = (json) => {
    if (json.result.length == 1) {
      glossaryAuthorContext.doSelectedNode(json.result[0]);
    } else {
      onErrorGetNode("Error did not get a node from the server");
    }
  };
  const onErrorGetNode = (msg) => {
    console.log("Error on Get Node" + msg);
    setErrorMsg(msg);
    glossaryAuthorContext.doRefreshSearchAction();
  };
  const onSuccessfulGetNodeLines = (json) => {
    if (json.result) {
      glossaryAuthorContext.doUpdateNodeLines(json.result);
    } else {
      onErrorGetNode("Error did not get node's lines from the server");
    }
  };
  const onErrorGetNodeLines = (msg) => {
    console.log("Error on Get Node's Lines" + msg);
    setErrorMsg(msg);
    // do we need to do this - as the main search table will not have changed
    glossaryAuthorContext.doRefreshSearchAction();
  };
  const onErrorDelete = (msg) => {
    console.log("Error on delete " + msg);
    setErrorMsg(msg);
    setCurrentNodeGuid(undefined);
    issueSearch(debouncedSearchCriteria);
  };
  const onSuccessfulDelete = () => {
    setCurrentNodeGuid(undefined);
    issueSearch(debouncedSearchCriteria);
  };
  const onSuccessfulUpdate = () => {
    glossaryAuthorContext.doResetSelectedAction();
    issueSearch(debouncedSearchCriteria);
  };
  const onErrorUpdate = (msg) => {
    glossaryAuthorContext.doResetSelectedAction();
    console.log("Error on update " + msg);
    setCurrentNodeGuid(undefined);
    setErrorMsg(msg);
    issueSearch(debouncedSearchCriteria);
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
        // If we have a selected node we need to show its row as selected.
        if (row.id == currentNodeGuid) {
          row.isSelected = true;
        } else {
          row.isSelected = false;
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
    glossaryAuthorContext.doSearchedAction();
  };
  function getCurrentNodeLines() {
    const url =
      glossaryAuthorContext.currentNodeType.url + "/" + currentNodeGuid + "/relationships";
    console.log("issueGet " + url);
    issueRestGet(url, onSuccessfulGetNodeLines, onErrorGetNodeLines);
  }
  function getCurrentNode() {
    const url =
      glossaryAuthorContext.currentNodeType.url + "/" + currentNodeGuid;
    console.log("issueGet " + url);
    issueRestGet(url, onSuccessfulGetNode, onErrorGetNode);
  }
  const onErrorSearch = (msg) => {
    console.log("Error " + msg);
    setErrorMsg(msg);
    setResults([]);
    updateSearchTableRows([]);
    setTotal(0);
    setIsSearching(false);
    glossaryAuthorContext.doSearchedAction();
  };
  // refresh the displayed search results
  // this involves taking the results from state and calculating what we need to display pased on the pagination options
  // current page is the subset of results that are displayed.
  function refreshSearchResults() {
    let selectedInResults = false;
    if (results && results.length > 0) {
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
      setCurrentNodeGuid(undefined);
      glossaryAuthorContext.doResetSelectedAction();
    }
  }
  // issue the create rest call for the supplied body
  function issueCreate(body) {
    const url = glossaryAuthorContext.currentNodeType.url;
    console.log("issueCreate " + url);
    issueRestCreate(url, body, onSuccessfulCreate, onErrorCreate);
  }
  const onSelectRow = (row) => {
    console.log("onSelectRow");
    setCurrentNodeGuid(row.id);
    glossaryAuthorContext.doResetSelectedAction();
  };
  const onNodeUpdate = (body) => {
    console.log("onNodeUpdate");
    const guid = glossaryAuthorContext.selectedNode.systemAttributes.guid;
    const url = glossaryAuthorContext.currentNodeType.url + "/" + guid;
    issueRestUpdate(url, body, onSuccessfulUpdate, onErrorUpdate);
  };
  const onUpdateClose = () => {
    setCurrentNodeGuid(undefined);
    glossaryAuthorContext.doResetSelectedAction();
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
  <div style={{ color: "red" }}>{errorMsg}</div>;

  if (glossaryAuthorContext.currentNodeType === undefined) {
    return null;
  } else {
    return (
      <div>
        {/* {(glossaryAuthorContext.isSetupComplete() || glossaryAuthorContext.isEdittingGlossary ||  glossaryAuthorContext.isEdittingProject)  && ( */}
        <div className="bx--row">
          <div className="bx--col-lg-1 bx--col-md-1">
            <Add16 kind="primary" onClick={() => onClickAdd()} />
          </div>
          <div className="bx--col-lg-1 bx--col-md-1">
            <Search16 onClick={() => onClickSearch()} />
          </div>
          {currentNodeGuid && (
            <div className="bx--col-lg-1 bx--col-md-1">
              <Delete16 onClick={() => issueDelete()} />
            </div>
          )}
          {currentNodeGuid && (
            <div className="bx--col-lg-1 bx--col-md-1">
              <Edit16 onClick={() => onClickEdit()} />
            </div>
          )}
          {currentNodeGuid && (
            <div className="bx--col-lg-1 bx--col-md-1">
              <Relationships16 onClick={() => onClickRelationships()} />
            </div>
          )}

          <div style={{ color: "red" }}>{errorMsg}</div>
        </div>
        <div className="bx--row">
          {glossaryAuthorContext.showCreatingNodeComponent() && (
            <NodeCreateView issueCreate={issueCreate} />
          )}
          {glossaryAuthorContext.showCreatedNodeComponent() && (
            <NodeCreateView createdNode={createdNode} />
          )}
          <div className="actions-container">
            {glossaryAuthorContext.showSearchComponent() && (
              <div className="actions-item">
                <NodeSearchView
                  tableKey={searchTableKey}
                  tableRows={searchTableRows}
                  pageSize={pageSize}
                  total={total}
                  pageNumber={pageNumber}
                  onPagination={onPagination}
                  onSelectRow={onSelectRow}
                  onExactMatch={onExactMatch}
                  onSearchCriteria={onSearchCriteria}
                />
              </div>
            )}
            {glossaryAuthorContext.selectedNode && (
              <div className="actions-item">
                <NodeUpdateView
                  onUpdate={onNodeUpdate}
                  onClose={onUpdateClose}
                />
              </div>
            )}
            {glossaryAuthorContext.selectedNodeLines && (
              <div className="actions-item">
                <LinesView onClose={onUpdateClose} />
              </div>
            )}
          </div>
        </div>
      </div>
    );
  }
};

export default NodeController;
