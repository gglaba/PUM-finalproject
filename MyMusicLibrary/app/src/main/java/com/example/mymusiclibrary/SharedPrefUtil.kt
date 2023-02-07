package com.example.mymusiclibrary

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

private const val ALBUMS_LIST = "albums"
private const val ALBUM_FILE = "album_file"

fun saveAlbumsList(context: Context, list: List<Album>) {
    val json = Gson().toJson(list)
    val sharedPreferences = context.getSharedPreferences(ALBUM_FILE, Context.MODE_PRIVATE)
    sharedPreferences.edit().putString(ALBUMS_LIST, json).apply()
}

fun getAlbumsList(context: Context): List<Album> {
    val sharedPreferences = context.getSharedPreferences(ALBUM_FILE, Context.MODE_PRIVATE)
    val json = sharedPreferences.getString(ALBUMS_LIST, null)

    if (json.isNullOrEmpty()) {
        return emptyList()
    }

    val type = object : TypeToken<List<Album>>() {}.type
    return Gson().fromJson(json, type)
}