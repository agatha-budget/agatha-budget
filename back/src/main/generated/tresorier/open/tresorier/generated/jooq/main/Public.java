/*
 * This file is generated by jOOQ.
 */
package open.tresorier.generated.jooq.main;


import java.util.Arrays;
import java.util.List;

import open.tresorier.generated.jooq.main.tables.Account;
import open.tresorier.generated.jooq.main.tables.Allocation;
import open.tresorier.generated.jooq.main.tables.BankAccount;
import open.tresorier.generated.jooq.main.tables.BankAgreement;
import open.tresorier.generated.jooq.main.tables.Budget;
import open.tresorier.generated.jooq.main.tables.Category;
import open.tresorier.generated.jooq.main.tables.FlywaySchemaHistory;
import open.tresorier.generated.jooq.main.tables.MasterCategory;
import open.tresorier.generated.jooq.main.tables.Operation;
import open.tresorier.generated.jooq.main.tables.Person;
import open.tresorier.generated.jooq.main.tables.UserActivity;

import org.jooq.Catalog;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.jooq.impl.SchemaImpl;


/**
 * standard public schema
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class Public extends SchemaImpl {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public</code>
     */
    public static final Public PUBLIC = new Public();

    /**
     * The table <code>public.account</code>.
     */
    public final Account ACCOUNT = Account.ACCOUNT;

    /**
     * The table <code>public.allocation</code>.
     */
    public final Allocation ALLOCATION = Allocation.ALLOCATION;

    /**
     * The table <code>public.bank_account</code>.
     */
    public final BankAccount BANK_ACCOUNT = BankAccount.BANK_ACCOUNT;

    /**
     * The table <code>public.bank_agreement</code>.
     */
    public final BankAgreement BANK_AGREEMENT = BankAgreement.BANK_AGREEMENT;

    /**
     * The table <code>public.budget</code>.
     */
    public final Budget BUDGET = Budget.BUDGET;

    /**
     * The table <code>public.category</code>.
     */
    public final Category CATEGORY = Category.CATEGORY;

    /**
     * The table <code>public.flyway_schema_history</code>.
     */
    public final FlywaySchemaHistory FLYWAY_SCHEMA_HISTORY = FlywaySchemaHistory.FLYWAY_SCHEMA_HISTORY;

    /**
     * The table <code>public.master_category</code>.
     */
    public final MasterCategory MASTER_CATEGORY = MasterCategory.MASTER_CATEGORY;

    /**
     * The table <code>public.operation</code>.
     */
    public final Operation OPERATION = Operation.OPERATION;

    /**
     * The table <code>public.person</code>.
     */
    public final Person PERSON = Person.PERSON;

    /**
     * The table <code>public.user_activity</code>.
     */
    public final UserActivity USER_ACTIVITY = UserActivity.USER_ACTIVITY;

    /**
     * No further instances allowed
     */
    private Public() {
        super(DSL.name("public"), null, DSL.comment("standard public schema"));
    }


    @Override
    public Catalog getCatalog() {
        return DefaultCatalog.DEFAULT_CATALOG;
    }

    @Override
    public final List<Table<?>> getTables() {
        return Arrays.asList(
            Account.ACCOUNT,
            Allocation.ALLOCATION,
            BankAccount.BANK_ACCOUNT,
            BankAgreement.BANK_AGREEMENT,
            Budget.BUDGET,
            Category.CATEGORY,
            FlywaySchemaHistory.FLYWAY_SCHEMA_HISTORY,
            MasterCategory.MASTER_CATEGORY,
            Operation.OPERATION,
            Person.PERSON,
            UserActivity.USER_ACTIVITY
        );
    }
}
