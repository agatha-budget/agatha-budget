/*
 * This file is generated by jOOQ.
 */
package open.tresorier.generated.jooq.test.public_.tables.daos;


import java.util.List;

import open.tresorier.generated.jooq.test.public_.tables.Person;
import open.tresorier.generated.jooq.test.public_.tables.records.PersonRecord;

import org.jooq.Configuration;
import org.jooq.impl.DAOImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class PersonDao extends DAOImpl<PersonRecord, open.tresorier.generated.jooq.test.public_.tables.pojos.Person, String> {

    /**
     * Create a new PersonDao without any configuration
     */
    public PersonDao() {
        super(Person.PERSON, open.tresorier.generated.jooq.test.public_.tables.pojos.Person.class);
    }

    /**
     * Create a new PersonDao with an attached configuration
     */
    public PersonDao(Configuration configuration) {
        super(Person.PERSON, open.tresorier.generated.jooq.test.public_.tables.pojos.Person.class, configuration);
    }

    @Override
    public String getId(open.tresorier.generated.jooq.test.public_.tables.pojos.Person object) {
        return object.getId();
    }

    /**
     * Fetch records that have <code>ID BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<open.tresorier.generated.jooq.test.public_.tables.pojos.Person> fetchRangeOfId(String lowerInclusive, String upperInclusive) {
        return fetchRange(Person.PERSON.ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>ID IN (values)</code>
     */
    public List<open.tresorier.generated.jooq.test.public_.tables.pojos.Person> fetchById(String... values) {
        return fetch(Person.PERSON.ID, values);
    }

    /**
     * Fetch a unique record that has <code>ID = value</code>
     */
    public open.tresorier.generated.jooq.test.public_.tables.pojos.Person fetchOneById(String value) {
        return fetchOne(Person.PERSON.ID, value);
    }

    /**
     * Fetch records that have <code>EMAIL BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<open.tresorier.generated.jooq.test.public_.tables.pojos.Person> fetchRangeOfEmail(String lowerInclusive, String upperInclusive) {
        return fetchRange(Person.PERSON.EMAIL, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>EMAIL IN (values)</code>
     */
    public List<open.tresorier.generated.jooq.test.public_.tables.pojos.Person> fetchByEmail(String... values) {
        return fetch(Person.PERSON.EMAIL, values);
    }

    /**
     * Fetch a unique record that has <code>EMAIL = value</code>
     */
    public open.tresorier.generated.jooq.test.public_.tables.pojos.Person fetchOneByEmail(String value) {
        return fetchOne(Person.PERSON.EMAIL, value);
    }

    /**
     * Fetch records that have <code>NAME BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<open.tresorier.generated.jooq.test.public_.tables.pojos.Person> fetchRangeOfName(String lowerInclusive, String upperInclusive) {
        return fetchRange(Person.PERSON.NAME, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>NAME IN (values)</code>
     */
    public List<open.tresorier.generated.jooq.test.public_.tables.pojos.Person> fetchByName(String... values) {
        return fetch(Person.PERSON.NAME, values);
    }

    /**
     * Fetch records that have <code>PASSWORD BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<open.tresorier.generated.jooq.test.public_.tables.pojos.Person> fetchRangeOfPassword(String lowerInclusive, String upperInclusive) {
        return fetchRange(Person.PERSON.PASSWORD, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>PASSWORD IN (values)</code>
     */
    public List<open.tresorier.generated.jooq.test.public_.tables.pojos.Person> fetchByPassword(String... values) {
        return fetch(Person.PERSON.PASSWORD, values);
    }

    /**
     * Fetch records that have <code>UNLOCKINGDATE BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<open.tresorier.generated.jooq.test.public_.tables.pojos.Person> fetchRangeOfUnlockingdate(Long lowerInclusive, Long upperInclusive) {
        return fetchRange(Person.PERSON.UNLOCKINGDATE, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>UNLOCKINGDATE IN (values)</code>
     */
    public List<open.tresorier.generated.jooq.test.public_.tables.pojos.Person> fetchByUnlockingdate(Long... values) {
        return fetch(Person.PERSON.UNLOCKINGDATE, values);
    }

    /**
     * Fetch records that have <code>LOGINATTEMPTCOUNT BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<open.tresorier.generated.jooq.test.public_.tables.pojos.Person> fetchRangeOfLoginattemptcount(Integer lowerInclusive, Integer upperInclusive) {
        return fetchRange(Person.PERSON.LOGINATTEMPTCOUNT, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>LOGINATTEMPTCOUNT IN (values)</code>
     */
    public List<open.tresorier.generated.jooq.test.public_.tables.pojos.Person> fetchByLoginattemptcount(Integer... values) {
        return fetch(Person.PERSON.LOGINATTEMPTCOUNT, values);
    }

    /**
     * Fetch records that have <code>DELETED BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<open.tresorier.generated.jooq.test.public_.tables.pojos.Person> fetchRangeOfDeleted(Boolean lowerInclusive, Boolean upperInclusive) {
        return fetchRange(Person.PERSON.DELETED, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>DELETED IN (values)</code>
     */
    public List<open.tresorier.generated.jooq.test.public_.tables.pojos.Person> fetchByDeleted(Boolean... values) {
        return fetch(Person.PERSON.DELETED, values);
    }

    /**
     * Fetch records that have <code>BILLING_ID BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<open.tresorier.generated.jooq.test.public_.tables.pojos.Person> fetchRangeOfBillingId(String lowerInclusive, String upperInclusive) {
        return fetchRange(Person.PERSON.BILLING_ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>BILLING_ID IN (values)</code>
     */
    public List<open.tresorier.generated.jooq.test.public_.tables.pojos.Person> fetchByBillingId(String... values) {
        return fetch(Person.PERSON.BILLING_ID, values);
    }

    /**
     * Fetch a unique record that has <code>BILLING_ID = value</code>
     */
    public open.tresorier.generated.jooq.test.public_.tables.pojos.Person fetchOneByBillingId(String value) {
        return fetchOne(Person.PERSON.BILLING_ID, value);
    }

    /**
     * Fetch records that have <code>BILLING_STATUS BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<open.tresorier.generated.jooq.test.public_.tables.pojos.Person> fetchRangeOfBillingStatus(Boolean lowerInclusive, Boolean upperInclusive) {
        return fetchRange(Person.PERSON.BILLING_STATUS, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>BILLING_STATUS IN (values)</code>
     */
    public List<open.tresorier.generated.jooq.test.public_.tables.pojos.Person> fetchByBillingStatus(Boolean... values) {
        return fetch(Person.PERSON.BILLING_STATUS, values);
    }
}
