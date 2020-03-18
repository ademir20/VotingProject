//This class works with the candidate...pretty straightforward -Ademir
package ElectionsManagement;
public class Candidate {

	public int receivedVotes;
	
	public int candidateID;
	
	public String candidatesName;
	
	public Candidate( String candidatesName, int candidateID) {
		
		this.receivedVotes = 0;
		
		this.candidatesName = candidatesName;
		
		this.candidateID = candidateID;
	
	}
	public int getReceivedVotes() {
		return receivedVotes;
	}

	public void setReceivedVotes(int receivedVotes) {
		this.receivedVotes = receivedVotes;
	}
	
	public void addReceivedVotes() {
		this.receivedVotes++;
	}
	

	public String getCandidatesName() {
		return candidatesName;
	}

	public void setName(String name) {
		this.candidatesName = name;
	}
	
	public int getCandidateID() {
		return candidateID;
	}

	public void setCandidateID(int candidatID) {
		this.candidateID = candidatID;
	}
}

