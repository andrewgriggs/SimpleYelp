package edu.stanford.griggsad.simpleyelp

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_restaurant.view.*

class RestaurantsAdapter(val context: Context, val restaurants: List<YelpRestaurant>) :
    RecyclerView.Adapter<RestaurantsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_restaurant,
                parent,
                false
            )
        )
    }

    override fun getItemCount() = restaurants.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val restaurant = restaurants[position]
        holder.bind(restaurant)

        holder.itemView.tvName.setOnClickListener() {
            val url =
                Uri.parse(restaurant.restaurantUrl) // get your url from list item or your code.
            val intent = Intent(Intent.ACTION_VIEW, url)
            context.startActivity(intent)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(restaurant: YelpRestaurant) {
            itemView.tvName.text = restaurant.name
            itemView.tvNumReviews.text = "${restaurant.numReviews} Reviews"
            itemView.tvAddress.text = restaurant.location.address
            itemView.tvCategory.text = restaurant.categories[0].title
            itemView.tvDistance.text = restaurant.displayDistance()
            itemView.tvPrice.text = restaurant.price
            Glide.with(context).load(restaurant.imageUrl).apply(RequestOptions().transforms(
                CenterCrop(), RoundedCorners(20)
            )).into(itemView.imageView)
            when {
                restaurant.rating < 1 -> itemView.ivStars.setImageResource(R.drawable.stars_small_0)
                restaurant.rating < 1.5 -> itemView.ivStars.setImageResource(R.drawable.stars_small_1)
                restaurant.rating < 2 -> itemView.ivStars.setImageResource(R.drawable.stars_small_1_half)
                restaurant.rating < 2.5 -> itemView.ivStars.setImageResource(R.drawable.stars_small_2)
                restaurant.rating < 3 -> itemView.ivStars.setImageResource(R.drawable.stars_small_2_half)
                restaurant.rating < 3.5 -> itemView.ivStars.setImageResource(R.drawable.stars_small_3)
                restaurant.rating < 4 -> itemView.ivStars.setImageResource(R.drawable.stars_small_3_half)
                restaurant.rating < 4.5 -> itemView.ivStars.setImageResource(R.drawable.stars_small_4)
                restaurant.rating < 5 -> itemView.ivStars.setImageResource(R.drawable.stars_small_4_half)
                else -> itemView.ivStars.setImageResource(R.drawable.stars_small_5)
            }
        }
    }
}