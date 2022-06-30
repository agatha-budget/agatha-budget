/*
 * This file is generated by jOOQ.
 */
package open.tresorier.generated.jooq.test.public_.tables.daos;


import java.util.List;

import open.tresorier.generated.jooq.test.public_.tables.BankAgreement;
import open.tresorier.generated.jooq.test.public_.tables.records.BankAgreementRecord;

import org.jooq.Configuration;
import org.jooq.impl.DAOImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class BankAgreementDao extends DAOImpl<BankAgreementRecord, open.tresorier.generated.jooq.test.public_.tables.pojos.BankAgreement, String> {

    /**
     * Create a new BankAgreementDao without any configuration
     */
    public BankAgreementDao() {
        super(BankAgreement.BANK_AGREEMENT, open.tresorier.generated.jooq.test.public_.tables.pojos.BankAgreement.class);
    }

    /**
     * Create a new BankAgreementDao with an attached configuration
     */
    public BankAgreementDao(Configuration configuration) {
        super(BankAgreement.BANK_AGREEMENT, open.tresorier.generated.jooq.test.public_.tables.pojos.BankAgreement.class, configuration);
    }

    @Override
    public String getId(open.tresorier.generated.jooq.test.public_.tables.pojos.BankAgreement object) {
        return object.getId();
    }

    /**
     * Fetch records that have <code>ID BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<open.tresorier.generated.jooq.test.public_.tables.pojos.BankAgreement> fetchRangeOfId(String lowerInclusive, String upperInclusive) {
        return fetchRange(BankAgreement.BANK_AGREEMENT.ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>ID IN (values)</code>
     */
    public List<open.tresorier.generated.jooq.test.public_.tables.pojos.BankAgreement> fetchById(String... values) {
        return fetch(BankAgreement.BANK_AGREEMENT.ID, values);
    }

    /**
     * Fetch a unique record that has <code>ID = value</code>
     */
    public open.tresorier.generated.jooq.test.public_.tables.pojos.BankAgreement fetchOneById(String value) {
        return fetchOne(BankAgreement.BANK_AGREEMENT.ID, value);
    }

    /**
     * Fetch records that have <code>PERSON_ID BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<open.tresorier.generated.jooq.test.public_.tables.pojos.BankAgreement> fetchRangeOfPersonId(String lowerInclusive, String upperInclusive) {
        return fetchRange(BankAgreement.BANK_AGREEMENT.PERSON_ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>PERSON_ID IN (values)</code>
     */
    public List<open.tresorier.generated.jooq.test.public_.tables.pojos.BankAgreement> fetchByPersonId(String... values) {
        return fetch(BankAgreement.BANK_AGREEMENT.PERSON_ID, values);
    }

    /**
     * Fetch records that have <code>BANK_ID BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<open.tresorier.generated.jooq.test.public_.tables.pojos.BankAgreement> fetchRangeOfBankId(String lowerInclusive, String upperInclusive) {
        return fetchRange(BankAgreement.BANK_AGREEMENT.BANK_ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>BANK_ID IN (values)</code>
     */
    public List<open.tresorier.generated.jooq.test.public_.tables.pojos.BankAgreement> fetchByBankId(String... values) {
        return fetch(BankAgreement.BANK_AGREEMENT.BANK_ID, values);
    }

    /**
     * Fetch records that have <code>VALID_UNTIL BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<open.tresorier.generated.jooq.test.public_.tables.pojos.BankAgreement> fetchRangeOfValidUntil(Long lowerInclusive, Long upperInclusive) {
        return fetchRange(BankAgreement.BANK_AGREEMENT.VALID_UNTIL, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>VALID_UNTIL IN (values)</code>
     */
    public List<open.tresorier.generated.jooq.test.public_.tables.pojos.BankAgreement> fetchByValidUntil(Long... values) {
        return fetch(BankAgreement.BANK_AGREEMENT.VALID_UNTIL, values);
    }

    /**
     * Fetch records that have <code>NORDIGEN_REQUISITION_ID BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<open.tresorier.generated.jooq.test.public_.tables.pojos.BankAgreement> fetchRangeOfNordigenRequisitionId(String lowerInclusive, String upperInclusive) {
        return fetchRange(BankAgreement.BANK_AGREEMENT.NORDIGEN_REQUISITION_ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>NORDIGEN_REQUISITION_ID IN (values)</code>
     */
    public List<open.tresorier.generated.jooq.test.public_.tables.pojos.BankAgreement> fetchByNordigenRequisitionId(String... values) {
        return fetch(BankAgreement.BANK_AGREEMENT.NORDIGEN_REQUISITION_ID, values);
    }

    /**
     * Fetch records that have <code>ARCHIVED BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<open.tresorier.generated.jooq.test.public_.tables.pojos.BankAgreement> fetchRangeOfArchived(Boolean lowerInclusive, Boolean upperInclusive) {
        return fetchRange(BankAgreement.BANK_AGREEMENT.ARCHIVED, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>ARCHIVED IN (values)</code>
     */
    public List<open.tresorier.generated.jooq.test.public_.tables.pojos.BankAgreement> fetchByArchived(Boolean... values) {
        return fetch(BankAgreement.BANK_AGREEMENT.ARCHIVED, values);
    }

    /**
     * Fetch records that have <code>DELETED BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<open.tresorier.generated.jooq.test.public_.tables.pojos.BankAgreement> fetchRangeOfDeleted(Boolean lowerInclusive, Boolean upperInclusive) {
        return fetchRange(BankAgreement.BANK_AGREEMENT.DELETED, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>DELETED IN (values)</code>
     */
    public List<open.tresorier.generated.jooq.test.public_.tables.pojos.BankAgreement> fetchByDeleted(Boolean... values) {
        return fetch(BankAgreement.BANK_AGREEMENT.DELETED, values);
    }
}
