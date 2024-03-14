/*
 * This file is generated by jOOQ.
 */
package open.tresorier.generated.jooq.main.tables.records;


import open.tresorier.generated.jooq.main.tables.UserActivity;

import org.jooq.Record1;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class UserActivityRecord extends UpdatableRecordImpl<UserActivityRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>public.user_activity.id</code>.
     */
    public UserActivityRecord setId(String value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>public.user_activity.id</code>.
     */
    public String getId() {
        return (String) get(0);
    }

    /**
     * Setter for <code>public.user_activity.user_id</code>.
     */
    public UserActivityRecord setUserId(String value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>public.user_activity.user_id</code>.
     */
    public String getUserId() {
        return (String) get(1);
    }

    /**
     * Setter for <code>public.user_activity.date</code>.
     */
    public UserActivityRecord setDate(Long value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>public.user_activity.date</code>.
     */
    public Long getDate() {
        return (Long) get(2);
    }

    /**
     * Setter for <code>public.user_activity.action</code>.
     */
    public UserActivityRecord setAction(String value) {
        set(3, value);
        return this;
    }

    /**
     * Getter for <code>public.user_activity.action</code>.
     */
    public String getAction() {
        return (String) get(3);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<String> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached UserActivityRecord
     */
    public UserActivityRecord() {
        super(UserActivity.USER_ACTIVITY);
    }

    /**
     * Create a detached, initialised UserActivityRecord
     */
    public UserActivityRecord(String id, String userId, Long date, String action) {
        super(UserActivity.USER_ACTIVITY);

        setId(id);
        setUserId(userId);
        setDate(date);
        setAction(action);
        resetChangedOnNotNull();
    }

    /**
     * Create a detached, initialised UserActivityRecord
     */
    public UserActivityRecord(open.tresorier.generated.jooq.main.tables.pojos.UserActivity value) {
        super(UserActivity.USER_ACTIVITY);

        if (value != null) {
            setId(value.getId());
            setUserId(value.getUserId());
            setDate(value.getDate());
            setAction(value.getAction());
            resetChangedOnNotNull();
        }
    }
}
