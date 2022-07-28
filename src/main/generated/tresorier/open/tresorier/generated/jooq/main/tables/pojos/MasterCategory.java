/*
 * This file is generated by jOOQ.
 */
package open.tresorier.generated.jooq.main.tables.pojos;


import java.io.Serializable;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class MasterCategory implements Serializable {

    private static final long serialVersionUID = 1823486214;

    private final String  id;
    private final String  budgetId;
    private final String  name;
    private final Boolean deleted;

    public MasterCategory(MasterCategory value) {
        this.id = value.id;
        this.budgetId = value.budgetId;
        this.name = value.name;
        this.deleted = value.deleted;
    }

    public MasterCategory(
        String  id,
        String  budgetId,
        String  name,
        Boolean deleted
    ) {
        this.id = id;
        this.budgetId = budgetId;
        this.name = name;
        this.deleted = deleted;
    }

    public String getId() {
        return this.id;
    }

    public String getBudgetId() {
        return this.budgetId;
    }

    public String getName() {
        return this.name;
    }

    public Boolean getDeleted() {
        return this.deleted;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("MasterCategory (");

        sb.append(id);
        sb.append(", ").append(budgetId);
        sb.append(", ").append(name);
        sb.append(", ").append(deleted);

        sb.append(")");
        return sb.toString();
    }
}