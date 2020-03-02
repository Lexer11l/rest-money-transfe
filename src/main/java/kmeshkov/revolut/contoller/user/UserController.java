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
    Response checkUserBalance(Long id);
    Response getAllUserAccounts(Long id);
}
