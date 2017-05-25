package io.tornadofx.admin.domain

import java.net.URI
import javax.xml.bind.JAXB
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement(name = "metadata")
class MavenMetadata {
    var versioning: Versioning = Versioning()

    class Versioning {
        var latest: String = ""
    }

    companion object {
        fun getTornadoFX(): MavenMetadata = JAXB.unmarshal(URI.create("http://repo1.maven.org/maven2/no/tornado/tornadofx/maven-metadata.xml"), MavenMetadata::class.java)

    }
}
