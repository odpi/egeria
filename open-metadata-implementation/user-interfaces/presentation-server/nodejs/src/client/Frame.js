/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React, { useContext } from "react";
import Search20 from "@carbon/icons-react/lib/search/20";
import Notification20 from "@carbon/icons-react/lib/notification/20";
import AppSwitcher20 from "@carbon/icons-react/lib/app-switcher/20";
import HeaderContainer from "carbon-components-react/lib/components/UIShell/HeaderContainer";
import { Content } from "carbon-components-react/lib/components/UIShell";
import { Link, Route, Switch } from "react-router-dom";
import Egeriawhite110 from "./images/Egeria_logo_white_110";
import EgeriaGlossAuth32 from "./images/Egeria_glossary_author_32";
import Home from "./components/Home";
import GlossaryAuthor from "./components/GlossaryAuthor/GlossaryAuthor";
import TypeExplorer from "./components/TypeExplorer/TypeExplorer";
import { IdentificationContext } from "./contexts/IdentificationContext";

import {
  Header,
  HeaderMenuButton,
  HeaderName,
  HeaderGlobalBar,
  HeaderGlobalAction,
  SkipToContent,
  SideNav,
  SideNavItems,
  SideNavLink
} from "carbon-components-react/lib/components/UIShell";

export default function Frame(props) {
  const identificationContext = useContext(IdentificationContext);
  console.log("Frame context", identificationContext);
  const rootUrl = identificationContext.getBrowserURL("");
  const homeUrl = identificationContext.getBrowserURL("home");
  const glossaryUrl = identificationContext.getBrowserURL("glossary-author");
  const typeUrl = identificationContext.getBrowserURL("type-explorer");

  return (
    <div className="container">
      <HeaderContainer
        render={({ isSideNavExpanded, onClickSideNavExpand }) => (
          <>
            <Header aria-label="Egeria governance solutions">
              <SkipToContent />
              <HeaderMenuButton
                aria-label="Open menu"
                onClick={onClickSideNavExpand}
                isActive={isSideNavExpanded}
              />
              <HeaderName href="#" prefix="">
                <Egeriawhite110 />
              </HeaderName>

              <HeaderGlobalBar>
                <HeaderGlobalAction aria-label="Search" onClick={() => {}}>
                  <Search20 />
                </HeaderGlobalAction>
                <HeaderGlobalAction
                  aria-label="Notifications"
                  onClick={() => {}}
                >
                  <Notification20 />
                </HeaderGlobalAction>
                <HeaderGlobalAction
                  aria-label="App Switcher"
                  onClick={() => {}}
                >
                  <AppSwitcher20 />
                </HeaderGlobalAction>
              </HeaderGlobalBar>
              <SideNav
                aria-label="Side navigation"
                expanded={isSideNavExpanded}
              >
                <SideNavItems>
                  <SideNavLink element={Link} to={homeUrl} isActive>
                    Home
                  </SideNavLink>
                  <SideNavLink
                    renderIcon={EgeriaGlossAuth32}
                    element={Link}
                    to={glossaryUrl}
                  >
                    Glossary Author
                  </SideNavLink>
                  <SideNavLink element={Link} to={typeUrl}>
                    Type Explorer
                  </SideNavLink>
                </SideNavItems>
              </SideNav>
            </Header>

            <Content id="main-content">
              <div className="bx--row">
                <section className="bx--offset-lg-3 bx--col-lg-13">
                  <Route path={rootUrl} exact>
                    <Home />
                  </Route>
                  <Route path={homeUrl}>
                    <Home />
                  </Route>
                  <Route path={glossaryUrl}>
                    <GlossaryAuthor />
                  </Route>
                  <Route path={typeUrl}>
                    <TypeExplorer />
                  </Route>
                </section>
              </div>
            </Content>
          </>
        )}
      />
    </div>
  );
}
