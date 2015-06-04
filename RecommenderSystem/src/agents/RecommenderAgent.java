package agents;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import recommendersystem.RecommenderSystem;
import utilities.Matrix;
import utilities.Pair;
import utilities.Recommendation;
import data.Item;
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

public class RecommenderAgent extends Agent {
	
	

	// Put agent initializations here
	protected void setup() {
		
		System.out.println("[a] Hello, I'm the Recommender Agent!");

		// Register the book-selling service in the yellow pages
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType("movie-recommender");
		sd.setName("JADE-movie-recommender");
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
		}
		catch (FIPAException fe) {
			fe.printStackTrace();
		}

		// Add the behaviour receiving classification from client agents
		addBehaviour(new ReceiveClassification());

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
		System.out.println("Recommender-agent "+getAID().getName()+" terminating.");
	}



	private class ReceiveClassification extends CyclicBehaviour {
		public void action() {
			MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
			ACLMessage msg = myAgent.receive(mt);
			if (msg != null) {
				// Inform Message received. Process it
				Rating rating= null;
				try {
					rating = (Rating) msg.getContentObject();
				} catch (UnreadableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				System.out.println("Received from " + msg.getSender().getName() + ":");
				System.out.println(rating.toString());
				
				// TODO update matrix
				
				addBehaviour(new SearchClientAgents(myAgent, 1000, rating));

			}
			else {
				block();
			}
		}
	}  
	
	
	private class CalculateRecommendations extends OneShotBehaviour
	{

		AID clientAgent;
		Rating rating;

		public CalculateRecommendations(AID clientAgent, Rating rating)
		{
			this.clientAgent= clientAgent;
			this.rating= rating;
		}

		@Override
		public void action() {
			
			//TODO calculate recommendations
			
			Double [][]matrix = Matrix.getRatingsMatrix(10, 20, 100);

			Double[][] binaryItemTaxonomyMatrix = Matrix.getItemTaxonomyMatrix(20, 19);

			Double[] predictionsMatrix= RecommenderSystem.test5(rating.getUser().getId() - 1, matrix, binaryItemTaxonomyMatrix);

			ArrayList<Item> recommendationsList= topNRecommendations(2, predictionsMatrix);
			
			Recommendation recommendation = new Recommendation(recommendationsList);


			addBehaviour(new InformNewRecommendations(clientAgent, recommendation));

		}

		public ArrayList<Item> topNRecommendations(int n, Double[] predictions)
		{
			ArrayList<Pair<Integer,Double>> pairIndexValuelist= new ArrayList<Pair<Integer, Double>>();

			for(int i = 0; i < predictions.length; i++)
			{
				pairIndexValuelist.add(new Pair<Integer, Double>(i, predictions[i]));

			}

			Collections.sort(pairIndexValuelist, new Comparator<Pair<Integer, Double>>() {
				@Override
				public int compare(Pair<Integer, Double> o1, Pair<Integer, Double> o2) {
					if (o1.getSecond() == null && o2.getSecond() == null) {
						return 0;
					}
					if (o1.getSecond() == null) {
						return 1;
					}
					if (o2.getSecond() == null) {
						return -1;
					}
					return o1.getSecond().compareTo(o2.getSecond()) * (-1);
				}
			});

			ArrayList<Item> topNElements= new ArrayList<Item>();

			int counter= 0;


			for(int i = 0; i < pairIndexValuelist.size() && counter < n; i++)
			{
				topNElements.add(RecommenderSystem.items.get(pairIndexValuelist.get(i).getFirst()));
				counter++;
			}

			return topNElements;
		}

	}
	
	// Add a TickerBehaviour that search for client agents every minute
	private class SearchClientAgents extends TickerBehaviour {
		
		AID[] clientAgents;
		Rating rating;

		public SearchClientAgents(Agent a, long period, Rating rating) {
			super(a, period);
			this.rating= rating;
			onTick();
		}

		protected void onTick() {
			// Update the list of recommender agents
			DFAgentDescription dfd = new DFAgentDescription();
			ServiceDescription sd = new ServiceDescription();
			sd.setType("clientAgent");
			dfd.addServices(sd);
			try {
				DFAgentDescription[] result = DFService.search(myAgent, dfd); 
				System.out.println("Found the following client agents:");
				clientAgents = new AID[result.length];
				for (int i = 0; i < result.length; ++i) {
					clientAgents[i] = result[i].getName();
					System.out.println(clientAgents[i].getName());
					
					// Inform client Agent with new recommendations
					myAgent.addBehaviour(new CalculateRecommendations(clientAgents[i], this.rating));
					
					
				}
			}
			catch (FIPAException fe) {
				fe.printStackTrace();
			}

			if(clientAgents.length > 0)
			{
				stop();
			}


		}

	}
	
	private class InformNewRecommendations extends OneShotBehaviour
	{

		AID clientAgent;
		Recommendation recommendation;

		public InformNewRecommendations(AID clientAgent, Recommendation recommendation)
		{
			this.clientAgent= clientAgent;
			this.recommendation= recommendation;
		}

		@Override
		public void action() {
			
			//TODO calculate recommendations for this agent specifically

			ACLMessage newRecommendationsInform = new ACLMessage(ACLMessage.INFORM);
			
			newRecommendationsInform.addReceiver(this.clientAgent);
			
			newRecommendationsInform.setConversationId("newRecommendation-info");
			
			
			try {
				newRecommendationsInform.setContentObject(this.recommendation);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			myAgent.send(newRecommendationsInform);

		}
		
		

	}


}
