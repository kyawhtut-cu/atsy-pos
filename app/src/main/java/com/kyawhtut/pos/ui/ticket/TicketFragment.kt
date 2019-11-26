package com.kyawhtut.pos.ui.ticket

import android.os.Bundle
import com.kyawhtut.pos.R
import com.kyawhtut.pos.ui.base.BaseFragment
import com.kyawhtut.pos.utils.popupDialogBuilder
import kotlinx.android.synthetic.main.fragment_ticket.*

class TicketFragment : BaseFragment(R.layout.fragment_ticket) {

    private val popupDialog = popupDialogBuilder {
        popupItemList {
            popupItem {
                title = "Print"
                icon = R.drawable.ic_search_black_24dp
            }
            popupItem {
                title = "Cancel"
                icon = R.drawable.ic_search_black_24dp
            }
        }
        callback = {

        }
    }

    override fun onViewCreated(bundle: Bundle) {
        iv_menu.setOnClickListener {
            popupDialog.apply {
                view = iv_menu
                bind().show()
            }
        }
    }
}