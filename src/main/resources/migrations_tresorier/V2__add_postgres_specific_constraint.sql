create or replace function are_category_and_account_from_same_budget (_category_id VARCHAR(36), _accound_id VARCHAR(36))
  returns boolean as $$
  select count(*) = 1
    from account acc
         join category cat
           on cat.budget_id = acc.budget_id
   where cat.id = _category_id
        and acc.id = _accound_id;
$$ language sql;

create or replace function is_either_inflow_or_outflow_null_but_not_both (_inflow decimal, _outflow decimal)
    returns boolean as $$
    SELECT
     (
        ((_inflow IS NOT NULL) OR (_outflow IS NOT NULL))
        AND
        ((_inflow IS NULL) OR (_outflow IS NULL))
     ) as result ;
$$ language sql;

ALTER TABLE operation
ADD CONSTRAINT either_inflow_or_outflow_is_null_but_not_both CHECK (is_either_inflow_or_outflow_null_but_not_both(inflow, outflow)),
-- BEWARE - this constraint will only prevent the creation of a wrong operation but if the budget_id is changed afterward, it won't be noticed.
ADD CONSTRAINT category_and_account_are_from_same_budget CHECK (are_category_and_account_from_same_budget(category_id, account_id));


ALTER TABLE allocation
ADD CONSTRAINT either_inflow_or_outflow_is_null_but_not_both CHECK (is_either_inflow_or_outflow_null_but_not_both(inflow, outflow));