package net.bxx2004.assembly.data

/**
 * @author 6hisea
 * @date  2025/10/2 17:17
 * @description: None
 */
data class AssemblyIdentifier(
    val namespace: String,
    val path: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AssemblyIdentifier

        if (namespace != other.namespace) return false
        if (path != other.path) return false

        return true
    }

    override fun hashCode(): Int {
        var result = namespace.hashCode()
        result = 31 * result + path.hashCode()
        return result
    }

    override fun toString(): String {
        return "$namespace:$path"
    }
    companion object{
        fun parse(str:String):AssemblyIdentifier{
            return AssemblyIdentifier(str.split(":")[0], str.split(":")[1])
        }
        fun String.id():AssemblyIdentifier{
            return parse(this)
        }
        fun List<String>.id():List<AssemblyIdentifier>{
            return map { it.id() }
        }
        fun Array<String>.id():Array<AssemblyIdentifier>{
            return map { it.id() }.toTypedArray()
        }
    }
}
