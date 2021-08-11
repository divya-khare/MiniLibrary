package fragment

import activity.Login
import activity.UserProfile
import adaptor.DashboardRecyclerAdapter
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.*
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.divyakhare.minilibrary.R
import database.SharedPreferenceManager
import model.Book
import org.json.JSONException
import util.ConnectionManager
import java.util.*
import kotlin.Comparator
import kotlin.collections.HashMap

class DashboardFragment : Fragment() {

    lateinit var recyclerDashboard: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapter: DashboardRecyclerAdapter
    lateinit var progressLayout: RelativeLayout
    lateinit var progressBar: ProgressBar
    val bookInfoList = arrayListOf<Book>()

    private var ratingComparator = Comparator<Book>{ book1, book2 ->

        if (book1.bookRating.compareTo(book2.bookRating, true) == 0) {
            // sort according to name if rating is same
            book1.bookName.compareTo(book2.bookName, true)
        } else {
            book1.bookRating.compareTo(book2.bookRating, true)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)
        recyclerDashboard = view.findViewById(R.id.recyclerDashboard)
        progressLayout = view.findViewById(R.id.progressLayout)
        progressBar = view.findViewById(R.id.progressBar)
        progressLayout.visibility = View.VISIBLE
        layoutManager = LinearLayoutManager(activity)
        //adding meny to fragment
        setHasOptionsMenu(true)

        val queue = Volley.newRequestQueue(activity as Context)
        val url = "http://13.235.250.119/v1/book/fetch_books/"
        if(ConnectionManager().checkConnectivity(activity as Context)){
            val jsonObjectRequest = object: JsonObjectRequest(Request.Method.GET,url,null, Response.Listener {
                val success = it.getBoolean("success")
                try {
                    progressLayout.visibility = View.GONE
                    if (success) {
                        val data = it.getJSONArray("data")
                        for (i in 0 until data.length()) {
                            val bookJsonObject = data.getJSONObject(i)
                            val bookObject = Book(
                                bookJsonObject.getString("book_id"),
                                bookJsonObject.getString("name"),
                                bookJsonObject.getString("author"),
                                bookJsonObject.getString("rating"),
                                bookJsonObject.getString("price"),
                                bookJsonObject.getString("image")
                            )
                            bookInfoList.add(bookObject)
                            recyclerAdapter =
                                DashboardRecyclerAdapter(activity as Context, bookInfoList)
                            recyclerDashboard.adapter = recyclerAdapter
                            recyclerDashboard.layoutManager = layoutManager
                        }
                    } else {
                        Toast.makeText(activity as Context, "Some error occured", Toast.LENGTH_LONG)
                            .show()
                    }
                }catch (e: JSONException){
                    Toast.makeText(activity as Context, "Unexpected error occured", Toast.LENGTH_LONG)
                        .show()
                }
            }, Response.ErrorListener {
                if(activity!=null){
                    Toast.makeText(activity as Context, "Volley error occured", Toast.LENGTH_LONG)
                        .show()}
            }){
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Content-type"] = "application/json"
                    headers["token"] = "cbc4edba39520b"
                    return headers
                }
            }
            queue.add(jsonObjectRequest)
        }else{
            val dialog = AlertDialog.Builder(activity as Context)
            dialog.setTitle("Error")
            dialog.setMessage("Internet connection not found")
            dialog.setPositiveButton("Open Settings"){text,listener ->
                val settingIntent = Intent(Settings.ACTION_WIFI_SETTINGS)
                startActivity(settingIntent)
                activity?.finish()
            }
            dialog.setNegativeButton("cancel"){text,listener ->
                ActivityCompat.finishAffinity(activity as Activity)
            }
            dialog.create()
            dialog.show()
        }

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater?.inflate(R.menu.menu_dashboard,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item?.itemId
        when(id){
            R.id.my_profile ->{
                openUserProfile()
            }
            R.id.log_out ->{
                confirmLogout()
            }
            R.id.action_sort ->{
                Collections.sort(bookInfoList, ratingComparator)
                bookInfoList.reverse()
            }
        }

        recyclerAdapter.notifyDataSetChanged()
        return super.onOptionsItemSelected(item)
    }

    private fun confirmLogout() {
        val dialog = AlertDialog.Builder(activity as Context)
        dialog.setTitle("Confirm Logout")
        dialog.setMessage("Do you really want to logout?")
        dialog.setPositiveButton("Yes"){text,listener ->
            val sharedPreference = SharedPreferenceManager(activity as Context,SharedPreferenceManager.Preference_Users)
            sharedPreference.logoutUserFromPreference()
            Toast.makeText(activity as Context, "Thank you for your time here", Toast.LENGTH_LONG).show()
            activity?.finish()
        }
        dialog.setNegativeButton("Cancel"){text,listener ->

        }
        dialog.create()
        dialog.show()
    }

    private fun openUserProfile() {
        val intent = Intent(activity as Context,UserProfile::class.java)
        startActivity(intent)
    }
}

