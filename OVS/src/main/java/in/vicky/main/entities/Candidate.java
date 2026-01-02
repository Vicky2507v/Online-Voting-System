package in.vicky.main.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table
public class Candidate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String candidateName;
    private String partyName;
    private String photoPath;
    private int voteCount = 0;
    
    
	public String getCandidateName() {
		return candidateName;
	}
	
	public void setCandidateName(String candidateName) {
		this.candidateName = candidateName;
	}
	
	public String getPartyName() {
		return partyName;
	}
	
	public void setPartyName(String partyName) {
		this.partyName = partyName;
	}
	
	public String getPhotoPath() {
		return photoPath;
	}
	
	public void setPhotoPath(String photoPath) {
		this.photoPath = photoPath;
	}
    
	public Long getId() {
	    return id;
	}

	public int getVoteCount() {
	    return voteCount;
	}

	public void setVoteCount(int voteCount) {
	    this.voteCount = voteCount;
	}

	public void setId(Long id) {
		this.id = id;
	}
    
    
    
}