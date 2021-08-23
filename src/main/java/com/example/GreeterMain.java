package com.example;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.*;

public class GreeterMain extends AbstractBehavior<GreeterMain.SayHello> {

	public static class SayHello {
		public final String targetName;
		public final String botName;

		public SayHello(String targetName, String botName) {
			this.targetName = targetName;
			this.botName = botName;
		}
	}

	private final ActorRef<Sender.Message> sender;
	private final ActorRef<Replier.Greeted> replier;

	public static Behavior<SayHello> create() {
		return Behaviors.setup(GreeterMain::new);
	}

	private GreeterMain(ActorContext<SayHello> context) {
		super(context);
		// #create-actors
		sender = context.spawn(Sender.create(), "sender");
		replier = context.spawn(Replier.create(), "replier");
	}

	// things to do when GreeterMain receives a message (with "tell")
	@Override
	public Receive<SayHello> createReceive() {
		return newReceiveBuilder().onMessage(SayHello.class, this::onSayHello).build();
	}

	private Behavior<SayHello> onSayHello(SayHello command) {
		getContext().getLog().info("Starting session for {}", command.targetName);
		sender.tell(new Sender.Start(getContext().getSelf().toString(), replier));
		return this;
	}
}
