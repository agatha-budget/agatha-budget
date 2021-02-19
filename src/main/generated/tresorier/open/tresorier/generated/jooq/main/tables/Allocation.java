/*
 * This file is generated by jOOQ.
 */
package open.tresorier.generated.jooq.main.tables;


import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import open.tresorier.generated.jooq.main.Keys;
import open.tresorier.generated.jooq.main.Public;
import open.tresorier.generated.jooq.main.tables.records.AllocationRecord;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row5;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Allocation extends TableImpl<AllocationRecord> {

    private static final long serialVersionUID = -1518417204;

    /**
     * The reference instance of <code>public.allocation</code>
     */
    public static final Allocation ALLOCATION = new Allocation();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<AllocationRecord> getRecordType() {
        return AllocationRecord.class;
    }

    /**
     * The column <code>public.allocation.id</code>.
     */
    public final TableField<AllocationRecord, String> ID = createField(DSL.name("id"), org.jooq.impl.SQLDataType.VARCHAR(36).nullable(false), this, "");

    /**
     * The column <code>public.allocation.category_id</code>.
     */
    public final TableField<AllocationRecord, String> CATEGORY_ID = createField(DSL.name("category_id"), org.jooq.impl.SQLDataType.VARCHAR(36).nullable(false), this, "");

    /**
     * The column <code>public.allocation.year</code>.
     */
    public final TableField<AllocationRecord, Integer> YEAR = createField(DSL.name("year"), org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>public.allocation.month</code>.
     */
    public final TableField<AllocationRecord, Integer> MONTH = createField(DSL.name("month"), org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>public.allocation.amount</code>.
     */
    public final TableField<AllocationRecord, BigDecimal> AMOUNT = createField(DSL.name("amount"), org.jooq.impl.SQLDataType.NUMERIC(10, 2), this, "");

    /**
     * Create a <code>public.allocation</code> table reference
     */
    public Allocation() {
        this(DSL.name("allocation"), null);
    }

    /**
     * Create an aliased <code>public.allocation</code> table reference
     */
    public Allocation(String alias) {
        this(DSL.name(alias), ALLOCATION);
    }

    /**
     * Create an aliased <code>public.allocation</code> table reference
     */
    public Allocation(Name alias) {
        this(alias, ALLOCATION);
    }

    private Allocation(Name alias, Table<AllocationRecord> aliased) {
        this(alias, aliased, null);
    }

    private Allocation(Name alias, Table<AllocationRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    public <O extends Record> Allocation(Table<O> child, ForeignKey<O, AllocationRecord> key) {
        super(child, key, ALLOCATION);
    }

    @Override
    public Schema getSchema() {
        return Public.PUBLIC;
    }

    @Override
    public UniqueKey<AllocationRecord> getPrimaryKey() {
        return Keys.ALLOCATION_PKEY;
    }

    @Override
    public List<UniqueKey<AllocationRecord>> getKeys() {
        return Arrays.<UniqueKey<AllocationRecord>>asList(Keys.ALLOCATION_PKEY, Keys.ONLY_ONE_ALLOCATION_PER_MONTH_AND_BUDGET);
    }

    @Override
    public List<ForeignKey<AllocationRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<AllocationRecord, ?>>asList(Keys.ALLOCATION__ALLOCATION_CATEGORY_ID_FKEY);
    }

    public Category category() {
        return new Category(this, Keys.ALLOCATION__ALLOCATION_CATEGORY_ID_FKEY);
    }

    @Override
    public Allocation as(String alias) {
        return new Allocation(DSL.name(alias), this);
    }

    @Override
    public Allocation as(Name alias) {
        return new Allocation(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Allocation rename(String name) {
        return new Allocation(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Allocation rename(Name name) {
        return new Allocation(name, null);
    }

    // -------------------------------------------------------------------------
    // Row5 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row5<String, String, Integer, Integer, BigDecimal> fieldsRow() {
        return (Row5) super.fieldsRow();
    }
}
