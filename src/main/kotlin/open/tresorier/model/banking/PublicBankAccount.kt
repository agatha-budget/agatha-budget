package open.tresorier.model.banking

open class PublicBankAccount (
    var id: String,
    var name: String,
    var bankId: String,
    var timestamp: Long
){

    override fun toString(): String {
        return "name: $name, bankId: $bankId, id: $id, timestamp: $timestamp"
    }
}