package com.github.spacepilothannah.spongiform.ui

import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.github.spacepilothannah.spongiform.data.api.dto.Request
import com.github.spacepilothannah.spongiform.R
import java.text.SimpleDateFormat

/**
 * [RecyclerView.Adapter] that can display a list of [Request]s and makes a call to the
 * specified [RequestSelectedHandler].
 */
class RequestsRecyclerViewAdapter(
        listener : RequestsFragment.RequestSelectedAllowedDeniedHandler
)
    : RecyclerView.Adapter<RequestsRecyclerViewAdapter.ViewHolder>() {

    var requests : List<Request> = ArrayList<Request>()
        set (newList) {
            field = newList
            notifyDataSetChanged()
        }

    private val mSelectedListener: View.OnClickListener
    private val mDeniedListener : View.OnClickListener
    private val mAllowedListener : View.OnClickListener

    init {
        mSelectedListener = View.OnClickListener { v ->
            val request = v.tag as Request
            Log.d("SPONGI","OnClickListener $request")
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            listener.requestSelected(request)
        }
        mAllowedListener = View.OnClickListener { v ->
            listener.requestAllowed(v.tag as Request)
        }
        mDeniedListener = View.OnClickListener { v ->
            listener.requestDenied(v.tag as Request)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.requests_list_request, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val request = requests[position]
        Log.d("SPONGI", "binding ViewHolder for position ${position} - ${requests[position].url}")

        holder.urlView?.text = request.url
        val view = holder.mView
        view.tag = request
        view.setOnClickListener(mSelectedListener)

        val denyButton = view.findViewById<View>(R.id.deny_button)!!
        denyButton.visibility = if(request.isPending()) View.VISIBLE else View.GONE
        denyButton.tag = request
        denyButton.setOnClickListener(mDeniedListener)

        val allowButton = view.findViewById<View>(R.id.allow_button)!!
        allowButton.visibility = if(request.isPending()) View.VISIBLE else View.GONE
        allowButton.tag = request
        allowButton.setOnClickListener(mAllowedListener)

        val stateImage = view.findViewById<ImageView>(R.id.state_icon)
        stateImage.visibility = if(request.isPending()) View.GONE else View.VISIBLE
        stateImage.setImageResource(if(request.isAllowed()) R.drawable.ic_check_black_24dp else R.drawable.ic_block_black_24dp)

        val eventText = view.findViewById<TextView>(R.id.event_text)
        val fmt = SimpleDateFormat("HH:mm 'on' dd MMM yyyy")
        if(request.isAllowed()) {
            eventText.text = "Allowed at " + fmt.format(request.allowed_at)
        } else if(request.isDenied()) {
            eventText.text = "Denied at " + fmt.format(request.denied_at)
        } else {
            eventText.text = "Requested at " + fmt.format(request.requested_at)
        }

    }

    override fun getItemCount(): Int = requests.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val urlView: TextView by lazy { mView.findViewById<TextView>(R.id.url) }

        override fun toString(): String {
            return super.toString() + " '" + urlView.text + "'"
        }
    }
}
