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
 * TODO: 1, 【20200926済】 deleteWordの実装
 *       2, 【20200926済】 first commit
 *       3,　【20200926済】 Adapter, Model などのフォルダ分け調査
 *       4, 【20201003済】レイアウト記述回りを DataBinding に書き換え
 *       5, 【20201004済】LiveDataの勉強　DataBindingの内容を最適化
 *       6,_変数の意味とは
 *       7, 【～20201103済】メモ帳アプリ　仕様検討（ざっくりノートに）
 *       8,　Roomの仕様理解（最大文字数、 テーブル設計）
 *       9, メモ帳に置き換え ブランチ切って作業
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