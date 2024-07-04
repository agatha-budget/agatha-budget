/*
 * This file is generated by jOOQ.
 */
package open.tresorier.generated.jooq.test.information_schema.tables.pojos;


import java.io.Serializable;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class QueryStatistics implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String sqlStatement;
    private final Integer executionCount;
    private final Double minExecutionTime;
    private final Double maxExecutionTime;
    private final Double cumulativeExecutionTime;
    private final Double averageExecutionTime;
    private final Double stdDevExecutionTime;
    private final Long minRowCount;
    private final Long maxRowCount;
    private final Long cumulativeRowCount;
    private final Double averageRowCount;
    private final Double stdDevRowCount;

    public QueryStatistics(QueryStatistics value) {
        this.sqlStatement = value.sqlStatement;
        this.executionCount = value.executionCount;
        this.minExecutionTime = value.minExecutionTime;
        this.maxExecutionTime = value.maxExecutionTime;
        this.cumulativeExecutionTime = value.cumulativeExecutionTime;
        this.averageExecutionTime = value.averageExecutionTime;
        this.stdDevExecutionTime = value.stdDevExecutionTime;
        this.minRowCount = value.minRowCount;
        this.maxRowCount = value.maxRowCount;
        this.cumulativeRowCount = value.cumulativeRowCount;
        this.averageRowCount = value.averageRowCount;
        this.stdDevRowCount = value.stdDevRowCount;
    }

    public QueryStatistics(
        String sqlStatement,
        Integer executionCount,
        Double minExecutionTime,
        Double maxExecutionTime,
        Double cumulativeExecutionTime,
        Double averageExecutionTime,
        Double stdDevExecutionTime,
        Long minRowCount,
        Long maxRowCount,
        Long cumulativeRowCount,
        Double averageRowCount,
        Double stdDevRowCount
    ) {
        this.sqlStatement = sqlStatement;
        this.executionCount = executionCount;
        this.minExecutionTime = minExecutionTime;
        this.maxExecutionTime = maxExecutionTime;
        this.cumulativeExecutionTime = cumulativeExecutionTime;
        this.averageExecutionTime = averageExecutionTime;
        this.stdDevExecutionTime = stdDevExecutionTime;
        this.minRowCount = minRowCount;
        this.maxRowCount = maxRowCount;
        this.cumulativeRowCount = cumulativeRowCount;
        this.averageRowCount = averageRowCount;
        this.stdDevRowCount = stdDevRowCount;
    }

    /**
     * Getter for
     * <code>INFORMATION_SCHEMA.QUERY_STATISTICS.SQL_STATEMENT</code>.
     */
    public String getSqlStatement() {
        return this.sqlStatement;
    }

    /**
     * Getter for
     * <code>INFORMATION_SCHEMA.QUERY_STATISTICS.EXECUTION_COUNT</code>.
     */
    public Integer getExecutionCount() {
        return this.executionCount;
    }

    /**
     * Getter for
     * <code>INFORMATION_SCHEMA.QUERY_STATISTICS.MIN_EXECUTION_TIME</code>.
     */
    public Double getMinExecutionTime() {
        return this.minExecutionTime;
    }

    /**
     * Getter for
     * <code>INFORMATION_SCHEMA.QUERY_STATISTICS.MAX_EXECUTION_TIME</code>.
     */
    public Double getMaxExecutionTime() {
        return this.maxExecutionTime;
    }

    /**
     * Getter for
     * <code>INFORMATION_SCHEMA.QUERY_STATISTICS.CUMULATIVE_EXECUTION_TIME</code>.
     */
    public Double getCumulativeExecutionTime() {
        return this.cumulativeExecutionTime;
    }

    /**
     * Getter for
     * <code>INFORMATION_SCHEMA.QUERY_STATISTICS.AVERAGE_EXECUTION_TIME</code>.
     */
    public Double getAverageExecutionTime() {
        return this.averageExecutionTime;
    }

    /**
     * Getter for
     * <code>INFORMATION_SCHEMA.QUERY_STATISTICS.STD_DEV_EXECUTION_TIME</code>.
     */
    public Double getStdDevExecutionTime() {
        return this.stdDevExecutionTime;
    }

    /**
     * Getter for
     * <code>INFORMATION_SCHEMA.QUERY_STATISTICS.MIN_ROW_COUNT</code>.
     */
    public Long getMinRowCount() {
        return this.minRowCount;
    }

    /**
     * Getter for
     * <code>INFORMATION_SCHEMA.QUERY_STATISTICS.MAX_ROW_COUNT</code>.
     */
    public Long getMaxRowCount() {
        return this.maxRowCount;
    }

    /**
     * Getter for
     * <code>INFORMATION_SCHEMA.QUERY_STATISTICS.CUMULATIVE_ROW_COUNT</code>.
     */
    public Long getCumulativeRowCount() {
        return this.cumulativeRowCount;
    }

    /**
     * Getter for
     * <code>INFORMATION_SCHEMA.QUERY_STATISTICS.AVERAGE_ROW_COUNT</code>.
     */
    public Double getAverageRowCount() {
        return this.averageRowCount;
    }

    /**
     * Getter for
     * <code>INFORMATION_SCHEMA.QUERY_STATISTICS.STD_DEV_ROW_COUNT</code>.
     */
    public Double getStdDevRowCount() {
        return this.stdDevRowCount;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final QueryStatistics other = (QueryStatistics) obj;
        if (this.sqlStatement == null) {
            if (other.sqlStatement != null)
                return false;
        }
        else if (!this.sqlStatement.equals(other.sqlStatement))
            return false;
        if (this.executionCount == null) {
            if (other.executionCount != null)
                return false;
        }
        else if (!this.executionCount.equals(other.executionCount))
            return false;
        if (this.minExecutionTime == null) {
            if (other.minExecutionTime != null)
                return false;
        }
        else if (!this.minExecutionTime.equals(other.minExecutionTime))
            return false;
        if (this.maxExecutionTime == null) {
            if (other.maxExecutionTime != null)
                return false;
        }
        else if (!this.maxExecutionTime.equals(other.maxExecutionTime))
            return false;
        if (this.cumulativeExecutionTime == null) {
            if (other.cumulativeExecutionTime != null)
                return false;
        }
        else if (!this.cumulativeExecutionTime.equals(other.cumulativeExecutionTime))
            return false;
        if (this.averageExecutionTime == null) {
            if (other.averageExecutionTime != null)
                return false;
        }
        else if (!this.averageExecutionTime.equals(other.averageExecutionTime))
            return false;
        if (this.stdDevExecutionTime == null) {
            if (other.stdDevExecutionTime != null)
                return false;
        }
        else if (!this.stdDevExecutionTime.equals(other.stdDevExecutionTime))
            return false;
        if (this.minRowCount == null) {
            if (other.minRowCount != null)
                return false;
        }
        else if (!this.minRowCount.equals(other.minRowCount))
            return false;
        if (this.maxRowCount == null) {
            if (other.maxRowCount != null)
                return false;
        }
        else if (!this.maxRowCount.equals(other.maxRowCount))
            return false;
        if (this.cumulativeRowCount == null) {
            if (other.cumulativeRowCount != null)
                return false;
        }
        else if (!this.cumulativeRowCount.equals(other.cumulativeRowCount))
            return false;
        if (this.averageRowCount == null) {
            if (other.averageRowCount != null)
                return false;
        }
        else if (!this.averageRowCount.equals(other.averageRowCount))
            return false;
        if (this.stdDevRowCount == null) {
            if (other.stdDevRowCount != null)
                return false;
        }
        else if (!this.stdDevRowCount.equals(other.stdDevRowCount))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.sqlStatement == null) ? 0 : this.sqlStatement.hashCode());
        result = prime * result + ((this.executionCount == null) ? 0 : this.executionCount.hashCode());
        result = prime * result + ((this.minExecutionTime == null) ? 0 : this.minExecutionTime.hashCode());
        result = prime * result + ((this.maxExecutionTime == null) ? 0 : this.maxExecutionTime.hashCode());
        result = prime * result + ((this.cumulativeExecutionTime == null) ? 0 : this.cumulativeExecutionTime.hashCode());
        result = prime * result + ((this.averageExecutionTime == null) ? 0 : this.averageExecutionTime.hashCode());
        result = prime * result + ((this.stdDevExecutionTime == null) ? 0 : this.stdDevExecutionTime.hashCode());
        result = prime * result + ((this.minRowCount == null) ? 0 : this.minRowCount.hashCode());
        result = prime * result + ((this.maxRowCount == null) ? 0 : this.maxRowCount.hashCode());
        result = prime * result + ((this.cumulativeRowCount == null) ? 0 : this.cumulativeRowCount.hashCode());
        result = prime * result + ((this.averageRowCount == null) ? 0 : this.averageRowCount.hashCode());
        result = prime * result + ((this.stdDevRowCount == null) ? 0 : this.stdDevRowCount.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("QueryStatistics (");

        sb.append(sqlStatement);
        sb.append(", ").append(executionCount);
        sb.append(", ").append(minExecutionTime);
        sb.append(", ").append(maxExecutionTime);
        sb.append(", ").append(cumulativeExecutionTime);
        sb.append(", ").append(averageExecutionTime);
        sb.append(", ").append(stdDevExecutionTime);
        sb.append(", ").append(minRowCount);
        sb.append(", ").append(maxRowCount);
        sb.append(", ").append(cumulativeRowCount);
        sb.append(", ").append(averageRowCount);
        sb.append(", ").append(stdDevRowCount);

        sb.append(")");
        return sb.toString();
    }
}