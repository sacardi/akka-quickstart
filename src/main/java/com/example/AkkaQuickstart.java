package com.example;

import akka.actor.typed.ActorSystem;

public class AkkaQuickstart {
	public static void main(String[] args) {

		// create actor system
		final ActorSystem<GreeterMain.SayHello> greeterMain = ActorSystem.create(GreeterMain.create(), "ActorSystem");

		// send message to GreeterBot Mat
		String targetName = "Mat";
		String botName = "FirstBot";
		greeterMain.tell(new GreeterMain.SayHello(targetName, botName));

		// send message to GreeterBot Rob
		//greeterMain.tell(new GreeterMain.SayHello("Rob", "SecondBot"));

		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			greeterMain.terminate();
		}

	}
}
