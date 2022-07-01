/*
 * This file is generated by jOOQ.
 */
package open.tresorier.generated.jooq.main.tables.pojos;


import java.io.Serializable;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class BankAgreement implements Serializable {

    private static final long serialVersionUID = 857672008;

    private final String  id;
    private final String  personId;
    private final String  bankId;
    private final Long    validUntil;
    private final String  nordigenRequisitionId;
    private final Boolean archived;
    private final Boolean deleted;

    public BankAgreement(BankAgreement value) {
        this.id = value.id;
        this.personId = value.personId;
        this.bankId = value.bankId;
        this.validUntil = value.validUntil;
        this.nordigenRequisitionId = value.nordigenRequisitionId;
        this.archived = value.archived;
        this.deleted = value.deleted;
    }

    public BankAgreement(
        String  id,
        String  personId,
        String  bankId,
        Long    validUntil,
        String  nordigenRequisitionId,
        Boolean archived,
        Boolean deleted
    ) {
        this.id = id;
        this.personId = personId;
        this.bankId = bankId;
        this.validUntil = validUntil;
        this.nordigenRequisitionId = nordigenRequisitionId;
        this.archived = archived;
        this.deleted = deleted;
    }

    public String getId() {
        return this.id;
    }

    public String getPersonId() {
        return this.personId;
    }

    public String getBankId() {
        return this.bankId;
    }

    public Long getValidUntil() {
        return this.validUntil;
    }

    public String getNordigenRequisitionId() {
        return this.nordigenRequisitionId;
    }

    public Boolean getArchived() {
        return this.archived;
    }

    public Boolean getDeleted() {
        return this.deleted;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("BankAgreement (");

        sb.append(id);
        sb.append(", ").append(personId);
        sb.append(", ").append(bankId);
        sb.append(", ").append(validUntil);
        sb.append(", ").append(nordigenRequisitionId);
        sb.append(", ").append(archived);
        sb.append(", ").append(deleted);

        sb.append(")");
        return sb.toString();
    }
}