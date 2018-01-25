package br.com.caprica.showcase.ws.service;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/heroes")
public class HeroService {
	
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
	
	@GET
	@Path("{id}")
	public Response getUserById(@PathParam("id") String id) {

	   return Response.status(200).entity("getUserById is called, id : " + id).build();

	}
}

class Hero {
	private int id;
	private String name;

	public Hero(int id, String nome) {
		this.id = id;
		this.name = nome;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String nome) {
		this.name = nome;
	}
	
	@Override
	public String toString() {
		return this.getId() + " - " + this.getName();
	}

}
