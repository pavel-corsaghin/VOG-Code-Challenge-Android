package com.example.fcmexample

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.HasDefaultViewModelProviderFactory
import com.example.fcmexample.databinding.ActivityMainBinding
import com.example.fcmexample.utils.PREFS_NAME
import com.example.fcmexample.utils.TOKEN
import com.example.fcmexample.utils.ViewModelProviderFactory
import com.example.fcmexample.utils.viewModel
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity(), HasDefaultViewModelProviderFactory {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: FCMViewModel by viewModel()
    private val clipboardManager by lazy { getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager }
    private val sharedPreferences by lazy { getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE) }

    override fun getDefaultViewModelProviderFactory() = ViewModelProviderFactory()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.apply {
            lifecycleOwner = this@MainActivity
            viewModel = this@MainActivity.viewModel
            copyClick = View.OnClickListener {
                clipboardManager.setPrimaryClip(ClipData.newPlainText("token", sharedPreferences.getString(
                    TOKEN, "")))
                Snackbar.make(binding.root, "Copied to clipboard", Snackbar.LENGTH_SHORT).show()
            }
            onStartSendActivityClick = View.OnClickListener {
                SendActivity.start(this@MainActivity)
            }
            recycler.adapter = NotificationListAdapter(ItemClickListener {

            })
        }
    }
}
