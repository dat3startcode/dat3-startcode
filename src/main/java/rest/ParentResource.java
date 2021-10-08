package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.ParentDTO;
import dtos.RenameMeDTO;
import entities.Parent;
import errorhandling.EntityNotFoundException;
import facades.FacadeExample;
import facades.IDataFacade;
import facades.ParentFacade;
import utils.EMF_Creator;

import javax.persistence.EntityManagerFactory;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

//Todo Remove or change relevant parts before ACTUAL use
@Path("parent")
public class ParentResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
       
    private static final IDataFacade<Parent> FACADE =  ParentFacade.getParentFacade(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
            
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response demo() {
        return Response.ok().entity(GSON.toJson(ParentDTO.toList(FACADE.getAll()))).build();
    }

    @GET
    @Path("{id}")
    public Response getById(@PathParam("id") int id) throws EntityNotFoundException {
        Parent p = FACADE.getById(id);
        return Response.ok().entity(new ParentDTO(p)).build();
    }
}
