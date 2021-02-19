/*
 * This file is generated by jOOQ.
 */
package open.tresorier.generated.jooq.main.tables;


import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import open.tresorier.generated.jooq.main.Keys;
import open.tresorier.generated.jooq.main.Public;
import open.tresorier.generated.jooq.main.tables.records.OperationRecord;

import org.jooq.Check;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row7;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Operation extends TableImpl<OperationRecord> {

    private static final long serialVersionUID = -870183785;

    /**
     * The reference instance of <code>public.operation</code>
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
     * The column <code>public.operation.id</code>.
     */
    public final TableField<OperationRecord, String> ID = createField(DSL.name("id"), org.jooq.impl.SQLDataType.VARCHAR(36).nullable(false), this, "");

    /**
     * The column <code>public.operation.operation_date</code>.
     */
    public final TableField<OperationRecord, Long> OPERATION_DATE = createField(DSL.name("operation_date"), org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>public.operation.account_id</code>.
     */
    public final TableField<OperationRecord, String> ACCOUNT_ID = createField(DSL.name("account_id"), org.jooq.impl.SQLDataType.VARCHAR(36).nullable(false), this, "");

    /**
     * The column <code>public.operation.category_id</code>.
     */
    public final TableField<OperationRecord, String> CATEGORY_ID = createField(DSL.name("category_id"), org.jooq.impl.SQLDataType.VARCHAR(36).nullable(false), this, "");

    /**
     * The column <code>public.operation.inflow</code>.
     */
    public final TableField<OperationRecord, BigDecimal> INFLOW = createField(DSL.name("inflow"), org.jooq.impl.SQLDataType.NUMERIC(10, 2), this, "");

    /**
     * The column <code>public.operation.outflow</code>.
     */
    public final TableField<OperationRecord, BigDecimal> OUTFLOW = createField(DSL.name("outflow"), org.jooq.impl.SQLDataType.NUMERIC(10, 2), this, "");

    /**
     * The column <code>public.operation.memo</code>.
     */
    public final TableField<OperationRecord, String> MEMO = createField(DSL.name("memo"), org.jooq.impl.SQLDataType.VARCHAR(280).nullable(false), this, "");

    /**
     * Create a <code>public.operation</code> table reference
     */
    public Operation() {
        this(DSL.name("operation"), null);
    }

    /**
     * Create an aliased <code>public.operation</code> table reference
     */
    public Operation(String alias) {
        this(DSL.name(alias), OPERATION);
    }

    /**
     * Create an aliased <code>public.operation</code> table reference
     */
    public Operation(Name alias) {
        this(alias, OPERATION);
    }

    private Operation(Name alias, Table<OperationRecord> aliased) {
        this(alias, aliased, null);
    }

    private Operation(Name alias, Table<OperationRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    public <O extends Record> Operation(Table<O> child, ForeignKey<O, OperationRecord> key) {
        super(child, key, OPERATION);
    }

    @Override
    public Schema getSchema() {
        return Public.PUBLIC;
    }

    @Override
    public UniqueKey<OperationRecord> getPrimaryKey() {
        return Keys.OPERATION_PKEY;
    }

    @Override
    public List<UniqueKey<OperationRecord>> getKeys() {
        return Arrays.<UniqueKey<OperationRecord>>asList(Keys.OPERATION_PKEY);
    }

    @Override
    public List<ForeignKey<OperationRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<OperationRecord, ?>>asList(Keys.OPERATION__OPERATION_ACCOUNT_ID_FKEY, Keys.OPERATION__OPERATION_CATEGORY_ID_FKEY);
    }

    public Account account() {
        return new Account(this, Keys.OPERATION__OPERATION_ACCOUNT_ID_FKEY);
    }

    public Category category() {
        return new Category(this, Keys.OPERATION__OPERATION_CATEGORY_ID_FKEY);
    }

    @Override
    public List<Check<OperationRecord>> getChecks() {
        return Arrays.<Check<OperationRecord>>asList(
              Internal.createCheck(this, DSL.name("category_and_account_are_from_same_budget"), "(are_category_and_account_from_same_budget(category_id, account_id))", true)
            , Internal.createCheck(this, DSL.name("either_inflow_or_outflow_is_null_but_not_both"), "(is_either_inflow_or_outflow_null_but_not_both(inflow, outflow))", true)
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

    // -------------------------------------------------------------------------
    // Row7 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row7<String, Long, String, String, BigDecimal, BigDecimal, String> fieldsRow() {
        return (Row7) super.fieldsRow();
    }
}