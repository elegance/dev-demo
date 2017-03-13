var express = require('express');
var path = require('path');

import React from 'react';
import {renderToString} from 'react-dom/server';
import {match, RouterContext} from 'react-router';
import routes from './modules/routes.jsx';

var app = express();

//app.use(express.static(__dirname, 'public'));

app.get('*', (req, res) => {
    console.log('gogo....');
    
    match({routes, location: req.url}, (err, redirect, props) => {
        if (err) {
            res.status(500).send(err.message);
        } else if (redirect) {
            res.redirect(redirect.pathname + redirect.search);
        } else if (props) {
            const appHtml = renderToString(<RouterContext {...props} />);
            res.send(renderPage(appHtml));
        } else {
            res.status(404).send('Not Found!');
        }
    });
});

// app.get('*', function (req, res) {
//     console.log(res);
//     const appHtml = renderToString(<RouterContext {...props}/>);

//     res.send(renderPage(appHtml));
// });

function renderPage(appHtml) {
    return `
        <!doctype html public="storage">
        <html>
        <meta charset=utf-8/>
        <title>My First React Router App</title>
        <link rel=stylesheet href=/index.css>
        <div id=app>${appHtml}</div>
        <script src="/bundle.js"></script>
    `;
}

var PORT = process.env.PORT || 8080;

app.listen(PORT, function () {
    console.log('Production Express server running at localhost:' + PORT);
});
