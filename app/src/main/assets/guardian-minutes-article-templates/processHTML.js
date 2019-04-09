const jsdom = require("jsdom");
const fs = require("fs");
const ncp = require('ncp').ncp;

const { JSDOM } = jsdom;
ncp.limit = 16;

// ncp('dev/', 'build/', function (err) {
//     if (err) {
//       return console.error(err);
//     }
// });


JSDOM.fromFile("build/articleTemplate.html", {}).then(dom => {
    const $ = require('jquery')(dom.window);
    $("link").each(function () {
        var href = $(this).attr('href');
        $(this).prop("href", "__TEMPLATES_DIRECTORY__" + href.slice(1))
    });

    $("script").each(function () {
        var src = $(this).attr('src');
        $(this).prop("src", "__TEMPLATES_DIRECTORY__" + src.slice(1))
    });

    fs.writeFile('build/articleTemplate.html', dom.serialize(), function (error){
        if (error) throw error;
    });
});