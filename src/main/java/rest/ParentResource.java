package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.ParentDTO;
import errorhandling.EntityNotFoundException;
import datafacades.IDataFacade;
import businessfacades.ParentDTOFacade;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

//Todo Remove or change relevant parts before ACTUAL use
@Path("parent")
public class ParentResource {
       
    private static final IDataFacade<ParentDTO> FACADE =  ParentDTOFacade.getFacade();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
            
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAll() {
        return Response.ok().entity(GSON.toJson(FACADE.getAll())).build();
    }

    @GET
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getById(@PathParam("id") int id) throws EntityNotFoundException {
        ParentDTO p = FACADE.getById(id);
        return Response.ok().entity(GSON.toJson(p)).build();
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response create(String content) {
        ParentDTO pdto = GSON.fromJson(content, ParentDTO.class);
        ParentDTO newPdto = FACADE.create(pdto);
        return Response.ok().entity(GSON.toJson(newPdto)).build();
    }

    @PUT
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response update(@PathParam("id") int id, String content) throws EntityNotFoundException {
        ParentDTO pdto = GSON.fromJson(content, ParentDTO.class);
        pdto.setId(id);
        ParentDTO updated = FACADE.update(pdto);
        return Response.ok().entity(GSON.toJson(updated)).build();
    }

    @DELETE
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response delete(@PathParam("id") int id) throws EntityNotFoundException {
        ParentDTO deleted = FACADE.delete(id);
        return Response.ok().entity(GSON.toJson(deleted)).build();
    }
}
