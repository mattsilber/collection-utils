package com.guardanis.collections.sample.glide

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.LibraryGlideModule
import okhttp3.OkHttpClient
import java.io.InputStream
import javax.net.ssl.SSLContext

@GlideModule
class GlideHttpClient: LibraryGlideModule() {

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        super.registerComponents(context, glide, registry)

        val trustManager = SingleX509TrustManager()

        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, arrayOf(trustManager), java.security.SecureRandom())

        val builder = OkHttpClient.Builder()
        builder.sslSocketFactory(sslContext.socketFactory, trustManager)
        builder.hostnameVerifier({ _, _ -> true })

        val client = builder.build()

        registry.replace(GlideUrl::class.java, InputStream::class.java, OkHttpUrlLoader.Factory(client))
    }
}