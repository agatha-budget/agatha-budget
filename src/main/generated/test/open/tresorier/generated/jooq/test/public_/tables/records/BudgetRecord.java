/*
 * This file is generated by jOOQ.
 */
package open.tresorier.generated.jooq.test.public_.tables.records;


import open.tresorier.generated.jooq.test.public_.tables.Budget;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record5;
import org.jooq.Row5;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class BudgetRecord extends UpdatableRecordImpl<BudgetRecord> implements Record5<String, String, String, Boolean, String> {

    private static final long serialVersionUID = 880187591;

    /**
     * Setter for <code>PUBLIC.BUDGET.ID</code>.
     */
    public BudgetRecord setId(String value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>PUBLIC.BUDGET.ID</code>.
     */
    public String getId() {
        return (String) get(0);
    }

    /**
     * Setter for <code>PUBLIC.BUDGET.PERSON_ID</code>.
     */
    public BudgetRecord setPersonId(String value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>PUBLIC.BUDGET.PERSON_ID</code>.
     */
    public String getPersonId() {
        return (String) get(1);
    }

    /**
     * Setter for <code>PUBLIC.BUDGET.NAME</code>.
     */
    public BudgetRecord setName(String value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>PUBLIC.BUDGET.NAME</code>.
     */
    public String getName() {
        return (String) get(2);
    }

    /**
     * Setter for <code>PUBLIC.BUDGET.DELETED</code>.
     */
    public BudgetRecord setDeleted(Boolean value) {
        set(3, value);
        return this;
    }

    /**
     * Getter for <code>PUBLIC.BUDGET.DELETED</code>.
     */
    public Boolean getDeleted() {
        return (Boolean) get(3);
    }

    /**
     * Setter for <code>PUBLIC.BUDGET.PROFILE</code>.
     */
    public BudgetRecord setProfile(String value) {
        set(4, value);
        return this;
    }

    /**
     * Getter for <code>PUBLIC.BUDGET.PROFILE</code>.
     */
    public String getProfile() {
        return (String) get(4);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<String> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record5 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row5<String, String, String, Boolean, String> fieldsRow() {
        return (Row5) super.fieldsRow();
    }

    @Override
    public Row5<String, String, String, Boolean, String> valuesRow() {
        return (Row5) super.valuesRow();
    }

    @Override
    public Field<String> field1() {
        return Budget.BUDGET.ID;
    }

    @Override
    public Field<String> field2() {
        return Budget.BUDGET.PERSON_ID;
    }

    @Override
    public Field<String> field3() {
        return Budget.BUDGET.NAME;
    }

    @Override
    public Field<Boolean> field4() {
        return Budget.BUDGET.DELETED;
    }

    @Override
    public Field<String> field5() {
        return Budget.BUDGET.PROFILE;
    }

    @Override
    public String component1() {
        return getId();
    }

    @Override
    public String component2() {
        return getPersonId();
    }

    @Override
    public String component3() {
        return getName();
    }

    @Override
    public Boolean component4() {
        return getDeleted();
    }

    @Override
    public String component5() {
        return getProfile();
    }

    @Override
    public String value1() {
        return getId();
    }

    @Override
    public String value2() {
        return getPersonId();
    }

    @Override
    public String value3() {
        return getName();
    }

    @Override
    public Boolean value4() {
        return getDeleted();
    }

    @Override
    public String value5() {
        return getProfile();
    }

    @Override
    public BudgetRecord value1(String value) {
        setId(value);
        return this;
    }

    @Override
    public BudgetRecord value2(String value) {
        setPersonId(value);
        return this;
    }

    @Override
    public BudgetRecord value3(String value) {
        setName(value);
        return this;
    }

    @Override
    public BudgetRecord value4(Boolean value) {
        setDeleted(value);
        return this;
    }

    @Override
    public BudgetRecord value5(String value) {
        setProfile(value);
        return this;
    }

    @Override
    public BudgetRecord values(String value1, String value2, String value3, Boolean value4, String value5) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached BudgetRecord
     */
    public BudgetRecord() {
        super(Budget.BUDGET);
    }

    /**
     * Create a detached, initialised BudgetRecord
     */
    public BudgetRecord(String id, String personId, String name, Boolean deleted, String profile) {
        super(Budget.BUDGET);

        set(0, id);
        set(1, personId);
        set(2, name);
        set(3, deleted);
        set(4, profile);
    }
}
