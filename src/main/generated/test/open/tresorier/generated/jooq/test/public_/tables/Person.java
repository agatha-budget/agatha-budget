/*
 * This file is generated by jOOQ.
 */
package open.tresorier.generated.jooq.test.public_.tables;


import java.util.Arrays;
import java.util.List;

import open.tresorier.generated.jooq.test.public_.Keys;
import open.tresorier.generated.jooq.test.public_.Public;
import open.tresorier.generated.jooq.test.public_.tables.records.PersonRecord;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row9;
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
public class Person extends TableImpl<PersonRecord> {

    private static final long serialVersionUID = -907895400;

    /**
     * The reference instance of <code>PUBLIC.PERSON</code>
     */
    public static final Person PERSON = new Person();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<PersonRecord> getRecordType() {
        return PersonRecord.class;
    }

    /**
     * The column <code>PUBLIC.PERSON.ID</code>.
     */
    public final TableField<PersonRecord, String> ID = createField(DSL.name("ID"), org.jooq.impl.SQLDataType.VARCHAR(36).nullable(false), this, "");

    /**
     * The column <code>PUBLIC.PERSON.EMAIL</code>.
     */
    public final TableField<PersonRecord, String> EMAIL = createField(DSL.name("EMAIL"), org.jooq.impl.SQLDataType.VARCHAR(255), this, "");

    /**
     * The column <code>PUBLIC.PERSON.NAME</code>.
     */
    public final TableField<PersonRecord, String> NAME = createField(DSL.name("NAME"), org.jooq.impl.SQLDataType.VARCHAR(255), this, "");

    /**
     * The column <code>PUBLIC.PERSON.PASSWORD</code>.
     */
    public final TableField<PersonRecord, String> PASSWORD = createField(DSL.name("PASSWORD"), org.jooq.impl.SQLDataType.VARCHAR(255), this, "");

    /**
     * The column <code>PUBLIC.PERSON.UNLOCKINGDATE</code>.
     */
    public final TableField<PersonRecord, Long> UNLOCKINGDATE = createField(DSL.name("UNLOCKINGDATE"), org.jooq.impl.SQLDataType.BIGINT.defaultValue(org.jooq.impl.DSL.field("0", org.jooq.impl.SQLDataType.BIGINT)), this, "");

    /**
     * The column <code>PUBLIC.PERSON.LOGINATTEMPTCOUNT</code>.
     */
    public final TableField<PersonRecord, Integer> LOGINATTEMPTCOUNT = createField(DSL.name("LOGINATTEMPTCOUNT"), org.jooq.impl.SQLDataType.INTEGER.defaultValue(org.jooq.impl.DSL.field("0", org.jooq.impl.SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>PUBLIC.PERSON.DELETED</code>.
     */
    public final TableField<PersonRecord, Boolean> DELETED = createField(DSL.name("DELETED"), org.jooq.impl.SQLDataType.BOOLEAN.defaultValue(org.jooq.impl.DSL.field("FALSE", org.jooq.impl.SQLDataType.BOOLEAN)), this, "");

    /**
     * The column <code>PUBLIC.PERSON.BILLING_ID</code>.
     */
    public final TableField<PersonRecord, String> BILLING_ID = createField(DSL.name("BILLING_ID"), org.jooq.impl.SQLDataType.VARCHAR(36).defaultValue(org.jooq.impl.DSL.field("NULL", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>PUBLIC.PERSON.BILLING_STATUS</code>.
     */
    public final TableField<PersonRecord, Boolean> BILLING_STATUS = createField(DSL.name("BILLING_STATUS"), org.jooq.impl.SQLDataType.BOOLEAN.defaultValue(org.jooq.impl.DSL.field("NULL", org.jooq.impl.SQLDataType.BOOLEAN)), this, "");

    /**
     * Create a <code>PUBLIC.PERSON</code> table reference
     */
    public Person() {
        this(DSL.name("PERSON"), null);
    }

    /**
     * Create an aliased <code>PUBLIC.PERSON</code> table reference
     */
    public Person(String alias) {
        this(DSL.name(alias), PERSON);
    }

    /**
     * Create an aliased <code>PUBLIC.PERSON</code> table reference
     */
    public Person(Name alias) {
        this(alias, PERSON);
    }

    private Person(Name alias, Table<PersonRecord> aliased) {
        this(alias, aliased, null);
    }

    private Person(Name alias, Table<PersonRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    public <O extends Record> Person(Table<O> child, ForeignKey<O, PersonRecord> key) {
        super(child, key, PERSON);
    }

    @Override
    public Schema getSchema() {
        return Public.PUBLIC;
    }

    @Override
    public UniqueKey<PersonRecord> getPrimaryKey() {
        return Keys.CONSTRAINT_8;
    }

    @Override
    public List<UniqueKey<PersonRecord>> getKeys() {
        return Arrays.<UniqueKey<PersonRecord>>asList(Keys.CONSTRAINT_8, Keys.CONSTRAINT_8C, Keys.CONSTRAINT_8C7);
    }

    @Override
    public Person as(String alias) {
        return new Person(DSL.name(alias), this);
    }

    @Override
    public Person as(Name alias) {
        return new Person(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Person rename(String name) {
        return new Person(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Person rename(Name name) {
        return new Person(name, null);
    }

    // -------------------------------------------------------------------------
    // Row9 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row9<String, String, String, String, Long, Integer, Boolean, String, Boolean> fieldsRow() {
        return (Row9) super.fieldsRow();
    }
}
