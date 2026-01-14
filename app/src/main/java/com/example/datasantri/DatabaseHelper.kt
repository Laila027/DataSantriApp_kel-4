package com.example.datasantri

import okhttp3.*
import java.io.IOException

class DatabaseHelper {
    fun updateSantri(nama: String, kelas: String, asal: String, noHp: String, callback: (Boolean) -> Unit) {
        // URL untuk memanggil proc=update
        val url = "https://appocalypse.my.id/santri.php?proc=update&nama=$nama&kelas=$kelas&asal=$asal&no_hp=$noHp"
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) { callback(false) }
            override fun onResponse(call: Call, response: Response) {
                callback(response.isSuccessful)
            }
        })
    }
    fun hapusSantri(nama: String, callback: (Boolean) -> Unit) {
        val url = "https://appocalypse.my.id/santri.php?proc=del&nama=$nama"
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) { callback(false) }
            override fun onResponse(call: Call, response: Response) {
                // Kita anggap berhasil jika server mengirim kode 200
                callback(response.isSuccessful)
            }
        })
    }
    private val client = OkHttpClient()

    // 1. Fungsi untuk SIMPAN data (Sudah kamu buat)
    fun tambahSantri(nama: String, kelas: String, asal: String, noHp: String, callback: (Boolean) -> Unit) {
        val url = "https://appocalypse.my.id/santri.php?proc=in&nama=$nama&kelas=$kelas&asal=$asal&no_hp=$noHp"
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) { callback(false) }
            override fun onResponse(call: Call, response: Response) { callback(response.isSuccessful) }
        })
    }

    // 2. Fungsi untuk AMBIL data (Tambahkan ini min!)
    fun ambilSantri(callback: (String?) -> Unit) {
        // Menggunakan proc=get sesuai tes browser tadi
        val url = "https://appocalypse.my.id/santri.php?proc=get"
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(null)
            }
            override fun onResponse(call: Call, response: Response) {
                // Mengambil teks JSON [{"id":"1",...}] untuk dikirim ke MainActivity
                callback(response.body?.string())
            }
        })
    }
}