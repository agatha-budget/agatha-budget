INSERT INTO
       person (id, email, name, password)
VALUES
        ('person1', 'mail1', 'name1', 'password1'),
        ('person2', 'mail2', 'name2', 'password2'),
        ('person3', 'mail3', 'name3', 'password3'),
        ('person4', 'mail4', 'name4', 'password4'),
        ('person5', 'mail5', 'name5', 'password5')
;


INSERT INTO
       budget (id, person_id, name)
VALUES
        ('budget1', 'person1', 'budget 1'),
        ('budget2', 'person2', 'budget 2'),
        ('budget3', 'person3', 'budget 3'),
        ('budget4', 'person4', 'budget 4'),
        ('budget5', 'person5', 'budget 5')
;


INSERT INTO
       account (id, budget_id, name)
VALUES
        ('account1', 'budget1', 'saving of 1'),
        ('account2', 'budget1', 'checking of 1'),
        ('account3', 'budget1', 'credit of 1'),
        ('account4', 'budget2', 'checking of 2'),
        ('account5', 'budget3', 'checking of 3')
;

INSERT INTO
       master_category (id, budget_id, name)
VALUES
        ('master_category1', 'budget1', 'variable of 1'),
        ('master_category2', 'budget1', 'fixed of 1'),
        ('master_category3', 'budget1', 'goals of 1'),
        ('master_category4', 'budget2', 'variable of 2'),
        ('master_category5', 'budget3', 'fixed of 3')
;


INSERT INTO
       category (id, master_category_id, name)
VALUES
        ('category1', 'master_category1', 'grocery of 1'),
        ('category2', 'master_category1', 'fun of 1'),
        ('category3', 'master_category1', 'gift of 1'),
        ('category4', 'master_category4', 'grocery of 2'),
        ('category5', 'master_category5', 'rent of 3')
;

INSERT INTO
       operation (id, month, day, account_id, category_id, amount, memo)
VALUES
        ('operation0', 202102, 3, 'account1', 'universal_income_category', 1000154, 'income'),
        ('operation1', 202102, 3, 'account1', 'category1', 9874466.45, 'saving 1 grocery'),
        ('operation2', 202103, 4, 'account1', 'category2', -8468456.45, 'saving 1 fun'),
        ('operation3', 202103, 8, 'account2', 'category3', 0.25, 'checking 1 gift'),
        ('operation4', 202105, 24, 'account4', 'category4', -0.25, 'checking 4 grocery'),
        ('operation5', 202108, 31, 'account3', 'category3', 4.45, 'credit 1 gift'),
        ('operation6', null, null, 'account1', null, 12.00, null)
;

INSERT INTO
       allocation (id, category_id, month, amount)
VALUES
        ('allocation1', 'category1', 202102, 9874466.45),
        ('allocation2', 'category2', 202101, -8468456.45),
        ('allocation3', 'category4', 202012, 0.25),
        ('allocation4', 'category5', 202011, -0.25),
        ('allocation5', 'category2', 202010, 4.45)
;