/*
 * This file is generated by jOOQ.
 */
package open.tresorier.generated.jooq.main.tables.records;


import open.tresorier.generated.jooq.main.tables.Category;

import org.jooq.Record1;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class CategoryRecord extends UpdatableRecordImpl<CategoryRecord> {

    private static final long serialVersionUID = 1L;

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

        setId(id);
        setMasterCategoryId(masterCategoryId);
        setName(name);
        setArchived(archived);
        setDeleted(deleted);
        resetTouchedOnNotNull();
    }

    /**
     * Create a detached, initialised CategoryRecord
     */
    public CategoryRecord(open.tresorier.generated.jooq.main.tables.pojos.Category value) {
        super(Category.CATEGORY);

        if (value != null) {
            setId(value.getId());
            setMasterCategoryId(value.getMasterCategoryId());
            setName(value.getName());
            setArchived(value.getArchived());
            setDeleted(value.getDeleted());
            resetTouchedOnNotNull();
        }
    }
}
