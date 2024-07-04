/*
 * This file is generated by jOOQ.
 */
package open.tresorier.generated.jooq.test.public_.tables.daos;


import java.util.List;
import java.util.Optional;

import open.tresorier.generated.jooq.test.public_.tables.Account;
import open.tresorier.generated.jooq.test.public_.tables.records.AccountRecord;

import org.jooq.Configuration;
import org.jooq.impl.DAOImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class AccountDao extends DAOImpl<AccountRecord, open.tresorier.generated.jooq.test.public_.tables.pojos.Account, String> {

    /**
     * Create a new AccountDao without any configuration
     */
    public AccountDao() {
        super(Account.ACCOUNT, open.tresorier.generated.jooq.test.public_.tables.pojos.Account.class);
    }

    /**
     * Create a new AccountDao with an attached configuration
     */
    public AccountDao(Configuration configuration) {
        super(Account.ACCOUNT, open.tresorier.generated.jooq.test.public_.tables.pojos.Account.class, configuration);
    }

    @Override
    public String getId(open.tresorier.generated.jooq.test.public_.tables.pojos.Account object) {
        return object.getId();
    }

    /**
     * Fetch records that have <code>ID BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<open.tresorier.generated.jooq.test.public_.tables.pojos.Account> fetchRangeOfId(String lowerInclusive, String upperInclusive) {
        return fetchRange(Account.ACCOUNT.ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>ID IN (values)</code>
     */
    public List<open.tresorier.generated.jooq.test.public_.tables.pojos.Account> fetchById(String... values) {
        return fetch(Account.ACCOUNT.ID, values);
    }

    /**
     * Fetch a unique record that has <code>ID = value</code>
     */
    public open.tresorier.generated.jooq.test.public_.tables.pojos.Account fetchOneById(String value) {
        return fetchOne(Account.ACCOUNT.ID, value);
    }

    /**
     * Fetch a unique record that has <code>ID = value</code>
     */
    public Optional<open.tresorier.generated.jooq.test.public_.tables.pojos.Account> fetchOptionalById(String value) {
        return fetchOptional(Account.ACCOUNT.ID, value);
    }

    /**
     * Fetch records that have <code>BUDGET_ID BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<open.tresorier.generated.jooq.test.public_.tables.pojos.Account> fetchRangeOfBudgetId(String lowerInclusive, String upperInclusive) {
        return fetchRange(Account.ACCOUNT.BUDGET_ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>BUDGET_ID IN (values)</code>
     */
    public List<open.tresorier.generated.jooq.test.public_.tables.pojos.Account> fetchByBudgetId(String... values) {
        return fetch(Account.ACCOUNT.BUDGET_ID, values);
    }

    /**
     * Fetch records that have <code>NAME BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<open.tresorier.generated.jooq.test.public_.tables.pojos.Account> fetchRangeOfName(String lowerInclusive, String upperInclusive) {
        return fetchRange(Account.ACCOUNT.NAME, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>NAME IN (values)</code>
     */
    public List<open.tresorier.generated.jooq.test.public_.tables.pojos.Account> fetchByName(String... values) {
        return fetch(Account.ACCOUNT.NAME, values);
    }

    /**
     * Fetch records that have <code>ARCHIVED BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<open.tresorier.generated.jooq.test.public_.tables.pojos.Account> fetchRangeOfArchived(Boolean lowerInclusive, Boolean upperInclusive) {
        return fetchRange(Account.ACCOUNT.ARCHIVED, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>ARCHIVED IN (values)</code>
     */
    public List<open.tresorier.generated.jooq.test.public_.tables.pojos.Account> fetchByArchived(Boolean... values) {
        return fetch(Account.ACCOUNT.ARCHIVED, values);
    }

    /**
     * Fetch records that have <code>DELETED BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<open.tresorier.generated.jooq.test.public_.tables.pojos.Account> fetchRangeOfDeleted(Boolean lowerInclusive, Boolean upperInclusive) {
        return fetchRange(Account.ACCOUNT.DELETED, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>DELETED IN (values)</code>
     */
    public List<open.tresorier.generated.jooq.test.public_.tables.pojos.Account> fetchByDeleted(Boolean... values) {
        return fetch(Account.ACCOUNT.DELETED, values);
    }

    /**
     * Fetch records that have <code>BANK_ACCOUNT_ID BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<open.tresorier.generated.jooq.test.public_.tables.pojos.Account> fetchRangeOfBankAccountId(String lowerInclusive, String upperInclusive) {
        return fetchRange(Account.ACCOUNT.BANK_ACCOUNT_ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>BANK_ACCOUNT_ID IN (values)</code>
     */
    public List<open.tresorier.generated.jooq.test.public_.tables.pojos.Account> fetchByBankAccountId(String... values) {
        return fetch(Account.ACCOUNT.BANK_ACCOUNT_ID, values);
    }
}