var webpack = require('webpack');
var path = require('path');
var fs = require('fs');

module.exports = {
    //entry: './index.jsx',
    entry: path.resolve(__dirname, 'server.js'),
    
    output: {
        filename: 'server.bundle.js'
    },
    
    target: 'node',
    
    externals: fs.readdirSync(path.resolve(__dirname, 'node_modules')).concat([
        'react-dom/server', 'react/addons'
    ]).reduce(function(ext, mod) {
        ext[mod] = 'commonjs' + mod;
        return ext;
    }, {}),
    
    node: {
        __filename: true,
        __dirname: true
    },
    
    module: {
        loaders: [
            {test: /\.js[x]?$/, exclude: /node_modules/, loader: 'babel-loader?presets[]=es2015&presets[]=react'},
            {test: /\.css$/, loader: 'style-loader!css-loader?modules'}
        ]
    }
};