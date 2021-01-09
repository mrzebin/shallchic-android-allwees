package com.project.app.bean

/**
 * 设置一个单例
 */
class RecentSearchBean {
    var historyItems:List<SearchHistoryItem> = listOf()
    /**
     * data 代表这是一个数据类
     */
    data class SearchHistoryItem(val keyWord:String)
}