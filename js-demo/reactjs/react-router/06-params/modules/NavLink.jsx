import React from 'react';
import {Link} from 'react-router';

import NavLinkStyle from './NavLink.css';

export default React.createClass({
    render() {
        return (<Link {...this.props} activeClassName={NavLinkStyle.active}></Link>);
    }
});