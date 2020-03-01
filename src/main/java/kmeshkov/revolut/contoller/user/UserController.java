package kmeshkov.revolut.contoller.user;

import kmeshkov.revolut.model.user.User;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public interface UserController {
    Response createUser(User entity);
    Response getUserById(Long id);
    Response updateUser(User entity);
    Response deactivateUser(Long id);

    @GET
    @Path("/{id}/balance")
    @Produces(MediaType.APPLICATION_JSON)
    //TODO Implement authentication to show balance only for owner or other authorized person
    Response checkUserBalance(Long id);

    @GET
    @Path("/user/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    Response getAllUserAccounts(@PathParam("id") Long id);
}
