import React from 'react';
import {Link} from 'react-router';
import NavLink from './NavLink.jsx'

export default React.createClass({
    render() {
        return (
            <div>
                <h1>React Router</h1>
                <ul role="nav">
                    <li><NavLink to="/about">About 2</NavLink></li>
                    <li><NavLink to="/repos">Repos 2</NavLink></li>
                </ul>
                ---{this.props.children}---
            </div>
        );
    }
});
