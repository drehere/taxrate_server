-- we don't know how to generate schema bitmain (class Schema) :(
create table wxgroup
(
	id int auto_increment
		primary key,
	groupID varchar(100) not null,
	createTime timestamp default CURRENT_TIMESTAMP null comment 'CURRENT_TIMESTAMP()',
	constraint wxgroup_groupid_uindex
		unique (groupID)
)
comment ' 微信群表'
;

create table wxuser
(
	id int auto_increment
		primary key,
	openID varchar(100) not null,
	phone int null,
	nickName varchar(100) null,
	city varchar(40) null,
	unionID varchar(100) null,
	gender varchar(20) null,
	avatarUrl varchar(300) null,
	sessionKey varchar(100) null,
	constraint wxuser_openid_uindex
		unique (openID)
)
comment '微信用户表'
;

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
	updateTime timestamp default CURRENT_TIMESTAMP null,
	constraint taxdata_wxuser_id_fk
		foreign key (userID) references wxuser (id)
)
comment '税率计算数据表'
;

create index taxdata_wxuser_id_fk
	on taxdata (userID)
;

create table wxgroup_user
(
	id int auto_increment
		primary key,
	group_id int null,
	user_id int null,
	insert_time timestamp default CURRENT_TIMESTAMP null,
	constraint wxgroup_user_wxgroup_id_fk
		foreign key (group_id) references wxgroup (id),
	constraint wxgroup_user_wxuser_id_fk
		foreign key (user_id) references wxuser (id)
)
comment '微信群成员表'
;

create index wxgroup_user_wxgroup_id_fk
	on wxgroup_user (group_id)
;

create index wxgroup_user_wxuser_id_fk
	on wxgroup_user (user_id)
;

