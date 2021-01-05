/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React, { useContext, useEffect, useState } from "react";
import HeaderContainer from "carbon-components-react/lib/components/UIShell/HeaderContainer";
import { Content, HeaderPanel, Switcher, SwitcherItem } from "carbon-components-react/lib/components/UIShell";
import { User20 } from "@carbon/icons-react/lib";
import { Link, Route } from "react-router-dom";
import axios from 'axios';
import Egeriawhite110 from "./images/odpi/Egeria_logo_white_110";
import Login from "./auth"
import Home from "./components/Home";
import GlossaryAuthor from "./components/GlossaryAuthor/GlossaryAuthor";
import RepositoryExplorer from "./components/RepositoryExplorer/RepositoryExplorer";
import TypeExplorer from "./components/TypeExplorer/TypeExplorer";
import Dino from "./components/Dino/Dino";
import ServerAuthor from "./components/ServerAuthor/ServerAuthor";
import { IdentificationContext } from "./contexts/IdentificationContext";
import ServerAuthorContext from "./contexts/ServerAuthorContext";

import {
  Header,
  HeaderMenuButton,
  HeaderName,
  HeaderGlobalBar,
  HeaderGlobalAction,
  SkipToContent,
  SideNav,
  SideNavItems,
  SideNavLink,
  SideNavMenu,
} from "carbon-components-react/lib/components/UIShell";

export default function Frame() {
  const {
    getBrowserURL,
    getUser,
    setAuthenticated,
    userId,
  } = useContext(IdentificationContext);
  const rootUrl = getBrowserURL("");
  const homeUrl = getBrowserURL("home");
  const glossaryAuthorUrl = getBrowserURL("glossary-author");
  const rexUrl = getBrowserURL("repository-explorer");
  const typeUrl = getBrowserURL("type-explorer");
  const serverUrl = getBrowserURL("server-author");
  const dinoUrl = getBrowserURL("dino");

  const [isLoading, setLoading] = useState(true);

  const [userOpen, setUserOpen] = useState(false);

  useEffect(() => {
    setLoading(true);
    console.log("fetching user");
    async function fetchUser() {
      await getUser();
      setLoading(false);
    }
    if (!userId) {
      fetchUser();
    } else {
      setLoading(false);
    }
  }, [getUser, userId]);

  console.log({isLoading});

  if (isLoading) return (<div className="frame"></div>);

  if (!userId) {
    const currentURL = window.location.href.slice(window.location.href.lastIndexOf('/') + 1);
    return (<Login currentURL={currentURL} />)
  }

  console.log(getBrowserURL('logout'));

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
                <HeaderGlobalAction 
                  aria-label="User" 
                  isActive={userOpen}
                  onClick={async () => {
                    setUserOpen(!userOpen);
                  }}
                >
                  <User20 />
                </HeaderGlobalAction>
              </HeaderGlobalBar>
              <HeaderPanel
                aria-label="Header Panel"
                expanded={userOpen}
              >
                <Switcher>
                  <SwitcherItem
                    style={{ textAlign: 'left' }}
                    onClick={async () => {
                      try {
                        console.log('logout!')
                        await axios.get(getBrowserURL('logout'));
                        sessionStorage.removeItem("egeria-userId");
                        setAuthenticated(false);
                        window.location.href = getBrowserURL('login');
                      } catch(err) {
                        console.error(err);
                      }
                    }}
                  >
                    Logout
                  </SwitcherItem>
                </Switcher>
              </HeaderPanel>
              <SideNav
                aria-label="Side navigation"
                expanded={isSideNavExpanded}
              >
                <SideNavItems>
                  <SideNavLink element={Link} to={homeUrl} isActive>
                    Home
                  </SideNavLink>
                  <SideNavMenu title="Solutions" defaultExpanded="true">
                    <SideNavLink
                      // uncomment (and import) if we want to show the icon
                      // renderIcon={EgeriaGlossAuth32}
                      element={Link}
                      to={glossaryAuthorUrl}
                    >
                      Glossary Author
                    </SideNavLink>
                  </SideNavMenu>
                  <SideNavMenu title="Ecosystem Tools" defaultExpanded="true">
                    <SideNavLink element={Link} to={rexUrl}>
                      Repository Explorer
                    </SideNavLink>
                    <SideNavLink element={Link} to={typeUrl}>
                      Type Explorer
                    </SideNavLink>
                    <SideNavLink element={Link} to={serverUrl}>
                      Server Author
                    </SideNavLink>
                    <SideNavLink element={Link} to={dinoUrl}>
                      Dino
                    </SideNavLink>
                  </SideNavMenu>
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
                  <Route path={glossaryAuthorUrl}>
                    <GlossaryAuthor />
                  </Route>
                  <Route path={rexUrl}>
                    <RepositoryExplorer />
                  </Route>
                  <Route path={typeUrl}>
                    <TypeExplorer />
                  </Route>
                  <Route path={serverUrl}>
                    <ServerAuthorContext>
                      <ServerAuthor />
                    </ServerAuthorContext>
                  </Route>
                  <Route path={dinoUrl}>
                    <Dino />
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