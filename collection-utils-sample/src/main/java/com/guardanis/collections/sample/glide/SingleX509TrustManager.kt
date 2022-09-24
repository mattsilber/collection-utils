package com.guardanis.collections.sample.glide

import java.security.KeyStore
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

class SingleX509TrustManager @Throws(CertificateException::class)
constructor(): X509TrustManager {

    private var x509TrustManagers: MutableList<X509TrustManager> = ArrayList<X509TrustManager>()

    init {
        val factories = ArrayList<TrustManagerFactory>()

        try {
            val original = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
            original.init(null as KeyStore?)

            factories.add(original)
        }
        catch (e: Exception) {
            throw CertificateException(e)
        }

        factories.flatMap({ it.trustManagers.toList() })
            .mapNotNull({ it as? X509TrustManager })
            .forEach({ x509TrustManagers.add(it) })

        if (x509TrustManagers.isEmpty())
            throw CertificateException("Couldn't find any X509TrustManagers")
    }

    @Throws(CertificateException::class)
    override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
        for (tm in x509TrustManagers) {
            try {
                tm.checkClientTrusted(chain, authType)

                return
            }
            catch (e: CertificateException) {
            }
        }

        throw CertificateException()
    }

    @Throws(CertificateException::class)
    override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
        for (tm in x509TrustManagers) {
            try {
                tm.checkServerTrusted(chain, authType)

                return
            }
            catch (e: CertificateException) {
            }
        }

        throw CertificateException()
    }

    override fun getAcceptedIssuers(): Array<X509Certificate> {
        return x509TrustManagers
            .flatMap({ it.acceptedIssuers.toList() })
            .toTypedArray()
    }
}