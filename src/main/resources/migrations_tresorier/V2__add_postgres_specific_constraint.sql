create or replace function are_category_and_account_from_same_budget (_category_id VARCHAR(36), _account_id VARCHAR(36))
  returns boolean as $$
declare
  	same_budget boolean;
  	universal_category boolean;
begin
    -- get the master category of the given category and assert its budget is the same than the given account's
    if _category_id is null then
        return true;
    else
        select count(*) = 1 into same_budget
            from account acc
            join master_category master_cat
                on master_cat.budget_id = acc.budget_id
            join category cat
                on cat.master_category_id = master_cat.id
            where cat.id = _category_id
                and acc.id = _account_id;

        -- if the master_category of the given category is null it means it is an universal category (like income or transfert)
        select count(*) = 1 into universal_category
            from category cat
            where cat.id = _category_id
                and cat.master_category_id is null;

        return same_budget or universal_category;
    end if;
end;
$$ language plpgsql;

ALTER TABLE operation
-- BEWARE - this constraint will only prevent the creation of a wrong operation but if the budget_id is changed afterward, it won't be noticed.
ADD CONSTRAINT category_and_account_are_from_same_budget CHECK (are_category_and_account_from_same_budget(category_id, account_id));