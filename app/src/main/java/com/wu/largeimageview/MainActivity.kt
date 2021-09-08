package com.wu.largeimageview

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnResultCallbackListener
import com.wu.base.util.screen.ScreenShotUtil
import com.wu.large.widget.ImageSource
import com.wu.large.widget.SubsamplingScaleImageView
import com.wu.largeimageview.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        //本地
//        binding!!.ivShow.setResource(R.drawable.iv_new_media_zw)
        //url
//

//
        binding!!.btXc.setOnClickListener {
            open()
        }
        binding!!.btBm.setOnClickListener {
            showBitmap()
        }
        binding!!.btUrl.setOnClickListener {
            binding!!.ivShow.setImageUrl("https://file.intbull.com/scenic/google_street/20/F8E2AAA5FD7C6969178151C26E2D3FA3.png")
        }
        binding!!.btRes.setOnClickListener {
            binding!!.ivShow.setResource(R.drawable.iv_new_media_zw)
        }
    }

    private fun showBitmap() {


        var bitmap = ScreenShotUtil.getScreenPath(binding!!.root);

        binding!!.ivShow.setBitmap(bitmap)

    }

    fun open() {
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofAll())
                .imageEngine(GlideEngine.createGlideEngine())
                .forResult(object : OnResultCallbackListener<LocalMedia?> {
                    override fun onResult(result: List<LocalMedia?>) {
                        // 结果回调
                        if (result.size > 0) {
                            // 图片
                            binding!!.ivShow.setFilePath(result[0]!!.realPath)
                            //本地gif
//                            binding!!.ivShow.setFilePath("/storage/emulated/0/Download/"+"666.gif")
                        }
                    }

                    override fun onCancel() {
                        // 取消
                    }
                })

    }

}