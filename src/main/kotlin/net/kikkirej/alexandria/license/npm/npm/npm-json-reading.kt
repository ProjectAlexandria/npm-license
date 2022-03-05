package net.kikkirej.alexandria.license.npm.npm

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Service

@Service
class NpmJsonReader {

    fun parseJson(json: String): Map<String, NpmJsonData> {
        val jsonParser = ObjectMapper()
        val result: Map<String, NpmJsonData> = jsonParser.readValue(json, object: TypeReference<Map<String, NpmJsonData>>() {})
        return result
    }

}

@JsonIgnoreProperties(ignoreUnknown = true)
class NpmJsonData(
   var licenses: String? = null,
   var repository: String? = null,
   var publisher: String? = null,
   var email: String? = null,
   var url: String? = null,
   var path: String? = null,
   var licenseFile: String? = null
)
