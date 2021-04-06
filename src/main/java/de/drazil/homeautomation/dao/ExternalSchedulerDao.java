package de.drazil.homeautomation.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
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
import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class ExternalSchedulerDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private NamedParameterJdbcTemplate namedJdbcTemplate;

	public boolean checkGroup(final String groupId) {
		return jdbcTemplate.queryForObject("select count(*) from calendar.event where group_id=?", Integer.class,
				new Object[] { groupId }) > 0;
	}

	public void removeEventByCalenderName(final String calenderName) {
		jdbcTemplate.update("delete from calendar.event where group_id=?", calenderName);
	}

	public void addEvent(final String groupdId, final String start_rule, final String end_rule,
			final String description, final boolean allDayEvent, final int categoryId, final int actionId) {
		jdbcTemplate.update(
				"INSERT INTO calendar.event(group_id,start_rule,end_rule,description,all_day_event,category_id,action_id) values (?,?,?,?,?,?,?)",
				groupdId, start_rule, end_rule, description, allDayEvent, categoryId, actionId);

	}

	public void addOrUpdateDynamicEvent(final String id, final String target_date) {
		final int result = jdbcTemplate.queryForObject("select count(*) from calendar.dynamic_event where id=?",
				Integer.class, new Object[] { id });
		if (result == 0) {
			jdbcTemplate.update("insert into calendar.dynamic_event(id,target_date) values (?,?)", id, target_date);
		} else {
			jdbcTemplate.update("update calendar.dynamic_event set target_date=? where id=?", target_date, id);
		}
	}

	public DynamicEvent getDynamicEventById(final String id) {
		DynamicEvent dynamicEvent = null;
		try {
			dynamicEvent = jdbcTemplate.queryForObject("select * from calendar.dynamic_event where id=?",
					new RowMapper<DynamicEvent>() {
						@Override
						public DynamicEvent mapRow(final ResultSet rs, final int rowNum) throws SQLException {
							final DynamicEvent de = new DynamicEvent();
							de.setId(rs.getString("id"));
							de.setTargetDate(rs.getString("target_date"));
							return de;
						}

					}, new Object[] { id });
		} catch (final EmptyResultDataAccessException e) {
			log.debug("no data found");
		}
		return dynamicEvent;

	}

	public List<Event> getEventList() {
		final List<Event> list = jdbcTemplate.query("select * from calendar.event", new RowMapper<Event>() {
			@Override
			public Event mapRow(final ResultSet rs, final int rowNum) throws SQLException {
				final Event event = new Event();
				event.setGroupId(rs.getString("group_id"));
				event.setStartRule(rs.getString("start_rule"));
				event.setEndRule(rs.getString("end_rule"));
				event.setDescription(rs.getString("description"));
				event.setActionId(rs.getLong("action_id"));
				event.setCategoryId(rs.getLong("category_id"));
				event.setAllDayEvent(rs.getBoolean("all_day_event"));
				// event.setDiff(rs.getLong("diff"));
				// event.setOccurrence(rs.getString("occurrence"));
				return event;
			}
		});
		return list;
	}

	public List<Event> getUpcomingEventList(final int from, final int to) {

		final List<Event> list = jdbcTemplate.query("select * from calendar.event_view where diff between ? and ?",
				new Object[] { from, to }, new int[] { Types.INTEGER, Types.INTEGER }, new RowMapper<Event>() {
					@Override
					public Event mapRow(final ResultSet rs, final int rowNum) throws SQLException {
						final Event event = new Event();
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

	public List<Event> getUpcomingEventList(final String events[]) {

		final List<String> eventList = Arrays.asList(events);
		final MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("occurrences", eventList);
		final List<Event> list = namedJdbcTemplate
				.query("select * from calendar.event_view where occurrence in (:occurrences)", new RowMapper<Event>() {
					@Override
					public Event mapRow(final ResultSet rs, final int rowNum) throws SQLException {
						final Event event = new Event();
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

	public List<Event> getUpcomingEventActionList(final String events[]) {

		// final List<String> eventList = Arrays.asList(events);
		// final MapSqlParameterSource parameters = new MapSqlParameterSource();
		// parameters.addValue("occurrences", eventList);
		int[] eta = new int[events.length];
		Arrays.fill(eta, Types.VARCHAR);

		final List<Event> list = namedJdbcTemplate.query(
				"select * from calendar.event_view where occurrence in (:occurrences) and action_command is not null",
				events, eta, new RowMapper<Event>() {
					@Override
					public Event mapRow(final ResultSet rs, final int rowNum) throws SQLException {
						final Event event = new Event();
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
