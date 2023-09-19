/*
 * This file is generated by jOOQ.
 */
package open.tresorier.generated.jooq.test.information_schema.tables.records;


import open.tresorier.generated.jooq.test.information_schema.tables.SessionState;

import org.jooq.Field;
import org.jooq.Record2;
import org.jooq.Row2;
import org.jooq.impl.TableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class SessionStateRecord extends TableRecordImpl<SessionStateRecord> implements Record2<String, String> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>INFORMATION_SCHEMA.SESSION_STATE.STATE_KEY</code>.
     */
    public SessionStateRecord setStateKey(String value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>INFORMATION_SCHEMA.SESSION_STATE.STATE_KEY</code>.
     */
    public String getStateKey() {
        return (String) get(0);
    }

    /**
     * Setter for <code>INFORMATION_SCHEMA.SESSION_STATE.STATE_COMMAND</code>.
     */
    public SessionStateRecord setStateCommand(String value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>INFORMATION_SCHEMA.SESSION_STATE.STATE_COMMAND</code>.
     */
    public String getStateCommand() {
        return (String) get(1);
    }

    // -------------------------------------------------------------------------
    // Record2 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row2<String, String> fieldsRow() {
        return (Row2) super.fieldsRow();
    }

    @Override
    public Row2<String, String> valuesRow() {
        return (Row2) super.valuesRow();
    }

    @Override
    public Field<String> field1() {
        return SessionState.SESSION_STATE.STATE_KEY;
    }

    @Override
    public Field<String> field2() {
        return SessionState.SESSION_STATE.STATE_COMMAND;
    }

    @Override
    public String component1() {
        return getStateKey();
    }

    @Override
    public String component2() {
        return getStateCommand();
    }

    @Override
    public String value1() {
        return getStateKey();
    }

    @Override
    public String value2() {
        return getStateCommand();
    }

    @Override
    public SessionStateRecord value1(String value) {
        setStateKey(value);
        return this;
    }

    @Override
    public SessionStateRecord value2(String value) {
        setStateCommand(value);
        return this;
    }

    @Override
    public SessionStateRecord values(String value1, String value2) {
        value1(value1);
        value2(value2);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached SessionStateRecord
     */
    public SessionStateRecord() {
        super(SessionState.SESSION_STATE);
    }

    /**
     * Create a detached, initialised SessionStateRecord
     */
    public SessionStateRecord(String stateKey, String stateCommand) {
        super(SessionState.SESSION_STATE);

        setStateKey(stateKey);
        setStateCommand(stateCommand);
        resetChangedOnNotNull();
    }

    /**
     * Create a detached, initialised SessionStateRecord
     */
    public SessionStateRecord(open.tresorier.generated.jooq.test.information_schema.tables.pojos.SessionState value) {
        super(SessionState.SESSION_STATE);

        if (value != null) {
            setStateKey(value.getStateKey());
            setStateCommand(value.getStateCommand());
            resetChangedOnNotNull();
        }
    }
}
