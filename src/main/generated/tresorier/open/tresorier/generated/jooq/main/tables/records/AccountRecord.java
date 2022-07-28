/*
 * This file is generated by jOOQ.
 */
package open.tresorier.generated.jooq.main.tables.records;


import open.tresorier.generated.jooq.main.tables.Account;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record6;
import org.jooq.Row6;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class AccountRecord extends UpdatableRecordImpl<AccountRecord> implements Record6<String, String, String, Boolean, Boolean, String> {

    private static final long serialVersionUID = -942754218;

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
    // Record6 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row6<String, String, String, Boolean, Boolean, String> fieldsRow() {
        return (Row6) super.fieldsRow();
    }

    @Override
    public Row6<String, String, String, Boolean, Boolean, String> valuesRow() {
        return (Row6) super.valuesRow();
    }

    @Override
    public Field<String> field1() {
        return Account.ACCOUNT.ID;
    }

    @Override
    public Field<String> field2() {
        return Account.ACCOUNT.BUDGET_ID;
    }

    @Override
    public Field<String> field3() {
        return Account.ACCOUNT.NAME;
    }

    @Override
    public Field<Boolean> field4() {
        return Account.ACCOUNT.ARCHIVED;
    }

    @Override
    public Field<Boolean> field5() {
        return Account.ACCOUNT.DELETED;
    }

    @Override
    public Field<String> field6() {
        return Account.ACCOUNT.BANK_ACCOUNT_ID;
    }

    @Override
    public String component1() {
        return getId();
    }

    @Override
    public String component2() {
        return getBudgetId();
    }

    @Override
    public String component3() {
        return getName();
    }

    @Override
    public Boolean component4() {
        return getArchived();
    }

    @Override
    public Boolean component5() {
        return getDeleted();
    }

    @Override
    public String component6() {
        return getBankAccountId();
    }

    @Override
    public String value1() {
        return getId();
    }

    @Override
    public String value2() {
        return getBudgetId();
    }

    @Override
    public String value3() {
        return getName();
    }

    @Override
    public Boolean value4() {
        return getArchived();
    }

    @Override
    public Boolean value5() {
        return getDeleted();
    }

    @Override
    public String value6() {
        return getBankAccountId();
    }

    @Override
    public AccountRecord value1(String value) {
        setId(value);
        return this;
    }

    @Override
    public AccountRecord value2(String value) {
        setBudgetId(value);
        return this;
    }

    @Override
    public AccountRecord value3(String value) {
        setName(value);
        return this;
    }

    @Override
    public AccountRecord value4(Boolean value) {
        setArchived(value);
        return this;
    }

    @Override
    public AccountRecord value5(Boolean value) {
        setDeleted(value);
        return this;
    }

    @Override
    public AccountRecord value6(String value) {
        setBankAccountId(value);
        return this;
    }

    @Override
    public AccountRecord values(String value1, String value2, String value3, Boolean value4, Boolean value5, String value6) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        return this;
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

        set(0, id);
        set(1, budgetId);
        set(2, name);
        set(3, archived);
        set(4, deleted);
        set(5, bankAccountId);
    }
}