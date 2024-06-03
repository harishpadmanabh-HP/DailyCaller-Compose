package com.tsciences.dailycaller.android.ui.comment

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.tsciences.dailycaller.android.R
import com.tsciences.dailycaller.android.core.util.isDeviceTablet
import com.tsciences.dailycaller.android.ui.newsDetail.NewsDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import spotIm.common.SpotCallback
import spotIm.common.SpotException
import spotIm.common.model.SsoWithJwtResponse
import spotIm.sdk.SpotIm

@AndroidEntryPoint
class CommentActivity : AppCompatActivity() {
    private val viewModel: NewsDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment)
        if (isDeviceTablet(this)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR)
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        }
    }

    override fun onStart() {
        super.onStart()
        if (viewModel.getSpotImToken().isNotEmpty()) {
            getPreConversationFragment(intent)
        } else if (viewModel.getPianoToken().isNotEmpty()) {
            SpotIm.ssoWithJwt(viewModel.getPianoToken(), object : SpotCallback<SsoWithJwtResponse> {
                override fun onSuccess(response: SsoWithJwtResponse) {
                    getPreConversationFragment(intent)
                    viewModel.setSpotImToken(viewModel.getPianoToken())
                }

                override fun onFailure(exception: SpotException) {
                    getPreConversationFragment(intent)
                }
            })
        } else {
            getPreConversationFragment(intent)
        }
    }

    private fun getPreConversationFragment(intent: Intent) {
        val postId = intent.getStringExtra("post_id")

        if (postId != null) {
            SpotIm.getPreConversationFragment(
                postId,
                callback =object :SpotCallback<Fragment> {
                    override fun onFailure(exception: SpotException) {
                        Log.d("SPOT_IM", exception.message!!)
                    }

                    override fun onSuccess(response: Fragment) {
                        Log.d("SPOT_IM", postId.toString())
                        setFragment(response)                    }

                }
            )
        }


    }

    private fun setFragment(fragment: Fragment?) {
        val fragmentManager: FragmentManager = supportFragmentManager
        if (fragment == null) {
            return
        }
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.commentFrameLayout, fragment)
        transaction.commit()
    }
}

