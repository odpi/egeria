/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React, { useState, useEffect } from "react";

import Add32 from "../../../images/Egeria_add_32";
import Delete32 from "../../../images/Egeria_delete_32";
import Edit32 from "../../../images/Egeria_edit_32";
import Term32 from "../../../images/Egeria_term_32";
import ParentChild32 from "../../../images/Egeria_parent_child_32";
import { LocalNodeCard, NodeCardSection } from "./NodeCard/NodeCard";
import { withRouter } from "react-router-dom";
import { Pagination, Toggle } from "carbon-components-react";
import NodeTableView from "./views/NodeTableView";

//import GlossaryImage from "../../../images/Egeria_glossary_32";
import getNodeType from "./properties/NodeTypes.js";
import { issueRestGet, issueRestDelete } from "./RestCaller";

import { Link } from "react-router-dom";

const GlossaryAuthorCategoriesNavigation = (props) => {
  const [nodes, setNodes] = useState([]);
  const [errorMsg, setErrorMsg] = useState();
  const [selectedNodeGuid, setSelectedNodeGuid] = useState();
  const [onlyTop, setOnlyTop] = useState(false);
  const [completeResults, setCompleteResults] = useState([]);
  const [isCardView, setIsCardView] = useState(true);
  const [total, setTotal] = useState(0);
  const [pageNumber, setPageNumber] = useState(1);
  const [pageSize, setPageSize] = useState(5);

  console.log("GlossaryAuthorCategoriesNavigation " + props);

  const nodeType = getNodeType("category");
  useEffect(() => {
    getChildren();
  }, [selectedNodeGuid, onlyTop]);

  const getChildren = () => {
    // encode the URI. Be aware the more recent RFC3986 for URLs makes use of square brackets which are reserved (for IPv6)
    let url = props.getCategoriesURL +"onlyTop="+onlyTop; 
     url = encodeURI(props.getCategoriesURL);
    issueRestGet(url, onSuccessfulGetChildren, onErrorGetChildren);
  };

  const onToggleTop = () => {
    console.log("onToggleTop");
    if (onlyTop) {
      setOnlyTop(false);
    } else {
      setOnlyTop(true);
    }
  };
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
    onChange: onPagination,
  });
  // driven when pagination options have changed - page size or page number
  const onPagination = (options) => {
    console.log("onPaginationChange");
    console.log(options);
    // save the pagination options in state
    //setPaginationOptions(options);
    setPageSize(options.pageSize);
    setPageNumber(options.page);
    refreshNodes(completeResults, options.pageSize, options.page);
  };

  // Refresh the displayed nodes search results
  // this involves taking the results and pagination options and calculating nodes that is the subset needs to be displayed
  // The results, page size and page number are passed as arguments, rather than picked up from state, as the state updates
  // are done asynchronously in a render cycle.

  function refreshNodes(results, passedPageSize, passedPageNumber) {
    let selectedInResults = false;
    setTotal(results.length);
    if (results && results.length > 0) {
      // if page = 1 and pageSize 10, searchTableRowsStart = 1
      // if page = 2 and pageSize 10, searchTableRowsStart = 11
      // if page = 2 and pageSize 10 and results.length = 15, searchTableRowsStart = 11 , searchTableRowsSize = 5
      const searchTableRowsStart = (passedPageNumber - 1) * passedPageSize;
      let searchTableRowsSize = passedPageSize;
      // if the last page is not complete ensure that we only specify up the end of the what is actually there in the results.
      if (searchTableRowsStart + searchTableRowsSize - 1 > results.length) {
        searchTableRowsSize = results.length - searchTableRowsStart;
      }
      const slicedResults = results.slice(
        searchTableRowsStart,
        searchTableRowsStart + searchTableRowsSize
      );
      slicedResults.map(function (row) {
        row.id = row.systemAttributes.guid;
        if (selectedNodeGuid && selectedNodeGuid == row.id) {
          row.isSelected = true;
          selectedInResults = true;
        }
      });
      setNodes(slicedResults);
    } else {
      setNodes([]);
    }
    // we have selectedNode but it is not in the search results - we must have deleted it.
    if (!selectedInResults) {
      setSelectedNodeGuid(undefined);
    }
  }
  const onToggleCard = () => {
    console.log("onToggleCard");
    if (isCardView) {
      setIsCardView(false);
    } else {
      setIsCardView(true);
    }
  };

  const onClickDelete = () => {
    setErrorMsg("");
    console.log("Delete");
    if (selectedNodeGuid) {
      nodes.forEach(deleteIfSelected);
    }
  };
  /**
   * Delete the supplied glossary if it's guid matches the selected one.
   * @param {*} glossary
   */
  const deleteIfSelected = (glossary) => {
    if (glossary.systemAttributes.guid == selectedNodeGuid) {
      const guid = selectedNodeGuid;
      const url = nodeType.url + "/" + guid;
      issueRestDelete(url, onSuccessfulDelete, onErrorDelete);
    }
  };

  const onSuccessfulDelete = () => {
    setSelectedNodeGuid(undefined);
    // get the children again
    getChildren();
  };

  const onErrorDelete = (msg) => {
    console.log("Error on delete " + msg);
    setErrorMsg(msg);
  };

  const onSuccessfulGetChildren = (json) => {
    setErrorMsg("");
    console.log("onSuccessfulGetChildren " + json.result);
    refreshNodes(json.result, pageSize, pageNumber);
    setCompleteResults(json.result);
  };

  const onErrorGetChildren = (msg) => {
    console.log("Error on get children " + msg);
    setErrorMsg(msg);
    setNodes([]);
  };

  function getAddCategoryUrl() {
    console.log("getAddCategoryUrl " + props);
    return props.match.url + "/categories/add-category";
  }
  function getEditNodeUrl() {
    return props.match.url + "/categories/edit-category/" + selectedNodeGuid;
  }
  const isSelected = (nodeGuid) => {
    return nodeGuid == selectedNodeGuid;
  };
  const setSelected = (nodeGuid) => {
    setSelectedNodeGuid(nodeGuid);
  };
  return (
    <div>
      <div className="bx--grid">
        <NodeCardSection>
          <article className="node-card__controls bx--col-sm-4 bx--col-md-1 bx--col-lg-3 bx--col-xlg-3 bx--col-max-2">
            <div className="bx--row">
              <Link to={getAddCategoryUrl}>
                <Add32 kind="primary" />
              </Link>
              {selectedNodeGuid && (
                <Link to={props.getCategoriesURL}>
                  <ParentChild32 kind="primary" />
                </Link>
              )}
              {selectedNodeGuid && (
                <Link to={getEditNodeUrl()}>
                  <Edit32 kind="primary" />
                </Link>
              )}
              {selectedNodeGuid && <Delete32 onClick={() => onClickDelete()} />}
            </div>
          </article>
        </NodeCardSection>
        <Toggle
          aria-label="topCategoryToggle"
          defaultToggled
          labelA="All Categories"
          labelB="Top Categories"
          id="category-filter-toggle"
          onToggle={onToggleTop}
        />
        <NodeCardSection className="landing-page__r3">
          <Toggle
            aria-label="nodeCardTableToggle"
            defaultToggled
            labelA="Table View"
            labelB="Card View"
            id="node-cardtable-toggle"
            onToggle={onToggleCard}
          />
        </NodeCardSection>
        <NodeCardSection className="landing-page__r3">
          <article style={{ color: "red" }}>{errorMsg}</article>
        </NodeCardSection>
        {isCardView && (
          <NodeCardSection className="landing-page__r3">
            {nodes.map((node) => (
              <LocalNodeCard
                key={node.systemAttributes.guid}
                heading={node.name}
                guid={node.systemAttributes.guid}
                body={node.description}
                icon={<Term32 />}
                isSelected={isSelected(node.systemAttributes.guid)}
                setSelected={setSelected}
              />
            ))}
          </NodeCardSection>
        )}

        {!isCardView && (
          <NodeTableView
            nodeType={nodeType}
            nodes={nodes}
            setSelected={setSelected}
          />
        )}
        {nodes.length == 0 && <div>No {nodeType.plural} found!</div>}
        {nodes.length > 0 && (
          <div className="search-item">
            <Pagination {...paginationProps()} />
          </div>
        )}
      </div>
    </div>
  );
};
export default withRouter(GlossaryAuthorCategoriesNavigation);
