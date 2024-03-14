/*
 * This file is generated by jOOQ.
 */
package open.tresorier.generated.jooq.test.public_.tables;


import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import open.tresorier.generated.jooq.test.public_.Keys;
import open.tresorier.generated.jooq.test.public_.Public;
import open.tresorier.generated.jooq.test.public_.tables.Account.AccountPath;
import open.tresorier.generated.jooq.test.public_.tables.Category.CategoryPath;
import open.tresorier.generated.jooq.test.public_.tables.records.OperationRecord;

import org.jooq.Check;
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
import org.jooq.impl.Internal;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Operation extends TableImpl<OperationRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>PUBLIC.OPERATION</code>
     */
    public static final Operation OPERATION = new Operation();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<OperationRecord> getRecordType() {
        return OperationRecord.class;
    }

    /**
     * The column <code>PUBLIC.OPERATION.ID</code>.
     */
    public final TableField<OperationRecord, String> ID = createField(DSL.name("ID"), SQLDataType.VARCHAR(36).nullable(false), this, "");

    /**
     * The column <code>PUBLIC.OPERATION.ACCOUNT_ID</code>.
     */
    public final TableField<OperationRecord, String> ACCOUNT_ID = createField(DSL.name("ACCOUNT_ID"), SQLDataType.VARCHAR(36).nullable(false), this, "");

    /**
     * The column <code>PUBLIC.OPERATION.DATE_MONTH</code>.
     */
    public final TableField<OperationRecord, Integer> DATE_MONTH = createField(DSL.name("DATE_MONTH"), SQLDataType.INTEGER.defaultValue(DSL.field(DSL.raw("NULL"), SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>PUBLIC.OPERATION.DATE_DAY</code>.
     */
    public final TableField<OperationRecord, Integer> DATE_DAY = createField(DSL.name("DATE_DAY"), SQLDataType.INTEGER.defaultValue(DSL.field(DSL.raw("NULL"), SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>PUBLIC.OPERATION.CATEGORY_ID</code>.
     */
    public final TableField<OperationRecord, String> CATEGORY_ID = createField(DSL.name("CATEGORY_ID"), SQLDataType.VARCHAR(36).defaultValue(DSL.field(DSL.raw("NULL"), SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>PUBLIC.OPERATION.MEMO</code>.
     */
    public final TableField<OperationRecord, String> MEMO = createField(DSL.name("MEMO"), SQLDataType.VARCHAR(280).defaultValue(DSL.field(DSL.raw("NULL"), SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>PUBLIC.OPERATION.AMOUNT</code>.
     */
    public final TableField<OperationRecord, Integer> AMOUNT = createField(DSL.name("AMOUNT"), SQLDataType.INTEGER.nullable(false).defaultValue(DSL.field(DSL.raw("0"), SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>PUBLIC.OPERATION.ORDER_IN_DAY</code>.
     */
    public final TableField<OperationRecord, Long> ORDER_IN_DAY = createField(DSL.name("ORDER_IN_DAY"), SQLDataType.BIGINT.nullable(false).defaultValue(DSL.field(DSL.raw("1"), SQLDataType.BIGINT)), this, "");

    /**
     * The column <code>PUBLIC.OPERATION.PENDING</code>.
     */
    public final TableField<OperationRecord, Boolean> PENDING = createField(DSL.name("PENDING"), SQLDataType.BOOLEAN.defaultValue(DSL.field(DSL.raw("FALSE"), SQLDataType.BOOLEAN)), this, "");

    /**
     * The column <code>PUBLIC.OPERATION.LOCKED</code>.
     */
    public final TableField<OperationRecord, Boolean> LOCKED = createField(DSL.name("LOCKED"), SQLDataType.BOOLEAN.defaultValue(DSL.field(DSL.raw("FALSE"), SQLDataType.BOOLEAN)), this, "");

    /**
     * The column <code>PUBLIC.OPERATION.MOTHER_OPERATION_ID</code>.
     */
    public final TableField<OperationRecord, String> MOTHER_OPERATION_ID = createField(DSL.name("MOTHER_OPERATION_ID"), SQLDataType.VARCHAR(36).defaultValue(DSL.field(DSL.raw("NULL"), SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>PUBLIC.OPERATION.IMPORT_IDENTIFIER</code>.
     */
    public final TableField<OperationRecord, String> IMPORT_IDENTIFIER = createField(DSL.name("IMPORT_IDENTIFIER"), SQLDataType.VARCHAR(150).defaultValue(DSL.field(DSL.raw("NULL"), SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>PUBLIC.OPERATION.IMPORT_TIMESTAMP</code>.
     */
    public final TableField<OperationRecord, Long> IMPORT_TIMESTAMP = createField(DSL.name("IMPORT_TIMESTAMP"), SQLDataType.BIGINT.defaultValue(DSL.field(DSL.raw("NULL"), SQLDataType.BIGINT)), this, "");

    private Operation(Name alias, Table<OperationRecord> aliased) {
        this(alias, aliased, (Field<?>[]) null, null);
    }

    private Operation(Name alias, Table<OperationRecord> aliased, Field<?>[] parameters, Condition where) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table(), where);
    }

    /**
     * Create an aliased <code>PUBLIC.OPERATION</code> table reference
     */
    public Operation(String alias) {
        this(DSL.name(alias), OPERATION);
    }

    /**
     * Create an aliased <code>PUBLIC.OPERATION</code> table reference
     */
    public Operation(Name alias) {
        this(alias, OPERATION);
    }

    /**
     * Create a <code>PUBLIC.OPERATION</code> table reference
     */
    public Operation() {
        this(DSL.name("OPERATION"), null);
    }

    public <O extends Record> Operation(Table<O> path, ForeignKey<O, OperationRecord> childPath, InverseForeignKey<O, OperationRecord> parentPath) {
        super(path, childPath, parentPath, OPERATION);
    }

    /**
     * A subtype implementing {@link Path} for simplified path-based joins.
     */
    public static class OperationPath extends Operation implements Path<OperationRecord> {

        private static final long serialVersionUID = 1L;
        public <O extends Record> OperationPath(Table<O> path, ForeignKey<O, OperationRecord> childPath, InverseForeignKey<O, OperationRecord> parentPath) {
            super(path, childPath, parentPath);
        }
        private OperationPath(Name alias, Table<OperationRecord> aliased) {
            super(alias, aliased);
        }

        @Override
        public OperationPath as(String alias) {
            return new OperationPath(DSL.name(alias), this);
        }

        @Override
        public OperationPath as(Name alias) {
            return new OperationPath(alias, this);
        }

        @Override
        public OperationPath as(Table<?> alias) {
            return new OperationPath(alias.getQualifiedName(), this);
        }
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Public.PUBLIC;
    }

    @Override
    public UniqueKey<OperationRecord> getPrimaryKey() {
        return Keys.CONSTRAINT_9;
    }

    @Override
    public List<UniqueKey<OperationRecord>> getUniqueKeys() {
        return Arrays.asList(Keys.CONSTRAINT_932E);
    }

    @Override
    public List<ForeignKey<OperationRecord, ?>> getReferences() {
        return Arrays.asList(Keys.CONSTRAINT_93, Keys.CONSTRAINT_932);
    }

    private transient AccountPath _account;

    /**
     * Get the implicit join path to the <code>PUBLIC.ACCOUNT</code> table.
     */
    public AccountPath account() {
        if (_account == null)
            _account = new AccountPath(this, Keys.CONSTRAINT_93, null);

        return _account;
    }

    private transient CategoryPath _category;

    /**
     * Get the implicit join path to the <code>PUBLIC.CATEGORY</code> table.
     */
    public CategoryPath category() {
        if (_category == null)
            _category = new CategoryPath(this, Keys.CONSTRAINT_932, null);

        return _category;
    }

    @Override
    public List<Check<OperationRecord>> getChecks() {
        return Arrays.asList(
            Internal.createCheck(this, DSL.name("NO_INVALID_DAY_OPERATION"), "\"DATE_DAY\" < 32", true),
            Internal.createCheck(this, DSL.name("NO_INVALID_MONTH_OPERATION"), "MOD(\"DATE_MONTH\", 100) < 13", true),
            Internal.createCheck(this, DSL.name("NO_NEGATIVE_DAY_OPERATION"), "\"DATE_DAY\" > 0", true),
            Internal.createCheck(this, DSL.name("NO_NEGATIVE_MONTH_OPERATION"), "MOD(\"DATE_MONTH\", 100) > 0", true)
        );
    }

    @Override
    public Operation as(String alias) {
        return new Operation(DSL.name(alias), this);
    }

    @Override
    public Operation as(Name alias) {
        return new Operation(alias, this);
    }

    @Override
    public Operation as(Table<?> alias) {
        return new Operation(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public Operation rename(String name) {
        return new Operation(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Operation rename(Name name) {
        return new Operation(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public Operation rename(Table<?> name) {
        return new Operation(name.getQualifiedName(), null);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Operation where(Condition condition) {
        return new Operation(getQualifiedName(), aliased() ? this : null, null, condition);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Operation where(Collection<? extends Condition> conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Operation where(Condition... conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Operation where(Field<Boolean> condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public Operation where(SQL condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public Operation where(@Stringly.SQL String condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public Operation where(@Stringly.SQL String condition, Object... binds) {
        return where(DSL.condition(condition, binds));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public Operation where(@Stringly.SQL String condition, QueryPart... parts) {
        return where(DSL.condition(condition, parts));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Operation whereExists(Select<?> select) {
        return where(DSL.exists(select));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Operation whereNotExists(Select<?> select) {
        return where(DSL.notExists(select));
    }
}
