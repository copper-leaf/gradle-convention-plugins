package com.copperleaf.gradle

import okhttp3.Credentials
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.gradle.api.Project
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import javax.xml.XMLConstants
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory

class Sonatype(private val project: Project) {
    val XML_MEDIA_TYPE = "application/xml".toMediaType()

    fun PublishConfiguration.sonatypeRequest(path: String, bodyXml: String): Request {
        return Request.Builder()
            .url("${mavenRepositoryBaseUrl}/$path")
            .post(bodyXml.toRequestBody(XML_MEDIA_TYPE))
            .addHeader("Authorization", Credentials.basic(ossrhUsername, ossrhPassword))
            .build()
    }

    fun Request.executeAndGetXmlResponse(): Document {
        val client = OkHttpClient()

        val response = client.newCall(this).execute()
        val responseBody = response.body!!.string()
        println(responseBody)

        val dbf: DocumentBuilderFactory = DocumentBuilderFactory.newInstance()
        dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        val db: DocumentBuilder = dbf.newDocumentBuilder()
        val doc: Document = db.parse(responseBody.byteInputStream())
        doc.documentElement.normalize()

        return doc
    }

    fun Request.executeAndIgnoreResponse() {
        val client = OkHttpClient()

        val response = client.newCall(this).execute()

        response.body!!.string()
    }

    operator fun Document.get(tagName: String): Node {
        return getElementsByTagName(tagName).item(0)
    }

    operator fun Node.get(tagName: String): Node {
        return (this as Element).getElementsByTagName(tagName).item(0)
    }

    fun openSonatypeStagingRepository()  {
        val publishConfiguration: PublishConfiguration = ConventionConfig.publishConfig(project)

        val doc = publishConfiguration
            .sonatypeRequest(
                path = "service/local/staging/profiles/${publishConfiguration.stagingProfileId}/start",
                bodyXml = """
            |<promoteRequest>
            |    <data>
            |        <description>Opened by Gradle</description>
            |    </data>
            |</promoteRequest>
            """.trimMargin()
            )
            .executeAndGetXmlResponse()

        val stagedRepositoryId = doc["promoteResponse"]["data"]["stagedRepositoryId"].textContent
        println("Opened Sonatype staging repository (id: $stagedRepositoryId")
        publishConfiguration.stagingRepositoryId = stagedRepositoryId
    }

    fun closeSonatypeStagingRepository() {
        val publishConfiguration: PublishConfiguration = ConventionConfig.publishConfig(project)

        publishConfiguration
            .sonatypeRequest(
                path = "service/local/staging/profiles/${publishConfiguration.stagingProfileId}/finish",
                bodyXml = """
            |<promoteRequest>
            |    <data>
            |        <stagedRepositoryId>${publishConfiguration.stagingRepositoryId}</stagedRepositoryId>
            |        <description>Closed by Gradle</description>
            |    </data>
            |</promoteRequest>
            """.trimMargin()
            )
            .executeAndIgnoreResponse()

        println("Closed Sonatype staging repository (id: ${publishConfiguration.stagingRepositoryId}")
        if(publishConfiguration.stagingRepositoryIdFile.exists()) {
            publishConfiguration.stagingRepositoryIdFile.delete()
        }
    }

    fun writeProjectVersion() {
        val projectVersion: ProjectVersion = ConventionConfig.projectVersion(project)

        val file = project.rootProject.layout.buildDirectory.asFile.get().resolve("export").resolve("projectVersion")
        if(!file.parentFile.exists()) {
            file.parentFile.mkdirs()
        }
        if(!file.exists()) {
            file.createNewFile()
        }

        file.writeText(projectVersion.toString())
    }

}
