package com.example.inventorymanager.operations

import android.app.DownloadManager
import android.content.Context
import android.os.Environment
import androidx.core.net.toUri

class FileDownloader(private val context: Context) : Downloader {
    private val downloadManager = context.getSystemService(DownloadManager::class.java)

    //"application/pdf"
    override fun downloadFile(url: String, mimeType: String): Long {
        val fileName = url.substringAfterLast("/")

        /*
            If you use setDestinationInExternalPublicDir

            For applications targeting Build.VERSION_CODES.Q or above, WRITE_EXTERNAL_STORAGE
            permission is not needed and the dirType must be one of the known public
            directories like Environment#DIRECTORY_DOWNLOADS, Environment#DIRECTORY_PICTURES,
            Environment#DIRECTORY_MOVIES, etc.
         */

        val request = DownloadManager.Request(url.toUri())
            .setMimeType(mimeType)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setTitle(fileName)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/inventory/$fileName")

        return downloadManager.enqueue(request)
    }
}