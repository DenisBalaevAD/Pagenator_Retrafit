package com.example.pagenator_retrafit

import android.annotation.SuppressLint
import android.os.Bundle;
import android.util.Log
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.pagenator_retrafit.databinding.ActivityMainBinding
import com.facebook.shimmer.ShimmerFrameLayout
import retrofit2.Call
import retrofit2.Callback

import java.util.ArrayList;


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var layoutManager: LinearLayoutManager

    var isLoading = false

    private var userModalArrayList: ArrayList<UserModal>? = null
    private var userRVAdapter: UserRVAdapter? = null
    private var userRV: RecyclerView? = null
    private var loadingPB: ProgressBar? = null
    lateinit var shimmerFrameLayout:ShimmerFrameLayout
    //private var nestedSV: NestedScrollView? = null

    var page = 0
    var limit = 2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userRV = findViewById(R.id.idRVUsers)
        loadingPB = findViewById(R.id.idPBLoading)
        shimmerFrameLayout = findViewById(R.id.shimmer_layout)

        userModalArrayList = ArrayList()

        layoutManager = LinearLayoutManager(this@MainActivity)
        userRV!!.layoutManager = layoutManager
        userRVAdapter = UserRVAdapter(userModalArrayList!!, this@MainActivity)
        userRV!!.adapter = userRVAdapter

        getDataFromAPI(page, limit)

        shimmerFrameLayout.startShimmer()

        userRV!!.addOnScrollListener(object : RecyclerView.OnScrollListener(){

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItemCount = layoutManager.childCount
                val pastVisibleItem = layoutManager.findFirstCompletelyVisibleItemPosition()
                val total = layoutManager.itemCount

                if ((visibleItemCount + pastVisibleItem) >= total && !isLoading) {
                    page++
                    isLoading = true
                    getDataFromAPI(page, limit)
                }
            }
        })

        // adding on scroll change listener method for our nested scroll view.
        /*nestedSV!!.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            // on scroll change we are checking when users scroll as bottom.
            if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
                page++
                loadingPB!!.visibility = View.VISIBLE
                getDataFromAPI(page, limit)
            }
        })*/
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getDataFromAPI(pages: Int, limit: Int) {
        shimmerFrameLayout.startShimmer()
        shimmerFrameLayout.visibility = View.VISIBLE
        loadingPB!!.visibility = View.VISIBLE
        if (pages > limit) {
            page=2
        }

        val api= Common.retrofitServices

        val call=api.getPage(page)

        call.enqueue(object : Callback<MainPage> {
            override fun onFailure(call: Call<MainPage>, t: Throwable) {
                Toast.makeText(this@MainActivity,"JQ",Toast.LENGTH_LONG).show()
                Log.d("TAG",t.message.toString())
            }

            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<MainPage>, response: retrofit2.Response<MainPage>) {
                if (response.isSuccessful) {
                    response.body()!!.data.forEach {children->
                        // on below line we are extracting data from our json object.
                        userModalArrayList!!.add(
                            UserModal(
                                children.first_name,
                                children.last_name,
                                children.email,
                                children.avatar
                            )
                        )
                    }

                    userRVAdapter!!.notifyDataSetChanged()
                    loadingPB!!.visibility = View.GONE
                    isLoading = false
                    shimmerFrameLayout.stopShimmer()
                    shimmerFrameLayout.visibility = View.GONE
                }
                else{
                    Toast.makeText(this@MainActivity,response.code().toString(),Toast.LENGTH_LONG).show()
                }
            }
        })
    }
}