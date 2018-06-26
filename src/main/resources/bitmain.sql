create table taxdata
(
	id int auto_increment
		primary key,
	userID int null,
	grossPay int null,
	fee int null,
	tax int null,
	realSalary int null,
	resultDesc varchar(100) null,
	updateTime DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	constraint taxdata_wxuser_id_fk
		foreign key (userID) references wxuser (id)
)
comment '税率计算数据表'
;

create index taxdata_wxuser_id_fk
	on taxdata (userID)
;

