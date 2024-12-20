/*
 * This file is generated by jOOQ.
 */
package open.tresorier.generated.jooq.test.tables;


import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import open.tresorier.generated.jooq.test.Keys;
import open.tresorier.generated.jooq.test.Public;
import open.tresorier.generated.jooq.test.tables.BankAccount.BankAccountPath;
import open.tresorier.generated.jooq.test.tables.Budget.BudgetPath;
import open.tresorier.generated.jooq.test.tables.records.BankAgreementRecord;

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
public class BankAgreement extends TableImpl<BankAgreementRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.bank_agreement</code>
     */
    public static final BankAgreement BANK_AGREEMENT = new BankAgreement();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<BankAgreementRecord> getRecordType() {
        return BankAgreementRecord.class;
    }

    /**
     * The column <code>public.bank_agreement.id</code>.
     */
    public final TableField<BankAgreementRecord, String> ID = createField(DSL.name("id"), SQLDataType.VARCHAR(36).nullable(false), this, "");

    /**
     * The column <code>public.bank_agreement.budget_id</code>.
     */
    public final TableField<BankAgreementRecord, String> BUDGET_ID = createField(DSL.name("budget_id"), SQLDataType.VARCHAR(36).nullable(false), this, "");

    /**
     * The column <code>public.bank_agreement.bank_id</code>.
     */
    public final TableField<BankAgreementRecord, String> BANK_ID = createField(DSL.name("bank_id"), SQLDataType.VARCHAR(100).nullable(false), this, "");

    /**
     * The column <code>public.bank_agreement.timestamp</code>.
     */
    public final TableField<BankAgreementRecord, Long> TIMESTAMP = createField(DSL.name("timestamp"), SQLDataType.BIGINT.defaultValue(DSL.field(DSL.raw("0"), SQLDataType.BIGINT)), this, "");

    /**
     * The column <code>public.bank_agreement.nordigen_requisition_id</code>.
     */
    public final TableField<BankAgreementRecord, String> NORDIGEN_REQUISITION_ID = createField(DSL.name("nordigen_requisition_id"), SQLDataType.VARCHAR(36).defaultValue(DSL.field(DSL.raw("NULL::character varying"), SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>public.bank_agreement.archived</code>.
     */
    public final TableField<BankAgreementRecord, Boolean> ARCHIVED = createField(DSL.name("archived"), SQLDataType.BOOLEAN.defaultValue(DSL.field(DSL.raw("false"), SQLDataType.BOOLEAN)), this, "");

    /**
     * The column <code>public.bank_agreement.deleted</code>.
     */
    public final TableField<BankAgreementRecord, Boolean> DELETED = createField(DSL.name("deleted"), SQLDataType.BOOLEAN.defaultValue(DSL.field(DSL.raw("false"), SQLDataType.BOOLEAN)), this, "");

    private BankAgreement(Name alias, Table<BankAgreementRecord> aliased) {
        this(alias, aliased, (Field<?>[]) null, null);
    }

    private BankAgreement(Name alias, Table<BankAgreementRecord> aliased, Field<?>[] parameters, Condition where) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table(), where);
    }

    /**
     * Create an aliased <code>public.bank_agreement</code> table reference
     */
    public BankAgreement(String alias) {
        this(DSL.name(alias), BANK_AGREEMENT);
    }

    /**
     * Create an aliased <code>public.bank_agreement</code> table reference
     */
    public BankAgreement(Name alias) {
        this(alias, BANK_AGREEMENT);
    }

    /**
     * Create a <code>public.bank_agreement</code> table reference
     */
    public BankAgreement() {
        this(DSL.name("bank_agreement"), null);
    }

    public <O extends Record> BankAgreement(Table<O> path, ForeignKey<O, BankAgreementRecord> childPath, InverseForeignKey<O, BankAgreementRecord> parentPath) {
        super(path, childPath, parentPath, BANK_AGREEMENT);
    }

    /**
     * A subtype implementing {@link Path} for simplified path-based joins.
     */
    public static class BankAgreementPath extends BankAgreement implements Path<BankAgreementRecord> {

        private static final long serialVersionUID = 1L;
        public <O extends Record> BankAgreementPath(Table<O> path, ForeignKey<O, BankAgreementRecord> childPath, InverseForeignKey<O, BankAgreementRecord> parentPath) {
            super(path, childPath, parentPath);
        }
        private BankAgreementPath(Name alias, Table<BankAgreementRecord> aliased) {
            super(alias, aliased);
        }

        @Override
        public BankAgreementPath as(String alias) {
            return new BankAgreementPath(DSL.name(alias), this);
        }

        @Override
        public BankAgreementPath as(Name alias) {
            return new BankAgreementPath(alias, this);
        }

        @Override
        public BankAgreementPath as(Table<?> alias) {
            return new BankAgreementPath(alias.getQualifiedName(), this);
        }
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Public.PUBLIC;
    }

    @Override
    public UniqueKey<BankAgreementRecord> getPrimaryKey() {
        return Keys.BANK_AGREEMENT_PKEY;
    }

    @Override
    public List<ForeignKey<BankAgreementRecord, ?>> getReferences() {
        return Arrays.asList(Keys.BANK_AGREEMENT__BANK_AGREEMENT_BUDGET_ID_FKEY);
    }

    private transient BudgetPath _budget;

    /**
     * Get the implicit join path to the <code>public.budget</code> table.
     */
    public BudgetPath budget() {
        if (_budget == null)
            _budget = new BudgetPath(this, Keys.BANK_AGREEMENT__BANK_AGREEMENT_BUDGET_ID_FKEY, null);

        return _budget;
    }

    private transient BankAccountPath _bankAccount;

    /**
     * Get the implicit to-many join path to the
     * <code>public.bank_account</code> table
     */
    public BankAccountPath bankAccount() {
        if (_bankAccount == null)
            _bankAccount = new BankAccountPath(this, null, Keys.BANK_ACCOUNT__BANK_ACCOUNT_AGREEMENT_ID_FKEY.getInverseKey());

        return _bankAccount;
    }

    @Override
    public BankAgreement as(String alias) {
        return new BankAgreement(DSL.name(alias), this);
    }

    @Override
    public BankAgreement as(Name alias) {
        return new BankAgreement(alias, this);
    }

    @Override
    public BankAgreement as(Table<?> alias) {
        return new BankAgreement(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public BankAgreement rename(String name) {
        return new BankAgreement(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public BankAgreement rename(Name name) {
        return new BankAgreement(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public BankAgreement rename(Table<?> name) {
        return new BankAgreement(name.getQualifiedName(), null);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public BankAgreement where(Condition condition) {
        return new BankAgreement(getQualifiedName(), aliased() ? this : null, null, condition);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public BankAgreement where(Collection<? extends Condition> conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public BankAgreement where(Condition... conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public BankAgreement where(Field<Boolean> condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public BankAgreement where(SQL condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public BankAgreement where(@Stringly.SQL String condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public BankAgreement where(@Stringly.SQL String condition, Object... binds) {
        return where(DSL.condition(condition, binds));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public BankAgreement where(@Stringly.SQL String condition, QueryPart... parts) {
        return where(DSL.condition(condition, parts));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public BankAgreement whereExists(Select<?> select) {
        return where(DSL.exists(select));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public BankAgreement whereNotExists(Select<?> select) {
        return where(DSL.notExists(select));
    }
}
