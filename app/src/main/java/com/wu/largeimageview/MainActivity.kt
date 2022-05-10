package com.wu.largeimageview

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.wu.base.util.screen.ScreenShotUtil
import com.wu.largeimageview.databinding.ActivityMainBinding
import com.wu.media.ImagePicker
import com.wu.media.PickerConfig
import com.wu.media.media.entity.Media
import com.wu.media.utils.AndroidQUtil
import java.util.*


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



        //打开相册
        ImagePicker. Builder()
                .maxNum(9)
                .setSelectGif(true) //                        .isSinglePick(true)
                .maxImageSize(25 * 1024 * 1024)
                .maxVideoSize(100 * 1024 * 1024)
                .isReturnUri(AndroidQUtil.isAndroidQ())
                .selectMode(PickerConfig.PICKER_IMAGE)
                .needCamera(true)
                .builder()
                .start(this, PickerConfig.PICKER_IMAGE, PickerConfig.DEFAULT_RESULT_CODE)

//        PictureSelector.create(this)
//                .openGallery(PictureMimeType.ofAll())
//                .imageEngine(GlideEngine.createGlideEngine())
//                .forResult(object : OnResultCallbackListener<LocalMedia?> {
//                    override fun onResult(result: List<LocalMedia?>) {
//                        // 结果回调
//                        if (result.size > 0) {
//                            // 图片
//                            binding!!.ivShow.setFilePath(result[0]!!.realPath)
//                            //本地gif
////                            binding!!.ivShow.setFilePath("/storage/emulated/0/Download/"+"666.gif")
//                        }
//                    }
//
//                    override fun onCancel() {
//                        // 取消
//                    }
//                })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == PickerConfig.DEFAULT_RESULT_CODE) {
           var select = data!!.getParcelableArrayListExtra<Media>(PickerConfig.EXTRA_RESULT)
            binding!!.ivShow.setImageUri(select!!.get(0).fileUri)
        }
    }

}