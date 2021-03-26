create or replace function are_category_and_account_from_same_budget (_category_id VARCHAR(36), _accound_id VARCHAR(36))
  returns boolean as $$
declare
  	same_budget boolean;
  	universal_category boolean;
begin
   	select count(*) = 1 into same_budget
        from account acc
             join category cat
               on cat.budget_id = acc.budget_id
        where cat.id = _category_id
            and acc.id = _accound_id;

	select count(*) = 1 into universal_category
        from category cat
        where cat.id = _category_id
            and cat.budget_id is null;

    return same_budget or universal_category;
end;
$$ language plpgsql;

ALTER TABLE operation
-- BEWARE - this constraint will only prevent the creation of a wrong operation but if the budget_id is changed afterward, it won't be noticed.
ADD CONSTRAINT category_and_account_are_from_same_budget CHECK (are_category_and_account_from_same_budget(category_id, account_id));