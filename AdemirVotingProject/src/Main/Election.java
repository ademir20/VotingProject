//In this class we are going to validate the ballots, remove candidates depending on casted votes and output this information in the console or text-Ademir
package Main;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import ElectionsManagement.Ballot;
import ElectionsManagement.Candidate;
import ElectionsManagement.LinkedList;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
public class Election {
	/**
	 * Function to verify the validation of the ballots
	 * @param ballotInfo checking the information inside the ballot (necessary for validation)
	 * @param totalAmountOfCandidates checking amount of participating candidates
	 */
	public static boolean validation(Ballot ballotInfo, int totalAmountOfCandidates) {
		// Check amount of votes depending on candidates -Ademir
		LinkedList<Integer> testVotes = new LinkedList<Integer>(); 
		//Checking candidates to verify amount of possible votes -Ademir
		LinkedList<Integer> testCandidates = new LinkedList<Integer>(); 
		int candidatesTotalAmount = ballotInfo.candidatesList.size();
		for (int i = 0; i < totalAmountOfCandidates; i++) {
			testCandidates.add(0);
			testVotes.add(0); 
		}
		for (String vote: ballotInfo.candidatesList) {
			String[] ballotManager = vote.split(":");
			//If casted vote is higher than permitted is invalid -Ademir
			if (Integer.valueOf(ballotManager[1]) > candidatesTotalAmount ) { 
				return false;
			}
			//If casted vote is lower than permitted is invalid -Ademir
			else if (Integer.valueOf(ballotManager[1]) <= 0 ) {
				return false;
			}
			// For candidate 1, add one count in testCandidates -Ademir
			testCandidates.set(Integer.valueOf(ballotManager[0]) - 1, testCandidates.get(Integer.valueOf(ballotManager[0]) - 1) + 1);
			// For candidate 2, add one count in testVotes -Ademir
			testVotes.set(Integer.valueOf(ballotManager[1]) - 1, testVotes.get(Integer.valueOf(ballotManager[1]) - 1) + 1);
		}
		// Now we check for votes from position 0 to k-1. Values must be one on both tests -Ademir
		for (int i = 0; i < candidatesTotalAmount; i++) {
			if (testVotes.get(i) != 1) {
				return false;
			}
		}
		int add = 0;
		// Verifying that there is only one unique candidate -Ademir
		for (int i = 0; i < totalAmountOfCandidates; i++) {
			if (testCandidates.get(i) > 1) {
				return false;
			}
			else if (testCandidates.get(i) == 1) {
				add++;
			}
		}
		// Searching if there is a repeated candidate "cheater" -Ademir
		if (add != candidatesTotalAmount) {
			return false;
		}
		return true;
	}
	/***
	 * This method works with the selection and elimination of candidates and manages the outputs in the console
	 * @param setLineList Tells us how many polls have been made
	 * @param candidatesList List of participating candidates
	 * @param candidatesTotalAmount Number of all participating candidates
	 * @param validBallots Accepted votes
	 * @param blankBallots Ballots with no casted votes
	 * @param output What will be printed in the console/output.txt
	 */
	public static Candidate primaries(LinkedList<LinkedList<Ballot>> setLineList, LinkedList<Candidate> candidatesList, 
			int candidatesTotalAmount, int validBallots, int blankBallots, FileWriter output) throws IOException {
		//List with ranks in primaries - Ademir
		LinkedList<Integer> votes = new LinkedList<Integer>();
		//List with removed candidates -Ademir
		LinkedList<Candidate> removed = new LinkedList<Candidate>();
		//Votes are set to 0 -Ademir
		for (int i  = 0; i < candidatesTotalAmount; i++) {
			votes.add(0);
		}
		int primariesPoll = 0;
		LinkedList<Integer> draw = new LinkedList<Integer>(); // This list saves candidates with equal minimum votes -Ademirs
		while (true) {
			primariesPoll++;
			for (int castedVotes = 0; castedVotes < candidatesTotalAmount; castedVotes++) {
				// Verifies casted votes -Ademir
				for (int i = 0; i < candidatesTotalAmount; i++) {
					votes.set(i, 0);
					//Checks casted votes for non-eliminated candidates -Ademir
					if (!removed.contains(candidatesList.get(i))) {
						for (int j = 0; j < candidatesTotalAmount; j++) {
							LinkedList<Ballot> set = setLineList.get(j);
							for(Ballot ballot: set) {
								if (ballot.getCandidateByRank(castedVotes) -1 == i) {
									votes.set(i, votes.get(i) + 1);
								}
							}
						}
					}
					else {
						votes.set(i, -1);
					}
				}
				//Update the other candidates casting votes -Ademir
				if (castedVotes == 1) {
					int minimum = 70000000; // only 7 billion people in the world -Ademir
					//Check other candidates casted votes = 1 -Ademir
					for (int i = 0; i < candidatesTotalAmount; i++) {
						if (votes.get(i) != -1) {
							candidatesList.get(i).setReceivedVotes((votes.get(i)));
							//Most votes 1 candidate -Ademir
							if ((float) votes.get(i) / validBallots > (0.5)) {
								return candidatesList.get(i);
							}		
							// If casted votes < minimum, we make votes = new minimum -Ademir
							if (votes.get(i) < minimum) {
								minimum = votes.get(i);
								//Restart  any draw lists - Ademir
								draw = new LinkedList<Integer>(); 
								draw.add(i); //Search and add the candidate with less votes - Ademir
							}
							// If there is a draw find and add the candidate ID -Ademir
							else if (votes.get(i) == minimum) {
								draw.add(i);
							}
						}
					}
					//Since a draw can only occur once at a time we eliminate the one with lowest number -Ademir
					if (draw.size() == 1) {
						if (candidatesTotalAmount - 2 != removed.size()) {
							output.write("		POLL " + String.valueOf(primariesPoll) + ": " + candidatesList.get(draw.get(0)).getCandidatesName()	+ "  GOT DISQUALIFIED FROM THE ELECTIONS WITH: " + String.valueOf(candidatesList.get(draw.get(0)).getReceivedVotes()) + " RANK 1 CASTED VOTES \n\n");
						}
						//Removed the candidate -Ademir
						removedCandidate(setLineList, candidatesList.get(draw.get(0)), removed); 
						//Reset any draw lists-Ademir
						draw = new LinkedList<Integer>(); 
					}
				}
				else {
					int least = 7000000; // only 7 billion people in the world -Ademir

					//Searching all of the candidates -Ademir
					for (int i = 0; i < draw.size(); i++) {
						if (votes.get(candidatesList.get(draw.get(i)).getCandidateID()-1) != -1) {
							// If casted votes are less than minimum we make casted votes the new minimum - Ademir
							if (votes.get(candidatesList.get(draw.get(i)).getCandidateID()-1) < least) {
								least = votes.get(candidatesList.get(draw.get(i)).getCandidateID()-1);
							}
						}
					}
					//Remove candidates with votes greater than minimum -Ademir
					for (int i = 0; i < draw.size(); i++) {
						if (votes.get(candidatesList.get(draw.get(i)).getCandidateID()-1) > least) {
							draw.remove(i);
						}
					}

					// Remove draw -Ademir
					if (draw.size() == 1) {
						if (candidatesTotalAmount - 2 != removed.size()) {
							output.write("		POLL " + String.valueOf(primariesPoll) + ": " + candidatesList.get(draw.get(0)).getCandidatesName() + "  GOT DISQUALIFIED FROM THE ELECTIONS WITH: " + String.valueOf(candidatesList.get(draw.get(0)).getReceivedVotes() + " RANK  1 CASTED VOTES \n\n"));
						}
						//Removed Candidate -Ademir
						removedCandidate(setLineList, candidatesList.get(draw.get(0)), removed); 
						//Reset any draw lists -Ademir
						draw = new LinkedList<Integer>();
						break;
					}
				}
			}

			// Still a draw? Remove last element of tied which is highest ID# - Ademir
			if (draw.size() > 1) {

				if (candidatesTotalAmount - 2 != removed.size()) {
					output.write("		POLL " + String.valueOf(primariesPoll) + ": " 
							+ candidatesList.get(draw.last()).getCandidatesName() + " 	GOT DISQUALIFIED FROM THE ELECTIONS WITH: " 
							+ String.valueOf(candidatesList.get(draw.last()).getReceivedVotes() + " RANK 1 CASTED VOTES \n\n "));

				}
				//Remove the candidate -Ademir
				removedCandidate(setLineList, candidatesList.get(draw.last()), removed); 
				//Reset the draw -Ademir
				draw = new LinkedList<Integer>(); 
			}
		}		
	}
	// Method established to remove the candidates -Ademir
	@SuppressWarnings("unused")
	public static void removedCandidate(LinkedList<LinkedList<Ballot>> setLineList, Candidate toRemove, LinkedList<Candidate> removed ) {				
		for (int i = 0; i < setLineList.get(toRemove.getCandidateID() - 1).size(); i++) {
			boolean removedCandidate = setLineList.get(toRemove.getCandidateID() - 1).get(i).eliminate(toRemove.getCandidateID()); //Remove candidate from ballot 
		}
		for (int i = 0; i < setLineList.get(toRemove.getCandidateID() - 1).size(); i++) {
			for (int j = 0; j < removed.size(); j++) {
				if (setLineList.get(toRemove.getCandidateID() - 1).get(i).getCandidateByRank(1) == removed.get(j).getCandidateID()) {
					boolean removedCandidate = setLineList.get(toRemove.getCandidateID() - 1).get(i).eliminate(removed.get(j).getCandidateID()); //Remove candidate from ballot 
					i = 0;
					j = 0;
				}
			}
		}
		//Adding candidates that lost to the list -Ademir
		removed.add(toRemove);
	}
	/***
	 * Declaring main method which receives the input from the resources package and outputs the "elections"
	 * @param args
	 */
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		String list = "";
		//Search for the candidates (currently holding 16 candidates, add candidates if necessary) -Ademir
		String candidatesManager = "./src/res/candidates.csv"; 
		/*
		 * Add Ballot Name you want to use for testing with the same format as initial ballots
		 * (ballotTester holds all the individual ballots/testers together and extra ballots) you may test them individually if desired -Ademir 
		 */
		String ballotsManager = "./src/res/ballotsTester.csv"; 
		BufferedReader reader = null;
		LinkedList<Candidate> listOfCandidates = new LinkedList<Candidate>();
		//Lets start reading does files! -Ademir
		try { 

			reader = new BufferedReader(new FileReader(candidatesManager));
			while ((list = reader.readLine()) != null) {
				//Using the commas to separate the lines in different lists -Ademir
				String[] ballotInfo = list.split(","); 
				Candidate c = new Candidate(ballotInfo[0], Integer.valueOf(ballotInfo[1])); 
				listOfCandidates.add(c);
			}
		} catch (FileNotFoundException excempt) {
			excempt.printStackTrace();
		} 
		catch (IOException excempt) {
			excempt.printStackTrace();
		} 
		finally {
			if (reader != null) {
				try {
					reader.close();
				} 
				catch (IOException excempt) {
					excempt.printStackTrace();
				}
			}
		}
		//Linkedlist to set -Ademir
		int candidatesTotalAmount = listOfCandidates.size();
		LinkedList<LinkedList<Ballot>> setCandidateList = new LinkedList<LinkedList<Ballot>>();
		for (int i = 0; i < candidatesTotalAmount; i++) {
			LinkedList<Ballot> set = new LinkedList<Ballot>();
			setCandidateList.add(set);
		}
		//Returning amount and type of ballot in the output -Ademir
		int allBallots = 0;
		int validBallots = 0;
		int blankBallots = 0;

