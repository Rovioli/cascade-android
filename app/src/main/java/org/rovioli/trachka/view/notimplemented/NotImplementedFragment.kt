package org.rovioli.trachka.view.notimplemented

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.rovioli.trachka.R

class NotImplementedFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.not_implemented, container, false)
        // TODO: fill view with data
        return view
    }
}