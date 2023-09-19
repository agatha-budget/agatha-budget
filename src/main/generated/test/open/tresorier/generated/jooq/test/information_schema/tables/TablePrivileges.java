/*
 * This file is generated by jOOQ.
 */
package open.tresorier.generated.jooq.test.information_schema.tables;


import java.util.function.Function;

import open.tresorier.generated.jooq.test.information_schema.InformationSchema;
import open.tresorier.generated.jooq.test.information_schema.tables.records.TablePrivilegesRecord;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Function8;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Records;
import org.jooq.Row8;
import org.jooq.Schema;
import org.jooq.SelectField;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TablePrivileges extends TableImpl<TablePrivilegesRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of
     * <code>INFORMATION_SCHEMA.TABLE_PRIVILEGES</code>
     */
    public static final TablePrivileges TABLE_PRIVILEGES = new TablePrivileges();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<TablePrivilegesRecord> getRecordType() {
        return TablePrivilegesRecord.class;
    }

    /**
     * The column <code>INFORMATION_SCHEMA.TABLE_PRIVILEGES.GRANTOR</code>.
     */
    public final TableField<TablePrivilegesRecord, String> GRANTOR = createField(DSL.name("GRANTOR"), SQLDataType.VARCHAR(1000000000), this, "");

    /**
     * The column <code>INFORMATION_SCHEMA.TABLE_PRIVILEGES.GRANTEE</code>.
     */
    public final TableField<TablePrivilegesRecord, String> GRANTEE = createField(DSL.name("GRANTEE"), SQLDataType.VARCHAR(1000000000), this, "");

    /**
     * The column
     * <code>INFORMATION_SCHEMA.TABLE_PRIVILEGES.TABLE_CATALOG</code>.
     */
    public final TableField<TablePrivilegesRecord, String> TABLE_CATALOG = createField(DSL.name("TABLE_CATALOG"), SQLDataType.VARCHAR(1000000000), this, "");

    /**
     * The column <code>INFORMATION_SCHEMA.TABLE_PRIVILEGES.TABLE_SCHEMA</code>.
     */
    public final TableField<TablePrivilegesRecord, String> TABLE_SCHEMA = createField(DSL.name("TABLE_SCHEMA"), SQLDataType.VARCHAR(1000000000), this, "");

    /**
     * The column <code>INFORMATION_SCHEMA.TABLE_PRIVILEGES.TABLE_NAME</code>.
     */
    public final TableField<TablePrivilegesRecord, String> TABLE_NAME = createField(DSL.name("TABLE_NAME"), SQLDataType.VARCHAR(1000000000), this, "");

    /**
     * The column
     * <code>INFORMATION_SCHEMA.TABLE_PRIVILEGES.PRIVILEGE_TYPE</code>.
     */
    public final TableField<TablePrivilegesRecord, String> PRIVILEGE_TYPE = createField(DSL.name("PRIVILEGE_TYPE"), SQLDataType.VARCHAR(1000000000), this, "");

    /**
     * The column <code>INFORMATION_SCHEMA.TABLE_PRIVILEGES.IS_GRANTABLE</code>.
     */
    public final TableField<TablePrivilegesRecord, String> IS_GRANTABLE = createField(DSL.name("IS_GRANTABLE"), SQLDataType.VARCHAR(1000000000), this, "");

    /**
     * The column
     * <code>INFORMATION_SCHEMA.TABLE_PRIVILEGES.WITH_HIERARCHY</code>.
     */
    public final TableField<TablePrivilegesRecord, String> WITH_HIERARCHY = createField(DSL.name("WITH_HIERARCHY"), SQLDataType.VARCHAR(1000000000), this, "");

    private TablePrivileges(Name alias, Table<TablePrivilegesRecord> aliased) {
        this(alias, aliased, null);
    }

    private TablePrivileges(Name alias, Table<TablePrivilegesRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.view());
    }

    /**
     * Create an aliased <code>INFORMATION_SCHEMA.TABLE_PRIVILEGES</code> table
     * reference
     */
    public TablePrivileges(String alias) {
        this(DSL.name(alias), TABLE_PRIVILEGES);
    }

    /**
     * Create an aliased <code>INFORMATION_SCHEMA.TABLE_PRIVILEGES</code> table
     * reference
     */
    public TablePrivileges(Name alias) {
        this(alias, TABLE_PRIVILEGES);
    }

    /**
     * Create a <code>INFORMATION_SCHEMA.TABLE_PRIVILEGES</code> table reference
     */
    public TablePrivileges() {
        this(DSL.name("TABLE_PRIVILEGES"), null);
    }

    public <O extends Record> TablePrivileges(Table<O> child, ForeignKey<O, TablePrivilegesRecord> key) {
        super(child, key, TABLE_PRIVILEGES);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : InformationSchema.INFORMATION_SCHEMA;
    }

    @Override
    public TablePrivileges as(String alias) {
        return new TablePrivileges(DSL.name(alias), this);
    }

    @Override
    public TablePrivileges as(Name alias) {
        return new TablePrivileges(alias, this);
    }

    @Override
    public TablePrivileges as(Table<?> alias) {
        return new TablePrivileges(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public TablePrivileges rename(String name) {
        return new TablePrivileges(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public TablePrivileges rename(Name name) {
        return new TablePrivileges(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public TablePrivileges rename(Table<?> name) {
        return new TablePrivileges(name.getQualifiedName(), null);
    }

    // -------------------------------------------------------------------------
    // Row8 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row8<String, String, String, String, String, String, String, String> fieldsRow() {
        return (Row8) super.fieldsRow();
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Function)}.
     */
    public <U> SelectField<U> mapping(Function8<? super String, ? super String, ? super String, ? super String, ? super String, ? super String, ? super String, ? super String, ? extends U> from) {
        return convertFrom(Records.mapping(from));
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Class,
     * Function)}.
     */
    public <U> SelectField<U> mapping(Class<U> toType, Function8<? super String, ? super String, ? super String, ? super String, ? super String, ? super String, ? super String, ? super String, ? extends U> from) {
        return convertFrom(toType, Records.mapping(from));
    }
}
