fi# angular-tour-of-heroes
A simple modification on the Angular Tour of Heroes tutorial, in order to have it access a Java Rest service

### Intro

I started studying Angular more seriously a couple weeks ago. I finished the basic tutorial, "Tour of Heroes", which is a great introduction to it. It's available here:

https://angular.io/tutorial

In the last part of the tutorial, Chapter 8 - HTTP, you have to create a service to deal with HTTP requests. In order to do so, the tutorial simulates a server, using the In-memory Web API. After I finished it, I decided to modify that step, to use a Java REST API instead.

### Angular Hero Service

The only thing you need to change in your Tour of Hero application is the address of the service. In the tutorial, you declare a heroesUrl attribute in the Hero Service (file hero.service.ts) with a link to the web api, like this:

	private heroesUrl = 'api/heroes';  // URL to web api

We are going to change that, to the local URL of the service we will create:

private heroesUrl = 'http://localhost:8080/showcase-services/rest/heroes/list';

I'm gonna explain that URL later.

### Java Environment

To create our very simple Java REST API, we will use:

	- Eclipse IDE (I'm using tOxygen.2)
	- Java 1.8
	- Tomcat 8
	- Maven (I'm using the one that comes with Eclipse)

### Create the project
	
Create a new Maven project. You can follow the steps described in one of these tutorials:

	- Eclipse: http://crunchify.com/how-to-create-dynamic-web-project-using-maven-in-eclipse/
	- Command line: https://www.mkyong.com/maven/how-to-create-a-web-application-project-with-maven/
	
To this project, we will use the standard JAX-RS API with the Jersey framework. Let's add the dependencies to our pom.xml:

	<!-- JAX-RS -->
	<dependency>
		<groupId>javax.ws.rs</groupId>
		<artifactId>javax.ws.rs-api</artifactId>
		<version>${jaxrs.version}</version>
	</dependency>

	<!-- Jersey 2.19 -->
	<dependency>
		<groupId>org.glassfish.jersey.containers</groupId>
		<artifactId>jersey-container-servlet</artifactId>
		<version>${jersey2.version}</version>
	</dependency>
	<dependency>
		<groupId>org.glassfish.jersey.core</groupId>
		<artifactId>jersey-server</artifactId>
		<version>${jersey2.version}</version>
	</dependency>
	<dependency>
		<groupId>org.glassfish.jersey.core</groupId>
		<artifactId>jersey-client</artifactId>
		<version>${jersey2.version}</version>
	</dependency>	
	<dependency>
		<groupId>org.glassfish.jersey.media</groupId>
		<artifactId>jersey-media-json-jackson</artifactId>
		<version>${jersey2.version}</version>
	</dependency>

Next, we will create a pojo called Hero:

class Hero {
	private int id;
	private String name;

	public Hero(int id, String name) {
		this.id = id;
		this.name = name;
	}
	// getters and setters
	
	@Override
	public String toString() {
		return this.getId() + " - " + this.getName();
	}
}

(This class doesn't have the "public" declaration cause it is a Nested Class, a class declared inside another one. I should have created a package "pojo", and created this class there, that's the correct way to do)

Then, let's create our service class! I'm gonna call it HeroService:

	@Path("/heroes")
	public class HeroService {
		...
	}

The annotation @Path has a similar idea to the one we used to annotate a Servlet in the showcase-jquery article. We are telling our server that calls to the URL /heroes will be treated by this service.

Now, we will need to methods: one to return the list of heroes, and another to return one Hero. In the second case, we want to pass a parameter, the id, and find the correspondent Hero in our list.

Once again, I'm not using a database for this example. Instead, I have a method to simulate a DB:

	private List<Hero> getHeros() {
		List<Hero> heroes = new ArrayList<>();
		heroes.add(new Hero(11, "Jessica Jones"));
		heroes.add(new Hero(12, "Kara Starbuck Thrace"));
		heroes.add(new Hero(13, "William Adama"));
		heroes.add(new Hero(14, "Olivia Duham"));
		heroes.add(new Hero(15, "Dana Scully"));
		heroes.add(new Hero(16, "Luke Skywalker"));
		heroes.add(new Hero(17, "Ms Marvel"));
		heroes.add(new Hero(18, "Rogue"));
		return heroes;
	}
	
Our two methods will look like this:

	@Path("/list/{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getHeroi(@PathParam("id") int id) {
		Hero theHero = new Hero(0, "Not found");
		
		for (Hero heroi : this.getHeros()) {
			if (heroi.getId() == id) {
				theHero = heroi;
			}
		}
		
		// Retornar a lista em JSON
		GenericEntity<Hero> entity = new GenericEntity<Hero>(theHero) {
		};
		return Response.ok(entity).build();
	}

	@Path("/list")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getListaHerois() {
		// Retornar a lista em JSON
		GenericEntity<List<Hero>> entity = new GenericEntity<List<Hero>>(getHeros()) {
		};
		return Response.ok(entity).build();
	}

Note that I also have the @Path annotation in the methods as well. That's because I want each of those methods to answer to a HTTP request GET. The getHeroi method receives a parameter, @PathParam("id"), which is added to the url you will use to call this method.

Now, you publish you project on Tomcat, how do you access it?

The URL you will call have two parts: one the identifies the service class, "/heroes", which is the value we used in the @Path in the class. Next, it's which method we are calling. If we try "/heroes/list", the getListaHerois() will be called. If we try "/heroes/list/11", for example, it's getHeroi(11) (in which 11 is the id).

So, in our Angular Tour of Heroes, that's why we have the URL

http://localhost:8080/showcase-services/rest/heroes/list