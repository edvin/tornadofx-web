package io.tornadofx.admin.felix

import io.tornadofx.admin.domain.MavenMetadata
import java.net.URI
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@WebServlet("/felix-bundle.zip")
class FelixDownloadServlet : HttpServlet() {
    companion object {
        val FelixVersion = "5.6.10"
        val JavaFXSupportVersion = "8.0.2"
        val ConfigAdminVersion = "1.8.16"
        val JsonVersion = "1.1.2"
        val KotlinVersion = "1.2.31"
        val CommonsLoggingVersion = "1.2"
        val HttpClientVersion = "4.5.3"
        val HttpCoreVersion = "4.4.6"

        val FelixURI = URI.create("http://www-eu.apache.org/dist/felix/org.apache.felix.main.distribution-$FelixVersion.zip")
        val JavaFXSupportURI = URI.create("http://repo1.maven.org/maven2/no/tornado/javafx-osgi/$JavaFXSupportVersion/javafx-osgi-$JavaFXSupportVersion.jar")
        val ConfigAdminURI = URI.create("http://www-eu.apache.org/dist/felix/org.apache.felix.configadmin-$ConfigAdminVersion.jar")
        val JsonURI = URI.create("http://repo1.maven.org/maven2/org/glassfish/javax.json/$JsonVersion/javax.json-$JsonVersion.jar")
        val KotlinURI = URI.create("http://repo1.maven.org/maven2/org/jetbrains/kotlin/kotlin-osgi-bundle/$KotlinVersion/kotlin-osgi-bundle-$KotlinVersion.jar")
        val CommonsLoggingURI = URI.create("http://repo1.maven.org/maven2/commons-logging/commons-logging/$CommonsLoggingVersion/commons-logging-$CommonsLoggingVersion.jar")
        val HttpClientURI = URI.create("http://repo1.maven.org/maven2/org/apache/httpcomponents/httpclient-osgi/$HttpClientVersion/httpclient-osgi-$HttpClientVersion.jar")
        val HttpCoreURI = URI.create("http://repo1.maven.org/maven2/org/apache/httpcomponents/httpcore-osgi/$HttpCoreVersion/httpcore-osgi-$HttpCoreVersion.jar")
    }

    override fun doGet(request: HttpServletRequest, response: HttpServletResponse) {
        response.addHeader("Content-Type", "application/octet-stream")

        val TornadoFXVersion = MavenMetadata.getTornadoFX().versioning.latest
        val TornadoFXURI = URI.create("http://repo1.maven.org/maven2/no/tornado/tornadofx/$TornadoFXVersion/tornadofx-$TornadoFXVersion.jar")

        ZipInputStream(FelixURI.toURL().openStream()).use { input ->
            ZipOutputStream(response.outputStream).use { output ->
                extractFelix(input, output)
                output.embedLibrary(TornadoFXURI)
                output.embedLibrary(JavaFXSupportURI)
                output.embedLibrary(ConfigAdminURI)
                output.embedLibrary(KotlinURI)
                output.embedLibrary(CommonsLoggingURI)
                output.embedLibrary(HttpClientURI)
                output.embedLibrary(HttpCoreURI)
                output.embedLibrary(JsonURI)
            }
        }
    }

    private fun extractFelix(input: ZipInputStream, output: ZipOutputStream) {
        var inputEntry: ZipEntry? = null
        do {
            inputEntry = input.nextEntry
            if (inputEntry != null) {
                val outputEntry = ZipEntry(inputEntry.name)
                output.putNextEntry(outputEntry)
                input.copyTo(output)
                output.closeEntry()
            }
        } while (inputEntry != null)
    }

    private fun ZipOutputStream.embedLibrary(library: URI) {
        val entry = ZipEntry("felix-framework-$FelixVersion/bundle/${library.path.substringAfterLast("/")}")
        putNextEntry(entry)
        library.toURL().openStream().use {
            it.copyTo(this)
        }
        closeEntry()
    }

}