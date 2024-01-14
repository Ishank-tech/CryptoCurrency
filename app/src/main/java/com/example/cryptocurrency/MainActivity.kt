package com.example.cryptocurrency

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.cryptocurrency.models.Currency
import com.example.cryptocurrency.utils.NetworkConnection
import com.example.cryptocurrency.utils.NetworkResult
import com.example.cryptocurrency.utils.SharedPrefs
import com.example.cryptocurrency.viewmodels.MainViewModel
import com.example.cryptocurrency.viewmodels.MainViewModelFactory
import java.util.Timer
import java.util.TimerTask

class MainActivity : AppCompatActivity() {
    private lateinit var feed: RecyclerView
    private lateinit var adapter: CurrencyAdapter
    private lateinit var mainViewModel: MainViewModel
    private lateinit var lastRefreshTimeTextView: TextView
    private lateinit var retryButton: Button
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var networkConnection: NetworkConnection

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeViews()
        initializeViewModel()
        setupRecyclerView()
        setupSwipeRefreshLayout()

        observeNetworkConnection()
        observeViewModel()

        scheduleAutoRefresh()
    }

    private fun initializeViews() {
        feed = findViewById(R.id.feed)
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        lastRefreshTimeTextView = findViewById(R.id.lastRefreshTimeTextView)
        retryButton = findViewById(R.id.retryButton)
        adapter = CurrencyAdapter(this)
        networkConnection = NetworkConnection(applicationContext)
    }

    private fun initializeViewModel() {
        val repository = (application as CryptoApp).cryptoRepository
        mainViewModel = ViewModelProvider(this, MainViewModelFactory(repository, SharedPrefs()))
            .get(MainViewModel::class.java)
    }

    private fun setupRecyclerView() {
        feed.adapter = adapter
        feed.layoutManager = LinearLayoutManager(this)
    }

    private fun setupSwipeRefreshLayout() {
        swipeRefreshLayout.setOnRefreshListener {
            handleSwipeRefresh()
        }

        retryButton.setOnClickListener {
            handleRetryButton()
        }
    }

    private fun handleSwipeRefresh() {
        if (networkConnection.value == true) {
            mainViewModel.refreshData()
            retryButton.visibility = View.GONE
        } else {
            showNetworkErrorMessage()
        }
        swipeRefreshLayout.isRefreshing = false
    }

    private fun handleRetryButton() {
        if (networkConnection.value == true) {
            swipeRefreshLayout.isRefreshing = true
            mainViewModel.refreshData()
            retryButton.visibility = View.GONE
        } else {
            showNetworkErrorMessage()
        }
    }

    private fun showNetworkErrorMessage() {
        Toast.makeText(this, "Please connect to the network", Toast.LENGTH_SHORT).show()
        swipeRefreshLayout.isRefreshing = false
    }

    private fun observeNetworkConnection() {
        networkConnection.observe(this) {
            if (!it) {
                showNetworkErrorMessage()
                retryButton.visibility = View.VISIBLE
                swipeRefreshLayout.isRefreshing = false
            }
        }
    }

    private fun observeViewModel() {
        swipeRefreshLayout.isRefreshing = true

        mainViewModel.currencies.observe(this) { result ->
            when (result) {
                is NetworkResult.Error -> {
                    showError(result.message.toString())
                }
                is NetworkResult.Success -> {
                    showSuccess(result.data)
                }
                is NetworkResult.Loading -> {
                    // Handle loading state if needed
                }
            }
        }

        mainViewModel.lastRefreshTime.observe(this) { lastRefreshTime ->
            lastRefreshTimeTextView.text = lastRefreshTime
        }
    }

    private fun showError(errorMessage: String) {
        Toast.makeText(this, "Error: $errorMessage", Toast.LENGTH_SHORT).show()
        retryButton.visibility = View.VISIBLE
        swipeRefreshLayout.isRefreshing = false
    }

    private fun showSuccess(data: List<Currency>?) {
        data?.let {
            adapter.currencies = it
            adapter.notifyDataSetChanged()
        }
        swipeRefreshLayout.isRefreshing = false
    }

    private fun scheduleAutoRefresh() {
        val timer = Timer()
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    mainViewModel.refreshData()
                }
            }
        }, 0, 180000) // Auto-refresh every 3 minutes
    }
}