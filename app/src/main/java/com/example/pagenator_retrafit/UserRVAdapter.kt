package com.example.pagenator_retrafit

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable
import com.squareup.picasso.Picasso


class UserRVAdapter(
    private val userModalArrayList: ArrayList<UserModal>, private val context: Context
) :
    RecyclerView.Adapter<UserRVAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.user_rv_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        // getting data from our array list in our modal class.
        val userModal = userModalArrayList[position]

        val shimmer:Shimmer = Shimmer.ColorHighlightBuilder()
            .setBaseColor(Color.parseColor("#E3E3E3"))
            .setBaseAlpha(1F)
            .setHighlightColor(Color.parseColor("#E7E7E7"))
            .setHighlightAlpha(1F)
            .setDropoff(50F)
            .build()

        val shimmerDrawable = ShimmerDrawable()
        shimmerDrawable.setShimmer(shimmer)

        // on below line we are setting data to our text view.
        holder.firstNameTV.text = userModal.first_name
        holder.lastNameTV.text = userModal.last_name
        holder.emailTV.text = userModal.email

        // on below line we are loading our image
        // from url in our image view using picasso.
        Picasso.get().load(userModal.avatar).placeholder(shimmerDrawable).into(holder.userIV)
    }

    override fun getItemCount(): Int {
        // returning the size of array list.
        return userModalArrayList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val firstNameTV: TextView
        val lastNameTV: TextView
        val emailTV: TextView
        val userIV: ImageView

        init {

            // initializing our variables.
            firstNameTV = itemView.findViewById(R.id.idTVFirstName)
            lastNameTV = itemView.findViewById(R.id.idTVLastName)
            emailTV = itemView.findViewById(R.id.idTVEmail)
            userIV = itemView.findViewById(R.id.idIVUser)
        }
    }
}
