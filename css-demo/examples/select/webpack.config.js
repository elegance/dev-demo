const path = require('path');
const webpack = require('webpack');

module.exports = {
    entry: {
        main: './script/app.js'
    },

    output: {
        path: path.join(__dirname, 'dist'),
        filename: '[name].js',
        chunkFilename: '[name].chunk.js',
        publicPath: '/'
    },

    module: {
        rules: [{
            test: /\.scss$/,
            use: [{
                loader: "style-loader" // creates style nodes from JS strings
            }, {
                loader: "css-loader" // translates CSS into CommonJS
            }, {
                loader: "sass-loader" // compiles Sass to CSS
            }]
        }]
        // rules: [
        //     {test: /\.js$/, exclude: /node_modules/, use: 'babel-loader'},
        //     {test: /\.css$/, use: 'style-loader!css-loader!postcss-loader?autoprefixer'},
        //     {test: /\.scss$/, use: 'style-loader!css-loader!sass-loader'},
        //     {test: /\.(gif|jpg|png|woff|svg|eot|ttf)\??.*$/, use: 'url-loader?limit=8192'},
        //     {test: /\.(html|tpl)$/, use: 'html-loader'}
        // ]
    },

    resolve: {
        extensions: ['.js']
    },

    plugins: []
};
