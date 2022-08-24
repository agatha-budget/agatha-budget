/*
 * This file is generated by jOOQ.
 */
package open.tresorier.generated.jooq.test.public_.tables.pojos;


import java.io.Serializable;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Account implements Serializable {

    private static final long serialVersionUID = 802388883;

    private final String  id;
    private final String  budgetId;
    private final String  name;
    private final Boolean archived;
    private final Boolean deleted;
    private final String  bankAccountId;

    public Account(Account value) {
        this.id = value.id;
        this.budgetId = value.budgetId;
        this.name = value.name;
        this.archived = value.archived;
        this.deleted = value.deleted;
        this.bankAccountId = value.bankAccountId;
    }

    public Account(
        String  id,
        String  budgetId,
        String  name,
        Boolean archived,
        Boolean deleted,
        String  bankAccountId
    ) {
        this.id = id;
        this.budgetId = budgetId;
        this.name = name;
        this.archived = archived;
        this.deleted = deleted;
        this.bankAccountId = bankAccountId;
    }

    public String getId() {
        return this.id;
    }

    public String getBudgetId() {
        return this.budgetId;
    }

    public String getName() {
        return this.name;
    }

    public Boolean getArchived() {
        return this.archived;
    }

    public Boolean getDeleted() {
        return this.deleted;
    }

    public String getBankAccountId() {
        return this.bankAccountId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Account (");

        sb.append(id);
        sb.append(", ").append(budgetId);
        sb.append(", ").append(name);
        sb.append(", ").append(archived);
        sb.append(", ").append(deleted);
        sb.append(", ").append(bankAccountId);

        sb.append(")");
        return sb.toString();
    }
}
