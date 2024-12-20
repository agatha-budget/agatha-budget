/*
 * This file is generated by jOOQ.
 */
package open.tresorier.generated.jooq.test.tables.daos;


import java.util.List;
import java.util.Optional;

import open.tresorier.generated.jooq.test.tables.Account;
import open.tresorier.generated.jooq.test.tables.records.AccountRecord;

import org.jooq.Configuration;
import org.jooq.impl.DAOImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class AccountDao extends DAOImpl<AccountRecord, open.tresorier.generated.jooq.test.tables.pojos.Account, String> {

    /**
     * Create a new AccountDao without any configuration
     */
    public AccountDao() {
        super(Account.ACCOUNT, open.tresorier.generated.jooq.test.tables.pojos.Account.class);
    }

    /**
     * Create a new AccountDao with an attached configuration
     */
    public AccountDao(Configuration configuration) {
        super(Account.ACCOUNT, open.tresorier.generated.jooq.test.tables.pojos.Account.class, configuration);
    }

    @Override
    public String getId(open.tresorier.generated.jooq.test.tables.pojos.Account object) {
        return object.getId();
    }

    /**
     * Fetch records that have <code>id BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<open.tresorier.generated.jooq.test.tables.pojos.Account> fetchRangeOfId(String lowerInclusive, String upperInclusive) {
        return fetchRange(Account.ACCOUNT.ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>id IN (values)</code>
     */
    public List<open.tresorier.generated.jooq.test.tables.pojos.Account> fetchById(String... values) {
        return fetch(Account.ACCOUNT.ID, values);
    }

    /**
     * Fetch a unique record that has <code>id = value</code>
     */
    public open.tresorier.generated.jooq.test.tables.pojos.Account fetchOneById(String value) {
        return fetchOne(Account.ACCOUNT.ID, value);
    }

    /**
     * Fetch a unique record that has <code>id = value</code>
     */
    public Optional<open.tresorier.generated.jooq.test.tables.pojos.Account> fetchOptionalById(String value) {
        return fetchOptional(Account.ACCOUNT.ID, value);
    }

    /**
     * Fetch records that have <code>budget_id BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<open.tresorier.generated.jooq.test.tables.pojos.Account> fetchRangeOfBudgetId(String lowerInclusive, String upperInclusive) {
        return fetchRange(Account.ACCOUNT.BUDGET_ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>budget_id IN (values)</code>
     */
    public List<open.tresorier.generated.jooq.test.tables.pojos.Account> fetchByBudgetId(String... values) {
        return fetch(Account.ACCOUNT.BUDGET_ID, values);
    }

    /**
     * Fetch records that have <code>name BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<open.tresorier.generated.jooq.test.tables.pojos.Account> fetchRangeOfName(String lowerInclusive, String upperInclusive) {
        return fetchRange(Account.ACCOUNT.NAME, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>name IN (values)</code>
     */
    public List<open.tresorier.generated.jooq.test.tables.pojos.Account> fetchByName(String... values) {
        return fetch(Account.ACCOUNT.NAME, values);
    }

    /**
     * Fetch records that have <code>archived BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<open.tresorier.generated.jooq.test.tables.pojos.Account> fetchRangeOfArchived(Boolean lowerInclusive, Boolean upperInclusive) {
        return fetchRange(Account.ACCOUNT.ARCHIVED, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>archived IN (values)</code>
     */
    public List<open.tresorier.generated.jooq.test.tables.pojos.Account> fetchByArchived(Boolean... values) {
        return fetch(Account.ACCOUNT.ARCHIVED, values);
    }

    /**
     * Fetch records that have <code>deleted BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<open.tresorier.generated.jooq.test.tables.pojos.Account> fetchRangeOfDeleted(Boolean lowerInclusive, Boolean upperInclusive) {
        return fetchRange(Account.ACCOUNT.DELETED, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>deleted IN (values)</code>
     */
    public List<open.tresorier.generated.jooq.test.tables.pojos.Account> fetchByDeleted(Boolean... values) {
        return fetch(Account.ACCOUNT.DELETED, values);
    }

    /**
     * Fetch records that have <code>bank_account_id BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<open.tresorier.generated.jooq.test.tables.pojos.Account> fetchRangeOfBankAccountId(String lowerInclusive, String upperInclusive) {
        return fetchRange(Account.ACCOUNT.BANK_ACCOUNT_ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>bank_account_id IN (values)</code>
     */
    public List<open.tresorier.generated.jooq.test.tables.pojos.Account> fetchByBankAccountId(String... values) {
        return fetch(Account.ACCOUNT.BANK_ACCOUNT_ID, values);
    }
}
