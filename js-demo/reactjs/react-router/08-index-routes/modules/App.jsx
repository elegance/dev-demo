import React from 'react';
import NavLink from './NavLink.jsx'
import NavLinkStyle from './NavLink.css';
import {IndexLink} from 'react-router';
import Home from './Home.jsx';


export default React.createClass({
    render() {
        return (
            <div>
                <h1>React Router</h1>
                <ul role="nav">
                    <li><NavLink to="/">Home 1(Always Active, parent routes are active when child routes are active!)</NavLink></li>
                    <li><IndexLink to="/" activeClassName={NavLinkStyle.active}> Home 2 use IndexLink </IndexLink></li>
                    <li><NavLink to="/" onlyActiveOnIndex={true}>Home 3 use Link props onlyActiveOnIndex</NavLink></li>
                    <li><NavLink to="/about">About 2</NavLink></li>
                    <li><NavLink to="/repos">Repos 2</NavLink></li>
                </ul>
                ---{this.props.children || <Home />}---
            </div>
        );
    }
});
