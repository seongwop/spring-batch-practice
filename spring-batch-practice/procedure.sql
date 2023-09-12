DELIMITER //
create procedure bulkInsert()
begin
declare i int default 1;
declare random_amount bigint default 1;
while (i <= 200000) do
set random_amount = floor(1000 + (rand() * 9000));
insert into `point_reservation`
(point_wallet_id, amount, earned_date, available_days, is_executed)
values (9, random_amount, '2023-09-12', 10, 0);
set i = i+1;
end while;
end;
//
DELIMITER ;