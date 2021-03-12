package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.RenameMeDTO;
import utils.EMF_Creator;
import facades.FacadeExample;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

//Todo Remove or change relevant parts before ACTUAL use
@OpenAPIDefinition(
            info = @Info(
                    title = "Simple Movie API",
                    version = "0.4",
                    description = "Simple API to get info about movies.",        
                    contact = @Contact( name = "Lars Mortensen", email = "lam@cphbusiness.dk")
            ),
          tags = {
                    @Tag(name = "movie", description = "API related to Movie Info")
              
            },
            servers = {
                    @Server(
                            description = "For Local host testing",
                            url = "http://localhost:8080/startcodeoas"
                    ),
                    @Server(
                            description = "Server API",
                            url = "http://mydroplet"
                    )
                          
            }
)
@Path("xxx")
public class RenameMeResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();

    private static final FacadeExample FACADE = FacadeExample.getFacadeExample(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String demo() {
        return "{\"msg\":\"Hello World\"}";
    }

    @Path("count")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getRenameMeCount() {
        long count = FACADE.getRenameMeCount();
        //System.out.println("--------------->"+count);
        return "{\"count\":" + count + "}";  //Done manually so no need for a DTO
    }

    @Operation(summary = "Get Movie info by ID",
            tags = {"movie"},
            responses = {
                     @ApiResponse(
                     content = @Content(mediaType = "application/json",schema = @Schema(implementation = RenameMeDTO.class))),
                    @ApiResponse(responseCode = "200", description = "The Requested Movie"),                       
                    @ApiResponse(responseCode = "400", description = "Movie not found")})
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public RenameMeDTO getMovieInfo(@PathParam("id") int id) {
        return new RenameMeDTO("First arg", "Second arg");
    }

}
