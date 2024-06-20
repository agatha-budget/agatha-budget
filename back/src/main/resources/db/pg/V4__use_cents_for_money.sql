ALTER TABLE operation
ADD COLUMN amountInt INT NOT NULL DEFAULT 0;

UPDATE operation
SET amountInt = operation.amount * 100;

ALTER TABLE operation
DROP COLUMN amount;

ALTER TABLE operation
RENAME COLUMN amountInt TO amount;

ALTER TABLE allocation
ADD COLUMN amountInt INT NOT NULL DEFAULT 0;

UPDATE allocation
SET amountInt = allocation.amount * 100;

ALTER TABLE allocation
DROP COLUMN amount;

ALTER TABLE allocation
RENAME COLUMN amountInt TO amount;