package com.example;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.*;

public class Sender extends AbstractBehavior<Sender.Message> {

	public interface Message {
	}

	public static final class Start implements Message {
		public String from;
		public final ActorRef<Replier.Greeted> messageDestination;

		public Start(String from, ActorRef<Replier.Greeted> dest) {
			this.from = from;
			this.messageDestination = dest;
		}
	}

	public static final class RecurrentMessage implements Message {
		public final String from;
		public final ActorRef<Replier.Greeted> messageDestination;

		public RecurrentMessage(String from, ActorRef<Replier.Greeted> replyTo) {
			this.from = from;
			this.messageDestination = replyTo;
		}
	}

	public static Behavior<Message> create() {
		return Behaviors.setup(Sender::new);
	}

	private Sender(ActorContext<Message> context) {
		super(context);
	}

	// things to do when Greeter receives a message (with "tell")
	@Override
	public Receive<Message> createReceive() {
		return newReceiveBuilder().onMessage(Start.class, this::onStart)
				.onMessage(RecurrentMessage.class, this::onRecurrentMessage).build();
	}

	private Behavior<Message> onStart(Start message) {
		getContext().getLog().info("Received Start command from {}, sending it to {}", message.from,
				message.messageDestination.toString());
		message.messageDestination.tell(new Replier.Greeted(getContext().getSelf().toString(), getContext().getSelf()));
		return this;
	}

	private Behavior<Message> onRecurrentMessage(RecurrentMessage command) {
		getContext().getLog().info("Received recurrent message from {}", command.from);

		command.messageDestination.tell(new Replier.Greeted(getContext().getSelf().toString(), getContext().getSelf()));

		return this;
	}
}
