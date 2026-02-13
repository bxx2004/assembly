package net.bxx2004.assembly.data.attribute

/**
 * @author 6hisea
 * @date  2025/10/21 19:58
 * @description: None
*/
class Sequence<T>(
    var seqs:List<T>,
    var repeatable:Boolean = true
) : LiteralAttribute<T>(seqs[0]) {
    fun reset(value:List<T>) {
        setValue(value[0])
        this.seqs = value
    }
    private var index = 0
    override fun getValue(): T {
        index++
        if (index >= seqs.size) {
            if (repeatable) {
                index = 0
            }else{
                return getValue()
            }
        }
        setValue(seqs[index])
        return super.getValue()
    }

    override fun toString(): String {
        return "Sequence(seqs=$seqs, repeatable=$repeatable, index=$index)"
    }
}