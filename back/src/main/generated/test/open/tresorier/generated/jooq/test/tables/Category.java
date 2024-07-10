/*
 * This file is generated by jOOQ.
 */
package open.tresorier.generated.jooq.test.tables;


import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import open.tresorier.generated.jooq.test.Keys;
import open.tresorier.generated.jooq.test.Public;
import open.tresorier.generated.jooq.test.tables.Allocation.AllocationPath;
import open.tresorier.generated.jooq.test.tables.MasterCategory.MasterCategoryPath;
import open.tresorier.generated.jooq.test.tables.Operation.OperationPath;
import open.tresorier.generated.jooq.test.tables.records.CategoryRecord;

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
public class Category extends TableImpl<CategoryRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.category</code>
     */
    public static final Category CATEGORY = new Category();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<CategoryRecord> getRecordType() {
        return CategoryRecord.class;
    }

    /**
     * The column <code>public.category.id</code>.
     */
    public final TableField<CategoryRecord, String> ID = createField(DSL.name("id"), SQLDataType.VARCHAR(36).nullable(false), this, "");

    /**
     * The column <code>public.category.master_category_id</code>.
     */
    public final TableField<CategoryRecord, String> MASTER_CATEGORY_ID = createField(DSL.name("master_category_id"), SQLDataType.VARCHAR(36).defaultValue(DSL.field(DSL.raw("NULL::character varying"), SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>public.category.name</code>.
     */
    public final TableField<CategoryRecord, String> NAME = createField(DSL.name("name"), SQLDataType.VARCHAR(100).nullable(false), this, "");

    /**
     * The column <code>public.category.archived</code>.
     */
    public final TableField<CategoryRecord, Boolean> ARCHIVED = createField(DSL.name("archived"), SQLDataType.BOOLEAN.defaultValue(DSL.field(DSL.raw("false"), SQLDataType.BOOLEAN)), this, "");

    /**
     * The column <code>public.category.deleted</code>.
     */
    public final TableField<CategoryRecord, Boolean> DELETED = createField(DSL.name("deleted"), SQLDataType.BOOLEAN.defaultValue(DSL.field(DSL.raw("false"), SQLDataType.BOOLEAN)), this, "");

    private Category(Name alias, Table<CategoryRecord> aliased) {
        this(alias, aliased, (Field<?>[]) null, null);
    }

    private Category(Name alias, Table<CategoryRecord> aliased, Field<?>[] parameters, Condition where) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table(), where);
    }

    /**
     * Create an aliased <code>public.category</code> table reference
     */
    public Category(String alias) {
        this(DSL.name(alias), CATEGORY);
    }

    /**
     * Create an aliased <code>public.category</code> table reference
     */
    public Category(Name alias) {
        this(alias, CATEGORY);
    }

    /**
     * Create a <code>public.category</code> table reference
     */
    public Category() {
        this(DSL.name("category"), null);
    }

    public <O extends Record> Category(Table<O> path, ForeignKey<O, CategoryRecord> childPath, InverseForeignKey<O, CategoryRecord> parentPath) {
        super(path, childPath, parentPath, CATEGORY);
    }

    /**
     * A subtype implementing {@link Path} for simplified path-based joins.
     */
    public static class CategoryPath extends Category implements Path<CategoryRecord> {

        private static final long serialVersionUID = 1L;
        public <O extends Record> CategoryPath(Table<O> path, ForeignKey<O, CategoryRecord> childPath, InverseForeignKey<O, CategoryRecord> parentPath) {
            super(path, childPath, parentPath);
        }
        private CategoryPath(Name alias, Table<CategoryRecord> aliased) {
            super(alias, aliased);
        }

        @Override
        public CategoryPath as(String alias) {
            return new CategoryPath(DSL.name(alias), this);
        }

        @Override
        public CategoryPath as(Name alias) {
            return new CategoryPath(alias, this);
        }

        @Override
        public CategoryPath as(Table<?> alias) {
            return new CategoryPath(alias.getQualifiedName(), this);
        }
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Public.PUBLIC;
    }

    @Override
    public UniqueKey<CategoryRecord> getPrimaryKey() {
        return Keys.CATEGORY_PKEY;
    }

    @Override
    public List<ForeignKey<CategoryRecord, ?>> getReferences() {
        return Arrays.asList(Keys.CATEGORY__CATEGORY_MASTER_CATEGORY_ID_FKEY);
    }

    private transient MasterCategoryPath _masterCategory;

    /**
     * Get the implicit join path to the <code>public.master_category</code>
     * table.
     */
    public MasterCategoryPath masterCategory() {
        if (_masterCategory == null)
            _masterCategory = new MasterCategoryPath(this, Keys.CATEGORY__CATEGORY_MASTER_CATEGORY_ID_FKEY, null);

        return _masterCategory;
    }

    private transient AllocationPath _allocation;

    /**
     * Get the implicit to-many join path to the <code>public.allocation</code>
     * table
     */
    public AllocationPath allocation() {
        if (_allocation == null)
            _allocation = new AllocationPath(this, null, Keys.ALLOCATION__ALLOCATION_CATEGORY_ID_FKEY.getInverseKey());

        return _allocation;
    }

    private transient OperationPath _operation;

    /**
     * Get the implicit to-many join path to the <code>public.operation</code>
     * table
     */
    public OperationPath operation() {
        if (_operation == null)
            _operation = new OperationPath(this, null, Keys.OPERATION__OPERATION_CATEGORY_ID_FKEY.getInverseKey());

        return _operation;
    }

    @Override
    public Category as(String alias) {
        return new Category(DSL.name(alias), this);
    }

    @Override
    public Category as(Name alias) {
        return new Category(alias, this);
    }

    @Override
    public Category as(Table<?> alias) {
        return new Category(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public Category rename(String name) {
        return new Category(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Category rename(Name name) {
        return new Category(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public Category rename(Table<?> name) {
        return new Category(name.getQualifiedName(), null);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Category where(Condition condition) {
        return new Category(getQualifiedName(), aliased() ? this : null, null, condition);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Category where(Collection<? extends Condition> conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Category where(Condition... conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Category where(Field<Boolean> condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public Category where(SQL condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public Category where(@Stringly.SQL String condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public Category where(@Stringly.SQL String condition, Object... binds) {
        return where(DSL.condition(condition, binds));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public Category where(@Stringly.SQL String condition, QueryPart... parts) {
        return where(DSL.condition(condition, parts));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Category whereExists(Select<?> select) {
        return where(DSL.exists(select));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Category whereNotExists(Select<?> select) {
        return where(DSL.notExists(select));
    }
}