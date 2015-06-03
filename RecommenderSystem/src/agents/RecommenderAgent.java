package agents;

import recommendersystem.RecommenderSystem;
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
				
				ACLMessage reply = msg.createReply();
				
				addBehaviour(new CalculateRecommendations(rating, reply));

			}
			else {
				block();
			}
		}
	}  
	
	
	private class CalculateRecommendations extends OneShotBehaviour
	{

		Rating rating;
		ACLMessage reply;

		public CalculateRecommendations(Rating rating, ACLMessage reply)
		{
			this.rating= rating;
			this.reply= reply;
		}

		@Override
		public void action() {
			
			//TODO calculate recommendations

			addBehaviour(new SearchClientAgents(myAgent, 1000));

		}

	}
	
	// Add a TickerBehaviour that search for client agents every minute
	private class SearchClientAgents extends TickerBehaviour {
		
		AID[] clientAgents;

		public SearchClientAgents(Agent a, long period) {
			super(a, period);
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
					myAgent.addBehaviour(new InformNewRecommendations(clientAgents[i]));
					
					
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

		public InformNewRecommendations(AID clientAgent)
		{
			this.clientAgent= clientAgent;
		}

		@Override
		public void action() {
			
			//TODO calculate recommendations for this agent specifically

			ACLMessage newRecommendationsInform = new ACLMessage(ACLMessage.INFORM);
			
			newRecommendationsInform.addReceiver(this.clientAgent);
			
			newRecommendationsInform.setConversationId("newRecommendation-info");
			
			String relpyMsg= "I got your message!";
			
			newRecommendationsInform.setContent(relpyMsg);
			
			myAgent.send(newRecommendationsInform);

		}

	}


}
