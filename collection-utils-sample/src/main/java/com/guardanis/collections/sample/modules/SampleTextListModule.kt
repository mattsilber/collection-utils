package com.guardanis.collections.sample.modules

import android.view.View
import com.guardanis.collections.adapters.ModularAdapter
import com.guardanis.collections.list.adapters.ListViewModule
import com.guardanis.collections.sample.R
import com.guardanis.fontutils.TextView
import java.util.*

class SampleTextListModule(val text: String) {

    class ViewModule: com.guardanis.collections.list.adapters.ListViewModule<SampleTextListModule>(R.layout.text_list_module) {

        var titleView: TextView? = null

        override fun locateViewComponents(convertView: View) {
            this.titleView = convertView.findViewById(R.id.text_module_title)
        }

        override fun updateView(adapter: ModularAdapter, item: SampleTextListModule, position: Int) {
            titleView?.text = item.text
        }
    }

    companion object {

        fun createInstance(): SampleTextListModule {
            return SampleTextListModule("Some random number: ${Random().nextInt(100000).toString()}")
        }
    }
}