create database Inventory;
use Inventory;

create table Object
(
	objectID int not null auto_increment,
	objectName varchar(50),
	objectPrice float,
	objectQR varchar(40),
	primary key(objectID)
);

create table Space
(
	spaceID int not null auto_increment,
	spaceName varchar(50),
	spaceQR varchar(40),
	primary key(spaceID)
);

create table ObjectSpace
(
	objectSpaceID int not null auto_increment,
	objectID int not null,
	spaceID int not null,
	primary key(objectSpaceID)
);

alter table ObjectSpace add constraint fKOSObject 
foreign key(objectID) 
references Object(objectID) 
on delete /*no action*/ cascade 
on update cascade;

alter table ObjectSpace add constraint fKOSSpace
foreign key(spaceID)
references Space(spaceID)
on delete cascade
on update cascade;

create table Detection
(
	detectionID int not null auto_increment,
	objectID int not null,
	spaceID int not null,
	captureDate date,
	captureTime time,
	primary key(detectionID)
);

alter table Detection add constraint fKDObject 
foreign key(objectID) 
references Object(objectID) 
on delete cascade 
on update cascade;

alter table Detection add constraint fKDSpace
foreign key(spaceID)
references Space(spaceID)
on delete cascade
on update cascade;

create table NotificationType
(
	notificationTypeID int not null auto_increment,
	description varchar(50),
	primary key(notificationTypeID)
);

create table Notification
(
	notificationID int not null auto_increment,
	objectID int not null,
	spaceID int not null,
	notificationTypeID int not null,
	limitAmount int,
	isActive boolean,
	primary key(notificationID)
);

alter table Notification add constraint fKNObject 
foreign key(objectID) 
references Object(objectID) 
on delete cascade 
on update cascade;

alter table Notification add constraint fkNSpace
foreign key(spaceID)
references Space(spaceID)
on delete cascade
on update cascade;

alter table Notification add constraint fkNNotificationType
foreign key(notificationTypeID)
references NotificationType(notificationTypeID)
on delete cascade
on update cascade;

insert into NotificationType values (null, "Limite de existencias"),
																		(null, "Ausencia");