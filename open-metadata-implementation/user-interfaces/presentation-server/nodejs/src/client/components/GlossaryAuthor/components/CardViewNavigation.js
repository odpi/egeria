/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React, { useState, useEffect } from "react";

import Add32 from "../../../images/Egeria_add_32";
import Delete32 from "../../../images/Egeria_delete_32";
import Edit32 from "../../../images/Egeria_edit_32";
import Term32 from "../../../images/Egeria_term_32";
import {
  LocalNodeCard,
  NodeCardSection
} from "./NodeCard/NodeCard";
import GlossaryImage from "../../../images/Egeria_glossary_32";
import getNodeType from "./properties/NodeTypes.js";
import { issueRestGet, issueRestDelete } from "./RestCaller";
import useDebounce from "./useDebounce";

import { Link } from "react-router-dom";

export default function CardViewNavigation({ match, nodeTypeName }) {
  const [nodes, setNodes] = useState([]);
  const nodeType = getNodeType(nodeTypeName);
  // State and setter for search term
  const [filterCriteria, setFilterCriteria] = useState("");
  const [exactMatch, setExactMatch] = useState(false);
  // Now we call our hook, passing in the current filterCriteria value.
  // The hook will only return the latest value (what we passed in) ...
  // ... if it's been more than 500ms since it was last called.
  // Otherwise, it will return the previous value of filterCriteria.
  // The goal is to only have the API call fire when user stops typing ...
  // ... so that we aren't hitting our API rapidly.
  const debouncedFilterCriteria = useDebounce(filterCriteria, 500);
  const [errorMsg, setErrorMsg] = useState();
  const [selectedNodeGuid, setSelectedNodeGuid] = useState();

  // Here's where the API call happens
  // We use useEffect since this is an asynchronous action
  useEffect(
    () => {
      processUserCriteriaAndIssueSearch();
    },
    // This is the useEffect input array
    // Our useEffect function will only execute if this value changes ...
    // ... and thanks to our hook it will only change if the original ...
    // value (FilterCriteria) hasn't changed for more than 500ms.
    // If the exactMatch changes then we need to re-issue the search.
    [debouncedFilterCriteria, exactMatch]
  );
  const processUserCriteriaAndIssueSearch = () => {
    // sort out the actual search criteria.
    let actualDebounceCriteria = debouncedFilterCriteria;
    if (actualDebounceCriteria) {
      if (!exactMatch) {
        actualDebounceCriteria = actualDebounceCriteria + ".*";
      }
    } else {
      // by default get everything
      actualDebounceCriteria = ".*";
    }
    // Fire off our API call
    issueNodeSearch(actualDebounceCriteria);
  };

  // issue search for first page of nodes
  const issueNodeSearch = (criteria) => {
    // encode the URI. Be aware the more recent RFC3986 for URLs makes use of square brackets which are reserved (for IPv6)
    const url = encodeURI(
      nodeType.url + "?searchCriteria=" + criteria
    );
    issueRestGet(url, onSuccessfulSearch, onErrorSearch);
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
    // reprocess the current criteria and issue the search
    processUserCriteriaAndIssueSearch();
  };

  const onErrorDelete = (msg) => {
    console.log("Error on delete " + msg);
    setErrorMsg(msg);
    // setNodes([]);
  };

  const onSuccessfulSearch = (json) => {
    setErrorMsg("");
    console.log("onSuccessfulSearch " + json.result);
    setNodes(json.result);
  };

  const onErrorSearch = (msg) => {
    console.log("Error on search " + msg);
    setErrorMsg(msg);
    setNodes([]);
  };

  const onClickExactMatch = () => {
    console.log("onClickExactMatch");
    const checkBox = document.getElementById("node_nav_exact_Match");
    setExactMatch(checkBox.checked);
  };

  const getNodeChildrenUrl = (guid) => {
    return match.path + "/glossaries/" + guid + "/children";
  };
  function getAddNodeUrl() {
    return match.path + "/glossaries/add-node";
  }
  function getQuickTermsUrl() {
    return match.path + "/glossaries/" + selectedNodeGuid + "/quick-terms";
  }
  function getEditNodeUrl() {
    return match.path + "/glossaries/edit-node/" + selectedNodeGuid;
  }
  const onFilterCriteria = (e) => {
    setFilterCriteria(e.target.value);
  };
  const isSelected = (nodeGuid) => {
    return nodeGuid == selectedNodeGuid;
  };
  const setSelected = (nodeGuid) => {
    setSelectedNodeGuid(nodeGuid);
  };

  return (
    <div>
      <div className="bx--grid">
        <NodeCardSection heading="nodes" className="landing-page__r3">
          <article className="node-card__controls bx--col-sm-4 bx--col-md-1 bx--col-lg-1 bx--col-xlg-1 bx--col-max-1">
            Choose {nodeType.key}
          </article>
          <article className="node-card__controls bx--col-sm-4 bx--col-md-2 bx--col-lg-4 bx--col-xlg-4 bx--col-max-4">
            <input
              type="text"
              id="filter-input"
              onChange={onFilterCriteria}
              placeholder="Filter"
            />
          </article>
          <article className="node-card__controls bx--col-sm-4 bx--col-md-1 bx--col-lg-2 bx--col-xlg-2 bx--col-max-2">
            <div className="node-card__exact_control">
              <label forHtml="exactMatch">Exact Match </label>
              <input
                type="checkbox"
                id="node_nav_exact_Match"
                onClick={onClickExactMatch}
              />
            </div>
          </article>
          <article className="node-card__controls bx--col-sm-4 bx--col-md-1 bx--col-lg-3 bx--col-xlg-3 bx--col-max-2">
            <div className="bx--row">
              <Link to={getAddNodeUrl}>
                <Add32 kind="primary" />
              </Link>
              {selectedNodeGuid && (
                  <Link to={getQuickTermsUrl}>
                    <Term32 kind="primary" />
                  </Link>
              )}
              {selectedNodeGuid && (
                <Link to={getEditNodeUrl()}>
                  <Edit32 kind="primary" />
                </Link>
              )}
              {selectedNodeGuid && (
                <Delete32 onClick={() => onClickDelete()} />
              )}
            </div>
          </article>
        </NodeCardSection>

        <NodeCardSection className="landing-page__r3">
          <article style={{ color: "red" }}>{errorMsg}</article>
        </NodeCardSection>

        <NodeCardSection className="landing-page__r3">
          {nodes.map((node) => (
            <LocalNodeCard
              key={node.systemAttributes.guid}
              heading={node.name}
              guid={node.systemAttributes.guid}
              body={node.description}
              icon={<GlossaryImage />}
              isSelected={isSelected(node.systemAttributes.guid)}
              setSelected={setSelected}
              link={getNodeChildrenUrl(node.systemAttributes.guid)}
            />
          ))}
          {nodes.length == 0 && <div>No {nodeType.plural} found!</div>}
        </NodeCardSection>
      </div>
    </div>
  );
}
