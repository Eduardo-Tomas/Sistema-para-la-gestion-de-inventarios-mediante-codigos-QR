package com.example.inventorymanager.operations

interface Downloader {
    // Returns the ID of the downloaded file
    fun downloadFile(url: String, mimeType: String): Long
}