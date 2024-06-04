package com.example.futuretatarstan

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class OnlineChat: AppCompatActivity() {
    private val client = OkHttpClient()
    private val apiKey = ""
    private val url = "http://api.proxyapi.ru/openai/v1/chat/completions"

    lateinit var txtResponse: TextView
    private lateinit var idTVQuestion: TextView
    private lateinit var etQuestion: TextInputEditText

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_online_chat)


        val home: Button = findViewById(R.id.button_glavn)
        home.setOnClickListener {
            val intent = Intent(this, MainScreen::class.java)
            startActivity(intent)
            finish()
        }
        val lk: Button = findViewById(R.id.button_lich)
        lk.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
            finish()
        }




        etQuestion = findViewById(R.id.etQuestion)
        idTVQuestion = findViewById(R.id.idTVQuestion)
        txtResponse = findViewById(R.id.txtResponse)

        etQuestion.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                txtResponse.text = "Зинһар, көтегез..."
                val question = etQuestion.text.toString().trim()
                if (question.isNotEmpty()) {
                    getResponse(question) { response ->
                        runOnUiThread {
                            txtResponse.text = response
                        }
                    }
                } else {
                    Toast.makeText(this, "Сорауыгызны кертегез", Toast.LENGTH_SHORT).show()
                }
                return@OnEditorActionListener true
            }
            false
        })
    }

    private fun getResponse(question: String, callback: (String) -> Unit) {
        idTVQuestion.text = question
        etQuestion.setText("")

        val requestBody = """
            {
                "model": "gpt-4o",
                "messages": [
                    {"role": "system", "content": "In this conversation, it is important for you to follow the following instructions: 1. answer only in Tatar; 2.use the Tatar alphabet if possible; 3. use English only in programming languages, do not use it in other places; 4. avoid the Latin alphabet if possible; 5. translate into Tatar other languages."},
                    {"role": "user", "content": "$question"}
                ],
                "max_tokens": 500
            }
        """.trimIndent()

        val request = Request.Builder()
            .url(url)
            .addHeader("Content-Type", "application/json")
            .addHeader("Authorization", "Bearer $apiKey")
            .post(requestBody.toRequestBody("application/json".toMediaTypeOrNull()))
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("OnlineCHhat", "API call failed", e)
                runOnUiThread {
                    txtResponse.text = "API call failed: ${e.message}"
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                if (body != null) {
                    Log.v("OnlineCHhat", body)
                    try {
                        val jsonObject = JSONObject(body)
                        val choicesArray: JSONArray = jsonObject.getJSONArray("choices")
                        val messageObject = choicesArray.getJSONObject(0).getJSONObject("message")
                        val textResult = messageObject.getString("content")
                        callback(textResult)
                    } catch (e: Exception) {
                        Log.e("OnlineCHhat", "JSON parsing error", e)
                        callback("Error parsing response")
                    }
                } else {
                    Log.v("OnlineCHhat", "Empty response")
                    callback("Empty response from API")
                }
            }
        })
    }
}
