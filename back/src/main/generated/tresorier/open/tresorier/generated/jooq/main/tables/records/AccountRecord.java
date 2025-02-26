/*
 * This file is generated by jOOQ.
 */
package open.tresorier.generated.jooq.main.tables.records;


import open.tresorier.generated.jooq.main.tables.Account;

import org.jooq.Record1;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class AccountRecord extends UpdatableRecordImpl<AccountRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>public.account.id</code>.
     */
    public AccountRecord setId(String value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>public.account.id</code>.
     */
    public String getId() {
        return (String) get(0);
    }

    /**
     * Setter for <code>public.account.budget_id</code>.
     */
    public AccountRecord setBudgetId(String value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>public.account.budget_id</code>.
     */
    public String getBudgetId() {
        return (String) get(1);
    }

    /**
     * Setter for <code>public.account.name</code>.
     */
    public AccountRecord setName(String value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>public.account.name</code>.
     */
    public String getName() {
        return (String) get(2);
    }

    /**
     * Setter for <code>public.account.archived</code>.
     */
    public AccountRecord setArchived(Boolean value) {
        set(3, value);
        return this;
    }

    /**
     * Getter for <code>public.account.archived</code>.
     */
    public Boolean getArchived() {
        return (Boolean) get(3);
    }

    /**
     * Setter for <code>public.account.deleted</code>.
     */
    public AccountRecord setDeleted(Boolean value) {
        set(4, value);
        return this;
    }

    /**
     * Getter for <code>public.account.deleted</code>.
     */
    public Boolean getDeleted() {
        return (Boolean) get(4);
    }

    /**
     * Setter for <code>public.account.bank_account_id</code>.
     */
    public AccountRecord setBankAccountId(String value) {
        set(5, value);
        return this;
    }

    /**
     * Getter for <code>public.account.bank_account_id</code>.
     */
    public String getBankAccountId() {
        return (String) get(5);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<String> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached AccountRecord
     */
    public AccountRecord() {
        super(Account.ACCOUNT);
    }

    /**
     * Create a detached, initialised AccountRecord
     */
    public AccountRecord(String id, String budgetId, String name, Boolean archived, Boolean deleted, String bankAccountId) {
        super(Account.ACCOUNT);

        setId(id);
        setBudgetId(budgetId);
        setName(name);
        setArchived(archived);
        setDeleted(deleted);
        setBankAccountId(bankAccountId);
        resetTouchedOnNotNull();
    }

    /**
     * Create a detached, initialised AccountRecord
     */
    public AccountRecord(open.tresorier.generated.jooq.main.tables.pojos.Account value) {
        super(Account.ACCOUNT);

        if (value != null) {
            setId(value.getId());
            setBudgetId(value.getBudgetId());
            setName(value.getName());
            setArchived(value.getArchived());
            setDeleted(value.getDeleted());
            setBankAccountId(value.getBankAccountId());
            resetTouchedOnNotNull();
        }
    }
}
