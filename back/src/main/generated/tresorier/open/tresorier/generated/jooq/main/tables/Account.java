/*
 * This file is generated by jOOQ.
 */
package open.tresorier.generated.jooq.main.tables;


import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import open.tresorier.generated.jooq.main.Keys;
import open.tresorier.generated.jooq.main.Public;
import open.tresorier.generated.jooq.main.tables.BankAccount.BankAccountPath;
import open.tresorier.generated.jooq.main.tables.Budget.BudgetPath;
import open.tresorier.generated.jooq.main.tables.Operation.OperationPath;
import open.tresorier.generated.jooq.main.tables.records.AccountRecord;

import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.InverseForeignKey;
import org.jooq.Name;
import org.jooq.Path;
import org.jooq.PlainSQL;
import org.jooq.QueryPart;
import org.jooq.Record;
import org.jooq.SQL;
import org.jooq.Schema;
import org.jooq.Select;
import org.jooq.Stringly;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class Account extends TableImpl<AccountRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.account</code>
     */
    public static final Account ACCOUNT = new Account();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<AccountRecord> getRecordType() {
        return AccountRecord.class;
    }

    /**
     * The column <code>public.account.id</code>.
     */
    public final TableField<AccountRecord, String> ID = createField(DSL.name("id"), SQLDataType.VARCHAR(36).nullable(false), this, "");

    /**
     * The column <code>public.account.budget_id</code>.
     */
    public final TableField<AccountRecord, String> BUDGET_ID = createField(DSL.name("budget_id"), SQLDataType.VARCHAR(36).nullable(false), this, "");

    /**
     * The column <code>public.account.name</code>.
     */
    public final TableField<AccountRecord, String> NAME = createField(DSL.name("name"), SQLDataType.VARCHAR(100).nullable(false), this, "");

    /**
     * The column <code>public.account.archived</code>.
     */
    public final TableField<AccountRecord, Boolean> ARCHIVED = createField(DSL.name("archived"), SQLDataType.BOOLEAN.defaultValue(DSL.field(DSL.raw("false"), SQLDataType.BOOLEAN)), this, "");

    /**
     * The column <code>public.account.deleted</code>.
     */
    public final TableField<AccountRecord, Boolean> DELETED = createField(DSL.name("deleted"), SQLDataType.BOOLEAN.defaultValue(DSL.field(DSL.raw("false"), SQLDataType.BOOLEAN)), this, "");

    /**
     * The column <code>public.account.bank_account_id</code>.
     */
    public final TableField<AccountRecord, String> BANK_ACCOUNT_ID = createField(DSL.name("bank_account_id"), SQLDataType.VARCHAR(36).defaultValue(DSL.field(DSL.raw("NULL::character varying"), SQLDataType.VARCHAR)), this, "");

    private Account(Name alias, Table<AccountRecord> aliased) {
        this(alias, aliased, (Field<?>[]) null, null);
    }

    private Account(Name alias, Table<AccountRecord> aliased, Field<?>[] parameters, Condition where) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table(), where);
    }

    /**
     * Create an aliased <code>public.account</code> table reference
     */
    public Account(String alias) {
        this(DSL.name(alias), ACCOUNT);
    }

    /**
     * Create an aliased <code>public.account</code> table reference
     */
    public Account(Name alias) {
        this(alias, ACCOUNT);
    }

    /**
     * Create a <code>public.account</code> table reference
     */
    public Account() {
        this(DSL.name("account"), null);
    }

    public <O extends Record> Account(Table<O> path, ForeignKey<O, AccountRecord> childPath, InverseForeignKey<O, AccountRecord> parentPath) {
        super(path, childPath, parentPath, ACCOUNT);
    }

    /**
     * A subtype implementing {@link Path} for simplified path-based joins.
     */
    public static class AccountPath extends Account implements Path<AccountRecord> {

        private static final long serialVersionUID = 1L;
        public <O extends Record> AccountPath(Table<O> path, ForeignKey<O, AccountRecord> childPath, InverseForeignKey<O, AccountRecord> parentPath) {
            super(path, childPath, parentPath);
        }
        private AccountPath(Name alias, Table<AccountRecord> aliased) {
            super(alias, aliased);
        }

        @Override
        public AccountPath as(String alias) {
            return new AccountPath(DSL.name(alias), this);
        }

        @Override
        public AccountPath as(Name alias) {
            return new AccountPath(alias, this);
        }

        @Override
        public AccountPath as(Table<?> alias) {
            return new AccountPath(alias.getQualifiedName(), this);
        }
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Public.PUBLIC;
    }

    @Override
    public UniqueKey<AccountRecord> getPrimaryKey() {
        return Keys.ACCOUNT_PKEY;
    }

    @Override
    public List<ForeignKey<AccountRecord, ?>> getReferences() {
        return Arrays.asList(Keys.ACCOUNT__ACCOUNT_BANK_ACCOUNT_ID_FKEY, Keys.ACCOUNT__ACCOUNT_BUDGET_ID_FKEY);
    }

    private transient BankAccountPath _bankAccount;

    /**
     * Get the implicit join path to the <code>public.bank_account</code> table.
     */
    public BankAccountPath bankAccount() {
        if (_bankAccount == null)
            _bankAccount = new BankAccountPath(this, Keys.ACCOUNT__ACCOUNT_BANK_ACCOUNT_ID_FKEY, null);

        return _bankAccount;
    }

    private transient BudgetPath _budget;

    /**
     * Get the implicit join path to the <code>public.budget</code> table.
     */
    public BudgetPath budget() {
        if (_budget == null)
            _budget = new BudgetPath(this, Keys.ACCOUNT__ACCOUNT_BUDGET_ID_FKEY, null);

        return _budget;
    }

    private transient OperationPath _operation;

    /**
     * Get the implicit to-many join path to the <code>public.operation</code>
     * table
     */
    public OperationPath operation() {
        if (_operation == null)
            _operation = new OperationPath(this, null, Keys.OPERATION__OPERATION_ACCOUNT_ID_FKEY.getInverseKey());

        return _operation;
    }

    @Override
    public Account as(String alias) {
        return new Account(DSL.name(alias), this);
    }

    @Override
    public Account as(Name alias) {
        return new Account(alias, this);
    }

    @Override
    public Account as(Table<?> alias) {
        return new Account(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public Account rename(String name) {
        return new Account(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Account rename(Name name) {
        return new Account(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public Account rename(Table<?> name) {
        return new Account(name.getQualifiedName(), null);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Account where(Condition condition) {
        return new Account(getQualifiedName(), aliased() ? this : null, null, condition);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Account where(Collection<? extends Condition> conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Account where(Condition... conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Account where(Field<Boolean> condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public Account where(SQL condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public Account where(@Stringly.SQL String condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public Account where(@Stringly.SQL String condition, Object... binds) {
        return where(DSL.condition(condition, binds));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public Account where(@Stringly.SQL String condition, QueryPart... parts) {
        return where(DSL.condition(condition, parts));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Account whereExists(Select<?> select) {
        return where(DSL.exists(select));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Account whereNotExists(Select<?> select) {
        return where(DSL.notExists(select));
    }
}
