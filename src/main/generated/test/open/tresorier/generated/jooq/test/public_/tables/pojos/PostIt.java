/*
 * This file is generated by jOOQ.
 */
package open.tresorier.generated.jooq.test.public_.tables.pojos;


import java.io.Serializable;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class PostIt implements Serializable {

    private static final long serialVersionUID = 2051749551;

    private final String  budgetId;
    private final Integer month;
    private final String  text;

    public PostIt(PostIt value) {
        this.budgetId = value.budgetId;
        this.month = value.month;
        this.text = value.text;
    }

    public PostIt(
        String  budgetId,
        Integer month,
        String  text
    ) {
        this.budgetId = budgetId;
        this.month = month;
        this.text = text;
    }

    public String getBudgetId() {
        return this.budgetId;
    }

    public Integer getMonth() {
        return this.month;
    }

    public String getText() {
        return this.text;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("PostIt (");

        sb.append(budgetId);
        sb.append(", ").append(month);
        sb.append(", ").append(text);

        sb.append(")");
        return sb.toString();
    }
}