//In this class we are going to manage everything about the ballot using LinkedList as our main Data Structure - Ademir
package ElectionsManagement;
public class Ballot {
	//List of candidates -Ademir
	public LinkedList<String> candidatesList;
	/**
	 * Mandatory Method that retrieves a candidate
	 * @param rank
	 * @return candidate (with that specific rank)
	 */
	 	public int getCandidateByRank(int rank) {
		for (String candidate: candidatesList) {
			String[] ballotInfo = candidate.split(":"); //Separate by (:) -Ademir
			if (Integer.valueOf(ballotInfo[1]) == (rank)) { 
				return Integer.valueOf(ballotInfo[0]); 
			}
		}
		return 0; //Dummy Return "Quotes Bienve" -Ademir
	}
	
	/**
	 * Mandatory Method that retrieves a rank
	 * @param candidateID
	 * @return rank (with that specific candidate)
	 */
	public int getRankByCandidate(int candidateID) {
		for (String candidate: candidatesList) {
			String[] ballotInfo = candidate.split(":"); //Separate by (:) -Ademir
			if (Integer.valueOf(ballotInfo[0]) == (candidateID-1)) { 
				return Integer.valueOf(ballotInfo[1]); 
			}
		}	
		return 0; //Dummy Return "Quotes Bienve" -Ademir
	}
	/**
	 * Mandatory Method that retrieves(removes) lowest 1 casted votes participant
	 * @param candidateID
	 * @return true (remove candidate)
	 */
	public boolean eliminate(int candidateID) {
		for (int i = 0; i < candidatesList.size(); i++) {
			String[] ballotInfo = candidatesList.get(i).split(":"); //Separate by (:) -Ademir
			if ((Integer.valueOf(ballotInfo[0]) == candidateID) && (Integer.valueOf(ballotInfo[1]) == 1)) { 
				candidatesList.remove(i); 
				// Adjusting remaining ballots -Ademir
				for (int j = 0; j < candidatesList.size(); j++) {
					String[] ballotValues = candidatesList.get(j).split(":"); 				
					ballotValues[1] = String.valueOf(Integer.valueOf(ballotValues[1]) - 1);
					candidatesList.set(j, String.join(":", ballotValues));
				}
				return true; 
			}
		}
		return false; // No elimination to be currently made -Ademir
	}
	//Mandatory variables -Ademir
	public int ballotID;
	public int getBallotNum() {
	return ballotID;
	}
	
	/** In the following method we are looking for every comma(,) to separate the strings inside the ballot
	 * First comma indicates ballotID
	 * Other commas indicate different candidates with their ranking
	 * Find desired values (strings) to manage the info inside the ballot
	 * @param list
	 * The structure of this method will be that of a List.
	 */
	public Ballot (String list) {
		String[] ballotInfo = list.split(","); // Using commas (,) to separate the candidates -Ademir
		this.ballotID = Integer.valueOf(ballotInfo[0]); //The first position in the string should be the Ballot ID -Ademir
		candidatesList = new LinkedList<String>();
		for (int i = 1; i < ballotInfo.length; i++) { // We are verifying all the casted votes in the Ballot - Ademir
			candidatesList.add(ballotInfo[i]); 
		}
	}		
}		