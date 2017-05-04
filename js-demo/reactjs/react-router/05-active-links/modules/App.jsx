import React from 'react';
import {Link} from 'react-router';
import NavLink from './NavLink.jsx'

export default React.createClass({
    render() {
        return (
            <div>
                <h1>React Router</h1>
                <ul role="nav">
                    <li><Link to="/about" activeStyle={{ color: 'red'}}>About</Link></li>
                    <li><Link to="/repos" activeStyle={{ color: 'red'}}>Repos</Link></li>
                    
                    <li><NavLink to="/about">About 2</NavLink></li>
                    <li><NavLink to="/repos">Repos 2</NavLink></li>
                </ul>
                ---{this.props.children}---
            </div>
        );
    }
});
