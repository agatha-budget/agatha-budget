package open.tresorier.model.banking

open class Bank (
    var id: String,
    var name: String,
    var logo: String
){
    override fun toString(): String {
        return "id: $id, name: $name, logo: $logo"
    }
}