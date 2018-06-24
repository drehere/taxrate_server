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

