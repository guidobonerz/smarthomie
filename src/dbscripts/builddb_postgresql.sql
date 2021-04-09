drop table calendar."action";
CREATE TABLE calendar."action" (
  "key" varchar(100) primary key,
  command varchar(100) ,
  randomize numeric(11) ,
  "value" numeric(11) 
);

drop table calendar.category;
CREATE TABLE calendar.category (
  id serial primary key,
  description varchar(45) NOT NULL,
  color varchar(7)  ,
  icon varchar(45)  ,
  can_have_reminder smallint 
) ;

drop table calendar.dynamic_event;
CREATE TABLE calendar.dynamic_event (
  id varchar(100) NOT NULL PRIMARY KEY,
  target_date varchar(20) NOT NULL
);

drop table calendar.event;
CREATE TABLE calendar.event (
  id serial primary key,
  group_id varchar(100) NOT NULL,
  start_rule varchar(20),
  end_rule varchar(20),
  description varchar(200),
  all_day_event bool ,
  category_id numeric(11) NOT NULL,
  action_id numeric(11),
  payload varchar(500)
);

drop table calendar.reminder;
CREATE TABLE calendar.reminder (
  id serial ,
  reminder_id numeric(11)  NOT NULL,
  "value" numeric(11) NOT NULL,
  unit_id numeric(11) NOT NULL,
  PRIMARY KEY (id,reminder_id)
) ;

drop table calendar.devices;
CREATE TABLE calendar.devices (
	id serial NOT NULL,
	"name" varchar NULL,
	"key" varchar NULL,
	category varchar NULL,
	ip_address varchar NULL,
	"user" varchar NULL,
	auth varchar NULL
);

drop view calendar.event_view;
create view calendar.event_view as
select
    e.group_id AS group_id,
    cast (case when des.target_date is not null then des.target_date else replace(e.start_rule,'{TODAY}',to_char(now(),'YYYY-MM-DD')) end as timestamp) as start_rule,
    cast (case when dee.target_date is not null then dee.target_date else replace(e.end_rule,'{TODAY}',to_char(now(),'YYYY-MM-DD')) end as timestamp) as end_rule,
    e.description AS description,
    e.all_day_event AS all_day_event,
    e.category_id AS category_id,
    a.command AS action_command,
    date_part('day', cast (case when des.target_date is not null then des.target_date else replace(e.start_rule,'{TODAY}',to_char(now(),'YYYY-MM-DD')) end as date)-now()) AS diff,
      
    (case   when date_part('day',cast (case when des.target_date is not null then des.target_date else replace(e.start_rule,'{TODAY}',to_char(now(),'YYYY-MM-DD')) end as date)-now())= 0 then 'today'
            when date_part('day',cast (case when des.target_date is not null then des.target_date else replace(e.start_rule,'{TODAY}',to_char(now(),'YYYY-MM-DD')) end as date)-now() )= 1 then 'tomorrow'
            when date_part('day',cast (case when des.target_date is not null then des.target_date else replace(e.start_rule,'{TODAY}',to_char(now(),'YYYY-MM-DD')) end as date)-now() )> 1 and date_part('day',to_date( replace(e.start_rule,'{TODAY}',to_char(now(),'YYYY-MM-DD')), 'YYYY-MM-DD' )-now() ) < 15 then 'upcoming'
            when date_part('day',cast (case when des.target_date is not null then des.target_date else replace(e.start_rule,'{TODAY}',to_char(now(),'YYYY-MM-DD')) end as date)-now() )< 0 then 'over' else 'sometime'   end ) AS occurrence,
     e.payload 
 	
from (calendar.event e  left join calendar.dynamic_event des on (e.start_rule = des.id) 
						left join calendar.dynamic_event dee on (e.end_rule = dee.id)
						left join calendar."action" a on (e.action_id=a.id)) 
order by cast (case when des.target_date is not null then des.target_date else replace(e.start_rule,'{TODAY}',to_char(now(),'YYYY-MM-DD')) end as date) asc;

