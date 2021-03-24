package com.example.fcmexample

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.fcmexample.databinding.ActivitySendBinding
import com.example.fcmexample.utils.ViewModelProviderFactory
import com.example.fcmexample.utils.viewModel
import com.google.android.material.snackbar.Snackbar


class SendActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySendBinding
    private val viewModel: SendNotificationVM by viewModel()

    companion object {
        fun start(activity: Activity) {
            val intent = Intent(activity, SendActivity::class.java)
            activity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_send)
        binding.apply {
            lifecycleOwner = this@SendActivity
            viewModel = this@SendActivity.viewModel
            onSendClick = View.OnClickListener {
                this@SendActivity.viewModel.sendNotification()
            }
            onBackClick = View.OnClickListener {
                onBackPressed()
            }
        }

        viewModel.apply {
            sendNotificationMessage.observe(this@SendActivity, Observer {message ->
                Snackbar.make(
                    binding.root,
                    message,
                    Snackbar.LENGTH_SHORT
                ).show()
            })
        }
    }


    override fun getDefaultViewModelProviderFactory(): ViewModelProvider.Factory =
        ViewModelProviderFactory()
}