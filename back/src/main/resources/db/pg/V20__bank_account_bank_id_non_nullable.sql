UPDATE bank_account
SET bank_id=id
WHERE bank_id is NULL;

ALTER TABLE bank_account
ALTER bank_id SET NOT NULL