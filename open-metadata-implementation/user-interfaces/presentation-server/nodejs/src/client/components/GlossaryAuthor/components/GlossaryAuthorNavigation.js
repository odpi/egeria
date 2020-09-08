/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React, { useState, useEffect, useContext } from "react";
import Add16 from "../../../images/Egeria_add_16";
import Delete16 from "../../../images/Egeria_delete_16";
import Edit16 from "../../../images/Egeria_edit_16";
import {
  LocalGlossaryCard,
  GlossaryCardSection,
} from "./GlossaryCard/GlossaryCard";
import GlossaryImage from "../../../images/Egeria_glossary_32";
import getNodeType from "./properties/NodeTypes.js";
import { issueRestGet } from "./RestCaller";
import useDebounce from "./useDebounce";
import GlossaryAdd from "./GlossaryAdd";
import GlossaryEdit from "./GlossaryEdit";
import GlossaryChildren from "./GlossaryChildren";
import { IdentificationContext } from "../../../contexts/IdentificationContext";

import { Route, Switch, Link, BrowserRouter } from "react-router-dom";

export default function GlossaryAuthorNavigation() {
  const [glossaries, setGlossaries] = useState([]);
  const nodeType = getNodeType("glossary");
  // State and setter for search term
  const [searchCriteria, setSearchCriteria] = useState("");
  const [exactMatch, setExactMatch] = useState(false);
  // Now we call our hook, passing in the current searchCriteria value.
  // The hook will only return the latest value (what we passed in) ...
  // ... if it's been more than 500ms since it was last called.
  // Otherwise, it will return the previous value of searchCriteria.
  // The goal is to only have the API call fire when user stops typing ...
  // ... so that we aren't hitting our API rapidly.
  const debouncedSearchCriteria = useDebounce(searchCriteria, 500);
  const [errorMsg, setErrorMsg] = useState();
  const identificationContext = useContext(IdentificationContext);

  // Here's where the API call happens
  // We use useEffect since this is an asynchronous action
  useEffect(
    () => {
      // sort out the actual search criteria.
      let actualDebounceCriteria = debouncedSearchCriteria;
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
    },
    // This is the useEffect input array
    // Our useEffect function will only execute if this value changes ...
    // ... and thanks to our hook it will only change if the original ...
    // value (searchCriteria) hasn't changed for more than 500ms.
    // If the exactMatch changes then we need to re-issue the search.
    [debouncedSearchCriteria, exactMatch]
  );

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
  };
  const onClickEdit = () => {
    setErrorMsg("");
    console.log("Edit");
  };

  const onErrorSearch = (msg) => {
    console.log("Error on Get " + msg);
    setErrorMsg(msg);
    setGlossaries([]);
  };
  const onSuccessfulSearch = (json) => {
    setErrorMsg("");
    console.log("onSuccessfulSearch " + json.result);
    setGlossaries(json.result);
  };
  const onClickExactMatch = () => {
    console.log("onClickExactMatch");
    const checkBox = document.getElementById("glossary_nav_exact_Match");
    setExactMatch(checkBox.checked);
  };

  const getGlossaryChildrenUrl = (name) => {
    return "/glossary-author/glossaries/" + name + "/children";
  };
  function getAddGlossaryUrl() {
    return "/glossary-author/add-glossary";
  }
  const onFilterCriteria = (e) => {
    setSearchCriteria(e.target.value);
  };

  return (
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
            <Delete16 onClick={() => onClickDelete()} />
            <Edit16 onClick={() => onClickEdit()} />
          </div>
        </article>
      </GlossaryCardSection>

      <GlossaryCardSection className="landing-page__r3">
        <article style={{ color: "red" }}>{errorMsg}</article>
      </GlossaryCardSection>

      <GlossaryCardSection className="landing-page__r3">
        {glossaries.map((glossary) => (
          <LocalGlossaryCard
            key={glossary.name}
            heading={glossary.name}
            body={glossary.description}
            icon={<GlossaryImage />}
            link={getGlossaryChildrenUrl(glossary.name)}
          />
        ))}
        {glossaries.length == 0 && <div>No Glossaries found!</div>}
      </GlossaryCardSection>
      <BrowserRouter>
        <Switch>
          <Route path="/glossary-add" exact component={GlossaryAdd} />
          {/* <Route path="/glossary-edit/:glossaryName">
            <GlossaryEdit />
          </Route>
          <Route path="/glossary/:glossaryName/children">
            <GlossaryChildren />
          </Route> */}
        </Switch>
      </BrowserRouter>
    </div>
  );
}
