package agents;

import java.io.IOException;

import data.Rating;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class ClientAgent extends Agent {
	
	// The list of known recommender agents
	private AID[] recommenderAgents;


	// Put agent initializations here
	protected void setup() {

		// Printout a welcome message
		System.out.println("Hello! Client Agent "+getAID().getName()+" is ready.");

		// Register the movie recommender system client service in the yellow pages
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType("clientAgent");
		sd.setName("JADE-clientAgent");
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
		}
		catch (FIPAException fe) {
			fe.printStackTrace();
		}

		//TODO When active user have rated an item call this 
		
		Rating myRating= new Rating(1,2,3,"1234556");
		
		addBehaviour(new SearchRecommenderAgents(this, 1000, myRating));
		
		// always ready to receive updated recommendations
		addBehaviour(new ReceiveRecommendations());

	}

	// Put agent clean-up operations here
	protected void takeDown() {
		// Deregister from the yellow pages
		try {
			DFService.deregister(this);
		}
		catch (FIPAException fe) {
			fe.printStackTrace();
		}

		// Printout a dismissal message
		System.out.println("Client-agent "+getAID().getName()+" terminating.");
	}

	// Add a TickerBehaviour that search for recommender agents every minute
	private class SearchRecommenderAgents extends TickerBehaviour {
		
		Rating rating;

		public SearchRecommenderAgents(Agent a, long period, Rating rating) {
			super(a, period);
			this.rating= rating;
			onTick();
		}
		
		protected void onTick() {
			// Update the list of recommender agents
			DFAgentDescription dfd = new DFAgentDescription();
			ServiceDescription sd = new ServiceDescription();
			sd.setType("movie-recommender");
			dfd.addServices(sd);
			try {
				DFAgentDescription[] result = DFService.search(myAgent, dfd); 
				System.out.println("Found the following recommender agents:");
				recommenderAgents = new AID[result.length];
				for (int i = 0; i < result.length; ++i) {
					recommenderAgents[i] = result[i].getName();
					System.out.println(recommenderAgents[i].getName());
				}
			}
			catch (FIPAException fe) {
				fe.printStackTrace();
			}
			
			if(recommenderAgents.length > 0)
			{
				// Perform the request
				myAgent.addBehaviour(new RatingInform(this.rating));
				
				stop();
			}

			
		}
		
	}

	private class RatingInform extends OneShotBehaviour {
		
		Rating rating;
		
		public RatingInform(Rating rating)
		{
			this.rating= rating;
		}

		@Override
		public void action() {
			// Send the inform rating to all RecommederAgents
			ACLMessage ratingInform = new ACLMessage(ACLMessage.INFORM);
			for (int i = 0; i < recommenderAgents.length; ++i) {
				ratingInform.addReceiver(recommenderAgents[i]);
			} 
			
			try {
				ratingInform.setContentObject(this.rating);
			} catch (IOException e) {
				e.printStackTrace();
			}
			ratingInform.setConversationId("newRating-info");
			myAgent.send(ratingInform);
			
		}
		
	}  
	
	private class ReceiveRecommendations extends CyclicBehaviour {
		public void action() {
			MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
			ACLMessage msg = myAgent.receive(mt);
			if (msg != null) {
				// Inform Message received. Process it
				String recommendation = null;
				
				recommendation = msg.getContent();
				
				System.out.println("Received from " + msg.getSender().getName() + ":");
				System.out.println(recommendation);
				
			}
			else {
				block();
			}
		}
	}  
	

}
