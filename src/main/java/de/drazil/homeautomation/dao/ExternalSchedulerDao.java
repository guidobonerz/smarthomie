package de.drazil.homeautomation.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import de.drazil.homeautomation.dto.DynamicEvent;
import de.drazil.homeautomation.dto.Event;

@Repository
public class ExternalSchedulerDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private NamedParameterJdbcTemplate namedJdbcTemplate;

	public boolean checkGroup(String groupId) {
		return jdbcTemplate.queryForObject("select count(*) from calendar.event where group_id=?",
				new Object[] { groupId }, Integer.class) > 0;
	}

	public void removeEventByCalenderName(String calenderName) {
		jdbcTemplate.update("delete from calendar.event where group_id=?", calenderName);
	}

	public void addEvent(String groupdId, String start_rule, String end_rule, String description, boolean allDayEvent,
			int categoryId, int actionId) {
		jdbcTemplate.update(
				"INSERT INTO calendar.event(group_id,start_rule,end_rule,description,all_day_event,category_id,action_id) values (?,?,?,?,?,?,?)",
				groupdId, start_rule, end_rule, description, allDayEvent, categoryId, actionId);

	}

	public void addOrUpdateDynamicEvent(String id, String target_date) {
		int result = jdbcTemplate.queryForObject("select count(*) from calendar.dynamic_event where id=?",
				new Object[] { id }, Integer.class);
		if (result == 0) {
			jdbcTemplate.update("insert into calendar.dynamic_event(id,target_date) values (?,?)", id, target_date);
		} else {
			jdbcTemplate.update("update calendar.dynamic_event set target_date=? where id=?", target_date, id);
		}
	}

	public DynamicEvent getDynamicEventById(String id) {
		DynamicEvent dynamicEvent = null;
		try {
			dynamicEvent = jdbcTemplate.queryForObject("select * from  calendar.dynamic_event where id=?",
					new RowMapper<DynamicEvent>() {
						@Override
						public DynamicEvent mapRow(ResultSet rs, int rowNum) throws SQLException {
							DynamicEvent de = new DynamicEvent();
							de.setId(rs.getString("id"));
							de.setTargetDate(rs.getString("target_date"));
							return de;
						}

					}, new Object[] { id });
		} catch (EmptyResultDataAccessException e) {
		}
		return dynamicEvent;

	}

	public List<Event> getEventList() {
		List<Event> list = jdbcTemplate.query("select * from calendar.event", (Object[]) null, new RowMapper<Event>() {
			@Override
			public Event mapRow(ResultSet rs, int rowNum) throws SQLException {
				Event event = new Event();
				event.setGroupId(rs.getString("group_id"));
				event.setStartRule(rs.getString("start_rule"));
				event.setEndRule(rs.getString("end_rule"));
				event.setDescription(rs.getString("description"));
				event.setActionId(rs.getLong("action_id"));
				event.setCategoryId(rs.getLong("category_id"));
				event.setAllDayEvent(rs.getBoolean("all_day_event"));
				//event.setDiff(rs.getLong("diff"));
				//event.setOccurrence(rs.getString("occurrence"));
				return event;
			}
		});
		return list;
	}

	public List<Event> getUpcomingEventList(int from, int to) {

		List<Event> list = jdbcTemplate.query("select * from calendar.event_view where diff between ? and ?",
				new Object[] { from, to }, new RowMapper<Event>() {
					@Override
					public Event mapRow(ResultSet rs, int rowNum) throws SQLException {
						Event event = new Event();
						event.setStartRule(rs.getString("start_rule"));
						event.setStartRule(rs.getString("start_rule"));
						event.setDescription(rs.getString("description"));
						event.setActionId(rs.getLong("action_command"));
						event.setCategoryId(rs.getLong("category_id"));
						event.setAllDayEvent(rs.getBoolean("all_day_event"));
						event.setDiff(rs.getLong("diff"));
						event.setOccurrence(rs.getString("occurrence"));
						return event;
					}
				});
		return list;
	}

	public List<Event> getUpcomingEventList(String events[]) {

		List<String> eventList = Arrays.asList(events);
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("occurrences", eventList);
		List<Event> list = namedJdbcTemplate.query(
				"select * from calendar.event_view where occurrence in (:occurrences)", parameters,
				new RowMapper<Event>() {
					@Override
					public Event mapRow(ResultSet rs, int rowNum) throws SQLException {
						Event event = new Event();
						event.setStartRule(rs.getString("start_rule"));
						event.setStartRule(rs.getString("start_rule"));
						event.setDescription(rs.getString("description"));
						event.setActionId(rs.getLong("action_command"));
						event.setCategoryId(rs.getLong("category_id"));
						event.setAllDayEvent(rs.getBoolean("all_day_event"));
						event.setDiff(rs.getLong("diff"));
						event.setOccurrence(rs.getString("occurrence"));
						return event;
					}
				});
		return list;
	}

	public List<Event> getUpcomingEventActionList(String events[]) {

		List<String> eventList = Arrays.asList(events);
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("occurrences", eventList);
		List<Event> list = namedJdbcTemplate.query(
				"select * from calendar.event_view where occurrence in (:occurrences) and action_command is not null",
				parameters, new RowMapper<Event>() {
					@Override
					public Event mapRow(ResultSet rs, int rowNum) throws SQLException {
						Event event = new Event();
						event.setStartRule(rs.getString("start_rule"));
						event.setStartRule(rs.getString("start_rule"));
						event.setDescription(rs.getString("description"));
						event.setActionId(rs.getLong("action_command"));
						event.setCategoryId(rs.getLong("category_id"));
						event.setAllDayEvent(rs.getBoolean("all_day_event"));
						event.setDiff(rs.getLong("diff"));
						event.setOccurrence(rs.getString("occurrence"));

						return event;
					}
				});
		return list;
	}

}
