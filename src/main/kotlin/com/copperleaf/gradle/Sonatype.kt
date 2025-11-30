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
import javax.xml.parsers.DocumentBuilderFactory
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

@Suppress("UNCHECKED_CAST")
class Sonatype(private val project: Project) {
    private val XML_MEDIA_TYPE = "application/xml".toMediaType()
    private val JSON_MEDIA_TYPE = "application/json".toMediaType()

    operator fun Document.get(tagName: String): Node {
        return getElementsByTagName(tagName).item(0)
    }

    operator fun Node.get(tagName: String): Node {
        return (this as Element).getElementsByTagName(tagName).item(0)
    }

    fun openSonatypeStagingRepository() {
        val publishConfiguration: PublishConfiguration = ConventionConfig.publishConfig(project)
        val client = OkHttpClient()

        val request = Request.Builder()
            .url("https://ossrh-staging-api.central.sonatype.com/service/local/staging/profiles/${publishConfiguration.stagingProfileId}/start")
            .addHeader(
                "Authorization",
                Credentials.basic(publishConfiguration.sonatypeUsername, publishConfiguration.sonatypePassword)
            )
            .post(
                """
                |<promoteRequest>
                |    <data>
                |        <description>Opened by Gradle</description>
                |    </data>
                |</promoteRequest>
                """.trimMargin().toRequestBody(XML_MEDIA_TYPE)
            )
            .build()

        val responseBody = client
            .newCall(request)
            .execute()
            .body!!
            .string()

        val doc: Document = DocumentBuilderFactory.newInstance()
            .also { it.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true) }
            .newDocumentBuilder()
            .parse(responseBody.byteInputStream())
            .also { it.documentElement.normalize() }

        val stagedRepositoryId = doc["promoteResponse"]["data"]["stagedRepositoryId"].textContent
        println("Opened Sonatype staging repository (id: $stagedRepositoryId")
        publishConfiguration.stagingRepositoryId = stagedRepositoryId
    }

    fun findSonatypeStagingRepository() = with(ConventionConfig.publishConfig(project)) {
        val stagedRepositoryId = jsonSonatypeRequest(
            method = "GET",
            url = "https://ossrh-staging-api.central.sonatype.com/manual/search/repositories",
        )
            .getArray("repositories")
            .getMapAt(0)
            .getString("key")

        println("Found Sonatype staging repository (id: $stagedRepositoryId")
        this.stagingRepositoryId = stagedRepositoryId
    }

    fun closeSonatypeStagingRepository() = with(ConventionConfig.publishConfig(project)) {
        findSonatypeStagingRepository()

        jsonSonatypeRequestIgnoreResponse(
            method = "POST",
            url = "https://ossrh-staging-api.central.sonatype.com/manual/upload/repository/$stagingRepositoryId?publishing_type=portal_api",
            body = "{}",
        )

        jsonSonatypeRequestIgnoreResponse(
            method = "DELETE",
            url = "https://ossrh-staging-api.central.sonatype.com/manual/drop/repository/$stagingRepositoryId",
            body = "{}",
        )

        println("Closed Sonatype staging repository (id: $stagingRepositoryId")
        if (stagingRepositoryIdFile.exists()) {
            stagingRepositoryIdFile.delete()
        }
    }

    fun writeProjectVersion() {
        val projectVersion: ProjectVersion = ConventionConfig.projectVersion(project)

        val file = project.rootProject.layout.buildDirectory.asFile.get().resolve("export").resolve("projectVersion")
        if (!file.parentFile.exists()) {
            file.parentFile.mkdirs()
        }
        if (!file.exists()) {
            file.createNewFile()
        }

        file.writeText(projectVersion.toString())
    }

    private fun PublishConfiguration.jsonSonatypeRequest(
        method: String,
        url: String,
        body: String? = null,
    ): Map<String, Any> {
        val client = OkHttpClient.Builder()
            .connectTimeout(30.seconds.toJavaDuration())
            .readTimeout(120.seconds.toJavaDuration())
            .build()

        val listRepositoriesRequest = Request.Builder()
            .url(url)
            .addHeader("Authorization", Credentials.basic(sonatypeUsername, sonatypePassword))
            .let {
                when (method) {
                    "GET" -> it.get()
                    "POST" -> it.post(body!!.toRequestBody(JSON_MEDIA_TYPE))
                    "DELETE" -> it.delete()
                    else -> throw IllegalArgumentException("Unsupported HTTP method: $method")
                }
            }
            .build()

        val responseBody = client
            .newCall(listRepositoriesRequest)
            .execute()
            .body!!
            .string()

        return groovy.json.JsonSlurper().parseText(responseBody) as Map<String, Any>
    }

    private fun PublishConfiguration.jsonSonatypeRequestIgnoreResponse(
        method: String,
        url: String,
        body: String? = null,
    ) {
        val client = OkHttpClient.Builder()
            .connectTimeout(30.seconds.toJavaDuration())
            .readTimeout(120.seconds.toJavaDuration())
            .build()

        val listRepositoriesRequest = Request.Builder()
            .url(url)
            .addHeader("Authorization", Credentials.basic(sonatypeUsername, sonatypePassword))
            .let {
                when (method) {
                    "GET" -> it.get()
                    "POST" -> it.post(body!!.toRequestBody(JSON_MEDIA_TYPE))
                    "DELETE" -> it.delete()
                    else -> throw IllegalArgumentException("Unsupported HTTP method: $method")
                }
            }
            .build()

        client
            .newCall(listRepositoriesRequest)
            .execute()
            .body!!
            .string()
    }

    private fun Map<*, *>.getArray(key: String): List<Any> {
        return this[key] as List<Any>
    }

    private fun Map<*, *>.getString(key: String): String {
        return this[key] as String
    }

    private fun List<*>.getMapAt(index: Int): Map<Any, Any> {
        return this[index] as Map<Any, Any>
    }
}
