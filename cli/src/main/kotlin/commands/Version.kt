package commands

enum class Version {
    V1, V2
}

fun fromString(version: String): Version {
    return when (version) {
        "V1" -> Version.V1
        "V2" -> Version.V2
        else -> throw IllegalArgumentException("Invalid version")
    }
}
