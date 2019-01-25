package de.drazil.homeautomation.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import de.drazil.homeautomation.dto.Event;

@Repository
public class ExternalSchedulerDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private NamedParameterJdbcTemplate namedJdbcTemplate;
	

	public void removeEventByCalenderName(String calenderName) {
		jdbcTemplate.update("delete from calendar.event where group_id=?", calenderName);
	}

	public void addEvent(String groupdId, String start_date, String start_time, String end_date, String end_time,
			String description, boolean allDayEvent, int categoryId, int actionId) {
		jdbcTemplate.update(
				"INSERT INTO calendar.event(group_id,start_date,start_time,end_date,end_time,description,all_day_event,category_id,action_id) values (?,?,?,?,?,?,?,?,?)",
				groupdId, start_date, start_time, end_date, end_time, description, allDayEvent, categoryId, actionId);

	}

	public void addOrUpdateDynamicEvent(String id, String start_date, String start_time) {
		int result = jdbcTemplate.queryForObject("select count(*) from calendar.dynamic_event where id=?", new Object[] { id },
				Integer.class);
		if (result == 0) {
			jdbcTemplate.update("insert into calendar.dynamic_event(id,start_date,start_time) values (?,?,?)", id, start_date,
					start_time);
		} else {
			jdbcTemplate.update("update calendar.dynamic_event set start_date=?, start_time=? where id=?", start_date,
					start_time, id);
		}
	}

	public List<Event> getUpcomingEventList(int from, int to) {

		List<Event> list = jdbcTemplate.query("select * from calendar.event_view where diff between ? and ?",
				new Object[] { from, to }, new RowMapper<Event>() {
					@Override
					public Event mapRow(ResultSet rs, int rowNum) throws SQLException {
						Event event = new Event();
						event.setGroupId(rs.getString("group_id"));
						event.setStartDate(rs.getString("start_date"));
						event.setStartTime(rs.getString("start_time"));
						event.setEndDate(rs.getString("end_date"));
						event.setEndTime(rs.getString("end_time"));
						event.setDescription(rs.getString("description"));
						event.setActionId(rs.getLong("action_id"));
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
		List<Event> list = namedJdbcTemplate.query("select * from calendar.event_view where occurrence in (:occurrences)", parameters,
				new RowMapper<Event>() {
					@Override
					public Event mapRow(ResultSet rs, int rowNum) throws SQLException {
						Event event = new Event();
						event.setGroupId(rs.getString("group_id"));
						event.setStartDate(rs.getString("start_date"));
						event.setStartTime(rs.getString("start_time"));
						event.setEndDate(rs.getString("end_date"));
						event.setEndTime(rs.getString("end_time"));
						event.setDescription(rs.getString("description"));
						event.setActionId(rs.getLong("action_id"));
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
