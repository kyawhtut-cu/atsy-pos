package com.kyawhtut.pos.utils.webserver.net

import android.os.Environment
import android.webkit.MimeTypeMap
import com.kyawhtut.pos.App
import com.yanzhenjie.andserver.http.multipart.MultipartFile
import com.yanzhenjie.andserver.util.StringUtils
import java.io.File
import java.util.*

/**
 * Created by Zhenjie Yan on 2018/6/9.
 */
object FileUtils {
    /**
     * Create a random file based on mimeType.
     *
     * @param file file.
     * @return file object.
     */
    fun createRandomFile(file: MultipartFile): File {
        var extension =
            MimeTypeMap.getSingleton().getExtensionFromMimeType(file.contentType.toString())
        if (StringUtils.isEmpty(extension)) {
            extension = MimeTypeMap.getFileExtensionFromUrl(file.filename)
        }
        val uuid = UUID.randomUUID().toString()
        return File(App.instance?.rootDir, "$uuid.$extension")
    }

    /**
     * SD is available.
     *
     * @return true, otherwise is false.
     */
    fun storageAvailable(): Boolean {
        return if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            val sd =
                File(Environment.getExternalStorageDirectory().absolutePath)
            sd.canWrite()
        } else {
            false
        }
    }
}