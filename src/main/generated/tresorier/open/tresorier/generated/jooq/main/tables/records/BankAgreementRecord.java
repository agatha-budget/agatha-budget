/*
 * This file is generated by jOOQ.
 */
package open.tresorier.generated.jooq.main.tables.records;


import open.tresorier.generated.jooq.main.tables.BankAgreement;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record7;
import org.jooq.Row7;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class BankAgreementRecord extends UpdatableRecordImpl<BankAgreementRecord> implements Record7<String, String, String, Long, String, Boolean, Boolean> {

    private static final long serialVersionUID = 1584904869;

    /**
     * Setter for <code>public.bank_agreement.id</code>.
     */
    public BankAgreementRecord setId(String value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>public.bank_agreement.id</code>.
     */
    public String getId() {
        return (String) get(0);
    }

    /**
     * Setter for <code>public.bank_agreement.budget_id</code>.
     */
    public BankAgreementRecord setBudgetId(String value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>public.bank_agreement.budget_id</code>.
     */
    public String getBudgetId() {
        return (String) get(1);
    }

    /**
     * Setter for <code>public.bank_agreement.bank_id</code>.
     */
    public BankAgreementRecord setBankId(String value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>public.bank_agreement.bank_id</code>.
     */
    public String getBankId() {
        return (String) get(2);
    }

    /**
     * Setter for <code>public.bank_agreement.timestamp</code>.
     */
    public BankAgreementRecord setTimestamp(Long value) {
        set(3, value);
        return this;
    }

    /**
     * Getter for <code>public.bank_agreement.timestamp</code>.
     */
    public Long getTimestamp() {
        return (Long) get(3);
    }

    /**
     * Setter for <code>public.bank_agreement.nordigen_requisition_id</code>.
     */
    public BankAgreementRecord setNordigenRequisitionId(String value) {
        set(4, value);
        return this;
    }

    /**
     * Getter for <code>public.bank_agreement.nordigen_requisition_id</code>.
     */
    public String getNordigenRequisitionId() {
        return (String) get(4);
    }

    /**
     * Setter for <code>public.bank_agreement.archived</code>.
     */
    public BankAgreementRecord setArchived(Boolean value) {
        set(5, value);
        return this;
    }

    /**
     * Getter for <code>public.bank_agreement.archived</code>.
     */
    public Boolean getArchived() {
        return (Boolean) get(5);
    }

    /**
     * Setter for <code>public.bank_agreement.deleted</code>.
     */
    public BankAgreementRecord setDeleted(Boolean value) {
        set(6, value);
        return this;
    }

    /**
     * Getter for <code>public.bank_agreement.deleted</code>.
     */
    public Boolean getDeleted() {
        return (Boolean) get(6);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<String> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record7 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row7<String, String, String, Long, String, Boolean, Boolean> fieldsRow() {
        return (Row7) super.fieldsRow();
    }

    @Override
    public Row7<String, String, String, Long, String, Boolean, Boolean> valuesRow() {
        return (Row7) super.valuesRow();
    }

    @Override
    public Field<String> field1() {
        return BankAgreement.BANK_AGREEMENT.ID;
    }

    @Override
    public Field<String> field2() {
        return BankAgreement.BANK_AGREEMENT.BUDGET_ID;
    }

    @Override
    public Field<String> field3() {
        return BankAgreement.BANK_AGREEMENT.BANK_ID;
    }

    @Override
    public Field<Long> field4() {
        return BankAgreement.BANK_AGREEMENT.TIMESTAMP;
    }

    @Override
    public Field<String> field5() {
        return BankAgreement.BANK_AGREEMENT.NORDIGEN_REQUISITION_ID;
    }

    @Override
    public Field<Boolean> field6() {
        return BankAgreement.BANK_AGREEMENT.ARCHIVED;
    }

    @Override
    public Field<Boolean> field7() {
        return BankAgreement.BANK_AGREEMENT.DELETED;
    }

    @Override
    public String component1() {
        return getId();
    }

    @Override
    public String component2() {
        return getBudgetId();
    }

    @Override
    public String component3() {
        return getBankId();
    }

    @Override
    public Long component4() {
        return getTimestamp();
    }

    @Override
    public String component5() {
        return getNordigenRequisitionId();
    }

    @Override
    public Boolean component6() {
        return getArchived();
    }

    @Override
    public Boolean component7() {
        return getDeleted();
    }

    @Override
    public String value1() {
        return getId();
    }

    @Override
    public String value2() {
        return getBudgetId();
    }

    @Override
    public String value3() {
        return getBankId();
    }

    @Override
    public Long value4() {
        return getTimestamp();
    }

    @Override
    public String value5() {
        return getNordigenRequisitionId();
    }

    @Override
    public Boolean value6() {
        return getArchived();
    }

    @Override
    public Boolean value7() {
        return getDeleted();
    }

    @Override
    public BankAgreementRecord value1(String value) {
        setId(value);
        return this;
    }

    @Override
    public BankAgreementRecord value2(String value) {
        setBudgetId(value);
        return this;
    }

    @Override
    public BankAgreementRecord value3(String value) {
        setBankId(value);
        return this;
    }

    @Override
    public BankAgreementRecord value4(Long value) {
        setTimestamp(value);
        return this;
    }

    @Override
    public BankAgreementRecord value5(String value) {
        setNordigenRequisitionId(value);
        return this;
    }

    @Override
    public BankAgreementRecord value6(Boolean value) {
        setArchived(value);
        return this;
    }

    @Override
    public BankAgreementRecord value7(Boolean value) {
        setDeleted(value);
        return this;
    }

    @Override
    public BankAgreementRecord values(String value1, String value2, String value3, Long value4, String value5, Boolean value6, Boolean value7) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached BankAgreementRecord
     */
    public BankAgreementRecord() {
        super(BankAgreement.BANK_AGREEMENT);
    }

    /**
     * Create a detached, initialised BankAgreementRecord
     */
    public BankAgreementRecord(String id, String budgetId, String bankId, Long timestamp, String nordigenRequisitionId, Boolean archived, Boolean deleted) {
        super(BankAgreement.BANK_AGREEMENT);

        set(0, id);
        set(1, budgetId);
        set(2, bankId);
        set(3, timestamp);
        set(4, nordigenRequisitionId);
        set(5, archived);
        set(6, deleted);
    }
}