		try { //Tries to read ballots file line by line -Ademir

			reader = new BufferedReader(new FileReader(ballotsManager));
			while ((list = reader.readLine()) != null) { 
				allBallots++;
				Ballot ballot = new Ballot(list);
				if (ballot.candidatesList.size() == 0) {
					blankBallots++;
				}
				else if (validation(ballot, candidatesTotalAmount)) {
					validBallots++;
					// Here we get the right set from lineList and add the valid ballot -Ademir
					setCandidateList.get(ballot.getCandidateByRank(1) - 1).add(ballot);	
				}
			}
			//Print output values in console/output.txt -Ademir
			int invalidBallots = allBallots - (validBallots + blankBallots); //Counting invalid ballots -Ademir
			FileWriter output = new FileWriter("./src/res/output.txt"	);
			output.write("		POOR HARBOR ELECTIONS 2020 \n\n");
			output.write("		AMOUNT OF PARTICIPATING CANDIDATES: " + candidatesTotalAmount + "\n\n");
			output.write("		IMPORTANT: PARTICIPATING CANDIDATES INFO!! \n\n");
			reader = new BufferedReader(new FileReader(candidatesManager));
			while ((list = reader.readLine()) != null) {
				//Using the commas to separate the lines in different lists to print a list of candidates Name and a list of candidates ID -Ademir
				String[] ballotInfo = list.split(","); 
				Candidate c = new Candidate(ballotInfo[0], Integer.valueOf(ballotInfo[1])); 
				listOfCandidates.add(c);
				output.write("		CANDIDATE #ID: " + c.getCandidateID() + "			 		CANDIDATE NAME : " + c.getCandidatesName().toUpperCase()+ "\n");
			}
			
			output.write("\n\n		TOTAL AMOUNT OF BALLOTS: " + String.valueOf(allBallots) + "			VALID VOTES: " + String.valueOf(validBallots));
			output.write("\n\n		INVALID VOTES: " + String.valueOf(invalidBallots)+ "					BLANK BALLOTS: " + String.valueOf(blankBallots)+ "\n\n");
			// If there are no valid ballots well i don't know elections cancelled I guess -Ademir
			if (validBallots == 0) {
				output.write("		FOR THE FIRST TIME IN POOR HARBOR HISTORY...THERE WERE NO VALID BALLOTS!!! ");
				output.close();
			}
			else {
			Candidate president = primaries(setCandidateList, listOfCandidates, candidatesTotalAmount, validBallots, blankBallots, output);
			output.write("\n\n		IN THE FINAL SELECTION THE NEW CHOSEN POOR HARBOR PRESIDENT IS..! " 	+ president.getCandidatesName().toUpperCase() + "!! WINS THE ELECTIONS WITH " 	+ String.valueOf(president.getReceivedVotes()+ " RANK 1 CASTED VOTES!!!"));
			output.write("\n\n		END OF POOR HARBOR ELECTIONS - THANKS FOR PARTICIPATING! \n\n ");
			output.close();
			}
		} catch (IOException excempt) {
			excempt.printStackTrace();
		}
	}
}