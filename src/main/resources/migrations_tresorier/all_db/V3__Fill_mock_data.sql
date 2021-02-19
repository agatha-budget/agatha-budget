INSERT INTO
       person (id, email, name, password)
VALUES
        ('1', 'mail1', 'name1', 'password1'),
        ('2', 'mail2', 'name2', 'password2'),
        ('3', 'mail3', 'name3', 'password3'),
        ('4', 'mail4', 'name4', 'password4'),
        ('5', 'mail5', 'name5', 'password5')
;


INSERT INTO
       budget (id, person_id, name)
VALUES
        ('1', '1', 'budget1'),
        ('2', '2', 'budget2'),
        ('3', '3', 'budget3'),
        ('4', '4', 'budget4'),
        ('5', '5', 'budget5')
;


INSERT INTO
       account (id, budget_id, name)
VALUES
        ('1', '1', 'saving1'),
        ('2', '1', 'checking1'),
        ('3', '1', 'credit1'),
        ('4', '2', 'checking4'),
        ('5', '3', 'checking5')
;


INSERT INTO
       category (id, budget_id, name)
VALUES
        ('1', '1', 'grocery'),
        ('2', '1', 'fun'),
        ('3', '1', 'gift'),
        ('4', '2', 'grocery'),
        ('5', '3', 'rent')
;

INSERT INTO
       operation (id, operation_date, account_id, category_id, amount, memo)
VALUES
        ('1', 2541, '1', '1', 9874466.45, 'saving 1 grocery'),
        ('2', 2545, '1', '2', -8468456.45, 'saving 1 fun'),
        ('3', 2546, '2', '3', 0.25, 'checking 1 gift'),
        ('4', 2547, '4', '4', -0.25, 'checking 4 grocery'),
        ('5', 2548, '3', '3', 4.45, 'credit 1 gift')
;