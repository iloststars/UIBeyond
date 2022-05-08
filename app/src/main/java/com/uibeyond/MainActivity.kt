package com.uibeyond

import android.os.Bundle
import android.view.View
import android.view.animation.BounceInterpolator
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.lzf.easyfloat.EasyFloat
import com.lzf.easyfloat.anim.DefaultAnimator
import com.lzf.easyfloat.enums.ShowPattern
import com.uibeyond.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var isShow = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Example of a call to a native method
        binding.sampleText.text = stringFromJNI()
        EasyFloat.with(this)
            .setAnimator(DefaultAnimator())
            .setTag("float_menu")
            .setShowPattern(ShowPattern.ALL_TIME)
            .setDragEnable(true)
            .setLayout(R.layout.float_menu){root ->
                root.findViewById<ImageButton>(R.id.imageButton).setOnClickListener {
                    if (isShow) {
                        isShow = false
                        root.findViewById<View>(R.id.floatMenu).visibility = View.GONE

                    } else {
                        isShow = true
                        root.findViewById<View>(R.id.floatMenu).visibility = View.VISIBLE
                    }
                }
            }
            .registerCallback {
                drag { view, motionEvent ->
                    view.alpha = 1.0f
                }
                dragEnd {
                    //吸附
                    Thread.sleep(500)
                    EasyFloat.updateFloat("float_menu",0)
                    it.alpha = 0.5f
                }
            }
            .show()

    }

    /**
     * A native method that is implemented by the 'uibeyond' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

    companion object {
        // Used to load the 'uibeyond' library on application startup.
        init {
            System.loadLibrary("uibeyond")
        }
    }
}
