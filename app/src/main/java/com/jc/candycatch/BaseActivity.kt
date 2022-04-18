package com.jc.candycatch

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.jc.candycatch.dialog.CountDownDialog
import com.jc.candycatch.dialog.ResultDialog
import com.jc.candycatch.utils.getScreenHeight
import com.jc.candycatch.utils.getScreenWidth
import java.lang.reflect.ParameterizedType

open class BaseActivity<VB : ViewBinding> : AppCompatActivity(),
    CountDownDialog.DialogDismissListener,
    ResultDialog.PlayAgainListener {

    lateinit var viewModel: PointViewModel
    lateinit var viewBinding: VB

    val viewMap: MutableMap<Int, TextView> = HashMap()
    var catchNumber = 0
    var isShowResultDialog = false

    val tvWidth = 140
    val tvHeight = 140
    val screenWidth = getScreenWidth()
    val screenHeight = getScreenHeight()
    val halfScreenWidth = screenWidth / 2
    val halfScreenHeight = screenHeight / 2

    val TAG = javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[PointViewModel::class.java]
        val method = getDataBindingClass().getDeclaredMethod("inflate", LayoutInflater::class.java)
        viewBinding = method.invoke(null, layoutInflater) as VB
        setContentView(viewBinding.root)

        initView()
        initObserve()
    }

    private fun getDataBindingClass(): Class<VB> {
        val type = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0]
        return type as Class<VB>
    }

    open fun initView() {
        showCountDownDialog()
    }

    open fun initObserve() {}

    override fun onDismiss() {
        viewModel.generatePointViewOnTime()
    }

    override fun playAgain() {
        viewMap.clear()
        catchNumber = 0
        isShowResultDialog = false

        viewModel.generatePointViewOnTime()
    }

    override fun backHomePage() {
        finish()
    }

    private fun showCountDownDialog() {
        CountDownDialog.newInstance(this)
            .show(supportFragmentManager.beginTransaction(), "CountDownDialog")
    }

    fun showResultDialog() {
        if (viewMap.isEmpty() && !isShowResultDialog
            && !supportFragmentManager.isDestroyed
        ) {
            ResultDialog.newInstance(this, catchNumber)
                .show(supportFragmentManager.beginTransaction(), "ResultDialog")
            isShowResultDialog = true
        }
    }

}