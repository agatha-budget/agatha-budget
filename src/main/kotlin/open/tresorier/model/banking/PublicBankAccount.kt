package open.tresorier.model.banking

open class PublicBankAccount (
    var id: String,
    var name: String,
    var bankId: String,
    var bankingId: String,
    var timestamp: Long,
    var balance: Int? = null
){

    override fun toString(): String {
        return "name: $name, bankId: $bankId, bankingId: $bankingId, id: $id, timestamp: $timestamp, balance: $balance"
    }
}