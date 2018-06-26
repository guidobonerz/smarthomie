CREATE TABLE `action` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `command` varchar(10) DEFAULT NULL,
  `randomize` int(11) DEFAULT NULL,
  `value` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `category` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `description` varchar(45) NOT NULL,
  `color` varchar(7) DEFAULT NULL,
  `icon` varchar(45) DEFAULT NULL,
  `can_have_reminder` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=latin1;

CREATE TABLE `dynamic_event` (
  `id` varchar(100) NOT NULL,
  `start_date` varchar(10) NOT NULL,
  `start_time` varchar(10) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `event` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `group_id` varchar(100) NOT NULL,
  `start_date` varchar(10) DEFAULT NULL,
  `start_time` varchar(10) DEFAULT NULL,
  `end_date` varchar(10) NOT NULL,
  `end_time` varchar(10) NOT NULL,
  `description` varchar(100) NOT NULL,
  `all_day_event` tinyint(1) DEFAULT NULL,
  `category_id` int(11) NOT NULL,
  `action_id` int(11) DEFAULT NULL,
  `dynamic_start` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18594 DEFAULT CHARSET=utf8;

CREATE TABLE `reminder` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `reminder_id` int(11) NOT NULL,
  `value` int(11) NOT NULL,
  `unit_id` int(11) NOT NULL,
  PRIMARY KEY (`id`,`reminder_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `unit` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `description` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;

create view `calender`.`event_view` as
select
    `e`.`group_id` AS `group_id`,
    if((`e`.`dynamic_start` is not null),convert(`de`.`start_date` using utf8),`e`.`start_date`) AS `start_date`,
    if((`e`.`dynamic_start` is not null),convert(`de`.`start_time` using utf8),`e`.`start_time`) AS `start_time`,
    `e`.`end_date` AS `end_date`,
    `e`.`end_time` AS `end_time`,
    `e`.`description` AS `description`,
    `e`.`all_day_event` AS `all_day_event`,
    `e`.`category_id` AS `category_id`,
    `e`.`action_id` AS `action_id`,
    (to_days( str_to_date( `e`.`start_date`, '%Y-%m-%d' ))- to_days( curdate())) AS `diff`,
    (case   when((to_days( str_to_date( `e`.`start_date`, '%Y-%m-%d' ))- to_days( curdate()))= 0) then 'today'
            when((to_days( str_to_date( `e`.`start_date`, '%Y-%m-%d' ))- to_days( curdate()))= 1) then 'tomorrow'
            when(((to_days( str_to_date( `e`.`start_date`, '%Y-%m-%d' ))- to_days( curdate()))> 1)and((to_days( str_to_date( `e`.`start_date`, '%Y-%m-%d' ))- to_days( curdate()))< 15)) then 'upcoming'
            when((to_days( str_to_date( `e`.`start_date`, '%Y-%m-%d' ))- to_days( curdate()))< 0) then 'over' else 'sometime' end ) AS `when`
from (`calender`.`event` `e` left join `calender`.`dynamic_event` `de` on ((`e`.`dynamic_start` = convert(`de`.`id` using utf8)))) order by str_to_date(`e`.`start_date`,'%Y-%m-%d');