insert into calendar.event (group_id,start_rule,end_rule,description,all_day_event,category_id,action_id,payload) values ('Licht',':TODAY 05:00:00',':SUNRISE','FloorLamp',false,-1,-1,null);
insert into calendar.event (group_id,start_rule,end_rule,description,all_day_event,category_id,action_id,payload) values ('Licht',':SUNSET',':TOMORROW 00:00:00','FloorLamp',false,-1,-1,null);
insert into calendar.event (group_id,start_rule,end_rule,description,all_day_event,category_id,action_id,payload) values ('Licht',':SUNSET',':TOMORROW 00:00:00','LivingroomLamp',false,-1,-1,null);
insert into calendar.event (group_id,start_rule,end_rule,description,all_day_event,category_id,action_id,payload) values ('Boiler',':WORKDAY 06:30:00',null,'Boiler',false,-1,-1,'50.0');
insert into calendar.event (group_id,start_rule,end_rule,description,all_day_event,category_id,action_id,payload) values ('Boiler',':WORKDAY 16:30:00',null,'Boiler',false,-1,-1,'50.0');
insert into calendar.event (group_id,start_rule,end_rule,description,all_day_event,category_id,action_id,payload) values ('Boiler',':WEEKEND 07:30:00',null,'Boiler',false,-1,-1,'50.0');
insert into calendar.event (group_id,start_rule,end_rule,description,all_day_event,category_id,action_id,payload) values ('Boiler',':WEEKEND 16:30:00',null,'Boiler',false,-1,-1,'50.0');
insert into calendar.event (group_id,start_rule,end_rule,description,all_day_event,category_id,action_id,payload) values ('Boiler',':BOOST 01:00:00',null,'Boiler',false,-1,-1,'70.0');

INSERT INTO calendar.category (cid,description,color,icon,can_have_reminder) VALUES (4,'Rote Tonne (Restmüll)','#ff0000',NULL,NULL);
INSERT INTO calendar.category (cid,description,color,icon,can_have_reminder) VALUES (5,'Braune Tonne (Biomüll)','#ffff00',NULL,NULL);
INSERT INTO calendar.category (cid,description,color,icon,can_have_reminder) VALUES (6,'Gelbe Tonne (Verpackungsmüll)','#523b35',NULL,NULL);
INSERT INTO calendar.category (cid,description,color,icon,can_have_reminder) VALUES (10,'Sperrmüll','#000000',NULL,NULL);
INSERT INTO calendar.category (cid,description,color,icon,can_have_reminder) VALUES (14,'Blaue Tonne(Altpapier)','#0000ff',NULL,NULL);
INSERT INTO calendar.category (cid,description,color,icon,can_have_reminder) VALUES (11,'Feiertage','#000000',NULL,NULL);
INSERT INTO calendar.category (cid,description,color,icon,can_have_reminder) VALUES (13,'Ferien','#000000',NULL,NULL);
INSERT INTO calendar.category (cid,description,color,icon,can_have_reminder) VALUES (20,'Geburtstage','#000000',NULL,NULL);
INSERT INTO calendar.category (cid,description,color,icon,can_have_reminder) VALUES (21,'Urlaub','#000000',NULL,NULL);
INSERT INTO calendar.category (cid,description,color,icon,can_have_reminder) VALUES (22,'Termin',NULL,NULL,NULL);


INSERT INTO calendar.devices (location,"name","key",category,ip_address,"user",auth) VALUES ('Flur','Flur','corridor','camera','10.100.200.171',NULL,NULL);
INSERT INTO calendar.devices (location,"name","key",category,ip_address,"user",auth) VALUES ('Hauseingang','Hauseingang','entry','camera','10.100.200.170',NULL,NULL);
INSERT INTO calendar.devices (location,"name","key",category,ip_address,"user",auth) VALUES ('Flur','Stehlampe','corridor1','lamp','10.100.200.140',NULL,NULL);
INSERT INTO calendar.devices (location,"name","key",category,ip_address,"user",auth) VALUES ('Wohnzimmer','Stehlampe','livingroom1','lamp','10.100.200.141',NULL,NULL);
INSERT INTO calendar.devices (location,"name","key",category,ip_address,"user",auth) VALUES ('Wohnzimmer','Baumlampe','livingroom','lamp','10.100.200.142',NULL,NULL);

