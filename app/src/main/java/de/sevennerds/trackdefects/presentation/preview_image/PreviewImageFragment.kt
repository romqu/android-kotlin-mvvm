package de.sevennerds.trackdefects.presentation.preview_image

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.clicks
import de.sevennerds.trackdefects.R
import de.sevennerds.trackdefects.presentation.MainActivity
import de.sevennerds.trackdefects.presentation.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_preview_image.*

class PreviewImageFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        super.onCreateView(inflater, container, savedInstanceState)

        return inflater.inflate(R.layout.fragment_preview_image,
                                container,
                                false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        MainActivity[context!!].supportActionBar?.hide()

        setup()

    }

    private fun setup() {
        setupEvents()
    }

    private fun setupEvents() {

        previewImageAcceptBtn
                .clicks()
                .subscribe()

        previewImageDismissBtn
                .clicks()
                .subscribe()

    }
}