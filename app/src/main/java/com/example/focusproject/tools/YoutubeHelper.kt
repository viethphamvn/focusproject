package com.example.focusproject.tools

import java.util.regex.Pattern

class YouTubeHelper {
    companion object {
        private const val youTubeUrlRegEx = "^(https?)?(://)?(www.)?(m.)?((youtube.com)|(youtu.be))/"
        private val videoIdRegex = arrayOf(
            "\\?vi?=([^&]*)",
            "watch\\?.*v=([^&]*)",
            "(?:embed|vi?)/([^/?]*)",
            "^([A-Za-z0-9\\-_]*)"
        )

        fun extractVideoIdFromUrl(url: String): String {
            val youTubeLinkWithoutProtocolAndDomain =
                youTubeLinkWithoutProtocolAndDomain(url)
            for (regex in videoIdRegex) {
                val compiledPattern = Pattern.compile(regex)
                val matcher =
                    compiledPattern.matcher(youTubeLinkWithoutProtocolAndDomain)
                if (matcher.find()) {
                    println(matcher.group(1))
                    return matcher.group(1)
                }
            }
            println("nothing")
            return ""
        }

        private fun youTubeLinkWithoutProtocolAndDomain(url: String): String {
            val compiledPattern =
                Pattern.compile(youTubeUrlRegEx)
            val matcher = compiledPattern.matcher(url)
            return if (matcher.find()) {
                url.replace(matcher.group(), "")
            } else url
        }
    }
}