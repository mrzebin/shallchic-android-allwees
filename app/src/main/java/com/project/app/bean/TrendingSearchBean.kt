package com.project.app.bean;

class TrendingSearchBean {
    var searchItems:List<SearchItem> = listOf()

    class SearchItem(word: String, highlight: Boolean) {
        var word:String = word;
        var highlight:Boolean = highlight
    }
}