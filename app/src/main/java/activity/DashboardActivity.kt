package activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import fragment.DashboardFragment
import fragment.FavouriteRestaurantFragment
import fragment.ProfileFragment
import com.vanshita.foodhunger.R
import com.vanshita.foodhunger.R.string.close_drawer
import fragment.FaqsFragment

class DashboardActivity : AppCompatActivity() {
    lateinit var drawerLayout : DrawerLayout
    lateinit var toolbar : Toolbar
    lateinit var coordinatorLayout: CoordinatorLayout
    lateinit var navigationView: NavigationView
    lateinit var frameLayout: FrameLayout
    lateinit var sharedPreferences: SharedPreferences
    lateinit var txtUser: TextView
    lateinit var txtNumber: TextView

    var previousMenuItemSelected : MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        sharedPreferences = getSharedPreferences(
                getString(R.string.shared_preferences),
                Context.MODE_PRIVATE
        )

        drawerLayout = findViewById(R.id.drawerLayout)
        toolbar = findViewById(R.id.toolbar)
        coordinatorLayout = findViewById(R.id.coordinatorLayout)
        navigationView = findViewById(R.id.navigationView)
        frameLayout = findViewById(R.id.frameLayout)

        val headerView = navigationView.getHeaderView(0)
        txtUser = headerView.findViewById(R.id.txtUser)
        txtNumber = headerView.findViewById(R.id.txtNumber)

        navigationView.menu.getItem(0).isCheckable = true
        navigationView.menu.getItem(0).isChecked = true

        setToolBar()

        txtUser.text = sharedPreferences.getString("name", "UserName")
        txtNumber.text = "+91- ${sharedPreferences.getString("mobile_number", "9999999999")}"

        //Hamburger icon setup for navigation drawer
        val actionBarDrawerToggle = ActionBarDrawerToggle(this@DashboardActivity,drawerLayout, R.string.open_drawer, close_drawer)
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        navigationView.setNavigationItemSelectedListener {

            if (previousMenuItemSelected != null) {
                previousMenuItemSelected?.isChecked = false
            }


            it.isCheckable = true
            it.isChecked = true
            previousMenuItemSelected = it

            when(it.itemId){
                R.id.homee ->{
                    //dashboard fragment
                    openDashboard()
                    drawerLayout.closeDrawers()
                }
                R.id.favouriteRestaurants ->{
                    //fav restaurants fragment
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frameLayout,
                            FavouriteRestaurantFragment(this)
                        ).commit()

                    supportActionBar?.title = "Favorites"
                    drawerLayout.closeDrawers()
                }
                R.id.myProfile ->{
                   //my profile fragment
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frameLayout,
                            ProfileFragment(this)
                        ).commit()

                    supportActionBar?.title = "My Profile"
                    drawerLayout.closeDrawers()
                }
                R.id.orderHistory ->{
                    //order history activity
                    val intent = Intent(this@DashboardActivity, OrderHistoryActivity::class.java)
                    drawerLayout.closeDrawers()
                    Toast.makeText(this@DashboardActivity, "Order History", Toast.LENGTH_SHORT).show()
                    startActivity(intent)
                }
                R.id.faq ->{
                    //faq fragment
                    supportFragmentManager.beginTransaction()
                            .replace(
                                    R.id.frameLayout,
                                    FaqsFragment()
                            ).commit()

                    supportActionBar?.title = "FAQs"
                    drawerLayout.closeDrawers()
                    Toast.makeText(this@DashboardActivity, "FAQs", Toast.LENGTH_SHORT).show()
                }
                R.id.logOut ->{
                    //logout
                    drawerLayout.closeDrawers()
                    val alterDialog = androidx.appcompat.app.AlertDialog.Builder(this)
                    alterDialog.setMessage("Do you wish to log out?")
                    alterDialog.setPositiveButton("Yes") { _, _ ->
                        sharedPreferences.edit().putBoolean("isLoggedIn", false).apply()
                        ActivityCompat.finishAffinity(this)
                    }
                    alterDialog.setNegativeButton("No") { _, _ ->

                    }
                    alterDialog.create()
                    alterDialog.show()
                }
                }
            return@setNavigationItemSelectedListener true
        }
        openDashboard()

    }
    //setup custom toolbar
    fun setToolBar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "All Restaurants"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onBackPressed() {
        when (supportFragmentManager.findFragmentById(R.id.frameLayout)) {
            !is DashboardFragment -> {
                navigationView.menu.getItem(0).isChecked = true
                openDashboard()
            }
            else -> super.onBackPressed()
        }
    }
    fun openDashboard() {
        supportFragmentManager.beginTransaction().replace(
            R.id.frameLayout,
            DashboardFragment(this)
        ).commit()

        supportActionBar?.title = "All Restaurants"
        navigationView.setCheckedItem(R.id.homee)
    }
    override fun onResume() {
        window.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        )
        super.onResume()
    }
}