/*
 * This file is generated by jOOQ.
 */
package open.tresorier.generated.jooq.main.tables.records;


import open.tresorier.generated.jooq.main.tables.Category;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record5;
import org.jooq.Row5;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class CategoryRecord extends UpdatableRecordImpl<CategoryRecord> implements Record5<String, String, String, Boolean, Boolean> {

    private static final long serialVersionUID = 2125377478;

    /**
     * Setter for <code>public.category.id</code>.
     */
    public CategoryRecord setId(String value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>public.category.id</code>.
     */
    public String getId() {
        return (String) get(0);
    }

    /**
     * Setter for <code>public.category.master_category_id</code>.
     */
    public CategoryRecord setMasterCategoryId(String value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>public.category.master_category_id</code>.
     */
    public String getMasterCategoryId() {
        return (String) get(1);
    }

    /**
     * Setter for <code>public.category.name</code>.
     */
    public CategoryRecord setName(String value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>public.category.name</code>.
     */
    public String getName() {
        return (String) get(2);
    }

    /**
     * Setter for <code>public.category.archived</code>.
     */
    public CategoryRecord setArchived(Boolean value) {
        set(3, value);
        return this;
    }

    /**
     * Getter for <code>public.category.archived</code>.
     */
    public Boolean getArchived() {
        return (Boolean) get(3);
    }

    /**
     * Setter for <code>public.category.deleted</code>.
     */
    public CategoryRecord setDeleted(Boolean value) {
        set(4, value);
        return this;
    }

    /**
     * Getter for <code>public.category.deleted</code>.
     */
    public Boolean getDeleted() {
        return (Boolean) get(4);
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
    public Row5<String, String, String, Boolean, Boolean> fieldsRow() {
        return (Row5) super.fieldsRow();
    }

    @Override
    public Row5<String, String, String, Boolean, Boolean> valuesRow() {
        return (Row5) super.valuesRow();
    }

    @Override
    public Field<String> field1() {
        return Category.CATEGORY.ID;
    }

    @Override
    public Field<String> field2() {
        return Category.CATEGORY.MASTER_CATEGORY_ID;
    }

    @Override
    public Field<String> field3() {
        return Category.CATEGORY.NAME;
    }

    @Override
    public Field<Boolean> field4() {
        return Category.CATEGORY.ARCHIVED;
    }

    @Override
    public Field<Boolean> field5() {
        return Category.CATEGORY.DELETED;
    }

    @Override
    public String component1() {
        return getId();
    }

    @Override
    public String component2() {
        return getMasterCategoryId();
    }

    @Override
    public String component3() {
        return getName();
    }

    @Override
    public Boolean component4() {
        return getArchived();
    }

    @Override
    public Boolean component5() {
        return getDeleted();
    }

    @Override
    public String value1() {
        return getId();
    }

    @Override
    public String value2() {
        return getMasterCategoryId();
    }

    @Override
    public String value3() {
        return getName();
    }

    @Override
    public Boolean value4() {
        return getArchived();
    }

    @Override
    public Boolean value5() {
        return getDeleted();
    }

    @Override
    public CategoryRecord value1(String value) {
        setId(value);
        return this;
    }

    @Override
    public CategoryRecord value2(String value) {
        setMasterCategoryId(value);
        return this;
    }

    @Override
    public CategoryRecord value3(String value) {
        setName(value);
        return this;
    }

    @Override
    public CategoryRecord value4(Boolean value) {
        setArchived(value);
        return this;
    }

    @Override
    public CategoryRecord value5(Boolean value) {
        setDeleted(value);
        return this;
    }

    @Override
    public CategoryRecord values(String value1, String value2, String value3, Boolean value4, Boolean value5) {
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
     * Create a detached CategoryRecord
     */
    public CategoryRecord() {
        super(Category.CATEGORY);
    }

    /**
     * Create a detached, initialised CategoryRecord
     */
    public CategoryRecord(String id, String masterCategoryId, String name, Boolean archived, Boolean deleted) {
        super(Category.CATEGORY);

        set(0, id);
        set(1, masterCategoryId);
        set(2, name);
        set(3, archived);
        set(4, deleted);
    }
}