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
        binding!!.ivShow.setImageUrl("https://www.baidu.com/img/flexible/logo/pc/result.png")

//        binding!!.ivShow.setImageUrl("https://cdn.cnbj1.fds.api.mi-img.com/middle.community.vip.bkt/79e7d7ff40199c56a0d52359d56f0870", "https://img0.baidu.com/it/u=1123836491,3626797667&fm=26&fmt=auto&gp=0.jpg")
        binding!!.btXc.setOnClickListener {
            open()
        }
    }

    fun open() {
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofAll())
                .imageEngine(GlideEngine.createGlideEngine())
                .forResult(object : OnResultCallbackListener<LocalMedia?> {
                    override fun onResult(result: List<LocalMedia?>) {
                        // 结果回调
                        if (result != null && result.size > 0) {
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