package in.chill.main.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Events")
public class Events {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private int event_id;
	
	@Column
	private String event_name;
	
	@Column
	private String event_start_date;
	
	@Column
	private String event_end_date;
	
	@Column
	private String event_time;
	
	@Column
	private String event_type;
	
	@Column
	private String event_description;
	
	@Column
	private Integer judge_id;
	
	@Column
	private Integer club_id;
	
	@Column
	private Integer venue_id;
	
	// Relationships
	@ManyToOne
	@JoinColumn(name = "venue_id", insertable = false, updatable = false)
	private Venue venue;
	
	@ManyToOne
	@JoinColumn(name = "club_id", insertable = false, updatable = false)
	private Club club;
	
	@ManyToOne
	@JoinColumn(name = "judge_id", insertable = false, updatable = false)
	private Judge judge;
	
	@OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
	@JsonIgnore
	private List<Volunteer> volunteers;
	
	@OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
	@JsonIgnore
	private List<Participation> participations;
	
	// Getters and setters
	public int getEvent_id() {
		return event_id;
	}
	public void setEvent_id(int event_id) {
		this.event_id = event_id;
	}
	public String getEvent_name() {
		return event_name;
	}
	public void setEvent_name(String event_name) {
		this.event_name = event_name;
	}
	public String getEvent_start_date() {
		return event_start_date;
	}
	public void setEvent_start_date(String event_start_date) {
		this.event_start_date = event_start_date;
	}
	public String getEvent_end_date() {
		return event_end_date;
	}
	public void setEvent_end_date(String event_end_date) {
		this.event_end_date = event_end_date;
	}
	public String getEvent_time() {
		return event_time;
	}
	public void setEvent_time(String event_time) {
		this.event_time = event_time;
	}
	public String getEvent_type() {
		return event_type;
	}
	public void setEvent_type(String event_type) {
		this.event_type = event_type;
	}
	public String getEvent_description() {
		return event_description;
	}
	public void setEvent_description(String event_description) {
		this.event_description = event_description;
	}
	public Integer getJudge_id() {
		return judge_id;
	}
	public void setJudge_id(Integer judge_id) {
		this.judge_id = judge_id;
	}
	public Integer getClub_id() {
		return club_id;
	}
	public void setClub_id(Integer club_id) {
		this.club_id = club_id;
	}
	public Integer getVenue_id() {
		return venue_id;
	}
	public void setVenue_id(Integer venue_id) {
		this.venue_id = venue_id;
	}
	
	// Relationship getters and setters
	public Venue getVenue() {
		return venue;
	}
	
	public void setVenue(Venue venue) {
		this.venue = venue;
	}
	
	public Club getClub() {
		return club;
	}
	
	public void setClub(Club club) {
		this.club = club;
	}
	
	public Judge getJudge() {
		return judge;
	}
	
	public void setJudge(Judge judge) {
		this.judge = judge;
	}
	
	public List<Volunteer> getVolunteers() {
		return volunteers;
	}
	
	public void setVolunteers(List<Volunteer> volunteers) {
		this.volunteers = volunteers;
	}
	
	public List<Participation> getParticipations() {
		return participations;
	}
	
	public void setParticipations(List<Participation> participations) {
		this.participations = participations;
	}
}
