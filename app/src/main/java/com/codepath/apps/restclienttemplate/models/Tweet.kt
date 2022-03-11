package com.codepath.apps.restclienttemplate.models

import com.codepath.apps.restclienttemplate.TimeFormatter
import org.json.JSONArray
import org.json.JSONObject

class Tweet {

    var body: String = ""
    var createAt: String = ""
    var user: User? = null

    companion object {
        fun fromJson(jsonObject: JSONObject) : Tweet {
            val tweet = Tweet()
            tweet.body = jsonObject.getString("text")
            tweet.createAt = getFormattedTimeStamp(jsonObject.getString("created_at"))
            tweet.user = User.fromJson(jsonObject.getJSONObject("user"))
            return tweet
        }

        private fun getFormattedTimeStamp(string: String?): String {
            return TimeFormatter.getTimeDifference(string)
        }

        //since we get a whole list of JSON objects, we need to convert a whole JSON array
        //...into a whole list of tweets as defined in TwitterClient.kt in HomeTimeline
        fun fromJsonArray(jsonArray: JSONArray): List<Tweet> {
            val tweets = ArrayList<Tweet>()
            for (i in 0 until jsonArray.length()) {
                tweets.add(fromJson(jsonArray.getJSONObject(i)))
            }
            return tweets
        }

    }

}