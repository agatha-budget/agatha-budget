/*
 * This file is generated by jOOQ.
 */
package open.tresorier.generated.jooq.test.tables;


import java.util.Collection;

import open.tresorier.generated.jooq.test.Keys;
import open.tresorier.generated.jooq.test.Public;
import open.tresorier.generated.jooq.test.tables.records.UserActivityRecord;

import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.Name;
import org.jooq.PlainSQL;
import org.jooq.QueryPart;
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
public class UserActivity extends TableImpl<UserActivityRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.user_activity</code>
     */
    public static final UserActivity USER_ACTIVITY = new UserActivity();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<UserActivityRecord> getRecordType() {
        return UserActivityRecord.class;
    }

    /**
     * The column <code>public.user_activity.id</code>.
     */
    public final TableField<UserActivityRecord, String> ID = createField(DSL.name("id"), SQLDataType.VARCHAR(36).nullable(false), this, "");

    /**
     * The column <code>public.user_activity.user_id</code>.
     */
    public final TableField<UserActivityRecord, String> USER_ID = createField(DSL.name("user_id"), SQLDataType.VARCHAR(36).nullable(false), this, "");

    /**
     * The column <code>public.user_activity.date</code>.
     */
    public final TableField<UserActivityRecord, Long> DATE = createField(DSL.name("date"), SQLDataType.BIGINT.defaultValue(DSL.field(DSL.raw("0"), SQLDataType.BIGINT)), this, "");

    /**
     * The column <code>public.user_activity.action</code>.
     */
    public final TableField<UserActivityRecord, String> ACTION = createField(DSL.name("action"), SQLDataType.VARCHAR(36).nullable(false).defaultValue(DSL.field(DSL.raw("'ACTION_LOGIN'::character varying"), SQLDataType.VARCHAR)), this, "");

    private UserActivity(Name alias, Table<UserActivityRecord> aliased) {
        this(alias, aliased, (Field<?>[]) null, null);
    }

    private UserActivity(Name alias, Table<UserActivityRecord> aliased, Field<?>[] parameters, Condition where) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table(), where);
    }

    /**
     * Create an aliased <code>public.user_activity</code> table reference
     */
    public UserActivity(String alias) {
        this(DSL.name(alias), USER_ACTIVITY);
    }

    /**
     * Create an aliased <code>public.user_activity</code> table reference
     */
    public UserActivity(Name alias) {
        this(alias, USER_ACTIVITY);
    }

    /**
     * Create a <code>public.user_activity</code> table reference
     */
    public UserActivity() {
        this(DSL.name("user_activity"), null);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Public.PUBLIC;
    }

    @Override
    public UniqueKey<UserActivityRecord> getPrimaryKey() {
        return Keys.USER_ACTIVITY_PKEY;
    }

    @Override
    public UserActivity as(String alias) {
        return new UserActivity(DSL.name(alias), this);
    }

    @Override
    public UserActivity as(Name alias) {
        return new UserActivity(alias, this);
    }

    @Override
    public UserActivity as(Table<?> alias) {
        return new UserActivity(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public UserActivity rename(String name) {
        return new UserActivity(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public UserActivity rename(Name name) {
        return new UserActivity(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public UserActivity rename(Table<?> name) {
        return new UserActivity(name.getQualifiedName(), null);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public UserActivity where(Condition condition) {
        return new UserActivity(getQualifiedName(), aliased() ? this : null, null, condition);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public UserActivity where(Collection<? extends Condition> conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public UserActivity where(Condition... conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public UserActivity where(Field<Boolean> condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public UserActivity where(SQL condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public UserActivity where(@Stringly.SQL String condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public UserActivity where(@Stringly.SQL String condition, Object... binds) {
        return where(DSL.condition(condition, binds));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public UserActivity where(@Stringly.SQL String condition, QueryPart... parts) {
        return where(DSL.condition(condition, parts));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public UserActivity whereExists(Select<?> select) {
        return where(DSL.exists(select));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public UserActivity whereNotExists(Select<?> select) {
        return where(DSL.notExists(select));
    }
}
