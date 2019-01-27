
--drop view calendar.event_view;
drop table calendar."action";
drop table calendar.category;
drop table calendar.dynamic_event;
drop table calendar.event;
drop table calendar.reminder;

CREATE TABLE calendar."action" (
  id serial primary key,
  command varchar(100) ,
  randomize numeric(11) ,
  "value" numeric(11) 
);


CREATE TABLE calendar.category (
  id serial primary key,
  description varchar(45) NOT NULL,
  color varchar(7)  ,
  icon varchar(45)  ,
  can_have_reminder smallint 
) ;


CREATE TABLE calendar.dynamic_event (
  id varchar(100) NOT NULL PRIMARY KEY,
  target_date varchar(20) NOT NULL
);

CREATE TABLE calendar.event (
  id serial primary key,
  group_id varchar(100) NOT NULL,
  start_rule varchar(20),
  end_rule varchar(20),
  description varchar(200),
  all_day_event bool ,
  category_id numeric(11) NOT NULL,
  action_id numeric(11)
);


CREATE TABLE calendar.reminder (
  id serial ,
  reminder_id numeric(11)  NOT NULL,
  "value" numeric(11) NOT NULL,
  unit_id numeric(11) NOT NULL,
  PRIMARY KEY (id,reminder_id)
) ;

/*
create view calendar.event_view as
select
    e.group_id AS group_id,
    case when e.start_rule is not null then sr.target_date else replace(e.start_date,'{TODAY}',to_char(now(),'YYYY-MM-DD')) end as start_date,
    case when e.end_rule is not null then er.target_date else replace(e.end_date,'{TODAY}',to_char(now(),'YYYY-MM-DD')) end as end_date,
    e.description AS description,
    e.all_day_event AS all_day_event,
    e.category_id AS category_id,
    a.command AS action_command,
    date_part('day', to_date( replace(e.start_date,'{TODAY}',to_char(now(),'YYYY-MM-DD')), 'YYYY-MM-DD' )-now()) AS diff,
    (case   when date_part('day',to_date( replace(e.start_date,'{TODAY}',to_char(now(),'YYYY-MM-DD')), 'YYYY-MM-DD' )-now())= 0 then 'today'
            when date_part('day',to_date( replace(e.start_date,'{TODAY}',to_char(now(),'YYYY-MM-DD')), 'YYYY-MM-DD' )-now() )= 1 then 'tomorrow'
            when date_part('day',to_date( replace(e.start_date,'{TODAY}',to_char(now(),'YYYY-MM-DD')), 'YYYY-MM-DD' )-now() )> 1 and date_part('day',to_date( replace(e.start_date,'{TODAY}',to_char(now(),'YYYY-MM-DD')), 'YYYY-MM-DD' )-now() ) < 15 then 'upcoming'
            when date_part('day',to_date( replace(e.start_date,'{TODAY}',to_char(now(),'YYYY-MM-DD')), 'YYYY-MM-DD' )-now() )< 0 then 'over' else 'sometime'   end ) AS occurrence,
 	e.start_rule,
 	e.end_rule
from (calendar.event e  left join calendar.rule sr on (e.start_rule = sr.id) 
						left join calendar.rule er on (e.end_rule = er.id)
						left join calendar."action" a on (e.action_id=a.id)) 
order by to_date(replace(e.start_date,'{TODAY}',to_char(now(),'YYYY-MM-DD')),'YYYY-MM-DD');
*/
insert into calendar.event (group_id,start_rule,end_rule,description,all_day_event,category_id,action_id) values ('Licht','{TODAY} 05:00:00','sunrise','FloorLamp',false,-1,-1);
insert into calendar.event (group_id,start_rule,end_rule,description,all_day_event,category_id,action_id) values ('Licht','sunset','{TODAY} 23:59:59','FloorLamp',false,-1,-1);
insert into calendar.event (group_id,start_rule,end_rule,description,all_day_event,category_id,action_id) values ('Licht','sunset','{TODAY} 23:59:59','LivingroomLamp',false,-1,-1);
