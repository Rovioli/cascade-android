package org.rovioli.trachka

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView.OnNavigationItemSelectedListener
import kotlinx.android.synthetic.main.activity_user.*
import kotlinx.android.synthetic.main.dialog_add_spending.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class UserActivity : AppCompatActivity() {

    private var userId = 0

    private val dialog: AlertDialog by lazy {
        val root = layoutInflater.inflate(R.layout.dialog_add_spending, null)
        val context = root.context
        root.dayOfWeekChooser.adapter = ArrayAdapter(
            context,
            android.R.layout.simple_spinner_dropdown_item,
            week
        )
        val builder = AlertDialog.Builder(this)
        builder.setMessage(R.string.add_spending)
            .setView(root)
            .setPositiveButton(R.string.add) { _, _ ->
                GlobalScope.launch(Dispatchers.Main) {
                    Connector.client.addSpending(
                        userId,
                        root.dayOfWeekChooser.selectedItemId.toInt() + 1,
                        root.comment.text.toString(),
                        root.amount_of_money.text
                            .toString()
                            .toInt()
                    )
                    Toast.makeText(
                        context,
                        "Paid ${root.amount_of_money.text.toString().toInt()} for ${root.comment.text}!",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            .setNegativeButton(android.R.string.cancel) { _, _ -> }

        builder.create()
    }

    private val onNavigationItemSelectedListener = OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                message.visibility = View.GONE
                myList.visibility = View.VISIBLE
                top.visibility = View.GONE
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_top -> {
                message.visibility = View.GONE
                myList.visibility = View.GONE
                top.visibility = View.VISIBLE
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_settings -> {
                message.visibility = View.VISIBLE
                myList.visibility = View.GONE
                top.visibility = View.GONE
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        val context = this
        userId = intent.getIntExtra("id", 0)

        GlobalScope.launch(Dispatchers.Main) {
            try {
                val body = Connector.client.getData().body() ?: throw NoSuchElementException("Have no body")
                initHome(context, userId, body)
                initTop(context, body)
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, R.string.connection_error, Toast.LENGTH_LONG).show()
            }
        }

        addButton.setOnClickListener { dialog.show() }
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
    }

    private fun initHome(context: Context, userId: Int, body: Data<Spending>) {
        val spending = body.data
            .filter { it.userid == userId }
            .sortedByDescending { it.dow }
        myList.adapter = SpendingAdapter(context, spending)
    }

    private fun initTop(context: Context, body: Data<Spending>) {
        val spending = body.data
            .sortedByDescending { it.dow }
        top.adapter = SpendingAdapter(context, spending, true)
    }
}
