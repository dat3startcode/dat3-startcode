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
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;

//Todo Remove or change relevant parts before ACTUAL use
@OpenAPIDefinition(
            info = @Info(
                    title = "Simple RenameMe API",
                    version = "0.4",
                    description = "Simple API to use as start code for backend web projects.",        
                    contact = @Contact( name = "Thomas Hartmann", email = "tha@cphbusiness.dk")
            ),
            tags = {
                    @Tag(name = "renameme", description = "API base example")
              
            },
            servers = {
                    @Server(
                            description = "For Local host testing",
                            url = "http://localhost:8080/sem3openapi"
                    ),
                    @Server(
                            description = "Server API",
                            url = "https://mydropletondigitalocean.dk"
                    )
                          
            }
)
@Path("renameme")
public class RenameMeResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();

    private static final FacadeExample FACADE = FacadeExample.getFacadeExample(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();


    @Operation(summary = "Test connection",
            tags = {"renameme"})
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String demo() {
        return "{\"msg\":\"Hello World\"}";
    }


    @Operation(summary = "Get RenameMe info by ID",
            tags = {"renameme"},
            responses = {
                     @ApiResponse(
                     content = @Content(mediaType = "application/json",schema = @Schema(implementation = RenameMeDTO.class))),
                    @ApiResponse(responseCode = "200", description = "The Requested Movie"),                       
                    @ApiResponse(responseCode = "400", description = "Movie not found")})
    @GET

    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public RenameMeDTO getRenameMeById(@PathParam("id") int id) {
        return new RenameMeDTO(id, "First arg", "Second arg");
    }
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get RenameMe info by ID",
            tags = {"renameme"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "The Newly created Movie"),                       
                    @ApiResponse(responseCode = "400", description = "Not all arguments provided with the body")
            }
    )
    public RenameMeDTO createRenameMe(RenameMeDTO rm){
        if(rm.getStr1() == null || rm.getStr2() == null){
            throw new WebApplicationException("Not all required arguments included",400);
        }
        rm.setId(4);
        System.out.println(rm);
        return rm;

    }
}
