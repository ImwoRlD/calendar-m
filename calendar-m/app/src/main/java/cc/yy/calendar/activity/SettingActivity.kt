package cc.yy.calendar.activity

import android.annotation.TargetApi
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import cc.yy.calendar.R
import cc.yy.calendar.util.Constant
import kotlinx.android.synthetic.main.activity_setting.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.startActivityForResult


/**
 * Created by zpy on 2018/3/13.
 */
class SettingActivity : BaseActivity() {
    override fun getLayout(): Int = R.layout.activity_setting
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    private fun init() {
        tv_title.text = "设置"
        initListener()
        val sex = app.getSpValue(Constant.SP_SEX, 0)
        val sexStr = if (sex == 0) {
            "男"
        } else {
            "女"
        }
        tv_sex.text = sexStr
    }

    private fun initListener() {
        rl_change_background.setOnClickListener {
            startActivity<ImageSelectActivity>()
        }
        rl_reset_sex.setOnClickListener {
            startActivityForResult<SelectSexActivity>(Constant.REQUEST_CODE_FOR_SELECT_SEX)
        }
        rl_constellation.setOnClickListener {
            startActivity<ConstellationActivity>()
        }

        rl_important_call.setOnClickListener{
            startActivity<Login>()
        }

    }  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constant.REQUEST_CODE_FOR_SELECT_SEX && resultCode == Constant.RESULT_CODE_OK) {
            val sex = data?.getIntExtra("sex", 0)
            val sexStr = if (sex == 0) {
                "男"
            } else {
                "女"
            }
            tv_sex.text = sexStr
        }
    }
}



/**
 * @param uri The Uri to check.
 * @return Whether the Uri authority is ExternalStorageProvider.
 */
private fun isExternalStorageDocument(uri: Uri): Boolean {
    return "com.android.externalstorage.documents" == uri.authority
}

/**
 * @param uri The Uri to check.
 * @return Whether the Uri authority is DownloadsProvider.
 */
private fun isDownloadsDocument(uri: Uri): Boolean {
    return "com.android.providers.downloads.documents" == uri.authority
}

/**
 * @param uri The Uri to check.
 * @return Whether the Uri authority is MediaProvider.
 */
private fun isMediaDocument(uri: Uri): Boolean {
    return "com.android.providers.media.documents" == uri.authority
}

/**
 * @param uri The Uri to check.
 * @return Whether the Uri authority is Google Photos.
 */
private fun isGooglePhotosUri(uri: Uri): Boolean {
    return "com.google.android.apps.photos.content" == uri.authority
}

private fun getDataColumn(context: Context, uri: Uri?, selection: String?, selectionArgs: Array<String>?): String? {
    var cursor: Cursor? = null
    val column = MediaStore.Images.Media.DATA
    val projection = arrayOf(column)
    try {
        cursor = context.contentResolver.query(uri!!, projection, selection, selectionArgs, null)
        if (cursor != null && cursor.moveToFirst()) {
            val index = cursor.getColumnIndexOrThrow(column)
            return cursor.getString(index)
        }
    } finally {
        if (cursor != null)
            cursor.close()
    }
    return null
}

/**
 * 根据Uri获取图片绝对路径，解决Android4.4以上版本Uri转换
 *
 * @param context
 * @param imageUri
 */
@TargetApi(19)
fun getImageAbsolutePath(context: Context?, imageUri: Uri?): String? {
    if (context == null || imageUri == null)
        return null
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, imageUri)) {
        if (isExternalStorageDocument(imageUri)) {
            val docId = DocumentsContract.getDocumentId(imageUri)
            val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val type = split[0]
            if ("primary".equals(type, ignoreCase = true)) {
                return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
            }
        } else if (isDownloadsDocument(imageUri)) {
            val id = DocumentsContract.getDocumentId(imageUri)
            val contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id)!!)
            return getDataColumn(context, contentUri, null, null)
        } else if (isMediaDocument(imageUri)) {
            val docId = DocumentsContract.getDocumentId(imageUri)
            val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val type = split[0]
            var contentUri: Uri? = null
            if ("image" == type) {
                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            } else if ("video" == type) {
                contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            } else if ("audio" == type) {
                contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            }
            val selection = MediaStore.Images.Media._ID + "=?"
            val selectionArgs = arrayOf(split[1])
            return getDataColumn(context, contentUri, selection, selectionArgs)
        }
    } // MediaStore (and general)
    else if ("content".equals(imageUri.scheme, ignoreCase = true)) {
        // Return the remote address
        return if (isGooglePhotosUri(imageUri)) imageUri.lastPathSegment else getDataColumn(context, imageUri, null, null)
    } else if ("file".equals(imageUri.scheme, ignoreCase = true)) {
        return imageUri.path
    }// File
    return null
}