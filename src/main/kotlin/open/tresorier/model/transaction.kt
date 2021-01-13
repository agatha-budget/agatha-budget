package open.tresorier.model

/**
 * The current `Transaction` type defines a transaction with at least a date and a price. Every output and input on any account will be registered by Tresorier as a transaction
 *
 * @property price a float that represent the value of the transaction ex: "10.25"
 * @property memo a note providing any kind of information on the transaction ex: "new book from my favorite author !"
 */
data class Transaction (val price: Double, val memo: String)