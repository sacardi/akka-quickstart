package com.example;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.*;

public class Replier extends AbstractBehavior<Replier.Greeted> {

	public static final class Greeted {
		public final String from;
		public final ActorRef<Sender.Message> messageDestination;

		public Greeted(String from, ActorRef<Sender.Message> dest) {
			this.from = from;
			this.messageDestination = dest;
		}
	}

	public static Behavior<Greeted> create() {
		return Behaviors.setup(context -> new Replier(context));
	}

	private int greetingCounter = 0;

	private Replier(ActorContext<Greeted> context) {
		super(context);
	}

	// things to do when GreeterBot receives a message (with "tell")
	@Override
	public Receive<Greeted> createReceive() {
		return newReceiveBuilder().onMessage(Greeted.class, this::onGreeted).build();
	}

	private Behavior<Greeted> onGreeted(Greeted message) {
		greetingCounter++;

		getContext().getLog().info("Received message #{} from {}. Sending back recurrent message", greetingCounter,
				message.from);

		message.messageDestination
				.tell(new Sender.RecurrentMessage(getContext().getSelf().toString(), getContext().getSelf()));
		return this;

	}
}
