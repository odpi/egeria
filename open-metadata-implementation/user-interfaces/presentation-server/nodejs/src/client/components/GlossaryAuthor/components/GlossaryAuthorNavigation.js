/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React, { useState, useEffect } from "react";

import Add16 from "../../../images/Egeria_add_16";
import Delete16 from "../../../images/Egeria_delete_16";
import Edit16 from "../../../images/Egeria_edit_16";
import {
  LocalGlossaryCard,
  GlossaryCardSection,
} from "./GlossaryCard/GlossaryCard";
import GlossaryImage from "../../../images/Egeria_glossary_32";
import getNodeType from "./properties/NodeTypes.js";
import { issueRestGet, issueRestDelete } from "./RestCaller";
import useDebounce from "./useDebounce";

import { Link } from "react-router-dom";

export default function GlossaryAuthorNavigation({ match }) {
  const [glossaries, setGlossaries] = useState([]);
  const nodeType = getNodeType("glossary");
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
  const [selectedGlossaryGuid, setSelectedGlossaryGuid] = useState();

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
    issueGlossarySearch(actualDebounceCriteria);
  };

  // issue search for first page of glossaries
  const issueGlossarySearch = (criteria) => {
    // encode the URI. Be aware the more recent RFC3986 for URLs makes use of square brackets which are reserved (for IPv6)
    const url = encodeURI(
      nodeType.url + "?offset=0&pageSize=1000&searchCriteria=" + criteria
    );
    issueRestGet(url, onSuccessfulSearch, onErrorSearch);
  };

  const onClickDelete = () => {
    setErrorMsg("");
    console.log("Delete");
    if (selectedGlossaryGuid) {
      glossaries.forEach(deleteIfSelected);
    }
  };
  /**
   * Delete the supplied glossary if it's guid matches the selected one.
   * @param {*} glossary
   */
  const deleteIfSelected = (glossary) => {
    if (glossary.systemAttributes.guid == selectedGlossaryGuid) {
      const guid = selectedGlossaryGuid;
      const url = nodeType.url + "/" + guid;
      issueRestDelete(url, onSuccessfulDelete, onErrorDelete);
    }
  };

  const onSuccessfulDelete = () => {
    setSelectedGlossaryGuid(undefined);
    // reprocess the current criteria and issue the search
    processUserCriteriaAndIssueSearch();
  };

  const onErrorDelete = (msg) => {
    console.log("Error on delete " + msg);
    setErrorMsg(msg);
    // setGlossaries([]);
  };

  const onSuccessfulSearch = (json) => {
    setErrorMsg("");
    console.log("onSuccessfulSearch " + json.result);
    setGlossaries(json.result);
  };

  const onErrorSearch = (msg) => {
    console.log("Error on search " + msg);
    setErrorMsg(msg);
    setGlossaries([]);
  };

  const onClickExactMatch = () => {
    console.log("onClickExactMatch");
    const checkBox = document.getElementById("glossary_nav_exact_Match");
    setExactMatch(checkBox.checked);
  };

  const getGlossaryChildrenUrl = (guid) => {
    return match.path + "/" + guid + "/children";
  };
  function getAddGlossaryUrl() {
    return match.path + "/add-glossary";
  }
  function getEditGlossaryUrl() {
    return match.path + "/edit-glossary/" + selectedGlossaryGuid;
  }
  const onFilterCriteria = (e) => {
    setFilterCriteria(e.target.value);
  };
  const isSelected = (glossaryGuid) => {
    return glossaryGuid == selectedGlossaryGuid;
  };
  const setSelected = (glossaryGuid) => {
    setSelectedGlossaryGuid(glossaryGuid);
  };

  return (
    <div>
      <div className="bx--grid">
        <GlossaryCardSection heading="Glossaries" className="landing-page__r3">
          <article className="glossary-card__controls bx--col-sm-4 bx--col-md-1 bx--col-lg-1 bx--col-xlg-1 bx--col-max-1">
            Choose glossary
          </article>
          <article className="glossary-card__controls bx--col-sm-4 bx--col-md-2 bx--col-lg-4 bx--col-xlg-4 bx--col-max-4">
            <input
              type="text"
              id="filter-input"
              onChange={onFilterCriteria}
              placeholder="Filter"
            />
          </article>
          <article className="glossary-card__controls bx--col-sm-4 bx--col-md-1 bx--col-lg-2 bx--col-xlg-2 bx--col-max-2">
            <div className="glossary-card__exact_control">
              <label forHtml="exactMatch">Exact Match </label>
              <input
                type="checkbox"
                id="glossary_nav_exact_Match"
                onClick={onClickExactMatch}
              />
            </div>
          </article>
          <article className="glossary-card__controls bx--col-sm-4 bx--col-md-1 bx--col-lg-1 bx--col-xlg-1 bx--col-max-1">
            <div className="bx--row">
              <Link to={getAddGlossaryUrl}>
                <Add16 kind="primary" />
              </Link>
              {selectedGlossaryGuid && (
                <Link to={getEditGlossaryUrl()}>
                  <Edit16 kind="primary" />
                </Link>
              )}
              {selectedGlossaryGuid && (
                <Delete16 onClick={() => onClickDelete()} />
              )}
        
            </div>
          </article>
        </GlossaryCardSection>

        <GlossaryCardSection className="landing-page__r3">
          <article style={{ color: "red" }}>{errorMsg}</article>
        </GlossaryCardSection>

        <GlossaryCardSection className="landing-page__r3">
          {glossaries.map((glossary) => (
            <LocalGlossaryCard
              key={glossary.systemAttributes.guid}
              heading={glossary.name}
              guid={glossary.systemAttributes.guid}
              body={glossary.description}
              icon={<GlossaryImage />}
              isSelected={isSelected(glossary.systemAttributes.guid)}
              setSelected={setSelected}
              link={getGlossaryChildrenUrl(glossary.systemAttributes.guid)}
            />
          ))}
          {glossaries.length == 0 && <div>No Glossaries found!</div>}
        </GlossaryCardSection>
      </div>
    </div>
  );
}
