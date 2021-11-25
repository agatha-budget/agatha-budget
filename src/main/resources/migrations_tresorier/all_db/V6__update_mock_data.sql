UPDATE person
SET person.billing_status = true
WHERE person.id in ('person1', 'person2', 'person3', 'person4', 'person5');