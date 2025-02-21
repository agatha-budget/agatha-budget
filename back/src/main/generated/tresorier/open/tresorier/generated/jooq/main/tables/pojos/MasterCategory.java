/*
 * This file is generated by jOOQ.
 */
package open.tresorier.generated.jooq.main.tables.pojos;


import java.io.Serializable;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class MasterCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String id;
    private final String budgetId;
    private final String name;
    private final Boolean deleted;
    private final String color;

    public MasterCategory(MasterCategory value) {
        this.id = value.id;
        this.budgetId = value.budgetId;
        this.name = value.name;
        this.deleted = value.deleted;
        this.color = value.color;
    }

    public MasterCategory(
        String id,
        String budgetId,
        String name,
        Boolean deleted,
        String color
    ) {
        this.id = id;
        this.budgetId = budgetId;
        this.name = name;
        this.deleted = deleted;
        this.color = color;
    }

    /**
     * Getter for <code>public.master_category.id</code>.
     */
    public String getId() {
        return this.id;
    }

    /**
     * Getter for <code>public.master_category.budget_id</code>.
     */
    public String getBudgetId() {
        return this.budgetId;
    }

    /**
     * Getter for <code>public.master_category.name</code>.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Getter for <code>public.master_category.deleted</code>.
     */
    public Boolean getDeleted() {
        return this.deleted;
    }

    /**
     * Getter for <code>public.master_category.color</code>.
     */
    public String getColor() {
        return this.color;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final MasterCategory other = (MasterCategory) obj;
        if (this.id == null) {
            if (other.id != null)
                return false;
        }
        else if (!this.id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("MasterCategory (");

        sb.append(id);
        sb.append(", ").append(budgetId);
        sb.append(", ").append(name);
        sb.append(", ").append(deleted);
        sb.append(", ").append(color);

        sb.append(")");
        return sb.toString();
    }
}
