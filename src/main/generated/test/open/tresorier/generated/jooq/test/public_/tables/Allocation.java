/*
 * This file is generated by jOOQ.
 */
package open.tresorier.generated.jooq.test.public_.tables;


import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import open.tresorier.generated.jooq.test.public_.Keys;
import open.tresorier.generated.jooq.test.public_.Public;
import open.tresorier.generated.jooq.test.public_.tables.records.AllocationRecord;

import org.jooq.Check;
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
import org.jooq.impl.Internal;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Allocation extends TableImpl<AllocationRecord> {

    private static final long serialVersionUID = 1697463956;

    /**
     * The reference instance of <code>PUBLIC.ALLOCATION</code>
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
     * The column <code>PUBLIC.ALLOCATION.ID</code>.
     */
    public final TableField<AllocationRecord, String> ID = createField(DSL.name("ID"), org.jooq.impl.SQLDataType.VARCHAR(36).nullable(false), this, "");

    /**
     * The column <code>PUBLIC.ALLOCATION.CATEGORY_ID</code>.
     */
    public final TableField<AllocationRecord, String> CATEGORY_ID = createField(DSL.name("CATEGORY_ID"), org.jooq.impl.SQLDataType.VARCHAR(36).nullable(false), this, "");

    /**
     * The column <code>PUBLIC.ALLOCATION.YEAR</code>.
     */
    public final TableField<AllocationRecord, Integer> YEAR = createField(DSL.name("YEAR"), org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>PUBLIC.ALLOCATION.MONTH</code>.
     */
    public final TableField<AllocationRecord, Integer> MONTH = createField(DSL.name("MONTH"), org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>PUBLIC.ALLOCATION.AMOUNT</code>.
     */
    public final TableField<AllocationRecord, BigDecimal> AMOUNT = createField(DSL.name("AMOUNT"), org.jooq.impl.SQLDataType.DECIMAL(10, 2), this, "");

    /**
     * Create a <code>PUBLIC.ALLOCATION</code> table reference
     */
    public Allocation() {
        this(DSL.name("ALLOCATION"), null);
    }

    /**
     * Create an aliased <code>PUBLIC.ALLOCATION</code> table reference
     */
    public Allocation(String alias) {
        this(DSL.name(alias), ALLOCATION);
    }

    /**
     * Create an aliased <code>PUBLIC.ALLOCATION</code> table reference
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
        return Keys.CONSTRAINT_A;
    }

    @Override
    public List<UniqueKey<AllocationRecord>> getKeys() {
        return Arrays.<UniqueKey<AllocationRecord>>asList(Keys.CONSTRAINT_A, Keys.ONLY_ONE_ALLOCATION_PER_MONTH_AND_BUDGET);
    }

    @Override
    public List<ForeignKey<AllocationRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<AllocationRecord, ?>>asList(Keys.CONSTRAINT_AA);
    }

    public Category category() {
        return new Category(this, Keys.CONSTRAINT_AA);
    }

    @Override
    public List<Check<AllocationRecord>> getChecks() {
        return Arrays.<Check<AllocationRecord>>asList(
              Internal.createCheck(this, DSL.name("NO_INVALID_MONTH"), "(\"MONTH\" < 13)", true)
            , Internal.createCheck(this, DSL.name("NO_NEGATIVE_MONTH"), "(\"MONTH\" > 0)", true)
        );
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
