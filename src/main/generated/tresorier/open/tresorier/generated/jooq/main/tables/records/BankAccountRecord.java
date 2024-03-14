/*
 * This file is generated by jOOQ.
 */
package open.tresorier.generated.jooq.main.tables.records;


import open.tresorier.generated.jooq.main.tables.BankAccount;

import org.jooq.Record1;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class BankAccountRecord extends UpdatableRecordImpl<BankAccountRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>public.bank_account.id</code>.
     */
    public BankAccountRecord setId(String value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>public.bank_account.id</code>.
     */
    public String getId() {
        return (String) get(0);
    }

    /**
     * Setter for <code>public.bank_account.name</code>.
     */
    public BankAccountRecord setName(String value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>public.bank_account.name</code>.
     */
    public String getName() {
        return (String) get(1);
    }

    /**
     * Setter for <code>public.bank_account.agreement_id</code>.
     */
    public BankAccountRecord setAgreementId(String value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>public.bank_account.agreement_id</code>.
     */
    public String getAgreementId() {
        return (String) get(2);
    }

    /**
     * Setter for <code>public.bank_account.deleted</code>.
     */
    public BankAccountRecord setDeleted(Boolean value) {
        set(3, value);
        return this;
    }

    /**
     * Getter for <code>public.bank_account.deleted</code>.
     */
    public Boolean getDeleted() {
        return (Boolean) get(3);
    }

    /**
     * Setter for <code>public.bank_account.bank_id</code>.
     */
    public BankAccountRecord setBankId(String value) {
        set(4, value);
        return this;
    }

    /**
     * Getter for <code>public.bank_account.bank_id</code>.
     */
    public String getBankId() {
        return (String) get(4);
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
     * Create a detached BankAccountRecord
     */
    public BankAccountRecord() {
        super(BankAccount.BANK_ACCOUNT);
    }

    /**
     * Create a detached, initialised BankAccountRecord
     */
    public BankAccountRecord(String id, String name, String agreementId, Boolean deleted, String bankId) {
        super(BankAccount.BANK_ACCOUNT);

        setId(id);
        setName(name);
        setAgreementId(agreementId);
        setDeleted(deleted);
        setBankId(bankId);
        resetChangedOnNotNull();
    }

    /**
     * Create a detached, initialised BankAccountRecord
     */
    public BankAccountRecord(open.tresorier.generated.jooq.main.tables.pojos.BankAccount value) {
        super(BankAccount.BANK_ACCOUNT);

        if (value != null) {
            setId(value.getId());
            setName(value.getName());
            setAgreementId(value.getAgreementId());
            setDeleted(value.getDeleted());
            setBankId(value.getBankId());
            resetChangedOnNotNull();
        }
    }
}
