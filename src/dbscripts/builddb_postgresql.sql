CREATE TABLE calendar.action (
  id serial primary key,
  command varchar(10) ,
  randomize numeric(11) ,
  value numeric(11) 
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
  start_date varchar(10) NOT NULL,
  start_time varchar(10) NOT NULL
) ;

CREATE TABLE calendar.event (
  id serial primary key,
  group_id varchar(100) NOT NULL,
  start_date varchar(10) ,
  start_time varchar(10) ,
  end_date varchar(10) NOT NULL,
  end_time varchar(10) NOT NULL,
  description varchar(100) NOT NULL,
  all_day_event smallint ,
  category_id numeric(11) NOT NULL,
  action_id numeric(11) ,
  dynamic_start varchar(100)
) ;

CREATE TABLE calendar.reminder (
  id serial ,
  reminder_id numeric(11)  NOT NULL,
  value numeric(11) NOT NULL,
  unit_id numeric(11) NOT NULL,
  PRIMARY KEY (id,reminder_id)
) ;

drop view calendar.event_view;
 create view calendar.event_view as
select
    e.group_id AS group_id,
    case when e.dynamic_start is not null then de.start_date else e.start_date end   as   start_date,
    case when e.dynamic_start is not null then de.start_time else e.start_time end   as  start_time,
    e.end_date AS end_date,
    e.end_time AS end_time,
    e.description AS description,
    e.all_day_event AS all_day_event,
    e.category_id AS category_id,
    e.action_id AS action_id,
    date_part('day', to_date( e.start_date, 'YYYY-MM-DD' )-now()) AS diff,
    (case   when date_part('day',to_date( e.start_date, 'YYYY-MM-DD' )-now())= 0 then 'today'
            when date_part('day',to_date( e.start_date, 'YYYY-MM-DD' )-now() )= 1 then 'tomorrow'
            when date_part('day',to_date( e.start_date, 'YYYY-MM-DD' )-now() )> 1 and date_part('day',to_date( e.start_date, 'YYYY-MM-DD' )-now() ) < 15 then 'upcoming'
            when date_part('day',to_date( e.start_date, 'YYYY-MM-DD' )-now() )< 0 then 'over' else 'sometime'   end ) AS occurrence
from (calendar.event e left join calendar.dynamic_event de on (e.dynamic_start = de.id)) order by to_date(e.start_date,'YYYY-MM-DD');

