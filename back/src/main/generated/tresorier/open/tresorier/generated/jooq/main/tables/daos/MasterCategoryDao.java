/*
 * This file is generated by jOOQ.
 */
package open.tresorier.generated.jooq.main.tables.daos;


import java.util.List;
import java.util.Optional;

import open.tresorier.generated.jooq.main.tables.MasterCategory;
import open.tresorier.generated.jooq.main.tables.records.MasterCategoryRecord;

import org.jooq.Configuration;
import org.jooq.impl.DAOImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class MasterCategoryDao extends DAOImpl<MasterCategoryRecord, open.tresorier.generated.jooq.main.tables.pojos.MasterCategory, String> {

    /**
     * Create a new MasterCategoryDao without any configuration
     */
    public MasterCategoryDao() {
        super(MasterCategory.MASTER_CATEGORY, open.tresorier.generated.jooq.main.tables.pojos.MasterCategory.class);
    }

    /**
     * Create a new MasterCategoryDao with an attached configuration
     */
    public MasterCategoryDao(Configuration configuration) {
        super(MasterCategory.MASTER_CATEGORY, open.tresorier.generated.jooq.main.tables.pojos.MasterCategory.class, configuration);
    }

    @Override
    public String getId(open.tresorier.generated.jooq.main.tables.pojos.MasterCategory object) {
        return object.getId();
    }

    /**
     * Fetch records that have <code>id BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<open.tresorier.generated.jooq.main.tables.pojos.MasterCategory> fetchRangeOfId(String lowerInclusive, String upperInclusive) {
        return fetchRange(MasterCategory.MASTER_CATEGORY.ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>id IN (values)</code>
     */
    public List<open.tresorier.generated.jooq.main.tables.pojos.MasterCategory> fetchById(String... values) {
        return fetch(MasterCategory.MASTER_CATEGORY.ID, values);
    }

    /**
     * Fetch a unique record that has <code>id = value</code>
     */
    public open.tresorier.generated.jooq.main.tables.pojos.MasterCategory fetchOneById(String value) {
        return fetchOne(MasterCategory.MASTER_CATEGORY.ID, value);
    }

    /**
     * Fetch a unique record that has <code>id = value</code>
     */
    public Optional<open.tresorier.generated.jooq.main.tables.pojos.MasterCategory> fetchOptionalById(String value) {
        return fetchOptional(MasterCategory.MASTER_CATEGORY.ID, value);
    }

    /**
     * Fetch records that have <code>budget_id BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<open.tresorier.generated.jooq.main.tables.pojos.MasterCategory> fetchRangeOfBudgetId(String lowerInclusive, String upperInclusive) {
        return fetchRange(MasterCategory.MASTER_CATEGORY.BUDGET_ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>budget_id IN (values)</code>
     */
    public List<open.tresorier.generated.jooq.main.tables.pojos.MasterCategory> fetchByBudgetId(String... values) {
        return fetch(MasterCategory.MASTER_CATEGORY.BUDGET_ID, values);
    }

    /**
     * Fetch records that have <code>name BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<open.tresorier.generated.jooq.main.tables.pojos.MasterCategory> fetchRangeOfName(String lowerInclusive, String upperInclusive) {
        return fetchRange(MasterCategory.MASTER_CATEGORY.NAME, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>name IN (values)</code>
     */
    public List<open.tresorier.generated.jooq.main.tables.pojos.MasterCategory> fetchByName(String... values) {
        return fetch(MasterCategory.MASTER_CATEGORY.NAME, values);
    }

    /**
     * Fetch records that have <code>deleted BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<open.tresorier.generated.jooq.main.tables.pojos.MasterCategory> fetchRangeOfDeleted(Boolean lowerInclusive, Boolean upperInclusive) {
        return fetchRange(MasterCategory.MASTER_CATEGORY.DELETED, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>deleted IN (values)</code>
     */
    public List<open.tresorier.generated.jooq.main.tables.pojos.MasterCategory> fetchByDeleted(Boolean... values) {
        return fetch(MasterCategory.MASTER_CATEGORY.DELETED, values);
    }

    /**
     * Fetch records that have <code>color BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<open.tresorier.generated.jooq.main.tables.pojos.MasterCategory> fetchRangeOfColor(String lowerInclusive, String upperInclusive) {
        return fetchRange(MasterCategory.MASTER_CATEGORY.COLOR, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>color IN (values)</code>
     */
    public List<open.tresorier.generated.jooq.main.tables.pojos.MasterCategory> fetchByColor(String... values) {
        return fetch(MasterCategory.MASTER_CATEGORY.COLOR, values);
    }
}
