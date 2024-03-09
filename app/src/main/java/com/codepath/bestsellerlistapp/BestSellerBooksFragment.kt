package com.codepath.bestsellerlistapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.ContentLoadingProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.RequestParams
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.internal.http2.Header
import org.json.JSONObject

private const val API_KEY = "evS3xF66NQKJu32xuiDcLQzQyc3aQvrF"

class BestSellerBooksFragment : Fragment(), OnListFragmentInteractionListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_best_seller_books_list, container, false)
        val progressBar = view.findViewById<ContentLoadingProgressBar>(R.id.progress)
        val recyclerView = view.findViewById<RecyclerView>(R.id.list)
        val context = view.context
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        updateAdapter(progressBar, recyclerView)
        return view
    }

    private fun updateAdapter(progressBar: ContentLoadingProgressBar, recyclerView: RecyclerView) {
        progressBar.show()

        val client = AsyncHttpClient()
        val params = RequestParams()
        params.put("api-key", API_KEY)

        client.get("https://api.nytimes.com/svc/books/v3/lists/current/hardcover-fiction.json", params,
            object : JsonHttpResponseHandler() {
                override fun onSuccess(statusCode: Int, headers: Array<out Header>?, json: JSONObject?) {
                    progressBar.hide()

                    //TODO - Parse JSON into Models

                    val gson = Gson()
                    val arrayBookType = object : TypeToken<List<BestSellerBook>>() {}.type
                    val models : List<BestSellerBook> = gson.fromJson(booksRawJSON, arrayBookType)
                    recyclerView.adapter = BestSellerBooksRecyclerViewAdapter(models, this@BestSellerBooksFragment)

                    recyclerView.adapter = BestSellerBooksRecyclerViewAdapter(models, this@BestSellerBooksFragment)

                    Log.d("BestSellerBooksFragment", "response successful")
                }

                override fun onFailure(statusCode: Int, headers: Array<out Header>?, errorResponse: String, t: Throwable?) {
                    progressBar.hide()

                    t?.message?.let {
                        Log.e("BestSellerBooksFragment", errorResponse)
                    }
                }
            })
    }

    override fun onItemClick(item: BestSellerBook) {
        Toast.makeText(context, "test: " + item.title, Toast.LENGTH_LONG).show()
    }
}
