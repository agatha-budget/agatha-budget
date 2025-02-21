/*
 * This file is generated by jOOQ.
 */
package open.tresorier.generated.jooq.test.tables.records;


import open.tresorier.generated.jooq.test.tables.Person;

import org.jooq.Record1;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class PersonRecord extends UpdatableRecordImpl<PersonRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>public.person.id</code>.
     */
    public PersonRecord setId(String value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>public.person.id</code>.
     */
    public String getId() {
        return (String) get(0);
    }

    /**
     * Setter for <code>public.person.email</code>.
     */
    public PersonRecord setEmail(String value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>public.person.email</code>.
     */
    public String getEmail() {
        return (String) get(1);
    }

    /**
     * Setter for <code>public.person.name</code>.
     */
    public PersonRecord setName(String value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>public.person.name</code>.
     */
    public String getName() {
        return (String) get(2);
    }

    /**
     * Setter for <code>public.person.password</code>.
     */
    public PersonRecord setPassword(String value) {
        set(3, value);
        return this;
    }

    /**
     * Getter for <code>public.person.password</code>.
     */
    public String getPassword() {
        return (String) get(3);
    }

    /**
     * Setter for <code>public.person.unlockingdate</code>.
     */
    public PersonRecord setUnlockingdate(Long value) {
        set(4, value);
        return this;
    }

    /**
     * Getter for <code>public.person.unlockingdate</code>.
     */
    public Long getUnlockingdate() {
        return (Long) get(4);
    }

    /**
     * Setter for <code>public.person.loginattemptcount</code>.
     */
    public PersonRecord setLoginattemptcount(Integer value) {
        set(5, value);
        return this;
    }

    /**
     * Getter for <code>public.person.loginattemptcount</code>.
     */
    public Integer getLoginattemptcount() {
        return (Integer) get(5);
    }

    /**
     * Setter for <code>public.person.deleted</code>.
     */
    public PersonRecord setDeleted(Boolean value) {
        set(6, value);
        return this;
    }

    /**
     * Getter for <code>public.person.deleted</code>.
     */
    public Boolean getDeleted() {
        return (Boolean) get(6);
    }

    /**
     * Setter for <code>public.person.billing_id</code>.
     */
    public PersonRecord setBillingId(String value) {
        set(7, value);
        return this;
    }

    /**
     * Getter for <code>public.person.billing_id</code>.
     */
    public String getBillingId() {
        return (String) get(7);
    }

    /**
     * Setter for <code>public.person.billing_status</code>.
     */
    public PersonRecord setBillingStatus(Boolean value) {
        set(8, value);
        return this;
    }

    /**
     * Getter for <code>public.person.billing_status</code>.
     */
    public Boolean getBillingStatus() {
        return (Boolean) get(8);
    }

    /**
     * Setter for <code>public.person.creation_date</code>.
     */
    public PersonRecord setCreationDate(Long value) {
        set(9, value);
        return this;
    }

    /**
     * Getter for <code>public.person.creation_date</code>.
     */
    public Long getCreationDate() {
        return (Long) get(9);
    }

    /**
     * Setter for <code>public.person.style</code>.
     */
    public PersonRecord setStyle(String value) {
        set(10, value);
        return this;
    }

    /**
     * Getter for <code>public.person.style</code>.
     */
    public String getStyle() {
        return (String) get(10);
    }

    /**
     * Setter for <code>public.person.dyslexia</code>.
     */
    public PersonRecord setDyslexia(Boolean value) {
        set(11, value);
        return this;
    }

    /**
     * Getter for <code>public.person.dyslexia</code>.
     */
    public Boolean getDyslexia() {
        return (Boolean) get(11);
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
     * Create a detached PersonRecord
     */
    public PersonRecord() {
        super(Person.PERSON);
    }

    /**
     * Create a detached, initialised PersonRecord
     */
    public PersonRecord(String id, String email, String name, String password, Long unlockingdate, Integer loginattemptcount, Boolean deleted, String billingId, Boolean billingStatus, Long creationDate, String style, Boolean dyslexia) {
        super(Person.PERSON);

        setId(id);
        setEmail(email);
        setName(name);
        setPassword(password);
        setUnlockingdate(unlockingdate);
        setLoginattemptcount(loginattemptcount);
        setDeleted(deleted);
        setBillingId(billingId);
        setBillingStatus(billingStatus);
        setCreationDate(creationDate);
        setStyle(style);
        setDyslexia(dyslexia);
        resetTouchedOnNotNull();
    }

    /**
     * Create a detached, initialised PersonRecord
     */
    public PersonRecord(open.tresorier.generated.jooq.test.tables.pojos.Person value) {
        super(Person.PERSON);

        if (value != null) {
            setId(value.getId());
            setEmail(value.getEmail());
            setName(value.getName());
            setPassword(value.getPassword());
            setUnlockingdate(value.getUnlockingdate());
            setLoginattemptcount(value.getLoginattemptcount());
            setDeleted(value.getDeleted());
            setBillingId(value.getBillingId());
            setBillingStatus(value.getBillingStatus());
            setCreationDate(value.getCreationDate());
            setStyle(value.getStyle());
            setDyslexia(value.getDyslexia());
            resetTouchedOnNotNull();
        }
    }
}
