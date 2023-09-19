/*
 * This file is generated by jOOQ.
 */
package open.tresorier.generated.jooq.test.public_.tables.records;


import open.tresorier.generated.jooq.test.public_.tables.Operation;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record13;
import org.jooq.Row13;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class OperationRecord extends UpdatableRecordImpl<OperationRecord> implements Record13<String, String, Integer, Integer, String, String, Integer, Long, Boolean, Boolean, String, String, Long> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>PUBLIC.OPERATION.ID</code>.
     */
    public OperationRecord setId(String value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>PUBLIC.OPERATION.ID</code>.
     */
    public String getId() {
        return (String) get(0);
    }

    /**
     * Setter for <code>PUBLIC.OPERATION.ACCOUNT_ID</code>.
     */
    public OperationRecord setAccountId(String value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>PUBLIC.OPERATION.ACCOUNT_ID</code>.
     */
    public String getAccountId() {
        return (String) get(1);
    }

    /**
     * Setter for <code>PUBLIC.OPERATION.DATE_MONTH</code>.
     */
    public OperationRecord setDateMonth(Integer value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>PUBLIC.OPERATION.DATE_MONTH</code>.
     */
    public Integer getDateMonth() {
        return (Integer) get(2);
    }

    /**
     * Setter for <code>PUBLIC.OPERATION.DATE_DAY</code>.
     */
    public OperationRecord setDateDay(Integer value) {
        set(3, value);
        return this;
    }

    /**
     * Getter for <code>PUBLIC.OPERATION.DATE_DAY</code>.
     */
    public Integer getDateDay() {
        return (Integer) get(3);
    }

    /**
     * Setter for <code>PUBLIC.OPERATION.CATEGORY_ID</code>.
     */
    public OperationRecord setCategoryId(String value) {
        set(4, value);
        return this;
    }

    /**
     * Getter for <code>PUBLIC.OPERATION.CATEGORY_ID</code>.
     */
    public String getCategoryId() {
        return (String) get(4);
    }

    /**
     * Setter for <code>PUBLIC.OPERATION.MEMO</code>.
     */
    public OperationRecord setMemo(String value) {
        set(5, value);
        return this;
    }

    /**
     * Getter for <code>PUBLIC.OPERATION.MEMO</code>.
     */
    public String getMemo() {
        return (String) get(5);
    }

    /**
     * Setter for <code>PUBLIC.OPERATION.AMOUNT</code>.
     */
    public OperationRecord setAmount(Integer value) {
        set(6, value);
        return this;
    }

    /**
     * Getter for <code>PUBLIC.OPERATION.AMOUNT</code>.
     */
    public Integer getAmount() {
        return (Integer) get(6);
    }

    /**
     * Setter for <code>PUBLIC.OPERATION.ORDER_IN_DAY</code>.
     */
    public OperationRecord setOrderInDay(Long value) {
        set(7, value);
        return this;
    }

    /**
     * Getter for <code>PUBLIC.OPERATION.ORDER_IN_DAY</code>.
     */
    public Long getOrderInDay() {
        return (Long) get(7);
    }

    /**
     * Setter for <code>PUBLIC.OPERATION.PENDING</code>.
     */
    public OperationRecord setPending(Boolean value) {
        set(8, value);
        return this;
    }

    /**
     * Getter for <code>PUBLIC.OPERATION.PENDING</code>.
     */
    public Boolean getPending() {
        return (Boolean) get(8);
    }

    /**
     * Setter for <code>PUBLIC.OPERATION.LOCKED</code>.
     */
    public OperationRecord setLocked(Boolean value) {
        set(9, value);
        return this;
    }

    /**
     * Getter for <code>PUBLIC.OPERATION.LOCKED</code>.
     */
    public Boolean getLocked() {
        return (Boolean) get(9);
    }

    /**
     * Setter for <code>PUBLIC.OPERATION.MOTHER_OPERATION_ID</code>.
     */
    public OperationRecord setMotherOperationId(String value) {
        set(10, value);
        return this;
    }

    /**
     * Getter for <code>PUBLIC.OPERATION.MOTHER_OPERATION_ID</code>.
     */
    public String getMotherOperationId() {
        return (String) get(10);
    }

    /**
     * Setter for <code>PUBLIC.OPERATION.IMPORT_IDENTIFIER</code>.
     */
    public OperationRecord setImportIdentifier(String value) {
        set(11, value);
        return this;
    }

    /**
     * Getter for <code>PUBLIC.OPERATION.IMPORT_IDENTIFIER</code>.
     */
    public String getImportIdentifier() {
        return (String) get(11);
    }

    /**
     * Setter for <code>PUBLIC.OPERATION.IMPORT_TIMESTAMP</code>.
     */
    public OperationRecord setImportTimestamp(Long value) {
        set(12, value);
        return this;
    }

    /**
     * Getter for <code>PUBLIC.OPERATION.IMPORT_TIMESTAMP</code>.
     */
    public Long getImportTimestamp() {
        return (Long) get(12);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<String> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record13 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row13<String, String, Integer, Integer, String, String, Integer, Long, Boolean, Boolean, String, String, Long> fieldsRow() {
        return (Row13) super.fieldsRow();
    }

    @Override
    public Row13<String, String, Integer, Integer, String, String, Integer, Long, Boolean, Boolean, String, String, Long> valuesRow() {
        return (Row13) super.valuesRow();
    }

    @Override
    public Field<String> field1() {
        return Operation.OPERATION.ID;
    }

    @Override
    public Field<String> field2() {
        return Operation.OPERATION.ACCOUNT_ID;
    }

    @Override
    public Field<Integer> field3() {
        return Operation.OPERATION.DATE_MONTH;
    }

    @Override
    public Field<Integer> field4() {
        return Operation.OPERATION.DATE_DAY;
    }

    @Override
    public Field<String> field5() {
        return Operation.OPERATION.CATEGORY_ID;
    }

    @Override
    public Field<String> field6() {
        return Operation.OPERATION.MEMO;
    }

    @Override
    public Field<Integer> field7() {
        return Operation.OPERATION.AMOUNT;
    }

    @Override
    public Field<Long> field8() {
        return Operation.OPERATION.ORDER_IN_DAY;
    }

    @Override
    public Field<Boolean> field9() {
        return Operation.OPERATION.PENDING;
    }

    @Override
    public Field<Boolean> field10() {
        return Operation.OPERATION.LOCKED;
    }

    @Override
    public Field<String> field11() {
        return Operation.OPERATION.MOTHER_OPERATION_ID;
    }

    @Override
    public Field<String> field12() {
        return Operation.OPERATION.IMPORT_IDENTIFIER;
    }

    @Override
    public Field<Long> field13() {
        return Operation.OPERATION.IMPORT_TIMESTAMP;
    }

    @Override
    public String component1() {
        return getId();
    }

    @Override
    public String component2() {
        return getAccountId();
    }

    @Override
    public Integer component3() {
        return getDateMonth();
    }

    @Override
    public Integer component4() {
        return getDateDay();
    }

    @Override
    public String component5() {
        return getCategoryId();
    }

    @Override
    public String component6() {
        return getMemo();
    }

    @Override
    public Integer component7() {
        return getAmount();
    }

    @Override
    public Long component8() {
        return getOrderInDay();
    }

    @Override
    public Boolean component9() {
        return getPending();
    }

    @Override
    public Boolean component10() {
        return getLocked();
    }

    @Override
    public String component11() {
        return getMotherOperationId();
    }

    @Override
    public String component12() {
        return getImportIdentifier();
    }

    @Override
    public Long component13() {
        return getImportTimestamp();
    }

    @Override
    public String value1() {
        return getId();
    }

    @Override
    public String value2() {
        return getAccountId();
    }

    @Override
    public Integer value3() {
        return getDateMonth();
    }

    @Override
    public Integer value4() {
        return getDateDay();
    }

    @Override
    public String value5() {
        return getCategoryId();
    }

    @Override
    public String value6() {
        return getMemo();
    }

    @Override
    public Integer value7() {
        return getAmount();
    }

    @Override
    public Long value8() {
        return getOrderInDay();
    }

    @Override
    public Boolean value9() {
        return getPending();
    }

    @Override
    public Boolean value10() {
        return getLocked();
    }

    @Override
    public String value11() {
        return getMotherOperationId();
    }

    @Override
    public String value12() {
        return getImportIdentifier();
    }

    @Override
    public Long value13() {
        return getImportTimestamp();
    }

    @Override
    public OperationRecord value1(String value) {
        setId(value);
        return this;
    }

    @Override
    public OperationRecord value2(String value) {
        setAccountId(value);
        return this;
    }

    @Override
    public OperationRecord value3(Integer value) {
        setDateMonth(value);
        return this;
    }

    @Override
    public OperationRecord value4(Integer value) {
        setDateDay(value);
        return this;
    }

    @Override
    public OperationRecord value5(String value) {
        setCategoryId(value);
        return this;
    }

    @Override
    public OperationRecord value6(String value) {
        setMemo(value);
        return this;
    }

    @Override
    public OperationRecord value7(Integer value) {
        setAmount(value);
        return this;
    }

    @Override
    public OperationRecord value8(Long value) {
        setOrderInDay(value);
        return this;
    }

    @Override
    public OperationRecord value9(Boolean value) {
        setPending(value);
        return this;
    }

    @Override
    public OperationRecord value10(Boolean value) {
        setLocked(value);
        return this;
    }

    @Override
    public OperationRecord value11(String value) {
        setMotherOperationId(value);
        return this;
    }

    @Override
    public OperationRecord value12(String value) {
        setImportIdentifier(value);
        return this;
    }

    @Override
    public OperationRecord value13(Long value) {
        setImportTimestamp(value);
        return this;
    }

    @Override
    public OperationRecord values(String value1, String value2, Integer value3, Integer value4, String value5, String value6, Integer value7, Long value8, Boolean value9, Boolean value10, String value11, String value12, Long value13) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        value8(value8);
        value9(value9);
        value10(value10);
        value11(value11);
        value12(value12);
        value13(value13);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached OperationRecord
     */
    public OperationRecord() {
        super(Operation.OPERATION);
    }

    /**
     * Create a detached, initialised OperationRecord
     */
    public OperationRecord(String id, String accountId, Integer dateMonth, Integer dateDay, String categoryId, String memo, Integer amount, Long orderInDay, Boolean pending, Boolean locked, String motherOperationId, String importIdentifier, Long importTimestamp) {
        super(Operation.OPERATION);

        setId(id);
        setAccountId(accountId);
        setDateMonth(dateMonth);
        setDateDay(dateDay);
        setCategoryId(categoryId);
        setMemo(memo);
        setAmount(amount);
        setOrderInDay(orderInDay);
        setPending(pending);
        setLocked(locked);
        setMotherOperationId(motherOperationId);
        setImportIdentifier(importIdentifier);
        setImportTimestamp(importTimestamp);
        resetChangedOnNotNull();
    }

    /**
     * Create a detached, initialised OperationRecord
     */
    public OperationRecord(open.tresorier.generated.jooq.test.public_.tables.pojos.Operation value) {
        super(Operation.OPERATION);

        if (value != null) {
            setId(value.getId());
            setAccountId(value.getAccountId());
            setDateMonth(value.getDateMonth());
            setDateDay(value.getDateDay());
            setCategoryId(value.getCategoryId());
            setMemo(value.getMemo());
            setAmount(value.getAmount());
            setOrderInDay(value.getOrderInDay());
            setPending(value.getPending());
            setLocked(value.getLocked());
            setMotherOperationId(value.getMotherOperationId());
            setImportIdentifier(value.getImportIdentifier());
            setImportTimestamp(value.getImportTimestamp());
            resetChangedOnNotNull();
        }
    }
}
