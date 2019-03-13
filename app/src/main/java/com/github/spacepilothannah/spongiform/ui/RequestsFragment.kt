package com.github.spacepilothannah.spongiform.ui

import android.content.Context

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.github.spacepilothannah.spongiform.R
import com.github.spacepilothannah.spongiform.data.DataManager
import com.github.spacepilothannah.spongiform.data.api.dto.Request

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [RequestSelectedHandler] interface.
 */
class RequestsFragment : androidx.fragment.app.Fragment() {
    var requestsAdapter: RequestsRecyclerViewAdapter? = null
        private set
    var requestSelectedAllowedDeniedHandler : RequestSelectedAllowedDeniedHandler? = null
        private set
    var dataManager : DataManager? = null
    private var filterPending : Boolean? = true
    private var filterAllowed : Boolean? = null

    private var swipeRefreshView : SwipeRefreshLayout? = null

    interface RequestSelectedAllowedDeniedHandler {
        fun requestAllowed(request: Request)
        fun requestDenied(request: Request)
        fun requestSelected(request: Request)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        Log.d("SPONGI","creating view $savedInstanceState")
        val view = inflater.inflate(R.layout.fragment_request_list, container, false)
        // Set the adapter
        val recycler = view.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.requests_list)

        Log.d("SPONGI","setting adapter")
        with(recycler) {
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
            // get
            adapter = RequestsRecyclerViewAdapter(requestSelectedAllowedDeniedHandler!!)
        }
        requestsAdapter = recycler.adapter as RequestsRecyclerViewAdapter

        swipeRefreshView = view.findViewById<SwipeRefreshLayout>(R.id.swipe_refresh)
        swipeRefreshView?.setOnRefreshListener {
            refreshRequests()
        }
        swipeRefreshView?.isRefreshing = true
        return view
    }

    private var requestActionsMenu : Menu? = null

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.requests_actions, menu)
        requestActionsMenu = menu
    }
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        super.onOptionsItemSelected(item)
        when(item?.itemId) {
            R.id.action_refresh -> {
                refreshRequests()
            }
            R.id.filter_allowed -> {
                requestActionsMenu?.findItem(R.id.filter_denied)?.setChecked(false)
                requestActionsMenu?.findItem(R.id.filter_pending)?.setChecked(false)
                item.setChecked(!item.isChecked)
                setFilter(allowed = true, pending = false)
            }
            R.id.filter_denied -> {
                requestActionsMenu?.findItem(R.id.filter_pending)?.setChecked(false)
                requestActionsMenu?.findItem(R.id.filter_allowed)?.setChecked(false)
                item.setChecked(!item.isChecked)
                item.groupId
                setFilter(allowed = false, pending = false)
            }
            R.id.filter_pending -> {
                requestActionsMenu?.findItem(R.id.filter_denied)?.setChecked(false)
                requestActionsMenu?.findItem(R.id.filter_allowed)?.setChecked(false)
                item.setChecked(!item.isChecked)
                setFilter(pending = item.isChecked, allowed = null)
            }
            else -> {

            }
        }
        return false
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is RequestSelectedAllowedDeniedHandler) {
            Log.d("SPONGI", "attaching requestSelectedAllowedDeniedHandler")
            requestSelectedAllowedDeniedHandler = context
        }
    }

    override fun onResume() {
        super.onResume()
        refreshRequests()
    }

    override fun onDetach() {
        super.onDetach()
    }

    fun setFilter(allowed : Boolean?, pending : Boolean) {
        var type = "Pending"
        if(pending == false) {
            filterPending = null
            filterAllowed = allowed

        } else {
            filterAllowed = null
            filterPending = true
        }

        refreshRequests()
    }

    fun refreshRequests() {
        dataManager?.getRequests(pending = filterPending, allowed = filterAllowed) { requests ->
            Log.d("SPONGI", "requests received $requests")
            updateTitle()
            swipeRefreshView?.isRefreshing = false
            requestsAdapter?.requests = requests
        }
    }

    private fun updateTitle() {
        var type = "Pending"
        if(filterPending == null) {
            if (filterAllowed == null) {
                type = "All"
            } else if (filterAllowed == true) {
                type = "Allowed"
            } else {
                type = "Denied"
            }
        }
        val ctx = this.context
        if(ctx is AppCompatActivity) {
            ctx.supportActionBar?.title = "$type Requests"
        }
    }

    companion object {
        const val ARG_DATA_MANAGER = "dataManager"

        @JvmStatic
        fun newInstance(dm : DataManager) : RequestsFragment  =
                RequestsFragment().apply {
                    arguments = Bundle().apply {
                    }
                    dataManager = dm
                }
    }
}
