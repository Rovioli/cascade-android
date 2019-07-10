package org.rovioli.trachka.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.stats_fragment.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.rovioli.trachka.Connector
import org.rovioli.trachka.R
import org.rovioli.trachka.Spending
import org.rovioli.trachka.states.TabThriftState
import org.rovioli.trachka.states.TabWasteState
import org.rovioli.trachka.states.ViewState

class StatisticsFragment : Fragment() {

    private var userId = 0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        userId = activity?.intent?.getIntExtra("id", 0) ?: 0
        val view = inflater.inflate(R.layout.stats_fragment, container, false)
        GlobalScope.launch(Dispatchers.Main) {
            val spending: List<Spending> = Connector.client.getData().body() ?: arrayListOf()
            val states: Array<ViewState> = arrayOf(
                TabWasteState(spending),
                TabThriftState(spending)
            )
            states[0].select(view)
            view.recordTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabReselected(tab: TabLayout.Tab?) {}

                override fun onTabUnselected(tab: TabLayout.Tab?) {}

                override fun onTabSelected(tab: TabLayout.Tab?) {
                    states[tab?.position ?: 0].select(view)
                }
            })
        }

        return view
    }
}