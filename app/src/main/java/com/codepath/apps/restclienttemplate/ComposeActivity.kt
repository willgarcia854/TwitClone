package com.codepath.apps.restclienttemplate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.codepath.apps.restclienttemplate.models.Tweet
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers


class ComposeActivity : AppCompatActivity() {

    lateinit var etCompose: EditText
    lateinit var btnTweet: Button
    lateinit var client: TwitterClient
    lateinit var charCount: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compose)

        etCompose = findViewById(R.id.etTweetCompose)
        btnTweet = findViewById(R.id.btnTweet)
        charCount = findViewById(R.id.charCount)
        client = TwitterApplication.getRestClient(this)

        // handling the text added to the EditText view (etCompose)
        etCompose.addTextChangedListener(object : TextWatcher{
            val tweetCont = etCompose.text

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // fires right as the text is being changed (even supplies the range of text
                if (!tweetCont.isEmpty() && tweetCont.length < 240){btnTweet.isEnabled==true}

            }
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Fires right before text is changing
            }

            override fun afterTextChanged(s: Editable) {
                charCount.setText(tweetCont.length.toString())

            }
        })

        // handling the user's click on the tweet button
        btnTweet.setOnClickListener {

            // grab the content of etCompose to know what the user wants to tweet
            val tweetContent = etCompose.text.toString()

            // 1. Make sure the tweet isn't empty
            if (tweetContent.isEmpty()){
                Toast.makeText(this, "Empty Tweets are not allowed!", Toast.LENGTH_SHORT).show()
                // look into displaying snack bar message
            } else
                // 2. Make sure the tweet is under the character count
                if (tweetContent.length > 280) {
                    Toast.makeText(this, "Tweet is too long! Limit is 280 characters", Toast.LENGTH_SHORT).show()
                } else {
                    client.publishTweet(tweetContent, object: JsonHttpResponseHandler() {
                        override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON) {
                            Log.i(TAG, "Successfully published tweet!")
                            val tweet = Tweet.fromJson(json.jsonObject)
                            val intent = Intent()
                            // note, tweet is a non-parsable object that we created and must make parsable
                            intent.putExtra("tweet", tweet)
                            setResult(RESULT_OK, intent)
                            finish()
                        }

                        override fun onFailure(
                            statusCode: Int,
                            headers: Headers?,
                            response: String?,
                            throwable: Throwable?
                        ) {
                            Log.e(TAG, "Failed to publish a tweet", throwable)
                        }

                    })
                }
        }
    }
    companion object {
        val TAG = "ComposeActivity"
    }
}

    private fun TextView.setTextColor() {
        TODO("Not yet implemented")
    }
