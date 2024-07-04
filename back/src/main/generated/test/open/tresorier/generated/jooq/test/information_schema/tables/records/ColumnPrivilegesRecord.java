/*
 * This file is generated by jOOQ.
 */
package open.tresorier.generated.jooq.test.information_schema.tables.records;


import open.tresorier.generated.jooq.test.information_schema.tables.ColumnPrivileges;

import org.jooq.impl.TableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class ColumnPrivilegesRecord extends TableRecordImpl<ColumnPrivilegesRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>INFORMATION_SCHEMA.COLUMN_PRIVILEGES.GRANTOR</code>.
     */
    public ColumnPrivilegesRecord setGrantor(String value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>INFORMATION_SCHEMA.COLUMN_PRIVILEGES.GRANTOR</code>.
     */
    public String getGrantor() {
        return (String) get(0);
    }

    /**
     * Setter for <code>INFORMATION_SCHEMA.COLUMN_PRIVILEGES.GRANTEE</code>.
     */
    public ColumnPrivilegesRecord setGrantee(String value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>INFORMATION_SCHEMA.COLUMN_PRIVILEGES.GRANTEE</code>.
     */
    public String getGrantee() {
        return (String) get(1);
    }

    /**
     * Setter for
     * <code>INFORMATION_SCHEMA.COLUMN_PRIVILEGES.TABLE_CATALOG</code>.
     */
    public ColumnPrivilegesRecord setTableCatalog(String value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for
     * <code>INFORMATION_SCHEMA.COLUMN_PRIVILEGES.TABLE_CATALOG</code>.
     */
    public String getTableCatalog() {
        return (String) get(2);
    }

    /**
     * Setter for
     * <code>INFORMATION_SCHEMA.COLUMN_PRIVILEGES.TABLE_SCHEMA</code>.
     */
    public ColumnPrivilegesRecord setTableSchema(String value) {
        set(3, value);
        return this;
    }

    /**
     * Getter for
     * <code>INFORMATION_SCHEMA.COLUMN_PRIVILEGES.TABLE_SCHEMA</code>.
     */
    public String getTableSchema() {
        return (String) get(3);
    }

    /**
     * Setter for <code>INFORMATION_SCHEMA.COLUMN_PRIVILEGES.TABLE_NAME</code>.
     */
    public ColumnPrivilegesRecord setTableName(String value) {
        set(4, value);
        return this;
    }

    /**
     * Getter for <code>INFORMATION_SCHEMA.COLUMN_PRIVILEGES.TABLE_NAME</code>.
     */
    public String getTableName() {
        return (String) get(4);
    }

    /**
     * Setter for <code>INFORMATION_SCHEMA.COLUMN_PRIVILEGES.COLUMN_NAME</code>.
     */
    public ColumnPrivilegesRecord setColumnName(String value) {
        set(5, value);
        return this;
    }

    /**
     * Getter for <code>INFORMATION_SCHEMA.COLUMN_PRIVILEGES.COLUMN_NAME</code>.
     */
    public String getColumnName() {
        return (String) get(5);
    }

    /**
     * Setter for
     * <code>INFORMATION_SCHEMA.COLUMN_PRIVILEGES.PRIVILEGE_TYPE</code>.
     */
    public ColumnPrivilegesRecord setPrivilegeType(String value) {
        set(6, value);
        return this;
    }

    /**
     * Getter for
     * <code>INFORMATION_SCHEMA.COLUMN_PRIVILEGES.PRIVILEGE_TYPE</code>.
     */
    public String getPrivilegeType() {
        return (String) get(6);
    }

    /**
     * Setter for
     * <code>INFORMATION_SCHEMA.COLUMN_PRIVILEGES.IS_GRANTABLE</code>.
     */
    public ColumnPrivilegesRecord setIsGrantable(String value) {
        set(7, value);
        return this;
    }

    /**
     * Getter for
     * <code>INFORMATION_SCHEMA.COLUMN_PRIVILEGES.IS_GRANTABLE</code>.
     */
    public String getIsGrantable() {
        return (String) get(7);
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached ColumnPrivilegesRecord
     */
    public ColumnPrivilegesRecord() {
        super(ColumnPrivileges.COLUMN_PRIVILEGES);
    }

    /**
     * Create a detached, initialised ColumnPrivilegesRecord
     */
    public ColumnPrivilegesRecord(String grantor, String grantee, String tableCatalog, String tableSchema, String tableName, String columnName, String privilegeType, String isGrantable) {
        super(ColumnPrivileges.COLUMN_PRIVILEGES);

        setGrantor(grantor);
        setGrantee(grantee);
        setTableCatalog(tableCatalog);
        setTableSchema(tableSchema);
        setTableName(tableName);
        setColumnName(columnName);
        setPrivilegeType(privilegeType);
        setIsGrantable(isGrantable);
        resetChangedOnNotNull();
    }

    /**
     * Create a detached, initialised ColumnPrivilegesRecord
     */
    public ColumnPrivilegesRecord(open.tresorier.generated.jooq.test.information_schema.tables.pojos.ColumnPrivileges value) {
        super(ColumnPrivileges.COLUMN_PRIVILEGES);

        if (value != null) {
            setGrantor(value.getGrantor());
            setGrantee(value.getGrantee());
            setTableCatalog(value.getTableCatalog());
            setTableSchema(value.getTableSchema());
            setTableName(value.getTableName());
            setColumnName(value.getColumnName());
            setPrivilegeType(value.getPrivilegeType());
            setIsGrantable(value.getIsGrantable());
            resetChangedOnNotNull();
        }
    }
}