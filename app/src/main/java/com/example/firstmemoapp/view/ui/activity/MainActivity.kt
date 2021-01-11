package com.example.firstmemoapp.view.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.firstmemoapp.R
import com.example.firstmemoapp.view.ui.fragment.EditMemoFragment
import com.example.firstmemoapp.view.ui.fragment.TopMemoListFragment
import com.example.firstmemoapp.viewModel.WordViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.firebase.ktx.Firebase


/**
 * TODO: 1, List -> Editへのデータ受け渡し
 *       2,　Roomにメモを保存(Id(AutoIncriment),タイトル、テキスト、現在時刻)
 *       3, Roomからメモを取得
 *       4,　Roomのなかみをリスト表示
 *       5,　リストからメモを削除
 *       6,　新規作成フローティングボタンを追加
 *       7,　リストからメモを削除するボタンをメニュー表示か（・・・　から　複数のメニューを表示）
 *       8, UX改善
 *       9,　広告など公開前準備
 *
 */
class MainActivity : AppCompatActivity() {

    private val newWordActivityRequestCode = 1
    private lateinit var wordViewModel: WordViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            val fragment = when {
                isFirstLaunch() -> EditMemoFragment()
                else -> TopMemoListFragment()
            }
            val transaction = supportFragmentManager.beginTransaction()

            transaction.add(R.id.container, fragment).commit()
        }

        // Firebase 広告
        MobileAds.initialize(applicationContext, "ca-app-pub-3940256099942544~3347511713")
        val mAdView = findViewById<AdView>(R.id.adView)
        val adRequest: AdRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        // Firebase イベントテスト

    }

    /**
     * 初回起動判定
     */
    private fun isFirstLaunch(): Boolean {
        // SharedPreferenceの新しいので初回起動判定を行う
        return false
    }
}