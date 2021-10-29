ALTER TABLE operation
-- BEWARE - this constraint will only prevent the creation of a wrong operation but if the budget_id is changed afterward, it won't be noticed.
REMOVE CONSTRAINT category_and_account_are_from_same_budget;